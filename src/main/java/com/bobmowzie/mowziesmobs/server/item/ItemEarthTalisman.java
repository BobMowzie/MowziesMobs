package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.text.ITextComponent;
import net.minecraft.resources.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 7/26/2017.
 */
public class ItemEarthTalisman extends Item {
    public ItemEarthTalisman(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.4").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.5").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.6").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.7").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
