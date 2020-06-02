package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageBlackPinkInYourArea;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

public final class BlackPinkInYourArea implements BiConsumer<World, EntityMinecart> {
    private BlackPinkInYourArea() {}

    @Override
    public void accept(World world, EntityMinecart minecart) {
        IBlockState state = minecart.getDisplayTile();
        if (state.getBlock() != BlockHandler.GROTTOL) {
            state = BlockHandler.GROTTOL.getDefaultState();
            minecart.setDisplayTileOffset(minecart.getDefaultDisplayTileOffset());
        }
        minecart.setDisplayTile(state.withProperty(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK));
        MowziesMobs.NETWORK_WRAPPER.sendToAllTracking(new MessageBlackPinkInYourArea(minecart), minecart);
    }

    public static BlackPinkInYourArea create() {
        return new BlackPinkInYourArea();
    }
}
