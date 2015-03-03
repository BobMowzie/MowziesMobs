package com.bobmowzie.mowziesmobs.client.renderer.tile;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.block.ModelBabyFoliaath;
import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBabyFoliaath extends TileEntitySpecialRenderer
{

    private static final ResourceLocation texture = new ResourceLocation(MowziesMobs.getModID() + "textures/blocks/TextureBabyFoliaath.png");
    private ModelBabyFoliaath model = new ModelBabyFoliaath();

    public RenderBabyFoliaath()
    {

    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
    {
        if (tileEntity instanceof TileBabyFoliaath)
        {
            TileBabyFoliaath tileEntityModel = (TileBabyFoliaath) tileEntity;
            GL11.glPushMatrix();
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
        }
    }
}