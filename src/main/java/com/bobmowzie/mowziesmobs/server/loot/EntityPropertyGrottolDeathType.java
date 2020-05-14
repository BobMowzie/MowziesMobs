package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.properties.EntityProperty;

import java.util.Random;

public class EntityPropertyGrottolDeathType implements EntityProperty {
    private final String deathTypeString;

    public EntityPropertyGrottolDeathType(String deathTypeString) {
        this.deathTypeString = deathTypeString;
    }

    @Override
    public boolean testProperty(Random random, Entity entity) {
        EntityGrottol.EnumDeathType requestedDeathType;
        if (deathTypeString.equals("pickaxe")) {
            requestedDeathType = EntityGrottol.EnumDeathType.PICKAXE;
        }
        else if (deathTypeString.equals("fortune_pickaxe") || deathTypeString.equals("pickaxe_fortune")) {
            requestedDeathType = EntityGrottol.EnumDeathType.FORTUNE_PICKAXE;
        }
        else {
            requestedDeathType = EntityGrottol.EnumDeathType.NORMAL;
        }
        return entity instanceof EntityGrottol && ((EntityGrottol)entity).getDeathType() == requestedDeathType;
    }

    public static class Serializer extends EntityProperty.Serializer<EntityPropertyGrottolDeathType> {
        public Serializer() {
            super(new ResourceLocation(MowziesMobs.MODID, "grottol_death_type"), EntityPropertyGrottolDeathType.class);
        }

        @Override
        public JsonElement serialize(EntityPropertyGrottolDeathType property, JsonSerializationContext serializationContext) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", property.deathTypeString);
            return obj;
        }

        @Override
        public EntityPropertyGrottolDeathType deserialize(JsonElement element, JsonDeserializationContext deserializationContext) {
            JsonObject obj = JsonUtils.getJsonObject(element, this.getName().getPath());
            return new EntityPropertyGrottolDeathType(JsonUtils.getString(obj.get("type"), "type"));
        }
    }
}
