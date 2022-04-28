package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBabyFoliaathEatAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EntityBabyFoliaath extends MowzieEntity {
    private static final EntityDataAccessor<Integer> GROWTH = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> INFANT = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HUNGRY = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<ItemStack> EATING = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.ITEM_STACK);

    public static final Animation EAT_ANIMATION = Animation.create(20);
    public ControlledAnimation activate = new ControlledAnimation(5);
    private double prevActivate;

    public EntityBabyFoliaath(EntityType<? extends EntityBabyFoliaath> type, Level world) {
        super(type, world);
        setInfant(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AnimationBabyFoliaathEatAI<EntityBabyFoliaath>(this, EAT_ANIMATION));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    protected boolean isMovementNoisy() {
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
    public void tick() {
        super.tick();
        setDeltaMovement(0, getDeltaMovement().y, 0);
        yBodyRot = 0;

        if (arePlayersCarryingMeat(getPlayersNearby(3, 3, 3, 3)) && getAnimation() == NO_ANIMATION && getHungry()) {
            activate.increaseTimer();
        } else {
            activate.decreaseTimer();
        }

        if (activate.getTimer() == 1 && prevActivate - activate.getTimer() < 0) {
            playSound(MMSounds.ENTITY_FOLIAATH_GRUNT.get(), 0.5F, 1.5F);
        }
        prevActivate = activate.getTimer();

        if (!level.isClientSide && getHungry() && getAnimation() == NO_ANIMATION) {
            for (ItemEntity meat : getMeatsNearby(0.4, 0.2, 0.4, 0.4)) {
                ItemStack stack = meat.getItem().split(1);
                if (!stack.isEmpty()) {
                    setEating(stack);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, EAT_ANIMATION);
                    playSound(MMSounds.ENTITY_FOLIAATH_BABY_EAT.get(), 0.5F, 1.2F);
                    incrementGrowth();
                    setHungry(false);
                    break;
                }
            }
        }
        if (level.isClientSide && getAnimation() == EAT_ANIMATION && (getAnimationTick() == 3 || getAnimationTick() == 7 || getAnimationTick() == 11 || getAnimationTick() == 15 || getAnimationTick() == 19)) {
            for (int i = 0; i <= 5; i++) {
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, getEating()), getX(), getY() + 0.2, getZ(), random.nextFloat() * 0.2 - 0.1, random.nextFloat() * 0.2, random.nextFloat() * 0.2 - 0.1);
            }
        }

        //Growing
        if (!level.isClientSide) {
            if (tickCount % 20 == 0 && !getHungry()) {
                incrementGrowth();
            }
            // TODO: cleanup this poor logic
            setInfant(getGrowth() < 600);
            if (getInfant()) {
                setHungry(false);
            }
            if (getGrowth() == 600) {
                setHungry(true);
            }
            if (getGrowth() == 1200) {
                setHungry(true);
            }
            if (getGrowth() == 1800) {
                setHungry(true);
            }
            if (getGrowth() == 2400) {
                EntityFoliaath adultFoliaath = new EntityFoliaath(EntityHandler.FOLIAATH.get(), level);
                adultFoliaath.setPos(getX(), getY(), getZ());
                adultFoliaath.setCanDespawn(false);
                level.addFreshEntity(adultFoliaath);
                discard() ;
            }
        }
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    private boolean arePlayersCarryingMeat(List<Player> players) {
        if (players.size() > 0) {
            for (Player player : players) {
                FoodProperties food = player.getMainHandItem().getItem().getFoodProperties();
                if (food != null && food.isMeat()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        for (int i = 0; i < 10; i++) {
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.JUNGLE_LEAVES.defaultBlockState()), getX(), getY() + 0.2, getZ(), 0, 0, 0);
        }
        discard() ;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity collider) {
        setDeltaMovement(0, getDeltaMovement().y, 0);
    }

    @Override
    protected SoundEvent getDeathSound() {
        playSound(SoundEvents.GRASS_BREAK, 1, 0.8F);
        return null;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, MobSpawnType reason) {
        if (world.isUnobstructed(this) && world.noCollision(this) && !world.containsAnyLiquid(getBoundingBox())) {
            BlockPos ground = new BlockPos(
                    Mth.floor(getX()),
                    Mth.floor(getBoundingBox().minY) - 1,
                    Mth.floor(getZ())
            );

            BlockState block = world.getBlockState(ground);

            if (block.getBlock() == Blocks.GRASS_BLOCK || block.getMaterial() == Material.DIRT || block.getMaterial() == Material.LEAVES) {
                playSound(SoundEvents.GRASS_HIT, 1, 0.8F);
                return true;
            }
        }
        return false;
    }

    public List<ItemEntity> getMeatsNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = level.getEntities(this, getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        ArrayList<ItemEntity> listEntityItem = new ArrayList<>();
        for (Entity entityNeighbor : list) {
            if (entityNeighbor instanceof ItemEntity && distanceTo(entityNeighbor) <= radius) {
                FoodProperties food = ((ItemEntity) entityNeighbor).getItem().getItem().getFoodProperties();
                if (food != null && food.isMeat()) {
                    listEntityItem.add((ItemEntity) entityNeighbor);
                }
            }
        }
        return listEntityItem;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("tickGrowth", getGrowth());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setGrowth(compound.getInt("tickGrowth"));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(GROWTH, 0);
        getEntityData().define(INFANT, false);
        getEntityData().define(HUNGRY, false);
        getEntityData().define(EATING, ItemStack.EMPTY);
    }

    public int getGrowth() {
        return getEntityData().get(GROWTH);
    }

    public void setGrowth(int growth) {
        getEntityData().set(GROWTH, growth);
    }

    public void incrementGrowth() {
        setGrowth(getGrowth() + 1);
    }

    public boolean getInfant() {
        return getEntityData().get(INFANT);
    }

    public void setInfant(boolean infant) {
        getEntityData().set(INFANT, infant);
    }

    public boolean getHungry() {
        return getEntityData().get(HUNGRY);
    }

    public void setHungry(boolean hungry) {
        getEntityData().set(HUNGRY, hungry);
    }

    public void setEating(ItemStack stack) {
        getEntityData().set(EATING, stack);
    }

    public ItemStack getEating() {
        return getEntityData().get(EATING);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{EAT_ANIMATION};
    }
}