package com.bobmowzie.mowziesmobs.common.potion;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MMPotions implements IContentHandler
{
    public static Potion sunsBlessing;

    /**
     * adds support for 256 different potions, hopefully this doesn't break mods
     **/
    public static void initPotionTypes()
    {
        try
        {
            Field potionTypesField = ReflectionHelper.findField(Potion.class, "potionTypes", "field_76425_a");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(potionTypesField, potionTypesField.getModifiers() & ~Modifier.FINAL);
            Potion[] potionTypes = (Potion[]) potionTypesField.get(null);
            final int targetLength = 256;
            if (potionTypes.length < targetLength)
            {
                Potion[] newPotionTypes = new Potion[targetLength];
                System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                potionTypesField.set(null, newPotionTypes);
            }
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public static void registerPotions()
    {
        sunsBlessing = new MMPotionSunsBlessing(getFreePotionId());
    }

    private static int getFreePotionId()
    {
        for (int id = 1; id < Potion.potionTypes.length; id++)
        {
            if (Potion.potionTypes[id] == null)
            {
                return id;
            }
        }
        return -1;
    }

    @Override
    public void init()
    {
        initPotionTypes();
        registerPotions();
    }

    @Override
    public void gameRegistry() throws Exception
    {
        MinecraftForge.EVENT_BUS.register(sunsBlessing);
    }
}
