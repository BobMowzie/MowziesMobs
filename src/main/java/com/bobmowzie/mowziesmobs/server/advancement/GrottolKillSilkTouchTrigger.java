package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.resources.ResourceLocation;

public class GrottolKillSilkTouchTrigger extends MMTrigger<CriterionInstance, GrottolKillSilkTouchTrigger.Listener> {
    public static final ResourceLocation ID = new ResourceLocation(MowziesMobs.MODID, "kill_grottol_silk_touch");

    public GrottolKillSilkTouchTrigger() {
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public CriterionInstance deserialize(JsonObject object, ConditionArrayParser conditions) {
        EntityPredicate.AndPredicate player = EntityPredicate.AndPredicate.deserializeJSONObject(object, "player", conditions);
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
            super(GrottolKillSilkTouchTrigger.ID, player);
        }
    }
}
