package com.bobmowzie.mowziesmobs.common.property;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class WroughtAxeSwingProperty implements IExtendedEntityProperties
{
    private static final String IDENTIFIER = "wroughtAxeSwing";

    private float time;

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

    public void swing()
    {
        time = 30;
    }

    public float getTime()
    {
        return time;
    }

    public void decrementTime()
    {
        time -= 1.5;
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
