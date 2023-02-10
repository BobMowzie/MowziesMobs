package com.bobmowzie.mowziesmobs.server.block.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockEntityHandler {

    public static final DeferredRegister<BlockEntityType<?>> REG = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MowziesMobs.MODID);

    public static RegistryObject<BlockEntityType<GongBlockEntity>> GONG_BLOCK_ENTITY = REG.register("gong_entity", () -> BlockEntityType.Builder.of(GongBlockEntity::new, BlockHandler.GONG.get()).build(null));
}
