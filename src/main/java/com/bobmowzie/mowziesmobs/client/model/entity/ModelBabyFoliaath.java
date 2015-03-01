package com.bobmowzie.mowziesmobs.client.model.entity;

/**
 * Created by jnad325 on 3/1/15.
 */

import com.bobmowzie.mowziesmobs.client.model.animation.tools.MowzieModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBabyFoliaath extends MowzieModelBase {
    public ModelRenderer InfantBase;
    public ModelRenderer JuvenileBase;
    public ModelRenderer InfantLeaf1;
    public ModelRenderer InfantLeaf2;
    public ModelRenderer InfantLeaf3;
    public ModelRenderer InfantLeaf4;
    public ModelRenderer JuvenileLeaf1;
    public ModelRenderer JuvenileLeaf2;
    public ModelRenderer JuvenileLeaf3;
    public ModelRenderer JuvenileLeaf4;
    public ModelRenderer MouthBase;
    public ModelRenderer Mouth1;
    public ModelRenderer Mouth2;
    public ModelRenderer MouthCover;
    public ModelRenderer Teeth1;
    public ModelRenderer Teeth2;

    public ModelBabyFoliaath() {
        this.textureWidth = 64;
        this.textureHeight = 16;
        this.JuvenileLeaf3 = new ModelRenderer(this, 27, 0);
        this.JuvenileLeaf3.setRotationPoint(-1.0F, 0.0F, 1.0F);
        this.JuvenileLeaf3.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        this.setRotateAngle(JuvenileLeaf3, -0.3490658503988659F, 2.356194490192345F, 0.0F);
        this.MouthBase = new ModelRenderer(this, 13, 0);
        this.MouthBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.MouthBase.addBox(-1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F);
        this.Mouth1 = new ModelRenderer(this, 20, 0);
        this.Mouth1.setRotationPoint(0.5F, -1.0F, 0.0F);
        this.Mouth1.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        this.setRotateAngle(Mouth1, 0.0F, 0.0F, 0F);
        this.InfantLeaf3 = new ModelRenderer(this, -3, 0);
        this.InfantLeaf3.setRotationPoint(0.2F, 0.0F, 0.2F);
        this.InfantLeaf3.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        this.setRotateAngle(InfantLeaf3, -0.5235987755982988F, -2.356194490192345F, 0.0F);
        this.InfantBase = new ModelRenderer(this, 0, 0);
        this.InfantBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.InfantBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.InfantLeaf2 = new ModelRenderer(this, -3, 0);
        this.InfantLeaf2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.InfantLeaf2.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        this.setRotateAngle(InfantLeaf2, -0.5235987755982988F, 0.7853981633974483F, 0.0F);
        this.JuvenileLeaf2 = new ModelRenderer(this, 27, 0);
        this.JuvenileLeaf2.setRotationPoint(-1.0F, 0.0F, -1.0F);
        this.JuvenileLeaf2.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        this.setRotateAngle(JuvenileLeaf2, -0.3490658503988659F, 0.7853981633974483F, 0.0F);
        this.JuvenileBase = new ModelRenderer(this, 0, 0);
        this.JuvenileBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.JuvenileBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.Teeth1 = new ModelRenderer(this, 49, 2);
        this.Teeth1.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.Teeth1.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        this.JuvenileLeaf4 = new ModelRenderer(this, 27, 0);
        this.JuvenileLeaf4.setRotationPoint(1.0F, 0.0F, 1.0F);
        this.JuvenileLeaf4.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        this.setRotateAngle(JuvenileLeaf4, -0.3490658503988659F, 3.9269908169872414F, 0.0F);
        this.JuvenileLeaf1 = new ModelRenderer(this, 27, 0);
        this.JuvenileLeaf1.setRotationPoint(1.0F, 0.0F, -1.0F);
        this.JuvenileLeaf1.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        this.setRotateAngle(JuvenileLeaf1, -0.3490658503988659F, -0.7853981633974483F, 0.0F);
        this.Teeth2 = new ModelRenderer(this, 37, 2);
        this.Teeth2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.Teeth2.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        this.Mouth2 = new ModelRenderer(this, 20, 0);
        this.Mouth2.setRotationPoint(-0.5F, -1.0F, 0.0F);
        this.Mouth2.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        this.setRotateAngle(Mouth2, 0.0F, 3.141592653589793F, 0F);
        this.InfantLeaf1 = new ModelRenderer(this, -3, 0);
        this.InfantLeaf1.setRotationPoint(0.2F, 0.0F, -0.2F);
        this.InfantLeaf1.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        this.setRotateAngle(InfantLeaf1, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        this.InfantLeaf4 = new ModelRenderer(this, -3, 0);
        this.InfantLeaf4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.InfantLeaf4.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        this.setRotateAngle(InfantLeaf4, -0.5235987755982988F, -3.9269908169872414F, 0.0F);
        this.MouthCover = new ModelRenderer(this, 0, 0);
        this.MouthCover.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.MouthCover.addBox(-2.0F, -1.0F, -2.5F, 4, 1, 5, 0.0F);
        this.JuvenileBase.addChild(this.JuvenileLeaf3);
        this.JuvenileBase.addChild(this.MouthBase);
        this.MouthBase.addChild(this.Mouth1);
        this.InfantBase.addChild(this.InfantLeaf3);
        this.InfantBase.addChild(this.InfantLeaf2);
        this.JuvenileBase.addChild(this.JuvenileLeaf2);
        this.Mouth1.addChild(this.Teeth1);
        this.JuvenileBase.addChild(this.JuvenileLeaf4);
        this.JuvenileBase.addChild(this.JuvenileLeaf1);
        this.Mouth2.addChild(this.Teeth2);
        this.MouthBase.addChild(this.Mouth2);
        this.InfantBase.addChild(this.InfantLeaf1);
        this.InfantBase.addChild(this.InfantLeaf4);
        this.MouthBase.addChild(this.MouthCover);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.InfantBase.render(f5);
        this.JuvenileBase.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
