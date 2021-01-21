package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTestStructure extends Item {
    public ItemTestStructure(Item.Properties properties) {
        super(properties);
    }


//    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
////        if (!context.getWorld().isRemote) StructureWroughtnautRoom.generate(context.getWorld(), context.getPos(), context.getWorld().rand, Direction.NORTH);
//        if (!context.getWorld().isRemote && context.getPlayer().isSneaking()) StructureBarakoaVillage.generateHouse(context.getWorld(), context.getWorld().rand, context.getPos(), context.getPlacementHorizontalFacing());
//        else if (!context.getWorld().isRemote) StructureBarakoaVillage.generateSideHouse(context.getWorld(), context.getWorld().rand, context.getPos(), context.getPlacementHorizontalFacing());
//        return ActionResultType.SUCCESS;
//    }
}
