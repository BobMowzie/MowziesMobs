package com.bobmowzie.mowziesmobs.client.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.item.BarakoaMaskModel;

@SideOnly(Side.CLIENT)
public class BarakoaMaskRenderer implements IItemRenderer {
    public static final ModelBase MODEL = new BarakoaMaskModel();

    private ResourceLocation texture;

    public BarakoaMaskRenderer(int id) {
        this.texture = new ResourceLocation(MowziesMobs.MODID, String.format("textures/entity/textureTribesman%s.png", id + 1));
    }

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
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.texture);

        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(90f, 0f, 1f, 0f);
            GL11.glTranslatef(-1f, -0.8f, 0f);
        } else if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
            GL11.glRotatef(-115f, 0f, 1f, 0f);
            GL11.glScalef(2f, 2f, 2f);
            GL11.glTranslatef(0f, -1.3f, -0.5f);
        } else if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor3f(1, 1, 1);
            GL11.glRotatef(45, 0, 1, 0);
            GL11.glRotatef(30, 1, 0, 0);
            GL11.glTranslatef(0, -1.7f, 0f);
            GL11.glScalef(1.25f, 1.25f, 1.25f);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glTranslatef(0f, -1.3f, 0f);
        }

        GL11.glTranslatef(x, y, z);
        GL11.glScalef(-1f, -1f, 1f);
        BarakoaMaskRenderer.MODEL.render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f);

        GL11.glPopMatrix();
    }
}
