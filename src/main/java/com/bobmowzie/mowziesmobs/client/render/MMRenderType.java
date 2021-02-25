package com.bobmowzie.mowziesmobs.client.render;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MMRenderType extends RenderType {

    public MMRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getLantern(ResourceLocation locationIn) {
        RenderType.State rendertype$state = RenderType.State.getBuilder().texture(new RenderState.TextureState(locationIn, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_ENABLED).build(true);
        return makeType("lantern", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
    }

    public static RenderType getGlowingEffect(ResourceLocation locationIn) {
        RenderType.State rendertype$state = RenderType.State.getBuilder().texture(new RenderState.TextureState(locationIn, false, false)).transparency(RenderState.TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_DISABLED).build(true);
        return makeType("solar_flare", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
    }

    public static RenderType getSolarFlare(ResourceLocation locationIn) {
        RenderType.State rendertype$state = RenderType.State.getBuilder().texture(new RenderState.TextureState(locationIn, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).depthTest(DEPTH_ALWAYS).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_DISABLED).build(true);
        return makeType("solar_flare", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
    }
}
