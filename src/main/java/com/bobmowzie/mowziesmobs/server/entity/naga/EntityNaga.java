package com.bobmowzie.mowziesmobs.server.entity.naga;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by Josh on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity {
    @SideOnly(Side.CLIENT)
    public DynamicChain dc;

    public static final Animation FLAP_ANIMATION = Animation.create(25);

    public ControlledAnimation hoverAnim = new ControlledAnimation(10);

    public EnumNagaMovement movement = EnumNagaMovement.GLIDING;

    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;

    public enum EnumNagaMovement {
        GLIDING,
        HOVERING,
        SWIMMING,
        FALLING,
        FALLEN
    }

    public EntityNaga(World world) {
        super(world);
        this.tasks.addTask(5, new EntityNaga.AIRandomFly(this));
        this.tasks.addTask(7, new EntityNaga.AILookAround(this));
//        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true, false, null));
        this.tasks.addTask(2, new AnimationAI<EntityNaga>(this, FLAP_ANIMATION, false) {
            @Override
            public void updateTask() {
                super.updateTask();
                if (getAnimationTick() >= 4 && getAnimationTick() <= 9) motionY += 0.12;
            }
        });

        this.moveHelper = new EntityNaga.GhastMoveHelper(this);
        setSize(3, 1);
        dc = new DynamicChain(this);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D * MowziesMobs.CONFIG.difficultyScaleNaga);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(9.0D * MowziesMobs.CONFIG.difficultyScaleNaga);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50);
    }

    @Override
    public void onUpdate() {
        prevMotionX = motionX;
        prevMotionY = motionY;
        prevMotionX = motionZ;

        super.onUpdate();
        if (world.isRemote) {
//            dc.updateSpringConstraint(0.2f, 0.3f, 2f, 100f, true, 0.5f, 1);
//            rotationYaw += Math.sin(ticksExisted * 0.2) * 5;
        }
//        setDead();
        renderYawOffset = rotationYaw;
//        posZ += 0.4 * Math.sin(ticksExisted *

//        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        if (getAttackTarget() != null) {
            movement = EnumNagaMovement.HOVERING;
            getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1.0D);
        }
        else {
            movement = EnumNagaMovement.GLIDING;
        }
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
        return new Animation[] {FLAP_ANIMATION};
    }

    public void fall(float distance, float damageMultiplier)
    {
//        super.fall(distance, damageMultiplier);
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
//        super.updateFallState(y, onGroundIn, state, pos);
    }

    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        if (this.isInWater()) {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.isInLava()) {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
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
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, this.onGround ? 0.1F * f1 : 0.02F);
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

        public AIRandomFly(EntityNaga ghast)
        {
            this.parentEntity = ghast;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

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
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 12.0D);
        }
    }

    static class GhastMoveHelper extends EntityMoveHelper
    {
        private final EntityNaga parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(EntityNaga naga)
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
