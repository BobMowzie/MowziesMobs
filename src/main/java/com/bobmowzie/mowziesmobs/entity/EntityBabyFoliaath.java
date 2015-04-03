package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.ai.animation.AnimBabyFoliaathEat;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

import java.util.ArrayList;
import java.util.List;

public class EntityBabyFoliaath extends MMEntityBase
{
    public ControlledAnimation activate = new ControlledAnimation(5);
    private int eatingItemID;
    private double prevActivate;

    public EntityBabyFoliaath(World world) {
        super(world);
        tasks.addTask(1, new AnimBabyFoliaathEat(this, 20));
        setSize(0.4F, 0.4F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1);
    }

    public void onUpdate()
    {
        super.onUpdate();
        motionX = 0;
        motionZ = 0;
        renderYawOffset = 0;

        if (arePlayersCarryingMeat(getPlayersNearby(3, 3, 3, 3)) && getAnimID() == 0 && getHungry() == 1)
        {
            activate.increaseTimer();
        }
        else activate.decreaseTimer();

        if (activate.getTimer() == 1 && prevActivate-activate.getTimer() < 0) playSound("mowziesmobs:foliaathgrunt", 0.5F, 1.5F);
        prevActivate = activate.getTimer();

        List<EntityItem> meats = getMeatsNearby(0.4, 0.2, 0.4, 0.4);
        if (getHungry() == 1 && meats.size() != 0 && getAnimID() == 0) {
            AnimationAPI.sendAnimPacket(this, MMAnimation.BABY_FOLIAATH_EAT.animID());
            eatingItemID = Item.getIdFromItem(meats.get(0).getEntityItem().getItem());
            meats.get(0).setDead();
            playSound("mowziesmobs:babyFoliaathEat", 0.5F, 1.2F);
            if (!worldObj.isRemote)
            {
                incrementGrowth();
                setHungry((byte) 0);
            }
        }

        if (getAnimTick() == 3 || getAnimTick() == 7 || getAnimTick() == 11 || getAnimTick() == 15 || getAnimTick() == 19)
        {
            for (int i = 0; i <= 5; i++) worldObj.spawnParticle("iconcrack_" + eatingItemID + "_1", posX, posY + 0.2, posZ, Math.random() * 0.2 - 0.1, Math.random() * 0.2, Math.random() * 0.2 - 0.1);
        }

        //Growing
        if (!worldObj.isRemote) {
            if (ticksExisted % 20 == 0 && getHungry() == 0) incrementGrowth();
            if (getGrowth() < 60) setInfant((byte) 1);
            else setInfant((byte) 0);
            if (getInfant() == 1) setHungry((byte) 0);
            if (getGrowth() == 60) setHungry((byte) 1);
            if (getGrowth() == 120) setHungry((byte) 1);
            if (getGrowth() == 180) setHungry((byte) 1);
            if (getGrowth() == 240)
            {
                EntityFoliaath adultFoliaath = new EntityFoliaath(worldObj);
                adultFoliaath.setPosition(posX, posY, posZ);
                worldObj.spawnEntityInWorld(adultFoliaath);
                setDead();
            }
        }
    }

    private boolean arePlayersCarryingMeat(List<EntityPlayer> players)
    {
        if (players.size() != 0) {
            for (EntityPlayer player : players)
            {
                String itemName = "";
                if (player.getHeldItem() != null) itemName = player.getHeldItem().getUnlocalizedName();
                if (itemName.contains("item.porkchop") || itemName.contains("item.beef") || itemName.contains("item.chicken") || itemName.contains("item.fish") || itemName.equals("item.rottenFlesh") || itemName.equals("item.spiderEye")) return true;
            }
        }
        return false;
    }

    @Override
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);
        for (int i = 0; i < 10; i++)
        {
            worldObj.spawnParticle("blockcrack_18_3", posX, posY + 0.2, posZ, 0, 0, 0);
        }
        setDead();
    }

    @Override
    public void applyEntityCollision(Entity collider)
    {
        posX = prevPosX;
        posZ = prevPosZ;
    }

    @Override
    protected String getDeathSound()
    {
        playSound("dig.grass", 1, 0.8F);
        return null;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        if (this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox)) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.boundingBox.minY);
            int k = MathHelper.floor_double(this.posZ);

            Block block = this.worldObj.getBlock(i, j - 1, k);

            if (block == Blocks.grass || block.isLeaves(worldObj, i, j - 1, k)|| block == Blocks.dirt)
            {
                playSound("dig.grass", 1, 0.8F);
                return true;
            }
        }
        return false;
    }

    public List<EntityItem> getMeatsNearby(double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityItem> listEntityItem = new ArrayList<EntityItem>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityItem && getDistanceToEntity(entityNeighbor) <= radius)
            {
                String itemName = ((EntityItem)entityNeighbor).getEntityItem().getUnlocalizedName();
                if (itemName.contains("item.porkchop") || itemName.contains("item.beef") || itemName.contains("item.chicken") || itemName.contains("item.fish") || itemName.equals("item.rottenFlesh") || itemName.equals("item.spiderEye")) listEntityItem.add((EntityItem) entityNeighbor);
            }
        }
        return listEntityItem;
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("tickGrowth", getGrowth());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setGrowth(compound.getInteger("tickGrowth"));
  //      MowziesMobs.networkWrapper.sendToAll(new PacketSyncTickGrowth(getEntityId(), tickGrowth));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(30, new Integer(0));
        dataWatcher.addObject(31, new Byte((byte) 1));
        dataWatcher.addObject(29, new Byte((byte) 0));
    }

    public void setGrowth(int growth) {
        dataWatcher.updateObject(30, new Integer(growth));
    }

    public int getGrowth() {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void incrementGrowth() {
        setGrowth(getGrowth() + 1);
    }

    public void setInfant(byte infant) {
        dataWatcher.updateObject(31, new Byte(infant));
    }

    public byte getInfant() {
        return dataWatcher.getWatchableObjectByte(31);
    }

    public void setHungry(byte hungry) {
        dataWatcher.updateObject(29, new Byte(hungry));
    }

    public byte getHungry() {
        return dataWatcher.getWatchableObjectByte(29);
    }
}