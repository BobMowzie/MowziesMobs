package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GeckoArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A> {
    public GeckoArmorLayer(IEntityRenderer<T, M> renderer, A innerModel, A outerModel) {
        super(renderer, innerModel, outerModel);
    }

    @Override
    public void func_241739_a_(MatrixStack stack, IRenderTypeBuffer buffer, T entity, EquipmentSlotType slot, int p_241739_5_, A model) {
        ItemStack itemstack = entity.getItemStackFromSlot(slot);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
            if (armoritem.getEquipmentSlot() == slot) {
                model = getArmorModelHook(entity, itemstack, slot, model);
                this.getEntityModel().setModelAttributes(model);
                this.setModelSlotVisible(model, slot);
                boolean flag = this.isLegSlot(slot);
                boolean flag1 = itemstack.hasEffect();
                if (armoritem instanceof net.minecraft.item.IDyeableArmorItem) {
                    int i = ((net.minecraft.item.IDyeableArmorItem) armoritem).getColor(itemstack);
                    float f = (float) (i >> 16 & 255) / 255.0F;
                    float f1 = (float) (i >> 8 & 255) / 255.0F;
                    float f2 = (float) (i & 255) / 255.0F;
                    this.renderModel(stack, buffer, p_241739_5_, flag1, model, f, f1, f2, this.getArmorResource(entity, itemstack, slot, null));
                    ModelBipedAnimated.setUseMatrixMode(model, true);
                    this.renderModel(stack, buffer, p_241739_5_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, "overlay"));
                } else {
                    this.renderModel(stack, buffer, p_241739_5_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, null));
                }
            }
        }
    }

    private void renderModel(MatrixStack p_241738_1_, IRenderTypeBuffer p_241738_2_, int p_241738_3_, boolean p_241738_5_, A p_241738_6_, float p_241738_8_, float p_241738_9_, float p_241738_10_, ResourceLocation armorResource) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(p_241738_2_, RenderType.getArmorCutoutNoCull(armorResource), false, p_241738_5_);
        p_241738_6_.render(p_241738_1_, ivertexbuilder, p_241738_3_, OverlayTexture.NO_OVERLAY, p_241738_8_, p_241738_9_, p_241738_10_, 1.0F);
    }

    private boolean isLegSlot(EquipmentSlotType slotIn) {
        return slotIn == EquipmentSlotType.LEGS;
    }
}
