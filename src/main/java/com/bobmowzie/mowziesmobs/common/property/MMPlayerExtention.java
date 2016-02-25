package com.bobmowzie.mowziesmobs.common.property;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Created by jnad325 on 2/24/16.
 */
public class MMPlayerExtention implements IExtendedEntityProperties {
    public int untilSunstrike = 0;

    @Override
    public void saveNBTData(NBTTagCompound compound) {

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {

    }

    @Override
    public void init(Entity entity, World world) {

    }
}
