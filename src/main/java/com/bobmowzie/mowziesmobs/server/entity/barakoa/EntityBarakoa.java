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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.controller.BodyController;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.resources.Hand;
import net.minecraft.resources.math.MathHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityBarakoa extends MowzieEntity implements IRangedAttackMob {
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

    private static final EntityDataAccessor<Boolean> DANCING = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MASK = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.VARINT);
    private static final EntityDataAccessor<Integer> WEAPON = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.VARINT);
    private static final EntityDataAccessor<Boolean> ACTIVE = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> HEALPOSX = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALPOSY = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALPOSZ = EntityDataManager.createKey(EntityBarakoa.class, EntityDataSerializers.FLOAT);
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

    public EntityBarakoa(EntityType<? extends EntityBarakoa> type, World world) {
        super(type, world);
        setMask(MaskType.from(MathHelper.nextInt(rand, 1, 4)));
        stepHeight = 1;
        circleTick += rand.nextInt(200);
        frame += rand.nextInt(50);
        experienceValue = 6;
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
        setPathPriority(PathNodeType.DAMAGE_FIRE, -8);
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(0, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        goalSelector.addGoal(0, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        goalSelector.addGoal(1, new AnimationDieAI<>(this));
        goalSelector.addGoal(3, new EntityAIAvoidEntity<>(this, EntitySunstrike.class, EntitySunstrike::isStriking, 3, 0.7F));
        goalSelector.addGoal(2, new AnimationBlockAI<>(this, BLOCK_ANIMATION));
        goalSelector.addGoal(2, new AnimationAttackAI<>(this, ATTACK_ANIMATION, MMSounds.ENTITY_BARAKOA_SWING.get(), null, 1, 2.5f, 1, 9, true));
        goalSelector.addGoal(2, new AnimationProjectileAttackAI<EntityBarakoa>(this, PROJECTILE_ATTACK_ANIMATION, 9, MMSounds.ENTITY_BARAKOA_BLOWDART.get(), true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_BARAKOA_INHALE.get(), 0.7f, 1.2f);
            }
        });
        goalSelector.addGoal(3, new AnimationTakeDamage<>(this));
        goalSelector.addGoal(4, new SimpleAnimationAI<EntityBarakoa>(this, IDLE_ANIMATION, false, true) {
            private LivingEntity talkTarget;
            private final EntityPredicate pred = new EntityPredicate().allowFriendlyFire().allowInvulnerable().setDistance(8).setSkipAttackChecks();

            @Override
            public void startExecuting() {
                super.startExecuting();
                LivingEntity player = this.entity.world.getClosestEntity(Player.class, pred, entity, entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ(), this.entity.getBoundingBox().grow(8.0D, 3.0D, 8.0D));
                LivingEntity barakoa = this.entity.world.getClosestEntity(EntityBarakoa.class, pred, this.entity, entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ(), this.entity.getBoundingBox().grow(8.0D, 3.0D, 8.0D));
                if (player == null) talkTarget = barakoa;
                else if (barakoa == null) talkTarget = player;
                else if (rand.nextBoolean()) talkTarget = player;
                else talkTarget = barakoa;
            }

            @Override
            public void tick() {
                super.tick();
                if (talkTarget != null) this.entity.lookController.setLookPositionWithEntity(this.talkTarget, (float)this.entity.getHorizontalFaceSpeed(), (float)this.entity.getVerticalFaceSpeed());
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, TELEPORT_ANIMATION, true, false) {
            private Vec3 teleportStart;

            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 2) playSound(MMSounds.ENTITY_BARAKOA_TELEPORT.get(entity.rand.nextInt(3)).get(), 3f, 1);
                if (getAnimationTick() == 16) playSound(MMSounds.ENTITY_BARAKOA_TELEPORT.get(entity.rand.nextInt(3)).get(), 3f, 1.2f);
                int startMoveFrame = 7;
                int endMoveFrame = 14;
                if (entity.getAnimationTick() == startMoveFrame) teleportStart = entity.getPositionVec();
                if (entity.teleportDestination != null && entity.getAnimationTick() > startMoveFrame && entity.getAnimationTick() < endMoveFrame) {
                    float t = (getAnimationTick() - startMoveFrame) / (float)(endMoveFrame - startMoveFrame);
                    t = (float) (0.5 - 0.5 * Math.cos(t * Math.PI));
                    Vec3 newPos = teleportStart.add(teleportDestination.subtract(teleportStart).scale(t));
                    entity.setPositionAndUpdate(newPos.getX(), newPos.getY(), newPos.getZ());
                    entity.getNavigator().clearPath();
                }
                if (entity.teleportDestination != null && entity.getAnimationTick() == endMoveFrame) {
                    entity.setPositionAndUpdate(entity.teleportDestination.getX(), entity.teleportDestination.getY(), entity.teleportDestination.getZ());
                    entity.setMotion(0, 0, 0);
                    entity.getNavigator().clearPath();
                }
                if (entity.getAttackTarget() != null) entity.getLookController().setLookPositionWithEntity(entity.getAttackTarget(), 30, 30);
            }
        });
        goalSelector.addGoal(2, new SimpleAnimationAI<EntityBarakoa>(this, HEAL_START_ANIMATION, true, false) {
            @Override
            public void tick() {
                super.tick();
                EntityBarakoa sunblocker = entity;
                if (sunblocker.getAttackTarget() != null) {
                    sunblocker.getLookController().setLookPositionWithEntity(sunblocker.getAttackTarget(), entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
                }
                if (sunblocker.getAnimationTick() == 19) {
                    playSound(MMSounds.ENTITY_BARAKOA_HEAL_START.get(entity.rand.nextInt(3)).get(), 4, 1);
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
                if (sunblocker.getAttackTarget() != null) {
                    sunblocker.getLookController().setLookPositionWithEntity(sunblocker.getAttackTarget(), entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
                }
                else {
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_STOP_ANIMATION);
                }
                if (getAnimationTick() == 19)
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, HEAL_LOOP_ANIMATION);
            }
        });
        goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.4));
        goalSelector.addGoal(8, new LookAtGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new LookAtGoal(this, EntityBarakoa.class, 8.0F));
        goalSelector.addGoal(8, new LookAtGoal(this, EntityBarako.class, 8.0F));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        registerTargetGoals();
    }

    protected void registerTargetGoals() {

    };

    protected void registerHuntingTargetGoals() {
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, 200, true, false, target -> {
            float volume = target.getWidth() * target.getWidth() * target.getHeight();
            return (target.getAttribute(Attributes.ATTACK_DAMAGE) == null || target.getAttributeValue(Attributes.ATTACK_DAMAGE) < 3.0D) && volume > 0.1 && volume < 6;
        }));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, 0, true, false, (e) -> !(e instanceof ZombifiedPiglin)));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 0, true, false, null));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ZoglinEntity.class, 0, true, false, null));
        this.targetSelector.addGoal(6, new AvoidEntityGoal<>(this, CreeperEntity.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, true, target -> {
            if (target instanceof Player) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((Player) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask);
            }
            return true;
        }));
    }

    @Override
    protected BodyController createBodyController() {
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
        if (getAttackTarget() == null) {
            int i = MathHelper.nextInt(rand, 0, 11);
            if (i < MMSounds.ENTITY_BARAKOA_TALK.size()) {
                playSound(MMSounds.ENTITY_BARAKOA_TALK.get(i).get(), 1, 1.5f);
                AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
            }
        } else {
            int i = MathHelper.nextInt(rand, 0, 7);
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

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MowzieEntity.createAttributes().createMutableAttribute(Attributes.ATTACK_DAMAGE, 3 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.attackMultiplier.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, 8 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.healthMultiplier.get());
    }

    protected void updateAttackAI() {
        if (!world.isClientSide && getAttackTarget() != null && !getAttackTarget().isAlive()) setAttackTarget(null);

        if (timeSinceAttack < 80) {
            timeSinceAttack++;
        }
        if (getAttackTarget() != null) {
            if (targetDistance > 6.5) {
                getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.6);
            } else {
                if (!attacking) {
                    updateCircling();
                }
            }
            if (rand.nextInt(80) == 0 && timeSinceAttack == 80 && getEntitySenses().canSee(getAttackTarget())) {
                attacking = true;
                if (getAnimation() == NO_ANIMATION && getWeapon() == 0) {
                    getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.5);
                }
            }
            if (attacking && getAnimation() == NO_ANIMATION && getEntitySenses().canSee(getAttackTarget())) {
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
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData livingData, CompoundNBT compound) {
        if (canHoldVaryingWeapons()) {
            setWeapon(rand.nextInt(3) == 0 ? 1 : 0);
        }
        if (reason == SpawnReason.COMMAND && !(this instanceof EntityBarakoana) && !(this instanceof EntityBarakoaya) && !(this instanceof EntityBarakoayaToPlayer)) setMask(MaskType.from(MathHelper.nextInt(rand, 1, 4)));
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }

    protected boolean canHoldVaryingWeapons() {
        return true;
    }

    protected void updateCircling() {
        LivingEntity target = getAttackTarget();
        if (target != null) {
            if (rand.nextInt(200) == 0) {
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
        super.tick();
        if (!world.isClientSide && active && !getActive()) {
            setActive(true);
        }
        active = getActive();
        if (!active) {
            getNavigator().clearPath();
            rotationYaw = prevRotationYaw;
            renderYawOffset = rotationYaw;
            if ((onGround || isInWater() || isInLava()) && getAnimation() == NO_ANIMATION) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                playSound(MMSounds.ENTITY_BARAKOA_EMERGE.get(), 1, 1);
            }
            return;
        }
        updateAttackAI();
        if (getAnimation() != NO_ANIMATION) {
            getNavigator().clearPath();
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
        if (!world.isClientSide && getAnimation() == NO_ANIMATION && danceTimer == 0 && rand.nextInt(800) == 0) {
            setDancing(true);
            playSound(MMSounds.ENTITY_BARAKOA_BATTLECRY_2.get(), 1.2f, 1.3f);
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
        if (getAttackTarget() != null && ticksWithoutTarget > 3) {
            cryDelay = MathHelper.nextInt(rand, -15, 30);
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 5) {
            playSound(MMSounds.ENTITY_BARAKOA_SHOUT.get(), 1, 1.1f);
        }
//        if (getAnimation() == PROJECTILE_ATTACK_ANIMATION && getAnimationTick() == 1) {
//            playSound(MMSounds.ENTITY_BARAKOA_INHALE, 0.7f, 1.2f);
//        }

        if (world.isClientSide && getAnimation() == HEAL_START_ANIMATION && getAnimationTick() == 22 && staffPos != null && staffPos.length >= 1)
            staffPos[0] = getPositionVec().add(0, getEyeHeight(), 0);
        if ((getAnimation() == HEAL_START_ANIMATION && getAnimationTick() >= 23) || getAnimation() == HEAL_LOOP_ANIMATION) {
            spawnHealParticles();
            sunBlockTarget();
        }

        if (getAnimation() == TELEPORT_ANIMATION) {
            if (world.isClientSide) {
                myPos[0] = getPositionVec().add(0, 1.2f, 0);
                if (getAnimationTick() == 5) {
                    ParticleComponent.KeyTrack keyTrack1 = ParticleComponent.KeyTrack.oscillate(0, 2, 24);
                    ParticleComponent.KeyTrack keyTrack2 = new ParticleComponent.KeyTrack(new float[]{0, 18, 18, 0}, new float[]{0, 0.2f, 0.8f, 1});
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.SUN.get(), getPosX(), getPosY(), getPosZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 15, true, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(myPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack2, false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack1, true),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                    });
                }
                myPos[0] = getPositionVec().add(0, 1.2f, 0);
                if (getAnimationTick() == 4 || getAnimationTick() == 18) {
                    int num = 5;
                    for (int i = 0; i < num * num; i++) {
                        Vec3 v = new Vec3((0.3 + 0.15 * rand.nextFloat()) * 0.8, 0, 0);
                        float increment = (float)Math.PI * 2f / (float) num;
//                        v = v.rotatePitch(increment * i);
                        v = v.rotateYaw(increment * rand.nextFloat() + increment * (i / (float)num));
                        v = v.rotateRoll(increment * rand.nextFloat() + increment * (i % num));
                        AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), myPos[0].getX(), myPos[0].getY(), myPos[0].getZ(), v.getX(), v.getY(), v.getZ(), true, 0, 0, 0, 0, 4f, 0.98, 0.94, 0.39, 1, 0.8, 6 + rand.nextFloat() * 4, true, false, new ParticleComponent[] {
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                        new float[] {4f, 0},
                                        new float[] {0.8f, 1}
                                ), false)
                        });
                    }
                }
            }
        }

        if (getAttackTarget() == null) {
            ticksWithoutTarget++;
        } else {
            ticksWithoutTarget = 0;
        }

        if (timeUntilDeath > 0) timeUntilDeath--;
        else if (timeUntilDeath == 0) {
            attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, null), getHealth());
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
            remove();
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
            if (!world.isClientSide) {
                ItemEntity itemEntity = entityDropItem(getDeactivatedMask(mask), 1.5f);
                if (itemEntity != null) {
                    ItemStack item = itemEntity.getItem();
                    item.setDamage((int) Math.ceil((1.0f - getHealthRatio()) * item.getMaxDamage()));
                    item.setDisplayName(this.getCustomName());
                }
            }
        }
    }

    protected ItemStack getDeactivatedMask(ItemBarakoaMask mask) {
        return new ItemStack(mask);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MMSounds.ENTITY_BARAKOA_DIE.get();
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(DANCING, false);
        getDataManager().register(MASK, 0);
        getDataManager().register(WEAPON, 0);
        getDataManager().register(ACTIVE, true);
        getDataManager().register(HEALPOSX, 0f);
        getDataManager().register(HEALPOSY, 0f);
        getDataManager().register(HEALPOSZ, 0f);
    }

    public boolean getDancing() {
        return getDataManager().get(DANCING);
    }

    public void setDancing(boolean dancing) {
        getDataManager().set(DANCING, dancing);
    }

    public MaskType getMask() {
        return MaskType.from(getDataManager().get(MASK));
    }

    public void setMask(MaskType type) {
        getDataManager().set(MASK, type.ordinal());
    }

    public int getWeapon() {
        return getDataManager().get(WEAPON);
    }

    public void setWeapon(int type) {
        getDataManager().set(WEAPON, type);
    }

    public boolean getActive() {
        return getDataManager().get(ACTIVE);
    }

    public void setActive(boolean active) {
        getDataManager().set(ACTIVE, active);
    }

    public Vec3 getHealPos() {
        return new Vec3(getDataManager().get(HEALPOSX), getDataManager().get(HEALPOSY), getDataManager().get(HEALPOSZ));
    }

    public void setHealPos(Vec3 vec) {
        getDataManager().set(HEALPOSX, (float) vec.x);
        getDataManager().set(HEALPOSY, (float) vec.y);
        getDataManager().set(HEALPOSZ, (float) vec.z);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("mask", getMask().ordinal());
        compound.putInt("weapon", getWeapon());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setMask(MaskType.from(compound.getInt("mask")));
        setWeapon(compound.getInt("weapon"));
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float p_82196_2_) {
        AbstractArrowEntity dart = new EntityDart(EntityHandler.DART, this.world, this);
        Vec3 targetPos = target.getPositionVec();
        double dx = targetPos.getX() - this.getPosX();
        double dy = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - dart.getPositionVec().getY();
        double dz = targetPos.getZ() - this.getPosZ();
        double dist = MathHelper.sqrt(dx * dx + dz * dz);
        dart.shoot(dx, dy + dist * 0.2D, dz, 1.6F, 1);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, this.getHeldItem(Hand.MAIN_HAND));
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, this.getHeldItem(Hand.MAIN_HAND));
        dart.setDamage((double) (p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.world.getDifficulty().getId() * 0.11F));

        if (i > 0) {
            dart.setDamage(dart.getDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            dart.setKnockbackStrength(j);
        }

        dart.setDamage(dart.getDamage() * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.attackMultiplier.get());

        this.world.addEntity(dart);
        attacking = false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (getAnimation() == DEACTIVATE_ANIMATION) {
            return false;
        }
        Entity entity = source.getTrueSource();
        boolean angleFlag = true;
        if (entity != null) {
            int arc = 220;
            Vec3 entityPos = entity.getPositionVec();
            float entityHitAngle = (float) ((Math.atan2(entityPos.getZ() - getPosZ(), entityPos.getX() - getPosX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = renderYawOffset % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            angleFlag = (entityRelativeAngle <= arc / 2.0 && entityRelativeAngle >= -arc / 2.0) || (entityRelativeAngle >= 360 - arc / 2.0 || entityRelativeAngle <= -arc + 90 / 2.0);
        }
        if (angleFlag && getMask().canBlock && entity instanceof LivingEntity && (getAnimation() == NO_ANIMATION || getAnimation() == HURT_ANIMATION || getAnimation() == BLOCK_ANIMATION) && !source.isUnblockable()) {
            blockingEntity = (LivingEntity) entity;
            playSound(SoundEvents.ITEM_SHIELD_BLOCK, 0.3F, 1.5F);
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BLOCK_ANIMATION);
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    @Override
    protected ResourceLocation getLootTable() {
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
    public boolean canBeCollidedWith() {
        return active;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultipler) {
        if (active) {
            return super.onLivingFall(distance, damageMultipler);
        }
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION, PROJECTILE_ATTACK_ANIMATION, BLOCK_ANIMATION, IDLE_ANIMATION, ACTIVATE_ANIMATION, DEACTIVATE_ANIMATION, TELEPORT_ANIMATION, HEAL_LOOP_ANIMATION, HEAL_START_ANIMATION, HEAL_STOP_ANIMATION};
    }

    public boolean isBarakoDevoted() {
        return true;
    }

    public int randomizeWeapon() {
        return rand.nextInt(3) == 0 ? 1 : 0;
    }

    public boolean canHeal(LivingEntity entity) {
        return false;
    }

    public void spawnHealParticles() {
        if (getAttackTarget() != null) {
            setHealPos(getAttackTarget().getPositionVec().add(new Vec3(0, getAttackTarget().getHeight() / 2f, 0)));
        }
        if (world.isClientSide && barakoPos != null) {
            barakoPos[0] = getHealPos();
            if (staffPos != null && staffPos[0] != null) {
                double dist = Math.max(barakoPos[0].distanceTo(staffPos[0]), 0.01);
                double radius = 0.5f;
                double yaw = rand.nextFloat() * 2 * Math.PI;
                double pitch = rand.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                if (ticksExisted % 5 == 0) AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), staffPos[0].getX(), staffPos[0].getY(), staffPos[0].getZ(), 0, 0, 0, false, 0, 0, 0, 0, 3.5F, 0.95, 0.9, 0.35, 0.75, 1, Math.min(2 * dist, 60), true, false, new ParticleComponent[]{
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
                if (ticksExisted % 5 == 0) AdvancedParticleBase.spawnParticle(world, ParticleHandler.RING2.get(), staffPos[0].getX(), staffPos[0].getY(), staffPos[0].getZ(), 0, 0, 0, true, 0, 0, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
                });
                int spawnFreq = 5;
                if (ticksExisted % spawnFreq == 0) ParticleRibbon.spawnRibbon(world, ParticleHandler.RIBBON_SQUIGGLE.get(), (int)(0.5 * dist), staffPos[0].getX(), staffPos[0].getY(), staffPos[0].getZ(), 0, 0, 0, true, 0, 0, 0, 0.5F, 0.95, 0.9, 0.35, 0.75, 1, spawnFreq, true, new ParticleComponent[]{
                        new RibbonComponent.BeamPinning(staffPos, barakoPos),
                        new RibbonComponent.PanTexture(0, 1)
                });
            }
        }
    }

    protected void sunBlockTarget() {

    }
}
