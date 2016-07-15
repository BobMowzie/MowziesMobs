package com.bobmowzie.mowziesmobs.client.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.item.WroughtHelmetModel;

@SideOnly(Side.CLIENT)
public class WroughtHelmetRenderer implements IItemRenderer {
    public static final ModelBase MODEL = new WroughtHelmetModel();
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtHelmet.png");

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
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(WroughtHelmetRenderer.TEXTURE);

        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(90f, 0f, 1f, 0f);
            GL11.glTranslatef(-1f, -0.8f, 0f);
        } else if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
            GL11.glRotatef(-115f, 0f, 1f, 0f);
            GL11.glScalef(2f, 2f, 2f);
            GL11.glTranslatef(0f, -1.3f, -0.8f);
        } else if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glTranslatef(-0.4f, -1.2f, 0f);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(0f, -1.3f, 0f);
        }

        GL11.glTranslatef(x, y, z);
        GL11.glScalef(-1f, -1f, 1f);
        WroughtHelmetRenderer.MODEL.render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f);

        GL11.glPopMatrix();
    }
}
