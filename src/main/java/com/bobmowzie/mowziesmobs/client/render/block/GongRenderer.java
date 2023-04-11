package com.bobmowzie.mowziesmobs.client.render.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.server.block.entity.GongBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GongRenderer implements BlockEntityRenderer<GongBlockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/block/gong.png");
    private final ModelPart gongBase;
    private final ModelPart chain;

    public GongRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(LayerHandler.GONG_LAYER);
        this.gongBase = modelpart.getChild("root");
        this.chain = gongBase.getChild("chain");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(69, 68).addBox(-35.75F, -23.25F, 5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(35, 65).addBox(0.25F, -57.25F, 7.0F, 4.0F, 34.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(69, 85).addBox(-1.75F, -59.25F, 5.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 69).addBox(-1.75F, -23.25F, 5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(52, 68).addBox(-33.75F, -57.25F, 7.0F, 4.0F, 34.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 86).addBox(-35.75F, -59.25F, 5.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-37.75F, -56.25F, 7.5F, 46.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-38.75F, -63.25F, 3.0F, 48.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(51, 23).addBox(-27.75F, -59.25F, 8.5F, 26.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(14.75F, 39.25F, -8.5F));

        PartDefinition chain = root.addOrReplaceChild("chain", CubeListBuilder.create().texOffs(51, 27).addBox(-11.0F, 0.0F, 0.0F, 22.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.75F, -54.25F, 8.5F));

        PartDefinition gong = chain.addOrReplaceChild("gong", CubeListBuilder.create().texOffs(1, 24).addBox(-11.75F, -11.75F, -1.0F, 22.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(51, 36).addBox(-3.75F, -3.75F, -1.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(49, 47).addBox(-9.75F, -9.75F, -1.0F, 18.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-9.75F, -9.75F, -0.5F, 18.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, 18.75F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void render(GongBlockEntity entity, float delta, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.485, 0.5);
        poseStack.mulPose(new Quaternion(0, 0, 180, true));
        if (entity.facing.getAxis() == Direction.Axis.X) {
            poseStack.mulPose(new Quaternion(0, 90, 0, true));
        }

        float f = (float)entity.ticks + delta;
        float f1 = 0.0F;
        if (entity.shaking) {
            float f3 = Mth.sin(f / (float)Math.PI) / (4.0F + f / 2.0F);
            if (entity.clickDirection == Direction.NORTH) {
                f1 = f3;
            } else if (entity.clickDirection == Direction.SOUTH) {
                f1 = -f3;
            } else if (entity.clickDirection == Direction.EAST) {
                f1 = f3;
            } else if (entity.clickDirection == Direction.WEST) {
                f1 = -f3;
            }
        }

        this.chain.xRot = f1;

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.gongBase.render(poseStack, vertexconsumer, packedLight, overlay);
        poseStack.popPose();
    }
}
