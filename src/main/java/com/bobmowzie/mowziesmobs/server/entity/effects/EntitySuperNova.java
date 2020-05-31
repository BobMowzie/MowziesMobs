package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent.KeyTrack;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleRibbon;
import com.bobmowzie.mowziesmobs.client.particles.util.RibbonComponent;
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
            MowzieParticleBase.spawnParticle(world, MMParticle.FLARE_RADIAL,posX + 6, posY + 6, posZ, 0.3, 0, 0, true, 0, 0, 0, 0,4F, 1,1,1, 1, 1, 60, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(EnumParticleProperty.MOTION_X, new ParticleComponent.Oscillator(-0.5f, 0.5f, 4, 0), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.MOTION_Z, new ParticleComponent.Oscillator(-0.5f, 0.5f, 4, (float) (Math.PI/2)), false),
                    new RibbonComponent(10, MMParticle.RIBBON_GLOW)
            });
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
