package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleDecal;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class FissureAbility extends PlayerAbility {
    public FissureAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 21)
        });
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        super.onRightClickBlock(event);
        if (event.getItemStack().isEmpty()) {
            float rotation = (float) Math.toRadians(getUser().yHeadRot + 180f);
            Vec3 pos = event.getHitVec().getLocation();
            ParticleDecal.spawnDecal(getUser().level(), ParticleHandler.GROUND_CRACK.get(), pos.x(), pos.y() + 0.01, pos.z(), 0, 0, 0, rotation, 2, 1F, 1F, 1F, 1F, 0, 200, false, 32, 64, new ParticleComponent[]{

            });
        }
    }
}
