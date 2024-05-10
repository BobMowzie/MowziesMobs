package com.bobmowzie.mowziesmobs.datagen;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.FrostmawStructure;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.UmvuthanaGroveStructure;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.WroughtnautChamberStructure;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

public class StructureHandler {
    public static final ResourceKey<Structure> WROUGHT_CHAMBER = createStructureKey("wrought_chamber");
    public static final ResourceKey<Structure> UMVUTHANA_GROVE = createStructureKey("umvuthana_grove");
    public static final ResourceKey<Structure> FROSTMAW = createStructureKey("frostmaw_spawn");
    public static final ResourceKey<Structure> MONASTERY = createStructureKey("monastery");

    private static ResourceKey<Structure> createStructureKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(MowziesMobs.MODID, name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        context.register(WROUGHT_CHAMBER, new WroughtnautChamberStructure(structure(TagHandler.HAS_MOWZIE_STRUCTURE, TerrainAdjustment.NONE, context)));
        context.register(UMVUTHANA_GROVE, new UmvuthanaGroveStructure(structure(TagHandler.HAS_MOWZIE_STRUCTURE, TerrainAdjustment.NONE, context)));
        context.register(FROSTMAW, new FrostmawStructure(structure(TagHandler.HAS_MOWZIE_STRUCTURE, TerrainAdjustment.BEARD_THIN, context)));
//        context.register(CONFIGURED_MONASTERY, FeatureHandler.MONASTERY.get().configured(new JigsawConfiguration(PlainVillagePools.START, 0), TagHandler.HAS_MOWZIE_STRUCTURE));
    }

    private static HolderSet<Biome> biomes(TagKey<Biome> biomeTag, BootstapContext<Structure> context) {
        return context.lookup(Registries.BIOME).getOrThrow(biomeTag);
    }

    private static Structure.StructureSettings structure(TagKey<Biome> biomeTag, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration generationStep, TerrainAdjustment terrainAdjustment, BootstapContext<Structure> context) {
        return new Structure.StructureSettings(biomes(biomeTag, context), spawnOverrides, generationStep, terrainAdjustment);
    }

    private static Structure.StructureSettings structure(TagKey<Biome> biomeTag, TerrainAdjustment terrainAdjustment, BootstapContext<Structure> context) {
        return structure(biomeTag, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdjustment, context);
    }
}
