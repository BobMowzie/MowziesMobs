package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EntityRockSling extends EntityBoulderProjectile implements IAnimatable, IAnimationTickable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private Vec3 launchVec;

    public EntityRockSling(EntityType<? extends EntityRockSling> type, Level worldIn) {
        super(type, worldIn);
    }


    public EntityRockSling(EntityType<? extends EntityBoulderProjectile> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    @Override
    public void tick() {
        super.tick();

        if(tickCount > 30 + random.nextInt(35) && launchVec != null) {
            setDeltaMovement(launchVec.normalize().multiply(2f + random.nextFloat()/5, 2f, 2f + random.nextFloat()/5));

        }

    }

    public void setLaunchVec(Vec3 vec){
        this.launchVec = vec;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<EntityRockSling> controller = new AnimationController<>(this, "controller", 0,
                event -> {
                    event.getController()
                            .setAnimation(new AnimationBuilder().addAnimation("roll", ILoopType.EDefaultLoopTypes.LOOP));
                    return PlayState.CONTINUE;
                });
        data.addAnimationController(controller);
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }
}
