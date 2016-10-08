package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemTestStructure extends Item {
    public ItemTestStructure() {
        this.setUnlocalizedName("testStructure");
        setRegistryName("test_structure");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Random rand = new Random();
            StructureWroughtnautRoom.tryWroughtChamber(worldIn, rand, pos.getX(), pos.getZ(), 1);
            return EnumActionResult.PASS;
    }
}