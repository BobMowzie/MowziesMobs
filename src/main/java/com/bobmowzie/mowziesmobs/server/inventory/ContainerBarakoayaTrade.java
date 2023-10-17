package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ContainerBarakoayaTrade extends ContainerTradeBase {
    private final EntityBarakoaVillager barakoaVillager;
    private final InventoryBarakoaya inventoryBarakoaya;

    public ContainerBarakoayaTrade(int id, Inventory playerInventory) {
        this(id, (EntityBarakoaVillager) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerBarakoayaTrade(int id, EntityBarakoaVillager barakoaya, Inventory playerInv) {
        this(id, barakoaya, new InventoryBarakoaya(barakoaya), playerInv);
    }

    public ContainerBarakoayaTrade(int id, EntityBarakoaVillager barakoaVillager, InventoryBarakoaya inventory, Inventory playerInv) {
        super(ContainerHandler.CONTAINER_BARAKOAYA_TRADE, id, barakoaVillager, inventory, playerInv);
        this.inventoryBarakoaya = inventory;
        this.barakoaVillager = barakoaVillager;
    }

    @Override
    protected void addCustomSlots(Inventory playerInv) {
        addSlot(new Slot(getInventory(), 0, 80, 54));
        addSlot(new SlotResult(getInventory(), 1, 133, 54));
    }

    @Override
    public void slotsChanged(Container inv) {
        inventoryBarakoaya.reset();
        super.slotsChanged(inv);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (barakoaVillager != null) barakoaVillager.setCustomer(null);
    }

    public EntityBarakoaVillager getBarakoaVillager() {
        return barakoaVillager;
    }

    public InventoryBarakoaya getInventoryBarakoaya() {
        return inventoryBarakoaya;
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
            stack.onCraftedBy(barakoaVillager.level, player, removeCount);
            removeCount = 0;
        }

        @Override
        public ItemStack safeTake(int p_150648_, int p_150649_, Player p_150650_) {
            return super.safeTake(p_150648_, p_150649_, p_150650_);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            checkTakeAchievements(stack);
            if (barakoaVillager != null && barakoaVillager.isOfferingTrade()) {
                Trade trade = barakoaVillager.getOfferingTrade();
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
