package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

public class ItemBarakoaMask extends ItemArmor {
    static final String ARMOR_TEXTURE_FORMAT = MowziesMobs.MODID + ":textures/entity/textureTribesman%s.png";

    private final BarakoaMaskType type;

    public ItemBarakoaMask(BarakoaMaskType type) {
        super(ArmorMaterial.CLOTH, 2, 0);
        this.type = type;
        setUnlocalizedName(type.getUnlocalizedName());
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
    }

    public BarakoaMaskType getType() {
        return type;
    }

    public int getPotionEffectId() {
        return type.getPotionID();
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String variant) {
        return type.getArmorTexture();
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        ModelBiped armorModel = null;
        if (itemStack != null && itemStack.getItem() instanceof ItemBarakoaMask) {
            armorModel = MowziesMobs.PROXY.getArmorModel(1);
        }
        return armorModel;
    }

    @Override
    public void registerIcons(IIconRegister registrar) {
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
        FURY(5), FEAR(1), RAGE(3), BLISS(8), MISERY(11);

        public static final BarakoaMaskType[] VALUES = values();
        private String unlocalizedName;
        private int potionID;
        private String armorTexture;

        BarakoaMaskType(int potionID) {
            this.potionID = potionID;
            int id = ordinal() + 1;
            this.unlocalizedName = "barakoaMask" + id;
            this.armorTexture = String.format(ItemBarakoaMask.ARMOR_TEXTURE_FORMAT, id);
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        public int getPotionID() {
            return potionID;
        }

        public String getArmorTexture() {
            return armorTexture;
        }
    }
}
