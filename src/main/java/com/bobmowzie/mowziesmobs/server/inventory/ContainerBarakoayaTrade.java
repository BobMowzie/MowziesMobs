package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;

public final class ContainerBarakoayaTrade extends Container {

    private final EntityBarakoaVillager barakoaya;

    private final InventoryBarakoaya inventory;

    private final Player player;

    public ContainerBarakoayaTrade(int id, PlayerInventory playerInventory) {
        this(id, (EntityBarakoaVillager) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerBarakoayaTrade(int id, EntityBarakoaVillager barakoaya, PlayerInventory playerInv) {
        this(id, barakoaya, new InventoryBarakoaya(barakoaya), playerInv);
    }

    public ContainerBarakoayaTrade(int id, EntityBarakoaVillager barakoaya, InventoryBarakoaya inventory, PlayerInventory playerInv) {
        super(ContainerHandler.CONTAINER_BARAKOAYA_TRADE, id);
        this.barakoaya = barakoaya;
        this.inventory = inventory;
        this.player = playerInv.player;
        addSlot(new Slot(inventory, 0, 80, 54));
        addSlot(new SlotResult(inventory, 1, 133, 54));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(Player player) {
        return barakoaya != null && inventory.isUsableByPlayer(player) && barakoaya.isAlive() && barakoaya.getDistance(player) < 8;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        inventory.reset();
        super.onCraftMatrixChanged(inv);
    }

    @Override
    public ItemStack transferStackInSlot(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack contained = slot.getStack();
            stack = contained.copy();
            if (index == 1) {
                if (!mergeItemStack(contained, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(contained, stack);
            } else if (index != 0) {
                if (index >= 2 && index < 29) {
                    if (!mergeItemStack(contained, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !mergeItemStack(contained, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(contained, 2, 38, false)) {
                return ItemStack.EMPTY;
            }
            if (contained.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (contained.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, contained);
        }
        return stack;
    }

    @Override
    public void onContainerClosed(Player player) {
        super.onContainerClosed(player);
        if (barakoaya != null) barakoaya.setCustomer(null);
        if (!player.level.isClientSide) {
            ItemStack stack = inventory.removeStackFromSlot(0);
            if (stack != ItemStack.EMPTY) {
                player.dropItem(stack, false);
            }
        }
    }

    public EntityBarakoaVillager getBarakoaya() {
        return barakoaya;
    }

    public InventoryBarakoaya getInventoryBarakoaya() {
        return inventory;
    }

    private class SlotResult extends Slot {
        private int removeCount;

        public SlotResult(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            if (getHasStack()) {
                removeCount += Math.min(amount, getStack().getCount());
            }
            return super.decrStackSize(amount);
        }

        @Override
        protected void onCrafting(ItemStack stack, int amount) {
            removeCount += amount;
            super.onCrafting(stack, amount);
        }

        @Override
        protected void onCrafting(ItemStack stack) {
            stack.onCrafting(barakoaya.world, player, removeCount);
            removeCount = 0;
        }

        @Override
        public ItemStack onTake(Player player, ItemStack stack) {
            onCrafting(stack);
            if (barakoaya != null && barakoaya.isOfferingTrade()) {
                Trade trade = barakoaya.getOfferingTrade();
                ItemStack input = inventory.getStackInSlot(0);
                ItemStack tradeInput = trade.getInput();
                if (input.getItem() == tradeInput.getItem() && input.getCount() >= tradeInput.getCount()) {
                    input.shrink(tradeInput.getCount());
                    if (input.getCount() <= 0) {
                        input = ItemStack.EMPTY;
                    }
                    inventory.setInventorySlotContents(0, input);
                }
            }
            return stack;
        }
    }
}
