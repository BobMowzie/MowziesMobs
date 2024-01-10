package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRibbon;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.BlockAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.DieAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.HurtAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.MeleeAttackAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.easing.EasingType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.EnumSet;

public abstract class EntityUmvuthana extends MowzieGeckoEntity implements RangedAttackMob {
    public static final AbilityType<EntityUmvuthana, DieAbility<EntityUmvuthana>> DIE_ABILITY = new AbilityType<>("umvuthana_die", (type, entity) -> new DieAbility<>(type, entity,"die", 70) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 1) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_HURT.get(), getUser().getSoundVolume(), getUser().getVoicePitch());
            if (getTicksInUse() == 15) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_RETRACT.get(), getUser().getSoundVolume(), 1);
        }
    });
    public static final AbilityType<EntityUmvuthana, UmvuthanaHurtAbility> HURT_ABILITY = new AbilityType<>("umvuthana_hurt", UmvuthanaHurtAbility::new);
    public static final AbilityType<EntityUmvuthana, UmvuthanaAttackAbility> ATTACK_ABILITY = new AbilityType<>("umvuthana_attack", UmvuthanaAttackAbility::new);
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> ALERT_ABILITY = new AbilityType<>("umvuthana_alert", (type, entity) -> new SimpleAnimationAbility<>(type, entity,"alert", 15, true) {
        int soundFrame;

        @Override
        public void start() {
            super.start();
            soundFrame = rand.nextInt(7);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (soundFrame == getTicksInUse()) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ALERT.get(), getUser().getSoundVolume(), getUser().getVoicePitch());
            if (getUser().getTarget() != null) {
                getUser().lookAt(getUser().getTarget(), 30F, 30F);
                getUser().getLookControl().setLookAt(getUser().getTarget(), 30F, 30F);
            }
        }

        @Override
        public void end() {
            super.end();
            if (rand.nextFloat() < 0.2) getUser().sendAbilityMessage(ROAR_ABILITY);
        }
    });
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> ROAR_ABILITY = new AbilityType<>("umvuthana_roar", (type, entity) -> new SimpleAnimationAbility<>(type, entity,"roar", 35, true) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 2) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ROAR.get(), getUser().getSoundVolume() + 0.5f, getUser().getVoicePitch());
        }
    });
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> ACTIVATE_ABILITY = new AbilityType<>("umvuthana_activate", (type, entity) -> new SimpleAnimationAbility<>(type, entity,"emerge", 21) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 5) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_EMERGE.get(), 1, 1);
            if (getTicksInUse() == 10) getUser().active = true;
        }
    });
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> DEACTIVATE_ABILITY = new AbilityType<>("umvuthana_deactivate", (type, entity) -> new SimpleAnimationAbility<>(type, entity,"retract", 11) {
        @Override
        public void end() {
            super.end();
            getUser().discard();
            ItemUmvuthanaMask mask = getMaskFromType(getUser().getMaskType());
            if (!getUser().level.isClientSide) {
                ItemEntity itemEntity = getUser().spawnAtLocation(getUser().getDeactivatedMask(mask), 1.5f);
                if (itemEntity != null) {
                    ItemStack item = itemEntity.getItem();
                    item.setDamageValue((int) Math.ceil((1.0f - getUser().getHealthRatio()) * item.getMaxDamage()));
                    item.setHoverName(getUser().getCustomName());
                }
            }
        }
    });
    public static final AbilityType<EntityUmvuthana, BlockAbility<EntityUmvuthana>> BLOCK_ABILITY = new AbilityType<>("umvuthana_block", (type, entity) -> new BlockAbility<>(type, entity,"block", 10));
    public static final AbilityType<EntityUmvuthana, UmvuthanaBlockCounterAbility> BLOCK_COUNTER_ABILITY = new AbilityType<>("umvuthana_block_counter", UmvuthanaBlockCounterAbility::new);

    public static final AbilityType<EntityUmvuthana, UmvuthanaTeleportAbility> TELEPORT_ABILITY = new AbilityType<>("umvuthana_teleport", UmvuthanaTeleportAbility::new);
    public static final AbilityType<EntityUmvuthana, UmvuthanaHealAbility> HEAL_ABILITY = new AbilityType<>("umvuthana_heal", UmvuthanaHealAbility::new);

    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MASK = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WEAPON = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> HEALPOSX = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALPOSY = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALPOSZ = SynchedEntityData.defineId(EntityUmvuthana.class, EntityDataSerializers.FLOAT);
    private boolean circleDirection = true;
    protected int circleTick = 0;
    protected boolean attacking = false;
    private int ticksWithoutTarget;
    public int timeUntilDeath = -1;
    private int blockCount = 0;

    @OnlyIn(Dist.CLIENT)
    public Vec3[] staffPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] headPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] barakoPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] myPos;

    protected Vec3 teleportDestination;

    private static final byte FOOTSTEP_ID = 69;
    private int footstepCounter = 0;

    int maskTimingOffset = this.random.nextInt(0, 150);
    protected AnimationController<MowzieGeckoEntity> maskController = new MowzieAnimationController<>(this, "mask_controller", 1, this::predicateMask, maskTimingOffset);
    protected MowzieAnimationController<MowzieGeckoEntity> walkRunController = new MowzieAnimationController<>(this, "walk_run_controller", 4, EasingType.EaseInOutQuad, this::predicateWalkRun, 0);

    private float prevMaskRot = 0;
    private boolean rattling = false;

    public EntityUmvuthana(EntityType<? extends EntityUmvuthana> type, Level world) {
        super(type, world);
        setMask(MaskType.from(Mth.nextInt(random, 1, 4)));
        maxUpStep = 1;
        circleTick += random.nextInt(200);
        frame += random.nextInt(50);
        xpReward = 6;
        active = false;

        if (world.isClientSide) {
            staffPos = new Vec3[]{new Vec3(0, 0, 0)};
            barakoPos = new Vec3[]{new Vec3(0, 0, 0)};
            myPos = new Vec3[]{new Vec3(0, 0, 0)};
            headPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -8);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(0, new UseAbilityAI<>(this, ACTIVATE_ABILITY));
        goalSelector.addGoal(0, new UseAbilityAI<>(this, DEACTIVATE_ABILITY));
        goalSelector.addGoal(1, new UseAbilityAI<>(this, DIE_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, HURT_ABILITY, false));
        goalSelector.addGoal(3, new EntityAIAvoidEntity<>(this, EntitySunstrike.class, EntitySunstrike::isStriking, 3, 0.7F));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, ATTACK_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, ALERT_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, ROAR_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, BLOCK_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, BLOCK_COUNTER_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, TELEPORT_ABILITY));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, HEAL_ABILITY, false));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.4));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityUmvuthana.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityUmvuthi.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        goalSelector.addGoal(5, new CircleAttackGoal(this, 6.5F));
        registerTargetGoals();
    }

    protected void registerTargetGoals() {

    };

    protected void registerHuntingTargetGoals() {
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, 200, true, false, target -> {
            float volume = target.getBbWidth() * target.getBbWidth() * target.getBbHeight();
            return (target.getAttribute(Attributes.ATTACK_DAMAGE) == null || target.getAttributeValue(Attributes.ATTACK_DAMAGE) < 3.0D) && volume > 0.1 && volume < 6;
        }));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, 0, true, false, (e) -> !(e instanceof ZombifiedPiglin)));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 0, true, false, null));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zoglin.class, 0, true, false, null));
        this.targetSelector.addGoal(6, new AvoidEntityGoal<>(this, Creeper.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, true, target -> {
            if (target instanceof Player) {
                if (this.level.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((Player) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask);
            }
            return true;
        }));
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(maskController);
        data.addAnimationController(walkRunController);
    }

    protected <E extends IAnimatable> PlayState predicateMask(AnimationEvent<E> event)
    {
        if (isAlive() && active && getActiveAbilityType() != HEAL_ABILITY) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mask_twitch", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    protected <E extends IAnimatable> PlayState predicateWalkRun(AnimationEvent<E> event)
    {
        float threshold = 0.9f;
        Animation currentAnim = event.getController().getCurrentAnimation();
        if (currentAnim != null && currentAnim.animationName.equals("run_switch")) {
            threshold = 0.7f;
        }

        if (event.getLimbSwingAmount() > threshold && !isStrafing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("run_switch", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk_switch", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> void loopingAnimations(AnimationEvent<E> event) {
        if (active) {
            if (isAggressive()) {
                event.getController().transitionLengthTicks = 4;
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("walk_aggressive", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_aggressive", ILoopType.EDefaultLoopTypes.LOOP));
                }
            } else {
                event.getController().transitionLengthTicks = 4;
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("walk_neutral", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_neutral", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
        }
        else {
            event.getController().transitionLengthTicks = 0;
            if (!isOnGround() && !isInLava() && !isInWater()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("tumble", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("inactive", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
//        return new SmartBodyHelper(this);
        return super.createBodyControl();
    }

    @Override
    public AbilityType getDeathAbility() {
        return DIE_ABILITY;
    }

    @Override
    public AbilityType getHurtAbility() {
        return HURT_ABILITY;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getActiveAbilityType() == DEACTIVATE_ABILITY) {
            return null;
        }
        int i = Mth.nextInt(random, 0, MMSounds.ENTITY_UMVUTHANA_IDLE.size());
        if (i < MMSounds.ENTITY_UMVUTHANA_IDLE.size()) {
            return MMSounds.ENTITY_UMVUTHANA_IDLE.get(i).get();
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return active ? MMSounds.ENTITY_UMVUTHANA_HURT.get() : null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.MAX_HEALTH, 8);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        if (canHoldVaryingWeapons()) {
            setWeapon(random.nextInt(3) == 0 ? 1 : 0);
        }
        if (reason == MobSpawnType.COMMAND && !(this instanceof EntityUmvuthanaRaptor) && !(this instanceof EntityUmvuthanaCrane) && !(this instanceof EntityUmvuthanaCraneToPlayer)) setMask(MaskType.from(Mth.nextInt(random, 1, 4)));
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    public int getAnimationTick() {
        return 0;
    }

    protected boolean canHoldVaryingWeapons() {
        return true;
    }

    protected Vec3 updateCirclingPosition(float radius, float speed) {
        LivingEntity target = getTarget();
        if (target != null) {
            if (random.nextInt(200) == 0) {
                circleDirection = !circleDirection;
            }
            if (circleDirection) {
                circleTick++;
            } else {
                circleTick--;
            }
            return circleEntityPosition(target, radius, speed, true, circleTick, 0);
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide()) {
            if (deathTime < 20 && active && !(getActiveAbilityType() == TELEPORT_ABILITY && getActiveAbility().getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY)) {
                if (this.tickTimer() % 10 == 1) {
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW.get(), getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 0.3, 0.4, 1, 9, true, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(headPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.oscillate(9, 10, 12), false)
                    });
                }
                if (headPos != null && headPos.length > 0 && headPos[0] != null) {
                    if (random.nextFloat() < 0.3F) {
                        int amount = random.nextInt(2) + 1;
                        while (amount-- > 0) {
                            float theta = random.nextFloat() * MathUtils.TAU;
                            float r = random.nextFloat() * 0.4F;
                            float x = r * Mth.cos(theta);
                            float z = r * Mth.sin(theta);
                            level.addParticle(ParticleTypes.SMOKE, headPos[0].x() + x, headPos[0].y() + 0.1, headPos[0].z() + z, 0, 0, 0);
                        }
                    }
                }
            }
        }

        if (getActiveAbilityType() != BLOCK_ABILITY && blockCount > 0 && tickCount % 10 == 0) blockCount--;

        if (!level.isClientSide && active && !getActive()) {
            setActive(true);
        }
        active = getActive();
        if (!active) {
            getNavigation().stop();
            setYRot(yRotO);
            yBodyRot = getYRot();
            if ((onGround || isInWater() || isInLava()) && getActiveAbility() == null) {
                sendAbilityMessage(ACTIVATE_ABILITY);
            }
            return;
        }
        if (getActiveAbility() != null) {
            getNavigation().stop();
            yHeadRot = yBodyRot = getYRot();
        }

        if (getTarget() != null && ticksWithoutTarget > 3) {
            sendAbilityMessage(ALERT_ABILITY);
        }

        if (getTarget() == null) {
            ticksWithoutTarget++;
        } else {
            ticksWithoutTarget = 0;
        }

        if (timeUntilDeath > 0) timeUntilDeath--;
        else if (timeUntilDeath == 0) {
            hurt(DamageSource.indirectMagic(this, null), getHealth() + 1);
        }

//        if (getActiveAbility() == null) AbilityHandler.INSTANCE.sendAbilityMessage(this, BLOCK_COUNTER_ABILITY);
    }

    public void updateRattleSound(float maskRot) {
        if (!rattling) {
            if (Math.abs(maskRot - prevMaskRot) > 0.06) {
                level.playLocalSound(getX(), getY(), getZ(), MMSounds.ENTITY_UMVUTHANA_RATTLE.get(), SoundSource.HOSTILE, 0.03f, getVoicePitch(), false);
            }
        }
        else {
            if (Math.abs(maskRot - prevMaskRot) < 0.00000001) {
                rattling = false;
            }
        }
        prevMaskRot = maskRot;
    }

    @Override
    protected float nextStep() {
        if (isOnGround()) this.level.broadcastEntityEvent(this, FOOTSTEP_ID);
        return super.nextStep();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == FOOTSTEP_ID) {
            footstepCounter++;
            float rotation = (float) Math.toRadians(yBodyRot + 180f);
            Vec3 offset = new Vec3(0, 0, footstepCounter % 2 == 0 ? 0.3 : -0.3).yRot(rotation);
            AdvancedParticleBase.spawnParticle(level, ParticleHandler.STRIX_FOOTPRINT.get(), getX() + offset.x(), getY() + 0.01, getZ() + offset.z(), 0, 0, 0, false, rotation, Math.PI/2f, 0, 0, 1F, 1, 0.95, 0.1, 1, 1, 200, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, new ParticleComponent.KeyTrack(
                            new float[]{0.995f, 0.05f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, new ParticleComponent.KeyTrack(
                            new float[]{0.95f, 0.05f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, new ParticleComponent.KeyTrack(
                            new float[]{0.1f, 0.05f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                            new float[]{1f, 0.8f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent() {
                        @Override
                        public void postUpdate(AdvancedParticleBase particle) {
                            super.postUpdate(particle);
                            if (particle.getAge() < 80 && random.nextFloat() < 0.3F) {
                                int amount = 1;
                                while (amount-- > 0) {
                                    float theta = random.nextFloat() * MathUtils.TAU;
                                    float r = random.nextFloat() * 0.2F;
                                    float x = r * Mth.cos(theta);
                                    float z = r * Mth.sin(theta);
                                    level.addParticle(ParticleTypes.SMOKE, particle.getPosX() + x, particle.getPosY() + 0.05, particle.getPosZ() + z, 0, 0, 0);
                                }
                            }
                        }
                    }
            });
        }
        else super.handleEntityEvent(id);
    }

    public static ItemUmvuthanaMask getMaskFromType(MaskType maskType) {
        ItemUmvuthanaMask mask = ItemHandler.UMVUTHANA_MASK_FURY;
        switch (maskType) {
            case BLISS:
                mask = ItemHandler.UMVUTHANA_MASK_BLISS;
                break;
            case FEAR:
                mask = ItemHandler.UMVUTHANA_MASK_FEAR;
                break;
            case FURY:
                mask = ItemHandler.UMVUTHANA_MASK_FURY;
                break;
            case MISERY:
                mask = ItemHandler.UMVUTHANA_MASK_MISERY;
                break;
            case RAGE:
                mask = ItemHandler.UMVUTHANA_MASK_RAGE;
                break;
            case FAITH:
                mask = ItemHandler.UMVUTHANA_MASK_FAITH;
                break;
        }
        return mask;
    }

    protected ItemStack getDeactivatedMask(ItemUmvuthanaMask mask) {
        return new ItemStack(mask);
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_UMVUTHANA_DIE.get(), 1f, 0.95f + random.nextFloat() * 0.1f);
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(DANCING, false);
        getEntityData().define(MASK, 0);
        getEntityData().define(WEAPON, 0);
        getEntityData().define(ACTIVE, true);
        getEntityData().define(HEALPOSX, 0f);
        getEntityData().define(HEALPOSY, 0f);
        getEntityData().define(HEALPOSZ, 0f);
    }

    public boolean getDancing() {
        return getEntityData().get(DANCING);
    }

    public void setDancing(boolean dancing) {
        getEntityData().set(DANCING, dancing);
    }

    public MaskType getMaskType() {
        return MaskType.from(getEntityData().get(MASK));
    }

    public void setMask(MaskType type) {
        getEntityData().set(MASK, type.ordinal());
        setItemSlot(EquipmentSlot.HEAD, getMaskFromType(type).getDefaultInstance());
    }

    public int getWeapon() {
        return getEntityData().get(WEAPON);
    }

    public void setWeapon(int type) {
        getEntityData().set(WEAPON, type);
    }

    public boolean getActive() {
        return getEntityData().get(ACTIVE);
    }

    public void setActive(boolean active) {
        getEntityData().set(ACTIVE, active);
    }

    public Vec3 getHealPos() {
        return new Vec3(getEntityData().get(HEALPOSX), getEntityData().get(HEALPOSY), getEntityData().get(HEALPOSZ));
    }

    public void setHealPos(Vec3 vec) {
        getEntityData().set(HEALPOSX, (float) vec.x);
        getEntityData().set(HEALPOSY, (float) vec.y);
        getEntityData().set(HEALPOSZ, (float) vec.z);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("mask", getMaskType().ordinal());
        compound.putInt("weapon", getWeapon());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setMask(MaskType.from(compound.getInt("mask")));
        setWeapon(compound.getInt("weapon"));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        AbstractArrow dart = new EntityDart(EntityHandler.DART.get(), this.level, this);
        Vec3 targetPos = target.position();
        double dx = targetPos.x() - this.getX();
        double dy = target.getBoundingBox().minY + (double)(target.getBbHeight() / 3.0F) - dart.position().y();
        double dz = targetPos.z() - this.getZ();
        double dist = Mth.sqrt((float) (dx * dx + dz * dz));
        dart.shoot(dx, dy + dist * 0.2D, dz, 1.6F, 1);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, this.getItemInHand(InteractionHand.MAIN_HAND));
        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, this.getItemInHand(InteractionHand.MAIN_HAND));
        dart.setBaseDamage((double) (p_82196_2_ * 2.0F) + this.random.nextGaussian() * 0.25D + (double) ((float) this.level.getDifficulty().getId() * 0.11F));

        if (i > 0) {
            dart.setBaseDamage(dart.getBaseDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            dart.setKnockback(j);
        }

        dart.setBaseDamage(dart.getBaseDamage() * ConfigHandler.COMMON.MOBS.UMVUTHANA.combatConfig.attackMultiplier.get());

        this.level.addFreshEntity(dart);
        attacking = false;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (getActiveAbilityType() == DEACTIVATE_ABILITY) {
            return false;
        }
        Entity entity = source.getEntity();
        if (source == DamageSource.HOT_FLOOR) return false;
        boolean angleFlag = true;
        if (entity != null) {
            int arc = 220;
            Vec3 entityPos = entity.position();
            float entityHitAngle = (float) ((Math.atan2(entityPos.z() - getZ(), entityPos.x() - getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            angleFlag = (entityRelativeAngle <= arc / 2.0 && entityRelativeAngle >= -arc / 2.0) || (entityRelativeAngle >= 360 - arc / 2.0 || entityRelativeAngle <= -arc + 90 / 2.0);
        }
        if (angleFlag && getMaskType().canBlock && entity instanceof LivingEntity && (getActiveAbility() == null || getActiveAbilityType() == HURT_ABILITY || getActiveAbilityType() == BLOCK_ABILITY) && !source.isBypassArmor()) {
            blockingEntity = (LivingEntity) entity;
            playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
            if (blockingEntity == getTarget() && random.nextFloat() < Mth.clamp(blockCount / 5.0, 0.0, 1.0) && distanceTo(blockingEntity) < 4) {
                AbilityHandler.INSTANCE.sendAbilityMessage(this, BLOCK_COUNTER_ABILITY);
                blockCount = 0;
            }
            else {
                AbilityHandler.INSTANCE.sendAbilityMessage(this, BLOCK_ABILITY);
                blockCount++;
            }
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        switch (getMaskType()) {
            case BLISS:
                return LootTableHandler.UMVUTHANA_BLISS;
            case FEAR:
                return LootTableHandler.UMVUTHANA_FEAR;
            case FURY:
                return LootTableHandler.UMVUTHANA_FURY;
            case MISERY:
                return LootTableHandler.UMVUTHANA_MISERY;
            case RAGE:
                return LootTableHandler.UMVUTHANA_RAGE;
            case FAITH:
                return LootTableHandler.UMVUTHANA_FAITH;
        }
        return LootTableHandler.UMVUTHANA_FURY;
    }

    @Override
    public boolean isPickable() {
        return active;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultipler, DamageSource source) {
        if (active) {
            return super.causeFallDamage(distance, damageMultipler, source);
        }
        return false;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.UMVUTHANA.combatConfig;
    }

    public boolean isUmvuthiDevoted() {
        return true;
    }

    public int randomizeWeapon() {
        return random.nextInt(3) == 0 ? 1 : 0;
    }

    public boolean canHeal(LivingEntity entity) {
        return false;
    }

    protected void sunBlockTarget() {

    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[] { DIE_ABILITY, HURT_ABILITY, ATTACK_ABILITY, ALERT_ABILITY, ROAR_ABILITY, ACTIVATE_ABILITY, DEACTIVATE_ABILITY, BLOCK_ABILITY, BLOCK_COUNTER_ABILITY, TELEPORT_ABILITY, HEAL_ABILITY };
    }

    protected static class CircleAttackGoal extends Goal {
        private final EntityUmvuthana mob;
        private final float attackRadius;
        private int strafingLeftRightMul;
        private int strafingFrontBackMul;
        private boolean chasing = false;

        protected boolean attacking = false;
        private int timeSinceAttack = 0;

        public CircleAttackGoal(EntityUmvuthana mob, float attackRadius) {
            this.mob = mob;
            this.attackRadius = attackRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone());
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
            timeSinceAttack = mob.random.nextInt(80);
        }

        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            mob.setStrafing(false);
            this.mob.getMoveControl().strafe(0, 0);
            attacking = false;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                if (timeSinceAttack < 80) {
                    timeSinceAttack++;
                }

                double distToTarget = this.mob.distanceTo(target);

                float frontBackDistBuffer = 2f;
                float leftRightDistBuffer = 1.5f;
                if (chasing && distToTarget <= attackRadius) {
                    chasing = false;
                }
                if (!chasing && distToTarget >= attackRadius + frontBackDistBuffer) {
                    chasing = true;
                }

                // Chasing
                if (chasing) {
                    this.mob.getNavigation().moveTo(target, 0.6);
                    this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    mob.setStrafing(false);
                    this.mob.getMoveControl().strafe(0, 0);
                }
                else {
                    // In range
                    if (!attacking && mob.getActiveAbility() == null) {
                        this.mob.getNavigation().stop();
                        float strafeSpeed = 0.5f;
                        Vec3 circlePos = mob.updateCirclingPosition(this.attackRadius, strafeSpeed - 0.2f);
                        double distToCirclePos = this.mob.position().distanceTo(circlePos);

                        if (distToCirclePos <= leftRightDistBuffer) {
                            mob.setStrafing(true);

                            if (distToTarget > this.attackRadius + 0.5) {
                                this.strafingFrontBackMul = 1;
                            } else if (distToTarget < this.attackRadius - 0.5) {
                                this.strafingFrontBackMul = -1;
                            } else {
                                this.strafingFrontBackMul = 0;
                            }

                            Vec3 toTarget = target.position().subtract(this.mob.position()).multiply(1, 0, 1).normalize();
                            Vec3 toCirclePos = circlePos.subtract(this.mob.position()).multiply(1, 0, 1).normalize();
                            Vec3 cross = toTarget.cross(toCirclePos);
                            if (cross.y > 0) strafingLeftRightMul = 1;
                            else if (cross.y < 0) strafingLeftRightMul = -1;
                            else strafingLeftRightMul = 0;

                            float distScale = (float) Math.min(Math.pow(distToCirclePos * 1f / leftRightDistBuffer, 0.7), 1.0);

                            this.mob.getMoveControl().strafe(this.strafingFrontBackMul * strafeSpeed, this.strafingLeftRightMul * strafeSpeed * distScale);
                            this.mob.lookAt(target, 30.0F, 30.0F);

                            if (mob.random.nextFloat() < 0.002) {
                                mob.sendAbilityMessage(ROAR_ABILITY);
                            }
                        } else {
                            mob.setStrafing(false);
                            this.mob.getMoveControl().strafe(0, 0);
                            this.mob.getNavigation().moveTo(circlePos.x, circlePos.y, circlePos.z, 0.53);
                            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                        }
                    }
                    else {
                        this.mob.getMoveControl().strafe(0, 0);
                        mob.setStrafing(false);
                    }

                    // Attacking logic
                    if (mob.random.nextInt(80) == 0 && timeSinceAttack >= 80 && mob.getSensing().hasLineOfSight(target)) {
                        attacking = true;
                    }
                    if (attacking && mob.getActiveAbility() == null) {
                        mob.getNavigation().moveTo(target, 0.5);
                        if (distToTarget <= 3.75 && mob.getSensing().hasLineOfSight(target)) {
                            attacking = false;
                            timeSinceAttack = 0;
                            AbilityHandler.INSTANCE.sendAbilityMessage(mob, ATTACK_ABILITY);
                        }
                    }
                }
            }
        }
    }

    private static class UmvuthanaAttackAbility extends MeleeAttackAbility<EntityUmvuthana> {

        public UmvuthanaAttackAbility(AbilityType<EntityUmvuthana, ? extends MeleeAttackAbility<EntityUmvuthana>> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new String[]{"attack_slash_left", "attack_slash_right"}, null, null, 1, 3.0f, 1, 13, 9, true);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 5) getUser().setDeltaMovement(getUser().getDeltaMovement().add(getUser().getForward().normalize().scale(0.5)));
            if (getTicksInUse() == 1) {
                int i = rand.nextInt(MMSounds.ENTITY_UMVUTHANA_ATTACK.size());
                getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ATTACK.get(i).get(), 1, rand.nextFloat(0.9f, 1.1f));
            }
        }
    }

    private static class UmvuthanaBlockCounterAbility extends MeleeAttackAbility<EntityUmvuthana> {

        public UmvuthanaBlockCounterAbility(AbilityType<EntityUmvuthana, UmvuthanaBlockCounterAbility> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new String[]{"block_counter"}, null, null, 3, 2.2f, 1.2f, 7, 11, false);
        }

        @Override
        public void start() {
            super.start();
            getUser().setInvulnerable(true);
        }

        @Override
        public void end() {
            super.end();
            getUser().setInvulnerable(false);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 5) {
                float distToTarget = 1.0f;
                if (getUser().getTarget() != null) distToTarget = Mth.clamp(getUser().distanceTo(getUser().getTarget()) / 2.0f - 1.0f, 0.0f, 1.0f);
                getUser().setDeltaMovement(getUser().getDeltaMovement().add(getUser().getForward().normalize().scale(1.6 * distToTarget)));
            }
            if (getTicksInUse() == 0) {
                getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ATTACK_BIG.get(), 1, rand.nextFloat(0.9f, 1.1f));
            }
        }

        @Override
        public boolean canCancelActiveAbility() {
            return super.canCancelActiveAbility() || getUser().getActiveAbility() instanceof BlockAbility<?> || getUser().getActiveAbility() instanceof HurtAbility<?>;
        }
    }

    private static class UmvuthanaHurtAbility extends HurtAbility<EntityUmvuthana> {

        public UmvuthanaHurtAbility(AbilityType<EntityUmvuthana, UmvuthanaHurtAbility> abilityType, EntityUmvuthana user) {
            super(abilityType, user, "", 12);
        }

        @Override
        public void start() {
            super.start();
            if (getUser().isAggressive()) {
                if (getUser().random.nextBoolean()) {
                    playAnimation("hurt_right_aggressive", false);
                }
                else {
                    playAnimation("hurt_left_aggressive", false);
                }
            }
            else {
                if (getUser().random.nextBoolean()) {
                    playAnimation("hurt_right_neutral", false);
                }
                else {
                    playAnimation("hurt_left_neutral", false);
                }
            }
        }
    }

    private static class UmvuthanaTeleportAbility extends Ability<EntityUmvuthana> {
        private Vec3 teleportStart;
        private static int ACTIVE_DURATION = 7;

        public UmvuthanaTeleportAbility(AbilityType<EntityUmvuthana, ? extends Ability> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 7),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, ACTIVE_DURATION),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 13)
            });
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                playAnimation("teleport_start", false);
            }
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                teleportStart = getUser().position();
                playAnimation("teleport_loop", true);
            }
            if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
                playAnimation("teleport_end", false);
            }
        }

        @Override
        protected void endSection(AbilitySection section) {
            super.endSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE && getUser().teleportDestination != null) {
                getUser().teleportTo(getUser().teleportDestination.x(), getUser().teleportDestination.y(), getUser().teleportDestination.z());
                getUser().setDeltaMovement(0, 0, 0);
                getUser().getNavigation().stop();
            }
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 2) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_TELEPORT.get(rand.nextInt(3)).get(), 3f, 1);
            if (getTicksInUse() == 16) getUser().playSound(MMSounds.ENTITY_UMVUTHANA_TELEPORT.get(rand.nextInt(3)).get(), 3f, 1.2f);

            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (teleportStart != null && getUser().teleportDestination != null) {
                    float t = getTicksInSection() / (float) (ACTIVE_DURATION);
                    t = (float) (0.5 - 0.5 * Math.cos(t * Math.PI));
                    Vec3 newPos = teleportStart.add(getUser().teleportDestination.subtract(teleportStart).scale(t));
                    getUser().teleportTo(newPos.x(), newPos.y(), newPos.z());
                    getUser().getNavigation().stop();
                }
            }

            if (getUser().getTarget() != null) getUser().getLookControl().setLookAt(getUser().getTarget(), 30, 30);

            if (getUser().level.isClientSide) {
                getUser().myPos[0] = getUser().position().add(0, 1.2f, 0);
                if (getTicksInUse() == 5) {
                    ParticleComponent.KeyTrack keyTrack1 = ParticleComponent.KeyTrack.oscillate(0, 2, 24);
                    ParticleComponent.KeyTrack keyTrack2 = new ParticleComponent.KeyTrack(new float[]{0, 18, 18, 0}, new float[]{0, 0.2f, 0.8f, 1});
                    AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.SUN.get(), getUser().getX(), getUser().getY(), getUser().getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 15, true, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(getUser().myPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack2, false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack1, true),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                    });
                }
                getUser().myPos[0] = getUser().position().add(0, 1.2f, 0);
                if (getTicksInUse() == 4 || getTicksInUse() == 18) {
                    int num = 5;
                    for (int i = 0; i < num * num; i++) {
                        Vec3 v = new Vec3((0.3 + 0.15 * rand.nextFloat()) * 0.8, 0, 0);
                        float increment = (float)Math.PI * 2f / (float) num;
//                        v = v.rotatePitch(increment * i);
                        v = v.yRot(increment * rand.nextFloat() + increment * (i / (float)num));
                        v = v.zRot(increment * rand.nextFloat() + increment * (i % num));
                        AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.PIXEL.get(), getUser().myPos[0].x(), getUser().myPos[0].y(), getUser().myPos[0].z(), v.x(), v.y(), v.z(), true, 0, 0, 0, 0, 4f, 0.98, 0.94, 0.39, 1, 0.8, 6 + rand.nextFloat() * 4, true, false, new ParticleComponent[] {
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                        new float[] {4f, 0},
                                        new float[] {0.8f, 1}
                                ), false)
                        });
                    }
                }
            }
        }

        @Override
        public boolean canCancelActiveAbility() {
            return getUser().getActiveAbility() instanceof UmvuthanaHealAbility;
        }
    }

    private static class UmvuthanaHealAbility extends Ability<EntityUmvuthana> {

        public UmvuthanaHealAbility(AbilityType<EntityUmvuthana, ? extends Ability> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP,  6),
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 16)
            });
        }

        @Override
        protected void beginSection(AbilitySection section) {
            if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                playAnimation("heal_start", false);
            }
            else if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            }
            else {
                playAnimation("heal_end", false);
            }
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getUser().getTarget() != null) {
                getUser().getLookControl().setLookAt(getUser().getTarget(), getUser().getMaxHeadYRot(), getUser().getMaxHeadXRot());
                getUser().lookAt(getUser().getTarget(), getUser().getMaxHeadYRot(), getUser().getMaxHeadXRot());
            }
            if (getTicksInUse() == 6) {
                getUser().playSound(MMSounds.ENTITY_UMVUTHANA_HEAL_START.get(rand.nextInt(3)).get(), 4, 1);
                MowziesMobs.PROXY.playSunblockSound(getUser());
            }

            if (getTicksInUse() >= 6) {
                EffectHandler.addOrCombineEffect(getUser(), MobEffects.GLOWING, 5, 0, false, false);
            }

            if (getUser().level.isClientSide && getTicksInUse() == 5 && getUser().headPos != null && getUser().headPos.length >= 1)
                getUser().headPos[0] = getUser().position().add(0, getUser().getEyeHeight(), 0);

            if (getTicksInUse() == 12) {
                playAnimation("heal_loop", true);
            }

            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                spawnHealParticles();
                getUser().sunBlockTarget();
                if (!getLevel().isClientSide() && getUser().getTarget() == null) {
                    AbilityHandler.INSTANCE.sendJumpToSectionMessage(getUser(), this.getAbilityType(), 2);
                }
            }
        }

        public void spawnHealParticles() {
            if (getUser().getTarget() != null) {
                getUser().setHealPos(getUser().getTarget().position().add(new Vec3(0, getUser().getTarget().getBbHeight() / 2f, 0)));
            }
            if (getUser().level.isClientSide && getUser().barakoPos != null) {
                getUser().barakoPos[0] = getUser().getHealPos();
                if (getUser().headPos != null && getUser().headPos[0] != null) {
                    double dist = Math.max(getUser().barakoPos[0].distanceTo(getUser().headPos[0]), 0.01);
                    double radius = 0.5f;
                    double yaw = rand.nextFloat() * 2 * Math.PI;
                    double pitch = rand.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    if (getTicksInUse() % 5 == 0) AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.ARROW_HEAD.get(), getUser().headPos[0].x(), getUser().headPos[0].y(), getUser().headPos[0].z(), 0, 0, 0, false, 0, 0, 0, 0, 3.5F, 0.95, 0.9, 0.35, 0.75, 1, Math.min(2 * dist, 60), true, false, new ParticleComponent[]{
                            new ParticleComponent.Attractor(getUser().barakoPos, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_X, new ParticleComponent.Oscillator(0, (float) ox, (float) (1 * dist), 2.5f), true),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Y, new ParticleComponent.Oscillator(0, (float) oy, (float) (1 * dist), 2.5f), true),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Z, new ParticleComponent.Oscillator(0, (float) oz, (float) (1 * dist), 2.5f), true),
                            new ParticleComponent.FaceMotion(),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                    });
                    if (getTicksInUse() % 5 == 0) AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), getUser().headPos[0].x(), getUser().headPos[0].y(), getUser().headPos[0].z(), 0, 0, 0, true, 0, 0, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
                    });
                    int spawnFreq = 5;
                    if (getTicksInUse() % spawnFreq == 0) ParticleRibbon.spawnRibbon(getUser().level, ParticleHandler.RIBBON_SQUIGGLE.get(), (int)(0.5 * dist), getUser().headPos[0].x(), getUser().headPos[0].y(), getUser().headPos[0].z(), 0, 0, 0, true, 0, 0, 0, 0.5F, 0.95, 0.9, 0.35, 0.75, 1, spawnFreq, true, new ParticleComponent[]{
                            new RibbonComponent.BeamPinning(getUser().headPos, getUser().barakoPos),
                            new RibbonComponent.PanTexture(0, 1)
                    });
                }
            }
        }

        @Override
        public boolean damageInterrupts() {
            return true;
        }
    }
}
