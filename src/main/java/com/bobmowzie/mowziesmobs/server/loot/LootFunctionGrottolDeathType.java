package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

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

    public static class Serializer extends LootFunction.Serializer<LootFunctionGrottolDeathType> {
        public Serializer() {
            super(new ResourceLocation(MowziesMobs.MODID, "grottol_death_type"), LootFunctionGrottolDeathType.class);
        }

        public void serialize(LootFunctionGrottolDeathType property, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionGrottolDeathType deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new LootFunctionGrottolDeathType(conditionsIn);
        }
    }
}
