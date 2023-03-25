package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntityRockSling extends EntityBoulderProjectile{

    private Vec3 launchVec;


    public EntityRockSling(EntityType<? extends EntityBoulderProjectile> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    @Override
    public void tick() {
        super.tick();

        if(tickCount > 30 + random.nextInt(35)) {
            setDeltaMovement(launchVec.normalize().multiply(2f + random.nextFloat()/5, 2f, 2f + random.nextFloat()/5));

        }

    }

    public void setLaunchVec(Vec3 vec){
        this.launchVec = vec;
    }


}
