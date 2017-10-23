package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.EnumMap;
import java.util.List;

public enum ItemHandler {
	INSTANCE;

	public Item foliaathSeed;
	public Item testStructure;
	public Item mobRemover;
	public Item wroughtAxe;
	public Item wroughtHelmet;
	public EnumMap<MaskType, ItemBarakoaMask> barakoaMasks;
	public ItemBarakoMask barakoMask;
	public Item dart;
	public Item spear;
	public Item blowgun;
	public Item spawnEgg;
	public Item grantSunsBlessing;
	public Item iceCrystal;
	public Item earthTalisman;

	public static void addItemText(Item item, List<String> lines) {
		String keyStart = item.getUnlocalizedName() + ".text.";
		for (int line = 0; ; line++) {
			String key = keyStart + line;
			if (I18n.canTranslate(key)) {
				lines.add(I18n.translateToLocal(key));
			} else {
				break;
			}
		}
	}

	public void onInit() {
		foliaathSeed = new ItemFoliaathSeed();
		testStructure = new ItemTestStructure();
		mobRemover = new ItemMobRemover();
		wroughtAxe = new ItemWroughtAxe();
		wroughtHelmet = new ItemWroughtHelm();
		barakoaMasks = MaskType.newEnumMap(ItemBarakoaMask.class);
		for (MaskType mask : MaskType.values()) {
			barakoaMasks.put(mask, new ItemBarakoaMask(mask));
		}
		barakoMask = new ItemBarakoMask();
		dart = new ItemDart();
		spear = new ItemSpear();
		blowgun = new ItemBlowgun();
		iceCrystal = new ItemIceCrystal();
		spawnEgg = new ItemSpawnEgg();
		grantSunsBlessing = new ItemGrantSunsBlessing();
		earthTalisman = new ItemEarthTalisman();


		GameRegistry.register(spawnEgg);

		GameRegistry.register(foliaathSeed);
		GameRegistry.register(wroughtAxe);
		GameRegistry.register(wroughtHelmet);
		for (ItemBarakoaMask itemBarakoaMask : barakoaMasks.values()) {
			GameRegistry.register(itemBarakoaMask);
		}
		GameRegistry.register(barakoMask);
		GameRegistry.register(spear);
		GameRegistry.register(blowgun);
		GameRegistry.register(dart);
		GameRegistry.register(iceCrystal);

		GameRegistry.register(grantSunsBlessing);
		GameRegistry.register(earthTalisman);
		GameRegistry.register(mobRemover);
//        GameRegistry.register(testStructure);
	}
}
