package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.BlockModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Josh on 4/14/2017.
 */
public class ModelBoulder extends AdvancedModelBase {

    public BlockModelRenderer small1;

    public ModelBoulder() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        small1 = new BlockModelRenderer(this);
        small1.setRotationPoint(0F, 0F, 0F);
    }

    public void render(EntityBoulder entity, float f5, float delta) {
        setRotationAngles(entity, f5, delta);
        small1.render(f5);
    }

    public void setRotationAngles(EntityBoulder entity, float f5, float delta) {
        int tick = Math.max(entity.ticksExisted, 0);
        small1.setBlockState(entity.getBlock());
        small1.setBiome(entity.world.getBiome(entity.getOrigin()));
        small1.setEntity(entity);
        small1.setOrigin(entity.getOrigin());
        small1.rotationPointY = (float)(-8 - 4 * Math.pow(0.25f * (tick + delta), -4f) + 2 * Math.cos(0.1f * (entity.ticksExisted + entity.animationOffset + delta)));
    }
}
