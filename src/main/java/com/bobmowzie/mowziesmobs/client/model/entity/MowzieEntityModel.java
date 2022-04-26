package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class MowzieEntityModel<T extends MowzieEntity> extends AdvancedModelBase<T> {
    protected final MMModelAnimator animator = MMModelAnimator.create();

    protected MowzieEntityModel() {
        super();
    }

    protected MowzieEntityModel(Function<ResourceLocation, RenderType> renderTypeFunction) {
        super(renderTypeFunction);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float delta = ageInTicks - entityIn.tickCount;
        this.animator.update(entityIn, delta);
        this.animate(entityIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, delta);
    }

    @SuppressWarnings("unchecked")
    private T cast(Entity entity) {
        return (T) entity;
    }

    protected abstract void animate(T entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta);

    protected static void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
