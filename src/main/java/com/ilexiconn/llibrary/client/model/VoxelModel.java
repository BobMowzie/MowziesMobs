package com.ilexiconn.llibrary.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VoxelModel extends ModelBase {
    public ModelRenderer voxel;

    public VoxelModel() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.voxel = new ModelRenderer(this, 0, 0);
        this.voxel.setRotationPoint(-3.0F, 18.0F, -3.0F);
        this.voxel.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        this.voxel.render(scale);
    }
}
