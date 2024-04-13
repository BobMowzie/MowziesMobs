package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ContainerUmvuthanaTrade extends ContainerTradeBase {
    private final EntityUmvuthanaMinion umvuthanaMinion;
    private final InventoryUmvuthana inventoryUmvuthana;

    public ContainerUmvuthanaTrade(int id, Inventory playerInventory) {
        this(id, (EntityUmvuthanaMinion) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerUmvuthanaTrade(int id, EntityUmvuthanaMinion barakoaya, Inventory playerInv) {
        this(id, barakoaya, new InventoryUmvuthana(barakoaya), playerInv);
    }

    public ContainerUmvuthanaTrade(int id, EntityUmvuthanaMinion umvuthanaMinion, InventoryUmvuthana inventory, Inventory playerInv) {
        super(ContainerHandler.CONTAINER_UMVUTHANA_TRADE.get(), id, umvuthanaMinion, inventory, playerInv);
        this.inventoryUmvuthana = inventory;
        this.umvuthanaMinion = umvuthanaMinion;
    }

    @Override
    protected void addCustomSlots(Inventory playerInv) {
        addSlot(new Slot(getInventory(), 0, 80, 54));
        addSlot(new SlotResult(getInventory(), 1, 133, 54));
    }

    @Override
    public void slotsChanged(Container inv) {
        inventoryUmvuthana.reset();
        super.slotsChanged(inv);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (umvuthanaMinion != null) umvuthanaMinion.setCustomer(null);
    }

    public EntityUmvuthanaMinion getUmvuthana() {
        return umvuthanaMinion;
    }

    public InventoryUmvuthana getInventoryUmvuthana() {
        return inventoryUmvuthana;
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
            stack.onCraftedBy(umvuthanaMinion.level, player, removeCount);
            removeCount = 0;
        }

        @Override
        public ItemStack safeTake(int p_150648_, int p_150649_, Player p_150650_) {
            return super.safeTake(p_150648_, p_150649_, p_150650_);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            checkTakeAchievements(stack);
            if (umvuthanaMinion != null && umvuthanaMinion.isOfferingTrade()) {
                Trade trade = umvuthanaMinion.getOfferingTrade();
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
