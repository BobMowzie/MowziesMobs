package com.bobmowzie.mowziesmobs.common;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.model.ModelBiped;

import com.bobmowzie.mowziesmobs.common.property.WroughtAxeSwingHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class ServerProxy
{
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new WroughtAxeSwingHandler());
    }

    public ModelBiped getArmorModel(int i)
    {
        return null;
    }
}
