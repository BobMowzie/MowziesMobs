package com.bobmowzie.mowziesmobs.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBlockTest extends ModelBase
{
    ModelRenderer General;
    ModelRenderer General1;
    ModelRenderer General2;
    ModelRenderer pantalla;
    ModelRenderer tapa;
    ModelRenderer deco;
    ModelRenderer cil1;
    ModelRenderer cil2;
    ModelRenderer cil3;
    ModelRenderer cil4;
    ModelRenderer fras1;
    ModelRenderer tap1;
    ModelRenderer tub2;
    ModelRenderer base;
    ModelRenderer fras2;
    ModelRenderer tap2;
    ModelRenderer tub1;

    public ModelBlockTest()
    {
        General = new ModelRenderer(this, 28, 6);
        General.setTextureSize(128, 64);
        General.addBox(-6F, -2.5F, -5F, 12, 5, 10);
        General.setRotationPoint(6F, 21.5F, -6F);
        General1 = new ModelRenderer(this, 76, 21);
        General1.setTextureSize(128, 64);
        General1.addBox(-6.5F, -4F, -5F, 13, 8, 10);
        General1.setRotationPoint(6F, 17.5F, -7F);
        General2 = new ModelRenderer(this, 71, 1);
        General2.setTextureSize(128, 64);
        General2.addBox(-7F, -2F, -5F, 14, 4, 10);
        General2.setRotationPoint(6F, 14.5F, -9F);
        pantalla = new ModelRenderer(this, 3, 27);
        pantalla.setTextureSize(128, 64);
        pantalla.addBox(-3F, -2F, -5F, 6, 4, 10);
        pantalla.setRotationPoint(9.9F, 11.5F, -8.8F);
        tapa = new ModelRenderer(this, 43, 29);
        tapa.setTextureSize(128, 64);
        tapa.addBox(-3F, -2F, -4.5F, 6, 4, 9);
        tapa.setRotationPoint(3F, 12.5F, -9F);
        deco = new ModelRenderer(this, 39, 44);
        deco.setTextureSize(128, 64);
        deco.addBox(-5.5F, -1.5F, -1F, 11, 3, 2);
        deco.setRotationPoint(6F, 18.5F, -12F);
        cil1 = new ModelRenderer(this, 12, 12);
        cil1.setTextureSize(128, 64);
        cil1.addBox(-0.5F, -0.5F, -2F, 1, 1, 4);
        cil1.setRotationPoint(3F, 19.5F, -14F);
        cil2 = new ModelRenderer(this, 12, 12);
        cil2.setTextureSize(128, 64);
        cil2.addBox(-0.5F, -0.5F, -2F, 1, 1, 4);
        cil2.setRotationPoint(9F, 19.5F, -14F);
        cil3 = new ModelRenderer(this, 12, 12);
        cil3.setTextureSize(128, 64);
        cil3.addBox(-0.5F, -0.5F, -2F, 1, 1, 4);
        cil3.setRotationPoint(7F, 19.5F, -14F);
        cil4 = new ModelRenderer(this, 12, 12);
        cil4.setTextureSize(128, 64);
        cil4.addBox(-0.5F, -0.5F, -2F, 1, 1, 4);
        cil4.setRotationPoint(5F, 19.5F, -14F);
        fras1 = new ModelRenderer(this, 13, 12);
        fras1.setTextureSize(128, 64);
        fras1.addBox(-1F, -1.5F, -1F, 2, 3, 2);
        fras1.setRotationPoint(14F, 21.5F, -10F);
        tap1 = new ModelRenderer(this, 11, 6);
        tap1.setTextureSize(128, 64);
        tap1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
        tap1.setRotationPoint(14F, 19.5F, -10F);
        tub2 = new ModelRenderer(this, 40, 10);
        tub2.setTextureSize(128, 64);
        tub2.addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1);
        tub2.setRotationPoint(13F, 17.5F, -10F);
        base = new ModelRenderer(this, 36, 9);
        base.setTextureSize(128, 64);
        base.addBox(-1.5F, -0.5F, -4.5F, 3, 1, 9);
        base.setRotationPoint(14F, 23.5F, -7F);
        fras2 = new ModelRenderer(this, 13, 12);
        fras2.setTextureSize(128, 64);
        fras2.addBox(-1F, -1.5F, -1F, 2, 3, 2);
        fras2.setRotationPoint(14F, 21.5F, -5F);
        tap2 = new ModelRenderer(this, 11, 6);
        tap2.setTextureSize(128, 64);
        tap2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
        tap2.setRotationPoint(14F, 19.5F, -5F);
        tub1 = new ModelRenderer(this, 40, 10);
        tub1.setTextureSize(128, 64);
        tub1.addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1);
        tub1.setRotationPoint(13F, 17.5F, -5F);
    }

    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        General.rotateAngleX = 0F;
        General.rotateAngleY = 0F;
        General.rotateAngleZ = 0F;
        General.renderWithRotation(par7);

        General1.rotateAngleX = 0.4363323F;
        General1.rotateAngleY = 0F;
        General1.rotateAngleZ = 0F;
        General1.renderWithRotation(par7);

        General2.rotateAngleX = 0F;
        General2.rotateAngleY = 0F;
        General2.rotateAngleZ = 0F;
        General2.renderWithRotation(par7);

        pantalla.rotateAngleX = -0.1745329F;
        pantalla.rotateAngleY = 0F;
        pantalla.rotateAngleZ = 0F;
        pantalla.renderWithRotation(par7);

        tapa.rotateAngleX = -0.1561269F;
        tapa.rotateAngleY = 0F;
        tapa.rotateAngleZ = 0F;
        tapa.renderWithRotation(par7);

        deco.rotateAngleX = 0.4363323F;
        deco.rotateAngleY = 0F;
        deco.rotateAngleZ = 0F;
        deco.renderWithRotation(par7);

        cil1.rotateAngleX = 0.4363323F;
        cil1.rotateAngleY = 0F;
        cil1.rotateAngleZ = 0F;
        cil1.renderWithRotation(par7);

        cil2.rotateAngleX = 0.4363323F;
        cil2.rotateAngleY = 0F;
        cil2.rotateAngleZ = 0F;
        cil2.renderWithRotation(par7);

        cil3.rotateAngleX = 0.4363323F;
        cil3.rotateAngleY = 0F;
        cil3.rotateAngleZ = 0F;
        cil3.renderWithRotation(par7);

        cil4.rotateAngleX = 0.4363323F;
        cil4.rotateAngleY = 0F;
        cil4.rotateAngleZ = 0F;
        cil4.renderWithRotation(par7);

        fras1.rotateAngleX = 0F;
        fras1.rotateAngleY = 0F;
        fras1.rotateAngleZ = 0F;
        fras1.renderWithRotation(par7);

        tap1.rotateAngleX = 0F;
        tap1.rotateAngleY = 0F;
        tap1.rotateAngleZ = 0F;
        tap1.renderWithRotation(par7);

        tub2.rotateAngleX = 0F;
        tub2.rotateAngleY = 0F;
        tub2.rotateAngleZ = 0.7540493F;
        tub2.renderWithRotation(par7);

        base.rotateAngleX = 0F;
        base.rotateAngleY = 0F;
        base.rotateAngleZ = 0F;
        base.renderWithRotation(par7);

        fras2.rotateAngleX = 0F;
        fras2.rotateAngleY = 0F;
        fras2.rotateAngleZ = 0F;
        fras2.renderWithRotation(par7);

        tap2.rotateAngleX = 0F;
        tap2.rotateAngleY = 0F;
        tap2.rotateAngleZ = 0F;
        tap2.renderWithRotation(par7);

        tub1.rotateAngleX = 0F;
        tub1.rotateAngleY = 0F;
        tub1.rotateAngleZ = 0.7540493F;
        tub1.renderWithRotation(par7);
    }
}
