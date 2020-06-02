package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.RenderHelper;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Josh on 6/28/2017.
 */
public enum FrozenRenderHandler {
    INSTANCE;

    private static final ResourceLocation FROZEN_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/frozen.png");

    public static class LayerFrozen implements LayerRenderer<EntityLivingBase> {
        private final RenderLivingBase<EntityLivingBase> renderer;
        private final Predicate<ModelRenderer> modelExclusions;

        public LayerFrozen(RenderLivingBase<EntityLivingBase> renderer, Predicate<ModelRenderer> modelExclusions) {
            this.renderer = renderer;
            this.modelExclusions = modelExclusions;
        }

        public LayerFrozen(RenderLivingBase<EntityLivingBase> renderer) {
            this(renderer, box -> {
//                if(renderer instanceof RenderPlayer) {
//                    RenderPlayer renderPlayer = (RenderPlayer) renderer;
//                    ModelPlayer playerModel = renderPlayer.getMainModel();
//                    return box == playerModel.bipedHeadwear || box == playerModel.bipedRightLegwear ||
//                            box == playerModel.bipedLeftLegwear || box == playerModel.bipedBodyWear ||
//                            box == playerModel.bipedRightArmwear || box == playerModel.bipedLeftArmwear;
//                }
                return false;
            });
        }

        @Override
        public void doRenderLayer(EntityLivingBase living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (living.isPotionActive(PotionHandler.FROZEN)) {
                ModelBase model = this.renderer.getMainModel();
                Map<ModelRenderer, Boolean> visibilities = new HashMap<>();
                for(ModelRenderer box : model.boxList) {
                    if(this.modelExclusions.test(box)) {
                        visibilities.put(box, box.showModel);
                        box.showModel = false;
                    }
                }

                //Render decay overlay
                float transparency = 1;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.renderer.bindTexture(FROZEN_TEXTURE);
                GlStateManager.color(1, 1, 1, transparency);
                model.render(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.color(1, 1, 1, 1);

                for(Map.Entry<ModelRenderer, Boolean> entry : visibilities.entrySet()) {
                    entry.getKey().showModel = entry.getValue();
                }


            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    @SubscribeEvent
    public void onPreRenderEntityLivingBase(RenderLivingEvent.Pre event) {
        EntityLivingBase player = event.getEntity();

        if(player.isPotionActive(PotionHandler.FROZEN)) {
            if(!RenderHelper.doesRendererHaveLayer(event.getRenderer(), LayerFrozen.class, false)) {
                event.getRenderer().addLayer(new LayerFrozen(event.getRenderer()));
            }
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderSpecificHandEvent event) {
        GlStateManager.pushMatrix();

        EntityPlayer player = Minecraft.getMinecraft().player;

        if(player != null && player.isPotionActive(PotionHandler.FROZEN)) {
            if(player.isPotionActive(PotionHandler.FROZEN)) {
                boolean isMainHand = event.getHand() == EnumHand.MAIN_HAND;
                if(isMainHand && !player.isInvisible() && event.getItemStack().isEmpty()) {
                    EnumHandSide enumhandside = isMainHand ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
                    renderArmFirstPersonFrozen(event.getEquipProgress(), event.getSwingProgress(), enumhandside);
                    event.setCanceled(true);
                }
            }
        }

        GlStateManager.popMatrix();
    }

    /**
     * From ItemRenderer#renderArmFirstPerson
     * @param swingProgress
     * @param equipProgress
     * @param handSide
     * @param decay
     */
    private void renderArmFirstPersonFrozen(float swingProgress, float equipProgress, EnumHandSide handSide) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager renderManager = mc.getRenderManager();
        boolean flag = handSide != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(equipProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(equipProgress * (float)Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + swingProgress * -0.6F, f4 + -0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(equipProgress * equipProgress * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = mc.player;
        mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer) (Render<?>) renderManager.getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();

        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);

            mc.renderEngine.bindTexture(FROZEN_TEXTURE);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float transparency = 1;
            GlStateManager.color(1, 1, 1, transparency);

            //From RenderPlayer#renderRightArm
            ModelPlayer modelplayer = renderplayer.getMainModel();
            GlStateManager.enableBlend();
            modelplayer.swingProgress = 0.0F;
            modelplayer.isSneak = false;
            modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, abstractclientplayer);
            modelplayer.bipedRightArm.rotateAngleX = 0.0F;
            modelplayer.bipedRightArm.render(0.0625F);
            modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
            modelplayer.bipedRightArmwear.render(0.0625F);
            GlStateManager.disableBlend();
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);

            mc.renderEngine.bindTexture(FROZEN_TEXTURE);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float transparency = 1;
            GlStateManager.color(1, 1, 1, transparency);

            //From RenderPlayer#renderLeftArm
            ModelPlayer modelplayer = renderplayer.getMainModel();
            GlStateManager.enableBlend();
            modelplayer.isSneak = false;
            modelplayer.swingProgress = 0.0F;
            modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, abstractclientplayer);
            modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
            modelplayer.bipedLeftArm.render(0.0625F);
            modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
            modelplayer.bipedLeftArmwear.render(0.0625F);
            GlStateManager.disableBlend();
        }

        GlStateManager.color(1, 1, 1, 1);

        GlStateManager.enableCull();
    }
}