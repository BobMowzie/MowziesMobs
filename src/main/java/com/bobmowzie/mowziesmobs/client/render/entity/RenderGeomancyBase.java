package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.compat.PatchouliCompat;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.Collections;

public class RenderGeomancyBase<T extends EntityGeomancyBase & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T> {
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/blocks/stone.png");
    private MultiBufferSource rtb;
    private T entity;

    protected final MowzieAnimatedGeoModel<T> modelProvider;

    protected RenderGeomancyBase(EntityRendererProvider.Context context, MowzieAnimatedGeoModel<T> modelProvider) {
        super(context);
        this.modelProvider = modelProvider;
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGeomancyBase entity) {
        return TEXTURE_STONE;
    }

    @Override
    public void renderEarly(T animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        IGeoRenderer.super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();

        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entity));
        modelProvider.setLivingAnimations(entity, this.getUniqueID(entity));

        RenderType renderType = getRenderType(entity, partialTicks, stack, bufferIn, null, packedLightIn, getTextureLocation(entity));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(renderType);
        render(model, entity, partialTicks, renderType, stack, bufferIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void setCurrentRTB(MultiBufferSource rtb) {
        this.rtb = rtb;
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    public T getEntity() {
        return entity;
    }
}
