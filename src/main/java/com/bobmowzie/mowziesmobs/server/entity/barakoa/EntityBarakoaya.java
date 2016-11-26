package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIBarakoayaTradeLook;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.TradeStore;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler.ContainerHolder;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.google.common.base.Optional;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBarakoaya extends EntityBarakoa implements ContainerHolder {
    private static final TradeStore DEFAULT = new TradeStore.Builder()
        .addTrade(Items.GOLD_NUGGET, 9, ItemHandler.INSTANCE.blowgun, 1, 6)
        .addTrade(Items.DYE, 20, EnumDyeColor.BROWN.getDyeDamage(), ItemHandler.INSTANCE.dart, 8, 0, 6)
        .addTrade(Items.GOLD_NUGGET, 15, ItemHandler.INSTANCE.spear, 1, 4)
        .addTrade(Items.GOLD_INGOT, 2, ItemHandler.INSTANCE.spear, 1, 4)
        .addTrade(Items.GOLD_NUGGET, 6, Item.getItemFromBlock(BlockHandler.INSTANCE.paintedAcacia), 2, 4)
        .addTrade(Items.DYE, 16, EnumDyeColor.BROWN.getDyeDamage(), Item.getItemFromBlock(BlockHandler.INSTANCE.paintedAcacia), 1, 0, 4)
        .addTrade(Items.DYE, 10, EnumDyeColor.BROWN.getDyeDamage(), Items.COOKED_CHICKEN, 2, 0, 2)
        .addTrade(Items.GOLD_NUGGET, 4, Items.COOKED_CHICKEN, 1, 2)
        .addTrade(Items.DYE, 14, EnumDyeColor.BROWN.getDyeDamage(), Items.COOKED_PORKCHOP, 2, 0, 2)
        .addTrade(Items.GOLD_NUGGET, 5, Items.COOKED_PORKCHOP, 1, 2)

        .addTrade(Items.MELON, 3, Items.GOLD_NUGGET, 5, 2)
        .addTrade(Items.CHICKEN, 1, Items.GOLD_NUGGET, 3, 2)
        .addTrade(Items.IRON_SWORD, 1, Items.GOLD_INGOT, 1, 2)
        .addTrade(Items.IRON_HELMET, 2, Items.GOLD_INGOT, 2, 2)
        .addTrade(Items.STONE_SWORD, 1, Items.GOLD_NUGGET, 5, 2)
        .build();

    private static final DataParameter<Optional<Trade>> TRADE = EntityDataManager.createKey(EntityBarakoaya.class, ServerProxy.OPTIONAL_TRADE);

    private static final int MIN_OFFER_TIME = 5 * 60 * 20;

    private static final int MAX_OFFER_TIME = 20 * 60 * 20;

    private TradeStore tradeStore = TradeStore.EMPTY;

    private int timeOffering;

    private EntityPlayer customer;

    public EntityBarakoaya(World world) {
        super(world);
        tasks.addTask(1, new EntityAIBarakoayaTrade(this));
        tasks.addTask(1, new EntityAIBarakoayaTradeLook(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(3, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, 0, true, true, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntitySkeleton.class, 0, true, false, null));
        setWeapon(0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(TRADE, Optional.absent());
    }

    public void setOfferingTrade(Trade trade) {
        getDataManager().set(TRADE, Optional.fromNullable(trade));
    }

    public Trade getOfferingTrade() {
        return getDataManager().get(TRADE).orNull();
    }

    public boolean isOfferingTrade() {
        return getDataManager().get(TRADE).isPresent();
    }

    public void setCustomer(EntityPlayer customer) {
        this.customer = customer;
    }

    public EntityPlayer getCustomer() {
        return customer;
    }

    public boolean isTrading() {
        return customer != null;
    }

    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public Container createContainer(World world, EntityPlayer player, int x, int y, int z) {
        return new ContainerBarakoayaTrade(this, player.inventory, world);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer createGui(World world, EntityPlayer player, int x, int y, int z) {
        return new GuiBarakoayaTrade(this, player.inventory, world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if ((!isOfferingTrade() || timeOffering <= 0) && tradeStore.hasStock()) {
            setOfferingTrade(tradeStore.get(rand));
            timeOffering = rand.nextInt(MAX_OFFER_TIME - MIN_OFFER_TIME + 1) + MIN_OFFER_TIME;
        }
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (canTradeWith(player)) {
            setCustomer(player);
            if (!worldObj.isRemote) {
                GuiHandler.open(GuiHandler.BARAKOA_TRADE, player, this);
            }
            return true;
        }
        return false;
    }

    public boolean canTradeWith(EntityPlayer player) {
        if (isTrading()) {
            return false;
        }
        ItemStack headStack = player.inventory.armorInventory[3];
        return headStack != null && headStack.getItem() instanceof BarakoaMask && isOfferingTrade();
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        tradeStore = DEFAULT;
        return super.onInitialSpawn(difficulty, data);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("tradeStore", tradeStore.serialize());
        if (isOfferingTrade()) {
            compound.setTag("offeringTrade", getOfferingTrade().serialize());
        }
        compound.setInteger("timeOffering", timeOffering);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        tradeStore = TradeStore.deserialize(compound.getCompoundTag("tradeStore"));
        setOfferingTrade(Trade.deserialize(compound.getCompoundTag("offeringTrade")));
        timeOffering = compound.getInteger("timeOffering");
    }
}
