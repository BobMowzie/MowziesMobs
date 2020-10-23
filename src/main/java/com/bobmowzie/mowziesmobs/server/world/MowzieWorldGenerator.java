package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureBarakoaVillage;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class MowzieWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider) {
        if (world.getDimension().isSurfaceWorld())
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        else if (world.getDimension().isNether()) generateNether(world, random, chunkX * 16, chunkZ * 16);
    }

    public static void generatePrePopulate(World world, Random random, int chunkX, int chunkZ) {
        if (canSpawnStructureAtCoords(chunkX, chunkZ, world, ConfigHandler.MOBS.BARAKO.generationData.generationFrequency)) StructureBarakoaVillage.generateVillage(world, random, chunkX * 16 + 8, chunkZ * 16 + 8);
        if (canSpawnStructureAtCoords(chunkX, chunkZ, world, ConfigHandler.MOBS.FROSTMAW.generationData.generationFrequency)) {
            EntityFrostmaw frostmaw = new EntityFrostmaw(EntityHandler.FROSTMAW, world);
            frostmaw.spawnInWorld(world, random, chunkX * 16 + 8, chunkZ * 16 + 8);
        }
    }

    private static boolean canSpawnStructureAtCoords(int chunkX, int chunkZ, World world, int genFrequency)
    {
        if (genFrequency <= 0) return false;
        int maxDistanceBetween = genFrequency + 8;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetween - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetween - 1;
        }

        int k = chunkX / maxDistanceBetween;
        int l = chunkZ / maxDistanceBetween;
        Random random = world.getRandom();
        k = k * maxDistanceBetween;
        l = l * maxDistanceBetween;
        k = k + random.nextInt(maxDistanceBetween - 8);
        l = l + random.nextInt(maxDistanceBetween - 8);

        if (i == k && j == l) return true;

        return false;
    }

    private void generateSurface(World world, Random random, int x, int z) {
        if (canSpawnStructureAtCoords(x, z, world, ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationData.generationFrequency))
            StructureWroughtnautRoom.tryWroughtChamber(world, random, x, z);
    }

    private void generateEnd(World world, Random random, int i, int i1) {
    }

    private void generateNether(World world, Random random, int i, int i1) {
    }

    public static int findGenHeight(World world, BlockPos pos, int heightMax, int heightMin) {
        BlockState topBlock = world.getBiome(pos).getSurfaceBuilderConfig().getTop();
        BlockState fillerBlock = world.getBiome(pos).getSurfaceBuilderConfig().getUnder();
        BlockState stone = Blocks.STONE.getDefaultState();
        for (int y = heightMax - pos.getY(); y > heightMin - pos.getY(); y--) {
            if (!Block.hasSolidSideOnTop(world, pos.add(0, y, 0))) continue;
            BlockState firstFullBlock = world.getBlockState(pos.add(0, y, 0));
            if (firstFullBlock != topBlock && firstFullBlock != fillerBlock && firstFullBlock != stone) break;
            return y;
        }
        //System.out.println("Failed to find height");
        return -1;
    }
}
