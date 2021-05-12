package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryBarako;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageBarakoTrade;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketDirection;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public final class GuiBarakoTrade extends ContainerScreen<ContainerBarakoTrade> implements InventoryBarako.ChangeListener {
    private static final ResourceLocation TEXTURE_TRADE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barako_trade.png");
    private static final ResourceLocation TEXTURE_REPLENISH = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/barako_replenish.png");

    private final EntityBarako barako;

    private final InventoryBarako inventory;

    private final ItemStack output = new ItemStack(ItemHandler.GRANT_SUNS_BLESSING);

    private Button grantButton;

    private boolean hasTraded;

    public GuiBarakoTrade(ContainerBarakoTrade screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.barako = screenContainer.getBarako();
        this.inventory = screenContainer.getInventoryBarako();
        this.hasTraded = barako.hasTradedWith(inv.player);
        inventory.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        String text = I18n.format(hasTraded ? "entity.mowziesmobs.barako.replenish.button.text" : "entity.mowziesmobs.barako.trade.button.text");
        grantButton = addButton(new Button(guiLeft + 115, guiTop + 52, 56, 20, new TranslationTextComponent(text), this::actionPerformed));
        grantButton.active = hasTraded;
        updateButton();
    }

    protected void actionPerformed(Button button) {
    	if (button == grantButton) {
            hasTraded = true;
            updateButton();
            MowziesMobs.NETWORK.sendToServer(new MessageBarakoTrade(barako));
            if (!Minecraft.getInstance().isIntegratedServerRunning()) {
                PlayerEntity player = playerInventory.player;
                boolean satisfied = barako.hasTradedWith(player);
                if (!satisfied) {
                    if (barako.fulfillDesire(container.getSlot(0))) {
                        barako.rememberTrade(player);
                        container.detectAndSendChanges();
                    }
                }
            }
    	}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color3f(1, 1, 1);
        minecraft.getTextureManager().bindTexture(hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        InventoryScreen.drawEntityOnScreen(guiLeft + 33, guiTop + 56, 14, 0, 0, barako);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        String title = I18n.format("entity.mowziesmobs.barako.trade");
        font.drawString(matrixStack, title, (xSize / 2f - font.getStringWidth(title) / 2f) + 30, 6, 0x404040);
        font.drawString(matrixStack, I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        ItemStack inSlot = inventory.getStackInSlot(0);
        matrixStack.push();
        RenderHelper.enableStandardItemLighting();
        RenderSystem.disableLighting();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableColorMaterial();
        RenderSystem.enableLighting();
        itemRenderer.zLevel = 100;
        if (hasTraded) {
            itemRenderer.renderItemAndEffectIntoGUI(output, guiLeft + 106, guiTop + 24);
            itemRenderer.renderItemOverlays(font, output, guiLeft + 106, guiTop + 24);
            if (isPointInRegion(106, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, output, mouseX, mouseY);
            }
        }
        else {
            itemRenderer.renderItemAndEffectIntoGUI(barako.getDesires(), guiLeft + 68, guiTop + 24);
            itemRenderer.renderItemOverlays(font, barako.getDesires(), guiLeft + 68, guiTop + 24);
            itemRenderer.renderItemAndEffectIntoGUI(output, guiLeft + 134, guiTop + 24);
            itemRenderer.renderItemOverlays(font, output, guiLeft + 134, guiTop + 24);
            if (isPointInRegion(68, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, barako.getDesires(), mouseX, mouseY);
            } else if (isPointInRegion(134, 24, 16, 16, mouseX, mouseY)) {
                renderTooltip(matrixStack, output, mouseX, mouseY);
            }
        }
        itemRenderer.zLevel = 0;
        RenderSystem.disableLighting();

        if (grantButton.isMouseOver(mouseX, mouseY)) {
            renderComponentHoverEffect(matrixStack, getHoverText(), mouseX, mouseY);
        }
        RenderSystem.enableLighting();
        RenderSystem.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
        matrixStack.pop();
    }

    @Override
	public void onChange(IInventory inv) {
        grantButton.active = hasTraded || barako.doesItemSatisfyDesire(inv.getStackInSlot(0));
	}

    private void updateButton() {
        if (hasTraded) {
            grantButton.setMessage(new TranslationTextComponent(I18n.format("entity.mowziesmobs.barako.replenish.button.text")));
            grantButton.setWidth(108);
            grantButton.x = guiLeft + 63;
        }
        else {
            grantButton.setMessage(new TranslationTextComponent(I18n.format("entity.mowziesmobs.barako.trade.button.text")));
        }
    }

    private Style getHoverText() {
        TranslationTextComponent text = new TranslationTextComponent(I18n.format(hasTraded ? "entity.mowziesmobs.barako.replenish.button.hover" : "entity.mowziesmobs.barako.trade.button.hover"));
        return text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}
