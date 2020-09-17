package com.ilexiconn.llibrary.client.model.tabula.baked.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

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
        translation.scale(0.0625F);
        translation.x = MathHelper.clamp(translation.x, -5.0F, 5.0F);
        translation.y = MathHelper.clamp(translation.y, -5.0F, 5.0F);
        translation.z = MathHelper.clamp(translation.z, -5.0F, 5.0F);
        Vector3f scale = this.parseVector3f(object, "scale", SCALE_DEFAULT);
        scale.x = MathHelper.clamp(scale.x, -4.0F, 4.0F);
        scale.y = MathHelper.clamp(scale.y, -4.0F, 4.0F);
        scale.z = MathHelper.clamp(scale.z, -4.0F, 4.0F);
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
