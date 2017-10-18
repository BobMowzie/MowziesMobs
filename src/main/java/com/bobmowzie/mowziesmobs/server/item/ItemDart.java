package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDart extends ItemArrow {
	public ItemDart() {
		setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
		setUnlocalizedName("dart");
		setRegistryName("dart");
	}

	public static DamageSource causeArrowDamage(EntityDart entitydart, Entity entity) {
		return new EntityDamageSourceIndirect("dart", entitydart, entity).setProjectile();
	}

	@Override
	public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter) {
		return new EntityDart(world, shooter);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		ItemHandler.addItemText(this, tooltip);
	}
}
