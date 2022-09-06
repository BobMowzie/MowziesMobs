package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class AnimationSolarBeam<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    protected LivingEntity entityTarget;

    private EntitySolarBeam solarBeam;

    public AnimationSolarBeam(T entity, Animation animation) {
        super(entity, animation);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void start() {
        super.start();
        entityTarget = entity.getTarget();
    }

    @Override
    public void tick() {
        super.tick();
        float radius1 = 0.8f;
        if (entity.getAnimationTick() == 4 && !entity.level.isClientSide) {
            solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM.get(), entity.level, entity, entity.getX() + radius1 * Math.sin(-entity.getYRot() * Math.PI / 180), entity.getY() + 1.4, entity.getZ() + radius1 * Math.cos(-entity.getYRot() * Math.PI / 180), (float) ((entity.yHeadRot + 90) * Math.PI / 180), (float) (-entity.getXRot() * Math.PI / 180), 55);
            entity.level.addFreshEntity(solarBeam);
        }
        if (entity.getAnimationTick() >= 22) {
            if (entityTarget != null) {
                entity.getLookControl().setLookAt(entityTarget.getX(), entityTarget.getY() + entityTarget.getBbHeight() / 2, entityTarget.getZ(), 2, 90);
            }
        }
    }
}
