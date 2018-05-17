package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.server.block.MowzieBlockAccess;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
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
    private Entity entity;
    private BlockPos origin;
    private boolean compiled;
    private int displayList;

    public BlockModelRenderer(AdvancedModelBase model) {
        super(model);
        access = new MowzieBlockAccess();
        setBlockState(Blocks.DIRT.getDefaultState());
        setOrigin(new BlockPos(0, 0, 0));
        setEntity(null);
    }

    public void setBlockState(IBlockState blockState) {
        this.blockState = blockState;
        access.setBlockState(blockState);
    }

    public void setBiome(Biome biome) {
        access.setBiome(biome);
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setOrigin(BlockPos origin) {
        this.origin = origin;
    }

    public void render(float scale) {
        super.render(scale);

        if (!this.isHidden) {
            if (this.showModel) {
                GlStateManager.pushMatrix();
                if (!this.compiled) {
                    this.compileDisplayList(scale);
                }
                GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                }
                if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
                    GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
                }
                GlStateManager.callList(this.displayList);
                GlStateManager.disableLighting();
                BufferBuilder buf = Tessellator.getInstance().getBuffer();
                buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                BlockPos blockpos = new BlockPos(0, 0, 0);
                if (entity != null) {
                    blockpos = new BlockPos(entity.posX, entity.posY, entity.posZ);
                    GL11.glTranslatef(-(blockpos.getX() + 0.5f), -(blockpos.getY() - 0.5f), -(blockpos.getZ() + 0.5f));
                }
                blockRendererDispatcher.getBlockModelRenderer().renderModel(entity.world, blockRendererDispatcher.getModelForState(blockState), blockState, blockpos, buf, false, MathHelper.getPositionRandom(origin));
                Tessellator.getInstance().draw();
                GlStateManager.enableLighting();
                if (!this.scaleChildren && (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F)) {
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    if (this.rotateAngleZ != 0.0F) {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                    }
                    if (this.rotateAngleY != 0.0F) {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                    }
                    if (this.rotateAngleX != 0.0F) {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                    }
                }
                if (this.childModels != null) {
                    for (ModelRenderer childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, 4864);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        for (ModelBox box : this.cubeList) {
            box.render(buffer, scale);
        }
        GlStateManager.glEndList();
        this.compiled = true;
    }
}
