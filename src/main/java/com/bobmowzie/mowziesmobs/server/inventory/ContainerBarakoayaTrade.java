package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ContainerBarakoayaTrade extends AbstractContainerMenu {

    private final EntityBarakoaVillager barakoaya;

    private final InventoryBarakoaya inventory;

    private final Player player;

    public ContainerBarakoayaTrade(int id, Inventory playerInventory) {
        this(id, (EntityBarakoaVillager) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerBarakoayaTrade(int id, EntityBarakoaVillager barakoaya, Inventory playerInv) {
        this(id, barakoaya, new InventoryBarakoaya(barakoaya), playerInv);
    }

    public ContainerBarakoayaTrade(int id, EntityBarakoaVillager barakoaya, InventoryBarakoaya inventory, Inventory playerInv) {
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
    public boolean stillValid(Player player) {
        return barakoaya != null && inventory.stillValid(player) && barakoaya.isAlive() && barakoaya.distanceTo(player) < 8;
    }

    @Override
    public void slotsChanged(Container inv) {
        inventory.reset();
        super.slotsChanged(inv);
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
        if (barakoaya != null) barakoaya.setCustomer(null);
        if (!player.level.isClientSide) {
            ItemStack stack = inventory.removeItemNoUpdate(0);
            if (stack != ItemStack.EMPTY) {
                player.drop(stack, false);
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

        public SlotResult(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack remove(int amount) {
            if (hasItem()) {
                removeCount += Math.min(amount, getItem().getCount());
            }
            return super.remove(amount);
        }

        @Override
        protected void onQuickCraft(ItemStack stack, int amount) {
            removeCount += amount;
            super.onQuickCraft(stack, amount);
        }

        @Override
        protected void checkTakeAchievements(ItemStack stack) {
            stack.onCraftedBy(barakoaya.level, player, removeCount);
            removeCount = 0;
        }

        @Override
        public ItemStack safeTake(int p_150648_, int p_150649_, Player p_150650_) {
            return super.safeTake(p_150648_, p_150649_, p_150650_);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            checkTakeAchievements(stack);
            if (barakoaya != null && barakoaya.isOfferingTrade()) {
                Trade trade = barakoaya.getOfferingTrade();
                ItemStack input = container.getItem(0);
                ItemStack tradeInput = trade.getInput();
                if (input.getItem() == tradeInput.getItem() && input.getCount() >= tradeInput.getCount()) {
                    input.shrink(tradeInput.getCount());
                    if (input.getCount() <= 0) {
                        input = ItemStack.EMPTY;
                    }
                    container.setItem(0, input);
                }
            }
        }
    }
}
