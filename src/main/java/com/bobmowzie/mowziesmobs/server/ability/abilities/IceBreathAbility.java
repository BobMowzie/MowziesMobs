package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

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
        if (!getUser().level.isClientSide()) {
            EntityIceBreath iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH.get(), user.level, user);
            iceBreath.absMoveTo(user.getX(), user.getY() + user.getEyeHeight() - 0.5f, user.getZ(), user.getYRot(), user.getXRot());
            user.level.addFreshEntity(iceBreath);
            this.iceBreath = iceBreath;
        }
        playAnimation("ice_breath_start", false);

        if (getUser().getUsedItemHand() == InteractionHand.MAIN_HAND) {
            heldItemMainHandVisualOverride = getUser().getMainHandItem();
            heldItemOffHandVisualOverride = ItemStack.EMPTY;
            firstPersonOffHandDisplay = Ability.HandDisplay.DONT_RENDER;
        }
        else {
            heldItemOffHandVisualOverride = getUser().getOffhandItem();
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
        if (iceBreath != null) iceBreath.discard() ;
    }

    private boolean checkIceCrystal() {
        ItemStack stack = getUser().getUseItem();
        if (getTicksInUse() <= 1) return true;
        if (stack.getItem() != ItemHandler.ICE_CRYSTAL) return false;
        return stack.getDamageValue() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get();
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
