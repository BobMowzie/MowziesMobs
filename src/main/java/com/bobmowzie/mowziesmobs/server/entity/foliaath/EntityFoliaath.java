package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public class EntityFoliaath extends MowzieEntity implements IMob {
    public static final Animation DIE_ANIMATION = Animation.create(50);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation ATTACK_ANIMATION = Animation.create(14);
    private static final DataParameter<Boolean> CAN_DESPAWN = EntityDataManager.createKey(EntityFoliaath.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ACTIVATE_TARGET = EntityDataManager.createKey(EntityFoliaath.class, DataSerializers.VARINT);
    private static final int ACTIVATE_DURATION = 30;
    public IntermittentAnimation<EntityFoliaath> openMouth = new IntermittentAnimation<>(this, 15, 30, 50, !world.isRemote);
    public ControlledAnimation activate = new ControlledAnimation(ACTIVATE_DURATION);
    public ControlledAnimation deathFlail = new ControlledAnimation(5);
    public ControlledAnimation stopDance = new ControlledAnimation(10);
    public int lastTimeDecrease = 0;
    private int resettingTargetTimer = 0;
    private double prevOpenMouth;
    private double prevActivate;
    private int activateTarget;

    public EntityFoliaath(EntityType<? extends EntityFoliaath> type, World world) {
        super(type, world);
        this.experienceValue = 10;
        this.addIntermittentAnimation(openMouth);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        setPathPriority(PathNodeType.WATER, 0);
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AnimationAttackAI<>(this, ATTACK_ANIMATION, MMSounds.ENTITY_FOLIAATH_BITE_1.get(), null, 2, 4F, ConfigHandler.MOBS.FOLIAATH.combatData.attackMultiplier, 3));
        this.goalSelector.addGoal(1, new AnimationTakeDamage<>(this));
        this.goalSelector.addGoal(1, new AnimationDieAI<>(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, true, false, e ->
                (PlayerEntity.class.isAssignableFrom(e.getClass()) || CreatureEntity.class.isAssignableFrom(e.getClass())) && !(e instanceof EntityFoliaath || e instanceof EntityBabyFoliaath))
        );
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(CAN_DESPAWN, true);
        getDataManager().register(ACTIVATE_TARGET, 0);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10 * ConfigHandler.MOBS.FOLIAATH.combatData.healthMultiplier);
    }

    @Override
    public int getAttack() {
        return 10;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_FOLIAATH_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return MMSounds.ENTITY_FOLIAATH_DIE.get();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
//        this.posX = prevPosX;
//        this.posZ = prevPosZ;
        setMotion(0, getMotion().y, 0);
        // Open mouth animation
        if (getAnimation() == NO_ANIMATION && !activate.canIncreaseTimer()) {
            openMouth.update();
        } else {
            openMouth.stop();
        }

        if (activate.getAnimationFraction() >= 0.8F) {
            if (!active) {
                active = true;
            }
        } else if (activate.getAnimationFraction() < 0.8F) {
            if (active) {
                active = false;
            }
        }

        // Sounds
        if (frame % 13 == 3 && getAnimation() != DIE_ANIMATION) {
            if (openMouth.getTimeRunning() >= 10) {
                playSound(MMSounds.ENTITY_FOLIAATH_PANT_1.get(), 1, 1);
            } else if (activate.getTimer() >= 25) {
                playSound(MMSounds.ENTITY_FOLIAATH_PANT_2.get(), 1, 1);
            }
        }

        int openMouthTime = openMouth.getTimeRunning();
        if (prevOpenMouth - openMouthTime < 0) {
            if (openMouthTime == 1) {
                playSound(MMSounds.ENTITY_FOLIAATH_RUSTLE.get(), 1, 1);
            } else if (openMouthTime == 13) {
                playSound(MMSounds.ENTITY_FOLIAATH_GRUNT.get(), 1, 1);
            }
        }

        prevOpenMouth = openMouthTime;

        int activateTime = activate.getTimer();
        if (!world.isRemote) {
            SoundEvent sound = null;
            if (prevActivate - activateTime < 0) {
                switch (activateTime) {
                    case 1:
                        sound = MMSounds.ENTITY_FOLIAATH_RUSTLE.get();
                        break;
                    case 5:
                        sound = MMSounds.ENTITY_FOLIAATH_MERGE.get();
                        break;
                }
            } else if (prevActivate - activateTime > 0) {
                switch (activateTime) {
                    case 24:
                        sound = MMSounds.ENTITY_FOLIAATH_RETREAT.get();
                        break;
                    case 28:
                        sound = MMSounds.ENTITY_FOLIAATH_RUSTLE.get();
                        break;
                }
            }
            if (sound != null) {
                playSound(sound, 1, 1);
            }
        }

        prevActivate = activateTime;

        // Targetting, attacking, and activating
        renderYawOffset = 0;
        rotationYaw = 0;

//        if (getAttackTarget() instanceof EntityFoliaath || getAttackTarget() instanceof EntityBabyFoliaath) {
//            setAttackTarget(null);
//        }

        if (resettingTargetTimer > 0 && !world.isRemote) {
            rotationYawHead = prevRotationYawHead;
        }

        if (getAttackTarget() != null) {
            rotationYawHead = targetAngle;

            if (targetDistance <= 4 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 2 && getAnimation() == NO_ANIMATION && active) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
            }

            if (targetDistance <= 10.5 && getAttackTarget().posY - posY >= -1.5 && getAttackTarget().posY - posY <= 2) {
                setActivateTarget(ACTIVATE_DURATION);
                lastTimeDecrease = 0;
            } else if (lastTimeDecrease <= 30 && getAnimation() == NO_ANIMATION) {
                setActivateTarget(0);
                lastTimeDecrease++;
            }
        } else if (!world.isRemote && lastTimeDecrease <= 30 && getAnimation() == NO_ANIMATION && resettingTargetTimer == 0) {
            setActivateTarget(0);
            lastTimeDecrease++;
        }

        if (getAnimation() == DIE_ANIMATION) {
            if (getAnimationTick() <= 12) {
                deathFlail.increaseTimer();
            } else {
                deathFlail.decreaseTimer();
            }
            stopDance.increaseTimer();
            setActivateTarget(ACTIVATE_DURATION);
        }

        if (resettingTargetTimer > 0) {
            resettingTargetTimer--;
        }

        if (getAttackTarget() != null && frame % 20 == 0 && getAnimation() == NO_ANIMATION) {
            setAttackTarget(null);
            resettingTargetTimer = 20;
        }
        if (activateTarget == activateTime) {
            activateTarget = getActivateTarget();
        } else if (activateTime < activateTarget && activate.canIncreaseTimer() || activateTime > activateTarget && activate.canDecreaseTimer()) {
            activate.increaseTimer(activateTime < activateTarget ? 1 : -2);
        }

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        return (damageSource.canHarmInCreative() || active) && super.attackEntityFrom(damageSource, amount);
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected ConfigHandler.SpawnData getSpawnConfig() {
        return ConfigHandler.MOBS.FOLIAATH.spawnData;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        Biome biome = world.getBiome(getPosition());
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos pos = new BlockPos(i, j, k);
        Block floor = world.getBlockState(pos.down()).getBlock();
        BlockState floorDown1 = world.getBlockState(pos.down(2));
        BlockState floorDown2 = world.getBlockState(pos.down(3));
        boolean notInTree = true;
        if (floor instanceof LeavesBlock && floorDown1 != biome.getSurfaceBuilder().config.getTop() && floorDown2 != biome.getSurfaceBuilder().config.getTop()) notInTree = false;
        return super.canSpawn(world, reason) && notInTree && getEntitiesNearby(AnimalEntity.class, 10, 10, 10, 10).isEmpty() && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public void onKillEntity(LivingEntity entity) {
        this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 300, 1, true, true));
    }

    @Override
    public boolean preventDespawn() {
        return !getDataManager().get(CAN_DESPAWN);
    }

    public void setCanDespawn(boolean canDespawn) {
        getDataManager().set(CAN_DESPAWN, canDespawn);
    }

    public int getActivateTarget() {
        return getDataManager().get(ACTIVATE_TARGET);
    }

    public void setActivateTarget(int activateTarget) {
        getDataManager().set(ACTIVATE_TARGET, activateTarget);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("canDespawn", getDataManager().get(CAN_DESPAWN));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setCanDespawn(compound.getBoolean("canDespawn"));
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION};
    }
}