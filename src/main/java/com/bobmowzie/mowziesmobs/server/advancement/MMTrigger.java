package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class MMTrigger<E extends ICriterionInstance, T extends MMTrigger.Listener<E>> implements ICriterionTrigger<E> {

    protected final Map<PlayerAdvancements, T> listeners = Maps.newHashMap();

    @Override
    public void addListener(PlayerAdvancements playerAdvancements, ICriterionTrigger.Listener<E> listener) {
        Listener<E> listeners = this.listeners.computeIfAbsent(playerAdvancements, this::createListener);

        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancements, ICriterionTrigger.Listener<E> listener) {
        Listener<E> listeners = this.listeners.get(playerAdvancements);

        if (listeners != null) {
            listeners.remove(listener);

            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public abstract T createListener(PlayerAdvancements playerAdvancements);

    static class Listener<E extends ICriterionInstance> {
        protected final PlayerAdvancements playerAdvancements;
        protected final Set<ICriterionTrigger.Listener<E>> listeners = Sets.newHashSet();

        public Listener(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<E> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<E> listener) {
            this.listeners.remove(listener);
        }
    }
}