package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 11/1/15.
 */
public class ItemBarakoaMask extends ItemArmor {
    private static final String ARMOR_TEXTURE_STRING = MowziesMobs.MODID + ":textures/entity/textureTribesman";
    public int maskType;

    public ItemBarakoaMask(int i) {
        super(ArmorMaterial.CLOTH, 2, 0);
        setUnlocalizedName("barakoaMask" + i);
        setCreativeTab(MMTabs.generic);
        maskType = i;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return ARMOR_TEXTURE_STRING + maskType + ".png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        ModelBiped armorModel = null;
        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemBarakoaMask) {
                armorModel = MowziesMobs.proxy.getArmorModel(1);
            }
        }
        return armorModel;
    }
}
