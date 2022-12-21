package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Optional;

public abstract class EntityGeomancyBase extends EntityMagicEffect implements IAnimatable, IAnimationTickable {
    private static final byte EXPLOSION_PARTICLES_ID = 69;

    protected static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(EntityGeomancyBase.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> TIER = SynchedEntityData.defineId(EntityGeomancyBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DEATH_TIME = SynchedEntityData.defineId(EntityGeomancyBase.class, EntityDataSerializers.INT);

    public enum GeomancyTier {
        NONE,
        SMALL,
        MEDIUM,
        LARGE,
        HUGE
    }

    private AnimationFactory factory = new AnimationFactory(this);

    private boolean doRemoveTimer = true;

    public EntityGeomancyBase(EntityType<? extends EntityMagicEffect> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityGeomancyBase(EntityType<? extends EntityMagicEffect> type, Level worldIn, LivingEntity caster, BlockState blockState, BlockPos pos) {
        super(type, worldIn, caster);
        if (!worldIn.isClientSide && blockState != null) {
            Block block = blockState.getBlock();
            BlockState newBlock = blockState;
            Material mat = blockState.getMaterial();
            if (blockState.getBlock() == Blocks.GRASS_BLOCK || blockState.getBlock() == Blocks.MYCELIUM || mat == Material.DIRT) newBlock = Blocks.DIRT.defaultBlockState();
            else if (mat == Material.STONE) {
                if (block.getRegistryName() != null && block.getRegistryName().getPath().contains("ore")) newBlock = Blocks.STONE.defaultBlockState();
                if (blockState.getBlock() == Blocks.NETHER_QUARTZ_ORE) newBlock = Blocks.NETHERRACK.defaultBlockState();
                if (blockState.getBlock() == Blocks.FURNACE
                        || blockState.getBlock() == Blocks.DISPENSER
                        || blockState.getBlock() == Blocks.DROPPER
                ) newBlock = Blocks.COBBLESTONE.defaultBlockState();
            }
            else if (mat == Material.CLAY) {
                if (blockState.getBlock() == Blocks.CLAY) newBlock = Blocks.TERRACOTTA.defaultBlockState();
            }
            else if (mat == Material.SAND) {
                if (blockState.getBlock() == Blocks.SAND) newBlock = Blocks.SANDSTONE.defaultBlockState();
                else if (blockState.getBlock() == Blocks.RED_SAND) newBlock = Blocks.RED_SANDSTONE.defaultBlockState();
                else if (blockState.getBlock() == Blocks.GRAVEL) newBlock = Blocks.COBBLESTONE.defaultBlockState();
                else if (blockState.getBlock() == Blocks.SOUL_SAND) newBlock = Blocks.NETHERRACK.defaultBlockState();
            }

            if (!newBlock.isRedstoneConductor(worldIn, pos)) {
                newBlock = Blocks.STONE.defaultBlockState();
            }
            setBlock(newBlock);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (doRemoveTimer()) {
            int newDeathTime = getDeathTime() - 1;
            setDeathTime(newDeathTime);
            if (newDeathTime < 0) this.explode();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(BLOCK_STATE, Optional.of(Blocks.DIRT.defaultBlockState()));
        getEntityData().define(DEATH_TIME, 1200);
        getEntityData().define(TIER, 0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        super.playSound(soundIn, volume, pitch + random.nextFloat() * 0.25f - 0.125f);
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    protected void explode() {
        this.level.broadcastEntityEvent(this, EXPLOSION_PARTICLES_ID);
        GeomancyTier tier = getTier();
        if (tier == GeomancyTier.SMALL) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.9f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK.get(), 1.5f, 1f);
        }
        else if (tier == GeomancyTier.MEDIUM) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.7f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_3.get(), 1.5f, 1.5f);
        }
        else if (tier == GeomancyTier.LARGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_1.get(), 1.5f, 0.9f);
            EntityCameraShake.cameraShake(level, position(), 15, 0.05f, 0, 20);

            for (int i = 0; i < 5; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 2, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(new Vec3(0, getBbHeight() / 4, 0));
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), level, 70, getBlock());
                fallingBlock.setPos(getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z);
                fallingBlock.setDeltaMovement((float) particlePos.x * 0.3f, 0.2f + random.nextFloat() * 0.6f, (float) particlePos.z * 0.3f);
                level.addFreshEntity(fallingBlock);
            }
        }
        else if (tier == GeomancyTier.HUGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.5f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_LARGE_1.get(), 1.5f, 0.5f);
            EntityCameraShake.cameraShake(level, position(), 20, 0.05f, 0, 20);

            for (int i = 0; i < 7; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 2.5f, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(new Vec3(0, getBbHeight() / 4, 0));
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), level, 70, getBlock());
                fallingBlock.setPos(getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z);
                fallingBlock.setDeltaMovement((float) particlePos.x * 0.3f, 0.2f + random.nextFloat() * 0.6f, (float) particlePos.z * 0.3f);
                level.addFreshEntity(fallingBlock);
            }
        }
        discard();
    }

    private void spawnExplosionParticles() {
        for (int i = 0; i < 40 * getBbWidth(); i++) {
            Vec3 particlePos = new Vec3(random.nextFloat() * 0.7 * getBbWidth(), 0, 0);
            particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
            particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, getBlock()), getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EXPLOSION_PARTICLES_ID) {
            spawnExplosionParticles();
        }
        else super.handleEntityEvent(id);
    }

    public BlockState getBlock() {
        Optional<BlockState> bsOp = getEntityData().get(BLOCK_STATE);
        return bsOp.orElse(null);
    }

    public void setBlock(BlockState block) {
        getEntityData().set(BLOCK_STATE, Optional.of(block));
    }

    public GeomancyTier getTier() {
        return GeomancyTier.values()[entityData.get(TIER)];
    }

    public void setTier(GeomancyTier size) {
        entityData.set(TIER, size.ordinal());
    }

    public int getDeathTime() {
        return entityData.get(DEATH_TIME);
    }

    public void setDeathTime(int deathTime) {
        entityData.set(DEATH_TIME, deathTime);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        BlockState blockState = getBlock();
        if (blockState != null) compound.put("block", NbtUtils.writeBlockState(blockState));
        if (doRemoveTimer()) compound.putInt("deathTime", getDeathTime());
        compound.putInt("tier", getTier().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Tag blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NbtUtils.readBlockState((CompoundTag) blockStateCompound);
            setBlock(blockState);
        }
        if (compound.contains("deathTime")) {
            doRemoveTimer = true;
            setDeathTime(compound.getInt("deathTime"));
        }
        else {
            doRemoveTimer = false;
        }
        setTier(GeomancyTier.values()[compound.getInt("tier")]);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    public boolean doRemoveTimer() {
        return doRemoveTimer;
    }

    public void setDoRemoveTimer(boolean doRemoveTimer) {
        this.doRemoveTimer = doRemoveTimer;
    }
}
