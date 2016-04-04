package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class BlockPaintedAcaciaSlab extends BlockSlab {
    public BlockPaintedAcaciaSlab() {
        super(false, Material.wood);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
        setBlockName("paintedAcaciaSlab");
        setBlockTextureName(new ResourceLocation(MowziesMobs.MODID, "texturePaintedAcacia").toString());
    }

    @Override
    public String func_150002_b(int p_150002_1_) {
        return null;
    }
}
