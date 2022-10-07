package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBoulder;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.BlockLayer;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.TreeMap;

@OnlyIn(Dist.CLIENT)
public class RenderPillar extends EntityRenderer<EntityPillar> {
    private static final ResourceLocation TEXTURE_DIRT = new ResourceLocation("textures/blocks/dirt.png");

    public RenderPillar(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPillar entity) {
        return TEXTURE_DIRT;
    }
}
