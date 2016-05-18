package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtAxe;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.event.Render3dItemEvent;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onItemRender(Render3dItemEvent.Pre event) {
        if (event.item == ItemHandler.INSTANCE.wrought_axe) {
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                int tick = 0;
                float time = 0;
                if (event.data[1] instanceof EntityPlayer) {
                    MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties((EntityPlayer) event.data[1], MowziePlayerProperties.class);
                    tick = property.getTick();
                    time = property.getSwingPercentage(LLibrary.PROXY.getPartialTicks());
                }
                if (tick > 2) {
                    float controller1 = MowziePlayerProperties.fnc2(time);
                    float controller2 = MowziePlayerProperties.fnc3(time, 0.166f, 0.833f, 30);
                    float controller3 = MowziePlayerProperties.fnc1(time);
                    GL11.glRotatef(90f * controller2, -1f, 0f, 1f);
                    GL11.glRotatef(90f * controller2, -1f, 0f, -1f);
                    GL11.glRotatef(60f * (controller3 + 1.2f * controller1), -1f, 0f, -1f);
                    GL11.glTranslatef(0.5f * controller2, -0.3f * controller2, -0.6f * controller2);
                    GL11.glScalef(1 + 0.8f * controller1, 1 + 0.8f * controller1, 1 + 0.8f * controller1);
                }

                GL11.glTranslatef(0f, -1.5f, 0f);
            }
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED) {
                float time = 0;
                if (event.data[1] instanceof EntityPlayer) {
                    time = EntityPropertiesHandler.INSTANCE.getProperties((EntityPlayer) event.data[1], MowziePlayerProperties.class).getSwingPercentage(LLibrary.PROXY.getPartialTicks());
                }
                float controller1 = MowziePlayerProperties.fnc2(time);
                float controller2 = MowziePlayerProperties.fnc3(time, 0.166f, 0.833f, 30);
                GL11.glRotatef(90f * controller2, -1f, 0f, 1f);
                GL11.glRotatef(90f * controller2, -1f, 0f, -1f);
                GL11.glTranslatef(0.5f * controller2, -0.3f * controller2, -0.8f * controller2);
                GL11.glScalef(1 + 0.3f * controller1, 1 + 0.3f * controller1, 1 + 0.3f * controller1);
                GL11.glTranslatef(-0.1f, -1f, 0.1f);
            }
            if (event.type == IItemRenderer.ItemRenderType.INVENTORY) {
                GL11.glTranslatef(1.3f, -1.6f, 0f);
                GL11.glScalef(0.47f, 0.47f, 0.47f);
                GL11.glRotatef(-30f, -1f, 0, 1f);
                GL11.glRotatef(45f, 1f, 0, 1f);

            }
            if (event.type == IItemRenderer.ItemRenderType.ENTITY) {
                GL11.glTranslatef(0f, -0.5f, 0f);
                GL11.glScalef(0.6f, 0.6f, 0.6f);
            }
        }

        if (event.item == ItemHandler.INSTANCE.wrought_helmet) {
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glRotatef(90f, 0f, 1f, 0f);
                GL11.glTranslatef(-1f, -0.8f, 0f);
            }
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED) {
                GL11.glRotatef(-115f, 0f, 1f, 0f);
                GL11.glScalef(2f, 2f, 2f);
                GL11.glTranslatef(0f, -1.3f, -0.8f);
            }
            if (event.type == IItemRenderer.ItemRenderType.INVENTORY) {
                GL11.glTranslatef(-0.4f, -1.2f, 0f);
                // GL11.glScalef(0.97f, 0.97f, 0.97f);
            }
            if (event.type == IItemRenderer.ItemRenderType.ENTITY) {
                GL11.glTranslatef(0f, -1.3f, 0f);
            }
        }

        if (event.item instanceof ItemBarakoaMask) {
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glRotatef(90f, 0f, 1f, 0f);
                GL11.glTranslatef(-1f, -0.8f, 0f);
            }
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED) {
                GL11.glRotatef(-115f, 0f, 1f, 0f);
                GL11.glScalef(2f, 2f, 2f);
                GL11.glTranslatef(0f, -1.3f, -0.5f);
            }
            if (event.type == IItemRenderer.ItemRenderType.INVENTORY) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor3f(1, 1, 1);
                GL11.glRotatef(45, 0, 1, 0);
                GL11.glRotatef(30, 1, 0, 0);
                GL11.glTranslatef(0, -1.7f, 0f);
                GL11.glScalef(1.25f, 1.25f, 1.25f);
            }
            if (event.type == IItemRenderer.ItemRenderType.ENTITY) {
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glTranslatef(0f, -1.3f, 0f);
            }
        }
    }

    @SubscribeEvent
    public void onFrameRender(RenderItemInFrameEvent event) {
        if (event.item.getItem() instanceof ItemWroughtAxe) {
            GL11.glTranslatef(-0.4f, -0.2f, 0f);
            GL11.glScalef(0.65f, 0.65f, 0.65f);
            GL11.glRotatef(45f, 0f, -1f, 0f);
            GL11.glRotatef(45f, -1f, 0f, -1f);
        }

        if (event.item.getItem() instanceof ItemWroughtHelm) {
            GL11.glRotatef(180f, 0f, 1f, 0f);
            GL11.glTranslatef(0f, -0.15f, 0.05f);
        }

        if (event.item.getItem() instanceof ItemBarakoaMask) {
            GL11.glRotatef(180f, 0f, 1f, 0f);
            GL11.glTranslatef(0f, -0.2f, 0.1f);
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(PlayerModelEvent.SetRotationAngles event)
    {
        if (event.getEntityPlayer() == null)
        {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWroughtAxe)
        {
            MowziePlayerProperties property = new MowziePlayerProperties();
            float time = property.getSwingPercentage(LLibrary.PROXY.getPartialTicks());
            System.out.println(property.getTick());
            if (time > 0)
            {
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
