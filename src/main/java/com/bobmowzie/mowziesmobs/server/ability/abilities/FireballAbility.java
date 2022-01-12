package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireballEntity;
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
            Vec3 lookVec = user.getLookVec();
            SmallFireballEntity smallfireballentity = new SmallFireballEntity(user.world, user, lookVec.x, lookVec.y, lookVec.z);
            smallfireballentity.setPosition(smallfireballentity.getPosX(), user.getPosYHeight(0.5D) + 0.5D, smallfireballentity.getPosZ());
            user.world.addEntity(smallfireballentity);
        }
    }
}
