package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 7/30/2018.
 */

public class ItemGlowingJelly extends Item {
    public static FoodProperties GLOWING_JELLY_FOOD = (new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0), 1.0f)).build();

    public ItemGlowingJelly(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
