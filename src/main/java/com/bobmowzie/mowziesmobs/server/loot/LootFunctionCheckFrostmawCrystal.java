package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.gson.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

public class LootFunctionCheckFrostmawCrystal extends LootFunction {
    public LootFunctionCheckFrostmawCrystal(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        if (entity instanceof EntityFrostmaw) {
            EntityFrostmaw frostmaw = (EntityFrostmaw)entity;
            if (!frostmaw.getHasCrystal()) {
                stack.setCount(0);
            }
        }
        return stack;
    }

    @Override
    public LootFunctionType getFunctionType() {
        return LootTableHandler.CHECK_FROSTMAW_CRYSTAL;
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionCheckFrostmawCrystal> {
        public Serializer() {
            super();
        }

        public void serialize(JsonObject object, LootFunctionCheckFrostmawCrystal function, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionCheckFrostmawCrystal deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionCheckFrostmawCrystal(conditionsIn);
        }
    }
}
