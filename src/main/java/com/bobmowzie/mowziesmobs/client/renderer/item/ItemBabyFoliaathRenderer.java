package com.bobmowzie.mowziesmobs.client.renderer.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.block.ModelBabyFoliaath;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemBabyFoliaathRenderer implements IItemRenderer
{
    public ResourceLocation texture = new ResourceLocation(MowziesMobs.getModID() + "textures/blocks/babyFoliaath.png");
    public ModelBabyFoliaath model = new ModelBabyFoliaath();

    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return helper != ItemRendererHelper.BLOCK_3D;
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
                renderBlock(0f, 1.5f, 0f);
                break;
            case EQUIPPED:
                renderBlock(0.5f, 1.5f, 0.5f);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderBlock(0.5f, 1.5f, 0.5f);
                break;
            case INVENTORY:
                GL11.glRotatef(180f, 0f, 1f, 0f);
                renderBlock(0f, 1f, 0f);
                break;
        }
    }

    public void renderBlock(float x, float y, float z)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(-1f, -1f, 1f);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
        model.render();
        GL11.glPopMatrix();
    }
}
