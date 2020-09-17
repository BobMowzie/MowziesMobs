package com.ilexiconn.llibrary.server.entity;

import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.server.capability.EntityDataHandler;
import com.ilexiconn.llibrary.server.capability.IEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author TheCyberBrick
 * @since 1.0.0
 */
public enum EntityPropertiesHandler {
    INSTANCE;

    private Map<Class<? extends EntityProperties>, String> propertiesIDMap = new HashMap<>();
    private Map<Class<? extends Entity>, List<Class<? extends EntityProperties<?>>>> registeredProperties = new HashMap<>();
    private Map<Class<? extends Entity>, List<String>> entityPropertiesCache = new HashMap<>();
    private Map<EntityPlayerMP, List<PropertiesTracker<?>>> trackerMap = new IdentityHashMap<>();

    /**
     * Register a new properties class.
     *
     * @param propertiesClass the properties class
     * @param <E> the entity type
     * @param <T> the properties type
     */
    public <E extends Entity, T extends EntityProperties<E>> void registerProperties(Class<T> propertiesClass) {
        T properties;
        try {
            Constructor<T> constructor = propertiesClass.getConstructor();
            properties = constructor.newInstance();
        } catch (Exception e) {
            LLibrary.LOGGER.fatal("Failed to register entity properties", e);
            return;
        }
        if (this.propertiesIDMap.containsValue(properties.getID())) {
            LLibrary.LOGGER.fatal("Duplicate entity properties with ID {}", properties.getID());
            return;
        }
        this.propertiesIDMap.put(propertiesClass, properties.getID());
        Class<E> entityClass = properties.getEntityClass();
        List<Class<? extends EntityProperties<?>>> list = this.registeredProperties.computeIfAbsent(entityClass, k -> new ArrayList<>());
        list.add(propertiesClass);
    }

    /**
     * Get the properties from a specific type for an entity.
     *
     * @param entity the entity instance
     * @param propertiesClass the properties class
     * @param <T> the properties type
     * @return the entity properties, null if they don't exist
     */
    public <T extends EntityProperties<?>> T getProperties(Entity entity, Class<T> propertiesClass) {
        if (entity != null) {
            String identifier = this.propertiesIDMap.get(propertiesClass);
            if (identifier != null) {
                return (T) EntityDataHandler.INSTANCE.getEntityData(entity, identifier);
            }
        }
        return null;
    }

    /**
     * Adds a tracker to a player.
     *
     * @param player the player instance
     * @param entity the entity instance
     * @param <T> the entity type
     */
    public <T extends Entity> void addTracker(EntityPlayerMP player, T entity) {
        List<String> entityProperties = this.entityPropertiesCache.get(entity.getClass());
        if (entityProperties != null) {
            List<PropertiesTracker<?>> trackerList = this.getEntityTrackers(player);
            for (String propID : entityProperties) {
                IEntityData extendedProperties = EntityDataHandler.INSTANCE.getEntityData(entity, propID);
                if (extendedProperties instanceof EntityProperties) {
                    EntityProperties properties = (EntityProperties) extendedProperties;
                    if (properties.getTrackingTime() >= 0) {
                        PropertiesTracker<T> tracker = properties.createTracker(entity);
                        tracker.setReady();
                        trackerList.add(tracker);
                    }
                }
            }
        }
    }

    /**
     * Remove a tracker from a player.
     *
     * @param player the player instance
     * @param entity the entity instance
     */
    public void removeTracker(EntityPlayerMP player, Entity entity) {
        List<PropertiesTracker<?>> trackerList = this.getEntityTrackers(player);
        if (trackerList != null && !trackerList.isEmpty()) {
            Iterator<PropertiesTracker<?>> iterator = trackerList.iterator();
            while (iterator.hasNext()) {
                PropertiesTracker<?> tracker = iterator.next();
                if (tracker.getEntity().equals(entity)) {
                    iterator.remove();
                    tracker.removeTracker();
                }
            }
        }
    }

    /**
     * @param entityClass the entity class
     * @return tue if the entity's properties are cached
     */
    public boolean hasEntityInCache(Class<? extends Entity> entityClass) {
        return this.entityPropertiesCache.containsKey(entityClass);
    }

    /**
     * Add an entity and its properties to the cache.
     *
     * @param entityClass the entity class
     * @param propertyIDs the property IDs
     */
    public void addEntityToCache(Class<? extends Entity> entityClass, List<String> propertyIDs) {
        this.entityPropertiesCache.put(entityClass, propertyIDs);
    }

    /**
     * @return a Stream of all registered properties
     */
    public Stream<Map.Entry<Class<? extends Entity>, List<Class<? extends EntityProperties<?>>>>> getRegisteredProperties() {
        return this.registeredProperties.entrySet().stream();
    }

    /**
     * Get all entity trackers from a player.
     *
     * @param player the player instance
     * @return all entity trackers
     */
    public List<PropertiesTracker<?>> getEntityTrackers(EntityPlayerMP player) {
        return this.trackerMap.computeIfAbsent(player, p -> new ArrayList<>());
    }

    /**
     * @return the Iterator of the current trackers
     */
    public Iterator<Map.Entry<EntityPlayerMP, List<PropertiesTracker<?>>>> getTrackerIterator() {
        return this.trackerMap.entrySet().iterator();
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            this.trackerMap.remove(event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer player = event.getOriginal();
        if (player instanceof EntityPlayerMP) {
            this.trackerMap.remove(player);
        }
    }
}
