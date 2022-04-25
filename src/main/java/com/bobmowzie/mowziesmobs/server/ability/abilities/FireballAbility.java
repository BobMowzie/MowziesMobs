package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;

public class FireballAbility extends Ability {
    public FireballAbility(AbilityType<FireballAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE)
        }, 20);
    }

    @Override
    public void tick() {
        super.tick();
        if (getTicksInUse() == 20) {
            LivingEntity user = getUser();
            Vec3 lookVec = user.getLookAngle();
            SmallFireball smallfireballentity = new SmallFireball(user.level, user, lookVec.x, lookVec.y, lookVec.z);
            smallfireballentity.setPos(smallfireballentity.getX(), user.getY(0.5D) + 0.5D, smallfireballentity.getZ());
            user.level.addFreshEntity(smallfireballentity);
        }
    }
}
