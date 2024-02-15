package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicPlayer;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MowzieEntity extends PathfinderMob implements IEntityAdditionalSpawnData, IntermittentAnimatableEntity {
    private static final byte START_IA_HEALTH_UPDATE_ID = 4;
    private static final byte MUSIC_PLAY_ID = 67;
    private static final byte MUSIC_STOP_ID = 68;

    public int frame;
    public float targetDistance = -1;
    public float targetAngle = -1;
    public boolean active;
    public LivingEntity blockingEntity = null;
    public boolean playsHurtAnimation = true;
    protected boolean dropAfterDeathAnim = true;
    public boolean hurtInterruptsAnimation = false;
    private final List<IntermittentAnimation<?>> intermittentAnimations = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public Vec3[] socketPosArray;

    protected boolean prevOnGround;
    protected boolean prevPrevOnGround;
    protected boolean willLandSoon;
    
    private int killDataRecentlyHit;
    private DamageSource killDataCause;
    private Player killDataAttackingPlayer;

    private final MMBossInfoServer bossInfo;

    private static final UUID HEALTH_CONFIG_MODIFIER_UUID = UUID.fromString("eff1c400-910c-11ec-b909-0242ac120002");
    private static final UUID ATTACK_CONFIG_MODIFIER_UUID = UUID.fromString("f76a7c90-910c-11ec-b909-0242ac120002");

    private static final EntityDataAccessor<Boolean> STRAFING = SynchedEntityData.defineId(MowzieEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean renderingInGUI = false;

    public MowzieEntity(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
        if (world.isClientSide) {
            socketPosArray = new Vec3[]{};
        }

        // Load config attribute multipliers
        ConfigHandler.CombatConfig combatConfig = getCombatConfig();
        if (combatConfig != null) {
            AttributeInstance maxHealthAttr = getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double difference = maxHealthAttr.getBaseValue() * getCombatConfig().healthMultiplier.get() - maxHealthAttr.getBaseValue();
                maxHealthAttr.addTransientModifier(new AttributeModifier(HEALTH_CONFIG_MODIFIER_UUID, "Health config multiplier", difference, AttributeModifier.Operation.ADDITION));
                this.setHealth(this.getMaxHealth());
            }

            AttributeInstance attackDamageAttr = getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamageAttr != null) {
                double difference = attackDamageAttr.getBaseValue() * getCombatConfig().attackMultiplier.get() - attackDamageAttr.getBaseValue();
                attackDamageAttr.addTransientModifier(new AttributeModifier(ATTACK_CONFIG_MODIFIER_UUID, "Attack config multiplier", difference, AttributeModifier.Operation.ADDITION));
            }
        }

        bossInfo = makeBossInfo();
    }

    protected MMBossInfoServer makeBossInfo() {
        return new MMBossInfoServer(this, null, null);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STRAFING, false);
    }

    public void setStrafing(boolean strafing) {
        entityData.set(STRAFING, strafing);
    }

    public boolean isStrafing() {
        return entityData.get(STRAFING);
    }

    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return null;
    }

    protected ConfigHandler.CombatConfig getCombatConfig() {
        return null;
    }

    public static boolean spawnPredicate(EntityType type, LevelAccessor world, MobSpawnType reason, BlockPos spawnPos, Random rand) {
        ConfigHandler.SpawnConfig spawnConfig = SpawnHandler.spawnConfigs.get(type);
        if (spawnConfig != null) {
            if (rand.nextDouble() > spawnConfig.extraRarity.get()) return false;

            // Dimension check
            List<? extends String> dimensionNames = spawnConfig.dimensions.get();
            ResourceLocation currDimensionName = ((ServerLevel)world).dimension().location();
            if (!dimensionNames.contains(currDimensionName.toString())) {
                return false;
            }

            // Height check
            float heightMax = spawnConfig.heightMax.get();
            float heightMin = spawnConfig.heightMin.get();
            if (spawnPos.getY() > heightMax && heightMax >= -64) {
                return false;
            }
            if (spawnPos.getY() < heightMin && heightMin >= -64) {
                return false;
            }

            // Light level check
            if (spawnConfig.needsDarkness.get() && !Monster.isDarkEnoughToSpawn((ServerLevelAccessor) world, spawnPos, rand)) {
                return false;
            }

            // Block check
            BlockState block = world.getBlockState(spawnPos.below());
            ResourceLocation blockName = block.getBlock().getRegistryName();
            List<? extends String> allowedBlocks = spawnConfig.allowedBlocks.get();
            List<? extends String> allowedBlockTags = spawnConfig.allowedBlockTags.get();
            if (blockName == null) return false;
            if (!allowedBlocks.isEmpty() && !allowedBlocks.contains(blockName.toString()) && !allowedBlocks.contains(blockName.getPath())) return false;
            if (!allowedBlockTags.isEmpty() && !isBlockTagAllowed(allowedBlockTags, block)) return false;

            // See sky
            if (spawnConfig.needsSeeSky.get() && !world.canSeeSkyFromBelowWater(spawnPos)) {
                return false;
            }
            if (spawnConfig.needsCantSeeSky.get() && world.canSeeSkyFromBelowWater(spawnPos)) {
                return false;
            }

            List<? extends String> avoidStructures = spawnConfig.avoidStructures.get();
            Registry<StructureSet> structureSetRegistry = world.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
            ServerLevel serverLevel = (ServerLevel) world;
            ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();
            long seed = serverLevel.getSeed();
            ChunkPos chunkPos = new ChunkPos(spawnPos);
            for (String structureName : avoidStructures) {
                Optional<StructureSet> structureSetOptional = structureSetRegistry.getOptional(new ResourceLocation(structureName));
                if (structureSetOptional.isEmpty()) continue;
                Optional<ResourceKey<StructureSet>> resourceKeyOptional = structureSetRegistry.getResourceKey(structureSetOptional.get());
                if (resourceKeyOptional.isEmpty()) continue;
                if (generator.hasFeatureChunkInRange(resourceKeyOptional.get(), seed, chunkPos.x, chunkPos.z, 3)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isBlockTagAllowed(List<? extends String> allowedBlockTags, BlockState block) {
        for (String allowedBlockTag : allowedBlockTags) {
            TagKey<Block> tagKey = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(allowedBlockTag));
            if (block.is(tagKey)) return true;
        }
        return false;
    }

    protected boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), (double)distance);
    }

    @Override
    public void tick() {
        prevPrevOnGround = prevOnGround;
        prevOnGround = onGround;
        super.tick();
        frame++;
        if (tickCount % 4 == 0) bossInfo.update();
        if (getTarget() != null) {
            targetDistance = distanceTo(getTarget()) - getTarget().getBbWidth() / 2f;
            targetAngle = (float) getAngleBetweenEntities(this, getTarget());
        }
        willLandSoon = !onGround && level.noCollision(getBoundingBox().move(getDeltaMovement()));

        if (!level.isClientSide && getBossMusic() != null) {
            if (canPlayMusic()) {
                this.level.broadcastEntityEvent(this, MUSIC_PLAY_ID);
            }
            else {
                this.level.broadcastEntityEvent(this, MUSIC_STOP_ID);
            }
        }
    }

    protected boolean canPlayMusic() {
        return !isSilent() && getTarget() instanceof Player;
    }

    public boolean canPlayerHearMusic(Player player) {
        return player != null
                && canAttack(player)
                && distanceTo(player) < 2500;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }


    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        yRotO = getYRot();
        yBodyRotO = yBodyRot = yHeadRotO = yHeadRot;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        return this.doHurtTarget(entityIn, 1.0F, 1.0f);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
//        System.out.println("Spawned " + getName().getString() + " at " + getPosition());
//        System.out.println("Block " + world.getBlockState(getPosition().down()).toString());
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean doHurtTarget(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier) {
        return doHurtTarget(entityIn, damageMultiplier, applyKnockbackMultiplier, false);
    }

    public boolean doHurtTarget(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier, boolean canDisableShield) { // Copied from mob class
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * damageMultiplier;
        float f1 = (float)this.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entityIn).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entityIn.setSecondsOnFire(i * 4);
        }

        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(applyKnockbackMultiplier, 1.0, applyKnockbackMultiplier));
            if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).knockback(f1 * 0.5F, Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            if (entityIn instanceof Player) {
                Player player = (Player)entityIn;
                if (canDisableShield) this.maybeDisableShield(player, player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
            }

            this.doEnchantDamageEffects(this, entityIn);
            this.setLastHurtMob(entityIn);
        }

        return flag;
    }

    private void maybeDisableShield(Player player, ItemStack itemStackBlock) { // Copied from mob class
        if (!itemStackBlock.isEmpty() && itemStackBlock.is(Items.SHIELD)) {
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level.broadcastEntityEvent(player, (byte)30);
            }
        }
    }

    public float getHealthRatio() {
        return this.getHealth() / this.getMaxHealth();
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getZ() - first.getZ(), second.getX() - first.getX()) * (180 / Math.PI) + 90;
    }

    public List<Player> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = level.getEntities(this, getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        List<Player> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof Player && distanceTo(entityNeighbor) <= radius + entityNeighbor.getBbWidth() / 2f).map(entityNeighbor -> (Player) entityNeighbor).collect(Collectors.toList());
        return listEntityPlayers;
    }

    public List<LivingEntity> getAttackableEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = level.getEntities(this, getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        List<LivingEntity> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && ((LivingEntity)entityNeighbor).attackable() && (!(entityNeighbor instanceof Player) || !((Player)entityNeighbor).isCreative()) && distanceTo(entityNeighbor) <= radius + entityNeighbor.getBbWidth() / 2f).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityLivingBase;
    }

    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return level.getEntitiesOfClass(entityClass, getBoundingBox().inflate(r, r, r), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return level.getEntitiesOfClass(entityClass, getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= getY() + dY);
    }

    @Override
    protected void tickDeath() { // Copied from entityLiving
        ++this.deathTime;
        int deathDuration = getDeathDuration();
        if (this.deathTime >= deathDuration && !this.level.isClientSide()) {
            lastHurtByPlayer = killDataAttackingPlayer;
            lastHurtByPlayerTime = killDataRecentlyHit;
            if (dropAfterDeathAnim && killDataCause != null) {
                dropAllDeathLoot(killDataCause);
            }
            this.level.broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    protected abstract int getDeathDuration();

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        if (!dropAfterDeathAnim || deathTime > 0) {
            super.dropAllDeathLoot(source);
        }
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.dead) {
            killDataCause = cause;
            killDataRecentlyHit = this.lastHurtByPlayerTime;
            killDataAttackingPlayer = lastHurtByPlayer;
        }
        super.die(cause);
        if (!this.isRemoved()) {
            bossInfo.update();
        }
    }

    protected void addIntermittentAnimation(IntermittentAnimation animation) {
        animation.setID((byte) intermittentAnimations.size());
        intermittentAnimations.add(animation);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id >= START_IA_HEALTH_UPDATE_ID && id - START_IA_HEALTH_UPDATE_ID < intermittentAnimations.size()) {
            intermittentAnimations.get(id - START_IA_HEALTH_UPDATE_ID).start();
        }
        else if (id == MUSIC_PLAY_ID) {
            BossMusicPlayer.playBossMusic(this);
        }
        else if (id == MUSIC_STOP_ID) {
            BossMusicPlayer.stopBossMusic(this);
        }
        else super.handleEntityEvent(id);
    }

    @Override
    public byte getOffsetEntityState() {
        return START_IA_HEALTH_UPDATE_ID;
    }

    public Vec3 circleEntityPosition(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset) {
        int directionInt = direction ? 1 : -1;
        double t = directionInt * circleFrame * 0.5 * speed / radius + offset;
        Vec3 movePos = target.position().add(radius * Math.cos(t), 0, radius * Math.sin(t));
        return movePos;
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<LivingEntity> nearbyEntities = getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            if (entity.isPickable() && !entity.noPhysics) {
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setDeltaMovement(-0.1 * Math.cos(angle), entity.getDeltaMovement().y, -0.1 * Math.sin(angle));
            }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    public boolean hasBossBar() {
        return false;
    }

    public boolean resetHealthOnPlayerRespawn() {
        return false;
    }

    protected BossEvent.BossBarColor bossBarColor() {
        return BossEvent.BossBarColor.PURPLE;
    }

    @OnlyIn(Dist.CLIENT)
    public void setSocketPosArray(int index, Vec3 pos) {
        if (socketPosArray != null && socketPosArray.length > index) {
            socketPosArray[index] = pos;
        }
    }

    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }

    // TODO: Copied from parent classes
    @Override
    public void push(Entity entityIn) {
        if (!this.isSleeping()) {
            if (!this.isPassengerOfSameVehicle(entityIn)) {
                if (!entityIn.noPhysics && !this.noPhysics) {
                    double d0 = entityIn.getX() - this.getX();
                    double d1 = entityIn.getZ() - this.getZ();
                    double d2 = Mth.absMax(d0, d1);
                    if (d2 >= (double)0.01F) {
                        d2 = Math.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * (double)0.05F;
                        d1 = d1 * (double)0.05F;
                        if (!this.isVehicle()) {
                            if (canBePushedByEntity(entityIn)) {
                                this.push(-d0, 0.0D, -d1);
                            }
                        }

                        if (!entityIn.isVehicle()) {
                            entityIn.push(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
    }

    public SoundEvent getBossMusic() {
        return null;
    }
}
