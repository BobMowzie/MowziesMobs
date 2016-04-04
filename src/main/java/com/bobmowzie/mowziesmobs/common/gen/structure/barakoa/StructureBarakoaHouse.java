package com.bobmowzie.mowziesmobs.common.gen.structure.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.block.BlockHandler;
import com.bobmowzie.mowziesmobs.common.gen.MowzieStructureGenerator;
import com.bobmowzie.mowziesmobs.common.gen.structure.StructureBase;
import coolalias.structuregenapi.util.Structure;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;

import java.util.Random;

public class StructureBarakoaHouse extends StructureBase {
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

    public static int[][][][] blockArray2 = {
            {
                    {
                            {}, {170}, {170}, {170}, {}
                    },
                    {
                            {170}, {170}, {170}, {170}, {170}
                    },
                    {
                            {170}, {170}, {170}, {170}, {170}
                    },
                    {
                            {170}, {170}, {170}, {170}, {170}
                    },
                    {
                            {}, {170}, {170}, {170}, {}
                    }
            },
            {
                    {
                            {}, {85}, {144, 1}, {85}, {}
                    },
                    {
                            {85}, {}, {}, {}, {85}
                    },
                    {
                            {144, 1}, {}, {}, {}, {144, 1}
                    },
                    {
                            {85}, {}, {}, {}, {85}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {144, 1}, {}, {144, 1}, {}
                    },
                    {
                            {144, 1}, {}, {}, {}, {144, 1}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {144, 1}, {}, {}, {}, {144, 1}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            }
    };

    public static int[][][][] blockArray3 = {
            {
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {87}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {}, {}, {162}, {}, {162}, {}, {}, {}
                    },
                    {
                            {}, {162}, {}, {}, {}, {}, {}, {162}, {}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {162}, {}, {}, {}, {44, 3}, {}, {}, {}, {162}
                    },
                    {
                            {}, {}, {}, {44, 3}, {51}, {44, 3}, {}, {}, {}
                    },
                    {
                            {162}, {}, {}, {}, {44, 3}, {}, {}, {}, {162}
                    },
                    {
                            {}, {}, {}, {}, {}, {}, {}, {}, {}
                    },
                    {
                            {}, {162}, {}, {}, {}, {}, {}, {162}, {}
                    },
                    {
                            {}, {}, {}, {162}, {}, {162}, {}, {}, {}
                    }
            }
    };

    public static int[][][][] blockArray4 = {
            {
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {85}, {}, {85}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {85}, {}, {85}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {126, 12}, {}, {126, 12}, {}
                    },
                    {
                            {126, 12}, {162}, {126, 12}, {162}, {126, 12}
                    },
                    {
                            {}, {126, 12}, {126, 12}, {126, 12}, {}
                    },
                    {
                            {126, 12}, {162}, {126, 12}, {162}, {126, 12}
                    },
                    {
                            {}, {126, 12}, {}, {126, 12}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {162, 8}, {}, {162, 8}, {}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {}, {162, 8}, {170}, {162, 8}, {}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {}, {162, 8}, {126, 9}, {162, 8}, {}
                    },
                    {
                            {}, {}, {126, 1}, {}, {}
                    },
                    {
                            {}, {}, {126, 9}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {85}, {}, {85}, {}
                    },
                    {
                            {85}, {162}, {126, 4}, {162}, {85}
                    },
                    {
                            {}, {126, 4}, {0}, {126, 4}, {}
                    },
                    {
                            {85}, {162}, {0}, {162}, {85}
                    },
                    {
                            {}, {85}, {0}, {85}, {18, 3}
                    },
                    {
                            {}, {}, {0}, {}, {}
                    },
                    {
                            {}, {}, {0}, {}, {}
                    },
                    {
                            {}, {}, {0}, {}, {}
                    }
            },
            {
                    {
                            {18, 3}, {85}, {18, 3}, {85}, {}
                    },
                    {
                            {85}, {162}, {49}, {162}, {85}
                    },
                    {
                            {}, {49}, {0}, {49}, {}
                    },
                    {
                            {85}, {162}, {0}, {162}, {85}
                    },
                    {
                            {}, {85}, {0}, {85}, {18, 3}
                    },
                    {
                            {}, {}, {0}, {}, {}
                    },
                    {
                            {}, {}, {0}, {}, {}
                    },
                    {
                            {}, {}, {0}, {}, {}
                    }
            },
            {
                    {
                            {18, 3}, {162, 8}, {18, 3}, {162, 8}, {18, 3}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {18, 3}, {162, 8}, {170}, {162, 8}, {}
                    },
                    {
                            {162, 4}, {162}, {162, 4}, {162}, {162, 4}
                    },
                    {
                            {18, 3}, {162, 8}, {}, {162, 8}, {18, 3}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {}
                    },
                    {
                            {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}
                    },
                    {
                            {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}
                    },
                    {
                            {18, 3}, {18, 3}, {18, 3}, {18, 3}, {18, 3}
                    },
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            },
            {
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {18, 3}, {}, {18, 3}, {}
                    },
                    {
                            {}, {18, 3}, {18, 3}, {18, 3}, {}
                    },
                    {
                            {}, {}, {18, 3}, {18, 3}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    },
                    {
                            {}, {}, {}, {}, {}
                    }
            }
    };

    public static void generateHouse1(World world, int x, int y, int z, int direction) {
        Random rand = new Random();
        Structure structure = MowzieStructureGenerator.structures.get(1);
        MowziesMobs.GENERATOR.setStructure(structure);
        MowziesMobs.GENERATOR.setStructureFacing(direction);
        MowziesMobs.GENERATOR.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
        MowziesMobs.GENERATOR.generate(world, rand, x, y, z);
        replaceBlocks(Blocks.planks, 4, BlockHandler.INSTANCE.blockPaintedAcacia, 0, x - 3, y, z, 7, 9, 7, world);
        replaceBlocks(Blocks.wooden_slab, 4, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, 0, x - 3, y, z, 7, 9, 7, world);
        replaceBlocks(Blocks.obsidian, 0, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, 12, x - 3, y, z, 7, 9, 7, world);

        //Fix Logs depending on rotation
        if (direction == 2 || direction == 4) {
            replaceBlocks(Blocks.log2, 8, Blocks.obsidian, 0, x - 3, y, z, 7, 9, 7, world);
            replaceBlocks(Blocks.log2, 4, Blocks.log2, 8, x - 3, y, z, 7, 9, 7, world);
            replaceBlocks(Blocks.obsidian, 0, Blocks.log2, 4, x - 3, y, z, 7, 9, 7, world);
        }

        //Stairs
        if (direction == 1) {
            for (int i = 1; i < 20; i++) {
                if (!world.getBlock(x, y + 2 - i, z + 6 + i).isOpaqueCube()) {
                    world.setBlock(x, y + 2 - i, z + 6 + i, Block.getBlockById(134), 3, 3);
                    world.setBlock(x, y + 2 - i, z + 6 + i - 1, Block.getBlockById(134), 6, 3);
                } else {
                    break;
                }
                int dir = rand.nextInt(6) + 1;
                if (dir <= 2) {
                    dir = (dir == 1) ? 2 : 4;
                    generateHouseExtra(world, x, y, z + 3, dir);
                }
            }
        } else if (direction == 2) {
            for (int i = 1; i < 20; i++) {
                if (!world.getBlock(x - 3 - i, y + 2 - i, z + 3).isOpaqueCube()) {
                    world.setBlock(x - 3 - i, y + 2 - i, z + 3, Block.getBlockById(134), 0, 3);
                    world.setBlock(x - 3 - i + 1, y + 2 - i, z + 3, Block.getBlockById(134), 5, 3);
                } else {
                    break;
                }
                int dir = rand.nextInt(6) + 1;
                if (dir <= 2) {
                    dir = (dir == 2) ? 1 : 3;
                    generateHouseExtra(world, x, y, z + 3, dir);
                }
            }
        } else if (direction == 3) {
            for (int i = 1; i < 20; i++) {
                if (!world.getBlock(x, y + 2 - i, z - i).isOpaqueCube()) {
                    world.setBlock(x, y + 2 - i, z - i, Block.getBlockById(134), 2, 3);
                    world.setBlock(x, y + 2 - i, z - i + 1, Block.getBlockById(134), 7, 3);
                } else {
                    break;
                }
                int dir = rand.nextInt(6) + 1;
                if (dir <= 2) {
                    dir = (dir == 1) ? 2 : 4;
                    generateHouseExtra(world, x, y, z + 3, dir);
                }
            }
        } else if (direction == 4) {
            for (int i = 1; i < 20; i++) {
                if (!world.getBlock(x + 3 + i, y + 2 - i, z + 3).isOpaqueCube()) {
                    world.setBlock(x + 3 + i, y + 2 - i, z + 3, Block.getBlockById(134), 1, 3);
                    world.setBlock(x + 3 + i - 1, y + 2 - i, z + 3, Block.getBlockById(134), 4, 3);
                } else {
                    break;
                }
                int dir = rand.nextInt(6) + 1;
                if (dir <= 2) {
                    dir = (dir == 2) ? 1 : 3;
                    generateHouseExtra(world, x, y, z + 3, dir);
                }
            }
        }

        //Fence poles
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x + 2, y - i, z + 1).isOpaqueCube()) {
                world.setBlock(x + 2, y - i, z + 1, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x - 2, y - i, z + 1).isOpaqueCube()) {
                world.setBlock(x - 2, y - i, z + 1, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x + 2, y - i, z + 5).isOpaqueCube()) {
                world.setBlock(x + 2, y - i, z + 5, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x - 2, y - i, z + 5).isOpaqueCube()) {
                world.setBlock(x - 2, y - i, z + 5, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }

        //Interior
        int tableCorner = rand.nextInt(6);
        int tableContent = rand.nextInt(4);
        if (tableCorner == 0) {
            world.setBlock(x - 1, y + 4, z + 4, Block.getBlockById(126), 12, 3);
            if (tableContent <= 1) {
                world.setBlock(x - 1, y + 5, z + 4, Block.getBlockById(50), 5, 3);
            }
            if (tableContent == 2) {
                world.setBlock(x - 1, y + 5, z + 4, Block.getBlockById(144), 1, 3);
                ((TileEntitySkull) world.getTileEntity(x - 1, y + 5, z + 4)).func_145903_a(2);
            }
        }
        if (tableCorner == 1) {
            world.setBlock(x + 1, y + 4, z + 4, Block.getBlockById(126), 12, 3);
            if (tableContent <= 1) {
                world.setBlock(x + 1, y + 5, z + 4, Block.getBlockById(50), 5, 3);
            }
            if (tableContent == 2) {
                world.setBlock(x + 1, y + 5, z + 4, Block.getBlockById(144), 1, 3);
                ((TileEntitySkull) world.getTileEntity(x + 1, y + 5, z + 4)).func_145903_a(-2);
            }
        }
        if (tableCorner == 2) {
            world.setBlock(x - 1, y + 4, z + 2, Block.getBlockById(126), 12, 3);
            if (tableContent <= 1) {
                world.setBlock(x - 1, y + 5, z + 2, Block.getBlockById(50), 5, 3);
            }
            if (tableContent == 2) {
                world.setBlock(x - 1, y + 5, z + 2, Block.getBlockById(144), 1, 3);
                ((TileEntitySkull) world.getTileEntity(x - 1, y + 5, z + 2)).func_145903_a(6);
            }
        }
        if (tableCorner == 3) {
            world.setBlock(x + 1, y + 4, z + 2, Block.getBlockById(126), 12, 3);
            if (tableContent <= 1) {
                world.setBlock(x + 1, y + 5, z + 2, Block.getBlockById(50), 5, 3);
            }
            if (tableContent == 2) {
                world.setBlock(x + 1, y + 5, z + 2, Block.getBlockById(144), 1, 3);
                ((TileEntitySkull) world.getTileEntity(x + 1, y + 5, z + 2)).func_145903_a(-6);
            }
        }

        int bedCorner = rand.nextInt(6);
        int bedDirection = rand.nextInt(2);
        if (bedCorner == tableCorner) {
            bedCorner = 6;
        }
        if (bedCorner == 0) {
            world.setBlock(x - 1, y + 4, z + 4, Block.getBlockById(171), 4, 3);
            if (bedDirection == 0) {
                world.setBlock(x, y + 4, z + 4, Block.getBlockById(171), 4, 3);
            } else {
                world.setBlock(x - 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
            }
        }
        if (bedCorner == 1) {
            world.setBlock(x + 1, y + 4, z + 4, Block.getBlockById(171), 4, 3);
            if (bedDirection == 0) {
                world.setBlock(x, y + 4, z + 4, Block.getBlockById(171), 4, 3);
            } else {
                world.setBlock(x + 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
            }
        }
        if (bedCorner == 2) {
            world.setBlock(x - 1, y + 4, z + 2, Block.getBlockById(171), 4, 3);
            if (bedDirection == 0) {
                world.setBlock(x, y + 4, z + 2, Block.getBlockById(171), 4, 3);
            } else {
                world.setBlock(x - 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
            }
        }
        if (bedCorner == 3) {
            world.setBlock(x + 1, y + 4, z + 2, Block.getBlockById(171), 4, 3);
            if (bedDirection == 0) {
                world.setBlock(x, y + 4, z + 2, Block.getBlockById(171), 4, 3);
            } else {
                world.setBlock(x + 1, y + 4, z + 3, Block.getBlockById(171), 4, 3);
            }
        }
    }

    public static void generateSkulls(World world, int x, int y, int z, int direction) {
        Structure structure = MowzieStructureGenerator.structures.get(2);
        MowziesMobs.GENERATOR.setStructure(structure);
        MowziesMobs.GENERATOR.setStructureFacing(direction);
        MowziesMobs.GENERATOR.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
        MowziesMobs.GENERATOR.generate(world, new Random(), x, y - 1, z);

        if (direction == 1) {
            ((TileEntitySkull) world.getTileEntity(x, y + 1, z)).func_145903_a(-7);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 1, z + 2)).func_145903_a(4);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 1, z + 2)).func_145903_a(-5);
            ((TileEntitySkull) world.getTileEntity(x + 1, y + 2, z)).func_145903_a(8);
            ((TileEntitySkull) world.getTileEntity(x - 1, y + 2, z)).func_145903_a(10);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 2, z + 1)).func_145903_a(-9);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 2, z + 3)).func_145903_a(10);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 2, z + 1)).func_145903_a(-9);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 2, z + 3)).func_145903_a(-10);
        }
        if (direction == 2) {
            ((TileEntitySkull) world.getTileEntity(x, y + 1, z)).func_145903_a(-7);
            ((TileEntitySkull) world.getTileEntity(x, y + 1, z + 4)).func_145903_a(0);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 1, z + 2)).func_145903_a(-5);
            ((TileEntitySkull) world.getTileEntity(x + 1, y + 2, z)).func_145903_a(8);
            ((TileEntitySkull) world.getTileEntity(x - 1, y + 2, z)).func_145903_a(10);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 2, z + 1)).func_145903_a(-9);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 2, z + 3)).func_145903_a(10);
            ((TileEntitySkull) world.getTileEntity(x + 1, y + 2, z + 4)).func_145903_a(-1);
            ((TileEntitySkull) world.getTileEntity(x - 1, y + 2, z + 4)).func_145903_a(2);
        }
        if (direction == 3) {
            ((TileEntitySkull) world.getTileEntity(x, y + 1, z + 4)).func_145903_a(0);
            ((TileEntitySkull) world.getTileEntity(x + 1, y + 2, z + 4)).func_145903_a(-1);
            ((TileEntitySkull) world.getTileEntity(x - 1, y + 2, z + 4)).func_145903_a(2);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 1, z + 2)).func_145903_a(-5);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 2, z + 1)).func_145903_a(-2);
            ((TileEntitySkull) world.getTileEntity(x + 2, y + 2, z + 3)).func_145903_a(-4);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 2, z + 1)).func_145903_a(5);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 2, z + 3)).func_145903_a(3);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 1, z + 2)).func_145903_a(4);
        }
        if (direction == 4) {
            ((TileEntitySkull) world.getTileEntity(x, y + 1, z)).func_145903_a(-7);
            ((TileEntitySkull) world.getTileEntity(x, y + 1, z + 4)).func_145903_a(0);
            ((TileEntitySkull) world.getTileEntity(x + 1, y + 2, z + 4)).func_145903_a(-1);
            ((TileEntitySkull) world.getTileEntity(x - 1, y + 2, z + 4)).func_145903_a(2);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 2, z + 1)).func_145903_a(5);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 2, z + 3)).func_145903_a(3);
            ((TileEntitySkull) world.getTileEntity(x - 2, y + 1, z + 2)).func_145903_a(4);
            ((TileEntitySkull) world.getTileEntity(x + 1, y + 2, z)).func_145903_a(8);
            ((TileEntitySkull) world.getTileEntity(x - 1, y + 2, z)).func_145903_a(10);
        }
    }

    public static void generateFirepit(World world, int x, int y, int z) {
        Structure structure = MowzieStructureGenerator.structures.get(4);
        MowziesMobs.GENERATOR.setStructure(structure);
        MowziesMobs.GENERATOR.setDefaultOffset(-4, 0, 0);
        MowziesMobs.GENERATOR.generate(world, new Random(), x, y - 1, z);
    }

    public static void generateHouseExtra(World world, int x, int y, int z, int direction) {
        Structure structure = MowzieStructureGenerator.structures.get(5);
        MowziesMobs.GENERATOR.setStructure(structure);
        MowziesMobs.GENERATOR.setDefaultOffset(2, 0, 0);
        MowziesMobs.GENERATOR.setStructureFacing(direction);
        MowziesMobs.GENERATOR.generate(world, new Random(), x, y, z);

        if (direction == 1) {
            z = z - 9;
        }
        if (direction == 2) {
            x = x + 4;
        }
        if (direction == 3) {
            z = z + 5;
        }
        if (direction == 4) {
            x = x - 9;
        }

        if (direction == 2 || direction == 4) {
            replaceBlocks(Blocks.wooden_slab, 4, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, 0, x, y, z - 2, 5, 9, 5, world);
            replaceBlocks(Blocks.obsidian, 0, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, 12, x, y, z - 2, 5, 9, 5, world);
            replaceBlocks(Blocks.log2, 8, Blocks.obsidian, 0, x, y, z - 2, 5, 9, 5, world);
            replaceBlocks(Blocks.log2, 4, Blocks.log2, 8, x, y, z - 2, 5, 9, 5, world);
            replaceBlocks(Blocks.obsidian, 0, Blocks.log2, 4, x, y, z - 2, 5, 9, 5, world);
        } else {
            replaceBlocks(Blocks.wooden_slab, 4, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, 0, x - 2, y, z, 5, 9, 5, world);
            replaceBlocks(Blocks.obsidian, 0, BlockHandler.INSTANCE.blockPaintedAcaciaSlab, 12, x - 2, y, z, 5, 9, 5, world);
        }

        if (direction == 2) {
            z = z - 2;
            x = x + 3;
        }
        if (direction == 4) {
            z = z - 2;
            x = x - 2;
        }

        //Fence poles
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x + 1, y - i, z + 1).isOpaqueCube()) {
                world.setBlock(x + 1, y - i, z + 1, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x - 1, y - i, z + 1).isOpaqueCube()) {
                world.setBlock(x - 1, y - i, z + 1, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x + 1, y - i, z + 3).isOpaqueCube()) {
                world.setBlock(x + 1, y - i, z + 3, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            if (!world.getBlock(x - 1, y - i, z + 3).isOpaqueCube()) {
                world.setBlock(x - 1, y - i, z + 3, Block.getBlockById(85), 0, 3);
            } else {
                break;
            }
        }
    }

    public static void generateSkull(World world, Random rand, int x, int y, int z) {
        world.setBlock(x, y + 1, z, Block.getBlockById(85));
        world.setBlock(x, y + 2, z, Block.getBlockById(144), 1, 3);
        ((TileEntitySkull) world.getTileEntity(x, y + 2, z)).func_145903_a(rand.nextInt(21) - 10);
    }

    public static void generateTorch(World world, int x, int y, int z) {
        world.setBlock(x, y + 1, z, Block.getBlockById(85));
        world.setBlock(x, y + 2, z, Block.getBlockById(50), 5, 3);
    }
}
