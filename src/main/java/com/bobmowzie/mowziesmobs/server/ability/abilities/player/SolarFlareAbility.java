package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderSunstrike;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class SolarFlareAbility extends PlayerAbility {

    public SolarFlareAbility(AbilityType<Player, SolarFlareAbility> abilityType, Player user) {
        super(abilityType, user, EntityUmvuthi.SolarFlareAbility.SECTION_TRACK);
    }

    @Override
    public void start() {
        super.start();
        getUser().playSound(MMSounds.ENTITY_UMVUTHI_BURST.get(), 1.7f, 1.5f);
        playAnimation("solar_flare", false);
    }

    @Override
    public boolean canUse() {
        if (getUser() != null && !(getUser().getInventory().getSelected().isEmpty())) return false;
        return getUser().hasEffect(EffectHandler.SUNS_BLESSING) && super.canUse();
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        getUser().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 2, false, false));

        if (getTicksInUse() <= 6 && getLevel().isClientSide) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f;
                double yaw = rand.nextFloat() * 2 * Math.PI;
                double pitch = rand.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                getLevel().addParticle(new ParticleOrb.OrbData((float) getUser().getX(), (float) getUser().getY() + getUser().getBbHeight() / 2f, (float) getUser().getZ(), 6), getUser().getX() + ox, getUser().getY() + getUser().getBbHeight() / 2f + oy, getUser().getZ() + oz, 0, 0, 0);
            }
        }

        if (getTicksInUse() == 10) {
            if (getLevel().isClientSide) {
                for (int i = 0; i < 30; i++) {
                    final float velocity = 0.25F;
                    float yaw = i * (MathUtils.TAU / 30);
                    float vy = rand.nextFloat() * 0.1F - 0.05f;
                    float vx = velocity * Mth.cos(yaw);
                    float vz = velocity * Mth.sin(yaw);
                    getLevel().addParticle(ParticleTypes.FLAME, getUser().getX(), getUser().getY() + 1, getUser().getZ(), vx, vy, vz);
                }
            }
        }
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            Player user = getUser();
            float radius = 3.2f;
            List<LivingEntity> hit = getEntityLivingBaseNearby(user, radius, radius, radius, radius);
            for (LivingEntity aHit : hit) {
                if (aHit == getUser()) {
                    continue;
                }
                float damage = 2.0f;
                float knockback = 3.0f;
                damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier.get();
                if (aHit.hurt(DamageSource.playerAttack(user), damage)) {
                    if (knockback > 0) {
                        Vec3 vec3 = aHit.position().subtract(user.position()).normalize().scale((double)knockback * 0.6D);
                        if (vec3.lengthSqr() > 0.0D) {
                            aHit.push(vec3.x, 0.1D, vec3.z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        super.onLeftClickEmpty(event);
        if (event.getPlayer().isCrouching()) AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SOLAR_FLARE_ABILITY);
    }

    @Override
    public void onLeftClickEntity(AttackEntityEvent event) {
        super.onLeftClickEntity(event);
        if (event.getPlayer().isCrouching()) AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SOLAR_FLARE_ABILITY);
    }
}
