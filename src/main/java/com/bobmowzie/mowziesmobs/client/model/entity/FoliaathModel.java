package com.bobmowzie.mowziesmobs.client.model.entity;

import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;

@SideOnly(Side.CLIENT)
public class FoliaathModel extends AdvancedModelBase {
    public AdvancedModelRenderer bigLeaf2Base;
    public AdvancedModelRenderer bigLeaf1Base;
    public AdvancedModelRenderer bigLeaf4Base;
    public AdvancedModelRenderer bigLeaf3Base;
    public AdvancedModelRenderer stem1Base;
    public AdvancedModelRenderer stem1Joint;
    public AdvancedModelRenderer stem2;
    public AdvancedModelRenderer stem3;
    public AdvancedModelRenderer stem4;
    public AdvancedModelRenderer headBase;
    public AdvancedModelRenderer mouthTop1;
    public AdvancedModelRenderer leaf1Head;
    public AdvancedModelRenderer leaf2Head;
    public AdvancedModelRenderer leaf3Head;
    public AdvancedModelRenderer leaf4Head;
    public AdvancedModelRenderer leaf5Head;
    public AdvancedModelRenderer leaf6Head;
    public AdvancedModelRenderer leaf7Head;
    public AdvancedModelRenderer leaf8Head;
    public AdvancedModelRenderer tongue1Base;
    public AdvancedModelRenderer mouthBack;
    public AdvancedModelRenderer mouthBottom1;
    public AdvancedModelRenderer mouthTop2;
    public AdvancedModelRenderer teethTop1;
    public AdvancedModelRenderer teethTop2;
    public AdvancedModelRenderer tongue2;
    public AdvancedModelRenderer tongue3;
    public AdvancedModelRenderer mouthBottom2;
    public AdvancedModelRenderer teethBottom1;
    public AdvancedModelRenderer teethBottom2;
    public AdvancedModelRenderer bigLeaf2End;
    public AdvancedModelRenderer bigLeaf1End;
    public AdvancedModelRenderer bigLeaf4End;
    public AdvancedModelRenderer bigLeaf3End;
    public AdvancedModelRenderer[] stemParts;
    public AdvancedModelRenderer[] tongueParts;
    public AdvancedModelRenderer[] leafParts1;
    public AdvancedModelRenderer[] leafParts2;
    public AdvancedModelRenderer[] leafParts3;
    public AdvancedModelRenderer[] leafParts4;
    private float activeProgress;

    private ModelAnimator animator;

    public FoliaathModel() {
        textureWidth = 128;
        textureHeight = 64;
        animator = ModelAnimator.create();

        headBase = new AdvancedModelRenderer(this, 80, 15);
        headBase.setRotationPoint(0.0F, -10.0F, 0.0F);
        headBase.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 2, 0.0F);
        setRotateAngle(headBase, 1.3658946726107624F, 0.0F, 0.0F);
        leaf6Head = new AdvancedModelRenderer(this, 0, 18);
        leaf6Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf6Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf6Head, 0.6829473363053812F, 0.0F, 3.9269908169872414F);
        bigLeaf3End = new AdvancedModelRenderer(this, 64, 0);
        bigLeaf3End.setRotationPoint(0.0F, -14.0F, 0.0F);
        bigLeaf3End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf3End, -1.2292353921796064F, 0.0F, 0.0F);
        stem1Base = new AdvancedModelRenderer(this, 0, 0);
        stem1Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        stem1Base.addBox(-1.5F, -15.0F, -1.5F, 3, 15, 3, 0.0F);
        setRotateAngle(stem1Base, 0.136659280431156F, 0.0F, 0.0F);
        stem1Joint = new AdvancedModelRenderer(this, 0, 0);
        stem1Joint.setRotationPoint(0.0F, 24.0F, 0.0F);
        stem1Joint.addBox(0F, 0F, 0F, 0, 0, 0, 0.0F);
        setRotateAngle(stem1Joint, 0.0F, 0.0F, 0.0F);
        teethBottom2 = new AdvancedModelRenderer(this, 15, 22);
        teethBottom2.setRotationPoint(0.0F, 0.0F, 0.0F);
        teethBottom2.addBox(-3.0F, 0.0F, 0.0F, 6, 3, 7, 0.0F);
        tongue1Base = new AdvancedModelRenderer(this, 40, 26);
        tongue1Base.setRotationPoint(0.0F, 0.0F, 2.0F);
        tongue1Base.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 6, 0.0F);
        bigLeaf2Base = new AdvancedModelRenderer(this, 64, 14);
        bigLeaf2Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        bigLeaf2Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf2Base, -0.6829473363053812F, 2.356194490192345F, 0.0F);
        mouthBack = new AdvancedModelRenderer(this, 54, 37);
        mouthBack.setRotationPoint(0.0F, -0.5F, 2.0F);
        mouthBack.addBox(-6.0F, -4.5F, 0.0F, 12, 9, 2, 0.0F);
        bigLeaf1End = new AdvancedModelRenderer(this, 64, 0);
        bigLeaf1End.setRotationPoint(0.0F, -14.0F, 0.0F);
        bigLeaf1End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf1End, -1.2292353921796064F, 0.0F, 0.0F);
        leaf4Head = new AdvancedModelRenderer(this, 0, 18);
        leaf4Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf4Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf4Head, 0.6829473363053812F, 0.0F, 2.356194490192345F);
        mouthBottom2 = new AdvancedModelRenderer(this, 36, 16);
        mouthBottom2.setRotationPoint(0.0F, 0.0F, 12.0F);
        mouthBottom2.addBox(-3.0F, -2.0F, 0.0F, 6, 2, 7, 0.0F);
        leaf5Head = new AdvancedModelRenderer(this, 0, 18);
        leaf5Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf5Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf5Head, 0.6829473363053812F, 0.0F, 3.141592653589793F);
        leaf3Head = new AdvancedModelRenderer(this, 0, 18);
        leaf3Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf3Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf3Head, 0.6829473363053812F, 0.0F, 1.5707963267948966F);
        bigLeaf1Base = new AdvancedModelRenderer(this, 64, 14);
        bigLeaf1Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        bigLeaf1Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf1Base, -0.6829473363053812F, 0.7853981633974483F, 0.0F);
        bigLeaf4End = new AdvancedModelRenderer(this, 64, 0);
        bigLeaf4End.setRotationPoint(0.0F, -14.0F, 0.0F);
        bigLeaf4End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf4End, -1.2292353921796064F, 0.0F, 0.0F);
        mouthBottom1 = new AdvancedModelRenderer(this, 16, 0);
        mouthBottom1.setRotationPoint(0.0F, 1.0F, 2.0F);
        mouthBottom1.addBox(-6.0F, -4.0F, 0.0F, 12, 4, 12, 0.0F);
        setRotateAngle(mouthBottom1, 0.0F, 0.0F, 3.141592653589793F);
        leaf8Head = new AdvancedModelRenderer(this, 0, 18);
        leaf8Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf8Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf8Head, 0.6829473363053812F, 0.0F, 5.497787143782138F);
        leaf2Head = new AdvancedModelRenderer(this, 0, 18);
        leaf2Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf2Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf2Head, 0.6829473363053812F, 0.0F, 0.7853981633974483F);
        teethBottom1 = new AdvancedModelRenderer(this, 80, 0);
        teethBottom1.setRotationPoint(0.0F, 0.0F, 0.0F);
        teethBottom1.addBox(-6.0F, 0.0F, 0.0F, 12, 3, 12, 0.0F);
        tongue3 = new AdvancedModelRenderer(this, 80, 24);
        tongue3.setRotationPoint(0.0F, 0.0F, 6.0F);
        tongue3.addBox(-2.0F, -1.0F, 0.0F, 4, 2, 5, 0.0F);
        leaf1Head = new AdvancedModelRenderer(this, 0, 18);
        leaf1Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf1Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf1Head, 0.6829473363053812F, 0.0F, 0.0F);
        teethTop2 = new AdvancedModelRenderer(this, 15, 22);
        teethTop2.setRotationPoint(0.0F, 0.0F, 0.0F);
        teethTop2.addBox(-3.0F, 0.0F, 0.0F, 6, 3, 7, 0.0F);
        stem3 = new AdvancedModelRenderer(this, 0, 0);
        stem3.setRotationPoint(0.0F, -15.0F, 0.0F);
        stem3.addBox(-1.5F, -13.0F, -1.5F, 3, 13, 3, 0.0F);
        setRotateAngle(stem3, -1.1383037381507017F, 0.0F, 0.0F);
        stem2 = new AdvancedModelRenderer(this, 0, 0);
        stem2.setRotationPoint(0.0F, -15.0F, 0.0F);
        stem2.addBox(-1.5F, -15.0F, -1.5F, 3, 15, 3, 0.0F);
        setRotateAngle(stem2, 0.36425021489121656F, 0.0F, 0.0F);
        bigLeaf3Base = new AdvancedModelRenderer(this, 64, 14);
        bigLeaf3Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        bigLeaf3Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf3Base, -0.6829473363053812F, 3.9269908169872414F, 0.0F);
        bigLeaf4Base = new AdvancedModelRenderer(this, 64, 14);
        bigLeaf4Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        bigLeaf4Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf4Base, -0.6829473363053812F, 5.497787143782138F, 0.0F);
        tongue2 = new AdvancedModelRenderer(this, 40, 26);
        tongue2.setRotationPoint(0.0F, 0.0F, 6.0F);
        tongue2.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 6, 0.0F);
        mouthTop2 = new AdvancedModelRenderer(this, 36, 16);
        mouthTop2.setRotationPoint(0.0F, 0.0F, 12.0F);
        mouthTop2.addBox(-3.0F, -2.0F, 0.0F, 6, 2, 7, 0.0F);
        bigLeaf2End = new AdvancedModelRenderer(this, 64, 0);
        bigLeaf2End.setRotationPoint(0.0F, -14.0F, 0.0F);
        bigLeaf2End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(bigLeaf2End, -1.2292353921796064F, 0.0F, 0.0F);
        stem4 = new AdvancedModelRenderer(this, 0, 0);
        stem4.setRotationPoint(0.0F, -13.0F, 0.0F);
        stem4.addBox(-1.5F, -10.0F, -1.5F, 3, 10, 3, 0.0F);
        setRotateAngle(stem4, -0.9105382707654417F, 0.0F, 0.0F);
        teethTop1 = new AdvancedModelRenderer(this, 80, 0);
        teethTop1.setRotationPoint(0.0F, 0.0F, 0.0F);
        teethTop1.addBox(-6.0F, 0.0F, 0.0F, 12, 3, 12, 0.0F);
        mouthTop1 = new AdvancedModelRenderer(this, 16, 0);
        mouthTop1.setRotationPoint(0.0F, -2.0F, 2.0F);
        mouthTop1.addBox(-6.0F, -4.0F, 0.0F, 12, 4, 12, 0.0F);
        leaf7Head = new AdvancedModelRenderer(this, 0, 18);
        leaf7Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        leaf7Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(leaf7Head, 0.6829473363053812F, 0.0F, 4.71238898038469F);
        stem4.addChild(headBase);
        headBase.addChild(leaf6Head);
        bigLeaf3Base.addChild(bigLeaf3End);
        mouthBottom2.addChild(teethBottom2);
        headBase.addChild(tongue1Base);
        headBase.addChild(mouthBack);
        bigLeaf1Base.addChild(bigLeaf1End);
        headBase.addChild(leaf4Head);
        mouthBottom1.addChild(mouthBottom2);
        headBase.addChild(leaf5Head);
        headBase.addChild(leaf3Head);
        bigLeaf4Base.addChild(bigLeaf4End);
        headBase.addChild(mouthBottom1);
        headBase.addChild(leaf8Head);
        headBase.addChild(leaf2Head);
        mouthBottom1.addChild(teethBottom1);
        tongue2.addChild(tongue3);
        headBase.addChild(leaf1Head);
        mouthTop2.addChild(teethTop2);
        stem2.addChild(stem3);
        stem1Base.addChild(stem2);
        stem1Joint.addChild(stem1Base);
        tongue1Base.addChild(tongue2);
        mouthTop1.addChild(mouthTop2);
        bigLeaf2Base.addChild(bigLeaf2End);
        stem3.addChild(stem4);
        mouthTop1.addChild(teethTop1);
        headBase.addChild(mouthTop1);
        headBase.addChild(leaf7Head);

        stemParts = new AdvancedModelRenderer[]{headBase, stem4, stem3, stem2, stem1Base};
        tongueParts = new AdvancedModelRenderer[]{tongue1Base, tongue2, tongue3};
        leafParts1 = new AdvancedModelRenderer[]{bigLeaf1End, bigLeaf1Base};
        leafParts2 = new AdvancedModelRenderer[]{bigLeaf2End, bigLeaf2Base};
        leafParts3 = new AdvancedModelRenderer[]{bigLeaf3End, bigLeaf3Base};
        leafParts4 = new AdvancedModelRenderer[]{bigLeaf4End, bigLeaf4Base};

        stem1Joint.rotationPointY += 2;
        stem1Joint.rotateAngleX += 0.05;
        stem2.rotateAngleX += 0.3;
        stem3.rotateAngleX += -0.1;
        headBase.rotateAngleX += -0.35;
        stem1Base.setRotationPoint(0, 0, 0);

        updateDefaultPose();
    }

    @Override
    public void render(Entity foliaath, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) foliaath, f, f1, f2, f3, f4, f5);
        float leafScale = 1.25F;
        GL11.glScalef(leafScale, leafScale, leafScale);
        bigLeaf2Base.rotationPointY -= 3.5;
        bigLeaf1Base.rotationPointY -= 3.5;
        bigLeaf3Base.rotationPointY -= 3.5;
        bigLeaf4Base.rotationPointY -= 3.5;
        bigLeaf2Base.render(f5);
        bigLeaf1Base.render(f5);
        bigLeaf3Base.render(f5);
        bigLeaf4Base.render(f5);
        GL11.glScalef(1 / leafScale, 1 / leafScale, 1 / leafScale);
        GL11.glTranslatef(0, 1.4F, 0);
        GL11.glTranslatef(0, -1.4F * activeProgress, 0);
        GL11.glScalef(activeProgress, activeProgress, activeProgress);
        stem1Joint.render(f5);
    }

    public void setRotateAngle(AdvancedModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(EntityFoliaath foliaath, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, foliaath);

        stem1Joint.rotateAngleY += (f3 / (180f / (float) Math.PI));
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float f, float f1, float partialTicks) {
        EntityFoliaath foliaath = (EntityFoliaath) entity;
        animator.update(foliaath);
        resetToDefaultPose();

        activeProgress = foliaath.activate.getAnimationProgressSinSqrt();
        float activeIntermittent = foliaath.activate.getAnimationProgressSinSqrt() - foliaath.activate.getAnimationProgressSinToTenWithoutReturn();
        float activeComplete = activeProgress - activeIntermittent;
        float stopDance = foliaath.stopDance.getAnimationProgressSinSqrt() - (foliaath.stopDance.getAnimationProgressSinSqrt() - foliaath.stopDance.getAnimationProgressSinToTenWithoutReturn());
        float frame = foliaath.frame + partialTicks;

        float globalSpeed = 0.9f;

        flap(stem1Base, 0.25F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 0F, 0F, frame, 1F);
        walk(stem1Base, 0.5F * globalSpeed, 0.05F * (activeComplete - stopDance), false, 0F, 0F, frame, 1F);
        walk(stem2, 0.5F * globalSpeed, 0.05F * (activeComplete - stopDance), false, 0.5F, 0F, frame, 1F);
        walk(stem3, 0.5F * globalSpeed, 0.07F * (activeComplete - stopDance), false, 1F, 0F, frame, 1F);
        walk(stem4, 0.5F * globalSpeed, 0.05F * (activeComplete - stopDance), false, 1.5F, 0F, frame, 1F);
        walk(headBase, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), true, 1.3F, 0F, frame, 1F);
        headBase.rotateAngleY += rotateBox(0.25F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 0F, 0F, frame, 1F);
        walk(leaf1Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf2Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf3Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf4Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf5Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf6Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf7Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        walk(leaf8Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
        chainWave(leafParts1, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);
        chainWave(leafParts2, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);
        chainWave(leafParts3, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);
        chainWave(leafParts4, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);

        //Open Mouth Animation
        float openMouthProgress = foliaath.openMouth.getAnimationProgressSinSqrt();
        float openMouthIntermittent = foliaath.openMouth.getAnimationProgressSinSqrt() - foliaath.openMouth.getAnimationProgressSinToTenWithoutReturn();
        float headLeafRotation = 0.2F * openMouthProgress - 0.8F * openMouthIntermittent;
        mouthTop1.rotateAngleX -= 0.3 * openMouthIntermittent;
        mouthBottom1.rotateAngleX -= 0.3 * openMouthIntermittent;
        mouthTop2.rotateAngleX += 0.3 * openMouthIntermittent;
        mouthBottom2.rotateAngleX += 0.3 * openMouthIntermittent;
        stem2.rotateAngleX += 0.9 * openMouthIntermittent;
        stem3.rotateAngleX -= 0.6 * openMouthIntermittent;
        stem4.rotateAngleX -= 0.6 * openMouthIntermittent;
        headBase.rotateAngleX += 0.6 * openMouthIntermittent;
        flap(headBase, 1.5F, 0.6F * openMouthIntermittent, false, 0F, 0F, frame, 1F);
        mouthTop1.rotateAngleX += 0.15 * openMouthProgress;
        mouthBottom1.rotateAngleX += 0.15 * openMouthProgress;
        chainWave(tongueParts, 0.5F * globalSpeed, -0.15F * (openMouthProgress - openMouthIntermittent), -2, frame, 1F);
        tongue1Base.rotateAngleY += 0.3 * (openMouthProgress - openMouthIntermittent);
        tongue2.rotateAngleY += 0.4 * (openMouthProgress - openMouthIntermittent);
        tongue2.rotateAngleX -= 0.1 * (openMouthProgress - openMouthIntermittent);
        tongue3.rotateAngleX -= 0.5 * (openMouthProgress - openMouthIntermittent);
        stem1Base.rotateAngleX -= 0.2 * openMouthProgress;
        stem2.rotateAngleX -= 0.1 * openMouthProgress;
        stem3.rotateAngleX -= 0.1 * openMouthProgress;
        stem4.rotateAngleX -= 0.1 * openMouthProgress;
        headBase.rotateAngleX += 0.6 * openMouthProgress;
        leaf1Head.rotateAngleX -= headLeafRotation;
        leaf2Head.rotateAngleX -= headLeafRotation;
        leaf3Head.rotateAngleX -= headLeafRotation;
        leaf4Head.rotateAngleX -= headLeafRotation;
        leaf5Head.rotateAngleX -= headLeafRotation;
        leaf6Head.rotateAngleX -= headLeafRotation;
        leaf7Head.rotateAngleX -= headLeafRotation;
        leaf8Head.rotateAngleX -= headLeafRotation;

        //Activate Animation
        chainFlap(stemParts, 0.7F, 0.2F * 2 * activeIntermittent, 2F, frame, 1F);
        chainSwing(tongueParts, 0.7F, 0.6F * 2 * activeIntermittent, -2F, frame, 1F);
        chainWave(leafParts1, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        chainWave(leafParts2, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        chainWave(leafParts3, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        chainWave(leafParts4, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        stem1Base.rotateAngleX -= 0.1 * 2 * activeIntermittent;
        stem2.rotateAngleX -= 0.5 * 2 * activeIntermittent;
        stem3.rotateAngleX += 0.9 * 2 * activeIntermittent;
        stem4.rotateAngleX += 0.6 * 2 * activeIntermittent;
        headBase.rotateAngleX += 0.6 * 2 * activeIntermittent;
        mouthTop1.rotateAngleX += 0.4 * 2 * activeIntermittent;
        mouthBottom1.rotateAngleX += 0.4 * 2 * activeIntermittent;
    }

    public void animate(IAnimatedEntity foliaath, float f, float f1, float f2, float f3, float f4, float f5) {
        EntityFoliaath entityfoliaath = (EntityFoliaath) foliaath;
        setRotationAngles(entityfoliaath, f, f1, f2, f3, f4, f5);

        //Bite
        animator.setAnimation(EntityFoliaath.ATTACK_ANIMATION);
        animator.startKeyframe(3);
        animator.rotate(stem1Base, 0.4F, 0, 0);
        animator.rotate(stem2, -0.3F, 0, 0);
        animator.rotate(stem3, 0.2F, 0, 0);
        animator.rotate(stem4, 0.2F, 0, 0);
        animator.rotate(headBase, -0.6F, 0, 0);
        animator.rotate(mouthTop1, 0.8F, 0, 0);
        animator.rotate(mouthBottom1, 0.8F, 0, 0);
        animator.rotate(tongue1Base, -0.2F, 0, 0);
        animator.rotate(tongue2, -0.5F, 0, 0);
        animator.move(tongue2, 0, -0.3F, 0);
        animator.rotate(tongue3, 0.4F, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(1);
        animator.startKeyframe(2);
        animator.rotate(stem1Base, -0.6F, 0, 0);
        animator.rotate(stem2, -1.2F, 0, 0);
        animator.rotate(stem3, 0.8F, 0, 0);
        animator.rotate(stem4, 0.8F, 0, 0);
        animator.rotate(headBase, 0.4F, 0, 0);
        animator.rotate(mouthTop1, -0.1F, 0, 0);
        animator.rotate(mouthBottom1, -0.1F, 0, 0);
        animator.rotate(mouthTop2, 0.15F, 0, 0);
        animator.rotate(mouthBottom2, 0.15F, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(3);
        animator.resetKeyframe(5);

        animator.setAnimation(EntityFoliaath.HURT_ANIMATION);
        animator.startKeyframe(3);
        animator.rotate(stem2, 0.6F, 0, 0);
        animator.rotate(stem3, -0.4F, 0, 0);
        animator.rotate(stem4, -0.4F, 0, 0);
        animator.rotate(headBase, -0.2F, 0, 0);
        animator.rotate(leaf1Head, 0.6F, 0, 0);
        animator.rotate(leaf2Head, 0.6F, 0, 0);
        animator.rotate(leaf3Head, 0.6F, 0, 0);
        animator.rotate(leaf4Head, 0.6F, 0, 0);
        animator.rotate(leaf5Head, 0.6F, 0, 0);
        animator.rotate(leaf6Head, 0.6F, 0, 0);
        animator.rotate(leaf7Head, 0.6F, 0, 0);
        animator.rotate(leaf8Head, 0.6F, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(7);

        float deathFlailProgress = entityfoliaath.deathFlail.getAnimationProgressSinSqrt();
        chainFlap(stemParts, 0.7F, 0.2F * deathFlailProgress, 2F, entityfoliaath.frame, 1F);
        chainSwing(tongueParts, 0.7F, 0.6F * deathFlailProgress, -2F, entityfoliaath.frame, 1F);
        chainWave(leafParts1, 1.5F, 0.1F * deathFlailProgress, 0, entityfoliaath.frame, 1F);
        chainWave(leafParts2, 1.5F, 0.1F * deathFlailProgress, 0, entityfoliaath.frame, 1F);
        chainWave(leafParts3, 1.5F, 0.1F * deathFlailProgress, 0, entityfoliaath.frame, 1F);
        chainWave(leafParts4, 1.5F, 0.1F * deathFlailProgress, 0, entityfoliaath.frame, 1F);
        animator.setAnimation(EntityFoliaath.DIE_ANIMATION);
        animator.startKeyframe(4);
        animator.rotate(stem1Base, -0.1F, 0, 0);
        animator.rotate(stem2, -0.5F, 0, 0);
        animator.rotate(stem3, 0.9F, 0, 0);
        animator.rotate(stem4, 0.6F, 0, 0);
        animator.rotate(headBase, 0.6F, 0, 0);
        animator.rotate(mouthTop1, 0.4F, 0, 0);
        animator.rotate(mouthBottom1, 0.4F, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(10);
        animator.startKeyframe(5);
        animator.rotate(stem1Base, -0.1F, 0, 0);
        animator.rotate(stem2, -0.5F, 0, 0);
        animator.rotate(stem3, 0.9F, 0, 0);
        animator.rotate(stem4, 0.6F, 0, 0);
        animator.rotate(headBase, 0.6F, 0, 0);
        animator.rotate(stem1Joint, 0, -0.4F, 0);
        animator.rotate(stem1Base, -0.6F, 0, 0);
        animator.rotate(stem2, -0.7F, 0, 0);
        animator.rotate(stem3, -0.6F, 0, 0);
        animator.rotate(stem4, -0.6F, 0, 0);
        animator.rotate(headBase, 1.25F, 0, 0);
        animator.rotate(mouthTop1, 0.1F, -0.05F, 0);
        animator.rotate(tongue1Base, 0, 0.3F, 0);
        animator.rotate(tongue2, -0.1F, 0.4F, 0);
        animator.rotate(tongue3, -0.5F, 0F, 0);
        animator.rotate(leaf1Head, 0.7F, 0, 0);
        animator.rotate(leaf2Head, 0.7F, 0, 0);
        animator.rotate(leaf3Head, 0.7F, 0, 0);
        animator.rotate(leaf4Head, 0.7F, 0, 0);
        animator.rotate(leaf5Head, 0.7F, 0, 0);
        animator.rotate(leaf6Head, 0.7F, 0, 0);
        animator.rotate(leaf7Head, 0.7F, 0, 0);
        animator.rotate(leaf8Head, 0.7F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(2);
        animator.rotate(stem1Base, -0.1F, 0, 0);
        animator.rotate(stem2, -0.5F, 0, 0);
        animator.rotate(stem3, 0.9F, 0, 0);
        animator.rotate(stem4, 0.6F, 0, 0);
        animator.rotate(headBase, 0.6F, 0, 0);
        animator.rotate(stem1Joint, 0, -0.4F, 0);
        animator.rotate(stem1Base, -0.5F, 0, 0);
        animator.rotate(stem2, -0.6F, 0, 0);
        animator.rotate(stem3, -0.5F, 0, 0);
        animator.rotate(stem4, -0.5F, 0, 0);
        animator.rotate(headBase, 0.7F, 0, 0);
        animator.rotate(mouthTop1, 0.1F, -0.05F, 0);
        animator.rotate(tongue1Base, 0, 0.3F, 0);
        animator.rotate(tongue2, -0.1F, 0.4F, 0);
        animator.rotate(tongue3, -0.5F, 0F, 0);
        animator.rotate(leaf1Head, 0.7F, 0, 0);
        animator.rotate(leaf2Head, 0.7F, 0, 0);
        animator.rotate(leaf3Head, 0.7F, 0, 0);
        animator.rotate(leaf4Head, 0.7F, 0, 0);
        animator.rotate(leaf5Head, 0.7F, 0, 0);
        animator.rotate(leaf6Head, 0.7F, 0, 0);
        animator.rotate(leaf7Head, 0.7F, 0, 0);
        animator.rotate(leaf8Head, 0.7F, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(2);
        animator.rotate(stem1Base, -0.1F, 0, 0);
        animator.rotate(stem2, -0.5F, 0, 0);
        animator.rotate(stem3, 0.9F, 0, 0);
        animator.rotate(stem4, 0.6F, 0, 0);
        animator.rotate(headBase, 0.6F, 0, 0);
        animator.rotate(stem1Joint, 0, -0.4F, 0);
        animator.rotate(stem1Base, -0.6F, 0, 0);
        animator.rotate(stem2, -0.7F, 0, 0);
        animator.rotate(stem3, -0.6F, 0, 0);
        animator.rotate(stem4, -0.6F, 0, 0);
        animator.rotate(headBase, 1.25F, 0, 0);
        animator.rotate(mouthTop1, 0.1F, -0.05F, 0);
        animator.rotate(tongue1Base, 0, 0.3F, 0);
        animator.rotate(tongue2, -0.1F, 0.4F, 0);
        animator.rotate(tongue3, -0.5F, 0F, 0);
        animator.rotate(leaf1Head, 0.7F, 0, 0);
        animator.rotate(leaf2Head, 0.7F, 0, 0);
        animator.rotate(leaf3Head, 0.7F, 0, 0);
        animator.rotate(leaf4Head, 0.7F, 0, 0);
        animator.rotate(leaf5Head, 0.7F, 0, 0);
        animator.rotate(leaf6Head, 0.7F, 0, 0);
        animator.rotate(leaf7Head, 0.7F, 0, 0);
        animator.rotate(leaf8Head, 0.7F, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(27);
    }

    public float rotateBox(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        if (invert) {
            return -MathHelper.cos(f * speed + offset) * degree * f1 + weight * f1;
        } else {
            return MathHelper.cos(f * speed + offset) * degree * f1 + weight * f1;
        }
    }
}