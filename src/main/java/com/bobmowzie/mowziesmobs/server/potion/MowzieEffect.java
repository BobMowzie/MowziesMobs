package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.effect.Effect;
import net.minecraft.world.effect.EffectType;
import net.minecraft.resources.ResourceLocation;

public class MowzieEffect extends Effect {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/potions.png");

    public MowzieEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }

    @Override
    public boolean isReady(int id, int amplifier) {
        return true;
    }
}
