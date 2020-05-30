package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent;
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
            float scale = 65f;
            MowzieParticleBase.spawnParticle(world, MMParticle.RING_BIG, posX, posY + 0.9, posZ, 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 1,1,1, 1, 1, 35, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0.1f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                            new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
            });
        }

        if (caster != null && caster instanceof EntityLiving && (ticksExisted + 4) % 8 == 0) {
            float ageFrac = ticksExisted / (float)(EntitySuperNova.DURATION);
            float scale = (float) Math.pow(ageFrac, 0.5) * 6f;
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
