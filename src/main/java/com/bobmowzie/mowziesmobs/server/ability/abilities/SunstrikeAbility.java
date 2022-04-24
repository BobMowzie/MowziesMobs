package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.BlockHitResult;
import net.minecraft.util.RayTraceContext;
import net.minecraft.util.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SunstrikeAbility extends Ability {
    private static final double REACH = 15;
    private final static int SUNSTRIKE_RECOVERY = 20;

    protected BlockHitResult rayTrace;

    public SunstrikeAbility(AbilityType<SunstrikeAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SUNSTRIKE_RECOVERY)
        });
    }

    private static BlockHitResult rayTrace(LivingEntity entity, double reach) {
        Vec3 pos = entity.getEyePosition(0);
        Vec3 segment = entity.getLookVec();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.world.rayTraceBlocks(new RayTraceContext(pos, segment, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
    }

    @Override
    public boolean tryAbility() {
        super.tryAbility();
        LivingEntity user = getUser();
        BlockHitResult raytrace = rayTrace(user, REACH);
        if (raytrace.getType() == HitResult.Type.BLOCK && raytrace.getFace() == Direction.UP) {
            this.rayTrace = raytrace;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        if (!user.level.isClientSide()) {
            BlockPos hit = rayTrace.getPos();
            EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, user.world, user, hit.x(), hit.y(), hit.z());
            sunstrike.onSummon();
            user.level.addFreshEntity(sunstrike);
        }
        playAnimation("sunstrike", false);
    }

    @Override
    public boolean canUse() {
        if (getUser() instanceof Player && !((Player)getUser()).inventory.getCurrentItem().isEmpty()) return false;
        return getUser().isPotionActive(EffectHandler.SUNS_BLESSING) && super.canUse();
    }

    @Override
    public boolean preventsBlockBreakingBuilding() {
        return false;
    }

    @Override
    public boolean preventsAttacking() {
        return false;
    }

    @Override
    public boolean preventsInteracting() {
        return false;
    }
}
