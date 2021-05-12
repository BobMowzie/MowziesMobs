package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class EntityBarakoaSunblocker extends EntityBarakoaya {
    EntityBarako healTarget;

    public EntityBarakoaSunblocker(EntityType<? extends EntityBarakoaya> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, TELEPORT_ANIMATION, false, false) {
            @Override
            public void tick() {
                super.tick();
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_START_ANIMATION, false, false) {
            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 23)
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_LOOP_ANIMATION);
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_STOP_ANIMATION, false, false));
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_LOOP_ANIMATION, false, false) {
            @Override
            public void tick() {
                super.tick();
                EntityBarakoaSunblocker barakeera = (EntityBarakoaSunblocker)entity;
                barakeera.addPotionEffect(new EffectInstance(Effects.GLOWING, 20, 0, false, false));
                if (barakeera.healTarget != null) barakeera.getLookController().setLookPositionWithEntity(barakeera.healTarget, entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
                if (getAnimationTick() == 19)
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_LOOP_ANIMATION);
            }
        });
    }

    @Override
    public void tick() {
        super.tick();

//        if (getAnimation() == HEAL_LOOP_ANIMATION && healTarget == null) AnimationHandler.INSTANCE.sendAnimationMessage(this, HEAL_STOP_ANIMATION);

        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, HEAL_START_ANIMATION);
    }
}
