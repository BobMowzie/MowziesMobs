package com.bobmowzie.mowziesmobs.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GongBlockEntity extends BlockEntity {
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;
    public Direction facing;

    public GongBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.GONG_BLOCK_ENTITY.get(), pos, state);
        facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public boolean triggerEvent(int p_58837_, int p_58838_) {
        if (p_58837_ == 1) {
            this.clickDirection = Direction.from3DDataValue(p_58838_);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.triggerEvent(p_58837_, p_58838_);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, GongBlockEntity entity) {
        if (entity.shaking) {
            ++entity.ticks;
        }

        if (entity.ticks >= 50) {
            entity.shaking = false;
            entity.ticks = 0;
        }

        if (entity.ticks >= 5) {
            level.playSound((Player)null, pos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void onHit(Direction p_58835_) {
        BlockPos blockpos = this.getBlockPos();
        this.clickDirection = p_58835_;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }

        this.level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, p_58835_.get3DDataValue());
    }
}
