package com.bobmowzie.mowziesmobs.common.gen.structure.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.blocks.MMBlocks;
import com.bobmowzie.mowziesmobs.common.gen.structure.StructureBase;
import net.ilexiconn.llibrary.common.structure.util.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by jnad325 on 6/24/15.
 */
public class StructureBarakoaHouse extends StructureBase
{
    public static int[][][][] blockArray1 = {
            { // y
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },

            {// Y = 2
                    {
                            {}, {126, 12}, {}, {}, {}, {126, 12}, {}
                    },
                    {
                            {126, 12}, {162}, {126, 12}, {126, 12}, {126, 12}, {162}, {126, 12}
                    },
                    {
                            {}, {126, 12}, {126, 12}, {126, 12}, {126, 12}, {126, 12}, {}
                    },
                    {
                            {}, {126, 12}, {126, 12}, {126, 12}, {126, 12}, {126, 12}, {}
                    },
                    {
                            {}, {126, 12}, {126, 12}, {126, 12}, {126, 12}, {126, 12}, {}
                    },
                    {
                            {126, 12}, {162}, {126, 12}, {134, 4}, {126, 12}, {162}, {126, 12}
                    },
                    {
                            {}, {126, 12}, {}, {134, 1}, {}, {126, 12}, {}
                    }
            },

            {// y = 3
                    {
                            {}, {162, 8}, {}, {}, {}, {162, 8}, {}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162, 4}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {}, {162, 8}, {170}, {170}, {170}, {162, 8}, {}
                    },
                    {
                            {}, {162, 8}, {170}, {170}, {170}, {162, 8}, {}
                    },
                    {
                            {}, {162, 8}, {170}, {170}, {170}, {162, 8}, {}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {134, 1}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {}, {162, 8}, {}, {}, {}, {162, 8}, {}
                    }
            },

            {// Y = 4
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {85}, {162}, {5, 4}, {126, 4}, {5, 4}, {162}, {85}
                    },
                    {
                            {}, {5, 4}, {0}, {0}, {0}, {5, 4}, {}
                    },
                    {
                            {}, {126, 4}, {0}, {0}, {0}, {126, 4}, {}
                    },
                    {
                            {}, {5, 4}, {0}, {0}, {0}, {5, 4}, {}
                    },
                    {
                            {85}, {162}, {5, 4}, {0}, {5, 4}, {162}, {85}
                    },
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    }
            },

            {// Y = 5
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {85}, {162}, {5, 4}, {49}, {5, 4}, {162}, {85}
                    },
                    {
                            {}, {5, 4}, {0}, {0}, {0}, {5, 4}, {}
                    },
                    {
                            {}, {49}, {0}, {0}, {0}, {49}, {}
                    },
                    {
                            {}, {5, 4}, {0}, {0}, {0}, {5, 4}, {}
                    },
                    {
                            {85}, {162}, {5, 4}, {0}, {5, 4}, {162}, {85}
                    },
                    {
                            {18, 3}, {85}, {}, {}, {18, 3}, {85}, {}
                    }
            },
            { // y = 6
                    {
                            {18, 3}, {162, 8}, {}, {}, {18, 3}, {162, 8}, {18, 3}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162, 4}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {18, 3}, {162, 8}, {0}, {0}, {0}, {162, 8}, {}
                    },
                    {
                            {18, 3}, {162, 8}, {0}, {0}, {0}, {162, 8}, {}
                    },
                    {
                            {}, {162, 8}, {0}, {0}, {0}, {162, 8}, {18, 3}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162, 4}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {18, 3}, {162, 8}, {}, {}, {18, 3}, {162, 8}, {18, 3}
                    }
            },
            { // y = 7
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {}
                    },
                    {
                            {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}
                    },
                    {
                            {}, {18, 3}, {170}, {170}, {170}, {18, 3}, {18, 3}
                    },
                    {
                            {18, 3}, {18, 3}, {170}, {170}, {170}, {18, 3}, {}
                    },
                    {
                            {18, 3}, {18, 3}, {170}, {170}, {170}, {18, 3}, {}
                    },
                    {
                            {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}
                    },
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {}
                    }
            },
            { // y = 8
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {18, 3}, {18, 3}, {18, 3}, {}, {}
                    },
                    {
                            {}, {}, {18, 3}, {18, 3}, {18, 3}, {}, {}
                    },
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {}, {}
                    },
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {}, {}
                    },
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}, {}
                    },
                    {
                            {}, {}, {}, {18, 3}, {18, 3}, {}, {}
                    }
            },
            { // y = 9
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {18, 3}, {}, {}, {}
                    },
                    {
                            {}, {}, {18, 3}, {18, 3}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },
    };

        public static void generateHouse1(World world, int x, int y, int z, int direction) {
                Structure structure = MowziesMobs.gen.structures.get(1);
                MowziesMobs.gen.setStructure(structure);
                MowziesMobs.gen.setStructureFacing(direction);
                MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
                MowziesMobs.gen.generate(world, new Random(), x, y, z);
//                System.out.println("Beginning generation at" + x + ", " + y + ", " + z);
                replaceBlocks(Blocks.planks, 4, MMBlocks.blockPaintedAcacia, 0, x - 3, y, z, 7, 9, 7, world);
                replaceBlocks(Blocks.wooden_slab, 4, MMBlocks.blockPaintedAcaciaSlab, 0, x - 3, y, z, 7, 9, 7, world);
                replaceBlocks(Blocks.obsidian, 0, MMBlocks.blockPaintedAcaciaSlab, 12, x - 3, y, z, 7, 9, 7, world);

                //Fix Logs depending on rotation
                if (direction == 2 || direction == 4) {
                        replaceBlocks(Blocks.log2, 8, Blocks.obsidian, 0, x - 3, y, z, 7, 9, 7, world);
                        replaceBlocks(Blocks.log2, 4, Blocks.log2, 8, x - 3, y, z, 7, 9, 7, world);
                        replaceBlocks(Blocks.obsidian, 0, Blocks.log2, 4, x - 3, y, z, 7, 9, 7, world);
                }

                //Stairs
                if (direction == 1) {
                        for (int i = 1; i < 20; i++) {
                                if (world.getBlock(x, y + 2 - i, z + 6 + i) instanceof BlockAir) {
                                        world.setBlock(x, y + 2 - i, z + 6 + i, Block.getBlockById(134), 3, 3);
                                        world.setBlock(x, y + 2 - i, z + 6 + i - 1, Block.getBlockById(134), 6, 3);
                                } else break;
                        }
                }
                else if (direction == 2) {
                        for (int i = 1; i < 20; i++) {
                                if (world.getBlock(x - 3 - i, y + 2 - i, z + 3) instanceof BlockAir) {
                                        world.setBlock(x - 3 - i, y + 2 - i, z + 3, Block.getBlockById(134), 0, 3);
                                        world.setBlock(x - 3 - i + 1, y + 2 - i, z + 3, Block.getBlockById(134), 5, 3);
                                } else break;
                        }
                }
                else if (direction == 3) {
                        for (int i = 1; i < 20; i++) {
                                if (world.getBlock(x, y + 2 - i, z - i) instanceof BlockAir) {
                                        world.setBlock(x, y + 2 - i, z - i, Block.getBlockById(134), 2, 3);
                                        world.setBlock(x, y + 2 - i, z - i + 1, Block.getBlockById(134), 7, 3);
                                } else break;
                        }
                }
                else if (direction == 4) {
                        for (int i = 1; i < 20; i++) {
                                if (world.getBlock(x + 3 + i, y + 2 - i, z + 3) instanceof BlockAir) {
                                        world.setBlock(x + 3 + i, y + 2 - i, z + 3, Block.getBlockById(134), 1, 3);
                                        world.setBlock(x + 3 + i - 1, y + 2 - i, z + 3, Block.getBlockById(134), 4, 3);
                                } else break;
                        }
                }

                //Fence poles
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x + 2, y - i, z + 1) instanceof BlockAir) world.setBlock(x + 2, y - i, z + 1, Block.getBlockById(85), 0, 3);
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x - 2, y - i, z + 1) instanceof BlockAir) world.setBlock(x - 2, y - i, z + 1, Block.getBlockById(85), 0, 3);
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x + 2, y - i, z + 5) instanceof BlockAir) world.setBlock(x + 2, y - i, z + 5, Block.getBlockById(85), 0, 3);
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x - 2, y - i, z + 5) instanceof BlockAir) world.setBlock(x - 2, y - i, z + 5, Block.getBlockById(85), 0, 3);
                        else break;
                }

                //Interior
                Random rand = new Random();
                int tableCorner = rand.nextInt(6);
                int tableContent = rand.nextInt(4);
                if (tableCorner == 0) {
                        world.setBlock(x - 1, y + 4, z + 4, Block.getBlockById(126), 12, 3);
                        if (tableContent <= 1) world.setBlock(x - 1, y + 5, z + 4, Block.getBlockById(50), 5, 3);
                        if (tableContent == 2) {
                                world.setBlock(x - 1, y + 5, z + 4, Block.getBlockById(144), 1, 3);
                                ((TileEntitySkull)world.getTileEntity(x - 1, y + 5, z + 4)).func_145903_a(2);
                        }
                }
                if (tableCorner == 1) {
                        world.setBlock(x + 1, y + 4, z + 4, Block.getBlockById(126), 12, 3);
                        if (tableContent <= 1) world.setBlock(x + 1, y + 5, z + 4, Block.getBlockById(50), 5, 3);
                        if (tableContent == 2) {
                                world.setBlock(x + 1, y + 5, z + 4, Block.getBlockById(144), 1, 3);
                                ((TileEntitySkull)world.getTileEntity(x + 1, y + 5, z + 4)).func_145903_a(-2);
                        }
                }
                if (tableCorner == 2) {
                        world.setBlock(x - 1, y + 4, z + 2, Block.getBlockById(126), 12, 3);
                        if (tableContent <= 1) world.setBlock(x - 1, y + 5, z + 2, Block.getBlockById(50), 5, 3);
                        if (tableContent == 2) {
                                world.setBlock(x - 1, y + 5, z + 2, Block.getBlockById(144), 1, 3);
                                ((TileEntitySkull)world.getTileEntity(x - 1, y + 5, z + 2)).func_145903_a(6);
                        }
                }
                if (tableCorner == 3) {
                        world.setBlock(x + 1, y + 4, z + 2, Block.getBlockById(126), 12, 3);
                        if (tableContent <= 1) world.setBlock(x + 1, y + 5, z + 2, Block.getBlockById(50), 5, 3);
                        if (tableContent == 2) {
                                world.setBlock(x + 1, y + 5, z + 2, Block.getBlockById(144), 1, 3);
                                ((TileEntitySkull)world.getTileEntity(x + 1, y + 5, z + 2)).func_145903_a(-6);
                        }
                }

                int bedCorner = rand.nextInt(6);
                int bedDirection = rand.nextInt(2);
                if (bedCorner == tableCorner) bedCorner = 6;
                if (bedCorner == 0) {
                        world.setBlock(x - 1, y + 4, z + 4, Block.getBlockById(171), 4, 3);
                        if (bedDirection == 0) world.setBlock(x, y + 4, z + 4, Block.getBlockById(171), 4, 3);
                        else world.setBlock(x - 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
                }
                if (bedCorner == 1) {
                        world.setBlock(x + 1, y + 4, z + 4, Block.getBlockById(171), 4, 3);
                        if (bedDirection == 0) world.setBlock(x, y + 4, z + 4, Block.getBlockById(171), 4, 3);
                        else world.setBlock(x + 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
                }
                if (bedCorner == 2) {
                        world.setBlock(x - 1, y + 4, z + 2, Block.getBlockById(171), 4, 3);
                        if (bedDirection == 0) world.setBlock(x, y + 4, z + 2, Block.getBlockById(171), 4, 3);
                        else world.setBlock(x - 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
                }
                if (bedCorner == 3) {
                        world.setBlock(x + 1, y + 4, z + 2, Block.getBlockById(171), 4, 3);
                        if (bedDirection == 0) world.setBlock(x, y + 4, z + 2, Block.getBlockById(171), 4, 3);
                        else world.setBlock(x + 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
                }
        }
}
