package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelRendererMatrix;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MowzieElytraModel<T extends LivingEntity> extends ElytraModel<T> {
    public ModelRendererMatrix bipedBody;

    public MowzieElytraModel(ModelPart bipedBody) {
        this.bipedBody = new ModelRendererMatrix(bipedBody);
        this.bipedBody.cubes.clear();
        this.bipedBody.addChild(rightWing);
        this.bipedBody.addChild(leftWing);
        rightWing.z = 2;
        leftWing.z = 2;
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.bipedBody);
    }
}