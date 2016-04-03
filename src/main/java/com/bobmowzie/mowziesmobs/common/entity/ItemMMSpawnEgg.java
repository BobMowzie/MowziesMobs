package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.creativetab.CreativeTabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class ItemMMSpawnEgg extends Item {
    private IIcon overlay;

    public ItemMMSpawnEgg() {
        setUnlocalizedName("monsterPlacer");
        setTextureName("spawn_egg");
        setHasSubtypes(true);
        setCreativeTab(CreativeTabHandler.INSTANCE.generic);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String name = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
        String entityName = EntityHandler.INSTANCE.getEntityNameById(itemStack.getItemDamage());
        if (entityName != null) {
            name = name + " " + StatCollector.translateToLocal("entity." + entityName + ".name");
        }
        return name;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int pass) {
        MMEntityEggInfo info = EntityHandler.INSTANCE.getEntityEggInfo(Integer.valueOf(itemStack.getItemDamage()));
        return info == null ? 0xFFFFFF : (pass == 0 ? info.primaryColor : info.secondaryColor);
    }

    @Override
    public boolean onItemUse(ItemStack heldItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double yOffset = 0;
            if (side == 1 && block.getRenderType() == 11) {
                yOffset = 0.5;
            }
            Entity entity = spawnCreature(world, heldItemStack.getItemDamage(), x + 0.5, y + yOffset, z + 0.5);
            if (entity != null) {
                if (entity instanceof EntityLivingBase && heldItemStack.hasDisplayName()) {
                    ((EntityLiving) entity).setCustomNameTag(heldItemStack.getDisplayName());
                }
                if (!player.capabilities.isCreativeMode) {
                    heldItemStack.stackSize--;
                }
            }

            return true;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack heldItemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return heldItemStack;
        } else {
            MovingObjectPosition hitVector = getMovingObjectPositionFromPlayer(world, player, true);

            if (hitVector == null) {
                return heldItemStack;
            } else {
                if (hitVector.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int x = hitVector.blockX;
                    int y = hitVector.blockY;
                    int z = hitVector.blockZ;
                    if (!world.canMineBlock(player, x, y, z)) {
                        return heldItemStack;
                    }
                    if (!player.canPlayerEdit(x, y, z, hitVector.sideHit, heldItemStack)) {
                        return heldItemStack;
                    }
                    if (world.getBlock(x, y, z) instanceof BlockLiquid) {
                        Entity entity = spawnCreature(world, heldItemStack.getItemDamage(), x, y, z);
                        if (entity instanceof EntityLivingBase && heldItemStack.hasDisplayName()) {
                            ((EntityLiving) entity).setCustomNameTag(heldItemStack.getDisplayName());
                        }
                        if (!player.capabilities.isCreativeMode) {
                            heldItemStack.stackSize--;
                        }
                    }
                }
                return heldItemStack;
            }
        }
    }

    public static Entity spawnCreature(World world, int id, double x, double y, double z) {
        if (EntityHandler.INSTANCE.hasEntityEggInfo(id)) {
            Entity entity = EntityHandler.INSTANCE.createEntityById(id, world);
            if (entity instanceof EntityLivingBase) {
                EntityLiving entityLiving = (EntityLiving) entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360), 0.0F);
                entityLiving.rotationYawHead = entityLiving.rotationYaw;
                entityLiving.renderYawOffset = entityLiving.rotationYaw;
                entityLiving.onSpawnWithEgg((IEntityLivingData) null);
                world.spawnEntityInWorld(entity);
                entityLiving.playLivingSound();
            }
            return entity;
        }
        return null;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        return pass > 0 ? overlay : super.getIconFromDamageForRenderPass(damage, pass);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List itemStacks) {
        Iterator<MMEntityEggInfo> iterator = EntityHandler.INSTANCE.getEntityEggInfoIterator();
        while (iterator.hasNext()) {
            MMEntityEggInfo info = iterator.next();
            itemStacks.add(new ItemStack(item, 1, info.spawnedID));
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        overlay = register.registerIcon(getIconString() + "_overlay");
    }
}
