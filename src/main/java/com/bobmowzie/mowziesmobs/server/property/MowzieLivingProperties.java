package com.bobmowzie.mowziesmobs.server.property;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.property.power.Power;
import com.bobmowzie.mowziesmobs.server.property.power.PowerGeomancy;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class MowzieLivingProperties extends EntityProperties<EntityLivingBase> {
    @NBTProperty
    public float freezeProgress = 0;
    @NBTProperty
    public float frozenYaw;
    @NBTProperty
    public float frozenPitch;
    @NBTProperty
    public float frozenYawHead;
    @NBTProperty
    public float frozenRenderYawOffset;
    @NBTProperty
    public float frozenSwingProgress;
    @NBTProperty
    public float frozenLimbSwingAmount;
    @NBTProperty
    public float frozenTicksExisted;

    public void setFrozenProperties(EntityLivingBase entity) {
        if (entity != null) {
            frozenYaw = entity.rotationYaw;
            frozenPitch = entity.rotationPitch;
            frozenYawHead = entity.rotationYawHead;
            frozenRenderYawOffset = entity.renderYawOffset;
            frozenSwingProgress = entity.swingProgress;
            frozenLimbSwingAmount = entity.limbSwingAmount;
            frozenTicksExisted = entity.ticksExisted;
        }
    }

    @Override
    public void init() {

    }

    @Override
    public String getID() {
        return "mm:living";
    }

    @Override
    public Class<EntityLivingBase> getEntityClass() {
        return EntityLivingBase.class;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.saveNBTData(this, compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.loadNBTData(this, compound);
    }
}
