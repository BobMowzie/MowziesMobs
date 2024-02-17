package com.bobmowzie.mowziesmobs.server.item;

import java.util.List;

import javax.annotation.Nullable;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed(Item.Properties properties) {
        super(properties);
    }

    public Entity spawnCreature(ServerLevelAccessor world, Mob entity, double x, double y, double z) {
        if (entity != null) {
            entity.moveTo(x + 0.5, y, z + 0.5, world.getLevel().random.nextFloat() * 360 - 180, 0);
            entity.yHeadRot = entity.getYRot();
            entity.yBodyRot = entity.getYRot();
            entity.finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            if (!entity.checkSpawnRules(world, MobSpawnType.MOB_SUMMONED)) {
                return null;
            }
            world.addFreshEntity(entity);
        }
        return entity;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        InteractionHand hand = context.getHand();
        Direction facing = context.getClickedFace();
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (!player.mayUseItemAt(pos.relative(facing), facing, stack)) {
            return InteractionResult.FAIL;
        }
        Entity entity = spawnCreature((ServerLevel) world, new EntityBabyFoliaath(EntityHandler.BABY_FOLIAATH.get(), world), pos.getX(), pos.getY() + 1, pos.getZ());
        if (entity != null) {
            if (entity instanceof LivingEntity && stack.hasCustomHoverName()) {
                entity.setCustomName(stack.getHoverName());
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
