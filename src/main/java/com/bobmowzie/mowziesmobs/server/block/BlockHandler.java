package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HayBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class BlockHandler {
    private BlockHandler() {}

    public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, MowziesMobs.MODID);
    public static final RegistryObject<Block> PAINTED_ACACIA = REG.register("painted_acacia", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<SlabBlock> PAINTED_ACACIA_SLAB = REG.register("painted_acacia_slab", () -> new SlabBlock(Block.Properties.copy(PAINTED_ACACIA.get())));
    public static final RegistryObject<Block> THATCH = REG.register("thatch_block", () -> new HayBlock(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)));
    //public static final RegistryObject<BlockGrottol> GROTTOL = REG.register("grottol", () -> new BlockGrottol(Block.Properties.create(Material.ROCK).noDrops()));

    public static void init() {
        FireBlock fireblock = (FireBlock)Blocks.FIRE;
        fireblock.setFlammable(THATCH.get(), 60, 20);
        fireblock.setFlammable(PAINTED_ACACIA.get(), 5, 20);
        fireblock.setFlammable(PAINTED_ACACIA_SLAB.get(), 5, 20);
    }
}