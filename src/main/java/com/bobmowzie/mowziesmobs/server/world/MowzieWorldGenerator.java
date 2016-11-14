package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureBarakoaVillage;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    public static void generatePrePopulate(World world, Random random, int x, int z) {
        StructureBarakoaVillage.generateVillage(world, random, x, z, MowziesMobs.CONFIG.spawnrateBarako * 3);
    }

    private void generateSurface(World world, Random random, int x, int z) {
        if (world.getWorldInfo().isMapFeaturesEnabled()) {
            StructureWroughtnautRoom.tryWroughtChamber(world, random, x, z, MowziesMobs.CONFIG.spawnrateWroughtnaut);
//            System.out.println("Trying wroughtnaut chamber at " + x + ", " + z);
        }
    }

    private void generateEnd(World world, Random random, int i, int i1) {
    }

    private void generateNether(World world, Random random, int i, int i1) {
    }
}
