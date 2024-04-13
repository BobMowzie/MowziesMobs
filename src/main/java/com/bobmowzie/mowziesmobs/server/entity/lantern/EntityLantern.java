package com.bobmowzie.mowziesmobs.server.entity.lantern;

import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

/**
 * Created by BobMowzie on 7/24/2018.
 */
public class EntityLantern extends MowzieLLibraryEntity {
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

    public EntityLantern(EntityType<? extends EntityLantern> type, Level world) {
        super(type, world);
        dir = null;

        if (world.isClientSide) {
            pos = new Vec3[1];
        }
        this.moveControl = new MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, PUFF_ANIMATION, false));
        this.goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        this.goalSelector.addGoal(1, new AnimationDieAI<>(this));
        this.goalSelector.addGoal(5, new RandomFlyGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.FLYING_SPEED, 0.3D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        if (getAnimation() == PUFF_ANIMATION && getAnimationTick() == 7) {
            if (groundDist == 0) groundDist = 1;
            setDeltaMovement(getDeltaMovement().add(0, 0.2d + 0.2d / groundDist, 0));
            if (level.isClientSide) {
                for (int i = 0; i < 5; i++) {
                    ParticleVanillaCloudExtended.spawnVanillaCloud(level, getX(), getY() + 0.3, getZ(), -getDeltaMovement().x() * 0.2 + 0.1 * (random.nextFloat() - 0.5), -getDeltaMovement().y() * 0.2 + 0.1 * (random.nextFloat() - 0.5), -getDeltaMovement().z() * 0.2 + 0.1 * (random.nextFloat() - 0.5), 0.8d + random.nextDouble() * 1d, 163d / 256d, 247d / 256d, 74d / 256d, 0.95, 30);
                }
                for (int i = 0; i < 8; i++) {
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL.get(), getX(), getY() + 0.3, getZ(), -getDeltaMovement().x() * 0.2 + 0.2 * (random.nextFloat() - 0.5), -getDeltaMovement().y() * 0.2 + 0.1 * (random.nextFloat() - 0.5), -getDeltaMovement().z() * 0.2 + 0.2 * (random.nextFloat() - 0.5), true, 0, 0, 0, 0, 4f, 163d / 256d, 247d / 256d, 74d / 256d, 1, 0.9, 17 + random.nextFloat() * 10, true, true, new ParticleComponent[] {
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[] {4f, 0},
                                    new float[] {0.8f, 1}
                            ), false)
                    });
                }
            }
            else {
                if (getMoveHelperController().isMovingTo()) {
                    Vec3 lvt_1_1_ = new Vec3(getMoveControl().getWantedX() - this.getX(), getMoveControl().getWantedY() - this.getY(), getMoveControl().getWantedZ() - this.getZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (getMoveHelperController().canReach(lvt_1_1_, Mth.ceil(lvt_2_1_))) {
                        setDeltaMovement(getDeltaMovement().add(lvt_1_1_.scale(0.2)));
                    }
                }
            }
            playSound(MMSounds.ENTITY_LANTERN_PUFF.get(), 0.6f, 1f + random.nextFloat() * 0.2f);
        }

        if (!level.isClientSide && getAnimation() == NO_ANIMATION) {
            if (groundDist < 5 || (random.nextInt(13) == 0 && groundDist < 16)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, PUFF_ANIMATION);
            }
        }

        if (groundDist >= 2) setDeltaMovement(getDeltaMovement().add(0, -0.0055d, 0));

        if (tickCount % 5 == 0) {
            BlockPos checkPos = blockPosition();
            int i;
            for (i = 0; i < 16; i++) {
                if (level.getBlockState(checkPos).getBlock() != Blocks.AIR) break;
                checkPos = checkPos.below();
            }
            groundDist = i;
        }

        if (level.isClientSide && ConfigHandler.CLIENT.glowEffect.get()) {
            pos[0] = position().add(0, getBbHeight() * 0.8, 0);
            if (tickCount % 70 == 0) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW.get(), pos[0].x, pos[0].y, pos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 20F, 0.8, 0.95, 0.35, 1, 1, 70, true, true, new ParticleComponent[]{
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
    protected void tickDeath() {
        super.tickDeath();
        if (getAnimationTick() == 1 && level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.ITEM_SLIME, getX(), getY(), getZ(), 0.2 * (random.nextFloat() - 0.5), 0.2 * (random.nextFloat() - 0.5), 0.2 * (random.nextFloat() - 0.5));
                level.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 163f / 256f, 247f / 256f, 74f / 256f, 10f + random.nextFloat() * 20f, 30, ParticleCloud.EnumCloudBehavior.GROW, 0.9f), getX(), getY() + 0.3, getZ(), 0.25 * (random.nextFloat() - 0.5), 0.25 * (random.nextFloat() - 0.5), 0.25 * (random.nextFloat() - 0.5));
                level.addParticle(new ParticleOrb.OrbData(163f / 256f, 247f / 256f, 74f / 256f, 1.5f, 25), getX(), getY() + 0.3, getZ(), 0.2f * (random.nextFloat() - 0.5f), 0.2f * (random.nextFloat() - 0.5f), 0.2f * (random.nextFloat() - 0.5f));
            }
        }
        if (getAnimationTick() == 2) playSound(MMSounds.ENTITY_LANTERN_POP.get(), 1f, 0.8f + random.nextFloat() * 0.4f);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
    }

    @Override
    public void travel(Vec3 movement)
    {
        if (this.isInWater()) {
            this.moveRelative(0.02F, movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        } else {
            BlockPos ground = new BlockPos(this.getX(), this.getBoundingBox().minY - 1.0D, this.getZ());
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.level.getBlockState(ground).getFriction(level, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.level.getBlockState(ground).getFriction(level, ground, this) * 0.91F;
            }

            this.moveRelative(this.isOnGround() ? 0.1F * f1 : 0.02F, movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(f));
        }

        this.animationSpeedOld = this.animationSpeed;
        double d1 = this.getX() - this.xo;
        double d0 = this.getZ() - this.zo;
        float f2 = Mth.sqrt((float) (d1 * d1 + d0 * d0)) * 4.0F;
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.animationSpeed += (f2 - this.animationSpeed) * 0.4F;
        this.animationPosition += this.animationSpeed;
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean onClimbable()
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
    protected ResourceLocation getDefaultLootTable() {
        return LootTableHandler.LANTERN;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.LANTERN.combatConfig;
    }

    public MoveHelperController getMoveHelperController() {
        if (getMoveControl() instanceof MoveHelperController)
            return (MoveHelperController) super.getMoveControl();
        return null;
    }

    static class RandomFlyGoal extends Goal {
        private final EntityLantern parentEntity;

        public RandomFlyGoal(EntityLantern p_i45836_1_) {
            this.parentEntity = p_i45836_1_;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl lvt_1_1_ = this.parentEntity.getMoveControl();
            if (!lvt_1_1_.hasWanted()) {
                return true;
            } else {
                double lvt_2_1_ = lvt_1_1_.getWantedX() - this.parentEntity.getX();
                double lvt_4_1_ = lvt_1_1_.getWantedY() - this.parentEntity.getY();
                double lvt_6_1_ = lvt_1_1_.getWantedZ() - this.parentEntity.getZ();
                double lvt_8_1_ = lvt_2_1_ * lvt_2_1_ + lvt_4_1_ * lvt_4_1_ + lvt_6_1_ * lvt_6_1_;
                return lvt_8_1_ < 1.0D || lvt_8_1_ > 3600.0D;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource lvt_1_1_ = this.parentEntity.getRandom();
            double lvt_2_1_ = this.parentEntity.getX() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_4_1_ = this.parentEntity.getY() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_6_1_ = this.parentEntity.getZ() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveControl().setWantedPosition(lvt_2_1_, lvt_4_1_, lvt_6_1_, 1.0D);
        }
    }

    static class MoveHelperController extends MoveControl {
        private final EntityLantern parentEntity;
        protected int courseChangeCooldown;

        public MoveHelperController(EntityLantern p_i45838_1_) {
            super(p_i45838_1_);
            this.parentEntity = p_i45838_1_;
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRandom().nextInt(5) + 2;
                    Vec3 lvt_1_1_ = new Vec3(this.wantedX - this.parentEntity.getX(), this.wantedY - this.parentEntity.getY(), this.wantedZ - this.parentEntity.getZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (!this.canReach(lvt_1_1_, Mth.ceil(lvt_2_1_))) {
                        this.operation = Operation.WAIT;
                    }
                }

            }
        }

        public boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
            AABB lvt_3_1_ = this.parentEntity.getBoundingBox();

            for(int lvt_4_1_ = 1; lvt_4_1_ < p_220673_2_; ++lvt_4_1_) {
                lvt_3_1_ = lvt_3_1_.move(p_220673_1_);
                if (!this.parentEntity.level.noCollision(this.parentEntity, lvt_3_1_)) {
                    return false;
                }
            }

            return true;
        }

        public boolean isMovingTo() {
            return operation == Operation.MOVE_TO;
        }
    }
}
