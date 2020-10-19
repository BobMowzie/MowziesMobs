package com.ilexiconn.llibrary.client.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VoxelModel extends EntityModel {
    public RendererModel voxel;

    public VoxelModel() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.voxel = new RendererModel(this, 0, 0);
        this.voxel.setRotationPoint(-3.0F, 18.0F, -3.0F);
        this.voxel.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        this.voxel.render(scale);
    }
}
