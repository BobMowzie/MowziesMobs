package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.Iterables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class EntityBoulderProjectile extends EntityBoulderBase {
    private final List<Entity> ridingEntities = new ArrayList<Entity>();
    private boolean travelling = false;
    private float speed = 1.5f;
    private int damage = 8;

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

        if (caster instanceof Player) damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.geomancyAttackMultiplier.get();
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        AABB boundingBox = super.makeBoundingBox();
        if (!travelling) boundingBox = boundingBox.expandTowards(0, -0.5, 0);
        return boundingBox;
    }

    @Override
    public void tick() {
        if (tickCount == 1) activate();
        super.tick();
        if (ridingEntities != null) ridingEntities.clear();
        List<Entity> onTopOfEntities = level.getEntities(this, getBoundingBox().contract(0, getBbHeight() - 1, 0).move(new Vec3(0, getBbHeight() - 0.5, 0)).inflate(0.6,0.5,0.6));
        for (Entity entity : onTopOfEntities) {
            if (entity != null && entity.isPickable() && !(entity instanceof EntityBoulderProjectile) && entity.getY() >= this.getY() + 0.2) ridingEntities.add(entity);
        }
        if (travelling){
            for (Entity entity : ridingEntities) {
                entity.move(MoverType.SHULKER_BOX, getDeltaMovement());
            }
        }
        
        // Hit entities
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(1.7);
        if (travelling && !entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (level.isClientSide) continue;
                if (entity == caster) continue;
                if (ridingEntities.contains(entity)) continue;
                if (caster != null) entity.hurt(DamageSource.indirectMobAttack(this, caster), damage);
                else entity.hurt(DamageSource.FALLING_BLOCK, damage);
                if (isAlive() && boulderSize != GeomancyTier.HUGE) this.explode();
            }
        }

        // Hit other boulders
        List<EntityBoulderProjectile> bouldersHit = level.getEntitiesOfClass(EntityBoulderProjectile.class, getBoundingBox().inflate(0.2, 0.2, 0.2).move(getDeltaMovement().normalize().scale(0.5)));
        if (travelling && !bouldersHit.isEmpty()) {
            for (EntityBoulderProjectile entity : bouldersHit) {
                if (!entity.travelling) {
                    entity.skipAttackInteraction(this);
                    explode();
                }
            }
        }

        // Hit blocks
        if (travelling) {
            if (
                    !level.getEntities(this, getBoundingBox().inflate(0.1), (e)->!ridingEntities.contains(e) && e.canBeCollidedWith()).isEmpty() ||
                            Iterables.size(level.getBlockCollisions(this, getBoundingBox().inflate(0.1))) > 0
            ) {
                this.explode();
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean skipAttackInteraction(Entity entityIn) {
        if (risingTick > finishedRisingTick - 1 && !travelling) {
            if (entityIn instanceof Player
                    && EffectGeomancy.canUse((Player)entityIn)) {
                Player player = (Player) entityIn;
                if (ridingEntities.contains(player)) {
                    Vec3 lateralLookVec = Vec3.directionFromRotation(0, player.getYRot()).normalize();
                    setDeltaMovement(speed * 0.5 * lateralLookVec.x, getDeltaMovement().y, speed * 0.5 * lateralLookVec.z);
                } else {
                    setDeltaMovement(player.getLookAngle().scale(speed * 0.5));
                }
                AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.HIT_BOULDER_ABILITY);
            }
            else if (entityIn instanceof EntityBoulderProjectile && ((EntityBoulderProjectile) entityIn).travelling) {
                EntityBoulderProjectile boulder = (EntityBoulderProjectile)entityIn;
                Vec3 thisPos = position();
                Vec3 boulderPos = boulder.position();
                Vec3 velVec = thisPos.subtract(boulderPos).normalize();
                setDeltaMovement(velVec.scale(speed * 0.5));
            }
            else {
                return super.skipAttackInteraction(entityIn);
            }
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
                EntityCameraShake.cameraShake(level, position(), 10, 0.05f, 0, 20);
            }
            else if (boulderSize == GeomancyTier.HUGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 1f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 0.9f);
                EntityCameraShake.cameraShake(level, position(), 15, 0.05f, 0, 20);
            }

            if (level.isClientSide) {
                Vec3 ringOffset = getDeltaMovement().scale(-1).normalize();
                ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING2.get(), (float) getX() + (float) ringOffset.x, (float) getY() + 0.5f + (float) ringOffset.y, (float) getZ() + (float) ringOffset.z, 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * getBbWidth()), true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * getBbWidth()) * 8f), false)
                });
            }
        }
        return super.skipAttackInteraction(entityIn);
    }

    public boolean isTravelling() {
        return travelling;
    }
}
