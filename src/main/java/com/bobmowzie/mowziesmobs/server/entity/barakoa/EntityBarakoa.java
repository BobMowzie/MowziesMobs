package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRibbon;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.SmartBodyHelper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
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

public abstract class EntityBarakoa extends MowzieEntity implements RangedAttackMob {
    public static final Animation DIE_ANIMATION = Animation.create(70);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation ATTACK_ANIMATION = Animation.create(19);
    public static final Animation PROJECTILE_ATTACK_ANIMATION = Animation.create(20);
    public static final Animation IDLE_ANIMATION = Animation.create(35);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(25);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(26);
    public static final Animation BLOCK_ANIMATION = Animation.create(10);

    public static final Animation TELEPORT_ANIMATION = Animation.create(27);
    public static final Animation HEAL_START_ANIMATION = Animation.create(25);
    public static final Animation HEAL_LOOP_ANIMATION = Animation.create(20);
    public static final Animation HEAL_STOP_ANIMATION = Animation.create(6);

    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MASK = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WEAPON = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> HEALPOSX = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALPOSY = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALPOSZ = SynchedEntityData.defineId(EntityBarakoa.class, EntityDataSerializers.FLOAT);
    public ControlledAnimation doWalk = new ControlledAnimation(3);
    public ControlledAnimation dancing = new ControlledAnimation(7);
    private boolean circleDirection = true;
    protected int circleTick = 0;
    protected boolean attacking = false;
    private int timeSinceAttack = 0;
    private int cryDelay = -1;
    private int danceTimer = 0;
    private int ticksWithoutTarget;
    public int timeUntilDeath = -1;

    @OnlyIn(Dist.CLIENT)
    public Vec3[] staffPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] barakoPos;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] myPos;

    protected Vec3 teleportDestination;

    public EntityBarakoa(EntityType<? extends EntityBarakoa> type, Level world) {
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
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -8);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(0, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        goalSelector.addGoal(0, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        goalSelector.addGoal(1, new AnimationDieAI<>(this));
        goalSelector.addGoal(3, new EntityAIAvoidEntity<>(this, EntitySunstrike.class, EntitySunstrike::isStriking, 3, 0.7F));
        goalSelector.addGoal(2, new AnimationBlockAI<>(this, BLOCK_ANIMATION));
        goalSelector.addGoal(2, new AnimationAttackAI<>(this, ATTACK_ANIMATION, MMSounds.ENTITY_BARAKOA_SWING.get(), null, 1, 2.5f, 1, 9, true));
        goalSelector.addGoal(2, new AnimationProjectileAttackAI<EntityBarakoa>(this, PROJECTILE_ATTACK_ANIMATION, 9, MMSounds.ENTITY_BARAKOA_BLOWDART.get(), true) {
            @Override
            public void start() {
                super.start();
                playSound(MMSounds.ENTITY_BARAKOA_INHALE.get(), 0.7f, 1.2f);
            }
        });
        goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        goalSelector.addGoal(4, new SimpleAnimationAI<EntityBarakoa>(this, IDLE_ANIMATION, false, true) {
            private LivingEntity talkTarget;
            private final TargetingConditions pred = TargetingConditions.forNonCombat().range(8);

            @Override
            public void start() {
                super.start();
                LivingEntity player = this.entity.level.getNearestEntity(Player.class, pred, entity, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), this.entity.getBoundingBox().inflate(8.0D, 3.0D, 8.0D));
                LivingEntity barakoa = this.entity.level.getNearestEntity(EntityBarakoa.class, pred, this.entity, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), this.entity.getBoundingBox().inflate(8.0D, 3.0D, 8.0D));
                if (player == null) talkTarget = barakoa;
                else if (barakoa == null) talkTarget = player;
                else if (random.nextBoolean()) talkTarget = player;
                else talkTarget = barakoa;
            }

            @Override
            public void tick() {
                super.tick();
                if (talkTarget != null) this.entity.lookControl.setLookAt(this.talkTarget, (float)this.entity.getMaxHeadYRot(), (float)this.entity.getMaxHeadXRot());
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, TELEPORT_ANIMATION, true, false) {
            private Vec3 teleportStart;

            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 2) playSound(MMSounds.ENTITY_BARAKOA_TELEPORT.get(entity.random.nextInt(3)).get(), 3f, 1);
                if (getAnimationTick() == 16) playSound(MMSounds.ENTITY_BARAKOA_TELEPORT.get(entity.random.nextInt(3)).get(), 3f, 1.2f);
                int startMoveFrame = 7;
                int endMoveFrame = 14;
                if (entity.getAnimationTick() == startMoveFrame) teleportStart = entity.position();
                if (teleportStart != null && entity.teleportDestination != null && entity.getAnimationTick() > startMoveFrame && entity.getAnimationTick() < endMoveFrame) {
                    float t = (getAnimationTick() - startMoveFrame) / (float) (endMoveFrame - startMoveFrame);
                    t = (float) (0.5 - 0.5 * Math.cos(t * Math.PI));
                    Vec3 newPos = teleportStart.add(entity.teleportDestination.subtract(teleportStart).scale(t));
                    entity.teleportTo(newPos.x(), newPos.y(), newPos.z());
                    entity.getNavigation().stop();
                }
                if (entity.teleportDestination != null && entity.getAnimationTick() == endMoveFrame) {
                    entity.teleportTo(entity.teleportDestination.x(), entity.teleportDestination.y(), entity.teleportDestination.z());
                    entity.setDeltaMovement(0, 0, 0);
                    entity.getNavigation().stop();
                }
                if (entity.getTarget() != null) entity.getLookControl().setLookAt(entity.getTarget(), 30, 30);
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_START_ANIMATION, true, false) {
            @Override
            public void tick() {
                super.tick();
                EntityBarakoa sunblocker = entity;
                if (sunblocker.getTarget() != null) {
                    sunblocker.getLookControl().setLookAt(sunblocker.getTarget(), entity.getMaxHeadYRot(), entity.getMaxHeadXRot());
                }
                if (sunblocker.getAnimationTick() == 19) {
                    playSound(MMSounds.ENTITY_BARAKOA_HEAL_START.get(entity.random.nextInt(3)).get(), 4, 1);
                    MowziesMobs.PROXY.playSunblockSound(sunblocker);
                }
                if (sunblocker.getAnimationTick() >= 19) {
                    EffectHandler.addOrCombineEffect(entity, MobEffects.GLOWING, 5, 0, false, false);
                }
                if (sunblocker.getAnimationTick() == 23)
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_LOOP_ANIMATION);
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_STOP_ANIMATION, false, false));
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_LOOP_ANIMATION, true, false) {
            @Override
            public void tick() {
                super.tick();
                EntityBarakoa sunblocker = entity;
                EffectHandler.addOrCombineEffect(entity, MobEffects.GLOWING, 5, 0, false, false);
                if (sunblocker.getTarget() != null) {
                    sunblocker.getLookControl().setLookAt(sunblocker.getTarget(), entity.getMaxHeadYRot(), entity.getMaxHeadXRot());
                }
                else {
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_STOP_ANIMATION);
                }
                if (getAnimationTick() == 19)
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_LOOP_ANIMATION);
            }
        });
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.4));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityBarakoa.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityBarako.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
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
                return !(headArmorStack.getItem() instanceof BarakoaMask);
            }
            return true;
        }));
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() == DEACTIVATE_ANIMATION) {
            return null;
        }
        if (!active || danceTimer != 0 || (getEntitiesNearby(EntityBarakoa.class, 8, 3, 8, 8).isEmpty() && getEntitiesNearby(EntityBarako.class, 8, 3, 8, 8).isEmpty() && getEntitiesNearby(Player.class, 8, 3, 8, 8).isEmpty())) {
            return null;
        }
        if (getTarget() == null) {
            int i = Mth.nextInt(random, 0, 11);
            if (i < MMSounds.ENTITY_BARAKOA_TALK.size()) {
                playSound(MMSounds.ENTITY_BARAKOA_TALK.get(i).get(), 1, 1.5f);
                AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
            }
        } else {
            int i = Mth.nextInt(random, 0, 7);
            if (i < MMSounds.ENTITY_BARAKOA_ANGRY.size()) {
                playSound(MMSounds.ENTITY_BARAKOA_ANGRY.get(i).get(), 1, 1.6f);
            }
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return active ? MMSounds.ENTITY_BARAKOA_HURT.get() : null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.MAX_HEALTH, 8);
    }

    protected void updateAttackAI() {
        if (!level.isClientSide && getTarget() != null && !getTarget().isAlive()) setTarget(null);

        if (timeSinceAttack < 80) {
            timeSinceAttack++;
        }
        if (getTarget() != null) {
            if (targetDistance > 6.5) {
                getNavigation().moveTo(getTarget(), 0.6);
            } else {
                if (!attacking) {
                    updateCircling();
                }
            }
            if (random.nextInt(80) == 0 && timeSinceAttack == 80 && getSensing().hasLineOfSight(getTarget())) {
                attacking = true;
                if (getAnimation() == NO_ANIMATION && getWeapon() == 0) {
                    getNavigation().moveTo(getTarget(), 0.5);
                }
            }
            if (attacking && getAnimation() == NO_ANIMATION && getSensing().hasLineOfSight(getTarget())) {
                if (targetDistance <= 2.5 && getWeapon() == 0) {
                    attacking = false;
                    timeSinceAttack = 0;
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
                }
                if (getWeapon() == 1) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, PROJECTILE_ATTACK_ANIMATION);
                }
            }
        } else {
            attacking = false;
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        if (canHoldVaryingWeapons()) {
            setWeapon(random.nextInt(3) == 0 ? 1 : 0);
        }
        if (reason == MobSpawnType.COMMAND && !(this instanceof EntityBarakoana) && !(this instanceof EntityBarakoaya) && !(this instanceof EntityBarakoayaToPlayer)) setMask(MaskType.from(Mth.nextInt(random, 1, 4)));
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    protected boolean canHoldVaryingWeapons() {
        return true;
    }

    protected void updateCircling() {
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
            if (!attacking && targetDistance < 4.5) {
                circleEntity(target, 7, 0.3f, true, circleTick, 0, 1.75f);
            } else {
                circleEntity(target, 7, 0.3f, true, circleTick, 0, 1);
            }
            attacking = false;
        }
    }

    @Override
    public void tick() {
        doWalk.updatePrevTimer();
        dancing.updatePrevTimer();
        super.tick();
        if (!level.isClientSide && active && !getActive()) {
            setActive(true);
        }
        active = getActive();
        if (!active) {
            getNavigation().stop();
            setYRot(yRotO);
            yBodyRot = getYRot();
            if ((onGround || isInWater() || isInLava()) && getAnimation() == NO_ANIMATION) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                playSound(MMSounds.ENTITY_BARAKOA_EMERGE.get(), 1, 1);
            }
            return;
        }
        updateAttackAI();
        if (getAnimation() != NO_ANIMATION) {
            getNavigation().stop();
        }

        if (getDancing()) {
            setDancing(false);
            danceTimer++;
        }

        if (getAnimation() == NO_ANIMATION || getAnimation() == IDLE_ANIMATION) {
            doWalk.increaseTimer();
        } else {
            doWalk.decreaseTimer();
        }

        if (danceTimer != 0 && danceTimer != 30) {
            danceTimer++;
            dancing.increaseTimer();
        } else {
            danceTimer = 0;
            dancing.decreaseTimer();
        }
        if (!level.isClientSide && getAnimation() == NO_ANIMATION && danceTimer == 0 && random.nextInt(800) == 0 && getTarget() != null) {
            setDancing(true);
            playSound(MMSounds.ENTITY_BARAKOA_BATTLECRY_2.get(), 1.2f, 1.5f);
        }
        if (getAnimation() != NO_ANIMATION) {
            danceTimer = 0;
        }

        if (cryDelay > -1) {
            cryDelay--;
        }
        if (cryDelay == 0) {
            playSound(MMSounds.ENTITY_BARAKOA_BATTLECRY.get(), 1.5f, 1.5f);
        }
        if (getTarget() != null && ticksWithoutTarget > 3) {
            cryDelay = Mth.nextInt(random, -15, 30);
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 5) {
            playSound(MMSounds.ENTITY_BARAKOA_SHOUT.get(), 1, 1.1f);
        }
//        if (getAnimation() == PROJECTILE_ATTACK_ANIMATION && getAnimationTick() == 1) {
//            playSound(MMSounds.ENTITY_BARAKOA_INHALE, 0.7f, 1.2f);
//        }

        if (level.isClientSide && getAnimation() == HEAL_START_ANIMATION && getAnimationTick() == 22 && staffPos != null && staffPos.length >= 1)
            staffPos[0] = position().add(0, getEyeHeight(), 0);
        if ((getAnimation() == HEAL_START_ANIMATION && getAnimationTick() >= 23) || getAnimation() == HEAL_LOOP_ANIMATION) {
            spawnHealParticles();
            sunBlockTarget();
        }

        if (getAnimation() == TELEPORT_ANIMATION) {
            if (level.isClientSide) {
                myPos[0] = position().add(0, 1.2f, 0);
                if (getAnimationTick() == 5) {
                    ParticleComponent.KeyTrack keyTrack1 = ParticleComponent.KeyTrack.oscillate(0, 2, 24);
                    ParticleComponent.KeyTrack keyTrack2 = new ParticleComponent.KeyTrack(new float[]{0, 18, 18, 0}, new float[]{0, 0.2f, 0.8f, 1});
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.SUN.get(), getX(), getY(), getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 15, true, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(myPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack2, false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack1, true),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                    });
                }
                myPos[0] = position().add(0, 1.2f, 0);
                if (getAnimationTick() == 4 || getAnimationTick() == 18) {
                    int num = 5;
                    for (int i = 0; i < num * num; i++) {
                        Vec3 v = new Vec3((0.3 + 0.15 * random.nextFloat()) * 0.8, 0, 0);
                        float increment = (float)Math.PI * 2f / (float) num;
//                        v = v.rotatePitch(increment * i);
                        v = v.yRot(increment * random.nextFloat() + increment * (i / (float)num));
                        v = v.zRot(increment * random.nextFloat() + increment * (i % num));
                        AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL.get(), myPos[0].x(), myPos[0].y(), myPos[0].z(), v.x(), v.y(), v.z(), true, 0, 0, 0, 0, 4f, 0.98, 0.94, 0.39, 1, 0.8, 6 + random.nextFloat() * 4, true, false, new ParticleComponent[] {
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                        new float[] {4f, 0},
                                        new float[] {0.8f, 1}
                                ), false)
                        });
                    }
                }
            }
        }

        if (getTarget() == null) {
            ticksWithoutTarget++;
        } else {
            ticksWithoutTarget = 0;
        }

        if (timeUntilDeath > 0) timeUntilDeath--;
        else if (timeUntilDeath == 0) {
            hurt(DamageSource.indirectMagic(this, null), getHealth());
        }

//        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, TELEPORT_ANIMATION);
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (animation == ACTIVATE_ANIMATION) {
            setActive(true);
            active = true;
        }
        if (animation == DEACTIVATE_ANIMATION) {
            discard() ;
            ItemBarakoaMask mask = ItemHandler.BARAKOA_MASK_FURY;
            switch (getMask()) {
                case BLISS:
                    mask = ItemHandler.BARAKOA_MASK_BLISS;
                    break;
                case FEAR:
                    mask = ItemHandler.BARAKOA_MASK_FEAR;
                    break;
                case FURY:
                    mask = ItemHandler.BARAKOA_MASK_FURY;
                    break;
                case MISERY:
                    mask = ItemHandler.BARAKOA_MASK_MISERY;
                    break;
                case RAGE:
                    mask = ItemHandler.BARAKOA_MASK_RAGE;
                    break;
                case FAITH:
                    mask = ItemHandler.BARAKOA_MASK_FAITH;
                    break;
            }
            if (!level.isClientSide) {
                ItemEntity itemEntity = spawnAtLocation(getDeactivatedMask(mask), 1.5f);
                if (itemEntity != null) {
                    ItemStack item = itemEntity.getItem();
                    item.setDamageValue((int) Math.ceil((1.0f - getHealthRatio()) * item.getMaxDamage()));
                    item.setHoverName(this.getCustomName());
                }
            }
        }
    }

    protected ItemStack getDeactivatedMask(ItemBarakoaMask mask) {
        return new ItemStack(mask);
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_BARAKOA_DIE.get(), 1f, 0.95f + random.nextFloat() * 0.1f);
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

    public MaskType getMask() {
        return MaskType.from(getEntityData().get(MASK));
    }

    public void setMask(MaskType type) {
        getEntityData().set(MASK, type.ordinal());
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
        compound.putInt("mask", getMask().ordinal());
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

        dart.setBaseDamage(dart.getBaseDamage() * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.attackMultiplier.get());

        this.level.addFreshEntity(dart);
        attacking = false;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (getAnimation() == DEACTIVATE_ANIMATION) {
            return false;
        }
        Entity entity = source.getEntity();
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
        if (angleFlag && getMask().canBlock && entity instanceof LivingEntity && (getAnimation() == NO_ANIMATION || getAnimation() == HURT_ANIMATION || getAnimation() == BLOCK_ANIMATION) && !source.isBypassArmor()) {
            blockingEntity = (LivingEntity) entity;
            playSound(SoundEvents.SHIELD_BLOCK, 0.3F, 1.5F);
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BLOCK_ANIMATION);
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        switch (getMask()) {
            case BLISS:
                return LootTableHandler.BARAKOA_BLISS;
            case FEAR:
                return LootTableHandler.BARAKOA_FEAR;
            case FURY:
                return LootTableHandler.BARAKOA_FURY;
            case MISERY:
                return LootTableHandler.BARAKOA_MISERY;
            case RAGE:
                return LootTableHandler.BARAKOA_RAGE;
            case FAITH:
                return LootTableHandler.BARAKOA_FAITH;
        }
        return LootTableHandler.BARAKOA_FURY;
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
        return ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION, PROJECTILE_ATTACK_ANIMATION, BLOCK_ANIMATION, IDLE_ANIMATION, ACTIVATE_ANIMATION, DEACTIVATE_ANIMATION, TELEPORT_ANIMATION, HEAL_LOOP_ANIMATION, HEAL_START_ANIMATION, HEAL_STOP_ANIMATION};
    }

    public boolean isBarakoDevoted() {
        return true;
    }

    public int randomizeWeapon() {
        return random.nextInt(3) == 0 ? 1 : 0;
    }

    public boolean canHeal(LivingEntity entity) {
        return false;
    }

    public void spawnHealParticles() {
        if (getTarget() != null) {
            setHealPos(getTarget().position().add(new Vec3(0, getTarget().getBbHeight() / 2f, 0)));
        }
        if (level.isClientSide && barakoPos != null) {
            barakoPos[0] = getHealPos();
            if (staffPos != null && staffPos[0] != null) {
                double dist = Math.max(barakoPos[0].distanceTo(staffPos[0]), 0.01);
                double radius = 0.5f;
                double yaw = random.nextFloat() * 2 * Math.PI;
                double pitch = random.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                if (tickCount % 5 == 0) AdvancedParticleBase.spawnParticle(level, ParticleHandler.ARROW_HEAD.get(), staffPos[0].x(), staffPos[0].y(), staffPos[0].z(), 0, 0, 0, false, 0, 0, 0, 0, 3.5F, 0.95, 0.9, 0.35, 0.75, 1, Math.min(2 * dist, 60), true, false, new ParticleComponent[]{
                        new ParticleComponent.Attractor(barakoPos, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                        new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                        }),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_X, new ParticleComponent.Oscillator(0, (float) ox, (float) (1 * dist), 2.5f), true),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Y, new ParticleComponent.Oscillator(0, (float) oy, (float) (1 * dist), 2.5f), true),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Z, new ParticleComponent.Oscillator(0, (float) oz, (float) (1 * dist), 2.5f), true),
                        new ParticleComponent.FaceMotion(),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                });
                if (tickCount % 5 == 0) AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING2.get(), staffPos[0].x(), staffPos[0].y(), staffPos[0].z(), 0, 0, 0, true, 0, 0, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
                });
                int spawnFreq = 5;
                if (tickCount % spawnFreq == 0) ParticleRibbon.spawnRibbon(level, ParticleHandler.RIBBON_SQUIGGLE.get(), (int)(0.5 * dist), staffPos[0].x(), staffPos[0].y(), staffPos[0].z(), 0, 0, 0, true, 0, 0, 0, 0.5F, 0.95, 0.9, 0.35, 0.75, 1, spawnFreq, true, new ParticleComponent[]{
                        new RibbonComponent.BeamPinning(staffPos, barakoPos),
                        new RibbonComponent.PanTexture(0, 1)
                });
            }
        }
    }

    protected void sunBlockTarget() {

    }
}
