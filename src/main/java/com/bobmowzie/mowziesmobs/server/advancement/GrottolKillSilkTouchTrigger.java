package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class GrottolKillSilkTouchTrigger extends MMTrigger<CriterionInstance, GrottolKillSilkTouchTrigger.Listener> {
    public static final ResourceLocation ID = new ResourceLocation(MowziesMobs.MODID, "kill_grottol_silk_touch");

    public GrottolKillSilkTouchTrigger() {
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public GrottolKillSilkTouchTrigger.Listener createListener(PlayerAdvancements playerAdvancements) {
        return new GrottolKillSilkTouchTrigger.Listener(playerAdvancements);
    }

    @Override
    public CriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new CriterionInstance(ID);
    }

    public void trigger(ServerPlayerEntity player) {
        GrottolKillSilkTouchTrigger.Listener listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Listener extends MMTrigger.Listener<CriterionInstance> {

        public Listener(PlayerAdvancements playerAdvancementsIn) {
            super(playerAdvancementsIn);
        }

        public void trigger() {
            this.listeners.stream().findFirst().ifPresent(listener -> listener.grantCriterion(this.playerAdvancements));
        }
    }
}
