package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtAxe;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onFrameRender(RenderItemInFrameEvent event) {
        if (event.item.getItem() instanceof ItemWroughtAxe) {
            GL11.glTranslatef(-0.4f, -0.2f, 0f);
            GL11.glScalef(0.65f, 0.65f, 0.65f);
            GL11.glRotatef(45f, 0f, -1f, 0f);
            GL11.glRotatef(45f, -1f, 0f, -1f);
        } else if (event.item.getItem() instanceof ItemWroughtHelm) {
            GL11.glRotatef(180f, 0f, 1f, 0f);
            GL11.glTranslatef(0f, -0.15f, 0.05f);
        } else if (event.item.getItem() instanceof ItemBarakoaMask) {
            GL11.glRotatef(180f, 0f, 1f, 0f);
            GL11.glTranslatef(0f, -0.2f, 0.1f);
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(PlayerModelEvent.SetRotationAngles event) {
        if (event.getEntityPlayer() == null) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWroughtAxe) {
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
