package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

// Based on https://github.com/YUNG-GANG/YUNGs-API/blob/multiloader/1.19/Common/src/main/java/com/yungnickyoung/minecraft/yungsapi/world/BlockStateRandomizer.java

public class BlockStateRandomizer {
    public static final Codec<BlockStateRandomizer> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    Entry.CODEC.listOf().optionalFieldOf("entries").forGetter((selector) -> selector.entries),
                    BlockState.CODEC.fieldOf("default").forGetter((selector) -> selector.defaultState))
            .apply(instance, BlockStateRandomizer::new));

    private Optional<List<Entry>> entries = Optional.empty();
    private BlockState defaultState = Blocks.AIR.defaultBlockState();

    public BlockStateRandomizer(Optional<List<Entry>> entries, BlockState defaultBlockState) {
        this.entries = entries;
        this.defaultState = defaultBlockState;
    }

    public BlockState chooseRandomState(Random randomSource) {
        if (entries.isPresent()) {
            float total = 0.0f;
            for (Entry entry : entries.get()) {
                total += entry.weight;
            }
            if (total != 0.0f) {
                float target = randomSource.nextFloat();
                float currBottom = 0;
                for (Entry entry : entries.get()) {
                    if (currBottom <= target && target < currBottom + (float) entry.weight / total) {
                        return entry.blockState;
                    }

                    currBottom += (float) entry.weight / total;
                }
            }
        }
        return this.defaultState;
    }

    public static class Entry {
        public static Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(
                        BlockState.CODEC.fieldOf("blockState").forGetter(entry -> entry.blockState),
                        Codec.INT.fieldOf("weight").forGetter(entry -> entry.weight))
                .apply(instance, Entry::new));

        public BlockState blockState;
        public int weight;

        public Entry(BlockState blockState, int weight) {
            this.blockState = blockState;
            this.weight = weight;
        }
    }
}
