package com.ilexiconn.llibrary.client;

import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.client.gui.SnackbarGUI;
import com.ilexiconn.llibrary.client.gui.survivaltab.PageButtonGUI;
import com.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTab;
import com.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTabGUI;
import com.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTabHandler;
import com.ilexiconn.llibrary.client.gui.update.ModUpdateGUI;
import com.ilexiconn.llibrary.client.model.VoxelModel;
import com.ilexiconn.llibrary.client.util.ClientUtils;
import com.ilexiconn.llibrary.server.config.ConfigHandler;
import com.ilexiconn.llibrary.server.event.SurvivalTabClickEvent;
import com.ilexiconn.llibrary.server.snackbar.Snackbar;
import com.ilexiconn.llibrary.server.snackbar.SnackbarHandler;
import com.ilexiconn.llibrary.server.update.UpdateHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

import java.awt.*;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private SnackbarGUI snackbarGUI;
    private boolean checkedForUpdates;
    private EntityModel voxelModel = new VoxelModel();

    public void setOpenSnackbar(SnackbarGUI snackbarGUI) {
        this.snackbarGUI = snackbarGUI;
    }

    @SubscribeEvent
    public void onInitGuiPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof MainMenuScreen) {
            int offsetX = 0;
            int offsetY = 0;
            int buttonX = event.getGui().width / 2 - 124 + offsetX;
            int buttonY = event.getGui().height / 4 + 48 + 24 * 2 + offsetY;
            while (true) {
                if (buttonX < 0) {
                    if (offsetY <= -48) {
                        buttonX = 0;
                        buttonY = 0;
                        break;
                    } else {
                        offsetX = 0;
                        offsetY -= 24;
                        buttonX = event.getGui().width / 2 - 124 + offsetX;
                        buttonY = event.getGui().height / 4 + 48 + 24 * 2 + offsetY;
                    }
                }

                Rectangle rectangle = new Rectangle(buttonX, buttonY, 20, 20);
                boolean intersects = false;
                for (int i = 0; i < event.getWidgetList().size(); i++) {
                    Widget button = event.getWidgetList().get(i);
                    if (!intersects) {
                        intersects = rectangle.intersects(new Rectangle(button.x, button.y, button.getWidth(), button.getHeight()));
                    }
                }

                if (!intersects) {
                    break;
                }

                buttonX -= 24;
            }

            if (!this.checkedForUpdates && !UpdateHandler.INSTANCE.getOutdatedModList().isEmpty()) {
                event.getWidgetList().add(new Button(buttonX, buttonY, 20, 20, "U", ClientProxy.UPDATE_BUTTON_ID));
                this.checkedForUpdates = true;
                SnackbarHandler.INSTANCE.showSnackbar(Snackbar.create(I18n.format("snackbar.com.ilexiconn.llibrary.updates_found")));
            }
        } else if (event.getGui() instanceof Container && (LLibrary.CONFIG.areTabsAlwaysVisible() || SurvivalTabHandler.INSTANCE.getSurvivalTabList().size() > 1)) {
            Container container = (Container) event.getGui();
            boolean flag = false;
            for (SurvivalTab survivalTab : SurvivalTabHandler.INSTANCE.getSurvivalTabList()) {
                if (survivalTab.getContainer() != null && survivalTab.getContainer().isInstance(event.getGui())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                int count = 2;
                for (SurvivalTab tab : SurvivalTabHandler.INSTANCE.getSurvivalTabList()) {
                    if (tab.getPage() == SurvivalTabHandler.INSTANCE.getCurrentPage()) {
                        event.getWidgetList().add(new SurvivalTabGUI(count, tab));
                    }
                    count++;
                }
                if (count > 7) {
                    int offsetY = (container.ySize - 136) / 2 - 10;
                    if (LLibrary.CONFIG.areTabsLeftSide()) {
                        event.addWidget(new PageButtonGUI(-1, container..guiLeft - 82, container.guiTop + 136 + offsetY, container));
                        event.addWidget(new PageButtonGUI(-2, container.guiLeft - 22, container.guiTop + 136 + offsetY, container));
                    } else {
                        event.addWidget(new PageButtonGUI(-1, container.guiLeft + container.xSize + 2, container.guiTop + 136 + offsetY, container));
                        event.addWidget(new PageButtonGUI(-2, container.guiLeft + container.xSize + 62, container.guiTop + 136 + offsetY, container));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onButtonPressPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.getGui() instanceof MainMenuScreen && event.getButton().id == ClientProxy.UPDATE_BUTTON_ID) {
            ClientProxy.MINECRAFT.displayGuiScreen(new ModUpdateGUI((MainMenuScreen) event.getGui()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event) {
        if (this.snackbarGUI == null && !ClientProxy.SNACKBAR_LIST.isEmpty()) {
            this.setOpenSnackbar(ClientProxy.SNACKBAR_LIST.get(0));
            ClientProxy.SNACKBAR_LIST.remove(this.snackbarGUI);
        }
        if (this.snackbarGUI != null) {
            this.snackbarGUI.updateSnackbar();
        }
    }

    @SubscribeEvent
    public void onRenderUpdate(TickEvent.RenderTickEvent event) {
        ClientUtils.updateLast();
    }

    @SubscribeEvent
    public void onRenderOverlayPost(RenderGameOverlayEvent.Post event) {
        if (ClientProxy.MINECRAFT.currentScreen == null && this.snackbarGUI != null && event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            this.snackbarGUI.drawSnackbar();
        }
    }

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (this.snackbarGUI != null) {
            this.snackbarGUI.drawSnackbar();
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        PlayerEntity player = event.getEntityPlayer();
        if (LLibrary.CONFIG.hasPatreonEffects() && ClientProxy.PATRONS != null && (ClientProxy.MINECRAFT.gameSettings.thirdPersonView != 0 || player != ClientProxy.MINECRAFT.player)) {
            UUID id = player.getGameProfile().getId();
            if (id != null && ClientProxy.PATRONS.contains(id.toString())) {
                GlStateManager.pushMatrix();
                GlStateManager.translated(event.getX(), event.getY(), event.getZ());
                GlStateManager.depthMask(false);
                GlStateManager.disableLighting();
                GlStateManager.translatef(0.0F, 1.37F, 0.0F);
                this.renderVoxel(event, 1.1F, 0.23F);
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
                GlStateManager.translatef(0.0F, 0.128F, 0.0F);
                this.renderVoxel(event, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderVoxel(RenderPlayerEvent.Post event, float scale, float color) {
        PlayerEntity player = event.getEntityPlayer();
        int ticksExisted = player.ticksExisted;
        float partialTicks = LLibrary.PROXY.getPartialTicks();
        float bob = MathHelper.sin(((float) ticksExisted + partialTicks) / 15.0F) * 0.1F;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture();
        GlStateManager.rotatef(-ClientUtils.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks), 0, 1.0F, 0);
        GlStateManager.color4f(color, color, color, 1.0F);
        GlStateManager.translatef(0.0F, -0.6F + bob, 0.0F);
        GlStateManager.rotatef((ticksExisted + partialTicks) % 360, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(0.75F, 0.0F, 0.0F);
        GlStateManager.rotatef((ticksExisted + partialTicks) % 360, 0.0F, 1.0F, 0.0F);
        GlStateManager.scalef(scale, scale, scale);
        this.voxelModel.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.enableTexture();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        ConfigHandler.INSTANCE.loadConfigForID(event.getModID());
        ConfigHandler.INSTANCE.saveConfigForID(event.getModID());
    }

    @SubscribeEvent
    public void onSurvivalTabClick(SurvivalTabClickEvent event) {
        if (event.getLabel().equals("container.inventory")) {
            ClientProxy.MINECRAFT.displayGuiScreen(new InventoryScreen(ClientProxy.MINECRAFT.player));
        }
    }
}
