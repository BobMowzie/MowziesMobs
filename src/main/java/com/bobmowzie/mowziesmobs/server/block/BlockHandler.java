package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockHandler {

    public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, MowziesMobs.MODID);

    public static final RegistryObject<Block> PAINTED_ACACIA = REG.register("painted_acacia", () -> new Block(Block.Properties.copy(Blocks.ACACIA_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<SlabBlock> PAINTED_ACACIA_SLAB = REG.register("painted_acacia_slab", () -> new SlabBlock(Block.Properties.copy(PAINTED_ACACIA.get())));
    public static final RegistryObject<Block> THATCH = REG.register("thatch_block", () -> new HayBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final RegistryObject<Block> GONG = REG.register("gong", () -> new GongBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.ANVIL)));
    public static final RegistryObject<Block> GONG_PART = REG.register("gong_part", () -> new GongBlock.GongPartBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.ANVIL)));
    public static final RegistryObject<RakedSandBlock> RAKED_SAND = REG.register("raked_sand", () -> new RakedSandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND), Blocks.SAND.defaultBlockState()));
    public static final RegistryObject<RakedSandBlock> RED_RAKED_SAND = REG.register("red_raked_sand", () -> new RakedSandBlock(11098145, BlockBehaviour.Properties.copy(Blocks.RED_SAND), Blocks.RED_SAND.defaultBlockState()));
    public static final RegistryObject<Block> CLAWED_LOG = REG.register("clawed_log", () -> new Block(Block.Properties.copy(Blocks.STRIPPED_JUNGLE_LOG)));
    //public static final RegistryObject<BlockGrottol> GROTTOL = REG.register("grottol", () -> new BlockGrottol(Block.Properties.copy(Material.STONE).noDrops()));

    public static void init() {
        FireBlock fireblock = (FireBlock)Blocks.FIRE;
        fireblock.setFlammable(THATCH.get(), 60, 20);
        fireblock.setFlammable(PAINTED_ACACIA.get(), 5, 20);
        fireblock.setFlammable(PAINTED_ACACIA_SLAB.get(), 5, 20);
        fireblock.setFlammable(CLAWED_LOG.get(), 5, 5);
    }
}