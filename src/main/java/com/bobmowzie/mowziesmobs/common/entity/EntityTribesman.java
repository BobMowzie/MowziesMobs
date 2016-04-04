package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.common.ai.animation.*;
import com.bobmowzie.mowziesmobs.common.item.ItemHandler;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTribesman extends MowzieEntity implements IRangedAttackMob, LeaderSunstrikeImmune {
    protected boolean attacking = false;
    protected int timeSinceAttack = 0;
    public ControlledAnimation doWalk = new ControlledAnimation(3);
    public ControlledAnimation dancing = new ControlledAnimation(7);
    private int danceTimer = 0;
    private int talkTimer = 0;
    boolean prevHasTarget = false;
    boolean prevprevHasTarget = false;
    boolean prevprevprevHasTarget = false;
    int cryDelay = -1;
    public boolean circleDirection = true;
    public int circleTick = 0;

    public static final Animation DIE_ANIMATION = Animation.create(70);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation ATTACK_ANIMATION = Animation.create(19);
    public static final Animation PROJECTILE_ATTACK_ANIMATION = Animation.create(20);
    public static final Animation IDLE_ANIMATION = Animation.create(35);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(20);

    public EntityTribesman(World world) {
        super(world);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5D, false));
        tasks.addTask(5, new EntityAIWander(this, 0.4));
        tasks.addTask(2, new AnimationAttackAI<>(this, ATTACK_ANIMATION, "mowziesmobs:barakoaSwing", "", 1, 3, 1, 9));
        tasks.addTask(2, new AnimationProjectileAttackAI<>(this, PROJECTILE_ATTACK_ANIMATION, 9, "mowziesmobs:barakoaBlowdart"));
        tasks.addTask(4, new AnimationAI<>(this, IDLE_ANIMATION));
        tasks.addTask(3, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(0, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        setMask(MathHelper.getRandomIntegerInRange(rand, 2, 5));
        setSize(0.6f, 1.7f);
        stepHeight = 1;
        circleTick += rand.nextInt(200);
        frame += rand.nextInt(50);
        experienceValue = 8;
        active = true;
    }

    @Override
    public int getAttack() {
        return 4;
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
    protected String getLivingSound() {
        if (!active) {
            return null;
        }
        if (danceTimer == 0) {
            if (getAttackTarget() == null) {
                int i = MathHelper.getRandomIntegerInRange(rand, 0, 11);
                if (i == 0) {
                    playSound("mowziesmobs:barakoaTalk1", 1, 1.5f);
                }
                if (i == 1) {
                    playSound("mowziesmobs:barakoaTalk2", 1, 1.5f);
                }
                if (i == 2) {
                    playSound("mowziesmobs:barakoaTalk3", 1, 1.5f);
                }
                if (i == 3) {
                    playSound("mowziesmobs:barakoaTalk4", 1, 1.5f);
                }
                if (i == 4) {
                    playSound("mowziesmobs:barakoaTalk5", 1, 1.5f);
                }
                if (i == 5) {
                    playSound("mowziesmobs:barakoaTalk6", 1, 1.5f);
                }
                if (i == 6) {
                    playSound("mowziesmobs:barakoaTalk7", 1, 1.5f);
                }
                if (i == 7) {
                    playSound("mowziesmobs:barakoaTalk8", 1, 1.5f);
                }
                if (i <= 7) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
                }
            } else {
                int i = MathHelper.getRandomIntegerInRange(rand, 0, 7);
                if (i == 0) {
                    playSound("mowziesmobs:barakoaAngry1", 1, 1.6f);
                }
                if (i == 1) {
                    playSound("mowziesmobs:barakoaAngry2", 1, 1.6f);
                }
                if (i == 2) {
                    playSound("mowziesmobs:barakoaAngry3", 1, 1.6f);
                }
                if (i == 3) {
                    playSound("mowziesmobs:barakoaAngry4", 1, 1.6f);
                }
                if (i == 4) {
                    playSound("mowziesmobs:barakoaAngry5", 1, 1.6f);
                }
                if (i == 5) {
                    playSound("mowziesmobs:barakoaAngry6", 1, 1.6f);
                }
            }
        }
        return null;
    }

    @Override
    protected String getHurtSound() {
        if (!active) {
            return null;
        }
        int i = MathHelper.getRandomIntegerInRange(rand, 0, 3);
        if (i == 0) {
            playSound("mowziesmobs:barakoaHurt1", 1, 1.6f);
        }
        if (i == 1) {
            playSound("mowziesmobs:barakoaHurt2", 0.7f, 1f);
        }
        if (i == 2) {
            playSound("mowziesmobs:barakoaHurt3", 0.7f, 1f);
        }
        if (i == 3) {
            playSound("mowziesmobs:barakoaHurt4", 0.7f, 1f);
        }
        return super.getHurtSound();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20);
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
        return super.attackEntityAsMob(p_70652_1_);
    }

    protected void updateAttackAI() {
        if (timeSinceAttack < 80) {
            timeSinceAttack++;
        }
        if (getAttackTarget() != null) {
            if (targetDistance > 7) {
                getNavigator().tryMoveToXYZ(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 0.6);
            } else {
                if (!attacking) {
                    updateCircling();
                }
            }
            if (rand.nextInt(80) == 0 && timeSinceAttack == 80) {
                attacking = true;
                if (getAnimation() == NO_ANIMATION && getWeapon() == 0) {
                    getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.5);
                }
            }
            if (attacking && getAnimation() == NO_ANIMATION) {
                if (targetDistance <= 3 && getWeapon() == 0) {
                    attacking = false;
                    timeSinceAttack = 0;
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
                }
                if (getWeapon() == 1) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, PROJECTILE_ATTACK_ANIMATION);
                }
            }
        } else {
            attacking = false;
        }
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
        if (!(this instanceof EntityTribeElite)) {
            int weapon = 0;
            if (rand.nextInt(3) == 0) {
                weapon = 1;
            }
            setWeapon(weapon);
        }
        return super.onSpawnWithEgg(p_110161_1_);
    }

    protected void updateCircling() {
        if (rand.nextInt(200) == 0) {
            circleDirection = !circleDirection;
        }
        if (circleDirection) {
            circleTick++;
        } else {
            circleTick--;
        }
        if (!attacking && targetDistance < 5) {
            circleEntity(getAttackTarget(), 7, 0.3f, true, circleTick, 0, 1.75f);
        } else {
            circleEntity(getAttackTarget(), 7, 0.3f, true, circleTick, 0, 1);
        }
        attacking = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote && active && getActive() == 0) {
            setActive(1);
        }
        active = getActive() == 1;
        if (!active) {
            getNavigator().clearPathEntity();
            rotationYaw = prevRotationYaw;
            renderYawOffset = rotationYaw;
            if (onGround && getAnimation() == NO_ANIMATION) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                playSound("mowziesmobs:barakoaEmerge", 1, 1);
            }
            return;
        }
        updateAttackAI();
        if (getAnimation() == NO_ANIMATION) {
            getNavigator().clearPathEntity();
        }

        if (getDancing() == 1) {
            setDancing(0);
            danceTimer++;
        }

        if (getAnimation() == NO_ANIMATION || getAnimation() == IDLE_ANIMATION) {
            doWalk.increaseTimer();
        } else {
            doWalk.decreaseTimer();
        }

        if (danceTimer != 0 && danceTimer != 30) {
            danceTimer++;
            dancing.increaseTimer();
        } else {
            danceTimer = 0;
            dancing.decreaseTimer();
        }
        if (!worldObj.isRemote && getAnimation() == NO_ANIMATION && danceTimer == 0 && rand.nextInt(800) == 0) {
            setDancing(1);
            playSound("mowziesmobs:barakoaBattlecry2", 1.2f, 1.3f);
        }
        if (getAnimation() != NO_ANIMATION) {
            danceTimer = 0;
        }

        if (cryDelay > -1) {
            cryDelay--;
        }
        if (cryDelay == 0) {
            playSound("mowziesmobs:barakoaBattlecry", 1.5f, 1.5f);
        }
        if (getAttackTarget() != null && !prevHasTarget && !prevprevHasTarget && !prevprevprevHasTarget) {
            cryDelay = MathHelper.getRandomIntegerInRange(rand, -15, 30);
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 5) {
            playSound("mowziesmobs:barakoaShout", 1, 1.1f);
        }
        if (getAnimation() == PROJECTILE_ATTACK_ANIMATION && getAnimationTick() == 1) {
            playSound("mowziesmobs:barakoaInhale", 0.7f, 1.2f);
        }

        prevprevprevHasTarget = prevprevHasTarget;
        prevprevHasTarget = prevHasTarget;
        prevHasTarget = (getAttackTarget() != null);

//        if (getAnimation() == NO_ANIMATION) AnimationAPI.sendAnimPacket(this, 4);
    }

    @Override
    protected String getDeathSound() {
        int i = MathHelper.getRandomIntegerInRange(rand, 0, 1);
        if (i == 0) {
            playSound("mowziesmobs:barakoaDie1", 1, 1);
        }
        if (i == 1) {
            playSound("mowziesmobs:barakoaDie2", 1, 1);
        }
        return super.getDeathSound();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(28, 0);
        dataWatcher.addObject(29, 0);
        dataWatcher.addObject(30, 0);
        dataWatcher.addObject(27, 1);
    }

    public int getDancing() {
        return dataWatcher.getWatchableObjectInt(28);
    }

    public void setDancing(Integer type) {
        dataWatcher.updateObject(28, type);
    }

    public int getMask() {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setMask(Integer type) {
        dataWatcher.updateObject(29, type);
    }

    public int getWeapon() {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void setWeapon(Integer type) {
        dataWatcher.updateObject(30, type);
    }

    public int getActive() {
        return dataWatcher.getWatchableObjectInt(27);
    }

    public void setActive(Integer active) {
        dataWatcher.updateObject(27, active);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("mask", getMask());
        compound.setInteger("weapon", getWeapon());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMask(compound.getInteger("mask"));
        setWeapon(compound.getInteger("weapon"));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
        EntityArrow entitydart = new EntityDart(this.worldObj, this, p_82196_1_, 1.6F, 1);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entitydart.setDamage((double) (p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

        if (i > 0) {
            entitydart.setDamage(entitydart.getDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            entitydart.setKnockbackStrength(j);
        }

        this.worldObj.spawnEntityInWorld(entitydart);
        attacking = false;
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot") && rand.nextInt(12) == 0) {
            dropItem(ItemHandler.INSTANCE.itemBarakoaMasks[getMask() - 1], 1);
        }
        super.onDeath(p_70645_1_);
    }

    @Override
    public boolean canBeCollidedWith() {
        return active;
    }

    @Override
    protected void fall(float p_70069_1_) {
        if (!active) {
            return;
        }
        super.fall(p_70069_1_);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION, PROJECTILE_ATTACK_ANIMATION, IDLE_ANIMATION, ACTIVATE_ANIMATION};
    }
}
