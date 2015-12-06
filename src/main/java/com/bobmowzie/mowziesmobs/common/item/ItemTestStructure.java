package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import com.bobmowzie.mowziesmobs.common.entity.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.common.gen.structure.barakoa.StructureBarakoThrone;
import com.bobmowzie.mowziesmobs.common.gen.structure.barakoa.StructureBarakoaHouse;
import net.ilexiconn.llibrary.common.structure.util.Structure;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by jnad325 on 6/24/15.
 */
public class ItemTestStructure extends Item
{
    int structure = 0;
    public ItemTestStructure()
    {
        super();
        setCreativeTab(MMTabs.generic);
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking()) {
            Random rand = new Random();
            if (!world.isRemote) {
                if (structure == 0) StructureBarakoaHouse.generateHouse1(world, x, y, z, rand.nextInt(4) + 1);
                else if (structure == 1) StructureBarakoThrone.generate(world, x, y, z, rand.nextInt(4) + 1);
                else if (structure == 2) generateWroughtChamber(world, rand, x, y, z, rand.nextInt(4) + 1);
                return true;
            }
            return false;
        }
        else {
            structure++;
            if (structure > 2) structure = 0;
        }
        return true;
    }

//    private void tryWroughtChamber(World world, Random random, int x, int z, int y)
//    {
//        int xzCheckDistance = 20;
//
//                if (world.getBlock(x, y, z).isAir(world, x, y, z))
//                {
//                    System.out.println("Starting point is air");
//                    for (int y2 = 1; y2 <= 30; y2++)
//                    {
//                        if (world.getBlock(x, y - y2, z).isOpaqueCube())
//                        {
//                            System.out.println("Found floor at " + (y - y2));
//                            int y4 = 0;
//                            int y5 = 0;
//                            for (int x2 = 0; x2 <= xzCheckDistance; x2++)
//                            {
//                                if (world.getBlock(x - x2, y - y2 + y4 + 1, z).isOpaqueCube())
//                                {
//                                    System.out.println("Found obstacle decreasing x");
//                                    Boolean wall = true;
//                                    for (int y3 = 1; y3 <= 4; y3 ++)
//                                    {
//                                        if (!world.getBlock(x - x2, y - y2 + y4 + 1 + y3, z).isOpaqueCube())
//                                        {
//                                            wall = false;
//                                            y4 += y3;
//                                            System.out.println("Raising search by " + y4 + " blocks");
//                                            break;
//                                        }
//                                    }
//                                    if (wall)
//                                    {
//                                        generateWroughtChamber(world, random, x - x2 - 9, y - y2 + y4, z - 9, 0);
//                                        return;
//                                    }
//                                }
//                                if (world.getBlock(x + x2, y - y2 + y5 + 1, z).isOpaqueCube())
//                                {
//                                    System.out.println("Found obstacle increasing x");
//                                    Boolean wall = true;
//                                    for (int y3 = 1; y3 <= 4; y3++)
//                                    {
//                                        if (!world.getBlock(x + x2, y - y2 + y5 + 1 + y3, z).isOpaqueCube())
//                                        {
//                                            wall = false;
//                                            y5 += y3;
//                                            break;
//                                        }
//                                    }
//                                    if (wall)
//                                    {
//                                        generateWroughtChamber(world, random, x + x2 + 9, y - y2 + y5, z - 9, 2);
//                                        return;
//                                    }
//                                }
//                            }
//                            System.out.println("Checking other axes");
//                            y4 = 0;
//                            y5 = 0;
//                            for (int z2 = 0; z2 <= xzCheckDistance; z2++)
//                            {
//                                if (world.getBlock(x - 1, y - y2 + y4 + 1, z-z2).isOpaqueCube())
//                                {
//                                    System.out.println("Found obstacle decreasing z");
//                                    Boolean wall = true;
//                                    for (int y3 = 1; y3 <= 4; y3 ++)
//                                    {
//                                        if (!world.getBlock(x - 1, y - y2 + y4 + 1 + y3, z-z2).isOpaqueCube())
//                                        {
//                                            wall = false;
//                                            y4 += y3;
//                                            break;
//                                        }
//                                    }
//                                    if (wall)
//                                    {
//                                        generateWroughtChamber(world, random, x - 1, y - y2 + y4, z - z2 - 18, 1);
//                                        return;
//                                    }
//                                }
//                                if (world.getBlock(x - 1, y - y2 + y5 + 1, z+z2).isOpaqueCube())
//                                {
//                                    System.out.println("Found obstacle increasing z");
//                                    Boolean wall = true;
//                                    for (int y3 = 1; y3 <= 4; y3 ++)
//                                    {
//                                        if (!world.getBlock(x - 1, y - y2 + y5 + 1 + y3, z+z2).isOpaqueCube())
//                                        {
//                                            wall = false;
//                                            y5 += y3;
//                                            break;
//                                        }
//                                    }
//                                    if (wall)
//                                    {
//                                        generateWroughtChamber(world, random, x - 1, y - y2 + y5, z + z2, 3);
//                                        return;
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//
    private void generateWroughtChamber(World world, Random random, int x, int y, int z, int direction)
    {
        Structure structure = MowziesMobs.gen.structures.get(0);
        EntityWroughtnaut wroughtnaut = new EntityWroughtnaut(world);
        wroughtnaut.setPositionAndRotation(x + 0.5, y + 1, z + 9.5, 0, 0);
        MowziesMobs.gen.setStructure(structure);
        MowziesMobs.gen.setStructureFacing(direction);
        MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
        MowziesMobs.gen.generate(world, random, x, y - 1, z);
        System.out.println(x + ", " + y + ", " + z);
        world.spawnEntityInWorld(wroughtnaut);
    }

    @Override
    public void registerIcons(IIconRegister registrar)
    {
    }
}