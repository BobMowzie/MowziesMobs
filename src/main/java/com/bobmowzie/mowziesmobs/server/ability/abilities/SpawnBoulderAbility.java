package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SpawnBoulderAbility extends Ability {
    private static int MAX_CHARGE = 60;
    public static final double SPAWN_BOULDER_REACH = 5;

    public BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    public Vector3d lookPos = new Vector3d(0, 0, 0);
    private BlockState spawnBoulderBlock = Blocks.DIRT.getDefaultState();
    private int spawnBoulderCharge = 0;

    public SpawnBoulderAbility(AbilityType<? extends Ability> abilityType, LivingEntity user) {
        super(abilityType, user,  new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, MAX_CHARGE),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 5)
        }, 200);
    }

    @Override
    public boolean tryAbility() {
        Vector3d from = getUser().getEyePosition(1.0f);
        Vector3d to = from.add(getUser().getLookVec().scale(SPAWN_BOULDER_REACH));
        BlockRayTraceResult result = getUser().world.rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, getUser()));
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            this.lookPos = result.getHitVec();
        }

        this.spawnBoulderPos = result.getPos();
        this.spawnBoulderBlock = getUser().world.getBlockState(spawnBoulderPos);
        if (result.getFace() != Direction.UP) {
            BlockState blockAbove = getUser().world.getBlockState(spawnBoulderPos.up());
            if (blockAbove.isSuffocating(getUser().world, spawnBoulderPos.up()) || blockAbove.isAir(getUser().world, spawnBoulderPos.up()))
                return false;
        }
        return EffectGeomancy.isBlockDiggable(spawnBoulderBlock);
    }

    @Override
    public void tick() {
        super.tick();
        if (isUsing() && getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            spawnBoulderCharge++;
            if (spawnBoulderCharge > 1) getUser().addPotionEffect(new EffectInstance(Effects.SLOWNESS, 3, 2, false, false));
            if (spawnBoulderCharge == 1 && getUser().world.isRemote) MowziesMobs.PROXY.playBoulderChargeSound(getUser());
            if ((spawnBoulderCharge + 10) % 10 == 0 && spawnBoulderCharge < 40) {
                if (getUser().world.isRemote) {
                    AdvancedParticleBase.spawnParticle(getUser().world, ParticleHandler.RING2.get(), (float) getUser().getPosX(), (float) getUser().getPosY() + getUser().getHeight() / 2f, (float) getUser().getPosZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd((0.8f + 2.7f * spawnBoulderCharge / 60f) * 10f, 0), false)
                    });
                }
            }
            if (spawnBoulderCharge == 50) {
                if (getUser().world.isRemote) {
                    AdvancedParticleBase.spawnParticle(getUser().world, ParticleHandler.RING2.get(), (float) getUser().getPosX(), (float) getUser().getPosY() + getUser().getHeight() / 2f, (float) getUser().getPosZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 20, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, 40f), false)
                    });
                }
                getUser().playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1, 1f);
            }
            if (getUser().world.isRemote && spawnBoulderCharge > 5 && spawnBoulderCharge < 30) {
                int particleCount = 4;
                while (--particleCount != 0) {
                    double radius = 0.5f + 1.5f * spawnBoulderCharge/30f;
                    double yaw = getUser().getRNG().nextFloat() * 2 * Math.PI;
                    double pitch = getUser().getRNG().nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    getUser().world.addParticle(new ParticleOrb.OrbData((float) getUser().getPosX(), (float) getUser().getPosY() + getUser().getHeight() /2f, (float) getUser().getPosZ(), 14), getUser().getPosX() + ox, getUser().getPosY() + oy + getUser().getHeight()/2, getUser().getPosZ() + oz, 0, 0, 0);
                }
            }

            int size = getBoulderSize() + 1;
            EntityType<EntityBoulder> type = EntityHandler.BOULDERS[size];
            if (
                    !getUser().world.hasNoCollisions(type.getBoundingBoxWithSizeApplied(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F))
                    || getUser().getDistanceSq(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) > 36
            ) {
                nextSection();
            }
        }
    }

    @Override
    protected void beginSection(AbilitySection section) {
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            spawnBoulder();
        }
    }

    private int getBoulderSize() {
        return (int) Math.min(Math.max(0, Math.floor(spawnBoulderCharge/10.f) - 1), 2);
    }

    private void spawnBoulder() {
        int size = getBoulderSize();
        if (spawnBoulderCharge >= 60) size = 3;
        EntityBoulder boulder = new EntityBoulder(EntityHandler.BOULDERS[size], getUser().world, getUser(), spawnBoulderBlock, spawnBoulderPos);
        boulder.setPosition(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F);
        if (!getUser().world.isRemote && boulder.checkCanSpawn()) {
            getUser().world.addEntity(boulder);
        }

        if (spawnBoulderCharge > 2) {
            Vector3d playerEyes = getUser().getEyePosition(1);
            Vector3d vec = playerEyes.subtract(lookPos).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            getUser().rotationYaw = (float) (yaw * 180f / Math.PI + 90);
            getUser().rotationPitch = (float) (pitch * 180f / Math.PI);
        }
        spawnBoulderCharge = 0;
    }

    @Override
    public void onRightMouseUp(PlayerEntity player) {
        super.onRightMouseUp(player);
        if (isUsing() && getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            if (player.getDistanceSq(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) < 36) {
                nextSection();
            } else {
                spawnBoulderCharge = 0;
            }
        }
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(getUser()) && super.canUse();
    }

    @Override
    public void end() {
        spawnBoulderCharge = 0;
        super.end();
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        super.onRightClickBlock(event);
        if (!event.getWorld().isRemote()) AbilityHandler.INSTANCE.sendAbilityMessage(event.getEntityLiving(), AbilityHandler.SPAWN_BOULDER_ABILITY);
    }

    @Override
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        super.onRightClickEmpty(event);
        AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SPAWN_BOULDER_ABILITY);
    }

    @Override
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        super.onRenderTick(event);
        if (isUsing() && getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP && getTicksInSection() > 1) {
            Vector3d playerEyes = getUser().getEyePosition(Minecraft.getInstance().getRenderPartialTicks());
            Vector3d vec = playerEyes.subtract(lookPos).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            getUser().rotationYaw = (float) (yaw * 180f / Math.PI + 90);
            getUser().rotationPitch = (float) (pitch * 180f / Math.PI);
            getUser().rotationYawHead = getUser().rotationYaw;
            getUser().prevRotationYaw = getUser().rotationYaw;
            getUser().prevRotationPitch = getUser().rotationPitch;
            getUser().prevRotationYawHead = getUser().rotationYawHead;
        }
    }
}
