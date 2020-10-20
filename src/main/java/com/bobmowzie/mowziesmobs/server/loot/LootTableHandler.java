package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.RegistryObject;

public class LootTableHandler {
    // Mob drops
    public static final ResourceLocation FERROUS_WROUGHTNAUT = null;//register("entities/ferrous_wroughtnaut");
    public static final ResourceLocation LANTERN = null;//register("entities/lantern");
    public static final ResourceLocation NAGA = null;//register("entities/naga");
    public static final ResourceLocation FOLIAATH = null;//register("entities/foliaath");
    public static final ResourceLocation GROTTOL = null;//register("entities/grottol");
    public static final ResourceLocation FROSTMAW = null;//register("entities/frostmaw");
    public static final ResourceLocation BARAKOA_FURY = null;//register("entities/barakoa_fury");
    public static final ResourceLocation BARAKOA_MISERY = null;//register("entities/barakoa_misery");
    public static final ResourceLocation BARAKOA_BLISS = null;//register("entities/barakoa_bliss");
    public static final ResourceLocation BARAKOA_RAGE = null;//register("entities/barakoa_rage");
    public static final ResourceLocation BARAKOA_FEAR = null;//register("entities/barakoa_fear");
    public static final ResourceLocation BARAKO = null;//register("entities/barako");// TODO

    // Mob misc

    // Entity properties
//    public static final ResourceLocation ENTITY_PROPERTY_FROSTMAW_HAS_CRYSTAL = register(new EntityPropertyFrostmawHasCrystal.Serializer());
//    public static final ResourceLocation ENTITY_PROPERTY_GROTTOL_DEATH_TYPE = register(new EntityPropertyGrottolDeathType.Serializer());

//    private static ResourceLocation register(String id) {
//        return LootTables.func_215796_a().register(new ResourceLocation(MowziesMobs.MODID, id));
//    }

//    private static ResourceLocation register(PropertyEnt.Serializer<?> serializer) {
//        EntityPropertyManager.registerProperty(serializer);
//        return serializer.getName();
//    }

    private static ResourceLocation register(ILootCondition.AbstractSerializer<?> serializer) {
        LootConditionManager.registerCondition(serializer);
        return serializer.getLootTableLocation();
    }

    private static ResourceLocation register(ILootFunction.Serializer<?> serializer) {
        LootFunctionManager.registerFunction(serializer);
        return serializer.getFunctionName();
    }
}
