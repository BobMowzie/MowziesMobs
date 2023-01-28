package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ProcessorHandler {
    public static StructureProcessorType<BaseProcessor> BASE_PROCESSOR = () -> BaseProcessor.CODEC;

    public static void registerStructureProcessors() {
        register("mowzie_element", BASE_PROCESSOR);
    }

    private static void register(String name, StructureProcessorType<?> codec) {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MowziesMobs.MODID, name), codec);
    }
}
