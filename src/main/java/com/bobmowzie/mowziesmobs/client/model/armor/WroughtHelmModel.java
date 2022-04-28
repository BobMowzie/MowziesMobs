package com.bobmowzie.mowziesmobs.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class WroughtHelmModel<T extends LivingEntity> extends HumanoidModel<T> {
    private final ModelPart shape1;
    private final ModelPart tuskRight1;
    private final ModelPart tuskRight2;
    private final ModelPart hornRight1;
    private final ModelPart hornLeft;
    private final ModelPart tuskLeft1;
    private final ModelPart tuskLeft2;
    private final ModelPart hornLeft1;
    private final ModelPart hornRight;

    public WroughtHelmModel() {
        super(0.0f);
//        this.texWidth = 64;
//        this.texHeight = 32;
        head.cubes.clear();

        shape1 = new ModelPart(this);
        shape1.setPos(0.0F, 0.0F, 0.0F);
        shape1.texOffs(0, 12).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);

        tuskRight1 = new ModelPart(this);
        tuskRight1.setPos(-2.5F, -1.5F, -2.5F);
        setRotationAngle(tuskRight1, 0.4363F, 0.7854F, 0.0F);
        tuskRight1.texOffs(40, 24).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        tuskRight2 = new ModelPart(this);
        tuskRight2.setPos(1.0F, 1.5F, -5.0F);
        tuskRight1.addChild(tuskRight2);
        setRotationAngle(tuskRight2, -0.8727F, 0.0F, 0.0F);
        tuskRight2.texOffs(34, 4).addBox(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

        hornRight1 = new ModelPart(this);
        hornRight1.setPos(3.0F, -8.0F, -3.0F);
        setRotationAngle(hornRight1, -0.3491F, -0.7854F, 0.0F);
        hornRight1.texOffs(8, 3).addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, 0.0F, true);

        hornLeft = new ModelPart(this);
        hornLeft.setPos(-1.0F, 1.5F, -6.0F);
        hornRight1.addChild(hornLeft);
        setRotationAngle(hornLeft, -1.2217F, 0.0F, 0.0F);
        hornLeft.texOffs(43, 12).addBox(0.0F, -2.0F, -6.0F, 2.0F, 2.0F, 6.0F, 0.0F, true);

        tuskLeft1 = new ModelPart(this);
        tuskLeft1.setPos(2.5F, -1.5F, -2.5F);
        setRotationAngle(tuskLeft1, 0.4363F, -0.7854F, 0.0F);
        tuskLeft1.texOffs(40, 24).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        tuskLeft2 = new ModelPart(this);
        tuskLeft2.setPos(1.0F, 1.5F, -5.0F);
        tuskLeft1.addChild(tuskLeft2);
        setRotationAngle(tuskLeft2, -0.8727F, 0.0F, 0.0F);
        tuskLeft2.texOffs(34, 4).addBox(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

        hornLeft1 = new ModelPart(this);
        hornLeft1.setPos(-3.0F, -8.0F, -3.0F);
        setRotationAngle(hornLeft1, -0.3491F, 0.7854F, 0.0F);
        hornLeft1.texOffs(8, 3).addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, 0.0F, true);

        hornRight = new ModelPart(this);
        hornRight.setPos(-1.0F, 1.5F, -6.0F);
        hornLeft1.addChild(hornRight);
        setRotationAngle(hornRight, -1.2217F, 0.0F, 0.0F);
        hornRight.texOffs(30, 12).addBox(0.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);

        this.head.addChild(this.shape1);
        this.head.addChild(this.hornLeft1);
        this.head.addChild(this.hornRight1);
        this.head.addChild(this.tuskLeft1);
        this.head.addChild(this.tuskRight1);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
