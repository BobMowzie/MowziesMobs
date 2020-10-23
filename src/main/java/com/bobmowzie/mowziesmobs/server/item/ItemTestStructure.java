package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.world.structure.StructureBarakoaVillage;
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


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getWorld().isRemote) StructureBarakoaVillage.generateVillage(context.getWorld(), context.getWorld().rand, context.getPos().getX(), context.getPos().getZ());
//        if (!worldIn.isRemote) StructureBarakoaVillage.generateThrone(worldIn, worldIn.rand, pos, Direction.NORTH);
        return ActionResultType.SUCCESS;
    }
}
