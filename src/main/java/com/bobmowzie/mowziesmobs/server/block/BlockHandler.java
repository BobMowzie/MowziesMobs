package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(MowziesMobs.MODID)
@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class BlockHandler {
    public static final BlockPaintedAcacia PAINTED_ACACIA = null;
    public static final BlockPaintedAcaciaSlab PAINTED_ACACIA_SLAB = null;
    public static final BlockPaintedAcaciaSlab PAINTED_ACACIA_DOUBLE_SLAB = null;
    public static final BlockCampfire CAMPFIRE = null;

    private BlockHandler() {}

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new BlockPaintedAcacia(),
                new BlockPaintedAcaciaSlab.Half(),
                new BlockPaintedAcaciaSlab.Double(),
                new BlockCampfire()
        );
    }
}