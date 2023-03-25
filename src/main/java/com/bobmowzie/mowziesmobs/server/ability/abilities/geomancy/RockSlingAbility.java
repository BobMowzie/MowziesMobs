package com.bobmowzie.mowziesmobs.server.ability.abilities.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderProjectile;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public class RockSlingAbility extends PlayerAbility {
    public static final double SPAWN_BOULDER_REACH = 5;
    public BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    public Vec3 lookPos = new Vec3(0, 0, 0);
    private BlockState spawnBoulderBlock = Blocks.DIRT.defaultBlockState();
    private int damage = 3;

    public RockSlingAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 5),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 10),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 5)
        });
    }

    @Override
    public void start() {
        super.start();
        Vec3 from = getUser().getEyePosition(1.0f);
        Vec3 to = from.add(getUser().getLookAngle().scale(SPAWN_BOULDER_REACH));
        BlockHitResult result = getUser().level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, getUser()));
        if (result.getType() == HitResult.Type.BLOCK) {
            this.lookPos = result.getLocation();
        }

        spawnBoulderPos = result.getBlockPos();
        this.spawnBoulderBlock = getUser().level.getBlockState(spawnBoulderPos);
        playAnimation("rock_sling_right", false);

        if(getUser().level.isClientSide()){
            AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), (float) getUser().getX(), (float) getUser().getY() + 0.01f, (float) getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, (0.8f + 2.7f * 20f / 60f) * 10f), false)
            });
        }
        else{
            for(int i = 0; i < 3; i++) {
                Vec3 spawnPos = new Vec3(0D, -1D, 2D).yRot((float) Math.toRadians(-getUser().getYRot())).yRot((float) Math.toRadians(-90 + (i * 80))).add(getUser().position());
                EntityRockSling boulder = new EntityRockSling(EntityHandler.BOULDER_PROJECTILE.get(), getUser().level, getUser(), spawnBoulderBlock, spawnBoulderPos, EntityGeomancyBase.GeomancyTier.values()[1]);
                boulder.setPos(spawnPos.x() + 0.5F, spawnPos.y() + 2, spawnPos.z() + 0.5F);
                boulder.setLaunchVec(getUser().getViewVector(1f).multiply(1f,0.9f,1f));
                boulder.setTravelling(true);
                boulder.setDamage(3);
                if (!getUser().level.isClientSide && boulder.checkCanSpawn()) {
                    getUser().level.addFreshEntity(boulder);
                }
            }
        }
    }
}
