package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarako;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageBarakoTrade;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class GuiBarakoTrade extends ContainerScreen implements InventoryBarako.ChangeListener {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barako.png");

    private final EntityBarako barako;

    private final InventoryBarako inventory;

    private final ItemStack output = new ItemStack(ItemHandler.GRANT_SUNS_BLESSING);

    private Button grantButton;

    private boolean hasTraded;

    public GuiBarakoTrade(EntityBarako barako, ContainerBarakoTrade container, PlayerInventory playerInv, ITextComponent title, boolean hasTraded) {
        this(barako, new InventoryBarako(barako), container, playerInv, title, hasTraded);
    }

    public GuiBarakoTrade(EntityBarako barako, InventoryBarako inventory, ContainerBarakoTrade container, PlayerInventory playerInv, ITextComponent title, boolean hasTraded) {
        super(container, playerInv, title);
        this.barako = barako;
        this.inventory = inventory;
        this.hasTraded = hasTraded;
        inventory.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        String text = I18n.format(hasTraded ? "entity.barako.replenish.button.text" : "entity.barako.trade.button.text");
//        grantButton = addButton(new Button(0, guiLeft + 114, guiTop + 52, 57, text, )); // TODO: Last parameter?
        grantButton.active = hasTraded;
        updateButtonText();
    }

    protected void actionPerformed(Button button) {
    	if (button == grantButton) {
            hasTraded = true;
            updateButtonText();
            MowziesMobs.NETWORK.sendToServer(new MessageBarakoTrade(barako));
    	}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float delta, int mouseX, int mouseY) {
        GlStateManager.color3f(1, 1, 1);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        InventoryScreen.drawEntityOnScreen(guiLeft + 33, guiTop + 56, 14, 0, 0, barako);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("entity.barako.trade");
        font.drawString(title, (xSize / 2f - font.getStringWidth(title) / 2f) + 30, 6, 0x404040);
        font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        ItemStack inSlot = inventory.getStackInSlot(0);
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        itemRenderer.zLevel = 100;
        itemRenderer.renderItemAndEffectIntoGUI(barako.getDesires(), guiLeft + 68, guiTop + 24);
        itemRenderer.renderItemOverlays(font, barako.getDesires(), guiLeft + 68, guiTop + 24);
        itemRenderer.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
        itemRenderer.renderItemOverlays(font, output, guiLeft + 134, guiTop + 24);
        itemRenderer.zLevel = 0;
        GlStateManager.disableLighting();
        if (isPointInRegion(68, 24, 16, 16, mouseX, mouseY)) {
            renderTooltip(barako.getDesires(), mouseX, mouseY);
        } else if (isPointInRegion(134, 24, 16, 16, mouseX, mouseY)) {
            renderTooltip(output, mouseX, mouseY);
        } else if (grantButton.isMouseOver(mouseX, mouseY)) {
            renderComponentHoverEffect(getHoverText(), mouseX, mouseY);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

	@Override
	public void onChange(IInventory inv) {
        grantButton.active = hasTraded || barako.doesItemSatisfyDesire(inv.getStackInSlot(0));
	}

    private void updateButtonText() {
        grantButton.setMessage(I18n.format(hasTraded ? "entity.barako.replenish.button.text" : "entity.barako.trade.button.text"));
    }

    private ITextComponent getHoverText() {
        return new TranslationTextComponent(I18n.format(hasTraded ? "entity.barako.replenish.button.hover" : "entity.barako.trade.button.hover"));
    }
}
