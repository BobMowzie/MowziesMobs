package com.bobmowzie.mowziesmobs.client.renderer.tile;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.block.ModelBabyFoliaath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBabyFoliaath extends TileEntitySpecialRenderer
{
    public ResourceLocation texture = new ResourceLocation(MowziesMobs.getModID() + "textures/blocks/babyFoliaath.png");
    public ModelBabyFoliaath model = new ModelBabyFoliaath();

    public RenderBabyFoliaath()
    {

    }

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 1.5f, z + 0.5f);
        GL11.glRotated(180f, 0f, 0f, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.render();
        GL11.glPopMatrix();
    }
}