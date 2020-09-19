package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBabyFoliaathEatAI;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.Sets;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EntityBabyFoliaath extends MowzieEntity {
    private static final int JUNGLE_LEAVES = Block.getStateId(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));

    private static final DataParameter<Integer> GROWTH = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.VARINT);

    private static final DataParameter<Boolean> INFANT = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> HUNGRY = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.BOOLEAN);

    private static final DataParameter<ItemStack> EATING = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.ITEM_STACK);

    public static final Animation EAT_ANIMATION = Animation.create(20);
    public ControlledAnimation activate = new ControlledAnimation(5);
    private double prevActivate;

    private static Set<Item> meatTypes;

    public EntityBabyFoliaath(EntityType<? extends EntityBabyFoliaath> type, World world) {
        super(type, world);
        getMeatList();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AnimationBabyFoliaathEatAI<EntityBabyFoliaath>(this, EAT_ANIMATION));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        motionX = 0;
        motionZ = 0;
        renderYawOffset = 0;

        if (arePlayersCarryingMeat(getPlayersNearby(3, 3, 3, 3)) && getAnimation() == NO_ANIMATION && getHungry()) {
            activate.increaseTimer();
        } else {
            activate.decreaseTimer();
        }

        if (activate.getTimer() == 1 && prevActivate - activate.getTimer() < 0) {
            playSound(MMSounds.ENTITY_FOLIAATH_GRUNT, 0.5F, 1.5F);
        }
        prevActivate = activate.getTimer();

        if (!world.isRemote && getHungry() && getAnimation() == NO_ANIMATION) {
            for (ItemEntity meat : getMeatsNearby(0.4, 0.2, 0.4, 0.4)) {
                ItemStack stack = meat.getItem().splitStack(1);
                if (!stack.isEmpty()) {
                    setEating(stack);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, EAT_ANIMATION);
                    playSound(MMSounds.ENTITY_FOLIAATH_BABY_EAT, 0.5F, 1.2F);
                    incrementGrowth();
                    setHungry(false);
                    break;
                }
            }
        }
        if (world.isRemote && getAnimation() == EAT_ANIMATION && (getAnimationTick() == 3 || getAnimationTick() == 7 || getAnimationTick() == 11 || getAnimationTick() == 15 || getAnimationTick() == 19)) {
            for (int i = 0; i <= 5; i++) {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY + 0.2, posZ, rand.nextFloat() * 0.2 - 0.1, rand.nextFloat() * 0.2, rand.nextFloat() * 0.2 - 0.1, Item.getIdFromItem(getEating().getItem()));
            }
        }

        //Growing
        if (!world.isRemote) {
            if (ticksExisted % 20 == 0 && !getHungry()) {
                incrementGrowth();
            }
            // TODO: cleanup this poor logic
            if (getGrowth() < 600) {
                setInfant(true);
            } else {
                setInfant(false);
            }
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
                EntityFoliaath adultFoliaath = new EntityFoliaath(world);
                adultFoliaath.setPosition(posX, posY, posZ);
                adultFoliaath.setCanDespawn(false);
                world.spawnEntity(adultFoliaath);
                setDead();
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

    private boolean arePlayersCarryingMeat(List<PlayerEntity> players) {
        if (players.size() > 0) {
            for (PlayerEntity player : players) {
                if (getMeatList().contains(player.getHeldItemMainhand().getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<Item> getMeatList() {
        if (meatTypes == null) {
            meatTypes = Sets.newHashSet();
            LootTable lootTable = getEntityWorld().getLootTableManager().getLootTableFromLocation(getFoodLootTable());
            if (lootTable != LootTable.EMPTY_LOOT_TABLE) {
                LootContext.Builder lootBuilder = (new LootContext.Builder((ServerWorld) this.world)).withLootedEntity(this);
                List<ItemStack> loot = lootTable.generateLootForPools(world.rand, lootBuilder.build());
                for (ItemStack itemStack : loot) {
                    meatTypes.add(itemStack.getItem());
                }
            }
        }
        return meatTypes;
    }

    protected ResourceLocation getFoodLootTable() {
        return LootTableHandler.BABY_FOLIAATH_FOOD;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        for (int i = 0; i < 10; i++) {
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX, posY + 0.2, posZ, 0, 0, 0, JUNGLE_LEAVES);
        }
        setDead();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity collider) {
//        posX = prevPosX;
//        posZ = prevPosZ;
        motionX = 0;
        motionZ = 0;
    }

    @Override
    protected SoundEvent getDeathSound() {
        playSound(SoundEvents.BLOCK_GRASS_HIT, 1, 0.8F);
        return null;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (world.checkNoEntityCollision(getBoundingBox()) && world.getCollisionBoxes(this, getBoundingBox()).isEmpty() && !world.containsAnyLiquid(getBoundingBox())) {
            BlockPos ground = new BlockPos(
                MathHelper.floor(posX),
                MathHelper.floor(getBoundingBox().minY) - 1,
                MathHelper.floor(posZ)
            );

            BlockState block = world.getBlockState(ground);

            if (block.getBlock() == Blocks.GRASS || block.getBlock() == Blocks.DIRT || block.getBlock().isLeaves(block, world, ground)) {
                playSound(SoundEvents.BLOCK_GRASS_HIT, 1, 0.8F);
                return true;
            }
        }
        return false;
    }

    public List<ItemEntity> getMeatsNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(distanceX, distanceY, distanceZ));
        ArrayList<ItemEntity> listEntityItem = new ArrayList<>();
        for (Entity entityNeighbor : list) {
            if (entityNeighbor instanceof ItemEntity && getDistance(entityNeighbor) <= radius) {
                if (getMeatList().contains(((ItemEntity) entityNeighbor).getItem().getItem())) {
                    listEntityItem.add((ItemEntity) entityNeighbor);
                }
            }
        }
        return listEntityItem;
    }

    @Override
    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("tickGrowth", getGrowth());
    }

    @Override
    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        setGrowth(compound.getInteger("tickGrowth"));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(GROWTH, 0);
        getDataManager().register(INFANT, false);
        getDataManager().register(HUNGRY, false);
        getDataManager().register(EATING, ItemStack.EMPTY);
    }

    public int getGrowth() {
        return getDataManager().get(GROWTH);
    }

    public void setGrowth(int growth) {
        getDataManager().set(GROWTH, growth);
    }

    public void incrementGrowth() {
        setGrowth(getGrowth() + 1);
    }

    public boolean getInfant() {
        return getDataManager().get(INFANT);
    }

    public void setInfant(boolean infant) {
        getDataManager().set(INFANT, infant);
    }

    public boolean getHungry() {
        return getDataManager().get(HUNGRY);
    }

    public void setHungry(boolean hungry) {
        getDataManager().set(HUNGRY, hungry);
    }

    public void setEating(ItemStack stack) {
        getDataManager().set(EATING, stack);
    }

    public ItemStack getEating() {
        return getDataManager().get(EATING);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{EAT_ANIMATION};
    }
}