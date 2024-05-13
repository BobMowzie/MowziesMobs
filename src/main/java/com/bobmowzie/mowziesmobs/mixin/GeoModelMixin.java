package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
public class GeoModelMixin {

    @ModifyVariable(method = "handleAnimations", at = @At("STORE"), ordinal = 0)
    private <T extends GeoAnimatable> double currentTimeInjected(double x, T animatable, long instanceId, AnimationState<T> animationState) {
        Minecraft mc = Minecraft.getInstance();
        AnimatableManager<T> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        Double currentTick = animationState.getData(DataTickets.TICK);
        return animatable instanceof Entity || animatable instanceof GeckoPlayer ? currentTick + mc.getFrameTime() : currentTick - animatableManager.getFirstTickTime();
    }
}
