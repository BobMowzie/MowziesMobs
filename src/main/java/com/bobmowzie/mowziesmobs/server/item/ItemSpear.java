package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.google.common.collect.Sets;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSpear extends ItemTool {
    public ItemSpear() {
        super(3 * ConfigHandler.TOOLS_AND_ABILITIES.spearAttackMultiplier, 1.6f, ToolMaterial.STONE, Sets.newHashSet());
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setTranslationKey("spear");
        setRegistryName("spear");
    }

    @Override
    public boolean hitEntity(ItemStack heldItemStack, EntityLivingBase entityHit, EntityLivingBase player) {
        heldItemStack.damageItem(1, player);
        if (entityHit instanceof EntityAnimal && entityHit.getMaxHealth() <= 30 && Math.random() <= 0.33) {
            entityHit.setHealth(0);
        }
        return true;
    }

    public static EntityLivingBase raytraceEntities(World world, EntityPlayer player, double range) {
        ItemSpear.HitResult result = new ItemSpear.HitResult();
        Vec3d pos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d segment = player.getLookVec();
        segment = pos.add(segment.x * range, segment.y * range, segment.z * range);
        result.setBlockHit(world.rayTraceBlocks(pos, segment, false, true, true));
        double collidePosX, collidePosY, collidePosZ;
        if (result.blockHit != null) {
            collidePosX = result.blockHit.hitVec.x;
            collidePosY = result.blockHit.hitVec.y;
            collidePosZ = result.blockHit.hitVec.z;
        }
        else {
            Vec3d end = player.getLookVec().scale(range).add(pos);
            collidePosX = end.x;
            collidePosY = end.y;
            collidePosZ = end.z;
        }

        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(Math.min(pos.x, collidePosX), Math.min(pos.y, collidePosY), Math.min(pos.z, collidePosZ), Math.max(pos.x, collidePosX), Math.max(pos.y, collidePosY), Math.max(pos.z, collidePosZ)).grow(1, 1, 1));
        EntityLivingBase closest = null;
        for (EntityLivingBase entity : entities) {
            if (entity == player) {
                continue;
            }
            float pad = entity.getCollisionBorderSize();
            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(pad, pad, pad);
            RayTraceResult hit = aabb.calculateIntercept(pos, segment);
            if (aabb.contains(pos) || hit != null) {
                result.addEntityHit(entity);
                if (closest == null || player.getDistance(closest) > player.getDistance(entity)) closest = entity;
            }
        }
        return closest;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
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
