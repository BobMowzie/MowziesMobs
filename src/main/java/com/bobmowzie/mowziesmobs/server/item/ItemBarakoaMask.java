package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBarakoaMask extends ItemArmor implements BarakoaMask {
    private final MaskType type;

    private static int d = ConfigHandler.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorData.damageReduction;
    private static ArmorMaterial ARMOR_BARAKOA_MASK = EnumHelper.addArmorMaterial("BARAKOA_MASK", "leather", 5, new int[]{d, d, d, d}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, ConfigHandler.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorData.toughness);

    public ItemBarakoaMask(MaskType type) {
        super(ARMOR_BARAKOA_MASK, 2, EntityEquipmentSlot.HEAD);
        this.type = type;
        setTranslationKey("barakoaMask." + type.name);
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
        return ARMOR_BARAKOA_MASK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ItemStack headStack = player.inventory.armorInventory.get(3);
        if (headStack.getItem() instanceof ItemBarakoMask) {
            spawnBarakoa(type, player, (float)stack.getItemDamage() / (float)stack.getMaxDamage());
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        else return super.onItemRightClick(world, player, hand);
    }

    private void spawnBarakoa(MaskType mask, EntityPlayer player, float durability) {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property.getPackSize() < 10) {
            player.playSound(MMSounds.ENTITY_BARAKO_BELLY, 1.5f, 1);
            player.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART, 1.5f, 0.5f);
            double angle = player.getRotationYawHead();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityBarakoanToPlayer barakoa = new EntityBarakoanToPlayer(player.world, player);
            barakoa.setMask(mask);
//            property.addPackMember(barakoa);
            if (!player.world.isRemote) {
                int weapon;
                if (mask != MaskType.FURY) weapon = barakoa.randomizeWeapon();
                else weapon = 0;
                barakoa.setWeapon(weapon);
                barakoa.setPositionAndRotation(player.posX + 1 * Math.sin(-angle * (Math.PI / 180)), player.posY + 1.5, player.posZ + 1 * Math.cos(-angle * (Math.PI / 180)), (float) angle, 0);
                barakoa.setActive(false);
                barakoa.active = false;
                player.world.spawnEntity(barakoa);
                barakoa.motionX = 0.5 * Math.sin(-angle * Math.PI / 180);
                barakoa.motionY = 0.5;
                barakoa.motionZ = 0.5 * Math.cos(-angle * Math.PI / 180);
                //System.out.println((1.0f - durability) * barakoa.getMaxHealth());
                barakoa.setHealth((1.0f - durability) * barakoa.getMaxHealth());
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
