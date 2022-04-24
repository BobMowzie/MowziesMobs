package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.sounds.NonNullList;

import java.util.List;

public final class InventoryBarakoaya implements IInventory {
    private final EntityBarakoaVillager barakoaya;

    private final List<ItemStack> slots = NonNullList.withSize(2, ItemStack.EMPTY);

    private Trade trade;

    public InventoryBarakoaya(EntityBarakoaVillager barakoaya) {
        this.barakoaya = barakoaya;
    }

    @Override
    public int getSizeInventory() {
        return slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return slots.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 1 && slots.get(index) != ItemStack.EMPTY) {
            return ItemStackHelper.getAndSplit(slots, index, slots.get(index).getCount());
        }
        ItemStack stack = ItemStackHelper.getAndSplit(slots, index, count);
        if (stack != ItemStack.EMPTY && doUpdateForSlotChange(index)) {
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
        slots.set(index, stack);
        if (stack != ItemStack.EMPTY && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
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
    public boolean isUsableByPlayer(Player player) {
        return barakoaya.getCustomer() == player;
    }

    @Override
    public void openInventory(Player player) {}

    @Override
    public void closeInventory(Player player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        slots.clear(); // NonNullList.clear fills with default value
    }

    public void reset() {
        trade = null;
        ItemStack input = slots.get(0);
        if (input == ItemStack.EMPTY) {
            setInventorySlotContents(1, ItemStack.EMPTY);
        } else if (barakoaya.isOfferingTrade()) {
            Trade trade = barakoaya.getOfferingTrade();
            ItemStack tradeInput = trade.getInput();
            if (areItemsEqual(input, tradeInput) && input.getCount() >= tradeInput.getCount()) {
                this.trade = trade;
                setInventorySlotContents(1, trade.getOutput());
            } else {
                setInventorySlotContents(1, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean areItemsEqual(ItemStack s1, ItemStack s2) {
        return ItemStack.areItemsEqual(s1, s2) && (!s2.hasTag() || s1.hasTag() && NBTUtil.areNBTEquals(s2.getTag(), s1.getTag(), false));
    }
}
