package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.Iterables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class EntityBoulder extends Entity {
    private static final byte EXPLOSION_PARTICLES_ID = 69;

    private LivingEntity caster;
    private boolean travelling;
    public BlockState storedBlock;
    private static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(EntityBoulder.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(EntityBoulder.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> DEATH_TIME = SynchedEntityData.defineId(EntityBoulder.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(EntityBoulder.class, EntityDataSerializers.INT);
    public float animationOffset = 0;
    private final List<Entity> ridingEntities = new ArrayList<Entity>();
    public BoulderSizeEnum boulderSize = BoulderSizeEnum.SMALL;

    private float speed = 1.5f;
    private int damage = 8;
    private int finishedRisingTick = 4;

    public enum BoulderSizeEnum {
        SMALL,
        MEDIUM,
        LARGE,
        HUGE
    }

    public EntityBoulder(EntityType<? extends EntityBoulder> type, Level world) {
        super(type, world);
        travelling = false;
        damage = 8;
        finishedRisingTick = 4;
        animationOffset = random.nextFloat() * 8;
        this.setOrigin(this.blockPosition());
    }

    public EntityBoulder(EntityType<? extends EntityBoulder> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos) {
        this(type, world);
        this.caster = caster;
        if (type == EntityHandler.BOULDER_SMALL.get()) setBoulderSize(BoulderSizeEnum.SMALL);
        else if (type == EntityHandler.BOULDER_MEDIUM.get()) setBoulderSize(BoulderSizeEnum.MEDIUM);
        else if (type == EntityHandler.BOULDER_LARGE.get()) setBoulderSize(BoulderSizeEnum.LARGE);
        else if (type == EntityHandler.BOULDER_HUGE.get()) setBoulderSize(BoulderSizeEnum.HUGE);
        setSizeParams();
        if (!world.isClientSide && blockState != null) {
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

            if (!newBlock.isRedstoneConductor(world, pos)) {
                newBlock = Blocks.STONE.defaultBlockState();
            }
            setBlock(newBlock);
        }
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.BLOCK;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean checkCanSpawn() {
        if (!level.getEntitiesOfClass(EntityBoulder.class, getBoundingBox().deflate(0.01)).isEmpty()) return false;
        return level.noCollision(this, getBoundingBox().deflate(0.01));
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(BLOCK_STATE, Optional.of(Blocks.DIRT.defaultBlockState()));
        getEntityData().define(ORIGIN, new BlockPos(0, 0, 0));
        getEntityData().define(DEATH_TIME, 1200);
        getEntityData().define(SIZE, 0);
    }

    public void setSizeParams() {
        BoulderSizeEnum size = getBoulderSize();
        if (size == BoulderSizeEnum.MEDIUM) {
            finishedRisingTick = 8;
            damage = 12;
            speed = 1.2f;
        }
        else if (size == BoulderSizeEnum.LARGE) {
            finishedRisingTick = 12;
            damage = 16;
            speed = 1f;
        }
        else if (size == BoulderSizeEnum.HUGE) {
            finishedRisingTick = 90;
            damage = 20;
            speed = 0.65f;
        }

        if (caster instanceof Player) damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.geomancyAttackMultiplier.get();

    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void tick() {
        if (firstTick) {
            setSizeParams();
            boulderSize = getBoulderSize();
        }
        if (storedBlock == null) storedBlock = getBlock();

        if (!travelling) {
            setBoundingBox(getType().getAABB(getX(), getY(), getZ()).expandTowards(0, -0.5, 0));
        }
        super.tick();
        move(MoverType.SELF, getDeltaMovement());
        if (ridingEntities != null) ridingEntities.clear();
        List<Entity> onTopOfEntities = level.getEntities(this, getBoundingBox().contract(0, getBbHeight() - 1, 0).move(new Vec3(0, getBbHeight() - 0.5, 0)).inflate(0.6,0.5,0.6));
        for (Entity entity : onTopOfEntities) {
            if (entity != null && entity.isPickable() && !(entity instanceof EntityBoulder) && entity.getY() >= this.getY() + 0.2) ridingEntities.add(entity);
        }
        if (travelling){
            for (Entity entity : ridingEntities) {
                entity.move(MoverType.SHULKER_BOX, getDeltaMovement());
            }
        }
        if (boulderSize == BoulderSizeEnum.HUGE && tickCount < finishedRisingTick) {
            float f = this.getBbWidth() / 2.0F;
            AABB aabb = new AABB(getX() - (double)f, getY() - 0.5, getZ() - (double)f, getX() + (double)f, getY() + Math.min(tickCount/(float)finishedRisingTick * 3.5f, 3.5f), getZ() + (double)f);
            setBoundingBox(aabb);
        }

        if (tickCount < finishedRisingTick) {
            List<Entity> popUpEntities = level.getEntities(this, getBoundingBox());
            for (Entity entity:popUpEntities) {
                if (entity.isPickable() && !(entity instanceof EntityBoulder)) {
                    if (boulderSize != BoulderSizeEnum.HUGE) entity.move(MoverType.SHULKER_BOX, new Vec3(0, 2 * (Math.pow(2, -tickCount * (0.6 - 0.1 * boulderSize.ordinal()))), 0));
                    else entity.move(MoverType.SHULKER_BOX, new Vec3(0, 0.6f, 0));
                }
            }
        }
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(1.7);
        if (travelling && !entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (level.isClientSide) continue;
                if (entity == caster) continue;
                if (ridingEntities.contains(entity)) continue;
                if (caster != null) entity.hurt(DamageSource.indirectMobAttack(this, caster), damage);
                else entity.hurt(DamageSource.FALLING_BLOCK, damage);
                if (isAlive() && boulderSize != BoulderSizeEnum.HUGE) this.explode();
            }
        }
        List<EntityBoulder> bouldersHit = level.getEntitiesOfClass(EntityBoulder.class, getBoundingBox().inflate(0.2, 0.2, 0.2).move(getDeltaMovement().normalize().scale(0.5)));
        if (travelling && !bouldersHit.isEmpty()) {
            for (EntityBoulder entity : bouldersHit) {
                if (!entity.travelling) {
                    entity.skipAttackInteraction(this);
                    explode();
                }
            }
        }

        if (travelling) {
            if (
                    !level.getEntities(this, getBoundingBox().inflate(0.1), (e)->!ridingEntities.contains(e)).isEmpty() ||
                    Iterables.size(level.getBlockCollisions(this, getBoundingBox().inflate(0.1))) > 0
            ) {
                this.explode();
            }
        }

        if (tickCount == 1) {
            for (int i = 0; i < 20 * getBbWidth(); i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 1.3 * getBbWidth(), 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, storedBlock), getX() + particlePos.x, getY() - 1, getZ() + particlePos.z, particlePos.x, 2, particlePos.z);
            }
            if (boulderSize == BoulderSizeEnum.SMALL) {
                playSound(MMSounds.EFFECT_GEOMANCY_SMALL_CRASH.get(), 1.5f, 1.3f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 1f);
            } else if (boulderSize == BoulderSizeEnum.MEDIUM) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_2.get(), 1.5f, 1.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.8f);
            } else if (boulderSize == BoulderSizeEnum.LARGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 0.9f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1.5f);
                EntityCameraShake.cameraShake(level, position(), 10, 0.05f, 0, 20);
            } else if (boulderSize == BoulderSizeEnum.HUGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 2f, 0.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_1.get(), 2, 0.8f);
                EntityCameraShake.cameraShake(level, position(), 15, 0.05f, 50, 30);
            }
            if (level.isClientSide) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING2.get(), getX(), getY() - 0.9f, getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * getBbWidth()), true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * getBbWidth()) * 10f), false)
                });
            }
        }
        if (tickCount == 30 && boulderSize == BoulderSizeEnum.HUGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_2.get(), 2, 0.7f);
        }

        int dripTick = tickCount - 2;
        if (boulderSize == BoulderSizeEnum.HUGE) dripTick -= 20;
        int dripNumber = (int)(getBbWidth() * 6 * Math.pow(1.03 + 0.04 * 1/getBbWidth(), -(dripTick)));
        if (dripNumber >= 1 && dripTick > 0) {
            dripNumber *= random.nextFloat();
            for (int i = 0; i < dripNumber; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 0.6 * getBbWidth(), 0, 0);
                particlePos = particlePos.yRot((float)(random.nextFloat() * 2 * Math.PI));
                float offsetY;
                if (boulderSize == BoulderSizeEnum.HUGE && tickCount < finishedRisingTick) offsetY = random.nextFloat() * (getBbHeight()-1) - getBbHeight() * (finishedRisingTick - tickCount)/finishedRisingTick;
                else offsetY = random.nextFloat() * (getBbHeight()-1);
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, storedBlock), getX() + particlePos.x, getY() + offsetY, getZ() + particlePos.z, 0, -1, 0);
            }
        }
        int newDeathTime = getDeathTime() - 1;
        setDeathTime(newDeathTime);
        if (newDeathTime < 0) this.explode();
    }

    private void explode() {
        this.level.broadcastEntityEvent(this, EXPLOSION_PARTICLES_ID);
        if (boulderSize == BoulderSizeEnum.SMALL) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.9f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK.get(), 1.5f, 1f);
        }
        else if (boulderSize == BoulderSizeEnum.MEDIUM) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.7f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_3.get(), 1.5f, 1.5f);
        }
        else if (boulderSize == BoulderSizeEnum.LARGE) {
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
        else if (boulderSize == BoulderSizeEnum.HUGE) {
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

    public BlockState getBlock() {
        Optional<BlockState> bsOp = getEntityData().get(BLOCK_STATE);
        return bsOp.orElse(null);
    }

    public void setBlock(BlockState block) {
        getEntityData().set(BLOCK_STATE, Optional.of(block));
        this.storedBlock = block;
    }

    public void setOrigin(BlockPos pos) {
        this.entityData.set(ORIGIN, pos);
    }

    public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    public int getDeathTime() {
        return entityData.get(DEATH_TIME);
    }

    public void setDeathTime(int deathTime) {
        entityData.set(DEATH_TIME, deathTime);
    }

    public BoulderSizeEnum getBoulderSize() {
        return BoulderSizeEnum.values()[entityData.get(SIZE)];
    }

    public void setBoulderSize(BoulderSizeEnum size) {
        entityData.set(SIZE, size.ordinal());
        boulderSize = size;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        BlockState blockState = getBlock();
        if (blockState != null) compound.put("block", NbtUtils.writeBlockState(blockState));
        compound.putInt("deathTime", getDeathTime());
        compound.putInt("size", getBoulderSize().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        Tag blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NbtUtils.readBlockState((CompoundTag) blockStateCompound);
            setBlock(blockState);
        }
        setDeathTime(compound.getInt("deathTime"));
        setBoulderSize(BoulderSizeEnum.values()[compound.getInt("size")]);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean skipAttackInteraction(Entity entityIn) {
        if (tickCount > finishedRisingTick - 1 && !travelling) {
            if (entityIn instanceof Player
                    && EffectGeomancy.canUse((Player)entityIn)) {
                Player player = (Player) entityIn;
                if (ridingEntities.contains(player)) {
                    Vec3 lateralLookVec = Vec3.directionFromRotation(0, player.getYRot()).normalize();
                    setDeltaMovement(speed * 0.5 * lateralLookVec.x, getDeltaMovement().y, speed * 0.5 * lateralLookVec.z);
                } else {
                    setDeltaMovement(player.getLookAngle().scale(speed * 0.5));
                }
                AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.HIT_BOULDER_ABILITY);
            }
            else if (entityIn instanceof EntityBoulder && ((EntityBoulder) entityIn).travelling) {
                EntityBoulder boulder = (EntityBoulder)entityIn;
                Vec3 thisPos = position();
                Vec3 boulderPos = boulder.position();
                Vec3 velVec = thisPos.subtract(boulderPos).normalize();
                setDeltaMovement(velVec.scale(speed * 0.5));
            }
            else {
                return super.skipAttackInteraction(entityIn);
            }
            if (!travelling) setDeathTime(60);
            travelling = true;
            setBoundingBox(getType().getAABB(getX(), getY(), getZ()));

            if (boulderSize == BoulderSizeEnum.SMALL) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 1.3f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.9f);
            }
            else if (boulderSize == BoulderSizeEnum.MEDIUM) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 0.9f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.5f);
            }
            else if (boulderSize == BoulderSizeEnum.LARGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 0.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1.3f);
                EntityCameraShake.cameraShake(level, position(), 10, 0.05f, 0, 20);
            }
            else if (boulderSize == BoulderSizeEnum.HUGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 1f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.9f);
                EntityCameraShake.cameraShake(level, position(), 15, 0.05f, 0, 20);
            }

            if (level.isClientSide) {
                Vec3 ringOffset = getDeltaMovement().scale(-1).normalize();
                ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING2.get(), (float) getX() + (float) ringOffset.x, (float) getY() + 0.5f + (float) ringOffset.y, (float) getZ() + (float) ringOffset.z, 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * getBbWidth()), true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * getBbWidth()) * 8f), false)
                });
            }
        }
        return super.skipAttackInteraction(entityIn);
    }

    private void spawnExplosionParticles() {
        for (int i = 0; i < 40 * getBbWidth(); i++) {
            Vec3 particlePos = new Vec3(random.nextFloat() * 0.7 * getBbWidth(), 0, 0);
            particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
            particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, storedBlock), getX() + particlePos.x, getY() + 0.5 + particlePos.y, getZ() + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EXPLOSION_PARTICLES_ID) {
            spawnExplosionParticles();
        }
        else super.handleEntityEvent(id);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public List<LivingEntity> getEntityLivingBaseNearby(double radius) {
        return getEntitiesNearby(LivingEntity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return level.getEntitiesOfClass(entityClass, getBoundingBox().inflate(r, r, r), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f);
    }


    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getZ() - first.getZ(), second.getX() - first.getX()) * (180 / Math.PI) + 90;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        super.playSound(soundIn, volume, pitch + random.nextFloat() * 0.25f - 0.125f);
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }
}
