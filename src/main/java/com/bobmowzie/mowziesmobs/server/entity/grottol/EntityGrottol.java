package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.google.common.eventbus.DeadEvent;
import com.sun.xml.internal.bind.v2.model.core.ID;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDigging;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;

/**
 * Created by Josh on 7/3/2018.
 */
public class EntityGrottol extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(73);
    public static final Animation HURT_ANIMATION = Animation.create(60);
    public static final Animation IDLE_ANIMATION = Animation.create(47);
    public static final Animation BURROW_ANIMATION = Animation.create(20);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            IDLE_ANIMATION,
            BURROW_ANIMATION
    };
    private int fleeTime = 0;
    private int timeSinceFlee = 50;

    private boolean killedWithPickaxe;
    private boolean killedWithSilkTouch;
    private boolean killedWithFortune;

    public EntityGrottol(World world) {
        super(world);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(4, new EntityAIWander(this, 0.3));
        tasks.addTask(1, new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 10f, 0.6, 0.8) {
            @Override
            public void updateTask() {
                super.updateTask();
                ((EntityGrottol)entity).fleeTime++;
            }

            @Override
            public void resetTask() {
                super.updateTask();
                ((EntityGrottol)entity).timeSinceFlee = 0;
            }
        });
        tasks.addTask(8, new EntityAILookIdle(this));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(1, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(3, new AnimationAI<>(this, IDLE_ANIMATION, false));
        tasks.addTask(1, new AnimationAI<>(this, BURROW_ANIMATION));
        experienceValue = 20;
        stepHeight = 1;
        setSize(1f, 1.2f);
        killedWithPickaxe = false;
        killedWithSilkTouch = false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entitySource = source.getTrueSource();
        if (entitySource != null && entitySource instanceof EntityLivingBase) {
            EntityLivingBase livingSource = (EntityLivingBase) entitySource;
            if (livingSource.getHeldItemMainhand().canHarvestBlock(Blocks.DIAMOND_ORE.getDefaultState())) {
                killedWithPickaxe = true;
                if (EnchantmentHelper.getEnchantments(livingSource.getHeldItemMainhand()).containsKey(Enchantments.SILK_TOUCH)) killedWithSilkTouch = true;
                if (EnchantmentHelper.getEnchantments(livingSource.getHeldItemMainhand()).containsKey(Enchantments.FORTUNE)) killedWithFortune = true;
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
        if (fleeTime >= 100 && getAnimation() == NO_ANIMATION) {
            IBlockState blockBeneath = world.getBlockState(getPosition().down());
            Material mat = blockBeneath.getMaterial();
            if (mat == Material.GRASS || mat == Material.GROUND || mat == Material.SAND || mat == Material.CLAY || mat == Material.ROCK) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, BURROW_ANIMATION);
            }
        }
        if (timeSinceFlee < 50) timeSinceFlee++;
        else fleeTime = 0;
        if (getAnimation() == BURROW_ANIMATION) {
            if (getAnimationTick() % 4 == 3) {
                IBlockState blockBeneath = world.getBlockState(getPosition().down());
                Material mat = blockBeneath.getMaterial();
                if (mat == Material.GRASS || mat == Material.GROUND || mat == Material.SAND || mat == Material.CLAY || mat == Material.ROCK) {
                    for (int i = 0; i < 8; i++) {
                        Vec3d particlePos = new Vec3d(0.7, 0, 0);
                        particlePos = particlePos.rotateYaw((float) Math.toRadians(-rotationYaw - 90));
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + particlePos.x + Math.random() * 0.5 - 0.25, posY + 0.05 + particlePos.y, posZ + particlePos.z + Math.random() * 0.5 - 0.25, 2 * (2 * Math.random() - 1), 5 * Math.random() + 2, 2 * (2 * Math.random() - 1), Block.getStateId(blockBeneath));
                    }
                }
            }
            if (getAnimationTick() == 19) {
                setDead();
            }
        }

//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, BURROW_ANIMATION);
//        }
    }

    @Override
    protected void dropLoot() {
        super.dropLoot();
        if (killedWithPickaxe) {
            int howMany = 1;
            if (killedWithFortune) howMany = 2;
            dropItem(Items.DIAMOND, howMany);
        }
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
