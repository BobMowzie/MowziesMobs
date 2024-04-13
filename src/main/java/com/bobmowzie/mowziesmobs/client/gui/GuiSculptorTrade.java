package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventorySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageSculptorTrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
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

public final class GuiSculptorTrade extends AbstractContainerScreen<ContainerSculptorTrade> implements InventorySculptor.ChangeListener {
    private static final ResourceLocation TEXTURE_TRADE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barako_trade.png");

    private final EntitySculptor sculptor;
    private final Player player;

    private final InventorySculptor inventory;

    private final ItemStack output = new ItemStack(ItemHandler.EARTHBORE_GAUNTLET);

    private Button beginButton;

    public GuiSculptorTrade(ContainerSculptorTrade screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.sculptor = screenContainer.getSculptor();
        this.player = inv.player;
        this.inventory = screenContainer.getInventorySculptor();
        inventory.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        String text = I18n.get("entity.mowziesmobs.sculptor.trade.button.text");
        beginButton = addRenderableWidget(new Button(leftPos + 115, topPos + 52, 56, 20, Component.translatable(text), this::actionPerformed));
        updateButton();
    }

    protected void actionPerformed(Button button) {
    	if (button == beginButton) {
            MowziesMobs.NETWORK.sendToServer(new MessageSculptorTrade(sculptor));
    	}
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_TRADE);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        InventoryScreen.renderEntityInInventory(leftPos + 33, topPos + 56, 14, 0, 0, sculptor);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        String title = I18n.get("entity.mowziesmobs.sculptor.trade");
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

        itemRenderer.renderAndDecorateItem(sculptor.getDesires(), leftPos + 68, topPos + 24);
        itemRenderer.renderGuiItemDecorations(font, sculptor.getDesires(), leftPos + 68, topPos + 24);
        itemRenderer.renderAndDecorateItem(output, leftPos + 134, topPos + 24);
        itemRenderer.renderGuiItemDecorations(font, output, leftPos + 134, topPos + 24);
        if (isHovering(68, 24, 16, 16, mouseX, mouseY)) {
            renderTooltip(matrixStack, sculptor.getDesires(), mouseX, mouseY);
        } else if (isHovering(134, 24, 16, 16, mouseX, mouseY)) {
            renderTooltip(matrixStack, output, mouseX, mouseY);
        }
        itemRenderer.blitOffset = 0;

        if (beginButton.isMouseOver(mouseX, mouseY)) {
            renderComponentHoverEffect(matrixStack, getHoverText(), mouseX, mouseY);
        }
        matrixStack.popPose();
    }

    @Override
	public void onChange(Container inv) {
        beginButton.active = sculptor.doesItemSatisfyDesire(inv.getItem(0));
	}

    private void updateButton() {
        beginButton.setMessage(Component.translatable(I18n.get("entity.mowziesmobs.sculptor.trade.button.text")));
    }

    private Style getHoverText() {
        MutableComponent text = Component.translatable(I18n.get("entity.mowziesmobs.sculptor.trade.button.hover"));
        return text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}
