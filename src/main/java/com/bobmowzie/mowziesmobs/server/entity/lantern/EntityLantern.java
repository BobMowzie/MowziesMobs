package com.bobmowzie.mowziesmobs.server.entity.lantern;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.controller.MovementController;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.math.AxisAlignedBB;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.math.MathHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Random;

/**
 * Created by BobMowzie on 7/24/2018.
 */
public class EntityLantern extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(25);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation PUFF_ANIMATION = Animation.create(28);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            PUFF_ANIMATION
    };

    public Vec3 dir;
    private int groundDist = 1;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] pos;

    public EntityLantern(EntityType<? extends EntityLantern> type, World world) {
        super(type, world);
        dir = null;

        if (world.isClientSide) {
            pos = new Vec3[1];
        }
        this.moveController = new MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, PUFF_ANIMATION, false));
        this.goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        this.goalSelector.addGoal(1, new AnimationDieAI<>(this));
        this.goalSelector.addGoal(5, new RandomFlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MowzieEntity.createAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 4.0D * ConfigHandler.COMMON.MOBS.LANTERN.healthMultiplier.get())
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.3D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public float getBrightness() {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        if (getAnimation() == PUFF_ANIMATION && getAnimationTick() == 7) {
            if (groundDist == 0) groundDist = 1;
            setMotion(getMotion().add(0, 0.2d + 0.2d / groundDist, 0));
            if (world.isClientSide) {
                for (int i = 0; i < 5; i++) {
                    ParticleVanillaCloudExtended.spawnVanillaCloud(world, getPosX(), getPosY() + 0.3, getPosZ(), -getMotion().getX() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -getMotion().getY() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -getMotion().getZ() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), 0.8d + rand.nextDouble() * 1d, 163d / 256d, 247d / 256d, 74d / 256d, 0.95, 30);
                }
                for (int i = 0; i < 8; i++) {
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), getPosX(), getPosY() + 0.3, getPosZ(), -getMotion().getX() * 0.2 + 0.2 * (rand.nextFloat() - 0.5), -getMotion().getY() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -getMotion().getZ() * 0.2 + 0.2 * (rand.nextFloat() - 0.5), true, 0, 0, 0, 0, 4f, 163d / 256d, 247d / 256d, 74d / 256d, 1, 0.9, 17 + rand.nextFloat() * 10, true, true, new ParticleComponent[] {
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[] {4f, 0},
                                    new float[] {0.8f, 1}
                            ), false)
                    });
                }
            }
            else {
                if (getMoveHelperController().getAction() == MovementController.Action.MOVE_TO) {
                    Vec3 lvt_1_1_ = new Vec3(getMoveHelper().getX() - this.getPosX(), getMoveHelper().getY() - this.getPosY(), getMoveHelper().getZ() - this.getPosZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (getMoveHelperController().func_220673_a(lvt_1_1_, MathHelper.ceil(lvt_2_1_))) {
                        setMotion(getMotion().add(lvt_1_1_.scale(0.2)));
                    }
                }
            }
            playSound(MMSounds.ENTITY_LANTERN_PUFF.get(), 0.6f, 1f + rand.nextFloat() * 0.2f);
        }

        if (!world.isClientSide && getAnimation() == NO_ANIMATION) {
            if (groundDist < 5 || (rand.nextInt(13) == 0 && groundDist < 16)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, PUFF_ANIMATION);
            }
        }

        if (groundDist >= 2) setMotion(getMotion().add(0, -0.0055d, 0));

        if (ticksExisted % 5 == 0) {
            BlockPos checkPos = getPosition();
            int i;
            for (i = 0; i < 16; i++) {
                if (world.getBlockState(checkPos).getBlock() != Blocks.AIR) break;
                checkPos = checkPos.down();
            }
            groundDist = i;
        }

        if (world.isClientSide && ConfigHandler.CLIENT.glowEffect.get()) {
            pos[0] = getPositionVec().add(0, getHeight() * 0.8, 0);
            if (ticksExisted % 70 == 0) {
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.GLOW.get(), pos[0].x, pos[0].y, pos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 20F, 0.8, 0.95, 0.35, 1, 1, 70, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                new float[]{0.0f, 0.8f, 0},
                                new float[]{0, 0.5f, 1}
                        ), false),
                        new ParticleComponent.PinLocation(pos)
                });
            }
        }

//        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, DIE_ANIMATION);
    }

    @Override
    protected void onDeathAIUpdate() {
        super.onDeathAIUpdate();
        if (getAnimationTick() == 1 && world.isClientSide) {
            for (int i = 0; i < 8; i++) {
                world.addParticle(ParticleTypes.ITEM_SLIME, getPosX(), getPosY(), getPosZ(), 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5));
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 163f / 256f, 247f / 256f, 74f / 256f, 10f + rand.nextFloat() * 20f, 30, ParticleCloud.EnumCloudBehavior.GROW, 0.9f), getPosX(), getPosY() + 0.3, getPosZ(), 0.25 * (rand.nextFloat() - 0.5), 0.25 * (rand.nextFloat() - 0.5), 0.25 * (rand.nextFloat() - 0.5));
                world.addParticle(new ParticleOrb.OrbData(163f / 256f, 247f / 256f, 74f / 256f, 1.5f, 25), getPosX(), getPosY() + 0.3, getPosZ(), 0.2f * (rand.nextFloat() - 0.5f), 0.2f * (rand.nextFloat() - 0.5f), 0.2f * (rand.nextFloat() - 0.5f));
            }
        }
        if (getAnimationTick() == 2) playSound(MMSounds.ENTITY_LANTERN_POP.get(), 1f, 0.8f + rand.nextFloat() * 0.4f);
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
    }

    @Override
    public void travel(Vec3 movement)
    {
        if (this.isInWater()) {
            this.moveRelative(0.02F, movement);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.8F));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, movement);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.5D));
        } else {
            BlockPos ground = new BlockPos(this.getPosX(), this.getBoundingBox().minY - 1.0D, this.getPosZ());
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            this.moveRelative(this.isOnGround() ? 0.1F * f1 : 0.02F, movement);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(f));
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.getPosX() - this.prevPosX;
        double d0 = this.getPosZ() - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isOnLadder()
    {
        return false;
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

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.LANTERN;
    }

    public MoveHelperController getMoveHelperController() {
        if (getMoveHelper() instanceof MoveHelperController)
            return (MoveHelperController) super.getMoveHelper();
        return null;
    }

    static class RandomFlyGoal extends Goal {
        private final EntityLantern parentEntity;

        public RandomFlyGoal(EntityLantern p_i45836_1_) {
            this.parentEntity = p_i45836_1_;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            MovementController lvt_1_1_ = this.parentEntity.getMoveHelper();
            if (!lvt_1_1_.isUpdating()) {
                return true;
            } else {
                double lvt_2_1_ = lvt_1_1_.getX() - this.parentEntity.getPosX();
                double lvt_4_1_ = lvt_1_1_.getY() - this.parentEntity.getPosY();
                double lvt_6_1_ = lvt_1_1_.getZ() - this.parentEntity.getPosZ();
                double lvt_8_1_ = lvt_2_1_ * lvt_2_1_ + lvt_4_1_ * lvt_4_1_ + lvt_6_1_ * lvt_6_1_;
                return lvt_8_1_ < 1.0D || lvt_8_1_ > 3600.0D;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            Random lvt_1_1_ = this.parentEntity.getRNG();
            double lvt_2_1_ = this.parentEntity.getPosX() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_4_1_ = this.parentEntity.getPosY() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_6_1_ = this.parentEntity.getPosZ() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(lvt_2_1_, lvt_4_1_, lvt_6_1_, 1.0D);
        }
    }

    static class MoveHelperController extends MovementController {
        private final EntityLantern parentEntity;
        protected int courseChangeCooldown;

        public MoveHelperController(EntityLantern p_i45838_1_) {
            super(p_i45838_1_);
            this.parentEntity = p_i45838_1_;
        }

        public void tick() {
            if (this.action == Action.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    Vec3 lvt_1_1_ = new Vec3(this.posX - this.parentEntity.getPosX(), this.posY - this.parentEntity.getPosY(), this.posZ - this.parentEntity.getPosZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (!this.func_220673_a(lvt_1_1_, MathHelper.ceil(lvt_2_1_))) {
                        this.action = Action.WAIT;
                    }
                }

            }
        }

        public boolean func_220673_a(Vec3 p_220673_1_, int p_220673_2_) {
            AxisAlignedBB lvt_3_1_ = this.parentEntity.getBoundingBox();

            for(int lvt_4_1_ = 1; lvt_4_1_ < p_220673_2_; ++lvt_4_1_) {
                lvt_3_1_ = lvt_3_1_.offset(p_220673_1_);
                if (!this.parentEntity.world.hasNoCollisions(this.parentEntity, lvt_3_1_)) {
                    return false;
                }
            }

            return true;
        }

        public Action getAction() {
            return action;
        }
    }
}
