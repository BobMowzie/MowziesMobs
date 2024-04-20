package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public class JigsawHandler {
    public static StructurePoolElementType<MowziePoolElement> MOWZIE_ELEMENT;
    public static StructurePoolElementType<FallbackPoolElement> FALLBACK_ELEMENT;

    public static void registerJigsawElements() {
        MOWZIE_ELEMENT = register("mowzie_element", MowziePoolElement.CODEC);
        FALLBACK_ELEMENT = register("fallback_element", FallbackPoolElement.CODEC);
    }

    private static <P extends StructurePoolElement> StructurePoolElementType<P> register(String name, Codec<P> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, new ResourceLocation(MowziesMobs.MODID, name), () -> codec);
    }
}
