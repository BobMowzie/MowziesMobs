package com.bobmowzie.mowziesmobs.client;

import java.util.List;
import java.util.Map;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class MMModels {
    public static final String[] HAND_MODEL_ITEMS = new String[] {"wrought_axe", "spear", "earthbore_gauntlet", "sculptor_staff"};

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> map = event.getModels();

        for (String item : HAND_MODEL_ITEMS) {
            ResourceLocation modelInventory = new ModelResourceLocation("mowziesmobs:" + item, "inventory");
            ResourceLocation modelHand = new ModelResourceLocation("mowziesmobs:" + item + "_in_hand", "inventory");

            BakedModel bakedModelDefault = map.get(modelInventory);
            BakedModel bakedModelHand = map.get(modelHand);
            BakedModel modelWrapper = new BakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
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
                public BakedModel applyTransform(ItemTransforms.TransformType cameraTransformType, PoseStack mat, boolean applyLeftHandTransform) {
                    BakedModel modelToUse = bakedModelDefault;
                    if (cameraTransformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || cameraTransformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                            || cameraTransformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
                        modelToUse = bakedModelHand;
                    }
                    return ForgeHooksClient.handleCameraTransforms(mat, modelToUse, cameraTransformType, applyLeftHandTransform);
                }
            };
            map.put(modelInventory, modelWrapper);
        }

        for (MaskType type : MaskType.values()) {
            ModelResourceLocation maskModelInventory = new ModelResourceLocation("mowziesmobs:umvuthana_mask_" + type.name, "inventory");
            ModelResourceLocation maskModelFrame = new ModelResourceLocation("mowziesmobs:umvuthana_mask_" + type.name + "_frame", "inventory");
            bakeMask(map, maskModelInventory, maskModelFrame);
        }
        ModelResourceLocation maskModelInventory = new ModelResourceLocation("mowziesmobs:sol_visage", "inventory");
        ModelResourceLocation maskModelFrame = new ModelResourceLocation("mowziesmobs:sol_visage_frame", "inventory");
        bakeMask(map, maskModelInventory, maskModelFrame);
    }

    private static void bakeMask(Map<ResourceLocation, BakedModel> map, ModelResourceLocation maskModelInventory, ModelResourceLocation maskModelFrame) {
        BakedModel maskBakedModelDefault = map.get(maskModelInventory);
        BakedModel maskBakedModelFrame = map.get(maskModelFrame);
        BakedModel maskModelWrapper = new BakedModel() {
            @Override
            public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
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
            public BakedModel applyTransform(ItemTransforms.TransformType cameraTransformType, PoseStack mat, boolean applyLeftHandTransform) {
                BakedModel modelToUse = maskBakedModelDefault;
                if (cameraTransformType == ItemTransforms.TransformType.FIXED) {
                    modelToUse = maskBakedModelFrame;
                }
                return ForgeHooksClient.handleCameraTransforms(mat, modelToUse, cameraTransformType, applyLeftHandTransform);
            }
        };

        map.put(maskModelInventory, maskModelWrapper);
    }
}