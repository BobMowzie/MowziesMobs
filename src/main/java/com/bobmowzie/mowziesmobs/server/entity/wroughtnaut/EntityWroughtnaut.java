package com.bobmowzie.mowziesmobs.server.entity.wroughtnaut;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
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
    public static final Animation DIE_ANIMATION = Animation.create(130);

    public static final Animation HURT_ANIMATION = Animation.create(15);

    public static final Animation ATTACK_ANIMATION = Animation.create(50);

    public static final Animation VERTICAL_ATTACK_ANIMATION = Animation.create(105);

    public static final Animation STOMP_ATTACK_ANIMATION = Animation.create(40);

    public static final Animation ACTIVATE_ANIMATION = Animation.create(45);

    public static final Animation DEACTIVATE_ANIMATION = Animation.create(15);

    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            ATTACK_ANIMATION,
            VERTICAL_ATTACK_ANIMATION,
            STOMP_ATTACK_ANIMATION,
            ACTIVATE_ANIMATION,
            DEACTIVATE_ANIMATION
    };

    private static final String[] LIVING_SOUNDS = {
            "mowziesmobs:wroughtnautGrunt1",
            "mowziesmobs:wroughtnautGrunt3",
            "mowziesmobs:wroughtnautShout1",
            "mowziesmobs:wroughtnautShout2",
            "mowziesmobs:wroughtnautShout3"
    };

    private static final float[][] VERTICAL_ATTACK_BLOCK_OFFSETS = {
            {-0.1F, -0.1F},
            {-0.1F, 0.1F},
            {0.1F, 0.1F},
            {0.1F, -0.1F}
    };

    public ControlledAnimation walkAnim = new ControlledAnimation(10);

    public boolean swingDirection;

    public boolean vulnerable;

    private int attacksWithoutVertical;

    private int ticksSinceLastStomp;

    private CeilingDisturbance disturbance;

    public EntityWroughtnaut(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimationFWNAttackAI(this, ATTACK_ANIMATION, "mowziesmobs:wroughtnautWhoosh", 4F, 5.5F, 100F));
        tasks.addTask(1, new AnimationFWNVerticalAttackAI(this, VERTICAL_ATTACK_ANIMATION, "mowziesmobs:wroughtnautWhoosh", 1F, 5.5F, 40F));
        tasks.addTask(1, new AnimationFWNStompAttackAI(this, STOMP_ATTACK_ANIMATION));
        tasks.addTask(1, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(1, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        tasks.addTask(1, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1, true));
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
    protected void fall(float distance) {
        distance = ForgeHooks.onLivingFall(this, distance);
        if (distance <= 0) {
            return;
        }
        super.fall(distance);
        PotionEffect jump = getActivePotionEffect(Potion.jump);
        float jumpAid = jump != null ? jump.getAmplifier() + 1 : 0;
        int damage = MathHelper.ceiling_float_int(distance - 3 - jumpAid);
        if (damage > 0) {
            playSound(func_146067_o(damage), 1, 1);
            attackEntityFrom(DamageSource.fall, damage);
            int x = MathHelper.floor_double(posX);
            int y = MathHelper.floor_double(posY - 0.2 - yOffset);
            int z = MathHelper.floor_double(posZ);
            Block block = worldObj.getBlock(x, y, z);
            if (block.getMaterial() != Material.air) {
                Block.SoundType step = block.stepSound;
                playSound(step.getStepResourcePath(), step.getVolume() * 0.5F, step.getPitch() * 0.75F);
            }
        }
    }

    @Override
    protected String getLivingSound() {
        if (getAnimation() == NO_ANIMATION && isActive()) {
            playSound(LIVING_SOUNDS[rand.nextInt(LIVING_SOUNDS.length)], 1, 1);
        }
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
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
                    playSound("minecraft:random.anvil_land", 0.4F, 2);
                    return false;
                } else {
                    if (currentAnim != null) {
                        currentAnim.resetTask();
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);
                    }
                    return super.attackEntityFrom(source, amount);
                }
            } else {
                playSound("minecraft:random.anvil_land", 0.4F, 2);
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
        if (!worldObj.isRemote) {
            if (getAnimation() == NO_ANIMATION) {
                if (isActive()) {
                    if (getAttackTarget() == null && moveForward == 0 && Math.abs(posX - getRestPosX()) <= 4 && Math.abs(posY - getRestPosY()) <= 4 && Math.abs(posZ - getRestPosZ()) <= 4) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        setActive(false);
                    }
                } else if (getAttackTarget() != null && targetDistance <= 5) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    setActive(true);
                }
            }
            ticksSinceLastStomp++;
        }

        if (!isActive()) {
            posX = prevPosX;
            posZ = prevPosZ;
            rotationYaw = prevRotationYaw;
        }
        renderYawOffset = rotationYaw;

        if (getAttackTarget() != null && isActive()) {
            if (getAnimation() == NO_ANIMATION) {
                getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.2);
            } else {
                getNavigator().clearPathEntity();
            }
            if (getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 3 && getAnimation() == NO_ANIMATION) {
                boolean couldStomp = targetDistance < 6 && ticksSinceLastStomp > 600;
                if (targetDistance < 3.5 && Math.abs(MathHelper.wrapAngleTo180_double(getAngleBetweenEntities(getAttackTarget(), this) - rotationYaw)) < 35 && (!couldStomp || rand.nextInt(3) > 0)) {
                    if (attacksWithoutVertical >= 4 || rand.nextInt(4) == 0) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, VERTICAL_ATTACK_ANIMATION);
                        attacksWithoutVertical = 0;
                    } else {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
                        attacksWithoutVertical++;
                    }
                } else if (couldStomp) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, STOMP_ATTACK_ANIMATION);
                    ticksSinceLastStomp = 0;
                    attacksWithoutVertical++;
                }
            }
        } else if (!getNavigator().tryMoveToXYZ(getRestPosX(), getRestPosY(), getRestPosZ(), 0.2)) {
            setRestPosX((int) posX);
            setRestPosY((int) posY);
            setRestPosZ((int) posZ);
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 1) {
            swingDirection = rand.nextBoolean();
        } else if (getAnimation() == ACTIVATE_ANIMATION) {
            int tick = getAnimationTick();
            if (tick == 1) {
                playSound("mowziesmobs:wroughtnautGrunt2", 1, 1);
            } else if (tick == 27 || tick == 44) {
                playSound("mob.zombie.metal", 0.5F, 0.5F);
            }
        } else if (getAnimation() == VERTICAL_ATTACK_ANIMATION && getAnimationTick() == 29) {
            doVerticalAttackHitFX();
        }

        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = MathHelper.sqrt_float(moveX * moveX + moveZ * moveZ);
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

        repelEntities(2.2F, 4, 2.2F, 2.2F);

        if (!active && getAttackTarget() == null) {
            addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 1, true));
        }

        if (disturbance != null) {
            if (disturbance.update()) {
                disturbance = null;
            }
        }
    }

    private void doVerticalAttackHitFX() {
        double theta = (renderYawOffset - 4) * (Math.PI / 180);
        double perpX = Math.cos(theta);
        double perpZ = Math.sin(theta);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double x = posX + 4.2 * vecX;
        double y = boundingBox.minY + 0.1;
        double z = posZ + 4.2 * vecZ;
        int hitY = MathHelper.floor_double(posY - 0.2);
        for (int t = 0; t < VERTICAL_ATTACK_BLOCK_OFFSETS.length; t++) {
            float ox = VERTICAL_ATTACK_BLOCK_OFFSETS[t][0], oy = VERTICAL_ATTACK_BLOCK_OFFSETS[t][1];
            int hitX = MathHelper.floor_double(x + ox);
            int hitZ = MathHelper.floor_double(z + oy);
            Block block = worldObj.getBlock(hitX, hitY, hitZ);
            if (block.getRenderType() != -1) {
                String particle = "blockcrack_" + Block.getIdFromBlock(block) + "_" + worldObj.getBlockMetadata(hitX, hitY, hitZ);
                for (int n = 0; n < 6; n++) {
                    double pa = rand.nextDouble() * 2 * Math.PI;
                    double pd = rand.nextDouble() * 0.6 + 0.1;
                    double px = x + Math.cos(pa) * pd;
                    double pz = z + Math.sin(pa) * pd;
                    double magnitude = rand.nextDouble() * 4 + 5;
                    double velX = perpX * magnitude;
                    double velY = rand.nextDouble() * 3 + 6;
                    double velZ = perpZ * magnitude;
                    if (vecX * (pz - posZ) - vecZ * (px - posX) > 0) {
                        velX = -velX;
                        velZ = -velZ;
                    }
                    worldObj.spawnParticle(particle, px, y, pz, velX, velY, velZ);
                }
            }
        }
        int hitX = MathHelper.floor_double(x);
        int ceilY = MathHelper.floor_double(boundingBox.maxY);
        int hitZ = MathHelper.floor_double(z);
        final int maxHeight = 5;
        int height = maxHeight;
        for (; height --> 0; ceilY++) {
            if (worldObj.getBlock(hitX, ceilY, hitZ).getMaterial().blocksMovement()) {
                break;
            }
        }
        float strength = height / (float) maxHeight;
        if (strength >= 0) {
            int radius = MathHelper.ceiling_float_int(MathHelper.sqrt_float(1 - strength * strength) * maxHeight);
            disturbance = new CeilingDisturbance(hitX, ceilY, hitZ, radius, rand.nextInt(5) + 3, rand.nextInt(60) + 20);
        }
    }

    private class CeilingDisturbance {
        private final int ceilX, ceilY, ceilZ;

        private final int radius;

        private int delay;

        private int remainingTicks;

        private int duration;

        public CeilingDisturbance(int x, int y, int z, int radius, int delay, int remainingTicks) {
            this.ceilX = x;
            this.ceilY = y;
            this.ceilZ = z;
            this.radius = radius;
            this.delay = delay;
            this.remainingTicks = remainingTicks;
            duration = remainingTicks;
        }

        public boolean update() {
            if (--delay > 0) {
                return false;
            }
            float t = remainingTicks / (float) duration;
            int amount = MathHelper.ceiling_float_int((1 - MathHelper.sqrt_double(1 - t * t)) * radius * radius * 0.15F);
            boolean playSound = true;
            while (amount --> 0) {
                double theta = rand.nextDouble() * Math.PI * 2;
                double dist = rand.nextDouble() * radius;
                double x = ceilX + Math.cos(theta) * dist;
                double y = ceilY - 0.1 - rand.nextDouble() * 0.3;
                double z = ceilZ + Math.sin(theta) * dist;
                int blockX = MathHelper.floor_double(x);
                int blockZ = MathHelper.floor_double(z);
                Block block = worldObj.getBlock(blockX, ceilY, blockZ);
                if (block.getRenderType() != -1) {
                    String particle = "blockdust_" + Block.getIdFromBlock(block) + "_" + worldObj.getBlockMetadata(blockX, ceilY, blockZ);
                    worldObj.spawnParticle(particle, x, y, z, 0, 0, 0);
                    if (playSound && rand.nextFloat() < 0.075F) {
                        SoundType sound = block.stepSound;
                        worldObj.playSound(posX, posY, posZ, sound.getBreakSound(), sound.getVolume() * 2, sound.getPitch() * 0.6F, false);
                        playSound = false;
                    }
                }
            }
            return --remainingTicks <= 0;
        }
    }

    public void onSpawn() {
        setRestPosX((int) posX);
        setRestPosY((int) posY);
        setRestPosZ((int) posZ);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        onSpawn();
        return super.onSpawnWithEgg(data);
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

    public void setRestPosX(int restPosX) {
        dataWatcher.updateObject(29, restPosX);
    }

    public int getRestPosY() {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void setRestPosY(int restPosY) {
        dataWatcher.updateObject(30, restPosY);
    }

    public int getRestPosZ() {
        return dataWatcher.getWatchableObjectInt(31);
    }

    public void setRestPosZ(int restPosZ) {
        dataWatcher.updateObject(31, restPosZ);
    }

    public boolean isActive() {
        return dataWatcher.getWatchableObjectByte(28) != 0;
    }

    public void setActive(boolean isActive) {
        dataWatcher.updateObject(28, (byte) (isActive ? 1 : 0));
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
    public void onDeath(DamageSource source) {
        if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
            dropItem(ItemHandler.INSTANCE.wrought_axe, 1);
            dropItem(ItemHandler.INSTANCE.wrought_helmet, 1);
        }
        super.onDeath(source);
    }

    @Override
    protected void func_145780_a(int x, int y, int z, Block block) {
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }
}