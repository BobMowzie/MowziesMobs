package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerHandler {
    public static final MenuType<ContainerBarakoayaTrade> CONTAINER_BARAKOAYA_TRADE = new MenuType<>(ContainerBarakoayaTrade::new);
    public static final MenuType<ContainerBarakoTrade> CONTAINER_BARAKO_TRADE = new MenuType<>(ContainerBarakoTrade::new);
    public static final MenuType<ContainerSculptorTrade> CONTAINER_SCULPTOR_TRADE = new MenuType<>(ContainerSculptorTrade::new);

    @SubscribeEvent
    public static void registerAll(RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().registerAll(
                CONTAINER_BARAKOAYA_TRADE.setRegistryName("barakoaya_trade"),
                CONTAINER_BARAKO_TRADE.setRegistryName("barako_trade"),
                CONTAINER_SCULPTOR_TRADE.setRegistryName("sculptor_trade")
        );
    }
}
