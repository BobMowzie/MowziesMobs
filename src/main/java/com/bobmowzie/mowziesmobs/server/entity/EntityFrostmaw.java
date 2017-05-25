package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.ai.animation.EntityAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Josh on 5/8/2017.
 */
public class EntityFrostmaw extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(130);
    public static final Animation HURT_ANIMATION = Animation.create(13);
    public static final Animation ROAR_ANIMATION = Animation.create(76);
    public static final Animation SWIPE_ANIMATION = Animation.create(27);
    public static final Animation SWIPE_TWICE_ANIMATION = Animation.create(55);
    public static final Animation ICE_BREATH_ANIMATION = Animation.create(77);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(20);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(100);

    public boolean swingWhichArm = false;

    public LegSolverQuadruped legSolver;

    public EntityFrostmaw(World world) {
        super(world);
        setSize(4.5f, 4.5f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(2, new AnimationAreaAttackAI<>(this, SWIPE_ANIMATION, null, null, 2, 7, 120, 1, 9));
        this.tasks.addTask(2, new AnimationAreaAttackAI<>(this, SWIPE_TWICE_ANIMATION, null, null, 1, 7, 120, 1, 9));
        this.tasks.addTask(2, new AnimationAI<>(this, ICE_BREATH_ANIMATION, true));
        this.tasks.addTask(2, new AnimationAI<>(this, ROAR_ANIMATION, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntitySheep.class, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        stepHeight = 1;
        frame += rand.nextInt(50);
        legSolver = new LegSolverQuadruped(1f, 2f, -1, 1.5f);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
    }

    @Override
    public void playLivingSound() {
        super.playLivingSound();
        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, ROAR_ANIMATION);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        legSolver.update(this);
//        renderYawOffset = rotationYaw;
        this.repelEntities(4f, 4f, 4f, 4f);

        if ((getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) && getAnimationTick() == 1) {
            swingWhichArm = rand.nextBoolean();
        }

        if (getAnimation() == SWIPE_TWICE_ANIMATION && currentAnim instanceof AnimationAreaAttackAI<?> && getAnimationTick() == 20) {
            ((AnimationAreaAttackAI<?>)currentAnim).hitEntities();
        }

        if (getAnimation() == ROAR_ANIMATION) {
            if (getAnimationTick() == 10) {
                playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
            }
        }

        if (getAttackTarget() != null) {
            getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1);
            if (targetDistance <= 6 && getAnimation() == NO_ANIMATION) {
                if (rand.nextInt(3) == 0) AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_TWICE_ANIMATION);
                else AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_ANIMATION);
            }
        }
        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BREATH_ANIMATION);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
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
        return new Animation[] {DIE_ANIMATION, HURT_ANIMATION, ROAR_ANIMATION, SWIPE_ANIMATION, SWIPE_TWICE_ANIMATION, ICE_BREATH_ANIMATION, ACTIVATE_ANIMATION, DEACTIVATE_ANIMATION};
    }
}
