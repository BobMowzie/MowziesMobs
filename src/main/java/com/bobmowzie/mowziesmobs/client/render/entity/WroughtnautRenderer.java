package com.bobmowzie.mowziesmobs.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.WroughtnautModel;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;

@SideOnly(Side.CLIENT)
public class WroughtnautRenderer extends RenderLiving<EntityWroughtnaut> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureWroughtnaut.png");

    public WroughtnautRenderer(RenderManager mgr) {
        super(mgr, new WroughtnautModel(), 1.0F);
    }

    @Override
    protected float getDeathMaxRotation(EntityWroughtnaut entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityWroughtnaut entity) {
        return WroughtnautRenderer.TEXTURE;
    }
}
