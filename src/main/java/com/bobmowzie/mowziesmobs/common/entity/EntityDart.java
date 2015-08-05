package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 8/4/15.
 */
public class EntityDart extends EntityArrow {
    public EntityDart(World p_i1753_1_) {
        super(p_i1753_1_);
    }

    public EntityDart(World p_i1754_1_, double p_i1754_2_, double p_i1754_4_, double p_i1754_6_)
    {
        super(p_i1754_1_, p_i1754_2_, p_i1754_4_, p_i1754_6_);
    }

    public EntityDart(World p_i1755_1_, EntityLivingBase p_i1755_2_, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
    {
        super(p_i1755_1_, p_i1755_2_, p_i1755_3_, p_i1755_4_, p_i1755_5_);
    }

    public EntityDart(World p_i1756_1_, EntityLivingBase p_i1756_2_, float p_i1756_3_)
    {
        super(p_i1756_1_, p_i1756_2_, p_i1756_3_);
    }
}