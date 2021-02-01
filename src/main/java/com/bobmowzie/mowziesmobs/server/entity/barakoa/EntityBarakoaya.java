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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class EntityBarakoaya extends EntityBarakoa implements LeaderSunstrikeImmune {
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

    private static final DataParameter<Optional<Trade>> TRADE = EntityDataManager.createKey(EntityBarakoaya.class, ServerProxy.OPTIONAL_TRADE);
    //    private static final DataParameter<Integer> NUM_SALES = EntityDataManager.createKey(EntityBarakoaya.class, DataSerializers.VARINT);
    //TODO: Sale limits. After X sales, go out of stock and change trade.

    private static final int MIN_OFFER_TIME = 5 * 60 * 20;

    private static final int MAX_OFFER_TIME = 20 * 60 * 20;

    private TradeStore tradeStore = TradeStore.EMPTY;

    private int timeOffering;

//    private static final int SOLD_OUT_TIME = 5 * 60 * 20;
//    private static final int MAX_SALES = 5;

    private PlayerEntity customer;

    public EntityBarakoaya(EntityType<? extends EntityBarakoaya> type, World world) {
        super(type, world);
        setWeapon(0);
//        setNumSales(MAX_SALES);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new EntityAIBarakoayaTrade(this));
        goalSelector.addGoal(1, new EntityAIBarakoayaTradeLook(this));
        targetSelector.addGoal(3, new BarakoaHurtByTargetAI(this, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, true, target -> {
            if (target instanceof PlayerEntity) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).inventory.armorInventory.get(3);
                if (headArmorStack.getItem() instanceof BarakoaMask) {
                    return false;
                }
            }
            return true;
        }));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 0, true, true, null));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, 0, true, false, null));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(TRADE, Optional.empty());
//        getDataManager().register(NUM_SALES, MAX_SALES);
    }

    public void setOfferingTrade(Trade trade) {
        getDataManager().set(TRADE, Optional.ofNullable(trade));
    }

    public Trade getOfferingTrade() {
        return getDataManager().get(TRADE).orElse(null);
    }
    }

//    public int getNumSales() {
//        return getDataManager().get(NUM_SALES);
//    }
//
//    public void setNumSales(int numSales) {
//        getDataManager().set(NUM_SALES, numSales);
//    }

    public boolean isOfferingTrade() {
        if (getDataManager().get(TRADE) instanceof Optional) {
            return getDataManager().get(TRADE).isPresent();
        }
        else return false;
    }

    public void setCustomer(PlayerEntity customer) {
        this.customer = customer;
    }

    public PlayerEntity getCustomer() {
        return customer;
    }

    public boolean isTrading() {
        return customer != null;
    }

    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    //    @Override
//    public ContainerBarakoayaTrade createContainer(World world, PlayerEntity player, int x, int y, int z) {
//        return
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ContainerScreen createGui(World world, PlayerEntity player, int x, int y, int z) {
//        return new GuiBarakoayaTrade(this, new InventoryBarakoaya(this), createContainer(world, player, x, y, z), player.inventory, getDisplayName());
//    }

    @Override
    public void tick() {
        super.tick();
        if ((!isOfferingTrade() || timeOffering <= 0) && tradeStore.hasStock()) {
            setOfferingTrade(tradeStore.get(rand));
            timeOffering = rand.nextInt(MAX_OFFER_TIME - MIN_OFFER_TIME + 1) + MIN_OFFER_TIME;
        }
    }

    public void openGUI(PlayerEntity playerEntity) {
        setCustomer(playerEntity);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.world.isRemote && getAttackTarget() == null && isAlive()) {
            playerEntity.openContainer(new INamedContainerProvider() {
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ContainerBarakoayaTrade(id, EntityBarakoaya.this, playerInventory);
                }

                @Override
                public ITextComponent getDisplayName() {
                    return EntityBarakoaya.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (canTradeWith(player) && getAttackTarget() == null && isAlive()) {
            openGUI(player);
            return true;
        }
        return false;
    }

    public boolean canTradeWith(PlayerEntity player) {
        if (isTrading()) {
            return false;
        }
        ItemStack headStack = player.inventory.armorInventory.get(3);
        return headStack.getItem() instanceof BarakoaMask && isOfferingTrade();
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
        tradeStore = DEFAULT;
        if (reason == SpawnReason.STRUCTURE) {
            this.setHomePosAndDistance(getPosition(), 16);
        }
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean preventDespawn() {
        return true;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("tradeStore", tradeStore.serialize());
        if (isOfferingTrade()) {
            compound.put("offeringTrade", getOfferingTrade().serialize());
        }
        compound.putInt("timeOffering", timeOffering);
//        compound.setInteger("numSales", getNumSales());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        tradeStore = TradeStore.deserialize(compound.getCompound("tradeStore"));
        setOfferingTrade(Trade.deserialize(compound.getCompound("offeringTrade")));
        timeOffering = compound.getInt("timeOffering");
//        setNumSales(compound.getInteger("numSales"));
    }
}
