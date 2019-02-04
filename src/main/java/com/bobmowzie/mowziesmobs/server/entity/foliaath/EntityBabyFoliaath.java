package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBabyFoliaathEatAI;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.Sets;

public class EntityBabyFoliaath extends MowzieEntity {
    private static final int JUNGLE_LEAVES = Block.getStateId(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));

    private static final DataParameter<Integer> GROWTH = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.VARINT);

    private static final DataParameter<Boolean> INFANT = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> HUNGRY = EntityDataManager.createKey(EntityBabyFoliaath.class, DataSerializers.BOOLEAN);

    public static final Animation EAT_ANIMATION = Animation.create(20);
    public ControlledAnimation activate = new ControlledAnimation(5);
    private Item eatingItemID;
    private double prevActivate;

    private static Set<Item> meat;

    public EntityBabyFoliaath(World world) {
        super(world);
        tasks.addTask(1, new AnimationBabyFoliaathEatAI<>(this, EAT_ANIMATION));
        setSize(0.4F, 0.4F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
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

        List<EntityItem> meats = getMeatsNearby(0.4, 0.2, 0.4, 0.4);
        if (getHungry() && meats.size() != 0 && getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, EAT_ANIMATION);
            eatingItemID = meats.get(0).getItem().getItem();
            meats.get(0).setDead();
            playSound(MMSounds.ENTITY_FOLIAATH_BABY_EAT, 0.5F, 1.2F);
            if (!world.isRemote) {
                incrementGrowth();
                setHungry(false);
            }
        }

        if (getAnimationTick() == 3 || getAnimationTick() == 7 || getAnimationTick() == 11 || getAnimationTick() == 15 || getAnimationTick() == 19) {
            try {
                for (int i = 0; i <= 5; i++) {
                    world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY + 0.2, posZ, Math.random() * 0.2 - 0.1, Math.random() * 0.2, Math.random() * 0.2 - 0.1, Item.getIdFromItem(eatingItemID));
                }
            } catch (Exception ignored) {
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

    private boolean arePlayersCarryingMeat(List<EntityPlayer> players) {
        if (players.size() > 0) {
            for (EntityPlayer player : players) {
                if (getMeat().contains(player.getHeldItemMainhand().getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<Item> getMeat() {
        if (meat == null) {
            meat = Sets.newHashSet(Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_FISH, Items.RABBIT, Items.COOKED_RABBIT, Items.MUTTON, Items.COOKED_MUTTON, Items.BEEF, Items.CHICKEN, Items.FISH, Items.SPIDER_EYE);
        }
        return meat;
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
    public void applyEntityCollision(Entity collider) {
        posX = prevPosX;
        posZ = prevPosZ;
    }

    @Override
    protected SoundEvent getDeathSound() {
        playSound(SoundEvents.BLOCK_GRASS_HIT, 1, 0.8F);
        return null;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (world.checkNoEntityCollision(getEntityBoundingBox()) && world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox())) {
            BlockPos ground = new BlockPos(
                MathHelper.floor(posX),
                MathHelper.floor(getEntityBoundingBox().minY) - 1,
                MathHelper.floor(posZ)
            );

            IBlockState block = world.getBlockState(ground);

            if (block.getBlock() == Blocks.GRASS || block.getBlock() == Blocks.DIRT || block.getBlock().isLeaves(block, world, ground)) {
                playSound(SoundEvents.BLOCK_GRASS_HIT, 1, 0.8F);
                return true;
            }
        }
        return false;
    }

    public List<EntityItem> getMeatsNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(distanceX, distanceY, distanceZ));
        ArrayList<EntityItem> listEntityItem = new ArrayList<>();
        for (Entity entityNeighbor : list) {
            if (entityNeighbor instanceof EntityItem && getDistanceToEntity(entityNeighbor) <= radius) {
                if (getMeat().contains(((EntityItem) entityNeighbor).getItem().getItem())) {
                    listEntityItem.add((EntityItem) entityNeighbor);
                }
            }
        }
        return listEntityItem;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("tickGrowth", getGrowth());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
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

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{EAT_ANIMATION};
    }
}