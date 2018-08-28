package com.bobmowzie.mowziesmobs.server.property;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

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
    public boolean prevHasAI;


    public boolean prevFrozen = false;
    public EntityFrozenController frozenController;

    @NBTProperty
    private int frozenEntityID = -1;

    public void onFreeze(EntityLivingBase entity) {
        if (entity != null) {
            frozenController = new EntityFrozenController(entity.world);
            frozenController.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
            entity.world.spawnEntity(frozenController);
            frozenController.setRenderYawOffset(entity.renderYawOffset);
            frozenYaw = entity.rotationYaw;
            frozenPitch = entity.rotationPitch;
            entity.startRiding(frozenController, true);

            if (entity instanceof EntityLiving) prevHasAI = !((EntityLiving)entity).isAIDisabled();
            if (entity instanceof EntityLiving) ((EntityLiving)entity).setNoAI(true);

            if (entity.world.isRemote) {
                int particleCount = (int) (10 + 1 * entity.height * entity.width * entity.width);
                for (int i = 0; i < particleCount; i++) {
                    double snowX = entity.posX + entity.width * Math.random() - entity.width / 2;
                    double snowZ = entity.posZ + entity.width * Math.random() - entity.width / 2;
                    double snowY = entity.posY + entity.height * Math.random();
                    Vec3d motion = new Vec3d(snowX - entity.posX, snowY - (entity.posY + entity.height / 2), snowZ - entity.posZ).normalize();
                    MMParticle.SNOWFLAKE.spawn(entity.world, snowX, snowY, snowZ, ParticleFactory.ParticleArgs.get().withData(0.1d * motion.x, 0.1d * motion.y, 0.1d * motion.z));
                }
            }
            entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH, 1, 1);
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
