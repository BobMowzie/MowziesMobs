package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarakoaya;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiBarakoayaTrade extends ContainerScreen<ContainerBarakoayaTrade> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barakoa.png");

    private final EntityBarakoaVillager barakoaya;

    private final InventoryBarakoaya inventory;

    private int cursorHit;

    public GuiBarakoayaTrade(ContainerBarakoayaTrade screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.barakoaya = screenContainer.getBarakoaya();
        this.inventory = screenContainer.getInventoryBarakoaya();
    }

    @Override
    public void tick() {
        super.tick();
        if (barakoaya.getAnimation() == EntityBarakoaVillager.ATTACK_ANIMATION && barakoaya.getAnimationTick() == 8) {
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
                barakoaya.setAnimation(EntityBarakoaVillager.ATTACK_ANIMATION);
                barakoaya.setAnimationTick(3);
            }
        }
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color3f(1, 1, 1);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        InventoryScreen.drawEntityOnScreen(guiLeft + 33, guiTop + 61, 22, guiLeft + 33 - x, guiTop + 21 - y, barakoaya);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        String title = this.title.getUnformattedComponentText();
        font.drawString(matrixStack, title, xSize / 2f - font.getStringWidth(title) / 2f, 6, 0x404040);
        font.drawString(matrixStack, I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        if (barakoaya.isOfferingTrade()) {
            Trade trade = barakoaya.getOfferingTrade();
            ItemStack input = trade.getInput();
            ItemStack output = trade.getOutput();
            matrixStack.push();
            RenderHelper.enableStandardItemLighting();
            RenderSystem.disableLighting();
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableColorMaterial();
            RenderSystem.enableLighting();
            itemRenderer.zLevel = 100;
            itemRenderer.renderItemAndEffectIntoGUI(input, guiLeft + 80, guiTop + 24);
            itemRenderer.renderItemOverlays(font, input, guiLeft + 80, guiTop + 24);
            itemRenderer.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
            itemRenderer.renderItemOverlays(font, output, guiLeft + 134, guiTop + 24);
            itemRenderer.zLevel = 0;
            RenderSystem.disableLighting();
            if (isPointInRegion(80, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, input, mouseX, mouseY);
            } else if (isPointInRegion(134, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, output, mouseX, mouseY);
            }
            RenderSystem.enableLighting();
            RenderSystem.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            matrixStack.pop();
        }
    }
}
