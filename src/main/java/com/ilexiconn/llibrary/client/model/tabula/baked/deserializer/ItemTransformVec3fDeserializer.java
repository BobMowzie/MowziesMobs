package com.ilexiconn.llibrary.client.model.tabula.baked.deserializer;

import com.google.gson.*;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.JsonUtils;

import java.lang.reflect.Type;

/**
 * @author pau101
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ItemTransformVec3fDeserializer implements JsonDeserializer<ItemTransformVec3f> {
    private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

    @Override
    public ItemTransformVec3f deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        Vector3f rotation = this.parseVector3f(object, "rotation", ROTATION_DEFAULT);
        Vector3f translation = this.parseVector3f(object, "translation", TRANSLATION_DEFAULT);
        translation.mul(0.0625F);
        float x = MathHelper.clamp(translation.getX(), -5.0F, 5.0F);
        float y = MathHelper.clamp(translation.getY(), -5.0F, 5.0F);
        float z = MathHelper.clamp(translation.getZ(), -5.0F, 5.0F);
        translation.set(x, y, z);
        Vector3f scale = this.parseVector3f(object, "scale", SCALE_DEFAULT);
        scale.clamp(-4.0f, 4.0f);
        return new ItemTransformVec3f(rotation, translation, scale);
    }

    private Vector3f parseVector3f(JsonObject object, String key, Vector3f defaultValue) {
        if (!object.has(key)) {
            return defaultValue;
        } else {
            JsonArray jsonarray = JsonUtils.getJsonArray(object, key);

            if (jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
            } else {
                float[] xyz = new float[3];

                for (int i = 0; i < xyz.length; ++i) {
                    xyz[i] = JsonUtils.getFloat(jsonarray.get(i), key + "[" + i + "]");
                }

                return new Vector3f(xyz[0], xyz[1], xyz[2]);
            }
        }
    }
}
