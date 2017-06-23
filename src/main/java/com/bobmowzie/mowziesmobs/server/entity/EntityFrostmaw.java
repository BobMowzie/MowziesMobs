package com.bobmowzie.mowziesmobs.server.entity;

import akka.actor.dsl.Creators;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Josh on 5/8/2017.
 */
public class EntityFrostmaw extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(130);
    public static final Animation HURT_ANIMATION = Animation.create(13);
    public static final Animation ROAR_ANIMATION = Animation.create(76);
    public static final Animation SWIPE_ANIMATION = Animation.create(28);
    public static final Animation SWIPE_TWICE_ANIMATION = Animation.create(57);
    public static final Animation ICE_BREATH_ANIMATION = Animation.create(92);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(118);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(25);

    private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityWroughtnaut.class, DataSerializers.BOOLEAN);

    public static final int ICE_BREATH_COOLDOWN = 200;

    public EntityItem crystal;
    public EntityIceBreath iceBreath;

    public boolean swingWhichArm = false;
    Vec3d prevRightHandPos = new Vec3d(0, 0, 0);
    Vec3d prevLeftHandPos = new Vec3d(0, 0, 0);
    int iceBreathCooldown = 0;
    int timeWithoutTarget;

    public LegSolverQuadruped legSolver;

    public EntityFrostmaw(World world) {
        super(world);
        setSize(4f, 4f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(2, new AnimationAreaAttackAI<>(this, SWIPE_ANIMATION, null, null, 2, 7, 6, 120, 1, 9));
        this.tasks.addTask(2, new AnimationAreaAttackAI<>(this, SWIPE_TWICE_ANIMATION, null, null, 1, 7, 6, 120, 1, 9));
        this.tasks.addTask(2, new AnimationAI<>(this, ICE_BREATH_ANIMATION, true));
        this.tasks.addTask(2, new AnimationAI<>(this, ROAR_ANIMATION, false));
        this.tasks.addTask(2, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        this.tasks.addTask(2, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntitySheep.class, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        stepHeight = 1;
        frame += rand.nextInt(50);
        legSolver = new LegSolverQuadruped(1f, 2f, -1, 1.5f);
        socketPosArray = new Vec3d[] {new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 0)};
        active = false;
        crystal = new EntityItem(world, posX, posY, posZ, new ItemStack(ItemHandler.INSTANCE.iceCrystal));
        world.spawnEntity(crystal);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(ACTIVE, false);
    }

    @Override
    public void playLivingSound() {
        if (!active) return;
        if (rand.nextInt(6) != 0) return;
        super.playLivingSound();
        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, ROAR_ANIMATION);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    public void onUpdate() {
        rotationYaw = renderYawOffset;
        super.onUpdate();
        this.repelEntities(4f, 4f, 4f, 4f);

        if (getActive() && getAnimation() != ACTIVATE_ANIMATION) {
            legSolver.update(this);

            if ((getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) && getAnimationTick() == 1) {
                swingWhichArm = rand.nextBoolean();
            }

            if (getAnimation() == SWIPE_ANIMATION) {
                if (getAnimationTick() == 6) {
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 1, 0.8f);
                }
            }

            if (getAnimation() == SWIPE_TWICE_ANIMATION && currentAnim instanceof AnimationAreaAttackAI<?>) {
                if (getAnimationTick() == 21) {
                    ((AnimationAreaAttackAI<?>) currentAnim).hitEntities();
                }
                if (getAnimationTick() == 16) {
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 1, 0.7f);
                }
                if (getAnimationTick() == 6) {
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 1, 0.8f);
                }
            }

            if (getAnimation() == ROAR_ANIMATION) {
                if (getAnimationTick() == 10) {
                    playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
                }
            }

            if (getAnimation() == ICE_BREATH_ANIMATION) {
                if (getAttackTarget() != null) getLookHelper().setLookPositionWithEntity(getAttackTarget(), 15, 15);
                Vec3d mouthPos = socketPosArray[2];
                if (getAnimationTick() == 13) {
                    iceBreath = new EntityIceBreath(world, this);
                    iceBreath.setPositionAndRotation(mouthPos.xCoord, mouthPos.yCoord, mouthPos.zCoord, rotationYawHead, rotationPitch + 10);
                    if (!world.isRemote) world.spawnEntity(iceBreath);
                }
                if (iceBreath != null)
                    iceBreath.setPositionAndRotation(mouthPos.xCoord, mouthPos.yCoord, mouthPos.zCoord, rotationYawHead, rotationPitch + 10);
            }

            spawnSwipeParticles();

            if (getAttackTarget() != null) {
                timeWithoutTarget = 0;
                if (targetDistance > 5 && !(getAnimation() == ICE_BREATH_ANIMATION && targetDistance < 8))
                    getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1);
                else getNavigator().clearPathEntity();
                if (targetDistance <= 6 && getAnimation() == NO_ANIMATION) {
                    if (rand.nextInt(3) == 0)
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_TWICE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_ANIMATION);
                }
                if (targetDistance >= 4 && targetDistance <= 14 && getAnimation() == NO_ANIMATION && iceBreathCooldown <= 0) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BREATH_ANIMATION);
                    iceBreathCooldown = ICE_BREATH_COOLDOWN;
                }
            }
            else if (!world.isRemote) {
                timeWithoutTarget++;
                if (timeWithoutTarget > 180) {
                    timeWithoutTarget = 0;
                    if (getAnimation() == NO_ANIMATION) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        setActive(false);
                    }
                }
            }
        }
        else {
            getNavigator().clearPathEntity();
            renderYawOffset = prevRenderYawOffset;
            if (!getAttackableEntityLivingBaseNearby(11, 11, 11, 11).isEmpty() && getAnimation() == NO_ANIMATION) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                setActive(true);
            }

            Vec3d rightHandPos = socketPosArray[0];
            crystal.setPosition(rightHandPos.xCoord, rightHandPos.yCoord - 1, rightHandPos.zCoord);
        }

        if (getAnimation() == ACTIVATE_ANIMATION) {
            if (getAnimationTick() == 52) {
                playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
            }
        }

//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
//            setActive(true);
//        }

        if (iceBreathCooldown > 0) iceBreathCooldown--;
        prevRotationYaw = rotationYaw;
    }

    private void spawnSwipeParticles() {
        int snowflakeDensity = 4;
        float snowflakeRandomness = 0.5f;
        int cloudDensity = 2;
        float cloudRandomness = 0.5f;
        if (getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) {
            Vec3d rightHandPos = socketPosArray[0];
            Vec3d leftHandPos = socketPosArray[1];
            if (getAnimation() == SWIPE_ANIMATION) {
                if (getAnimationTick() > 8 && getAnimationTick() < 14) {
                    if (swingWhichArm) {
                        double length = prevRightHandPos.subtract(rightHandPos).lengthVector();
                        int numClouds = (int) Math.floor(2 * length);
                        for (int i = 0; i < numClouds; i++) {
                            double x = prevRightHandPos.xCoord + i * (rightHandPos.xCoord - prevRightHandPos.xCoord) / numClouds;
                            double y = prevRightHandPos.yCoord + i * (rightHandPos.yCoord - prevRightHandPos.yCoord) / numClouds;
                            double z = prevRightHandPos.zCoord + i * (rightHandPos.zCoord - prevRightHandPos.zCoord) / numClouds;
                            for (int j = 0; j < snowflakeDensity; j++) {
                                float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
                            }
                            for (int j = 0; j < cloudDensity; j++) {
                                float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                double value = rand.nextFloat() * 0.1f;
                                MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
                            }
                        }
                    } else {
                        double length = prevLeftHandPos.subtract(leftHandPos).lengthVector();
                        int numClouds = (int) Math.floor(2.5 * length);
                        for (int i = 0; i < numClouds; i++) {
                            double x = prevLeftHandPos.xCoord + i * (leftHandPos.xCoord - prevLeftHandPos.xCoord) / numClouds;
                            double y = prevLeftHandPos.yCoord + i * (leftHandPos.yCoord - prevLeftHandPos.yCoord) / numClouds;
                            double z = prevLeftHandPos.zCoord + i * (leftHandPos.zCoord - prevLeftHandPos.zCoord) / numClouds;
                            for (int j = 0; j < snowflakeDensity; j++) {
                                float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
                            }
                            for (int j = 0; j < cloudDensity; j++) {
                                float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                double value = rand.nextFloat() * 0.1f;
                                MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
                            }
                        }
                    }
                }
            } else {
                if ((swingWhichArm && getAnimationTick() > 8 && getAnimationTick() < 14) || (!swingWhichArm && getAnimationTick() > 19 && getAnimationTick() < 25)) {
                    double length = prevRightHandPos.subtract(rightHandPos).lengthVector();
                    int numClouds = (int) Math.floor(2 * length);
                    for (int i = 0; i < numClouds; i++) {
                        double x = prevRightHandPos.xCoord + i * (rightHandPos.xCoord - prevRightHandPos.xCoord) / numClouds;
                        double y = prevRightHandPos.yCoord + i * (rightHandPos.yCoord - prevRightHandPos.yCoord) / numClouds;
                        double z = prevRightHandPos.zCoord + i * (rightHandPos.zCoord - prevRightHandPos.zCoord) / numClouds;
                        for (int j = 0; j < snowflakeDensity; j++) {
                            float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
                        }
                        for (int j = 0; j < cloudDensity; j++) {
                            float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            double value = rand.nextFloat() * 0.1f;
                            MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
                        }
                    }
                } else if ((!swingWhichArm && getAnimationTick() > 8 && getAnimationTick() < 14) || (swingWhichArm && getAnimationTick() > 19 && getAnimationTick() < 25)) {
                    double length = prevLeftHandPos.subtract(leftHandPos).lengthVector();
                    int numClouds = (int) Math.floor(2.5 * length);
                    for (int i = 0; i < numClouds; i++) {
                        double x = prevLeftHandPos.xCoord + i * (leftHandPos.xCoord - prevLeftHandPos.xCoord) / numClouds;
                        double y = prevLeftHandPos.yCoord + i * (leftHandPos.yCoord - prevLeftHandPos.yCoord) / numClouds;
                        double z = prevLeftHandPos.zCoord + i * (leftHandPos.zCoord - prevLeftHandPos.zCoord) / numClouds;
                        for (int j = 0; j < snowflakeDensity; j++) {
                            float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                            MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
                        }
                        for (int j = 0; j < cloudDensity; j++) {
                            float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                            double value = rand.nextFloat() * 0.1f;
                            MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
                        }
                    }
                }
            }
            prevLeftHandPos = leftHandPos;
            prevRightHandPos = rightHandPos;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    public void setActive(boolean active) {
        getDataManager().set(ACTIVE, active);
    }

    public boolean getActive() {
        this.active = getDataManager().get(ACTIVE);
        return active;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] {DIE_ANIMATION, HURT_ANIMATION, ROAR_ANIMATION, SWIPE_ANIMATION, SWIPE_TWICE_ANIMATION, ICE_BREATH_ANIMATION, ACTIVATE_ANIMATION, DEACTIVATE_ANIMATION};
    }
}
