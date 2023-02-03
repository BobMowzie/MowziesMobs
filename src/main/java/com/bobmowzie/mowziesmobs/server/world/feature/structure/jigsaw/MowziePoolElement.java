package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Collections;
import java.util.List;

public class MowziePoolElement extends SinglePoolElement {
    public static final Codec<MowziePoolElement> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    templateCodec(),
                    processorsCodec(),
                    projectionCodec(),
                    Codec.BOOL.optionalFieldOf("ignore_bounds", false).forGetter(element -> element.ignoreBounds),
                    Codec.BOOL.optionalFieldOf("two_way", false).forGetter(element -> element.twoWay),
                    Codec.INT.optionalFieldOf("bounds_min_x", 0).forGetter(element -> element.boundsMinX),
                    Codec.INT.optionalFieldOf("bounds_max_x", 0).forGetter(element -> element.boundsMaxX),
                    Codec.INT.optionalFieldOf("bounds_min_z", 0).forGetter(element -> element.boundsMinZ),
                    Codec.INT.optionalFieldOf("bounds_max_z", 0).forGetter(element -> element.boundsMaxZ),
                    Codec.INT.optionalFieldOf("bounds_min_y", 0).forGetter(element -> element.boundsMinY),
                    Codec.INT.optionalFieldOf("bounds_max_y", 0).forGetter(element -> element.boundsMaxY),
                    Codec.INT.optionalFieldOf("offset_x", 0).forGetter(element -> element.offsetX),
                    Codec.INT.optionalFieldOf("offset_y", 0).forGetter(element -> element.offsetY),
                    Codec.INT.optionalFieldOf("offset_z", 0).forGetter(element -> element.offsetZ),
                    Codec.INT.optionalFieldOf("max_depth", -1).forGetter(element -> element.maxDepth),
                    Codec.STRING.listOf().optionalFieldOf("priority_pools", Collections.emptyList()).forGetter(element -> element.priorityPools)
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
    public final int boundsMinX;
    public final int boundsMaxX;
    public final int boundsMinZ;
    public final int boundsMaxZ;
    public final int boundsMinY;
    public final int boundsMaxY;

    /**
     * Offset the piece's location
     */
    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;

    /**
     * Maximum iteration depth at which this piece can be chosen
     */
    public final int maxDepth;

    /**
     * Pools to generate first if present on child jigsaws
     */
    public final List<String> priorityPools;

    protected MowziePoolElement(Either<ResourceLocation, StructureTemplate> p_210415_, Holder<StructureProcessorList> p_210416_, StructureTemplatePool.Projection p_210417_, boolean ignoreBounds, boolean twoWay,
                                int boundsMinX, int boundsMaxX,
                                int boundsMinZ, int boundsMaxZ,
                                int boundsMinY, int boundsMaxY,
                                int offsetX, int offsetY, int offsetZ,
                                int maxDepth, List<String> priorityPools
    ) {
        super(p_210415_, p_210416_, p_210417_);
        this.ignoreBounds = ignoreBounds;
        this.twoWay = twoWay;
        this.boundsMinX = boundsMinX;
        this.boundsMaxX = boundsMaxX;
        this.boundsMinZ = boundsMinZ;
        this.boundsMaxZ = boundsMaxZ;
        this.boundsMinY = boundsMinY;
        this.boundsMaxY = boundsMaxY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.maxDepth = maxDepth;
        this.priorityPools = priorityPools;
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
        return new Vec3i(offsetX, offsetY, offsetZ);
    }

    @Override
    public BoundingBox getBoundingBox(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        BoundingBox superBox = super.getBoundingBox(structureManager, blockPos, rotation);
        BlockPos offsetMin = BlockPos.ZERO;
        BlockPos offsetMax = BlockPos.ZERO;
        switch (rotation) {
            case NONE -> {
                offsetMin = new BlockPos(boundsMinX, boundsMinY, boundsMinZ);
                offsetMax = new BlockPos(boundsMaxX, boundsMaxY, boundsMaxZ);
            }
            case CLOCKWISE_90 -> {
                offsetMin = new BlockPos(boundsMinZ, boundsMinY, -boundsMaxX);
                offsetMax = new BlockPos(boundsMaxZ, boundsMaxY, -boundsMinX);
            }
            case CLOCKWISE_180 -> {
                offsetMin = new BlockPos(-boundsMaxX, boundsMinY, -boundsMaxZ);
                offsetMax = new BlockPos(-boundsMinX, boundsMaxY, -boundsMinZ);
            }
            case COUNTERCLOCKWISE_90 -> {
                offsetMin = new BlockPos(-boundsMaxZ, boundsMinY, boundsMinX);
                offsetMax = new BlockPos(-boundsMinZ, boundsMaxY, boundsMaxX);
            }
        }
        return new BoundingBox(superBox.minX() + offsetMin.getX(), superBox.minY() + offsetMin.getY(), superBox.minZ() + offsetMin.getZ(), superBox.maxX() + offsetMax.getX(), superBox.maxY() + offsetMax.getY(), superBox.maxZ() + offsetMax.getZ());
    }
}
