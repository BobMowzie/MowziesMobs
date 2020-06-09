package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class SneakVillageTrigger extends MMTrigger<AbstractCriterionInstance, SneakVillageTrigger.Listener> {
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
    public AbstractCriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new AbstractCriterionInstance(ID);
    }

    public void trigger(EntityPlayerMP player) {
        SneakVillageTrigger.Listener listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Listener extends MMTrigger.Listener<AbstractCriterionInstance> {

        public Listener(PlayerAdvancements playerAdvancementsIn) {
            super(playerAdvancementsIn);
        }

        public void trigger() {
            this.listeners.stream().findFirst().ifPresent(listener -> listener.grantCriterion(this.playerAdvancements));
        }
    }
}
