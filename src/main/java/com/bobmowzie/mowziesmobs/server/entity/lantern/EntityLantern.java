package com.bobmowzie.mowziesmobs.server.entity.lantern;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

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

    @SideOnly(Side.CLIENT)
    private Vec3d[] pos;

    public EntityLantern(World world) {
        super(world);
        setSize(1, 1);
        dir = null;

        if (world.isRemote) {
            pos = new Vec3d[1];
        }
    }

    @Override
    public boolean isCreatureType(EntityClassification type, boolean forSpawnCount) {
        if (forSpawnCount && isNoDespawnRequired()) return false;
        return type == EntityClassification.AMBIENT;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(2, new SimpleAnimationAI<>(this, PUFF_ANIMATION, false));
        this.tasks.addTask(3, new AnimationTakeDamage<>(this));
        this.tasks.addTask(1, new AnimationDieAI<>(this));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D * ConfigHandler.MOBS.LANTERN.healthMultiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return 0xF000F0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (getAnimation() == PUFF_ANIMATION && getAnimationTick() == 7) {
            if (groundDist == 0) groundDist = 1;
            motionY += 0.2D + 0.2 / groundDist;
            if (world.isRemote) {
                for (int i = 0; i < 5; i++) {
                    ParticleVanillaCloudExtended.spawnVanillaCloud(world, posX, posY + 0.3, posZ, -motionX * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -motionY * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -motionZ * 0.2 + 0.1 * (rand.nextFloat() - 0.5), 0.8d + rand.nextDouble() * 1d, 163d / 256d, 247d / 256d, 74d / 256d, 0.95, 30);
                }
                for (int i = 0; i < 8; i++) {
                    MowzieParticleBase.spawnParticle(world, MMParticle.PIXEL, posX, posY + 0.3, posZ, -motionX * 0.2 + 0.2 * (rand.nextFloat() - 0.5), -motionY * 0.2 + 0.1 * (rand.nextFloat() - 0.5), -motionZ * 0.2 + 0.2 * (rand.nextFloat() - 0.5), true, 0, 0, 0, 0, 4f, 163d / 256d, 247d / 256d, 74d / 256d, 1, 0.9, 17 + rand.nextFloat() * 10, true, new ParticleComponent[] {
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[] {4f, 0},
                                    new float[] {0.8f, 1}
                            ), false)
                    });
                }
            }
            playSound(MMSounds.ENTITY_LANTERN_PUFF, 0.6f, 1f + rand.nextFloat() * 0.2f);
        }

        if (!world.isRemote && getAnimation() == NO_ANIMATION) {
            if (groundDist < 5 || (rand.nextInt(13) == 0 && groundDist < 16)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, PUFF_ANIMATION);
            }
        }

        if (groundDist >= 2) motionY -= 0.0055;

        if (ticksExisted % 5 == 0) {
            BlockPos checkPos = getPosition();
            int i;
            for (i = 0; i < 16; i++) {
                if (world.getBlockState(checkPos).getBlock() != Blocks.AIR) break;
                checkPos = checkPos.down();
            }
            groundDist = i;
        }

        if (world.isRemote && ConfigHandler.MOBS.LANTERN.glowEffect) {
            pos[0] = getPositionVector().add(0, height * 0.8, 0);
            if (ticksExisted % 70 == 0) {
                MowzieParticleBase.spawnParticle(world, MMParticle.GLOW, pos[0].x, pos[0].y, pos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 20F, 0.8, 0.95, 0.35, 1, 1, 70, true, new ParticleComponent[]{
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
                world.spawnParticle(EnumParticleTypes.SLIME, posX, posY, posZ, 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5));
                MMParticle.CLOUD.spawn(world, posX, posY + 0.3, posZ, ParticleFactory.ParticleArgs.get().withData(0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 163d / 256d, 247d / 256d, 74d / 256d, 1, 10d + rand.nextDouble() * 20d, 30, ParticleCloud.EnumCloudBehavior.GROW));
                MMParticle.ORB.spawn(world, posX, posY + 0.3, posZ, ParticleFactory.ParticleArgs.get().withData(0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 0.2 * (rand.nextFloat() - 0.5), 163d / 256d, 247d / 256d, 74d / 256d, 1.5d, 25));
            }
        }
        if (getAnimationTick() == 2) playSound(MMSounds.ENTITY_LANTERN_POP, 1f, 0.8f + rand.nextFloat() * 0.4f);
    }

    @Override
    protected Item getDropItem() {
        return null;//ItemHandler.GLOWING_JELLY;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.LANTERN;
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
    }

    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (this.isInWater())
        {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.isInLava())
        {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float f = 0.91F;

            if (this.onGround)
            {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                BlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (this.onGround)
            {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                BlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)f;
            this.motionY *= (double)f;
            this.motionZ *= (double)f;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    protected ConfigHandler.SpawnData getSpawnConfig() {
        return ConfigHandler.MOBS.LANTERN.spawnData;
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
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

}
