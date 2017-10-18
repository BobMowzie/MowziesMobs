package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemSpear extends ItemSword {
    public ItemSpear() {
        super(ToolMaterial.STONE);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("spear");
        setRegistryName("spear");
    }

    @Override
    public boolean hitEntity(ItemStack heldItemStack, EntityLivingBase entityHit, EntityLivingBase player) {
        if (entityHit instanceof EntityAnimal && Math.random() <= 0.33) {
            entityHit.setHealth(0);
        }
        return true;
    }

    public static EntityLivingBase raytraceEntities(World world, EntityPlayer player, double range) {
        ItemSpear.HitResult result = new ItemSpear.HitResult();
        Vec3d pos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d segment = player.getLookVec();
        segment = pos.addVector(segment.x * range, segment.y * range, segment.z * range);
        result.setBlockHit(world.rayTraceBlocks(pos, segment, false, true, true));
        double collidePosX, collidePosY, collidePosZ;
        if (result.blockHit != null) {
            collidePosX = result.blockHit.hitVec.x;
            collidePosY = result.blockHit.hitVec.y;
            collidePosZ = result.blockHit.hitVec.z;
        }
        else {
            Vec3d end = player.getLookVec().scale(range);
            collidePosX = end.x;
            collidePosY = end.y;
            collidePosZ = end.z;
        }

        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(Math.min(pos.x, collidePosX), Math.min(pos.y, collidePosY), Math.min(pos.z, collidePosZ), Math.max(pos.x, collidePosX), Math.max(pos.y, collidePosY), Math.max(pos.z, collidePosZ)).expand(1, 1, 1));
        EntityLivingBase closest = null;
        for (EntityLivingBase entity : entities) {
            if (entity == player) {
                continue;
            }
            float pad = entity.getCollisionBorderSize();
            AxisAlignedBB aabb = entity.getEntityBoundingBox().expand(pad, pad, pad);
            RayTraceResult hit = aabb.calculateIntercept(pos, segment);
            if (aabb.isVecInside(pos) || hit != null) {
                result.addEntityHit(entity);
                if (closest == null || player.getDistance(closest) > player.getDistance(entity)) closest = entity;
            }
        }
        return closest;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        ItemHandler.addItemText(this, tooltip);
    }

    public static class HitResult {
        private RayTraceResult blockHit;

        private List<EntityLivingBase> entities = new ArrayList<>();

        public RayTraceResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(RayTraceResult blockHit) {
            this.blockHit = blockHit;
        }

        public void addEntityHit(EntityLivingBase entity) {
            entities.add(entity);
        }
    }
}
