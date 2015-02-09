package com.bobmowzie.mowziesmobs.client.model.animation.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class MowzieModelRenderer extends ModelRenderer
{
    public float initRotateAngleX;
    public float initRotateAngleY;
    public float initRotateAngleZ;

    public float initRotationPointX;
    public float initRotationPointY;
    public float initRotationPointZ;

    public MowzieModelRenderer(ModelBase modelBase, String name)
    {
        super(modelBase, name);
    }

    public MowzieModelRenderer(ModelBase modelBase, int x, int y)
    {
        super(modelBase, x, y);
    }

    public MowzieModelRenderer(ModelBase modelBase)
    {
        super(modelBase);
    }

    public void setInitValuesToCurrentPose()
    {
        initRotateAngleX = rotateAngleX;
        initRotateAngleY = rotateAngleY;
        initRotateAngleZ = rotateAngleZ;

        initRotationPointX = rotationPointX;
        initRotationPointY = rotationPointY;
        initRotationPointZ = rotationPointZ;
    }

    public void setCurrentPoseToInitValues()
    {
        rotateAngleX = initRotateAngleX;
        rotateAngleY = initRotateAngleY;
        rotateAngleZ = initRotateAngleZ;

        rotationPointX = initRotationPointX;
        rotationPointY = initRotationPointY;
        rotationPointZ = initRotationPointZ;
    }

    public void setRotationAngles(float x, float y, float z)
    {
        rotateAngleX = x;
        rotateAngleY = y;
        rotateAngleZ = z;
    }

    /**
     * Resets all rotation points.
     */
    public void resetAllRotationPoints()
    {
        this.rotationPointX = this.initRotationPointX;
        this.rotationPointY = this.initRotationPointY;
        this.rotationPointZ = this.initRotationPointZ;
    }

    /**
     * Resets X rotation point.
     */
    public void resetXRotationPoints()
    {
        this.rotationPointX = this.initRotationPointX;
    }

    /**
     * Resets Y rotation point.
     */
    public void resetYRotationPoints()
    {
        this.rotationPointY = this.initRotationPointY;
    }

    /**
     * Resets Z rotation point.
     */
    public void resetZRotationPoints()
    {
        this.rotationPointZ = this.initRotationPointZ;
    }

    /**
     * Resets all rotations.
     */
    public void resetAllRotations()
    {
        this.rotateAngleX = this.initRotateAngleX;
        this.rotateAngleY = this.initRotateAngleY;
        this.rotateAngleZ = this.initRotateAngleZ;
    }

    /**
     * Resets X rotation.
     */
    public void resetXRotations()
    {
        this.rotateAngleX = this.initRotateAngleX;
    }

    /**
     * Resets Y rotation.
     */
    public void resetYRotations()
    {
        this.rotateAngleY = this.initRotateAngleY;
    }

    /**
     * Resets Z rotation.
     */
    public void resetZRotations()
    {
        this.rotateAngleZ = this.initRotateAngleZ;
    }

    /**
     * Copies the rotation point coordinates.
     */
    public void copyAllRotationPoints(MowzieModelRenderer target)
    {
        this.rotationPointX = target.rotationPointX;
        this.rotationPointY = target.rotationPointY;
        this.rotationPointZ = target.rotationPointZ;
    }

    /**
     * Copies X rotation point.
     */
    public void copyXRotationPoint(MowzieModelRenderer target)
    {
        this.rotationPointX = target.rotationPointX;
    }

    /**
     * Copies Y rotation point.
     */
    public void copyYRotationPoint(MowzieModelRenderer target)
    {
        this.rotationPointY = target.rotationPointY;
    }

    /**
     * Copies Z rotation point.
     */
    public void copyZRotationPoint(MowzieModelRenderer target)
    {
        this.rotationPointZ = target.rotationPointZ;
    }
}