package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.world.structure.StructureBarakoaVillage;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
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
        if (!context.getWorld().isRemote) StructureWroughtnautRoom.generate(context.getWorld(), context.getPos(), context.getWorld().rand, Direction.NORTH);
//        if (!worldIn.isRemote) StructureBarakoaVillage.generateThrone(worldIn, worldIn.rand, pos, Direction.NORTH);
        return ActionResultType.SUCCESS;
    }
}
