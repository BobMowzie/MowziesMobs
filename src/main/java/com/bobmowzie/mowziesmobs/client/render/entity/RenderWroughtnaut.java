package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.WroughtnautEyesLayer;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderWroughtnaut extends MobRenderer<EntityWroughtnaut, ModelWroughtnaut<EntityWroughtnaut>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    public RenderWroughtnaut(EntityRendererManager mgr) {
        super(mgr, new ModelWroughtnaut<>(), 1.0F);
        addLayer(new WroughtnautEyesLayer<>(this));
        addLayer(new ItemLayer<>(this, getEntityModel().sword, Items.DIAMOND_SWORD.getDefaultInstance()));
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
