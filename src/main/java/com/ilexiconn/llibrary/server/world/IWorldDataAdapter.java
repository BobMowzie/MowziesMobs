package com.ilexiconn.llibrary.server.world;

import net.minecraft.nbt.CompoundNBT;
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
    void loadNBTData(CompoundNBT compound, World world);

    /**
     * Save data to the world.
     *
     * @param compound the compound tag
     * @param world    the world instance
     */
    void saveNBTData(CompoundNBT compound, World world);
}
