package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ExportingModelAnimator;
import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public abstract class MowzieEntityModel<T extends MowzieEntity> extends AdvancedModelBase {
    protected MMModelAnimator animator = MMModelAnimator.create();

    int id = -1;

    @Override
    public final void render(Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        T mowzie = this.cast(entity);
        float delta = age - entity.ticksExisted;
        if (entity.getEntityId() != id) {
            id = entity.getEntityId();
            animator = new ExportingModelAnimator();
            if (mowzie instanceof EntityWroughtnaut) {
                ((EntityWroughtnaut) mowzie).setActive(true);
            }
            ((ExportingModelAnimator) animator).addBones(this, mowzie);
            for (Animation a : mowzie.getAnimations()) {
                /*if (a != EntityWroughtnaut.VERTICAL_ATTACK_ANIMATION) continue;*/
                mowzie.setAnimation(a);
                this.animator.update(mowzie, delta);
                this.animate(mowzie, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
                ((ExportingModelAnimator) animator).endAnimation();
            }
            ((ExportingModelAnimator) animator).finish();
            animator = MMModelAnimator.create();
        }
        this.animator.update(mowzie, delta);
        this.animate(mowzie, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        this.render(mowzie, scale);
    }

    @SuppressWarnings("unchecked")
    private T cast(Entity entity) {
        return (T) entity;
    }

    protected abstract void animate(T entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta);

    protected abstract void render(T entity, float scale);

    protected static void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
