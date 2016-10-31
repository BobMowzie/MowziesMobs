package com.bobmowzie.mowziesmobs.server.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;

public class ItemDart extends ItemArrow {
    public ItemDart() {
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("dart");
        setRegistryName("dart");
    }

    @Override
    public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter) {
        return new EntityDart(world, shooter);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        ItemHandler.addItemText(this, tooltip);
    }

    public static DamageSource causeArrowDamage(EntityDart entitydart, Entity entity) {
        return new EntityDamageSourceIndirect("dart", entitydart, entity).setProjectile();
    }
}
