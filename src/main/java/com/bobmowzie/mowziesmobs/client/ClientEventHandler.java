package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoFirstPersonRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.item.*;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

@OnlyIn(Dist.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private static final ResourceLocation FROZEN_BLUR = new ResourceLocation(MowziesMobs.MODID, "textures/gui/frozenblur.png");

    @SubscribeEvent
    public void onHandRender(RenderHandEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        boolean shouldAnimate = false;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) shouldAnimate = abilityCapability.getActiveAbility() != null;
//        shouldAnimate = (player.ticksExisted / 20) % 2 == 0;
        if (shouldAnimate) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                GeckoPlayer.GeckoPlayerFirstPerson geckoPlayer = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
                if (geckoPlayer != null) {
                    ModelGeckoPlayerFirstPerson geckoFirstPersonModel = (ModelGeckoPlayerFirstPerson) geckoPlayer.getModel();
                    GeckoFirstPersonRenderer firstPersonRenderer = (GeckoFirstPersonRenderer) geckoPlayer.getPlayerRenderer();

                    if (geckoFirstPersonModel != null && firstPersonRenderer != null) {
                        if (!geckoFirstPersonModel.isUsingSmallArms() && ((AbstractClientPlayerEntity) player).getSkinType().equals("slim")) {
                            firstPersonRenderer.setSmallArms();
                        }
                        event.setCanceled(geckoFirstPersonModel.resourceForModelId((AbstractClientPlayerEntity) player));

                        if (event.isCanceled()) {
                            float delta = event.getPartialTicks();
                            float f1 = MathHelper.lerp(delta, player.prevRotationPitch, player.rotationPitch);
                            firstPersonRenderer.renderItemInFirstPerson((AbstractClientPlayerEntity) player, f1, delta, event.getHand(), event.getSwingProgress(), event.getItemStack(), event.getEquipProgress(), event.getMatrixStack(), event.getBuffers(), event.getLight(), geckoPlayer);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        float delta = event.getPartialRenderTick();
        boolean shouldAnimate = player.isPotionActive(EffectHandler.FROZEN);
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) shouldAnimate = shouldAnimate || abilityCapability.getActiveAbility() != null;
//        shouldAnimate = (player.ticksExisted / 20) % 2 == 0;
        if (shouldAnimate) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                GeckoPlayer.GeckoPlayerThirdPerson geckoPlayer = playerCapability.getGeckoPlayer();
                if (geckoPlayer != null) {
                    ModelGeckoPlayerThirdPerson geckoPlayerModel = (ModelGeckoPlayerThirdPerson) geckoPlayer.getModel();
                    GeckoRenderPlayer animatedPlayerRenderer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();

                    if (geckoPlayerModel != null && animatedPlayerRenderer != null) {
                        if (!geckoPlayerModel.isUsingSmallArms() && ((AbstractClientPlayerEntity) player).getSkinType().equals("slim")) {
                            animatedPlayerRenderer.setSmallArms();
                        }

                        event.setCanceled(geckoPlayerModel.resourceForModelId((AbstractClientPlayerEntity) player));

                        if (event.isCanceled()) {
                            animatedPlayerRenderer.render((AbstractClientPlayerEntity) event.getEntity(), event.getEntity().rotationYaw, delta, event.getMatrixStack(), event.getBuffers(), event.getLight(), geckoPlayer);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
//        PlayerEntity player = Minecraft.getInstance().player;
//        if (player != null) {
//            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
//            if (playerCapability != null && playerCapability.getGeomancy().canUse(player) && playerCapability.getGeomancy().isSpawningBoulder() && playerCapability.getGeomancy().getSpawnBoulderCharge() > 2) {
//                Vector3d lookPos = playerCapability.getGeomancy().getLookPos();
//                Vector3d playerEyes = player.getEyePosition(Minecraft.getInstance().getRenderPartialTicks());
//                Vector3d vec = playerEyes.subtract(lookPos).normalize();
//                float yaw = (float) Math.atan2(vec.z, vec.x);
//                float pitch = (float) Math.asin(vec.y);
//                player.rotationYaw = (float) (yaw * 180f/Math.PI + 90);
//                player.rotationPitch = (float) (pitch * 180f/Math.PI);
//                player.rotationYawHead = player.rotationYaw;
//                player.prevRotationYaw = player.rotationYaw;
//                player.prevRotationPitch = player.rotationPitch;
//                player.prevRotationYawHead = player.rotationYawHead;
//            }
//            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(player, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
//            if (frozenCapability != null && player.isPotionActive(EffectHandler.FROZEN) && frozenCapability.getPrevFrozen()) {
//                player.rotationYaw = frozenCapability.getFrozenYaw();
//                player.rotationPitch = frozenCapability.getFrozenPitch();
//                player.rotationYawHead = frozenCapability.getFrozenYawHead();
//                player.prevRotationYaw = player.rotationYaw;
//                player.prevRotationPitch = player.rotationPitch;
//                player.prevRotationYawHead = player.rotationYawHead;
//            }
//        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
        if (frozenCapability != null && entity.isPotionActive(EffectHandler.FROZEN) && frozenCapability.getPrevFrozen()) {
            entity.rotationYaw = entity.prevRotationYaw = frozenCapability.getFrozenYaw();
            entity.rotationPitch = entity.prevRotationPitch = frozenCapability.getFrozenPitch();
            entity.rotationYawHead = entity.prevRotationYawHead = frozenCapability.getFrozenYawHead();
            entity.renderYawOffset = entity.prevRenderYawOffset = frozenCapability.getFrozenRenderYawOffset();
            entity.swingProgress = entity.prevSwingProgress = frozenCapability.getFrozenSwingProgress();
            entity.limbSwingAmount = entity.prevLimbSwingAmount = frozenCapability.getFrozenLimbSwingAmount();
            entity.setSneaking(false);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post e) {
        final int startTime = 210;
        final int pointStart = 1200;
        final int timePerMillis = 22;
        if (e.getType() == ElementType.POTION_ICONS) {
            /*long now = System.currentTimeMillis();
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
                Minecraft.getInstance().getTextureManager().bindTexture(MARIO);
                MainWindow res = e.getWindow();
                int offsetY = 16;
                int col = res.getScaledWidth() / 4;
                // MARIO
                int marioOffsetX = col / 2 - 18;
                AbstractGui.blit(marioOffsetX, offsetY, 0, 16, 39, 7, 64, 64);
                // points
                drawMarioNumber(marioOffsetX, offsetY + 8, points, 6);
                // Coin
                int coinOffsetX = col + col / 2 - 15;
                int coinU = 40 + ((int) (Math.max(0, MathHelper.sin(t * 0.005F)) * 2 + 0.5F)) * 6;
                AbstractGui.blit(coinOffsetX, offsetY + 8, coinU, 8, 5, 8, 64, 64);
                // x02
                AbstractGui.blit(coinOffsetX + 9, offsetY + 8, 16, 8, 23, 7, 64, 64);
                // WORLD 1-1
                AbstractGui.blit(col * 2 + col / 2 - 19, offsetY, 0, 24, 39, 15, 64, 64);
                // TIME
                int timeOffsetX = col * 3 + col / 2 - 15;
                AbstractGui.blit(timeOffsetX, offsetY, 0, 40, 30, 7, 64, 64);
                // Time
                drawMarioNumber(timeOffsetX + 8, offsetY + 8, time, 3);
            }*/
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isPotionActive(EffectHandler.FROZEN) && Minecraft.getInstance().gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
                Minecraft.getInstance().getTextureManager().bindTexture(FROZEN_BLUR);
                MainWindow res = e.getWindow();
                AbstractGui.blit(e.getMatrixStack(), 0, 0, 0, 0, res.getScaledWidth(), res.getScaledHeight(), res.getScaledWidth(), res.getScaledHeight());
            }
        }
    }

    // Remove frozen overlay
    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent.Pre event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.isPassenger()) {
            if (player.getRidingEntity() instanceof EntityFrozenController) {
                if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTHMOUNT)) {
                    event.setCanceled(true);
                }
                if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
                    Minecraft.getInstance().ingameGUI.setOverlayMessage(new TranslationTextComponent(""), false);
                }
            }
        }
    }

    @SubscribeEvent
    public void updateFOV(FOVUpdateEvent event) {
        PlayerEntity player = event.getEntity();
        if (player.isHandActive() && player.getActiveItemStack().getItem() instanceof ItemBlowgun) {
            int i = player.getItemInUseMaxCount();
            float f1 = (float)i / 5.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            event.setNewfov(1.0F - f1 * 0.15F);
        }
    }

    @SubscribeEvent
    public void onSetupCamera(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getRenderPartialTicks();
        float ticksExistedDelta = player.ticksExisted + delta;
        if (player != null) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null && playerCapability.getGeomancy().canUse(player) && playerCapability.getGeomancy().isSpawningBoulder() && playerCapability.getGeomancy().getSpawnBoulderCharge() > 2) {
                Vector3d lookPos = playerCapability.getGeomancy().getLookPos();
                Vector3d playerEyes = player.getEyePosition(delta);
                Vector3d vec = playerEyes.subtract(lookPos).normalize();
                float yaw = (float) Math.atan2(vec.z, vec.x);
                float pitch = (float) Math.asin(vec.y);
                player.rotationYaw = (float) (yaw * 180f/Math.PI + 90);
                player.rotationPitch = (float) (pitch * 180f/Math.PI);
                player.rotationYawHead = player.rotationYaw;
                player.prevRotationYaw = player.rotationYaw;
                player.prevRotationPitch = player.rotationPitch;
                player.prevRotationYawHead = player.rotationYawHead;
                event.setPitch(pitch);
                event.setYaw(yaw);
            }
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(player, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null && player.isPotionActive(EffectHandler.FROZEN) && frozenCapability.getPrevFrozen()) {
                player.rotationYaw = frozenCapability.getFrozenYaw();
                player.rotationPitch = frozenCapability.getFrozenPitch();
                player.rotationYawHead = frozenCapability.getFrozenYawHead();
                player.prevRotationYaw = player.rotationYaw;
                player.prevRotationPitch = player.rotationPitch;
                player.prevRotationYawHead = player.rotationYawHead;

                event.setPitch(frozenCapability.getFrozenPitch());
                event.setYaw(frozenCapability.getFrozenYaw());
            }

            if (ConfigHandler.CLIENT.doCameraShakes.get()) {
                float shakeAmplitude = 0;
                for (EntityCameraShake cameraShake : player.world.getEntitiesWithinAABB(EntityCameraShake.class, player.getBoundingBox().grow(20, 20, 20))) {
                    if (cameraShake.getDistance(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

}
