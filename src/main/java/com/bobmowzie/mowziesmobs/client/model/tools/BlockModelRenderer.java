package com.bobmowzie.mowziesmobs.client.model.tools;

import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

/**
 * Created by BobMowzie on 5/1/2017.
 */
public class BlockModelRenderer extends AdvancedModelRenderer {
    private BlockState blockState;

    public BlockModelRenderer(AdvancedModelBase model) {
        super(model);
        setBlockState(Blocks.DIRT.defaultBlockState());
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
