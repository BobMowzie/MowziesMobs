package com.bobmowzie.mowziesmobs.server.entity.grottol;

import java.util.function.BiConsumer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageBlackPinkInYourArea;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;

public final class BlackPinkInYourArea implements BiConsumer<World, EntityMinecart> {
    private BlackPinkInYourArea() {}

    @Override
    public void accept(World world, EntityMinecart minecart) {
        IBlockState state = minecart.getDisplayTile();
        if (state.getBlock() != BlockHandler.GROTTOL) {
            state = BlockHandler.GROTTOL.getDefaultState();
        }
        minecart.setDisplayTile(state.withProperty(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK));
        minecart.setHasDisplayTile(true);
        MowziesMobs.NETWORK_WRAPPER.sendToAllTracking(new MessageBlackPinkInYourArea(minecart), minecart);
    }

    public static BlackPinkInYourArea create() {
        return new BlackPinkInYourArea();
    }
}
