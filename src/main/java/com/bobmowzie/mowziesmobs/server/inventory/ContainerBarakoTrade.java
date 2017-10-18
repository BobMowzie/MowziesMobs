package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
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
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack contained = slot.getStack();
			stack = contained.copy();
			if (index != 0) {
				if (index >= 1 && index < 28) {
					if (!mergeItemStack(contained, 28, 37, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 28 && index < 37 && !mergeItemStack(contained, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(contained, 1, 37, false)) {
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
		barako.setCustomer(null);
		if (!world.isRemote) {
			ItemStack stack = inventory.removeStackFromSlot(0);
			if (stack != ItemStack.EMPTY) {
				EntityItem dropped = player.dropItem(stack, false);
				if (dropped != null) {
					dropped.motionX *= 0.5;
					dropped.motionZ *= 0.5;
				}
			}
		}
	}

	public InventoryBarako getInventoryBarako() {
		return inventory;
	}
}
