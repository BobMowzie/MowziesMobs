package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicSound;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtGoal;
import net.minecraft.world.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.IronGolemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.sounds.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class EntityBarako extends MowzieEntity implements LeaderSunstrikeImmune, Enemy {
    public static final Animation DIE_ANIMATION = Animation.create(130);
    public static final Animation HURT_ANIMATION = Animation.create(13);
    public static final Animation BELLY_ANIMATION = Animation.create(40);
    public static final Animation TALK_ANIMATION = Animation.create(80);
    public static final Animation SUNSTRIKE_ANIMATION = Animation.create(15);
    public static final Animation ATTACK_ANIMATION = Animation.create(30);
    public static final Animation SPAWN_ANIMATION = Animation.create(17);
    public static final Animation SPAWN_SUNBLOCKERS_ANIMATION = Animation.create(17);
    public static final Animation SOLAR_BEAM_ANIMATION = Animation.create(100);
    public static final Animation BLESS_ANIMATION = Animation.create(60);
    public static final Animation SUPERNOVA_ANIMATION = Animation.create(100);
    private static final int MAX_HEALTH = 150;
    private static final int SUNSTRIKE_PAUSE_MAX = 50;
    private static final int SUNSTRIKE_PAUSE_MIN = 30;
    private static final int LASER_PAUSE = 230;
    private static final int SUPERNOVA_PAUSE = 230;
    private static final int BARAKOA_PAUSE = 200;
    private static final int HEAL_PAUSE = 75;
    private static final int HEALTH_LOST_BETWEEN_SUNBLOCKERS = 45;
    private static final EntityDataAccessor<Integer> DIRECTION = SynchedEntityData.defineId(EntityBarako.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DIALOGUE = SynchedEntityData.defineId(EntityBarako.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityBarako.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DESIRES = SynchedEntityData.defineId(EntityBarako.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<CompoundTag> TRADED_PLAYERS = SynchedEntityData.defineId(EntityBarako.class, EntityDataSerializers.COMPOUND_NBT);
    private static final EntityDataAccessor<Float> HEALTH_LOST = SynchedEntityData.defineId(EntityBarako.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Optional<UUID>> MISBEHAVED_PLAYER = SynchedEntityData.defineId(EntityBarakoaVillager.class, EntityDataSerializers.OPTIONAL_UNIQUE_ID);
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    public ControlledAnimation angryEyebrow = new ControlledAnimation(5);
    private Player customer;
    public int barakoaSpawnCount = 0;
    // TODO: use Direction!
    private int direction = 0;
    private boolean blocksByFeet = true;
    private int timeUntilSunstrike = 0;
    private int timeUntilLaser = 0;
    private int timeUntilBarakoa = 0;
    private int timeUntilSupernova = 0;
    private int timeUntilHeal = 0;
    public Player blessingPlayer;
    private BarakoaHurtByTargetAI hurtByTargetAI;

    @OnlyIn(Dist.CLIENT)
    public Vec3[] betweenHandPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] blessingPlayerPos;

    private static final EntityPredicate GIVE_ACHIEVEMENT_PRED = new EntityPredicate().setUseInvisibilityCheck();

    private static ParticleComponent.KeyTrack superNovaKeyTrack1 = new ParticleComponent.KeyTrack(
            new float[]{0, 20f, 20f, 0},
            new float[]{0, 0.5f, 0.9f, 1}
    );
    private static ParticleComponent.KeyTrack superNovaKeyTrack2 = ParticleComponent.KeyTrack.oscillate(0, 1, 30);

    public EntityBarako(EntityType<? extends EntityBarako> type, Level world) {
        super(type, world);
        if (getDirection() == 0) {
            this.setDirection(random.nextInt(4) + 1);
        }
        xpReward = 45;

        if (level.isClientSide) {
            betweenHandPos = new Vec3[]{new Vec3(0, 0, 0)};
            blessingPlayerPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        hurtByTargetAI = new BarakoaHurtByTargetAI(this, true);
        this.targetSelector.addGoal(3, hurtByTargetAI);
        this.targetSelector.addGoal(4, new NearestAttackableTargetPredicateGoal<Player>(this, Player.class, 0, false, true, (new EntityPredicate()).setDistance(getAttributeValue(Attributes.FOLLOW_RANGE)).setCustomPredicate(target -> {
            if (target instanceof Player) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((Player) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask) || target == getMisbehavedPlayer();
            }
            return true;
        }).setIgnoresLineOfSight()){
            @Override
            public void resetTask() {
                super.resetTask();
                setMisbehavedPlayerId(null);
            }
        });
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, 0, false, false, null));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, 0, false, false, (e) -> !(e instanceof ZombifiedPiglin)));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 0, false, false, null));
        this.goalSelector.addGoal(6, new SimpleAnimationAI<>(this, BELLY_ANIMATION, false, true));
        this.goalSelector.addGoal(6, new SimpleAnimationAI<EntityBarako>(this, TALK_ANIMATION, false, true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
//                whichDialogue = getWhichDialogue();
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarako>(this, BLESS_ANIMATION, false) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                blessingPlayer = getCustomer();
            }
        });
        this.goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarako>(this, SUPERNOVA_ANIMATION, false) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_SUPERNOVA_START.get(), 3f, 1f);
            }

            @Override
            public void tick() {
                super.tick();
                if (entity.getAnimationTick() == 30) {
                    playSound(MMSounds.ENTITY_SUPERNOVA_BLACKHOLE.get(), 2f, 1.2f);
                }
                if (entity.getAnimationTick() == 40) {
                    playSound(MMSounds.ENTITY_BARAKO_SCREAM.get(), 1.5f, 1f);
                }

                if (!entity.level.isClientSide) {
                    if (entity.getAnimationTick() == 44) {
                        Vec3 offset = new Vec3(1.1f, 0, 0);
                        offset = offset.rotateYaw((float) Math.toRadians(-entity.getYRot() - 90));
                        EntitySuperNova superNova = new EntitySuperNova(EntityHandler.SUPER_NOVA, entity.world, entity, entity.getX() + offset.x, entity.getY() + 0.05, entity.getZ() + offset.z);
                        level.addFreshEntity(superNova);
                    }
                }
            }
        });
        this.goalSelector.addGoal(2, new AnimationSunStrike<>(this, SUNSTRIKE_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationRadiusAttack<EntityBarako>(this, ATTACK_ANIMATION, 4f, ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get().floatValue(), 3f, 12, true){
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_BARAKO_BURST.get(), 1.7f, 1.5f);
            }
        });
        this.goalSelector.addGoal(2, new AnimationSpawnBarakoa(this, SPAWN_ANIMATION, false));
        this.goalSelector.addGoal(2, new AnimationSpawnBarakoa(this, SPAWN_SUNBLOCKERS_ANIMATION, true));
        this.goalSelector.addGoal(2, new AnimationSolarBeam<>(this, SOLAR_BEAM_ANIMATION));
        this.goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        this.goalSelector.addGoal(1, new AnimationDieAI<>(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, EntityBarakoa.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.4f;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.MAX_HEALTH, MAX_HEALTH * ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.healthMultiplier.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 40);
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() == NO_ANIMATION) {
            if (getTarget() == null && !isNoAi()) {
                int soundType = Mth.nextInt(rand, 0, 9);
                if (soundType < MMSounds.ENTITY_BARAKO_TALK.size()) {
                    this.playSound(MMSounds.ENTITY_BARAKO_TALK.get(soundType).get(), 2F, 1.0F);
                    this.setWhichDialogue(soundType + 1);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, TALK_ANIMATION);
                }
            } else {
                int soundType = Mth.nextInt(rand, 1, 10);
                if (soundType < 7) {
                    this.playSound(MMSounds.ENTITY_BARAKO_ANGRY.get(soundType - 1).get(), 2F, 1.0F);
//                    setWhichDialogue(soundType);
//                    AnimationHandler.INSTANCE.sendAnimationMessage(this, 3);
                }
            }
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_BARAKO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_BARAKO_DIE.get(), 2f, 1);
        return null;
    }

    @Override
    public boolean isPushableByEntity(Entity entity) {
        return false;
    }

    @Override
    public void tick() {
        setDeltaMovement(0, getDeltaMovement().y, 0);
        super.tick();
        if (tickCount == 1) {
            direction = getDirection();
        }
        if (!(getAnimation() == ATTACK_ANIMATION && getAnimationTick() >= 12 && getAnimationTick() <= 14)) this.repelEntities(1.2f, 1.2f, 1.2f, 1.2f);
        this.getYRot() = (direction - 1) * 90;
        this.yBodyRot = yRot;
//        this.posX = xo;
//        this.posZ = zo;

        if (!level.isClientSide && getHealthLost() >= HEALTH_LOST_BETWEEN_SUNBLOCKERS && getAnimation() == NO_ANIMATION && !isNoAi() && getEntitiesNearby(EntityBarakoaya.class, 40).size() < 3) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, SPAWN_SUNBLOCKERS_ANIMATION);
            setHealthLost(0);
        }
        if (getTarget() != null) {
            LivingEntity target = getTarget();
            this.setAngry(true);
            float entityHitAngle = (float) ((Math.atan2(target.getZ() - getZ(), target.getX() - getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = yRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = Math.abs(entityHitAngle - entityAttackingAngle);
            Vec3 betweenEntitiesVec = getPositionVec().subtract(target.position());
            boolean targetComingCloser = target.getDeltaMovement().dotProduct(betweenEntitiesVec) > 0 && target.getDeltaMovement().lengthSquared() > 0.015;
            if (getAnimation() == NO_ANIMATION && !isNoAi() && random.nextInt(80) == 0 && (targetDistance > 5.5 || isPotionActive(EffectHandler.SUNBLOCK)) && timeUntilBarakoa <= 0 && getEntitiesNearby(EntityBarakoa.class, 50).size() < 4) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SPAWN_ANIMATION);
                timeUntilBarakoa = BARAKOA_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && !isNoAi() && getHealthRatio() <= 0.6 && timeUntilLaser <= 0 && (entityRelativeAngle < 60 || entityRelativeAngle > 300) && getEntitySenses().canSee(target) && targetDistance < EntitySolarBeam.RADIUS_BARAKO) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SOLAR_BEAM_ANIMATION);
                timeUntilLaser = LASER_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && !isNoAi() && getHealthRatio() <= 0.6 && !isPotionActive(EffectHandler.SUNBLOCK) && timeUntilSupernova <= 0 && targetDistance <= 10.5) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SUPERNOVA_ANIMATION);
                timeUntilSupernova = SUPERNOVA_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && !isNoAi() && ((targetDistance <= 6f && targetComingCloser) || targetDistance < 4.f)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
            } else if (getAnimation() == NO_ANIMATION && !isNoAi() && timeUntilSunstrike <= 0) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SUNSTRIKE_ANIMATION);
                timeUntilSunstrike = getTimeUntilSunstrike();
            }
            if (hurtByTargetAI != null && !hurtByTargetAI.shouldContinueExecuting()) {
                hurtByTargetAI.resetTask();
            }
        } else {
            if (!level.isClientSide) {
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

        if (getAnimation() == NO_ANIMATION && !isNoAi() && getTarget() == null && random.nextInt(200) == 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BELLY_ANIMATION);
        }

        if (getAnimation() == BELLY_ANIMATION && (getAnimationTick() == 9 || getAnimationTick() == 29)) {
            this.playSound(MMSounds.ENTITY_BARAKO_BELLY.get(), 3f, 1f);
        }

//        if (getAnimation() == TALK_ANIMATION && getAnimationTick() == 1) {
//            whichDialogue = getWhichDialogue();
//        }

        if (getAnimation() == ATTACK_ANIMATION) {
            yHeadRot = yRot;
//            if (getAnimationTick() == 1) {
//                this.playSound(MMSounds.ENTITY_BARAKO_BURST, 1.7f, 1.5f);
//            }
            if (getAnimationTick() == 10) {
                if (level.isClientSide) {
                    spawnExplosionParticles(30);
                }
                this.playSound(MMSounds.ENTITY_BARAKO_ATTACK.get(), 1.7f, 0.9f);
            }
            if (getAnimationTick() <= 6 && level.isClientSide) {
                int particleCount = 8;
                while (--particleCount != 0) {
                    double radius = 2f;
                    double yaw = random.nextFloat() * 2 * Math.PI;
                    double pitch = random.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    float offsetX = (float) (-0.3 * Math.sin(yRot * Math.PI / 180));
                    float offsetZ = (float) (-0.3 * Math.cos(yRot * Math.PI / 180));
                    float offsetY = 1;
                    level.addParticle(new ParticleOrb.OrbData((float) getX() + offsetX, (float) getY() + offsetY, (float) getZ() + offsetZ, 6), getX() + ox + offsetX, getY() + offsetY + oy, getZ() + oz + offsetZ, 0, 0, 0);
                }
            }
        }

        if (getAnimation() == BLESS_ANIMATION) {
            yHeadRot = yRot;

            if (getAnimationTick() == 1) {
                blessingPlayer = getCustomer();
            }
            if (level.isClientSide && blessingPlayer != null) {
                blessingPlayerPos[0] = blessingPlayer.position().add(new Vec3(0, blessingPlayer.getHeight() / 2f, 0));
                if (getAnimationTick() > 5 && getAnimationTick() < 40) {
                    int particleCount = 2;
                    while (--particleCount != 0) {
                        double radius = 0.7f;
                        double yaw = random.nextFloat() * 2 * Math.PI;
                        double pitch = random.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        AdvancedParticleBase.spawnParticle(world, ParticleHandler.ORB2.get(), getX() + ox, getY() + 0.8f + oy, getZ() + oz, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 20, true, true, new ParticleComponent[]{
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
                if (getAnimationTick() % 15 == 0) {
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.RING2.get(), getX(), getY() + 0.8f, getZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 1, 223/255f, 66/255f, 1, 1, 15, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(5f, 35f), false)
                    });
                }
            }
        }

        if (getAnimation() == SUPERNOVA_ANIMATION) {
            if (level.isClientSide && betweenHandPos.length > 0) {
                superNovaMobEffects();
            }
            if (getAnimationTick() < 30) {
                List<LivingEntity> entities = getEntityLivingBaseNearby(16, 16, 16, 16);
                for (LivingEntity inRange : entities) {
                    if (inRange instanceof LeaderSunstrikeImmune) continue;
                    if (inRange instanceof Player && ((Player)inRange).abilities.disableDamage) continue;
                    Vec3 diff = inRange.position().subtract(getPositionVec().add(0, 3, 0));
                    diff = diff.normalize().scale(0.03);
                    inRange.setDeltaMovement(inRange.getDeltaMovement().subtract(diff));

                    if (inRange.getY() < getY() + 3) inRange.setDeltaMovement(inRange.getDeltaMovement().add(0, 0.075, 0));
                }
            }
        }

        if (tickCount % 40 == 0) {
            for (Player player : getPlayersNearby(15, 15, 15, 15)) {
                ItemStack headArmorStack = player.inventory.armorInventory.get(3);
                if (getTarget() != player && canAttack(player, GIVE_ACHIEVEMENT_PRED) && headArmorStack.getItem() instanceof BarakoaMask) {
                    if (player instanceof ServerPlayer) AdvancementHandler.SNEAK_VILLAGE_TRIGGER.trigger((ServerPlayer) player);
                }
            }
        }

        if (!level.isClientSide && getTarget() == null && getAnimation() != SOLAR_BEAM_ANIMATION && getAnimation() != SUPERNOVA_ANIMATION) {
            timeUntilHeal--;
            if (ConfigHandler.COMMON.MOBS.BARAKO.healsOutOfBattle.get() && timeUntilHeal <= 0) heal(0.3f);
            if (getHealth() == getMaxHealth()) setHealthLost(0);
        }
        else {
            timeUntilHeal = HEAL_PAUSE;
        }

        if (timeUntilSunstrike > 0) {
            timeUntilSunstrike--;
        }
        if (timeUntilLaser > 0 && getAnimation() != SUPERNOVA_ANIMATION) {
            timeUntilLaser--;
        }
        if (timeUntilBarakoa > 0) {
            timeUntilBarakoa--;
        }
        if (timeUntilSupernova > 0 && getAnimation() != SOLAR_BEAM_ANIMATION) {
            timeUntilSupernova--;
        }

//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, SOLAR_BEAM_ANIMATION);
//        }
    }

    private void superNovaMobEffects() {
        if (getAnimationTick() == 1) {
            superNovaKeyTrack1 = new ParticleComponent.KeyTrack(
                    new float[]{0, 25f, 32f, 0},
                    new float[]{0, 0.6f, 0.85f, 1}
            );
            superNovaKeyTrack2 = ParticleComponent.KeyTrack.oscillate(0, 7, 24);
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.SUN.get(), getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 33, true, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack1, false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack2, true)
            });
        }
        if (getAnimationTick() == 33) {
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.SUN_NOVA.get(), getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 20F, 1, 1, 1, 0, 1, 13, true, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
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
        if (getAnimationTick() == 32) {
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.FLARE.get(), getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 1,1,1, 0.7, 1, 3, true, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, ParticleComponent.constant(-0.15f), true),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0f, 22f, 0f},
                            new float[]{0, 0.2f, 1}
                    ), false)
            });
        }
        if (getAnimationTick() > 30 && getAnimationTick() < 41) {
            for (int i = 0; i < 6; i++) {
                float phaseOffset = random.nextFloat();
                double value = random.nextDouble() * 0.3 + 0.05;
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), betweenHandPos[0].x, betweenHandPos[0].y, betweenHandPos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 6, false, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 3f},
                                new float[]{0, 0.2f}
                        ), false),
                        new ParticleComponent.Orbit(betweenHandPos, ParticleComponent.KeyTrack.startAndEnd(0 + phaseOffset, -0.4f + phaseOffset), ParticleComponent.KeyTrack.startAndEnd(0.5f + random.nextFloat(), 0), ParticleComponent.constant(0), ParticleComponent.constant(0), ParticleComponent.constant(0), true),
                });
            }
        }
        if (getAnimationTick() > 1 && getAnimationTick() < 27) {
            for (int i = 0; i < 6; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 5, 0, 0);
                particlePos = particlePos.rotateYaw((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(betweenHandPos[0]);
                double value = random.nextDouble() * 0.5 + 0.1;
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 7, false, true, new ParticleComponent[]{
                        new ParticleComponent.Attractor(betweenHandPos, 1.1f, 1f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 3.5f},
                                new float[]{0, 0.2f}
                        ), false)
                });
            }
        }
        float timeFrac = Math.min((float)getAnimationTick() / 20f, 1f);
        if (getAnimationTick() > 1 && getAnimationTick() < 25 && getAnimationTick() % (int)(4 * (1 - timeFrac) + 1) == 0) {
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.RING_SPARKS.get(),  getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, random.nextFloat() * (float)Math.PI * 2, 5F, 1, 1, 1, 1, 1, 6 + random.nextFloat() * 3, true, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f + 20f * timeFrac * timeFrac + 10f * random.nextFloat() * timeFrac, 0f), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false)
            });
        }
        if (getAnimationTick() == 14) {
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.FLARE.get(),  getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 18, true, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, ParticleComponent.constant(-0.1f), true),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0f, 35f, 0f},
                            new float[]{0, 0.8f, 1}
                    ), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-5, 5, 42, 0), true)
            });
        }

        if (getAnimationTick() == 32) {
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.BURST_IN.get(),  getX(), getY(), getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 0, 0, 0, 1, 1, 10, true, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(25f, 0f), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 1f), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-2, 2, 42, 0), true),
            });
        }

        if (getAnimationTick() == 44) {
            float scale = 85f;
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.RING_BIG.get(), betweenHandPos[0].x, betweenHandPos[0].y, betweenHandPos[0].z, 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 1,1,1, 1, 1, 40, true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                            new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
            });
            scale = 120f;
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.GLOW.get(), betweenHandPos[0].x, betweenHandPos[0].y, betweenHandPos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.95, 0.9,0.35, 1, 1, 40, true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                            new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
            });
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (isPotionActive(EffectHandler.SUNBLOCK) && !source.canHarmInCreative()) {
            if (source.getImmediateSource() != null) playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
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

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    private boolean checkBlocksByFeet() {
        BlockState blockLeft;
        BlockState blockRight;
        BlockPos posLeft;
        BlockPos posRight;
        if (direction == 1) {
            posLeft = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            posRight = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            blockLeft = level.getBlockState(posLeft);
            blockRight = level.getBlockState(posRight);
        } else if (direction == 2) {
            posLeft = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            posRight = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            blockLeft = level.getBlockState(posLeft);
            blockRight = level.getBlockState(posRight);
        } else if (direction == 3) {
            posLeft = new BlockPos(Mth.floor(getX()) - 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            posRight = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            blockLeft = level.getBlockState(posLeft);
            blockRight = level.getBlockState(posRight);
        } else if (direction == 4) {
            posLeft = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) - 1);
            posRight = new BlockPos(Mth.floor(getX()) + 1, Math.round((float) (getY() - 1)), Mth.floor(getZ()) + 1);
            blockLeft = level.getBlockState(posLeft);
            blockRight = level.getBlockState(posRight);
        } else {
            return false;
        }
//        System.out.println(direction + ", " + (Mth.floor(posX) - 1) + ", " + Math.round((float) (posY - 1)) + ", " + Mth.floor(posZ) + 1);
        return blockLeft.getMaterial().blocksMotion() || blockRight.getMaterial().blocksMotion();
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.25F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = random.nextFloat() * 0.1F - 0.05f;
            float vx = velocity * Mth.cos(yaw);
            float vz = velocity * Mth.sin(yaw);
            level.addParticle(ParticleTypes.FLAME, getX(), getY() + 1, getZ(), vx, vy, vz);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(DIRECTION, 0);
        getEntityData().define(DIALOGUE, 0);
        getEntityData().define(ANGRY, false);
        Item tradeItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ConfigHandler.COMMON.MOBS.BARAKO.whichItem.get()));
        getEntityData().define(DESIRES, new ItemStack(tradeItem, ConfigHandler.COMMON.MOBS.BARAKO.howMany.get()));
        getEntityData().define(TRADED_PLAYERS, new CompoundTag());
        getEntityData().define(HEALTH_LOST, 0.f);
        getEntityData().define(MISBEHAVED_PLAYER, Optional.empty());
    }

    public int getDirection() {
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

    public void setTradedPlayersCompound(ListNBT players) {
        CompoundTag compound = new CompoundTag();
        compound.put("players", players);
        getEntityData().set(TRADED_PLAYERS, compound);
    }

    public Set<UUID> getTradedPlayers() {
        Set<UUID> tradedPlayers = new HashSet<>();
        CompoundTag compound = getEntityData().get(TRADED_PLAYERS);
        ListNBT players = compound.getList("players", Constants.NBT.TAG_INT_ARRAY);
        for (net.minecraft.nbt.INBT player : players) {
            tradedPlayers.add(NBTUtil.readUniqueId(player));
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
        if (canPayFor(input.getStack(), desires)) {
            input.decrStackSize(desires.getCount());
            return true;
        }
        return false;
    }

    public boolean hasTradedWith(Player player) {
        return getTradedPlayers().contains(Player.getUUID(player.getGameProfile()));
    }

    public void rememberTrade(Player player) {
        UUID uuid = Player.getUUID(player.getGameProfile());
        CompoundTag compound = getEntityData().get(TRADED_PLAYERS);
        ListNBT players = compound.getList("players", Constants.NBT.TAG_INT_ARRAY);
        players.add(NBTUtil.func_240626_a_(uuid));
        compound.put("players", players);
        getEntityData().set(TRADED_PLAYERS, compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("direction", getDirection());
        CompoundTag compoundTradedPlayers = getEntityData().get(TRADED_PLAYERS);
        ListNBT players = compoundTradedPlayers.getList("players", Constants.NBT.TAG_INT_ARRAY);
        compound.put("players", players);
        compound.putInt("HomePosX", this.getHomePosition().x());
        compound.putInt("HomePosY", this.getHomePosition().y());
        compound.putInt("HomePosZ", this.getHomePosition().z());
        compound.putFloat("healthLost", this.getHealthLost());
        if (this.getMisbehavedPlayerId() != null) {
            compound.putUniqueId("MisbehavedPlayer", this.getMisbehavedPlayerId());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setDirection(compound.getInt("direction"));
        ListNBT players = compound.getList("players", Constants.NBT.TAG_INT_ARRAY);
        setTradedPlayersCompound(players);
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        this.setHomePosAndDistance(new BlockPos(i, j, k), -1);
        setHealthLost(compound.getInt("healthLost"));
        UUID uuid;
        if (compound.hasUniqueId("MisbehavedPlayer")) {
            uuid = compound.getUniqueId("MisbehavedPlayer");
        } else {
            String s = compound.getString("MisbehavedPlayer");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s);
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
        return this.dataManager.get(MISBEHAVED_PLAYER).orElse((UUID)null);
    }

    public void setMisbehavedPlayerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(MISBEHAVED_PLAYER, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public LivingEntity getMisbehavedPlayer() {
        try {
            UUID uuid = this.getMisbehavedPlayerId();
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
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
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, BELLY_ANIMATION, TALK_ANIMATION, SUNSTRIKE_ANIMATION, ATTACK_ANIMATION, SPAWN_ANIMATION, SPAWN_SUNBLOCKERS_ANIMATION, SOLAR_BEAM_ANIMATION, BLESS_ANIMATION, SUPERNOVA_ANIMATION};
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        List<EntityBarakoa> barakoa = getEntitiesNearby(EntityBarakoa.class, 30, 20, 30, 30);
        for (EntityBarakoa entityBarakoa : barakoa) {
            if (entityBarakoa.isBarakoDevoted()) {
                if (entityBarakoa instanceof EntityBarakoaya) ((EntityBarakoaya)entityBarakoa).hasTriedOrSucceededTeleport = true;
                entityBarakoa.timeUntilDeath = random.nextInt(20);
            }
        }

        super.onDeath(cause);
    }

    public boolean isTrading() {
        return customer != null;
    }

    public Player getCustomer() {
        return customer;
    }

    public void setCustomer(Player customer) {
        this.customer = customer;
    }

    public void openGUI(Player Player) {
        setCustomer(Player);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.level.isClientSide && getTarget() == null && isAlive()) {
            Player.openContainer(new INamedContainerProvider() {
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, Player player) {
                    return new ContainerBarakoTrade(id, EntityBarako.this, playerInventory);
                }

                @Override
                public TextComponent getDisplayName() {
                    return EntityBarako.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected ActionResultType getEntityInteractionResult(Player player, Hand hand) {
        if (canTradeWith(player) && getTarget() == null && isAlive()) {
            openGUI(player);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    public boolean canTradeWith(Player player) {
        if (isTrading() || getHealth() <= 0) {
            return false;
        }
        ItemStack headStack = player.inventory.armorInventory.get(3);
        return headStack.getItem() instanceof BarakoaMask;
    }

    private static boolean canPayFor(ItemStack stack, ItemStack worth) {
        return stack.getItem() == worth.getItem() && stack.getCount() >= worth.getCount();
    }

    @Override
    protected boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.BARAKO.hasBossBar.get();
    }

    @Override
    protected BossEvent.Color bossBarColor() {
        return BossEvent.Color.YELLOW;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LootTableHandler.BARAKO;
    }

    @Override
    public SpawnGroupData finalizeSpawn(IServerLevel world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        if (reason == MobSpawnType.SPAWN_EGG) {
            // Try to guess which player spawned Barako, rotate towards them
            List<Player> players = getPlayersNearby(5, 5, 5, 5);
            if (!players.isEmpty()) {
                Player closestPlayer = players.get(0);
                float closestPlayerDist = 6;
                for (Player player : players) {
                    if (player.getMainHandItem().getItem() == ItemHandler.BARAKO_SPAWN_EGG || player.getMainHandItem().getItem() == ItemHandler.BARAKO_SPAWN_EGG) {
                        float thisDist = this.getDistance(player);
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
        if (reason != MobSpawnType.STRUCTURE) setHomePosAndDistance(getPosition(), -1);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
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
        return MMSounds.MUSIC_BARAKO_THEME.get();
    }
}
