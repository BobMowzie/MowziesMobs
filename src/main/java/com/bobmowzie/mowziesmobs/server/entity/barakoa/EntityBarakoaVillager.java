package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIBarakoayaTradeLook;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.TradeStore;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.sounds.ActionResultType;
import net.minecraft.sounds.Hand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityBarakoaVillager extends EntityBarakoa implements LeaderSunstrikeImmune, Enemy {
    private static final TradeStore DEFAULT = new TradeStore.Builder()
        .addTrade(Items.GOLD_INGOT, 2, ItemHandler.BLOWGUN, 1, 6)
        .addTrade(Items.COCOA_BEANS, 10, ItemHandler.DART, 8, 6)
        .addTrade(Items.GOLD_INGOT, 3, ItemHandler.SPEAR, 1, 4)
        .addTrade(Items.GOLD_NUGGET, 5, BlockHandler.PAINTED_ACACIA.get().asItem(), 2, 4)
        .addTrade(Items.COCOA_BEANS, 16, BlockHandler.PAINTED_ACACIA.get().asItem(), 1, 4)
        .addTrade(Items.COCOA_BEANS, 10, Items.COOKED_CHICKEN, 2, 2)
        .addTrade(Items.GOLD_NUGGET, 8, Items.COOKED_CHICKEN, 1, 2)
        .addTrade(Items.COCOA_BEANS, 14, Items.COOKED_PORKCHOP, 2, 2)
        .addTrade(Items.GOLD_NUGGET, 9, Items.COOKED_PORKCHOP, 1, 2)

        .addTrade(Items.MELON, 3, Items.GOLD_NUGGET, 5, 2)
        .addTrade(Items.CHICKEN, 1, Items.GOLD_NUGGET, 3, 2)
        .addTrade(Items.IRON_SWORD, 1, Items.GOLD_INGOT, 2, 2)
        .addTrade(Items.IRON_HELMET, 1, Items.GOLD_INGOT, 4, 2)
        .build();

    private static final EntityDataAccessor<Optional<Trade>> TRADE = SynchedEntityData.defineId(EntityBarakoaVillager.class, ServerProxy.OPTIONAL_TRADE);
    //    private static final EntityDataAccessor<Integer> NUM_SALES = SynchedEntityData.defineId(EntityBarakoaya.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> MISBEHAVED_PLAYER = SynchedEntityData.defineId(EntityBarakoaVillager.class, EntityDataSerializers.OPTIONAL_UNIQUE_ID);

    //TODO: Sale limits. After X sales, go out of stock and change trade.

    private static final int MIN_OFFER_TIME = 5 * 60 * 20;

    private static final int MAX_OFFER_TIME = 20 * 60 * 20;

    private TradeStore tradeStore = TradeStore.EMPTY;

    private int timeOffering;

//    private static final int SOLD_OUT_TIME = 5 * 60 * 20;
//    private static final int MAX_SALES = 5;

    private Player customer;

    public EntityBarakoaVillager(EntityType<? extends EntityBarakoaVillager> type, Level world) {
        super(type, world);
        setWeapon(0);
//        setNumSales(MAX_SALES);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new EntityAIBarakoayaTrade(this));
        goalSelector.addGoal(1, new EntityAIBarakoayaTradeLook(this));
        this.goalSelector.addGoal(7, new MoveTowardsRestrictionGoal(this, 0.4));
    }

    @Override
    protected void registerTargetGoals() {
        targetSelector.addGoal(3, new BarakoaHurtByTargetAI(this, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<Player>(this, Player.class, 0, true, true, target -> {
            if (target instanceof Player) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((Player) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask) || target == getMisbehavedPlayer();
            }
            return true;
        }){
            @Override
            public void resetTask() {
                super.resetTask();
                setMisbehavedPlayerId(null);
            }
        });
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Zombie.class, 0, true, true, null));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 0, true, false, null));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TRADE, Optional.empty());
        this.dataManager.register(MISBEHAVED_PLAYER, Optional.empty());
//        getEntityData().define(NUM_SALES, MAX_SALES);
    }

    public void setOfferingTrade(Trade trade) {
        getEntityData().set(TRADE, Optional.ofNullable(trade));
    }

    public Trade getOfferingTrade() {
        return getEntityData().get(TRADE).orElse(null);
    }

    //    public int getNumSales() {
//        return getEntityData().get(NUM_SALES);
//    }
//
//    public void setNumSales(int numSales) {
//        getEntityData().set(NUM_SALES, numSales);
//    }

    public boolean isOfferingTrade() {
        if (getEntityData().get(TRADE) instanceof Optional) {
            return getEntityData().get(TRADE).isPresent();
        }
        else return false;
    }

    public void setCustomer(Player customer) {
        this.customer = customer;
    }

    public Player getCustomer() {
        return customer;
    }

    public boolean isTrading() {
        return customer != null;
    }

    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (getTarget() instanceof Player) {
            if (((Player) getTarget()).isCreative() || getTarget().isSpectator()) setTarget(null);
        }
        if ((!isOfferingTrade() || timeOffering <= 0) && tradeStore.hasStock()) {
            setOfferingTrade(tradeStore.get(rand));
            timeOffering = random.nextInt(MAX_OFFER_TIME - MIN_OFFER_TIME + 1) + MIN_OFFER_TIME;
        }
    }

    public void openGUI(Player Player) {
        setCustomer(Player);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.level.isClientSide && getTarget() == null && isAlive()) {
            Player.openContainer(new INamedContainerProvider() {
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, Player player) {
                    return new ContainerBarakoayaTrade(id, EntityBarakoaVillager.this, playerInventory);
                }

                @Override
                public TextComponent getDisplayName() {
                    return EntityBarakoaVillager.this.getDisplayName();
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
        if (isTrading()) {
            return false;
        }
        ItemStack headStack = player.inventory.armorInventory.get(3);
        return headStack.getItem() instanceof BarakoaMask && isOfferingTrade();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(IServerLevel world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag compound) {
        tradeStore = DEFAULT;
        if (reason == MobSpawnType.COMMAND) setHomePosAndDistance(getPosition(), 25);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("tradeStore", tradeStore.serialize());
        if (isOfferingTrade()) {
            compound.put("offeringTrade", getOfferingTrade().serialize());
        }
        compound.putInt("timeOffering", timeOffering);
        compound.putInt("HomePosX", this.getHomePosition().x());
        compound.putInt("HomePosY", this.getHomePosition().y());
        compound.putInt("HomePosZ", this.getHomePosition().z());
        compound.putInt("HomeDist", (int) this.getMaximumHomeDistance());
        if (this.getMisbehavedPlayerId() != null) {
            compound.putUniqueId("MisbehavedPlayer", this.getMisbehavedPlayerId());
        }
//        compound.setInteger("numSales", getNumSales());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        tradeStore = TradeStore.deserialize(compound.getCompound("tradeStore"));
        setOfferingTrade(Trade.deserialize(compound.getCompound("offeringTrade")));
        timeOffering = compound.getInt("timeOffering");
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        int dist = compound.getInt("HomeDist");
        this.setHomePosAndDistance(new BlockPos(i, j, k), dist);
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
//        setNumSales(compound.getInteger("numSales"));
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
}
