package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.KeyTrack;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.damage.DamageUtil;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntitySuperNova extends EntityMagicEffect {
    public static int DURATION = 40;

    public EntitySuperNova(EntityType<? extends EntitySuperNova> type, Level world) {
        super(type, world);
    }

    public EntitySuperNova(EntityType<? extends EntitySuperNova> type, Level world, LivingEntity caster, double x, double y, double z) {
        this(type, world);
        if (!level.isClientSide) {
            this.setCasterID(caster.getEntityId());
        }
        this.setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        if (caster == null || !caster.isAlive()) this.remove();

        if (tickCount == 1) {
            EntityCameraShake.cameraShake(world, getPositionVec(), 30, 0.05f, 10, 30);
            playSound(MMSounds.ENTITY_SUPERNOVA_END.get(), 3f, 1f);
            if (level.isClientSide) {
                float scale = 8.2f;
                for (int i = 0; i < 15; i++) {
                    float phaseOffset = random.nextFloat();
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), getX(), getY(), getZ(), 0, 0, 0, false, 0, 0, 0, 0, 8F, 0.95, 0.9, 0.35, 1, 1, 30, true, true, new ParticleComponent[]{
                            new ParticleComponent.Orbit(new Vec3[]{getPositionVec().add(0, getHeight() / 2, 0)}, KeyTrack.startAndEnd(0 + phaseOffset, 1.6f + phaseOffset), new ParticleComponent.KeyTrack(
                                    new float[]{0.2f * scale, 0.63f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                                    new float[]{0, 0.15f, 0.3f, 0.45f, 0.6f, 0.75f}
                            ), KeyTrack.startAndEnd(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), KeyTrack.startAndEnd(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), KeyTrack.startAndEnd(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), false),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.2F, 0.95, 0.9, 0.35, 1, true, true, new ParticleComponent[]{
                                    new PropertyOverLength(PropertyOverLength.EnumRibbonProperty.SCALE, KeyTrack.startAndEnd(1, 0)),
                                    new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1, 0), false)
                            }),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1, 0), false),
                            new ParticleComponent.FaceMotion()
                    });
                }
            }
        }

        if (caster != null && caster instanceof MobEntity) {
            float ageFrac = tickCount / (float)(EntitySuperNova.DURATION);
            float scale = (float) Math.pow(ageFrac, 0.5) * 5f;
            setBoundingBox(getBoundingBox().grow(scale));
            setPosition(xo, prevPosY, zo);
            List<LivingEntity> hitList = getEntitiesNearbyCube(LivingEntity.class, scale);
            for (LivingEntity entity : hitList) {
                if (entity instanceof LeaderSunstrikeImmune) continue;
                if (caster.canAttack(entity)) {
                    float damageFire = 4f;
                    float damageMob = 4f;
                    damageFire *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                    damageMob *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                    boolean hitWithFire = DamageUtil.dealMixedDamage(entity, DamageSource.causeIndirectDamage(this, caster), damageMob, DamageSource.ON_FIRE, damageFire).getRight();
                    if (hitWithFire) {
                        Vec3 diff = entity.position().subtract(getPositionVec());
                        diff = diff.normalize();
                        entity.applyKnockback(0.4f, -diff.x, -diff.z);
                        entity.setFire(5);
                    }
                }
            }
        }
        if (tickCount > DURATION) remove();
    }

    @Override
    public float getBrightness() {
        return 15728880;
    }
}
