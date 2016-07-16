package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

public class ItemBarakoaMask extends ItemArmor {
    private final BarakoaMaskType type;

    public ItemBarakoaMask(BarakoaMaskType type) {
        super(ArmorMaterial.LEATHER, 2, EntityEquipmentSlot.HEAD);
        this.type = type;
        setUnlocalizedName(type.getUnlocalizedName());
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setRegistryName("barakoa_" + type.name().toLowerCase());
    }

    public BarakoaMaskType getType() {
        return type;
    }

    public Potion getPotion() {
        return type.getPotion();
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

    public enum BarakoaMaskType {
        FURY(MobEffects.STRENGTH),
        FEAR(MobEffects.SPEED),
        RAGE(MobEffects.HASTE),
        BLISS(MobEffects.JUMP_BOOST),
        MISERY(MobEffects.RESISTANCE);

        private String unlocalizedName;
        private Potion potion;

        BarakoaMaskType(Potion potion) {
            this.potion = potion;
            int id = ordinal() + 1;
            this.unlocalizedName = "barakoaMask" + id;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        public Potion getPotion() {
            return potion;
        }
    }
}
