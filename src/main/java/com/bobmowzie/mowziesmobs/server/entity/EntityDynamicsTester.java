package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDynamicsTester extends MowzieEntity {
    @OnlyIn(Dist.CLIENT)
    public DynamicChain dc;

    public EntityDynamicsTester(Level world) {
        super(EntityHandler.NAGA.get(), world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new RandomStrollGoal(this, 0.3));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            if (tickCount == 1) {
                dc = new DynamicChain(this);
            }
            dc.updateSpringConstraint(0.1f, 0.3f, 0.6f, 1f, true, 0.5f, 1);
            yBodyRot = yRot;
        }
    }
}
