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

public class ItemWroughtHelm extends ItemArmor
{
    private static final String ARMOR_TEXTURE_STRING = MowziesMobs.MODID + ":textures/items/modeled/textureWroughtHelm.png";

    public ItemWroughtHelm()
    {
        super(ArmorMaterial.IRON, 2, 0);
        setUnlocalizedName("wroughtHelm");
        setCreativeTab(MMTabs.generic);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        super.onArmorTick(world, player, itemStack);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack materialItemStack)
    {
        return false;
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }

    @Override
    public int getDamage(ItemStack stack)
    {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage)
    {

    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return ARMOR_TEXTURE_STRING;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        ModelBiped armorModel = null;
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemWroughtHelm)
            {
                armorModel = MowziesMobs.proxy.getArmorModel();
            }
        }
        return armorModel;
    }
}
