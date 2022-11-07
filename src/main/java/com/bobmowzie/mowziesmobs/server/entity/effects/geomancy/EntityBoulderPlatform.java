package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import software.bernie.shadowed.eliotlash.mclib.utils.MathUtils;

public class EntityBoulderPlatform extends EntityBoulderBase {
    private static final float MAX_DIST_HORIZONTAL = 4.0f;
    private static final float MAX_DIST_VERTICAL = 2.5f;
    private static final int MAX_TRIES = 10;

    public EntityBoulderPlatform(EntityType<? extends EntityBoulderBase> type, Level world) {
        super(type, world);
    }

    public EntityBoulderPlatform(EntityType<? extends EntityBoulderBase> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    @Override
    public boolean doRemoveTimer() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickTimer() == 10) nextBoulder();

        if (caster instanceof EntitySculptor && !caster.isRemoved()) {
            EntitySculptor sculptor = (EntitySculptor) caster;
            EntityPillar pillar = sculptor.getPillar();
            if (pillar == null || pillar.isRemoved()) {
                this.explode();
            }
        }
        else {
            this.explode();
        }
    }

    public EntityBoulderPlatform nextBoulder() {
        if (caster instanceof EntitySculptor) {
            EntitySculptor sculptor = (EntitySculptor) caster;
            EntityPillar pillar = sculptor.getPillar();
            if (pillar != null) {
                int baseHeight = (int) sculptor.getPillar().position().y;
                if (position().y() > baseHeight + EntitySculptor.TEST_HEIGHT) return null;

                EntityBoulderPlatform nextBoulder = new EntityBoulderPlatform(EntityHandler.BOULDER_PLATFORM.get(), getLevel(), caster, getBlock(), blockPosition(), getTier());

                for (int i = 0; i < MAX_TRIES; i++) {
                    nextBoulder.setPos(chooseRandomLocation());

                    if (level.noCollision(nextBoulder)) {
                        getLevel().addFreshEntity(nextBoulder);
                        return nextBoulder;
                    }
                }
            }
        }
        return null;
    }

    protected Vec3 chooseRandomLocation() {
        if (caster instanceof EntitySculptor) {
            EntitySculptor sculptor = (EntitySculptor) caster;
            EntityPillar pillar = sculptor.getPillar();
            if (pillar != null) {
                Vec3 startLocation = position();
                Vec2 fromPillarPos = new Vec2((float) (startLocation.x - caster.getX()), (float) (startLocation.z - caster.getZ()));
                float horizontalOffset = random.nextFloat(1, MAX_DIST_HORIZONTAL);
                float verticalOffset = random.nextFloat(0, MAX_DIST_VERTICAL);

                float baseAngle = (float) Math.toDegrees(Math.atan2(fromPillarPos.y, fromPillarPos.x));
                float minRandomAngle = (float) (Math.pow(2, -fromPillarPos.length()) * 90f);
                float randomAngle = random.nextFloat(minRandomAngle, 180f);
                if (random.nextBoolean()) randomAngle *= -1;
                randomAngle *= 1f - Math.pow((startLocation.y() - pillar.getY()) / EntitySculptor.TEST_HEIGHT, 10f);
                Vec3 offset = new Vec3(horizontalOffset, verticalOffset, 0);
                float finalAngle = (float) Math.toRadians(MathHelper.wrapDegrees(baseAngle + randomAngle));
                System.out.println(randomAngle);
                offset = offset.yRot(finalAngle);

                return startLocation.add(offset);
            }
        }
        return position();
    }

    protected Vec3 startLocation() {
        return position();
    }
}
