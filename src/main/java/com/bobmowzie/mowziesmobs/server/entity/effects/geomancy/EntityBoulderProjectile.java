package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.Iterables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class EntityBoulderProjectile extends EntityBoulderBase {
    protected final List<Entity> ridingEntities = new ArrayList<Entity>();
    protected boolean travelling = false;
    protected float speed = 1.5f;
    protected int damage = 8;

    private boolean didShootParticles = false;
    private static final EntityDataAccessor<Vector3f> SHOOT_DIRECTION = SynchedEntityData.defineId(EntityBoulderProjectile.class, EntityDataSerializers.VECTOR3);

    public EntityBoulderProjectile(EntityType<? extends EntityBoulderProjectile> type, Level world) {
        super(type, world);
    }

    public EntityBoulderProjectile(EntityType<? extends EntityBoulderProjectile> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    public void setSizeParams() {
        super.setSizeParams();
        GeomancyTier size = getTier();
        if (size == GeomancyTier.MEDIUM) {
            damage = 12;
            speed = 1.2f;
        }
        else if (size == GeomancyTier.LARGE) {
            damage = 16;
            speed = 1f;
        }
        else if (size == GeomancyTier.HUGE) {
            damage = 20;
            speed = 0.65f;
        }

        if (getCaster() instanceof Player) damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.attackMultiplier.get();
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        AABB boundingBox = super.makeBoundingBox();
        if (!travelling) boundingBox = boundingBox.expandTowards(0, -0.5, 0);
        return boundingBox;
    }

    protected void findRidingEntities() {
        if (!(getCaster() instanceof EntitySculptor)) {
            if (ridingEntities != null) ridingEntities.clear();
            List<Entity> onTopOfEntities = level().getEntities(this, getBoundingBox().contract(0, getBbHeight() - 1, 0).move(new Vec3(0, getBbHeight() - 0.5, 0)).inflate(0.6, 0.5, 0.6));
            for (Entity entity : onTopOfEntities) {
                if (entity != null && entity.isPickable() && !(entity instanceof EntityBoulderProjectile) && entity.getY() >= this.getY() + 0.2)
                    ridingEntities.add(entity);
            }
        }
    }

    @Override
    public void tick() {
        if (startActive() && tickCount == 1) activate();
        super.tick();
        if (getCaster() == null || getCaster().isRemoved()) explode();
        findRidingEntities();
        if (travelling){
            for (Entity entity : ridingEntities) {
                entity.move(MoverType.SHULKER_BOX, getDeltaMovement().add(0, 0.1, 0));
            }
        }
        
        // Hit entities
        List<Entity> entitiesHit = getEntitiesNearby(1.7);
        if (travelling && !entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (level().isClientSide) continue;
                if (entity == getCaster()) continue;
                if (entity.noPhysics) continue;
                if (!entity.canBeHitByProjectile()) continue;
                if (!travellingBlockedBy(entity)) continue;
                if (ridingEntities != null && ridingEntities.contains(entity)) continue;
                if (getCaster() != null) entity.hurt(damageSources().mobProjectile(this, getCaster()), damage);
                else entity.hurt(damageSources().generic(), damage);
                if (isAlive() && boulderSize != GeomancyTier.HUGE) this.explode();
            }
        }

        // Hit other boulders
        handleHitOtherBoulders();

        // Hit blocks
        if (travelling) {
            if (
                    !level().getEntities(this, getBoundingBox().inflate(0.1), (e) -> (ridingEntities == null || !ridingEntities.contains(e)) && e.canBeCollidedWith() && this.travellingBlockedBy(e)).isEmpty() ||
                            Iterables.size(level().getBlockCollisions(this, getBoundingBox().inflate(0.1))) > 0
            ) {
                this.explode();
            }
        }

        if (level().isClientSide() && getEntityData().get(SHOOT_DIRECTION).length() > 0 && !didShootParticles) {
            Vec3 ringOffset = new Vec3(getEntityData().get(SHOOT_DIRECTION)).scale(-1).normalize();
            ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
            AdvancedParticleBase.spawnAlwaysVisibleParticle(level(), ParticleHandler.RING2.get(), 64, (float) getX() + (float) ringOffset.x, (float) getY() + 0.5f + (float) ringOffset.y, (float) getZ() + (float) ringOffset.z, 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * getBbWidth()), true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * getBbWidth()) * 8f * getShootRingParticleScale()), false)
            });
            didShootParticles = true;
        }
    }

    public List<Entity> getRidingEntities() {
        return ridingEntities;
    }

    protected boolean startActive() {
        return true;
    }

    protected boolean travellingBlockedBy(Entity entity) {
        if (this.getCaster() instanceof EntitySculptor) {
            return !(entity instanceof EntityBoulderBase && ((EntityBoulderProjectile)entity).getCaster() == getCaster());
        }
        return true;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        if (this.getCaster() instanceof EntitySculptor) {
            return super.canCollideWith(entity) && !(entity instanceof EntityBoulderBase && ((EntityBoulderProjectile)entity).getCaster() == getCaster());
        }
        return super.canCollideWith(entity);
    }

    protected void handleHitOtherBoulders() {
        List<EntityBoulderProjectile> bouldersHit = level().getEntitiesOfClass(EntityBoulderProjectile.class, getBoundingBox().inflate(0.2, 0.2, 0.2).move(getDeltaMovement().normalize().scale(0.5)));
        if (travelling && !bouldersHit.isEmpty()) {
            for (EntityBoulderProjectile entity : bouldersHit) {
                if (!entity.travelling && this.travellingBlockedBy(entity)) {
                    entity.skipAttackInteraction(this);
                    explode();
                }
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public void shoot(Vec3 shootDirection) {
        setDeltaMovement(shootDirection);
        if (!travelling) setDeathTime(60);
        travelling = true;
        setBoundingBox(getType().getAABB(getX(), getY(), getZ()));

        if (boulderSize == GeomancyTier.SMALL) {
            playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 1.3f);
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.9f);
        }
        else if (boulderSize == GeomancyTier.MEDIUM) {
            playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 0.9f);
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.5f);
        }
        else if (boulderSize == GeomancyTier.LARGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL.get(), 1.5f, 0.5f);
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1.3f);
            EntityCameraShake.cameraShake(level(), position(), 10, 0.05f, 0, 20);
        }
        else if (boulderSize == GeomancyTier.HUGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 1f);
            playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.9f);
            EntityCameraShake.cameraShake(level(), position(), 15, 0.05f, 0, 20);
        }

        getEntityData().set(SHOOT_DIRECTION, shootDirection.toVector3f());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(SHOOT_DIRECTION, new Vector3f(0, 0, 0));
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
    }

    protected float getShootRingParticleScale() {
        return 1.0f;
    }

    @Override
    public boolean skipAttackInteraction(Entity entityIn) {
        if (risingTick > finishedRisingTick - 1 && !travelling) {
            if (entityIn instanceof Player player) {
                if (EffectGeomancy.canUse((Player) entityIn)) {
                    if (ridingEntities.contains(player)) {
                        Vec3 lateralLookVec = Vec3.directionFromRotation(0, player.getYRot()).normalize();
                        shoot(new Vec3(speed * 0.5 * lateralLookVec.x, getDeltaMovement().y, speed * 0.5 * lateralLookVec.z));
                    } else {
                        shoot(player.getLookAngle().scale(speed * 0.5));
                    }
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.HIT_BOULDER_ABILITY);
                }
                else if (!level().isClientSide() && this.getCaster() == player) {
                    explode();
                }
            }
            else if (entityIn instanceof EntityBoulderProjectile boulder && ((EntityBoulderProjectile) entityIn).travelling) {
                Vec3 thisPos = position();
                Vec3 boulderPos = boulder.position();
                Vec3 velVec = thisPos.subtract(boulderPos).normalize();
                shoot(velVec.scale(speed * 0.5));
            }
        }
        return super.skipAttackInteraction(entityIn);
    }

    public boolean isTravelling() {
        return travelling;
    }

    public void setTravelling(boolean travel){
        travelling = travel;
    }

    public void setDamage(int dam){
        damage = dam;
    }
}
