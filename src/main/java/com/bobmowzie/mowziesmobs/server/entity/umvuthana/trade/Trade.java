package com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

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
            return weight == trade.weight && ItemStack.matches(input, trade.input) && ItemStack.matches(output, trade.output);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 961 * input.hashCode() + 31 * output.hashCode() + weight;
    }

    public CompoundTag serialize() {
        CompoundTag compound = new CompoundTag();
        compound.put("input", input.save(new CompoundTag()));
        compound.put("output", output.save(new CompoundTag()));
        compound.putInt("weight", weight);
        return compound;
    }

    public static Trade deserialize(CompoundTag compound) {
        ItemStack input = ItemStack.of(compound.getCompound("input"));
        ItemStack output = ItemStack.of(compound.getCompound("output"));
        int weight = compound.getInt("weight");
        if (input.isEmpty() || output.isEmpty() || weight < 1) {
            return null;
        }
        return new Trade(input, output, weight);
    }
}
