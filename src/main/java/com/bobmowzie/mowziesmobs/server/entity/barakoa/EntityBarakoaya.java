package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.TradeStore;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler.ContainerHolder;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityBarakoaya extends EntityBarakoa implements ContainerHolder {
    private static final TradeStore DEFAULT = new TradeStore.Builder()
        .addTrade(new ItemStack(Items.GOLD_NUGGET, 12), new ItemStack(ItemHandler.INSTANCE.blowgun), 5)
        .addTrade(new ItemStack(Items.DYE, 6, EnumDyeColor.BROWN.getDyeDamage()), new ItemStack(ItemHandler.INSTANCE.dart, 8), 6)
        .addTrade(new ItemStack(Items.GOLD_NUGGET, 8), new ItemStack(ItemHandler.INSTANCE.spear), 5)
        .build();

    private static final int MIN_OFFER_TIME = 5 * 60 * 20;

    private static final int MAX_OFFER_TIME = 20 * 60 * 20;

    private TradeStore tradeStore = TradeStore.EMPTY;

    private Trade offeringTrade;

    private int timeOffering;

    public EntityBarakoaya(World world) {
        super(world);
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(3, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, 0, true, true, null));
    }

    @Override
    public Container createContainer(World world, EntityPlayer player) {
        return null;
    }

    @Override
    public GuiContainer createGui(World world, EntityPlayer player) {
        return null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if ((offeringTrade == null || timeOffering <= 0) && tradeStore.hasStock()) {
            offeringTrade = tradeStore.get(rand);
            timeOffering = rand.nextInt(MAX_OFFER_TIME - MIN_OFFER_TIME + 1) + MIN_OFFER_TIME;
        }
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack headStack = player.inventory.armorInventory[3];
        if (headStack != null && headStack.getItem() instanceof BarakoaMask && offeringTrade != null) {
            if (!worldObj.isRemote) {
                GuiHandler.open(GuiHandler.BARAKOA_TRADE, player, this);
            }
            return true;
        }
        return false;
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
        if (offeringTrade != null) {
            compound.setTag("offeringTrade", offeringTrade.serialize());
        }
        compound.setInteger("timeOffering", timeOffering);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        tradeStore = TradeStore.deserialize(compound.getCompoundTag("tradeStore"));
        offeringTrade = Trade.deserialize(compound.getCompoundTag("offeringTrade"));
        timeOffering = compound.getInteger("timeOffering");
    }
}
