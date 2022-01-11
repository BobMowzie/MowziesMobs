package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

public class LootFunctionGrottolDeathType extends LootFunction {
    public LootFunctionGrottolDeathType(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        if (entity instanceof EntityGrottol) {
            EntityGrottol grottol = (EntityGrottol)entity;
            EntityGrottol.EnumDeathType deathType = grottol.getDeathType();
            if (deathType == EntityGrottol.EnumDeathType.NORMAL) {
                stack.setCount(0);
            } else if (deathType == EntityGrottol.EnumDeathType.FORTUNE_PICKAXE) {
                stack.setCount(stack.getCount() + 1);
            }
        }
        return stack;
    }

    @Override
    public LootFunctionType getFunctionType() {
        return null;
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionGrottolDeathType> {
        public Serializer() {
            super();
        }

        public void serialize(JsonObject object, LootFunctionGrottolDeathType functionClazz, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionGrottolDeathType deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionGrottolDeathType(conditionsIn);
        }
    }
}
