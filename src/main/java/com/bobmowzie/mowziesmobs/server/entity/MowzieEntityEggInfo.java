package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class MowzieEntityEggInfo {
	public final ResourceLocation id;

	public final Class<? extends EntityLiving> clazz;

	public final int primaryColor;

	public final int secondaryColor;

	public MowzieEntityEggInfo(ResourceLocation id, Class<? extends EntityLiving> clazz, int primaryColor, int secondaryColor) {
		this.id = id;
		this.clazz = clazz;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}
}
