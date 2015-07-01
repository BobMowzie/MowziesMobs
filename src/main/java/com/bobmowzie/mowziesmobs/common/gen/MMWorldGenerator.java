package com.bobmowzie.mowziesmobs.common.gen;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntityWroughtnaut;
import cpw.mods.fml.common.IWorldGenerator;
import net.ilexiconn.llibrary.common.structure.util.Structure;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class MMWorldGenerator implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        switch (world.provider.dimensionId)
        {
            case 0: //surface gen
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
            case 1: //end gen
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
            case -1: //nether gen
                generateNether(world, random, chunkX * 16, chunkZ * 16);
            default:
                return;
        }
    }

    private void generateSurface(World world, Random random, int x, int z)
    {
        int myCaveChance = 150;

        if (random.nextInt(myCaveChance) == 0)
        {
            int y = 16 + random.nextInt(12);
            System.out.println(x + ", " + y + ", " + z);
            Structure structure = MowziesMobs.gen.structures.get(0);
            EntityWroughtnaut wroughtnaut = new EntityWroughtnaut(world);
            wroughtnaut.setPositionAndRotation(x + 0.5, y + 2, z + 9.5, 0, 0);
            MowziesMobs.gen.setStructure(structure);
            MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
            MowziesMobs.gen.generate(world, random, x, y, z);
            world.spawnEntityInWorld(wroughtnaut);
        }
    }

    private void generateEnd(World world, Random random, int i, int i1)
    {
    }

    private void generateNether(World world, Random random, int i, int i1)
    {
    }
}
