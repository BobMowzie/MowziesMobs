package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.WroughtnautEyesLayer;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderWroughtnaut extends MobRenderer<EntityWroughtnaut, ModelWroughtnaut<EntityWroughtnaut>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    public RenderWroughtnaut(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelWroughtnaut<>(), 1.0F);
        addLayer(new WroughtnautEyesLayer<>(this));
        addLayer(new ItemLayer<>(this, getModel().sword, Items.DIAMOND_SWORD.getDefaultInstance(), ItemTransforms.TransformType.GROUND));
    }
    
    @Override
    public void render(EntityWroughtnaut p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
        if (entityRenderDispatcher.shouldRenderHitBoxes() && !p_115455_.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
            Vec3 forward = p_115455_.getForward();
            Vec3 bodyFacing = Vec3.directionFromRotation(0, p_115455_.yBodyRot);
            Matrix4f matrix4f = p_115458_.last().pose();
            Matrix3f matrix3f = p_115458_.last().normal();
            VertexConsumer consumer = p_115459_.getBuffer(RenderType.lines());
            consumer.vertex(matrix4f, 0.0F, p_115455_.getEyeHeight() + 0.1f, 0.0F).color(0, 255, 255, 255).normal(matrix3f, (float) forward.x, (float) forward.y, (float) forward.z).endVertex();
            consumer.vertex(matrix4f, (float) (forward.x * 2.0D), (float) ((double) p_115455_.getEyeHeight() + 0.1f + forward.y * 2.0D), (float) (forward.z * 2.0D)).color(0, 255, 255, 255).normal(matrix3f, (float) forward.x, (float) forward.y, (float) forward.z).endVertex();

            consumer.vertex(matrix4f, 0.0F, p_115455_.getEyeHeight() + 0.2f, 0.0F).color(255, 0, 255, 255).normal(matrix3f, (float) bodyFacing.x, (float) bodyFacing.y, (float) bodyFacing.z).endVertex();
            consumer.vertex(matrix4f, (float) (bodyFacing.x * 2.0D), (float) ((double) p_115455_.getEyeHeight() + 0.2f + bodyFacing.y * 2.0D), (float) (bodyFacing.z * 2.0D)).color(255, 0, 255, 255).normal(matrix3f, (float) bodyFacing.x, (float) bodyFacing.y, (float) bodyFacing.z).endVertex();
        }
    }

    @Override
    protected float getFlipDegrees(EntityWroughtnaut entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWroughtnaut entity) {
        return RenderWroughtnaut.TEXTURE;
    }
}
