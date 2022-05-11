package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FrozenCapability {
    public static int MAX_FREEZE_DECAY_DELAY = 10;

    public interface IFrozenCapability extends INBTSerializable<CompoundTag> {
        boolean getFrozen();

        float getFreezeProgress();

        void setFreezeProgress(float freezeProgress);

        float getFrozenYaw();

        void setFrozenYaw(float frozenYaw);

        float getFrozenPitch();

        void setFrozenPitch(float frozenPitch);

        float getFrozenYawHead();

        void setFrozenYawHead(float frozenYawHead);

        float getFrozenRenderYawOffset();

        void setFrozenRenderYawOffset(float frozenRenderYawOffset);

        float getFrozenSwingProgress();

        void setFrozenSwingProgress(float frozenSwingProgress);

        float getFrozenLimbSwingAmount();

        void setFrozenLimbSwingAmount(float frozenLimbSwingAmount);

        boolean prevHasAI();

        void setPrevHasAI(boolean prevHasAI);

        int getFreezeDecayDelay();

        void setFreezeDecayDelay(int freezeDecayDelay);

        boolean getPrevFrozen();

        void setPrevFrozen(boolean prevFrozen);

        UUID getPreAttackTarget();

        void setPreAttackTarget(UUID livingEntity);

        EntityFrozenController getFrozenController();

        void setFrozenController(EntityFrozenController frozenController);

        void addFreezeProgress(LivingEntity entity, float amount);

        void onFreeze(LivingEntity entity);

        void onUnfreeze(LivingEntity entity);

        void tick(LivingEntity entity);
    }

    public static class FrozenCapabilityImp implements IFrozenCapability {
        public boolean frozen;
        public float freezeProgress = 0;
        public float frozenYaw;
        public float frozenPitch;
        public float frozenYawHead;
        public float frozenRenderYawOffset;
        public float frozenSwingProgress;
        public float frozenLimbSwingAmount;
        public boolean prevHasAI = true;
        public UUID prevAttackTarget;

        // After taking freeze progress, this timer needs to reach zero before freeze progress starts to fade
        public int freezeDecayDelay;

        public boolean prevFrozen = false;
        public EntityFrozenController frozenController;

        @Override
        public boolean getFrozen() {
            return frozen;
        }

        @Override
        public float getFreezeProgress() {
            return freezeProgress;
        }

        @Override
        public void setFreezeProgress(float freezeProgress) {
            this.freezeProgress = freezeProgress;
        }

        @Override
        public float getFrozenYaw() {
            return frozenYaw;
        }

        @Override
        public void setFrozenYaw(float frozenYaw) {
            this.frozenYaw = frozenYaw;
        }

        @Override
        public float getFrozenPitch() {
            return frozenPitch;
        }

        @Override
        public void setFrozenPitch(float frozenPitch) {
            this.frozenPitch = frozenPitch;
        }

        @Override
        public float getFrozenYawHead() {
            return frozenYawHead;
        }

        @Override
        public void setFrozenYawHead(float frozenYawHead) {
            this.frozenYawHead = frozenYawHead;
        }

        @Override
        public float getFrozenRenderYawOffset() {
            return frozenRenderYawOffset;
        }

        @Override
        public void setFrozenRenderYawOffset(float frozenRenderYawOffset) {
            this.frozenRenderYawOffset = frozenRenderYawOffset;
        }

        @Override
        public float getFrozenSwingProgress() {
            return frozenSwingProgress;
        }

        @Override
        public void setFrozenSwingProgress(float frozenSwingProgress) {
            this.frozenSwingProgress = frozenSwingProgress;
        }

        @Override
        public float getFrozenLimbSwingAmount() {
            return frozenLimbSwingAmount;
        }

        @Override
        public void setFrozenLimbSwingAmount(float frozenLimbSwingAmount) {
            this.frozenLimbSwingAmount = frozenLimbSwingAmount;
        }

        @Override
        public boolean prevHasAI() {
            return prevHasAI;
        }

        @Override
        public void setPrevHasAI(boolean prevHasAI) {
            this.prevHasAI = prevHasAI;
        }

        @Override
        public int getFreezeDecayDelay() {
            return freezeDecayDelay;
        }

        @Override
        public void setFreezeDecayDelay(int freezeDecayDelay) {
            this.freezeDecayDelay = freezeDecayDelay;
        }

        @Override
        public boolean getPrevFrozen() {
            return prevFrozen;
        }

        @Override
        public void setPrevFrozen(boolean prevFrozen) {
            this.prevFrozen = prevFrozen;
        }

        @Override
        public UUID getPreAttackTarget() {
            return prevAttackTarget;
        }

        @Override
        public void setPreAttackTarget(UUID livingEntity) {
            prevAttackTarget = livingEntity;
        }

        @Override
        public EntityFrozenController getFrozenController() {
            return frozenController;
        }

        @Override
        public void setFrozenController(EntityFrozenController frozenController) {
            this.frozenController = frozenController;
        }

        @Override
        public void addFreezeProgress(LivingEntity entity, float amount) {
            if (!entity.level.isClientSide && !entity.hasEffect(EffectHandler.FROZEN)) {
                freezeProgress += amount;
                freezeDecayDelay = MAX_FREEZE_DECAY_DELAY;
            }
        }

        @Override
        public void onFreeze(LivingEntity entity) {
            if (entity != null) {
                frozen = true;
                frozenController = new EntityFrozenController(EntityHandler.FROZEN_CONTROLLER.get(), entity.level);
                frozenController.absMoveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
                entity.level.addFreshEntity(frozenController);
                frozenController.setYBodyRot(entity.yBodyRot);
                frozenYaw = entity.getYRot();
                frozenPitch = entity.getXRot();
                frozenYawHead = entity.yHeadRot;
                frozenLimbSwingAmount = 0;//entity.limbSwingAmount;
                frozenRenderYawOffset = entity.yBodyRot;
                frozenSwingProgress = entity.attackAnim;
                entity.startRiding(frozenController, true);
                entity.stopUsingItem();

                if (entity instanceof Mob) {
                    Mob mobEntity = (Mob) entity;
                    if (mobEntity.getTarget() != null) setPreAttackTarget(mobEntity.getTarget().getUUID());
                    prevHasAI = !((Mob) entity).isNoAi();
                    mobEntity.setNoAi(true);
                }

                if (entity.level.isClientSide) {
                    int particleCount = (int) (10 + 1 * entity.getBbHeight() * entity.getBbWidth() * entity.getBbWidth());
                    for (int i = 0; i < particleCount; i++) {
                        double snowX = entity.getX() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                        double snowZ = entity.getZ() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                        double snowY = entity.getY() + entity.getBbHeight() * entity.getRandom().nextFloat();
                        Vec3 motion = new Vec3(snowX - entity.getX(), snowY - (entity.getY() + entity.getBbHeight() / 2), snowZ - entity.getZ()).normalize();
                        entity.level.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), snowX, snowY, snowZ, 0.1d * motion.x, 0.1d * motion.y, 0.1d * motion.z);
                    }
                }
                entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH.get(), 1, 1);
            }
        }

        @Override
        public void onUnfreeze(LivingEntity entity) {
            if (entity != null) {
                freezeProgress = 0;
                if (frozen) {
                    entity.removeEffectNoUpdate(EffectHandler.FROZEN);
                    frozen = false;
                    if (frozenController != null) {
                        Vec3 oldPosition = entity.position();
                        entity.stopRiding();
                        entity.teleportTo(oldPosition.x(), oldPosition.y(), oldPosition.z());
                        frozenController.discard() ;
                    }
                    entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH.get(), 1, 0.5f);
                    if (entity.level.isClientSide) {
                        int particleCount = (int) (10 + 1 * entity.getBbHeight() * entity.getBbWidth() * entity.getBbWidth());
                        for (int i = 0; i < particleCount; i++) {
                            double particleX = entity.getX() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                            double particleZ = entity.getZ() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                            double particleY = entity.getY() + entity.getBbHeight() * entity.getRandom().nextFloat() + 0.3f;
                            entity.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()), particleX, particleY, particleZ, 0, 0, 0);
                        }
                    }
                    if (entity instanceof Mob) {
                        if (((Mob) entity).isNoAi() && prevHasAI) {
                            ((Mob) entity).setNoAi(false);
                        }
                        if (getPreAttackTarget() != null) {
                            Player target = entity.level.getPlayerByUUID(getPreAttackTarget());
                            if (target != null) {
                                ((Mob) entity).setTarget(target);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            // Freeze logic
            if (getFreezeProgress() >= 1 && !entity.hasEffect(EffectHandler.FROZEN)) {
                entity.addEffect(new MobEffectInstance(EffectHandler.FROZEN, 50, 0, false, false));
                freezeProgress = 1f;
            } else if (freezeProgress > 0) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 9, Mth.floor(freezeProgress * 5 + 1), false, false));
            }

            if (frozenController == null) {
                Entity riding = entity.getVehicle();
                if (riding instanceof EntityFrozenController) frozenController = (EntityFrozenController) riding;
            }

            if (frozen) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 50, false, false));
                entity.setShiftKeyDown(false);

                if (entity.level.isClientSide && entity.tickCount % 2 == 0) {
                    double cloudX = entity.getX() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                    double cloudZ = entity.getZ() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                    double cloudY = entity.getY() + entity.getBbHeight() * entity.getRandom().nextFloat();
                    entity.level.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f, 0.75f, 1f, 15f, 25, ParticleCloud.EnumCloudBehavior.CONSTANT, 1f), cloudX, cloudY, cloudZ, 0f, -0.01f, 0f);

                    double snowX = entity.getX() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                    double snowZ = entity.getZ() + entity.getBbWidth() * entity.getRandom().nextFloat() - entity.getBbWidth() / 2;
                    double snowY = entity.getY() + entity.getBbHeight() * entity.getRandom().nextFloat();
                    entity.level.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), snowX, snowY, snowZ, 0d, -0.01d, 0d);
                }
            }
            else {
                if (!entity.level.isClientSide && getPrevFrozen()) {
                    onUnfreeze(entity);
                }
            }

            if (freezeDecayDelay <= 0) {
                freezeProgress -= 0.1;
                if (freezeProgress < 0) freezeProgress = 0;
            }
            else {
                freezeDecayDelay--;
            }
            prevFrozen = entity.hasEffect(EffectHandler.FROZEN);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putFloat("freezeProgress", getFreezeProgress());
            compound.putInt("freezeDecayDelay", getFreezeDecayDelay());
            compound.putFloat("frozenLimbSwingAmount", getFrozenLimbSwingAmount());
            compound.putFloat("frozenRenderYawOffset", getFrozenRenderYawOffset());
            compound.putFloat("frozenSwingProgress", getFrozenSwingProgress());
            compound.putFloat("frozenPitch", getFrozenPitch());
            compound.putFloat("frozenYaw", getFrozenYaw());
            compound.putFloat("frozenYawHead", getFrozenYawHead());
            compound.putBoolean("prevHasAI", prevHasAI());
            if (getPreAttackTarget() != null) {
                compound.putUUID("prevAttackTarget", getPreAttackTarget());
            }
            compound.putBoolean("frozen", frozen);
            compound.putBoolean("prevFrozen", prevFrozen);
            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag compound) {
            setFreezeProgress(compound.getFloat("freezeProgress"));
            setFreezeDecayDelay(compound.getInt("freezeDecayDelay"));
            setFrozenLimbSwingAmount(compound.getFloat("frozenLimbSwingAmount"));
            setFrozenRenderYawOffset(compound.getFloat("frozenRenderYawOffset"));
            setFrozenSwingProgress(compound.getFloat("frozenSwingProgress"));
            setFrozenPitch(compound.getFloat("frozenPitch"));
            setFrozenYaw(compound.getFloat("frozenYaw"));
            setFrozenYawHead(compound.getFloat("frozenYawHead"));
            setPrevHasAI(compound.getBoolean("prevHasAI"));
            try {
                setPreAttackTarget(compound.getUUID("prevAttackTarget"));
            }
            catch (NullPointerException ignored) {}
            frozen = compound.getBoolean("frozen");
            prevFrozen = compound.getBoolean("prevFrozen");
        }
    }

    public static class FrozenProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
    {
        private final LazyOptional<FrozenCapability.IFrozenCapability> instance = LazyOptional.of(FrozenCapability.FrozenCapabilityImp::new);

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
            return CapabilityHandler.FROZEN_CAPABILITY.orEmpty(cap, instance.cast());
        }
    }
}
