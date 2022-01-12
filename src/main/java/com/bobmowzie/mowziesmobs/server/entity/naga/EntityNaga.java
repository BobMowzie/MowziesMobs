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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.RandomPositionGenerator;
import net.minecraft.world.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.world.entity.ai.controller.LookController;
import net.minecraft.world.entity.ai.controller.MovementController;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.IMob;
import net.minecraft.world.entity.animal.IFlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.SoundCategory;
import net.minecraft.resources.SoundEvent;
import net.minecraft.resources.math.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

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

    private static final EntityDataAccessor<Boolean> ATTACKING = EntityDataManager.createKey(EntityNaga.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> BANKING = EntityDataManager.createKey(EntityNaga.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PREV_BANKING = EntityDataManager.createKey(EntityNaga.class, EntityDataSerializers.FLOAT);

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

    public EntityNaga(EntityType<? extends EntityNaga> type, World world) {
        super(type, world);
        if (world.isClientSide) {
            dc = new DynamicChain(this);
            mouthPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
        this.experienceValue = 10;

        this.moveController = new NagaMoveHelper(this);
        this.lookController = new NagaLookController(this);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.WATER_BORDER, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, -1.0F);
        this.setPathPriority(PathNodeType.WALKABLE, -1.0F);
    }

    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EntityNaga.FlyOutOfWaterGoal(this));
        this.goalSelector.addGoal(5, new EntityNaga.WanderGoal());
        this.goalSelector.addGoal(4, new EntityNaga.AIFlyAroundTarget(this));
        this.goalSelector.addGoal(3, new EntityNaga.AIFlyTowardsTarget(this));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(this, Player.class, 0, true, true, target -> target.getPosition().withinDistance(getHomePosition(), getMaximumHomeDistance())) {
            @Override
            public boolean shouldContinueExecuting() {
                return super.shouldContinueExecuting() && getAttackTarget() != null && getAttackTarget().getPosition().withinDistance(getHomePosition(), getMaximumHomeDistance()) && getAnimation() == NO_ANIMATION;
            }
        });
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

                Vec3 v = new Vec3(0, 0, 0);

                int phase1Length = 15;
                int phase2Length = 21;
                LivingEntity target = getAttackTarget();
                if (getAnimationTick() < phase1Length) {
                    if (target != null) {
                        entity.faceEntity(target, 100, 100);
                        entity.lookController.setLookPositionWithEntity(target, 30F, 30F);
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
                            v = v.add(new Vec3(
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
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
            public boolean canEntityStandOnPos(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(false);
        flyingpathnavigator.setCanEnterDoors(false);
        return flyingpathnavigator;
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(ATTACKING, false);
        getDataManager().register(BANKING, 0.0f);
        getDataManager().register(PREV_BANKING, 0.0f);
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
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 40)
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
        if (world.isClientSide()) {
            banking = getBanking();
            prevBanking = getPrevBanking();
        }

        if (spitCooldown > 0) spitCooldown--;
        if (swoopCooldown > 0) swoopCooldown--;
        if (onGroundTimer > 0) onGroundTimer--;
        if (roarAnimation < ROAR_DURATION) roarAnimation++;

        if (getAnimation() == null) AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);

        if (isPotionActive(MobEffects.POISON)) removeActivePotionEffect(MobEffects.POISON);

//        if (ticksExisted == 1) {
//            System.out.println("Naga at " + getPosition());
//        }

        if (!world.isClientSide) {
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

                if (getAnimation() == NO_ANIMATION && !world.isClientSide) {
                    List<ProjectileEntity> projectilesNearby = getEntitiesNearby(ProjectileEntity.class, 30);
                    for (ProjectileEntity a : projectilesNearby) {
                        Vec3 aActualMotion = new Vec3(a.getPosX() - a.prevPosX, a.getPosY() - a.prevPosY, a.getPosZ() - a.prevPosZ);
                        if (aActualMotion.length() < 0.1 || a.ticksExisted <= 1) {
                            continue;
                        }

                        float dot = (float) a.getMotion().normalize().dotProduct(this.getPositionVec().subtract(a.getPositionVec()).normalize());
                        if (dot > 0.96) {
                            Vec3 dodgeVec = a.getMotion().crossProduct(new Vec3(0, 1, 0)).normalize().scale(1.2);
                            Vec3 newPosLeft = getPositionVec().add(dodgeVec.scale(2));
                            Vec3 newPosRight = getPositionVec().add(dodgeVec.scale(-2));
                            Vec3 diffLeft = newPosLeft.subtract(a.getPositionVec());
                            Vec3 diffRight = newPosRight.subtract(a.getPositionVec());
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

        if (getAnimation() == SPIT_ANIMATION && world.isClientSide && mouthPos != null && !interrupted) {
            if (getAnimationTick() == 33) {
//            System.out.println(mouthPos);
                float explodeSpeed = 2.4f;
                for (int i = 0; i < 25; i++) {
                    Vec3 particlePos = new Vec3(0.25, 0, 0);
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
                    Vec3 particlePos = new Vec3(3, 0, 0);
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

        if (world.isClientSide && movement == EnumNagaMovement.HOVERING && flapAnim.getAnimationFraction() >= 0.5) {

            if (shoulderRot > 0.9) hasFlapSoundPlayed = false;

            if (shoulderRot <= 0.7 && !hasFlapSoundPlayed) {
                world.playSound(getPosX(), getPosY(), getPosZ(), MMSounds.ENTITY_NAGA_FLAP_1.get(), SoundCategory.HOSTILE, 2, (float) (0.85 + rand.nextFloat() * 0.2), false);
                hasFlapSoundPlayed = true;
            }
        }

        hoverAnimFrac = hoverAnim.getAnimationProgressSinSqrt();
        flapAnimFrac = flapAnim.getAnimationProgressSinSqrt();

        if (!this.world.isClientSide && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
        }

//        setAttacking(true);
//        getNavigator().clearPath();
//        setMotion(new Vec3(0, 0, 0));
//        setPosition(getPosX(), 10, getPosZ());
//
//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, TAIL_DEMO_ANIMATION);
//        }
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        setHomePosAndDistance(this.getPosition(), MAX_DIST_FROM_HOME);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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
        Vec3 projectilePos = new Vec3(1, -0.7, 0);
        projectilePos = projectilePos.rotateYaw((float)Math.toRadians(-rotationYaw - 90));
        projectilePos = projectilePos.add(getPositionVec());
        projectilePos = projectilePos.add(new Vec3(0, 0, 1).rotatePitch((float)Math.toRadians(-rotationPitch)).rotateYaw((float)Math.toRadians(-rotationYawHead)));
        projectilePos = projectilePos.add(new Vec3(0, 0, 0));
        EntityPoisonBall poisonBall = new EntityPoisonBall(EntityHandler.POISON_BALL, this.world, this);
        poisonBall.setPosition(projectilePos.x, projectilePos.y, projectilePos.z);
        Vec3 look = getLookVec();
        Vec3 dir = new Vec3(look.x, 0, look.z).normalize();
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

    public void travel(Vec3 motion) {
        double d0 = 0.08D;
        ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        boolean flag = this.getMotion().y <= 0.0D;
//        if (flag && this.isPotionActive(MobEffects.SLOW_FALLING)) {
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

            if (this.isPotionActive(MobEffects.DOLPHINS_GRACE)) {
                f5 = 0.96F;
            }

            f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
            this.moveRelative(f6, motion);
            this.move(MoverType.SELF, this.getMotion());
            Vec3 vector3d6 = this.getMotion();
            if (this.collidedHorizontally && this.isOnLadder()) {
                vector3d6 = new Vec3(vector3d6.x, 0.2D, vector3d6.z);
            }

//            this.setMotion(vector3d6.mul(f5, 0.8F, f5));
            Vec3 vector3d2 = this.func_233626_a_(d0, flag, this.getMotion());
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
                Vec3 vector3d3 = this.func_233626_a_(d0, flag, this.getMotion());
                this.setMotion(vector3d3);
            } else {
                this.setMotion(this.getMotion().scale(0.5D));
            }

            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -d0 / 4.0D, 0.0D));
            }

            Vec3 vector3d4 = this.getMotion();
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

            BlockPos destination = this.getNavigator().getTargetPos();
            if (destination != null) {
                double dx = destination.getX() - this.getPosX();
                double dy = destination.getY() - this.getPosY();
                double dz = destination.getZ() - this.getPosZ();
                double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distanceToDest < 0.1 && getAnimation() == NO_ANIMATION) {
                    setMotion(0, 0, 0);
                }
            }
        } else if (movement == EnumNagaMovement.GLIDING) {
            Vec3 Vec33 = this.getMotion();
            if (Vec33.y > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vec3 moveDirection = this.getLookVec();
            moveDirection = moveDirection.normalize();
            float f6 = this.getXRot() * ((float) Math.PI / 180F);
            double d9 = Math.sqrt(moveDirection.x * moveDirection.x + moveDirection.z * moveDirection.z);
            double d11 = Math.sqrt(horizontalMag(Vec33));
            double d12 = moveDirection.length();
            float f3 = MathHelper.cos(f6);
            f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
            Vec33 = this.getMotion().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);
            if (Vec33.y < 0.0D && d9 > 0.0D) {
                double d3 = Vec33.y * -0.1D * (double) f3;
                Vec33 = Vec33.add(moveDirection.x * d3 / d9, d3, moveDirection.z * d3 / d9);
            }

            if (f6 < 0.0F && d9 > 0.0D) {
                double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                Vec33 = Vec33.add(-moveDirection.x * d13 / d9, d13 * 3.2D, -moveDirection.z * d13 / d9);
            }

            if (d9 > 0.0D) {
                Vec33 = Vec33.add((moveDirection.x / d9 * d11 - Vec33.x) * 0.1D, 0.0D, (moveDirection.z / d9 * d11 - Vec33.z) * 0.1D);
            }

            this.setMotion(Vec33.mul(0.99F, 0.98F, 0.99F));
            this.move(MoverType.SELF, this.getMotion());
            if (moveDirection.getY() < 0 && getAnimation() == NO_ANIMATION)
                AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        } else if (movement == EnumNagaMovement.FALLING || movement == EnumNagaMovement.FALLEN || isAIDisabled()) {
            BlockPos blockpos = new BlockPos(this.getPosX(), this.getBoundingBox().minY - 1.0D, this.getPosZ());
            float f5 = this.world.getBlockState(blockpos).getSlipperiness(world, blockpos, this);
            float f7 = this.isOnGround() ? f5 * 0.91F : 0.91F;

            this.move(MoverType.SELF, this.getMotion());
            Vec3 Vec35 = this.getMotion();
            if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
                Vec35 = new Vec3(Vec35.x, 0.2D, Vec35.z);
            }

            double d10 = Vec35.y;
            if (this.isPotionActive(MobEffects.LEVITATION)) {
                d10 += (0.05D * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - Vec35.y) * 0.2D;
                this.fallDistance = 0.0F;
            } else if (this.world.isClientSide && !this.world.isBlockLoaded(blockpos)) {
                if (this.getPosY() > 0.0D) {
                    d10 = -0.1D;
                } else {
                    d10 = 0.0D;
                }
            } else if (!this.hasNoGravity()) {
                d10 -= d0;
            }

            this.setMotion(Vec35.x * (double)f7, d10 * (double)0.98F, Vec35.z * (double)f7);
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

    public float getBanking() {
        return getDataManager().get(BANKING);
    }

    public void setBanking(float banking) {
        getDataManager().set(BANKING, banking);
    }

    public float getPrevBanking() {
        return getDataManager().get(PREV_BANKING);
    }

    public void setPrevBanking(float prevBanking) {
        getDataManager().set(PREV_BANKING, prevBanking);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("HomePosX", this.getHomePosition().getX());
        compound.putInt("HomePosY", this.getHomePosition().getY());
        compound.putInt("HomePosZ", this.getHomePosition().getZ());
        compound.putInt("HomeDist", (int) this.getMaximumHomeDistance());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        int dist = compound.getInt("HomeDist");
        this.setHomePosAndDistance(new BlockPos(i, j, k), dist);
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
                Vec3 motion = this.parentEntity.getMotion();
                this.parentEntity.getYRot() = -((float)MathHelper.atan2(motion.x, motion.z)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.getYRot();
            }
            else
            {
                LivingEntity entitylivingbase = this.parentEntity.getAttackTarget();
                if (entitylivingbase.getDistanceSq(this.parentEntity) < 1600.0D)
                {
                    double d1 = entitylivingbase.getPosX() - this.parentEntity.getPosX();
                    double d2 = entitylivingbase.getPosZ() - this.parentEntity.getPosZ();
                    this.parentEntity.getYRot() = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.getYRot();
                }
            }
        }
    }

    class WanderGoal extends Goal {
        private boolean seesGround;

        WanderGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return EntityNaga.this.navigator.noPath() && getAttackTarget() == null;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return EntityNaga.this.navigator.hasPath() && !EntityNaga.this.navigator.getTargetPos().withinDistance(EntityNaga.this.getPosition(), 4) && getAttackTarget() == null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            Vec3 vector3d = this.getRandomLocation();
            if (vector3d != null) {
                EntityNaga.this.navigator.setSearchDepthMultiplier(0.5F);
                EntityNaga.this.navigator.setPath(EntityNaga.this.navigator.getPathToPos(new BlockPos(vector3d), 1), 1.0D);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (!seesGround) {
                Vec3 newLocation = getRandomLocation();
                if (newLocation != null && seesGround) {
                    EntityNaga.this.navigator.setPath(EntityNaga.this.navigator.getPathToPos(new BlockPos(newLocation), 1), 1.0D);
                }
            }
        }

        @Override
        public void resetTask() {
            super.resetTask();
            EntityNaga.this.getNavigator().clearPath();
        }

        @Nullable
        private Vec3 getRandomLocation() {
            Vec3 vector3d;
//            if (EntityNaga.this.isHiveValid() && !EntityNaga.this.isWithinDistance(EntityNaga.this.hivePos, 22)) {
//                Vec3 vector3d1 = Vec3.copyCentered(EntityNaga.this.hivePos);
//                vector3d = vector3d1.subtract(EntityNaga.this.getPositionVec()).normalize();
//            } else {
                vector3d = EntityNaga.this.getLook(0.0F);
//            }
            Vec3 position = RandomPositionGenerator.findAirTarget(EntityNaga.this, 24, 24, vector3d, ((float)Math.PI / 2F), 8, 18);
            if (position == null) {
                position = RandomPositionGenerator.func_226344_b_(EntityNaga.this, 24, 8, -8, getPositionVec().add(vector3d), ((float)Math.PI / 2F));
                seesGround = false;
            }
            else {
                seesGround = true;
            }
            if (position == null || !world.isAirBlock(new BlockPos(position).down())) return null;
            Vec3 offset = position.subtract(EntityNaga.this.getPositionVec());
            AxisAlignedBB newBB = EntityNaga.this.getBoundingBox().offset(offset);
            if (world.hasNoCollisions(newBB) && EntityNaga.this.world.rayTraceBlocks(new RayTraceContext(EntityNaga.this.getPositionVec(), position, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, EntityNaga.this)).getType() != RayTraceResult.Type.BLOCK)
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
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (parentEntity.getAttackTarget() != null) {
                if (this.parentEntity.getNavigator().noPath()) {
                    return parentEntity.rand.nextInt(60) == 0;
                }
            }
            return false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            if (EntityNaga.this.getAttackTarget() == null) return false;
            BlockPos navigatorPos = this.parentEntity.getNavigator().getTargetPos();
            if (navigatorPos == null) {
                return false;
            }
            double dx = navigatorPos.getX() - this.parentEntity.getPosX();
            double dy = navigatorPos.getY() - this.parentEntity.getPosY();
            double dz = navigatorPos.getZ() - this.parentEntity.getPosZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest > 60.D) return false;

            LivingEntity target = parentEntity.getAttackTarget();
            double dx2 = navigatorPos.getX() - target.getPosX();
            double dy2 = navigatorPos.getY() - target.getPosY();
            double dz2 = navigatorPos.getZ() - target.getPosZ();
            double distanceDestToTarget = Math.sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2);
            if (distanceDestToTarget > 20.D || distanceDestToTarget < 5) return false;

            return EntityNaga.this.navigator.hasPath() && !EntityNaga.this.navigator.getTargetPos().withinDistance(EntityNaga.this.getPosition(), 1);
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
                this.parentEntity.getNavigator().tryMoveToXYZ(d0, d1, d2, speed);
        }

        @Override
        public void resetTask() {
            super.resetTask();
            EntityNaga.this.getNavigator().clearPath();
        }
    }

    class AIFlyTowardsTarget extends Goal
    {
        private final EntityNaga parentEntity;

        public AIFlyTowardsTarget(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return parentEntity.getAttackTarget() != null && EntityNaga.this.getDistanceSq(this.parentEntity.getAttackTarget()) >= 870.25D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            if (EntityNaga.this.getAttackTarget() == null) return false;
            if (EntityNaga.this.getAttackTarget().getDistanceSq(this.parentEntity) <= 500.0D) return false;
            BlockPos navigatorPos = this.parentEntity.getNavigator().getTargetPos();
            if (navigatorPos == null) {
                return false;
            }
            double dx = navigatorPos.getX() - this.parentEntity.getPosX();
            double dy = navigatorPos.getY() - this.parentEntity.getPosY();
            double dz = navigatorPos.getZ() - this.parentEntity.getPosZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest > 60.D) return false;

            return EntityNaga.this.navigator.hasPath();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = parentEntity.getAttackTarget();
            double speed = parentEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            BlockPos targetPos = target.getPosition().up(8);
            if (!parentEntity.world.hasWater(targetPos))
                this.parentEntity.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
        }

        @Override
        public void resetTask() {
            super.resetTask();
            EntityNaga.this.getNavigator().clearPath();
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

        @Override
        public void startExecuting() {
            if (entity.getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityNaga.FLAP_ANIMATION);
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }
    }

    class NagaMoveHelper extends MovementController
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
                if (this.action == Action.MOVE_TO) {
                    if (EntityNaga.this.collidedHorizontally) {
                        EntityNaga naga = EntityNaga.this;
                        naga.getYRot() += 180.0F;
                        this.speedFactor = 0.1F;
                        getNavigator().clearPath();
                    }

                    float orbitOffsetDiffX = (float) (getNavigator().getTargetPos().getX() - EntityNaga.this.getPosX());
                    float orbitOffsetDiffY = (float) (getNavigator().getTargetPos().getY() - EntityNaga.this.getPosY());
                    float orbitOffsetDiffZ = (float) (getNavigator().getTargetPos().getZ() - EntityNaga.this.getPosZ());
                    double horizontalDistToOrbitOffset = (double) MathHelper.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ);
                    double yFractionReduction = 1.0D - (double) MathHelper.abs(orbitOffsetDiffY * 0.7F) / horizontalDistToOrbitOffset;
                    orbitOffsetDiffX = (float) ((double) orbitOffsetDiffX * yFractionReduction);
                    orbitOffsetDiffZ = (float) ((double) orbitOffsetDiffZ * yFractionReduction);
                    horizontalDistToOrbitOffset = (double) MathHelper.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ);
                    double distToOrbitOffset = (double) MathHelper.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ + orbitOffsetDiffY * orbitOffsetDiffY);
                    float rotationYaw = EntityNaga.this.getYRot();
                    float desiredRotationYaw = (float) MathHelper.atan2((double) orbitOffsetDiffZ, (double) orbitOffsetDiffX);
                    float rotationYawWrapped = MathHelper.wrapDegrees(EntityNaga.this.getYRot() + 90.0F);
                    float desiredRotationYawWrapped = MathHelper.wrapDegrees(desiredRotationYaw * 57.295776F);
                    EntityNaga.this.getYRot() = MathHelper.approachDegrees(rotationYawWrapped, desiredRotationYawWrapped, 4.0F) - 90.0F;
                    float newBanking = MowzieMathUtil.approachDegreesSmooth(getBanking(), getPrevBanking(), EntityNaga.this.getYRot() - rotationYaw, 0.5f, 0.1f);
                    setPrevBanking(getBanking());
                    setBanking(newBanking);
                    EntityNaga.this.renderYawOffset = EntityNaga.this.getYRot();
//                if (MathHelper.degreesDifferenceAbs(rotationYaw, EntityNaga.this.getYRot()) < 3.0F) {
//                    this.speedFactor = MathHelper.approach(this.speedFactor, 1.8F, 0.005F * (1.8F / this.speedFactor));
//                } else {
//                    this.speedFactor = MathHelper.approach(this.speedFactor, 0.2F, 0.025F);
//                }

                    float desiredPitch = (float) (-(MathHelper.atan2((double) (-orbitOffsetDiffY), horizontalDistToOrbitOffset) * 57.2957763671875D));
                    EntityNaga.this.getXRot() = MathHelper.approachDegrees(EntityNaga.this.getXRot(), desiredPitch, 8);
                    float rotationYaw1 = EntityNaga.this.getYRot() + 90.0F;
                    double xMotion = (double) (this.speedFactor * MathHelper.cos(rotationYaw1 * 0.017453292F)) * Math.abs((double) orbitOffsetDiffX / distToOrbitOffset);
                    double yMotion = (double) (this.speedFactor * MathHelper.sin(rotationYaw1 * 0.017453292F)) * Math.abs((double) orbitOffsetDiffZ / distToOrbitOffset);
                    double zMotion = (double) (this.speedFactor * MathHelper.sin(desiredPitch * 0.017453292F)) * Math.abs((double) orbitOffsetDiffY / distToOrbitOffset);
                    Vec3 motion = EntityNaga.this.getMotion();
                    EntityNaga.this.setMotion(motion.add((new Vec3(xMotion, zMotion, yMotion)).subtract(motion).scale(0.1D)));
                }
            }
            else if (movement == EnumNagaMovement.HOVERING) {
                if (getAnimation() == NO_ANIMATION || getAnimation() == SPIT_ANIMATION) {
                    LivingEntity target = getAttackTarget();
                    if (target != null && EntityNaga.this.getDistanceSq(this.parentEntity) < 1600.0D) {
                        faceEntity(target, 100, 100);
                    }

                    if (this.action == MovementController.Action.MOVE_TO) {
                        if (this.courseChangeCooldown-- <= 0) {
                            this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                            Vec3 Vec3 = new Vec3(this.posX - this.parentEntity.getPosX(), this.posY - this.parentEntity.getPosY(), this.posZ - this.parentEntity.getPosZ());
                            double d0 = Vec3.length();
                            Vec3 = Vec3.normalize();
                            if (this.checkCollisions(Vec3, MathHelper.ceil(d0))) {
                                this.parentEntity.setMotion(this.parentEntity.getMotion().add(Vec3.scale(0.1D)));
                            } else {
                                this.action = MovementController.Action.WAIT;
                            }
                        }
                    }

                }
            }
        }

        public boolean checkCollisions(Vec3 p_220673_1_, int p_220673_2_) {
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

    class NagaLookController extends LookController {
        public NagaLookController(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            if (this.isLooking) {
                this.isLooking = false;
                this.mob.getYRot()Head = this.clampedRotate(this.mob.getYRot()Head, this.getTargetYaw(), this.deltaLookYaw);
            } else {
                this.mob.getYRot()Head = this.clampedRotate(this.mob.getYRot()Head, this.mob.renderYawOffset, 10.0F);
            }

            if (!this.mob.getNavigator().noPath()) {
                this.mob.getYRot()Head = MathHelper.func_219800_b(this.mob.getYRot()Head, this.mob.renderYawOffset, (float)this.mob.getHorizontalFaceSpeed());
            }
        }
    }
}
