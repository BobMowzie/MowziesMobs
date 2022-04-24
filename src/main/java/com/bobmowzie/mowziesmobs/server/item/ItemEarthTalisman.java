package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;

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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.4").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.5").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.6").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.7").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
