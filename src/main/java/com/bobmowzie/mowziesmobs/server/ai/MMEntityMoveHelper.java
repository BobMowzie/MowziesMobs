package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigation;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Mth;

public class MMEntityMoveHelper extends MovementController
{
    private float maxRotate = 90;

    public MMEntityMoveHelper(MobEntity entitylivingIn, float maxRotate)
    {
        super(entitylivingIn);
        this.maxRotate = maxRotate;
    }

    public void tick()
    {
        if (this.action == MMEntityMoveHelper.Action.STRAFE)
        {
            float f = (float)this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            float f1 = (float)this.speed * f;
            float f2 = this.moveForward;
            float f3 = this.moveStrafe;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F)
            {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = Mth.sin(this.mob.getYRot() * 0.017453292F);
            float f6 = Mth.cos(this.mob.getYRot() * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigation pathnavigate = this.mob.getNavigation();

            if (pathnavigate != null)
            {
                NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();

                if (nodeprocessor != null && nodeprocessor.getFloorNodeType(this.mob.world, Mth.floor(this.mob.getX() + (double)f7), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + (double)f8)) != PathNodeType.WALKABLE)
                {
                    this.moveForward = 1.0F;
                    this.moveStrafe = 0.0F;
                    f1 = f;
                }
            }

            this.mob.setAIMoveSpeed(f1);
            this.mob.setMoveForward(this.moveForward);
            this.mob.setMoveStrafing(this.moveStrafe);
            this.action = MMEntityMoveHelper.Action.WAIT;
        }
        else if (this.action == MMEntityMoveHelper.Action.MOVE_TO)
        {
            this.action = MMEntityMoveHelper.Action.WAIT;
            double d0 = this.posX - this.mob.getX();
            double d1 = this.posZ - this.mob.getZ();
            double d2 = this.posY - this.mob.getY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D)
            {
                this.mob.setMoveForward(0.0F);
                return;
            }

            float f9 = (float)(Mth.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            this.mob.getYRot() = this.limitAngle(this.mob.getYRot(), f9, maxRotate);
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

            if (d2 > (double)this.mob.maxUpStep && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getBbWidth()))
            {
                this.mob.getJumpController().setJumping();
                this.action = MMEntityMoveHelper.Action.JUMPING;
            }
        }
        else if (this.action == MMEntityMoveHelper.Action.JUMPING)
        {
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

            if (this.mob.isOnGround())
            {
                this.action = MMEntityMoveHelper.Action.WAIT;
            }
        }
        else
        {
            this.mob.setMoveForward(0.0F);
        }
    }
}