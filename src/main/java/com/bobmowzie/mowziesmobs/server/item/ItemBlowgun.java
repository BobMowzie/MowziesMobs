package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.Hand;
import net.minecraft.resources.SoundEvents;
import net.minecraft.resources.SoundCategory;
import net.minecraft.resources.text.ITextComponent;
import net.minecraft.resources.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ItemBlowgun extends BowItem {
    public static final Predicate<ItemStack> DARTS = (p_220002_0_) -> {
        return p_220002_0_.getItem() == ItemHandler.DART;
    };

    public ItemBlowgun(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof Player) {
            Player Player = (Player)entityLiving;
            boolean flag = Player.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = Player.findAmmo(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, Player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = Player.abilities.isCreativeMode || (itemstack.getItem() instanceof ItemDart && ((ItemDart)itemstack.getItem()).isInfinite(itemstack, stack, Player));
                    if (!worldIn.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ItemDart ? itemstack.getItem() : ItemHandler.DART);
                        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, Player);
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.setDirectionAndMovement(Player, Player.getXRot(), Player.getYRot(), 0.0F, f * 1.1F /*ALTERED FROM PARENT*/, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            abstractarrowentity.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            abstractarrowentity.setFire(100);
                        }

                        stack.damageItem(1, Player, (player) -> {
                            player.sendBreakAnimation(Player.getActiveHand());
                        });
                        if (flag1 || Player.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addEntity(abstractarrowentity);
                    }

                    worldIn.playSound((Player)null, Player.getPosX(), Player.getPosY(), Player.getPosZ(), MMSounds.ENTITY_BARAKOA_BLOWDART.get(), SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F); //CHANGED FROM PARENT CLASS
                    if (!flag1 && !Player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            Player.inventory.deleteStack(itemstack);
                        }
                    }

                    Player.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static float getArrowVelocity(int charge) {
        float f = (float)charge / 5.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return DARTS;
    }

    @Override
    public Predicate<ItemStack> getAmmoPredicate() {
        return DARTS;
    }
}
