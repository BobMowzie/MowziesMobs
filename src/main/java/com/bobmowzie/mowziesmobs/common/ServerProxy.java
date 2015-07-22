package com.bobmowzie.mowziesmobs.common;

import net.minecraft.client.model.ModelBiped;

import com.bobmowzie.mowziesmobs.common.property.WroughtAxeSwingHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class ServerProxy
{
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new WroughtAxeSwingHandler());
    }

    public ModelBiped getArmorModel()
    {
        return null;
    }
}
