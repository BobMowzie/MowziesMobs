package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AnimationSunStrike<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    protected EntityLivingBase entityTarget;
    private double prevX;
    private double prevZ;
    private int newX;
    private int newZ;
    private int y;

    public AnimationSunStrike(T entity, Animation animation) {
        super(entity, animation, false);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = entity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entityTarget == null) {
            return;
        }
        if (entity.getAnimationTick() < 9) {
            entity.getLookHelper().setLookPositionWithEntity(entityTarget, 30, 30);
        }

        if (entity.getAnimationTick() == 1) {
            prevX = entityTarget.posX;
            prevZ = entityTarget.posZ;
        }
        if (entity.getAnimationTick() == 7) {
            double x = entityTarget.posX;
            y = MathHelper.floor(entityTarget.posY - 1);
            double z = entityTarget.posZ;
            double vx = (x - prevX) / 9;
            double vz = (z - prevZ) / 9;
            int t = EntitySunstrike.STRIKE_EXPLOSION + 3;
            newX = MathHelper.floor(x + vx * t);
            newZ = MathHelper.floor(z + vz * t);
            for (int i = 0; i < 5; i++) {
                if (!entity.world.canBlockSeeSky(new BlockPos(newX, y, newZ))) {
                    y++;
                } else {
                    break;
                }
            }
        }
        if (!entity.world.isRemote && entity.getAnimationTick() == 9) {
            entity.playSound(MMSounds.ENTITY_BARAKO_ATTACK, 1.4f, 1);
            EntitySunstrike sunstrike = new EntitySunstrike(entity.world, entity, newX, y, newZ);
            sunstrike.onSummon();
            entity.world.spawnEntity(sunstrike);
        }
        if (entity.getAnimationTick() > 6) {
            entity.getLookHelper().setLookPosition(newX, y, newZ, 20, 20);
        }
    }
}
