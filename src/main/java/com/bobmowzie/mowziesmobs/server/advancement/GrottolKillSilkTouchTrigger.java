package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

public class GrottolKillSilkTouchTrigger extends MMTrigger<AbstractCriterionTriggerInstance, GrottolKillSilkTouchTrigger.Listener> {
    public static final ResourceLocation ID = new ResourceLocation(MowziesMobs.MODID, "kill_grottol_silk_touch");

    public GrottolKillSilkTouchTrigger() {
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public AbstractCriterionTriggerInstance createInstance(JsonObject object, DeserializationContext conditions) {
        ContextAwarePredicate player = EntityPredicate.fromJson(object, "player", conditions);
        return new GrottolKillSilkTouchTrigger.Instance(player);
    }

    @Override
    public GrottolKillSilkTouchTrigger.Listener createListener(PlayerAdvancements playerAdvancements) {
        return new GrottolKillSilkTouchTrigger.Listener(playerAdvancements);
    }

    public void trigger(ServerPlayer player) {
        GrottolKillSilkTouchTrigger.Listener listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Listener extends MMTrigger.Listener<AbstractCriterionTriggerInstance> {

        public Listener(PlayerAdvancements playerAdvancementsIn) {
            super(playerAdvancementsIn);
        }

        public void trigger() {
            this.listeners.stream().findFirst().ifPresent(listener -> listener.run(this.playerAdvancements));
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate player) {
            super(GrottolKillSilkTouchTrigger.ID, player);
        }
    }
}
