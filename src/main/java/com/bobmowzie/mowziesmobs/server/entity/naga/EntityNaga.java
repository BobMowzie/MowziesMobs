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
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.potion.Effects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
 * Created by Josh on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity implements IRangedAttackMob, IMob, IFlyingAnimal {
    @OnlyIn(Dist.CLIENT)
    public DynamicChain dc;
    @OnlyIn(Dist.CLIENT)
    public Vec3d[] mouthPos;

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


        this.experienceValue = 10;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        setPathPriority(PathNodeType.WATER, 0);
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

                Vec3d v = new Vec3d(0, 0, 0);

                int phase1Length = 15;
                int phase2Length = 21;
                if (getAnimationTick() < 23 + phase2Length) {
                    LivingEntity target = getAttackTarget();
                    if (getAnimationTick() >= 1 && getAnimationTick() < 1 + phase1Length) {
                        float frame = (getAnimationTick() - 1) / (float) phase1Length;
                        v = v.add(new Vec3d(
                                1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                -1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                0.8f * Math.sin(frame * Math.PI * 2)
                        ));
                    } else if (getAnimationTick() >= 16) {
                        if (target != null)
                            entity.lookController.setLookPositionWithEntity(target, 30F, 30F);
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

        this.moveController = new NagaMoveHelper(this);
        if (world.isRemote) {
            dc = new DynamicChain(this);
            mouthPos = new Vec3d[] {new Vec3d(0, 0, 0)};
        }
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

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D * ConfigHandler.MOBS.NAGA.combatConfig.healthMultiplier.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(12.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D * ConfigHandler.MOBS.NAGA.combatConfig.attackMultiplier.get());
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(45);
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
                    List<AbstractArrowEntity> arrowsNearby = getEntitiesNearby(AbstractArrowEntity.class, 30);
                    for (AbstractArrowEntity a : arrowsNearby) {
                        Vec3d aActualMotion = new Vec3d(a.posX - a.prevPosX, a.posY - a.prevPosY, a.posZ - a.prevPosZ);
                        if (aActualMotion.length() < 0.1 || a.ticksExisted <= 1) {
                            continue;
                        }

                        float dot = (float) getMotion().normalize().dotProduct(this.getPositionVector().subtract(a.getPositionVector()).normalize());
                        if (dot > 0.96) {
                            Vec3d dodgeVec = getMotion().crossProduct(new Vec3d(0, 1, 0)).normalize().scale(1.2);
                            Vec3d newPosLeft = getPositionVector().add(dodgeVec.scale(2));
                            Vec3d newPosRight = getPositionVector().add(dodgeVec.scale(-2));
                            Vec3d diffLeft = newPosLeft.subtract(a.getPositionVector());
                            Vec3d diffRight = newPosRight.subtract(a.getPositionVector());
                            if (diffRight.dotProduct(getMotion()) > diffLeft.dotProduct(getMotion())) {
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
            if (onGround) {
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
                    Vec3d particlePos = new Vec3d(0.25, 0, 0);
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
                    Vec3d particlePos = new Vec3d(3, 0, 0);
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
                world.playSound(posX, posY, posZ, MMSounds.ENTITY_NAGA_FLAP_1.get(), SoundCategory.HOSTILE, 2, (float) (0.85 + rand.nextFloat() * 0.2), false);
                hasFlapSoundPlayed = true;
            }
        }

        hoverAnimFrac = hoverAnim.getAnimationProgressSinSqrt();
        flapAnimFrac = flapAnim.getAnimationProgressSinSqrt();

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
        }

        /*setAttacking(true);
        getNavigator().clearPath();
        posX = prevPosX;
        posY = prevPosY;
        posZ = prevPosZ;
        motionX = motionZ = 0;
        posY = 10;

        if (getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, SPIT_ANIMATION);
        }*/
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.MOBS.NAGA.spawnConfig;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        boolean flag = super.canSpawn(world, reason);
        setPosition(posX, posY + 5, posZ);
        return flag && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean isNotColliding(IWorldReader worldIn) {
        boolean liquid = !this.world.containsAnyLiquid(this.getBoundingBox());
        boolean worldCollision = this.world.isCollisionBoxesEmpty(this, this.getBoundingBox());
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
        if (flag && movement != EnumNagaMovement.FALLING && (isSpitting || isSwooping)) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.HURT_TO_FALL_ANIMATION);
            interrupted = true;
        }
        return flag;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        if (interrupted) return;
        Vec3d projectilePos = new Vec3d(1, -0.7, 0);
        projectilePos = projectilePos.rotateYaw((float)Math.toRadians(-rotationYaw - 90));
        projectilePos = projectilePos.add(getPositionVector());
        projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotatePitch((float)Math.toRadians(-rotationPitch)).rotateYaw((float)Math.toRadians(-rotationYawHead)));
        projectilePos = projectilePos.add(new Vec3d(0, 0, 0));
        EntityPoisonBall poisonBall = new EntityPoisonBall(EntityHandler.POISON_BALL, this.world, this);
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

    public void fall(float distance, float damageMultiplier)
    {
        if (movement == EnumNagaMovement.FALLING) {
            super.fall(distance, damageMultiplier);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
        if (movement == EnumNagaMovement.FALLING) {
            super.updateFallState(y, onGroundIn, state, pos);
        }
    }

    public void travel(Vec3d motion) {
        double d0 = 0.08D;
        IAttributeInstance gravity = this.getAttribute(ENTITY_GRAVITY);
        boolean flag = this.getMotion().y <= 0.0D;
        /*if (flag && this.isPotionActive(Effects.SLOW_FALLING)) {
            if (!gravity.hasModifier(SLOW_FALLING)) gravity.applyModifier(SLOW_FALLING);
            this.fallDistance = 0.0F;
        } else if (gravity.hasModifier(SLOW_FALLING)) {
            gravity.removeModifier(SLOW_FALLING);
        }*/ // TODO: Slow fall modifier has private access. Ignore?
        d0 = gravity.getValue();

        if (this.isInWater()) {
            this.moveRelative(0.02F, motion);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale((double)0.8F));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, motion);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.5D));
        }
        else if (movement == EnumNagaMovement.HOVERING) {
            BlockPos ground = new BlockPos(this.posX, this.getBoundingBox().minY - 1.0D, this.posZ);
            float f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, motion);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale((double)f));

            MovementController entitymovehelper = this.getMoveHelper();
            double dx = entitymovehelper.getX() - this.posX;
            double dy = entitymovehelper.getY() - this.posY;
            double dz = entitymovehelper.getZ() - this.posZ;
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest < 0.1 && getAnimation() == NO_ANIMATION) {
                setMotion(0, 0, 0);
            }
        } else if (movement == EnumNagaMovement.GLIDING) {
            Vec3d vec3d3 = this.getMotion();
            if (vec3d3.y > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vec3d vec3d = this.getLookVec();
            float f6 = this.rotationPitch * ((float) Math.PI / 180F);
            double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
            double d11 = Math.sqrt(horizontalMag(vec3d3));
            double d12 = vec3d.length();
            float f3 = MathHelper.cos(f6);
            f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
            vec3d3 = this.getMotion().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);
            if (vec3d3.y < 0.0D && d9 > 0.0D) {
                double d3 = vec3d3.y * -0.1D * (double) f3;
                vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
            }

            if (f6 < 0.0F && d9 > 0.0D) {
                double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
            }

            if (d9 > 0.0D) {
                vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
            }

            this.setMotion(vec3d3.mul((double) 0.99F, (double) 0.98F, (double) 0.99F));
            this.move(MoverType.SELF, this.getMotion());
            if (getMoveHelper().getY() - posY > 0 && getAnimation() == NO_ANIMATION)
                AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        } else if (movement == EnumNagaMovement.FALLING || movement == EnumNagaMovement.FALLEN || isAIDisabled()) {
            BlockPos blockpos = new BlockPos(this.posX, this.getBoundingBox().minY - 1.0D, this.posZ);
            float f5 = this.world.getBlockState(blockpos).getSlipperiness(world, blockpos, this);
            float f7 = this.onGround ? f5 * 0.91F : 0.91F;
            this.moveRelative(this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (f5 * f5 * f5)) : this.jumpMovementFactor, motion);
            this.move(MoverType.SELF, this.getMotion());
            Vec3d vec3d5 = this.getMotion();
            if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
                vec3d5 = new Vec3d(vec3d5.x, 0.2D, vec3d5.z);
            }

            double d10 = vec3d5.y;
            if (this.isPotionActive(Effects.LEVITATION)) {
                d10 += (0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vec3d5.y) * 0.2D;
                this.fallDistance = 0.0F;
            } else if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
                if (this.posY > 0.0D) {
                    d10 = -0.1D;
                } else {
                    d10 = 0.0D;
                }
            } else if (!this.hasNoGravity()) {
                d10 -= d0;
            }

            this.setMotion(vec3d5.x * (double)f7, d10 * (double)0.98F, vec3d5.z * (double)f7);
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d6 = this.posZ - this.prevPosZ;
        double d8 = this.posY - this.prevPosY;
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
        public void updateTask()
        {
            if (this.parentEntity.getAttackTarget() == null)
            {
                Vec3d motion = this.parentEntity.getMotion();
                this.parentEntity.rotationYaw = -((float)MathHelper.atan2(motion.x, motion.z)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
            else
            {
                LivingEntity entitylivingbase = this.parentEntity.getAttackTarget();
                if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D)
                {
                    double d1 = entitylivingbase.posX - this.parentEntity.posX;
                    double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
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
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, parentEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
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
                    double dx = entitymovehelper.getX() - this.parentEntity.posX;
                    double dy = entitymovehelper.getY() - this.parentEntity.posY;
                    double dz = entitymovehelper.getZ() - this.parentEntity.posZ;
                    double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    LivingEntity target = parentEntity.getAttackTarget();
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
            LivingEntity target = parentEntity.getAttackTarget();
            float yaw = (float) (random.nextFloat() * Math.PI * 2);
            float radius = 16;
            double d0 = target.posX + Math.cos(yaw) * radius;
            double d1 = target.posY + 8 + random.nextFloat() * 5;
            double d2 = target.posZ + Math.sin(yaw) * radius;
            double speed = parentEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
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
            if (this.action == Action.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    Vec3d lvt_1_1_ = new Vec3d(this.posX - this.parentEntity.posX, this.posY - this.parentEntity.posY, this.posZ - this.parentEntity.posZ);
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (this.func_220673_a(lvt_1_1_, MathHelper.ceil(lvt_2_1_))) {
                        this.parentEntity.setMotion(this.parentEntity.getMotion().add(lvt_1_1_.scale(0.1D)));
                    } else {
                        this.action = Action.WAIT;
                    }
                }

            }
        }

        private boolean func_220673_a(Vec3d p_220673_1_, int p_220673_2_) {
            AxisAlignedBB lvt_3_1_ = this.parentEntity.getBoundingBox();

            for(int lvt_4_1_ = 1; lvt_4_1_ < p_220673_2_; ++lvt_4_1_) {
                lvt_3_1_ = lvt_3_1_.offset(p_220673_1_);
                if (!this.parentEntity.world.isCollisionBoxesEmpty(this.parentEntity, lvt_3_1_)) {
                    return false;
                }
            }

            return true;
        }
    }
}
