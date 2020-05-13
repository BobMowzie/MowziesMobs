package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Josh on 7/26/2017.
 */
public class ItemEarthTalisman extends Item {
    public ItemEarthTalisman() {
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setTranslationKey("earthTalisman");
        setRegistryName("earth_talisman");
        maxStackSize = 1;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
