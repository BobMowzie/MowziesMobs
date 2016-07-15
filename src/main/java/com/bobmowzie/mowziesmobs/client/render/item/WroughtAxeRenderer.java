package com.bobmowzie.mowziesmobs.client.render.item;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.item.WroughtAxeModel;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;

@SideOnly(Side.CLIENT)
public class WroughtAxeRenderer implements IItemRenderer {
    public static final ModelBase MODEL = new WroughtAxeModel();
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtAxe.png");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.BLOCK_3D;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY:
                renderBlock(0f, 1.5f, 0f, type, data);
                break;
            case EQUIPPED:
                renderBlock(0.5f, 1.5f, 0.5f, type, data);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderBlock(0.5f, 1.5f, 0.5f, type, data);
                break;
            case INVENTORY:
                GL11.glRotatef(180f, 0f, 1f, 0f);
                renderBlock(0f, 1f, 0f, type, data);
                break;
            default:
                renderBlock(0.5f, 1.5f, 0.5f, type, data);
                break;
        }
    }

    public void renderBlock(float x, float y, float z, ItemRenderType type, Object... data) {
        GL11.glPushMatrix();
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(WroughtAxeRenderer.TEXTURE);

        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            int tick = 0;
            float time = 0;
            if (data[1] instanceof EntityPlayer) {
                MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties((EntityPlayer) data[1], MowziePlayerProperties.class);
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
        } else if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
            float time = 0;
            if (data[1] instanceof EntityPlayer) {
                time = EntityPropertiesHandler.INSTANCE.getProperties((EntityPlayer) data[1], MowziePlayerProperties.class).getSwingPercentage(LLibrary.PROXY.getPartialTicks());
            }
            float controller1 = MowziePlayerProperties.fnc2(time);
            float controller2 = MowziePlayerProperties.fnc3(time, 0.166f, 0.833f, 30);
            GL11.glRotatef(90f * controller2, -1f, 0f, 1f);
            GL11.glRotatef(90f * controller2, -1f, 0f, -1f);
            GL11.glTranslatef(0.5f * controller2, -0.3f * controller2, -0.8f * controller2);
            GL11.glScalef(1 + 0.3f * controller1, 1 + 0.3f * controller1, 1 + 0.3f * controller1);
            GL11.glTranslatef(-0.1f, -1f, 0.1f);
        } else if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glTranslatef(1.3f, -1.6f, 0f);
            GL11.glScalef(0.47f, 0.47f, 0.47f);
            GL11.glRotatef(-30f, -1f, 0, 1f);
            GL11.glRotatef(45f, 1f, 0, 1f);

        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(0f, -0.5f, 0f);
            GL11.glScalef(0.6f, 0.6f, 0.6f);
        }

        GL11.glTranslatef(x, y, z);
        GL11.glScalef(-1f, -1f, 1f);
        WroughtAxeRenderer.MODEL.render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f);

        GL11.glPopMatrix();
    }
}
