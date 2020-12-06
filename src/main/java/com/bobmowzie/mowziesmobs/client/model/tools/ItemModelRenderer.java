package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.server.block.MowzieBlockAccess;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Josh on 5/1/2017.
 */
public class ItemModelRenderer extends AdvancedModelRenderer {
    private ItemStack itemStack;

    private ItemCameraTransforms.TransformType transformType;
    private LivingEntity entity;
    private BlockPos origin;
    private boolean compiled;
    private int displayList;

    public ItemModelRenderer(AdvancedModelBase model) {
        super(model);
        setItemStack(Items.APPLE.getDefaultInstance());
        setTransformType(ItemCameraTransforms.TransformType.NONE);
        setEntity(null);
    }

    public void setTransformType(ItemCameraTransforms.TransformType transformType) {
        this.transformType = transformType;
    }

    public void setItemStack(ItemStack stack) {
        this.itemStack = stack;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public void render(float scale) {
        if (!this.isHidden) {
            if (this.showModel) {
                //GlStateManager.pushMatrix();
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
                if (this.opacity != 1.0F) {
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    GlStateManager.color4f(1F, 1F, 1F, this.opacity);
                }
//                GlStateManager.scalef(1, -1,1);
                GlStateManager.rotatef(180,1, 0,0);
                GlStateManager.rotatef(90,0, 1,0);
                Minecraft.getInstance().gameRenderer.itemRenderer.renderItem(entity, itemStack, transformType);
                GlStateManager.rotatef(90,0, -1,0);
                GlStateManager.rotatef(180,-1, 0,0);
//                GlStateManager.scalef(1, -1,1);
                if (this.opacity != 1.0F) {
                    GlStateManager.disableBlend();
                    GlStateManager.color4f(1F, 1F, 1F, 1F);
                }
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
                    for (RendererModel childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
                //GlStateManager.popMatrix();
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
