package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed() {
        setUnlocalizedName("foliaathSeed");
        setTextureName(MowziesMobs.MODID + ":foliaathSeed");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
    }

    public Entity spawnCreature(World world, EntityLiving entity, double x, double y, double z) {
        if (entity != null) {
            entity.setLocationAndAngles(x + 0.5, y, z + 0.5, world.rand.nextFloat() * 360 - 180, 0);
            entity.rotationYawHead = entity.rotationYaw;
            entity.renderYawOffset = entity.rotationYaw;
            entity.onSpawnWithEgg(null);
            if (!entity.getCanSpawnHere()) {
                return null;
            }
            world.spawnEntityInWorld(entity);
        }

        return entity;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int meta, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[meta];
            y += Facing.offsetsYForSide[meta];
            z += Facing.offsetsZForSide[meta];
            Entity entity = spawnCreature(world, new EntityBabyFoliaath(world), x, y, z);
            if (entity != null) {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                    ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                }
                if (!player.capabilities.isCreativeMode) {
                    --stack.stackSize;
                }
            }
            return true;
        }
    }
}
