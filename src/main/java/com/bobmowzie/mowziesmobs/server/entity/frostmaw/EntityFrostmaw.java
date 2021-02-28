package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.MMEntityMoveHelper;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationActivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAreaAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDeactivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LegSolverQuadruped;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 5/8/2017.
 */
public class EntityFrostmaw extends MowzieEntity implements IMob {
    public static final Animation DIE_ANIMATION = Animation.create(94);
    public static final Animation HURT_ANIMATION = Animation.create(0);
    public static final Animation ROAR_ANIMATION = Animation.create(76);
    public static final Animation SWIPE_ANIMATION = Animation.create(28);
    public static final Animation SWIPE_TWICE_ANIMATION = Animation.create(57);
    public static final Animation ICE_BREATH_ANIMATION = Animation.create(92);
    public static final Animation ICE_BALL_ANIMATION = Animation.create(50);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(118);
    public static final Animation ACTIVATE_NO_CRYSTAL_ANIMATION = Animation.create(100);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(25);
    public static final Animation DODGE_ANIMATION = Animation.create(15);
    public static final Animation LAND_ANIMATION = Animation.create(14);
    public static final Animation SLAM_ANIMATION = Animation.create(113);

    private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityFrostmaw.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_CRYSTAL = EntityDataManager.createKey(EntityFrostmaw.class, DataSerializers.BOOLEAN);

    public static final int ICE_BREATH_COOLDOWN = 260;
    public static final int ICE_BALL_COOLDOWN = 200;
    public static final int SLAM_COOLDOWN = 500;
    public static final int DODGE_COOLDOWN = 200;

    public EntityIceBreath iceBreath;

    public boolean swingWhichArm = false;
    private Vec3d prevRightHandPos = new Vec3d(0, 0, 0);
    private Vec3d prevLeftHandPos = new Vec3d(0, 0, 0);
    private int iceBreathCooldown = 0;
    private int iceBallCooldown = 0;
    private int slamCooldown = 0;
    private int timeWithoutTarget;
    private int shouldDodgeMeasure = 0;
    private int dodgeCooldown = 0;
    private boolean shouldDodge;
    private float dodgeYaw = 0;

    private Vec3d prevTargetPos = new Vec3d(0, 0, 0);

    private boolean shouldPlayLandAnimation = false;

    public LegSolverQuadruped legSolver;

    public EntityFrostmaw(EntityType<? extends EntityFrostmaw> type, World world) {
        super(type, world);
        stepHeight = 1;
        frame += rand.nextInt(50);
        legSolver = new LegSolverQuadruped(1f, 2f, -1, 1.5f);
        socketPosArray = new Vec3d[] {new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 0)};
        active = false;
        playsHurtAnimation = false;
        rotationYaw = renderYawOffset = rand.nextFloat() * 360;
        experienceValue = 60;

        moveController = new MMEntityMoveHelper(this, 7);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(2, new AnimationAreaAttackAI<EntityFrostmaw>(this, SWIPE_ANIMATION, null, null, 2, 6.5f, 6, 135, ConfigHandler.MOBS.FROSTMAW.combatConfig.attackMultiplier.get(), 9) {
            @Override
            public void startExecuting() {
                super.startExecuting();
            }
        });
        this.goalSelector.addGoal(2, new AnimationAreaAttackAI<EntityFrostmaw>(this, SWIPE_TWICE_ANIMATION, null, null, 1, 6.5f, 6, 135, ConfigHandler.MOBS.FROSTMAW.combatConfig.attackMultiplier.get(), 9) {
            @Override
            public void startExecuting() {
                super.startExecuting();
            }

            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 21) {
                    hitEntities();
                }
                if (getAnimationTick() == 16) {
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH.get(), 2, 0.7f);
                }
                if (getAnimationTick() == 6) {
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH.get(), 2, 0.8f);
                }
                if (getAttackTarget() != null) lookController.setLookPositionWithEntity(getAttackTarget(), 30, 30);
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, ICE_BREATH_ANIMATION, true));
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityFrostmaw>(this, ICE_BALL_ANIMATION, true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_FROSTMAW_ICEBALL_CHARGE.get(), 2, 0.9f);
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, ROAR_ANIMATION, false));
        this.goalSelector.addGoal(2, new AnimationActivateAI<EntityFrostmaw>(this, ACTIVATE_ANIMATION) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP.get(), 1, 1);
            }
        });
        this.goalSelector.addGoal(2, new AnimationActivateAI<EntityFrostmaw>(this, ACTIVATE_NO_CRYSTAL_ANIMATION) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP.get(), 1, 1);
            }
        });
        this.goalSelector.addGoal(2, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, LAND_ANIMATION, false));
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, SLAM_ANIMATION, false));
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, DODGE_ANIMATION, true));
        this.goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        this.goalSelector.addGoal(1, new AnimationDieAI<>(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, null));
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new MMPathNavigateGround(this, world);
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250.0D * ConfigHandler.MOBS.FROSTMAW.combatConfig.healthMultiplier.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(ACTIVE, false);
        getDataManager().register(HAS_CRYSTAL, true);
    }

    @Override
    public void playAmbientSound() {
        if (!active) return;
        int i = rand.nextInt(4);
        super.playAmbientSound();
        if (i == 0 && getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, ROAR_ANIMATION);
            return;
        }
        if (i < MMSounds.ENTITY_FROSTMAW_LIVING.size()) playSound(MMSounds.ENTITY_FROSTMAW_LIVING.get(i).get(), 2, 0.8f + rand.nextFloat() * 0.3f);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    public void tick() {
//        if (ticksExisted == 1)
//            System.out.println("Spawned " + getName().getFormattedText() + " at " + getPosition());
        rotationYaw = renderYawOffset;
        super.tick();
        this.repelEntities(3.8f, 3.8f, 3.8f, 3.8f);

        if (getAttackTarget() != null && (!getAttackTarget().isAlive() || getAttackTarget().getHealth() <= 0)) setAttackTarget(null);

        if (getActive() && getAnimation() != ACTIVATE_ANIMATION && getAnimation() != ACTIVATE_NO_CRYSTAL_ANIMATION) {
            legSolver.update(this);

            if (getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) {
//                if (getAnimationTick() == 1) swingWhichArm = rand.nextBoolean();
                if (getAnimationTick() == 3) {
                    int i = MathHelper.nextInt(rand, 0, MMSounds.ENTITY_FROSTMAW_ATTACK.size());
                    if (i < MMSounds.ENTITY_FROSTMAW_ATTACK.size()) {
                        playSound(MMSounds.ENTITY_FROSTMAW_ATTACK.get(i).get(), 2, 0.9f + rand.nextFloat() * 0.2f);
                    }
                }
            }

            if (getAnimation() == SWIPE_ANIMATION) {
                if (getAnimationTick() == 6) {
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH.get(), 2, 0.8f);
                }
                if (getAttackTarget() != null) lookController.setLookPositionWithEntity(getAttackTarget(), 30, 30);
            }

            if (getAnimation() == ROAR_ANIMATION) {
                if (getAnimationTick() == 10) {
                    playSound(MMSounds.ENTITY_FROSTMAW_ROAR.get(), 4, 1);
                }
                if (getAnimationTick() >= 8 && getAnimationTick() < 65) {
                    doRoarEffects();
                }
            }

            if (getAnimation() == LAND_ANIMATION) {
                if (getAnimationTick() == 3) {
                    playSound(MMSounds.ENTITY_FROSTMAW_LAND.get(), 3, 0.9f);
                }
            }

            if (getAnimation() == SLAM_ANIMATION) {
                if (getAnimationTick() == 82) {
                    playSound(MMSounds.ENTITY_FROSTMAW_LIVING_1.get(), 2, 1);
                }
                if (getAttackTarget() != null) lookController.setLookPositionWithEntity(getAttackTarget(), 30, 30);
                if (getAnimationTick() == 82) {
                    int i = MathHelper.nextInt(rand, 0, MMSounds.ENTITY_FROSTMAW_ATTACK.size() - 1);
                    if (i < MMSounds.ENTITY_FROSTMAW_ATTACK.size()) {
                        playSound(MMSounds.ENTITY_FROSTMAW_ATTACK.get(i).get(), 2, 0.9f + rand.nextFloat() * 0.2f);
                    }
                    playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH.get(), 2, 0.7f);
                }
                if (getAnimationTick() == 87) {
                    playSound(MMSounds.ENTITY_FROSTMAW_LAND.get(), 3, 1f);
                    float radius = 4;
                    float slamPosX = (float) (posX + radius * Math.cos(Math.toRadians(rotationYaw + 90)));
                    float slamPosZ = (float) (posZ + radius * Math.sin(Math.toRadians(rotationYaw + 90)));
                    if (world.isRemote) world.addParticle(new ParticleRing.RingData(0f, (float)Math.PI/2f, 17, 1f, 1f, 1f, 1f, 60f, false, ParticleRing.EnumRingBehavior.GROW), slamPosX, posY + 0.2f, slamPosZ, 0, 0, 0);
                    AxisAlignedBB hitBox = new AxisAlignedBB(new BlockPos(slamPosX - 0.5f, posY, slamPosZ - 0.5f)).grow(3, 3, 3);
                    List<LivingEntity> entitiesHit = world.getEntitiesWithinAABB(LivingEntity.class, hitBox);
                    for (LivingEntity entity: entitiesHit) {
                        if (entity != this) {
                            attackEntityAsMob(entity, 4f * ConfigHandler.MOBS.FROSTMAW.combatConfig.attackMultiplier.get(), 1);
                            if (entity.isActiveItemStackBlocking()) entity.getActiveItemStack().damageItem(400, entity, p -> p.sendBreakAnimation(entity.getActiveHand()));
                        }
                    }
                }
            }
            if (getAnimation() == DODGE_ANIMATION && !world.isRemote) {
                getNavigator().clearPath();
                if (getAnimationTick() == 2) {
                    dodgeYaw = (float) Math.toRadians(targetAngle + 90 + rand.nextFloat() * 150 - 75);
                }
                if (getAnimationTick() == 6 && (onGround || isInLava())) {
                    float speed = 1.7f;
                    Vec3d m = getMotion().add(speed * Math.cos(dodgeYaw), 0, speed * Math.sin(dodgeYaw));
                    setMotion(m.x, 0.6, m.z);
                }
                if (getAttackTarget() != null) lookController.setLookPositionWithEntity(getAttackTarget(), 30, 30);
            }

            if (getAnimation() == ICE_BREATH_ANIMATION) {
                if (getAttackTarget() != null) {
                    lookController.setLookPositionWithEntity(getAttackTarget(), 30, 30);
                    faceEntity(getAttackTarget(), 30, 30);
                }
                Vec3d mouthPos = new Vec3d(2.3, 2.65, 0);
                mouthPos = mouthPos.rotateYaw((float)Math.toRadians(-rotationYaw - 90));
                mouthPos = mouthPos.add(getPositionVector());
                mouthPos = mouthPos.add(new Vec3d(0, 0, 1).rotatePitch((float)Math.toRadians(-rotationPitch)).rotateYaw((float)Math.toRadians(-rotationYawHead)));
                if (getAnimationTick() == 13) {
                    iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH, world, this);
                    iceBreath.setPositionAndRotation(mouthPos.x, mouthPos.y, mouthPos.z, rotationYawHead, rotationPitch + 10);
                    if (!world.isRemote) world.addEntity(iceBreath);
                }
                if (iceBreath != null)
                    iceBreath.setPositionAndRotation(mouthPos.x, mouthPos.y, mouthPos.z, rotationYawHead, rotationPitch + 10);
            }

            if (getAnimation() == ICE_BALL_ANIMATION) {
                if (getAttackTarget() != null) lookController.setLookPositionWithEntity(getAttackTarget(), 15, 15);
                Vec3d projectilePos = new Vec3d(2.0, 1.9, 0);
                projectilePos = projectilePos.rotateYaw((float)Math.toRadians(-rotationYaw - 90));
                projectilePos = projectilePos.add(getPositionVector());
                projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotatePitch((float)Math.toRadians(-rotationPitch)).rotateYaw((float)Math.toRadians(-rotationYawHead)));
                if (world.isRemote) {
                    Vec3d mouthPos = socketPosArray[2];
                    if (getAnimationTick() < 12) {
                        for (int i = 0; i < 6; i++) {
                            Vec3d particlePos = new Vec3d(3.5, 0, 0);
                            particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                            float value = rand.nextFloat() * 0.15f;
                            world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f + value, 0.75f + value, 1f, 5f + rand.nextFloat() * 15f, 30, ParticleCloud.EnumCloudBehavior.CONSTANT, 1f), mouthPos.x + particlePos.x, mouthPos.y + particlePos.y, mouthPos.z + particlePos.z, -0.1 * particlePos.x, -0.1 * particlePos.y, -0.1 * particlePos.z);
                        }
                        for (int i = 0; i < 8; i++) {
                            Vec3d particlePos = new Vec3d(3.5, 0, 0);
                            particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                            world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), mouthPos.x + particlePos.x, mouthPos.y + particlePos.y, mouthPos.z + particlePos.z, -0.07 * particlePos.x, -0.07 * particlePos.y, -0.07 * particlePos.z);
                        }
                    }
                }

                if (getAnimationTick() == 32) {
                    if (getAttackTarget() != null) prevTargetPos = getAttackTarget().getPositionVector().add(new Vec3d(0f, getAttackTarget().getHeight() / 2.0, 0f));
                }
                if (getAnimationTick() == 33) {
                    playSound(MMSounds.ENTITY_FROSTMAW_ICEBALL_SHOOT.get(), 2, 0.7f);

                    EntityIceBall iceBall = new EntityIceBall(EntityHandler.ICE_BALL, world, this);
                    iceBall.setPositionAndRotation(projectilePos.x, projectilePos.y, projectilePos.z, rotationYawHead, rotationPitch + 10);
                    float projSpeed = 1.6f;
                    if (getAttackTarget() != null) {
                        float ticksUntilHit = targetDistance / projSpeed;
                        Vec3d targetPos = getAttackTarget().getPositionVector().add(new Vec3d(0f, getAttackTarget().getHeight() / 2.0, 0f));
                        Vec3d targetMovement = targetPos.subtract(prevTargetPos).scale(ticksUntilHit * 0.95);
                        targetMovement = targetMovement.subtract(0, targetMovement.y, 0);
                        Vec3d futureTargetPos = targetPos.add(targetMovement);
                        Vec3d projectileMid = projectilePos.add(new Vec3d(0, iceBall.getHeight() / 2.0, 0));
                        Vec3d shootVec = futureTargetPos.subtract(projectileMid).normalize();
                        iceBall.shoot(shootVec.x, shootVec.y, shootVec.z, projSpeed, 0);
                    }
                    else {
                        iceBall.shoot(getLookVec().x, getLookVec().y, getLookVec().z, projSpeed, 0);
                    }
                    if (!world.isRemote) world.addEntity(iceBall);
                }
            }

            spawnSwipeParticles();

            if ((fallDistance > 0.2 && !onGround) || getAnimation() == DODGE_ANIMATION) shouldPlayLandAnimation = true;
            if (onGround && shouldPlayLandAnimation && getAnimation() != DODGE_ANIMATION) {
                if (!world.isRemote && getAnimation() == NO_ANIMATION) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, LAND_ANIMATION);
                }
                shouldPlayLandAnimation = false;
            }

            if (getAttackTarget() != null) {
                timeWithoutTarget = 0;

                float entityHitAngle = (float) ((Math.atan2(getAttackTarget().posZ - posZ, getAttackTarget().posX - posX) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = renderYawOffset % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if (getNavigator().noPath() && !((entityRelativeAngle <= 30 / 2f && entityRelativeAngle >= -30 / 2f) || (entityRelativeAngle >= 360 - 30 / 2f || entityRelativeAngle <= -360 + 30 / 2f))) {
                    getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.85);
                }

                if (shouldDodgeMeasure >= 14) shouldDodge = true;
                if (targetDistance < 4 && shouldDodge && getAnimation() == NO_ANIMATION) {
                    shouldDodge = false;
                    dodgeCooldown = DODGE_COOLDOWN;
                    shouldDodgeMeasure = 0;
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
                }

                if (targetDistance > 5.5 && !(getAnimation() == ICE_BREATH_ANIMATION && targetDistance < 7.5) && onGround) {
                    if (getAnimation() != SLAM_ANIMATION) getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1);
                    else getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.95);
                }
                else getNavigator().clearPath();
                if (targetDistance <= 8.5 && getAnimation() == NO_ANIMATION && slamCooldown <= 0 && rand.nextInt(4) == 0 && !getAttackTarget().isPotionActive(PotionHandler.FROZEN) && getHealthRatio() < 0.6) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SLAM_ANIMATION);
                    slamCooldown = SLAM_COOLDOWN;
                }
                if (targetDistance <= 6.5 && getAnimation() == NO_ANIMATION) {
                    if (rand.nextInt(4) == 0)
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_TWICE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_ANIMATION);
                }
                if (targetDistance >= 3.5 && targetDistance <= 13.5 && getAnimation() == NO_ANIMATION && iceBreathCooldown <= 0 && getHasCrystal() && onGround) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BREATH_ANIMATION);
                    iceBreathCooldown = ICE_BREATH_COOLDOWN;
                }
                if (targetDistance >= 14.5 && getAnimation() == NO_ANIMATION && iceBallCooldown <= 0 && getHasCrystal() && onGround) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BALL_ANIMATION);
                    iceBallCooldown = ICE_BALL_COOLDOWN;
                }
            }
            else if (!world.isRemote) {
                timeWithoutTarget++;
                if (timeWithoutTarget > 1200 || world.getDifficulty() == Difficulty.PEACEFUL) {
                    timeWithoutTarget = 0;
                    if (getAnimation() == NO_ANIMATION) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        setActive(false);
                    }
                }
            }
        }
        else {
            getNavigator().clearPath();
            renderYawOffset = prevRenderYawOffset;
            if (!world.isRemote && getAnimation() != ACTIVATE_ANIMATION) {
                if (ConfigHandler.MOBS.FROSTMAW.healsOutOfBattle.get()) heal(0.3f);
            }
            if (getAttackTarget() != null && getAttackTarget().isPotionActive(Effects.INVISIBILITY)) {
                setAttackTarget(null);
            }
            if (!getAttackableEntityLivingBaseNearby(8, 8, 8, 8).isEmpty() && getAttackTarget() != null && getAnimation() == NO_ANIMATION) {
                if (world.getDifficulty() != Difficulty.PEACEFUL) {
                    if (getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                    setActive(true);
                }
            }

            if (ConfigHandler.MOBS.FROSTMAW.stealableIceCrystal.get() && getHasCrystal() && ticksExisted > 20) {
                Vec3d crystalPos = new Vec3d(1.6, 0.4, 1.8);
                crystalPos = crystalPos.rotateYaw((float) Math.toRadians(-rotationYaw - 90));
                crystalPos = crystalPos.add(getPositionVector());
                for (PlayerEntity player : getPlayersNearby(8, 8, 8, 8)) {
                    if (player.getPositionVector().distanceTo(crystalPos) <= 1.8 && (player.isCreative() || player.isInvisible()) && !isInventoryFull(player.inventory)) {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemHandler.ICE_CRYSTAL));
                        setHasCrystal(false);
                        if (world.getDifficulty() != Difficulty.PEACEFUL) {
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                            setActive(true);
                        }
                        if (player instanceof ServerPlayerEntity) AdvancementHandler.STEAL_ICE_CRYSTAL_TRIGGER.trigger((ServerPlayerEntity)player);
                        break;
                    }
                }
            }
        }

        if (getAnimation() == ACTIVATE_ANIMATION || getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION) {
            //if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP, 1, 1);
            if (getAnimation() == ACTIVATE_ANIMATION && getAnimationTick() == 18) playSound(MMSounds.ENTITY_FROSTMAW_ATTACK.get(0).get(), 1.5f, 1);
            if ((getAnimation() == ACTIVATE_ANIMATION && getAnimationTick() == 52) || (getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION && getAnimationTick() == 34)) {
                playSound(MMSounds.ENTITY_FROSTMAW_ROAR.get(), 4, 1);
            }
            if ((getAnimation() == ACTIVATE_ANIMATION && getAnimationTick() >= 51 && getAnimationTick() < 108) || (getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION && getAnimationTick() >= 33 && getAnimationTick() < 90)) {
                doRoarEffects();
            }
        }

        if (world.isRemote) {
            if ((getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) && getAnimationTick() == 1) {
                swingWhichArm = rand.nextBoolean();
            }
        }

        //Footstep Sounds
        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (frame % 16 == 5 && speed > 0.05 && active) {
            playSound(MMSounds.ENTITY_FROSTMAW_STEP.get(), 3F, 0.8F + rand.nextFloat() * 0.2f);
        }

        //Breathing sounds
        if (frame % 118 == 1 && !active) {
            int i = MathHelper.nextInt(rand, 0, 1);
            playSound(MMSounds.ENTITY_FROSTMAW_BREATH.get(i).get(), 1.5F, 1.1F + rand.nextFloat() * 0.1f);
        }

//        if (getAnimation() == NO_ANIMATION && onGround) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, SLAM_ANIMATION);
//            setActive(true);
//        }

        if (iceBreathCooldown > 0) iceBreathCooldown--;
        if (iceBallCooldown > 0) iceBallCooldown--;
        if (slamCooldown > 0) slamCooldown--;
        if (shouldDodgeMeasure > 0 && ticksExisted % 7 == 0) shouldDodgeMeasure--;
        if (dodgeCooldown > 0) dodgeCooldown--;
        prevRotationYaw = rotationYaw;
    }

    private void doRoarEffects() {
        List<LivingEntity> entities = getEntityLivingBaseNearby(10, 3, 10, 10);
        for (LivingEntity entity:entities) {
            if (entity == this) continue;
            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            double distance = getDistance(entity) - 4;
            entity.setMotion(entity.getMotion().add(Math.min(1/(distance * distance), 1) * -1 * Math.cos(angle), 0, Math.min(1/ (distance * distance), 1) * -1 * Math.sin(angle)));
        }
        if (getAnimationTick() % 12 == 0 && world.isRemote) {
            int particleCount = 15;
            for (int i = 1; i <= particleCount; i++) {
                double yaw = i * 360.f / particleCount;
                double speed = 0.9;
                double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f, 0.75f,1f, 40f, 22, ParticleCloud.EnumCloudBehavior.GROW, 1f), posX, posY + 1f, posZ, xSpeed, 0, zSpeed);
            }
            for (int i = 1; i <= particleCount; i++) {
                double yaw = i * 360.f / particleCount;
                double speed = 0.65;
                double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f, 0.75f,1f, 35f, 22, ParticleCloud.EnumCloudBehavior.GROW, 1f), posX, posY + 1f, posZ, xSpeed, 0, zSpeed);
            }
        }
    }

    private static boolean isInventoryFull(PlayerInventory inventory) {
        for(ItemStack itemstack : inventory.mainInventory) {
            if (itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        List<LivingEntity> nearby = getEntityLivingBaseNearby(100, 100, 100, 100);
        for (LivingEntity nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityFrostmaw || nearbyEntity instanceof VillagerEntity) {
//                System.out.println("Could not spawn");
                return false;
            }
        }
//        System.out.println("Can spawn");
        return super.canSpawn(world, reason);
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
        setHasCrystal(true);
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }

    private void spawnSwipeParticles() {
        if (world.isRemote) {
            double motionX = getMotion().getX();
            double motionY = getMotion().getY();
            double motionZ = getMotion().getZ();

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
                            double length = prevRightHandPos.subtract(rightHandPos).length();
                            int numClouds = (int) Math.floor(2 * length);
                            for (int i = 0; i < numClouds; i++) {
                                double x = prevRightHandPos.x + i * (rightHandPos.x - prevRightHandPos.x) / numClouds;
                                double y = prevRightHandPos.y + i * (rightHandPos.y - prevRightHandPos.y) / numClouds;
                                double z = prevRightHandPos.z + i * (rightHandPos.z - prevRightHandPos.z) / numClouds;
                                for (int j = 0; j < snowflakeDensity; j++) {
                                    float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                    float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                    float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                    world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                                }
                                for (int j = 0; j < cloudDensity; j++) {
                                    float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                    float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                    float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                    float value = rand.nextFloat() * 0.1f;
                                    world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.8f + value, 0.8f + value, 1f, (float) (10d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                                }
                            }
                        } else {
                            double length = prevLeftHandPos.subtract(leftHandPos).length();
                            int numClouds = (int) Math.floor(2.5 * length);
                            for (int i = 0; i < numClouds; i++) {
                                double x = prevLeftHandPos.x + i * (leftHandPos.x - prevLeftHandPos.x) / numClouds;
                                double y = prevLeftHandPos.y + i * (leftHandPos.y - prevLeftHandPos.y) / numClouds;
                                double z = prevLeftHandPos.z + i * (leftHandPos.z - prevLeftHandPos.z) / numClouds;
                                for (int j = 0; j < snowflakeDensity; j++) {
                                    float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                    float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                    float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                    world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                                }
                                for (int j = 0; j < cloudDensity; j++) {
                                    float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                    float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                    float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                    float value = rand.nextFloat() * 0.1f;
                                    world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.8f + value, 0.8f + value, 1f, (float) (10d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                                }
                            }
                        }
                    }
                } else {
                    if ((swingWhichArm && getAnimationTick() > 8 && getAnimationTick() < 14) || (!swingWhichArm && getAnimationTick() > 19 && getAnimationTick() < 25)) {
                        double length = prevRightHandPos.subtract(rightHandPos).length();
                        int numClouds = (int) Math.floor(2 * length);
                        for (int i = 0; i < numClouds; i++) {
                            double x = prevRightHandPos.x + i * (rightHandPos.x - prevRightHandPos.x) / numClouds;
                            double y = prevRightHandPos.y + i * (rightHandPos.y - prevRightHandPos.y) / numClouds;
                            double z = prevRightHandPos.z + i * (rightHandPos.z - prevRightHandPos.z) / numClouds;
                            for (int j = 0; j < snowflakeDensity; j++) {
                                float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                            }
                            for (int j = 0; j < cloudDensity; j++) {
                                float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float value = rand.nextFloat() * 0.1f;
                                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.8f + value, 0.8f + value, 1f, (float) (10d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                            }
                        }
                    } else if ((!swingWhichArm && getAnimationTick() > 8 && getAnimationTick() < 14) || (swingWhichArm && getAnimationTick() > 19 && getAnimationTick() < 25)) {
                        double length = prevLeftHandPos.subtract(leftHandPos).length();
                        int numClouds = (int) Math.floor(2.5 * length);
                        for (int i = 0; i < numClouds; i++) {
                            double x = prevLeftHandPos.x + i * (leftHandPos.x - prevLeftHandPos.x) / numClouds;
                            double y = prevLeftHandPos.y + i * (leftHandPos.y - prevLeftHandPos.y) / numClouds;
                            double z = prevLeftHandPos.z + i * (leftHandPos.z - prevLeftHandPos.z) / numClouds;
                            for (int j = 0; j < snowflakeDensity; j++) {
                                float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
                                world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                            }
                            for (int j = 0; j < cloudDensity; j++) {
                                float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
                                float value = rand.nextFloat() * 0.1f;
                                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.8f + value, 0.8f + value, 1f, (float) (10d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                            }
                        }
                    }
                }
                prevLeftHandPos = leftHandPos;
                prevRightHandPos = rightHandPos;
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (source == DamageSource.FALL) return false;
        if (source == DamageSource.LAVA && getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
        }

        if (source.isFireDamage()) damage *= 1.25;

        if (source.getImmediateSource() instanceof AbstractArrowEntity) {
            playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.4F, 2);
            Entity entity = source.getTrueSource();
            if (entity != null && entity instanceof LivingEntity && (!(entity instanceof PlayerEntity) || !((PlayerEntity)entity).isCreative()) && getAttackTarget() == null && !(entity instanceof EntityFrostmaw)) setAttackTarget((LivingEntity) entity);
            if (!getActive()) {
                if (getAnimation() != DIE_ANIMATION) {
                    if (world.getDifficulty() != Difficulty.PEACEFUL) {
                        if (getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                        else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                    }
                }
                setActive(true);
            }
            return false;
        }

        boolean attack = super.attackEntityFrom(source, damage);

        if (attack) {
            shouldDodgeMeasure += damage;
            Entity entity = source.getTrueSource();
            if (entity != null && entity instanceof LivingEntity && (!(entity instanceof PlayerEntity) || !((PlayerEntity)entity).isCreative()) && getAttackTarget() == null && !(entity instanceof EntityFrostmaw)) setAttackTarget((LivingEntity) entity);
            if (!getActive()) {
                if (getAnimation() != DIE_ANIMATION && world.getDifficulty() != Difficulty.PEACEFUL) {
                    if (getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                }
                setActive(true);
            }
        }

        return attack;
    }

    @Override
    protected void onDeathAIUpdate() {
        super.onDeathAIUpdate();
        if (getAnimationTick() == 5) {
            playSound(MMSounds.ENTITY_FROSTMAW_DIE.get(), 2.5f, 1);
        } else if (getAnimationTick() == 53) {
            playSound(MMSounds.ENTITY_FROSTMAW_LAND.get(), 2.5f, 1);
        }
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

    public void setHasCrystal(boolean hasCrystal) {
        getDataManager().set(HAS_CRYSTAL, hasCrystal);
    }

    public boolean getHasCrystal() {
        return getDataManager().get(HAS_CRYSTAL);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] {DIE_ANIMATION, HURT_ANIMATION, ROAR_ANIMATION, SWIPE_ANIMATION, SWIPE_TWICE_ANIMATION, ICE_BREATH_ANIMATION, ICE_BALL_ANIMATION, ACTIVATE_ANIMATION, ACTIVATE_NO_CRYSTAL_ANIMATION, DEACTIVATE_ANIMATION, SLAM_ANIMATION, LAND_ANIMATION, DODGE_ANIMATION};
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setHasCrystal(compound.getBoolean("has_crystal"));
        setActive(compound.getBoolean("active"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("has_crystal", getHasCrystal());
        compound.putBoolean("active", getActive());
    }

    @Override
    public boolean preventDespawn() {
        return getHasCrystal();
    }

    @Override
    protected boolean hasBossBar() {
        return ConfigHandler.MOBS.FROSTMAW.hasBossBar.get();
    }

    @Override
    protected BossInfo.Color bossBarColor() {
        return BossInfo.Color.WHITE;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.FROSTMAW;
    }

    @Override
    public void move(MoverType typeIn, Vec3d pos) {
        if (typeIn == MoverType.SELF && !active) return;
        super.move(typeIn, pos);
    }
}
