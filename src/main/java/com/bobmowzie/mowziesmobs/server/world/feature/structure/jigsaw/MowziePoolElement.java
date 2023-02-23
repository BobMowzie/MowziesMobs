package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

public class MowziePoolElement extends SinglePoolElement {
    public static final Codec<MowziePoolElement> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    templateCodec(),
                    processorsCodec(),
                    projectionCodec(),
                    BoundsParams.CODEC.optionalFieldOf("bounds", new BoundsParams(false, BlockPos.ZERO, BlockPos.ZERO, BlockPos.ZERO)).forGetter(element -> element.bounds),
                    Codec.BOOL.optionalFieldOf("two_way", false).forGetter(element -> element.twoWay),
                    Codec.INT.optionalFieldOf("min_depth", -1).forGetter(element -> element.maxDepth),
                    Codec.INT.optionalFieldOf("max_depth", -1).forGetter(element -> element.maxDepth),
                    Codec.INT.optionalFieldOf("min_height").forGetter(element -> element.minHeight),
                    Codec.INT.optionalFieldOf("max_height").forGetter(element -> element.maxHeight)
            ).apply(builder, MowziePoolElement::new));

    /**
     * Bounds related parameters
     */
    public final BoundsParams bounds;

    /**
     * Whether this piece's horizontal jigsaw blocks can connect in both directions
     */
    public final boolean twoWay;

    /**
     * Maximum iteration depth at which this piece can be chosen
     */
    public final int minDepth;
    public final int maxDepth;

    /**
     * Distances to stop the piece from placing too high or too low down.
     */
    public final Optional<Integer> minHeight;
    public final Optional<Integer> maxHeight;

    protected MowziePoolElement(Either<ResourceLocation, StructureTemplate> p_210415_, Holder<StructureProcessorList> p_210416_, StructureTemplatePool.Projection p_210417_, BoundsParams bounds, boolean twoWay,
                                int minDepth,int maxDepth,
                                Optional<Integer> minHeight, Optional<Integer> maxHeight
    ) {
        super(p_210415_, p_210416_, p_210417_);
        this.bounds = bounds;
        this.twoWay = twoWay;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public static boolean canAttachTwoWays(StructureTemplate.StructureBlockInfo p_54246_, StructureTemplate.StructureBlockInfo p_54247_) {
        Direction direction = JigsawBlock.getFrontFacing(p_54246_.state);
        Direction direction1 = JigsawBlock.getFrontFacing(p_54247_.state);
        Direction direction2 = JigsawBlock.getTopFacing(p_54246_.state);
        Direction direction3 = JigsawBlock.getTopFacing(p_54247_.state);
        JigsawBlockEntity.JointType jigsawblockentity$jointtype = JigsawBlockEntity.JointType.byName(p_54246_.nbt.getString("joint")).orElseGet(() -> {
            return direction.getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE;
        });
        boolean flag = jigsawblockentity$jointtype == JigsawBlockEntity.JointType.ROLLABLE;
        return direction == direction1 && (flag || direction2 == direction3) && p_54246_.nbt.getString("target").equals(p_54247_.nbt.getString("name"));
    }

    public boolean ignoresBounds() {
        return this.bounds.ignoreBounds;
    }

    public boolean twoWay() {
        return this.twoWay;
    }

    public Vec3i offset() {
        return new Vec3i(bounds.offset.getX(), bounds.offset.getY(), bounds.offset.getZ());
    }

    @Override
    public BoundingBox getBoundingBox(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        StructureTemplate structuretemplate = this.getTemplate(structureManager);

        Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
        BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(bounds.boundsMinOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(bounds.boundsMaxOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        return BoundingBox.fromCorners(blockpos, blockpos1).move(blockPos);
    }

    public static class BoundsParams {
        public static final Codec<BoundsParams> CODEC = RecordCodecBuilder.create((builder) -> builder
                .group(
                        Codec.BOOL.optionalFieldOf("ignore_bounds", false).forGetter(element -> element.ignoreBounds),
                        BlockPos.CODEC.optionalFieldOf("bounds_min_offset", BlockPos.ZERO).forGetter(element -> element.boundsMinOffset),
                        BlockPos.CODEC.optionalFieldOf("bounds_max_offset", BlockPos.ZERO).forGetter(element -> element.boundsMaxOffset),
                        BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(element -> element.offset)
                ).apply(builder, BoundsParams::new));

        /**
         * Whether this piece should ignore the usual piece boundary checks.
         * Enabling this allows this piece to spawn while overlapping other pieces.
         */
        public final boolean ignoreBounds;

        /**
         * Adjust the piece's bounds on all 6 sides
         */
        public final BlockPos boundsMinOffset;
        public final BlockPos boundsMaxOffset;

        /**
         * Offset the piece's location
         */
        public final BlockPos offset;

        private BoundsParams(boolean ignoreBounds, BlockPos boundsMinOffset, BlockPos boundsMaxOffset, BlockPos offset) {
            this.ignoreBounds = ignoreBounds;
            this.boundsMinOffset = boundsMinOffset;
            this.boundsMaxOffset = boundsMaxOffset;
            this.offset = offset;
        }
    }
}
