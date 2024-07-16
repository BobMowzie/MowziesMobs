package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.block.ICopiedBlockProperties;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class EntityGeomancyBase extends EntityMagicEffect implements GeoEntity {
    private static final byte EXPLOSION_PARTICLES_ID = 69;

    protected static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(EntityGeomancyBase.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> TIER = SynchedEntityData.defineId(EntityGeomancyBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DEATH_TIME = SynchedEntityData.defineId(EntityGeomancyBase.class, EntityDataSerializers.INT);

    public enum GeomancyTier {
        NONE,
        SMALL,
        MEDIUM,
        LARGE,
        HUGE
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean doRemoveTimer = true;

    public EntityGeomancyBase(EntityType<? extends EntityMagicEffect> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityGeomancyBase(EntityType<? extends EntityMagicEffect> type, Level worldIn, LivingEntity caster, BlockState blockState, BlockPos pos) {
        super(type, worldIn, caster);
        if (!worldIn.isClientSide && blockState != null && EffectGeomancy.isBlockUseable(blockState)) {
            BlockState newBlock = changeBlock(blockState);
            setBlock(newBlock);
        }
    }

    // Change the specified block to its geomancy version. I.E. Grass blocks turn to dirt, stairs and slabs turn to base versions.
    public BlockState changeBlock(BlockState blockState) {
        if (!blockState.is(TagHandler.GEOMANCY_USEABLE)) {
            ICopiedBlockProperties properties = (ICopiedBlockProperties) blockState.getBlock().properties;
            Block baseBlock = properties.getBaseBlock();
            if (baseBlock != null) {
                blockState = baseBlock.defaultBlockState();
            }
        }

        if (
                blockState.getBlock() == Blocks.GRASS_BLOCK ||
                blockState.getBlock() == Blocks.MYCELIUM ||
                blockState.getBlock() == Blocks.PODZOL ||
                blockState.getBlock() == Blocks.DIRT_PATH
        ) {
            blockState = Blocks.DIRT.defaultBlockState();
        }
        else if (blockState.is(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)) blockState = Blocks.DEEPSLATE.defaultBlockState();
        else if (blockState.is(BlockTags.NYLIUM)) blockState = Blocks.NETHERRACK.defaultBlockState();
        else if (blockState.is(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)) blockState = Blocks.NETHERRACK.defaultBlockState();
        else if (blockState.is(Tags.Blocks.ORES_IN_GROUND_STONE)) blockState = Blocks.STONE.defaultBlockState();
        else if (blockState.is(Tags.Blocks.SAND_RED)) blockState = Blocks.RED_SANDSTONE.defaultBlockState();
        else if (blockState.is(Tags.Blocks.SAND_COLORLESS)) blockState = Blocks.SANDSTONE.defaultBlockState();
        else if (blockState.getBlock() == Blocks.SOUL_SAND) blockState = Blocks.SOUL_SOIL.defaultBlockState();

        return blockState;
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
        getEntityData().define(BLOCK_STATE, Blocks.DIRT.defaultBlockState());
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
        this.level().broadcastEntityEvent(this, EXPLOSION_PARTICLES_ID);
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
            EntityCameraShake.cameraShake(level(), position(), 15, 0.05f, 0, 20);

            for (int i = 0; i < 5; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 2, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(new Vec3(0, getBbHeight() / 4, 0));
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), level(), 70, getBlock());
                fallingBlock.setPos(getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z);
                fallingBlock.setDeltaMovement((float) particlePos.x * 0.3f, 0.2f + random.nextFloat() * 0.6f, (float) particlePos.z * 0.3f);
                level().addFreshEntity(fallingBlock);
            }
        }
        else if (tier == GeomancyTier.HUGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.5f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_LARGE_1.get(), 1.5f, 0.5f);
            EntityCameraShake.cameraShake(level(), position(), 20, 0.05f, 0, 20);

            for (int i = 0; i < 7 * fallingBlockCountMultiplier(); i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 2.5f, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(new Vec3(0, getBbHeight() / 4, 0));
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), level(), 70, getBlock());
                fallingBlock.setPos(getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z);
                fallingBlock.setDeltaMovement((float) particlePos.x * 0.3f, 0.2f + random.nextFloat() * 0.6f, (float) particlePos.z * 0.3f);
                level().addFreshEntity(fallingBlock);
            }
        }
        discard();
    }

    protected float fallingBlockCountMultiplier() {
        return 1;
    }

    private void spawnExplosionParticles() {
        for (int i = 0; i < 40 * getBbWidth(); i++) {
            Vec3 particlePos = new Vec3(random.nextFloat() * 0.7 * getBbWidth(), 0, 0);
            particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
            particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
            particlePos.add(0, getBbHeight() / 2.0, 0);
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            boolean overrideLimiter = camera.getPosition().distanceToSqr(getX(), getY(), getZ()) < 64 * 64;
            level().addAlwaysVisibleParticle(new BlockParticleOption(ParticleTypes.BLOCK, getBlock()), overrideLimiter, getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
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
        return getEntityData().get(BLOCK_STATE);
    }

    public void setBlock(BlockState block) {
        getEntityData().set(BLOCK_STATE, block);
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
            BlockState blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), (CompoundTag) blockStateCompound);
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public boolean doRemoveTimer() {
        return doRemoveTimer;
    }

    public void setDoRemoveTimer(boolean doRemoveTimer) {
        this.doRemoveTimer = doRemoveTimer;
    }
}
