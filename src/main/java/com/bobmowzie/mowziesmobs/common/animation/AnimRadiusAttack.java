package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.EntityTribeLeader;
import com.bobmowzie.mowziesmobs.common.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import java.util.List;

/**
 * Created by jnad325 on 12/3/15.
 */
public class AnimRadiusAttack extends MMAnimBase {
    float radius;
    int damage;
    float knockBack;
    int damageFrame;
    public AnimRadiusAttack(MMEntityBase entity, int id, int duration, float radius, int damage, float knockBack, int damageFrame) {
        super(entity, id, duration);
        this.radius = radius;
        this.damage = damage;
        this.knockBack = knockBack;
        this.damageFrame = damageFrame;
        setMutexBits(8);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (animatingEntity.getAnimTick() == damageFrame){
            List<EntityLivingBase> hit = animatingEntity.getEntityLivingBaseNearby(radius, 2 * radius, radius, radius);
            for (int i = 0; i < hit.size(); i++) {
                if (animatingEntity instanceof EntityTribeLeader && hit.get(i) instanceof LeaderSunstrikeImmune) continue;
                hit.get(i).attackEntityFrom(DamageSource.causeMobDamage(animatingEntity), damage);
                hit.get(i).motionX *= knockBack;
                hit.get(i).motionZ *= knockBack;
            }
        }
    }
}