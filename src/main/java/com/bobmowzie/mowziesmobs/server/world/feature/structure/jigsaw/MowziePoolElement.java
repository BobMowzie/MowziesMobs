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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class MowziePoolElement extends SinglePoolElement {
    public static final Codec<MowziePoolElement> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    templateCodec(),
                    processorsCodec(),
                    projectionCodec(),
                    Codec.BOOL.optionalFieldOf("ignore_bounds", false).forGetter(element -> element.ignoreBounds),
                    Codec.BOOL.optionalFieldOf("two_way", false).forGetter(element -> element.twoWay),
                    BlockPos.CODEC.optionalFieldOf("bounds_min_offset", BlockPos.ZERO).forGetter(element -> element.boundsMinOffset),
                    BlockPos.CODEC.optionalFieldOf("bounds_max_offset", BlockPos.ZERO).forGetter(element -> element.boundsMaxOffset),
                    BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(element -> element.offset),
                    Codec.INT.optionalFieldOf("max_depth", -1).forGetter(element -> element.maxDepth),
                    Codec.INT.optionalFieldOf("min_height", -1).forGetter(element -> element.minHeight),
                    Codec.INT.optionalFieldOf("max_height", -1).forGetter(element -> element.maxHeight)
            ).apply(builder, MowziePoolElement::new));

    /**
     * Whether this piece should ignore the usual piece boundary checks.
     * Enabling this allows this piece to spawn while overlapping other pieces.
     */
    public final boolean ignoreBounds;

    /**
     * Whether this piece's horizontal jigsaw blocks can connect in both directions
     */
    public final boolean twoWay;

    /**
     * Adjust the piece's bounds on all 6 sides
     */
    public final BlockPos boundsMinOffset;
    public final BlockPos boundsMaxOffset;

    /**
     * Offset the piece's location
     */
    public final BlockPos offset;

    /**
     * Maximum iteration depth at which this piece can be chosen
     */
    public final int maxDepth;

    /**
     * Distances to stop the piece from placing too high or too low down.
     */
    public final int minHeight;
    public final int maxHeight;

    protected MowziePoolElement(Either<ResourceLocation, StructureTemplate> p_210415_, Holder<StructureProcessorList> p_210416_, StructureTemplatePool.Projection p_210417_, boolean ignoreBounds, boolean twoWay,
                                BlockPos boundsMinOffset, BlockPos boundsMaxOffset, BlockPos offset,
                                int maxDepth, int minHeight, int maxHeight
    ) {
        super(p_210415_, p_210416_, p_210417_);
        this.ignoreBounds = ignoreBounds;
        this.twoWay = twoWay;
        this.boundsMinOffset = boundsMinOffset;
        this.boundsMaxOffset = boundsMaxOffset;
        this.offset = offset;
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
        return this.ignoreBounds;
    }

    public boolean twoWay() {
        return this.twoWay;
    }

    public Vec3i offset() {
        return new Vec3i(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public BoundingBox getBoundingBox(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        StructureTemplate structuretemplate = this.getTemplate(structureManager);

        Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
        BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(boundsMinOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(boundsMaxOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        return BoundingBox.fromCorners(blockpos, blockpos1).move(blockPos);
    }
}
