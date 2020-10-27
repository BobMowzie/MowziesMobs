package com.ilexiconn.llibrary.client.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author pau101
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class StackUnderflowError extends Error {
    public StackUnderflowError() {
        super();
    }

    public StackUnderflowError(String s) {
        super(s);
    }
}
