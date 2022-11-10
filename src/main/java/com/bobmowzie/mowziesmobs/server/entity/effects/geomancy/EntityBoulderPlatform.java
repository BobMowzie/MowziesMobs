package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import software.bernie.shadowed.eliotlash.mclib.utils.MathUtils;

public class EntityBoulderPlatform extends EntityBoulderBase {
    private static final float MAX_DIST_HORIZONTAL = 4.0f;
    private static final float MAX_DIST_VERTICAL = 2.5f;
    private static final int MAX_TRIES = 10;

    private EntityBoulderPlatform nextBoulder;

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
        if (tickTimer() == 7 && !level.isClientSide()) {
//            int numNextBoulders = (int) (Math.pow(random.nextFloat(), 16) * 3) + 1;
//            for (int i = 0; i < numNextBoulders; i++)
                nextBoulder();
        }

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

                int whichTierIndex = (int) (Math.pow(random.nextFloat(), 2) * (GeomancyTier.values().length - 2) + 1);
                GeomancyTier nextTier = GeomancyTier.values()[whichTierIndex];
                EntityBoulderPlatform nextBoulder = new EntityBoulderPlatform(EntityHandler.BOULDER_PLATFORM.get(), getLevel(), caster, getBlock(), blockPosition(), nextTier);

                for (int i = 0; i < MAX_TRIES; i++) {
                    Vec3 randomPos;
                    if (position().y() < baseHeight + EntitySculptor.TEST_HEIGHT) {
                        randomPos = chooseRandomLocation(nextBoulder);
                    }
                    // If the platform is already at max height, next platform should move towards sculptor
                    else if (position().multiply(1, 0, 1).distanceTo(sculptor.position().multiply(1, 0, 1)) > MAX_DIST_HORIZONTAL) {
                        randomPos = chooseTowardsSculptorLocation(nextBoulder);
                    }
                    else return null;
                    nextBoulder.setPos(randomPos);

                    if (level.noCollision(nextBoulder)) {
                        getLevel().addFreshEntity(nextBoulder);
                        this.nextBoulder = nextBoulder;
                        return nextBoulder;
                    }
                }
            }
        }
        return null;
    }

    protected Vec3 chooseRandomLocation(EntityBoulderPlatform nextBoulder) {
        if (caster instanceof EntitySculptor) {
            EntitySculptor sculptor = (EntitySculptor) caster;
            EntityPillar pillar = sculptor.getPillar();
            if (pillar != null) {
                Vec3 startLocation = position();
                Vec2 fromPillarPos = new Vec2((float) (caster.getX() - startLocation.x), (float) (caster.getZ() - startLocation.z));
                float horizontalOffset = random.nextFloat(1, MAX_DIST_HORIZONTAL) + this.getBbWidth()/2f + nextBoulder.getBbWidth()/2f;
                float verticalOffset = random.nextFloat(0, MAX_DIST_VERTICAL) - (nextBoulder.getBbHeight() - this.getBbHeight());

                float baseAngle = (float) -Math.toDegrees(Math.atan2(fromPillarPos.y, fromPillarPos.x));
                // Minimum and maximum angles force the angle to approach 90 degrees as it gets too close or too far from the pillar
                float minRandomAngle = (float) (Math.min(Math.pow(3f, -fromPillarPos.length() + 3), 1f) * 90f);
                float maxRandomAngle = 180f - (float) (Math.min(Math.pow(3f, fromPillarPos.length() - EntitySculptor.TEST_RADIUS), 1f) * 90f);
                float randomAngle = random.nextFloat(minRandomAngle, maxRandomAngle);
                if (random.nextBoolean()) randomAngle *= -1;
                randomAngle *= 1f - Math.pow((startLocation.y() - pillar.getY()) / EntitySculptor.TEST_HEIGHT, 5f);
                Vec3 offset = new Vec3(horizontalOffset, verticalOffset, 0);
                float finalAngle = (float) Math.toRadians(MathHelper.wrapDegrees(baseAngle + randomAngle));
                offset = offset.yRot(finalAngle);
                Vec3 nextLocation = startLocation.add(offset);
                if (nextLocation.y() + nextBoulder.getBbHeight() > pillar.getY() + EntitySculptor.TEST_HEIGHT) {
                    nextLocation = new Vec3(nextLocation.x(), pillar.getY() + EntitySculptor.TEST_HEIGHT - getBbHeight(), nextLocation.z());
                }

                return nextLocation;
            }
        }
        return position();
    }

    // For when the platforms have reached max height and just need to head towards sculptor
    protected Vec3 chooseTowardsSculptorLocation(EntityBoulderPlatform nextBoulder) {
        if (caster instanceof EntitySculptor) {
            EntitySculptor sculptor = (EntitySculptor) caster;
            EntityPillar pillar = sculptor.getPillar();
            if (pillar != null) {
                Vec3 startLocation = position();
                Vec2 fromPillarPos = new Vec2((float) (caster.getX() - startLocation.x), (float) (caster.getZ() - startLocation.z));
                float horizontalOffset = random.nextFloat(1, MAX_DIST_HORIZONTAL) + this.getBbWidth()/2f + nextBoulder.getBbWidth()/2f;

                float baseAngle = (float) -Math.toDegrees(Math.atan2(fromPillarPos.y, fromPillarPos.x));
                Vec3 offset = new Vec3(horizontalOffset, 0, 0);
                float finalAngle = (float) Math.toRadians(MathHelper.wrapDegrees(baseAngle));
                offset = offset.yRot(finalAngle);

                return startLocation.add(offset);
            }
        }
        return position();
    }

    protected Vec3 startLocation() {
        return position();
    }

    public EntityBoulderPlatform getNextBoulder() {
        return nextBoulder;
    }

    public boolean checkJumpPath(EntityBoulderPlatform platform) {
        EntityBoulderPlatform next = platform.getNextBoulder();
        if (next == null) return true;

        Vec3 toNext = next.position().subtract(platform.position());
        Vec3 startPos = platform.position().add(toNext.multiply(1, 0, 1).normalize().scale(platform.getBbWidth()/2f));
        Vec3 endPos = platform.position().add(toNext.multiply(1, 0, 1).normalize().scale(-next.getBbWidth()/2f));

        float gravity = (float) net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get().getDefaultValue();

        AABB playerBounds = EntityType.PLAYER.getDimensions().makeBoundingBox(0,0,0);

        return true;
    }
}
