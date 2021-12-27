package com.bobmowzie.mowziesmobs.server.config;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class  ConfigHandler {
    private ConfigHandler() {}

    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";

    public static final Common COMMON;
    public static final Client CLIENT;

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;
    private static final Predicate<Object> ITEM_NAME_PREDICATE = STRING_PREDICATE.and(s -> ForgeRegistries.ITEMS.containsKey(new ResourceLocation((String)s)));

    static {
        COMMON = new Common(COMMON_BUILDER);
        CLIENT = new Client(CLIENT_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    // Config templates
    public static class BiomeConfig {
        BiomeConfig(final ForgeConfigSpec.Builder builder, List<? extends String> biomeTypes, List<? extends String> biomeWhitelist, List<? extends String> biomeBlacklist) {
            builder.push("biome_config");
            this.biomeTypes = builder.comment("Each entry is a combination of allowed biome types.", "Separate types with commas to require biomes to have all types in an entry", "Put a '!' before a biome type to mean NOT that type", "A blank entry means all biomes. No entries means no biomes.", "For example, 'FOREST,MAGICAL,!SNOWY' would mean all biomes that are magical forests but not snowy", "'!MOUNTAIN' would mean all non-mountain biomes")
                    .translation(LANG_PREFIX + "biome_type")
                    .defineList("biome_type", biomeTypes, STRING_PREDICATE);
            this.biomeWhitelist = builder.comment("Allow spawns in these biomes regardless of the biome type settings")
                    .translation(LANG_PREFIX + "biome_whitelist")
                    .defineList("biome_whitelist", biomeWhitelist, STRING_PREDICATE);
            this.biomeBlacklist = builder.comment("Prevent spawns in these biomes regardless of the biome type settings")
                    .translation(LANG_PREFIX + "biome_blacklist")
                    .defineList("biome_blacklist", biomeBlacklist, STRING_PREDICATE);
            builder.pop();
        }

        public final ConfigValue<List<? extends String>> biomeTypes;

        public final ConfigValue<List<? extends String>> biomeWhitelist;

        public final ConfigValue<List<? extends String>> biomeBlacklist;
    }

    public static class SpawnConfig {
        SpawnConfig(final ForgeConfigSpec.Builder builder, int spawnRate, int minGroupSize, int maxGroupSize, double extraRarity, BiomeConfig biomeConfig, List<? extends String> allowedBlocks, List<? extends String> allowedBlockTags, int heightMax, int heightMin, boolean needsDarkness, boolean needsSeeSky, boolean needsCantSeeSky, List<String> avoidStructures) {
            builder.comment("Controls for vanilla-style mob spawning");
            builder.push("spawn_config");
            this.spawnRate = builder.comment("Smaller number causes less spawning, 0 to disable spawning")
                    .translation(LANG_PREFIX + "spawn_rate")
                    .defineInRange("spawn_rate", spawnRate, 0, Integer.MAX_VALUE);
            this.minGroupSize = builder.comment("Minimum number of mobs that appear in a spawn group")
                    .translation(LANG_PREFIX + "min_group_size")
                    .defineInRange("min_group_size", minGroupSize, 1, Integer.MAX_VALUE);
            this.maxGroupSize = builder.comment("Maximum number of mobs that appear in a spawn group")
                    .translation(LANG_PREFIX + "max_group_size")
                    .defineInRange("max_group_size", maxGroupSize, 1, Integer.MAX_VALUE);
            this.extraRarity = builder.comment("Probability of a spawn attempt succeeding. 1 for normal spawning, 0 will prevent spawning. Used to make mobs extra rare.")
                    .translation(LANG_PREFIX + "extra_rarity")
                    .defineInRange("extra_rarity", extraRarity, 0.0, 1.0);
            this.biomeConfig = biomeConfig;
            this.dimensions = builder.comment("Names of dimensions this mob can spawn in")
                    .translation(LANG_PREFIX + "dimensions")
                    .defineList("dimensions", Collections.singletonList("minecraft:overworld"), STRING_PREDICATE);
            this.allowedBlocks = builder.comment("Names of blocks this mob is allowed to spawn on. Leave blank to ignore block names.")
                    .translation(LANG_PREFIX + "allowed_blocks")
                    .defineList("allowed_blocks", allowedBlocks, STRING_PREDICATE);
            this.allowedBlockTags = builder.comment("Tags of blocks this mob is allowed to spawn on. Leave blank to ignore block tags.")
                    .translation(LANG_PREFIX + "allowed_block_tags")
                    .defineList("allowed_block_tags", allowedBlockTags, STRING_PREDICATE);
            this.heightMax = builder.comment("Maximum height for this spawn. -1 to ignore.")
                    .translation(LANG_PREFIX + "height_max")
                    .defineInRange("height_max", heightMax, -1, 256);
            this.heightMin = builder.comment("Minimum height for this spawn. -1 to ignore.")
                    .translation(LANG_PREFIX + "height_min")
                    .defineInRange("height_min", heightMin, -1, 256);
            this.needsDarkness = builder.comment("Set to true to only allow this mob to spawn in the dark, like zombies and skeletons.")
                    .translation(LANG_PREFIX + "needs_darkness")
                    .define("needs_darkness", needsDarkness);
            this.needsSeeSky = builder.comment("Set to true to only spawn mob if it can see the sky.")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("needs_see_sky", needsSeeSky);
            this.needsCantSeeSky = builder.comment("Set to true to only spawn mob if it can't see the sky.")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("needs_cant_see_sky", needsCantSeeSky);
            this.avoidStructures = builder.comment("Names of structures this mob will avoid spawning near.")
                    .translation(LANG_PREFIX + "avoid_structures")
                    .defineList("avoid_structures", avoidStructures, STRING_PREDICATE);
            builder.pop();
        }

        public final IntValue spawnRate;

        public final IntValue minGroupSize;

        public final IntValue maxGroupSize;

        public final DoubleValue extraRarity;

        public final BiomeConfig biomeConfig;

        public final ConfigValue<List<? extends String>> dimensions;

        public final IntValue heightMin;

        public final IntValue heightMax;

        public final BooleanValue needsDarkness;

        public final BooleanValue needsSeeSky;

        public final BooleanValue needsCantSeeSky;

        public final ConfigValue<List<? extends String>> allowedBlocks;

        public final ConfigValue<List<? extends String>> allowedBlockTags;

        public final ConfigValue<List<? extends String>> avoidStructures;
    }

    public static class GenerationConfig {
        GenerationConfig(final ForgeConfigSpec.Builder builder, int generationDistance, int generationSeparation, BiomeConfig biomeConfig, float heightMin, float heightMax, List<String> avoidStructures) {
            builder.comment("Controls for spawning structure/mob with world generation");
            builder.push("generation_config");
            this.generationDistance = builder.comment("Smaller number causes more generation, -1 to disable generation", "Maximum number of chunks between placements of this mob/structure")
                    .translation(LANG_PREFIX + "generation_distance")
                    .defineInRange("generation_distance", generationDistance, -1, Integer.MAX_VALUE);
            this.generationSeparation = builder.comment("Smaller number causes more generation, -1 to disable generation", "Minimum number of chunks between placements of this mob/structure")
                    .translation(LANG_PREFIX + "generation_separation")
                    .defineInRange("generation_separation", generationSeparation, -1, Integer.MAX_VALUE);
            this.biomeConfig = biomeConfig;
            this.dimensions = builder.comment("Names of dimensions this mob/structure can generate in")
                    .translation(LANG_PREFIX + "dimensions")
                    .defineList("dimensions", Collections.singletonList("minecraft:overworld"), STRING_PREDICATE);
            this.heightMax = builder.comment("Maximum height for generation placement. -1 to ignore")
                    .translation(LANG_PREFIX + "height_max")
                    .defineInRange("height_max", heightMax, -1, 256);
            this.heightMin = builder.comment("Minimum height for generation placement. -1 to ignore")
                    .translation(LANG_PREFIX + "height_min")
                    .defineInRange("height_min", heightMin, -1, 256);
            this.avoidStructures = builder.comment("Names of structures this mob/structure will avoid when generating")
                    .translation(LANG_PREFIX + "avoid_structures")
                    .defineList("avoid_structures", avoidStructures, STRING_PREDICATE);
            builder.pop();
        }

        public final IntValue generationDistance;

        public final IntValue generationSeparation;

        public final BiomeConfig biomeConfig;

        public final ConfigValue<List<? extends String>> dimensions;

        public final DoubleValue heightMin;

        public final DoubleValue heightMax;

        public final ConfigValue<List<? extends String>> avoidStructures;
    }

    public static class CombatConfig {
        CombatConfig(final ForgeConfigSpec.Builder builder, float healthMultiplier, float attackMultiplier) {
            builder.push("combat_config");
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .defineInRange("health_multiplier", healthMultiplier, 0d, Double.MAX_VALUE);
            this.attackMultiplier = builder.comment("Scale mob attack damage by this value")
                    .translation(LANG_PREFIX + "attack_multiplier")
                    .defineInRange("attack_multiplier", attackMultiplier, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue healthMultiplier;

        public final DoubleValue attackMultiplier;
    }

    public static class ToolConfig {
        ToolConfig(final ForgeConfigSpec.Builder builder, float attackDamage, float attackSpeed) {
            builder.push("tool_config");
            this.attackDamage = builder.comment("Tool attack damage")
                    .translation(LANG_PREFIX + "attack_damage")
                    .defineInRange("attack_damage", attackDamage, 0d, Double.MAX_VALUE);
            this.attackSpeed = builder.comment("Tool attack speed")
                    .translation(LANG_PREFIX + "attack_speed")
                    .defineInRange("attack_speed", attackSpeed, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue attackDamage;

        public final DoubleValue attackSpeed;
    }

    public static class ArmorConfig {
        ArmorConfig(final ForgeConfigSpec.Builder builder, int damageReduction, float toughness) {
            builder.push("armor_config");
            this.damageReduction = builder.comment("See official Minecraft Wiki for an explanation of how armor damage reduction works.")
                    .translation(LANG_PREFIX + "damage_reduction")
                    .defineInRange("damage_reduction", damageReduction, 0, Integer.MAX_VALUE);
            this.toughness = builder.comment("See official Minecraft Wiki for an explanation of how armor toughness works.")
                    .translation(LANG_PREFIX + "toughness")
                    .defineInRange("toughness", toughness, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final IntValue damageReduction;

        public final DoubleValue toughness;
    }

    // Mob configuration
    public static class Foliaath {
        Foliaath(final ForgeConfigSpec.Builder builder) {
            builder.push("foliaath");
            spawnConfig = new SpawnConfig(builder,
                    70, 1, 4, 1,
                    new BiomeConfig(builder, Collections.singletonList("JUNGLE"), new ArrayList<>(), new ArrayList<>()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"),
                    -1, 60, false, false, false,
                    Arrays.asList("minecraft:village", "minecraft:pillager_outpost")

            );
            combatConfig = new CombatConfig(builder, 1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Barakoa {
        Barakoa(final ForgeConfigSpec.Builder builder) {
            builder.push("barakoa");
            builder.comment("Controls spawning for Barakoana hunting groups", "Group size controls how many elites spawn, not followers", "See Barako config for village controls");
            spawnConfig = new SpawnConfig(builder,
                    5, 1, 1, 1,
                    new BiomeConfig(builder, Collections.singletonList("SAVANNA"), new ArrayList<>(), new ArrayList<>()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:sand"),
                    -1, 60, false, false, false,
                    Arrays.asList("minecraft:village", "minecraft:pillager_outpost", "mowziesmobs:barakoa_village")
            );
            combatConfig = new CombatConfig(builder,1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Naga {
        Naga(final ForgeConfigSpec.Builder builder) {
            builder.push("naga");
            spawnConfig = new SpawnConfig(builder,
                    15, 2, 4, 1,
                    new BiomeConfig(builder, Arrays.asList("BEACH,MOUNTAIN", "BEACH,HILLS"), Collections.singletonList("minecraft:stone_shore"), new ArrayList<>()),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    -1, 70, false, true, false,
                    Arrays.asList("minecraft:village", "minecraft:pillager_outpost")
            );
            combatConfig = new CombatConfig(builder,1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Lantern {
        Lantern(final ForgeConfigSpec.Builder builder) {
            builder.push("lantern");
            spawnConfig = new SpawnConfig(builder,
                    5, 2, 4, 1,
                    new BiomeConfig(builder, Collections.singletonList("FOREST,MAGICAL,!SNOWY"), Arrays.asList("minecraft:dark_forest", "minecraft:dark_forest_hills"), new ArrayList<>()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"),
                    -1, 60, true, false, false,
                    Collections.emptyList()
            );
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .defineInRange("health_multiplier", 1.0f, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final DoubleValue healthMultiplier;
    }

    public static class Grottol {
        Grottol(final ForgeConfigSpec.Builder builder) {
            builder.push("grottol");
            this.spawnConfig = new SpawnConfig(builder,
                    2, 1, 1, 1,
                    new BiomeConfig(builder,  Collections.singletonList("!MUSHROOM"), new ArrayList<>(), new ArrayList<>()),
                    Collections.emptyList(),
                    Collections.singletonList("minecraft:base_stone_overworld"),
                    25, -1, true, false, true,
                    Collections.emptyList()
            );
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .defineInRange("health_multiplier", 1.0f, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final DoubleValue healthMultiplier;
    }

    public static class FerrousWroughtnaut {
        FerrousWroughtnaut(final ForgeConfigSpec.Builder builder) {
            builder.push("ferrous_wroughtnaut");
            generationConfig = new GenerationConfig(builder, 15, 5,
                    new BiomeConfig(builder,  Arrays.asList("!OCEAN"), new ArrayList<>(), new ArrayList<>()),
                    30, 55,
                    Collections.emptyList()
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Ferrous Wroughtnauts' boss health bars")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable Ferrous Wroughtnaut healing while not active")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;
        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;
    }

    public static class Barako {
        Barako(final ForgeConfigSpec.Builder builder) {
            builder.push("barako");
            builder.comment("Generation controls for Barakoa villages");
            generationConfig = new GenerationConfig(builder, 25, 8,
                    new BiomeConfig(builder,  Arrays.asList("SAVANNA"), new ArrayList<>(), new ArrayList<>()),
                    50, 100,
                    Arrays.asList("minecraft:village", "minecraft:pillager_outpost")
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Barako's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable Barako healing while not in combat")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.whichItem = builder.comment("Which item Barako desires in exchange for the Sun's Blessing")
                    .translation(LANG_PREFIX + "trade_which_item")
                    .define("trade_which_item", "minecraft:gold_block", ITEM_NAME_PREDICATE);
            this.howMany = builder.comment("How many of the item Barako desires in exchange for the Sun's Blessing")
                    .translation(LANG_PREFIX + "trade_how_many")
                    .defineInRange("trade_how_many", 7, 0, 64);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;

        public final ConfigValue<? extends String> whichItem;

        public final IntValue howMany;
    }

    public static class Frostmaw {
        Frostmaw(final ForgeConfigSpec.Builder builder) {
            builder.push("frostmaw");
            generationConfig = new GenerationConfig(builder, 25, 8,
                    new BiomeConfig(builder,  Arrays.asList("SNOWY,!OCEAN,!RIVER,!BEACH,!FOREST"), new ArrayList<>(), new ArrayList<>()),
                    50, 100,
                    Arrays.asList("minecraft:village", "minecraft:pillager_outpost")
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Barako's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable frostmaws healing while asleep")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.stealableIceCrystal = builder.comment("Allow players to steal frostmaws' ice crystals (only using specific means!)")
                    .translation(LANG_PREFIX + "stealable_ice_crystal")
                    .define("stealable_ice_crystal", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final BooleanValue stealableIceCrystal;

        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;
    }

    public static class WroughtHelm {
        WroughtHelm(final ForgeConfigSpec.Builder builder) {
            builder.push("wrought_helm");
            armorConfig = new ArmorConfig(builder, ArmorMaterial.IRON.getDamageReductionAmount(EquipmentSlotType.HEAD), ArmorMaterial.IRON.getToughness());
            breakable = builder.comment("Set to true for the Wrought Helm to have limited durability.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            builder.pop();
        }

        public final ArmorConfig armorConfig;

        public final BooleanValue breakable;
    }

    public static class AxeOfAThousandMetals {
        AxeOfAThousandMetals(final ForgeConfigSpec.Builder builder) {
            builder.push("axe_of_a_thousand_metals");
            toolConfig = new ToolConfig(builder, 9, 0.9f);
            breakable = builder.comment("Set to true for the Axe of a Thousand Metals to have limited durability.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            builder.pop();
        }

        public final ToolConfig toolConfig;

        public final BooleanValue breakable;
    }

    public static class SolVisage {
        SolVisage(final ForgeConfigSpec.Builder builder) {
            builder.push("sol_visage");
            armorConfig = new ArmorConfig(builder, ArmorMaterial.GOLD.getDamageReductionAmount(EquipmentSlotType.HEAD), ArmorMaterial.GOLD.getToughness());
            breakable = builder.comment("Set to true for the Sol Visage to have limited durability.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            builder.pop();
        }

        public final ArmorConfig armorConfig;

        public final BooleanValue breakable;
    }

    public static class BarakoaMask {
        BarakoaMask(final ForgeConfigSpec.Builder builder) {
            builder.push("barakoa_mask");
            armorConfig = new ArmorConfig(builder, ArmorMaterial.LEATHER.getDamageReductionAmount(EquipmentSlotType.HEAD), ArmorMaterial.LEATHER.getToughness());
            builder.pop();
        }

        public final ArmorConfig armorConfig;
    }

    public static class IceCrystal {
        IceCrystal(final ForgeConfigSpec.Builder builder) {
            builder.push("ice_crystal");
            attackMultiplier = builder.comment("Multiply all damage done with the ice crystal by this amount.")
                    .translation(LANG_PREFIX + "attack_multiplier")
                    .defineInRange("attack_multiplier", 1f, 0d, Double.MAX_VALUE);
            breakable = builder.comment("Set to true for the ice crystal to have limited durability.", "Prevents regeneration in inventory.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            durability = builder.comment("Ice crystal durability")
                    .translation(LANG_PREFIX + "durability")
                    .defineInRange("durability", 600, 1, Integer.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue attackMultiplier;

        public final BooleanValue breakable;

        public final IntValue durability;
    }

    public static class BarakoaSpear {
        BarakoaSpear(final ForgeConfigSpec.Builder builder) {
            builder.push("barakoa_spear");
            toolConfig = new ToolConfig(builder, 5, 1.6f);
            builder.pop();
        }

        public final ToolConfig toolConfig;
    }

    public static class NagaFangDagger {
        NagaFangDagger(final ForgeConfigSpec.Builder builder) {
            builder.push("naga_fang_dagger");
            toolConfig = new ToolConfig(builder, 3, 2);
            poisonDuration = builder.comment("Duration in ticks of the poison effect (20 ticks = 1 second).")
                    .translation(LANG_PREFIX + "poison_duration")
                    .defineInRange("poison_duration", 40, 0, Integer.MAX_VALUE);
            backstabDamageMultiplier = builder.comment("Damage multiplier when attacking from behind")
                    .translation(LANG_PREFIX + "backstab_damage_mult")
                    .defineInRange("backstab_damage_mult", 2f, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final ToolConfig toolConfig;

        public final IntValue poisonDuration;

        public final DoubleValue backstabDamageMultiplier;
    }

    public static class Blowgun {
        Blowgun(final ForgeConfigSpec.Builder builder) {
            builder.push("blowgun");
            poisonDuration = builder.comment("Duration in ticks of the poison effect (20 ticks = 1 second).")
                    .translation(LANG_PREFIX + "poison_duration")
                    .defineInRange("poison_duration", 40, 0, Integer.MAX_VALUE);
            attackDamage = builder.comment("Multiply all damage done with the blowgun/darts by this amount.")
                    .translation(LANG_PREFIX + "attack_damage")
                    .defineInRange("attack_damage", 1d, 0, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue attackDamage;

        public final IntValue poisonDuration;
    }

    public static class SunsBlessing {
        SunsBlessing(final ForgeConfigSpec.Builder builder) {
            builder.push("suns_blessing");
            effectDuration = builder.comment("Duration in minutes of the Sun's Blessing effect.")
                    .translation(LANG_PREFIX + "suns_blessing_duration")
                    .defineInRange("suns_blessing_duration", 60, 0, Integer.MAX_VALUE);
            sunsBlessingAttackMultiplier = builder.translation(LANG_PREFIX + "suns_blessing_attack_multiplier")
                    .defineInRange("suns_blessing_attack_multiplier", 1f, 0, Double.MAX_VALUE);
            solarBeamCost = builder.comment("Cost in minutes of using the solar beam ability.")
                    .translation(LANG_PREFIX + "solar_beam_cost")
                    .defineInRange("solar_beam_cost", 5, 0, Integer.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue sunsBlessingAttackMultiplier;

        public final IntValue effectDuration;

        public final IntValue solarBeamCost;
    }

    public static class Mobs {
        Mobs(final ForgeConfigSpec.Builder builder) {
            builder.push("mobs");
            FROSTMAW = new Frostmaw(builder);
            BARAKO = new Barako(builder);
            FERROUS_WROUGHTNAUT = new FerrousWroughtnaut(builder);
            GROTTOL = new Grottol(builder);
            LANTERN = new Lantern(builder);
            BARAKOA = new Barakoa(builder);
            NAGA = new Naga(builder);
            FOLIAATH = new Foliaath(builder);
            builder.pop();
        }

        public final Frostmaw FROSTMAW;

        public final Barako BARAKO;

        public final FerrousWroughtnaut FERROUS_WROUGHTNAUT;

        public final Grottol GROTTOL;

        public final Lantern LANTERN;

        public final Barakoa BARAKOA;

        public final Naga NAGA;

        public final Foliaath FOLIAATH;
    }

    public static class ToolsAndAbilities {
        ToolsAndAbilities(final ForgeConfigSpec.Builder builder) {
            builder.push("tools_and_abilities");
            geomancyAttackMultiplier = builder.translation(LANG_PREFIX + "geomancy_attack_multiplier")
                    .defineInRange("geomancy_attack_multiplier", 1f, 0, Double.MAX_VALUE);
            SUNS_BLESSING = new SunsBlessing(builder);
            WROUGHT_HELM = new WroughtHelm(builder);
            AXE_OF_A_THOUSAND_METALS = new AxeOfAThousandMetals(builder);
            SOL_VISAGE = new SolVisage(builder);
            ICE_CRYSTAL = new IceCrystal(builder);
            BARAKOA_MASK = new BarakoaMask(builder);
            BARAKOA_SPEAR = new BarakoaSpear(builder);
            NAGA_FANG_DAGGER = new NagaFangDagger(builder);
            BLOW_GUN = new Blowgun(builder);
            builder.pop();
        }

        public final DoubleValue geomancyAttackMultiplier;

        public final SunsBlessing SUNS_BLESSING;

        public final WroughtHelm WROUGHT_HELM;

        public final AxeOfAThousandMetals AXE_OF_A_THOUSAND_METALS;

        public final SolVisage SOL_VISAGE;

        public final IceCrystal ICE_CRYSTAL;

        public final BarakoaMask BARAKOA_MASK;

        public final BarakoaSpear BARAKOA_SPEAR;

        public final NagaFangDagger NAGA_FANG_DAGGER;

        public final Blowgun BLOW_GUN;
    }

    public static class General {
        public final ConfigValue<List<? extends String>> freeze_blacklist;

        private General(final ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.freeze_blacklist = builder.comment("Add a mob's full name here to prevent it from being frozen or taking damage from ice magic.")
                    .translation(LANG_PREFIX + "freeze_blacklist")
                    .defineList("freeze_blacklist", Arrays.asList("mowziesmobs:frostmaw", "minecraft:enderdragon", "minecraft:blaze", "minecraft:magma_cube", "minecraft:stray", "minecraft:polar_bear", "minecraft:snow_golem"), STRING_PREDICATE);
            builder.pop();
        }
    }

    public static class Client {
        private Client(final ForgeConfigSpec.Builder builder) {
            builder.push("client");
            this.glowEffect = builder.comment("Toggles the lantern glow effect, which may look bad with certain shaders")
                    .translation(LANG_PREFIX + "glow_effect")
                    .define("glow_effect", true);
            this.oldBarakoaTextures = builder.comment("Use the old Barakoa textures instead of the current ones")
                    .translation(LANG_PREFIX + "old_barakoa_textures")
                    .define("old_barakoa_textures", false);
            this.doCameraShakes = builder.comment("Enable camera shaking during certain mob attacks and abilities.")
                    .translation(LANG_PREFIX + "do_camera_shake")
                    .define("do_camera_shake", true);
            builder.pop();
        }

        public final BooleanValue glowEffect;

        public final BooleanValue oldBarakoaTextures;

        public final BooleanValue doCameraShakes;
    }

    public static class Common {
        private Common(final ForgeConfigSpec.Builder builder) {
            GENERAL = new General(builder);
            TOOLS_AND_ABILITIES = new ToolsAndAbilities(builder);
            MOBS = new Mobs(builder);
        }

        public final General GENERAL;

        public final ToolsAndAbilities TOOLS_AND_ABILITIES;

        public final Mobs MOBS;
    }
}
