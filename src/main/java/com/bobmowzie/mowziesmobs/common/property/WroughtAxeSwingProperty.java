package com.bobmowzie.mowziesmobs.common.property;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class WroughtAxeSwingProperty implements IExtendedEntityProperties
{
    private static final String IDENTIFIER = "wroughtAxeSwing";

    private int prevTime;
    private int time;

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
    }

    @Override
    public void init(Entity entity, World world)
    {
    }

    public void update()
    {
        prevTime = time;
    }

    public void swing()
    {
        time = 30;
    }

    public int getTime()
    {
        return time;
    }

    public float getTime(float partialRenderTicks)
    {
        return prevTime + (time - prevTime) * partialRenderTicks;
    }

    public void decrementTime()
    {
        time -= 2;
    }

    public static WroughtAxeSwingProperty getProperty(EntityPlayer player)
    {
        IExtendedEntityProperties properties = player.getExtendedProperties(IDENTIFIER);
        if (!(properties instanceof WroughtAxeSwingProperty))
        {
            properties = new WroughtAxeSwingProperty();
            player.registerExtendedProperties(IDENTIFIER, properties);
        }
        return (WroughtAxeSwingProperty) properties;
    }

    public static float fnc1(float x)
    {
        return -9*x*(x-1)*(3*x-2);
    }

    public static float fnc2(float x)
    {
        return (float) Math.sin(x*Math.PI);
    }

    public static float fnc3(float x, float incline, float decline, float steepness)
    {
        return (float) (1 / (1 + Math.exp(-steepness*(x - incline))) - (1 / (1 + Math.exp(-steepness*(x - decline)))));
    }
}
