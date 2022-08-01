package com.bobmowzie.mowziesmobs.server.advancement;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.server.PlayerAdvancements;

import java.util.Map;
import java.util.Set;

public abstract class MMTrigger<E extends CriterionTriggerInstance, T extends MMTrigger.Listener<E>> implements CriterionTrigger<E> {

    protected final Map<PlayerAdvancements, T> listeners = Maps.newHashMap();

    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancements, CriterionTrigger.Listener<E> listener) {
        Listener<E> listeners = this.listeners.computeIfAbsent(playerAdvancements, this::createListener);

        listeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancements, CriterionTrigger.Listener<E> listener) {
        Listener<E> listeners = this.listeners.get(playerAdvancements);

        if (listeners != null) {
            listeners.remove(listener);

            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public abstract T createListener(PlayerAdvancements playerAdvancements);

    static class Listener<E extends CriterionTriggerInstance> {
        protected final PlayerAdvancements playerAdvancements;
        protected final Set<CriterionTrigger.Listener<E>> listeners = Sets.newHashSet();

        public Listener(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(CriterionTrigger.Listener<E> listener) {
            this.listeners.add(listener);
        }

        public void remove(CriterionTrigger.Listener<E> listener) {
            this.listeners.remove(listener);
        }
    }
}