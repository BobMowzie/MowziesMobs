package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent.KeyTrack;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleRibbon;
import com.bobmowzie.mowziesmobs.client.particles.util.RibbonComponent;
import com.bobmowzie.mowziesmobs.client.particles.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.security.Key;
import java.util.List;

public class EntitySuperNova extends EntityMagicEffect {
    public static int DURATION = 40;

    public EntitySuperNova(World world) {
        super(world);
        this.setSize(1, 1);
    }

    public EntitySuperNova(World world, EntityLivingBase caster, double x, double y, double z) {
        this(world);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (world.isRemote && ticksExisted == 1) {
            float scale = 8.2f;
            for (int i = 0; i < 13; i++) {
                float phaseOffset = rand.nextFloat();
                MowzieParticleBase.spawnParticle(world, MMParticle.ARROW_HEAD, posX, posY, posZ, 0, 0, 0, false, 0, 0, 0, 0, 8F, 0.95, 0.9,0.35, 1, 1, 30, true, new ParticleComponent[]{
                        new ParticleComponent.Orbit(new Vec3d[]{getPositionVector().add(0, height / 2, 0)}, KeyTrack.startAndEnd(0 + phaseOffset, 1.6f + phaseOffset), new ParticleComponent.KeyTrack(
                                new float[]{0.2f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                                new float[]{0, 0.15f, 0.3f, 0.45f, 0.6f, 75f}
                        ), KeyTrack.startAndEnd(rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1), KeyTrack.startAndEnd(rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1), KeyTrack.startAndEnd(rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1), false),
                        new RibbonComponent(MMParticle.RIBBON_FLAT, 10, 0, 0, 0, 0.2F, 0.95, 0.9,0.35, 1, true, true, new ParticleComponent[]{
                                new PropertyOverLength(PropertyOverLength.EnumRibbonProperty.SCALE, KeyTrack.startAndEnd(1, 0)),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1, 0), false)
                        }),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1, 0), false),
                        new ParticleComponent.FaceMotion()
                });
            }
        }

        if (caster != null && caster instanceof EntityLiving && (ticksExisted + 7) % 8 == 0) {
            float ageFrac = ticksExisted / (float)(EntitySuperNova.DURATION);
            float scale = (float) Math.pow(ageFrac, 0.5) * 5f;
            setSize(scale, scale);
            setPosition(prevPosX, prevPosY, prevPosZ);
            List<EntityLivingBase> hitList = getEntitiesNearbyCube(EntityLivingBase.class, scale);
            for (EntityLivingBase entity : hitList) {
                if (entity instanceof LeaderSunstrikeImmune) continue;
                if (EntityAITarget.isSuitableTarget((EntityLiving) caster, entity, false, false)) {
                    float damageFire = 5f;
                    float damageMob = 5f;
                    damageFire *= ConfigHandler.BARAKO.combatData.attackMultiplier;
                    damageMob *= ConfigHandler.BARAKO.combatData.attackMultiplier;
                    entity.attackEntityFrom(DamageSource.ON_FIRE, damageFire);
                    entity.attackEntityFrom(DamageSource.causeMobDamage(caster), damageMob);
                    entity.setFire(5);

                    Vec3d diff = entity.getPositionVector().subtract(getPositionVector());
                    diff = diff.normalize().scale(1);
                    entity.knockBack(this, 0.4f, -diff.x, -diff.z);
                }
            }
        }
        if (ticksExisted > DURATION) setDead();
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }
}
