package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

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
            solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM.get(), entity.level, entity, entity.getX() + radius1 * Math.sin(-entity.yRot * Math.PI / 180), entity.getY() + 1.4, entity.getZ() + radius1 * Math.cos(-entity.yRot * Math.PI / 180), (float) ((entity.yHeadRot + 90) * Math.PI / 180), (float) (-entity.xRot * Math.PI / 180), 55);
            entity.level.addFreshEntity(solarBeam);
        }
        if (entity.getAnimationTick() >= 4) {
            if (solarBeam != null) {
                float radius2 = 1.1f;
                double x = entity.getX() + radius1 * Math.sin(-entity.yRot * Math.PI / 180) + radius2 * Math.sin(-entity.yHeadRot * Math.PI / 180) * Math.cos(-entity.xRot * Math.PI / 180);
                double y = entity.getY() + 1.4 + radius2 * Math.sin(-entity.xRot * Math.PI / 180);
                double z = entity.getZ() + radius1 * Math.cos(-entity.yRot * Math.PI / 180) + radius2 * Math.cos(-entity.yHeadRot * Math.PI / 180) * Math.cos(-entity.xRot * Math.PI / 180);
                solarBeam.setPos(x, y, z);

                float yaw = entity.yHeadRot + 90;
                float pitch = -entity.xRot;
                solarBeam.setYaw((float) (yaw * Math.PI / 180));
                solarBeam.setPitch((float) (pitch * Math.PI / 180));
            }
        }
        if (entity.getAnimationTick() >= 22) {
            if (entityTarget != null) {
                entity.getLookControl().setLookAt(entityTarget.getX(), entityTarget.getY() + entityTarget.getBbHeight() / 2, entityTarget.getZ(), 2, 90);
            }
        }
    }
}
