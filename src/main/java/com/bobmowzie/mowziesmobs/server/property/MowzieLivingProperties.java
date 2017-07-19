package com.bobmowzie.mowziesmobs.server.property;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

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

    @NBTProperty
    private int frozenEntityID = -1;

    public void setFrozenProperties(EntityLivingBase entity) {
        if (entity != null) {
            frozenYaw = entity.rotationYaw;
            frozenPitch = entity.rotationPitch;
            frozenYawHead = entity.rotationYawHead;
            frozenRenderYawOffset = entity.renderYawOffset;
            frozenSwingProgress = entity.swingProgress;
            frozenLimbSwingAmount = entity.limbSwingAmount;
            frozenTicksExisted = entity.ticksExisted;

//            frozenController = new EntityFrozenController(entity.world);
//            frozenController.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
//            entity.world.spawnEntity(frozenController);
//            frozenController.setRenderYawOffset(entity.renderYawOffset);
//            entity.startRiding(frozenController, true);
//            System.out.println(entity.isRiding());
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
