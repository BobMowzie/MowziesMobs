package com.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public interface IEntityData<T extends Entity> {
    /**
     * Initialize this data context.
     *
     * @param entity the new entity
     * @param world  the new world
     */
    void init(T entity, World world);

    /**
     * Saves data to an entity
     *
     * @param compound the NBTTagCompound to write the data to
     */
    void saveNBTData(CompoundNBT compound);

    /**
     * Reads data saved on an entity
     *
     * @param compound the NBTTagCompound to read the data from
     */
    void loadNBTData(CompoundNBT compound);

    /**
     * @return the name of the NBTTagCompound to write/read in the entities data
     */
    String getID();
}
