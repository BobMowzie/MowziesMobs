package com.bobmowzie.mowziesmobs.server.entity.wroughtnaut;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.WroughtnautAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.SmartBodyHelper;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class EntityWroughtnaut extends MowzieEntity implements Enemy {
    public static final Animation DIE_ANIMATION = Animation.create(130);

    public static final Animation HURT_ANIMATION = Animation.create(15);

    public static final Animation ATTACK_ANIMATION = Animation.create(50);

    public static final Animation ATTACK_TWICE_ANIMATION = Animation.create(36);

    public static final Animation ATTACK_THRICE_ANIMATION = Animation.create(59);

    public static final Animation VERTICAL_ATTACK_ANIMATION = Animation.create(105);

    public static final Animation STOMP_ATTACK_ANIMATION = Animation.create(40);

    public static final Animation ACTIVATE_ANIMATION = Animation.create(45);

    public static final Animation DEACTIVATE_ANIMATION = Animation.create(15);

    private static final Animation[] ANIMATIONS = {
        DIE_ANIMATION,
        HURT_ANIMATION,
        ATTACK_ANIMATION,
        ATTACK_TWICE_ANIMATION,
        ATTACK_THRICE_ANIMATION,
        VERTICAL_ATTACK_ANIMATION,
        STOMP_ATTACK_ANIMATION,
        ACTIVATE_ANIMATION,
        DEACTIVATE_ANIMATION
    };

    private static final float[][] VERTICAL_ATTACK_BLOCK_OFFSETS = {
        {-0.1F, -0.1F},
        {-0.1F, 0.1F},
        {0.1F, 0.1F},
        {0.1F, -0.1F}
    };

    private static final EntityDataAccessor<Optional<BlockPos>> REST_POSITION = SynchedEntityData.defineId(EntityWroughtnaut.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(EntityWroughtnaut.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityWroughtnaut.class, EntityDataSerializers.BOOLEAN);

    public ControlledAnimation walkAnim = new ControlledAnimation(10);

    public boolean swingDirection;

    public boolean vulnerable;

    private CeilingDisturbance disturbance;

    public Vec3 leftEyePos, rightEyePos;
    public Vec3 leftEyeRot, rightEyeRot;

    public EntityWroughtnaut(EntityType<? extends EntityWroughtnaut> type, Level world) {
        super(type, world);
        xpReward = 30;
        active = false;
        maxUpStep = 1;
//        rightEyePos = new Vector3d(0, 0, 0);
//        leftEyePos = new Vector3d(0, 0, 0);
//        rightEyeRot = new Vector3d(0, 0, 0);
//        leftEyeRot = new Vector3d(0, 0, 0);

        dropAfterDeathAnim = true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new AnimationFWNAttackAI(this, 4F, 5F, 100F));
        goalSelector.addGoal(1, new AnimationFWNVerticalAttackAI(this, VERTICAL_ATTACK_ANIMATION, MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 1F, 5F, 40F));
        goalSelector.addGoal(1, new AnimationFWNStompAttackAI(this, STOMP_ATTACK_ANIMATION));
        goalSelector.addGoal(1, new AnimationTakeDamage<>(this));
        goalSelector.addGoal(1, new AnimationDieAI<>(this));
        goalSelector.addGoal(1, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        goalSelector.addGoal(1, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        goalSelector.addGoal(2, new WroughtnautAttackAI(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.98F;
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_WROUGHT_HURT_1.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        playSound(MMSounds.ENTITY_WROUGHT_SCREAM.get(), 1f, 1f);
        return null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getAnimation() == NO_ANIMATION && isActive() ? MMSounds.ENTITY_WROUGHT_AMBIENT.get() : null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entitySource = source.getEntity();
        if (entitySource != null) {
            if ((!active || getTarget() == null) && entitySource instanceof LivingEntity && !(entitySource instanceof Player && ((Player) entitySource).isCreative()) && !(entitySource instanceof EntityWroughtnaut)) setTarget((LivingEntity) entitySource);
            if (vulnerable) {
                int arc = 220;
                float entityHitAngle = (float) ((Math.atan2(entitySource.getZ() - getZ(), entitySource.getX() - getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if ((entityRelativeAngle <= arc / 2f && entityRelativeAngle >= -arc / 2f) || (entityRelativeAngle >= 360 - arc / 2f || entityRelativeAngle <= -arc + 90f / 2f)) {
                    playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
                    return false;
                } else {
                    setAnimation(NO_ANIMATION);
                    return super.hurt(source, amount);
                }
            } else {
                playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
            }
        }
        else if (source.isBypassInvul()) {
            return super.hurt(source, amount);
        }
        return false;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        walkAnim.updatePrevTimer();

//        if (getAnimation() == NO_ANIMATION) {
//            setActive(true);
//            swingDirection = true;
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, VERTICAL_ATTACK_ANIMATION);
//            getNavigator().clearPath();
//        }

        if (getTarget() != null && (!getTarget().isAlive() || getTarget().getHealth() <= 0)) setTarget(null);

        if (!level.isClientSide) {
            if (isAlwaysActive()) {
                setActive(true);
                active = true;
            }
            else if (getAnimation() == NO_ANIMATION && !isNoAi()) {
                if (isActive()) {
                    if (getTarget() == null && zza == 0 && isAtRestPos()) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        setActive(false);
                    }
                } else if (getTarget() != null && targetDistance <= 4.5) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    setActive(true);
                }
            }
        }

        if (!isActive()) {
//            posX = prevPosX;
//            posZ = prevPosZ;
            setDeltaMovement(0, getDeltaMovement().y, 0);
            setYRot(yRotO);
        }
//        else if (world.isRemote && leftEyePos != null && rightEyePos != null) {
//            MowzieParticleBase.spawnParticle(world, MMParticle.EYE, leftEyePos.x, leftEyePos.y, leftEyePos.z, 0, 0, 0, false, leftEyeRot.y + 1.5708, 0, 0, 0, 5f, 0.8f, 0.1f, 0.1f, 1f, 1, 10, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)});
//            MowzieParticleBase.spawnParticle(world, MMParticle.EYE, rightEyePos.x, rightEyePos.y, rightEyePos.z, 0, 0, 0, false, rightEyeRot.y, 0, 0, 0, 5f, 0.8f, 0.1f, 0.1f, 1f, 1, 10, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)});
//        }
        if (getAnimation() != NO_ANIMATION || !isActive()) {
            yHeadRot = yBodyRot = getYRot();
        }

        if (!isAlwaysActive() && getTarget() == null && getNavigation().isDone() && !isAtRestPos() && isActive()) updateRestPos();

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 1) {
            swingDirection = random.nextBoolean();
        } else if (getAnimation() == ACTIVATE_ANIMATION) {
            int tick = getAnimationTick();
            if (tick == 1) {
                playSound(MMSounds.ENTITY_WROUGHT_GRUNT_2.get(), 1, 1);
            } else if (tick == 27 || tick == 44) {
                playSound(MMSounds.ENTITY_WROUGHT_STEP.get(), 0.5F, 0.5F);
            }
        } else if (getAnimation() == VERTICAL_ATTACK_ANIMATION && getAnimationTick() == 29) {
            doVerticalAttackHitFX();
        } else if (getAnimation() == STOMP_ATTACK_ANIMATION) {
            doStompFX();
        }

        float moveX = (float) (getX() - xo);
        float moveZ = (float) (getZ() - zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (speed > 0.01) {
            if (getAnimation() == NO_ANIMATION) {
                walkAnim.increaseTimer();
            }
        } else {
            walkAnim.decreaseTimer();
        }
        if (getAnimation() != NO_ANIMATION) {
            walkAnim.decreaseTimer(2);
        }

        if (this.level.isClientSide && frame % 20 == 1 && speed > 0.03 && getAnimation() == NO_ANIMATION && isActive()) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), MMSounds.ENTITY_WROUGHT_STEP.get(), this.getSoundSource(), 0.5F, 0.5F, false);
        }

        repelEntities(1.7F, 4, 1.7F, 1.7F);

        if (!active && !level.isClientSide && getAnimation() != ACTIVATE_ANIMATION) {
            if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.healsOutOfBattle.get()) heal(0.3f);
        }

        if (disturbance != null) {
            if (disturbance.update()) {
                disturbance = null;
            }
        }

//        if (!this.world.isRemote) {
//            Path path = this.getNavigator().getPath();
//            if (path != null) {
//                for (int i = 0; i < path.getCurrentPathLength(); i++) {
//                    Vector3d p = path.getVectorFromIndex(this, i);
//                    ((WorldServer) this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, p.x, p.y + 0.1D, p.z, 1, 0.1D, 0.0D, 0.1D, 0.01D, Block.getIdFromBlock(i < path.getCurrentPathIndex() ? Blocks.GOLD_BLOCK : i == path.getCurrentPathIndex() ? Blocks.DIAMOND_BLOCK : Blocks.DIRT));
//                }
//            }
//        }
    }

    @Override
    protected void repelEntities(float x, float y, float z, float radius) {
        super.repelEntities(x, y, z, radius);
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    private boolean isAtRestPos() {
        Optional<BlockPos> restPos = getRestPos();
        if (restPos.isPresent()) {
            return restPos.get().distSqr(blockPosition()) < 36;
        }
        return false;
    }

    private void updateRestPos() {
        boolean reassign = true;
        if (getRestPos().isPresent()) {
            BlockPos pos = getRestPos().get();
            if (getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 0.2)) {
                reassign = false;
            }
        }
        if (reassign) {
            setRestPos(blockPosition());
        }
    }

    private void doVerticalAttackHitFX() {
        double theta = (yBodyRot - 4) * (Math.PI / 180);
        double perpX = Math.cos(theta);
        double perpZ = Math.sin(theta);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double x = getX() + 4.2 * vecX;
        double y = getBoundingBox().minY + 0.1;
        double z = getZ() + 4.2 * vecZ;
        int hitY = Mth.floor(getY() - 0.2);
        for (int t = 0; t < VERTICAL_ATTACK_BLOCK_OFFSETS.length; t++) {
            float ox = VERTICAL_ATTACK_BLOCK_OFFSETS[t][0], oy = VERTICAL_ATTACK_BLOCK_OFFSETS[t][1];
            int hitX = Mth.floor(x + ox);
            int hitZ = Mth.floor(z + oy);
            BlockPos hit = new BlockPos(hitX, hitY, hitZ);
            BlockState block = level.getBlockState(hit);
            if (block.getRenderShape() != RenderShape.INVISIBLE) {
                for (int n = 0; n < 6; n++) {
                    double pa = random.nextDouble() * 2 * Math.PI;
                    double pd = random.nextDouble() * 0.6 + 0.1;
                    double px = x + Math.cos(pa) * pd;
                    double pz = z + Math.sin(pa) * pd;
                    double magnitude = random.nextDouble() * 4 + 5;
                    double velX = perpX * magnitude;
                    double velY = random.nextDouble() * 3 + 6;
                    double velZ = perpZ * magnitude;
                    if (vecX * (pz - getZ()) - vecZ * (px - getX()) > 0) {
                        velX = -velX;
                        velZ = -velZ;
                    }
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), px, y, pz, velX, velY, velZ);
                }
            }
        }
        int hitX = Mth.floor(x);
        int ceilY = Mth.floor(getBoundingBox().maxY);
        int hitZ = Mth.floor(z);
        final int maxHeight = 5;
        int height = maxHeight;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (; height --> 0; ceilY++) {
            pos.set(hitX, ceilY, hitZ);
            if (level.getBlockState(pos).getMaterial().blocksMotion()) {
                break;
            }
        }
        float strength = height / (float) maxHeight;
        if (strength >= 0) {
            int radius = Mth.ceil(Mth.sqrt(1 - strength * strength) * maxHeight);
            disturbance = new CeilingDisturbance(hitX, ceilY, hitZ, radius, random.nextInt(5) + 3, random.nextInt(60) + 20);
        }
    }

    private void doStompFX() {
        double perpFacing = yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int tick = getAnimationTick();
        final int maxDistance = 6;
        if (tick > 9 && tick < 17) {
            if (tick == 12) {
                final double infront = 1.47, side = -0.21;
                double vx = Math.cos(facingAngle) * infront;
                double vz = Math.sin(facingAngle) * infront;
                double perpX = Math.cos(perpFacing);
                double perpZ = Math.sin(perpFacing);
                double fx = getX() + vx + perpX * side;
                double fy = getBoundingBox().minY + 0.1;
                double fz = getZ() + vz + perpZ * side;
                int amount = 16 + level.random.nextInt(8);
                while (amount-- > 0) {
                    double theta = level.random.nextDouble() * Math.PI * 2;
                    double dist = level.random.nextDouble() * 0.1 + 0.25;
                    double sx = Math.cos(theta);
                    double sz = Math.sin(theta);
                    double px = fx + sx * dist;
                    double py = fy + level.random.nextDouble() * 0.1;
                    double pz = fz + sz * dist;
                    level.addParticle(ParticleTypes.SMOKE, px, py, pz, sx * 0.065, 0, sz * 0.065);
                }
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = Mth.ceil(distance * spread);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = getX() + vx * distance;
                    double pz = getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    if (level.random.nextBoolean()) {
                        int amount = level.random.nextInt(5);
                        while (amount-- > 0) {
                            double velX = vx * 0.075;
                            double velY = factor * 0.3 + 0.025;
                            double velZ = vz * 0.075;
                            level.addParticle(ParticleTypes.CLOUD, px + level.random.nextFloat() * 2 - 1, getBoundingBox().minY + 0.1 + level.random.nextFloat() * 1.5, pz + level.random.nextFloat() * 2 - 1, velX, velY, velZ);
                        }
                    }
                }
            }
        }
    }

    private class CeilingDisturbance {
        private final int ceilX, ceilY, ceilZ;

        private final int radius;

        private int delay;

        private int remainingTicks;

        private final int duration;

        public CeilingDisturbance(int x, int y, int z, int radius, int delay, int remainingTicks) {
            this.ceilX = x;
            this.ceilY = y;
            this.ceilZ = z;
            this.radius = radius;
            this.delay = delay;
            this.remainingTicks = remainingTicks;
            duration = remainingTicks;
        }

        public boolean update() {
            if (--delay > 0) {
                return false;
            }
            float t = remainingTicks / (float) duration;
            int amount = Mth.ceil((1 - Mth.sqrt(1 - t * t)) * radius * radius * 0.15F);
            boolean playSound = true;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            while (amount --> 0) {
                double theta = random.nextDouble() * Math.PI * 2;
                double dist = random.nextDouble() * radius;
                double x = ceilX + Math.cos(theta) * dist;
                double y = ceilY - 0.1 - random.nextDouble() * 0.3;
                double z = ceilZ + Math.sin(theta) * dist;
                int blockX = Mth.floor(x);
                int blockZ = Mth.floor(z);
                pos.set(blockX, ceilY, blockZ);
                BlockState block = level.getBlockState(pos);
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, block), x, y, z, 0, 0, 0);
                    if (playSound && random.nextFloat() < 0.075F) {
                        SoundType sound = block.getBlock().getSoundType(block, level, pos, null);
                        level.playLocalSound(getX(), getY(), getZ(), sound.getBreakSound(), SoundSource.BLOCKS, sound.getVolume() * 2, sound.getPitch() * 0.6F, false);
                        playSound = false;
                    }
                }
            }
            return --remainingTicks <= 0;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag compound) {
        setRestPos(blockPosition());
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(REST_POSITION, Optional.empty());
        getEntityData().define(ACTIVE, false);
        getEntityData().define(ALWAYS_ACTIVE, false);
    }

    public Optional<BlockPos> getRestPos() {
        return getEntityData().get(REST_POSITION);
    }

    public void setRestPos(BlockPos pos) {
        getEntityData().set(REST_POSITION, Optional.of(pos));
    }

    public boolean isActive() {
        return getEntityData().get(ACTIVE);
    }

    public void setActive(boolean isActive) {
        getEntityData().set(ACTIVE, isActive);
    }

    public boolean isAlwaysActive() {
        return getEntityData().get(ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean isAlwaysActive) {
        getEntityData().set(ALWAYS_ACTIVE, isAlwaysActive);
        if (isAlwaysActive) {
            this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.2D));
            this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buf) {
        super.writeSpawnData(buf);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        Optional<BlockPos> restPos = getRestPos();
        if (restPos.isPresent()) {
            compound.put("restPos", NbtUtils.writeBlockPos(getRestPos().get()));
        }
        compound.putBoolean("active", isActive());
        compound.putBoolean("alwaysActive", isAlwaysActive());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("restPos")) {
            setRestPos(NbtUtils.readBlockPos(compound.getCompound("restPos")));
        }
        setActive(compound.getBoolean("active"));
        active = isActive();
        setAlwaysActive(compound.getBoolean("alwaysActive"));
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.combatConfig;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LootTableHandler.FERROUS_WROUGHTNAUT;
    }

    @Override
    protected boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.hasBossBar.get();
    }

    @Override
    protected BossEvent.BossBarColor bossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    @Override
    public SoundEvent getBossMusic() {
        return MMSounds.MUSIC_FERROUS_WROUGHTNAUT_THEME.get();
    }

    @Override
    protected boolean canPlayMusic() {
        return super.canPlayMusic() && (active || getAnimation() == ACTIVATE_ANIMATION);
    }
}