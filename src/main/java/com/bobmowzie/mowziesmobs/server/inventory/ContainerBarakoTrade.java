package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public final class ContainerBarakoTrade extends AbstractContainerMenu {
    private final EntityBarako barako;

    private final Player player;

    private final InventoryBarako inventory;

    public ContainerBarakoTrade(int id, Inventory playerInventory) {
        this(id, (EntityBarako) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerBarakoTrade(int id, EntityBarako barako, Inventory playerInv) {
        this(id, barako, new InventoryBarako(barako), playerInv);
    }

    public ContainerBarakoTrade(int id, EntityBarako Barako, InventoryBarako inventory, Inventory playerInv) {
        super(ContainerHandler.CONTAINER_BARAKO_TRADE, id);
        this.barako = Barako;
        this.player = playerInv.player;
        this.inventory = inventory;
        if (barako != null && !barako.hasTradedWith(playerInv.player)) addSlot(new Slot(inventory, 0, 69, 54));
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
    public boolean stillValid(Player player) {
        return barako != null && inventory.stillValid(player) && barako.isAlive() && barako.distanceTo(player) < 8;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack contained = slot.getItem();
            stack = contained.copy();
            if (index != 0) {
                if (index >= 1 && index < 28) {
                    if (!moveItemStackTo(contained, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 28 && index < 37 && !moveItemStackTo(contained, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(contained, 1, 37, false)) {
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
        if (barako != null) barako.setCustomer(null);
        if (!player.level.isClientSide) {
            ItemStack stack = inventory.removeItemNoUpdate(0);
            if (stack != ItemStack.EMPTY) {
                ItemEntity dropped = player.drop(stack, false);
                if (dropped != null) {
                    dropped.setDeltaMovement(dropped.getDeltaMovement().scale(0.5));
                }
            }
        }
    }

    public void returnItems() {
        if (!player.level.isClientSide) {
            ItemStack stack = inventory.removeItemNoUpdate(0);
            if (stack != ItemStack.EMPTY) {
                ItemHandlerHelper.giveItemToPlayer(player, stack);
            }
        }
    }

    public EntityBarako getBarako() {
        return barako;
    }

    public InventoryBarako getInventoryBarako() {
        return inventory;
    }
}
