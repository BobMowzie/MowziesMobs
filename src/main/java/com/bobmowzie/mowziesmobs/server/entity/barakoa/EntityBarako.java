package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoTrade;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityBarako extends MowzieEntity implements LeaderSunstrikeImmune, GuiHandler.ContainerHolder, IMob {
    public static final Animation DIE_ANIMATION = Animation.create(130);
    public static final Animation HURT_ANIMATION = Animation.create(13);
    public static final Animation BELLY_ANIMATION = Animation.create(40);
    public static final Animation TALK_ANIMATION = Animation.create(80);
    public static final Animation SUNSTRIKE_ANIMATION = Animation.create(15);
    public static final Animation ATTACK_ANIMATION = Animation.create(30);
    public static final Animation SPAWN_ANIMATION = Animation.create(20);
    public static final Animation SOLAR_BEAM_ANIMATION = Animation.create(100);
    public static final Animation BLESS_ANIMATION = Animation.create(60);
    public static final Animation SUPERNOVA_ANIMATION = Animation.create(100);
    private static final int MAX_HEALTH = 140;
    private static final int SUNSTRIKE_PAUSE_MAX = 40;
    private static final int SUNSTRIKE_PAUSE_MIN = 15;
    private static final int LASER_PAUSE = 230;
    private static final int SUPERNOVA_PAUSE = 230;
    private static final int BARAKOA_PAUSE = 140;
    private static final DataParameter<Integer> DIRECTION = EntityDataManager.createKey(EntityBarako.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DIALOGUE = EntityDataManager.createKey(EntityBarako.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ANGRY = EntityDataManager.createKey(EntityBarako.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> DESIRES = EntityDataManager.createKey(EntityBarako.class, DataSerializers.ITEM_STACK);
    private final Set<UUID> tradedPlayers = new HashSet<>();
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    public ControlledAnimation angryEyebrow = new ControlledAnimation(5);
    private PlayerEntity customer;
    // TODO: Enum!
    public int whichDialogue = 0;
    public int barakoaSpawnCount = 0;
    // TODO: use EnumFacing!
    private int direction = 0;
    private boolean blocksByFeet = true;
    private int timeUntilSunstrike = 0;
    private int timeUntilLaser = 0;
    private int timeUntilBarakoa = 0;
    private int timeUntilSupernova = 0;
    private PlayerEntity blessingPlayer;
    private BarakoaHurtByTargetAI hurtByTargetAI;

    @SideOnly(Side.CLIENT)
    public Vec3d[] betweenHandPos;
    @SideOnly(Side.CLIENT)
    public Vec3d[] blessingPlayerPos;

    private static ParticleComponent.KeyTrack superNovaKeyTrack1 = new ParticleComponent.KeyTrack(
            new float[]{0, 20f, 20f, 0},
            new float[]{0, 0.5f, 0.9f, 1}
    );
    private static ParticleComponent.KeyTrack superNovaKeyTrack2 = ParticleComponent.KeyTrack.oscillate(0, 1, 30);

    public EntityBarako(World world) {
        super(world);
        hurtByTargetAI = new BarakoaHurtByTargetAI(this, true);
        this.targetTasks.addTask(3, hurtByTargetAI);
        this.targetTasks.addTask(4, new BarakoaAttackTargetAI(this, PlayerEntity.class, 0, false, true));
        this.targetTasks.addTask(4, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 0, true, false, null));
        this.targetTasks.addTask(4, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, 0, true, false, null));
        this.tasks.addTask(2, new SimpleAnimationAI<>(this, BELLY_ANIMATION, false));
        this.tasks.addTask(2, new SimpleAnimationAI<EntityBarako>(this, TALK_ANIMATION, false) {
            @Override
            public void startExecuting() {
                super.startExecuting();
//                whichDialogue = getWhichDialogue();
            }
        });
        this.tasks.addTask(2, new SimpleAnimationAI<EntityBarako>(this, BLESS_ANIMATION, false) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                blessingPlayer = getCustomer();
            }
        });
        this.tasks.addTask(2, new SimpleAnimationAI<EntityBarako>(this, SUPERNOVA_ANIMATION, false) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_SUPERNOVA_START, 3f, 1f);
            }

            @Override
            public void updateTask() {
                super.updateTask();
                if (entity.getAnimationTick() == 30) {
                    playSound(MMSounds.ENTITY_SUPERNOVA_BLACKHOLE, 2f, 1.2f);
                }
                if (entity.getAnimationTick() == 40) {
                    playSound(MMSounds.ENTITY_BARAKO_SCREAM, 1.5f, 1f);
                }

                if (!entity.world.isRemote) {
                    if (entity.getAnimationTick() == 44) {
                        Vec3d offset = new Vec3d(1.1f, 0, 0);
                        offset = offset.rotateYaw((float) Math.toRadians(-entity.rotationYaw - 90));
                        EntitySuperNova superNova = new EntitySuperNova(entity.world, entity, entity.posX + offset.x, entity.posY + 0.05, entity.posZ + offset.z);
                        world.spawnEntity(superNova);
                    }
                }
            }
        });
        this.tasks.addTask(2, new AnimationSunStrike<EntityBarako>(this, SUNSTRIKE_ANIMATION) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                if (entityTarget != null) {
                    prevX = entityTarget.posX;
                    prevZ = entityTarget.posZ;
                }
            }
        });
        this.tasks.addTask(2, new AnimationRadiusAttack<EntityBarako>(this, ATTACK_ANIMATION, 4f, (int)(5 * ConfigHandler.MOBS.BARAKO.combatData.attackMultiplier), 3f, 12, true){
            @Override
            public void startExecuting() {
                super.startExecuting();
                playSound(MMSounds.ENTITY_BARAKO_BURST, 1.7f, 1.5f);
            }
        });
        this.tasks.addTask(2, new AnimationSpawnBarakoa(this, SPAWN_ANIMATION));
        this.tasks.addTask(2, new AnimationSolarBeam<>(this, SOLAR_BEAM_ANIMATION));
        this.tasks.addTask(3, new AnimationTakeDamage<>(this));
        this.tasks.addTask(1, new AnimationDieAI<>(this));
        this.tasks.addTask(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(7, new LookAtGoal(this, EntityBarakoa.class, 8.0F));
        this.tasks.addTask(8, new LookRandomlyGoal(this));
        this.setSize(1.5f, 2.4f);
        if (getDirection() == 0) {
            this.setDirection(rand.nextInt(4) + 1);
        }
        experienceValue = 45;

        if (world.isRemote) {
            betweenHandPos = new Vec3d[]{new Vec3d(0, 0, 0)};
            blessingPlayerPos = new Vec3d[]{new Vec3d(0, 0, 0)};
        }
    }

    public EntityBarako(World world, int direction) {
        this(world);
        this.setDirection(direction);
    }

    @Override
    public float getEyeHeight() {
        return 1.4f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH * ConfigHandler.MOBS.BARAKO.combatData.healthMultiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() == NO_ANIMATION) {
            if (getAttackTarget() == null && !isAIDisabled()) {
                int soundType = MathHelper.getInt(this.rand, 0, 9);
                if (soundType < MMSounds.ENTITY_BARAKO_TALK.size()) {
                    this.playSound(MMSounds.ENTITY_BARAKO_TALK.get(soundType).get(), 2F, 1.0F);
                    this.setWhichDialogue(soundType + 1);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, TALK_ANIMATION);
                }
            } else {
                int soundType = MathHelper.getInt(rand, 1, 10);
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
        return MMSounds.ENTITY_BARAKO_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_BARAKO_DIE, 2f, 1);
        return null;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1) {
            direction = getDirection();
        }
        if (!(getAnimation() == ATTACK_ANIMATION && getAnimationTick() >= 12 && getAnimationTick() <= 14)) this.repelEntities(2.2f, 2.5f, 2.2f, 2.2f);
        this.rotationYaw = (direction - 1) * 90;
        this.renderYawOffset = rotationYaw;
//        this.posX = prevPosX;
//        this.posZ = prevPosZ;
        motionX = 0;
        motionZ = 0;

        if (getAttackTarget() != null) {
            LivingEntity target = getAttackTarget();
            this.setAngry(true);
            float entityHitAngle = (float) ((Math.atan2(target.posZ - posZ, target.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = rotationYaw % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = Math.abs(entityHitAngle - entityAttackingAngle);
            Vec3d targetMoveVec = new Vec3d(target.motionX, target.motionY, target.motionZ);
            Vec3d betweenEntitiesVec = getPositionVector().subtract(target.getPositionVector());
            boolean targetComingCloser = targetMoveVec.dotProduct(betweenEntitiesVec) > 0;
            if (getAnimation() == NO_ANIMATION && !isAIDisabled() && rand.nextInt(80) == 0 && getEntitiesNearby(EntityBarakoa.class, 25).size() < 5 && targetDistance > 4.5 && timeUntilBarakoa <= 0) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SPAWN_ANIMATION);
                timeUntilBarakoa = BARAKOA_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && !isAIDisabled() && getHealthRatio() <= 0.6 && timeUntilLaser <= 0 && (entityRelativeAngle < 60 || entityRelativeAngle > 300) && getEntitySenses().canSee(target)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SOLAR_BEAM_ANIMATION);
                timeUntilLaser = LASER_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && !isAIDisabled() && getHealthRatio() <= 0.6 && timeUntilSupernova <= 0 && targetDistance <= 10.5) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SUPERNOVA_ANIMATION);
                timeUntilSupernova = SUPERNOVA_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && !isAIDisabled() && (targetDistance <= 6f && targetComingCloser || targetDistance < 4.f)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
            } else if (getAnimation() == NO_ANIMATION && !isAIDisabled() && timeUntilSunstrike <= 0) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SUNSTRIKE_ANIMATION);
                timeUntilSunstrike = getTimeUntilSunstrike();
            }
            if (!hurtByTargetAI.shouldContinueExecuting()) {
                hurtByTargetAI.resetTask();
            }
        } else {
            if (!world.isRemote) {
                this.setAngry(false);
            }
        }

        if (ticksExisted % 20 == 0) {
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

        if (getAnimation() == NO_ANIMATION && !isAIDisabled() && getAttackTarget() == null && rand.nextInt(200) == 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BELLY_ANIMATION);
        }

        if (getAnimation() == BELLY_ANIMATION && (getAnimationTick() == 9 || getAnimationTick() == 29)) {
            this.playSound(MMSounds.ENTITY_BARAKO_BELLY, 3f, 1f);
        }

//        if (getAnimation() == TALK_ANIMATION && getAnimationTick() == 1) {
//            whichDialogue = getWhichDialogue();
//        }

        if (getAnimation() == ATTACK_ANIMATION) {
            rotationYawHead = rotationYaw;
//            if (getAnimationTick() == 1) {
//                this.playSound(MMSounds.ENTITY_BARAKO_BURST, 1.7f, 1.5f);
//            }
            if (getAnimationTick() == 10) {
                if (world.isRemote) {
                    spawnExplosionParticles(30);
                }
                this.playSound(MMSounds.ENTITY_BARAKO_ATTACK, 1.7f, 0.9f);
            }
            if (getAnimationTick() <= 6 && world.isRemote) {
                int particleCount = 8;
                while (--particleCount != 0) {
                    double radius = 2f;
                    double yaw = rand.nextFloat() * 2 * Math.PI;
                    double pitch = rand.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    double offsetX = -0.3 * Math.sin(rotationYaw * Math.PI / 180);
                    double offsetZ = -0.3 * Math.cos(rotationYaw * Math.PI / 180);
                    double offsetY = 1;
                    MMParticle.ORB.spawn(world, posX + ox + offsetX, posY + offsetY + oy, posZ + oz + offsetZ, ParticleArgs.get().withData(posX + offsetX, posY + offsetY, posZ + offsetZ, 6));
                }
            }
        }

        if (getAnimation() == BLESS_ANIMATION) {
            rotationYawHead = rotationYaw;

            if (getAnimationTick() == 1) {
                blessingPlayer = getCustomer();
            }
            if (world.isRemote && blessingPlayer != null) {
                blessingPlayerPos[0] = blessingPlayer.getPositionVector().add(new Vec3d(0, blessingPlayer.height / 2f, 0));
                if (getAnimationTick() > 5 && getAnimationTick() < 40) {
                    int particleCount = 2;
                    while (--particleCount != 0) {
                        double radius = 0.7f;
                        double yaw = rand.nextFloat() * 2 * Math.PI;
                        double pitch = rand.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        MowzieParticleBase.spawnParticle(world, MMParticle.ORB2, posX + ox, posY + 0.8f + oy, posZ + oz, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 20, true, new ParticleComponent[]{
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
            }
            if (getAnimationTick() % 15 == 0) {
                EntityRing ring = new EntityRing(world, (float)posX, (float)posY + 0.8f, (float)posZ, new Vec3d(0, 0, 0), 15, 1, 223/255f, 66/255f, 1, 3.5f, true);
                world.spawnEntity(ring);
            }
        }

        if (getAnimation() == SUPERNOVA_ANIMATION) {
            if (world.isRemote && betweenHandPos.length > 0) {
                superNovaEffects();
            }
            if (getAnimationTick() < 30) {
                List<LivingEntity> entities = getEntityLivingBaseNearby(16, 16, 16, 16);
                for (LivingEntity inRange : entities) {
                    if (inRange instanceof LeaderSunstrikeImmune) continue;
                    if (inRange instanceof PlayerEntity && ((PlayerEntity)inRange).capabilities.disableDamage) continue;
                    Vec3d diff = inRange.getPositionVector().subtract(getPositionVector().add(0, 3, 0));
                    diff = diff.normalize().scale(0.03);
                    inRange.motionX += -diff.x;
                    inRange.motionZ += -diff.z;
                    inRange.motionY += -diff.y;

                    if (inRange.posY < posY + 3) inRange.motionY += 0.075;
                }
            }
        }

        if (ticksExisted % 40 == 0) {
            for (PlayerEntity player : getPlayersNearby(15, 15, 15, 15)) {
                ItemStack headArmorStack = player.inventory.armorInventory.get(3);
                if (getAttackTarget() != player && TargetGoal.isSuitableTarget(this, player, false, false) && headArmorStack.getItem() instanceof BarakoaMask) {
                    if (player instanceof ServerPlayerEntity) AdvancementHandler.SNEAK_VILLAGE_TRIGGER.trigger((ServerPlayerEntity) player);
                }
            }
        }

        if (!world.isRemote && getAttackTarget() == null && getAnimation() != SOLAR_BEAM_ANIMATION && getAnimation() != SUPERNOVA_ANIMATION) {
            if (ConfigHandler.MOBS.BARAKO.healsOutOfBattle) heal(0.3f);
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
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, SUPERNOVA_ANIMATION);
//        }
    }

    private void superNovaEffects() {
        if (getAnimationTick() == 1) {
            superNovaKeyTrack1 = new ParticleComponent.KeyTrack(
                    new float[]{0, 25f, 32f, 0},
                    new float[]{0, 0.6f, 0.85f, 1}
            );
            superNovaKeyTrack2 = ParticleComponent.KeyTrack.oscillate(0, 7, 24);
            MowzieParticleBase.spawnParticle(world, MMParticle.SUN, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 33, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack1, false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack2, true)
            });
        }
        if (getAnimationTick() == 33) {
            MowzieParticleBase.spawnParticle(world, MMParticle.SUN_NOVA, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 20F, 1, 1, 1, 0, 1, 13, true, new ParticleComponent[]{
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
            MowzieParticleBase.spawnParticle(world, MMParticle.FLARE,  0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1,1,1, 0.7, 1, 3, true, new ParticleComponent[]{
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
                float phaseOffset = rand.nextFloat();
                double value = rand.nextDouble() * 0.3 + 0.05;
                MowzieParticleBase.spawnParticle(world, MMParticle.PIXEL, betweenHandPos[0].x, betweenHandPos[0].y, betweenHandPos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 6, false, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 3f},
                                new float[]{0, 0.2f}
                        ), false),
                        new ParticleComponent.Orbit(betweenHandPos, ParticleComponent.KeyTrack.startAndEnd(0 + phaseOffset, -0.4f + phaseOffset), ParticleComponent.KeyTrack.startAndEnd(0.5f + rand.nextFloat(), 0), ParticleComponent.constant(0), ParticleComponent.constant(0), ParticleComponent.constant(0), true),
                });
            }
        }
        if (getAnimationTick() > 1 && getAnimationTick() < 27) {
            for (int i = 0; i < 6; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 5, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(betweenHandPos[0]);
                double value = rand.nextDouble() * 0.5 + 0.1;
                MowzieParticleBase.spawnParticle(world, MMParticle.PIXEL, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 7, false, new ParticleComponent[]{
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
            MowzieParticleBase.spawnParticle(world, MMParticle.RING_SPARKS, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, rand.nextFloat() * (float)Math.PI * 2, 5F, 1, 1, 1, 1, 1, 6 + rand.nextFloat() * 3, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f + 20f * timeFrac * timeFrac + 10f * rand.nextFloat() * timeFrac, 0f), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false)
            });
        }
        if (getAnimationTick() == 14) {
            MowzieParticleBase.spawnParticle(world, MMParticle.FLARE, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 18, true, new ParticleComponent[]{
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
            MowzieParticleBase.spawnParticle(world, MMParticle.BURST_IN, 0, 0, 0, 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 0, 0, 0, 1, 1, 10, true, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(betweenHandPos),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(25f, 0f), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 1f), false),
                    new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-2, 2, 42, 0), true),
            });
        }

        if (getAnimationTick() == 44) {
            float scale = 85f;
            MowzieParticleBase.spawnParticle(world, MMParticle.RING_BIG, betweenHandPos[0].x, betweenHandPos[0].y, betweenHandPos[0].z, 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 1,1,1, 1, 1, 40, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                            new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
            });
            scale = 120f;
            MowzieParticleBase.spawnParticle(world, MMParticle.GLOW, betweenHandPos[0].x, betweenHandPos[0].y, betweenHandPos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.95, 0.9,0.35, 1, 1, 40, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                            new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                            new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
            });
        }
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
        if (direction == 1) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
        } else if (direction == 2) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
        } else if (direction == 3) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
        } else if (direction == 4) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
        } else {
            return false;
        }
//        System.out.println(direction + ", " + (MathHelper.floor(posX) - 1) + ", " + Math.round((float) (posY - 1)) + ", " + MathHelper.floor(posZ) + 1);
        return !(blockLeft.getBlock() instanceof AirBlock && blockRight.getBlock() instanceof AirBlock);
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.25F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = rand.nextFloat() * 0.1F - 0.05f;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
            world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 1, posZ, vx, vy, vz);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(DIRECTION, 0);
        getDataManager().register(DIALOGUE, 0);
        getDataManager().register(ANGRY, false);
        getDataManager().register(DESIRES, new ItemStack(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 7));
    }

    public int getDirection() {
        return getDataManager().get(DIRECTION);
    }

    public void setDirection(int direction) {
        getDataManager().set(DIRECTION, direction);
    }

    public int getWhichDialogue() {
        return getDataManager().get(DIALOGUE);
    }

    public void setWhichDialogue(int dialogue) {
        getDataManager().set(DIALOGUE, dialogue);
    }

    public boolean getAngry() {
        return getDataManager().get(ANGRY);
    }

    public void setAngry(boolean angry) {
        getDataManager().set(ANGRY, angry);
    }

    public void setDesires(ItemStack stack) {
    	getDataManager().set(DESIRES, stack);
    }

    public ItemStack getDesires() {
    	return getDataManager().get(DESIRES);
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

    public boolean hasTradedWith(PlayerEntity player) {
        return tradedPlayers.contains(PlayerEntity.getUUID(player.getGameProfile()));
    }

    public void rememberTrade(PlayerEntity player) {
        tradedPlayers.add(PlayerEntity.getUUID(player.getGameProfile()));
    }

    @Override
    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("direction", getDirection());
        ListNBT players = new ListNBT();
        for (UUID uuid : tradedPlayers) {
            players.appendTag(NBTUtil.createUUIDTag(uuid));
        }
        compound.setTag("players", players);
    }

    @Override
    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        setDirection(compound.getInteger("direction"));
        tradedPlayers.clear();
        ListNBT players = compound.getTagList("players", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < players.tagCount(); i++) {
            tradedPlayers.add(NBTUtil.getUUIDFromTag(players.getCompoundTagAt(i)));
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {}

    private int getTimeUntilSunstrike() {
        float damageRatio = 1 - getHealthRatio();
        if (damageRatio > 0.6) {
            damageRatio = 0.6f;
        }
        return (int) (SUNSTRIKE_PAUSE_MAX - (damageRatio / 0.6f) * (SUNSTRIKE_PAUSE_MAX - SUNSTRIKE_PAUSE_MIN));
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, BELLY_ANIMATION, TALK_ANIMATION, SUNSTRIKE_ANIMATION, ATTACK_ANIMATION, SPAWN_ANIMATION, SOLAR_BEAM_ANIMATION, BLESS_ANIMATION, SUPERNOVA_ANIMATION};
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        List<EntityBarakoa> barakoa = getEntitiesNearby(EntityBarakoa.class, 20, 10, 20, 20);
        for (EntityBarakoa entityBarakoa : barakoa) {
            if (entityBarakoa.isBarakoDevoted()) entityBarakoa.timeUntilDeath = rand.nextInt(20);
        }

        super.onDeath(cause);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.BARAKO;
    }

    public boolean isTrading() {
        return customer != null;
    }

    public PlayerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(PlayerEntity customer) {
        this.customer = customer;
    }

    @Override
    public Container createContainer(World world, PlayerEntity player, int x, int y, int z) {
        return new ContainerBarakoTrade(this, player.inventory, world);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ContainerScreen createGui(World world, PlayerEntity player, int x, int y, int z) {
        return new GuiBarakoTrade(this, player.inventory, world, y != 0);
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (canTradeWith(player) && getAttackTarget() == null && !isDead) {
            setCustomer(player);
            if (!world.isRemote) {
                GuiHandler.open(GuiHandler.BARAKO_TRADE, player, this, hasTradedWith(player) ? 1 : 0);
            }
            return true;
        }
        return false;
    }

    public boolean canTradeWith(PlayerEntity player) {
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
        return ConfigHandler.MOBS.BARAKO.hasBossBar;
    }

    @Override
    protected BossInfo.Color bossBarColor() {
        return BossInfo.Color.YELLOW;
    }
}
