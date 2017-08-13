package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;

import java.util.List;

import javax.annotation.Nullable;

public class ItemMobRemover extends Item {
    public ItemMobRemover() {
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("mobRemover");
        setRegistryName("mob_remover");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (entity instanceof MowzieEntity) {
            entity.setDead();
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}