package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.ActionResult;
import net.minecraft.sounds.Hand;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TextComponent;
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
    public ActionResult<ItemStack> onItemRightClick(Level worldIn, Player playerIn, Hand hand) {
        playerIn.addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get() * 60 * 20, 0, false, false));
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int effectDuration = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get();
        int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get();
        tooltip.add(
                new TextComponent(getDescriptionId() + ".text.0")
                .appendString(" " + effectDuration + " ")
                .appendSibling(new TextComponent(getDescriptionId() + ".text.1")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
        tooltip.add(new TextComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(
                new TextComponent(getDescriptionId() + ".text.3")
                .appendString(" " + solarBeamCost + " ")
                .appendSibling(new TextComponent(getDescriptionId() + ".text.4")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
    }
}
