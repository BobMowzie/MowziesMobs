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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by Josh on 7/24/2018.
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

    public Vec3d dir;
    private int groundDist = 1;

    @OnlyIn(Dist.CLIENT)
    private Vec3d[] pos;

    public EntityLantern(EntityType<? extends EntityLantern> type, World world) {
        super(type, world);
        dir = null;

        if (world.isRemote) {
            pos = new Vec3d[1];
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SimpleAnimationAI<>(this, PUFF_ANIMATION, false));
        this.goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        this.goalSelector.addGoal(1, new AnimationDieAI<>(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D * ConfigHandler.MOBS.LANTERN.healthMultiplier.get());
        this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        if (getAnimation() == PUFF_ANIMATION && getAnimationTick() == 7) {
            if (groundDist == 0) groundDist = 1;
            setMotion(getMotion().add(0, 0.2d + 0.2d / groundDist, 0));
            if (world.isRemote) {
                for (int i = 0; i < 5; i++) {
                    ParticleVanillaCloudExtended.spawnVanillaCloud(world, posX, posY + 0.3, posZ, -getMotion().getX() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -getMotion().getY() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -getMotion().getZ() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), 0.8d + rand.nextDouble() * 1d, 163d / 256d, 247d / 256d, 74d / 256d, 0.95, 30);
                }
                for (int i = 0; i < 8; i++) {
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), posX, posY + 0.3, posZ, -getMotion().getX() * 0.2 + 0.2 * (rand.nextFloat() - 0.5), -getMotion().getY() * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -getMotion().getZ() * 0.2 + 0.2 * (rand.nextFloat() - 0.5), true, 0, 0, 0, 0, 4f, 163d / 256d, 247d / 256d, 74d / 256d, 1, 0.9, 17 + rand.nextFloat() * 10, true, new ParticleComponent[] {
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[] {4f, 0},
                                    new float[] {0.8f, 1}
                            ), false)
                    });
                }
            }
            playSound(MMSounds.ENTITY_LANTERN_PUFF.get(), 0.6f, 1f + rand.nextFloat() * 0.2f);
        }

        if (!world.isRemote && getAnimation() == NO_ANIMATION) {
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

        if (world.isRemote && ConfigHandler.CLIENT.glowEffect.get()) {
            pos[0] = getPositionVector().add(0, getHeight() * 0.8, 0);
            if (ticksExisted % 70 == 0) {
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.GLOW.get(), pos[0].x, pos[0].y, pos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 20F, 0.8, 0.95, 0.35, 1, 1, 70, true, new ParticleComponent[]{
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
        if (getAnimationTick() == 1 && world.isRemote) {
            for (int i = 0; i < 8; i++) {
                world.addParticle(ParticleTypes.ITEM_SLIME, posX, posY, posZ, 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5));
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 163f / 256f, 247f / 256f, 74f / 256f, 10f + rand.nextFloat() * 20f, 30, ParticleCloud.EnumCloudBehavior.GROW, 0.9f), posX, posY + 0.3, posZ, 0.25 * (rand.nextFloat() - 0.5), 0.25 * (rand.nextFloat() - 0.5), 0.25 * (rand.nextFloat() - 0.5));
                world.addParticle(new ParticleOrb.OrbData(163f / 256f, 247f / 256f, 74f / 256f, 1.5f, 25), posX, posY + 0.3, posZ, 0.2f * (rand.nextFloat() - 0.5f), 0.2f * (rand.nextFloat() - 0.5f), 0.2f * (rand.nextFloat() - 0.5f));
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
    public void travel(Vec3d movement)
    {
        if (this.isInWater()) {
            this.moveRelative(0.02F, movement);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale((double)0.8F));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, movement);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.5D));
        } else {
            BlockPos ground = new BlockPos(this.posX, this.getBoundingBox().minY - 1.0D, this.posZ);
            float f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
            }

            this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, movement);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale((double)f));
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.MOBS.LANTERN.spawnConfig;
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
}
