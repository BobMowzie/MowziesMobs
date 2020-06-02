package com.bobmowzie.mowziesmobs.server.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiPredicate;

public enum SpawnHandler {
    INSTANCE;

    public static Set<Biome> FROSTMAW_BIOMES;
    public static Set<Biome> BARAKO_BIOMES;
    public static Set<Biome> FERROUS_WROUGHTNAUT_BIOMES;

    private static final Map<String, Type> BY_NAME = new HashMap<String, Type>();

    public void registerSpawnPlacementTypes() {
        EnumHelper.addSpawnPlacementType("MMSPAWN", new BiPredicate<IBlockAccess, BlockPos>(){
            @Override
            public boolean test(IBlockAccess t, BlockPos pos) {
                IBlockState block = t.getBlockState(pos.down());
                boolean flag = block.getBlock() != Blocks.BEDROCK && block.getBlock() != Blocks.BARRIER && block.getMaterial().blocksMovement();
                IBlockState iblockstateUp = t.getBlockState(pos);
                IBlockState iblockstateUp2 = t.getBlockState(pos.up());
                flag = flag && WorldEntitySpawner.isValidEmptySpawnBlock(iblockstateUp) && WorldEntitySpawner.isValidEmptySpawnBlock(iblockstateUp2);
                return flag;
            }});
    }

    public void registerSpawns() {
        EntityLiving.SpawnPlacementType mmSpawn = EntityLiving.SpawnPlacementType.valueOf("MMSPAWN");
        if(mmSpawn != null) {
            EntitySpawnPlacementRegistry.setPlacementType(EntityFoliaath.class, mmSpawn);
            EntitySpawnPlacementRegistry.setPlacementType(EntityLantern.class, mmSpawn);
            EntitySpawnPlacementRegistry.setPlacementType(EntityBarakoana.class, mmSpawn);
            EntitySpawnPlacementRegistry.setPlacementType(EntityGrottol.class, mmSpawn);
            EntitySpawnPlacementRegistry.setPlacementType(EntityNaga.class, mmSpawn);
        }

        for (Type type : BiomeDictionary.Type.getAll()) {
            BY_NAME.put(type.getName(), type);
        }

        if (ConfigHandler.FROSTMAW.generationData.generationFrequency > 0) {
            FROSTMAW_BIOMES = getBiomesFromConfig(ConfigHandler.FROSTMAW.generationData.biomeData);
            //System.out.println("Frostmaw biomes " + FROSTMAW_BIOMES);
        }
        if (ConfigHandler.BARAKO.generationData.generationFrequency > 0) {
            BARAKO_BIOMES = getBiomesFromConfig(ConfigHandler.BARAKO.generationData.biomeData);
            //System.out.println("Barako biomes " + BARAKO_BIOMES);
        }
        if (ConfigHandler.FERROUS_WROUGHTNAUT.generationData.generationFrequency > 0) {
            FERROUS_WROUGHTNAUT_BIOMES = getBiomesFromConfig(ConfigHandler.FERROUS_WROUGHTNAUT.generationData.biomeData);
            //System.out.println("Ferrous Wroughtnaut biomes " + FERROUS_WROUGHTNAUT_BIOMES);
        }

        if (ConfigHandler.FOLIAATH.spawnData.spawnRate > 0) {
            Set<Biome> foliaathBiomes = getBiomesFromConfig(ConfigHandler.FOLIAATH.spawnData.biomeData);
            //System.out.println("Foliaath biomes " + foliaathBiomes);
            EntityRegistry.addSpawn(EntityFoliaath.class, ConfigHandler.FOLIAATH.spawnData.spawnRate, ConfigHandler.FOLIAATH.spawnData.minGroupSize, ConfigHandler.FOLIAATH.spawnData.maxGroupSize, EnumCreatureType.MONSTER, foliaathBiomes.toArray(new Biome[foliaathBiomes.size()]));
        }
        if (ConfigHandler.BARAKOA.spawnData.spawnRate > 0) {
            Set<Biome> barakoaBiomes = getBiomesFromConfig(ConfigHandler.BARAKOA.spawnData.biomeData);
            EntityRegistry.addSpawn(EntityBarakoana.class, ConfigHandler.BARAKOA.spawnData.spawnRate, ConfigHandler.BARAKOA.spawnData.minGroupSize, ConfigHandler.BARAKOA.spawnData.maxGroupSize, EnumCreatureType.MONSTER, barakoaBiomes.toArray(new Biome[barakoaBiomes.size()]));
        }
        if (ConfigHandler.GROTTOL.spawnData.spawnRate > 0) {
            Set<Biome> grottolBiomes = getBiomesFromConfig(ConfigHandler.GROTTOL.spawnData.biomeData);
            EntityRegistry.addSpawn(EntityGrottol.class, ConfigHandler.GROTTOL.spawnData.spawnRate, ConfigHandler.GROTTOL.spawnData.minGroupSize, ConfigHandler.GROTTOL.spawnData.maxGroupSize, EnumCreatureType.MONSTER, grottolBiomes.toArray(new Biome[grottolBiomes.size()]));
        }
        if (ConfigHandler.LANTERN.spawnData.spawnRate > 0) {
            Set<Biome> lanternBiomes = getBiomesFromConfig(ConfigHandler.LANTERN.spawnData.biomeData);
            EntityRegistry.addSpawn(EntityLantern.class, ConfigHandler.LANTERN.spawnData.spawnRate, ConfigHandler.LANTERN.spawnData.minGroupSize, ConfigHandler.LANTERN.spawnData.maxGroupSize, EnumCreatureType.AMBIENT, lanternBiomes.toArray(new Biome[lanternBiomes.size()]));
        }
        if (ConfigHandler.NAGA.spawnData.spawnRate > 0) {
            Set<Biome> nagaBiomes = getBiomesFromConfig(ConfigHandler.NAGA.spawnData.biomeData);
            //System.out.println("Naga biomes " + nagaBiomes);
            EntityRegistry.addSpawn(EntityNaga.class, ConfigHandler.NAGA.spawnData.spawnRate, ConfigHandler.NAGA.spawnData.minGroupSize, ConfigHandler.NAGA.spawnData.maxGroupSize, EnumCreatureType.MONSTER, nagaBiomes.toArray(new Biome[nagaBiomes.size()]));
        }
    }

    private class BiomeCombo {
        Type[] neededTypes;
        boolean[] inverted;
        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.toUpperCase().replace(" ", "").split(",");
            neededTypes = new Type[typeStrings.length];
            inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].length() == 0) {
                    continue;
                }
                inverted[i] = typeStrings[i].charAt(0) == '!';
                String name = typeStrings[i].replace("!", "");
                Type neededType = BY_NAME.get(name);
                if (neededType == null) System.out.println("Mowzie's Mobs config warning: No biome dictionary type with name '" + name + "'. Unable to check for type.");
                neededTypes[i] = neededType;
            }
        }

        private boolean acceptsBiome(Biome biome) {
            Set<Type> thisTypes = BiomeDictionary.getTypes(biome);
            for (int i = 0; i < neededTypes.length; i++) {
                if (neededTypes[i] == null) continue;
                if (!inverted[i]) {
                    if (!thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
                else {
                    if (thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private Set<Biome> getBiomesFromConfig(ConfigHandler.BiomeData biomeData) {
        Field f = null;
        try {
            f = Biome.class.getDeclaredField("biomeName");
            f.setAccessible(true);
        }
        catch (Exception e) {
            System.out.println("Reflection failed");
        }

        Set<String> biomeWhitelistNames = new HashSet<>(Arrays.asList(biomeData.biomeWhitelist));
        Set<String> biomeBlacklistNames = new HashSet<>(Arrays.asList(biomeData.biomeBlacklist));
        Set<String> whiteOrBlacklistedBiomes = new HashSet<>();
        Set<BiomeCombo> biomeCombos = new HashSet<>();
        for (String biomeComboString : biomeData.biomeTypes) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            biomeCombos.add(biomeCombo);
        }

        Set<Biome> toReturn = new HashSet<>();
        for (Biome b : Biome.REGISTRY) {
            String biomeName = "";
            try {
                biomeName = (String) f.get(b);
            }
            catch (Exception e) {
                System.out.println("Reflection failed");
            }

            if (biomeWhitelistNames.contains(biomeName)) {
                toReturn.add(b);
                whiteOrBlacklistedBiomes.add(biomeName);
                continue;
            }
            if (biomeBlacklistNames.contains(biomeName)) {
                whiteOrBlacklistedBiomes.add(biomeName);
                continue;
            }
            for (BiomeCombo biomeCombo : biomeCombos) {
                if (biomeCombo.acceptsBiome(b)) toReturn.add(b);
            }
        }

        for (String biomeName : biomeWhitelistNames) {
            if (!whiteOrBlacklistedBiomes.contains(biomeName)) {
                System.out.println("Mowzie's Mobs config warning: No biome with name '" + biomeName + "'. Unable to whitelist.");
            }
        }
        for (String biomeName : biomeBlacklistNames) {
            if (!whiteOrBlacklistedBiomes.contains(biomeName)) {
                System.out.println("Mowzie's Mobs config warning: No biome with name '" + biomeName + "'. Unable to blacklist.");
            }
        }

        return toReturn;
    }
}