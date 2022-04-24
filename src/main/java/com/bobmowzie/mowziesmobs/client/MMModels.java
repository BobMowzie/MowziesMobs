package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
    public static final String[] HAND_MODEL_ITEMS = new String[] {"wrought_axe", "spear"};

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> map = event.getModelRegistry();

        for (String item : HAND_MODEL_ITEMS) {
            ResourceLocation modelInventory = new ModelResourceLocation("mowziesmobs:" + item, "inventory");
            ResourceLocation modelHand = new ModelResourceLocation("mowziesmobs:" + item + "_in_hand", "inventory");

            IBakedModel bakedModelDefault = map.get(modelInventory);
            IBakedModel bakedModelHand = map.get(modelHand);
            IBakedModel modelWrapper = new IBakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                    return bakedModelDefault.getQuads(state, side, rand);
                }

                @Override
                public boolean isAmbientOcclusion() {
                    return bakedModelDefault.isAmbientOcclusion();
                }

                @Override
                public boolean isGui3d() {
                    return bakedModelDefault.isGui3d();
                }

                @Override
                public boolean isSideLit() {
                    return false;
                }

                @Override
                public boolean isBuiltInRenderer() {
                    return bakedModelDefault.isBuiltInRenderer();
                }

                @Override
                public TextureAtlasSprite getParticleTexture() {
                    return bakedModelDefault.getParticleTexture();
                }

                @Override
                public ItemOverrideList getOverrides() {
                    return bakedModelDefault.getOverrides();
                }

                @Override
                public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
                    IBakedModel modelToUse = bakedModelDefault;
                    if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                            || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
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

            IBakedModel maskBakedModelDefault = map.get(maskModelInventory);
            IBakedModel maskBakedModelFrame = map.get(maskModelFrame);
            IBakedModel maskModelWrapper = new IBakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                    return maskBakedModelDefault.getQuads(state, side, rand);
                }

                @Override
                public boolean isAmbientOcclusion() {
                    return maskBakedModelDefault.isAmbientOcclusion();
                }

                @Override
                public boolean isGui3d() {
                    return maskBakedModelDefault.isGui3d();
                }

                @Override
                public boolean isSideLit() {
                    return false;
                }

                @Override
                public boolean isBuiltInRenderer() {
                    return maskBakedModelDefault.isBuiltInRenderer();
                }

                @Override
                public TextureAtlasSprite getParticleTexture() {
                    return maskBakedModelDefault.getParticleTexture();
                }

                @Override
                public ItemOverrideList getOverrides() {
                    return maskBakedModelDefault.getOverrides();
                }

                @Override
                public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
                    IBakedModel modelToUse = maskBakedModelDefault;
                    if (cameraTransformType == ItemCameraTransforms.TransformType.FIXED) {
                        modelToUse = maskBakedModelFrame;
                    }
                    return ForgeHooksClient.handlePerspective(modelToUse, cameraTransformType, mat);
                }
            };

            map.put(maskModelInventory, maskModelWrapper);
        }
    }
}