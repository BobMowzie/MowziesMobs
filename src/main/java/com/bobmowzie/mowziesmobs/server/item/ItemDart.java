package com.bobmowzie.mowziesmobs.server.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;

public class ItemDart extends Item {
    public ItemDart() {
        super();
        setUnlocalizedName("dart");
        setTextureName(MowziesMobs.MODID + ":dart");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
    }

    public static DamageSource causeArrowDamage(EntityDart entitydart, Entity entity) {
        return (new EntityDamageSourceIndirect("dart", entitydart, entity)).setProjectile();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemDart item = (ItemDart) stack.getItem();
    }
}
