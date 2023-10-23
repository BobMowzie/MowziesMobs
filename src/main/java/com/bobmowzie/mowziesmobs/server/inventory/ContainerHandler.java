package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerHandler {
    public static final MenuType<ContainerUmvuthanaTrade> CONTAINER_UMVUTHANA_TRADE = new MenuType<>(ContainerUmvuthanaTrade::new);
    public static final MenuType<ContainerUmvuthiTrade> CONTAINER_UMVUTHI_TRADE = new MenuType<>(ContainerUmvuthiTrade::new);
    public static final MenuType<ContainerSculptorTrade> CONTAINER_SCULPTOR_TRADE = new MenuType<>(ContainerSculptorTrade::new);

    @SubscribeEvent
    public static void registerAll(RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().registerAll(
                CONTAINER_UMVUTHANA_TRADE.setRegistryName("umvuthana_trade"),
                CONTAINER_UMVUTHI_TRADE.setRegistryName("umvuthi_trade"),
                CONTAINER_SCULPTOR_TRADE.setRegistryName("sculptor_trade")
        );
    }
}
