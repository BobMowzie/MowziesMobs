package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityInstance;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.vector.Vector3d;

public class FireballAbility extends Ability<AbilityInstance> {
    public FireballAbility() {
        super(new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE)
        }, 20);
    }

    @Override
    protected void start(AbilityInstance abilityInstance) {
        super.start(abilityInstance);
    }

    @Override
    public void tick(AbilityInstance abilityInstance) {
        super.tick(abilityInstance);
        if (abilityInstance.getTicksInUse() == 20) {
            LivingEntity user = abilityInstance.getUser();
            Vector3d lookVec = user.getLookVec();
            SmallFireballEntity smallfireballentity = new SmallFireballEntity(user.world, user, lookVec.x, lookVec.y, lookVec.z);
            smallfireballentity.setPosition(smallfireballentity.getPosX(), user.getPosYHeight(0.5D) + 0.5D, smallfireballentity.getPosZ());
            user.world.addEntity(smallfireballentity);
        }
    }

    @Override
    public AbilityInstance makeInstance(LivingEntity user) {
        return new AbilityInstance(this, user);
    }
}
