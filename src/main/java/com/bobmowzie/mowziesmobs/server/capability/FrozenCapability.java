package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FrozenCapability {
    public static int MAX_FREEZE_DECAY_DELAY = 10;

    public interface IFrozenCapability {
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

        boolean isPrevFrozen();

        void setPrevFrozen(boolean prevFrozen);

        EntityFrozenController getFrozenController();

        void setFrozenController(EntityFrozenController frozenController);

        void addFreezeProgress(LivingEntity entity, float amount);

        void onFreeze(LivingEntity entity);

        void onUnfreeze(LivingEntity entity);

        void tick(LivingEntity entity);

        INBT writeNBT();

        void readNBT(INBT nbt);
    }

    public static class FrozenCapabilityImp implements IFrozenCapability {
        public float freezeProgress = 0;
        public float frozenYaw;
        public float frozenPitch;
        public float frozenYawHead;
        public float frozenRenderYawOffset;
        public float frozenSwingProgress;
        public float frozenLimbSwingAmount;
        public boolean prevHasAI;

        // After taking freeze progress, this timer needs to reach zero before freeze progress starts to fade
        public int freezeDecayDelay;

        public boolean prevFrozen = false;
        public EntityFrozenController frozenController;

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
        public boolean isPrevFrozen() {
            return prevFrozen;
        }

        @Override
        public void setPrevFrozen(boolean prevFrozen) {
            this.prevFrozen = prevFrozen;
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
            if (!entity.world.isRemote && !entity.isPotionActive(PotionHandler.FROZEN)) {
                freezeProgress += amount;
                freezeDecayDelay = MAX_FREEZE_DECAY_DELAY;
//                MowziesMobs.NETWORK_WRAPPER.sendToDimension(new MessageAddFreezeProgress(entity, amount), entity.dimension); TODO
            }
        }

        @Override
        public void onFreeze(LivingEntity entity) {
            if (entity != null) {
                frozenController = new EntityFrozenController(EntityHandler.FROZEN_CONTROLLER, entity.world);
                frozenController.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                entity.world.addEntity(frozenController);
                frozenController.setRenderYawOffset(entity.renderYawOffset);
                frozenYaw = entity.rotationYaw;
                frozenPitch = entity.rotationPitch;
                frozenYawHead = entity.rotationYawHead;
                frozenLimbSwingAmount = entity.limbSwingAmount;
                frozenRenderYawOffset = entity.renderYawOffset;
                frozenSwingProgress = entity.swingProgress;
                entity.startRiding(frozenController, true);

                if (entity instanceof MobEntity) prevHasAI = !((MobEntity)entity).isAIDisabled();
                if (entity instanceof MobEntity) ((MobEntity)entity).setNoAI(true);

                if (entity.world.isRemote) {
                    int particleCount = (int) (10 + 1 * entity.getHeight() * entity.getWidth() * entity.getWidth());
                    for (int i = 0; i < particleCount; i++) {
                        double snowX = entity.posX + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                        double snowZ = entity.posZ + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                        double snowY = entity.posY + entity.getHeight() * entity.getRNG().nextFloat();
                        Vec3d motion = new Vec3d(snowX - entity.posX, snowY - (entity.posY + entity.getHeight() / 2), snowZ - entity.posZ).normalize();
                        MMParticle.SNOWFLAKE.spawn(entity.world, snowX, snowY, snowZ, ParticleFactory.ParticleArgs.get().withData(0.1d * motion.x, 0.1d * motion.y, 0.1d * motion.z));
                    }
                }
                entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH.get(), 1, 1);
            }
        }

        @Override
        public void onUnfreeze(LivingEntity entity) {
            if (entity != null && frozenController != null) {
                entity.dismountEntity(frozenController);
                entity.setPosition(frozenController.posX, frozenController.posY, frozenController.posZ);
                frozenController.remove();
                entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH.get(), 1, 0.5f);

                if (entity.world.isRemote) {
                    int particleCount = (int) (10 + 1 * entity.getHeight() * entity.getWidth() * entity.getWidth());
                    for (int i = 0; i < particleCount; i++) {
                        double particleX = entity.posX + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                        double particleZ = entity.posZ + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                        double particleY = entity.posY + entity.getHeight() * entity.getRNG().nextFloat() + 0.3f;
//                        entity.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, particleX, particleY, particleZ, 0, 0, 0, Block.getStateId(Blocks.ICE.getDefaultState())); TODO
                    }
                }
                if (entity instanceof MobEntity && ((MobEntity) entity).isAIDisabled() && prevHasAI) {
                    ((MobEntity) entity).setNoAI(false);
                }
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            // Freeze logic
            if (getFreezeProgress() >= 1) {
                entity.addPotionEffect(new EffectInstance(PotionHandler.FROZEN, 50, 0, false, false));
                freezeProgress = 1f;
            } else if (freezeProgress > 0) {
                entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 9, MathHelper.floor(freezeProgress * 5 + 1), false, false));
            }

            if (entity.isPotionActive(PotionHandler.FROZEN) && !prevFrozen) {
                onFreeze(entity);
            }

            if (!entity.world.isRemote) {
                Item headItemStack = entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
                if (headItemStack instanceof ItemBarakoaMask) {
                    ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
                    entity.addPotionEffect(new EffectInstance(mask.getPotion(), 45, 0, true, false));
                }
            }

            if (entity.isPotionActive(PotionHandler.FROZEN)) {
                if (entity.getActivePotionEffect(PotionHandler.FROZEN).getDuration() <= 0) entity.removeActivePotionEffect(PotionHandler.FROZEN);
                entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 50, false, false));
                entity.setSneaking(false);

                if (entity.world.isRemote && entity.ticksExisted % 2 == 0) {
                    double cloudX = entity.posX + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double cloudZ = entity.posZ + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double cloudY = entity.posY + entity.getHeight() * entity.getRNG().nextFloat();
                    MMParticle.CLOUD.spawn(entity.world, cloudX, cloudY, cloudZ, ParticleFactory.ParticleArgs.get().withData(0d, -0.01d, 0d, 0.75d, 0.75d, 1d, 1, 15d, 25, ParticleCloud.EnumCloudBehavior.CONSTANT));

                    double snowX = entity.posX + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double snowZ = entity.posZ + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double snowY = entity.posY + entity.getHeight() * entity.getRNG().nextFloat();
                    MMParticle.SNOWFLAKE.spawn(entity.world, snowX, snowY, snowZ, ParticleFactory.ParticleArgs.get().withData(0d, -0.01d, 0d));
                }
            }
            else {
                if (frozenController != null && frozenController.isAlive()) {
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
            prevFrozen = entity.isPotionActive(PotionHandler.FROZEN);
        }

        @Override
        public INBT writeNBT() {
            CompoundNBT compound = new CompoundNBT();
            compound.putFloat("freezeProgress", getFreezeProgress());
            compound.putInt("freezeDecayDelay", getFreezeDecayDelay());
            compound.putFloat("frozenLimbSwingAmount", getFrozenLimbSwingAmount());
            compound.putFloat("frozenRenderYawOffset", getFrozenRenderYawOffset());
            compound.putFloat("frozenSwingProgress", getFrozenSwingProgress());
            compound.putFloat("frozenPitch", getFrozenPitch());
            compound.putFloat("frozenYaw", getFrozenYaw());
            compound.putFloat("frozenYawHead", getFrozenYawHead());
            compound.putBoolean("prevHasAI", prevHasAI());
            return compound;
        }

        @Override
        public void readNBT(INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
            setFreezeProgress(compound.getFloat("freezeProgress"));
            setFreezeDecayDelay(compound.getInt("freezeDecayDelay"));
            setFrozenLimbSwingAmount(compound.getFloat("frozenLimbSwingAmount"));
            setFrozenRenderYawOffset(compound.getFloat("frozenRenderYawOffset"));
            setFrozenSwingProgress(compound.getFloat("frozenSwingProgress"));
            setFrozenPitch(compound.getFloat("frozenPitch"));
            setFrozenYaw(compound.getFloat("frozenYaw"));
            setFrozenYawHead(compound.getFloat("frozenYawHead"));
            setPrevHasAI(compound.getBoolean("prevHasAI"));
        }
    }

    public static class FrozenStorage implements Capability.IStorage<IFrozenCapability> {
        @Override
        public INBT writeNBT(Capability<IFrozenCapability> capability, IFrozenCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<IFrozenCapability> capability, IFrozenCapability instance, Direction side, INBT nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class FrozenProvider implements ICapabilitySerializable<INBT>
    {
        @CapabilityInject(IFrozenCapability.class)
        public static final Capability<IFrozenCapability> FROZEN_CAPABILITY = null;

        private LazyOptional<IFrozenCapability> instance = LazyOptional.of(FROZEN_CAPABILITY::getDefaultInstance);

        @Override
        public INBT serializeNBT() {
            return FROZEN_CAPABILITY.getStorage().writeNBT(FROZEN_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            FROZEN_CAPABILITY.getStorage().readNBT(FROZEN_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null, nbt);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == FROZEN_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
