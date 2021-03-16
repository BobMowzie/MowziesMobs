package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
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

    public int frame;
    public float targetDistance;
    public float targetAngle;
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

    public static boolean spawnPredicate(EntityType type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return true;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        ConfigHandler.SpawnConfig spawnConfig = getSpawnConfig();
        if (spawnConfig != null) {
            int i = MathHelper.floor(this.getPosX());
            int j = MathHelper.floor(this.getBoundingBox().minY);
            int k = MathHelper.floor(this.getPosZ());
            BlockPos pos = new BlockPos(i, j, k);

            // Dimension check
            List<String> dimensionNames = spawnConfig.dimensions.get();
            MutableRegistry<DimensionType> mutableregistry = DynamicRegistries.func_239770_b_().getRegistry(Registry.DIMENSION_TYPE_KEY);
            ResourceLocation currDimensionName = mutableregistry.getOptionalKey(world.getDimensionType()).get().getRegistryName();
            if (currDimensionName == null || !dimensionNames.contains(currDimensionName.toString())) {
                return false;
            }

            // Height check
            float heightMax = spawnConfig.heightMax.get();
            float heightMin = spawnConfig.heightMin.get();
            if (getPosY() > heightMax && heightMax >= 0) {
                return false;
            }
            if (getPosY() < heightMin) {
                return false;
            }

            // Light level check
            if (spawnConfig.needsDarkness.get() && !MonsterEntity.isValidLightLevel((IServerWorld) world, pos, rand)) {
                return false;
            }

            // Block check
            ResourceLocation blockName = world.getBlockState(pos.down()).getBlock().getRegistryName();
            List<String> allowedBlocks = spawnConfig.allowedBlocks.get();
            if (blockName == null) return false;
            if (!allowedBlocks.isEmpty() && !allowedBlocks.contains(blockName.getPath())) return false;

            // See sky
            if (spawnConfig.needsSeeSky.get() && !world.canBlockSeeSky(pos)) {
                return false;
            }
            if (spawnConfig.needsCantSeeSky.get() && world.canBlockSeeSky(pos)) {
                return false;
            }
        }

        return super.canSpawn(world, reason);
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
        if (getAttackTarget() != null) {
            targetDistance = getDistance(getAttackTarget()) - getAttackTarget().getWidth() / 2f;
            targetAngle = (float) getAngleBetweenEntities(this, getAttackTarget());
        }
        willLandSoon = !onGround && world.hasNoCollisions(getBoundingBox().offset(getMotion()));
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.update();
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
//        System.out.println("Spawned " + getName().getFormattedText() + " at " + getPosition());
//        System.out.println("Block " + world.getBlockState(getPosition().down()).toString());
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean attackEntityAsMob(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier) { // TODO copy from mob class
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
                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.canDisableShield(itemstack1, playerentity, this) && itemstack1.isShield(playerentity)) {
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
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
                if (dropAfterDeathAnim && killDataCause != null) spawnDrops(killDataCause);
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

                if (dropAfterDeathAnim)
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
            return;
        }
        super.handleStatusUpdate(id);
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
            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            entity.setMotion(-0.1 * Math.cos(angle), entity.getMotion().y,-0.1 * Math.sin(angle));
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
}
