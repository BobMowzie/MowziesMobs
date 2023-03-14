package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FallbackPoolElement extends StructurePoolElement {
    public static final Codec<FallbackPoolElement> CODEC = Codec.unit(() -> {
        return FallbackPoolElement.INSTANCE;
    });
    public static final FallbackPoolElement INSTANCE = new FallbackPoolElement();

    private FallbackPoolElement() {
        super(StructureTemplatePool.Projection.TERRAIN_MATCHING);
    }

    public Vec3i getSize(StructureManager p_210191_, Rotation p_210192_) {
        return Vec3i.ZERO;
    }

    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager p_210198_, BlockPos p_210199_, Rotation p_210200_, Random p_210201_) {
        return Collections.emptyList();
    }

    public BoundingBox getBoundingBox(StructureManager p_210194_, BlockPos p_210195_, Rotation p_210196_) {
        throw new IllegalStateException("Invalid call to MowzieFallbackElement.getBoundingBox, filter me!");
    }

    public boolean place(StructureManager p_210180_, WorldGenLevel p_210181_, StructureFeatureManager p_210182_, ChunkGenerator p_210183_, BlockPos p_210184_, BlockPos p_210185_, Rotation p_210186_, BoundingBox p_210187_, Random p_210188_, boolean p_210189_) {
        return true;
    }

    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.EMPTY;
    }

    public String toString() {
        return "Fallback";
    }
}
