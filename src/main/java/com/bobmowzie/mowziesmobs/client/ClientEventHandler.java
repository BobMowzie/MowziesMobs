package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.item.ItemBarakoMask;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtAxe;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onFrameRender(RenderItemInFrameEvent event) {
        if (event.getItem().getItem() instanceof ItemWroughtAxe) {
            GlStateManager.translate(-0.4f, -0.2f, 0f);
            GlStateManager.scale(0.65f, 0.65f, 0.65f);
            GlStateManager.rotate(45f, 0f, -1f, 0f);
            GlStateManager.rotate(45f, -1f, 0f, -1f);
        } else if (event.getItem().getItem() instanceof ItemWroughtHelm) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.translate(0f, -0.15f, 0.05f);
        } else if (event.getItem().getItem() instanceof ItemBarakoaMask) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.translate(0f, -0.2f, 0.1f);
        } else if (event.getItem().getItem() instanceof ItemBarakoMask) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.translate(0f, -0.2f, 0.1f);
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(PlayerModelEvent.SetRotationAngles event) {
        if (event.getEntityPlayer() == null) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemWroughtAxe) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityPlayer(), MowziePlayerProperties.class);
            float time = property.getSwingPercentage(LLibrary.PROXY.getPartialTicks());
            if (time > 0) {
                float controller1 = MowziePlayerProperties.fnc1(time);
                float controller2 = MowziePlayerProperties.fnc2(time);
                float controller3 = MowziePlayerProperties.fnc3(time, 0.1f, 0.9f, 30);
                ModelRenderer rightArm = event.getModel().bipedRightArm;
                float normalAmount = time < 0.1F ? 1 - time / 0.1F : time > 0.9F ? (time - 0.9F) / 0.1F : 0;
                float swingAmount = 1 - normalAmount;
                rightArm.rotateAngleY = rightArm.rotateAngleY * normalAmount + (0.6F * controller1 + 0.3F * controller2) * swingAmount;
                rightArm.rotateAngleX = rightArm.rotateAngleX * normalAmount + ((float) -Math.PI / 2 * controller3) * swingAmount;
            }
        }
    }
}
