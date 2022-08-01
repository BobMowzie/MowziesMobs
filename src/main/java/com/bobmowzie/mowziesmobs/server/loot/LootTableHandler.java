package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootTableHandler {
    // Mob drops
    public static final ResourceLocation FERROUS_WROUGHTNAUT = register("entities/ferrous_wroughtnaut");
    public static final ResourceLocation LANTERN = register("entities/lantern");
    public static final ResourceLocation NAGA = register("entities/naga");
    public static final ResourceLocation FOLIAATH = register("entities/foliaath");
    public static final ResourceLocation GROTTOL = register("entities/grottol");
    public static final ResourceLocation FROSTMAW = register("entities/frostmaw");
    public static final ResourceLocation BARAKOA_FURY = register("entities/barakoa_fury");
    public static final ResourceLocation BARAKOA_MISERY = register("entities/barakoa_misery");
    public static final ResourceLocation BARAKOA_BLISS = register("entities/barakoa_bliss");
    public static final ResourceLocation BARAKOA_RAGE = register("entities/barakoa_rage");
    public static final ResourceLocation BARAKOA_FEAR = register("entities/barakoa_fear");
    public static final ResourceLocation BARAKOA_FAITH = register("entities/barakoa_faith");
    public static final ResourceLocation BARAKO = register("entities/barako");
    public static final ResourceLocation BARAKOA_VILLAGE_HOUSE = register("chests/barakoa_village_house");

    public static LootItemFunctionType CHECK_FROSTMAW_CRYSTAL;
    public static LootItemFunctionType GROTTOL_DEATH_TYPE;

    public static LootItemConditionType FROSTMAW_HAS_CRYSTAL;

    public static void init() {
//        CHECK_FROSTMAW_CRYSTAL = registerFunction("mowziesmobs:has_crystal", new LootFunctionCheckFrostmawCrystal.Serializer());
        GROTTOL_DEATH_TYPE = registerFunction("mowziesmobs:grottol_death_type", new LootFunctionGrottolDeathType.FunctionSerializer());
        FROSTMAW_HAS_CRYSTAL = registerCondition("mowziesmobs:has_crystal", new LootConditionFrostmawHasCrystal.ConditionSerializer());
    }

    private static ResourceLocation register(String id) {
        return new ResourceLocation(MowziesMobs.MODID, id);
    }

    private static LootItemFunctionType registerFunction(String name, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(name), new LootItemFunctionType(serializer));
    }

    private static LootItemConditionType registerCondition(String registryName, Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(registryName), new LootItemConditionType(serializer));
    }
}
