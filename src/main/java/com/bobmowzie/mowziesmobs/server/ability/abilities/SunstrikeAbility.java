package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityInstance;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class SunstrikeAbility extends Ability<SunstrikeAbility.SunstrikeAbilityInstance> {
    private static final double REACH = 15;
    private final static int SUNSTRIKE_RECOVERY = 20;

    public SunstrikeAbility() {
        super(new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SUNSTRIKE_RECOVERY)
        });
    }

    private static BlockRayTraceResult rayTrace(LivingEntity entity, double reach) {
        Vector3d pos = entity.getEyePosition(0);
        Vector3d segment = entity.getLookVec();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.world.rayTraceBlocks(new RayTraceContext(pos, segment, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
    }

    @Override
    public boolean tryAbility(SunstrikeAbilityInstance abilityInstance) {
        LivingEntity user = abilityInstance.getUser();
        BlockRayTraceResult raytrace = rayTrace(user, REACH);
        if (raytrace.getType() == RayTraceResult.Type.BLOCK && raytrace.getFace() == Direction.UP) {
            abilityInstance.rayTrace = raytrace;
            return true;
        }
        return false;
    }

    @Override
    protected void start(SunstrikeAbilityInstance abilityInstance) {
        super.start(abilityInstance);
        LivingEntity user = abilityInstance.getUser();
        if (!abilityInstance.getUser().world.isRemote()) {
            BlockPos hit = abilityInstance.rayTrace.getPos();
            EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, user.world, user, hit.getX(), hit.getY(), hit.getZ());
            sunstrike.onSummon();
            abilityInstance.getUser().world.addEntity(sunstrike);
        }
    }

    @Override
    public boolean canUse(LivingEntity user) {
        if (user instanceof PlayerEntity && !((PlayerEntity)user).inventory.getCurrentItem().isEmpty()) return false;
        return user.isPotionActive(EffectHandler.SUNS_BLESSING);
    }

    @Override
    public SunstrikeAbilityInstance makeInstance(LivingEntity user) {
        return new SunstrikeAbilityInstance(this, user);
    }

    protected static class SunstrikeAbilityInstance extends AbilityInstance {
        protected BlockRayTraceResult rayTrace;
        public SunstrikeAbilityInstance(SunstrikeAbility abilityType, LivingEntity user) {
            super(abilityType, user);
        }
    }
}
