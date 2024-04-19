package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class EffectGeomancy extends MowzieEffect {
    public EffectGeomancy() {
        super(MobEffectCategory.BENEFICIAL, 0xCDFF78);
    }

    // TODO: Change to use block tags
    public static boolean isBlockDiggable(BlockState blockState) {
        if (!blockState.is(BlockTags.DIRT)
//                && !blockState.is(BlockTags.GRASS)
                && !blockState.is(Tags.Blocks.STONE)
//                && !blockState.is(BlockTags.CLAY)
                && !blockState.is(Tags.Blocks.SAND)
        ) {
            return false;
        }
        return blockState.getBlock() != Blocks.HAY_BLOCK
                && blockState.getBlock() != Blocks.NETHER_WART_BLOCK
                && !(blockState.getBlock() instanceof FenceBlock)
                && blockState.getBlock() != Blocks.SPAWNER
                && blockState.getBlock() != Blocks.BONE_BLOCK
                && blockState.getBlock() != Blocks.ENCHANTING_TABLE
                && blockState.getBlock() != Blocks.END_PORTAL_FRAME
                && blockState.getBlock() != Blocks.ENDER_CHEST
                && blockState.getBlock() != Blocks.SLIME_BLOCK
                && blockState.getBlock() != Blocks.HOPPER
                && blockState.getBlock() != BlockHandler.THATCH.get()
                && !blockState.hasBlockEntity();
    }

    public static boolean canUse(LivingEntity entity) {
        return entity.getMainHandItem().isEmpty() && entity.hasEffect(EffectHandler.GEOMANCY.get())	;
    }
}
