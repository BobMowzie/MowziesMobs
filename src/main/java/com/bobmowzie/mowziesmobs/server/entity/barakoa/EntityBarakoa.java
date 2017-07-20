package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationActivateAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBlockAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationProjectileAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.ai.animation.EntityAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.SmartBodyHelper;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class EntityBarakoa extends MowzieEntity implements IRangedAttackMob, LeaderSunstrikeImmune {
    public static final Animation DIE_ANIMATION = Animation.create(70);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation ATTACK_ANIMATION = Animation.create(19);
    public static final Animation PROJECTILE_ATTACK_ANIMATION = Animation.create(20);
    public static final Animation IDLE_ANIMATION = Animation.create(35);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(25);
    public static final Animation BLOCK_ANIMATION = Animation.create(10);
    private static final DataParameter<Boolean> DANCING = EntityDataManager.createKey(EntityBarakoa.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> MASK = EntityDataManager.createKey(EntityBarakoa.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WEAPON = EntityDataManager.createKey(EntityBarakoa.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityBarakoa.class, DataSerializers.BOOLEAN);
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

    public EntityBarakoa(World world) {
        super(world);
        setPathPriority(PathNodeType.WATER, 0);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(0, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntitySunstrike.class, EntitySunstrike::isStriking, 3, 0.7F));
        tasks.addTask(2, new AnimationBlockAI<>(this, BLOCK_ANIMATION));
        tasks.addTask(2, new AnimationAttackAI<>(this, ATTACK_ANIMATION, MMSounds.ENTITY_BARAKOA_SWING, null, 1, 3, 1, 9));
        tasks.addTask(2, new AnimationProjectileAttackAI<>(this, PROJECTILE_ATTACK_ANIMATION, 9, MMSounds.ENTITY_BARAKOA_BLOWDART));
        tasks.addTask(3, new AnimationTakeDamage<>(this));
        tasks.addTask(4, new AnimationAI<>(this, IDLE_ANIMATION));
        tasks.addTask(4, new EntityAIAttackMelee(this, 0.5D, false));
        tasks.addTask(5, new EntityAIWander(this, 0.4));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        setMask(MaskType.from(MathHelper.getInt(rand, 1, 4)));
        stepHeight = 1;
        circleTick += rand.nextInt(200);
        frame += rand.nextInt(50);
        experienceValue = 8;
        active = true;
    }

    @Override
    protected SmartBodyHelper createBodyHelper() {
        return new SmartBodyHelper(this);
    }

    @Override
    public int getAttack() {
        return getMask() == MaskType.FURY ? 6 : 4;
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
        if (!active || danceTimer != 0 || (getEntitiesNearby(EntityBarakoa.class, 8, 3, 8, 8).isEmpty() && getEntitiesNearby(EntityBarako.class, 8, 3, 8, 8).isEmpty() && getEntitiesNearby(EntityPlayer.class, 8, 3, 8, 8).isEmpty())) {
            return null;
        }
        if (getAttackTarget() == null) {
            int i = MathHelper.getInt(rand, 0, 11);
            if (i < MMSounds.ENTITY_BARAKOA_TALK.length) {
                playSound(MMSounds.ENTITY_BARAKOA_TALK[i], 1, 1.5f);
                AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
            }
        } else {
            int i = MathHelper.getInt(rand, 0, 7);
            if (i < MMSounds.ENTITY_BARAKOA_ANGRY.length) {
                playSound(MMSounds.ENTITY_BARAKOA_ANGRY[i], 1, 1.6f);
            }
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return active ? MMSounds.ENTITY_BARAKOA_HURT : null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(17  * MowziesMobs.CONFIG.difficultyScaleBarakoa);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return super.attackEntityAsMob(entity);
    }

    protected void updateAttackAI() {
        if (timeSinceAttack < 80) {
            timeSinceAttack++;
        }
        if (getAttackTarget() != null) {
            if (targetDistance > 7) {
                getNavigator().tryMoveToXYZ(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 0.6);
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
                if (targetDistance <= 3 && getWeapon() == 0) {
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
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        if (canHoldVaryingWeapons()) {
            setWeapon(rand.nextInt(3) == 0 ? 1 : 0);
        }
        return super.onInitialSpawn(difficulty, data);
    }

    protected boolean canHoldVaryingWeapons() {
        return true;
    }

    protected void updateCircling() {
        if (rand.nextInt(200) == 0) {
            circleDirection = !circleDirection;
        }
        if (circleDirection) {
            circleTick++;
        } else {
            circleTick--;
        }
        if (!attacking && targetDistance < 5) {
            circleEntity(getAttackTarget(), 7, 0.3f, true, circleTick, 0, 1.75f);
        } else {
            circleEntity(getAttackTarget(), 7, 0.3f, true, circleTick, 0, 1);
        }
        attacking = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote && active && !getActive()) {
            setActive(true);
        }
        active = getActive();
        if (!active) {
            getNavigator().clearPathEntity();
            rotationYaw = prevRotationYaw;
            renderYawOffset = rotationYaw;
            if (onGround && getAnimation() == NO_ANIMATION) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                playSound(MMSounds.ENTITY_BARAKOA_EMERGE, 1, 1);
            }
            return;
        }
        updateAttackAI();
        if (getAnimation() != NO_ANIMATION) {
            getNavigator().clearPathEntity();
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
        if (!world.isRemote && getAnimation() == NO_ANIMATION && danceTimer == 0 && rand.nextInt(800) == 0) {
            setDancing(true);
            playSound(MMSounds.ENTITY_BARAKOA_BATTLECRY_2, 1.2f, 1.3f);
        }
        if (getAnimation() != NO_ANIMATION) {
            danceTimer = 0;
        }

        if (cryDelay > -1) {
            cryDelay--;
        }
        if (cryDelay == 0) {
            playSound(MMSounds.ENTITY_BARAKOA_BATTLECRY, 1.5f, 1.5f);
        }
        if (getAttackTarget() != null && ticksWithoutTarget > 3) {
            cryDelay = MathHelper.getInt(rand, -15, 30);
        }

        if (getAnimation() == ATTACK_ANIMATION && getAnimationTick() == 5) {
            playSound(MMSounds.ENTITY_BARAKOA_SHOUT, 1, 1.1f);
        }
        if (getAnimation() == PROJECTILE_ATTACK_ANIMATION && getAnimationTick() == 1) {
            playSound(MMSounds.ENTITY_BARAKOA_INHALE, 0.7f, 1.2f);
        }

        if (getAttackTarget() == null) {
            ticksWithoutTarget++;
        } else {
            ticksWithoutTarget = 0;
        }

        if (timeUntilDeath > 0) timeUntilDeath--;
        else if (timeUntilDeath == 0) {
            attackEntityFrom(DamageSource.causeMobDamage(this), getHealth());
        }

//        if (ticksExisted > 50) setDead();
//        if (getAnimation() == NO_ANIMATION) AnimationAPI.sendAnimPacket(this, 4);
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (animation == ACTIVATE_ANIMATION) {
            setActive(true);
            active = true;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MMSounds.ENTITY_BARAKOA_DIE;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(DANCING, false);
        getDataManager().register(MASK, 0);
        getDataManager().register(WEAPON, 0);
        getDataManager().register(ACTIVE, true);
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
        setSize(type.entityWidth, type.entityHeight);
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

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("mask", getMask().ordinal());
        compound.setInteger("weapon", getWeapon());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMask(MaskType.from(compound.getInteger("mask")));
        setWeapon(compound.getInteger("weapon"));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
        EntityArrow dart = new EntityDart(this.world, this);
        double dx = target.posX - this.posX;
        double dy = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - dart.posY;
        double dz = target.posZ - this.posZ;
        double dist = (double)MathHelper.sqrt(dx * dx + dz * dz);
        dart.setThrowableHeading(dx, dy + dist * 0.2D, dz, 1.6F, 1);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, this.getHeldItem(EnumHand.MAIN_HAND));
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, this.getHeldItem(EnumHand.MAIN_HAND));
        dart.setDamage((double) (p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.world.getDifficulty().getDifficultyId() * 0.11F));

        if (i > 0) {
            dart.setDamage(dart.getDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            dart.setKnockbackStrength(j);
        }

        this.world.spawnEntity(dart);
        attacking = false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        boolean angleFlag = true;
        if (source.getEntity() != null) {
            int arc = 220;
            Entity entitySource = source.getEntity();
            float entityHitAngle = (float) ((Math.atan2(entitySource.posZ - posZ, entitySource.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = renderYawOffset % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            angleFlag = (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -arc + 90 / 2);
        }
        if (angleFlag && getMask().canBlock && entity instanceof EntityLivingBase && (getAnimation() == NO_ANIMATION || getAnimation() == HURT_ANIMATION || getAnimation() == BLOCK_ANIMATION)) {
            blockingEntity = (EntityLivingBase) entity;
            playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 0.3F, 1.5F);
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BLOCK_ANIMATION);
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    @Override
    protected void dropLoot() {
        super.dropLoot();
        if (rand.nextInt(3) == 0){
            dropItem(ItemHandler.INSTANCE.barakoaMasks.get(getMask()), 1);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return active;
    }

    @Override
    public void fall(float distance, float damageMultipler) {
        if (active) {
            super.fall(distance, damageMultipler);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION, PROJECTILE_ATTACK_ANIMATION, BLOCK_ANIMATION, IDLE_ANIMATION, ACTIVATE_ANIMATION};
    }

    public boolean isBarakoDevoted() {
        return true;
    }

    public int randomizeWeapon() {
        return rand.nextInt(3) == 0 ? 1 : 0;
    }
}
