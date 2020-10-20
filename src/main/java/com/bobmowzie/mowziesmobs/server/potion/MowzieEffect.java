package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

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
