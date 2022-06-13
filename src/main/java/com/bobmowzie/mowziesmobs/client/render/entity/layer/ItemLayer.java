package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ItemLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private AdvancedModelRenderer modelRenderer;
    private ItemStack itemstack;
    private ItemTransforms.TransformType transformType;

    public ItemLayer(RenderLayerParent<T, M> renderer, AdvancedModelRenderer modelRenderer, ItemStack itemstack, ItemTransforms.TransformType transformType) {
        super(renderer);
        this.itemstack = itemstack;
        this.modelRenderer = modelRenderer;
        this.transformType = transformType;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!modelRenderer.showModel || modelRenderer.isHidden()) return;
        matrixStackIn.pushPose();
        MowzieRenderUtils.matrixStackFromModel(matrixStackIn, getModelRenderer());
        Minecraft.getInstance().getItemInHandRenderer().renderItem(entitylivingbaseIn, getItemstack(), transformType, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    public ItemStack getItemstack() {
        return itemstack;
    }

    public void setItemstack(ItemStack itemstack) {
        this.itemstack = itemstack;
    }

    public AdvancedModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    public void setModelRenderer(AdvancedModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }
}
