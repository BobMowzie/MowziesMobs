package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicSound;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MowzieEntity extends CreatureEntity implements IEntityAdditionalSpawnData, IAnimatedEntity, IntermittentAnimatableEntity {
    private static final byte START_IA_HEALTH_UPDATE_ID = 4;
    private static final byte MUSIC_PLAY_ID = 67;
    private static final byte MUSIC_STOP_ID = 68;

    public int frame;
    public float targetDistance = -1;
    public float targetAngle = -1;
    public boolean active;
    public LivingEntity blockingEntity = null;
    private int animationTick;
    private Animation animation = NO_ANIMATION;
    private final List<IntermittentAnimation<?>> intermittentAnimations = new ArrayList<>();
    public boolean playsHurtAnimation = true;
    protected boolean dropAfterDeathAnim = true;
    public boolean hurtInterruptsAnimation = false;

    @OnlyIn(Dist.CLIENT)
    public Vector3d[] socketPosArray;

    protected boolean prevOnGround;
    protected boolean prevPrevOnGround;
    protected boolean willLandSoon;
    
    private int killDataRecentlyHit;
    private DamageSource killDataCause;
    private PlayerEntity killDataAttackingPlayer;

    private final MMBossInfoServer bossInfo = new MMBossInfoServer(this);

    public static BossMusicSound bossMusic;

    public MowzieEntity(EntityType<? extends MowzieEntity> type, World world) {
        super(type, world);
        if (world.isRemote) {
            socketPosArray = new Vector3d[]{};
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.ATTACK_DAMAGE);
    }

    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return null;
    }

    public static boolean spawnPredicate(EntityType type, IWorld world, SpawnReason reason, BlockPos spawnPos, Random rand) {
        ConfigHandler.SpawnConfig spawnConfig = SpawnHandler.spawnConfigs.get(type);
        if (spawnConfig != null) {
            if (rand.nextDouble() > spawnConfig.extraRarity.get()) return false;

            // Dimension check
            List<? extends String> dimensionNames = spawnConfig.dimensions.get();
            ResourceLocation currDimensionName = ((ServerWorld)world).getDimensionKey().getLocation();
            if (!dimensionNames.contains(currDimensionName.toString())) {
                return false;
            }

            // Height check
            float heightMax = spawnConfig.heightMax.get();
            float heightMin = spawnConfig.heightMin.get();
            if (spawnPos.getY() > heightMax && heightMax >= 0) {
                return false;
            }
            if (spawnPos.getY() < heightMin) {
                return false;
            }

            // Light level check
            if (spawnConfig.needsDarkness.get() && !MonsterEntity.isValidLightLevel((IServerWorld) world, spawnPos, rand)) {
                return false;
            }

            // Block check
            Block block = world.getBlockState(spawnPos.down()).getBlock();
            ResourceLocation blockName = block.getRegistryName();
            List<? extends String> allowedBlocks = spawnConfig.allowedBlocks.get();
            List<? extends String> allowedBlockTags = spawnConfig.allowedBlockTags.get();
            if (blockName == null) return false;
            if (!allowedBlocks.isEmpty() && !allowedBlocks.contains(blockName.toString()) && !allowedBlocks.contains(blockName.getPath())) return false;
            if (!allowedBlockTags.isEmpty() && !isBlockTagAllowed(allowedBlockTags, block)) return false;

            // See sky
            if (spawnConfig.needsSeeSky.get() && !world.canBlockSeeSky(spawnPos)) {
                return false;
            }
            if (spawnConfig.needsCantSeeSky.get() && world.canBlockSeeSky(spawnPos)) {
                return false;
            }

            List<? extends String> avoidStructures = spawnConfig.avoidStructures.get();
            for (String structureName : avoidStructures) {
                Structure<?> structure = ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(structureName));
                if (structure == null) continue;
                BlockPos pos = ((ServerWorld) world).getStructureLocation(structure, spawnPos, 3, false);
                if (pos == null) continue;
                double dist = spawnPos.add(0, -spawnPos.getY(), 0).distanceSq(pos);
                if (dist < 900) return false;
            }
        }
        return true;
    }

    private static boolean structureNearby(Structure structure, ChunkGenerator chunkGenerator, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkY) {
        StructureSeparationSettings structureseparationsettings = chunkGenerator.func_235957_b_().func_236197_a_(structure);
        if (structureseparationsettings != null) {
            for (int i = chunkX - 10; i <= chunkX + 10; ++i) {
                for (int j = chunkY - 10; j <= chunkY + 10; ++j) {
                    ChunkPos chunkpos = structure.getChunkPosForStructure(structureseparationsettings, seed, chunkRandom, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private static boolean isBlockTagAllowed(List<? extends String> allowedBlockTags, Block block) {
        for (String allowedBlockTag : allowedBlockTags) {
            if (BlockTags.getCollection().getTagByID(new ResourceLocation(allowedBlockTag)).contains(block)) return true;
        }
        return false;
    }

    @Override
    public void tick() {
        prevPrevOnGround = prevOnGround;
        prevOnGround = onGround;
        super.tick();
        frame++;
        if (getAnimation() != NO_ANIMATION) {
            animationTick++;
            if (world.isRemote && animationTick >= animation.getDuration()) {
                setAnimation(NO_ANIMATION);
            }
        }
        if (ticksExisted % 4 == 0) bossInfo.update();
        if (getAttackTarget() != null) {
            targetDistance = getDistance(getAttackTarget()) - getAttackTarget().getWidth() / 2f;
            targetAngle = (float) getAngleBetweenEntities(this, getAttackTarget());
        }
        willLandSoon = !onGround && world.hasNoCollisions(getBoundingBox().offset(getMotion()));

        if (!world.isRemote && getBossMusic() != null) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (canPlayMusic()) {
                this.world.setEntityState(this, MUSIC_PLAY_ID);
            }
            else {
                this.world.setEntityState(this, MUSIC_STOP_ID);
            }
        }
    }

    protected boolean canPlayMusic() {
        return !isSilent() && getAttackTarget() instanceof PlayerEntity;
    }

    protected boolean canPlayerHearMusic(PlayerEntity player) {
        return player != null
                && canAttack(player)
                && getDistance(player) < 2500;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    protected void onAnimationFinish(Animation animation) {}

    @Override
    public void writeSpawnData(PacketBuffer buf) {
        buf.writeInt(ArrayUtils.indexOf(this.getAnimations(), this.getAnimation()));
        buf.writeInt(this.getAnimationTick());
    }

    @Override
    public void readSpawnData(PacketBuffer buf) {
        prevRotationYaw = rotationYaw;
        prevRenderYawOffset = renderYawOffset = prevRotationYawHead = rotationYawHead;
        int animOrdinal = buf.readInt();
        int animTick = buf.readInt();
        this.setAnimation(animOrdinal == -1 ? IAnimatedEntity.NO_ANIMATION : this.getAnimations()[animOrdinal]);
        this.setAnimationTick(animTick);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return this.attackEntityAsMob(entityIn, 1.0F, 1.0f);
    }

//    @Override
//    public boolean isNotColliding() {
//        return !this.world.containsAnyLiquid(this.getBoundingBox()) && this.world.checkNoEntityCollision(this.getBoundingBox(), this);
//    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
//        System.out.println("Spawned " + getName().getString() + " at " + getPosition());
//        System.out.println("Block " + world.getBlockState(getPosition().down()).toString());
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean attackEntityAsMob(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier) {
        return attackEntityAsMob(entityIn, damageMultiplier, applyKnockbackMultiplier, false);
    }

    public boolean attackEntityAsMob(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier, boolean canDisableShield) { // TODO copy from mob class
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * damageMultiplier;
        float f1 = (float)this.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue() * applyKnockbackMultiplier;
        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity)entityIn).getCreatureAttribute());
            f1 += (float)EnchantmentHelper.getKnockbackModifier(this);
        }

        int i = EnchantmentHelper.getFireAspectModifier(this);
        if (i > 0) {
            entityIn.setFire(i * 4);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).applyKnockback(f1 * 0.5F, MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)), -MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F)));
                this.setMotion(this.getMotion().mul(0.6D, 1.0D, 0.6D));
            }

            if (entityIn instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY;
                if (((!itemstack.isEmpty() && itemstack.canDisableShield(itemstack1, playerentity, this)) || canDisableShield) && !itemstack1.isEmpty() && itemstack1.isShield(playerentity)) {
                    float f2 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
                    if (this.rand.nextFloat() < f2) {
                        playerentity.getCooldownTracker().setCooldown(itemstack.getItem(), 100);
                        this.world.setEntityState(playerentity, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
            this.setLastAttackedEntity(entityIn);
        }

        return flag;
    }

    public float getHealthRatio() {
        return this.getHealth() / this.getMaxHealth();
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getPosZ() - first.getPosZ(), second.getPosX() - first.getPosX()) * (180 / Math.PI) + 90;
    }

    public List<PlayerEntity> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(distanceX, distanceY, distanceZ));
        List<PlayerEntity> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof PlayerEntity && getDistance(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).map(entityNeighbor -> (PlayerEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityPlayers;
    }

    public List<LivingEntity> getAttackableEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(distanceX, distanceY, distanceZ));
        List<LivingEntity> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && ((LivingEntity)entityNeighbor).attackable() && (!(entityNeighbor instanceof PlayerEntity) || !((PlayerEntity)entityNeighbor).isCreative()) && getDistance(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityLivingBase;
    }

    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f && e.getPosY() <= getPosY() + dY);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (getHealth() <= 0.0F) {
            Animation death;
            if ((death = getDeathAnimation()) != null) {
                onDeathUpdate(death.getDuration() - 20);   
            } else {
                onDeathUpdate(20);
            }
        }
    }

    private void onDeathUpdate(int deathDuration) { // TODO copy from entityLiving
        onDeathAIUpdate();

        ++this.deathTime;
        if (this.deathTime == deathDuration) {
            attackingPlayer = killDataAttackingPlayer;
            recentlyHit = killDataRecentlyHit;
            if (!world.isRemote() && dropAfterDeathAnim && killDataCause != null) {
                spawnDrops(killDataCause);
            }

            this.remove(false);

            for(int i = 0; i < 20; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
            }
        }
    }

    protected void onDeathAIUpdate() {}

    @Override
    protected final void onDeathUpdate() {}

    @Override
    public void onDeath(DamageSource cause) // TODO copy from entityLiving
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.dead) {
            Entity entity = cause.getTrueSource();
            LivingEntity livingentity = this.getAttackingEntity();
            if (this.scoreValue >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.scoreValue, cause);
            }

            if (this.isSleeping()) {
                this.wakeUp();
            }

            this.dead = true;
            this.getCombatTracker().reset();
            if (this.world instanceof ServerWorld) {
                if (entity != null) {
                    entity.onKillEntity((ServerWorld)this.world, this);
                }

                if (!dropAfterDeathAnim)
                    this.spawnDrops(cause);
                this.createWitherRose(livingentity);
            }
            killDataCause = cause;
            killDataRecentlyHit = this.recentlyHit;
            killDataAttackingPlayer = attackingPlayer;

            this.world.setEntityState(this, (byte)3);
            this.setPose(Pose.DYING);
            bossInfo.update();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        boolean attack = super.attackEntityFrom(source, damage);
        if (attack) {
            if (getHealth() > 0.0F && (getAnimation() == NO_ANIMATION || hurtInterruptsAnimation) && playsHurtAnimation) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getHurtAnimation());
            } else if (getHealth() <= 0.0F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getDeathAnimation());
            }
        }
        return attack;
    }

    protected void addIntermittentAnimation(IntermittentAnimation animation) {
        animation.setID((byte) intermittentAnimations.size());
        intermittentAnimations.add(animation);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id >= START_IA_HEALTH_UPDATE_ID && id - START_IA_HEALTH_UPDATE_ID < intermittentAnimations.size()) {
            intermittentAnimations.get(id - START_IA_HEALTH_UPDATE_ID).start();
        }
        else if (id == MUSIC_PLAY_ID) {
            SoundEvent soundEvent = getBossMusic();
            if (soundEvent != null && this.isAlive()) {
                PlayerEntity player = Minecraft.getInstance().player;
                if (bossMusic != null) {
                    float f2 = Minecraft.getInstance().gameSettings.getSoundLevel(SoundCategory.MUSIC);
                    if (f2 <= 0) {
                        bossMusic = null;
                    }
                    else if (bossMusic.getBoss() == this && !canPlayerHearMusic(player)) {
                        bossMusic.setBoss(null);
                    }
                }
                else {
                    if (canPlayerHearMusic(player)) {
                        if (bossMusic == null) {
                            bossMusic = new BossMusicSound(getBossMusic(), this);
                        }
                        else if (bossMusic.getBoss() == null && bossMusic.getSoundEvent() == soundEvent) {
                            bossMusic.setBoss(this);
                        }
                    }
                }
                if (bossMusic != null && !Minecraft.getInstance().getSoundHandler().isPlaying(bossMusic)) {
                    Minecraft.getInstance().getSoundHandler().play(bossMusic);
                }
            }
        }
        else if (id == MUSIC_STOP_ID) {
            if (bossMusic != null && bossMusic.getBoss() == this)
                bossMusic.setBoss(null);
        }
        else super.handleStatusUpdate(id);
    }

    @Override
    public byte getOffsetEntityState() {
        return START_IA_HEALTH_UPDATE_ID;
    }

    public void circleEntity(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset, float moveSpeedMultiplier) {
        int directionInt = direction ? 1 : -1;
        double t = directionInt * circleFrame * 0.5 * speed / radius + offset;
        Vector3d movePos = target.getPositionVec().add(radius * Math.cos(t), 0, radius * Math.sin(t));
        this.getNavigator().tryMoveToXYZ(movePos.getX(), movePos.getY(), movePos.getZ(), speed * moveSpeedMultiplier);
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<LivingEntity> nearbyEntities = getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            if (entity.canBeCollidedWith() && !entity.noClip) {
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setMotion(-0.1 * Math.cos(angle), entity.getMotion().y, -0.1 * Math.sin(angle));
            }
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (animation == NO_ANIMATION) {
            onAnimationFinish(this.animation);
        }
        this.animation = animation;
        setAnimationTick(0);
    }

    public abstract Animation getDeathAnimation();

    public abstract Animation getHurtAnimation();

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    protected boolean hasBossBar() {
        return false;
    }

    protected BossInfo.Color bossBarColor() {
        return BossInfo.Color.PURPLE;
    }

    @OnlyIn(Dist.CLIENT)
    public void setSocketPosArray(int index, Vector3d pos) {
        if (socketPosArray != null && socketPosArray.length > index) {
            socketPosArray[index] = pos;
        }
    }

    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }

    // TODO: Copied from parent classes
    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.isSleeping()) {
            if (!this.isRidingSameEntity(entityIn)) {
                if (!entityIn.noClip && !this.noClip) {
                    double d0 = entityIn.getPosX() - this.getPosX();
                    double d1 = entityIn.getPosZ() - this.getPosZ();
                    double d2 = MathHelper.absMax(d0, d1);
                    if (d2 >= (double)0.01F) {
                        d2 = (double)MathHelper.sqrt(d2);
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
                        d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - this.entityCollisionReduction);
                        if (!this.isBeingRidden()) {
                            if (canBePushedByEntity(entityIn)) {
                                this.addVelocity(-d0, 0.0D, -d1);
                            }
                        }

                        if (!entityIn.isBeingRidden()) {
                            entityIn.addVelocity(d0, 0.0D, d1);
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
