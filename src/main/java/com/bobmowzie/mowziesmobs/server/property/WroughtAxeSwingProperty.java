package com.bobmowzie.mowziesmobs.server.property;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class WroughtAxeSwingProperty implements IExtendedEntityProperties {
    private static final String IDENTIFIER = "wroughtAxeSwing";

    private static final int SWING_LENGTH = 20;
    public static final int SWING_HIT_TICK = 10;

    private int prevTime;
    private int time;

    @Override
    public void saveNBTData(NBTTagCompound compound) {

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {

    }

    @Override
    public void init(Entity entity, World world) {

    }

    public void update() {
        prevTime = time;
    }

    public void swing() {
        time = SWING_LENGTH;
    }

    public int getTick() {
        return time;
    }

    public float getSwingPercentage(float partialRenderTicks) {
        return (prevTime + (time - prevTime) * partialRenderTicks) / SWING_LENGTH;
    }

    public void decrementTime() {
        time--;
    }

    public static WroughtAxeSwingProperty getProperty(EntityPlayer player) {
        IExtendedEntityProperties properties = player.getExtendedProperties(IDENTIFIER);
        if (!(properties instanceof WroughtAxeSwingProperty)) {
            properties = new WroughtAxeSwingProperty();
            player.registerExtendedProperties(IDENTIFIER, properties);
        }
        return (WroughtAxeSwingProperty) properties;
    }

    public static float fnc1(float x) {
        return x * ((45 - 27 * x) * x - 18);
    }

    public static float fnc2(float x) {
        return MathHelper.sin(x * (float) Math.PI);
    }

    public static float fnc3(float x, float incline, float decline, float steepness) {
        return (float) (1 / (1 + Math.exp(-steepness * (x - incline))) - (1 / (1 + Math.exp(-steepness * (x - decline)))));
    }
}
