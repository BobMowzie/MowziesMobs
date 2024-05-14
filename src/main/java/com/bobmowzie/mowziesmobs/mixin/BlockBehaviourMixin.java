package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.block.ICopiedBlockProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.Properties.class)
public class BlockBehaviourMixin implements ICopiedBlockProperties {
    public Block baseBlock;

    @Inject(method = "copy", at = @At("RETURN"))
    private static void onCopy(BlockBehaviour blockBehaviour, CallbackInfoReturnable<BlockBehaviour.Properties> cir) {
        if (blockBehaviour instanceof Block) {
            ICopiedBlockProperties copiedBlockProperties = (ICopiedBlockProperties) cir.getReturnValue();
            copiedBlockProperties.setBaseBlock((Block) blockBehaviour);
        }
    }

    @Override
    public Block getBaseBlock() {
        return baseBlock;
    }

    @Override
    public void setBaseBlock(Block block) {
        baseBlock = block;
    }
}
