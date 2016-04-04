package com.bobmowzie.mowziesmobs.common.potion;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.potion.Potion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public enum PotionHandler {
    INSTANCE;

    public Potion sunsBlessing;

    private void initPotionTypes() {
        try {
            Field potionTypesField = ReflectionHelper.findField(Potion.class, "potionTypes", "field_76425_a");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(potionTypesField, potionTypesField.getModifiers() & ~Modifier.FINAL);
            Potion[] potionTypes = (Potion[]) potionTypesField.get(null);
            final int targetLength = 256;
            if (potionTypes.length < targetLength) {
                Potion[] newPotionTypes = new Potion[targetLength];
                System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                potionTypesField.set(null, newPotionTypes);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerPotions() {
        sunsBlessing = new MowziePotionSunsBlessing(getFreePotionId());
    }

    private int getFreePotionId() {
        for (int id = 1; id < Potion.potionTypes.length; id++) {
            if (Potion.potionTypes[id] == null) {
                return id;
            }
        }
        return -1;
    }

    public void onInit() {
        initPotionTypes();
        registerPotions();
    }
}
