package com.bobmowzie.mowziesmobs.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityCreature;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class MMEntityBase extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity
{
    private int animTick;
    private int animID;
    public int frame;

    public MMEntityBase(World world)
    {
        super(world);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        //Server
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        //Server
    }

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int p_71124_1_) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {

    }

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[0];
    }

    @Override
    public void writeSpawnData(ByteBuf buf)
    {
        //Client
    }

    @Override
    public void readSpawnData(ByteBuf buf)
    {
        //Client
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        frame++;
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public void setAnimID(int i) {

    }

    @Override
    public void setAnimTick(int i) {

    }

    @Override
    public int getAnimID() {
        return 0;
    }

    @Override
    public int getAnimTick() {
        return 0;
    }
}
