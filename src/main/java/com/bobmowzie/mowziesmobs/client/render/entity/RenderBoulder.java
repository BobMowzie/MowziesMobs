package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBoulder;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.BlockLayer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.TreeMap;

@OnlyIn(Dist.CLIENT)
public class RenderBoulder extends EntityRenderer<EntityBoulder> {
    private static final ResourceLocation TEXTURE_DIRT = new ResourceLocation("textures/blocks/dirt.png");
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/blocks/stone.png");
    private static final ResourceLocation TEXTURE_SANDSTONE = new ResourceLocation("textures/blocks/sandstone.png");
    private static final ResourceLocation TEXTURE_CLAY = new ResourceLocation("textures/blocks/clay.png");
    Map<String, ResourceLocation> texMap;

    ModelBoulder model;

    public RenderBoulder(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelBoulder();
        texMap = new TreeMap<String, ResourceLocation>();
        texMap.put(Blocks.STONE.getTranslationKey(), TEXTURE_STONE);
        texMap.put(Blocks.DIRT.getTranslationKey(), TEXTURE_DIRT);
        texMap.put(Blocks.CLAY.getTranslationKey(), TEXTURE_CLAY);
        texMap.put(Blocks.SANDSTONE.getTranslationKey(), TEXTURE_SANDSTONE);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBoulder entity) {
//        if (entity.storedBlock != null) {
//            return Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(entity.storedBlock).;
//        }
//        else return TEXTURE_DIRT;
        if (entity.storedBlock != null) {
            ResourceLocation tex = texMap.get(entity.storedBlock.getBlock().getTranslationKey());
            if (tex != null) return tex;
        }
        return TEXTURE_DIRT;
    }

    @Override
    public void render(EntityBoulder entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        model.setRotationAngles(entityIn, 0, 0, entityIn.ticksExisted + partialTicks, 0, 0);
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        AdvancedModelRenderer root;
        if (entityIn.boulderSize == EntityBoulder.BoulderSizeEnum.SMALL) root = model.boulder0block1;
        else  if (entityIn.boulderSize == EntityBoulder.BoulderSizeEnum.MEDIUM) root = model.boulder1;
        else  if (entityIn.boulderSize == EntityBoulder.BoulderSizeEnum.LARGE) root = model.boulder2;
        else root = model.boulder3;
        matrixStackIn.translate(-0.5f, 0.5f, -0.5f);
        BlockLayer.processModelRenderer(root, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1, blockrendererdispatcher);
        matrixStackIn.pop();
    }
}
