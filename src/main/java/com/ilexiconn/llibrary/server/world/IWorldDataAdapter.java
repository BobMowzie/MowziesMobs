package com.ilexiconn.llibrary.server.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author iLexiconn
 * @since 1.2.0
 */
public interface IWorldDataAdapter {
    /**
     * This ID will be used for as file name.
     *
     * @return the adapter id
     */
    String getID();

    /**
     * Load data from the world.
     *
     * @param compound the compound tag
     * @param world    the world instance
     */
    void loadNBTData(NBTTagCompound compound, World world);

    /**
     * Save data to the world.
     *
     * @param compound the compound tag
     * @param world    the world instance
     */
    void saveNBTData(NBTTagCompound compound, World world);
}
