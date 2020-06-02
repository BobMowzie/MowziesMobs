package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarakoaya;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

public final class GuiBarakoayaTrade extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barakoa.png");

    private final EntityBarakoaya barakoaya;

    private final InventoryBarakoaya inventory;

    private int cursorHit;

    public GuiBarakoayaTrade(EntityBarakoaya barakoaya, InventoryPlayer playerInv, World world) {
        this(barakoaya, new InventoryBarakoaya(barakoaya), playerInv, world);
    }

    private GuiBarakoayaTrade(EntityBarakoaya barakoaya, InventoryBarakoaya inventory, InventoryPlayer playerInv, World world) {
        super(new ContainerBarakoayaTrade(barakoaya, inventory, playerInv, world));
        this.barakoaya = barakoaya;
        this.inventory = inventory;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (barakoaya.getAnimation() == EntityBarakoaya.ATTACK_ANIMATION && barakoaya.getAnimationTick() == 8) {
            cursorHit = 6;
        }
        if (cursorHit > 0) {
            cursorHit--;
            Mouse.setCursorPosition(Mouse.getX() + cursorHit * 16 / 6, Mouse.getY() - cursorHit * 44 / 6);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (barakoaya.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            if (isPointInRegion(13, 23, 8, 14, mouseX, mouseY)) {
                barakoaya.setAnimation(EntityBarakoaya.ATTACK_ANIMATION);
                barakoaya.setAnimationTick(3);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float delta, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GuiInventory.drawEntityOnScreen(guiLeft + 33, guiTop + 61, 22, guiLeft + 33 - mouseX, guiTop + 21 - mouseY, barakoaya);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format(inventory.getDisplayName().getUnformattedText());
        fontRenderer.drawString(title, xSize / 2 - fontRenderer.getStringWidth(title) / 2, 6, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        super.drawScreen(mouseX, mouseY, delta);
        if (barakoaya.isOfferingTrade()) {
            Trade trade = barakoaya.getOfferingTrade();
            ItemStack input = trade.getInput();
            ItemStack output = trade.getOutput();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            itemRender.zLevel = 100;
            itemRender.renderItemAndEffectIntoGUI(input, guiLeft + 80, guiTop + 24);
            itemRender.renderItemOverlays(fontRenderer, input, guiLeft + 80, guiTop + 24);
            itemRender.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
            itemRender.renderItemOverlays(fontRenderer, output, guiLeft + 134, guiTop + 24);
            itemRender.zLevel = 0;
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
}
