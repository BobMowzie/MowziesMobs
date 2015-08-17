package com.bobmowzie.mowziesmobs.client.model.extension;

import thehippomaster.AnimationAPI.AnimationAPI;

import com.bobmowzie.mowziesmobs.common.item.ItemWroughtAxe;
import com.bobmowzie.mowziesmobs.common.property.WroughtAxeSwingProperty;

import net.ilexiconn.llibrary.client.render.IModelExtension;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by jnad325 on 7/7/15.
 */
public class ModelPlayerExtension extends ModelBiped implements IModelExtension
{
    @Override
    public void setRotationAngles(ModelBiped modelBiped, float v, float v1, float v2, float v3, float v4, float v5, Entity entity)
    {
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWroughtAxe)
        {
            WroughtAxeSwingProperty property = WroughtAxeSwingProperty.getProperty(player);
            float time = property.getSwingPercentage(AnimationAPI.proxy.getPartialTick());
            if (time > 0)
            {
                float controller1 = WroughtAxeSwingProperty.fnc1(time);
                float controller2 = WroughtAxeSwingProperty.fnc2(time);
                float controller3 = WroughtAxeSwingProperty.fnc3(time, 0.1f, 0.9f, 30);
                ModelRenderer rightArm = modelBiped.bipedRightArm;
                float normalAmount = time < 0.1F ? 1 - time / 0.1F : time > 0.9F ? (time - 0.9F) / 0.1F : 0;
                float swingAmount = 1 - normalAmount;
                rightArm.rotateAngleY = rightArm.rotateAngleY * normalAmount + (0.6F * controller1 + 0.3F * controller2) * swingAmount;
                rightArm.rotateAngleX = rightArm.rotateAngleX * normalAmount + ((float) -Math.PI / 2 * controller3) * swingAmount;
            }
        }
    }

    @Override
    public void preRender(EntityPlayer entityPlayer, ModelBase modelBase, float v)
    {
    }

    @Override
    public void postRender(EntityPlayer entityPlayer, ModelBase modelBase, float v)
    {
    }

    @Override
    public void init(ModelBase modelBase)
    {
    }
}
