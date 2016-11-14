package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Arrays;

public final class InventoryBarako implements IInventory {
    private final EntityBarako barako;

    private final ItemStack[] slots = new ItemStack[2];

    public ItemStack desires;

    public InventoryBarako(EntityBarako barako) {
        this.barako = barako;
        desires = barako.desires;
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
        return slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return slots[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 1 && slots[index] != null) {
            return ItemStackHelper.getAndSplit(slots, index, slots[index].stackSize);
        }
        ItemStack stack = ItemStackHelper.getAndSplit(slots, index, count);
        if (stack != null && doUpdateForSlotChange(index)) {
            reset();
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(slots, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        slots[index] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        if (doUpdateForSlotChange(index)) {
            reset();
        }
    }

    private boolean doUpdateForSlotChange(int slot) {
        return slot == 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        reset();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return barako.getCustomer() == player;
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
        Arrays.fill(slots, null);
    }

    public void reset() {
        ItemStack input = slots[0];
        if (input != null && areItemsEqual(input, desires) && input.stackSize >= desires.stackSize) {
                System.out.println("Hello!");
        }
    }

    private static boolean areItemsEqual(ItemStack s1, ItemStack s2) {
        return ItemStack.areItemsEqual(s1, s2) && (!s2.hasTagCompound() || s1.hasTagCompound() && NBTUtil.areNBTEquals(s2.getTagCompound(), s1.getTagCompound(), false));
    }
}
