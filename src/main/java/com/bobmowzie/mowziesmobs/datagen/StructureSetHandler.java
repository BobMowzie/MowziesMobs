package com.bobmowzie.mowziesmobs.datagen;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.bobmowzie.mowziesmobs.datagen.StructureHandler;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.*;

public class StructureSetHandler {

    public static ResourceKey<StructureSet> WROUGHT_CHAMBERS = createSetKey("wrought_chambers");
    public static ResourceKey<StructureSet> UMVUTHANA_GROVES = createSetKey("umvuthana_groves");
    public static ResourceKey<StructureSet> FROSTMAWS = createSetKey("frostmaw_spawns");
    public static ResourceKey<StructureSet> MONASTERIES = createSetKey("monasteries");

    private static ResourceKey<StructureSet> createSetKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(MowziesMobs.MODID, name));
    }

    public static void bootstrap(BootstapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(WROUGHT_CHAMBERS, new StructureSet(structures.getOrThrow(StructureHandler.WROUGHT_CHAMBER), new RandomSpreadStructurePlacement(15, 5, RandomSpreadType.TRIANGULAR, 23217347)));
        context.register(UMVUTHANA_GROVES, new StructureSet(structures.getOrThrow(StructureHandler.UMVUTHANA_GROVE), new RandomSpreadStructurePlacement(25, 8, RandomSpreadType.TRIANGULAR, 23311138)));
        context.register(FROSTMAWS, new StructureSet(structures.getOrThrow(StructureHandler.FROSTMAW), new RandomSpreadStructurePlacement(25, 8, RandomSpreadType.TRIANGULAR, 23317578)));
//        context.register(MONASTERIES, new StructureSet(structures.getOrThrow(StructureHandler.MONASTERY), new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 25327374)));
    }
}
