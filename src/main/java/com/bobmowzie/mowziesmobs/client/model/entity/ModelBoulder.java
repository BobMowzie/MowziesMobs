package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.BlockModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

import java.util.Random;

/**
 * Created by Josh on 4/14/2017.
 */
public class ModelBoulder<T extends EntityBoulder> extends AdvancedModelBase {

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

    public AdvancedModelRenderer boulder3;
    public BlockModelRenderer boulder3block1;
    public BlockModelRenderer boulder3block2;
    public BlockModelRenderer boulder3block3;
    public BlockModelRenderer boulder3block4;
    public BlockModelRenderer boulder3block5;
    public BlockModelRenderer boulder3block6;
    public BlockModelRenderer boulder3block7;
    public BlockModelRenderer boulder3block8;
    public BlockModelRenderer boulder3block9;
    public BlockModelRenderer boulder3block10;
    public BlockModelRenderer boulder3block11;
    public BlockModelRenderer boulder3block12;
    public BlockModelRenderer boulder3block13;
    public BlockModelRenderer boulder3block14;
    public BlockModelRenderer boulder3block15;
    public BlockModelRenderer boulder3block16;
    public BlockModelRenderer boulder3block17;
    public BlockModelRenderer boulder3block18;
    public BlockModelRenderer boulder3block19;
    public BlockModelRenderer boulder3block20;
    public BlockModelRenderer boulder3block21;
    public BlockModelRenderer boulder3block22;
    public BlockModelRenderer boulder3block23;
    public BlockModelRenderer boulder3block24;
    public BlockModelRenderer boulder3block25;
    public BlockModelRenderer boulder3block26;
    public BlockModelRenderer boulder3block27;
    public BlockModelRenderer boulder3block28;

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

        this.boulder3block9 = new BlockModelRenderer(this);
        this.boulder3block9.setRotationPoint(10.0F, 16.0F, -10.0F);
        this.boulder3block9.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block15 = new BlockModelRenderer(this);
        this.boulder3block15.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.boulder3block15.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block3 = new BlockModelRenderer(this);
        this.boulder3block3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.boulder3block3.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block5 = new BlockModelRenderer(this);
        this.boulder3block5.setRotationPoint(-16.0F, 16.0F, 0.0F);
        this.boulder3block5.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block16 = new BlockModelRenderer(this);
        this.boulder3block16.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.boulder3block16.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block4 = new BlockModelRenderer(this);
        this.boulder3block4.setRotationPoint(16.0F, 16.0F, 0.0F);
        this.boulder3block4.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block8 = new BlockModelRenderer(this);
        this.boulder3block8.setRotationPoint(10.0F, 16.0F, 10.0F);
        this.boulder3block8.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3 = new AdvancedModelRenderer(this);
        this.boulder3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder3block10 = new BlockModelRenderer(this);
        this.boulder3block10.setRotationPoint(-10.0F, 16.0F, -10.0F);
        this.boulder3block10.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block7 = new BlockModelRenderer(this);
        this.boulder3block7.setRotationPoint(0.0F, 16.0F, 16.0F);
        this.boulder3block7.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block6 = new BlockModelRenderer(this);
        this.boulder3block6.setRotationPoint(0.0F, 16.0F, -16.0F);
        this.boulder3block6.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block11 = new BlockModelRenderer(this);
        this.boulder3block11.setRotationPoint(-10.0F, 16.0F, 10.0F);
        this.boulder3block11.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block12 = new BlockModelRenderer(this);
        this.boulder3block12.setRotationPoint(0.0F, -16.0F, 0.0F);
        this.boulder3block12.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block1 = new BlockModelRenderer(this);
        this.boulder3block1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.boulder3block1.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block14 = new BlockModelRenderer(this);
        this.boulder3block14.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.boulder3block14.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block13 = new BlockModelRenderer(this);
        this.boulder3block13.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.boulder3block13.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block2 = new BlockModelRenderer(this);
        this.boulder3block2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder3block2.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block17 = new BlockModelRenderer(this);
        this.boulder3block17.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.boulder3block17.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block18 = new BlockModelRenderer(this);
        this.boulder3block18.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.boulder3block18.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block19 = new BlockModelRenderer(this);
        this.boulder3block19.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.boulder3block19.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block20 = new BlockModelRenderer(this);
        this.boulder3block20.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.boulder3block20.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block21 = new BlockModelRenderer(this);
        this.boulder3block21.setRotationPoint(8.0F, 0.0F, 8.0F);
        this.boulder3block21.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block22 = new BlockModelRenderer(this);
        this.boulder3block22.setRotationPoint(-8.0F, 0.0F, 8.0F);
        this.boulder3block22.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block23 = new BlockModelRenderer(this);
        this.boulder3block23.setRotationPoint(-8.0F, 0.0F, -8.0F);
        this.boulder3block23.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block24 = new BlockModelRenderer(this);
        this.boulder3block24.setRotationPoint(8.0F, 0.0F, -8.0F);
        this.boulder3block24.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block25 = new BlockModelRenderer(this);
        this.boulder3block25.setRotationPoint(16.0F, 16.0F, 0.0F);
        this.boulder3block25.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block26 = new BlockModelRenderer(this);
        this.boulder3block26.setRotationPoint(-16.0F, 16.0F, 0.0F);
        this.boulder3block26.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block27 = new BlockModelRenderer(this);
        this.boulder3block27.setRotationPoint(0.0F, 16.0F, -16.0F);
        this.boulder3block27.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block28 = new BlockModelRenderer(this);
        this.boulder3block28.setRotationPoint(0.0F, 16.0F, 16.0F);
        this.boulder3block28.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        
        this.boulder3.addChild(this.boulder3block9);
        this.boulder3.addChild(this.boulder3block15);
        this.boulder3.addChild(this.boulder3block3);
        this.boulder3.addChild(this.boulder3block5);
        this.boulder3.addChild(this.boulder3block16);
        this.boulder3.addChild(this.boulder3block4);
        this.boulder3.addChild(this.boulder3block8);
        this.boulder3.addChild(this.boulder3block10);
        this.boulder3.addChild(this.boulder3block7);
        this.boulder3.addChild(this.boulder3block6);
        this.boulder3.addChild(this.boulder3block11);
        this.boulder3.addChild(this.boulder3block1);
        this.boulder3.addChild(this.boulder3block14);
        this.boulder3.addChild(this.boulder3block12);
        this.boulder3.addChild(this.boulder3block13);
        this.boulder3.addChild(this.boulder3block2);
        this.boulder3.addChild(this.boulder3block17);
        this.boulder3.addChild(this.boulder3block18);
        this.boulder3.addChild(this.boulder3block19);
        this.boulder3.addChild(this.boulder3block20);
        this.boulder3.addChild(this.boulder3block21);
        this.boulder3.addChild(this.boulder3block22);
        this.boulder3.addChild(this.boulder3block23);
        this.boulder3.addChild(this.boulder3block24);
        this.boulder3.addChild(this.boulder3block25);
        this.boulder3.addChild(this.boulder3block26);
        this.boulder3.addChild(this.boulder3block27);
        this.boulder3.addChild(this.boulder3block28);

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

        blockModels = new BlockModelRenderer[]{boulder0block1, boulder1block1, boulder1block2, boulder1block3, boulder1block4, boulder1block5, boulder1block6, boulder2block1, boulder2block2, boulder2block3, boulder2block4, boulder2block5, boulder2block6, boulder2block7, boulder2block8, boulder2block9, boulder2block10, boulder2block11, boulder2block12, boulder2block13, boulder2block14, boulder2block15, boulder2block16,
            boulder3block1, boulder3block2, boulder3block3, boulder3block4, boulder3block5, boulder3block6, boulder3block7, boulder3block8, boulder3block9, boulder3block10, boulder3block11, boulder3block12, boulder3block13, boulder3block14, boulder3block15, boulder3block16, boulder3block17, boulder3block18, boulder3block19, boulder3block20, boulder3block21, boulder3block22, boulder3block23, boulder3block24, boulder3block25, boulder3block26, boulder3block27, boulder3block28
        };
        Random rng = new Random(0x11c08b85b1943001L);
        for (BlockModelRenderer blockModel: blockModels) {
            float scale = rng.nextFloat() * 0.01f - 0.005f;
            blockModel.setScale(1 + scale, 1 + scale, 1 + scale);
        }
        updateDefaultPose();
    }

    public void render(EntityBoulder entity, float f5, float delta) {
        setRotationAngles(entity, f5, delta);
        EntityBoulder.BoulderSizeEnum size = entity.getBoulderSize();
        if (size == EntityBoulder.BoulderSizeEnum.SMALL) boulder0block1.render(f5);
        else  if (size == EntityBoulder.BoulderSizeEnum.MEDIUM) boulder1.render(f5);
        else  if (size == EntityBoulder.BoulderSizeEnum.LARGE) boulder2.render(f5);
        else boulder3.render(f5);
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

        boulder3.rotationPointY += -90 + Math.min(90, 1.2 * (tick + delta));
        boulder3.rotationPointY += 3.2 * Math.cos(0.03f * (entity.ticksExisted + entity.animationOffset + delta));

        boulder3.rotationPointY += 16;
        boulder3block1.rotationPointY -= 8;
        boulder3block2.rotationPointY -= 8;
        boulder3block4.rotationPointX += 8;
        boulder3block5.rotationPointX -= 8;
        boulder3block6.rotationPointZ -= 8;
        boulder3block7.rotationPointZ += 8;
        boulder3block8.rotationPointX += 6;
        boulder3block8.rotationPointZ += 6;
        boulder3block9.rotationPointX += 6;
        boulder3block9.rotationPointZ -= 6;
        boulder3block10.rotationPointX -= 6;
        boulder3block10.rotationPointZ -= 6;
        boulder3block11.rotationPointX -= 6;
        boulder3block11.rotationPointZ += 6;
        boulder3block12.rotationPointY -= 8;
        boulder3block13.rotationPointZ -= 8;
        boulder3block14.rotationPointX -= 8;
        boulder3block15.rotationPointX += 8;
        boulder3block16.rotationPointZ += 8;
        boulder3block17.rotationPointY -= 16;
        boulder3block18.rotationPointY -= 16;
        boulder3block19.rotationPointY -= 16;
        boulder3block20.rotationPointY -= 16;
    }
}
