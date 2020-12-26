package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

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

    public static class Serializer extends LootFunction.Serializer<LootFunctionCheckFrostmawCrystal> {
        public Serializer() {
            super(new ResourceLocation(MowziesMobs.MODID, "has_crystal"), LootFunctionCheckFrostmawCrystal.class);
        }

        public void serialize(LootFunctionCheckFrostmawCrystal property, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionCheckFrostmawCrystal deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionCheckFrostmawCrystal(conditionsIn);
        }
    }
}
