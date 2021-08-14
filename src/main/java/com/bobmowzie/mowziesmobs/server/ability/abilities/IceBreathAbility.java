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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class IceBreathAbility extends Ability {
    protected EntityIceBreath iceBreath;

    public IceBreathAbility(AbilityType<IceBreathAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        if (!getUser().world.isRemote()) {
            EntityIceBreath iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH, user.world, user);
            iceBreath.setPositionAndRotation(user.getPosX(), user.getPosY() + user.getEyeHeight() - 0.5f, user.getPosZ(), user.rotationYaw, user.rotationPitch);
            user.world.addEntity(iceBreath);
            this.iceBreath = iceBreath;
        }
    }

    @Override
    public void end() {
        super.end();
        if (iceBreath != null) iceBreath.remove();
    }

    private boolean checkIceCrystal() {
        ItemStack stack = getUser().getActiveItemStack();
        if (stack.getItem() != ItemHandler.ICE_CRYSTAL) return false;
        return stack.getDamage() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get();
    }

    @Override
    public boolean canUse() {
        return checkIceCrystal() && super.canUse();
    }

    @Override
    protected boolean canContinueUsing() {
        return checkIceCrystal();
    }
}
