package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public class JigsawHandler {
    public static StructurePoolElementType<MowziePoolElement> TWO_WAY_ELEMENT;

    public static void registerJigsawElements() {
        TWO_WAY_ELEMENT = register("mowzie_element", MowziePoolElement.CODEC);
    }

    private static <P extends StructurePoolElement> StructurePoolElementType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_POOL_ELEMENT, new ResourceLocation(MowziesMobs.MODID, name), () -> codec);
    }
}
