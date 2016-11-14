package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class ContainerBarakoTrade extends Container {
    private final EntityBarako barako;

    private final EntityPlayer player;

    private final World world;

    private InventoryBarako inventory;

    public ContainerBarakoTrade(EntityBarako barako, InventoryPlayer playerInv, World world) {
        this(barako, new InventoryBarako(barako), playerInv, world);
    }

    public ContainerBarakoTrade(EntityBarako Barako, InventoryBarako inventory, InventoryPlayer playerInv, World world) {
        this.barako = Barako;
        this.player = playerInv.player;
        this.world = world;
        this.inventory = inventory;
        addSlotToContainer(new Slot(inventory, 0, 69, 54));
//        addSlotToContainer(new SlotResult(inventory, 1, 133, 54));
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
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        inventory.reset();
        super.onCraftMatrixChanged(inv);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = null;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack contained = slot.getStack();
            stack = contained.copy();
            if (index == 1) {
                if (!mergeItemStack(contained, 2, 38, true)) {
                    return null;
                }
                slot.onSlotChange(contained, stack);
            } else if (index != 0) {
                if (index >= 2 && index < 29) {
                    if (!mergeItemStack(contained, 29, 38, false)) {
                        return null;
                    }
                } else if (index >= 29 && index < 38 && !mergeItemStack(contained, 2, 29, false)) {
                    return null;
                }
            } else if (!mergeItemStack(contained, 2, 38, false)) {
                return null;
            }
            if (contained.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (contained.stackSize == stack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, contained);
        }
        return stack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        barako.setCustomer(null);
        if (!world.isRemote) {
            ItemStack stack = inventory.removeStackFromSlot(0);
            if (stack != null) {
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
                removeCount += Math.min(amount, getStack().stackSize);
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
            stack.onCrafting(barako.worldObj, player, removeCount);
            removeCount = 0;
        }

        @Override
        public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
            onCrafting(stack);
            ItemStack input = inventory.getStackInSlot(0);
            if (input != null && input.getItem() == barako.desires.getItem() && input.stackSize >= barako.desires.stackSize) {
                input.stackSize -= barako.desires.stackSize;
                if (input.stackSize <= 0) {
                    input = null;
                }
                inventory.setInventorySlotContents(0, input);
            }
        }
    }
}
