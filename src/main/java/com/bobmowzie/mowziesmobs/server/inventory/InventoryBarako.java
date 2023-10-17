package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.world.entity.player.Player;

public final class InventoryBarako extends InventoryOneInput {
    private final EntityUmvuthi barako;

    public InventoryBarako(EntityUmvuthi barako) {
        super(barako);
        this.barako = barako;
    }

    @Override
    public boolean stillValid(Player player) {
        return barako.getCustomer() == player;
    }
}
