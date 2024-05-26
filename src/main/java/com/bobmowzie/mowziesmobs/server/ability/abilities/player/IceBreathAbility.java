package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animation.RawAnimation;

public class IceBreathAbility extends PlayerAbility {
    protected EntityIceBreath iceBreath;

    public IceBreathAbility(AbilityType<Player, IceBreathAbility> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 6)
        });
    }

    private static final RawAnimation ICE_BREATH_START_ANIM = RawAnimation.begin().thenPlay("ice_breath_start");
    private static final RawAnimation ICE_BREATH_LOOP_ANIM = RawAnimation.begin().thenLoop("ice_breath_loop");
    private static final RawAnimation ICE_BREATH_END_ANIM = RawAnimation.begin().thenPlay("ice_breath_end");

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        if (!getUser().level().isClientSide()) {
            EntityIceBreath iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH.get(), user.level(), user);
            iceBreath.absMoveTo(user.getX(), user.getY() + user.getEyeHeight() - 0.5f, user.getZ(), user.getYRot(), user.getXRot());
            user.level().addFreshEntity(iceBreath);
            this.iceBreath = iceBreath;
        }
        playAnimation(ICE_BREATH_START_ANIM);

        if (getUser().getUsedItemHand() == InteractionHand.MAIN_HAND) {
            heldItemMainHandVisualOverride = getUser().getMainHandItem();
            heldItemOffHandVisualOverride = ItemStack.EMPTY;
            firstPersonOffHandDisplay = PlayerAbility.HandDisplay.DONT_RENDER;
        }
        else {
            heldItemOffHandVisualOverride = getUser().getOffhandItem();
            heldItemMainHandVisualOverride = ItemStack.EMPTY;
            firstPersonMainHandDisplay = PlayerAbility.HandDisplay.DONT_RENDER;
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY && !checkIceCrystal()) {
            jumpToSection(2);
        }
    }

    @Override
    public void end() {
        super.end();
        if (iceBreath != null) iceBreath.discard() ;
    }

    private boolean checkIceCrystal() {
        ItemStack stack = getUser().getUseItem();
        if (getTicksInUse() <= 1) return true;
        if (stack.getItem() != ItemHandler.ICE_CRYSTAL.get()) return false;
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
            playAnimation(ICE_BREATH_START_ANIM);
        }
        else if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            playAnimation(ICE_BREATH_LOOP_ANIM);
        }
        else if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            playAnimation(ICE_BREATH_END_ANIM);
        }
    }

    @Override
    public boolean preventsItemUse(ItemStack stack) {
        return stack.getItem() != ItemHandler.ICE_CRYSTAL.get();
    }
}
