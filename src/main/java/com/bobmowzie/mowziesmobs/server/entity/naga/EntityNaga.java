package com.bobmowzie.mowziesmobs.server.entity.naga;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.bobmowzie.mowziesmobs.server.ai.MMAINearestAttackableTarget;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity {
    @SideOnly(Side.CLIENT)
    public DynamicChain dc;

    public static final Animation FLAP_ANIMATION = Animation.create(25);
    public static final Animation DODGE_ANIMATION = Animation.create(10);
    public static final Animation SPIT_ANIMATION = Animation.create(25);
    public static final Animation SWOOP_ANIMATION = Animation.create(25);
    public static final Animation HURT_ANIMATION = Animation.create(25);
    public static final Animation HURT_TO_FALL_ANIMATION = Animation.create(25);
    public static final Animation LAND_ANIMATION = Animation.create(25);
    public static final Animation GET_UP_ANIMTATION = Animation.create(25);

    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityNaga.class, DataSerializers.BOOLEAN);

    private ControlledAnimation hoverAnim = new ControlledAnimation(10);
    public float hoverAnimFrac;
    public float prevHoverAnimFrac;

    public enum EnumNagaMovement {
        GLIDING,
        HOVERING,
        SWIMMING,
        FALLING,
        FALLEN
    }

    public EnumNagaMovement movement = EnumNagaMovement.GLIDING;

    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;


    public EntityNaga(World world) {
        super(world);
        this.tasks.addTask(5, new EntityNaga.AIRandomFly(this));
        this.tasks.addTask(5, new EntityNaga.AIFlyAroundTarget(this));
        this.tasks.addTask(7, new EntityNaga.AILookAround(this));
        this.tasks.addTask(6, new EntityAILookIdle(this));
//        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.targetTasks.addTask(1, new MMAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, true, null));
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, FLAP_ANIMATION, false) {
            @Override
            public void updateTask() {
                super.updateTask();
                if (getAnimationTick() >= 4 && getAnimationTick() <= 9) motionY += 0.12;
            }
        });
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, DODGE_ANIMATION, false));

        this.moveHelper = new NagaMoveHelper(this);
        setSize(3, 1);
        dc = new DynamicChain(this);
        setRenderDistanceWeight(2.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(ATTACKING, false);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return super.isInRangeToRenderDist(distance);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D * MowziesMobs.CONFIG.healthScaleNaga);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(9.0D * MowziesMobs.CONFIG.healthScaleNaga);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40);
    }

    @Override
    public void onUpdate() {
        prevMotionX = motionX;
        prevMotionY = motionY;
        prevMotionZ = motionZ;
        prevHoverAnimFrac = hoverAnimFrac;

        super.onUpdate();
//        setDead();
        renderYawOffset = rotationYaw;
//        posZ += 0.4 * Math.sin(ticksExisted *

//        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        if (!world.isRemote) {
            if (getAttackTarget() != null && targetDistance < 25) {
                setAttacking(true);
            } else {
                setAttacking(false);
            }
        }

        if (getAttacking()) {
            movement = EnumNagaMovement.HOVERING;
            hoverAnim.increaseTimer();

            if (getAnimation() == NO_ANIMATION && !world.isRemote) {
                List<EntityArrow> arrowsNearby = getEntitiesNearby(EntityArrow.class, 30);
                for (EntityArrow a : arrowsNearby) {
                    Vec3d aActualMotion = new Vec3d(a.posX - a.prevPosX, a.posY - a.prevPosY, a.posZ - a.prevPosZ);
                    if (aActualMotion.lengthVector() < 0.1) {
                        continue;
                    }

                    Vec3d aMotion = new Vec3d(a.motionX, a.motionY, a.motionZ);
                    float dot = (float) aMotion.normalize().dotProduct(this.getPositionVector().subtract(a.getPositionVector()).normalize());
                    System.out.println(dot);
                    if (dot > 0.96) {
                        Vec3d dodgeVec = aMotion.crossProduct(new Vec3d(0, 1, 0)).normalize().scale(1);
                        Vec3d newPosLeft = getPositionVector().add(dodgeVec.scale(3));
                        Vec3d newPosRight = getPositionVector().add(dodgeVec.scale(-3));
                        Vec3d diffLeft = newPosLeft.subtract(a.getPositionVector());
                        Vec3d diffRight = newPosRight.subtract(a.getPositionVector());
                        if (diffRight.dotProduct(aMotion) > diffLeft.dotProduct(aMotion)) {
                            dodgeVec = dodgeVec.scale(-1);
                        }
                        motionX += dodgeVec.x;
                        motionY += dodgeVec.y;
                        motionZ += dodgeVec.z;
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
                    }
                }
            }
        }
        else {
            movement = EnumNagaMovement.GLIDING;
            hoverAnim.decreaseTimer();
        }

        hoverAnimFrac = hoverAnim.getAnimationProgressSinSqrt();

//        System.out.println("Attacking? " + getAttacking());
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] {FLAP_ANIMATION, DODGE_ANIMATION, SWOOP_ANIMATION, SPIT_ANIMATION, HURT_ANIMATION, HURT_TO_FALL_ANIMATION, LAND_ANIMATION};
    }

    public void fall(float distance, float damageMultiplier)
    {
//        super.fall(distance, damageMultiplier);
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
//        super.updateFallState(y, onGroundIn, state, pos);
    }

    public void travel(float strafe, float upward, float forward) {
        if (this.isInWater()) {
            this.moveRelative(strafe, upward, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.isInLava()) {
            this.moveRelative(strafe, upward, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else if (movement == EnumNagaMovement.HOVERING) {
            float f = 0.91F;

            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(strafe, upward, forward, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) f;
            this.motionY *= (double) f;
            this.motionZ *= (double) f;

            EntityMoveHelper entitymovehelper = this.getMoveHelper();
            double dx = entitymovehelper.getX() - this.posX;
            double dy = entitymovehelper.getY() - this.posY;
            double dz = entitymovehelper.getZ() - this.posZ;
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest < 0.1 && getAnimation() == NO_ANIMATION) {
                motionX = 0;
                motionY = 0;
                motionZ = 0;
            }
        }
        else {
            if (this.motionY > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vec3d vec3d = this.getLookVec();
            float f = this.rotationPitch * 0.017453292F;
            double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
            double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            double d1 = vec3d.lengthVector();
            float f4 = MathHelper.cos(f);
            f4 = (float) ((double) f4 * (double) f4 * Math.min(1.0D, d1 / 0.4D));
            this.motionY += -0.08D + (double) f4 * 0.06D;

            if (this.motionY < 0.0D && d6 > 0.0D) {
                double d2 = this.motionY * -0.1D * (double) f4;
                this.motionY += d2;
                this.motionX += vec3d.x * d2 / d6;
                this.motionZ += vec3d.z * d2 / d6;
            }

            if (f < 0.0F) {
                double d10 = d8 * (double) (-MathHelper.sin(f)) * 0.04D;
                this.motionY += d10 * 3.2D;
                this.motionX -= vec3d.x * d10 / d6;
                this.motionZ -= vec3d.z * d10 / d6;
            }

            if (d6 > 0.0D) {
                this.motionX += (vec3d.x / d6 * d8 - this.motionX) * 0.1D;
                this.motionZ += (vec3d.z / d6 * d8 - this.motionZ) * 0.1D;
            }

            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9900000095367432D;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (getMoveHelper().getY() - posY > 0 && motionY < 0 && getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

            if (this.isCollidedHorizontally && !this.world.isRemote) {
                double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                double d3 = d8 - d11;
                float f5 = (float) (d3 * 10.0D - 3.0D);

                if (f5 > 0.0F) {
                    this.playSound(this.getFallSound((int) f5), 1.0F, 1.0F);
                    this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f5);
                }
            }

            if (this.onGround && !this.world.isRemote) {
                this.setFlag(7, false);
            }
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isOnLadder()
    {
        return false;
    }

    public boolean getAttacking() {
        return getDataManager().get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        getDataManager().set(ATTACKING, attacking);
    }



    static class AILookAround extends EntityAIBase
    {
        private final EntityNaga parentEntity;

        public AILookAround(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexBits(2);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            if (this.parentEntity.getAttackTarget() == null)
            {
                this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
            else
            {
                EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                double d0 = 64.0D;

                if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < 4096.0D)
                {
                    double d1 = entitylivingbase.posX - this.parentEntity.posX;
                    double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }
        }
    }

    static class AIRandomFly extends EntityAIBase
    {
        private final EntityNaga parentEntity;

        public AIRandomFly(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (parentEntity.getAttacking()) return false;

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - this.parentEntity.posX;
                double d1 = entitymovehelper.getY() - this.parentEntity.posY;
                double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 24.0F);
            double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 24.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, parentEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        }
    }

    static class AIFlyAroundTarget extends EntityAIBase
    {
        private final EntityNaga parentEntity;

        public AIFlyAroundTarget(EntityNaga naga)
        {
            this.parentEntity = naga;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (parentEntity.getAttackTarget() != null) {
                if (!entitymovehelper.isUpdating()) {
                    return true;
                } else {
                    double dx = entitymovehelper.getX() - this.parentEntity.posX;
                    double dy = entitymovehelper.getY() - this.parentEntity.posY;
                    double dz = entitymovehelper.getZ() - this.parentEntity.posZ;
                    double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    EntityLivingBase target = parentEntity.getAttackTarget();
                    double dx2 = entitymovehelper.getX() - target.posX;
                    double dy2 = entitymovehelper.getY() - target.posY;
                    double dz2 = entitymovehelper.getZ() - target.posZ;
                    double distanceDestToTarget = Math.sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2);

                    boolean randomChance = parentEntity.rand.nextInt(60) == 0;

//                    System.out.println(randomChance);
//                    System.out.println(distanceToDest);
//                    System.out.println(distanceDestToTarget);

                    return distanceToDest > 60.D || distanceDestToTarget > 20.D || distanceDestToTarget < 5.D || randomChance;
                }
            }
            else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            EntityLivingBase target = parentEntity.getAttackTarget();
            float yaw = (float) (random.nextFloat() * Math.PI * 2);
            float radius = 14;
            double d0 = target.posX + Math.cos(yaw) * radius;
            double d1 = target.posY + 5 + random.nextFloat() * 5;
            double d2 = target.posZ + Math.sin(yaw) * radius;
            double speed = parentEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, speed);
        }
    }

    static class NagaMoveHelper extends EntityMoveHelper
    {
        private final EntityNaga parentEntity;
        private int courseChangeCooldown;

        public NagaMoveHelper(EntityNaga naga)
        {
            super(naga);
            this.parentEntity = naga;
        }

        public void onUpdateMoveHelper()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0)
                {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(7) + 2;
                    d3 = (double) MathHelper.sqrt(d3);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                    {
                        this.parentEntity.motionX += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.motionZ += d2 / d3 * 0.1D;
                    }
                    else
                    {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        /**
         * Checks if entity bounding box is not colliding with terrain
         */
        private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - this.parentEntity.posX) / p_179926_7_;
            double d1 = (y - this.parentEntity.posY) / p_179926_7_;
            double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }
}
