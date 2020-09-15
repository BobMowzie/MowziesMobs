package com.ilexiconn.llibrary.server.capability;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public enum EntityDataHandler {
    INSTANCE;

    // TODO: Remove in 1.13
    private final Map<Entity, List<IEntityData>> queuedEntityData = new IdentityHashMap<>();

    /**
     * Registers an Extended Entity Data Manager to the given entity
     *
     * @param entity the entity to add data to
     * @param entityData the data manager
     * @param <T> the entity type
     *
     * @deprecated Use {@link net.ilexiconn.llibrary.server.event.CollectEntityDataEvent} to register data
     */
    @Deprecated
    public <T extends Entity> void registerExtendedEntityData(T entity, IEntityData<T> entityData) {
        IEntityDataCapability dataCap = entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
        if (dataCap == null) {
            List<IEntityData> data = this.queuedEntityData.computeIfAbsent(entity, e -> new ArrayList<>());
            data.add(entityData);
        } else {
            dataCap.registerData(entityData);
        }
    }

    /**
     * @param entity the entity
     * @param identifier the string identifier
     * @param <T> the entity type
     * @return an IEntityData instance on the given entity with the given identifier
     */
    @Nullable
    public <T extends Entity> IEntityData<T> getEntityData(T entity, String identifier) {
        IEntityDataCapability dataCap = entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
        if (dataCap == null) {
            throw new IllegalStateException("Cannot get entity data on entity without data cap");
        }
        return dataCap.getData(identifier);
    }

    /**
     * Get a list with all the registered data manager for the specified entity
     *
     * @param entity the entity instance
     * @param <T> the entity type
     * @return a list with all the data managers, never null
     */
    public <T extends Entity> List<IEntityData<T>> getEntityData(T entity) {
        IEntityDataCapability dataCap = entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
        if (dataCap == null) {
            throw new IllegalStateException("Cannot get entity data on entity without data cap");
        }
        return dataCap.getData();
    }

    @Deprecated
    public void putQueuedData(Entity entity, IEntityDataCapability dataCap) {
        List<IEntityData> queuedData = this.queuedEntityData.remove(entity);
        if (queuedData != null) {
            queuedData.forEach(dataCap::registerData);
        }
    }

    @Deprecated
    public void releaseQueuedData(Entity entity) {
        this.queuedEntityData.remove(entity);
    }
}
