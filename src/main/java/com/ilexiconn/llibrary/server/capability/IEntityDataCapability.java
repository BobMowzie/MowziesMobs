package com.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public interface IEntityDataCapability extends ICapabilitySerializable<NBTBase> {
    /**
     * Initialize this data context.
     *
     * @param entity the new entity
     * @param world the new world
     * @param init true if all managers should be initialized
     */
    void init(Entity entity, World world, boolean init);

    /**
     * Saves this capability to NBT.
     *
     * @param compound the tag to write to
     */
    void saveToNBT(NBTTagCompound compound);

    /**
     * Loads this capability from NBT.
     *
     * @param compound the tag to read from
     */
    void loadFromNBT(NBTTagCompound compound);

    /**
     * Registered entity data to this capability
     *
     * @param data the data to register
     * @param <T> the entity type
     */
    <T extends Entity> void registerData(IEntityData<T> data);

    /**
     * Gets registered entity data with the given identifier
     *
     * @param identifier the identifier to retrieve
     * @param <T> the entity type
     * @return retrieved entity data, or null if doesn't exist on this entity
     */
    @Nullable
    <T extends Entity> IEntityData<T> getData(String identifier);

    /**
     * Gets all registered entity data on this cap
     *
     * @param <T> the entity type
     * @return list of all registered data
     */
    <T extends Entity> List<IEntityData<T>> getData();
}
