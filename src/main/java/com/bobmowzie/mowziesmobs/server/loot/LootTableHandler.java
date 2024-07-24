package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LootTableHandler {
    // Mob drops
    public static final ResourceLocation FERROUS_WROUGHTNAUT = register("entities/ferrous_wroughtnaut");
    public static final ResourceLocation LANTERN = register("entities/lantern");
    public static final ResourceLocation NAGA = register("entities/naga");
    public static final ResourceLocation FOLIAATH = register("entities/foliaath");
    public static final ResourceLocation GROTTOL = register("entities/grottol");
    public static final ResourceLocation FROSTMAW = register("entities/frostmaw");
    public static final ResourceLocation UMVUTHANA_FURY = register("entities/umvuthana_fury");
    public static final ResourceLocation UMVUTHANA_MISERY = register("entities/umvuthana_misery");
    public static final ResourceLocation UMVUTHANA_BLISS = register("entities/umvuthana_bliss");
    public static final ResourceLocation UMVUTHANA_RAGE = register("entities/umvuthana_rage");
    public static final ResourceLocation UMVUTHANA_FEAR = register("entities/umvuthana_fear");
    public static final ResourceLocation UMVUTHANA_FAITH = register("entities/umvuthana_faith");
    public static final ResourceLocation UMVUTHI = register("entities/umvuthi");
    public static final ResourceLocation UMVUTHANA_GROVE_CHEST = register("chests/umvuthana_grove_chest");
    public static final ResourceLocation MONASTERY_CHEST = register("chests/monastery_chest");

    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTION_TYPE_REG = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, MowziesMobs.MODID);
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPE_REG = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MowziesMobs.MODID);

    public static RegistryObject<LootItemFunctionType> GROTTOL_DEATH_TYPE = registerFunction("grottol_death_type", new LootFunctionGrottolDeathType.FunctionSerializer());

    public static RegistryObject<LootItemConditionType> FROSTMAW_HAS_CRYSTAL = registerCondition("has_crystal", new LootConditionFrostmawHasCrystal.ConditionSerializer());

    private static ResourceLocation register(String id) {
        return new ResourceLocation(MowziesMobs.MODID, id);
    }

    private static RegistryObject<LootItemFunctionType> registerFunction(String name, Serializer<? extends LootItemFunction> serializer) {
        return LOOT_FUNCTION_TYPE_REG.register(name, () -> new LootItemFunctionType(serializer));
    }

    private static RegistryObject<LootItemConditionType> registerCondition(String registryName, Serializer<? extends LootItemCondition> serializer) {
        return LOOT_CONDITION_TYPE_REG.register(registryName, () -> new LootItemConditionType(serializer));
    }
}
