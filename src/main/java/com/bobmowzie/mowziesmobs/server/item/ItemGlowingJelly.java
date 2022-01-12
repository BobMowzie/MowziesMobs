package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Food;
import net.minecraft.world.item.Item;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.text.ITextComponent;
import net.minecraft.resources.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 7/30/2018.
 */

public class ItemGlowingJelly extends Item {
    public static Food GLOWING_JELLY_FOOD = (new Food.Builder().hunger(1).saturation(0.1f).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0), 1.0f)).build();

    public ItemGlowingJelly(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
