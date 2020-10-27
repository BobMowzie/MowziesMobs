package com.ilexiconn.llibrary.client.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    private static long lastUpdate = System.currentTimeMillis();

    /**
     * Update the current time.
     */
    public static void updateLast() {
        ClientUtils.lastUpdate = System.currentTimeMillis();
    }

    /**
     * Update a value with the default factor of 0.5.
     *
     * @param current the current value
     * @param target the target
     * @return the updated value
     */
    public static float updateValue(float current, float target) {
        return ClientUtils.updateValue(current, target, 0.5F);
    }

    /**
     * Update a value with a custom factor. How higher the factor, how slower the update. The value will get updated
     * slower if the user has a low framerate.
     *
     * @param current the current value
     * @param target the target
     * @param factor the factor
     * @return the updated value
     */
    public static float updateValue(float current, float target, float factor) {
        float times = (System.currentTimeMillis() - ClientUtils.lastUpdate) / 16.666666666666668F;
        float off = (off = target - current) > 0.01F || off < -0.01F ? off * (float) Math.pow(factor, times) : 0.0F;
        return target - off;
    }

    public static float interpolate(float prev, float current, float partialTicks) {
        return prev + partialTicks * (current - prev);
    }

    public static float interpolateRotation(float prev, float current, float partialTicks) {
        float shortest = ((current - prev) % 360 + 540) % 360 - 180;
        return prev + shortest * partialTicks;
    }

    /**
     * Sets a player specific texture. This can be used to change the skin or cape texture.
     *
     * @param player the player to set the texture to
     * @param type the type of texture to apply
     * @param texture the texture to apply
     */
    /*public static void setPlayerTexture(AbstractClientPlayerEntity player, MinecraftProfileTexture.Type type, ResourceLocation texture) {
        if (player.hasPlayerInfo() && texture != null) {
            player.getPlayerInfo().playerTextures.put(type, texture);
        }
    }

    public static void setPlayerSkinType(AbstractClientPlayer player, String type) {
        player.getPlayerInfo().skinType = type;
    }

    public static void addPlayerSkinTypeRenderer(String type, RenderPlayer renderPlayer) {
        ClientProxy.MINECRAFT.getRenderManager().getSkinMap().put(type, renderPlayer);
    }*/
}
