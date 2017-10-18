package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDart extends EntityTippedArrow {
	public EntityDart(World world) {
		super(world);
	}

	public EntityDart(World world, EntityLivingBase shooter) {
		super(world, shooter);
		setDamage(1);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ItemHandler.INSTANCE.dart);
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		Entity hit = raytraceResultIn.entityHit;
		if (hit == null || !(hit instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) hit;
		if (world.isRemote || (shootingEntity == hit) || (shootingEntity instanceof EntityBarakoa && living instanceof EntityBarakoa && ((EntityBarakoa) shootingEntity).isBarakoDevoted() == ((EntityBarakoa) living).isBarakoDevoted()))
			return;
		super.onHit(raytraceResultIn);
		if (shootingEntity instanceof EntityPlayer)
			living.addPotionEffect(new PotionEffect(MobEffects.POISON, 40, 3, false, true));
		else living.addPotionEffect(new PotionEffect(MobEffects.POISON, 40, 1, false, true));
		living.setArrowCountInEntity(living.getArrowCountInEntity() - 1);
	}
}