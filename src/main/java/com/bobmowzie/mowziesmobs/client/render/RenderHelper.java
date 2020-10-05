package com.bobmowzie.mowziesmobs.client.render;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Josh on 7/14/2017.
 */
public class RenderHelper {
    private static Field fieldLayerRenderers = ObfuscationReflectionHelper.findField(LivingRenderer.class, "layerRenderers");

    @SuppressWarnings("unchecked")
    public static <T extends LayerRenderer<?, ?>> T getRenderLayer(LivingRenderer<?, ?> renderer, Class<T> cls, boolean subclasses) {
        try {
            List<LayerRenderer<?, ?>> layers = (List<LayerRenderer<?, ?>>) fieldLayerRenderers.get(renderer);
            for(LayerRenderer<?, ?> layer : layers) {
                if(subclasses ? cls.isInstance(layer) : cls == layer.getClass()) {
                    return (T) layer;
                }
            }
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public static boolean doesRendererHaveLayer(LivingRenderer<?, ?> renderer, Class<? extends LayerRenderer<?, ?>> cls, boolean subclasses) {
        return getRenderLayer(renderer, cls, subclasses) != null;
    }
}
