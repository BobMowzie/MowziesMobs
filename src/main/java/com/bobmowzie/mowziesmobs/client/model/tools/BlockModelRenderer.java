package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.server.block.MowzieBlockAccess;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Josh on 5/1/2017.
 */
public class BlockModelRenderer extends AdvancedModelRenderer {
    private BlockState blockState;
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

    public void setBlockState(BlockState blockState) {
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
                GlStateManager.translatef(this.offsetX, this.offsetY, this.offsetZ);
                GlStateManager.translatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                }
                if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
                    GlStateManager.scalef(this.scaleX, this.scaleY, this.scaleZ);
                }
                GlStateManager.callList(this.displayList);
                GlStateManager.disableLighting();
                BufferBuilder buf = Tessellator.getInstance().getBuffer();
                buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                BlockRendererDispatcher blockRendererDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                BlockPos blockpos = new BlockPos(0, 0, 0);
                if (entity != null) {
                    blockpos = new BlockPos(entity.posX, entity.posY, entity.posZ);
                    GL11.glTranslatef(-(blockpos.getX() + 0.5f), -(blockpos.getY() - 0.5f), -(blockpos.getZ() + 0.5f));
                }
                blockRendererDispatcher.getBlockModelRenderer().renderModel(entity.world, blockRendererDispatcher.getModelForState(blockState), blockState, blockpos, buf, false, new Random(), blockState.getPositionRandom(new BlockPos(0, 0, 0)));
                Tessellator.getInstance().draw();
                GlStateManager.enableLighting();
                if (!this.scaleChildren && (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F)) {
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef(this.offsetX, this.offsetY, this.offsetZ);
                    GlStateManager.translatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    if (this.rotateAngleZ != 0.0F) {
                        GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                    }
                    if (this.rotateAngleY != 0.0F) {
                        GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                    }
                    if (this.rotateAngleX != 0.0F) {
                        GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
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
        GlStateManager.newList(this.displayList, 4864);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        for (ModelBox box : this.cubeList) {
            box.render(buffer, scale);
        }
        GlStateManager.endList();
        this.compiled = true;
    }
}
