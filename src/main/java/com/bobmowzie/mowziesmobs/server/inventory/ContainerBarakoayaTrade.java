package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class ContainerBarakoayaTrade extends Container {
    private final EntityBarakoaya barakoaya;

    private final InventoryBarakoaya inventory;

    private final EntityPlayer player;

    private final World world;

    public ContainerBarakoayaTrade(EntityBarakoaya barakoaya, InventoryPlayer playerInv, World world) {
        this(barakoaya, new InventoryBarakoaya(barakoaya), playerInv, world);
    }

    public ContainerBarakoayaTrade(EntityBarakoaya barakoaya, InventoryBarakoaya inventory, InventoryPlayer playerInv, World world) {
        this.barakoaya = barakoaya;
        this.inventory = inventory;
        this.player = playerInv.player;
        this.world = world;
        addSlotToContainer(new Slot(inventory, 0, 80, 54));
        addSlotToContainer(new SlotResult(inventory, 1, 133, 54));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        inventory.reset();
        super.onCraftMatrixChanged(inv);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
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
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        barakoaya.setCustomer(null);
        if (!world.isRemote) {
            ItemStack stack = inventory.removeStackFromSlot(0);
            if (stack != ItemStack.EMPTY) {
                player.dropItem(stack, false);
            }
        }
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
        public ItemStack onTake(EntityPlayer player, ItemStack stack) {
            onCrafting(stack);
            if (barakoaya.isOfferingTrade()) {
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
