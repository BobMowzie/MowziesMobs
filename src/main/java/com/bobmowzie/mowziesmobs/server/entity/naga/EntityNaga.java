package com.bobmowzie.mowziesmobs.server.entity.naga;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationProjectileAttackAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.potion.Effects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by BobMowzie on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity implements IRangedAttackMob, IMob, IFlyingAnimal {
    @OnlyIn(Dist.CLIENT)
    public DynamicChain dc;
    @OnlyIn(Dist.CLIENT)
    public Vector3d[] mouthPos;

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

    private final ControlledAnimation hoverAnim = new ControlledAnimation(10);
    private final ControlledAnimation flapAnim = new ControlledAnimation(10);
    public float hoverAnimFrac;
    public float prevHoverAnimFrac;
    public float flapAnimFrac;
    public float prevFlapAnimFrac;

    private boolean hasFlapSoundPlayed = false;
    @OnlyIn(Dist.CLIENT)
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

    public EntityNaga(EntityType<? extends EntityNaga> type, World world) {
        super(type, world);
        if (world.isRemote) {
            dc = new DynamicChain(this);
            mouthPos = new Vector3d[] {new Vector3d(0, 0, 0)};
        }
        this.experienceValue = 10;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        setPathPriority(PathNodeType.WATER, -1);
        this.goalSelector.addGoal(0, new EntityNaga.FlyOutOfWaterGoal(this));
        this.goalSelector.addGoal(5, new EntityNaga.AIRandomFly(this));
        this.goalSelector.addGoal(5, new EntityNaga.AIFlyAroundTarget(this));
        this.goalSelector.addGoal(7, new EntityNaga.AILookAround(this));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, null));
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, FLAP_ANIMATION, false) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_NAGA_FLAP_1.get(), 2, (float) (0.85 + rand.nextFloat() * 0.2));
            }

            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() >= 4 && getAnimationTick() <= 9) {
                    setMotion(getMotion().add(0, 0.1, 0));
                }
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, DODGE_ANIMATION, false));
        this.goalSelector.addGoal(2, new AnimationProjectileAttackAI<EntityNaga>(this, SPIT_ANIMATION, 30, null) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE.get(), 2, 1);
            }

            @Override
            public void tick() {
                super.tick();
                if (interrupted) return;
                //if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE, 2, 1);
                if (getAnimationTick() < 9) setMotion(getMotion().add(0, 0.015, 0));
//                if (getAnimationTick() == 28) motionY -= 0.2;
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, SWOOP_ANIMATION, true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_NAGA_FLAP_1.get(), 2, 0.7f);
            }

            @Override
            public void tick() {
                super.tick();
                if (interrupted) return;

                Vector3d v = new Vector3d(0, 0, 0);

                int phase1Length = 15;
                int phase2Length = 21;
                if (getAnimationTick() < 23 + phase2Length) {
                    LivingEntity target = getAttackTarget();
                    if (getAnimationTick() >= 1 && getAnimationTick() < 1 + phase1Length) {
                        float frame = (getAnimationTick() - 1) / (float) phase1Length;
                        v = v.add(new Vector3d(
                                1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                -1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                0.8f * Math.sin(frame * Math.PI * 2)
                        ));
                    } else if (getAnimationTick() >= 16) {
                        if (target != null)
                            entity.lookController.setLookPositionWithEntity(target, 30F, 30F);
                        if (getAnimationTick() == 23) {
                            if (target != null) {
                                swoopTargetCorrectY = 0.09f * (float) Math.abs(getPosY() - target.getPosY());
                                swoopTargetCorrectX = 0.1f * (float) Math.sqrt((getPosX() - target.getPosX()) * (getPosX() - target.getPosX()) + (getPosZ() - target.getPosZ()) * (getPosZ() - target.getPosZ()));
                                if (swoopTargetCorrectX > 1.8f) swoopTargetCorrectX = 1.8f;
                                if (swoopTargetCorrectY > 2f) swoopTargetCorrectY = 2f;
                            }
                            else {
                                swoopTargetCorrectX = swoopTargetCorrectY = 1;
                            }
                        }
                        if (getAnimationTick() >= 23 && getAnimationTick() < 23 + phase2Length) {
                            float frame = (getAnimationTick() - 23) / (float) phase2Length;
                            v = v.add(new Vector3d(
                                    swoopTargetCorrectX * 1.4 * (1 - Math.exp(2 * (frame - 1))),
                                    swoopTargetCorrectY * -1.5 * (Math.cos(frame * Math.PI) * (1 - Math.exp(7 * (frame - 1)))),
                                    0
                            ));

                            List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(4, 4, 4, 4);
                            for (LivingEntity entityHit : entitiesHit) {
                                if (entityHit instanceof EntityNaga) continue;
                                attackEntityAsMob(entityHit);
                            }
                        }
                    }

                    v = v.rotateYaw((float) Math.toRadians(-rotationYaw - 90));
                    setMotion(v.x, v.y, v.z);
                }

                if (getAnimationTick() == 22) MowziesMobs.PROXY.playNagaSwoopSound(entity);

                if (getAnimationTick() == 7) playSound(MMSounds.ENTITY_NAGA_GRUNT_3.get(), 2, 1f);
                if (getAnimationTick() == 22) playSound(MMSounds.ENTITY_NAGA_ROAR_1.get(), 3, 1f);
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, HURT_TO_FALL_ANIMATION, true) {
            @Override
            public void tick() {

            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, LAND_ANIMATION, true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.MISC_GROUNDHIT_2.get(), 1.5f, 1);
            }
        });
        this.goalSelector.addGoal(1, new SimpleAnimationAI<EntityNaga>(this, GET_UP_ANIMATION, true) {
            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 13) playSound(MMSounds.ENTITY_NAGA_FLAP_1.get(), 2f, 1);

                if (getAnimationTick() == 15) {
                    setMotion(getMotion().add(0, 1.6, 0));
                }
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, TAIL_DEMO_ANIMATION, false));

        this.moveController = new NagaMoveHelper(this);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(ATTACKING, false);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 16600;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().grow(12.0D);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MowzieEntity.createAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.healthMultiplier.get())
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 12.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier.get());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() != NO_ANIMATION) return null;
        int r = rand.nextInt(4);
        if (r == 0) {
            playSound(MMSounds.ENTITY_NAGA_ROAR.get(rand.nextInt(4)).get(), 5, 1);
            roarAnimation = 0;
        }
        else if (r <= 2) {
            playSound(MMSounds.ENTITY_NAGA_GROWL.get(rand.nextInt(3)).get(), 4, 1);
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
    public void tick() {
        prevMotionX = getMotion().x;
        prevMotionY = getMotion().y;
        prevMotionZ = getMotion().z;
        prevHoverAnimFrac = hoverAnimFrac;
        prevFlapAnimFrac = flapAnimFrac;

        super.tick();
//        setDead();
        renderYawOffset = rotationYaw;

        if (spitCooldown > 0) spitCooldown--;
        if (swoopCooldown > 0) swoopCooldown--;
        if (onGroundTimer > 0) onGroundTimer--;
        if (roarAnimation < ROAR_DURATION) roarAnimation++;

        if (getAnimation() == null) AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);

        if (isPotionActive(Effects.POISON)) removeActivePotionEffect(Effects.POISON);

//        if (ticksExisted == 1) {
//            System.out.println("Naga at " + getPosition());
//        }

        if (!world.isRemote) {
            if (getAttackTarget() != null && targetDistance < 29.5 && movement != EnumNagaMovement.FALLEN && movement != EnumNagaMovement.FALLING) {
                setAttacking(true);
                if (getAnimation() == NO_ANIMATION && swoopCooldown == 0 && rand.nextInt(80) == 0 && getPosY() - getAttackTarget().getPosY() > 0) {
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
                    List<ProjectileEntity> projectilesNearby = getEntitiesNearby(ProjectileEntity.class, 30);
                    for (ProjectileEntity a : projectilesNearby) {
                        Vector3d aActualMotion = new Vector3d(a.getPosX() - a.prevPosX, a.getPosY() - a.prevPosY, a.getPosZ() - a.prevPosZ);
                        if (aActualMotion.length() < 0.1 || a.ticksExisted <= 1) {
                            continue;
                        }

                        float dot = (float) a.getMotion().normalize().dotProduct(this.getPositionVec().subtract(a.getPositionVec()).normalize());
                        if (dot > 0.96) {
                            Vector3d dodgeVec = a.getMotion().crossProduct(new Vector3d(0, 1, 0)).normalize().scale(1.2);
                            Vector3d newPosLeft = getPositionVec().add(dodgeVec.scale(2));
                            Vector3d newPosRight = getPositionVec().add(dodgeVec.scale(-2));
                            Vector3d diffLeft = newPosLeft.subtract(a.getPositionVec());
                            Vector3d diffRight = newPosRight.subtract(a.getPositionVec());
                            if (diffRight.dotProduct(a.getMotion()) > diffLeft.dotProduct(a.getMotion())) {
                                dodgeVec = dodgeVec.scale(-1);
                            }
                            setMotion(getMotion().add(dodgeVec));
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
            if (onGround || isInLava() || isInWater()) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.LAND_ANIMATION);
                movement = EnumNagaMovement.FALLEN;
                onGroundTimer = GROUND_TIMER_MAX;
                setMotion(0, getMotion().y, 0);
                getNavigator().clearPath();
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
                float explodeSpeed = 2.4f;
                for (int i = 0; i < 25; i++) {
                    Vector3d particlePos = new Vector3d(0.25, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                    double value = rand.nextFloat() * 0.1f;
                    double life = rand.nextFloat() * 10f + 20f;
                    ParticleVanillaCloudExtended.spawnVanillaCloud(world, particlePos.x + mouthPos[0].x, particlePos.y + mouthPos[0].y, particlePos.z + mouthPos[0].z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                }
            }

            if (getAnimationTick() <= 15 && mouthPos != null && !interrupted) {
//            System.out.println(mouthPos);

                int howMany = 4;
                for (int i = 0; i < howMany; i++) {
                    Vector3d particlePos = new Vector3d(3, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                    double value = rand.nextFloat() * 0.15f;
                    ParticleVanillaCloudExtended.spawnVanillaCloudDestination(world, particlePos.x + mouthPos[0].x, particlePos.y + mouthPos[0].y, particlePos.z + mouthPos[0].z, 0, 0, 0, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.9, 15, mouthPos);
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
                world.playSound(getPosX(), getPosY(), getPosZ(), MMSounds.ENTITY_NAGA_FLAP_1.get(), SoundCategory.HOSTILE, 2, (float) (0.85 + rand.nextFloat() * 0.2), false);
                hasFlapSoundPlayed = true;
            }
        }

        hoverAnimFrac = hoverAnim.getAnimationProgressSinSqrt();
        flapAnimFrac = flapAnim.getAnimationProgressSinSqrt();

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
        }

//        setAttacking(true);
//        getNavigator().clearPath();
//        setMotion(new Vector3d(0, 0, 0));
//        setPosition(getPosX(), 10, getPosZ());
//
//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, TAIL_DEMO_ANIMATION);
//        }
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.NAGA.spawnConfig;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        boolean flag = super.canSpawn(world, reason);
        setPosition(getPosX(), getPosY() + 5, getPosZ());
        return flag && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean isNotColliding(IWorldReader worldIn) {
        boolean liquid = !this.world.containsAnyLiquid(this.getBoundingBox());
        boolean worldCollision = this.world.hasNoCollisions(this, this.getBoundingBox());
        boolean mobCollision = this.world.checkNoEntityCollision(this);

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
        if (flag && movement != EnumNagaMovement.FALLING && (isSpitting || isSwooping) && damage > 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.HURT_TO_FALL_ANIMATION);
            interrupted = true;
        }
        return flag;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        if (interrupted) return;
        Vector3d projectilePos = new Vector3d(1, -0.7, 0);
        projectilePos = projectilePos.rotateYaw((float)Math.toRadians(-rotationYaw - 90));
        projectilePos = projectilePos.add(getPositionVec());
        projectilePos = projectilePos.add(new Vector3d(0, 0, 1).rotatePitch((float)Math.toRadians(-rotationPitch)).rotateYaw((float)Math.toRadians(-rotationYawHead)));
        projectilePos = projectilePos.add(new Vector3d(0, 0, 0));
        EntityPoisonBall poisonBall = new EntityPoisonBall(EntityHandler.POISON_BALL, this.world, this);
        poisonBall.setPosition(projectilePos.x, projectilePos.y, projectilePos.z);
        Vector3d look = getLookVec();
        Vector3d dir = new Vector3d(look.x, 0, look.z).normalize();
        if (target != null) {
            float dy = (float) (projectilePos.y - target.getPosY());
            float dx = (float) (projectilePos.x - target.getPosX());
            float dz = (float) (projectilePos.z - target.getPosZ());
            float dist = (float) Math.sqrt(dx * dx + dz * dz);
            float timeGuess = (float) Math.sqrt(2 * dy / EntityPoisonBall.GRAVITY);
            float speed = Math.min(dist/timeGuess, 0.9f);
            poisonBall.shoot(dir.x * speed, 0.1, dir.z * speed, 1f, 0);
        }
        this.world.addEntity(poisonBall);

        playSound(MMSounds.ENTITY_NAGA_ACID_SPIT.get(), 2, 1);
        playSound(MMSounds.ENTITY_NAGA_ACID_SPIT_HISS.get(), 2, 1);
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
    public Animation[] getAnimations() {
        return new Animation[] {FLAP_ANIMATION, DODGE_ANIMATION, SWOOP_ANIMATION, SPIT_ANIMATION, HURT_TO_FALL_ANIMATION, LAND_ANIMATION, GET_UP_ANIMATION, DIE_AIR_ANIMATION, DIE_GROUND_ANIMATION, TAIL_DEMO_ANIMATION};
    }

    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        if (movement == EnumNagaMovement.FALLING) {
            return super.onLivingFall(distance, damageMultiplier);
        }
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
        if (movement == EnumNagaMovement.FALLING) {
            super.updateFallState(y, onGroundIn, state, pos);
        }
    }

    public void travel(Vector3d motion) {
        double d0 = 0.08D;
        ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        boolean flag = this.getMotion().y <= 0.0D;
//        if (flag && this.isPotionActive(Effects.SLOW_FALLING)) {
//            if (!gravity.hasModifier(SLOW_FALLING)) gravity.applyNonPersistentModifier(SLOW_FALLING);
//            this.fallDistance = 0.0F;
//        } else if (gravity.hasModifier(SLOW_FALLING)) {
//            gravity.removeModifier(SLOW_FALLING);
//        } TODO: SLOW_FALLING has private access. Skip?
        d0 = gravity.getValue();

        FluidState fluidstate = this.world.getFluidState(this.getPosition());
        if (this.isInWater() && this.func_241208_cS_() && !this.func_230285_a_(fluidstate.getFluid())) {
            double d8 = this.getPosY();
            float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
            float f6 = 0.02F;
            float f7 = (float) EnchantmentHelper.getDepthStriderModifier(this);
            if (f7 > 3.0F) {
                f7 = 3.0F;
            }

            if (!this.onGround) {
                f7 *= 0.5F;
            }

            if (f7 > 0.0F) {
                f5 += (0.54600006F - f5) * f7 / 3.0F;
                f6 += (this.getAIMoveSpeed() - f6) * f7 / 3.0F;
            }

            if (this.isPotionActive(Effects.DOLPHINS_GRACE)) {
                f5 = 0.96F;
            }

            f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
            this.moveRelative(f6, motion);
            this.move(MoverType.SELF, this.getMotion());
            Vector3d vector3d6 = this.getMotion();
            if (this.collidedHorizontally && this.isOnLadder()) {
                vector3d6 = new Vector3d(vector3d6.x, 0.2D, vector3d6.z);
            }

//            this.setMotion(vector3d6.mul(f5, 0.8F, f5));
            Vector3d vector3d2 = this.func_233626_a_(d0, flag, this.getMotion());
            this.setMotion(vector3d2);
            if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vector3d2.x, vector3d2.y + (double)0.6F - this.getPosY() + d8, vector3d2.z)) {
                this.setMotion(vector3d2.x, 0.3F, vector3d2.z);
            }
        } else if (this.isInLava() && this.func_241208_cS_() && !this.func_230285_a_(fluidstate.getFluid())) {
            double d7 = this.getPosY();
            this.moveRelative(0.02F, motion);
            this.move(MoverType.SELF, this.getMotion());
            if (this.func_233571_b_(FluidTags.LAVA) <= this.getFluidJumpHeight()) {
                this.setMotion(this.getMotion().mul(0.5D, 0.8F, 0.5D));
                Vector3d vector3d3 = this.func_233626_a_(d0, flag, this.getMotion());
                this.setMotion(vector3d3);
            } else {
                this.setMotion(this.getMotion().scale(0.5D));
            }

            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -d0 / 4.0D, 0.0D));
            }

            Vector3d vector3d4 = this.getMotion();
            if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vector3d4.x, vector3d4.y + (double) 0.6F - this.getPosY() + d7, vector3d4.z)) {
                this.setMotion(vector3d4.x, 0.3F, vector3d4.z);
            }
        }
        else if (movement == EnumNagaMovement.HOVERING) {
            BlockPos ground = new BlockPos(this.getPosX(), this.getBoundingBox().minY - 1.0D, this.getPosZ());
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            this.moveRelative(this.isOnGround() ? 0.1F * f1 : 0.02F, motion);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(f));

            MovementController entitymovehelper = this.getMoveHelper();
            double dx = entitymovehelper.getX() - this.getPosX();
            double dy = entitymovehelper.getY() - this.getPosY();
            double dz = entitymovehelper.getZ() - this.getPosZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest < 0.1 && getAnimation() == NO_ANIMATION) {
                setMotion(0, 0, 0);
            }
        } else if (movement == EnumNagaMovement.GLIDING) {
            Vector3d Vector3d3 = this.getMotion();
            if (Vector3d3.y > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vector3d Vector3d = this.getLookVec();
            float f6 = this.rotationPitch * ((float) Math.PI / 180F);
            double d9 = Math.sqrt(Vector3d.x * Vector3d.x + Vector3d.z * Vector3d.z);
            double d11 = Math.sqrt(horizontalMag(Vector3d3));
            double d12 = Vector3d.length();
            float f3 = MathHelper.cos(f6);
            f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
            Vector3d3 = this.getMotion().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);
            if (Vector3d3.y < 0.0D && d9 > 0.0D) {
                double d3 = Vector3d3.y * -0.1D * (double) f3;
                Vector3d3 = Vector3d3.add(Vector3d.x * d3 / d9, d3, Vector3d.z * d3 / d9);
            }

            if (f6 < 0.0F && d9 > 0.0D) {
                double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                Vector3d3 = Vector3d3.add(-Vector3d.x * d13 / d9, d13 * 3.2D, -Vector3d.z * d13 / d9);
            }

            if (d9 > 0.0D) {
                Vector3d3 = Vector3d3.add((Vector3d.x / d9 * d11 - Vector3d3.x) * 0.1D, 0.0D, (Vector3d.z / d9 * d11 - Vector3d3.z) * 0.1D);
            }

            this.setMotion(Vector3d3.mul(0.99F, 0.98F, 0.99F));
            this.move(MoverType.SELF, this.getMotion());
            if (getMoveHelper().getY() - getPosY() > 0 && getAnimation() == NO_ANIMATION)
                AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        } else if (movement == EnumNagaMovement.FALLING || movement == EnumNagaMovement.FALLEN || isAIDisabled()) {
            BlockPos blockpos = new BlockPos(this.getPosX(), this.getBoundingBox().minY - 1.0D, this.getPosZ());
            float f5 = this.world.getBlockState(blockpos).getSlipperiness(world, blockpos, this);
            float f7 = this.isOnGround() ? f5 * 0.91F : 0.91F;
//            this.moveRelative(this.isOnGround() ? this.getAIMoveSpeed() * (0.21600002F / (f5 * f5 * f5)) : this.jumpMovementFactor, motion);
            this.move(MoverType.SELF, this.getMotion());
            Vector3d Vector3d5 = this.getMotion();
            if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
                Vector3d5 = new Vector3d(Vector3d5.x, 0.2D, Vector3d5.z);
            }

            double d10 = Vector3d5.y;
            if (this.isPotionActive(Effects.LEVITATION)) {
                d10 += (0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - Vector3d5.y) * 0.2D;
                this.fallDistance = 0.0F;
            } else if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
                if (this.getPosY() > 0.0D) {
                    d10 = -0.1D;
                } else {
                    d10 = 0.0D;
                }
            } else if (!this.hasNoGravity()) {
                d10 -= d0;
            }

            this.setMotion(Vector3d5.x * (double)f7, d10 * (double)0.98F, Vector3d5.z * (double)f7);
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.getPosX() - this.prevPosX;
        double d6 = this.getPosZ() - this.prevPosZ;
        double d8 = this.getPosY() - this.prevPosY;
        float f8 = MathHelper.sqrt(d5 * d5 + d8 * d8 + d6 * d6) * 4.0F;
        if (f8 > 1.0F) {
            f8 = 1.0F;
        }

        this.limbSwingAmount += (f8 - this.limbSwingAmount) * 0.4F;
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
    protected ResourceLocation getLootTable() {
        return LootTableHandler.NAGA;
    }

    static class AILookAround extends Goal
    {
        private final EntityNaga parentEntity;

        public AILookAround(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexFlags(EnumSet.of(Flag.LOOK));
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
        public void tick()
        {
            if (this.parentEntity.getAttackTarget() == null)
            {
                Vector3d motion = this.parentEntity.getMotion();
                this.parentEntity.rotationYaw = -((float)MathHelper.atan2(motion.x, motion.z)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
            else
            {
                LivingEntity entitylivingbase = this.parentEntity.getAttackTarget();
                if (entitylivingbase.getDistanceSq(this.parentEntity) < 1600.0D)
                {
                    double d1 = entitylivingbase.getPosX() - this.parentEntity.getPosX();
                    double d2 = entitylivingbase.getPosZ() - this.parentEntity.getPosZ();
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }
        }
    }

    static class AIRandomFly extends Goal
    {
        private final EntityNaga parentEntity;

        public AIRandomFly(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            MovementController entitymovehelper = this.parentEntity.getMoveHelper();

            if (parentEntity.getAttacking()) return false;

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - this.parentEntity.getPosX();
                double d1 = entitymovehelper.getY() - this.parentEntity.getPosY();
                double d2 = entitymovehelper.getZ() - this.parentEntity.getPosZ();
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
            double d0 = this.parentEntity.getPosX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 24.0F);
            double d1 = this.parentEntity.getPosY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.getPosZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 24.0F);
            if (!parentEntity.world.hasWater(new BlockPos(d0, d1, d2)))
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, parentEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
        }
    }

    static class AIFlyAroundTarget extends Goal
    {
        private final EntityNaga parentEntity;

        public AIFlyAroundTarget(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            MovementController entitymovehelper = this.parentEntity.getMoveHelper();

            if (parentEntity.getAttackTarget() != null) {
                if (!entitymovehelper.isUpdating()) {
                    return true;
                } else {
                    double dx = entitymovehelper.getX() - this.parentEntity.getPosX();
                    double dy = entitymovehelper.getY() - this.parentEntity.getPosY();
                    double dz = entitymovehelper.getZ() - this.parentEntity.getPosZ();
                    double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    LivingEntity target = parentEntity.getAttackTarget();
                    double dx2 = entitymovehelper.getX() - target.getPosX();
                    double dy2 = entitymovehelper.getY() - target.getPosY();
                    double dz2 = entitymovehelper.getZ() - target.getPosZ();
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
            LivingEntity target = parentEntity.getAttackTarget();
            float yaw = (float) (random.nextFloat() * Math.PI * 2);
            float radius = 16;
            double d0 = target.getPosX() + Math.cos(yaw) * radius;
            double d1 = target.getPosY() + 8 + random.nextFloat() * 5;
            double d2 = target.getPosZ() + Math.sin(yaw) * radius;
//            while (parentEntity.world.hasWater(new BlockPos(d0, d1, d2))) {
//                d1 += 1;
//            }
            double speed = parentEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            if (!parentEntity.world.hasWater(new BlockPos(d0, d1, d2)))
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, speed);
        }
    }

    static class NagaMoveHelper extends MovementController
    {
        private final EntityNaga parentEntity;
        private int courseChangeCooldown;

        public NagaMoveHelper(EntityNaga naga)
        {
            super(naga);
            this.parentEntity = naga;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    Vector3d Vector3d = new Vector3d(this.posX - this.parentEntity.getPosX(), this.posY - this.parentEntity.getPosY(), this.posZ - this.parentEntity.getPosZ());
                    double d0 = Vector3d.length();
                    Vector3d = Vector3d.normalize();
                    if (this.checkCollisions(Vector3d, MathHelper.ceil(d0))) {
                        this.parentEntity.setMotion(this.parentEntity.getMotion().add(Vector3d.scale(0.1D)));
                    } else {
                        this.action = MovementController.Action.WAIT;
                    }
                }

            }
        }

        public boolean checkCollisions(Vector3d p_220673_1_, int p_220673_2_) {
            AxisAlignedBB axisalignedbb = this.parentEntity.getBoundingBox();

            for(int i = 1; i < p_220673_2_; ++i) {
                axisalignedbb = axisalignedbb.offset(p_220673_1_);
                if (!this.parentEntity.world.hasNoCollisions(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }

            return true;
        }
    }

    public class FlyOutOfWaterGoal extends Goal {
        private final EntityNaga entity;

        public FlyOutOfWaterGoal(EntityNaga entityIn) {
            this.entity = entityIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return this.entity.isInWater() && this.entity.func_233571_b_(FluidTags.WATER) > this.entity.getFluidJumpHeight() || this.entity.isInLava();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (entity.getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityNaga.FLAP_ANIMATION);

        }
    }

}
