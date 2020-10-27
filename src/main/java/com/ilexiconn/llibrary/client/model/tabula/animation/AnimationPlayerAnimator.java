package com.ilexiconn.llibrary.client.model.tabula.animation;

import com.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import com.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import com.ilexiconn.llibrary.client.model.tabula.container.TabulaAnimationComponentContainer;
import com.ilexiconn.llibrary.client.model.tabula.container.TabulaAnimationContainer;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import com.ilexiconn.llibrary.server.animation.NamedAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Tabula Model animator that can play Tabula animations. USE {@link NamedAnimation} !!
 * @author jglrxavpok
 * @param <T> the entity type, needs to be an {@link IAnimatedEntity}
 */
public class AnimationPlayerAnimator<T extends Entity&IAnimatedEntity> implements ITabulaModelAnimator<T> {
    @Override
    public void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        float partialTicks = LLibrary.PROXY.getPartialTicks();
        Animation anim = entity.getAnimation();
        TabulaAnimationContainer animation;
        if(anim instanceof NamedAnimation) {
            animation = model.getAnimation(((NamedAnimation) anim).getName());
        } else {
            return;
        }

        if(animation == null)
            return;

        prepareAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, partialTicks);

        animation.getComponents().entrySet().forEach(entry -> {
            for(TabulaAnimationComponentContainer component : entry.getValue()) {
                applyComponent(component, model.getCubeByIdentifier(entry.getKey()), entity, partialTicks);
            }
        });

        finishAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, partialTicks);
    }

    /**
     * Callback to use if you want to change model parts **before animating** but **after resetting to the default pose**. Uses the same argument as {@link #setRotationAngles(TabulaModel, Entity, float, float, float, float, float, float)}
     */
    protected void prepareAnimation(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float partialTicks) { }

    /**
     * Callback to use if you want to change model parts **after animating**. Uses the same argument as {@link #setRotationAngles(TabulaModel, Entity, float, float, float, float, float, float)}
     */
    protected void finishAnimation(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float partialTicks) { }

    /**
     * Animates a given cube with the component
     * @param component the animation component
     * @param cube the cube to animate
     * @param entity the animated entity
     */
    private void applyComponent(TabulaAnimationComponentContainer component, AdvancedModelRenderer cube, T entity, float partialTicks) {
        float tick = entity.getAnimationTick() + partialTicks;
        double progress = (tick - component.getStartKey()) / (double)component.getLength();
        progress = MathHelper.clamp(progress, 0.0, 1.0);
        if(tick >= component.getStartKey()) {
            cube.rotateAngleX += Math.toRadians(component.getRotationOffset()[0]);
            cube.rotateAngleY += Math.toRadians(component.getRotationOffset()[1]);
            cube.rotateAngleZ += Math.toRadians(component.getRotationOffset()[2]);

            cube.rotationPointX += component.getPositionOffset()[0];
            cube.rotationPointY += component.getPositionOffset()[1];
            cube.rotationPointZ += component.getPositionOffset()[2];

            cube.scaleX += component.getScaleOffset()[0];
            cube.scaleY += component.getScaleOffset()[1];
            cube.scaleZ += component.getScaleOffset()[2];

            cube.opacity += component.getOpacityOffset();
        }
        cube.rotateAngleX += Math.toRadians(component.getRotationChange()[0] * progress);
        cube.rotateAngleY += Math.toRadians(component.getRotationChange()[1] * progress);
        cube.rotateAngleZ += Math.toRadians(component.getRotationChange()[2] * progress);

        cube.rotationPointX += component.getPositionChange()[0] * progress;
        cube.rotationPointY += component.getPositionChange()[1] * progress;
        cube.rotationPointZ += component.getPositionChange()[2] * progress;

        cube.scaleX += component.getScaleChange()[0] * progress;
        cube.scaleY += component.getScaleChange()[1] * progress;
        cube.scaleZ += component.getScaleChange()[2] * progress;

        cube.opacity += component.getOpacityChange() * progress;
    }
}
