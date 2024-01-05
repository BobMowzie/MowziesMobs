package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryUmvuthi;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageUmvuthiTrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class GuiUmvuthiTrade extends AbstractContainerScreen<ContainerUmvuthiTrade> implements InventoryUmvuthi.ChangeListener {
    private static final ResourceLocation TEXTURE_TRADE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/umvuthi_trade.png");
    private static final ResourceLocation TEXTURE_REPLENISH = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/umvuthi_replenish.png");

    private final EntityUmvuthi umvuthi;
    private final Player player;

    private final InventoryUmvuthi inventory;

    private final ItemStack output = new ItemStack(ItemHandler.GRANT_SUNS_BLESSING);

    private Button grantButton;

    private boolean hasTraded;

    public GuiUmvuthiTrade(ContainerUmvuthiTrade screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.umvuthi = screenContainer.getUmvuthi();
        this.player = inv.player;
        this.inventory = screenContainer.getInventoryUmvuthi();
        this.hasTraded = umvuthi.hasTradedWith(inv.player);
        inventory.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        String text = I18n.get(hasTraded ? "entity.mowziesmobs.umvuthi.replenish.button.text" : "entity.mowziesmobs.umvuthi.trade.button.text");
        grantButton = addRenderableWidget(new Button(leftPos + 115, topPos + 52, 56, 20, new TranslatableComponent(text), this::actionPerformed));
        grantButton.active = hasTraded;
        updateButton();
    }

    protected void actionPerformed(Button button) {
    	if (button == grantButton) {
            hasTraded = true;
            updateButton();
            MowziesMobs.NETWORK.sendToServer(new MessageUmvuthiTrade(umvuthi));
            if (!Minecraft.getInstance().isLocalServer()) {
                boolean satisfied = umvuthi.hasTradedWith(player);
                if (!satisfied) {
                    if (umvuthi.fulfillDesire(menu.getSlot(0))) {
                        umvuthi.rememberTrade(player);
                        menu.broadcastChanges();
                    }
                }
            }
    	}
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        //minecraft.getTextureManager().bindForSetup(hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        umvuthi.renderingInGUI = true;
        InventoryScreen.renderEntityInInventory(leftPos + 33, topPos + 57, 10, leftPos + 33 - x, topPos + 21 - y, umvuthi);
        umvuthi.renderingInGUI = false;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        String title = I18n.get("entity.mowziesmobs.umvuthi.trade");
        font.draw(matrixStack, title, (imageWidth / 2f - font.width(title) / 2f) + 30, 6, 0x404040);
        font.draw(matrixStack, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 0x404040);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        ItemStack inSlot = inventory.getItem(0);
        matrixStack.pushPose();

        itemRenderer.blitOffset = 100;
        if (hasTraded) {
            itemRenderer.renderAndDecorateItem(output, leftPos + 106, topPos + 24);
            itemRenderer.renderGuiItemDecorations(font, output, leftPos + 106, topPos + 24);
            if (isHovering(106, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, output, mouseX, mouseY);
            }
        }
        else {
            itemRenderer.renderAndDecorateItem(umvuthi.getDesires(), leftPos + 68, topPos + 24);
            itemRenderer.renderGuiItemDecorations(font, umvuthi.getDesires(), leftPos + 68, topPos + 24);
            itemRenderer.renderAndDecorateItem(output, leftPos + 134, topPos + 24);
            itemRenderer.renderGuiItemDecorations(font, output, leftPos + 134, topPos + 24);
            if (isHovering(68, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, umvuthi.getDesires(), mouseX, mouseY);
            } else if (isHovering(134, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, output, mouseX, mouseY);
            }
        }
        itemRenderer.blitOffset = 0;

        if (grantButton.isMouseOver(mouseX, mouseY)) {
            renderComponentHoverEffect(matrixStack, getHoverText(), mouseX, mouseY);
        }
        matrixStack.popPose();
    }

    @Override
	public void onChange(Container inv) {
        grantButton.active = hasTraded || umvuthi.doesItemSatisfyDesire(inv.getItem(0));
	}

    private void updateButton() {
        if (hasTraded) {
            grantButton.setMessage(new TranslatableComponent(I18n.get("entity.mowziesmobs.barako.replenish.button.text")));
            grantButton.setWidth(108);
            grantButton.x = leftPos + 63;
        }
        else {
            grantButton.setMessage(new TranslatableComponent(I18n.get("entity.mowziesmobs.barako.trade.button.text")));
        }
    }

    private Style getHoverText() {
        TranslatableComponent text = new TranslatableComponent(I18n.get(hasTraded ? "entity.mowziesmobs.barako.replenish.button.hover" : "entity.mowziesmobs.barako.trade.button.hover"));
        return text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}
