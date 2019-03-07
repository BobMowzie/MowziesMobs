package com.bobmowzie.mowziesmobs.server.entity.naga;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.ai.MMAINearestAttackableTarget;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationProjectileAttackAI;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity implements IRangedAttackMob, IMob {
    @SideOnly(Side.CLIENT)
    public DynamicChain dc;
    @SideOnly(Side.CLIENT)
    public Vec3d mouthPos;

    public static final Animation FLAP_ANIMATION = Animation.create(25);
    public static final Animation DODGE_ANIMATION = Animation.create(10);
    public static final Animation SPIT_ANIMATION = Animation.create(50);
    public static final Animation SWOOP_ANIMATION = Animation.create(54);
    public static final Animation HURT_TO_FALL_ANIMATION = Animation.create(20);
    public static final Animation LAND_ANIMATION = Animation.create(8);
    public static final Animation GET_UP_ANIMATION = Animation.create(33);
    public static final Animation TAIL_DEMO_ANIMATION = Animation.create(80);
    public static final Animation DIE_AIR_ANIMATION = Animation.create(70);
    public static final Animation DIE_GROUND_ANIMATION = Animation.create(70);

    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityNaga.class, DataSerializers.BOOLEAN);

    private ControlledAnimation hoverAnim = new ControlledAnimation(10);
    private ControlledAnimation flapAnim = new ControlledAnimation(10);
    public float hoverAnimFrac;
    public float prevHoverAnimFrac;
    public float flapAnimFrac;
    public float prevFlapAnimFrac;

    private boolean hasFlapSoundPlayed = false;
    @SideOnly(Side.CLIENT)
    public float shoulderRot;

    public static final int ROAR_DURATION = 30;
    public int roarAnimation = 0;

    public enum EnumNagaMovement {
        GLIDING,
        HOVERING,
        SWIMMING,
        FALLING,
        FALLEN
    }

    public EnumNagaMovement movement = EnumNagaMovement.GLIDING;

    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;

    public static int SPIT_COOLDOWN_MAX = 120;
    public int spitCooldown = 0;

    public static int SWOOP_COOLDOWN_MAX = 90;
    public int swoopCooldown = 0;
    public float swoopTargetCorrectY;
    public float swoopTargetCorrectX;

    public static int GROUND_TIMER_MAX = 60;
    public int onGroundTimer = 0;

    public boolean interrupted = false;

    public EntityNaga(World world) {
        super(world);
        this.tasks.addTask(5, new EntityNaga.AIRandomFly(this));
        this.tasks.addTask(5, new EntityNaga.AIFlyAroundTarget(this));
        this.tasks.addTask(7, new EntityNaga.AILookAround(this));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.targetTasks.addTask(1, new MMAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, true, null));
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, FLAP_ANIMATION, false) {
            @Override
            public void updateTask() {
                super.updateTask();
                if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_NAGA_FLAP_1, 2, (float) (0.85 + Math.random() * 0.2));
                if (getAnimationTick() >= 4 && getAnimationTick() <= 9) {
                    motionY += 0.1;
                }
            }
        });
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, DODGE_ANIMATION, false));
        this.tasks.addTask(2, new AnimationProjectileAttackAI<EntityNaga>(this, SPIT_ANIMATION, 30, null) {
            @Override
            public void updateTask() {
                super.updateTask();
                if (interrupted) return;
                if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE, 2, 1);
                if (getAnimationTick() < 9) motionY += 0.015;
//                if (getAnimationTick() == 28) motionY -= 0.2;
            }
        });
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, SWOOP_ANIMATION, true) {
            @Override
            public void updateTask() {
                super.updateTask();
                if (interrupted) return;

                Vec3d v = new Vec3d(0, 0, 0);

                int phase1Length = 15;
                int phase2Length = 21;
                if (getAnimationTick() < 23 + phase2Length) {
                    EntityLivingBase target = getAttackTarget();
                    if (getAnimationTick() >= 1 && getAnimationTick() < 1 + phase1Length) {
                        float frame = (getAnimationTick() - 1) / (float) phase1Length;
                        v = v.add(new Vec3d(
                                1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                -1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                0.8f * Math.sin(frame * Math.PI * 2)
                        ));
                    } else if (getAnimationTick() >= 16) {
                        if (target != null)
                            entity.getLookHelper().setLookPositionWithEntity(target, 30F, 30F);
                        if (getAnimationTick() == 23) {
                            if (target != null) {
                                swoopTargetCorrectY = 0.09f * (float) Math.abs(posY - target.posY);
                                swoopTargetCorrectX = 0.1f * (float) Math.sqrt((posX - target.posX) * (posX - target.posX) + (posZ - target.posZ) * (posZ - target.posZ));
                                if (swoopTargetCorrectX > 1.8f) swoopTargetCorrectX = 1.8f;
                                if (swoopTargetCorrectY > 2f) swoopTargetCorrectY = 2f;
                            }
                            else {
                                swoopTargetCorrectX = swoopTargetCorrectY = 1;
                            }
                        }
                        if (getAnimationTick() >= 23 && getAnimationTick() < 23 + phase2Length) {
                            float frame = (getAnimationTick() - 23) / (float) phase2Length;
                            v = v.add(new Vec3d(
                                    swoopTargetCorrectX * 1.4 * (1 - Math.exp(2 * (frame - 1))),
                                    swoopTargetCorrectY * -1.5 * (Math.cos(frame * Math.PI) * (1 - Math.exp(7 * (frame - 1)))),
                                    0
                            ));

                            List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(4, 4, 4, 4);
                            for (EntityLivingBase entityHit : entitiesHit) {
                                if (entityHit instanceof EntityNaga) continue;
                                attackEntityAsMob(entityHit);
                            }
                        }
                    }

                    v = v.rotateYaw((float) Math.toRadians(-rotationYaw - 90));
                    motionX = v.x;
                    motionY = v.y;
                    motionZ = v.z;
                }

                if (getAnimationTick() == 22) MowziesMobs.PROXY.playNagaSwoopSound(entity);
                if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_NAGA_FLAP_1, 2, 0.7f);

                if (getAnimationTick() == 7) playSound(MMSounds.ENTITY_NAGA_GRUNT_3, 2, 1f);
                if (getAnimationTick() == 22) playSound(MMSounds.ENTITY_NAGA_ROAR_1, 3, 1f);
            }
        });
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, HURT_TO_FALL_ANIMATION, true) {
            @Override
            public void updateTask() {

            }
        });
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, LAND_ANIMATION, true) {
            @Override
            public void updateTask() {
                if (getAnimationTick() == 1) playSound(MMSounds.MISC_GROUNDHIT_2, 1.5f, 1);
            }
        });
        this.tasks.addTask(1, new AnimationAI<EntityNaga>(this, GET_UP_ANIMATION, true) {
            @Override
            public void updateTask() {
                super.updateTask();
                if (getAnimationTick() == 13) playSound(MMSounds.ENTITY_NAGA_FLAP_1, 2f, 1);

                if (getAnimationTick() == 15) {
                    motionY += 1.6f;
                }
            }
        });

        this.moveHelper = new NagaMoveHelper(this);
        setSize(3, 1);
        if (world.isRemote) {
            dc = new DynamicChain(this);
        }
        setRenderDistanceWeight(3.0D);

        this.experienceValue = 10;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(ATTACKING, false);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 16600;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D * MowziesMobs.CONFIG.healthScaleNaga);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D * MowziesMobs.CONFIG.attackScaleNaga);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(45);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() != NO_ANIMATION) return null;
        int r = rand.nextInt(4);
        if (r == 0) {
            playSound(MMSounds.ENTITY_NAGA_ROAR.get(rand.nextInt(4)).get(), 3, 1);
            roarAnimation = 0;
        }
        else if (r <= 2) {
            playSound(MMSounds.ENTITY_NAGA_GROWL.get(rand.nextInt(3)).get(), 2, 1);
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        playSound(MMSounds.ENTITY_NAGA_GRUNT.get(rand.nextInt(3)).get(), 2, 1);
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        playSound(MMSounds.ENTITY_NAGA_ROAR.get(rand.nextInt(4)).get(), 3, 1);
        return null;
    }

    @Override
    public void onUpdate() {
        prevMotionX = motionX;
        prevMotionY = motionY;
        prevMotionZ = motionZ;
        prevHoverAnimFrac = hoverAnimFrac;
        prevFlapAnimFrac = flapAnimFrac;

        super.onUpdate();
//        setDead();
        renderYawOffset = rotationYaw;

        if (spitCooldown > 0) spitCooldown--;
        if (swoopCooldown > 0) swoopCooldown--;
        if (onGroundTimer > 0) onGroundTimer--;
        if (roarAnimation < ROAR_DURATION) roarAnimation++;

        if (getAnimation() == null) AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);

//        if (ticksExisted == 1) {
//            System.out.println("Naga at " + getPosition());
//        }

        if (!world.isRemote) {
            if (getAttackTarget() != null && targetDistance < 30 && movement != EnumNagaMovement.FALLEN && movement != EnumNagaMovement.FALLING) {
                setAttacking(true);
                if (getAnimation() == NO_ANIMATION && swoopCooldown == 0 && rand.nextInt(80) == 0 && posY - getAttackTarget().posY > 0) {
                    interrupted = false;
//                    System.out.println("Swoop");
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SWOOP_ANIMATION);
                    swoopCooldown = SWOOP_COOLDOWN_MAX;
                }
                else if (getAnimation() == NO_ANIMATION && spitCooldown == 0 && rand.nextInt(80) == 0) {
                    interrupted = false;
//                    System.out.println("Spit");
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SPIT_ANIMATION);
                    spitCooldown = SPIT_COOLDOWN_MAX;
                }
            } else {
                setAttacking(false);
            }
        }

        if (movement != EnumNagaMovement.FALLING && movement != EnumNagaMovement.FALLEN) {
            if (getAttacking()) {
                movement = EnumNagaMovement.HOVERING;
                hoverAnim.increaseTimer();

                if (getAnimation() == NO_ANIMATION && !world.isRemote) {
                    List<EntityArrow> arrowsNearby = getEntitiesNearby(EntityArrow.class, 30);
                    for (EntityArrow a : arrowsNearby) {
                        Vec3d aActualMotion = new Vec3d(a.posX - a.prevPosX, a.posY - a.prevPosY, a.posZ - a.prevPosZ);
                        if (aActualMotion.lengthVector() < 0.1 || a.ticksExisted <= 1) {
                            continue;
                        }

                        Vec3d aMotion = new Vec3d(a.motionX, a.motionY, a.motionZ);
                        float dot = (float) aMotion.normalize().dotProduct(this.getPositionVector().subtract(a.getPositionVector()).normalize());
                        if (dot > 0.96) {
                            Vec3d dodgeVec = aMotion.crossProduct(new Vec3d(0, 1, 0)).normalize().scale(1.2);
                            Vec3d newPosLeft = getPositionVector().add(dodgeVec.scale(2));
                            Vec3d newPosRight = getPositionVector().add(dodgeVec.scale(-2));
                            Vec3d diffLeft = newPosLeft.subtract(a.getPositionVector());
                            Vec3d diffRight = newPosRight.subtract(a.getPositionVector());
                            if (diffRight.dotProduct(aMotion) > diffLeft.dotProduct(aMotion)) {
                                dodgeVec = dodgeVec.scale(-1);
                            }
                            motionX += dodgeVec.x;
                            motionY += dodgeVec.y;
                            motionZ += dodgeVec.z;
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
                        }
                    }
                }
            }
            else {
                movement = EnumNagaMovement.GLIDING;
                hoverAnim.decreaseTimer();
                flapAnim.decreaseTimer();
            }
        }
        else if (movement == EnumNagaMovement.FALLING) {
            if (onGround) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.LAND_ANIMATION);
                movement = EnumNagaMovement.FALLEN;
                onGroundTimer = GROUND_TIMER_MAX;
                motionX = motionZ = 0;
                getNavigator().clearPathEntity();
            }
        }

        if (movement == EnumNagaMovement.FALLEN) {
            if (onGroundTimer <= 0 && getAnimation() == NO_ANIMATION) {
                setAnimationTick(0);
                AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.GET_UP_ANIMATION);
            }
        }

        if (getAnimation() == SWOOP_ANIMATION && getAnimationTick() < 43) {
            hoverAnim.increaseTimer();
            flapAnim.decreaseTimer();
        }
        else if (getAnimation() == HURT_TO_FALL_ANIMATION) {
            flapAnim.decreaseTimer();
            hoverAnim.increaseTimer();
        }
        else if (getAnimation() == LAND_ANIMATION) {
            flapAnim.decreaseTimer();
            hoverAnim.increaseTimer();
        }
        else if (getAnimation() == GET_UP_ANIMATION && getAnimationTick() < 26) {
            flapAnim.decreaseTimer();
            hoverAnim.increaseTimer();
        }
        else if (movement == EnumNagaMovement.FALLEN) {
            flapAnim.decreaseTimer();
            hoverAnim.increaseTimer();
            rotationYaw = prevRotationYaw;
            rotationYawHead = prevRotationYawHead;
            rotationPitch = prevRotationPitch;
        }
        else if (movement == EnumNagaMovement.FALLING) {
            flapAnim.decreaseTimer();
            hoverAnim.increaseTimer();
        }
        else {
            flapAnim.increaseTimer();
        }

        if (getAnimation() == SPIT_ANIMATION && world.isRemote && mouthPos != null && !interrupted) {
            if (getAnimationTick() == 33) {
//            System.out.println(mouthPos);
                float explodeSpeed = 1.5f;
                for (int i = 0; i < 15; i++) {
                    Vec3d particlePos = new Vec3d(0.25, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                    double value = rand.nextFloat() * 0.15f;
                    MMParticle.CLOUD.spawn(world, particlePos.x + mouthPos.x, particlePos.y + mouthPos.y - 1, particlePos.z + mouthPos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0.1d + value, 0.4d, 0.1d + value, 2, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW, 0.7d));
                }
                for (int i = 0; i < 15; i++) {
                    Vec3d particlePos = new Vec3d(0.2, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                    double value = rand.nextFloat() * 0.15f;
                    MMParticle.CLOUD.spawn(world, particlePos.x + mouthPos.x, particlePos.y + mouthPos.y - 1, particlePos.z + mouthPos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0.3d + value, 1d, 0.3d + value, 2, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW, 0.7d));
                }
                for (int i = 0; i < 13; i++) {
                    Vec3d particlePos = new Vec3d(0.25, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                    MMParticle.CLOUD.spawn(world, particlePos.x + mouthPos.x, particlePos.y + mouthPos.y - 1, particlePos.z + mouthPos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0.1d, 0.2d, 0.1d, 0, 2d, 30, ParticleCloud.EnumCloudBehavior.CONSTANT, 0.7d));
                }
            }

            if (getAnimationTick() <= 15 && mouthPos != null && !interrupted) {
//            System.out.println(mouthPos);

                float returnSpeed = -0.03f;
                int howMany = 2;
                for (int i = 0; i < howMany; i++) {
                    Vec3d particlePos = new Vec3d(3, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                    double value = rand.nextFloat() * 0.15f;
                    MMParticle.CLOUD.spawn(world, particlePos.x + mouthPos.x, particlePos.y + mouthPos.y, particlePos.z + mouthPos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * returnSpeed, particlePos.y * returnSpeed, particlePos.z * returnSpeed, 0.1d + value, 0.4d, 0.1d + value, 2, 10d + rand.nextDouble() * 20d, 27, ParticleCloud.EnumCloudBehavior.SHRINK, 1.2d));
                }
                for (int i = 0; i < howMany; i++) {
                    Vec3d particlePos = new Vec3d(3, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                    double value = rand.nextFloat() * 0.15f;
                    MMParticle.CLOUD.spawn(world, particlePos.x + mouthPos.x, particlePos.y + mouthPos.y, particlePos.z + mouthPos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * returnSpeed, particlePos.y * returnSpeed, particlePos.z * returnSpeed, 0.3d + value, 1d, 0.3d + value, 2, 10d + rand.nextDouble() * 20d, 27, ParticleCloud.EnumCloudBehavior.SHRINK, 1.2d));
                }
                for (int i = 0; i < howMany - 2; i++) {
                    Vec3d particlePos = new Vec3d(3, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                    MMParticle.CLOUD.spawn(world, particlePos.x + mouthPos.x, particlePos.y + mouthPos.y, particlePos.z + mouthPos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * returnSpeed, particlePos.y * returnSpeed, particlePos.z * returnSpeed, 0.1d, 0.2d, 0.1d, 0, 2d, 18, ParticleCloud.EnumCloudBehavior.CONSTANT, 1.2d));
                }
            }
        }

        if (getAnimation() == HURT_TO_FALL_ANIMATION && getAnimationTick() == 17) {
            movement = EnumNagaMovement.FALLING;
        }

        if (getAnimation() == GET_UP_ANIMATION && getAnimationTick() == 26) {
            movement = EnumNagaMovement.HOVERING;
        }

        if (world.isRemote && movement == EnumNagaMovement.HOVERING && flapAnim.getAnimationFraction() >= 0.5) {

            if (shoulderRot > 0.9) hasFlapSoundPlayed = false;

            if (shoulderRot <= 0.7 && !hasFlapSoundPlayed) {
                world.playSound(posX, posY, posZ, MMSounds.ENTITY_NAGA_FLAP_1, SoundCategory.HOSTILE, 2, (float) (0.85 + Math.random() * 0.2), false);
                hasFlapSoundPlayed = true;
            }
        }

        hoverAnimFrac = hoverAnim.getAnimationProgressSinSqrt();
        flapAnimFrac = flapAnim.getAnimationProgressSinSqrt();

//        setAttacking(true);
//        getNavigator().clearPathEntity();
//        posX = prevPosX;
//        posY = prevPosY;
//        posZ = prevPosZ;
//        motionX = motionZ = 0;
//        posY = 10;
//
//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, TAIL_DEMO_ANIMATION);
//        }
    }

    @Override
    public boolean getCanSpawnHere() {
        setPosition(posX, posY + 5, posZ);
        boolean flag = super.getCanSpawnHere() && world.canSeeSky(getPosition());
//        System.out.println("Try spawn " + flag);
//        if (flag) System.out.println(getPosition());
        return flag;
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    public boolean isNotColliding()
    {
        boolean liquid = !this.world.containsAnyLiquid(this.getEntityBoundingBox());
        boolean worldCollision = this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
        boolean mobCollision = this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);

        return liquid && worldCollision && mobCollision;
    }

    @Override
    protected void onDeathAIUpdate() {
        super.onDeathAIUpdate();
        if (deathTime == 15 && movement != EnumNagaMovement.FALLEN) movement = EnumNagaMovement.FALLING;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        boolean flag = super.attackEntityFrom(source, damage);
        boolean isSpitting = getAnimation() == SPIT_ANIMATION && getAnimationTick() < 30;
        boolean isSwooping = getAnimation() == SWOOP_ANIMATION && getAnimationTick() < 25;
        if (flag && movement != EnumNagaMovement.FALLING && (isSpitting || isSwooping)) {
            setAnimationTick(0);
            if (currentAnim != null) currentAnim.resetTask();
            AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);
            AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.HURT_TO_FALL_ANIMATION);
            interrupted = true;
        }
        return flag;
    }

    protected void despawnEntity()
    {
        net.minecraftforge.fml.common.eventhandler.Event.Result result = null;

        if ((this.idleTime & 0x1F) == 0x1F && (result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this)) != net.minecraftforge.fml.common.eventhandler.Event.Result.DEFAULT)
        {
            if (result == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
            {
                this.idleTime = 0;
            }
            else
            {
                this.setDead();
            }
        }
        else
        {
            Entity entity = this.world.getClosestPlayerToEntity(this, -1.0D);

            if (entity != null)
            {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posY - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.canDespawn() && d3 > 16384.0D * 2)
                {
                    this.setDead();
                }

                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn())
                {
                    this.setDead();
                }
                else if (d3 < 1024.0D)
                {
                    this.idleTime = 0;
                }
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if (interrupted) return;
        Vec3d projectilePos = new Vec3d(1, -0.7, 0);
        projectilePos = projectilePos.rotateYaw((float)Math.toRadians(-rotationYaw - 90));
        projectilePos = projectilePos.add(getPositionVector());
        projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotatePitch((float)Math.toRadians(-rotationPitch)).rotateYaw((float)Math.toRadians(-rotationYawHead)));
        projectilePos = projectilePos.add(new Vec3d(0, 0, 0));
        EntityPoisonBall poisonBall = new EntityPoisonBall(this.world, this);
        poisonBall.setPosition(projectilePos.x, projectilePos.y, projectilePos.z);
        Vec3d look = getLookVec();
        Vec3d dir = new Vec3d(look.x, 0, look.z).normalize();
        if (target != null) {
            float dy = (float) (projectilePos.y - target.posY);
            float dx = (float) (projectilePos.x - target.posX);
            float dz = (float) (projectilePos.z - target.posZ);
            float dist = (float) Math.sqrt(dx * dx + dz * dz);
            float timeGuess = (float) Math.sqrt(2 * dy / EntityPoisonBall.GRAVITY);
            float speed = Math.min(dist/timeGuess, 0.9f);
            poisonBall.setThrowableHeading(dir.x * speed, 0.1, dir.z * speed, 1f, 0);
        }

        this.world.spawnEntity(poisonBall);

        playSound(MMSounds.ENTITY_NAGA_ACID_SPIT, 2, 1);
        playSound(MMSounds.ENTITY_NAGA_ACID_SPIT_HISS, 2, 1);
    }

    @Override
    public Animation getDeathAnimation() {
        if (movement == EnumNagaMovement.FALLEN) return DIE_GROUND_ANIMATION;
        else return DIE_AIR_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    protected void dropLoot() {
        super.dropLoot();
        if (rand.nextInt(4) == 0) dropItem(ItemHandler.NAGA_FANG, 1);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] {FLAP_ANIMATION, DODGE_ANIMATION, SWOOP_ANIMATION, SPIT_ANIMATION, HURT_TO_FALL_ANIMATION, LAND_ANIMATION, GET_UP_ANIMATION, DIE_AIR_ANIMATION, DIE_GROUND_ANIMATION, TAIL_DEMO_ANIMATION};
    }

    public void fall(float distance, float damageMultiplier)
    {
        if (movement == EnumNagaMovement.FALLING) {
            super.fall(distance, damageMultiplier);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
        if (movement == EnumNagaMovement.FALLING) {
            super.updateFallState(y, onGroundIn, state, pos);
        }
    }

    public void travel(float strafe, float upward, float forward) {
        if (this.isInWater()) {
            this.moveRelative(strafe, upward, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.isInLava()) {
            this.moveRelative(strafe, upward, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else if (movement == EnumNagaMovement.HOVERING) {
            float f = 0.91F;

            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(strafe, upward, forward, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) f;
            this.motionY *= (double) f;
            this.motionZ *= (double) f;

            EntityMoveHelper entitymovehelper = this.getMoveHelper();
            double dx = entitymovehelper.getX() - this.posX;
            double dy = entitymovehelper.getY() - this.posY;
            double dz = entitymovehelper.getZ() - this.posZ;
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest < 0.1 && getAnimation() == NO_ANIMATION) {
                motionX = 0;
                motionY = 0;
                motionZ = 0;
            }
        }
        else if (movement == EnumNagaMovement.GLIDING) {
            if (this.motionY > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vec3d vec3d = this.getLookVec();
            float f = this.rotationPitch * 0.017453292F;
            double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
            double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            double d1 = vec3d.lengthVector();
            float f4 = MathHelper.cos(f);
            f4 = (float) ((double) f4 * (double) f4 * Math.min(1.0D, d1 / 0.4D));
            this.motionY += -0.08D + (double) f4 * 0.06D;

            if (this.motionY < 0.0D && d6 > 0.0D) {
                double d2 = this.motionY * -0.1D * (double) f4;
                this.motionY += d2;
                this.motionX += vec3d.x * d2 / d6;
                this.motionZ += vec3d.z * d2 / d6;
            }

            if (f < 0.0F) {
                double d10 = d8 * (double) (-MathHelper.sin(f)) * 0.04D;
                this.motionY += d10 * 3.2D;
                this.motionX -= vec3d.x * d10 / d6;
                this.motionZ -= vec3d.z * d10 / d6;
            }

            if (d6 > 0.0D) {
                this.motionX += (vec3d.x / d6 * d8 - this.motionX) * 0.1D;
                this.motionZ += (vec3d.z / d6 * d8 - this.motionZ) * 0.1D;
            }

            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9900000095367432D;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (getMoveHelper().getY() - posY > 0 && motionY < 0 && getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

            if (this.isCollidedHorizontally && !this.world.isRemote) {
                double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                double d3 = d8 - d11;
                float f5 = (float) (d3 * 10.0D - 3.0D);
            }

            if (this.onGround && !this.world.isRemote) {
                this.setFlag(7, false);
            }
        }
        else if (movement == EnumNagaMovement.FALLING || movement == EnumNagaMovement.FALLEN || isAIDisabled()) {
            float f6 = 0.91F;
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);

            if (this.onGround)
            {
                IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos);
                f6 = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this) * 0.91F;
            }

            float f7 = 0.16277136F / (f6 * f6 * f6);
            float f8;

            if (this.onGround)
            {
                f8 = this.getAIMoveSpeed() * f7;
            }
            else
            {
                f8 = this.jumpMovementFactor;
            }

//            this.moveRelative(strafe, upward, forward, f8);
            f6 = 0.91F;

            if (this.onGround)
            {
                IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ));
                f6 = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this) * 0.91F;
            }

            if (this.isOnLadder())
            {
                float f9 = 0.15F;
                this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
                this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
                this.fallDistance = 0.0F;

                if (this.motionY < -0.15D)
                {
                    this.motionY = -0.15D;
                }
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            if (this.isCollidedHorizontally && this.isOnLadder())
            {
                this.motionY = 0.2D;
            }

            if (this.isPotionActive(MobEffects.LEVITATION))
            {
                this.motionY += (0.05D * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
            }
            else
            {
                blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);

                if (!this.world.isRemote || this.world.isBlockLoaded(blockpos$pooledmutableblockpos) && this.world.getChunkFromBlockCoords(blockpos$pooledmutableblockpos).isLoaded())
                {
                    if (!this.hasNoGravity())
                    {
                        this.motionY -= 0.08D;
                    }
                }
                else if (this.posY > 0.0D)
                {
                    this.motionY = -0.1D;
                }
                else
                {
                    this.motionY = 0.0D;
                }
            }

            this.motionY *= 0.9800000190734863D;
            this.motionX *= (double)f6;
            this.motionZ *= (double)f6;
            blockpos$pooledmutableblockpos.release();
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isOnLadder()
    {
        return false;
    }

    public boolean getAttacking() {
        return getDataManager().get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        getDataManager().set(ATTACKING, attacking);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }



    static class AILookAround extends EntityAIBase
    {
        private final EntityNaga parentEntity;

        public AILookAround(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexBits(2);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            if (this.parentEntity.getAttackTarget() == null)
            {
                this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
            else
            {
                EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                double d0 = 64.0D;

                if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < 4096.0D)
                {
                    double d1 = entitylivingbase.posX - this.parentEntity.posX;
                    double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }
        }
    }

    static class AIRandomFly extends EntityAIBase
    {
        private final EntityNaga parentEntity;

        public AIRandomFly(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (parentEntity.getAttacking()) return false;

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - this.parentEntity.posX;
                double d1 = entitymovehelper.getY() - this.parentEntity.posY;
                double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 24.0F);
            double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 24.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, parentEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        }
    }

    static class AIFlyAroundTarget extends EntityAIBase
    {
        private final EntityNaga parentEntity;

        public AIFlyAroundTarget(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (parentEntity.getAttackTarget() != null) {
                if (!entitymovehelper.isUpdating()) {
                    return true;
                } else {
                    double dx = entitymovehelper.getX() - this.parentEntity.posX;
                    double dy = entitymovehelper.getY() - this.parentEntity.posY;
                    double dz = entitymovehelper.getZ() - this.parentEntity.posZ;
                    double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    EntityLivingBase target = parentEntity.getAttackTarget();
                    double dx2 = entitymovehelper.getX() - target.posX;
                    double dy2 = entitymovehelper.getY() - target.posY;
                    double dz2 = entitymovehelper.getZ() - target.posZ;
                    double distanceDestToTarget = Math.sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2);

                    boolean randomChance = parentEntity.rand.nextInt(60) == 0;

//                    System.out.println(randomChance);
//                    System.out.println(distanceToDest);
//                    System.out.println(distanceDestToTarget);

                    return distanceToDest > 60.D || distanceDestToTarget > 20.D || distanceDestToTarget < 5.D || randomChance;
                }
            }
            else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            EntityLivingBase target = parentEntity.getAttackTarget();
            float yaw = (float) (random.nextFloat() * Math.PI * 2);
            float radius = 16;
            double d0 = target.posX + Math.cos(yaw) * radius;
            double d1 = target.posY + 8 + random.nextFloat() * 5;
            double d2 = target.posZ + Math.sin(yaw) * radius;
            double speed = parentEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, speed);
        }
    }

    static class NagaMoveHelper extends EntityMoveHelper
    {
        private final EntityNaga parentEntity;
        private int courseChangeCooldown;

        public NagaMoveHelper(EntityNaga naga)
        {
            super(naga);
            this.parentEntity = naga;
        }

        public void onUpdateMoveHelper()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0)
                {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(7) + 2;
                    d3 = (double) MathHelper.sqrt(d3);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                    {
                        this.parentEntity.motionX += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.motionZ += d2 / d3 * 0.1D;
                    }
                    else
                    {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        /**
         * Checks if entity bounding box is not colliding with terrain
         */
        private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - this.parentEntity.posX) / p_179926_7_;
            double d1 = (y - this.parentEntity.posY) / p_179926_7_;
            double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }
}
