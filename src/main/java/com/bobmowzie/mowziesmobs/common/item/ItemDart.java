package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.common.entity.EntityDart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

import java.util.List;

public class ItemDart extends Item {
    public ItemDart() {
        super();
        setUnlocalizedName("dart");
        setTextureName(MowziesMobs.MODID + ":dart");
        setCreativeTab(CreativeTabHandler.INSTANCE.generic);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemDart item = (ItemDart) stack.getItem();
    }


    public static DamageSource causeArrowDamage(EntityDart entitydart, Entity entity) {
        return (new EntityDamageSourceIndirect("dart", entitydart, entity)).setProjectile();
    }
}
