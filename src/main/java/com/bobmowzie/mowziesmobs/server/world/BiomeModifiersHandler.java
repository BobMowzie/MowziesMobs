package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeModifiersHandler
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> REG = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MowziesMobs.MODID);
    public static final RegistryObject<Codec<? extends BiomeModifier>> MOWZIE_MOB_SPAWNS = REG.register("mowzie_mob_spawns", MobSpawnBiomeModifier::makeCodec);
}