package com.bobmowzie.mowziesmobs.server.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum PotionHandler {
	INSTANCE;

	public Potion frozen;
	public Potion sunsBlessing;
	public Potion geomancy;

	private void registerPotions() {
		sunsBlessing = new MowziePotionSunsBlessing();
		GameRegistry.register(sunsBlessing);
		geomancy = new MowziePotionGeomancy();
		GameRegistry.register(geomancy);
		frozen = new MowziePotionFrozen();
		GameRegistry.register(frozen);
	}

	public void onInit() {
		registerPotions();
	}
}
