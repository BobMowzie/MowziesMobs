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

public class StealIceCrystalTrigger extends MMTrigger<AbstractCriterionTriggerInstance, StealIceCrystalTrigger.Listener> {
    public static final ResourceLocation ID = new ResourceLocation(MowziesMobs.MODID, "steal_ice_crystal");

    public StealIceCrystalTrigger() {
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public AbstractCriterionTriggerInstance createInstance(JsonObject object, DeserializationContext conditions) {
        ContextAwarePredicate player = EntityPredicate.fromJson(object, "player", conditions);
        return new StealIceCrystalTrigger.Instance(player);
    }

    @Override
    public StealIceCrystalTrigger.Listener createListener(PlayerAdvancements playerAdvancements) {
        return new StealIceCrystalTrigger.Listener(playerAdvancements);
    }

    public void trigger(ServerPlayer player) {
        StealIceCrystalTrigger.Listener listeners = this.listeners.get(player.getAdvancements());

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
            super(StealIceCrystalTrigger.ID, player);
        }
    }
}
