package com.bobmowzie.mowziesmobs.server.inventory;

import java.util.ArrayList;
import java.util.List;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public final class InventoryBarako implements IInventory {
    private final EntityBarako barako;

    private ItemStack input = null;

    private List<ChangeListener> listeners;

    public InventoryBarako(EntityBarako barako) {
        this.barako = barako;
    }

    public void addListener(ChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    @Override
    public String getName() {
        return "entity.barako.trade";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? input : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack;
        if (index == 0 && input != null && count > 0) {
            ItemStack split = input.splitStack(count);
            if (input.stackSize == 0) {
                input = null;
            }
            stack = split;
            markDirty();
        } else {
            stack = null;
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index != 0) {
            return null;
        }
        ItemStack s = input;
        input = null;
        markDirty();
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) {
            input = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
            }
            markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (listeners != null) {
            for (ChangeListener listener : listeners) {
                listener.onChange(this);
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        input = null;
        markDirty();
    }

    public interface ChangeListener {
        void onChange(IInventory inv);
    }
}
