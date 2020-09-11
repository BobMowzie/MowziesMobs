package com.bobmowzie.mowziesmobs.server.entity.barakoa.trade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public final class Trade {
    private final ItemStack input;

    private final ItemStack output;

    private final int weight;

    public Trade(ItemStack input, ItemStack output, int weight) {
        this.input = input.copy();
        this.output = output.copy();
        this.weight = weight;
    }

    public Trade(Trade trade) {
        this(trade.input, trade.output, trade.weight);
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Trade) {
            Trade trade = (Trade) o;
            return weight == trade.weight && ItemStack.areItemsEqual(input, trade.input) && ItemStack.areItemsEqual(output, trade.output);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 961 * input.hashCode() + 31 * output.hashCode() + weight;
    }

    public CompoundNBT serialize() {
        CompoundNBT compound = new CompoundNBT();
        compound.setTag("input", input.writeToNBT(new CompoundNBT()));
        compound.setTag("output", output.writeToNBT(new CompoundNBT()));
        compound.setInteger("weight", weight);
        return compound;
    }

    public static Trade deserialize(CompoundNBT compound) {
        ItemStack input = new ItemStack(compound.getCompoundTag("input"));
        ItemStack output = new ItemStack(compound.getCompoundTag("output"));
        int weight = compound.getInteger("weight");
        if (input.isEmpty() || output.isEmpty() || weight < 1) {
            return null;
        }
        return new Trade(input, output, weight);
    }
}
