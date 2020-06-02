package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.google.common.collect.Sets;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNagaFangDagger extends ItemTool {
    public ItemNagaFangDagger() {
        super(1 * ConfigHandler.TOOLS_AND_ABILITIES.nagaDaggerAttackMultiplier, 1.5f, ToolMaterial.STONE, Sets.newHashSet());
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setTranslationKey("nagaFangDagger");
        setRegistryName("naga_fang_dagger");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (super.hitEntity(stack, target, attacker)) {
            target.addPotionEffect(new PotionEffect(MobEffects.POISON, (int)(40 * ConfigHandler.TOOLS_AND_ABILITIES.nagaDaggerAttackMultiplier), 3, false, true));
            return true;
        }
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        Item item = repair.getItem();
        return item instanceof ItemNagaFang;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
