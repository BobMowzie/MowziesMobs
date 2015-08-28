package com.bobmowzie.mowziesmobs.common.gen.structure.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.blocks.MMBlocks;
import net.ilexiconn.llibrary.common.structure.util.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by jnad325 on 6/24/15.
 */
public class StructureBarakoaHouse
{
        public static int paintedAcaciaID;
    public static int[][][][] blockArray1;

        public static void generateHouse1(World world, int x, int y, int z) {
                paintedAcaciaID = Block.getIdFromBlock(MMBlocks.blockPaintedAcacia);
                
                if (blockArray1 == null) blockArray1 = new int[][][][]
                {
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
                                    {85}, {162}, {Block.getIdFromBlock(MMBlocks.blockPaintedAcacia)}, {0}, {Block.getIdFromBlock(MMBlocks.blockPaintedAcacia)}, {162}, {85}
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
                                    {85}, {162}, {5, 4}, {126, 12}, {5, 4}, {162}, {85}
                            },
                            {
                                    {}, {5, 4}, {0}, {0}, {0}, {5, 4}, {}
                            },
                            {
                                    {}, {126, 12}, {0}, {0}, {0}, {126, 12}, {}
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
                
                Structure structure = MowziesMobs.gen.structures.get(1);
                System.out.println(paintedAcaciaID);
                MowziesMobs.gen.setStructure(structure);
                MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
                MowziesMobs.gen.generate(world, new Random(), x, y, z);
                for (int i = 1; i < 20; i++) {
                        if (world.getBlock(x, y + 2 - i, z - i) instanceof BlockAir) {
                                world.setBlock(x, y + 2 - i, z - i, Block.getBlockById(134), 2, 0);
                                world.setBlock(x, y + 2 - i, z - i + 1, Block.getBlockById(134), 7, 0);
                        }
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x + 2, y - i, z + 1) instanceof BlockAir) world.setBlock(x + 2, y - i, z + 1, Block.getBlockById(85), 0, 0);
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x - 2, y - i, z + 1) instanceof BlockAir) world.setBlock(x - 2, y - i, z + 1, Block.getBlockById(85), 0, 0);
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x + 2, y - i, z + 5) instanceof BlockAir) world.setBlock(x + 2, y - i, z + 5, Block.getBlockById(85), 0, 0);
                        else break;
                }
                for (int i = 0; i < 20; i++) {
                        if (world.getBlock(x - 2, y - i, z + 5) instanceof BlockAir) world.setBlock(x - 2, y - i, z + 5, Block.getBlockById(85), 0, 0);
                        else break;
                }
        }
}
