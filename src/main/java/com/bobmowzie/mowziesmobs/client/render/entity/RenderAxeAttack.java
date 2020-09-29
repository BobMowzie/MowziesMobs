package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderAxeAttack extends EntityRenderer<EntityAxeAttack> {
    private static ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    ModelAxeAttack model;

    public RenderAxeAttack(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelAxeAttack();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAxeAttack entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(EntityAxeAttack entity, double x, double y, double z, float yaw, float delta) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == entity.getCaster() && Minecraft.getInstance().gameSettings.thirdPersonView == 0) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y, z);
            GlStateManager.rotatef(yaw, 0, -1, 0);
            bindTexture(TEXTURE);
            model.render(entity, 0.0625F, delta);
            GlStateManager.popMatrix();
        }
    }
}
