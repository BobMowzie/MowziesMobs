package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.world.structure.StructureBarakoaVillage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
    public ActionResultType onItemUse(PlayerEntity playerIn, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
            if (!worldIn.isRemote) StructureBarakoaVillage.generateVillage(worldIn, worldIn.rand, pos.getX(), pos.getZ());
//        if (!worldIn.isRemote) StructureBarakoaVillage.generateThrone(worldIn, worldIn.rand, pos, EnumFacing.NORTH);
        return ActionResultType.SUCCESS;
    }
}
