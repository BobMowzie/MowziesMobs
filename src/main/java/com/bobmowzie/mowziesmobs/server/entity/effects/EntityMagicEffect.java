package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * Created by Josh on 9/2/2018.
 */
public abstract class EntityMagicEffect extends Entity {
    public EntityLivingBase caster;
    private static final DataParameter<Integer> CASTER = EntityDataManager.createKey(EntityMagicEffect.class, DataSerializers.VARINT);

    public EntityMagicEffect(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        getDataManager().register(CASTER, -1);
    }

    public int getCasterID() {
        return getDataManager().get(CASTER);
    }

    public void setCasterID(int id) {
        getDataManager().set(CASTER, id);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1) {
            caster = (EntityLivingBase) world.getEntityByID(getCasterID());
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
