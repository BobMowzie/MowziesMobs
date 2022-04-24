package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vector2f;

public class AnimationSunStrike<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    protected LivingEntity entityTarget;
    public double prevX;
    public double prevZ;
    private int newX;
    private int newZ;
    private int y;

    public AnimationSunStrike(T entity, Animation animation) {
        super(entity, animation, false);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = entity.getTarget();
        if (entityTarget != null) {
            prevX = entityTarget.getX();
            prevZ = entityTarget.getZ();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (entityTarget == null) {
            return;
        }
        if (entity.getAnimationTick() < 9) {
            entity.getLookController().setLookPositionWithEntity(entityTarget, 30, 30);
        }

        if (entity.getAnimationTick() == 7) {
            double x = entityTarget.getX();
            y = Mth.floor(entityTarget.getY() - 1);
            double z = entityTarget.getZ();
            double vx = (x - prevX) / 9;
            double vz = (z - prevZ) / 9;
            int t = EntitySunstrike.STRIKE_EXPLOSION + 3;
            newX = Mth.floor(x + vx * t);
            newZ = Mth.floor(z + vz * t);
            double dx = newX - entity.getX();
            double dz = newZ - entity.getZ();
            double dist2ToBarako = dx * dx + dz * dz;
            if (dist2ToBarako < 3) {
                newX = Mth.floor(entityTarget.getX());
                newZ = Mth.floor(entityTarget.getZ());
            }
            for (int i = 0; i < 5; i++) {
                if (!entity.world.canBlockSeeSky(new BlockPos(newX, y, newZ))) {
                    y++;
                } else {
                    break;
                }
            }
        }
        if (!entity.level.isClientSide && entity.getAnimationTick() == 9) {
            entity.playSound(MMSounds.ENTITY_BARAKO_ATTACK.get(), 1.4f, 1);
            EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, entity.world, entity, newX, y, newZ);
            sunstrike.onSummon();
            entity.level.addFreshEntity(sunstrike);
        }
        if (entity.getAnimationTick() > 6) {
            entity.getLookController().setLookPosition(newX, y, newZ, 20, 20);
        }
    }
}
