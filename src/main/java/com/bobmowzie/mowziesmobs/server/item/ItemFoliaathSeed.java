package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed(Item.Properties properties) {
        super(properties);
    }

    public Entity spawnCreature(IServerWorld world, MobEntity entity, double x, double y, double z) {
        if (entity != null) {
            entity.setLocationAndAngles(x + 0.5, y, z + 0.5, world.getWorld().rand.nextFloat() * 360 - 180, 0);
            entity.rotationYawHead = entity.rotationYaw;
            entity.renderYawOffset = entity.rotationYaw;
            entity.onInitialSpawn(world, world.getDifficultyForLocation(entity.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
            if (!entity.canSpawn(world, SpawnReason.MOB_SUMMONED)) {
                return null;
            }
            world.addEntity(entity);
        }
        return entity;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.FAIL;
        Hand hand = context.getHand();
        Direction facing = context.getFace();
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return ActionResultType.FAIL;
        }
        Entity entity = spawnCreature((ServerWorld) world, new EntityBabyFoliaath(EntityHandler.BABY_FOLIAATH.get(), world), pos.getX(), pos.getY() + 1, pos.getZ());
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
