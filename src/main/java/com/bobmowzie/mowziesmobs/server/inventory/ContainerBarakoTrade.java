package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public final class ContainerBarakoTrade extends ContainerTradeBase {
    private final EntityBarako barako;
    private final InventoryBarako inventoryBarako;

    public ContainerBarakoTrade(int id, Inventory playerInventory) {
        this(id, (EntityBarako) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerBarakoTrade(int id, EntityBarako barako, Inventory playerInv) {
        this(id, barako, new InventoryBarako(barako), playerInv);
    }

    public ContainerBarakoTrade(int id, EntityBarako barako, InventoryBarako inventory, Inventory playerInv) {
        super(ContainerHandler.CONTAINER_BARAKO_TRADE, id, barako, inventory, playerInv);
        this.barako = barako;
        this.inventoryBarako = inventory;
    }

    @Override
    protected void addCustomSlots(Inventory playerInv) {
        EntityBarako barako = (EntityBarako) getTradingMob();
        InventoryBarako inventoryBarako = (InventoryBarako) this.inventory;
        if (barako != null && !barako.hasTradedWith(playerInv.player)) addSlot(new Slot(inventoryBarako, 0, 69, 54));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (barako != null) barako.setCustomer(null);
    }

    public EntityBarako getBarako() {
        return barako;
    }

    public InventoryBarako getInventoryBarako() {
        return inventoryBarako;
    }
}
