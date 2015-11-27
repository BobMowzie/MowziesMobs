package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.animation.MMAnimBase;
import net.ilexiconn.llibrary.client.model.modelbase.ControlledAnimation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

/**
 * Created by jnad325 on 7/9/15.
 */
public class EntityTribeLeader extends MMEntityBase {
    int direction = 0;
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    private boolean blocksByFeet = true;
    public int whichDialogue = 0;
    public EntityTribeLeader(World world) {
        super(world);
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
        tasks.addTask(2, new MMAnimBase(this, 1, 40, false));
        tasks.addTask(2, new MMAnimBase(this, 2, 80, false));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityTribesman.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        setSize(1.5f, 2.4f);
        if (getDirection() == 0) setDirection(rand.nextInt(4) + 1);

    }

    public EntityTribeLeader(World world, int direction) {
        super(world);
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
        tasks.addTask(2, new MMAnimBase(this, 1, 40));
        setSize(1.5f, 2.4f);
        setDirection(direction);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected String getLivingSound() {
        if (getAnimID() == 0) {
            if (getAttackTarget() == null) {
                int i = MathHelper.getRandomIntegerInRange(rand, 1, 10);
                if (i == 1) playSound("mowziesmobs:barakoTalk1", 1.4f, 1);
                if (i == 2) playSound("mowziesmobs:barakoTalk2", 1.4f, 1);
                if (i == 3) playSound("mowziesmobs:barakoTalk3", 1.4f, 1);
                if (i == 4) playSound("mowziesmobs:barakoTalk4", 1.4f, 1);
                if (i == 5) playSound("mowziesmobs:barakoTalk5", 1.4f, 1);
                if (i == 6) playSound("mowziesmobs:barakoTalk6", 1.4f, 1);
                if (i < 7)
                {
                    setWhichDialogue(i);
                    AnimationAPI.sendAnimPacket(this, 2);
                }
            } else {
            }
        }
        return null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1) direction = getDirection();
        repelEntities(2.2f, 2.5f, 2.2f, 2.2f);
        rotationYaw = (direction - 1) * 90;
        renderYawOffset = rotationYaw;
        posX = prevPosX;
        posZ = prevPosZ;

        if (getAttackTarget() != null) {
            EntityLivingBase target = getAttackTarget();
        }
        else {
        }

        if (ticksExisted % 20 == 0) {
            if (checkBlocksByFeet()) blocksByFeet = true;
            else blocksByFeet = false;
        }

        if (blocksByFeet) legsUp.increaseTimer();
        else legsUp.decreaseTimer();

        if (getAnimID() == 0 && rand.nextInt(200) == 0) AnimationAPI.sendAnimPacket(this, 1);

        if (getAnimID() == 1 && (getAnimTick() == 9 || getAnimTick() == 29)) playSound("mowziesmobs:barakoBelly", 1.4f, 1f);

        if (getAnimID() == 2 && getAnimTick() == 1) whichDialogue = getWhichDialogue();

//        if (getAnimID() == 0) getLivingSound();
    }

    private boolean checkBlocksByFeet()
    {
        Block blockLeft;
        Block blockRight;
//        System.out.println(direction);
        if (direction == 1) {
            blockLeft = worldObj.getBlock(MathHelper.floor_double(posX) + 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) + 1);
            blockRight = worldObj.getBlock(MathHelper.floor_double(posX) - 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) + 1);
        }
        else if (direction == 2) {
            blockLeft = worldObj.getBlock(MathHelper.floor_double(posX) - 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) + 1);
            blockRight = worldObj.getBlock(MathHelper.floor_double(posX) - 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) - 1);
        }
        else if (direction == 3) {
            blockLeft = worldObj.getBlock(MathHelper.floor_double(posX) - 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) - 1);
            blockRight = worldObj.getBlock(MathHelper.floor_double(posX) + 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) - 1);
        }
        else if (direction == 4) {
            blockLeft = worldObj.getBlock(MathHelper.floor_double(posX) + 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) - 1);
            blockRight = worldObj.getBlock(MathHelper.floor_double(posX) + 1, Math.round((float)(posY - 1)), MathHelper.floor_double(posZ) + 1);
        }
        else return false;

        if (blockLeft instanceof BlockAir && blockRight instanceof BlockAir) return false;
        return true;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(28, 0);
        dataWatcher.addObject(29, 0);
    }

    public int getDirection()
    {
        return dataWatcher.getWatchableObjectInt(28);
    }

    public void setDirection(Integer direction)
    {
        dataWatcher.updateObject(28, direction);
    }

    public int getWhichDialogue()
    {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setWhichDialogue(Integer i)
    {
        dataWatcher.updateObject(29, i);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("direction", getDirection());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setDirection(compound.getInteger("direction"));
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {

    }
}
