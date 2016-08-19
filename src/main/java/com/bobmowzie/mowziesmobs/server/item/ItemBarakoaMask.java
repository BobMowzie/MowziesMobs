package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribePlayer;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeVillager;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBarakoaMask extends ItemArmor {
    private final Type type;

    public ItemBarakoaMask(Type type) {
        super(ArmorMaterial.LEATHER, 2, EntityEquipmentSlot.HEAD);
        this.type = type;
        setUnlocalizedName("barakoaMask." + type.name);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setRegistryName("barakoa_mask_" + type.name);
    }

    public Potion getPotion() {
        return type.potion;
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public int getColor(ItemStack itemStack) {
        return 0xFFFFFFFF;
    }

    @Override
    public ArmorMaterial getArmorMaterial() {
        return ArmorMaterial.CHAIN;
    }

    public enum Type {
        FURY(MobEffects.STRENGTH),
        FEAR(MobEffects.SPEED),
        RAGE(MobEffects.HASTE),
        BLISS(MobEffects.JUMP_BOOST),
        MISERY(MobEffects.RESISTANCE);

        public final String name;

        public final Potion potion;

        private Type(Potion potion) {
            this.potion = potion;
            name = name().toLowerCase();
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.inventory.armorItemInSlot(3) != null && playerIn.inventory.armorItemInSlot(3).getItem() instanceof ItemBarakoMask) {
            spawnBarakoa(this.type.name, playerIn);
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        else return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    private void spawnBarakoa(String typeName, EntityPlayer player) {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property.getPackSize() < 10) {
            player.playSound(MMSounds.ENTITY_BARAKO_BELLY, 1.5f, 1);
            player.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART, 1.5f, 0.5f);
            double angle = player.getRotationYawHead();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityTribePlayer tribesman = new EntityTribePlayer(player.worldObj);
            int mask;
            if (typeName.equals("fury")) mask = 1;
            else if (typeName.equals("fear")) mask = 2;
            else if (typeName.equals("rage")) mask = 3;
            else if (typeName.equals("bliss")) mask = 4;
            else mask = 5;
            tribesman.setMask(mask);
            tribesman.setPositionAndRotation(player.posX + 1 * Math.sin(-angle * Math.PI / 180), player.posY + 1.5, player.posZ + 1 * Math.cos(-angle * Math.PI / 180), (float) angle, 0);
            tribesman.setActive(false);
            tribesman.active = false;
            property.addPackMember(tribesman);
            if (!player.worldObj.isRemote) player.worldObj.spawnEntityInWorld(tribesman);
            tribesman.motionX = 0.5 * Math.sin(-angle * Math.PI / 180);
            tribesman.motionY = 0.5;
            tribesman.motionZ = 0.5 * Math.cos(-angle * Math.PI / 180);
        }
    }
}
