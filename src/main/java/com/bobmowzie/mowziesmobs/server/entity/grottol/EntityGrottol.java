package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.server.ai.MMAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.MMEntityMoveHelper;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.base.Predicates;
import com.google.common.eventbus.DeadEvent;
import com.sun.xml.internal.bind.v2.model.core.ID;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.Sound;
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
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Josh on 7/3/2018.
 */
public class EntityGrottol extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(73);
    public static final Animation HURT_ANIMATION = Animation.create(10);
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
        setPathPriority(PathNodeType.DANGER_OTHER, 1);
        setPathPriority(PathNodeType.WATER, 3);
        setPathPriority(PathNodeType.LAVA, 1);
        setPathPriority(PathNodeType.DANGER_FIRE, 1);
        setPathPriority(PathNodeType.DANGER_CACTUS, 1);
        tasks.addTask(3, new EntityAISwimming(this));
        tasks.addTask(4, new EntityAIWander(this, 0.3));
        tasks.addTask(1, new MMAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 16f, 0.5, 0.7) {
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
        tasks.addTask(5, new AnimationAI<>(this, IDLE_ANIMATION, false));
        tasks.addTask(2, new AnimationAI<>(this, BURROW_ANIMATION, false));
        experienceValue = 20;
        stepHeight = 1;
        setSize(1f, 1.2f);
        killedWithPickaxe = false;
        killedWithSilkTouch = false;

        moveHelper = new MMEntityMoveHelper(this, 45);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return (float)(new Vec3d(pos.getX(), pos.getY(), pos.getZ()).distanceTo(getPositionVector()));
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        return super.handleWaterMovement();
    }

    @Override
    protected float getWaterSlowDown() {
        return 1;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
    }

    @Override
    public boolean getCanSpawnHere() {
        return posY <= 50 && !world.canSeeSky(getPosition()) && getEntitiesNearby(EntityGrottol.class, 20, 20, 20, 20).isEmpty() && super.getCanSpawnHere();
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
        if (ticksExisted == 1) System.out.println("Grottle at " + getPosition());

        //Footstep Sounds
        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (frame % 6 == 0 && speed > 0.05) {
            playSound(MMSounds.ENTITY_GROTTOL_STEP, 1F, 1.8f);
        }

        if (getAnimation() == NO_ANIMATION && rand.nextInt(180) == 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
        }
        if (fleeTime >= 70 && getAnimation() == NO_ANIMATION) {
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
                playSound(SoundEvents.BLOCK_SAND_PLACE, 1, 0.8f + rand.nextFloat() * 0.4f);
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

        if (getAnimation() == IDLE_ANIMATION) {
            if (getAnimationTick() == 28 || getAnimationTick() == 33) playSound(SoundEvents.BLOCK_STONE_STEP, 0.5f, 1.4f);
        }

//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
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
    protected SoundEvent getDeathSound() {
        playSound(MMSounds.EFFECT_GEOMANCY_BREAK, 1f, 1.3f);
        return null;
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
