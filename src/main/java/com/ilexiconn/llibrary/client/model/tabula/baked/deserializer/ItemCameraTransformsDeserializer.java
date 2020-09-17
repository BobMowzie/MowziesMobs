package com.ilexiconn.llibrary.client.model.tabula.baked.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;

/**
 * @author pau101
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ItemCameraTransformsDeserializer implements JsonDeserializer<ItemCameraTransforms> {
    @Override
    public ItemCameraTransforms deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        ItemTransformVec3f thirdperson_righthand = this.getTransform(context, object, "thirdperson_righthand");
        ItemTransformVec3f thirdperson_lefthand = this.getTransform(context, object, "thirdperson_lefthand");

        if (thirdperson_lefthand == ItemTransformVec3f.DEFAULT) {
            thirdperson_lefthand = thirdperson_righthand;
        }

        ItemTransformVec3f firstperson_righthand = this.getTransform(context, object, "firstperson_righthand");
        ItemTransformVec3f firstperson_lefthand = this.getTransform(context, object, "firstperson_lefthand");

        if (firstperson_lefthand == ItemTransformVec3f.DEFAULT) {
            firstperson_lefthand = firstperson_righthand;
        }

        ItemTransformVec3f head = this.getTransform(context, object, "head");
        ItemTransformVec3f gui = this.getTransform(context, object, "gui");
        ItemTransformVec3f ground = this.getTransform(context, object, "ground");
        ItemTransformVec3f fixed = this.getTransform(context, object, "fixed");
        return new ItemCameraTransforms(thirdperson_lefthand, thirdperson_righthand, firstperson_lefthand, firstperson_righthand, head, gui, ground, fixed);
    }

    private ItemTransformVec3f getTransform(JsonDeserializationContext context, JsonObject object, String key) {
        return object.has(key) ? (ItemTransformVec3f) context.deserialize(object.get(key), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
    }
}

