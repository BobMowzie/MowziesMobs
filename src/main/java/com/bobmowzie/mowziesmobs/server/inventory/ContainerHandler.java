package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerHandler {
    public static final ContainerType<ContainerBarakoayaTrade> CONTAINER_BARAKOAYA_TRADE = new ContainerType<>(ContainerBarakoayaTrade::new);
    public static final ContainerType<ContainerBarakoTrade> CONTAINER_BARAKO_TRADE = new ContainerType<>(ContainerBarakoTrade::new);

    @SubscribeEvent
    public static void registerAll(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().registerAll(
                CONTAINER_BARAKOAYA_TRADE.setRegistryName("barakoaya_trade"),
                CONTAINER_BARAKO_TRADE.setRegistryName("barako_trade")
        );
    }
}
