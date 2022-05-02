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
import com.bobmowzie.mowziesmobs.server.util.MowzieMathUtil;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

/**
 * Created by BobMowzie on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity implements RangedAttackMob, Enemy, FlyingAnimal {
    @OnlyIn(Dist.CLIENT)
    public DynamicChain dc;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] mouthPos;

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

    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(EntityNaga.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> BANKING = SynchedEntityData.defineId(EntityNaga.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PREV_BANKING = SynchedEntityData.defineId(EntityNaga.class, EntityDataSerializers.FLOAT);

    public static int MAX_DIST_FROM_HOME = 50;

    private final ControlledAnimation hoverAnim = new ControlledAnimation(10);
    private final ControlledAnimation flapAnim = new ControlledAnimation(10);
    public float hoverAnimFrac;
    public float prevHoverAnimFrac;
    public float flapAnimFrac;
    public float prevFlapAnimFrac;

    private boolean hasFlapSoundPlayed = false;
    @OnlyIn(Dist.CLIENT)
    public float shoulderRot;

    @OnlyIn(Dist.CLIENT)
    public float banking;
    @OnlyIn(Dist.CLIENT)
    public float prevBanking;

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

    public EntityNaga(EntityType<? extends EntityNaga> type, Level world) {
        super(type, world);
        if (world.isClientSide) {
            dc = new DynamicChain(this);
            mouthPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
        this.xpReward = 10;

        this.moveControl = new NagaMoveHelper(this);
        this.lookControl = new NagaLookController(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WALKABLE, -1.0F);
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EntityNaga.FlyOutOfWaterGoal(this));
        this.goalSelector.addGoal(5, new EntityNaga.WanderGoal());
        this.goalSelector.addGoal(4, new EntityNaga.AIFlyAroundTarget(this));
        this.goalSelector.addGoal(3, new EntityNaga.AIFlyTowardsTarget(this));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(this, Player.class, 0, true, true, target -> target.blockPosition().closerThan(getRestrictCenter(), getRestrictRadius())) {
            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && getTarget() != null && getTarget().blockPosition().closerThan(getRestrictCenter(), getRestrictRadius()) && getAnimation() == NO_ANIMATION;
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, FLAP_ANIMATION, false) {
            @Override
            public void start() {
                super.start();
                playSound(MMSounds.ENTITY_NAGA_FLAP_1.get(), 2, (float) (0.85 + random.nextFloat() * 0.2));
            }

            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() >= 4 && getAnimationTick() <= 9) {
                    setDeltaMovement(getDeltaMovement().add(0, 0.1, 0));
                }
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, DODGE_ANIMATION, false));
        this.goalSelector.addGoal(2, new AnimationProjectileAttackAI<EntityNaga>(this, SPIT_ANIMATION, 30, null) {
            @Override
            public void start() {
                super.start();
                playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE.get(), 2, 1);
            }

            @Override
            public void tick() {
                super.tick();
                if (interrupted) return;
                //if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE, 2, 1);
                if (getAnimationTick() < 9) setDeltaMovement(getDeltaMovement().add(0, 0.015, 0));
//                if (getAnimationTick() == 28) motionY -= 0.2;
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, SWOOP_ANIMATION, true) {
            @Override
            public void start() {
                super.start();
                playSound(MMSounds.ENTITY_NAGA_FLAP_1.get(), 2, 0.7f);
            }

            @Override
            public void tick() {
                super.tick();
                if (interrupted) return;

                Vec3 v = new Vec3(0, 0, 0);

                int phase1Length = 15;
                int phase2Length = 21;
                LivingEntity target = getTarget();
                if (getAnimationTick() < phase1Length) {
                    if (target != null) {
                        entity.lookAt(target, 100, 100);
                        entity.lookControl.setLookAt(target, 30F, 30F);
                    }
                }
                if (getAnimationTick() < 23 + phase2Length) {
                    if (getAnimationTick() >= 1 && getAnimationTick() < 1 + phase1Length) {
                        float frame = (getAnimationTick() - 1) / (float) phase1Length;
                        v = v.add(new Vec3(
                                1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                -1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                0.8f * Math.sin(frame * Math.PI * 2)
                        ));
                    } else if (getAnimationTick() >= 16) {
                        if (getAnimationTick() == 23) {
                            if (target != null) {
                                swoopTargetCorrectY = 0.09f * (float) Math.abs(getY() - target.getY());
                                swoopTargetCorrectX = 0.1f * (float) Math.sqrt((getX() - target.getX()) * (getX() - target.getX()) + (getZ() - target.getZ()) * (getZ() - target.getZ()));
                                if (swoopTargetCorrectX > 1.8f) swoopTargetCorrectX = 1.8f;
                                if (swoopTargetCorrectY > 2f) swoopTargetCorrectY = 2f;
                            }
                            else {
                                swoopTargetCorrectX = swoopTargetCorrectY = 1;
                            }
                        }
                        if (getAnimationTick() >= 23 && getAnimationTick() < 23 + phase2Length) {
                            float frame = (getAnimationTick() - 23) / (float) phase2Length;
                            v = v.add(new Vec3(
                                    swoopTargetCorrectX * 1.4 * (1 - Math.exp(2 * (frame - 1))),
                                    swoopTargetCorrectY * -1.5 * (Math.cos(frame * Math.PI) * (1 - Math.exp(7 * (frame - 1)))),
                                    0
                            ));

                            List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(4, 4, 4, 4);
                            for (LivingEntity entityHit : entitiesHit) {
                                if (entityHit instanceof EntityNaga) continue;
                                doHurtTarget(entityHit);
                            }
                        }
                    }

                    v = v.yRot((float) Math.toRadians(-getYRot() - 90));
                    setDeltaMovement(v.x, v.y, v.z);
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
            public void start() {
                super.start();
                playSound(MMSounds.MISC_GROUNDHIT_2.get(), 1.5f, 1);
            }
        });
        this.goalSelector.addGoal(1, new SimpleAnimationAI<EntityNaga>(this, GET_UP_ANIMATION, true) {
            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 13) playSound(MMSounds.ENTITY_NAGA_FLAP_1.get(), 2f, 1);

                if (getAnimationTick() == 15) {
                    setDeltaMovement(getDeltaMovement().add(0, 1.6, 0));
                }
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityNaga>(this, TAIL_DEMO_ANIMATION, false));
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(false);
        flyingpathnavigator.setCanPassDoors(false);
        return flyingpathnavigator;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(ATTACKING, false);
        getEntityData().define(BANKING, 0.0f);
        getEntityData().define(PREV_BANKING, 0.0f);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 16600;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(12.0D);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 12.0D)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() != NO_ANIMATION) return null;
        int r = random.nextInt(4);
        if (r == 0) {
            playSound(MMSounds.ENTITY_NAGA_ROAR.get(random.nextInt(4)).get(), 5, 1);
            roarAnimation = 0;
        }
        else if (r <= 2) {
            playSound(MMSounds.ENTITY_NAGA_GROWL.get(random.nextInt(3)).get(), 4, 1);
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        playSound(MMSounds.ENTITY_NAGA_GRUNT.get(random.nextInt(3)).get(), 2, 1);
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        playSound(MMSounds.ENTITY_NAGA_ROAR.get(random.nextInt(4)).get(), 3, 1);
        return null;
    }

    @Override
    public void tick() {
        prevMotionX = getDeltaMovement().x;
        prevMotionY = getDeltaMovement().y;
        prevMotionZ = getDeltaMovement().z;
        prevHoverAnimFrac = hoverAnimFrac;
        prevFlapAnimFrac = flapAnimFrac;

        super.tick();
//        setDead();
        yBodyRot = getYRot();
        if (level.isClientSide()) {
            banking = getBanking();
            prevBanking = getPrevBanking();
        }

        if (spitCooldown > 0) spitCooldown--;
        if (swoopCooldown > 0) swoopCooldown--;
        if (onGroundTimer > 0) onGroundTimer--;
        if (roarAnimation < ROAR_DURATION) roarAnimation++;

        if (getAnimation() == null) AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);

        if (hasEffect(MobEffects.POISON)) removeEffectNoUpdate(MobEffects.POISON);

//        if (ticksExisted == 1) {
//            System.out.println("Naga at " + getPosition());
//        }

        if (!level.isClientSide) {
            if (getTarget() != null && targetDistance < 29.5 && movement != EnumNagaMovement.FALLEN && movement != EnumNagaMovement.FALLING) {
                setAttacking(true);
                if (getAnimation() == NO_ANIMATION && swoopCooldown == 0 && random.nextInt(80) == 0 && getY() - getTarget().getY() > 0) {
                    interrupted = false;
//                    System.out.println("Swoop");
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SWOOP_ANIMATION);
                    swoopCooldown = SWOOP_COOLDOWN_MAX;
                }
                else if (getAnimation() == NO_ANIMATION && spitCooldown == 0 && random.nextInt(80) == 0) {
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

                if (getAnimation() == NO_ANIMATION && !level.isClientSide) {
                    List<Projectile> projectilesNearby = getEntitiesNearby(Projectile.class, 30);
                    for (Projectile a : projectilesNearby) {
                        Vec3 aActualMotion = new Vec3(a.getX() - a.xo, a.getY() - a.yo, a.getZ() - a.zo);
                        if (aActualMotion.length() < 0.1 || a.tickCount <= 1) {
                            continue;
                        }

                        float dot = (float) a.getDeltaMovement().normalize().dot(this.position().subtract(a.position()).normalize());
                        if (dot > 0.96) {
                            Vec3 dodgeVec = a.getDeltaMovement().cross(new Vec3(0, 1, 0)).normalize().scale(1.2);
                            Vec3 newPosLeft = position().add(dodgeVec.scale(2));
                            Vec3 newPosRight = position().add(dodgeVec.scale(-2));
                            Vec3 diffLeft = newPosLeft.subtract(a.position());
                            Vec3 diffRight = newPosRight.subtract(a.position());
                            if (diffRight.dot(a.getDeltaMovement()) > diffLeft.dot(a.getDeltaMovement())) {
                                dodgeVec = dodgeVec.scale(-1);
                            }
                            setDeltaMovement(getDeltaMovement().add(dodgeVec));
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
                setDeltaMovement(0, getDeltaMovement().y, 0);
                getNavigation().stop();
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
            setYRot(yRotO);
            yHeadRot = yHeadRotO;
            setXRot(xRotO);
        }
        else if (movement == EnumNagaMovement.FALLING) {
            flapAnim.decreaseTimer();
            hoverAnim.increaseTimer();
        }
        else {
            flapAnim.increaseTimer();
        }

        if (getAnimation() == SPIT_ANIMATION && level.isClientSide && mouthPos != null && !interrupted) {
            if (getAnimationTick() == 33) {
//            System.out.println(mouthPos);
                float explodeSpeed = 2.4f;
                for (int i = 0; i < 25; i++) {
                    Vec3 particlePos = new Vec3(0.25, 0, 0);
                    particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                    double value = random.nextFloat() * 0.1f;
                    double life = random.nextFloat() * 10f + 20f;
                    ParticleVanillaCloudExtended.spawnVanillaCloud(level, particlePos.x + mouthPos[0].x, particlePos.y + mouthPos[0].y, particlePos.z + mouthPos[0].z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                }
            }

            if (getAnimationTick() <= 15 && mouthPos != null && !interrupted) {
//            System.out.println(mouthPos);

                int howMany = 4;
                for (int i = 0; i < howMany; i++) {
                    Vec3 particlePos = new Vec3(3, 0, 0);
                    particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                    double value = random.nextFloat() * 0.15f;
                    ParticleVanillaCloudExtended.spawnVanillaCloudDestination(level, particlePos.x + mouthPos[0].x, particlePos.y + mouthPos[0].y, particlePos.z + mouthPos[0].z, 0, 0, 0, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.9, 15, mouthPos);
                }
            }
        }

        if (getAnimation() == HURT_TO_FALL_ANIMATION && getAnimationTick() == 17) {
            movement = EnumNagaMovement.FALLING;
        }

        if (getAnimation() == GET_UP_ANIMATION && getAnimationTick() == 26) {
            movement = EnumNagaMovement.HOVERING;
        }

        if (level.isClientSide && movement == EnumNagaMovement.HOVERING && flapAnim.getAnimationFraction() >= 0.5) {

            if (shoulderRot > 0.9) hasFlapSoundPlayed = false;

            if (shoulderRot <= 0.7 && !hasFlapSoundPlayed) {
                level.playLocalSound(getX(), getY(), getZ(), MMSounds.ENTITY_NAGA_FLAP_1.get(), SoundSource.HOSTILE, 2, (float) (0.85 + random.nextFloat() * 0.2), false);
                hasFlapSoundPlayed = true;
            }
        }

        hoverAnimFrac = hoverAnim.getAnimationProgressSinSqrt();
        flapAnimFrac = flapAnim.getAnimationProgressSinSqrt();

        if (!this.level.isClientSide && this.level.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.discard() ;
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        restrictTo(this.blockPosition(), MAX_DIST_FROM_HOME);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.NAGA.spawnConfig;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.NAGA.combatConfig;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, MobSpawnType reason) {
        boolean flag = super.checkSpawnRules(world, reason);
        setPos(getX(), getY() + 5, getZ());
        return flag && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        boolean liquid = !this.level.containsAnyLiquid(this.getBoundingBox());
        boolean worldCollision = this.level.noCollision(this, this.getBoundingBox());
        boolean mobCollision = this.level.isUnobstructed(this);

        return liquid && worldCollision && mobCollision;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (deathTime == 15 && movement != EnumNagaMovement.FALLEN) movement = EnumNagaMovement.FALLING;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        boolean flag = super.hurt(source, damage);
        boolean isSpitting = getAnimation() == SPIT_ANIMATION && getAnimationTick() < 30;
        boolean isSwooping = getAnimation() == SWOOP_ANIMATION && getAnimationTick() < 25;
        if (flag && movement != EnumNagaMovement.FALLING && (isSpitting || isSwooping) && damage > 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.HURT_TO_FALL_ANIMATION);
            interrupted = true;
        }
        return flag;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (interrupted) return;
        Vec3 projectilePos = new Vec3(1, -0.7, 0);
        projectilePos = projectilePos.yRot((float)Math.toRadians(-getYRot() - 90));
        projectilePos = projectilePos.add(position());
        projectilePos = projectilePos.add(new Vec3(0, 0, 1).xRot((float)Math.toRadians(-getXRot())).yRot((float)Math.toRadians(-yHeadRot)));
        projectilePos = projectilePos.add(new Vec3(0, 0, 0));
        EntityPoisonBall poisonBall = new EntityPoisonBall(EntityHandler.POISON_BALL.get(), this.level, this);
        poisonBall.setPos(projectilePos.x, projectilePos.y, projectilePos.z);
        Vec3 look = getLookAngle();
        Vec3 dir = new Vec3(look.x, 0, look.z).normalize();
        if (target != null) {
            float dy = (float) (projectilePos.y - target.getY());
            float dx = (float) (projectilePos.x - target.getX());
            float dz = (float) (projectilePos.z - target.getZ());
            float dist = (float) Math.sqrt(dx * dx + dz * dz);
            float timeGuess = (float) Math.sqrt(2 * dy / EntityPoisonBall.GRAVITY);
            float speed = Math.min(dist/timeGuess, 0.9f);
            poisonBall.shoot(dir.x * speed, 0.1, dir.z * speed, 1f, 0);
        }
        this.level.addFreshEntity(poisonBall);

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

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (movement == EnumNagaMovement.FALLING) {
            return super.causeFallDamage(distance, damageMultiplier, source);
        }
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
        if (movement == EnumNagaMovement.FALLING) {
            super.checkFallDamage(y, onGroundIn, state, pos);
        }
    }

    public void travel(Vec3 motion) {
        double d0 = 0.08D;
        AttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        boolean flag = this.getDeltaMovement().y <= 0.0D;
//        if (flag && this.isPotionActive(Effects.SLOW_FALLING)) {
//            if (!gravity.hasModifier(SLOW_FALLING)) gravity.applyNonPersistentModifier(SLOW_FALLING);
//            this.fallDistance = 0.0F;
//        } else if (gravity.hasModifier(SLOW_FALLING)) {
//            gravity.removeModifier(SLOW_FALLING);
//        } TODO: SLOW_FALLING has private access. Skip?
        d0 = gravity.getValue();

        FluidState fluidstate = this.level.getFluidState(this.blockPosition());
        if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
            double d8 = this.getY();
            float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
            float f6 = 0.02F;
            float f7 = (float) EnchantmentHelper.getDepthStrider(this);
            if (f7 > 3.0F) {
                f7 = 3.0F;
            }

            if (!this.onGround) {
                f7 *= 0.5F;
            }

            if (f7 > 0.0F) {
                f5 += (0.54600006F - f5) * f7 / 3.0F;
                f6 += (this.getSpeed() - f6) * f7 / 3.0F;
            }

            if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                f5 = 0.96F;
            }

            f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
            this.moveRelative(f6, motion);
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 vector3d6 = this.getDeltaMovement();
            if (this.horizontalCollision && this.onClimbable()) {
                vector3d6 = new Vec3(vector3d6.x, 0.2D, vector3d6.z);
            }

//            this.setMotion(vector3d6.mul(f5, 0.8F, f5));
            Vec3 vector3d2 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
            this.setDeltaMovement(vector3d2);
            if (this.horizontalCollision && this.isFree(vector3d2.x, vector3d2.y + (double)0.6F - this.getY() + d8, vector3d2.z)) {
                this.setDeltaMovement(vector3d2.x, 0.3F, vector3d2.z);
            }
        } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
            double d7 = this.getY();
            this.moveRelative(0.02F, motion);
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, 0.8F, 0.5D));
                Vec3 vector3d3 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                this.setDeltaMovement(vector3d3);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            }

            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
            }

            Vec3 vector3d4 = this.getDeltaMovement();
            if (this.horizontalCollision && this.isFree(vector3d4.x, vector3d4.y + (double) 0.6F - this.getY() + d7, vector3d4.z)) {
                this.setDeltaMovement(vector3d4.x, 0.3F, vector3d4.z);
            }
        }
        else if (movement == EnumNagaMovement.HOVERING) {
            BlockPos ground = new BlockPos(this.getX(), this.getBoundingBox().minY - 1.0D, this.getZ());
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.level.getBlockState(ground).getFriction(level, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.level.getBlockState(ground).getFriction(level, ground, this) * 0.91F;
            }

            this.moveRelative(this.isOnGround() ? 0.1F * f1 : 0.02F, motion);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(f));

            BlockPos destination = this.getNavigation().getTargetPos();
            if (destination != null) {
                double dx = destination.getX() - this.getX();
                double dy = destination.getY() - this.getY();
                double dz = destination.getZ() - this.getZ();
                double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distanceToDest < 0.1 && getAnimation() == NO_ANIMATION) {
                    setDeltaMovement(0, 0, 0);
                }
            }
        } else if (movement == EnumNagaMovement.GLIDING) {
            Vec3 vec3 = this.getDeltaMovement();
            if (vec3.y > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vec3 moveDirection = this.getLookAngle();
            moveDirection = moveDirection.normalize();
            float f6 = this.getXRot() * ((float) Math.PI / 180F);
            double d9 = Math.sqrt(moveDirection.x * moveDirection.x + moveDirection.z * moveDirection.z);
            double d11 = Math.sqrt(vec3.horizontalDistanceSqr());
            double d12 = moveDirection.length();
            float f3 = Mth.cos(f6);
            f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
            vec3 = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);
            if (vec3.y < 0.0D && d9 > 0.0D) {
                double d3 = vec3.y * -0.1D * (double) f3;
                vec3 = vec3.add(moveDirection.x * d3 / d9, d3, moveDirection.z * d3 / d9);
            }

            if (f6 < 0.0F && d9 > 0.0D) {
                double d13 = d11 * (double) (-Mth.sin(f6)) * 0.04D;
                vec3 = vec3.add(-moveDirection.x * d13 / d9, d13 * 3.2D, -moveDirection.z * d13 / d9);
            }

            if (d9 > 0.0D) {
                vec3 = vec3.add((moveDirection.x / d9 * d11 - vec3.x) * 0.1D, 0.0D, (moveDirection.z / d9 * d11 - vec3.z) * 0.1D);
            }

            this.setDeltaMovement(vec3.multiply(0.99F, 0.98F, 0.99F));
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (moveDirection.y() < 0 && getAnimation() == NO_ANIMATION)
                AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        } else if (movement == EnumNagaMovement.FALLING || movement == EnumNagaMovement.FALLEN || isNoAi()) {
            BlockPos blockpos = new BlockPos(this.getX(), this.getBoundingBox().minY - 1.0D, this.getZ());
            float f5 = this.level.getBlockState(blockpos).getFriction(level, blockpos, this);
            float f7 = this.isOnGround() ? f5 * 0.91F : 0.91F;

            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 Vector3d5 = this.getDeltaMovement();
            if ((this.horizontalCollision || this.jumping) && this.onClimbable()) {
                Vector3d5 = new Vec3(Vector3d5.x, 0.2D, Vector3d5.z);
            }

            double d10 = Vector3d5.y;
            if (this.hasEffect(MobEffects.LEVITATION)) {
                d10 += (0.05D * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - Vector3d5.y) * 0.2D;
                this.fallDistance = 0.0F;
            } else if (this.level.isClientSide && !this.level.hasChunkAt(blockpos)) {
                if (this.getY() > 0.0D) {
                    d10 = -0.1D;
                } else {
                    d10 = 0.0D;
                }
            } else if (!this.isNoGravity()) {
                d10 -= d0;
            }

            this.setDeltaMovement(Vector3d5.x * (double)f7, d10 * (double)0.98F, Vector3d5.z * (double)f7);
        }

        this.animationSpeedOld = this.animationSpeed;
        double d5 = this.getX() - this.xo;
        double d6 = this.getZ() - this.zo;
        double d8 = this.getY() - this.yo;
        float f8 = Mth.sqrt((float) (d5 * d5 + d8 * d8 + d6 * d6)) * 4.0F;
        if (f8 > 1.0F) {
            f8 = 1.0F;
        }

        this.animationSpeed += (f8 - this.animationSpeed) * 0.4F;
        this.animationPosition += this.animationSpeed;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean onClimbable()
    {
        return false;
    }

    public boolean getAttacking() {
        return getEntityData().get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        getEntityData().set(ATTACKING, attacking);
    }

    public float getBanking() {
        return getEntityData().get(BANKING);
    }

    public void setBanking(float banking) {
        getEntityData().set(BANKING, banking);
    }

    public float getPrevBanking() {
        return getEntityData().get(PREV_BANKING);
    }

    public void setPrevBanking(float prevBanking) {
        getEntityData().set(PREV_BANKING, prevBanking);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HomePosX", this.getRestrictCenter().getX());
        compound.putInt("HomePosY", this.getRestrictCenter().getY());
        compound.putInt("HomePosZ", this.getRestrictCenter().getZ());
        compound.putInt("HomeDist", (int) this.getRestrictRadius());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        int dist = compound.getInt("HomeDist");
        this.restrictTo(new BlockPos(i, j, k), dist);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LootTableHandler.NAGA;
    }

    static class AILookAround extends Goal
    {
        private final EntityNaga parentEntity;

        public AILookAround(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canUse()
        {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick()
        {
            if (this.parentEntity.getTarget() == null)
            {
                Vec3 motion = this.parentEntity.getDeltaMovement();
                this.parentEntity.setYRot(-((float)Mth.atan2(motion.x, motion.z)) * (180F / (float)Math.PI));
                this.parentEntity.yBodyRot = this.parentEntity.getYRot();
            }
            else
            {
                LivingEntity entitylivingbase = this.parentEntity.getTarget();
                if (entitylivingbase.distanceToSqr(this.parentEntity) < 1600.0D)
                {
                    double d1 = entitylivingbase.getX() - this.parentEntity.getX();
                    double d2 = entitylivingbase.getZ() - this.parentEntity.getZ();
                    this.parentEntity.setYRot(-((float)Mth.atan2(d1, d2)) * (180F / (float)Math.PI));
                    this.parentEntity.yBodyRot = this.parentEntity.getYRot();
                }
            }
        }
    }

    class WanderGoal extends Goal {
        private boolean seesGround;

        WanderGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return EntityNaga.this.navigation.isDone() && getTarget() == null;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return EntityNaga.this.navigation.isInProgress() && !EntityNaga.this.navigation.getTargetPos().closerThan(EntityNaga.this.blockPosition(), 4) && getTarget() == null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            Vec3 vector3d = this.getRandomLocation();
            if (vector3d != null) {
                EntityNaga.this.navigation.setMaxVisitedNodesMultiplier(0.5F);
                EntityNaga.this.navigation.moveTo(EntityNaga.this.navigation.createPath(new BlockPos(vector3d), 1), 1.0D);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (!seesGround) {
                Vec3 newLocation = getRandomLocation();
                if (newLocation != null && seesGround) {
                    EntityNaga.this.navigation.moveTo(EntityNaga.this.navigation.createPath(new BlockPos(newLocation), 1), 1.0D);
                }
            }
        }

        @Override
        public void stop() {
            super.stop();
            EntityNaga.this.getNavigation().stop();
        }

        @Nullable
        private Vec3 getRandomLocation() {
            Vec3 vector3d;
//            if (EntityNaga.this.isHiveValid() && !EntityNaga.this.isWithinDistance(EntityNaga.this.hivePos, 22)) {
//                Vector3d vector3d1 = Vector3d.copyCentered(EntityNaga.this.hivePos);
//                vector3d = vector3d1.subtract(EntityNaga.this.getPositionVec()).normalize();
//            } else {
                vector3d = EntityNaga.this.getViewVector(0.0F);
//            }
            Vec3 position = HoverRandomPos.getPos(EntityNaga.this, 24, 24, vector3d.x, vector3d.z, ((float)Math.PI / 2F), 8, 18);
            if (position == null) {
                Vec3 sumPos = position().add(vector3d);
                position = AirAndWaterRandomPos.getPos(EntityNaga.this, 24, 8, -8, sumPos.x, sumPos.z, ((float)Math.PI / 2F));
                seesGround = false;
            }
            else {
                seesGround = true;
            }
            if (position == null || !level.isEmptyBlock(new BlockPos(position).below())) return null;
            Vec3 offset = position.subtract(EntityNaga.this.position());
            AABB newBB = EntityNaga.this.getBoundingBox().move(offset);
            if (level.noCollision(newBB) && EntityNaga.this.level.clip(new ClipContext(EntityNaga.this.position(), position, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, EntityNaga.this)).getType() != HitResult.Type.BLOCK)
                return position;
            return null;
        }
    }

    class AIFlyAroundTarget extends Goal
    {
        private final EntityNaga parentEntity;

        public AIFlyAroundTarget(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canUse()
        {
            if (parentEntity.getTarget() != null) {
                if (this.parentEntity.getNavigation().isDone()) {
                    return parentEntity.random.nextInt(60) == 0;
                }
            }
            return false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse()
        {
            if (EntityNaga.this.getTarget() == null) return false;
            BlockPos navigatorPos = this.parentEntity.getNavigation().getTargetPos();
            if (navigatorPos == null) {
                return false;
            }
            double dx = navigatorPos.getX() - this.parentEntity.getX();
            double dy = navigatorPos.getY() - this.parentEntity.getY();
            double dz = navigatorPos.getZ() - this.parentEntity.getZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest > 60.D) return false;

            LivingEntity target = parentEntity.getTarget();
            double dx2 = navigatorPos.getX() - target.getX();
            double dy2 = navigatorPos.getY() - target.getY();
            double dz2 = navigatorPos.getZ() - target.getZ();
            double distanceDestToTarget = Math.sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2);
            if (distanceDestToTarget > 20.D || distanceDestToTarget < 5) return false;

            return EntityNaga.this.navigation.isInProgress() && !EntityNaga.this.navigation.getTargetPos().closerThan(EntityNaga.this.blockPosition(), 1);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start()
        {
            Random random = this.parentEntity.getRandom();
            LivingEntity target = parentEntity.getTarget();
            float yaw = (float) (random.nextFloat() * Math.PI * 2);
            float radius = 16;
            double d0 = target.getX() + Math.cos(yaw) * radius;
            double d1 = target.getY() + 8 + random.nextFloat() * 5;
            double d2 = target.getZ() + Math.sin(yaw) * radius;
//            while (parentEntity.world.hasWater(new BlockPos(d0, d1, d2))) {
//                d1 += 1;
//            }
            double speed = parentEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            if (!parentEntity.level.isWaterAt(new BlockPos(d0, d1, d2)))
                this.parentEntity.getNavigation().moveTo(d0, d1, d2, speed);
        }

        @Override
        public void stop() {
            super.stop();
            EntityNaga.this.getNavigation().stop();
        }
    }

    class AIFlyTowardsTarget extends Goal
    {
        private final EntityNaga parentEntity;

        public AIFlyTowardsTarget(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canUse()
        {
            return parentEntity.getTarget() != null && EntityNaga.this.distanceToSqr(this.parentEntity.getTarget()) >= 870.25D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse()
        {
            if (EntityNaga.this.getTarget() == null) return false;
            if (EntityNaga.this.getTarget().distanceToSqr(this.parentEntity) <= 500.0D) return false;
            BlockPos navigatorPos = this.parentEntity.getNavigation().getTargetPos();
            if (navigatorPos == null) {
                return false;
            }
            double dx = navigatorPos.getX() - this.parentEntity.getX();
            double dy = navigatorPos.getY() - this.parentEntity.getY();
            double dz = navigatorPos.getZ() - this.parentEntity.getZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest > 60.D) return false;

            return EntityNaga.this.navigation.isInProgress();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = parentEntity.getTarget();
            double speed = parentEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            BlockPos targetPos = target.blockPosition().above(8);
            if (!parentEntity.level.isWaterAt(targetPos))
                this.parentEntity.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
        }

        @Override
        public void stop() {
            super.stop();
            EntityNaga.this.getNavigation().stop();
        }
    }

    public class FlyOutOfWaterGoal extends Goal {
        private final EntityNaga entity;

        public FlyOutOfWaterGoal(EntityNaga entityIn) {
            this.entity = entityIn;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.entity.isInWater() && this.entity.getFluidHeight(FluidTags.WATER) > this.entity.getFluidJumpThreshold() || this.entity.isInLava();
        }

        @Override
        public void start() {
            if (entity.getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityNaga.FLAP_ANIMATION);
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }
    }

    class NagaMoveHelper extends MoveControl
    {
        private final EntityNaga parentEntity;
        private int courseChangeCooldown;

        private float speedFactor = 0.1F;

        public NagaMoveHelper(EntityNaga naga)
        {
            super(naga);
            this.parentEntity = naga;
        }

        public void tick() {
            speedFactor = 1;

            if (movement == EnumNagaMovement.GLIDING) {
                if (this.operation == Operation.MOVE_TO) {
                    if (EntityNaga.this.horizontalCollision) {
                        EntityNaga naga = EntityNaga.this;
                        naga.setYRot(naga.getYRot() + 180.0F);
                        this.speedFactor = 0.1F;
                        getNavigation().stop();
                    }

                    float orbitOffsetDiffX = (float) (getNavigation().getTargetPos().getX() - EntityNaga.this.getX());
                    float orbitOffsetDiffY = (float) (getNavigation().getTargetPos().getY() - EntityNaga.this.getY());
                    float orbitOffsetDiffZ = (float) (getNavigation().getTargetPos().getZ() - EntityNaga.this.getZ());
                    double horizontalDistToOrbitOffset = (double) Mth.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ);
                    double yFractionReduction = 1.0D - (double) Mth.abs(orbitOffsetDiffY * 0.7F) / horizontalDistToOrbitOffset;
                    orbitOffsetDiffX = (float) ((double) orbitOffsetDiffX * yFractionReduction);
                    orbitOffsetDiffZ = (float) ((double) orbitOffsetDiffZ * yFractionReduction);
                    horizontalDistToOrbitOffset = (double) Mth.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ);
                    double distToOrbitOffset = (double) Mth.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ + orbitOffsetDiffY * orbitOffsetDiffY);
                    float rotationYaw = EntityNaga.this.getYRot();
                    float desiredRotationYaw = (float) Mth.atan2((double) orbitOffsetDiffZ, (double) orbitOffsetDiffX);
                    float rotationYawWrapped = Mth.wrapDegrees(EntityNaga.this.getYRot() + 90.0F);
                    float desiredRotationYawWrapped = Mth.wrapDegrees(desiredRotationYaw * 57.295776F);
                    EntityNaga.this.setYRot(Mth.approachDegrees(rotationYawWrapped, desiredRotationYawWrapped, 4.0F) - 90.0F);
                    float newBanking = MowzieMathUtil.approachDegreesSmooth(getBanking(), getPrevBanking(), EntityNaga.this.getYRot() - rotationYaw, 0.5f, 0.1f);
                    setPrevBanking(getBanking());
                    setBanking(newBanking);
                    EntityNaga.this.yBodyRot = EntityNaga.this.getYRot();
//                if (MathHelper.degreesDifferenceAbs(rotationYaw, EntityNaga.this.rotationYaw) < 3.0F) {
//                    this.speedFactor = MathHelper.approach(this.speedFactor, 1.8F, 0.005F * (1.8F / this.speedFactor));
//                } else {
//                    this.speedFactor = MathHelper.approach(this.speedFactor, 0.2F, 0.025F);
//                }

                    float desiredPitch = (float) (-(Mth.atan2((double) (-orbitOffsetDiffY), horizontalDistToOrbitOffset) * 57.2957763671875D));
                    EntityNaga.this.setXRot(Mth.approachDegrees(EntityNaga.this.getXRot(), desiredPitch, 8));
                    float rotationYaw1 = EntityNaga.this.getYRot() + 90.0F;
                    double xMotion = (double) (this.speedFactor * Mth.cos(rotationYaw1 * 0.017453292F)) * Math.abs((double) orbitOffsetDiffX / distToOrbitOffset);
                    double yMotion = (double) (this.speedFactor * Mth.sin(rotationYaw1 * 0.017453292F)) * Math.abs((double) orbitOffsetDiffZ / distToOrbitOffset);
                    double zMotion = (double) (this.speedFactor * Mth.sin(desiredPitch * 0.017453292F)) * Math.abs((double) orbitOffsetDiffY / distToOrbitOffset);
                    Vec3 motion = EntityNaga.this.getDeltaMovement();
                    EntityNaga.this.setDeltaMovement(motion.add((new Vec3(xMotion, zMotion, yMotion)).subtract(motion).scale(0.1D)));
                }
            }
            else if (movement == EnumNagaMovement.HOVERING) {
                if (getAnimation() == NO_ANIMATION || getAnimation() == SPIT_ANIMATION) {
                    LivingEntity target = getTarget();
                    if (target != null && EntityNaga.this.distanceToSqr(this.parentEntity) < 1600.0D) {
                        lookAt(target, 100, 100);
                    }

                    if (this.operation == MoveControl.Operation.MOVE_TO) {
                        if (this.courseChangeCooldown-- <= 0) {
                            this.courseChangeCooldown += this.parentEntity.getRandom().nextInt(5) + 2;
                            Vec3 Vector3d = new Vec3(this.wantedX - this.parentEntity.getX(), this.wantedY - this.parentEntity.getY(), this.wantedZ - this.parentEntity.getZ());
                            double d0 = Vector3d.length();
                            Vector3d = Vector3d.normalize();
                            if (this.checkCollisions(Vector3d, Mth.ceil(d0))) {
                                this.parentEntity.setDeltaMovement(this.parentEntity.getDeltaMovement().add(Vector3d.scale(0.1D)));
                            } else {
                                this.operation = MoveControl.Operation.WAIT;
                            }
                        }
                    }

                }
            }
        }

        public boolean checkCollisions(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.getBoundingBox();

            for(int i = 1; i < p_220673_2_; ++i) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.level.noCollision(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }

            return true;
        }
    }

    class NagaLookController extends LookControl {
        public NagaLookController(Mob entity) {
            super(entity);
        }

        public void tick() {
            if (this.hasWanted && this.getYRotD().isPresent()) {
                this.hasWanted = false;
                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.getYRotD().get(), this.yMaxRotSpeed);
            } else {
                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
            }

            if (!this.mob.getNavigation().isDone()) {
                this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
            }
        }
    }
}
