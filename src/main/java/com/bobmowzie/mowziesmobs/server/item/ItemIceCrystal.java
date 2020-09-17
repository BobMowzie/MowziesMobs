package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Josh on 6/6/2017.
 */
public class ItemIceCrystal extends Item {
    public ItemIceCrystal(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

//    @Override
//    public int getMaxItemUseDuration(ItemStack stack) {
//        return 40;
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn.getHeldItemOffhand().getItem() != Items.SHIELD) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(playerIn, MowziePlayerProperties.class);
            if (stack.getDamage() + 20 < stack.getMaxDamage() || ConfigHandler.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
                if (!property.usingIceBreath) {
                    property.icebreath = new EntityIceBreath(worldIn, playerIn);
                    property.icebreath.setPositionAndRotation(playerIn.posX, playerIn.posY + playerIn.eyeHeight - 0.5f, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
                    if (!worldIn.isRemote) worldIn.addEntity(property.icebreath);
                    property.usingIceBreath = true;
                }
                stack.damageItem(20, playerIn, p -> p.sendBreakAnimation(handIn));
                showDurabilityBar(playerIn.getHeldItem(handIn));
                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
            } else {
                property.icebreath.remove();
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
