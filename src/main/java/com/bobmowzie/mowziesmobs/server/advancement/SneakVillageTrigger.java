package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class SneakVillageTrigger extends MMTrigger<CriterionInstance, SneakVillageTrigger.Listener> {
    public static final ResourceLocation ID = new ResourceLocation(MowziesMobs.MODID, "sneak_village");

    public SneakVillageTrigger() {
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public SneakVillageTrigger.Listener createListener(PlayerAdvancements playerAdvancements) {
        return new SneakVillageTrigger.Listener(playerAdvancements);
    }

    @Override
    public CriterionInstance deserialize(JsonObject object, ConditionArrayParser conditions) {
        EntityPredicate.AndPredicate player = EntityPredicate.AndPredicate.deserializeJSONObject(object, "player", conditions);
        return new SneakVillageTrigger.Instance(player);
    }

    public void trigger(ServerPlayerEntity player) {
        SneakVillageTrigger.Listener listeners = this.listeners.get(player.getAdvancements());

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

    public static class Instance extends CriterionInstance {
        public Instance(EntityPredicate.AndPredicate player) {
            super(SneakVillageTrigger.ID, player);
        }
    }
}
