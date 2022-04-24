package com.bobmowzie.mowziesmobs.server.entity.grottol;

import net.minecraft.world.level.block.AbstractRailBlock;
import net.minecraft.world.entity.item.minecart.AbstractMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vector3i;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public final class BlackPinkRailLine implements Consumer<AbstractMinecart> {
    private final BlackPinkInYourArea action;

    private State state = new StateAcquireVertex();

    private BlackPinkRailLine(BlackPinkInYourArea action) {
        this.action = action;
    }

    @Override
    public void accept(AbstractMinecart minecart) {
        state = next(minecart.world, minecart);
    }

    private State next(Level world, AbstractMinecart minecart) {
        BlockPos pos = getRailPosition(world, new BlockPos(minecart.position()));
        if (AbstractRailBlock.isRail(level.getBlockState(pos))) {
            return state.apply(world, minecart, pos);
        }
        return state.derail();
    }

    private static BlockPos getRailPosition(Level world, BlockPos pos) {
        BlockPos below = pos.below();
        return AbstractRailBlock.isRail(world, below) ? below : pos;
    }

    public static BlackPinkRailLine create() {
        return new BlackPinkRailLine(BlackPinkInYourArea.create());
    }

    private abstract class State {
        abstract State apply(Level world, AbstractMinecart minecart, BlockPos pos);

        abstract State derail();
    }

    private final class StateAcquireVertex extends State {
        @Override
        public State apply(Level world, AbstractMinecart minecart, BlockPos vertex) {
            return new StateAcquireEdge(vertex);
        }

        @Override
        State derail() {
            return this;
        }
    }

    private final class StateAcquireEdge extends State {
        private final BlockPos vertex;

        private StateAcquireEdge(BlockPos vertex) {
            this.vertex = vertex;
        }

        @Override
        public State apply(Level world, AbstractMinecart minecart, BlockPos vertex) {
            return new StateSearch(vertex.subtract(this.vertex), vertex);
        }

        @Override
        State derail() {
            return new StateAcquireVertex();
        }
    }

    private final class StateSearch extends State {
        private final long[] mask = {
            0xFFBAEA55D752A95L,
            0xFF5555EAAAAF57AL,
            0xFFFFFFBFFFFDFEFL,
            0xFFEFBFFF7DFFFFFL
        };

        private final long test = 0x10000000000000L;

        private Vector3i edge;

        private BlockPos vertex;

        private int ordinal;

        private long state = 0xFFFFFFFFFFFFEL;

        private StateSearch(Vector3i edge, BlockPos vertex) {
            this.edge = edge;
            this.vertex = vertex;
        }

        @Override
        public State apply(Level world, AbstractMinecart minecart, BlockPos vertex) {
            if (!this.vertex.equals(vertex)) {
                Vector3i edge = vertex.subtract(this.vertex);
                int ordinal = getOrdinal(this.edge, edge);
                if (ordinal >= 0 && ordinal < 4 && (ordinal != 1 || ordinal != this.ordinal) && ((state = (state | mask[ordinal]) << 1) & test) == 0) {
                    action.accept(world, minecart);
                }
                this.ordinal = ordinal;
                this.vertex = vertex;
                this.edge = edge;
            }
            return this;
        }

        private int getOrdinal(Vector3i v0, Vector3i v1) {
            return 1 + (v1.z() * v0.x() - v1.x() * v0.z()) + ((v0.x() * v1.x() + v0.z() * v1.z()) & 2);
        }

        @Override
        State derail() {
            return new StateAcquireVertex();
        }
    }
}
