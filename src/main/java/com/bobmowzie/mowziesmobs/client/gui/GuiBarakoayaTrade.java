package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiBarakoayaTrade extends AbstractContainerScreen<ContainerBarakoayaTrade> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barakoa.png");

    private final EntityUmvuthanaMinion barakoaya;

    public GuiBarakoayaTrade(ContainerBarakoayaTrade screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.barakoaya = screenContainer.getBarakoaVillager();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
//        if (barakoaya.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
//            if (isHovering(13, 23, 8, 14, mouseX, mouseY)) {
//                barakoaya.setAnimation(EntityBarakoaVillager.ATTACK_ANIMATION);
//                barakoaya.setAnimationTick(3);
//            }
//        } TODO
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,TEXTURE);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        InventoryScreen.renderEntityInInventory(leftPos + 33, topPos + 61, 22, leftPos + 33 - x, topPos + 21 - y, barakoaya);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        String title = this.title.getContents();
        font.draw(matrixStack, title, imageWidth / 2f - font.width(title) / 2f, 6, 0x404040);
        font.draw(matrixStack, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 0x404040);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if (barakoaya.isOfferingTrade()) {
            Trade trade = barakoaya.getOfferingTrade();
            ItemStack input = trade.getInput();
            ItemStack output = trade.getOutput();
            matrixStack.pushPose();

            itemRenderer.blitOffset = 100;
            itemRenderer.renderAndDecorateItem(input, leftPos + 80, topPos + 24);
            itemRenderer.renderGuiItemDecorations(font, input, leftPos + 80, topPos + 24);
            itemRenderer.renderAndDecorateItem(output, leftPos + 134, topPos + 24);
            itemRenderer.renderGuiItemDecorations(font, output, leftPos + 134, topPos + 24);
            itemRenderer.blitOffset = 0;

            if (isHovering(80, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, input, mouseX, mouseY);
            } else if (isHovering(134, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, output, mouseX, mouseY);
            }
            matrixStack.popPose();
        }
    }
}
