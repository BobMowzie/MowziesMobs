package com.bobmowzie.mowziesmobs.datagen;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;


@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, provider);
        generator.addProvider(event.includeServer(), datapackProvider);

    }
}
