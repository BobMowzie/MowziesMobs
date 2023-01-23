package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class TwoWayPoolElement extends SinglePoolElement {
    public static final Codec<TwoWayPoolElement> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    templateCodec(),
                    processorsCodec(),
                    projectionCodec()
            ).apply(builder, TwoWayPoolElement::new));

    protected TwoWayPoolElement(Either<ResourceLocation, StructureTemplate> p_210415_, Holder<StructureProcessorList> p_210416_, StructureTemplatePool.Projection p_210417_) {
        super(p_210415_, p_210416_, p_210417_);
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
}
