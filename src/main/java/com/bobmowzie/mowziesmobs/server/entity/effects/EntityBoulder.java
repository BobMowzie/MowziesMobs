package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particles.ParticleFallingBlock;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Josh on 4/14/2017.
 */
public class EntityBoulder extends Entity {
    int blockId;
    private LivingEntity caster;
    private boolean travelling;
    public BlockState storedBlock;
    private static final DataParameter<Optional<BlockState>> BLOCK_STATE = EntityDataManager.createKey(EntityBoulder.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> SHOULD_EXPLODE = EntityDataManager.createKey(EntityBoulder.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityBoulder.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> DEATH_TIME = EntityDataManager.createKey(EntityBoulder.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(EntityBoulder.class, DataSerializers.VARINT);
    public float animationOffset = 0;
    private List<Entity> ridingEntities = new ArrayList<Entity>();
    public int boulderSize = 0;

    private float speed = 1.5f;
    private int damage = 8;
    private int finishedRisingTick = 4;

    public EntityBoulder(EntityType<? extends EntityBoulder> type, World world) {
        super(type, world);
        travelling = false;
        damage = 8;
        finishedRisingTick = 4;
        animationOffset = rand.nextFloat() * 8;
        this.setOrigin(new BlockPos(this));
    }

    public EntityBoulder(EntityType<? extends EntityBoulder> type, World world, LivingEntity caster, int size, BlockState block) {
        this(type, world);
        this.caster = caster;
        setBoulderSize(size);
        setSizeParams();
        if (!world.isRemote && block != null) {
            BlockState newBlock = block;
            Material mat = block.getMaterial();
            if (block.getBlock() == Blocks.GRASS_BLOCK || block.getBlock() == Blocks.MYCELIUM || mat == Material.EARTH) newBlock = Blocks.DIRT.getDefaultState();
            else if (mat == Material.ROCK) {
                if (block.getBlock().getTranslationKey().contains("ore")) newBlock = Blocks.STONE.getDefaultState();
                if (block.getBlock() == Blocks.NETHER_QUARTZ_ORE) newBlock = Blocks.NETHERRACK.getDefaultState();
                if (block.getBlock() == Blocks.FURNACE
                        || block.getBlock() == Blocks.DISPENSER
                        || block.getBlock() == Blocks.DROPPER
                        ) newBlock = Blocks.COBBLESTONE.getDefaultState();
            }
            else if (mat == Material.CLAY) {
                if (block.getBlock() == Blocks.CLAY) newBlock = Blocks.TERRACOTTA.getDefaultState();
            }
            else if (mat == Material.SAND) {
                if (block.getBlock() == Blocks.SAND) newBlock = Blocks.SANDSTONE.getDefaultState();
                else if (block.getBlock() == Blocks.RED_SAND) newBlock = Blocks.RED_SANDSTONE.getDefaultState();
                else if (block.getBlock() == Blocks.GRAVEL) newBlock = Blocks.COBBLESTONE.getDefaultState();
                else if (block.getBlock() == Blocks.SOUL_SAND) newBlock = Blocks.NETHERRACK.getDefaultState();
            }
            setBlock(newBlock);
        }
    }

    public boolean checkCanSpawn() {
        if (!world.getEntitiesWithinAABB(EntityBoulder.class, getBoundingBox()).isEmpty()) return false;
        if (world.checkBlockCollision(getBoundingBox())) return false;
        else return true;
    }

    @Override
    protected void registerData() {
        getDataManager().register(BLOCK_STATE, Optional.of(Blocks.DIRT.getDefaultState()));
        getDataManager().register(SHOULD_EXPLODE, false);
        getDataManager().register(ORIGIN, new BlockPos(0, 0, 0));
        getDataManager().register(DEATH_TIME, 1200);
        getDataManager().register(SIZE, 0);
    }

    public void setSizeParams() {
        //TODO
        int size = getBoulderSize();
        if (size == 0) {
//            setSize(1, 1);
        }
        else if (size == 1) {
//            setSize(2, 1.5f);
            finishedRisingTick = 8;
            damage = 12;
            speed = 1.2f;
        }
        else if (size == 2) {
//            setSize(3, 2.5f);
            finishedRisingTick = 12;
            damage = 16;
            speed = 1f;
        }
        else {
//            setSize(4, 3.5f);
            finishedRisingTick = 90;
            damage = 20;
            speed = 0.65f;
        }

        if (caster instanceof PlayerEntity) damage *= ConfigHandler.TOOLS_AND_ABILITIES.geomancyAttackMultiplier;

    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void tick() {
        if (firstUpdate) {
            setSizeParams();
            boulderSize = getBoulderSize();
        }
        if (storedBlock == null) storedBlock = getBlock();
        if (getShouldExplode()) explode();
        super.tick();
        move(MoverType.SELF, getMotion());
        if (ridingEntities != null) ridingEntities.clear();
        List<Entity> onTopOfEntities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().contract(0, getHeight() - 1, 0).offset(new Vec3d(0, getHeight() - 0.5, 0)).grow(0.6,0.5,0.6));
        for (Entity entity : onTopOfEntities) {
            if (entity != null && entity.canBeCollidedWith() && !(entity instanceof EntityBoulder) && entity.posY >= this.posY + 0.2) ridingEntities.add(entity);
        }
        if (travelling){
            for (Entity entity : ridingEntities) {
                entity.move(MoverType.SHULKER_BOX, getMotion());
            }
        }
//        if (boulderSize == 3) setSize(width, Math.min(ticksExisted/(float)finishedRisingTick * 3.5f, 3.5f)); //TODO

        if (ticksExisted < finishedRisingTick) {
            List<Entity> popUpEntities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox());
            for (Entity entity:popUpEntities) {
                if (entity.canBeCollidedWith() && !(entity instanceof EntityBoulder)) {
                    if (boulderSize != 3) entity.move(MoverType.SHULKER_BOX, new Vec3d(0, 2 * (Math.pow(2, -ticksExisted * (0.6 - 0.1 * boulderSize))), 0));
                    else entity.move(MoverType.SHULKER_BOX, new Vec3d(0, 0.6f, 0));
                }
            }
        }
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(1.7);
        if (travelling && !entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (world.isRemote) continue;
                if (entity == caster) continue;
                if (ridingEntities.contains(entity)) continue;
                if (caster != null) entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, caster), damage);
                else entity.attackEntityFrom(DamageSource.GENERIC, damage); // TODO: Magic damage goes through armor, but no other damage type fits. Create new damage type
                if (isAlive() && boulderSize != 3) setShouldExplode(true);
            }
        }
        List<EntityBoulder> bouldersHit = world.getEntitiesWithinAABB(EntityBoulder.class, getBoundingBox().grow(0.2, 0.2, 0.2).offset(getMotion().normalize().scale(0.5)));
        if (travelling && !bouldersHit.isEmpty()) {
            for (EntityBoulder entity : bouldersHit) {
                if (!entity.travelling) {
                    entity.hitByEntity(this);
                    explode();
                }
            }
        }

        if (travelling && world.checkBlockCollision(getBoundingBox().grow(0.1,0.1,0.1))) setShouldExplode(true);

        if (ticksExisted == 1) {
            for (int i = 0; i < 20 * getWidth(); i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 1.3 * getWidth(), 0, 0);
                particlePos = particlePos.rotateYaw((float)(rand.nextFloat() * 2 * Math.PI));
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, storedBlock), posX + particlePos.x, posY - 1, posZ + particlePos.z, particlePos.x, 2, particlePos.z);
            }
            if (boulderSize == 0) {
                playSound(MMSounds.EFFECT_GEOMANCY_SMALL_CRASH.get(), 1.5f, 1.3f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 1f);
            }
            else if (boulderSize == 1) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_2.get(), 1.5f, 1.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.8f);
            }
            else if (boulderSize == 2) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 0.9f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1.5f);
            }
            else if (boulderSize == 3) {
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 2f, 0.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_1.get(), 2, 0.8f);
            }
            EntityRing ring = new EntityRing(EntityHandler.RING, world, (float)posX, (float)posY - 0.9f, (float)posZ, new Vec3d(0,1,0), (int) (5 + 2 * getWidth()), 0.83f, 1, 0.39f, 1f, 1.0f + 0.5f * getWidth(), false);
            world.addEntity(ring);
        }
        if (ticksExisted == 30 && boulderSize == 3) {
            playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_2.get(), 2, 0.7f);
        }

        int dripTick = ticksExisted - 2;
        if (boulderSize == 3) dripTick -= 20;
        int dripNumber = (int)(getWidth() * 6 * Math.pow(1.03 + 0.04 * 1/getWidth(), -(dripTick)));
        if (dripNumber >= 1 && dripTick > 0) {
            dripNumber *= rand.nextFloat();
            for (int i = 0; i < dripNumber; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.6 * getWidth(), 0, 0);
                particlePos = particlePos.rotateYaw((float)(rand.nextFloat() * 2 * Math.PI));
                float offsetY;
                if (boulderSize == 3 && ticksExisted < finishedRisingTick) offsetY = (float) (rand.nextFloat() * (getHeight()-1) - getHeight() * (finishedRisingTick - ticksExisted)/finishedRisingTick);
                else offsetY = (float) (rand.nextFloat() * (getHeight()-1));
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, storedBlock), posX + particlePos.x, posY + offsetY, posZ + particlePos.z, 0, -1, 0);
            }
        }
        int newDeathTime = getDeathTime() - 1;
        setDeathTime(newDeathTime);
        if (newDeathTime < 0) this.explode();
    }

    private void explode() {
        remove();
        for (int i = 0; i < 40 * getWidth(); i++) {
            Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.7 * getWidth(), 0, 0);
            particlePos = particlePos.rotateYaw((float)(rand.nextFloat() * 2 * Math.PI));
            particlePos = particlePos.rotatePitch((float)(rand.nextFloat() * 2 * Math.PI));
            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, storedBlock), posX + particlePos.x, posY + 0.5 + particlePos.y, posZ + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
        }
        if (boulderSize == 0) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.9f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK.get(), 1.5f, 1f);
        }
        else if (boulderSize == 1) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.7f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_3.get(), 1.5f, 1.5f);
        }
        else if (boulderSize == 2) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_1.get(), 1.5f, 0.9f);

            if (world.isRemote) {
                for (int i = 0; i < 5; i++) {
                    Vec3d particlePos = new Vec3d(rand.nextFloat() * 2, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.add(new Vec3d(0, getHeight() / 4, 0));
                    ParticleFallingBlock.spawnFallingBlock(world, posX + particlePos.x, posY + 0.5 + particlePos.y, posZ + particlePos.z, 10.f, 90, 1, (float) particlePos.x * 0.3f, 0.2f + (float) rand.nextFloat() * 0.6f, (float) particlePos.z * 0.3f, ParticleFallingBlock.EnumScaleBehavior.CONSTANT, getBlock());
                }
            }
        }
        else if (boulderSize == 3) {
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.5f);
            playSound(MMSounds.EFFECT_GEOMANCY_BREAK_LARGE_1.get(), 1.5f, 0.5f);

            if (world.isRemote) {
                for (int i = 0; i < 7; i++) {
                    Vec3d particlePos = new Vec3d(rand.nextFloat() * 3, 0, 0);
                    particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.add(new Vec3d(0, getHeight() / 4, 0));
                    ParticleFallingBlock.spawnFallingBlock(world, posX + particlePos.x, posY + 0.5 + particlePos.y, posZ + particlePos.z, 10.f, 70, 1, (float) particlePos.x * 0.3f, 0.2f + (float) rand.nextFloat() * 0.6f, (float) particlePos.z * 0.3f, ParticleFallingBlock.EnumScaleBehavior.CONSTANT, getBlock());
                }
            }
        }
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return getBoundingBox();
    }

    public BlockState getBlock() {
        Optional<BlockState> bsOp = getDataManager().get(BLOCK_STATE);
        return bsOp.orElse(null);
    }

    public void setBlock(BlockState block) {
        getDataManager().set(BLOCK_STATE, Optional.of(block));
        this.storedBlock = block;
    }

    public boolean getShouldExplode() {
        return getDataManager().get(SHOULD_EXPLODE);
    }

    public void setShouldExplode(boolean shouldExplode) {
        getDataManager().set(SHOULD_EXPLODE, shouldExplode);
    }

    public void setOrigin(BlockPos pos) {
        this.dataManager.set(ORIGIN, pos);
    }

    public BlockPos getOrigin() {
        return this.dataManager.get(ORIGIN);
    }

    public int getDeathTime() {
        return dataManager.get(DEATH_TIME);
    }

    public void setDeathTime(int deathTime) {
        dataManager.set(DEATH_TIME, deathTime);
    }

    public int getBoulderSize() {
        return dataManager.get(SIZE);
    }

    public void setBoulderSize(int size) {
        dataManager.set(SIZE, size);
        boulderSize = size;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        BlockState blockState = getBlock();
        if (blockState != null) compound.put("block", NBTUtil.writeBlockState(blockState));
        compound.putInt("deathTime", getDeathTime());
        compound.putInt("size", getBoulderSize());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        INBT blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NBTUtil.readBlockState((CompoundNBT) blockStateCompound);
            setBlock(blockState);
        }
        setDeathTime(compound.getInt("deathTime"));
        setBoulderSize(compound.getInt("size"));
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (ticksExisted > finishedRisingTick - 1) {
            if (entityIn instanceof PlayerEntity
                    && ((PlayerEntity)entityIn).inventory.getCurrentItem().isEmpty()
                    && ((PlayerEntity) entityIn).isPotionActive(PotionHandler.GEOMANCY)) {
                PlayerEntity player = (PlayerEntity) entityIn;
                if (ridingEntities.contains(player)) {
                    Vec3d lateralLookVec = Vec3d.fromPitchYaw(0, player.rotationYaw).normalize();
                    setMotion(speed * 0.5 * lateralLookVec.x, getMotion().y, speed * 0.5 * lateralLookVec.z);
                } else {
                    setMotion(player.getLookVec().scale(speed * 0.5));
                }
            }
            else if (entityIn instanceof EntityBoulder && ((EntityBoulder) entityIn).travelling) {
                EntityBoulder boulder = (EntityBoulder)entityIn;
                Vec3d thisPos = new Vec3d(posX, posY, posZ);
                Vec3d boulderPos = new Vec3d(boulder.posX, boulder.posY, boulder.posZ);
                Vec3d velVec = thisPos.subtract(boulderPos).normalize();
                setMotion(velVec.scale(speed * 0.5));
            }
            else {
                return super.hitByEntity(entityIn);
            }
            if (!travelling) setDeathTime(60);
            travelling = true;

            if (boulderSize == 0) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 1.3f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.9f);
            }
            else if (boulderSize == 1) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 0.9f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.5f);
            }
            else if (boulderSize == 2) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 0.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1.3f);
            }
            else if (boulderSize == 3) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 1f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.9f);
            }

            Vec3d ringOffset = getMotion().normalize().scale(-1);
            EntityRing ring = new EntityRing(EntityHandler.RING, entityIn.world, (float) posX + (float) ringOffset.x, (float) posY + 0.5f + (float) ringOffset.y, (float) posZ + (float) ringOffset.z, ringOffset.normalize(), (int) (4 + 1 * getWidth()), 0.83f, 1, 0.39f, 1f, 1.0f + 0.5f * getWidth(), false);
            entityIn.world.addEntity(ring);
        }
        return super.hitByEntity(entityIn);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public List<LivingEntity> getEntityLivingBaseNearby(double radius) {
        return getEntitiesNearby(LivingEntity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f);
    }


    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        super.playSound(soundIn, volume, pitch + rand.nextFloat() * 0.25f - 0.125f);
    }
}
