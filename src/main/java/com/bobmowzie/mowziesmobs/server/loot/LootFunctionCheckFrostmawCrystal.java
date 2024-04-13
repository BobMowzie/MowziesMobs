package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
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

public class LootFunctionCheckFrostmawCrystal extends LootItemConditionalFunction {
    public LootFunctionCheckFrostmawCrystal(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof EntityFrostmaw) {
            EntityFrostmaw frostmaw = (EntityFrostmaw) entity;
            if (!frostmaw.getHasCrystal()) {
                stack.setCount(0);
            }
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootTableHandler.CHECK_FROSTMAW_CRYSTAL;
    }

    public static class FunctionSerializer extends LootItemConditionalFunction.Serializer<LootFunctionCheckFrostmawCrystal> {

        @Override
        public void serialize(JsonObject object, LootFunctionCheckFrostmawCrystal function, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionCheckFrostmawCrystal deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new LootFunctionCheckFrostmawCrystal(conditionsIn);
        }
    }
}
