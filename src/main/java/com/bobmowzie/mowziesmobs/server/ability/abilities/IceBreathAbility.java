package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.Hand;

public class IceBreathAbility extends Ability {
    protected EntityIceBreath iceBreath;

    public IceBreathAbility(AbilityType<IceBreathAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 6)
        });
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        if (!getUser().world.isClientSide()) {
            EntityIceBreath iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH, user.world, user);
            iceBreath.setPositionAndRotation(user.getPosX(), user.getPosY() + user.getEyeHeight() - 0.5f, user.getPosZ(), user.getYRot(), user.getXRot());
            user.world.addEntity(iceBreath);
            this.iceBreath = iceBreath;
        }
        playAnimation("ice_breath_start", false);

        if (getUser().getActiveHand() == Hand.MAIN_HAND) {
            heldItemMainHandVisualOverride = getUser().getHeldItemMainhand();
            heldItemOffHandVisualOverride = ItemStack.EMPTY;
            firstPersonOffHandDisplay = Ability.HandDisplay.DONT_RENDER;
        }
        else {
            heldItemOffHandVisualOverride = getUser().getHeldItemOffhand();
            heldItemMainHandVisualOverride = ItemStack.EMPTY;
            firstPersonMainHandDisplay = Ability.HandDisplay.DONT_RENDER;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY && !checkIceCrystal()) jumpToSection(2);
    }

    @Override
    public void end() {
        super.end();
        if (iceBreath != null) iceBreath.remove();
    }

    private boolean checkIceCrystal() {
        ItemStack stack = getUser().getActiveItemStack();
        if (getTicksInUse() <= 1) return true;
        if (stack.getItem() != ItemHandler.ICE_CRYSTAL) return false;
        return stack.getDamage() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get();
    }

    @Override
    public boolean canUse() {
        return checkIceCrystal() && super.canUse();
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            playAnimation("ice_breath_start", false);
        }
        else if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            playAnimation("ice_breath_loop", true);
        }
        else if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            playAnimation("ice_breath_end", false);
        }
    }
}
