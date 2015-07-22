package com.bobmowzie.mowziesmobs.common.property;

import java.util.ArrayList;
import java.util.List;

import com.bobmowzie.mowziesmobs.common.item.MMItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class WroughtAxeSwingProperty implements IExtendedEntityProperties
{
    private static final String IDENTIFIER = "wroughtAxeSwing";

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

    public void swing()
    {
        time = 30;
    }

    public int getTime()
    {
        return time;
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
