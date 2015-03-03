package com.bobmowzie.mowziesmobs.client.model.block;

import com.bobmowzie.mowziesmobs.client.model.animation.tools.MowzieModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBabyFoliaath extends MowzieModelBase
{
    public ModelRenderer infantBase;
    public ModelRenderer juvenileBase;
    public ModelRenderer infantLeaf1;
    public ModelRenderer infantLeaf2;
    public ModelRenderer infantLeaf3;
    public ModelRenderer infantLeaf4;
    public ModelRenderer juvenileLeaf1;
    public ModelRenderer juvenileLeaf2;
    public ModelRenderer juvenileLeaf3;
    public ModelRenderer juvenileLeaf4;
    public ModelRenderer mouthBase;
    public ModelRenderer mouth1;
    public ModelRenderer mouth2;
    public ModelRenderer mouthCover;
    public ModelRenderer teeth1;
    public ModelRenderer teeth2;

    public ModelBabyFoliaath() {
        textureWidth = 64;
        textureHeight = 16;
        juvenileLeaf3 = new ModelRenderer(this, 27, 0);
        juvenileLeaf3.setRotationPoint(-1.0F, 0.0F, 1.0F);
        juvenileLeaf3.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf3, -0.3490658503988659F, 2.356194490192345F, 0.0F);
        mouthBase = new ModelRenderer(this, 13, 0);
        mouthBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        mouthBase.addBox(-1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F);
        mouth1 = new ModelRenderer(this, 20, 0);
        mouth1.setRotationPoint(0.5F, -1.0F, 0.0F);
        mouth1.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        setRotateAngle(mouth1, 0.0F, 0.0F, 0F);
        infantLeaf3 = new ModelRenderer(this, -3, 0);
        infantLeaf3.setRotationPoint(0.2F, 0.0F, 0.2F);
        infantLeaf3.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf3, -0.5235987755982988F, -2.356194490192345F, 0.0F);
        infantBase = new ModelRenderer(this, 0, 0);
        infantBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        infantBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        infantLeaf2 = new ModelRenderer(this, -3, 0);
        infantLeaf2.setRotationPoint(0.0F, 0.0F, 0.0F);
        infantLeaf2.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf2, -0.5235987755982988F, 0.7853981633974483F, 0.0F);
        juvenileLeaf2 = new ModelRenderer(this, 27, 0);
        juvenileLeaf2.setRotationPoint(-1.0F, 0.0F, -1.0F);
        juvenileLeaf2.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf2, -0.3490658503988659F, 0.7853981633974483F, 0.0F);
        juvenileBase = new ModelRenderer(this, 0, 0);
        juvenileBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        juvenileBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        teeth1 = new ModelRenderer(this, 49, 2);
        teeth1.setRotationPoint(-1.0F, 0.0F, 0.0F);
        teeth1.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        juvenileLeaf4 = new ModelRenderer(this, 27, 0);
        juvenileLeaf4.setRotationPoint(1.0F, 0.0F, 1.0F);
        juvenileLeaf4.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf4, -0.3490658503988659F, 3.9269908169872414F, 0.0F);
        juvenileLeaf1 = new ModelRenderer(this, 27, 0);
        juvenileLeaf1.setRotationPoint(1.0F, 0.0F, -1.0F);
        juvenileLeaf1.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(juvenileLeaf1, -0.3490658503988659F, -0.7853981633974483F, 0.0F);
        teeth2 = new ModelRenderer(this, 37, 2);
        teeth2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        teeth2.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        mouth2 = new ModelRenderer(this, 20, 0);
        mouth2.setRotationPoint(-0.5F, -1.0F, 0.0F);
        mouth2.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        setRotateAngle(mouth2, 0.0F, 3.141592653589793F, 0F);
        infantLeaf1 = new ModelRenderer(this, -3, 0);
        infantLeaf1.setRotationPoint(0.2F, 0.0F, -0.2F);
        infantLeaf1.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf1, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        infantLeaf4 = new ModelRenderer(this, -3, 0);
        infantLeaf4.setRotationPoint(0.0F, 0.0F, 0.0F);
        infantLeaf4.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(infantLeaf4, -0.5235987755982988F, -3.9269908169872414F, 0.0F);
        mouthCover = new ModelRenderer(this, 0, 0);
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
    }

    public void render()
    {
        infantBase.render(0.0625f);
        juvenileBase.render(0.0625f);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
