package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class InventoryBarakoaya implements Container {
    private final EntityUmvuthanaMinion barakoaya;

    private final List<ItemStack> slots = NonNullList.withSize(2, ItemStack.EMPTY);

    private Trade trade;

    public InventoryBarakoaya(EntityUmvuthanaMinion barakoaya) {
        this.barakoaya = barakoaya;
    }

    @Override
    public int getContainerSize() {
        return slots.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return slots.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index == 1 && slots.get(index) != ItemStack.EMPTY) {
            return ContainerHelper.removeItem(slots, index, slots.get(index).getCount());
        }
        ItemStack stack = ContainerHelper.removeItem(slots, index, count);
        if (stack != ItemStack.EMPTY && doUpdateForSlotChange(index)) {
            reset();
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(slots, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        slots.set(index, stack);
        if (stack != ItemStack.EMPTY && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if (doUpdateForSlotChange(index)) {
            reset();
        }
    }

    private boolean doUpdateForSlotChange(int slot) {
        return slot == 0;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        reset();
    }

    @Override
    public boolean stillValid(Player player) {
        return barakoaya.getCustomer() == player;
    }

    @Override
    public void startOpen(Player player) {}

    @Override
    public void stopOpen(Player player) {}

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clearContent() {
        slots.clear(); // NonNullList.clear fills with default value
    }

    public void reset() {
        trade = null;
        ItemStack input = slots.get(0);
        if (input == ItemStack.EMPTY) {
            setItem(1, ItemStack.EMPTY);
        } else if (barakoaya.isOfferingTrade()) {
            Trade trade = barakoaya.getOfferingTrade();
            ItemStack tradeInput = trade.getInput();
            if (areItemsEqual(input, tradeInput) && input.getCount() >= tradeInput.getCount()) {
                this.trade = trade;
                setItem(1, trade.getOutput());
            } else {
                setItem(1, ItemStack.EMPTY);
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
        return ItemStack.isSame(s1, s2) && (!s2.hasTag() || s1.hasTag() && NbtUtils.compareNbt(s2.getTag(), s1.getTag(), false));
    }
}
