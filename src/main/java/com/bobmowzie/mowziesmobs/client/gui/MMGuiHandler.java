package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.container.ContainerTest;
import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MMGuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileBabyFoliaath && ID == 0)
            return new ContainerTest(player.inventory, (TileBabyFoliaath) tileEntity);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileBabyFoliaath) return new GuiTest(player.inventory, (TileBabyFoliaath) tileEntity);
        return null;
    }
}
