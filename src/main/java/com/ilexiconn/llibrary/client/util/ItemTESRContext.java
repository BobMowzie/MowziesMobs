package com.ilexiconn.llibrary.client.util;

import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

/**
 * Handles context for Item TESRs, such as the ItemStack and viewed perspective
 *
 * @author gegy1000
 * @since 1.7.7
 */
@OnlyIn(Dist.CLIENT)
public enum ItemTESRContext {
    INSTANCE;

    @Nonnull
    private ItemStack currentStack = ItemStack.EMPTY;

    @Nonnull
    private ItemCameraTransforms.TransformType currentTransform = ItemCameraTransforms.TransformType.GROUND;

    public void provideStackContext(@Nonnull ItemStack stack) {
        this.currentStack = stack;
    }

    public void providePerspectiveContext(@Nonnull ItemCameraTransforms.TransformType transform) {
        this.currentTransform = transform;
    }

    /**
     * @return the stack currently being rendered
     */
    @Nonnull
    public ItemStack getCurrentStack() {
        return this.currentStack;
    }

    /**
     * @return the current camera transform context
     */
    @Nonnull
    public ItemCameraTransforms.TransformType getCurrentTransform() {
        return this.currentTransform;
    }
}
