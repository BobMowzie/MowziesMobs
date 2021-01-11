package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
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
        IBakedModel axeModelWrapper = new IBakedModel()
        {
            @Override
            public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand)
            {
                return axeBakedModelDefault.getQuads(state, side, rand);
            }

            @Override
            public boolean isAmbientOcclusion()
            {
                return axeBakedModelDefault.isAmbientOcclusion();
            }

            @Override
            public boolean isGui3d()
            {
                return axeBakedModelDefault.isGui3d();
            }

            @Override
            public boolean isBuiltInRenderer()
            {
                return axeBakedModelDefault.isBuiltInRenderer();
            }

            @Override
            public TextureAtlasSprite getParticleTexture()
            {
                return axeBakedModelDefault.getParticleTexture();
            }

            @Override
            public ItemOverrideList getOverrides()
            {
                return axeBakedModelDefault.getOverrides();
            }

            @Override
            public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
                IBakedModel modelToUse = axeBakedModelDefault;
                if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                        || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                {
                    modelToUse = axeBakedModelHand;
                }
                return ForgeHooksClient.handlePerspective(modelToUse, cameraTransformType);
            }
        };
        map.put(axeModelInventory, axeModelWrapper);
    }
}