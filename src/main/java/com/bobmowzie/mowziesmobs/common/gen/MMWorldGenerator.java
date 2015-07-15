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
        int myCaveChance = 75;
        int xzCheckDistance = 10;

        if (random.nextInt(myCaveChance) == 0)
        {
            for (int y = 50; y >= 30; y--) {
                if (world.getBlock(x, y, z).isAir(world, x, y, z))
                {
                    for (int y2 = 1; y2 <= 30; y2++)
                    {
                        if (world.getBlock(x, y - y2, z).isOpaqueCube())
                        {
                            for (int x2 = 0; x2 <= xzCheckDistance; x2++)
                            {
                                if (world.getBlock(x - x2, y - y2, z).isAir(world, x - x2, y - y2, z))
                                {
                                    generateWroughtChamber(world, random, x-x2, y-y2, z, 1);
                                    return;
                                }
                                if (world.getBlock(x + x2, y - y2, z).isOpaqueCube())
                                {
                                    generateWroughtChamber(world, random, x+x2, y-y2, z, 3);
                                    return;
                                }
                            }
                            for (int z2 = 0; z2 <= xzCheckDistance; z2++)
                            {
                                if (world.getBlock(x, y - y2, z-z2).isOpaqueCube())
                                {
                                    generateWroughtChamber(world, random, x, y-y2, z-z2, 2);
                                    return;
                                }
                                if (world.getBlock(x, y - y2, z+z2).isOpaqueCube()) {
                                    generateWroughtChamber(world, random, x, y-y2, z+z2, 4);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateWroughtChamber(World world, Random random, int x, int y, int z, int direction)
    {
        Structure structure = MowziesMobs.gen.structures.get(0);
        EntityWroughtnaut wroughtnaut = new EntityWroughtnaut(world);
        wroughtnaut.setPositionAndRotation(x + 0.5, y + 1, z + 9.5, 90 * direction, 0);
        MowziesMobs.gen.setStructureFacing(3);
        MowziesMobs.gen.setStructure(structure);
        MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
        MowziesMobs.gen.generate(world, random, x, y - 1, z);
        //System.out.println(x + ", " + y + ", " + z);
        world.spawnEntityInWorld(wroughtnaut);
    }

    private void generateEnd(World world, Random random, int i, int i1)
    {
    }

    private void generateNether(World world, Random random, int i, int i1)
    {
    }
}
