package com.bobmowzie.mowziesmobs.server.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RakedSandBlock extends SandBlock {
    public static final EnumProperty<RakedSandShape> SHAPE = EnumProperty.create("shape", RakedSandShape.class);

    public RakedSandBlock(int dustColor, Properties properties) {
        super(dustColor, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RakedSandShape.NORTH_SOUTH));
    }

    public void tick(BlockState blockState, ServerLevel level, BlockPos pos, Random random) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, pos, Blocks.SAND.defaultBlockState());
            this.falling(fallingblockentity);
        }
    }

    public static boolean isRakedSand(Level level, BlockPos pos) {
        return isRakedSand(level.getBlockState(pos));
    }

    public static boolean isRakedSand(BlockState state) {
        return state.getBlock() instanceof RakedSandBlock;
    }

    public boolean canSurvive(BlockState p_49395_, LevelReader p_49396_, BlockPos p_49397_) {
        return canSupportRigidBlock(p_49396_, p_49397_.below());
    }

    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState previousState, boolean p_49412_) {
        if (!previousState.is(blockState.getBlock())) {
            this.updateState(blockState, level, pos, p_49412_);
        }
    }

    public BlockState updateState(BlockState state, Level level, BlockPos pos, boolean p_49393_) {
        state = this.updateDir(level, pos, state, true);

        return state;
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos1, boolean p_49382_) {
        if (!level.isClientSide && level.getBlockState(pos).is(this)) {
            this.updateState(state, level, pos, block);
        }
    }

    protected void updateState(BlockState p_49372_, Level p_49373_, BlockPos p_49374_, Block p_49375_) {
    }

    protected BlockState updateDir(Level level, BlockPos pos, BlockState state, boolean p_49371_) {
        if (level.isClientSide) {
            return state;
        } else {
            RakedSandShape RakedSandShape = state.getValue(this.getShapeProperty());
            return (new RakedSandState(level, pos, state)).place(level.hasNeighborSignal(pos), p_49371_, RakedSandShape).getState();
        }
    }

    public PushReaction getPistonPushReaction(BlockState p_49415_) {
        return PushReaction.NORMAL;
    }

    public void onRemove(BlockState p_49384_, Level p_49385_, BlockPos p_49386_, BlockState p_49387_, boolean p_49388_) {
        if (!p_49388_) {
            super.onRemove(p_49384_, p_49385_, p_49386_, p_49387_, p_49388_);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_49363_) {
        BlockState blockstate = super.defaultBlockState();
        Direction direction = p_49363_.getHorizontalDirection();
        boolean flag1 = direction == Direction.EAST || direction == Direction.WEST;
        return blockstate.setValue(this.getShapeProperty(), flag1 ? RakedSandShape.EAST_WEST : RakedSandShape.NORTH_SOUTH);
    }

    public Property<RakedSandShape> getShapeProperty() {
        return SHAPE;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case CLOCKWISE_180:
                switch((RakedSandShape)state.getValue(SHAPE)) {
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RakedSandShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RakedSandShape.SOUTH_WEST);
                    case NORTH_SOUTH: //Forge fix: MC-196102
                    case EAST_WEST:
                        return state;
                }
            case COUNTERCLOCKWISE_90:
                switch((RakedSandShape)state.getValue(SHAPE)) {
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RakedSandShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RakedSandShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RakedSandShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch((RakedSandShape)state.getValue(SHAPE)) {
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, RakedSandShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, RakedSandShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, RakedSandShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, RakedSandShape.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState p_55402_, Mirror p_55403_) {
        RakedSandShape RakedSandShape = p_55402_.getValue(SHAPE);
        switch(p_55403_) {
            case LEFT_RIGHT:
                switch(RakedSandShape) {
                    case SOUTH_EAST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.NORTH_WEST);
                    case NORTH_WEST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.SOUTH_EAST);
                    default:
                        return super.mirror(p_55402_, p_55403_);
                }
            case FRONT_BACK:
                switch(RakedSandShape) {
                    default:
                        break;
                    case SOUTH_EAST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.NORTH_EAST);
                    case NORTH_EAST:
                        return p_55402_.setValue(SHAPE, RakedSandShape.NORTH_WEST);
                }
        }

        return super.mirror(p_55402_, p_55403_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55408_) {
        p_55408_.add(SHAPE);
    }

    public RakedSandShape getRakedDirection(BlockState state) {
        return state.getValue(getShapeProperty());
    }

    public enum RakedSandShape implements StringRepresentable {
        NORTH_SOUTH("north_south"),
        EAST_WEST("east_west"),
        SOUTH_EAST("south_east"),
        SOUTH_WEST("south_west"),
        NORTH_WEST("north_west"),
        NORTH_EAST("north_east");

        private final String name;

        RakedSandShape(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }

        public boolean isCurved() {
            return this == NORTH_SOUTH || this == EAST_WEST;
        }
    }
    
    public static class RakedSandState {
        private final Level level;
        private final BlockPos pos;
        private final RakedSandBlock block;
        private BlockState state;
        private final List<BlockPos> connections = Lists.newArrayList();

        public RakedSandState(Level p_55421_, BlockPos p_55422_, BlockState p_55423_) {
            this.level = p_55421_;
            this.pos = p_55422_;
            this.state = p_55423_;
            this.block = (RakedSandBlock)p_55423_.getBlock();
            RakedSandShape RakedSandShape = this.block.getRakedDirection(state);
            this.updateConnections(RakedSandShape);
        }

        public List<BlockPos> getConnections() {
            return this.connections;
        }

        private void updateConnections(RakedSandShape shape) {
            this.connections.clear();
            switch (shape) {
                case NORTH_SOUTH -> {
                    this.connections.add(this.pos.north());
                    this.connections.add(this.pos.south());
                }
                case EAST_WEST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.east());
                }
                case SOUTH_EAST -> {
                    this.connections.add(this.pos.east());
                    this.connections.add(this.pos.south());
                }
                case SOUTH_WEST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.south());
                }
                case NORTH_WEST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.north());
                }
                case NORTH_EAST -> {
                    this.connections.add(this.pos.east());
                    this.connections.add(this.pos.north());
                }
            }

        }

        private void removeSoftConnections() {
            for(int i = 0; i < this.connections.size(); ++i) {
                RakedSandState rakedSandState = this.getRakedSand(this.connections.get(i));
                if (rakedSandState != null && rakedSandState.connectsTo(this)) {
                    this.connections.set(i, rakedSandState.pos);
                } else {
                    this.connections.remove(i--);
                }
            }

        }

        private boolean hasRakedSand(BlockPos pos) {
            return RakedSandBlock.isRakedSand(this.level, pos);
        }

        @Nullable
        private RakedSandState getRakedSand(BlockPos pos) {
            BlockState blockstate = this.level.getBlockState(pos);
            if (RakedSandBlock.isRakedSand(blockstate)) {
                return new RakedSandState(this.level, pos, blockstate);
            }
            return null;
        }

        private boolean connectsTo(RakedSandState rakedSandState) {
            return this.hasConnection(rakedSandState.pos);
        }

        private boolean hasConnection(BlockPos p_55444_) {
            for(int i = 0; i < this.connections.size(); ++i) {
                BlockPos blockpos = this.connections.get(i);
                if (blockpos.getX() == p_55444_.getX() && blockpos.getZ() == p_55444_.getZ()) {
                    return true;
                }
            }

            return false;
        }

        protected int countPotentialConnections() {
            int i = 0;

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                if (this.hasRakedSand(this.pos.relative(direction))) {
                    ++i;
                }
            }

            return i;
        }

        private boolean canConnectTo(RakedSandState otherState) {
            return (this.connectsTo(otherState) || this.connections.size() != 2) && otherState.block.getRakedDirection(otherState.state) != this.block.getRakedDirection(state);
        }

        private void connectTo(RakedSandState p_55442_) {
            this.connections.add(p_55442_.pos);
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.hasConnection(blockpos);
            boolean flag1 = this.hasConnection(blockpos1);
            boolean flag2 = this.hasConnection(blockpos2);
            boolean flag3 = this.hasConnection(blockpos3);
            RakedSandShape RakedSandShape = null;
            if (flag || flag1) {
                RakedSandShape = RakedSandShape.NORTH_SOUTH;
            }

            if (flag2 || flag3) {
                RakedSandShape = RakedSandShape.EAST_WEST;
            }

            if (flag1 && flag3 && !flag && !flag2) {
                RakedSandShape = RakedSandShape.SOUTH_EAST;
            }

            if (flag1 && flag2 && !flag && !flag3) {
                RakedSandShape = RakedSandShape.SOUTH_WEST;
            }

            if (flag && flag2 && !flag1 && !flag3) {
                RakedSandShape = RakedSandShape.NORTH_WEST;
            }

            if (flag && flag3 && !flag1 && !flag2) {
                RakedSandShape = RakedSandShape.NORTH_EAST;
            }

            if (RakedSandShape == null) {
                RakedSandShape = RakedSandShape.NORTH_SOUTH;
            }

            this.state = this.state.setValue(this.block.getShapeProperty(), RakedSandShape);
            this.level.setBlock(this.pos, this.state, 3);
        }

        private boolean hasNeighbor(BlockPos pos) {
            RakedSandState rakedSandState = this.getRakedSand(pos);
            if (rakedSandState == null) {
                return false;
            } else {
                rakedSandState.removeSoftConnections();
                return rakedSandState.canConnectTo(this);
            }
        }

        public RakedSandState place(boolean hasNeighborSignal, boolean p_55433_, RakedSandShape placeShape) {
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.hasNeighbor(blockpos);
            boolean flag1 = this.hasNeighbor(blockpos1);
            boolean flag2 = this.hasNeighbor(blockpos2);
            boolean flag3 = this.hasNeighbor(blockpos3);
            RakedSandShape resultShape = null;
            boolean flag4 = flag || flag1;
            boolean flag5 = flag2 || flag3;
            if (flag4 && !flag5) {
                resultShape = RakedSandShape.NORTH_SOUTH;
            }

            if (flag5 && !flag4) {
                resultShape = RakedSandShape.EAST_WEST;
            }

            boolean flag6 = flag1 && flag3;
            boolean flag7 = flag1 && flag2;
            boolean flag8 = flag && flag3;
            boolean flag9 = flag && flag2;
            if (flag6 && !flag && !flag2) {
                resultShape = RakedSandShape.SOUTH_EAST;
            }

            if (flag7 && !flag && !flag3) {
                resultShape = RakedSandShape.SOUTH_WEST;
            }

            if (flag9 && !flag1 && !flag3) {
                resultShape = RakedSandShape.NORTH_WEST;
            }

            if (flag8 && !flag1 && !flag2) {
                resultShape = RakedSandShape.NORTH_EAST;
            }

            if (resultShape == null) {
                if (flag4 && flag5) {
                    resultShape = placeShape;
                } else if (flag4) {
                    resultShape = RakedSandShape.NORTH_SOUTH;
                } else if (flag5) {
                    resultShape = RakedSandShape.EAST_WEST;
                }

                if (hasNeighborSignal) {
                    if (flag6) {
                        resultShape = RakedSandShape.SOUTH_EAST;
                    }

                    if (flag7) {
                        resultShape = RakedSandShape.SOUTH_WEST;
                    }

                    if (flag8) {
                        resultShape = RakedSandShape.NORTH_EAST;
                    }

                    if (flag9) {
                        resultShape = RakedSandShape.NORTH_WEST;
                    }
                } else {
                    if (flag9) {
                        resultShape = RakedSandShape.NORTH_WEST;
                    }

                    if (flag8) {
                        resultShape = RakedSandShape.NORTH_EAST;
                    }

                    if (flag7) {
                        resultShape = RakedSandShape.SOUTH_WEST;
                    }

                    if (flag6) {
                        resultShape = RakedSandShape.SOUTH_EAST;
                    }
                }
            }

            if (resultShape == null) {
                resultShape = placeShape;
            }

            this.updateConnections(resultShape);
            this.state = this.state.setValue(this.block.getShapeProperty(), resultShape);
            if (p_55433_ || this.level.getBlockState(this.pos) != this.state) {
                this.level.setBlock(this.pos, this.state, 3);

                for(int i = 0; i < this.connections.size(); ++i) {
                    RakedSandState rakedState = this.getRakedSand(this.connections.get(i));
                    if (rakedState != null) {
                        rakedState.removeSoftConnections();
                        if (rakedState.canConnectTo(this)) {
                            rakedState.connectTo(this);
                        }
                    }
                }
            }

            return this;
        }

        public BlockState getState() {
            return this.state;
        }
    }
}
