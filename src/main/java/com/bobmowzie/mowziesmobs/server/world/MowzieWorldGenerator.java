package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureBarakoaVillage;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class MowzieWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case 0: //surface GENERATOR
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
            case 1: //end GENERATOR
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
            case -1: //nether GENERATOR
                generateNether(world, random, chunkX * 16, chunkZ * 16);
            default:
                return;
        }
    }

    public static void generatePrePopulate(World world, Random random, int chunkX, int chunkZ) {
        if (canSpawnVillageAtCoords(chunkX, chunkZ, world)) StructureBarakoaVillage.generateVillage(world, random, chunkX * 16 + 8, chunkZ * 16 + 8, 1);
        if (canSpawnFrostmawAtCoords(chunkX, chunkZ, world)) {
            EntityFrostmaw frostmaw = new EntityFrostmaw(world);
            frostmaw.spawnInWorld(world, random, chunkX * 16 + 8, chunkZ * 16 + 8);
        }
    }

    private static boolean canSpawnFrostmawAtCoords(int chunkX, int chunkZ, World world)
    {
        if (ConfigHandler.FROSTMAW.GENERATION_DATA.generationFrequency <= 0) return false;
        int maxDistanceBetweenFrostmaws = ConfigHandler.FROSTMAW.GENERATION_DATA.generationFrequency + 8;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetweenFrostmaws - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetweenFrostmaws - 1;
        }

        int k = chunkX / maxDistanceBetweenFrostmaws;
        int l = chunkZ / maxDistanceBetweenFrostmaws;
        Random random = world.setRandomSeed(k, l, 14357617);
        k = k * maxDistanceBetweenFrostmaws;
        l = l * maxDistanceBetweenFrostmaws;
        k = k + random.nextInt(maxDistanceBetweenFrostmaws - 8);
        l = l + random.nextInt(maxDistanceBetweenFrostmaws - 8);

        if (i == k && j == l) return true;

        return false;
    }

    private static boolean canSpawnVillageAtCoords(int chunkX, int chunkZ, World world)
    {
        if (ConfigHandler.BARAKO.GENERATION_DATA.generationFrequency <= 0) return false;
        int maxDistanceBetweenVillages = ConfigHandler.BARAKO.GENERATION_DATA.generationFrequency + 8;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetweenVillages - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetweenVillages - 1;
        }

        int k = chunkX / maxDistanceBetweenVillages;
        int l = chunkZ / maxDistanceBetweenVillages;
        Random random = world.setRandomSeed(k, l, 14357617);
        k = k * maxDistanceBetweenVillages;
        l = l * maxDistanceBetweenVillages;
        k = k + random.nextInt(maxDistanceBetweenVillages - 8);
        l = l + random.nextInt(maxDistanceBetweenVillages - 8);

        if (i == k && j == l) return true;

        return false;
    }

    private void generateSurface(World world, Random random, int x, int z) {
        if (world.getWorldInfo().isMapFeaturesEnabled()) {
            StructureWroughtnautRoom.tryWroughtChamber(world, random, x, z, ConfigHandler.FERROUS_WROUGHTNAUT.GENERATION_DATA.generationFrequency);
//            System.out.println("Trying wroughtnaut chamber at " + x + ", " + z);
        }
    }

    private void generateEnd(World world, Random random, int i, int i1) {
    }

    private void generateNether(World world, Random random, int i, int i1) {
    }

    public static int findGenHeight(World world, BlockPos pos) {
        for (int y = 70 - pos.getY(); y > 50 - pos.getY(); y--) {
            if (!(world.getBlockState(pos.add(0, y, 0)).isFullBlock())) continue;
            if (world.getBlockState(pos.add(0, y, 0)) != Blocks.GRASS.getDefaultState()) break;
            return y;
        }
        //System.out.println("Failed to find height");
        return -1;
    }
}
