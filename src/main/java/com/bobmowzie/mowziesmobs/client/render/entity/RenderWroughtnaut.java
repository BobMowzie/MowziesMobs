package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWroughtnaut extends RenderLiving<EntityWroughtnaut> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    public RenderWroughtnaut(RenderManager mgr) {
        super(mgr, new ModelWroughtnaut(), 1.0F);
    }

    @Override
    protected float getDeathMaxRotation(EntityWroughtnaut entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityWroughtnaut entity) {
        return RenderWroughtnaut.TEXTURE;
    }
}
