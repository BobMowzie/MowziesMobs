package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.BlockModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import org.lwjgl.Sys;

/**
 * Created by Josh on 4/14/2017.
 */
public class ModelBoulder extends AdvancedModelBase {

    public BlockModelRenderer boulder0block1;
    public AdvancedModelRenderer boulder1;
    public BlockModelRenderer boulder1block1;
    public BlockModelRenderer boulder1block2;
    public BlockModelRenderer boulder1block3;
    public BlockModelRenderer boulder1block4;
    public BlockModelRenderer boulder1block5;
    public BlockModelRenderer boulder1block6;
    public AdvancedModelRenderer boulder2;
    public BlockModelRenderer boulder2block1;
    public BlockModelRenderer boulder2block2;
    public BlockModelRenderer boulder2block3;
    public BlockModelRenderer boulder2block4;
    public BlockModelRenderer boulder2block5;
    public BlockModelRenderer boulder2block6;
    public BlockModelRenderer boulder2block7;
    public BlockModelRenderer boulder2block8;
    public BlockModelRenderer boulder2block9;
    public BlockModelRenderer boulder2block10;
    public BlockModelRenderer boulder2block11;
    public BlockModelRenderer boulder2block12;
    public BlockModelRenderer boulder2block13;
    public BlockModelRenderer boulder2block14;
    public BlockModelRenderer boulder2block15;
    public BlockModelRenderer boulder2block16;

    public BlockModelRenderer[] blockModels;

    public ModelBoulder() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        boulder0block1 = new BlockModelRenderer(this);
        boulder0block1.setRotationPoint(0F, -8F, 0F);
        boulder0block1.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        boulder1 = new AdvancedModelRenderer(this);
        boulder1.setRotationPoint(0F, 0F, 0F);
        boulder1block1 = new BlockModelRenderer(this);
        boulder1block1.setRotationPoint(0, 0, 0);
        boulder0block1.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        boulder1block2 = new BlockModelRenderer(this);
        boulder1block2.setRotationPoint(0, -10, 0);
        boulder1block2.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        boulder1block2.setScale(0.99f, 1, 0.99f);
        boulder1block3 = new BlockModelRenderer(this);
        boulder1block3.setRotationPoint(0, -0.01f, 8);
        boulder1block3.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        boulder1block4 = new BlockModelRenderer(this);
        boulder1block4.setRotationPoint(8, -0.01f, 0);
        boulder1block4.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        boulder1block5 = new BlockModelRenderer(this);
        boulder1block5.setRotationPoint(-8, -0.01f, 0);
        boulder1block5.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        boulder1block6 = new BlockModelRenderer(this);
        boulder1block6.setRotationPoint(0, -0.01f, -8);
        boulder1block6.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        
        this.boulder2block9 = new BlockModelRenderer(this);
        this.boulder2block9.setRotationPoint(10.0F, 16.0F, -10.0F);
        this.boulder2block9.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block15 = new BlockModelRenderer(this);
        this.boulder2block15.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.boulder2block15.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block3 = new BlockModelRenderer(this);
        this.boulder2block3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.boulder2block3.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block5 = new BlockModelRenderer(this);
        this.boulder2block5.setRotationPoint(-16.0F, 16.0F, 0.0F);
        this.boulder2block5.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block12 = new BlockModelRenderer(this);
        this.boulder2block12.setRotationPoint(-10.0F, 16.0F, -10.0F);
        this.boulder2block12.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block16 = new BlockModelRenderer(this);
        this.boulder2block16.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.boulder2block16.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block4 = new BlockModelRenderer(this);
        this.boulder2block4.setRotationPoint(16.0F, 16.0F, 0.0F);
        this.boulder2block4.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block8 = new BlockModelRenderer(this);
        this.boulder2block8.setRotationPoint(10.0F, 16.0F, 10.0F);
        this.boulder2block8.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2 = new AdvancedModelRenderer(this);
        this.boulder2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder2block10 = new BlockModelRenderer(this);
        this.boulder2block10.setRotationPoint(-10.0F, 16.0F, -10.0F);
        this.boulder2block10.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block7 = new BlockModelRenderer(this);
        this.boulder2block7.setRotationPoint(0.0F, 16.0F, 16.0F);
        this.boulder2block7.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block6 = new BlockModelRenderer(this);
        this.boulder2block6.setRotationPoint(0.0F, 16.0F, -16.0F);
        this.boulder2block6.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block11 = new BlockModelRenderer(this);
        this.boulder2block11.setRotationPoint(-10.0F, 16.0F, 10.0F);
        this.boulder2block11.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block1 = new BlockModelRenderer(this);
        this.boulder2block1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.boulder2block1.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block14 = new BlockModelRenderer(this);
        this.boulder2block14.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.boulder2block14.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block13 = new BlockModelRenderer(this);
        this.boulder2block13.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.boulder2block13.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block2 = new BlockModelRenderer(this);
        this.boulder2block2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder2block2.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2.addChild(this.boulder2block9);
        this.boulder2.addChild(this.boulder2block15);
        this.boulder2.addChild(this.boulder2block3);
        this.boulder2.addChild(this.boulder2block5);
        this.boulder2.addChild(this.boulder2block12);
        this.boulder2.addChild(this.boulder2block16);
        this.boulder2.addChild(this.boulder2block4);
        this.boulder2.addChild(this.boulder2block8);
        this.boulder2.addChild(this.boulder2block10);
        this.boulder2.addChild(this.boulder2block7);
        this.boulder2.addChild(this.boulder2block6);
        this.boulder2.addChild(this.boulder2block11);
        this.boulder2.addChild(this.boulder2block1);
        this.boulder2.addChild(this.boulder2block14);
        this.boulder2.addChild(this.boulder2block13);
        this.boulder2.addChild(this.boulder2block2);

        boulder1.addChild(boulder1block1);
        boulder1.addChild(boulder1block2);
        boulder1.addChild(boulder1block3);
        boulder1.addChild(boulder1block4);
        boulder1.addChild(boulder1block5);
        boulder1.addChild(boulder1block6);

        blockModels = new BlockModelRenderer[]{boulder0block1, boulder1block1, boulder1block2, boulder1block3, boulder1block4, boulder1block5, boulder1block6, boulder2block1, boulder2block2, boulder2block3, boulder2block4, boulder2block5, boulder2block6, boulder2block7, boulder2block8, boulder2block9, boulder2block10, boulder2block11, boulder2block12, boulder2block13, boulder2block14, boulder2block15, boulder2block16};
        for (BlockModelRenderer blockModel: blockModels) {
            float scale = (float) (Math.random() * 0.01f - 0.005f);
            blockModel.setScale(1 + scale, 1 + scale, 1 + scale);
        }
        updateDefaultPose();
    }

    public void render(EntityBoulder entity, float f5, float delta) {
        setRotationAngles(entity, f5, delta);
        int size = entity.getBoulderSize();
        if (size == 0) boulder0block1.render(f5);
        else  if (size == 1) boulder1.render(f5);
        else boulder2.render(f5);
    }

    public void setRotationAngles(EntityBoulder entity, float f5, float delta) {
        resetToDefaultPose();
        int tick = Math.max(entity.ticksExisted, 0);
        for (int i = 0; i < blockModels.length; i++) {
            blockModels[i].setBlockState(entity.getBlock());
            blockModels[i].setBiome(entity.world.getBiome(entity.getOrigin()));
            blockModels[i].setEntity(entity);
            blockModels[i].setOrigin(entity.getOrigin());
        }
        boulder0block1.rotationPointY += -32 * (float)(Math.pow(0.6 * (tick + delta + 1), -3));
        boulder0block1.rotationPointY += 2 * Math.cos(0.1f * (entity.ticksExisted + entity.animationOffset + delta));

        boulder1.rotationPointY += -32 * (float)(Math.pow(0.2 * (tick + delta + 1), -3));
        boulder1.rotationPointY += 2.4 * Math.cos(0.07f * (entity.ticksExisted + entity.animationOffset + delta));

        boulder2.rotationPointY += -8 * (float)(Math.pow(0.05 * (tick + delta + 1), -1));
        boulder2.rotationPointY += 2.8 * Math.cos(0.04f * (entity.ticksExisted + entity.animationOffset + delta));
    }
}
