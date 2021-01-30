package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageBlackPinkInYourArea;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.BiConsumer;

public final class BlackPinkInYourArea implements BiConsumer<World, AbstractMinecartEntity> {
    private BlackPinkInYourArea() {}

    @Override
    public void accept(World world, AbstractMinecartEntity minecart) {
        BlockState state = minecart.getDisplayTile();
        if (state.getBlock() != BlockHandler.GROTTOL.get()) {
            state = BlockHandler.GROTTOL.get().getDefaultState();
            minecart.setDisplayTileOffset(minecart.getDefaultDisplayTileOffset());
        }
        minecart.setDisplayTile(state.with(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK));
        ((ServerWorld) minecart.world).getChunkProvider().sendToAllTracking(minecart, new MessageBlackPinkInYourArea(minecart));
    }

    public static BlackPinkInYourArea create() {
        return new BlackPinkInYourArea();
    }
}
