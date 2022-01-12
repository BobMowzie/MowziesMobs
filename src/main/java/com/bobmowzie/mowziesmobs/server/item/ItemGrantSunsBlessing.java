package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.ActionResult;
import net.minecraft.resources.Hand;
import net.minecraft.resources.text.ITextComponent;
import net.minecraft.resources.text.TranslationTextComponent;
import net.minecraft.world.World;

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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, Player playerIn, Hand hand) {
        playerIn.addPotionEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get() * 60 * 20, 0, false, false));
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int effectDuration = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get();
        int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get();
        tooltip.add(
                new TranslationTextComponent(getTranslationKey() + ".text.0")
                .appendString(" " + effectDuration + " ")
                .appendSibling(new TranslationTextComponent(getTranslationKey() + ".text.1")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(
                new TranslationTextComponent(getTranslationKey() + ".text.3")
                .appendString(" " + solarBeamCost + " ")
                .appendSibling(new TranslationTextComponent(getTranslationKey() + ".text.4")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
    }
}
