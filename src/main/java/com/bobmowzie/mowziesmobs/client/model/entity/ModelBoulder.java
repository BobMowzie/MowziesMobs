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

    public BlockModelRenderer[] blockModels;

    public ModelBoulder() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        boulder0block1 = new BlockModelRenderer(this);
        boulder0block1.setRotationPoint(-8F, -8F, -8F);
        boulder0block1.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);
        boulder1 = new AdvancedModelRenderer(this);
        boulder1.setRotationPoint(-8F, -8F, -8F);
        boulder1block1 = new BlockModelRenderer(this);
        boulder1block1.setRotationPoint(0, 0, 0);
        boulder0block1.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);
        boulder1block2 = new BlockModelRenderer(this);
        boulder1block2.setRotationPoint(0, -10, 0);
        boulder1block2.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);
        boulder1block2.setScale(0.99f, 1, 0.99f);
        boulder1block3 = new BlockModelRenderer(this);
        boulder1block3.setRotationPoint(0, -0.01f, 8);
        boulder1block3.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);
        boulder1block4 = new BlockModelRenderer(this);
        boulder1block4.setRotationPoint(8, -0.01f, 0);
        boulder1block4.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);
        boulder1block5 = new BlockModelRenderer(this);
        boulder1block5.setRotationPoint(-8, -0.01f, 0);
        boulder1block5.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);
        boulder1block6 = new BlockModelRenderer(this);
        boulder1block6.setRotationPoint(0, -0.01f, -8);
        boulder1block6.addBox(0F, 8F, 0F, 0, 0, 0, 0.0F);

        boulder1.addChild(boulder1block1);
        boulder1.addChild(boulder1block2);
        boulder1.addChild(boulder1block3);
        boulder1.addChild(boulder1block4);
        boulder1.addChild(boulder1block5);
        boulder1.addChild(boulder1block6);

        blockModels = new BlockModelRenderer[]{boulder0block1, boulder1block1, boulder1block2, boulder1block3, boulder1block4, boulder1block5, boulder1block6};

    }

    public void render(EntityBoulder entity, float f5, float delta) {
        setRotationAngles(entity, f5, delta);
        if (entity.getBoulderSize() == 0) boulder0block1.render(f5);
        else  boulder1.render(f5);
    }

    public void setRotationAngles(EntityBoulder entity, float f5, float delta) {
        int tick = Math.max(entity.ticksExisted, 0);
        for (int i = 0; i < blockModels.length; i++) {
            blockModels[i].setBlockState(entity.getBlock());
            blockModels[i].setBiome(entity.world.getBiome(entity.getOrigin()));
            blockModels[i].setEntity(entity);
            blockModels[i].setOrigin(entity.getOrigin());
        }
        boulder0block1.rotationPointY = (float)(-8 - 4 * Math.pow(0.25f * (tick + delta), -4f) + 2 * Math.cos(0.1f * (entity.ticksExisted + entity.animationOffset + delta)));
    }
}
