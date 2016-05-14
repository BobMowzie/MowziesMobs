package com.bobmowzie.mowziesmobs.server.entity.wroughtnaut;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityWroughtnaut extends MowzieEntity {
    //    public double walkFrame;
    public ControlledAnimation walkAnim = new ControlledAnimation(10);
    public boolean swingDirection = false;
    public boolean vulnerable = false;
    private int attacksWithoutVertical = 0;

    public static final Animation DIE_ANIMATION = Animation.create(130);
    public static final Animation HURT_ANIMATION = Animation.create(15);
    public static final Animation ATTACK_ANIMATION = Animation.create(50);
    public static final Animation VERTICAL_ATTACK_ANIMATION = Animation.create(105);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(45);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(15);

    public EntityWroughtnaut(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimationFWNAttackAI<>(this, ATTACK_ANIMATION, "mowziesmobs:wroughtnautWhoosh", 4F, 5.5F, 100F));
        tasks.addTask(1, new AnimationFWNVerticalAttackAI<>(this, VERTICAL_ATTACK_ANIMATION, "mowziesmobs:wroughtnautWhoosh", 1F, 5.5F, 40F));
        tasks.addTask(1, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(1, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        tasks.addTask(1, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 30;
        setSize(2.5F, 3.7F);
        active = false;
        stepHeight = 1;
    }

    @Override
    public int getAttack() {
        return 30;
    }

    @Override
    public String getHurtSound() {
        return "mowziesmobs:wroughtnautHurt1";
    }

    @Override
    public String getDeathSound() {
        playSound("mowziesmobs:wroughtnautScream", 1, 1);
        return null;
    }

    @Override
    protected void fall(float p_70069_1_) {
        p_70069_1_ = ForgeHooks.onLivingFall(this, p_70069_1_);
        if (p_70069_1_ <= 0) {
            return;
        }
        super.fall(p_70069_1_);
        PotionEffect potioneffect = this.getActivePotionEffect(Potion.jump);
        float f1 = potioneffect != null ? (float) (potioneffect.getAmplifier() + 1) : 0.0F;
        int i = MathHelper.ceiling_float_int(p_70069_1_ - 3.0F - f1);

        if (i > 0) {
            this.playSound(this.func_146067_o(i), 1.0F, 1.0F);
            this.attackEntityFrom(DamageSource.fall, (float) i);
            int j = MathHelper.floor_double(this.posX);
            int k = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
            int l = MathHelper.floor_double(this.posZ);
            Block block = this.worldObj.getBlock(j, k, l);

            if (block.getMaterial() != Material.air) {
                Block.SoundType soundtype = block.stepSound;
                this.playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    @Override
    protected String getLivingSound() {
        if (getAnimation() == NO_ANIMATION && getActive() == 1) {
            int i = MathHelper.getRandomIntegerInRange(rand, 0, 4);
            if (i == 0) {
                playSound("mowziesmobs:wroughtnautGrunt1", 1, 1);
            }
            if (i == 1) {
                playSound("mowziesmobs:wroughtnautGrunt3", 1, 1);
            }
            if (i == 2) {
                playSound("mowziesmobs:wroughtnautShout1", 1, 1);
            }
            if (i == 3) {
                playSound("mowziesmobs:wroughtnautShout2", 1, 1);
            }
            if (i == 4) {
                playSound("mowziesmobs:wroughtnautShout3", 1, 1);
            }
        }
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float p_70097_2_) {
        if (source.getEntity() != null) {
            if (vulnerable && source.getEntity() != null) {
                int arc = 220;
                Entity entitySource = source.getEntity();
                float entityHitAngle = (float) ((Math.atan2(entitySource.posZ - posZ, entitySource.posX - posX) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = renderYawOffset % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if ((entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -arc + 90 / 2)) {
                    playSound("minecraft:random.anvil_land", 0.4F, 2F);
                    return false;
                } else {
                    if (currentAnim != null) {
                        currentAnim.resetTask();
                    }
                    return super.attackEntityFrom(source, p_70097_2_);
                }
            } else {
                playSound("minecraft:random.anvil_land", 0.4F, 2F);
            }
        }
        return false;
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
    public void onUpdate() {
        super.onUpdate();

        if (getActive() == 0 && getAttackTarget() != null && targetDistance <= 5 && getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
            setActive((byte) 1);
        }

        if (isClientWorld() && getActive() == 1 && getAttackTarget() == null && moveForward == 0 && getAnimation() == NO_ANIMATION && Math.abs(posX - getRestPosX()) <= 4 && Math.abs(posY - getRestPosY()) <= 4 && Math.abs(posZ - getRestPosZ()) <= 4) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
            setActive((byte) 0);
        }

        if (getActive() == 0) {
            posX = prevPosX;
            posZ = prevPosZ;
            rotationYaw = prevRotationYaw;
        } else {
            renderYawOffset = rotationYaw;
        }

        if (getAttackTarget() != null && getActive() == 1) {
            if (getAnimation() == NO_ANIMATION) {
                getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.2D);
            } else {
                getNavigator().clearPathEntity();
            }

            if (targetDistance <= 3.5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 3 && Math.abs(MathHelper.wrapAngleTo180_double(getAngleBetweenEntities(getAttackTarget(), this) - rotationYaw)) < 35 && getAnimation() == NO_ANIMATION) {
                int i = (int) (3 * Math.random() + 0.5);
                if (attacksWithoutVertical == 4) {
                    i = 0;
                }
                if (i == 0) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, VERTICAL_ATTACK_ANIMATION);
                    attacksWithoutVertical = 0;
                } else {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
                }
            }
        } else if (!getNavigator().tryMoveToXYZ((double) getRestPosX(), (double) getRestPosY(), (double) getRestPosZ(), 0.2D)) {
            setRestPosX((int) posX);
            setRestPosY((int) posY);
            setRestPosZ((int) posZ);
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 1) {
            attacksWithoutVertical++;
            int i = (int) (Math.random() + 0.5);
            if (i == 0) {
                swingDirection = false;
            }
            if (i == 1) {
                swingDirection = true;
            }
        }

        if (getAnimation() == ACTIVATE_ANIMATION) {
            if (getAnimationTick() == 1) {
                playSound("mowziesmobs:wroughtnautGrunt2", 1, 1);
            }
            if (getAnimationTick() == 27) {
                playSound("mob.zombie.metal", 0.5F, 0.5F);
            }
            if (getAnimationTick() == 44) {
                playSound("mob.zombie.metal", 0.5F, 0.5F);
            }
        }

        if (getAnimation() == VERTICAL_ATTACK_ANIMATION && getAnimationTick() == 29) {
            int i = MathHelper.floor_double(posX + 4 * Math.cos((renderYawOffset + 90) * Math.PI / 180));
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double) yOffset);
            int k = MathHelper.floor_double(posZ + 4 * Math.sin((renderYawOffset + 90) * Math.PI / 180));
            Block block = worldObj.getBlock(i, j, k);

            if (block.getMaterial() != Material.air) {
                for (int n = 0; n <= 20; n++) {
                    worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + worldObj.getBlockMetadata(i, j, k), posX + 4.5 * Math.cos((renderYawOffset + 90) * Math.PI / 180) + ((double) rand.nextFloat() - 0.5D) * (double) width, boundingBox.minY + 0.1D, posZ + 4.5 * Math.sin((renderYawOffset + 90) * Math.PI / 180) + ((double) rand.nextFloat() - 0.5D) * (double) width, 4.0D * ((double) rand.nextFloat() - 0.5D), 3D, ((double) rand.nextFloat() - 0.5D) * 4.0D);
                }
            }
        }

//        double walkFrameIncrement = 1.5 * Math.pow(Math.sin((Math.PI * 0.05) * (frame - 9)), 2) + 0.25;
//        walkFrame += walkFrameIncrement;

        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = (float) Math.sqrt(moveX * moveX + moveZ * moveZ);
        if (speed > 0.01) {
            if (getAnimation() == NO_ANIMATION) {
                walkAnim.increaseTimer();
            }
        } else {
            walkAnim.decreaseTimer();
        }
        if (getAnimation() != NO_ANIMATION) {
            walkAnim.decreaseTimer(2);
        }

        if (frame % 20 == 5 && speed > 0.03 && getAnimation() == NO_ANIMATION && active) {
            playSound("mob.zombie.metal", 0.5F, 0.5F);
        }

//        List<EntityLivingBase> nearestEntities = getEntityLivingBaseNearby(2.2, 2.2, 4, 2.2);
//        for (Entity entity : nearestEntities)
//        {
//            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
//            entity.motionX = -0.1 * Math.cos(angle);
//            entity.motionZ = -0.1 * Math.sin(angle);
//        }
        repelEntities(2.2f, 4, 2.2f, 2.2f);

        if (!active && getAttackTarget() == null) {
            addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 1, true));
        }
    }

    public void onSpawn() {
        setRestPosX((int) posX);
        setRestPosY((int) posY);
        setRestPosZ((int) posZ);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
        onSpawn();
        return super.onSpawnWithEgg(p_110161_1_);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(29, 0);
        dataWatcher.addObject(30, 0);
        dataWatcher.addObject(31, 0);
        dataWatcher.addObject(28, (byte) 0);
    }

    public int getRestPosX() {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setRestPosX(Integer restPosX) {
        dataWatcher.updateObject(29, restPosX);
    }

    public int getRestPosY() {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void setRestPosY(Integer restPosY) {
        dataWatcher.updateObject(30, restPosY);
    }

    public int getRestPosZ() {
        return dataWatcher.getWatchableObjectInt(31);
    }

    public void setRestPosZ(Integer restPosZ) {
        dataWatcher.updateObject(31, restPosZ);
    }

    public byte getActive() {
        return dataWatcher.getWatchableObjectByte(28);
    }

    public void setActive(Byte active) {
        dataWatcher.updateObject(28, active);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("restPosX", getRestPosX());
        compound.setInteger("restPosY", getRestPosY());
        compound.setInteger("restPosZ", getRestPosZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setRestPosX(compound.getInteger("restPosX"));
        setRestPosY(compound.getInteger("restPosY"));
        setRestPosZ(compound.getInteger("restPosZ"));
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
            dropItem(ItemHandler.INSTANCE.wrought_axe, 1);
            dropItem(ItemHandler.INSTANCE.wrought_helmet, 1);
        }
        super.onDeath(p_70645_1_);
    }

    @Override
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION, VERTICAL_ATTACK_ANIMATION, ACTIVATE_ANIMATION, DEACTIVATE_ANIMATION};
    }
}