package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.container.ContainerTest;
import com.bobmowzie.mowziesmobs.tile.TileTest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTest extends GuiContainer
{

	public GuiTest(InventoryPlayer inventoryPlayer, TileTest tileEntity)
	{
		super(new ContainerTest(inventoryPlayer, tileEntity));
		this.xSize = 176;
		this.ySize = 188;
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)
	{

	}

	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{

	}
}
