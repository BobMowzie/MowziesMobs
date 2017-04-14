package com.bobmowzie.mowziesmobs.server.inventory;

import java.util.Arrays;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public final class InventoryBarakoaya implements IInventory {
    private final EntityBarakoaya barakoaya;

    private final ItemStack[] slots = new ItemStack[2];

    private Trade trade;

    public InventoryBarakoaya(EntityBarakoaya barakoaya) {
        this.barakoaya = barakoaya;
    }

    @Override
    public String getName() {
        return "entity.barakoaya.trade";
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
    public boolean isUsableByPlayer(EntityPlayer player) {
        return barakoaya.getCustomer() == player;
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
        trade = null;
        ItemStack input = slots[0];
        if (input == null) {
            setInventorySlotContents(1, null);
        } else if (barakoaya.isOfferingTrade()) {
            Trade trade = barakoaya.getOfferingTrade();
            ItemStack tradeInput = trade.getInput();
            if (areItemsEqual(input, tradeInput) && input.stackSize >= tradeInput.stackSize) {
                this.trade = trade;
                setInventorySlotContents(1, trade.getOutput());
            } else {
                setInventorySlotContents(1, null);
            }
        }
    }

    private static boolean areItemsEqual(ItemStack s1, ItemStack s2) {
        return ItemStack.areItemsEqual(s1, s2) && (!s2.hasTagCompound() || s1.hasTagCompound() && NBTUtil.areNBTEquals(s2.getTagCompound(), s1.getTagCompound(), false));
    }
}
