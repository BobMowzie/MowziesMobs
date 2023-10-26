package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.item.RenderUmvuthanaMaskArmor;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.List;
import java.util.stream.Collectors;

// From https://github.com/Alex-the-666/AlexsMobs/blob/1.19/src/main/java/com/github/alexthe666/alexsmobs/client/ClientLayerRegistry.java
@OnlyIn(Dist.CLIENT)
public class ClientLayerRegistry {
    @SubscribeEvent@OnlyIn(Dist.CLIENT)
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(
                ForgeRegistries.ENTITIES.getValues().stream()
                        .filter(DefaultAttributes::hasSupplier)
                        .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                        .collect(Collectors.toList()));
        entityTypes.forEach((entityType -> {
            addLayerIfApplicable(entityType, event);
        }));
        for (String skinType : event.getSkins()){
            event.getSkin(skinType).addLayer(new FrozenRenderHandler.LayerFrozen(event.getSkin(skinType)));
            event.getSkin(skinType).addLayer(new SunblockLayer(event.getSkin(skinType)));
        }

        GeoArmorRenderer.registerArmorRenderer(ItemUmvuthanaMask.class, () -> new RenderUmvuthanaMaskArmor());

        GeckoPlayer.GeckoPlayerThirdPerson.initRenderer();
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = null;
        if(entityType != EntityType.ENDER_DRAGON){
            try{
                renderer = event.getRenderer(entityType);
            }catch (Exception e){
                if (!entityType.getBaseClass().isAssignableFrom(MowzieEntity.class)) {
                    MowziesMobs.LOGGER.warn("Could not apply layer to " + entityType.getRegistryName() + ", has custom renderer that is not LivingEntityRenderer.");
                }
            }
            if(renderer != null){
                renderer.addLayer(new FrozenRenderHandler.LayerFrozen(renderer));
                renderer.addLayer(new SunblockLayer(renderer));
            }
        }
    }
}
