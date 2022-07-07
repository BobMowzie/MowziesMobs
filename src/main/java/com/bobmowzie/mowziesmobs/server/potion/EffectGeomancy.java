package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectGeomancy extends MowzieEffect {
    public EffectGeomancy() {
        super(MobEffectCategory.BENEFICIAL, 0xCDFF78);
    }

    public static boolean isBlockDiggable(BlockState blockState) {
        Material mat = blockState.getMaterial();
        if (mat != Material.GRASS
                && mat != Material.DIRT
                && mat != Material.STONE
                && mat != Material.CLAY
                && mat != Material.SAND
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
        return entity.getMainHandItem().isEmpty() && entity.hasEffect(EffectHandler.GEOMANCY);
    }
}
