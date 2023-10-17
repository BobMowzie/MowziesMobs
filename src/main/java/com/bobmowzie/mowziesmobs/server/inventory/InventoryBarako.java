package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.minecraft.world.entity.player.Player;

public final class InventoryBarako extends InventoryOneInput {
    private final EntityBarako barako;

    public InventoryBarako(EntityBarako barako) {
        super(barako);
        this.barako = barako;
    }

    @Override
    public boolean stillValid(Player player) {
        return barako.getCustomer() == player;
    }
}
