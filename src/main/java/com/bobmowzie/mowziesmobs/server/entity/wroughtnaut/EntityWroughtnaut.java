package com.bobmowzie.mowziesmobs.server.entity.wroughtnaut;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationActivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDeactivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationFWNAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationFWNStompAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationFWNVerticalAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.SmartBodyHelper;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.base.Optional;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWroughtnaut extends MowzieEntity {
    public static final Animation DIE_ANIMATION = Animation.create(130);

    public static final Animation HURT_ANIMATION = Animation.create(15);

    public static final Animation ATTACK_ANIMATION = Animation.create(50);

    public static final Animation ATTACK_TWICE_ANIMATION = Animation.create(76);

    public static final Animation ATTACK_THRICE_ANIMATION = Animation.create(125);

    public static final Animation VERTICAL_ATTACK_ANIMATION = Animation.create(105);

    public static final Animation STOMP_ATTACK_ANIMATION = Animation.create(40);

    public static final Animation ACTIVATE_ANIMATION = Animation.create(45);

    public static final Animation DEACTIVATE_ANIMATION = Animation.create(15);

    public static final Animation DAB_ANIMATION = Animation.create(18);

    private static final Animation[] ANIMATIONS = {
        DIE_ANIMATION,
        HURT_ANIMATION,
        ATTACK_ANIMATION,
        ATTACK_TWICE_ANIMATION,
        ATTACK_THRICE_ANIMATION,
        VERTICAL_ATTACK_ANIMATION,
        STOMP_ATTACK_ANIMATION,
        DAB_ANIMATION,
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

    private int attacksWithoutVertical;

    private int ticksSinceLastStomp;

    private CeilingDisturbance disturbance;

    @SideOnly(Side.CLIENT)
    public Vec3d leftEyePos, rightEyePos;

    public EntityWroughtnaut(World world) {
        super(world);
        setPathPriority(PathNodeType.WATER, 0);
        tasks.addTask(1, new AnimationFWNAttackAI(this, ATTACK_ANIMATION, MMSounds.ENTITY_WROUGHT_WHOOSH, 4F, 5.5F, 100F, 1));
        tasks.addTask(1, new AnimationFWNAttackAI(this, ATTACK_TWICE_ANIMATION, MMSounds.ENTITY_WROUGHT_WHOOSH, 4F, 5.5F, 100F, 2));
        tasks.addTask(1, new AnimationFWNAttackAI(this, ATTACK_THRICE_ANIMATION, MMSounds.ENTITY_WROUGHT_WHOOSH, 4F, 5.5F, 100F, 3));
        tasks.addTask(1, new AnimationFWNVerticalAttackAI(this, VERTICAL_ATTACK_ANIMATION, MMSounds.ENTITY_WROUGHT_WHOOSH, 1F, 5.5F, 40F));
        tasks.addTask(1, new AnimationFWNStompAttackAI(this, STOMP_ATTACK_ANIMATION));
        tasks.addTask(1, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(1, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        tasks.addTask(1, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        tasks.addTask(1, new AnimationAI<EntityWroughtnaut>(this, DAB_ANIMATION) {
            {
                setMutexBits(8);
            }

            @Override
            public void updateTask() {
                entity.motionX = 0;
                entity.motionZ = 0;
            }
        });
        tasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 0, true, false, null));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1, true));
        experienceValue = 30;
        setSize(2.45F, 3.7F);
        active = false;
        stepHeight = 1;
        rightEyePos = new Vec3d(0, 0, 0);
        leftEyePos = new Vec3d(0, 0, 0);
    }

    @Override
    protected PathNavigate createNavigator(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected SmartBodyHelper createBodyHelper() {
        return new SmartBodyHelper(this);
    }

    @Override
    public int getAttack() {
        return (int)(30 * MowziesMobs.CONFIG.attackScaleWroughtnaut);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_WROUGHT_HURT_1;
    }

    @Override
    public SoundEvent getDeathSound() {
        playSound(MMSounds.ENTITY_WROUGHT_SCREAM, 1f, 1f);
        return null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getAnimation() == NO_ANIMATION && isActive() ? MMSounds.ENTITY_WROUGHT_AMBIENT : null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40 * MowziesMobs.CONFIG.healthScaleWroughtnaut);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entitySource = source.getTrueSource();
        if (entitySource != null) {
            if ((!active || getAttackTarget() == null) && entitySource instanceof EntityLivingBase && !(entitySource instanceof EntityPlayer && ((EntityPlayer) entitySource).capabilities.isCreativeMode) && !(entitySource instanceof EntityWroughtnaut)) setAttackTarget((EntityLivingBase) entitySource);
            if (vulnerable) {
                int arc = 220;
                float entityHitAngle = (float) ((Math.atan2(entitySource.posZ - posZ, entitySource.posX - posX) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = renderYawOffset % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if ((entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -arc + 90 / 2)) {
                    playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.4F, 2);
                    return false;
                } else {
                    if (currentAnim != null) {
                        currentAnim.resetTask();
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);
                    }
                    return super.attackEntityFrom(source, amount);
                }
            } else {
                playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.4F, 2);
            }
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
    public void onUpdate() {
        super.onUpdate();

//        if (getAnimation() == NO_ANIMATION) {
//            setActive(true);
//            swingDirection = false;
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_THRICE_ANIMATION);
//        }

        if (getAttackTarget() != null && (getAttackTarget().isDead || getAttackTarget().getHealth() <= 0)) setAttackTarget(null);

        if (!world.isRemote) {
            if (getAnimation() == NO_ANIMATION && !isAIDisabled()) {
                if (isActive()) {
                    if (getAttackTarget() == null && moveForward == 0 && isAtRestPos()) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        setActive(false);
                    }
                } else if (getAttackTarget() != null && targetDistance <= 5) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    setActive(true);
                }
            }
            ticksSinceLastStomp++;
        }

        if (!isActive()) {
            posX = prevPosX;
            posZ = prevPosZ;
            rotationYaw = prevRotationYaw;
        }
        else if (world.isRemote) {
//            MMParticle.ORB.spawn(world, leftEyePos.x, leftEyePos.y, leftEyePos.z, ParticleFactory.ParticleArgs.get().withData(0d, 0d, 0d, 247d / 256d, 94d / 256d, 74d / 256d, 1d, 25));
//            MMParticle.ORB.spawn(world, rightEyePos.x, rightEyePos.y, rightEyePos.z, ParticleFactory.ParticleArgs.get().withData(0d, 0d, 0d, 247d / 256d, 94d / 256d, 74d / 256d, 1d, 25));
        }
        renderYawOffset = rotationYaw;

        if (getAttackTarget() != null && isActive()) {
            if (getAnimation() == NO_ANIMATION) {
                getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.2);
            } else {
                getNavigator().clearPathEntity();
            }
            if (getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 3 && getAnimation() == NO_ANIMATION && !isAIDisabled()) {
                boolean couldStomp = targetDistance < 6 && ticksSinceLastStomp > 600;
                if (targetDistance < 3.5 && Math.abs(MathHelper.wrapDegrees(getAngleBetweenEntities(getAttackTarget(), this) - rotationYaw)) < 35 && (!couldStomp || rand.nextInt(3) > 0)) {
                    if (attacksWithoutVertical >= 4 || rand.nextInt(4) == 0) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, VERTICAL_ATTACK_ANIMATION);
                        attacksWithoutVertical = 0;
                    } else {
                        if (getHealth()/getMaxHealth() <= 0.6 && rand.nextInt(2) == 0) {
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_THRICE_ANIMATION);
                            attacksWithoutVertical += 3;
                        }
                        else if (getHealth()/getMaxHealth() <= 0.9 && rand.nextInt(2) == 0) {
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_TWICE_ANIMATION);
                            attacksWithoutVertical += 2;
                        }
                        else {
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
                            attacksWithoutVertical += 1;
                        }
                    }
                } else if (couldStomp) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, STOMP_ATTACK_ANIMATION);
                    ticksSinceLastStomp = 0;
                    attacksWithoutVertical++;
                }
            }
        } else {
            updateRestPos();
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 1) {
            swingDirection = rand.nextBoolean();
        } else if (getAnimation() == ACTIVATE_ANIMATION) {
            int tick = getAnimationTick();
            if (tick == 1) {
                playSound(MMSounds.ENTITY_WROUGHT_GRUNT_2, 1, 1);
            } else if (tick == 27 || tick == 44) {
                playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.5F, 0.5F);
            }
        } else if (getAnimation() == VERTICAL_ATTACK_ANIMATION && getAnimationTick() == 29) {
            doVerticalAttackHitFX();
        }

        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
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

        if (frame % 20 == 5 && speed > 0.03 && getAnimation() == NO_ANIMATION && active) {
            playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.5F, 0.5F);
        }

        repelEntities(2.2F, 4, 2.2F, 2.2F);

        if (!active) {
            addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, 2, true, true));
        }

        if (disturbance != null) {
            if (disturbance.update()) {
                disturbance = null;
            }
        }
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
        double x = posX + 4.2 * vecX;
        double y = getEntityBoundingBox().minY + 0.1;
        double z = posZ + 4.2 * vecZ;
        int hitY = MathHelper.floor(posY - 0.2);
        for (int t = 0; t < VERTICAL_ATTACK_BLOCK_OFFSETS.length; t++) {
            float ox = VERTICAL_ATTACK_BLOCK_OFFSETS[t][0], oy = VERTICAL_ATTACK_BLOCK_OFFSETS[t][1];
            int hitX = MathHelper.floor(x + ox);
            int hitZ = MathHelper.floor(z + oy);
            BlockPos hit = new BlockPos(hitX, hitY, hitZ);
            IBlockState block = world.getBlockState(hit);
            if (block.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                int stateId = Block.getStateId(block);
                for (int n = 0; n < 6; n++) {
                    double pa = rand.nextDouble() * 2 * Math.PI;
                    double pd = rand.nextDouble() * 0.6 + 0.1;
                    double px = x + Math.cos(pa) * pd;
                    double pz = z + Math.sin(pa) * pd;
                    double magnitude = rand.nextDouble() * 4 + 5;
                    double velX = perpX * magnitude;
                    double velY = rand.nextDouble() * 3 + 6;
                    double velZ = perpZ * magnitude;
                    if (vecX * (pz - posZ) - vecZ * (px - posX) > 0) {
                        velX = -velX;
                        velZ = -velZ;
                    }
                    world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, px, y, pz, velX, velY, velZ, stateId);
                }
            }
        }
        int hitX = MathHelper.floor(x);
        int ceilY = MathHelper.floor(getEntityBoundingBox().maxY);
        int hitZ = MathHelper.floor(z);
        final int maxHeight = 5;
        int height = maxHeight;
        PooledMutableBlockPos pos = PooledMutableBlockPos.retain();
        for (; height --> 0; ceilY++) {
            pos.setPos(hitX, ceilY, hitZ);
            if (world.getBlockState(pos).getMaterial().blocksMovement()) {
                break;
            }
        }
        pos.release();
        float strength = height / (float) maxHeight;
        if (strength >= 0) {
            int radius = MathHelper.ceil(MathHelper.sqrt(1 - strength * strength) * maxHeight);
            disturbance = new CeilingDisturbance(hitX, ceilY, hitZ, radius, rand.nextInt(5) + 3, rand.nextInt(60) + 20);
        }
    }

    private class CeilingDisturbance {
        private final int ceilX, ceilY, ceilZ;

        private final int radius;

        private int delay;

        private int remainingTicks;

        private int duration;

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
            PooledMutableBlockPos pos = PooledMutableBlockPos.retain();
            while (amount --> 0) {
                double theta = rand.nextDouble() * Math.PI * 2;
                double dist = rand.nextDouble() * radius;
                double x = ceilX + Math.cos(theta) * dist;
                double y = ceilY - 0.1 - rand.nextDouble() * 0.3;
                double z = ceilZ + Math.sin(theta) * dist;
                int blockX = MathHelper.floor(x);
                int blockZ = MathHelper.floor(z);
                pos.setPos(blockX, ceilY, blockZ);
                IBlockState block = world.getBlockState(pos);
                if (block.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                    int stateId = Block.getStateId(block);
                    world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, 0, 0, 0, stateId);
                    if (playSound && rand.nextFloat() < 0.075F) {
                        SoundType sound = block.getBlock().getSoundType();
                        world.playSound(posX, posY, posZ, sound.getBreakSound(), SoundCategory.BLOCKS, sound.getVolume() * 2, sound.getPitch() * 0.6F, false);
                        playSound = false;
                    }
                }
            }
            pos.release();
            return --remainingTicks <= 0;
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        setRestPos(getPosition());
        return super.onInitialSpawn(difficulty, data);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(REST_POSITION, Optional.absent());
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
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        Optional<BlockPos> restPos = getRestPos();
        if (restPos.isPresent()) {
            compound.setTag("restPos", NBTUtil.createPosTag(getRestPos().get()));   
        }
        compound.setBoolean("active", isActive());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("restPos", NBT.TAG_COMPOUND)) {
            setRestPos(NBTUtil.getPosFromTag(compound.getCompoundTag("restPos")));   
        }
        setActive(compound.getBoolean("active"));
    }

    @Override
    protected void dropLoot() {
        super.dropLoot();
        dropItem(ItemHandler.WROUGHT_AXE, 1);
        dropItem(ItemHandler.WROUGHT_HELMET, 1);
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {}

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }
}