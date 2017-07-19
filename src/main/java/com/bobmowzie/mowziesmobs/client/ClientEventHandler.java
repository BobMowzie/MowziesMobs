package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoMask;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtAxe;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private static final ResourceLocation MARIO = new ResourceLocation(MowziesMobs.MODID, "textures/gui/mario.png");
    private static final ResourceLocation FROZEN_BLUR = new ResourceLocation(MowziesMobs.MODID, "textures/gui/frozenblur.png");

    long startWroughtnautHitTime;

    long lastWroughtnautHitTime;

    @SubscribeEvent
    public void onFrameRender(RenderItemInFrameEvent event) {
        if (event.getItem().getItem() instanceof ItemWroughtAxe) {
            GlStateManager.translate(0.325f, 0.4f, -0.05f);
            GlStateManager.scale(-0.65f, -0.65f, 0.65f);
            GlStateManager.rotate(45f, 0f, -1f, 0f);
            GlStateManager.rotate(45f, -1f, 0f, -1f);
        } else if (event.getItem().getItem() instanceof ItemWroughtHelm) {
            GlStateManager.translate(0.19f, -0.37f, -0.25f);
        } else if (event.getItem().getItem() instanceof ItemBarakoaMask) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.translate(0f, -0.2f, 0.1f);
        } else if (event.getItem().getItem() instanceof ItemBarakoMask) {
            GlStateManager.scale(0.85f, 0.85f, 0.85f);
            GlStateManager.translate(0.32f, -0.4f, -0.25f);
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(PlayerModelEvent.SetRotationAngles event) {
        if (event.getEntityPlayer() == null) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        player.getHeldItem(EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            MowziePlayerProperties propertyPlayer = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
            if (propertyPlayer != null && propertyPlayer.geomancy.canUse(player) && propertyPlayer.geomancy.isSpawningBoulder() && propertyPlayer.geomancy.getSpawnBoulderCharge() > 2) {
                Vec3d lookPos = propertyPlayer.geomancy.getLookPos();
                Vec3d playerEyes = player.getPositionEyes(LLibrary.PROXY.getPartialTicks());
                Vec3d vec = playerEyes.subtract(lookPos).normalize();
                float yaw = (float) Math.atan2(vec.zCoord, vec.xCoord);
                float pitch = (float) Math.asin(vec.yCoord);
                float dYaw = ((float) (yaw * 180/Math.PI + 90) - player.rotationYaw)/2f;
                float dPitch = ((float)(pitch * 180/Math.PI) - player.rotationPitch)/2f;
                player.rotationYaw += dYaw;
                player.rotationPitch += dPitch;
            }
            MowzieLivingProperties propertyLiving = EntityPropertiesHandler.INSTANCE.getProperties(player, MowzieLivingProperties.class);
            if (player.isPotionActive(PotionHandler.INSTANCE.frozen)) {
                player.rotationYaw = propertyLiving.frozenYaw;
                player.rotationPitch = propertyLiving.frozenPitch;
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent event) {
        EntityLivingBase entity = event.getEntity();
        MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
        if (entity.isPotionActive(PotionHandler.INSTANCE.frozen)) {
            entity.motionX = 0;
            entity.motionZ = 0;
//            entity.posX = entity.prevPosX;
//            entity.posZ = entity.prevPosZ;
            entity.rotationYaw = property.frozenYaw;
            entity.rotationPitch = property.frozenPitch;
            entity.rotationYawHead = property.frozenYawHead;
            entity.renderYawOffset = property.frozenRenderYawOffset;
            entity.swingProgress = property.frozenSwingProgress;
            entity.limbSwingAmount = property.frozenLimbSwingAmount;
            entity.setSneaking(false);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post e) {
        final int startTime = 210;
        final int pointStart = 1200;
        final int timePerMillis = 22;
        if (e.getType() == ElementType.POTION_ICONS) {
            long now = System.currentTimeMillis();
            if (now - lastWroughtnautHitTime < 500) {
                int t = (int) (now - startWroughtnautHitTime);
                int progress = t / timePerMillis;
                int time = startTime - progress;
                if (time < 0) {
                    startWroughtnautHitTime = now;
                    progress = 0;
                    time = startTime;
                }
                int points = pointStart + progress * 50;
                Minecraft.getMinecraft().getTextureManager().bindTexture(MARIO);
                ScaledResolution res = e.getResolution();
                int offsetY = 16;
                int col = res.getScaledWidth() / 4;
                // MARIO
                int marioOffsetX = col / 2 - 18;
                Gui.drawModalRectWithCustomSizedTexture(marioOffsetX, offsetY, 0, 16, 39, 7, 64, 64);
                // points
                drawMarioNumber(marioOffsetX, offsetY + 8, points, 6);
                // Coin
                int coinOffsetX = col + col / 2 - 15;
                int coinU = 40 + ((int) (Math.max(0, MathHelper.sin(t * 0.005F)) * 2 + 0.5F)) * 6;
                Gui.drawModalRectWithCustomSizedTexture(coinOffsetX, offsetY + 8, coinU, 8, 5, 8, 64, 64);
                // x02
                Gui.drawModalRectWithCustomSizedTexture(coinOffsetX + 9, offsetY + 8, 16, 8, 23, 7, 64, 64);
                // WORLD 1-1
                Gui.drawModalRectWithCustomSizedTexture(col * 2 + col / 2 - 19, offsetY, 0, 24, 39, 15, 64, 64);
                // TIME
                int timeOffsetX = col * 3 + col / 2 - 15;
                Gui.drawModalRectWithCustomSizedTexture(timeOffsetX, offsetY, 0, 40, 30, 7, 64, 64);
                // Time
                drawMarioNumber(timeOffsetX + 8, offsetY + 8, time, 3);
            }
            if (Minecraft.getMinecraft().player.isPotionActive(PotionHandler.INSTANCE.frozen)) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(FROZEN_BLUR);
                ScaledResolution res = e.getResolution();
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, res.getScaledWidth(), res.getScaledHeight(), res.getScaledWidth(), res.getScaledHeight());
            }
        }
    }

    @SubscribeEvent
    public void onMouseMove(MouseEvent event) {
        if (Minecraft.getMinecraft().player.isPotionActive(PotionHandler.INSTANCE.frozen)) {
            event.setCanceled(true);
        }
    }

    private static void drawMarioNumber(int x, int y, int value, int length) {
        for (int n = 0; n < length; n++, value /= 10) {
            int digit = value % 10;
            Gui.drawModalRectWithCustomSizedTexture(x + 8 * (length - n - 1), y, digit * 8 % 64, digit / 8 * 8, 8, 7, 64, 64);
        }
    }
}
