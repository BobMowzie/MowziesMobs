package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class MMModels {
    public static final String[] HAND_MODEL_ITEMS = new String[] {"wrought_axe", "spear", "earthbore_gauntlet", "sculptor_staff"};

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        Map<ResourceLocation, BakedModel> map = event.getModelRegistry();

        for (String item : HAND_MODEL_ITEMS) {
            ResourceLocation modelInventory = new ModelResourceLocation("mowziesmobs:" + item, "inventory");
            ResourceLocation modelHand = new ModelResourceLocation("mowziesmobs:" + item + "_in_hand", "inventory");

            BakedModel bakedModelDefault = map.get(modelInventory);
            BakedModel bakedModelHand = map.get(modelHand);
            BakedModel modelWrapper = new BakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                    return bakedModelDefault.getQuads(state, side, rand);
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return bakedModelDefault.useAmbientOcclusion();
                }

                @Override
                public boolean isGui3d() {
                    return bakedModelDefault.isGui3d();
                }

                @Override
                public boolean usesBlockLight() {
                    return false;
                }

                @Override
                public boolean isCustomRenderer() {
                    return bakedModelDefault.isCustomRenderer();
                }

                @Override
                public TextureAtlasSprite getParticleIcon() {
                    return bakedModelDefault.getParticleIcon();
                }

                @Override
                public ItemOverrides getOverrides() {
                    return bakedModelDefault.getOverrides();
                }

                @Override
                public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat) {
                    BakedModel modelToUse = bakedModelDefault;
                    if (cameraTransformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || cameraTransformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                            || cameraTransformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
                        modelToUse = bakedModelHand;
                    }
                    return ForgeHooksClient.handlePerspective(modelToUse, cameraTransformType, mat);
                }
            };
            map.put(modelInventory, modelWrapper);
        }

        for (MaskType type : MaskType.values()) {
            ResourceLocation maskModelInventory = new ModelResourceLocation("mowziesmobs:barakoa_mask_" + type.name, "inventory");
            ResourceLocation maskModelFrame = new ModelResourceLocation("mowziesmobs:barakoa_mask_" + type.name + "_frame", "inventory");

            BakedModel maskBakedModelDefault = map.get(maskModelInventory);
            BakedModel maskBakedModelFrame = map.get(maskModelFrame);
            BakedModel maskModelWrapper = new BakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                    return maskBakedModelDefault.getQuads(state, side, rand);
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return maskBakedModelDefault.useAmbientOcclusion();
                }

                @Override
                public boolean isGui3d() {
                    return maskBakedModelDefault.isGui3d();
                }

                @Override
                public boolean usesBlockLight() {
                    return false;
                }

                @Override
                public boolean isCustomRenderer() {
                    return maskBakedModelDefault.isCustomRenderer();
                }

                @Override
                public TextureAtlasSprite getParticleIcon() {
                    return maskBakedModelDefault.getParticleIcon();
                }

                @Override
                public ItemOverrides getOverrides() {
                    return maskBakedModelDefault.getOverrides();
                }

                @Override
                public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat) {
                    BakedModel modelToUse = maskBakedModelDefault;
                    if (cameraTransformType == ItemTransforms.TransformType.FIXED) {
                        modelToUse = maskBakedModelFrame;
                    }
                    return ForgeHooksClient.handlePerspective(modelToUse, cameraTransformType, mat);
                }
            };

            map.put(maskModelInventory, maskModelWrapper);
        }
    }
}