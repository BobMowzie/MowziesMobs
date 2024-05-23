package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class GeckoArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {
    public GeckoArmorLayer(RenderLayerParent<T, M> layerParent, A innerModel, A outerModel, ModelManager modelManager) {
        super(layerParent, innerModel, outerModel, modelManager);
    }

    @Override
    protected void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T entity, EquipmentSlot equipmentSlot, int p_117123_, A baseModel) {
        ItemStack itemstack = entity.getItemBySlot(equipmentSlot);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
            if (armoritem.getType().getSlot() == equipmentSlot) {
                net.minecraft.client.model.Model model = getArmorModelHook(entity, itemstack, equipmentSlot, baseModel);
                if (model instanceof HumanoidModel<?>) {
                    HumanoidModel<T> humanoidModel = (HumanoidModel<T>) model;
                    this.getParentModel().copyPropertiesTo(baseModel);
                    this.setPartVisibility(baseModel, equipmentSlot);
                    this.setPartVisibility((A) humanoidModel, equipmentSlot);
                    boolean flag = this.usesInnerModel(equipmentSlot);
                    boolean flag1 = itemstack.hasFoil();
                    ModelBipedAnimated.setUseMatrixMode(humanoidModel, true);
                    if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
                        int i = ((net.minecraft.world.item.DyeableLeatherItem) armoritem).getColor(itemstack);
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        this.renderModel(poseStack, bufferSource, p_117123_, flag1, model, f, f1, f2, this.getArmorResource(entity, itemstack, equipmentSlot, null));
                        this.renderModel(poseStack, bufferSource, p_117123_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, equipmentSlot, "overlay"));
                    } else {
                        this.renderModel(poseStack, bufferSource, p_117123_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, equipmentSlot, null));
                    }
                }
            }
        }
    }

    private void renderModel(PoseStack p_117107_, MultiBufferSource p_117108_, int p_117109_, boolean p_117111_, net.minecraft.client.model.Model p_117112_, float p_117114_, float p_117115_, float p_117116_, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(p_117108_, RenderType.armorCutoutNoCull(armorResource), false, p_117111_);
        if (p_117112_ instanceof MowzieGeoArmorRenderer<?>) ((MowzieGeoArmorRenderer<?>) p_117112_).usingCustomPlayerAnimations = true;
        p_117112_.renderToBuffer(p_117107_, vertexconsumer, p_117109_, OverlayTexture.NO_OVERLAY, p_117114_, p_117115_, p_117116_, 1.0F);
    }
}
