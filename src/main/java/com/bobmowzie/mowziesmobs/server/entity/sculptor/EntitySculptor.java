package com.bobmowzie.mowziesmobs.server.entity.sculptor;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderPlatform;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class EntitySculptor extends MowzieGeckoEntity {
    public static int TEST_HEIGHT = 40;
    public static int TEST_RADIUS = 16;

    public static final AbilityType<EntitySculptor, StartTestAbility> START_TEST = new AbilityType<>("testStart", StartTestAbility::new);
    public static final AbilityType<EntitySculptor, EndTestAbility> END_TEST = new AbilityType<>("testEnd", EndTestAbility::new);

    public boolean handLOpen = true;
    public boolean handROpen = true;

    private EntityPillar pillar;

    public EntitySculptor(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, START_TEST));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (getActiveAbility() == null) {
            if (pillar == null && onGround) sendAbilityMessage(START_TEST);
//            else sendAbilityMessage(END_TEST);
        }
    }

    public EntityPillar getPillar() {
        return pillar;
    }

    private <ENTITY extends IAnimatable> void instructionListener(CustomInstructionKeyframeEvent<ENTITY> event) {
        if (event.instructions.contains("closeHandR")) {
            handROpen = false;
        }
        if (event.instructions.contains("closeHandL")) {
            handLOpen = false;
        }
        if (event.instructions.contains("openHandR")) {
            handROpen = true;
        }
        if (event.instructions.contains("openHandL")) {
            handLOpen = true;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        controller.registerCustomInstructionListener(this::instructionListener);
    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[] {START_TEST, END_TEST};
    }

    public static class StartTestAbility extends Ability<EntitySculptor> {
        private static int MAX_RANGE_TO_GROUND = 12;

        private BlockPos spawnPillarPos;
        private BlockState spawnPillarBlock;
        private EntityPillar pillar;

        public StartTestAbility(AbilityType<EntitySculptor, StartTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 18),
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
            });
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
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            playAnimation("testStart", false);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE && spawnPillarPos != null) {
                if (spawnPillarBlock == null || !EffectGeomancy.isBlockDiggable(spawnPillarBlock)) spawnPillarBlock = Blocks.STONE.defaultBlockState();
                pillar = new EntityPillar(EntityHandler.PILLAR.get(), getUser().level, getUser(), Blocks.STONE.defaultBlockState(), spawnPillarPos);
                pillar.setTier(EntityGeomancyBase.GeomancyTier.SMALL);
                pillar.setPos(spawnPillarPos.getX() + 0.5F, spawnPillarPos.getY() + 1, spawnPillarPos.getZ() + 0.5F);
                pillar.setDoRemoveTimer(false);
                if (!getUser().level.isClientSide && pillar.checkCanSpawn()) {
                    getUser().level.addFreshEntity(pillar);
                }
                getUser().pillar = pillar;

                BlockPos spawnBoulderPos = spawnPillarPos.north(6);
                EntityBoulderPlatform boulderPlatform = new EntityBoulderPlatform(EntityHandler.BOULDER_PLATFORM.get(), getUser().getLevel(), getUser(), Blocks.STONE.defaultBlockState(), spawnBoulderPos, EntityGeomancyBase.GeomancyTier.MEDIUM);
                boulderPlatform.setPos(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F);
                getUser().getLevel().addFreshEntity(boulderPlatform);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE && pillar != null && pillar.getHeight() > TEST_HEIGHT) {
                nextSection();
            }
        }

        @Override
        public void end() {
            super.end();
            if (pillar != null) pillar.stopRising();
        }
    }

    public static class EndTestAbility extends Ability<EntitySculptor> {

        public EndTestAbility(AbilityType<EntitySculptor, EndTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 20)
            });
        }

        @Override
        public boolean tryAbility() {
            return getUser().pillar != null;
        }

        @Override
        public void start() {
            super.start();
            playAnimation("testStart", false);
            if (getUser().pillar != null) getUser().pillar.startFalling();
        }

        @Override
        public void tick() {
            super.tick();
            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (getUser().pillar == null || getUser().pillar.isRemoved()) nextSection();
            }
        }

        @Override
        public void end() {
            super.end();
            if (getUser() != null) getUser().pillar = null;
        }
    }
}
