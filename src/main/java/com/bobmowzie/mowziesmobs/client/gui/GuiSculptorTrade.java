package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventorySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageSculptorTrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
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

public final class GuiSculptorTrade extends AbstractContainerScreen<ContainerSculptorTrade> implements InventorySculptor.ChangeListener {
    private static final ResourceLocation TEXTURE_TRADE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/umvuthi_trade.png");

    private final EntitySculptor sculptor;
    private final Player player;

    private final InventorySculptor inventory;

    private final ItemStack output = new ItemStack(ItemHandler.EARTHREND_GAUNTLET.get());

    private Button beginButton;

    private boolean prevBlocked;

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
        beginButton = addRenderableWidget(Button.builder(Component.translatable(text), this::actionPerformed).pos(leftPos + 115, topPos + 52).size(56, 20).build());
        updateButton();
    }

    protected void actionPerformed(Button button) {
    	if (button == beginButton) {
            MowziesMobs.NETWORK.sendToServer(new MessageSculptorTrade(sculptor));
    	}
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE_TRADE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, leftPos + 33, topPos + 56, 14, leftPos + 33 - x, topPos + 21 - y, sculptor);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.drawString(font, title, (int) (imageWidth / 2f - font.width(title) / 2f) + 30, 6, 0x404040, false);
        guiGraphics.drawString(font, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 0x404040, false);
        if (sculptor.isTestObstructed()) {
            String blocked = I18n.get("entity.mowziesmobs.sculptor.trade.blocked");
            guiGraphics.drawString(font, blocked, (int) (imageWidth / 2f - font.width(blocked) / 2f) + 30, 42, 0x404040, false);
        }
        if (prevBlocked != sculptor.isTestObstructed()) {
            onChange(inventory);
        }
        prevBlocked = sculptor.isTestObstructed();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        ItemStack inSlot = inventory.getItem(0);
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        guiGraphics.renderItem(sculptor.getDesires(), leftPos + 68, topPos + 24);
        guiGraphics.renderItemDecorations(font, sculptor.getDesires(), leftPos + 68, topPos + 24);
        guiGraphics.renderItem(output, leftPos + 134, topPos + 24);
        guiGraphics.renderItemDecorations(font, output, leftPos + 134, topPos + 24);
        if (isHovering(68, 24, 16, 16, mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, sculptor.getDesires(), mouseX, mouseY);
        } else if (isHovering(134, 24, 16, 16, mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, output, mouseX, mouseY);
        }

        if (beginButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderComponentHoverEffect(font, getHoverText(), mouseX, mouseY);
        }

        guiGraphics.pose().popPose();
    }

    @Override
	public void onChange(Container inv) {
        beginButton.active = sculptor.doesItemSatisfyDesire(inv.getItem(0)) && !sculptor.isTestObstructed();
	}

    private void updateButton() {
        beginButton.setMessage(Component.translatable(I18n.get("entity.mowziesmobs.sculptor.trade.button.text")));
    }

    private Style getHoverText() {
        MutableComponent text = Component.translatable(I18n.get("entity.mowziesmobs.sculptor.trade.button.hover"));
        return text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}
