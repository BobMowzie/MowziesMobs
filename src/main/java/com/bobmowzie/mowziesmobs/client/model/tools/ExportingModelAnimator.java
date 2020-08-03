package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.client.model.entity.MowzieEntityModel;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.Transform;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// quick and dirty
public class ExportingModelAnimator extends MMModelAnimator {
    static final DecimalFormat KEYFRAME_FORMAT = new DecimalFormat("0.0###");
    static final DecimalFormat VALUE_FORMAT = new DecimalFormat("0.#");

    final Map<ModelRenderer, String> bones = new HashMap<>();
    final Map<Animation, String> animations = new HashMap<>();
    JsonObject animationsJson;
    JsonObject bonesJson;
    Animation animation = IAnimatedEntity.NO_ANIMATION;
    String entityName = "";

    @Override
    public void setStaticKeyframe(int duration) {
        if (!correctAnimation) return;
        super.startKeyframe(duration);
        String start = KEYFRAME_FORMAT.format(this.startFrame / 20.0F);
        String end = KEYFRAME_FORMAT.format(this.endFrame / 20.0F);
        for (Map.Entry<ModelRenderer, Transform> entry : this.prevTransformMap.entrySet()) {
            AdvancedModelRenderer bone = (AdvancedModelRenderer) entry.getKey();
            String name = bones.get(bone);
            if (name == null) continue;
            Transform transform = entry.getValue();
            float rotationX = bone.rotateAngleX + transform.getRotationX() - bone.defaultRotationX;
            float rotationY = bone.rotateAngleY + transform.getRotationY() - bone.defaultRotationY;
            float rotationZ = bone.rotateAngleZ + transform.getRotationZ() - bone.defaultRotationZ;
            float positionX = bone.rotationPointX + transform.getOffsetX() - bone.defaultPositionX;
            float positionY = bone.rotationPointY + transform.getOffsetY() - bone.defaultPositionY;
            float positionZ = bone.rotationPointZ + transform.getOffsetZ() - bone.defaultPositionZ;
            addKeyframe(start, name, rotationX, rotationY, rotationZ, positionX, positionY, positionZ);
            addKeyframe(end, name, rotationX, rotationY, rotationZ, positionX, positionY, positionZ);
        }
        for (Map.Entry<ModelRenderer, String> entry : this.bones.entrySet()) {
            AdvancedModelRenderer bone = (AdvancedModelRenderer) entry.getKey();
            if (this.prevTransformMap.containsKey(bone)) continue;
            addDefault(start, this.bones.get(bone), bone);
            addDefault(end, this.bones.get(bone), bone);
        }
    }

    @Override
    public void endKeyframe() {
        if (!correctAnimation) return;
        String time = KEYFRAME_FORMAT.format(this.endFrame / 20.0F);
        for (Map.Entry<ModelRenderer, Transform> entry : this.transformMap.entrySet()) {
            AdvancedModelRenderer bone = (AdvancedModelRenderer) entry.getKey();
            String name = bones.get(bone);
            if (name == null) continue;
            Transform transform = entry.getValue();
            float rotationX = bone.rotateAngleX + transform.getRotationX() - bone.defaultRotationX;
            float rotationY = bone.rotateAngleY + transform.getRotationY() - bone.defaultRotationY;
            float rotationZ = bone.rotateAngleZ + transform.getRotationZ() - bone.defaultRotationZ;
            float positionX = bone.rotationPointX + transform.getOffsetX() - bone.defaultPositionX;
            float positionY = bone.rotationPointY + transform.getOffsetY() - bone.defaultPositionY;
            float positionZ = bone.rotationPointZ + transform.getOffsetZ() - bone.defaultPositionZ;
            addKeyframe(time, name, rotationX, rotationY, rotationZ, positionX, positionY, positionZ);
        }
        this.prevTransformMap.clear();
        this.prevTransformMap.putAll(this.transformMap);
        this.transformMap.clear();
    }

    void addKeyframe(String time, String bone, float rx, float ry, float rz, float tx, float ty, float tz) {
        JsonObject boneJson = getOrCreate(bonesJson, bone);
        JsonArray rotation = new JsonArray();

//        Matrix4d m = new Matrix4d();
//        m.setIdentity();
//        Matrix4d tmp = new Matrix4d();
//        tmp.set(new AxisAngle4d(0.0D, 0.0D, 1.0D, rz));
//        m.mul(tmp);
//        tmp.set(new AxisAngle4d(0.0D, 1.0D, 0.0D, ry));
//        m.mul(tmp);
//        tmp.set(new AxisAngle4d(1.0D, 0.0D, 0.0D, rx));
//        m.mul(tmp);
//        tmp.setZero();
//        tmp.m00 = -1.0D;
//        tmp.m11 = 1.0D;
//        tmp.m22 = -1.0D;
//        tmp.m33 = 1.0D;
//        m.mul(tmp);
//        Matrix3d rot = new Matrix3d();
//        m.get(rot);
//        double invX, invY, invZ;
//        if (rot.m20 < 1.0D) {
//            if (rot.m20 > -1.0D) {
//                invY = Math.asin(-rot.m20);
//                invZ = Math.atan2(rot.m10, rot.m00);
//                invX = Math.atan2(rot.m21, rot.m22);
//            } else {
//                invY = Math.PI / 2.0D;
//                invZ = -Math.atan2(-rot.m12, rot.m11);
//                invX = 0.0D;
//            }
//        } else {
//            invY = -Math.PI / 2.0D;
//            invZ = Math.atan2(-m.m12, m.m11);
//            invX = 0.0D;
//        }
//        if (rx != 0 || ry != 0 || rz != 0) {
//            LogManager.getLogger().info("{}, {}, {} -> {}, {}, {}", rx, ry, rz, invX, invY, invZ);
//        }
        rotation.add(formatAngle(rx));
        rotation.add(formatAngle(-ry));
        rotation.add(formatAngle(-rz));
        JsonObject rotationKeyframe = new JsonObject();
        rotationKeyframe.add("vector", rotation);
        rotationKeyframe.addProperty("easing", "easeOutSine");
        getOrCreate(boneJson, "rotation").add(time, rotation);
        JsonArray position = new JsonArray();
        position.add(formatNumber(tx));
        position.add(formatNumber(-ty));
        position.add(formatNumber(-tz));
        JsonObject positionKeyframe = new JsonObject();
        positionKeyframe.add("vector", position);
        positionKeyframe.addProperty("easing", "easeOutSine");
        getOrCreate(boneJson, "position").add(time, position);
    }

    double formatAngle(double value) {
        return formatNumber(Math.toDegrees(value));
    }

    double formatNumber(double value) {
        double d = Math.floor(value * 10.0D) / 10.0D;
        return d == -0.0D ? 0.0D : d;
    }

    static JsonObject getOrCreate(JsonObject obj, String name) {
        JsonObject member;
        if (obj.has(name)) {
            member = obj.getAsJsonObject(name);
        } else {
            obj.add(name, member = new JsonObject());
        }
        return member;
    }

    @Override
    public boolean setAnimation(Animation animation) {
        if (super.setAnimation(animation)) {
            this.animation = animation;
            String start = KEYFRAME_FORMAT.format(0.0D);
            for (Map.Entry<ModelRenderer, String> entry : this.bones.entrySet()) {
                AdvancedModelRenderer bone = (AdvancedModelRenderer) entry.getKey();
                this.addDefault(start, entry.getValue(), bone);
            }
            return true;
        }
        return false;
    }

    private void addDefault(String start, String name, AdvancedModelRenderer bone) {
        this.addKeyframe(start, name,
                bone.rotateAngleX - bone.defaultRotationX,
                bone.rotateAngleY - bone.defaultRotationY,
                bone.rotateAngleZ - bone.defaultRotationZ,
                bone.rotationPointX - bone.defaultPositionX,
                bone.rotationPointY - bone.defaultPositionY,
                bone.rotationPointZ - bone.defaultPositionZ
        );
    }

    @Override
    public void update(IAnimatedEntity entity, float delta) {
        super.update(entity, delta);
        this.bonesJson = new JsonObject();
    }

    public void addBones(MowzieEntityModel<?> model, MowzieEntity entity) {
        this.entityName = EntityList.getKey(entity).getPath();
        this.bones.clear();
        for (Field f : model.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()) && AdvancedModelRenderer.class.equals(f.getType())) {
                f.setAccessible(true);
                AdvancedModelRenderer bone;
                try {
                    bone = (AdvancedModelRenderer) f.get(model);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                this.bones.put(bone, f.getName());
            }
        }
        this.animations.clear();
        for (Field f : entity.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()) && Animation.class.equals(f.getType())) {
                f.setAccessible(true);
                Animation animation;
                try {
                    animation = (Animation) f.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                this.animations.put(animation, f.getName().replaceFirst("_ANIMATION$", "").toLowerCase(Locale.ROOT));
            }
        }
        this.animationsJson = new JsonObject();
    }

    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void endAnimation() {
        if (!correctAnimation) return;
        String time = KEYFRAME_FORMAT.format(this.endFrame / 20.0F);
        for (Map.Entry<ModelRenderer, String> entry : this.bones.entrySet()) {
            AdvancedModelRenderer bone = (AdvancedModelRenderer) entry.getKey();
            if (this.prevTransformMap.containsKey(bone)) continue;
            addDefault(time, entry.getValue(), bone);
        }
        String n = this.animations.get(this.animation);
        if (n != null) {
            JsonObject animJson = new JsonObject();
            animJson.addProperty("loop", false);
            animJson.addProperty("animation_length", Math.floor(this.endFrame / 20.0F * 1000.0F) / 1000.0F);
            for (String s : this.bones.values()) {
                JsonObject boneJson = this.bonesJson.getAsJsonObject(s);
                if (this.isZero(boneJson.getAsJsonObject("rotation"))) {
                    boneJson.remove("rotation");
                }
                if (this.isZero(boneJson.getAsJsonObject("position"))) {
                    boneJson.remove("position");
                }
            }
            animJson.add("bones", this.bonesJson);
            this.animationsJson.add(n, animJson);
        }
    }

    private boolean isZero(JsonObject frames) {
        for (Map.Entry<String, JsonElement> e : frames.entrySet()) {
            JsonArray arr = e.getValue().getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getAsDouble() != 0.0D) {
                    return false;
                }
            }
        }
        return true;
    }

    public void finish() {
        JsonObject json = new JsonObject();
        json.addProperty("format_version", "1.8.0");
        json.add("animations", this.animationsJson);
        try (final BufferedWriter out = Files.newBufferedWriter(Paths.get(this.entityName + ".json"))) {
            GSON.toJson(json, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
