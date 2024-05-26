package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.DieAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.HurtAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.LookAtTargetGoal;
import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.*;

public class EntityUmvuthi extends MowzieGeckoEntity implements LeaderSunstrikeImmune, Enemy {
    public static final AbilityType<EntityUmvuthi, DieAbility<EntityUmvuthi>> DIE_ABILITY = new AbilityType<>("umvuthi_die", (type, entity) -> new DieAbility<>(type, entity, RawAnimation.begin().thenPlay("death"), 115) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 1) getUser().playSound(MMSounds.ENTITY_UMVUTHI_HURT.get(), getUser().getSoundVolume(), getUser().getVoicePitch());
            if (getTicksInUse() == 14) getUser().playSound(MMSounds.ENTITY_UMVUTHI_DIE.get(), getUser().getSoundVolume(), 1);
            if (getTicksInUse() == 80) getUser().playSound(MMSounds.MISC_METAL_IMPACT.get(), getUser().getSoundVolume(), 1);
        }
    });
    public static final AbilityType<EntityUmvuthi, HurtAbility<EntityUmvuthi>> HURT_ABILITY = new AbilityType<>("umvuthi_hurt", (type, entity) -> new HurtAbility<>(type, entity,RawAnimation.begin().thenPlay("hurt"), 13));
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> BELLY_ABILITY = new AbilityType<>("umvuthi_belly", (type, entity) -> new SimpleAnimationAbility<>(type, entity,RawAnimation.begin().thenPlay("belly_drum"), 40, true) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 9 || getTicksInUse() == 29) {
                getUser().playSound(MMSounds.ENTITY_UMVUTHI_BELLY.get(), 3f, 1f);
            }
        }
    });
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> TALK_ABILITY = new AbilityType<>("umvuthi_talk", (type, entity) -> new SimpleAnimationAbility<>(type, entity,RawAnimation.begin().thenPlay("talk"), 23, true));
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> ROAR_ABILITY = new AbilityType<>("umvuthi_roar", (type, entity) -> new SimpleAnimationAbility<>(type, entity,RawAnimation.begin().thenPlay("roar"), 70, false) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 2) {
                getUser().playSound(MMSounds.ENTITY_UMVUTHI_ROAR.get(), 3f, 1f);
            }
        }
    });
    public static final AbilityType<EntityUmvuthi, SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>("umvuthi_sunstrike", SunstrikeAbility::new);
    public static final AbilityType<EntityUmvuthi, SolarFlareAbility> SOLAR_FLARE_ABILITY = new AbilityType<>("umvuthi_flare", SolarFlareAbility::new);
    public static final AbilityType<EntityUmvuthi, SpawnFollowersAbility> SPAWN_ABILITY = new AbilityType<>("umvuthi_spawn", (type, entity) -> new SpawnFollowersAbility(type, entity, false));
    public static final AbilityType<EntityUmvuthi, SpawnFollowersAbility> SPAWN_SUNBLOCKERS_ABILITY = new AbilityType<>("umvuthi_spawn_healers", (type, entity) -> new SpawnFollowersAbility(type, entity, true));
    public static final AbilityType<EntityUmvuthi, SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>("umvuthi_solar_beam", SolarBeamAbility::new);
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> BLESS_ABILITY = new AbilityType<>("umvuthi_bless", (type, entity) -> new SimpleAnimationAbility<>(type, entity,RawAnimation.begin().thenPlay("bless"), 84) {
        @Override
        public boolean canCancelActiveAbility() {
            return getUser().getActiveAbilityType() == ROAR_ABILITY || getUser().getActiveAbilityType() == TALK_ABILITY || getUser().getActiveAbilityType() == BELLY_ABILITY;
        }
    });
    public static final AbilityType<EntityUmvuthi, SupernovaAbility> SUPERNOVA_ABILITY = new AbilityType<>("umvuthi_supernova", SupernovaAbility::new);

    protected AnimationController<MowzieGeckoEntity> maskController = new MowzieAnimationController<>(this, "mask_controller", 1, this::predicateMask, 0.0);
    protected AnimationController<MowzieGeckoEntity> blinkController = new MowzieAnimationController<>(this, "blink_controller", 1, this::predicateBlink, 0.0);

    private static final int MAX_HEALTH = 150;
    private static final int SUNSTRIKE_PAUSE_MAX = 50;
    private static final int SUNSTRIKE_PAUSE_MIN = 30;
    private static final int LASER_PAUSE = 230;
    private static final int SUPERNOVA_PAUSE = 230;
    private static final int UMVUTHANA_PAUSE = 200;
    private static final int ROAR_PAUSE = 300;
    private static final int HEAL_PAUSE = 75;
    private static final int HEALTH_LOST_BETWEEN_SUNBLOCKERS = 45;
    private static final EntityDataAccessor<Integer> DIRECTION = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DIALOGUE = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DESIRES = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<CompoundTag> TRADED_PLAYERS = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Float> HEALTH_LOST = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Optional<UUID>> MISBEHAVED_PLAYER = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> IS_TRADING = SynchedEntityData.defineId(EntityUmvuthi.class, EntityDataSerializers.BOOLEAN);
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    public ControlledAnimation angryEyebrow = new ControlledAnimation(5);
    private Player customer;
    public int umvuthanaSpawnCount = 0;
    // TODO: use Direction!
    private int direction = 0;
    private boolean blocksByFeet = true;
    private int timeUntilSunstrike = 0;
    private int timeUntilLaser = 0;
    private int timeUntilUmvuthana = 0;
    private int timeUntilRoar = 0;
    private int timeUntilSupernova = 0;
    private int timeUntilHeal = 0;
    public Player blessingPlayer;
    private UmvuthanaHurtByTargetAI hurtByTargetAI;

    @OnlyIn(Dist.CLIENT)
    public Vec3[] betweenHandPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] headPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] blessingPlayerPos;

    private static final TargetingConditions GIVE_ACHIEVEMENT_PRED = TargetingConditions.forCombat().ignoreInvisibilityTesting();

    private float prevMaskRot = 0;
    private boolean rattling = false;

    public EntityUmvuthi(EntityType<? extends EntityUmvuthi> type, Level world) {
        super(type, world);
        if (getDirectionData() == 0) {
            this.setDirection(random.nextInt(4) + 1);
        }
        xpReward = 45;

        if (world.isClientSide) {
            headPos = new Vec3[]{new Vec3(0, 0, 0)};
            betweenHandPos = new Vec3[]{new Vec3(0, 0, 0)};
            blessingPlayerPos = new Vec3[]{new Vec3(0, 0, 0)};
        }

        active = true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        hurtByTargetAI = new UmvuthanaHurtByTargetAI(this);
        this.targetSelector.addGoal(3, hurtByTargetAI);
        this.targetSelector.addGoal(4, new NearestAttackableTargetPredicateGoal<Player>(this, Player.class, 0, false, true, (TargetingConditions.forCombat().range(getAttributeValue(Attributes.FOLLOW_RANGE)).selector(target -> {
            if (target instanceof Player) {
                if (this.level().getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((Player) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask) || target == getMisbehavedPlayer();
            }
            return true;
        }).ignoreLineOfSight())) {
            @Override
            public void stop() {
                super.stop();
                setMisbehavedPlayerId(null);
            }
        });
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, 0, false, false, null));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, 0, false, false, (e) -> !(e instanceof ZombifiedPiglin)));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 0, false, false, null));
        this.goalSelector.addGoal(1, new UseAbilityAI<>(this, DIE_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, HURT_ABILITY, false));
        this.goalSelector.addGoal(6, new UseAbilityAI<>(this, BELLY_ABILITY, false));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, SUNSTRIKE_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, SOLAR_FLARE_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, SOLAR_BEAM_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, SUPERNOVA_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, SPAWN_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, SPAWN_SUNBLOCKERS_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, BLESS_ABILITY));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, TALK_ABILITY, false));
        this.goalSelector.addGoal(2, new UseAbilityAI<>(this, ROAR_ABILITY, true));
        this.goalSelector.addGoal(5, new LookAtTargetGoal(this,24.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, EntityUmvuthana.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return super.getStandingEyeHeight(poseIn, sizeIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 40);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(maskController);
        controllers.add(blinkController);
    }

    private static RawAnimation MASK_TWITCH_ANIM = RawAnimation.begin().thenLoop("mask_twitch");

    protected <E extends GeoEntity> PlayState predicateMask(AnimationState<E> state)
    {
        if (isAlive() && getActiveAbilityType() != SOLAR_BEAM_ABILITY && getActiveAbilityType() != SUPERNOVA_ABILITY && getActiveAbilityType() != SPAWN_ABILITY && getActiveAbilityType() != SPAWN_SUNBLOCKERS_ABILITY) {
            state.getController().setAnimation(MASK_TWITCH_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private static RawAnimation BLINK_ANIM = RawAnimation.begin().thenLoop("blink");

    protected <E extends GeoEntity> PlayState predicateBlink(AnimationState<E> event)
    {
        if (isAlive() && getActiveAbilityType() != SOLAR_BEAM_ABILITY) {
            event.getController().setAnimation(BLINK_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected <E extends GeoEntity> void loopingAnimations(AnimationState<E> event) {
        event.getController().transitionLength(4);
        super.loopingAnimations(event);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getActiveAbility() == null) {
            sendAbilityMessage(TALK_ABILITY);
            return MMSounds.ENTITY_UMVUTHI_IDLE.get();
        }
        return null;
    }

    public void updateRattleSound(float maskRot) {
        if (!rattling) {
            if (Math.abs(maskRot - prevMaskRot) > 0.06) {
                level().playLocalSound(getX(), getY(), getZ(), MMSounds.ENTITY_UMVUTHANA_RATTLE.get(), SoundSource.HOSTILE, 0.04f, getVoicePitch() * 0.75f, false);
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
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_UMVUTHI_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    public boolean shouldRenderSun() {
        return deathTime < 85 && !(getActiveAbilityType() == EntityUmvuthi.SUPERNOVA_ABILITY && getActiveAbility().getTicksInUse() > 5 && getActiveAbility().getTicksInUse() <= 90);
    }

    @Override
    public void tick() {
        legsUp.updatePrevTimer();
        angryEyebrow.updatePrevTimer();
        setDeltaMovement(0, getDeltaMovement().y, 0);
        super.tick();
        if (tickCount == 1) {
            direction = getDirectionData();
        }
        if (!(getActiveAbilityType() == SOLAR_FLARE_ABILITY && getActiveAbility().getTicksInUse() >= 12 && getActiveAbility().getTicksInUse() <= 14)) this.repelEntities(1.2f, 1.2f, 1.2f, 1.2f);
        this.setYRot((direction - 1) * 90);
        this.yBodyRot = getYRot();
//        this.posX = prevPosX;
//        this.posZ = prevPosZ;

        if (level().isClientSide()) {
            if (shouldRenderSun()) {
                if (headPos != null && headPos.length > 0 && headPos[0] != null) {
                    if (this.tickCount % 10 == 1) {
                        AdvancedParticleBase.spawnParticle(level(), ParticleHandler.GLOW.get(), getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 0.3, 0.4, 1, 9, true, false, new ParticleComponent[]{
                                new ParticleComponent.PinLocation(headPos),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.oscillate(12.5f, 13.5f, 12), false)
                        });
                    }
                    if (random.nextFloat() < 0.3F) {
                        int amount = random.nextInt(2) + 1;
                        while (amount-- > 0) {
                            float theta = random.nextFloat() * MathUtils.TAU;
                            float r = random.nextFloat() * 0.4F;
                            float x = r * Mth.cos(theta);
                            float z = r * Mth.sin(theta);
                            level().addParticle(ParticleTypes.SMOKE, headPos[0].x() + x, headPos[0].y() + 0.1, headPos[0].z() + z, 0, 0, 0);
                        }
                    }
                }
            }
        }

        if (!level().isClientSide && getHealthLost() >= HEALTH_LOST_BETWEEN_SUNBLOCKERS && getActiveAbility() == null && !isNoAi() && getEntitiesNearby(EntityUmvuthanaCrane.class, 40).size() < 3) {
            sendAbilityMessage(SPAWN_SUNBLOCKERS_ABILITY);
            setHealthLost(0);
        }
        if (getTarget() != null) {
            LivingEntity target = getTarget();
            this.setAngry(true);
            float entityHitAngle = (float) ((Math.atan2(target.getZ() - getZ(), target.getX() - getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = getYRot() % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = Math.abs(entityHitAngle - entityAttackingAngle);
            Vec3 betweenEntitiesVec = position().subtract(target.position());
            boolean targetComingCloser = target.getDeltaMovement().dot(betweenEntitiesVec) > 0 && target.getDeltaMovement().lengthSqr() > 0.015;

            // Attacks
            if (getActiveAbility() == null && !isNoAi() && random.nextInt(80) == 0 && (targetDistance > 5.5 || hasEffect(EffectHandler.SUNBLOCK.get())) && timeUntilUmvuthana <= 0 && getEntitiesNearby(EntityUmvuthana.class, 50).size() < 4) {
                sendAbilityMessage(SPAWN_ABILITY);
                timeUntilUmvuthana = UMVUTHANA_PAUSE;
            } else if (getActiveAbility() == null && !isNoAi() && getHealthRatio() <= 0.6 && timeUntilLaser <= 0 && (entityRelativeAngle < 60 || entityRelativeAngle > 300) && getSensing().hasLineOfSight(target) && targetDistance < EntitySolarBeam.RADIUS_UMVUTHI) {
                sendAbilityMessage(SOLAR_BEAM_ABILITY);
                timeUntilLaser = LASER_PAUSE;
            } else if (getActiveAbility() == null && !isNoAi() && getHealthRatio() <= 0.6 && !hasEffect(EffectHandler.SUNBLOCK.get()) && timeUntilSupernova <= 0 && targetDistance <= 10.5) {
                sendAbilityMessage(SUPERNOVA_ABILITY);
                timeUntilSupernova = SUPERNOVA_PAUSE;
            } else if (getActiveAbility() == null && !isNoAi() && ((targetDistance <= 6f && targetComingCloser) || targetDistance < 4.f)) {
                sendAbilityMessage(SOLAR_FLARE_ABILITY);
            } else if (getActiveAbility() == null && !isNoAi() && timeUntilSunstrike <= 0) {
                sendAbilityMessage(SUNSTRIKE_ABILITY);
                timeUntilSunstrike = getTimeUntilSunstrike();
            }

            if (hurtByTargetAI != null && !hurtByTargetAI.canContinueToUse()) {
                hurtByTargetAI.stop();
            }
        } else {
            if (!level().isClientSide) {
                this.setAngry(false);
            }
        }

        if (tickCount % 20 == 0) {
            blocksByFeet = checkBlocksByFeet();
        }

        if (blocksByFeet) {
            legsUp.increaseTimer();
        } else {
            legsUp.decreaseTimer();
        }

        if (getAngry()) {
            angryEyebrow.increaseTimer();
        } else {
            angryEyebrow.decreaseTimer();
        }

        if (getActiveAbility() == null && !isNoAi() && getTarget() == null && random.nextInt(200) == 0) {
            sendAbilityMessage(BELLY_ABILITY);
        }

        if (getActiveAbility() == null && !isNoAi() && getTarget() == null && timeUntilRoar <= 0 && random.nextInt(300) == 0) {
            sendAbilityMessage(ROAR_ABILITY);
            timeUntilRoar = ROAR_PAUSE;
        }

//        if (getActiveAbilityType() == TALK_ABILITY && getActiveAbility().getTicksInUse() == 1) {
//            whichDialogue = getWhichDialogue();
//        }

        if (getActiveAbilityType() == SOLAR_FLARE_ABILITY) {
            yHeadRot = getYRot();
//            if (getActiveAbility().getTicksInUse() == 1) {
//                this.playSound(MMSounds.ENTITY_UMVUTHI_BURST, 1.7f, 1.5f);
//            }
            if (getActiveAbility().getTicksInUse() == 10) {
                if (level().isClientSide) {
                    spawnExplosionParticles(30);
                }
                this.playSound(MMSounds.ENTITY_UMVUTHI_ATTACK.get(), 1.7f, 0.9f);
            }
            if (getActiveAbility().getTicksInUse() <= 6 && level().isClientSide) {
                int particleCount = 8;
                while (--particleCount != 0) {
                    double radius = 2f;
                    double yaw = random.nextFloat() * 2 * Math.PI;
                    double pitch = random.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    float offsetX = (float) (-0.3 * Math.sin(getYRot() * Math.PI / 180));
                    float offsetZ = (float) (-0.3 * Math.cos(getYRot() * Math.PI / 180));
                    float offsetY = 1;
                    level().addParticle(new ParticleOrb.OrbData((float) getX() + offsetX, (float) getY() + offsetY, (float) getZ() + offsetZ, 6), getX() + ox + offsetX, getY() + offsetY + oy, getZ() + oz + offsetZ, 0, 0, 0);
                }
            }
        }

        if (getActiveAbilityType() == BLESS_ABILITY) {
            yHeadRot = getYRot();

            if (getActiveAbility().getTicksInUse() == 1) {
                blessingPlayer = getCustomer();
            }
            if (level().isClientSide && blessingPlayer != null) {
                blessingPlayerPos[0] = blessingPlayer.position().add(new Vec3(0, blessingPlayer.getBbHeight() / 2f, 0));
                if (getActiveAbility().getTicksInUse() > 5 && getActiveAbility().getTicksInUse() < 40) {
                    int particleCount = 2;
                    while (--particleCount != 0) {
                        double radius = 0.7f;
                        double yaw = random.nextFloat() * 2 * Math.PI;
                        double pitch = random.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        AdvancedParticleBase.spawnParticle(level(), ParticleHandler.ORB2.get(), getX() + ox, getY() + 0.8f + oy, getZ() + oz, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 20, true, true, new ParticleComponent[]{
                                new ParticleComponent.Attractor(blessingPlayerPos, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.POS_X, new ParticleComponent.Oscillator(0, (float) ox, 6f, 2.5f), true),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, new ParticleComponent.Oscillator(0, (float) oy, 6f, 2.5f), true),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Z, new ParticleComponent.Oscillator(0, (float) oz, 6f, 2.5f), true),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                        new float[]{0f, 1f},
                                        new float[]{0, 0.8f}
                                ), false)
                        });
                    }
                }
                if (getActiveAbility().getTicksInUse() % 15 == 0) {
                    AdvancedParticleBase.spawnParticle(level(), ParticleHandler.RING2.get(), getX(), getY() + 0.8f, getZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 1, 223/255f, 66/255f, 1, 1, 15, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(5f, 35f), false)
                    });
                }
            }
        }

        if (tickCount % 40 == 0) {
            for (Player player : getPlayersNearby(15, 15, 15, 15)) {
                ItemStack headArmorStack = player.getInventory().armor.get(3);
                if (getTarget() != player && canAttack(player, GIVE_ACHIEVEMENT_PRED) && headArmorStack.getItem() instanceof UmvuthanaMask) {
                    if (player instanceof ServerPlayer) AdvancementHandler.SNEAK_VILLAGE_TRIGGER.trigger((ServerPlayer) player);
                }
            }
        }

        if (!level().isClientSide && getTarget() == null && getActiveAbilityType() != SOLAR_BEAM_ABILITY && getActiveAbilityType() != SUPERNOVA_ABILITY) {
            timeUntilHeal--;
            if (ConfigHandler.COMMON.MOBS.UMVUTHI.healsOutOfBattle.get() && timeUntilHeal <= 0) heal(0.3f);
            if (getHealth() == getMaxHealth()) setHealthLost(0);
        }
        else {
            timeUntilHeal = HEAL_PAUSE;
        }

        if (timeUntilSunstrike > 0) {
            timeUntilSunstrike--;
        }
        if (timeUntilLaser > 0 && getActiveAbilityType() != SUPERNOVA_ABILITY) {
            timeUntilLaser--;
        }
        if (timeUntilUmvuthana > 0) {
            timeUntilUmvuthana--;
        }
        if (timeUntilSupernova > 0 && getActiveAbilityType() != SOLAR_BEAM_ABILITY) {
            timeUntilSupernova--;
        }
        if (timeUntilRoar > 0) {
            timeUntilRoar--;
        }

//        if (getActiveAbility() == null && tickCount % 60 == 0) {
//            sendAbilityMessage(SOLAR_BEAM_ABILITY);
//        }
    }

    @Override
    public AbilityType getHurtAbility() {
        return HURT_ABILITY;
    }

    @Override
    public AbilityType getDeathAbility() {
        return DIE_ABILITY;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
    	if (source == level().damageSources().hotFloor()) return false;
        if (hasEffect(EffectHandler.SUNBLOCK.get()) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (source.getDirectEntity() != null) playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
            return false;
        }
        timeUntilHeal = HEAL_PAUSE;
        float prevHealth = getHealth();
        boolean superResult = super.hurt(source, damage);
        if (superResult) {
            float diffHealth = prevHealth - getHealth();
            setHealthLost(getHealthLost() + diffHealth);
        }
        return superResult;
    }

    private boolean checkBlocksByFeet() {
        BlockState blockLeft;
        BlockState blockRight;
        BlockPos posLeft;
        BlockPos posRight;
        if (direction == 1) {
            posLeft = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            posRight = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            blockLeft = level().getBlockState(posLeft);
            blockRight = level().getBlockState(posRight);
        } else if (direction == 2) {
            posLeft = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            posRight = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            blockLeft = level().getBlockState(posLeft);
            blockRight = level().getBlockState(posRight);
        } else if (direction == 3) {
            posLeft = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            posRight = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            blockLeft = level().getBlockState(posLeft);
            blockRight = level().getBlockState(posRight);
        } else if (direction == 4) {
            posLeft = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            posRight = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            blockLeft = level().getBlockState(posLeft);
            blockRight = level().getBlockState(posRight);
        } else {
            return false;
        }
//        System.out.println(direction + ", " + (MathHelper.floor(posX) - 1) + ", " + Math.round((float) (posY - 1)) + ", " + MathHelper.floor(posZ) + 1);
        return blockLeft.blocksMotion() || blockRight.blocksMotion();
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.25F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = random.nextFloat() * 0.1F - 0.05f;
            float vx = velocity * Mth.cos(yaw);
            float vz = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.FLAME, getX(), getY() + 1, getZ(), vx, vy, vz);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(DIRECTION, 0);
        getEntityData().define(DIALOGUE, 0);
        getEntityData().define(ANGRY, false);
        Item tradeItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ConfigHandler.COMMON.MOBS.UMVUTHI.whichItem.get()));
        getEntityData().define(DESIRES, new ItemStack(tradeItem, ConfigHandler.COMMON.MOBS.UMVUTHI.howMany.get()));
        getEntityData().define(TRADED_PLAYERS, new CompoundTag());
        getEntityData().define(HEALTH_LOST, 0.f);
        getEntityData().define(MISBEHAVED_PLAYER, Optional.empty());
        getEntityData().define(IS_TRADING, false);
    }

    public int getDirectionData() {
        return getEntityData().get(DIRECTION);
    }

    public void setDirection(int direction) {
        getEntityData().set(DIRECTION, direction);
    }

    public int getWhichDialogue() {
        return getEntityData().get(DIALOGUE);
    }

    public void setWhichDialogue(int dialogue) {
        getEntityData().set(DIALOGUE, dialogue);
    }

    public boolean getAngry() {
        return getEntityData().get(ANGRY);
    }

    public void setAngry(boolean angry) {
        getEntityData().set(ANGRY, angry);
    }

    public void setDesires(ItemStack stack) {
    	getEntityData().set(DESIRES, stack);
    }

    public ItemStack getDesires() {
    	return getEntityData().get(DESIRES);
    }

    public void setTradedPlayersCompound(ListTag players) {
        CompoundTag compound = new CompoundTag();
        compound.put("players", players);
        getEntityData().set(TRADED_PLAYERS, compound);
    }

    public Set<UUID> getTradedPlayers() {
        Set<UUID> tradedPlayers = new HashSet<>();
        CompoundTag compound = getEntityData().get(TRADED_PLAYERS);
        ListTag players = compound.getList("players", Tag.TAG_INT_ARRAY);
        for (net.minecraft.nbt.Tag player : players) {
            tradedPlayers.add(NbtUtils.loadUUID(player));
        }
        return tradedPlayers;
    }

    public float getHealthLost() {
        return getEntityData().get(HEALTH_LOST);
    }

    public void setHealthLost(float amount) {
        getEntityData().set(HEALTH_LOST, amount);
    }

    public boolean doesItemSatisfyDesire(ItemStack stack) {
        return canPayFor(stack, getDesires());
    }

    public boolean fulfillDesire(Slot input) {
        ItemStack desires = getDesires();
        if (canPayFor(input.getItem(), desires)) {
            input.remove(desires.getCount());
            return true;
        }
        return false;
    }

    public boolean hasTradedWith(Player player) {
        return getTradedPlayers().contains(UUIDUtil.getOrCreatePlayerUUID(player.getGameProfile()));
    }

    public void rememberTrade(Player player) {
        UUID uuid = UUIDUtil.getOrCreatePlayerUUID(player.getGameProfile());
        CompoundTag compound = getEntityData().get(TRADED_PLAYERS);
        ListTag players = compound.getList("players", Tag.TAG_INT_ARRAY);
        players.add(NbtUtils.createUUID(uuid));
        compound.put("players", players);
        getEntityData().set(TRADED_PLAYERS, compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("direction", getDirectionData());
        CompoundTag compoundTradedPlayers = getEntityData().get(TRADED_PLAYERS);
        ListTag players = compoundTradedPlayers.getList("players", Tag.TAG_INT_ARRAY);
        compound.put("players", players);
        compound.putInt("HomePosX", this.getRestrictCenter().getX());
        compound.putInt("HomePosY", this.getRestrictCenter().getY());
        compound.putInt("HomePosZ", this.getRestrictCenter().getZ());
        compound.putFloat("healthLost", this.getHealthLost());
        if (this.getMisbehavedPlayerId() != null) {
            compound.putUUID("MisbehavedPlayer", this.getMisbehavedPlayerId());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setDirection(compound.getInt("direction"));
        ListTag players = compound.getList("players", Tag.TAG_INT_ARRAY);
        setTradedPlayersCompound(players);
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        this.restrictTo(new BlockPos(i, j, k), -1);
        setHealthLost(compound.getInt("healthLost"));
        UUID uuid;
        if (compound.hasUUID("MisbehavedPlayer")) {
            uuid = compound.getUUID("MisbehavedPlayer");
        } else {
            String s = compound.getString("MisbehavedPlayer");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setMisbehavedPlayerId(uuid);
            } catch (Throwable ignored) {

            }
        }
    }

    @Nullable
    public UUID getMisbehavedPlayerId() {
        return this.entityData.get(MISBEHAVED_PLAYER).orElse((UUID)null);
    }

    public void setMisbehavedPlayerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(MISBEHAVED_PLAYER, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public LivingEntity getMisbehavedPlayer() {
        try {
            UUID uuid = this.getMisbehavedPlayerId();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {}

    private int getTimeUntilSunstrike() {
        float damageRatio = 1 - getHealthRatio();
        if (damageRatio > 0.6) {
            damageRatio = 0.6f;
        }
        return (int) (SUNSTRIKE_PAUSE_MAX - (damageRatio / 0.6f) * (SUNSTRIKE_PAUSE_MAX - SUNSTRIKE_PAUSE_MIN));
    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[]{DIE_ABILITY, HURT_ABILITY, BELLY_ABILITY, TALK_ABILITY, SUNSTRIKE_ABILITY, SOLAR_FLARE_ABILITY, SPAWN_ABILITY, SPAWN_SUNBLOCKERS_ABILITY, SOLAR_BEAM_ABILITY, BLESS_ABILITY, SUPERNOVA_ABILITY, ROAR_ABILITY};
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        List<EntityUmvuthana> umvuthana = getEntitiesNearby(EntityUmvuthana.class, 30, 20, 30, 30);
        for (EntityUmvuthana entityUmvuthana : umvuthana) {
            if (entityUmvuthana.isUmvuthiDevoted()) {
                if (entityUmvuthana instanceof EntityUmvuthanaCrane) ((EntityUmvuthanaCrane)entityUmvuthana).hasTriedOrSucceededTeleport = true;
                entityUmvuthana.timeUntilDeath = random.nextInt(20);
            }
        }

        super.die(cause);
    }

    public void setTrading(boolean trading) {
        entityData.set(IS_TRADING, trading);
    }

    public boolean isTrading() {
        return entityData.get(IS_TRADING);
    }

    public Player getCustomer() {
        return customer;
    }

    public void setCustomer(Player customer) {
        setTrading(customer != null);
        this.customer = customer;
    }

    public void openGUI(Player playerEntity) {
        setCustomer(playerEntity);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.level().isClientSide && getTarget() == null && isAlive()) {
            playerEntity.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                    return new ContainerUmvuthiTrade(id, EntityUmvuthi.this, playerInventory);
                }

                @Override
                public Component getDisplayName() {
                    return EntityUmvuthi.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (canTradeWith(player) && getTarget() == null && isAlive()) {
            openGUI(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public boolean canTradeWith(Player player) {
        if (isTrading() || getHealth() <= 0) {
            return false;
        }
        ItemStack headStack = player.getInventory().armor.get(3);
        return headStack.getItem() instanceof UmvuthanaMask;
    }

    private static boolean canPayFor(ItemStack stack, ItemStack worth) {
        return stack.getItem() == worth.getItem() && stack.getCount() >= worth.getCount();
    }

    @Override
    public boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.UMVUTHI.hasBossBar.get();
    }

    @Override
    protected BossEvent.BossBarColor bossBarColor() {
        return BossEvent.BossBarColor.YELLOW;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LootTableHandler.UMVUTHI;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        if (reason == MobSpawnType.SPAWN_EGG) {
            // Try to guess which player spawned Umvuthi, rotate towards them
            List<Player> players = getPlayersNearby(5, 5, 5, 5);
            if (!players.isEmpty()) {
                Player closestPlayer = players.get(0);
                float closestPlayerDist = 6;
                for (Player player : players) {
                    if (player.getMainHandItem().getItem() == ItemHandler.UMVUTHI_SPAWN_EGG.get() || player.getMainHandItem().getItem() == ItemHandler.UMVUTHI_SPAWN_EGG.get()) {
                        float thisDist = this.distanceTo(player);
                        if (thisDist < closestPlayerDist) {
                            closestPlayer = player;
                            closestPlayerDist = thisDist;
                        }
                    }
                }
                float angle = (float) getAngleBetweenEntities(this, closestPlayer) + 225;
                int direction = (int) (angle / 90) % 4 + 1;
                setDirection(direction);
            }
        }
        if (reason != MobSpawnType.STRUCTURE) restrictTo(blockPosition(), -1);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
        super.push(0, y, 0);
    }

    @Override
    public SoundEvent getBossMusic() {
        return MMSounds.MUSIC_UMVUTHI_THEME.get();
    }

    @Override
    public boolean resetHealthOnPlayerRespawn() {
        return ConfigHandler.COMMON.MOBS.UMVUTHI.resetHealthWhenRespawn.get();
    }

    public static class SunstrikeAbility extends Ability<EntityUmvuthi> {
        private static int STARTUP_DURATION = 9;

        protected LivingEntity entityTarget;
        public double prevX;
        public double prevZ;
        private int newX;
        private int newZ;
        private int y;

        public SunstrikeAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, STARTUP_DURATION),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 12)
            });
        }

        private static final RawAnimation SUN_STRIKE_ANIM = RawAnimation.begin().then("sun_strike", Animation.LoopType.PLAY_ONCE);

        @Override
        public void start() {
            super.start();
            entityTarget = getUser().getTarget();
            if (entityTarget != null) {
                prevX = entityTarget.getX();
                prevZ = entityTarget.getZ();
            }
            playAnimation(SUN_STRIKE_ANIM);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (!getUser().level().isClientSide()) {
                if (entityTarget == null) {
                    return;
                }

                if (getTicksInUse() == STARTUP_DURATION - 2) {
                    double x = entityTarget.getX();
                    y = Mth.floor(entityTarget.getY() - 1);
                    double z = entityTarget.getZ();
                    double vx = (x - prevX) / STARTUP_DURATION;
                    double vz = (z - prevZ) / STARTUP_DURATION;
                    int t = EntitySunstrike.STRIKE_EXPLOSION + 3;
                    newX = Mth.floor(x + vx * t);
                    newZ = Mth.floor(z + vz * t);
                    double dx = newX - getUser().getX();
                    double dz = newZ - getUser().getZ();
                    double dist2ToUmvuthi = dx * dx + dz * dz;
                    if (dist2ToUmvuthi < 3) {
                        newX = Mth.floor(entityTarget.getX());
                        newZ = Mth.floor(entityTarget.getZ());
                    }
                    for (int i = 0; i < 5; i++) {
                        if (!getUser().level().canSeeSkyFromBelowWater(new BlockPos(newX, y, newZ))) {
                            y++;
                        } else {
                            break;
                        }
                    }
                }

                if (getTicksInUse() < STARTUP_DURATION - 2) {
                    getUser().getLookControl().setLookAt(entityTarget, 30, 30);
                }
                if (getTicksInUse() >= STARTUP_DURATION - 2) {
                    getUser().getLookControl().setLookAt(newX, y + entityTarget.getEyeHeight(), newZ, 50, 50);
                }
            }
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (!getUser().level().isClientSide()) {
                if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                    getUser().playSound(MMSounds.ENTITY_UMVUTHI_ATTACK.get(), 1.4f, 1);
                    EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE.get(), getUser().level(), getUser(), newX, y, newZ);
                    sunstrike.onSummon();
                    getUser().level().addFreshEntity(sunstrike);
                }
            }
        }
    }

    public static class SolarBeamAbility extends Ability<EntityUmvuthi> {
        protected LivingEntity entityTarget;
        private EntitySolarBeam solarBeam;

        public SolarBeamAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 22),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 68),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 10)
            });
        }

        private static final RawAnimation SOLAR_BEAM_ANIM = RawAnimation.begin().then("solar_beam", Animation.LoopType.PLAY_ONCE);

        @Override
        public void start() {
            super.start();
            entityTarget = getUser().getTarget();
            playAnimation(SOLAR_BEAM_ANIM);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            float radius1 = 0.8f;
            EntityUmvuthi entity = getUser();
            if (getTicksInUse() == 4 && !entity.level().isClientSide) {
                solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM.get(), getUser().level(), entity, entity.getX() + radius1 * Math.sin(-entity.getYRot() * Math.PI / 180), entity.getY() + 1.4, entity.getZ() + radius1 * Math.cos(-entity.getYRot() * Math.PI / 180), (float) ((entity.yHeadRot + 90) * Math.PI / 180), (float) (-entity.getXRot() * Math.PI / 180), 55);
                entity.level().addFreshEntity(solarBeam);
            }
            if (getTicksInUse() >= 22) {
                if (entityTarget != null) {
                    entity.getLookControl().setLookAt(entityTarget.getX(), entityTarget.getY() + entityTarget.getBbHeight() / 2, entityTarget.getZ(), 2, 90);
                }
            }
        }
    }

    public static class SolarFlareAbility extends Ability<EntityUmvuthi> {
        public static AbilitySection[] SECTION_TRACK = new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 12),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 18)
        };

        public SolarFlareAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, SECTION_TRACK);
        }

        private static final RawAnimation FLARE_ANIM = RawAnimation.begin().then("flare", Animation.LoopType.PLAY_ONCE);

        @Override
        public void start() {
            super.start();
            getUser().playSound(MMSounds.ENTITY_UMVUTHI_BURST.get(), 1.7f, 1.5f);
            playAnimation(FLARE_ANIM);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                EntityUmvuthi entity = getUser();
                float radius = 4f;
                List<LivingEntity> hit = entity.getEntityLivingBaseNearby(radius, 2 * radius, radius, radius);
                for (LivingEntity aHit : hit) {
                    if (aHit instanceof LeaderSunstrikeImmune) {
                        continue;
                    }
                    entity.doHurtTarget(aHit, 1f, 3f);
                    if (!aHit.isInvulnerable()) {
                        if (aHit instanceof Player && ((Player) aHit).getAbilities().invulnerable) continue;
                        double knockback = 3;
                        double angle = entity.getAngleBetweenEntities(entity, aHit);
                        double x = knockback * Math.cos(Math.toRadians(angle - 90));
                        double z = knockback * Math.sin(Math.toRadians(angle - 90));
                        aHit.setDeltaMovement(x, 0.3, z);
                        if (aHit instanceof ServerPlayer) {
                            ((ServerPlayer) aHit).connection.send(new ClientboundSetEntityMotionPacket(aHit));
                        }
                    }
                }
            }
        }
    }

    public static class SpawnFollowersAbility extends Ability<EntityUmvuthi> {
        private boolean spawnSunblockers;

        public SpawnFollowersAbility(AbilityType abilityType, EntityUmvuthi user, boolean spawnSunblockers) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 11),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 11),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 11)
            });
            this.spawnSunblockers = spawnSunblockers;
        }

        private static final RawAnimation SPAWN_STRIX_ANIM = RawAnimation.begin().then("spawn_strix", Animation.LoopType.PLAY_ONCE);

        @Override
        public void start() {
            super.start();
            getUser().umvuthanaSpawnCount++;
            getUser().playSound(MMSounds.ENTITY_UMVUTHANA_INHALE.get(), 1.2f, 0.5f);
            playAnimation(SPAWN_STRIX_ANIM);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            EntityUmvuthi entity = getUser();
            if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                getUser().playSound(MMSounds.ENTITY_UMVUTHANA_INHALE.get(), 1.2f, 0.5f);
                playAnimation(SPAWN_STRIX_ANIM);
            }
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (!getUser().level().isClientSide()) {
                    entity.playSound(MMSounds.ENTITY_UMVUTHI_BELLY.get(), 1.5f, 1);
                    entity.playSound(MMSounds.ENTITY_UMVUTHANA_BLOWDART.get(), 1.5f, 0.5f);
                    double angle = entity.yHeadRot;
                    if (angle < 0) {
                        angle = angle + 360;
                    }
                    if (angle - entity.getYRot() > 70) {
                        angle = 70 + entity.getYRot();
                    } else if (angle - entity.getYRot() < -70) {
                        angle = -70 + entity.getYRot();
                    }
                    EntityUmvuthanaMinion umvuthana;
                    if (spawnSunblockers) {
                        umvuthana = new EntityUmvuthanaCrane(EntityHandler.UMVUTHANA_CRANE.get(), entity.level());
                        ((EntityUmvuthanaCrane) umvuthana).hasTriedOrSucceededTeleport = false;
                    } else umvuthana = new EntityUmvuthanaMinion(EntityHandler.UMVUTHANA_MINION.get(), entity.level());
                    umvuthana.absMoveTo(entity.getX() + 2 * Math.sin(-angle * (Math.PI / 180)), entity.getY() + 2.5, entity.getZ() + 2 * Math.cos(-angle * (Math.PI / 180)), entity.yHeadRot, 0);
                    umvuthana.setActive(false);
                    umvuthana.active = false;
                    umvuthana.finalizeSpawn((ServerLevelAccessor) entity.getCommandSenderWorld(), entity.level().getCurrentDifficultyAt(umvuthana.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    umvuthana.restrictTo(entity.getRestrictCenter(), 25);
                    if (entity.getTeam() instanceof PlayerTeam) {
                        umvuthana.level().getScoreboard().addPlayerToTeam(umvuthana.getScoreboardName(), (PlayerTeam) entity.getTeam());
                    }
                    entity.level().addFreshEntity(umvuthana);
                    umvuthana.setDeltaMovement(0.7 * Math.sin(-angle * (Math.PI / 180)), 0.5, 0.7 * Math.cos(-angle * (Math.PI / 180)));
                    if (!spawnSunblockers) {
                        umvuthana.setTarget(entity.getTarget());
                        if (entity.getTarget() instanceof Player) {
                            umvuthana.setMisbehavedPlayerId(entity.getTarget().getUUID());
                        }
                    }
                }
            }
        }

        @Override
        protected void endSection(AbilitySection section) {
            super.endSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
                if (getUser().targetDistance <= 6 && getUser().getTarget() != null && !spawnSunblockers) {
                    interrupt();
                }
            }
        }
    }

    public static class SupernovaAbility extends Ability<EntityUmvuthi> {
        public static AbilitySection[] SECTION_TRACK = new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 44),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 40),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 16)
        };

        public SupernovaAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, SECTION_TRACK);
        }

        private static final RawAnimation SUPERNOVA_ANIM = RawAnimation.begin().then("supernova", Animation.LoopType.PLAY_ONCE);

        @Override
        public void start() {
            super.start();
            getUser().playSound(MMSounds.ENTITY_SUPERNOVA_START.get(), 3f, 1f);
            playAnimation(SUPERNOVA_ANIM);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (getTicksInUse() == 30) {
                getUser().playSound(MMSounds.ENTITY_SUPERNOVA_BLACKHOLE.get(), 2f, 1.2f);
            }

            if (getTicksInUse() < 30) {
                List<LivingEntity> entities = getUser().getEntityLivingBaseNearby(16, 16, 16, 16);
                for (LivingEntity inRange : entities) {
                    if (inRange instanceof LeaderSunstrikeImmune) continue;
                    if (inRange instanceof Player && ((Player)inRange).getAbilities().invulnerable) continue;
                    Vec3 diff = inRange.position().subtract(getUser().position().add(0, 3, 0));
                    diff = diff.normalize().scale(0.03);
                    inRange.setDeltaMovement(inRange.getDeltaMovement().subtract(diff));

                    if (inRange.getY() < getUser().getY() + 3) inRange.setDeltaMovement(inRange.getDeltaMovement().add(0, 0.075, 0));
                }
            }

            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                getUser().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 1, false, false));
            }

            if (getTicksInUse() == 40) {
                getUser().playSound(MMSounds.ENTITY_UMVUTHI_ROAR.get(), 3f, 1f);
            }

            if (getLevel().isClientSide) {
                superNovaEffects(this, getUser().betweenHandPos, getLevel());
            }
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (!getUser().level().isClientSide) {
                    Vec3 offset = new Vec3(1.1f, 0, 0);
                    offset = offset.yRot((float) Math.toRadians(-getUser().getYRot() - 90));
                    EntitySuperNova superNova = new EntitySuperNova(EntityHandler.SUPER_NOVA.get(), getUser().level(), getUser(), getUser().getX() + offset.x, getUser().getY() + 0.05, getUser().getZ() + offset.z);
                    getUser().level().addFreshEntity(superNova);
                }
            }
        }

        private static final ParticleComponent.KeyTrack superNovaKeyTrack1 = new ParticleComponent.KeyTrack(
                new float[]{0, 25f, 32f, 0},
                new float[]{0, 0.6f, 0.85f, 1}
        );
        private static final ParticleComponent.KeyTrack superNovaKeyTrack2 = ParticleComponent.KeyTrack.oscillate(0, 7, 24);

        @OnlyIn(Dist.CLIENT)
        public static void superNovaEffects(Ability activeAbility, Vec3[] pinLocation, Level level) {
            // Darken sky
            Player clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer == null) return;
            double distToCaster = activeAbility.getUser().position().distanceToSqr(clientPlayer.position());
            if (distToCaster < 1000) {
                Minecraft.getInstance().gameRenderer.darkenWorldAmount += 0.06f;
                if (Minecraft.getInstance().gameRenderer.darkenWorldAmount > 1.0f)
                    Minecraft.getInstance().gameRenderer.darkenWorldAmount = 1.0f;
            }

            // Particle effects
            if (pinLocation == null || pinLocation.length == 0 || pinLocation[0] == null) return;
            int ticksInUse = activeAbility.getTicksInUse();
            LivingEntity user = activeAbility.getUser();
            RandomSource random = user.getRandom();

            if (ticksInUse == 1) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.SUN.get(), user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 33, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack1, false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack2, true)
                });
            }
            if (ticksInUse == 33) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.SUN_NOVA.get(), user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 20F, 1, 1, 1, 0, 1, 13, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{11f, 7f, 5.5f, 1f, 30},
                                new float[]{0, 0.15f, 0.8f, 0.89f, 1}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                new float[]{0f, 1f, 1f, 0f},
                                new float[]{0, 0.15f, 0.89f, 1}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.PARTICLE_ANGLE, ParticleComponent.KeyTrack.startAndEnd(0f, -6f), false)
                });
            }
            if (ticksInUse == 32) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.FLARE.get(), user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 1,1,1, 0.7, 1, 3, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, ParticleComponent.constant(-0.15f), true),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 22f, 0f},
                                new float[]{0, 0.2f, 1}
                        ), false)
                });
            }
            if (ticksInUse > 30 && ticksInUse < 41) {
                for (int i = 0; i < 6; i++) {
                    float phaseOffset = random.nextFloat();
                    double value = random.nextDouble() * 0.3 + 0.05;
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL.get(), pinLocation[0].x, pinLocation[0].y, pinLocation[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 6, false, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[]{0f, 3f},
                                    new float[]{0, 0.2f}
                            ), false),
                            new ParticleComponent.Orbit(pinLocation, ParticleComponent.KeyTrack.startAndEnd(0 + phaseOffset, -0.4f + phaseOffset), ParticleComponent.KeyTrack.startAndEnd(0.5f + random.nextFloat(), 0), ParticleComponent.constant(0), ParticleComponent.constant(0), ParticleComponent.constant(0), true),
                    });
                }
            }
            if (ticksInUse > 1 && ticksInUse < 27) {
                for (int i = 0; i < 6; i++) {
                    Vec3 particlePos = new Vec3(random.nextFloat() * 5, 0, 0);
                    particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.add(pinLocation[0]);
                    double value = random.nextDouble() * 0.5 + 0.1;
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 7, false, true, new ParticleComponent[]{
                            new ParticleComponent.Attractor(pinLocation, 1.1f, 1f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[]{0f, 3.5f},
                                    new float[]{0, 0.2f}
                            ), false)
                    });
                }
            }
            float timeFrac = Math.min((float)ticksInUse / 20f, 1f);
            if (ticksInUse > 1 && ticksInUse < 25 && ticksInUse % (int)(4 * (1 - timeFrac) + 1) == 0) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING_SPARKS.get(),  user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, random.nextFloat() * (float)Math.PI * 2, 5F, 1, 1, 1, 1, 1, 6 + random.nextFloat() * 3, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f + 20f * timeFrac * timeFrac + 10f * random.nextFloat() * timeFrac, 0f), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false)
                });
            }
            if (ticksInUse == 14) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.FLARE.get(), user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 18, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, ParticleComponent.constant(-0.1f), true),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 35f, 0f},
                                new float[]{0, 0.8f, 1}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-5, 5, 42, 0), true)
                });
            }

            if (ticksInUse == 32) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.BURST_IN.get(), user.getX(), user.getY(), user.getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 0, 0, 0, 1, 1, 10, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(25f, 0f), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 1f), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-2, 2, 42, 0), true),
                });
            }

            if (ticksInUse == 44) {
                float scale = 85f;
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING_BIG.get(), pinLocation[0].x, pinLocation[0].y, pinLocation[0].z, 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 1,1,1, 1, 1, 40, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, scale},
                                new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                        ), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                });
                scale = 120f;
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW.get(), pinLocation[0].x, pinLocation[0].y, pinLocation[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.95, 0.9,0.35, 1, 1, 40, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, scale},
                                new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                        ), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                });
            }
        }
    }
}
