package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
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

    public Vec3d[] socketPosArray = new Vec3d[]{};

    protected boolean prevOnGround;
    protected boolean prevPrevOnGround;
    protected boolean willLandSoon;
    
    private boolean killDataRecentlyHitFlag;
    private int killDataLootingLevel;
    private DamageSource killDataCause;
    private PlayerEntity killDataAttackingPlayer;

    private final MMBossInfoServer bossInfo = new MMBossInfoServer(this);

    public MowzieEntity(EntityType<? extends MowzieEntity> type, World world) {
        super(type, world);
    }

    /*@Override
    public ItemStack getPickedResult(RayTraceResult target) {
        String id = getPickedEntityId();
        if (id == null) {
            return ItemStack.EMPTY;
        }
        Optional<EntityType<?>> type = EntityType.byKey(id);
        if (type.isPresent() && EntityHandler.INSTANCE.hasEntityEggInfo(type.get())) {
            ItemStack stack = new ItemStack(ItemHandler.SPAWN_EGG);
            ItemSpawnEgg.applyEntityIdToItemStack(stack, type.get());
            return stack;
        }
        return ItemStack.EMPTY;
    }*/ // TODO

    protected String getPickedEntityId() {
        return getEntityString();
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    protected ConfigHandler.SpawnData getSpawnConfig() {
        return null;
    }

    public static boolean spawnPredicate(EntityType type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return true;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        ConfigHandler.SpawnData spawnData = getSpawnConfig();
        if (spawnData != null) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.getBoundingBox().minY);
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);

            // Dimension check
            List<String> dimensionNames = Arrays.asList(spawnData.dimensions);
            ResourceLocation currDimensionName = world.getDimension().getType().getRegistryName();
            if (currDimensionName == null || !dimensionNames.contains(currDimensionName.toString())) {
                return false;
            }

            // Height check
            float heightMax = spawnData.heightMax;
            float heightMin = spawnData.heightMin;
            if (posY > heightMax && heightMax >= 0) {
                return false;
            }
            if (posY < heightMin) {
                return false;
            }

            // Light level check
            if (spawnData.needsDarkness && !isValidLightLevel(world, pos, rand)) {
                return false;
            }

            // Block check
            ResourceLocation blockName = world.getBlockState(pos.down()).getBlock().getRegistryName();
            List<String> allowedBlocks = Arrays.asList(spawnData.allowedBlocks);
            if (blockName == null) return false;
            if (!allowedBlocks.isEmpty() && !allowedBlocks.contains(blockName.getPath())) return false;

            // See sky
            if (spawnData.needsSeeSky && !world.canBlockSeeSky(pos)) {
                return false;
            }
            if (spawnData.needsCantSeeSky && world.canBlockSeeSky(pos)) {
                return false;
            }
        }

        return super.canSpawn(world, reason);
    }

    protected boolean isValidLightLevel(IWorld world, BlockPos pos, Random rand) {
        if (world.getLightFor(LightType.SKY, pos) > rand.nextInt(32)) {
            return false;
        } else {
            int lightLevel = world.getWorld().isThundering() ? world.getNeighborAwareLightSubtracted(pos, 10) : world.getLight(pos);
            return lightLevel <= rand.nextInt(8);
        }
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
        willLandSoon = !onGround && world.checkBlockCollision(getBoundingBox().offset(getMotion()));
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

    public int getAttack() {
        return 0;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return this.attackEntityAsMob(entityIn, 1.0F, 1.0f);
    }

//    @Override
//    public boolean isNotColliding() {
//        return !this.world.containsAnyLiquid(this.getBoundingBox()) && this.world.checkNoEntityCollision(this.getBoundingBox(), this);
//    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
//        System.out.println("Spawned " + getName() + " at " + getPosition());
//        System.out.println("Block " + world.getBlockState(getPosition().down()).toString());
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }

    public boolean attackEntityAsMob(Entity entityIn, float damageMultiplier, float knockbackMultiplier) {
        float f = (float)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue() * damageMultiplier;
        float f1 = (float)this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).getValue() * knockbackMultiplier;
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
                ((LivingEntity)entityIn).knockBack(this, f1 * 0.5F, (double)MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F))));
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
        }

        return flag;
    }

    public float getHealthRatio() {
        return this.getHealth() / this.getMaxHealth();
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
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
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f && e.posY <= posY + dY);
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

    private void onDeathUpdate(int deathDuration) {
        onDeathAIUpdate();

        attackingPlayer = killDataAttackingPlayer;
        ++this.deathTime;
        if (this.deathTime == deathDuration) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
                if (dropAfterDeathAnim && getLastDamageSource() != null) spawnDrops(getLastDamageSource());
                int i = this.getExperiencePoints(this.attackingPlayer);

                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while(i > 0) {
                    int j = ExperienceOrbEntity.getXPSplit(i);
                    i -= j;
                    this.world.addEntity(new ExperienceOrbEntity(this.world, this.posX, this.posY, this.posZ, j));
                }
            }

            this.remove(false);

            for(int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.posX + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.posY + (double)(this.rand.nextFloat() * this.getHeight()), this.posZ + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), d2, d0, d1);
            }
        }
    }

    protected void onDeathAIUpdate() {}

    @Override
    protected final void onDeathUpdate() {}

    @Override
    public void onDeath(DamageSource cause)
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.dead) {
            Entity entity = cause.getTrueSource();
            LivingEntity livingentity = this.getAttackingEntity();
            if (this.scoreValue >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.scoreValue, cause);
            }

            if (entity != null) {
                entity.onKillEntity(this);
            }

            if (this.isSleeping()) {
                this.wakeUp();
            }

            this.dead = true;
            this.getCombatTracker().reset();
            if (!this.world.isRemote) {
                if (!dropAfterDeathAnim) this.spawnDrops(cause);
                boolean flag = false;
                if (livingentity instanceof WitherEntity) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
                        BlockState blockstate = Blocks.WITHER_ROSE.getDefaultState();
                        if (this.world.getBlockState(blockpos).isAir() && blockstate.isValidPosition(this.world, blockpos)) {
                            this.world.setBlockState(blockpos, blockstate, 3);
                            flag = true;
                        }
                    }

                    if (!flag) {
                        ItemEntity itementity = new ItemEntity(this.world, this.posX, this.posY, this.posZ, new ItemStack(Items.WITHER_ROSE));
                        this.world.addEntity(itementity);
                    }
                }
            }

            this.world.setEntityState(this, (byte)3);
            this.setPose(Pose.DYING);
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
        this.getNavigator().tryMoveToXYZ(target.posX + radius * Math.cos(t), target.posY, target.posZ + radius * Math.sin(t), speed * moveSpeedMultiplier);
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
}
