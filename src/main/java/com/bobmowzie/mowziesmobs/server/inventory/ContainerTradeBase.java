package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class ContainerTradeBase extends AbstractContainerMenu {
    protected final MowzieEntity tradingMob;
    protected final Container inventory;
    protected final Player player;

    public ContainerTradeBase(MenuType<?> menuType, int id, MowzieEntity tradingMob, Container inventory, Inventory playerInv) {
        super(menuType, id);
        this.tradingMob = tradingMob;
        this.inventory = inventory;
        this.player = playerInv.player;
        addCustomSlots(playerInv);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    protected void addCustomSlots(Inventory playerInv) {

    }

    @Override
    public boolean stillValid(Player player) {
        return tradingMob != null && inventory.stillValid(player) && tradingMob.isAlive() && tradingMob.distanceTo(player) < 8;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack contained = slot.getItem();
            stack = contained.copy();
            if (index == 1) {
                if (!moveItemStackTo(contained, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(contained, stack);
            } else if (index != 0) {
                if (index >= 2 && index < 29) {
                    if (!moveItemStackTo(contained, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !moveItemStackTo(contained, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(contained, 2, 38, false)) {
                return ItemStack.EMPTY;
            }
            if (contained.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (contained.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, contained);
        }
        return stack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        returnItems();
    }

    public void returnItems() {
        if (!player.level.isClientSide) {
            ItemStack stack = inventory.removeItemNoUpdate(0);
            if (stack != ItemStack.EMPTY) {
                ItemHandlerHelper.giveItemToPlayer(player, stack);
            }
        }
    }

    public MowzieEntity getTradingMob() {
        return tradingMob;
    }

    public Container getInventory() {
        return inventory;
    }
}