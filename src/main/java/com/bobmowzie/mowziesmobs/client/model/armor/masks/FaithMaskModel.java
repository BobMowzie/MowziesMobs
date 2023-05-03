package com.bobmowzie.mowziesmobs.client.model.armor.masks;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class FaithMaskModel extends AnimatedGeoModel<ItemBarakoaMask> {

    @Override
    public ResourceLocation getModelLocation(ItemBarakoaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_faith.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemBarakoaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/mask_faith.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemBarakoaMask animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/empty.animation.json");
    }
}