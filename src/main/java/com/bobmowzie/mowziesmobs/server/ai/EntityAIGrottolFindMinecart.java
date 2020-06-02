package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityMinecartEmpty;

import java.util.Comparator;
import java.util.List;

public final class EntityAIGrottolFindMinecart extends EntityAIBase {
    private final EntityGrottol grottol;

    private final Comparator<Entity> sorter;

    private final Predicate<EntityMinecartEmpty> predicate;

    private EntityMinecartEmpty minecart;

    private int time;

    public EntityAIGrottolFindMinecart(EntityGrottol grottol) {
        this.grottol = grottol;
        this.sorter = Comparator.comparing(grottol::getDistanceSq);
        this.predicate = minecart -> minecart != null && minecart.isEntityAlive() && !minecart.isBeingRidden() && EntityGrottol.isBlockRail(minecart.world.getBlockState(minecart.getPosition()).getBlock());
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (grottol.fleeTime <= 1) return false;
        List<EntityMinecartEmpty> minecarts = grottol.world.getEntitiesWithinAABB(EntityMinecartEmpty.class, grottol.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D), predicate);
        minecarts.sort(sorter);
        if (minecarts.isEmpty()) {
            return false;
        }
        minecart = minecarts.get(0);
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return predicate.test(minecart) && time < 1200 && !grottol.isInMinecart();
    }

    @Override
    public void startExecuting() {
        time = 0;
        grottol.getNavigator().tryMoveToEntityLiving(minecart, 0.5D);
    }

    @Override
    public void resetTask() {
        grottol.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        if (grottol.getDistanceSq(minecart) > 1.45D * 1.45D) {
            grottol.getLookHelper().setLookPositionWithEntity(minecart, 10.0F, grottol.getVerticalFaceSpeed());
            if (++time % 40 == 0) {
                grottol.getNavigator().tryMoveToEntityLiving(minecart, 0.5D);
            }
        } else {
            grottol.startRiding(minecart, true);
            if (minecart.getRollingAmplitude() == 0) {
                minecart.setRollingDirection(-minecart.getRollingDirection());
                minecart.setRollingAmplitude(10);
                minecart.setDamage(50.0F);
            }
        }
    }
}
