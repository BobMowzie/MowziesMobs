package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;

public class EntityDart extends EntityArrow {
    public EntityDart(World world) {
        super(world);
    }

    public EntityDart(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemHandler.INSTANCE.dart);
    }
}