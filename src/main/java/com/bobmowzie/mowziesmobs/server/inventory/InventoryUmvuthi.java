package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.world.entity.player.Player;

public final class InventoryUmvuthi extends InventoryOneInput {
    private final EntityUmvuthi umvuthi;

    public InventoryUmvuthi(EntityUmvuthi umvuthi) {
        super(umvuthi);
        this.umvuthi = umvuthi;
    }

    @Override
    public boolean stillValid(Player player) {
        return umvuthi.getCustomer() == player;
    }
}
