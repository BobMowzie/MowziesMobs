package com.bobmowzie.mowziesmobs.server.gen;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.gen.structure.barakoa.StructureBarakoThrone;
import com.bobmowzie.mowziesmobs.server.gen.structure.barakoa.StructureBarakoaHouse;
import coolalias.structuregenapi.util.Structure;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.BlockStone;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class MowzieWorldGenerator implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        switch (world.provider.dimensionId)
        {
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

    private void generateSurface(World world, Random random, int x, int z)
    {
        if (world.getWorldInfo().isMapFeaturesEnabled()) tryWroughtChamber(world, random, x, z, MowziesMobs.CONFIG.spawnrateWroughtnaut);
    }

    private void tryWroughtChamber(World world, Random random, int x, int z, int chance)
    {
        if (chance <= 0) return;
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
                                        if (world.getBlock(x - x2 - 9, y - y2 + y4, z - 9) instanceof BlockStone)
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
                                        if (world.getBlock(x + x2 + 9, y - y2 + y5, z - 9) instanceof BlockStone)
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
                                        if (world.getBlock(x - 1, y - y2 + y4, z - z2 - 18) instanceof BlockStone)
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
                                        if (world.getBlock(x - 1, y - y2 + y5, z + z2) instanceof BlockStone)
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
        Structure structure = MowzieStructureGenerator.structures.get(0);
        EntityWroughtnaut wroughtnaut = new EntityWroughtnaut(world);
        wroughtnaut.setPositionAndRotation(x + 0.5, y + 1, z + 9.5, 0, 0);
        MowziesMobs.GENERATOR.setStructure(structure);
        MowziesMobs.GENERATOR.setStructureFacing(direction);
        MowziesMobs.GENERATOR.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
        MowziesMobs.GENERATOR.generate(world, random, x, y - 1, z);
        //System.out.println(x + ", " + y + ", " + z);
        world.spawnEntityInWorld(wroughtnaut);
    }

    private void generateBarakoaVillage(World world, Random random, int x, int y, int z) {
        StructureBarakoaHouse.generateFirepit(world, x, y, z);
        int currentX = x;
        int currentZ = z;
        int throneDirection = random.nextInt(3);
        if (throneDirection == 0) currentX += 10;
        else if (throneDirection == 1) currentZ += 10;
        else if (throneDirection == 2) currentX -= 10;
        else if (throneDirection == 3) currentZ -= 10;
        StructureBarakoThrone.generate(world, currentX, y, currentZ, throneDirection);
    }

    private void generateEnd(World world, Random random, int i, int i1)
    {
    }

    private void generateNether(World world, Random random, int i, int i1)
    {
    }
}
