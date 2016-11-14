package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarako;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarakoaya;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public final class GuiBarakoTrade extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barako.png");

    private final EntityBarako barako;
    private final InventoryBarako inventory;

    private ItemStack input;
    private ItemStack output;

    private GuiButton button;

    public GuiBarakoTrade(EntityBarako barako, InventoryPlayer playerInv, World world) {
        this(barako, new InventoryBarako(barako), playerInv, world);
    }

    public GuiBarakoTrade(EntityBarako barako, InventoryBarako inventory, InventoryPlayer playerInv, World world) {
        super(new ContainerBarakoTrade(barako, playerInv, world));
        this.barako = barako;
        this.inventory = inventory;
        input = barako.desires;
        output = new ItemStack(ItemHandler.INSTANCE.grantSunsBlessing);
        button = new GuiButton(0, 0, 0, 47, 20, "Receive");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float delta, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GuiInventory.drawEntityOnScreen(guiLeft + 33, guiTop + 56, 14, guiLeft + 33 - mouseX, guiTop + 16 - mouseY, barako);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("entity.barako.trade");
        fontRendererObj.drawString(title, (xSize / 2 - fontRendererObj.getStringWidth(title) / 2) + 30, 6, 0x404040);
        fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        super.drawScreen(mouseX, mouseY, delta);
        ItemStack inSlot = inventory.getStackInSlot(0);
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        itemRender.zLevel = 100;
        itemRender.renderItemAndEffectIntoGUI(input, guiLeft + 68, guiTop + 24);
        itemRender.renderItemOverlays(fontRendererObj, input, guiLeft + 68, guiTop + 24);
        itemRender.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
        itemRender.renderItemOverlays(fontRendererObj, output, guiLeft + 134, guiTop + 24);
        itemRender.zLevel = 0;
        if (inSlot != null && inSlot.getItem() == barako.desires.getItem() && inSlot.stackSize >= barako.desires.stackSize) {
            button.enabled = true;
        }
        else {
            button.enabled = false;
        }
        button.xPosition = guiLeft + 119;
        button.yPosition = guiTop + 52;
        button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        GlStateManager.disableLighting();
        if (isPointInRegion(80, 24, 16, 16, mouseX, mouseY)) {
            renderToolTip(input, mouseX, mouseY);
        } else if (isPointInRegion(134, 24, 16, 16, mouseX, mouseY)) {
            renderToolTip(output, mouseX, mouseY);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
