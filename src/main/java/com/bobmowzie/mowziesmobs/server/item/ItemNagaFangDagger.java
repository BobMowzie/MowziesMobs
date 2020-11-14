package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.google.common.collect.Sets;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemTier;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNagaFangDagger extends ToolItem {
    public ItemNagaFangDagger(Item.Properties properties) {
        super(-2 + ConfigHandler.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolData.attackDamage, -4f + ConfigHandler.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolData.attackSpeed, ItemTier.STONE, Sets.newHashSet(), properties);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.type == EnchantmentType.WEAPON || enchantment.type == EnchantmentType.BREAKABLE || enchantment.type == EnchantmentType.ALL;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (super.hitEntity(stack, target, attacker)) {
            target.addPotionEffect(new EffectInstance(Effects.POISON, ConfigHandler.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.poisonDuration, 3, false, true));
            return true;
        }
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        Item item = repair.getItem();
        return item instanceof ItemNagaFang;
    }
}
