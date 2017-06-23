package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by Josh on 6/6/2017.
 */
public class ItemIceCrystal extends Item {
    public ItemIceCrystal() {
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("icecrystal");
        setRegistryName("icecrystal");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(playerIn, MowziePlayerProperties.class);
        if (!property.usingIceBreath) {
            property.icebreath = new EntityIceBreath(worldIn, playerIn);
            property.icebreath.setPositionAndRotation(playerIn.posX, playerIn.posY + playerIn.eyeHeight - 0.75f, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
            if (!worldIn.isRemote) worldIn.spawnEntity(property.icebreath);
            property.usingIceBreath = true;
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
