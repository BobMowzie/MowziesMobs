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
        tryWroughtChamber(world, random, x, z, 75);
    }

    private void tryWroughtChamber(World world, Random random, int x, int z, int chance)
    {
        int xzCheckDistance = 10;

        if (random.nextInt(chance) == 0)
        {
            for (int y = 50; y >= 30; y--)
            {
                if (world.getBlock(x, y, z).isAir(world, x, y, z))
                {
                    for (int y2 = 1; y2 <= 30; y2++)
                    {
                        if (world.getBlock(x, y - y2, z).isOpaqueCube())
                        {
                            int y4 = 0;
                            int y5 = 0;
                            for (int x2 = 0; x2 <= xzCheckDistance; x2++) {
                                if (world.getBlock(x - x2, y - y2 + y4 + 1, z).isOpaqueCube()) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        if (!world.getBlock(x - x2, y - y2 + y4 + 1 + y3, z).isOpaqueCube()) {
                                            wall = false;
                                            y4 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        generateWroughtChamber(world, random, x - x2 - 9, y - y2 + y4, z - 9, 0);
                                        return;
                                    }
                                }
                                if (world.getBlock(x + x2, y - y2 + y5 + 1, z).isOpaqueCube()) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        if (!world.getBlock(x + x2, y - y2 + y5 + 1 + y3, z).isOpaqueCube()) {
                                            wall = false;
                                            y5 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        generateWroughtChamber(world, random, x + x2 + 9, y - y2 + y5, z - 9, 2);
                                        return;
                                    }
                                }
                            }
                            y4 = 0;
                            y5 = 0;
                            for (int z2 = 0; z2 <= xzCheckDistance; z2++) {
                                if (world.getBlock(x - 1, y - y2 + y4 + 1, z - z2).isOpaqueCube()) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        if (!world.getBlock(x - 1, y - y2 + y4 + 1 + y3, z - z2).isOpaqueCube()) {
                                            wall = false;
                                            y4 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        generateWroughtChamber(world, random, x - 1, y - y2 + y4, z - z2 - 18, 1);
                                        return;
                                    }
                                }
                                if (world.getBlock(x - 1, y - y2 + y5 + 1, z + z2).isOpaqueCube()) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        if (!world.getBlock(x - 1, y - y2 + y5 + 1 + y3, z + z2).isOpaqueCube()) {
                                            wall = false;
                                            y5 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        generateWroughtChamber(world, random, x - 1, y - y2 + y5, z + z2, 3);
                                        return;
                                    }
                                }
                            }
                            break;
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
        wroughtnaut.setPositionAndRotation(x + 0.5, y + 1, z + 9.5, 180 + 90 * direction, 0);
        MowziesMobs.gen.setStructure(structure);
        MowziesMobs.gen.setStructureFacing(direction);
        MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
        MowziesMobs.gen.generate(world, random, x, y - 1, z);
        System.out.println(x + ", " + y + ", " + z);
        world.spawnEntityInWorld(wroughtnaut);
    }

    private void generateEnd(World world, Random random, int i, int i1)
    {
    }

    private void generateNether(World world, Random random, int i, int i1)
    {
    }
}
