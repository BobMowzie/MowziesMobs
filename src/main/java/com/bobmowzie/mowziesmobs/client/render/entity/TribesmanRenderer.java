package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeElite;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribesman;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TribesmanRenderer extends RenderLiving {
    private static final ResourceLocation ELITE_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman1.png");
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman2.png");
    private static final ResourceLocation TEXTURE3 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman3.png");
    private static final ResourceLocation TEXTURE4 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman4.png");
    private static final ResourceLocation TEXTURE5 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman5.png");

    public TribesmanRenderer(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof EntityTribeElite) {
            return TribesmanRenderer.ELITE_TEXTURE;
        }
        EntityTribesman tribesman = (EntityTribesman) entity;
        if (tribesman.getMask() == 2) {
            return TribesmanRenderer.TEXTURE2;
        }
        if (tribesman.getMask() == 3) {
            return TribesmanRenderer.TEXTURE3;
        }
        if (tribesman.getMask() == 4) {
            return TribesmanRenderer.TEXTURE4;
        }
        if (tribesman.getMask() == 5) {
            return TribesmanRenderer.TEXTURE5;
        }
        return TribesmanRenderer.TEXTURE2;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity) {
        return 0;
    }
}
