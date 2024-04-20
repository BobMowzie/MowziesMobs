package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerHandler {
    public static final DeferredRegister<MenuType<?>> REG = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MowziesMobs.MODID);
    
    public static final MenuType<ContainerUmvuthanaTrade> UMVUTHANA_TRADE = new MenuType<>(ContainerUmvuthanaTrade::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<ContainerUmvuthiTrade> UMVUTHI_TRADE = new MenuType<>(ContainerUmvuthiTrade::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<ContainerSculptorTrade> SCULPTOR_TRADE = new MenuType<>(ContainerSculptorTrade::new, FeatureFlags.VANILLA_SET);
    
    public static final RegistryObject<MenuType<ContainerUmvuthanaTrade>> CONTAINER_UMVUTHANA_TRADE = REG.register("umvuthana_trade", () -> UMVUTHANA_TRADE);
    public static final RegistryObject<MenuType<ContainerUmvuthiTrade>> CONTAINER_UMVUTHI_TRADE = REG.register("umvuthi_trade", () -> UMVUTHI_TRADE);
    public static final RegistryObject<MenuType<ContainerSculptorTrade>> CONTAINER_SCULPTOR_TRADE = REG.register("sculptor_trade", () -> SCULPTOR_TRADE);
}
