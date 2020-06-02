package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.properties.EntityProperty;

import java.util.Random;

public class EntityPropertyFrostmawHasCrystal implements EntityProperty {
    private final boolean hasCrystal;

    public EntityPropertyFrostmawHasCrystal(boolean hasCrystal) {
        this.hasCrystal = hasCrystal;
    }

    @Override
    public boolean testProperty(Random random, Entity entity) {
        return entity instanceof EntityFrostmaw && ((EntityFrostmaw)entity).getHasCrystal() == this.hasCrystal;
    }

    public static class Serializer extends EntityProperty.Serializer<EntityPropertyFrostmawHasCrystal> {
        public Serializer() {
            super(new ResourceLocation(MowziesMobs.MODID, "has_crystal"), EntityPropertyFrostmawHasCrystal.class);
        }

        @Override
        public JsonElement serialize(EntityPropertyFrostmawHasCrystal property, JsonSerializationContext serializationContext) {
            return new JsonPrimitive(property.hasCrystal);
        }

        @Override
        public EntityPropertyFrostmawHasCrystal deserialize(JsonElement element, JsonDeserializationContext deserializationContext) {
            return new EntityPropertyFrostmawHasCrystal(JsonUtils.getBoolean(element, this.getName().getPath()));
        }
    }
}
