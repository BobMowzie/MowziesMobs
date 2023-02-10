package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.block.entity.GongBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GongBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
//    public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
    private static final VoxelShape EAST_WEST_FLOOR_SHAPE = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);

    public GongBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE));
    }

    public void neighborChanged(BlockState p_49729_, Level p_49730_, BlockPos p_49731_, Block p_49732_, BlockPos p_49733_, boolean p_49734_) {
        boolean flag = p_49730_.hasNeighborSignal(p_49731_);
        if (flag != p_49729_.getValue(POWERED)) {
            if (flag) {
                this.attemptToRing(p_49730_, p_49731_, (Direction)null);
            }

            p_49730_.setBlock(p_49731_, p_49729_.setValue(POWERED, Boolean.valueOf(flag)), 3);
        }

    }

    public void onProjectileHit(Level p_49708_, BlockState p_49709_, BlockHitResult p_49710_, Projectile p_49711_) {
        Entity entity = p_49711_.getOwner();
        Player player = entity instanceof Player ? (Player)entity : null;
        this.onHit(p_49708_, p_49709_, p_49710_, player, true);
    }

    public InteractionResult use(BlockState p_49722_, Level p_49723_, BlockPos p_49724_, Player p_49725_, InteractionHand p_49726_, BlockHitResult p_49727_) {
        return this.onHit(p_49723_, p_49722_, p_49727_, p_49725_, true) ? InteractionResult.sidedSuccess(p_49723_.isClientSide) : InteractionResult.PASS;
    }

    public boolean onHit(Level p_49702_, BlockState p_49703_, BlockHitResult p_49704_, @javax.annotation.Nullable Player p_49705_, boolean p_49706_) {
        Direction direction = p_49704_.getDirection();
        BlockPos blockpos = p_49704_.getBlockPos();
        boolean flag = !p_49706_ || this.isProperHit(p_49703_, direction, p_49704_.getLocation().y - (double)blockpos.getY());
        if (flag) {
            boolean flag1 = this.attemptToRing(p_49705_, p_49702_, blockpos, direction);
            if (flag1 && p_49705_ != null) {
                p_49705_.awardStat(Stats.BELL_RING);
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean isProperHit(BlockState p_49740_, Direction p_49741_, double p_49742_) {
        if (p_49741_.getAxis() != Direction.Axis.Y && !(p_49742_ > (double)0.8124F)) {
            Direction direction = p_49740_.getValue(FACING);
            return direction.getAxis() == p_49741_.getAxis();
        } else {
            return false;
        }
    }

    public boolean attemptToRing(Level p_49713_, BlockPos p_49714_, @javax.annotation.Nullable Direction p_49715_) {
        return this.attemptToRing((Entity)null, p_49713_, p_49714_, p_49715_);
    }

    public boolean attemptToRing(@javax.annotation.Nullable Entity p_152189_, Level p_152190_, BlockPos p_152191_, @javax.annotation.Nullable Direction p_152192_) {
        BlockEntity blockentity = p_152190_.getBlockEntity(p_152191_);
        if (!p_152190_.isClientSide && blockentity instanceof GongBlockEntity) {
            if (p_152192_ == null) {
                p_152192_ = p_152190_.getBlockState(p_152191_).getValue(FACING);
            }

            ((GongBlockEntity)blockentity).onHit(p_152192_);
            p_152190_.playSound((Player)null, p_152191_, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
            p_152190_.gameEvent(p_152189_, GameEvent.RING_BELL, p_152191_);
            return true;
        } else {
            return false;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49751_) {
        p_49751_.add(FACING, POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GongBlockEntity(pos, state);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152194_, BlockState p_152195_, BlockEntityType<T> p_152196_) {
        return createTickerHelper(p_152196_, BlockEntityHandler.GONG_BLOCK_ENTITY.get(), GongBlockEntity::tick);
    }

    public VoxelShape getCollisionShape(BlockState p_49760_, BlockGetter p_49761_, BlockPos p_49762_, CollisionContext p_49763_) {
        return this.getVoxelShape(p_49760_);
    }

    public VoxelShape getShape(BlockState p_49755_, BlockGetter p_49756_, BlockPos p_49757_, CollisionContext p_49758_) {
        return this.getVoxelShape(p_49755_);
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    private VoxelShape getVoxelShape(BlockState p_49767_) {
        Direction direction = p_49767_.getValue(FACING);
        return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_FLOOR_SHAPE : NORTH_SOUTH_FLOOR_SHAPE;
    }

    public boolean isPathfindable(BlockState p_49717_, BlockGetter p_49718_, BlockPos p_49719_, PathComputationType p_49720_) {
        return false;
    }

    public PushReaction getPistonPushReaction(BlockState p_49765_) {
        return PushReaction.DESTROY;
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        Direction.Axis direction$axis = direction.getAxis();
        if (direction$axis == Direction.Axis.Y) {
            BlockState blockstate = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
            if (blockstate.canSurvive(context.getLevel(), blockpos)) {
                return blockstate;
            }
        } else {
            BlockState blockstate1 = this.defaultBlockState().setValue(FACING, direction.getOpposite());
            if (blockstate1.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate1;
            }
        }

        return null;
    }
}
