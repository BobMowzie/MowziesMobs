package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> map = event.getModelRegistry();

        ResourceLocation axeModelInventory = new ModelResourceLocation("mowziesmobs:wrought_axe", "inventory");
        ResourceLocation axeModelHand = new ModelResourceLocation("mowziesmobs:wrought_axe_in_hand", "inventory");

        IBakedModel axeBakedModelDefault = map.get(axeModelInventory);
        IBakedModel axeBakedModelHand = map.get(axeModelHand);
        IBakedModel axeModelWrapper = new IBakedModel() {
            @Override
            public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                return axeBakedModelDefault.getQuads(state, side, rand);
            }

            @Override
            public boolean isAmbientOcclusion() {
                return axeBakedModelDefault.isAmbientOcclusion();
            }

            @Override
            public boolean isGui3d() {
                return axeBakedModelDefault.isGui3d();
            }

            @Override
            public boolean isSideLit() {
                return false;
            }

            @Override
            public boolean isBuiltInRenderer() {
                return axeBakedModelDefault.isBuiltInRenderer();
            }

            @Override
            public TextureAtlasSprite getParticleTexture() {
                return axeBakedModelDefault.getParticleTexture();
            }

            @Override
            public ItemOverrideList getOverrides() {
                return axeBakedModelDefault.getOverrides();
            }

            @Override
            public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
                IBakedModel modelToUse = axeBakedModelDefault;
                if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                        || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
                    modelToUse = axeBakedModelHand;
                }
                return ForgeHooksClient.handlePerspective(modelToUse, cameraTransformType, mat);
            }
        };
        map.put(axeModelInventory, axeModelWrapper);


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