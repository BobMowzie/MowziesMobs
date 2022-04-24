package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.sounds.ActionResultType;
import net.minecraft.core.Direction;
import net.minecraft.sounds.Hand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ItemTestStructure extends Item {
    public ItemTestStructure(Item.Properties properties) {
        super(properties);
    }


//    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
////        if (!context.getLevel().isClientSide) StructureWroughtnautRoom.generate(context.getLevel(), context.getPos(), context.getLevel().rand, Direction.NORTH);
//        if (!context.getLevel().isClientSide && context.getPlayer().isSneaking()) StructureBarakoaVillage.generateHouse(context.getLevel(), context.getLevel().rand, context.getPos(), context.getPlacementHorizontalFacing());
//        else if (!context.getLevel().isClientSide) StructureBarakoaVillage.generateSideHouse(context.getLevel(), context.getLevel().rand, context.getPos(), context.getPlacementHorizontalFacing());
//        return ActionResultType.SUCCESS;
//    }
}
