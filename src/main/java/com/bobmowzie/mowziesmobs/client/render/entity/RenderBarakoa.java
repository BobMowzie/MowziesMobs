package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarakoa;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumMap;

@OnlyIn(Dist.CLIENT)
public class RenderBarakoa extends MobRenderer<EntityBarakoa, ModelBarakoa<EntityBarakoa>> {
    private static final EnumMap<MaskType, ResourceLocation> TEXTURES = MaskType.newEnumMap(ResourceLocation.class);
    private static final EnumMap<MaskType, ResourceLocation> WADOO_TEXTURES = MaskType.newEnumMap(ResourceLocation.class);

    static {
        for (MaskType mask : MaskType.values()) {
            TEXTURES.put(mask, new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + mask.name + (ConfigHandler.CLIENT.oldBarakoaTextures.get() ? "_old" : "") + ".png"));
            WADOO_TEXTURES.put(mask, new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + mask.name + "_wadoo.png"));
        }
    }

    public RenderBarakoa(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelBarakoa<>(), 0.6F);
        addLayer(new ItemLayer(this, getModel().bone, Items.BONE.getDefaultInstance(), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND));
        addLayer(new ItemLayer(this, getModel().spear, ItemHandler.SPEAR.getDefaultInstance(), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND));
        addLayer(new ItemLayer(this, getModel().blowgun, ItemHandler.BLOWGUN.getDefaultInstance(), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND));
        addLayer(new ItemLayer(this, getModel().staff, ItemHandler.SUNBLOCK_STAFF.getDefaultInstance(), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND));
    }

    @Override
    public void render(EntityBarakoa entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        boolean healingAnim = entityIn.getAnimation() == EntityBarakoa.HEAL_LOOP_ANIMATION || entityIn.getAnimation() == EntityBarakoa.HEAL_START_ANIMATION || entityIn.getAnimation() == EntityBarakoa.HEAL_STOP_ANIMATION;
        float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        if (healingAnim && entityIn.staffPos != null && entityIn.staffPos.length > 0) entityIn.staffPos[0] = MowzieRenderUtils.getWorldPosFromModel(entityIn, f, getModel().staffEnd);
    }

    @Override
    protected float getFlipDegrees(EntityBarakoa entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBarakoa entity) {
        String s = ChatFormatting.stripFormatting(entity.getName().getString());
        if (s != null && s.equals("Wadoo"))
            return WADOO_TEXTURES.get(entity.getMask());
        else
            return TEXTURES.get(entity.getMask());
    }
}
