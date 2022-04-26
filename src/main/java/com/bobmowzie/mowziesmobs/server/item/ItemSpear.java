package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.google.common.collect.Sets;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSpear extends MowzieToolItem {
    public ItemSpear(Item.Properties properties) {
        super(-2 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_SPEAR.toolConfig.attackDamage.get().floatValue(), -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_SPEAR.toolConfig.attackSpeed.get().floatValue(), Tiers.STONE, Sets.newHashSet(), properties);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category == EnchantmentCategory.WEAPON || enchantment.category == EnchantmentCategory.BREAKABLE;
    }

    public static LivingEntity raytraceEntities(Level world, Player player, double range) {
        ItemSpear.HitResult result = new ItemSpear.HitResult();
        Vec3 pos = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 segment = player.getLookAngle();
        segment = pos.add(segment.x * range, segment.y * range, segment.z * range);
        result.setBlockHit(world.clip(new ClipContext(pos, segment, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)));
        double collidePosX, collidePosY, collidePosZ;
        if (result.blockHit != null) {
            collidePosX = result.blockHit.getLocation().x;
            collidePosY = result.blockHit.getLocation().y;
            collidePosZ = result.blockHit.getLocation().z;
        }
        else {
            Vec3 end = player.getLookAngle().scale(range).add(pos);
            collidePosX = end.x;
            collidePosY = end.y;
            collidePosZ = end.z;
        }

        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(pos.x, collidePosX), Math.min(pos.y, collidePosY), Math.min(pos.z, collidePosZ), Math.max(pos.x, collidePosX), Math.max(pos.y, collidePosY), Math.max(pos.z, collidePosZ)).inflate(1, 1, 1));
        LivingEntity closest = null;
        for (LivingEntity entity : entities) {
            if (entity == player) {
                continue;
            }
            float pad = entity.getPickRadius();
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            boolean hit = aabb.intersects(pos, segment);
            if (aabb.contains(pos) || hit) {
                result.addEntityHit(entity);
                if (closest == null || player.distanceTo(closest) > player.distanceTo(entity)) closest = entity;
            }
        }
        return closest;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_SPEAR.toolConfig;
    }

    public static class HitResult {
        private HitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public HitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult blockHit) {
            this.blockHit = blockHit;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
