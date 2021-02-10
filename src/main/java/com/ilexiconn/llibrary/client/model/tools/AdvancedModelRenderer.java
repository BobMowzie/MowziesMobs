package com.ilexiconn.llibrary.client.model.tools;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An enhanced ModelRenderer
 *
 * @author gegy1000
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AdvancedModelRenderer extends ModelRenderer {
    public float defaultRotationX, defaultRotationY, defaultRotationZ;
    public float defaultPositionX, defaultPositionY, defaultPositionZ;
    public float scaleX = 1.0F, scaleY = 1.0F, scaleZ = 1.0F;
    public float opacity = 1.0F;
    public int textureOffsetX, textureOffsetY;
    public boolean scaleChildren;
    private AdvancedModelBase model;
    private AdvancedModelRenderer parent;
    private int displayList;
    private boolean compiled;

    public AdvancedModelRenderer(AdvancedModelBase model) {
        super(model);
        this.model = model;
    }

    public AdvancedModelRenderer(AdvancedModelBase model, int textureOffsetX, int textureOffsetY) {
        super(model, textureOffsetX, textureOffsetY);
        this.model = model;
    }

    /*public AdvancedModelRenderer add3DTexture(float posX, float posY, float posZ, int width, int height) {
        this.cubeList.add(new Model3DTexture(this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, width, height));
        return this;
    }

    @Override
    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
        return this;
    }

    @Override
    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F, mirrored));
        return this;
    }*/

    /**
     * Creates a textured box.
     */
    /*@Override
    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, scaleFactor));
    }*/

    /**
     * If true, when using setScale, the children of this model part will be scaled as well as just this part. If false, just this part will be scaled.
     *
     * @param scaleChildren true if this parent should scale the children
     * @since 1.1.0
     */
    public void setShouldScaleChildren(boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }

    /**
     * Sets the scale for this AdvancedModelRenderer to be rendered at. (Performs a call to GLStateManager.scale()).
     *
     * @param scaleX the x scale
     * @param scaleY the y scale
     * @param scaleZ the z scale
     * @since 1.1.0
     */
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * Sets this ModelRenderer's default pose to the current pose.
     */
    public void updateDefaultPose() {
        this.defaultRotationX = this.rotateAngleX;
        this.defaultRotationY = this.rotateAngleY;
        this.defaultRotationZ = this.rotateAngleZ;

        this.defaultPositionX = this.rotationPointX;
        this.defaultPositionY = this.rotationPointY;
        this.defaultPositionZ = this.rotationPointZ;
    }

    /**
     * Sets the current pose to the previously set default pose.
     */
    public void resetToDefaultPose() {
        this.rotateAngleX = this.defaultRotationX;
        this.rotateAngleY = this.defaultRotationY;
        this.rotateAngleZ = this.defaultRotationZ;

        this.rotationPointX = this.defaultPositionX;
        this.rotationPointY = this.defaultPositionY;
        this.rotationPointZ = this.defaultPositionZ;
    }

    @Override
    public void addChild(ModelRenderer child) {
        super.addChild(child);
        if (child instanceof AdvancedModelRenderer) {
            AdvancedModelRenderer advancedChild = (AdvancedModelRenderer) child;
            advancedChild.setParent(this);
        }
    }

    /**
     * @return the parent of this box
     */
    public AdvancedModelRenderer getParent() {
        return this.parent;
    }

    /**
     * Sets the parent of this box
     *
     * @param parent the new parent
     */
    public void setParent(AdvancedModelRenderer parent) {
        this.parent = parent;
    }

    /**
     * Post renders this box with all its parents
     *
     * @param scale the render scale
     */
//    public void parentedPostRender(float scale) {
//        if (this.parent != null) {
//            this.parent.parentedPostRender(scale);
//        }
//        this.postRender(scale);
//    }

    /**
     * Renders this box with all it's parents
     *
     * @param scale the render scale
     */
//    public void renderWithParents(float scale) {
//        if (this.parent != null) {
//            this.parent.renderWithParents(scale);
//        }
//        this.render(scale);
//    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    /*@Override
    public void render(float scale) {
        if (!this.isHidden) {
            if (this.showModel) {
                GlStateManager.pushMatrix();
                if (!this.compiled) {
                    this.compileDisplayList(scale);
                }
                GlStateManager.translatef(this.offsetX, this.offsetY, this.offsetZ);
                GlStateManager.translatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                }
                if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
                    GlStateManager.scalef(this.scaleX, this.scaleY, this.scaleZ);
                }
                if (this.opacity != 1.0F) {
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    GlStateManager.color4f(1F, 1F, 1F, this.opacity);
                }
                GlStateManager.callList(this.displayList);
                if (this.opacity != 1.0F) {
                    GlStateManager.disableBlend();
                    GlStateManager.color4f(1F, 1F, 1F, 1F);
                }
                if (!this.scaleChildren && (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F)) {
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef(this.offsetX, this.offsetY, this.offsetZ);
                    GlStateManager.translatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    if (this.rotateAngleZ != 0.0F) {
                        GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                    }
                    if (this.rotateAngleY != 0.0F) {
                        GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                    }
                    if (this.rotateAngleX != 0.0F) {
                        GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                    }
                }
                if (this.childModels != null) {
                    for (ModelRenderer childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }*/ // TODO

    /*private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.newList(this.displayList, 4864);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        for (ModelBox box : this.cubeList) {
            box.render(buffer, scale);
        }
        GlStateManager.endList();
        this.compiled = true;
    }*/

    public AdvancedModelBase getModel() {
        return this.model;
    }

    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float movementScale = this.model.getMovementScale();
        float rotation = (MathHelper.cos(f * (speed * movementScale) + offset) * (degree * movementScale) * f1) + (weight * f1);
        return invert ? -rotation : rotation;
    }

    /**
     * Rotates this box back and forth (rotateAngleX). Useful for arms and legs.
     *
     * @param speed      is how fast the animation runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the animation
     * @param weight     will make the animation favor one direction more based on how fast the mob is moving
     * @param walk       is the walked distance
     * @param walkAmount is the walk speed
     */
    public void walk(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        this.rotateAngleX += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * Rotates this box up and down (rotateAngleZ). Useful for wing and ears.
     *
     * @param speed      is how fast the animation runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the animation
     * @param weight     will make the animation favor one direction more based on how fast the mob is moving
     * @param flap       is the flapped distance
     * @param flapAmount is the flap speed
     */
    public void flap(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        this.rotateAngleZ += this.calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /**
     * Rotates this box side to side (rotateAngleY).
     *
     * @param speed       is how fast the animation runs
     * @param degree      is how far the box will rotate;
     * @param invert      will invert the rotation
     * @param offset      will offset the timing of the animation
     * @param weight      will make the animation favor one direction more based on how fast the mob is moving
     * @param swing       is the swung distance
     * @param swingAmount is the swing speed
     */
    public void swing(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        this.rotateAngleY += this.calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    /**
     * Moves this box up and down (rotationPointY). Useful for bodies.
     *
     * @param speed  is how fast the animation runs;
     * @param degree is how far the box will move;
     * @param bounce will make the box bounce;
     * @param f      is the walked distance;
     * @param f1     is the walk speed.
     */
    public void bob(float speed, float degree, boolean bounce, float f, float f1) {
        float movementScale = this.model.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float) (Math.sin(f * speed) * f1 * degree - f1 * degree);
        if (bounce) {
            bob = (float) -Math.abs((Math.sin(f * speed) * f1 * degree));
        }
        this.rotationPointY += bob;
    }

    @Override
    public AdvancedModelRenderer setTextureOffset(int textureOffsetX, int textureOffsetY) {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        return this;
    }

    public void transitionTo(AdvancedModelRenderer to, float timer, float maxTime) {
        this.rotateAngleX += ((to.rotateAngleX - this.rotateAngleX) / maxTime) * timer;
        this.rotateAngleY += ((to.rotateAngleY - this.rotateAngleY) / maxTime) * timer;
        this.rotateAngleZ += ((to.rotateAngleZ - this.rotateAngleZ) / maxTime) * timer;

        this.rotationPointX += ((to.rotationPointX - this.rotationPointX) / maxTime) * timer;
        this.rotationPointY += ((to.rotationPointY - this.rotationPointY) / maxTime) * timer;
        this.rotationPointZ += ((to.rotationPointZ - this.rotationPointZ) / maxTime) * timer;
    }
    
    /**
     * Returns the position of the model renderer in world space.
     */
    public Vec3d getWorldPos(Entity entity, float delta) {
        Vec3d modelPos = getModelPos(this, new Vec3d(rotationPointX/16, -rotationPointY/16, -rotationPointZ/16));
        float x = (float) modelPos.x;
        float y = (float) modelPos.y + 1.5f;
        float z = (float) modelPos.z;
        Matrix4f entityTranslate = new Matrix4f();
        Matrix4f entityRotate = new Matrix4f();
        float dx = (float) (entity.prevPosX + (entity.getPosX() - entity.prevPosX) * delta);
        float dy = (float) (entity.prevPosY + (entity.getPosY() - entity.prevPosY) * delta);
        float dz = (float) (entity.prevPosZ + (entity.getPosZ() - entity.prevPosZ) * delta);
        entityTranslate.setTranslation(dx, dy, dz);
        entityRotate.mul(new Quaternion(new Vector3f(0, 1, 0), (float) -Math.toRadians(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta), false));
        Vector4f rendererPos = new Vector4f(x, y, z, 1);
        rendererPos.transform(entityRotate);
        rendererPos.transform(entityTranslate);
        return new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ());
    }

    public Vec3d getWorldPos(Entity entity) {
        return getWorldPos(entity, 0);
    }

    public Vec3d getModelPos(AdvancedModelRenderer modelRenderer, Vec3d recurseValue) {
        /*double x = recurseValue.x;
        double y = recurseValue.y;
        double z = recurseValue.z;
        Point3d rendererPos = new Point3d(x, y, z);

        AdvancedModelRenderer parent = modelRenderer.getParent();
        if (parent != null) {
            Matrix4f boxTranslate = new Matrix4f();
            Matrix4f boxRotateX = new Matrix4f();
            Matrix4f boxRotateY = new Matrix4f();
            Matrix4f boxRotateZ = new Matrix4f();
            boxTranslate.set(new Vector3d(parent.rotationPointX / 16, -parent.rotationPointY / 16, -parent.rotationPointZ / 16));
            boxRotateX.rotX(parent.rotateAngleX);
            boxRotateY.rotY(-parent.rotateAngleY);
            boxRotateZ.rotZ(-parent.rotateAngleZ);

            boxRotateX.transform(rendererPos);
            boxRotateY.transform(rendererPos);
            boxRotateZ.transform(rendererPos);
            boxTranslate.transform(rendererPos);

            return getModelPos(parent, new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ()));
        }
        return new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ());*/ // TODO
        return new Vec3d(0, 0, 0);
    }

    public Vec3d getWorldRotation(Entity entity, float delta) {
        Vec3d modelRot = getModelRotation(this, new Vec3d(rotateAngleX, -rotateAngleY, -rotateAngleZ));
        double x = modelRot.x;
        double y = modelRot.y;
        double z = modelRot.z;
        y += -Math.toRadians(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta);
        return new Vec3d(x, y, z);
    }

    public Vec3d getModelRotation(AdvancedModelRenderer modelRenderer, Vec3d recurseValue) {
        double x = recurseValue.x;
        double y = recurseValue.y;
        double z = recurseValue.z;

        AdvancedModelRenderer parent = modelRenderer.getParent();
        if (parent != null) {
            x += parent.rotateAngleX;
            y += -parent.rotateAngleY;
            z += -parent.rotateAngleZ;

            return getModelRotation(parent, new Vec3d(x, y, z));
        }
        return new Vec3d(x, y, z);
    }

    public void setWorldPos(Entity entity, Vec3d pos, float delta) {
        /*Matrix4f entityTranslate = new Matrix4f();
        Matrix4f entityRotate = new Matrix4f();
        float dx = (float) (entity.prevPosX + (entity.getPosX() - entity.prevPosX) * delta);
        float dy = (float) (entity.prevPosY + (entity.getPosY() - entity.prevPosY) * delta);
        float dz = (float) (entity.prevPosZ + (entity.getPosZ() - entity.prevPosZ) * delta);
        entityTranslate.set(new Vector3d(dx, dy, dz));
        entityRotate.rotY(-Math.toRadians(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta));
        entityTranslate.invert();
        entityRotate.invert();
        Point3d rendererPos = new Point3d(pos.x, pos.y, pos.z);
        entityTranslate.transform(rendererPos);
        entityRotate.transform(rendererPos);
        rendererPos.y -= 1.5f;
        rendererPos.scale(16);
        setRotationPoint((float)rendererPos.x, -(float)rendererPos.y, -(float)rendererPos.z);*/ //TODO
    }
}
