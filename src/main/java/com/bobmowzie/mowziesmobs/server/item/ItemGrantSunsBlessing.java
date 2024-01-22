package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 10/31/2016.
 */
public class ItemGrantSunsBlessing extends Item {
    public ItemGrantSunsBlessing(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        playerIn.addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get() * 60 * 20, 0, false, false));
        return super.use(worldIn, playerIn, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int effectDuration = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get();
        int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get();
        tooltip.add(
                new TranslatableComponent(getDescriptionId() + ".text.0")
                .append(" " + effectDuration + " ")
                .append(new TranslatableComponent(getDescriptionId() + ".text.1")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(
                new TranslatableComponent(getDescriptionId() + ".text.4")
                .append(" " + solarBeamCost + " ")
                .append(new TranslatableComponent(getDescriptionId() + ".text.5")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.6").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
