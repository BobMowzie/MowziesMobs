package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
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

public class SunstrikeAbility extends Ability {
    private static final double REACH = 15;
    private final static int SUNSTRIKE_RECOVERY = 20;

    protected BlockRayTraceResult rayTrace;

    public SunstrikeAbility(AbilityType<SunstrikeAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
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
    public boolean tryAbility() {
        super.tryAbility();
        LivingEntity user = getUser();
        BlockRayTraceResult raytrace = rayTrace(user, REACH);
        if (raytrace.getType() == RayTraceResult.Type.BLOCK && raytrace.getFace() == Direction.UP) {
            this.rayTrace = raytrace;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        if (!user.world.isRemote()) {
            BlockPos hit = rayTrace.getPos();
            EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, user.world, user, hit.getX(), hit.getY(), hit.getZ());
            sunstrike.onSummon();
            user.world.addEntity(sunstrike);
        }
    }

    @Override
    public boolean canUse() {
        if (getUser() instanceof PlayerEntity && !((PlayerEntity)getUser()).inventory.getCurrentItem().isEmpty()) return false;
        return getUser().isPotionActive(EffectHandler.SUNS_BLESSING);
    }
}
