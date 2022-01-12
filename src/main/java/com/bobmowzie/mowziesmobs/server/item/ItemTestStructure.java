package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.resources.ActionResultType;
import net.minecraft.resources.Direction;
import net.minecraft.resources.Hand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.World;

public class ItemTestStructure extends Item {
    public ItemTestStructure(Item.Properties properties) {
        super(properties);
    }


//    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
////        if (!context.getWorld().isClientSide) StructureWroughtnautRoom.generate(context.getWorld(), context.getPos(), context.getWorld().rand, Direction.NORTH);
//        if (!context.getWorld().isClientSide && context.getPlayer().isSneaking()) StructureBarakoaVillage.generateHouse(context.getWorld(), context.getWorld().rand, context.getPos(), context.getPlacementHorizontalFacing());
//        else if (!context.getWorld().isClientSide) StructureBarakoaVillage.generateSideHouse(context.getWorld(), context.getWorld().rand, context.getPos(), context.getPlacementHorizontalFacing());
//        return ActionResultType.SUCCESS;
//    }
}
