package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.StructureTypeHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobSpawnBiomeModifier implements BiomeModifier 
{
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(MowziesMobs.MODID, "mowzie_mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MowziesMobs.MODID);

    public MobSpawnBiomeModifier() {
    	
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            SpawnHandler.addBiomeSpawns(biome, builder);
            StructureTypeHandler.addBiomeSpawns(biome);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<MobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(MobSpawnBiomeModifier::new);
    }
}