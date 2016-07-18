package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

public class ItemBarakoaMask extends ItemArmor {
    private final Type type;

    public ItemBarakoaMask(Type type) {
        super(ArmorMaterial.LEATHER, 2, EntityEquipmentSlot.HEAD);
        this.type = type;
        setUnlocalizedName("barakoaMask." + type.name);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setRegistryName("barakoa_mask_" + type.name);
    }

    public Potion getPotion() {
        return type.potion;
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public int getColor(ItemStack itemStack) {
        return 0xFFFFFFFF;
    }

    @Override
    public ArmorMaterial getArmorMaterial() {
        return ArmorMaterial.CHAIN;
    }

    public enum Type {
        FURY(MobEffects.STRENGTH),
        FEAR(MobEffects.SPEED),
        RAGE(MobEffects.HASTE),
        BLISS(MobEffects.JUMP_BOOST),
        MISERY(MobEffects.RESISTANCE);

        public final String name;

        public final Potion potion;

        private Type(Potion potion) {
            this.potion = potion;
            name = name().toLowerCase();
        }
    }
}
