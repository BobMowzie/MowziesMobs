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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public final class GuiUmvuthiTrade extends AbstractContainerScreen<ContainerUmvuthiTrade> implements InventoryUmvuthi.ChangeListener {
    private static final ResourceLocation TEXTURE_TRADE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/umvuthi_trade.png");
    private static final ResourceLocation TEXTURE_REPLENISH = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/umvuthi_replenish.png");

    private final EntityUmvuthi umvuthi;
    private final Player player;

    private final InventoryUmvuthi inventory;

    private final ItemStack output = new ItemStack(ItemHandler.GRANT_SUNS_BLESSING.get());

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
        grantButton = addRenderableWidget(Button.builder(Component.translatable(text), this::actionPerformed).width(204).pos(leftPos + 115, topPos + 52).size(56, 20).build());
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
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        //minecraft.getTextureManager().bindForSetup(hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        guiGraphics.blit(hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        umvuthi.renderingInGUI = true;
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, leftPos + 33, topPos + 57, 10, leftPos + 33 - x, topPos + 21 - y, umvuthi);
        umvuthi.renderingInGUI = false;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        String title = I18n.get("entity.mowziesmobs.umvuthi.trade");
        guiGraphics.drawString(font, title, (int) (imageWidth / 2f - font.width(title) / 2f) + 30, 6, 0x404040, false);
        guiGraphics.drawString(font, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        ItemStack inSlot = inventory.getItem(0);
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(0, 0, 100);
        if (hasTraded) {
            guiGraphics.renderItem(output, leftPos + 106, topPos + 24);
            guiGraphics.renderItemDecorations(font, output, leftPos + 106, topPos + 24);
            if (isHovering(106, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, output, mouseX, mouseY);
            }
        }
        else {
            guiGraphics.renderItem(umvuthi.getDesires(), leftPos + 68, topPos + 24);
            guiGraphics.renderItemDecorations(font, umvuthi.getDesires(), leftPos + 68, topPos + 24);
            guiGraphics.renderItem(output, leftPos + 134, topPos + 24);
            guiGraphics.renderItemDecorations(font, output, leftPos + 134, topPos + 24);
            if (isHovering(68, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, umvuthi.getDesires(), mouseX, mouseY);
            } else if (isHovering(134, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, output, mouseX, mouseY);
            }
        }

        if (grantButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderComponentHoverEffect(font, getHoverText(), mouseX, mouseY);
        }
        guiGraphics.pose().popPose();
    }

    @Override
	public void onChange(Container inv) {
        grantButton.active = hasTraded || umvuthi.doesItemSatisfyDesire(inv.getItem(0));
	}

    private void updateButton() {
        if (hasTraded) {
            grantButton.setMessage(Component.translatable(I18n.get("entity.mowziesmobs.umvuthi.replenish.button.text")));
            grantButton.setWidth(108);
            grantButton.setPosition(leftPos + 63, grantButton.getY());
        }
        else {
            grantButton.setMessage(Component.translatable(I18n.get("entity.mowziesmobs.umvuthi.trade.button.text")));
        }
    }

    private Style getHoverText() {
        MutableComponent text = Component.translatable(I18n.get(hasTraded ? "entity.mowziesmobs.umvuthi.replenish.button.hover" : "entity.mowziesmobs.umvuthi.trade.button.hover"));
        return text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}
