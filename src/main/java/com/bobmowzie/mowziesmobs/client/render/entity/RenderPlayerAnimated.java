package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPlayerAnimated;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class RenderPlayerAnimated extends PlayerRenderer {
    public RenderPlayerAnimated(EntityRendererManager renderManager, boolean useSmallArms) {
        super(renderManager, useSmallArms);
        this.layerRenderers.clear();
        this.addLayer(new BipedArmorLayer<>(this, new ModelBipedAnimated(0.5F), new ModelBipedAnimated(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new Deadmau5HeadLayer(this));
        this.addLayer(new CapeLayer(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new ParrotVariantLayer<>(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));
        this.addLayer(new BeeStingerLayer<>(this));

        this.entityModel = new ModelPlayerAnimated(0.0f, useSmallArms);
    }
}
