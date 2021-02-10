package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelFoliaathBaby<T extends EntityBabyFoliaath> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer infantBase;
    public AdvancedModelRenderer juvenileBase;
    public AdvancedModelRenderer infantLeaf1;
    public AdvancedModelRenderer infantLeaf2;
    public AdvancedModelRenderer infantLeaf3;
    public AdvancedModelRenderer infantLeaf4;
    public AdvancedModelRenderer juvenileLeaf1;
    public AdvancedModelRenderer juvenileLeaf2;
    public AdvancedModelRenderer juvenileLeaf3;
    public AdvancedModelRenderer juvenileLeaf4;
    public AdvancedModelRenderer mouthBase;
    public AdvancedModelRenderer mouth1;
    public AdvancedModelRenderer mouth2;
    public AdvancedModelRenderer mouthCover;
    public AdvancedModelRenderer teeth1;
    public AdvancedModelRenderer teeth2;

    public ModelFoliaathBaby() {
        textureWidth = 64;
        textureHeight = 16;
        juvenileLeaf3 = new AdvancedModelRenderer(this, 27, 0);
        juvenileLeaf3.setRotationPoint(-1.0F, 0.0F, 1.0F);
        juvenileLeaf3.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf3, -0.3490658503988659F, 2.356194490192345F, 0.0F);
        mouthBase = new AdvancedModelRenderer(this, 13, 0);
        mouthBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        mouthBase.addBox(-1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F);
        mouth1 = new AdvancedModelRenderer(this, 20, 0);
        mouth1.setRotationPoint(0.5F, -1.0F, 0.0F);
        mouth1.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        setRotateAngle(mouth1, 0.0F, 0.0F, 0F);
        infantLeaf3 = new AdvancedModelRenderer(this, -3, 0);
        infantLeaf3.setRotationPoint(0.2F, 0.0F, 0.2F);
        infantLeaf3.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf3, -0.5235987755982988F, -2.356194490192345F, 0.0F);
        infantBase = new AdvancedModelRenderer(this, 0, 0);
        infantBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        infantBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        infantLeaf2 = new AdvancedModelRenderer(this, -3, 0);
        infantLeaf2.setRotationPoint(0.0F, 0.0F, 0.0F);
        infantLeaf2.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf2, -0.5235987755982988F, 0.7853981633974483F, 0.0F);
        juvenileLeaf2 = new AdvancedModelRenderer(this, 27, 0);
        juvenileLeaf2.setRotationPoint(-1.0F, 0.0F, -1.0F);
        juvenileLeaf2.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf2, -0.3490658503988659F, 0.7853981633974483F, 0.0F);
        juvenileBase = new AdvancedModelRenderer(this, 0, 0);
        juvenileBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        juvenileBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        teeth1 = new AdvancedModelRenderer(this, 49, 2);
        teeth1.setRotationPoint(-1.0F, 0.0F, 0.0F);
        teeth1.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        juvenileLeaf4 = new AdvancedModelRenderer(this, 27, 0);
        juvenileLeaf4.setRotationPoint(1.0F, 0.0F, 1.0F);
        juvenileLeaf4.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf4, -0.3490658503988659F, 3.9269908169872414F, 0.0F);
        juvenileLeaf1 = new AdvancedModelRenderer(this, 27, 0);
        juvenileLeaf1.setRotationPoint(1.0F, 0.0F, -1.0F);
        juvenileLeaf1.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf1, -0.3490658503988659F, -0.7853981633974483F, 0.0F);
        teeth2 = new AdvancedModelRenderer(this, 37, 2);
        teeth2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        teeth2.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        mouth2 = new AdvancedModelRenderer(this, 20, 0);
        mouth2.setRotationPoint(-0.5F, -1.0F, 0.0F);
        mouth2.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        setRotateAngle(mouth2, 0.0F, 3.141592653589793F, 0F);
        infantLeaf1 = new AdvancedModelRenderer(this, -3, 0);
        infantLeaf1.setRotationPoint(0.2F, 0.0F, -0.2F);
        infantLeaf1.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf1, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        infantLeaf4 = new AdvancedModelRenderer(this, -3, 0);
        infantLeaf4.setRotationPoint(0.0F, 0.0F, 0.0F);
        infantLeaf4.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf4, -0.5235987755982988F, -3.9269908169872414F, 0.0F);
        mouthCover = new AdvancedModelRenderer(this, 0, 0);
        mouthCover.setRotationPoint(0.0F, -1.0F, 0.0F);
        mouthCover.addBox(-2.0F, -1.0F, -2.5F, 4, 1, 5, 0.0F);
        juvenileBase.addChild(juvenileLeaf3);
        juvenileBase.addChild(mouthBase);
        mouthBase.addChild(mouth1);
        infantBase.addChild(infantLeaf3);
        infantBase.addChild(infantLeaf2);
        juvenileBase.addChild(juvenileLeaf2);
        mouth1.addChild(teeth1);
        juvenileBase.addChild(juvenileLeaf4);
        juvenileBase.addChild(juvenileLeaf1);
        mouth2.addChild(teeth2);
        mouthBase.addChild(mouth2);
        infantBase.addChild(infantLeaf1);
        infantBase.addChild(infantLeaf4);
        mouthBase.addChild(mouthCover);

        //parts = Lists.newArrayList(infantBase, juvenileBase, infantLeaf1, infantLeaf2, infantLeaf3, infantLeaf4, juvenileLeaf1, juvenileLeaf2, juvenileLeaf3, juvenileLeaf4, mouthBase, mouth1, mouth2, mouthCover, teeth1, teeth2);
        updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        infantBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        juvenileBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setDefaultAngles(EntityBabyFoliaath entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        resetToDefaultPose();
        float frame = entity.frame + delta;
        float openMouthProgress = entity.activate.getAnimationProgressSinSqrt();
        mouth1.rotateAngleZ += 0.5 * openMouthProgress;
        mouth2.rotateAngleZ -= 0.5 * openMouthProgress;
        walk(juvenileLeaf1, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        walk(juvenileLeaf2, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        walk(juvenileLeaf3, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        walk(juvenileLeaf4, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        flap(mouth1, 1F, 0.07F * openMouthProgress, false, -1, 0, frame, 1F);
        flap(mouth2, 1F, -0.07F * openMouthProgress, false, -1, 0, frame, 1F);
        infantBase.showModel = !(juvenileBase.showModel = !entity.getInfant());
    }

    @Override
    protected void animate(EntityBabyFoliaath entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngles(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        animator.setAnimation(EntityBabyFoliaath.EAT_ANIMATION);
        animator.startKeyframe(2);
        animator.rotate(mouth1, 0, 0, 0.5F);
        animator.rotate(mouth2, 0, 0, -0.5F);
        animator.endKeyframe();
        animator.resetKeyframe(2);
        animator.startKeyframe(2);
        animator.rotate(mouth1, 0, 0, 0.5F);
        animator.rotate(mouth2, 0, 0, -0.5F);
        animator.endKeyframe();
        animator.resetKeyframe(2);
        animator.startKeyframe(2);
        animator.rotate(mouth1, 0, 0, 0.5F);
        animator.rotate(mouth2, 0, 0, -0.5F);
        animator.endKeyframe();
        animator.resetKeyframe(2);
        animator.startKeyframe(2);
        animator.rotate(mouth1, 0, 0, 0.5F);
        animator.rotate(mouth2, 0, 0, -0.5F);
        animator.endKeyframe();
        animator.resetKeyframe(2);
        animator.startKeyframe(2);
        animator.rotate(mouth1, 0, 0, 0.5F);
        animator.rotate(mouth2, 0, 0, -0.5F);
        animator.endKeyframe();
        animator.resetKeyframe(2);
    }
}
