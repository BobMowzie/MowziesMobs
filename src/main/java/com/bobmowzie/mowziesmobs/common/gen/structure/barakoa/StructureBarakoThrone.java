package com.bobmowzie.mowziesmobs.common.gen.structure.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.block.BlockHandler;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeLeader;
import com.bobmowzie.mowziesmobs.common.gen.structure.StructureBase;
import coolalias.structuregenapi.util.Structure;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by jnad325 on 11/1/15.
 */
public class StructureBarakoThrone extends StructureBase {
    public static int[][][][] blockArray = {
            {//y = 1
                    {
                            {162}, {162, 4}, {162, 4}, {162, 4}, {162, 4}, {162, 4}, {162}
                    },
                    {
                            {162, 8}, {170}, {170}, {170}, {170}, {170}, {162, 8}
                    },
                    {
                            {162, 8}, {170}, {170}, {170}, {170}, {170}, {162, 8}
                    },
                    {
                            {162, 8}, {170}, {170}, {170}, {170}, {170}, {162, 8}
                    },
                    {
                            {162, 8}, {170}, {170}, {170}, {170}, {170}, {162, 8}
                    },
                    {
                            {162, 8}, {170}, {170}, {170}, {170}, {170}, {162, 8}
                    },
                    {
                            {162, 8}, {170, 8}, {159, 14}, {170}, {159, 14}, {170, 8}, {162, 8}
                    },
                    {
                            {162, 8}, {170, 8}, {159, 14}, {170}, {159, 14}, {170, 8}, {162, 8}
                    },
                    {
                            {162, 8}, {170, 8}, {159, 14}, {170}, {159, 14}, {170, 8}, {162, 8}
                    },
                    {
                            {162, 8}, {170, 8}, {159, 14}, {170}, {159, 14}, {170, 8}, {162, 8}
                    },
                    {
                            {162, 8}, {170, 8}, {159, 14}, {170}, {159, 14}, {170, 8}, {162, 8}
                    },
                    {
                            {162, 8}, {170, 8}, {159, 14}, {170}, {159, 14}, {170, 8}, {162, 8}
                    },
                    {
                            {162}, {162, 4}, {162, 4}, {162, 4}, {162, 4}, {162, 4}, {162}
                    }
            },
            {//y = 2
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {162}, {85}, {85}, {85}, {162}, {}
                    },
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {}, {85}, {}, {}, {}, {85}, {}
                    },
                    {
                            {}, {162}, {}, {}, {}, {162}, {}
                    },
                    {
                            {162}, {}, {49}, {5, 4}, {49}, {}, {162}
                    },
                    {
                            {85}, {}, {45}, {126, 4}, {45}, {}, {85}
                    },
                    {
                            {162}, {}, {}, {}, {}, {}, {162}
                    },
                    {
                            {85}, {}, {}, {}, {}, {}, {85}
                    },
                    {
                            {162}, {}, {}, {}, {}, {}, {162}
                    },
                    {
                            {85}, {}, {}, {}, {}, {}, {85}
                    },
                    {
                            {162}, {}, {}, {}, {}, {}, {162}
                    }
            },
            {//y = 3
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {162}, {126, 4}, {126, 4}, {126, 4}, {162}, {}
                    },
                    {
                            {}, {126, 4}, {49}, {49}, {49}, {126, 4}, {}
                    },
                    {
                            {}, {126, 4}, {49}, {5, 4}, {49}, {126, 4}, {}
                    },
                    {
                            {}, {126, 4}, {49}, {5, 4}, {49}, {126, 4}, {}
                    },
                    {
                            {}, {162}, {45}, {126, 4}, {45}, {162}, {}
                    },
                    {
                            {162}, {85}, {}, {}, {}, {85}, {162}
                    },
                    {
                            {144, 1}, {}, {}, {}, {}, {}, {144, 1}
                    },
                    {
                            {162}, {}, {}, {}, {}, {}, {162}
                    },
                    {
                            {144, 1}, {}, {}, {}, {}, {}, {144, 1}
                    },
                    {
                            {162}, {}, {}, {}, {}, {}, {162}
                    },
                    {
                            {144, 1}, {}, {}, {}, {}, {}, {144, 1}
                    },
                    {
                            {162}, {}, {}, {}, {}, {}, {162}
                    }
            },
            {//y = 4
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {162}, {162, 4}, {162, 4}, {162, 4}, {162}, {}
                    },
                    {
                            {}, {162, 8}, {162}, {162}, {162}, {162, 8}, {}
                    },
                    {
                            {}, {162, 8}, {162}, {162}, {162}, {162, 8}, {}
                    },
                    {
                            {}, {162, 8}, {}, {}, {}, {162, 8}, {}
                    },
                    {
                            {}, {134, 1}, {}, {}, {}, {134, 1}, {}
                    },
                    {
                            {134}, {50}, {}, {}, {}, {50}, {134}
                    },
                    {
                            {162, 8}, {}, {}, {}, {}, {}, {162, 8}
                    },
                    {
                            {162, 8}, {}, {}, {}, {}, {}, {162, 8}
                    },
                    {
                            {162, 8}, {}, {}, {}, {}, {}, {162, 8}
                    },
                    {
                            {162, 8}, {}, {}, {}, {}, {}, {162, 8}
                    },
                    {
                            {162, 8}, {}, {}, {}, {}, {}, {162, 8}
                    },
                    {
                            {134, 1}, {}, {}, {}, {}, {}, {134, 1}
                    }
            },
            {//y = 5
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {162}, {49}, {49}, {49}, {162}, {}
                    },
                    {
                            {}, {162, 8}, {126, 4}, {126, 4}, {126, 4}, {162, 8}, {}
                    },
                    {
                            {}, {162, 8}, {126, 4}, {126, 4}, {126, 4}, {162, 8}, {}
                    },
                    {
                            {}, {134, 1}, {}, {}, {}, {134, 1}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {126, 4}, {}, {}, {}, {}, {}, {126, 4}
                    },
                    {
                            {45}, {}, {}, {}, {}, {}, {45}
                    },
                    {
                            {45}, {}, {}, {}, {}, {}, {45}
                    },
                    {
                            {45}, {}, {}, {}, {}, {}, {45}
                    },
                    {
                            {126, 4}, {}, {}, {}, {}, {}, {126, 4}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },
            {//y = 6
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {162}, {49}, {49}, {49}, {162}, {}
                    },
                    {
                            {}, {134, 1}, {}, {}, {}, {134, 1}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },
            {//y = 6.5
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {134, 2}, {162, 4}, {162, 4}, {162, 4}, {134, 3}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {50, 5}, {}, {}, {}, {50, 5}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },
            {//y = 7
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {162}, {162}, {162}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },
            {//y = 8
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {134, 2}, {41}, {134, 3}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            },
            {//y = 9
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {126, 4}, {}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
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
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}
                    }
            }
    };

        public static void generate(World world, int x, int y, int z, int direction) {
                System.out.println(direction);
                if (direction == 1) {z += 3;}
                if (direction == 2) { x += 3; z -= 10;}
//                if (direction == 3) { x -= 20;}
                if (direction == 4) { x -= 16; z -= 3;}
                Structure structure = MowziesMobs.GENERATOR.structures.get(3);
                MowziesMobs.GENERATOR.setStructure(structure);
                MowziesMobs.GENERATOR.setStructureFacing(direction);
                MowziesMobs.GENERATOR.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
                MowziesMobs.GENERATOR.generate(world, new Random(), x, y - 1, z);
//                System.out.println("Beginning generation at " + x + ", " + y + ", " + z);
                if (direction == 1) {
                        replaceBlocks(Blocks.obsidian, BlockHandler.INSTANCE.blockPaintedAcacia, x - 3, y - 1, z, 7, 10, 13, world);
                        replaceBlocks(Blocks.brick_block, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, x - 3, y - 1, z, 7, 10, 13, world);
                        for (int i = 0; i <= 2; i++) ((TileEntitySkull)world.getTileEntity(x - 3, y + 2, z + 7 + 2 * i)).func_145903_a(4);
                        for (int i = 0; i <= 2; i++) ((TileEntitySkull)world.getTileEntity(x + 3, y + 2, z + 7 + 2 * i)).func_145903_a(-4);
                        EntityTribeLeader barako = new EntityTribeLeader(world, direction);
                        barako.setPosition(x + 0.5, y + 6, z + 3.5);
                        world.spawnEntityInWorld(barako);
                }
                if (direction == 2) {
                        replaceBlocks(Blocks.obsidian, BlockHandler.INSTANCE.blockPaintedAcacia, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.brick_block, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, x - 7, y - 1, z, 13, 10, 7, world);

                        //Fix logs and hay
                        replaceBlocks(Blocks.log2, 8, Blocks.obsidian, 0, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.log2, 4, Blocks.log2, 8, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.obsidian, 0, Blocks.log2, 4, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.hay_block, 8, Blocks.hay_block, 4, x - 7, y - 1, z, 13, 10, 7, world);

                        for (int i = 0; i <= 2; i++) ((TileEntitySkull)world.getTileEntity(x - 1 - 2 * i, y + 2, z)).func_145903_a(8);
                        EntityTribeLeader barako = new EntityTribeLeader(world, direction);
                        barako.setPosition(x + 3.5, y + 6, z + 3.5);
                        world.spawnEntityInWorld(barako);
                }
                if (direction == 3) {
                        replaceBlocks(Blocks.obsidian, BlockHandler.INSTANCE.blockPaintedAcacia, x - 3, y - 1, z + 1, 7, 10, 13, world);
                        replaceBlocks(Blocks.brick_block, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, x - 3, y - 1, z + 1, 7, 10, 13, world);
                        for (int i = 0; i <= 2; i++) ((TileEntitySkull)world.getTileEntity(x + 3, y + 2, z + 1 + 2 * i)).func_145903_a(-4);
                        for (int i = 0; i <= 2; i++) ((TileEntitySkull)world.getTileEntity(x - 3, y + 2, z + 1 + 2 * i)).func_145903_a(4);
                        EntityTribeLeader barako = new EntityTribeLeader(world, direction);
                        barako.setPosition(x + 0.5, y + 6, z + 9.5);
                        world.spawnEntityInWorld(barako);
                }
                if (direction == 4) {
                        replaceBlocks(Blocks.obsidian, BlockHandler.INSTANCE.blockPaintedAcacia, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.brick_block, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, x - 7, y - 1, z, 13, 10, 7, world);

                        //Fix logs and hay
                        replaceBlocks(Blocks.log2, 8, Blocks.obsidian, 0, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.log2, 4, Blocks.log2, 8, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.obsidian, 0, Blocks.log2, 4, x - 7, y - 1, z, 13, 10, 7, world);
                        replaceBlocks(Blocks.hay_block, 8, Blocks.hay_block, 4, x - 7, y - 1, z, 13, 10, 7, world);

                        for (int i = 0; i <= 2; i++) ((TileEntitySkull)world.getTileEntity(x + 1 + 2 * i, y + 2, z)).func_145903_a(8);
                        EntityTribeLeader barako = new EntityTribeLeader(world, direction);
                        barako.setPosition(x - 2.5, y + 6, z + 3.5);
                        world.spawnEntityInWorld(barako);
                }
        }
}
