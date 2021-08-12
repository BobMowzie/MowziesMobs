package com.bobmowzie.mowziesmobs.server.potion;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class EffectGeomancy extends MowzieEffect {
    public EffectGeomancy() {
        super(EffectType.BENEFICIAL, 0xCDFF78);
    }

    public static boolean isBlockDiggable(BlockState blockState) {
        Material mat = blockState.getMaterial();
        if (mat != Material.ORGANIC
                && mat != Material.EARTH
                && mat != Material.ROCK
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
                && !blockState.hasTileEntity();
    }

    public static boolean canUse(LivingEntity entity) {
        return entity.getHeldItemMainhand().isEmpty() && entity.isPotionActive(EffectHandler.GEOMANCY);
    }
}
