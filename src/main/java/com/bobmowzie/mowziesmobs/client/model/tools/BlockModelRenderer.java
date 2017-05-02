package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.server.block.MowzieBlockAccess;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomePlains;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * Created by Josh on 5/1/2017.
 */
public class BlockModelRenderer extends AdvancedModelRenderer {
    private IBlockState blockState;
    private MowzieBlockAccess access;

    public BlockModelRenderer(AdvancedModelBase model) {
        super(model);
        access = new MowzieBlockAccess();
        setBlockState(Blocks.DIRT.getDefaultState());
    }

    public void setBlockState(IBlockState blockState) {
        this.blockState = blockState;
        access.setBlockState(blockState);
    }

    public void setBiome(Biome biome) {
        access.setBiome(biome);
    }

    public void render(float scale, Entity entity) {
//        super.render(scale);
        GlStateManager.disableLighting();
        VertexBuffer buf = Tessellator.getInstance().getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos blockpos = new BlockPos(entity.posX, entity.posY, entity.posZ);
        GL11.glTranslatef(-(blockpos.getX() + 0.5f), -(blockpos.getY() - 0.5f), -(blockpos.getZ() + 0.5f));
        GL11.glTranslatef(rotationPointX/16, rotationPointY/16, rotationPointZ/16);
        blockRendererDispatcher.getBlockModelRenderer().renderModel(entity.world, blockRendererDispatcher.getModelForState(blockState), blockState, blockpos, buf, false, 0);
        Tessellator.getInstance().draw();
        GlStateManager.enableLighting();
    }
}
