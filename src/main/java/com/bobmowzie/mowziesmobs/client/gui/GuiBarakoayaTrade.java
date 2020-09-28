package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarakoaya;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public final class GuiBarakoayaTrade extends ContainerScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barakoa.png");

    private final EntityBarakoaya barakoaya;

    private final InventoryBarakoaya inventory;

    private int cursorHit;

    private GuiBarakoayaTrade(EntityBarakoaya barakoaya, InventoryBarakoaya inventory, ContainerBarakoayaTrade container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        this.barakoaya = barakoaya;
        this.inventory = inventory;
    }

    @Override
    public void tick() {
        super.tick();
        if (barakoaya.getAnimation() == EntityBarakoaya.ATTACK_ANIMATION && barakoaya.getAnimationTick() == 8) {
            cursorHit = 6;
        }
        if (cursorHit > 0) {
            cursorHit--;
//            Mouse.setCursorPosition(Mouse.getX() + cursorHit * 16 / 6, Mouse.getY() - cursorHit * 44 / 6); // TODO
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        if (barakoaya.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            if (isPointInRegion(13, 23, 8, 14, mouseX, mouseY)) {
                barakoaya.setAnimation(EntityBarakoaya.ATTACK_ANIMATION);
                barakoaya.setAnimationTick(3);
            }
        }
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float delta, int mouseX, int mouseY) {
        GlStateManager.color3f(1, 1, 1);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        InventoryScreen.drawEntityOnScreen(guiLeft + 33, guiTop + 61, 22, guiLeft + 33 - mouseX, guiTop + 21 - mouseY, barakoaya);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = this.title.getUnformattedComponentText();
        font.drawString(title, xSize / 2f - font.getStringWidth(title) / 2f, 6, 0x404040);
        font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
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
            itemRenderer.zLevel = 100;
            itemRenderer.renderItemAndEffectIntoGUI(input, guiLeft + 80, guiTop + 24);
            itemRenderer.renderItemOverlays(font, input, guiLeft + 80, guiTop + 24);
            itemRenderer.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
            itemRenderer.renderItemOverlays(font, output, guiLeft + 134, guiTop + 24);
            itemRenderer.zLevel = 0;
            GlStateManager.disableLighting();
            if (isPointInRegion(80, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(input, mouseX, mouseY);
            } else if (isPointInRegion(134, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(output, mouseX, mouseY);
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }
}
