package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 6/26/15.
 */
public class ItemMobRemover extends Item
{

    public ItemMobRemover()
    {
        setUnlocalizedName("mobRemover");
        setTextureName(MowziesMobs.MODID + ":mobRemover");
        setCreativeTab(MMTabs.generic);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (entity instanceof MMEntityBase) entity.setDead();
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack heldItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            EntitySolarBeam solarBeam = new EntitySolarBeam(world, player, x, y + 2, z, 2, 0, 80);
            world.spawnEntityInWorld(solarBeam);
        }
        return true;
    }
}