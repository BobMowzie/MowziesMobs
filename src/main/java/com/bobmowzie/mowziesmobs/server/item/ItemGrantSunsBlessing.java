package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Josh on 10/31/2016.
 */
public class ItemGrantSunsBlessing extends Item {
    public ItemGrantSunsBlessing(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        playerIn.addPotionEffect(new EffectInstance(PotionHandler.SUNS_BLESSING, 24000 * 3, 0, false, false));
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
