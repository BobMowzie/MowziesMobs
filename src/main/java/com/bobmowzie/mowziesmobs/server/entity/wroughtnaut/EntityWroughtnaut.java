package com.bobmowzie.mowziesmobs.server.entity.wroughtnaut;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.WroughtnautAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationActivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDeactivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationFWNAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationFWNStompAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationFWNVerticalAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.SmartBodyHelper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EntityWroughtnaut extends MowzieEntity implements IMob {
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

    private static final DataParameter<Optional<BlockPos>> REST_POSITION = EntityDataManager.createKey(EntityWroughtnaut.class, DataSerializers.OPTIONAL_BLOCK_POS);

    private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityWroughtnaut.class, DataSerializers.BOOLEAN);

    public ControlledAnimation walkAnim = new ControlledAnimation(10);

    public boolean swingDirection;

    public boolean vulnerable;

    private CeilingDisturbance disturbance;

    public Vector3d leftEyePos, rightEyePos;
    public Vector3d leftEyeRot, rightEyeRot;

    public EntityWroughtnaut(EntityType<? extends EntityWroughtnaut> type, World world) {
        super(type, world);
        experienceValue = 30;
        active = false;
        stepHeight = 1;
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
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, null));

    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.98F;
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected BodyController createBodyController() {
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

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MowzieEntity.createAttributes().createMutableAttribute(Attributes.ATTACK_DAMAGE, 30)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40 * ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.combatConfig.healthMultiplier.get())
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entitySource = source.getTrueSource();
        if (entitySource != null) {
            if ((!active || getAttackTarget() == null) && entitySource instanceof LivingEntity && !(entitySource instanceof PlayerEntity && ((PlayerEntity) entitySource).isCreative()) && !(entitySource instanceof EntityWroughtnaut)) setAttackTarget((LivingEntity) entitySource);
            if (vulnerable) {
                int arc = 220;
                float entityHitAngle = (float) ((Math.atan2(entitySource.getPosZ() - getPosZ(), entitySource.getPosX() - getPosX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = renderYawOffset % 360;
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
                    return super.attackEntityFrom(source, amount);
                }
            } else {
                playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
            }
        }
        else if (source.canHarmInCreative()) {
            return super.attackEntityFrom(source, amount);
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
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

//        if (getAnimation() == NO_ANIMATION) {
//            setActive(true);
//            swingDirection = true;
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, STOMP_ATTACK_ANIMATION);
//            getNavigator().clearPath();
//        }

        if (getAttackTarget() != null && (!getAttackTarget().isAlive() || getAttackTarget().getHealth() <= 0)) setAttackTarget(null);

        if (!world.isRemote) {
            if (getAnimation() == NO_ANIMATION && !isAIDisabled()) {
                if (isActive()) {
                    if (getAttackTarget() == null && moveForward == 0 && isAtRestPos()) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        setActive(false);
                    }
                } else if (getAttackTarget() != null && targetDistance <= 4.5) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    setActive(true);
                }
            }
        }

        if (!isActive()) {
//            posX = prevPosX;
//            posZ = prevPosZ;
            setMotion(0, getMotion().y, 0);
            rotationYaw = prevRotationYaw;
        }
//        else if (world.isRemote && leftEyePos != null && rightEyePos != null) {
//            MowzieParticleBase.spawnParticle(world, MMParticle.EYE, leftEyePos.x, leftEyePos.y, leftEyePos.z, 0, 0, 0, false, leftEyeRot.y + 1.5708, 0, 0, 0, 5f, 0.8f, 0.1f, 0.1f, 1f, 1, 10, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)});
//            MowzieParticleBase.spawnParticle(world, MMParticle.EYE, rightEyePos.x, rightEyePos.y, rightEyePos.z, 0, 0, 0, false, rightEyeRot.y, 0, 0, 0, 5f, 0.8f, 0.1f, 0.1f, 1f, 1, 10, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)});
//        }
        renderYawOffset = rotationYaw;

        if (getAttackTarget() == null && getNavigator().noPath() && !isAtRestPos() && isActive()) updateRestPos();

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 1) {
            swingDirection = rand.nextBoolean();
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

        float moveX = (float) (getPosX() - prevPosX);
        float moveZ = (float) (getPosZ() - prevPosZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
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

        if (this.world.isRemote && frame % 20 == 1 && speed > 0.03 && getAnimation() == NO_ANIMATION && isActive()) {
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), MMSounds.ENTITY_WROUGHT_STEP.get(), this.getSoundCategory(), 0.5F, 0.5F, false);
        }

        repelEntities(1.7F, 4, 1.7F, 1.7F);

        if (!active && !world.isRemote && getAnimation() != ACTIVATE_ANIMATION) {
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
            return restPos.get().distanceSq(getPosition()) < 36;
        }
        return false;
    }

    private void updateRestPos() {
        boolean reassign = true;
        if (getRestPos().isPresent()) {
            BlockPos pos = getRestPos().get();
            if (getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 0.2)) {
                reassign = false;
            }
        }
        if (reassign) {
            setRestPos(getPosition());
        }
    }

    private void doVerticalAttackHitFX() {
        double theta = (renderYawOffset - 4) * (Math.PI / 180);
        double perpX = Math.cos(theta);
        double perpZ = Math.sin(theta);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double x = getPosX() + 4.2 * vecX;
        double y = getBoundingBox().minY + 0.1;
        double z = getPosZ() + 4.2 * vecZ;
        int hitY = MathHelper.floor(getPosY() - 0.2);
        for (int t = 0; t < VERTICAL_ATTACK_BLOCK_OFFSETS.length; t++) {
            float ox = VERTICAL_ATTACK_BLOCK_OFFSETS[t][0], oy = VERTICAL_ATTACK_BLOCK_OFFSETS[t][1];
            int hitX = MathHelper.floor(x + ox);
            int hitZ = MathHelper.floor(z + oy);
            BlockPos hit = new BlockPos(hitX, hitY, hitZ);
            BlockState block = world.getBlockState(hit);
            if (block.getRenderType() != BlockRenderType.INVISIBLE) {
                for (int n = 0; n < 6; n++) {
                    double pa = rand.nextDouble() * 2 * Math.PI;
                    double pd = rand.nextDouble() * 0.6 + 0.1;
                    double px = x + Math.cos(pa) * pd;
                    double pz = z + Math.sin(pa) * pd;
                    double magnitude = rand.nextDouble() * 4 + 5;
                    double velX = perpX * magnitude;
                    double velY = rand.nextDouble() * 3 + 6;
                    double velZ = perpZ * magnitude;
                    if (vecX * (pz - getPosZ()) - vecZ * (px - getPosX()) > 0) {
                        velX = -velX;
                        velZ = -velZ;
                    }
                    world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), px, y, pz, velX, velY, velZ);
                }
            }
        }
        int hitX = MathHelper.floor(x);
        int ceilY = MathHelper.floor(getBoundingBox().maxY);
        int hitZ = MathHelper.floor(z);
        final int maxHeight = 5;
        int height = maxHeight;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (; height --> 0; ceilY++) {
            pos.setPos(hitX, ceilY, hitZ);
            if (world.getBlockState(pos).getMaterial().blocksMovement()) {
                break;
            }
        }
        float strength = height / (float) maxHeight;
        if (strength >= 0) {
            int radius = MathHelper.ceil(MathHelper.sqrt(1 - strength * strength) * maxHeight);
            disturbance = new CeilingDisturbance(hitX, ceilY, hitZ, radius, rand.nextInt(5) + 3, rand.nextInt(60) + 20);
        }
    }

    private void doStompFX() {
        double perpFacing = renderYawOffset * (Math.PI / 180);
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
                double fx = getPosX() + vx + perpX * side;
                double fy = getBoundingBox().minY + 0.1;
                double fz = getPosZ() + vz + perpZ * side;
                int amount = 16 + world.rand.nextInt(8);
                while (amount-- > 0) {
                    double theta = world.rand.nextDouble() * Math.PI * 2;
                    double dist = world.rand.nextDouble() * 0.1 + 0.25;
                    double sx = Math.cos(theta);
                    double sz = Math.sin(theta);
                    double px = fx + sx * dist;
                    double py = fy + world.rand.nextDouble() * 0.1;
                    double pz = fz + sz * dist;
                    world.addParticle(ParticleTypes.SMOKE, px, py, pz, sx * 0.065, 0, sz * 0.065);
                }
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(distance * spread);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = getPosX() + vx * distance;
                    double pz = getPosZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    if (world.rand.nextBoolean()) {
                        int amount = world.rand.nextInt(5);
                        while (amount-- > 0) {
                            double velX = vx * 0.075;
                            double velY = factor * 0.3 + 0.025;
                            double velZ = vz * 0.075;
                            world.addParticle(ParticleTypes.CLOUD, px + world.rand.nextFloat() * 2 - 1, getBoundingBox().minY + 0.1 + world.rand.nextFloat() * 1.5, pz + world.rand.nextFloat() * 2 - 1, velX, velY, velZ);
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
            int amount = MathHelper.ceil((1 - MathHelper.sqrt(1 - t * t)) * radius * radius * 0.15F);
            boolean playSound = true;
            BlockPos.Mutable pos = new BlockPos.Mutable();
            while (amount --> 0) {
                double theta = rand.nextDouble() * Math.PI * 2;
                double dist = rand.nextDouble() * radius;
                double x = ceilX + Math.cos(theta) * dist;
                double y = ceilY - 0.1 - rand.nextDouble() * 0.3;
                double z = ceilZ + Math.sin(theta) * dist;
                int blockX = MathHelper.floor(x);
                int blockZ = MathHelper.floor(z);
                pos.setPos(blockX, ceilY, blockZ);
                BlockState block = world.getBlockState(pos);
                if (block.getRenderType() != BlockRenderType.INVISIBLE) {
                    world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, block), x, y, z, 0, 0, 0);
                    if (playSound && rand.nextFloat() < 0.075F) {
                        SoundType sound = block.getBlock().getSoundType(block, world, pos, null);
                        world.playSound(getPosX(), getPosY(), getPosZ(), sound.getBreakSound(), SoundCategory.BLOCKS, sound.getVolume() * 2, sound.getPitch() * 0.6F, false);
                        playSound = false;
                    }
                }
            }
            return --remainingTicks <= 0;
        }
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
        setRestPos(getPosition());
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean preventDespawn() {
        return true;
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(REST_POSITION, Optional.empty());
        getDataManager().register(ACTIVE, false);
    }

    public Optional<BlockPos> getRestPos() {
        return getDataManager().get(REST_POSITION);
    }

    public void setRestPos(BlockPos pos) {
        getDataManager().set(REST_POSITION, Optional.of(pos));
    }

    public boolean isActive() {
        return getDataManager().get(ACTIVE);
    }

    public void setActive(boolean isActive) {
        getDataManager().set(ACTIVE, isActive);
    }

    @Override
    public void writeSpawnData(PacketBuffer buf) {
        super.writeSpawnData(buf);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        Optional<BlockPos> restPos = getRestPos();
        if (restPos.isPresent()) {
            compound.put("restPos", NBTUtil.writeBlockPos(getRestPos().get()));
        }
        compound.putBoolean("active", isActive());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("restPos", NBT.TAG_COMPOUND)) {
            setRestPos(NBTUtil.readBlockPos(compound.getCompound("restPos")));
        }
        setActive(compound.getBoolean("active"));
        active = isActive();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.FERROUS_WROUGHTNAUT;
    }

    @Override
    protected boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.hasBossBar.get();
    }

    @Override
    protected BossInfo.Color bossBarColor() {
        return BossInfo.Color.RED;
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