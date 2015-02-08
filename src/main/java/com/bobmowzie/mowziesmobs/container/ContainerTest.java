package com.bobmowzie.mowziesmobs.container;

import com.bobmowzie.mowziesmobs.tile.TileTest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerTest extends Container
{
    public ContainerTest(InventoryPlayer inventory, TileTest tileEntity)
    {
    	
    }

    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
