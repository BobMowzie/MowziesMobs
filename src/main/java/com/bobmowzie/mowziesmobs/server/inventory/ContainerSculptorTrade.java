package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public final class ContainerSculptorTrade extends ContainerTradeBase {
    private final EntitySculptor sculptor;
    private final InventorySculptor inventorySculptor;

    public ContainerSculptorTrade(int id, Inventory playerInventory) {
        this(id, (EntitySculptor) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerSculptorTrade(int id, EntitySculptor sculptor, Inventory playerInv) {
        this(id, sculptor, new InventorySculptor(sculptor), playerInv);
    }

    public ContainerSculptorTrade(int id, EntitySculptor sculptor, InventorySculptor inventory, Inventory playerInv) {
        super(ContainerHandler.CONTAINER_SCULPTOR_TRADE, id, sculptor, inventory, playerInv);
        this.sculptor = sculptor;
        this.inventorySculptor = inventory;
    }

    @Override
    protected void addCustomSlots(Inventory playerInv) {
        addSlot(new Slot(inventory, 0, 69, 54));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (sculptor != null) sculptor.setCustomer(null);
    }

    public EntitySculptor getSculptor() {
        return sculptor;
    }

    public InventorySculptor getInventorySculptor() {
        return inventorySculptor;
    }

    @Override
    public boolean stillValid(Player player) {
        return super.stillValid(player) && !sculptor.isTesting();
    }
}
