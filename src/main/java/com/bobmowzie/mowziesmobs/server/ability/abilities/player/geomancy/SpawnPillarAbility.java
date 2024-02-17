package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SpawnPillarAbility extends PlayerAbility {
    private static int MAX_DURATION = 120;
    private static int MAX_RANGE_TO_GROUND = 12;
    private BlockPos spawnPillarPos;
    private BlockState spawnPillarBlock;
    private EntityPillar pillar;

    public SpawnPillarAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, MAX_DURATION)
        });
    }

    @Override
    public void start() {
        super.start();
        playAnimation("pillar_spawn", false);
        getUser().setDeltaMovement(getUser().getDeltaMovement().add(0d,-2d,0d));
    }

    @Override
    public boolean tryAbility() {
        Vec3 from = getUser().position();
        Vec3 to = from.subtract(0, MAX_RANGE_TO_GROUND, 0);
        BlockHitResult result = getUser().level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, getUser()));
        if (result.getType() != HitResult.Type.MISS) {
            this.spawnPillarPos = result.getBlockPos();
            this.spawnPillarBlock = getUser().level.getBlockState(spawnPillarPos);
            if (result.getDirection() != Direction.UP) {
                BlockState blockAbove = getUser().level.getBlockState(spawnPillarPos.above());
                if (blockAbove.isSuffocating(getUser().level, spawnPillarPos.above()) || blockAbove.isAir())
                    return false;
            }
            return EffectGeomancy.isBlockDiggable(spawnPillarBlock);
        }
        return false;
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
    }

    @Override
    protected void beginSection(AbilitySection section) {
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            spawnPillar();
        }
    }

    private void spawnPillar() {
        //playAnimation("spawn_boulder_instant", false);

        pillar = new EntityPillar(EntityHandler.PILLAR.get(), getUser().level, getUser(), spawnPillarBlock, spawnPillarPos);
        pillar.setPos(spawnPillarPos.getX() + 0.5F, spawnPillarPos.getY() + 1, spawnPillarPos.getZ() + 0.5F);
        if (!getUser().level.isClientSide && pillar.checkCanSpawn()) {
            getUser().level.addFreshEntity(pillar);
        }
    }

    @Override
    public void end() {
        super.end();
        if (pillar != null) pillar.stopRising();
        pillar = null;
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(getUser()) && super.canUse();
    }

    @Override
    public void onJump(LivingEvent.LivingJumpEvent event) {
        super.onJump(event);
        if (getUser().isCrouching()) {
            if (!event.getEntity().getLevel().isClientSide()) AbilityHandler.INSTANCE.sendAbilityMessage(event.getEntity(), AbilityHandler.SPAWN_PILLAR_ABILITY);
        }
    }

    @Override
    public void onSneakUp(Player player) {
        super.onSneakUp(player);
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE && isUsing()) {
            if (pillar != null) pillar.stopRising();
            nextSection();
        }
    }
}
