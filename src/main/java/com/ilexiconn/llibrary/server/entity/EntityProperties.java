package com.ilexiconn.llibrary.server.entity;

import com.ilexiconn.llibrary.server.capability.IEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * If you want to keep certain values in sync with clients, use a tracker time of 0 or higher.
 * Use 0 if you want changes to be sent every tick if the data has changed. Using a tracker time of 1 or higher
 * limits how often the packet is sent. The tracker automatically detects any changes and keeps
 * the values in sync.
 *
 * @param <T> the entity type
 * @author TheCyberBrick
 * @since 1.0.0
 */
public abstract class EntityProperties<T extends Entity> implements IEntityData<T> {
    private World world;
    private T entity;
    private Set<PropertiesTracker<?>> trackers = Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    public final void init(T entity, World world) {
        this.entity = entity;
        this.world = world;
        this.init();
    }

    /**
     * @return a list of all currently active trackers
     */
    public Set<PropertiesTracker<?>> getTrackers() {
        return this.trackers;
    }

    /**
     * Forces all active trackers to sync the next tick
     */
    public void sync() {
        this.trackers.forEach(PropertiesTracker::setReady);
    }

    /**
     * @return the world
     */
    public final World getWorld() {
        return this.world;
    }

    /**
     * @return the entity
     */
    public final T getEntity() {
        return this.entity;
    }

    /**
     * Initializes the properties
     */
    public abstract void init();

    /**
     * @return the ID of this property
     */
    @Override
    public abstract String getID();

    /**
     * @return the entity class or superclass this property should be applied to
     */
    public abstract Class<T> getEntityClass();

    /**
     * @return the tracking time, return a negative number for no tracking
     */
    public int getTrackingTime() {
        return -1;
    }

    /**
     * @return how often the tracking sensitive data is compared
     */
    public int getTrackingUpdateTime() {
        return 0;
    }

    /**
     * Write any tracking sensitive data to this NBT. The tracker will fire if
     * the NBT isn't equal and the tracking timer is ready.
     *
     * @param compound the compound to save to
     */
    public void saveTrackingSensitiveData(NBTTagCompound compound) {
        this.saveNBTData(compound);
    }

    /**
     * Client reads tracking sensitive data from this hook
     *
     * @param compound the compound to load from
     */
    public void loadTrackingSensitiveData(NBTTagCompound compound) {
        this.loadNBTData(compound);
    }

    /**
     * Called when the data is syncing
     */
    public void onSync() {

    }

    /**
     * Creates a new tracker for this property
     *
     * @param entity the entity instance
     * @return the new PropertiesTracker instance
     */
    public PropertiesTracker<T> createTracker(T entity) {
        PropertiesTracker<T> tracker = new PropertiesTracker<>(entity, this);
        this.trackers.add(tracker);
        return tracker;
    }
}
