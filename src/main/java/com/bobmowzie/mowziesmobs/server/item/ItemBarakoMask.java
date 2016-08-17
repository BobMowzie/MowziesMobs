package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * Created by Josh on 8/15/2016.
 */
public class ItemBarakoMask extends ItemArmor {
        public ItemBarakoMask() {
        super(ItemArmor.ArmorMaterial.LEATHER, 2, EntityEquipmentSlot.HEAD);
        setUnlocalizedName("barakoMask");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setRegistryName("barakoa_mask");
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }
}
