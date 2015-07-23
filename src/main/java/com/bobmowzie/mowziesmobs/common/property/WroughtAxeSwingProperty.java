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
        time--;
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
}
