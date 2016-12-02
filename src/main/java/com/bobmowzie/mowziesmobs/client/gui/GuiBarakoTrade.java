package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarako;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;

import com.bobmowzie.mowziesmobs.server.message.MessageBarakoTrade;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class GuiBarakoTrade extends GuiContainer implements InventoryBarako.ChangeListener {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barako.png");

    private final EntityBarako barako;

    private final InventoryBarako inventory;

    private final ItemStack output = new ItemStack(ItemHandler.INSTANCE.grantSunsBlessing);

    private GuiButton grantButton;

    private boolean hasTraded;

    public GuiBarakoTrade(EntityBarako barako, InventoryPlayer playerInv, World world, boolean hasTraded) {
        this(barako, new InventoryBarako(barako), playerInv, world, hasTraded);
    }

    public GuiBarakoTrade(EntityBarako barako, InventoryBarako inventory, InventoryPlayer playerInv, World world, boolean hasTraded) {
        super(new ContainerBarakoTrade(barako, inventory, playerInv, world));
        this.barako = barako;
        this.inventory = inventory;
        this.hasTraded = hasTraded;
        inventory.addListener(this);
    }

    @Override
    public void initGui() {
    	super.initGui();
    	buttonList.clear();
        String text = I18n.format(hasTraded ? "entity.barako.replenish.button.text" : "entity.barako.trade.button.text");
        grantButton = addButton(new GuiButton(0, guiLeft + 114, guiTop + 52, 57, 20, text));
        grantButton.enabled = hasTraded;
        updateButtonText();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button == grantButton) {
            hasTraded = true;
            updateButtonText();
            MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessageBarakoTrade(barako));
    	}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float delta, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GuiInventory.drawEntityOnScreen(guiLeft + 33, guiTop + 56, 14, 0, 0, barako);
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
        itemRender.renderItemAndEffectIntoGUI(barako.getDesires(), guiLeft + 68, guiTop + 24);
        itemRender.renderItemOverlays(fontRendererObj, barako.getDesires(), guiLeft + 68, guiTop + 24);
        itemRender.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
        itemRender.renderItemOverlays(fontRendererObj, output, guiLeft + 134, guiTop + 24);
        itemRender.zLevel = 0;
        GlStateManager.disableLighting();
        if (isPointInRegion(68, 24, 16, 16, mouseX, mouseY)) {
            renderToolTip(barako.getDesires(), mouseX, mouseY);
        } else if (isPointInRegion(134, 24, 16, 16, mouseX, mouseY)) {
            renderToolTip(output, mouseX, mouseY);
        } else if (grantButton.isMouseOver()) {
            drawHoveringText(getHoverText(), mouseX, mouseY);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

	@Override
	public void onChange(IInventory inv) {
        grantButton.enabled = hasTraded || barako.doesItemSatisfyDesire(inv.getStackInSlot(0));
	}

    private void updateButtonText() {
        grantButton.displayString = I18n.format(hasTraded ? "entity.barako.replenish.button.text" : "entity.barako.trade.button.text");
    }

    private List<String> getHoverText() {
        return Collections.singletonList(I18n.format(hasTraded ? "entity.barako.replenish.button.hover" : "entity.barako.trade.button.hover"));
    }
}
