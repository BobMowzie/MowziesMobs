package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;

public class MMEntityMoveHelper extends MoveControl
{
    private float maxRotate = 90;

    public MMEntityMoveHelper(Mob entitylivingIn, float maxRotate)
    {
        super(entitylivingIn);
        this.maxRotate = maxRotate;
    }

    public void tick()
    {
        if (this.operation == MMEntityMoveHelper.Operation.STRAFE)
        {
            float f = (float)this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            float f1 = (float)this.speedModifier * f;
            float f2 = this.strafeForwards;
            float f3 = this.strafeRight;
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
                NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();

                if (nodeprocessor != null && nodeprocessor.getBlockPathType(this.mob.level, Mth.floor(this.mob.getX() + (double)f7), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + (double)f8)) != BlockPathTypes.WALKABLE)
                {
                    this.strafeForwards = 1.0F;
                    this.strafeRight = 0.0F;
                    f1 = f;
                }
            }

            this.mob.setSpeed(f1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MMEntityMoveHelper.Operation.WAIT;
        }
        else if (this.operation == MMEntityMoveHelper.Operation.MOVE_TO)
        {
            this.operation = MMEntityMoveHelper.Operation.WAIT;
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d2 = this.wantedY - this.mob.getY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D)
            {
                this.mob.setZza(0.0F);
                return;
            }

            float f9 = (float)(Mth.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, maxRotate));
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

            if (d2 > (double)this.mob.maxUpStep && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getBbWidth()))
            {
                this.mob.getJumpControl().jump();
                this.operation = MMEntityMoveHelper.Operation.JUMPING;
            }
        }
        else if (this.operation == MMEntityMoveHelper.Operation.JUMPING)
        {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

            if (this.mob.isOnGround())
            {
                this.operation = MMEntityMoveHelper.Operation.WAIT;
            }
        }
        else
        {
            this.mob.setZza(0.0F);
        }
    }
}