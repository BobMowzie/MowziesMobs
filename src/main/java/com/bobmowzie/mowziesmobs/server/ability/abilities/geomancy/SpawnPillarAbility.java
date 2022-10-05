package com.bobmowzie.mowziesmobs.server.ability.abilities.geomancy;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SpawnPillarAbility extends Ability {
    private static int MAX_DURATION = 60;
    private BlockPos spawnPillarPos;
    private BlockState spawnPillarBlock;

    public SpawnPillarAbility(AbilityType<? extends Ability> abilityType, LivingEntity user) {
        super(abilityType, user,  new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, MAX_DURATION)
        });
    }

    @Override
    public void start() {
        super.start();
        playAnimation("spawn_boulder_start", false);
    }

    @Override
    public boolean tryAbility() {
        Vec3 from = getUser().getPosition(0);
        Vec3 to = from.subtract(0, from.y(), 0).add(0, getUser().getLevel().getMinBuildHeight(), 0);
        BlockHitResult result = getUser().level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, getUser()));
        this.spawnPillarPos = result.getBlockPos();
        this.spawnPillarBlock = getUser().level.getBlockState(spawnPillarPos);
        if (result.getDirection() != Direction.UP) {
            BlockState blockAbove = getUser().level.getBlockState(spawnPillarPos.above());
            if (blockAbove.isSuffocating(getUser().level, spawnPillarPos.above()) || blockAbove.isAir())
                return false;
        }
        return EffectGeomancy.isBlockDiggable(spawnPillarBlock);
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
        playAnimation("spawn_boulder_instant", false);

        EntityPillar pillar = new EntityPillar(EntityHandler.PILLAR.get(), getUser().level, getUser(), spawnPillarBlock, spawnPillarPos);
        pillar.setPos(spawnPillarPos.getX() + 0.5F, spawnPillarPos.getY() + 2, spawnPillarPos.getZ() + 0.5F);
        if (!getUser().level.isClientSide && pillar.checkCanSpawn()) {
            getUser().level.addFreshEntity(pillar);
        }
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(getUser()) && super.canUse();
    }
}
