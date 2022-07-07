package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SpawnBoulderAbility extends Ability {
    private static int MAX_CHARGE = 60;
    public static final double SPAWN_BOULDER_REACH = 5;

    public BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    public Vec3 lookPos = new Vec3(0, 0, 0);
    private BlockState spawnBoulderBlock = Blocks.DIRT.defaultBlockState();
    private int spawnBoulderCharge = 0;

    public SpawnBoulderAbility(AbilityType<? extends Ability> abilityType, LivingEntity user) {
        super(abilityType, user,  new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, MAX_CHARGE),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 12)
        });
    }

    @Override
    public void start() {
        super.start();
        playAnimation("spawn_boulder_start", false);
    }

    @Override
    public boolean tryAbility() {
        Vec3 from = getUser().getEyePosition(1.0f);
        Vec3 to = from.add(getUser().getLookAngle().scale(SPAWN_BOULDER_REACH));
        BlockHitResult result = getUser().level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, getUser()));
        if (result.getType() == HitResult.Type.BLOCK) {
            this.lookPos = result.getLocation();
        }

        this.spawnBoulderPos = result.getBlockPos();
        this.spawnBoulderBlock = getUser().level.getBlockState(spawnBoulderPos);
        if (result.getDirection() != Direction.UP) {
            BlockState blockAbove = getUser().level.getBlockState(spawnBoulderPos.above());
            if (blockAbove.isSuffocating(getUser().level, spawnBoulderPos.above()) || blockAbove.isAir())
                return false;
        }
        return EffectGeomancy.isBlockDiggable(spawnBoulderBlock);
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            spawnBoulderCharge++;
            if (spawnBoulderCharge > 1) getUser().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3, 3, false, false));
            if (spawnBoulderCharge == 1 && getUser().level.isClientSide) MowziesMobs.PROXY.playBoulderChargeSound(getUser());
            if ((spawnBoulderCharge + 10) % 10 == 0 && spawnBoulderCharge < 40) {
                if (getUser().level.isClientSide) {
                    AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), (float) getUser().getX(), (float) getUser().getY() + getUser().getBbHeight() / 2f, (float) getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd((0.8f + 2.7f * spawnBoulderCharge / 60f) * 10f, 0), false)
                    });
                }
            }
            if (spawnBoulderCharge == 50) {
                if (getUser().level.isClientSide) {
                    AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), (float) getUser().getX(), (float) getUser().getY() + getUser().getBbHeight() / 2f, (float) getUser().getZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 20, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, 40f), false)
                    });
                }
                getUser().playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1, 1f);
            }

            int size = getBoulderSize() + 1;
            EntityType<EntityBoulder> type = EntityHandler.BOULDERS[size].get();
            if (
                    !getUser().level.noCollision(type.getAABB(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F))
                    || getUser().distanceToSqr(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) > 36
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
        if (spawnBoulderCharge <= 2) {
            playAnimation("spawn_boulder_instant", false);
        }
        else {
            playAnimation("spawn_boulder_end", false);
        }

        int size = getBoulderSize();
        if (spawnBoulderCharge >= 60) size = 3;
        EntityBoulder boulder = new EntityBoulder(EntityHandler.BOULDERS[size].get(), getUser().level, getUser(), spawnBoulderBlock, spawnBoulderPos);
        boulder.setPos(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F);
        if (!getUser().level.isClientSide && boulder.checkCanSpawn()) {
            getUser().level.addFreshEntity(boulder);
        }

        if (spawnBoulderCharge > 2) {
            Vec3 playerEyes = getUser().getEyePosition(1);
            Vec3 vec = playerEyes.subtract(lookPos).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            getUser().setYRot((float) (yaw * 180f / Math.PI + 90));
            getUser().setXRot((float) (pitch * 180f / Math.PI));
        }
        spawnBoulderCharge = 0;
    }

    @Override
    public void onRightMouseUp(Player player) {
        super.onRightMouseUp(player);
        if (isUsing() && getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            if (player.distanceToSqr(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) < 36) {
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
    public void readNBT(Tag nbt) {
        super.readNBT(nbt);
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) spawnBoulderCharge = getTicksInSection();
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        super.onRightClickBlock(event);
        if (!event.getWorld().isClientSide()) AbilityHandler.INSTANCE.sendAbilityMessage(event.getEntityLiving(), AbilityHandler.SPAWN_BOULDER_ABILITY);
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
            Vec3 playerEyes = getUser().getEyePosition(Minecraft.getInstance().getFrameTime());
            Vec3 vec = playerEyes.subtract(lookPos).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            getUser().setYRot((float) (yaw * 180f / Math.PI + 90));
            getUser().setXRot((float) (pitch * 180f / Math.PI));
            getUser().yHeadRot = getUser().getYRot();
            getUser().yRotO = getUser().getYRot();
            getUser().xRotO = getUser().getXRot();
            getUser().yHeadRotO = getUser().yHeadRot;
        }
    }
}
