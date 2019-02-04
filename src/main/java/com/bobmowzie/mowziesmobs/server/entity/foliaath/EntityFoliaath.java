package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

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
    int resettingTargetTimer = 0;
    private double prevOpenMouth;
    private double prevActivate;
    private int activateTarget;

    public EntityFoliaath(World world) {
        super(world);
        setPathPriority(PathNodeType.WATER, 0);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AnimationAttackAI<>(this, ATTACK_ANIMATION, MMSounds.ENTITY_FOLIAATH_BITE_1, null, 2, 4.5F, MowziesMobs.CONFIG.attackScaleFoliaath, 3));
        this.tasks.addTask(1, new AnimationTakeDamage<>(this));
        this.tasks.addTask(1, new AnimationDieAI<>(this));
        this.tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, false, e ->
            EntityPlayer.class.isAssignableFrom(e.getClass()) || EntityCreature.class.isAssignableFrom(e.getClass())) {

            @Override
            protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles) {
                return !(target instanceof EntityFoliaath) && !(target instanceof EntityBabyFoliaath) && !target.isInvisible() && !target.getIsInvulnerable() && super.isSuitableTarget(target, includeInvincibles);
            }
        });
        this.setSize(0.5F, 2.5F);
        this.experienceValue = 10;
        this.addIntermittentAnimation(openMouth);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(CAN_DESPAWN, true);
        getDataManager().register(ACTIVATE_TARGET, 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10 * MowziesMobs.CONFIG.healthScaleFoliaath);
    }

    @Override
    public int getAttack() {
        return 10;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_FOLIAATH_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return MMSounds.ENTITY_FOLIAATH_DIE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.posX = prevPosX;
        this.posZ = prevPosZ;
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
                playSound(MMSounds.ENTITY_FOLIAATH_PANT_1, 1, 1);
            } else if (activate.getTimer() >= 25) {
                playSound(MMSounds.ENTITY_FOLIAATH_PANT_2, 1, 1);
            }
        }

        int openMouthTime = openMouth.getTimeRunning();
        if (prevOpenMouth - openMouthTime < 0) {
            if (openMouthTime == 1) {
                playSound(MMSounds.ENTITY_FOLIAATH_RUSTLE, 1, 1);
            } else if (openMouthTime == 13) {
                playSound(MMSounds.ENTITY_FOLIAATH_GRUNT, 1, 1);
            }
        }

        prevOpenMouth = openMouthTime;

        int activateTime = activate.getTimer();
        if (!world.isRemote) {
            SoundEvent sound = null;
            if (prevActivate - activateTime < 0) {
                switch (activateTime) {
                    case 1:
                        sound = MMSounds.ENTITY_FOLIAATH_RUSTLE;
                        break;
                    case 5:
                        sound = MMSounds.ENTITY_FOLIAATH_MERGE;
                        break;
                }
            } else if (prevActivate - activateTime > 0) {
                switch (activateTime) {
                    case 24:
                        sound = MMSounds.ENTITY_FOLIAATH_RETREAT;
                        break;
                    case 28:
                        sound = MMSounds.ENTITY_FOLIAATH_RUSTLE;
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

            if (targetDistance <= 4.5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 2 && getAnimation() == NO_ANIMATION && active) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
            }

            if (targetDistance <= 11) {
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
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        return active && super.attackEntityFrom(damageSource, amount);
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
    public void applyEntityCollision(Entity entity) {
        if (!entity.getPassengers().contains(this) && entity.getRidingEntity() != this) {
            double deltaX = entity.posX - posX;
            double deltaZ = entity.posZ - posZ;
            double majorAxis = MathHelper.absMax(deltaX, deltaZ);
            if (majorAxis >= 0.009999999) {
                majorAxis = MathHelper.sqrt(majorAxis);
                deltaX /= majorAxis;
                deltaZ /= majorAxis;
                double reciprocal = 1 / majorAxis;
                if (reciprocal > 1) {
                    reciprocal = 1;
                }
                deltaX *= reciprocal;
                deltaZ *= reciprocal;
                deltaX *= 0.05;
                deltaZ *= 0.05;
                deltaX *= 1 - entityCollisionReduction;
                deltaZ *= 1 - entityCollisionReduction;
                this.addVelocity(deltaX, 0, deltaZ);
                entity.addVelocity(deltaX, 0, deltaZ);
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (world.checkNoEntityCollision(getEntityBoundingBox()) && world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox())) {
            BlockPos ground = new BlockPos(
                MathHelper.floor(posX),
                MathHelper.floor(getEntityBoundingBox().minY) - 1,
                MathHelper.floor(posZ)
             );
            if (ground.getY() < 64) {
                return false;
            }
            IBlockState block = world.getBlockState(ground);
            if (block.getBlock() == Blocks.GRASS || block.getBlock().isLeaves(block, world, ground)) {
                if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel()
    {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering())
            {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    @Override
    public void onKillEntity(EntityLivingBase entity) {
        this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 1, true, true));
    }

    @Override
    protected Item getDropItem() {
        return rand.nextBoolean() ? ItemHandler.FOLIAATH_SEED : null;
    }

    @Override
    public boolean canDespawn() {
        return getDataManager().get(CAN_DESPAWN);
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
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("canDespawn", canDespawn());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setCanDespawn(compound.getBoolean("canDespawn"));
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {}

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION};
    }
}