package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.sounds.ActionResultType;
import net.minecraft.core.Direction;
import net.minecraft.sounds.Hand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.IServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed(Item.Properties properties) {
        super(properties);
    }

    public Entity spawnCreature(IServerLevel world, MobEntity entity, double x, double y, double z) {
        if (entity != null) {
            entity.setLocationAndAngles(x + 0.5, y, z + 0.5, world.getLevel().random.nextFloat() * 360 - 180, 0);
            entity.getYRot()Head = entity.getYRot();
            entity.yBodyRot = entity.getYRot();
            entity.finalizeSpawn(world, world.getDifficultyForLocation(entity.getPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            if (!entity.checkSpawnRules(world, MobSpawnType.MOB_SUMMONED)) {
                return null;
            }
            level.addFreshEntity(entity);
        }
        return entity;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Player player = context.getPlayer();
        if (player == null) return ActionResultType.FAIL;
        Hand hand = context.getHand();
        Direction facing = context.getFace();
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = context.getPos();
        Level world = context.getLevel();
        if (level.isClientSide) {
            return ActionResultType.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return ActionResultType.FAIL;
        }
        Entity entity = spawnCreature((ServerLevel) world, new EntityBabyFoliaath(EntityHandler.BABY_FOLIAATH, world), pos.x(), pos.y() + 1, pos.z());
        if (entity != null) {
            if (entity instanceof LivingEntity && stack.hasDisplayName()) {
                entity.setCustomName(stack.getDisplayName());
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TextComponent(getDescriptionId() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
