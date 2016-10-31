package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBarakoaMask extends ItemArmor implements BarakoaMask {
    private final MaskType type;

    public ItemBarakoaMask(MaskType type) {
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        ItemStack headStack = player.inventory.armorInventory[3];
        if (headStack != null && headStack.getItem() instanceof ItemBarakoMask) {
            if (!player.worldObj.isRemote) {
                spawnBarakoa(type, player);
            }
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        else return super.onItemRightClick(stack, world, player, hand);
    }

    private void spawnBarakoa(MaskType mask, EntityPlayer player) {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property.getPackSize() < 10) {
            player.playSound(MMSounds.ENTITY_BARAKO_BELLY, 1.5f, 1);
            player.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART, 1.5f, 0.5f);
            double angle = player.getRotationYawHead();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityBarakoanToPlayer barakoa = new EntityBarakoanToPlayer(player.worldObj);
            barakoa.setMask(mask);
            barakoa.setPositionAndRotation(player.posX + 1 * Math.sin(-angle * (Math.PI / 180)), player.posY + 1.5, player.posZ + 1 * Math.cos(-angle * (Math.PI / 180)), (float) angle, 0);
            barakoa.setActive(false);
            barakoa.active = false;
            property.addPackMember(barakoa);
            player.worldObj.spawnEntityInWorld(barakoa);
            barakoa.motionX = 0.5 * Math.sin(-angle * Math.PI / 180);
            barakoa.motionY = 0.5;
            barakoa.motionZ = 0.5 * Math.cos(-angle * Math.PI / 180);
        }
    }
}
