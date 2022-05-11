package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootFunctionGrottolDeathType extends LootItemConditionalFunction {
    public LootFunctionGrottolDeathType(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof EntityGrottol grottol) {
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
    public LootItemFunctionType getType() {
        return null;
    }

    public static class FunctionSerializer extends LootItemConditionalFunction.Serializer<LootFunctionGrottolDeathType> {
        @Override
        public void serialize(JsonObject object, LootFunctionGrottolDeathType functionClazz, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionGrottolDeathType deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new LootFunctionGrottolDeathType(conditionsIn);
        }
    }
}
