package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Josh on 8/15/2016.
 */
public class ItemBarakoMask extends ItemArmor implements BarakoaMask {
        public ItemBarakoMask() {
        super(ItemArmor.ArmorMaterial.GOLD, 2, EntityEquipmentSlot.HEAD);
        setUnlocalizedName("barakoMask");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setRegistryName("barakoa_mask");
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {}

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
