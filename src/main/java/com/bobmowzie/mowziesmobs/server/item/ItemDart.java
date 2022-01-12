package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.IndirectEntityDamageSource;
import net.minecraft.resources.text.ITextComponent;
import net.minecraft.resources.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDart extends ArrowItem {
    public ItemDart(Item.Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new EntityDart(EntityHandler.DART, world, shooter);
    }

    public static DamageSource causeArrowDamage(EntityDart entitydart, Entity entity) {
        return new IndirectEntityDamageSource("dart", entitydart, entity).setProjectile();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
