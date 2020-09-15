package com.ilexiconn.llibrary.client.model.qubble.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gegy1000
 * @since 1.7.5
 */
public class QubbleVanillaTexture implements INBTSerializable<NBTTagCompound> {
    private String texture;
    private Map<String, String> properties = new HashMap<>();

    private QubbleVanillaTexture() {
    }

    public static QubbleVanillaTexture create(String value) {
        QubbleVanillaTexture texture = new QubbleVanillaTexture();
        texture.texture = value;
        return texture;
    }

    public static QubbleVanillaTexture deserialize(NBTTagCompound compound) {
        QubbleVanillaTexture texture = new QubbleVanillaTexture();
        texture.deserializeNBT(compound);
        return texture;
    }

    public String getTexture() {
        return this.texture;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public String getProperty(String key) {
        return this.properties.get(key);
    }

    public boolean getBoolean(String key) {
        String property = this.getProperty(key);
        return property != null && property.equals("true");
    }

    public void setProperty(String key, String value) {
        this.properties.put(key, value);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("texture", this.texture);
        NBTTagList propertyList = new NBTTagList();
        for (Map.Entry<String, String> entry : this.properties.entrySet()) {
            NBTTagCompound property = new NBTTagCompound();
            property.setString("key", entry.getKey());
            property.setString("value", entry.getValue());
            propertyList.appendTag(property);
        }
        compound.setTag("properties", propertyList);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.texture = compound.getString("texture");
        NBTTagList propertyList = compound.getTagList("properties", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < propertyList.tagCount(); i++) {
            NBTTagCompound property = propertyList.getCompoundTagAt(i);
            if (property.hasKey("key") && property.hasKey("value")) {
                this.properties.put(property.getString("key"), property.getString("value"));
            }
        }
    }

    public QubbleVanillaTexture copy() {
        QubbleVanillaTexture texture = QubbleVanillaTexture.create(this.texture);
        for (Map.Entry<String, String> entry : this.properties.entrySet()) {
            texture.setProperty(entry.getKey(), entry.getValue());
        }
        return texture;
    }
}
