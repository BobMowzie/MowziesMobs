package com.bobmowzie.mowziesmobs.server.entity.skuttler;

import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.google.common.eventbus.DeadEvent;
import com.sun.xml.internal.bind.v2.model.core.ID;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by Josh on 7/3/2018.
 */
public class EntitySkuttler extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(60);
    public static final Animation HURT_ANIMATION = Animation.create(60);
    public static final Animation IDLE_ANIMATION = Animation.create(47);
    public static final Animation BURROW_ANIMATION = Animation.create(60);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            IDLE_ANIMATION,
            BURROW_ANIMATION
    };

    private boolean killedWithPickaxe;

    public EntitySkuttler(World world) {
        super(world);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(4, new EntityAIWander(this, 0.3));
        tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 10f, 0.6, 0.8));
        tasks.addTask(8, new EntityAILookIdle(this));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(1, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(3, new AnimationAI<>(this, IDLE_ANIMATION, false));
        experienceValue = 20;
        stepHeight = 1;
        setSize(1f, 1.2f);
        killedWithPickaxe = false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entitySource = source.getTrueSource();
        if (entitySource != null && entitySource instanceof EntityLivingBase) {
            EntityLivingBase livingSource = (EntityLivingBase) entitySource;
            if (livingSource.getHeldItemMainhand().canHarvestBlock(Blocks.DIAMOND_ORE.getDefaultState())) {
                killedWithPickaxe = true;
                super.attackEntityFrom(source, 20);
            }
            else {
                playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.4F, 2);
                return false;
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (getAnimation() == NO_ANIMATION && rand.nextInt(180) == 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
        }
//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, DIE_ANIMATION);
//        }
    }

    @Override
    protected void dropLoot() {
        super.dropLoot();
        if (killedWithPickaxe) dropItem(Items.DIAMOND, 1);
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }
}
