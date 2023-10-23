package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class InventoryOneInput implements Container {
    protected final MowzieEntity tradingEntity;
    protected ItemStack input = ItemStack.EMPTY;
    protected List<InventoryOneInput.ChangeListener> listeners;

    public InventoryOneInput(MowzieEntity tradingEntity) {
        this.tradingEntity = tradingEntity;
    }

    public void addListener(InventoryUmvuthi.ChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public void startOpen(Player player) {}

    @Override
    public void stopOpen(Player player) {}

    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? input : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack;
        if (index == 0 && input != ItemStack.EMPTY && count > 0) {
            ItemStack split = input.split(count);
            if (input.getCount() == 0) {
                input = ItemStack.EMPTY;
            }
            stack = split;
            setChanged();
        } else {
            stack = ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index != 0) {
            return ItemStack.EMPTY;
        }
        ItemStack s = input;
        input = ItemStack.EMPTY;
        setChanged();
        return s;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == 0) {
            input = stack;
            if (stack != ItemStack.EMPTY && stack.getCount() > getMaxStackSize()) {
                stack.setCount(getMaxStackSize());
            }
            setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clearContent() {
        input = ItemStack.EMPTY;
        setChanged();
    }

    public interface ChangeListener {
        void onChange(Container inv);
    }

    @Override
    public boolean isEmpty() {
        return !input.isEmpty();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        if (listeners != null) {
            for (ChangeListener listener : listeners) {
                listener.onChange(this);
            }
        }
    }
}
