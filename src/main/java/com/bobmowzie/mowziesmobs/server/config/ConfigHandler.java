package com.bobmowzie.mowziesmobs.server.config;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ConfigHandler {
    private ConfigHandler() {}

    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";

    public static final Common COMMON;
    public static final Client CLIENT;

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;
    private static final Predicate<Object> RESOURCE_LOCATION_PREDICATE = STRING_PREDICATE.and(s -> ResourceLocation.isValidResourceLocation((String) s));
    private static final Predicate<Object> BIOME_COMBO_PREDICATE = STRING_PREDICATE.and(s -> {
        String bigString = (String) s;
        String[] typeStrings = bigString.replace(" ", "").split("[,!]");
        for (String string : typeStrings) {
            if (!RESOURCE_LOCATION_PREDICATE.test(string)) {
                return false;
            }
        }
        return true;
    });
    private static final Predicate<Object> ITEM_NAME_PREDICATE = RESOURCE_LOCATION_PREDICATE.and(s -> ForgeRegistries.ITEMS.containsKey(new ResourceLocation((String) s)));

    static {
        COMMON = new Common(COMMON_BUILDER);
        CLIENT = new Client(CLIENT_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    // Config templates
    public static class BiomeConfig {
        BiomeConfig(final ForgeConfigSpec.Builder builder, List<? extends String> biomeTags, List<? extends String> biomeWhitelist, List<? extends String> biomeBlacklist) {
            builder.push("biome_config");
            builder.comment("Mowzie's Mobs bosses cannot generate in modded or non-overworld biomes unless the biome is added to the 'has_structure/has_mowzie_structure' tag via a datapack!");
            this.biomeTags = builder.comment("Each entry is a combination of allowed biome tags or biome names.", "Separate types with commas to require biomes to have all tags in an entry", "Put a '!' before a biome tag to mean NOT that tag", "A blank entry means all biomes. No entries means no biomes.", "For example, 'minecraft:is_forest,forge:is_spooky,!forge:is_snowy' would mean all biomes that are spooky forests but not snowy forests", "'!minecraft:is_mountain' would mean all non-mountain biomes")
                    .translation(LANG_PREFIX + "biome_tags")
                    .defineList("biome_tags", biomeTags, BIOME_COMBO_PREDICATE);
            this.biomeWhitelist = builder.comment("Allow spawns in these biomes regardless of the biome tag settings")
                    .translation(LANG_PREFIX + "biome_whitelist")
                    .defineList("biome_whitelist", biomeWhitelist, BIOME_COMBO_PREDICATE);
            this.biomeBlacklist = builder.comment("Prevent spawns in these biomes regardless of the biome tag settings")
                    .translation(LANG_PREFIX + "biome_blacklist")
                    .defineList("biome_blacklist", biomeBlacklist, BIOME_COMBO_PREDICATE);
            builder.pop();
        }

        public final ConfigValue<List<? extends String>> biomeTags;

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
            this.heightMax = builder.comment("Maximum height for this spawn. -65 to ignore.")
                    .translation(LANG_PREFIX + "height_max")
                    .defineInRange("height_max", heightMax, -65, 256);
            this.heightMin = builder.comment("Minimum height for this spawn. -65 to ignore.")
                    .translation(LANG_PREFIX + "height_min")
                    .defineInRange("height_min", heightMin, -65, 256);
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
            this.generationDistance = builder.comment("Smaller number causes more generation, -1 to disable generation", "Maximum number of chunks between placements of this mob/structure.", "NO LONGER USED! USE DATAPACK INSTEAD")
                    .translation(LANG_PREFIX + "generation_distance")
                    .defineInRange("generation_distance", generationDistance, -1, Integer.MAX_VALUE);
            this.generationSeparation = builder.comment("Smaller number causes more generation, -1 to disable generation", "Minimum number of chunks between placements of this mob/structure.", "NO LONGER USED! USE DATAPACK INSTEAD")
                    .translation(LANG_PREFIX + "generation_separation")
                    .defineInRange("generation_separation", generationSeparation, -1, Integer.MAX_VALUE);
            this.biomeConfig = biomeConfig;
            this.heightMax = builder.comment("Maximum height for generation placement. -65 to ignore")
                    .translation(LANG_PREFIX + "height_max")
                    .defineInRange("height_max", heightMax, -65, 256);
            this.heightMin = builder.comment("Minimum height for generation placement. -65 to ignore")
                    .translation(LANG_PREFIX + "height_min")
                    .defineInRange("height_min", heightMin, -65, 256);
            this.avoidStructures = builder.comment("Names of structures this mob/structure will avoid when generating.", "NO LONGER USED! USE DATAPACK INSTEAD")
                    .translation(LANG_PREFIX + "avoid_structures")
                    .defineList("avoid_structures", avoidStructures, STRING_PREDICATE);
            builder.pop();
        }

        public final IntValue generationDistance;

        public final IntValue generationSeparation;

        public final BiomeConfig biomeConfig;

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
        
        public float attackDamageValue = 9;
        public float attackSpeedValue = 0.9F;

        public final DoubleValue attackSpeed;
    }

    public static class ArmorConfig {
        ArmorConfig(final ForgeConfigSpec.Builder builder) {
            builder.push("armor_config");
            this.damageReductionMultiplier = builder.comment("Multiply armor damage reduction by this amount. See official Minecraft Wiki for an explanation of how armor damage reduction works.")
                    .translation(LANG_PREFIX + "damage_reduction_multiplier")
                    .defineInRange("damage_reduction_multiplier", 1.0f, 0d, Double.MAX_VALUE);
            this.toughnessMultiplier = builder.comment("Multiply armor toughness by this amount. See official Minecraft Wiki for an explanation of how armor toughness works.")
                    .translation(LANG_PREFIX + "toughness_multiplier")
                    .defineInRange("toughness_multiplier", 1.0f, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue damageReductionMultiplier;
        public final DoubleValue toughnessMultiplier;

        public float damageReductionMultiplierValue = ArmorMaterials.IRON.getDefenseForType(ArmorItem.Type.HELMET);
        public float toughnessMultiplierValue = ArmorMaterials.IRON.getToughness();
    }

    // Mob configuration
    public static class Foliaath {
        Foliaath(final ForgeConfigSpec.Builder builder) {
            builder.push("foliaath");
            spawnConfig = new SpawnConfig(builder,
                    70, 1, 4, 1,
                    new BiomeConfig(builder, Collections.singletonList("minecraft:is_jungle"), Collections.emptyList(), Collections.emptyList()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"),
                    -65, 60, true, false, false,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts")

            );
            combatConfig = new CombatConfig(builder, 1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Umvuthana {
        Umvuthana(final ForgeConfigSpec.Builder builder) {
            builder.push("umvuthana");
            builder.comment("Controls spawning for Umvuthana hunting groups", "Group size controls how many raptors spawn, not followers", "See Umvuthi config for grove structure controls");
            spawnConfig = new SpawnConfig(builder,
                    5, 1, 1, 1,
                    new BiomeConfig(builder, Collections.singletonList("minecraft:is_savanna"), Collections.emptyList(), Collections.emptyList()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:sand"),
                    -65, 60, false, false, false,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts", "mowziesmobs:umvuthana_groves")
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
                    13, 2, 3, 1,
                    new BiomeConfig(builder, Arrays.asList("minecraft:is_beach,minecraft:is_mountain", "minecraft:is_beach,minecraft:is_hill"), Collections.singletonList("minecraft:stony_shore"), Collections.emptyList()),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    -65, 70, false, true, false,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts")
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
                    new BiomeConfig(builder, Collections.singletonList("minecraft:is_forest,mowziesmobs:is_magical,!forge:is_snowy"), Collections.emptyList(), Collections.emptyList()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"),
                    -65, 60, true, false, false,
                    Collections.emptyList()
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Grottol {
        Grottol(final ForgeConfigSpec.Builder builder) {
            builder.push("grottol");
            this.spawnConfig = new SpawnConfig(builder,
                    2, 1, 1, 1,
                    new BiomeConfig(builder,  Collections.singletonList("!forge:is_mushroom"), Collections.emptyList(), Collections.emptyList()),
                    Collections.emptyList(),
                    Collections.singletonList("minecraft:base_stone_overworld"),
                    16, -65, true, false, true,
                    Collections.emptyList()
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class FerrousWroughtnaut {
        FerrousWroughtnaut(final ForgeConfigSpec.Builder builder) {
            builder.push("ferrous_wroughtnaut");
            generationConfig = new GenerationConfig(builder, 15, 5,
                    new BiomeConfig(builder, Collections.singletonList("!minecraft:is_ocean"), Collections.emptyList(), Collections.emptyList()),
                    20, 50,
                    Collections.emptyList()
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Ferrous Wroughtnaut's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable Ferrous Wroughtnaut healing while not active")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.resetHealthWhenRespawn = builder.comment("Disable/enable Ferrous Wroughtnaut resetting health when a player respawns nearby. (Prevents respawn cheese!)")
                    .translation(LANG_PREFIX + "reset_health_when_respawn")
                    .define("reset_health_when_respawn", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;
        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;

        public final BooleanValue resetHealthWhenRespawn;
    }

    public static class Umvuthi {
        Umvuthi(final ForgeConfigSpec.Builder builder) {
            builder.push("umvuthi");
            builder.comment("Generation controls for Umvuthana Groves");
            generationConfig = new GenerationConfig(builder, 25, 8,
                    new BiomeConfig(builder, Collections.singletonList("minecraft:is_savanna"), Collections.emptyList(), Collections.emptyList()),
                    50, 100,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts")
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Umvuthi's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable Umvuthi healing while not in combat")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.whichItem = builder.comment("Which item Umvuthi desires in exchange for the Sun's Blessing")
                    .translation(LANG_PREFIX + "trade_which_item")
                    .define("trade_which_item", "minecraft:gold_block", ITEM_NAME_PREDICATE);
            this.howMany = builder.comment("How many of the item Umvuthi desires in exchange for the Sun's Blessing")
                    .translation(LANG_PREFIX + "trade_how_many")
                    .defineInRange("trade_how_many", 7, 0, 64);
            this.resetHealthWhenRespawn = builder.comment("Disable/enable Umvuthi resetting health when a player respawns nearby. (Prevents respawn cheese!)")
                    .translation(LANG_PREFIX + "reset_health_when_respawn")
                    .define("reset_health_when_respawn", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;

        public final ConfigValue<? extends String> whichItem;

        public final IntValue howMany;

        public final BooleanValue resetHealthWhenRespawn;
    }

    public static class Frostmaw {
        Frostmaw(final ForgeConfigSpec.Builder builder) {
            builder.push("frostmaw");
            generationConfig = new GenerationConfig(builder, 25, 8,
                    new BiomeConfig(builder, Collections.singletonList("forge:is_snowy,!minecraft:is_ocean,!minecraft:is_river,!minecraft:is_beach,!minecraft:is_forest,!minecraft:is_taiga"), Collections.emptyList(), Collections.emptyList()),
                    50, 100,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts")
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Frostmaw's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable frostmaws healing while asleep")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.stealableIceCrystal = builder.comment("Allow players to steal frostmaws' ice crystals (only using specific means!)")
                    .translation(LANG_PREFIX + "stealable_ice_crystal")
                    .define("stealable_ice_crystal", true);
            this.resetHealthWhenRespawn = builder.comment("Disable/enable frostmaws resetting health when a player respawns nearby. (Prevents respawn cheese!)")
                    .translation(LANG_PREFIX + "reset_health_when_respawn")
                    .define("reset_health_when_respawn", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final BooleanValue stealableIceCrystal;

        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;

        public final BooleanValue resetHealthWhenRespawn;
    }

    public static class Sculptor {
        Sculptor(final ForgeConfigSpec.Builder builder) {
            builder.push("sculptor");
            generationConfig = new GenerationConfig(builder, 25, 8,
                    new BiomeConfig(builder, Collections.singletonList("minecraft:is_mountain"), Collections.emptyList(), Collections.emptyList()),
                    120, 200,
                    Collections.emptyList()
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.testHeight = builder.comment("How tall (in blocks) the Sculptor's test will be")
                    .translation(LANG_PREFIX + "test_height")
                    .defineInRange("test_height", 50, 1, 500);
            this.testTimeLimit = builder.comment("The time limit (in seconds) for completing the Sculptor's test")
                    .translation(LANG_PREFIX + "test_time_limit")
                    .defineInRange("test_time_limit", 360, 1, 36000);
            this.healsOutOfBattle = builder.comment("Disable/enable the Sculptor healing while not in combat")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.hasBossBar = builder.comment("Disable/enable the Sculptor's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.whichItem = builder.comment("Which item the Sculptor desires in exchange for a chance to try his challenge")
                    .translation(LANG_PREFIX + "trade_which_item")
                    .define("trade_which_item", "minecraft:crossbow", ITEM_NAME_PREDICATE);
            this.howMany = builder.comment("How many of the item the Sculptor desires in exchange for a chance to try his challenge")
                    .translation(LANG_PREFIX + "trade_how_many")
                    .defineInRange("trade_how_many", 1, 0, 64);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final IntValue testHeight;

        public final IntValue testTimeLimit;

        public final BooleanValue healsOutOfBattle;

        public final BooleanValue hasBossBar;

        public final ConfigValue<? extends String> whichItem;

        public final IntValue howMany;
    }

    public static class WroughtHelm {
        WroughtHelm(final ForgeConfigSpec.Builder builder) {
            builder.push("wrought_helm");
            armorConfig = new ArmorConfig(builder);
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
            armorConfig = new ArmorConfig(builder);
            breakable = builder.comment("Set to true for the Sol Visage to have limited durability.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            maxFollowers = builder.comment("Maximum number of Umvuthana followers a player can summon at once using the Sol Visage")
                    .translation(LANG_PREFIX + "max_followers")
                    .defineInRange("max_followers", 10, 0, 300);
            builder.pop();
        }

        public final ArmorConfig armorConfig;

        public final BooleanValue breakable;

        public final IntValue maxFollowers;
    }

    public static class UmvuthanaMask {
        UmvuthanaMask(final ForgeConfigSpec.Builder builder) {
            builder.push("umvuthana_mask");
            armorConfig = new ArmorConfig(builder);
            builder.pop();
        }

        public final ArmorConfig armorConfig;
    }

    public static class GeomancerArmor {
        GeomancerArmor(final ForgeConfigSpec.Builder builder) {
            builder.push("geomancerArmor");
            armorConfig = new ArmorConfig(builder);
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
        public int durabilityValue;
    }

    public static class EarthrendGauntlet {
        EarthrendGauntlet(final ForgeConfigSpec.Builder builder) {
            builder.push("earthrend_gauntlet");
            attackMultiplier = builder.comment("Multiply all damage done with the Earthrend Gauntlet by this amount.")
                    .translation(LANG_PREFIX + "attack_multiplier")
                    .defineInRange("attack_multiplier", 1f, 0d, Double.MAX_VALUE);
            breakable = builder.comment("Set to true for the Earthrend Gauntlet to have limited durability.", "Prevents regeneration in inventory.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            durability = builder.comment("Earthrend Gauntlet durability")
                    .translation(LANG_PREFIX + "durability")
                    .defineInRange("durability", 400, 1, Integer.MAX_VALUE);
            enableTunneling = builder.comment("Set to false to disable the Earthrend Gauntlet's tunneling ability.")
                    .translation(LANG_PREFIX + "enable_tunneling")
                    .define("enable_tunneling", false);
            toolConfig = new ToolConfig(builder, 6, 1.2f);
            builder.pop();
        }

        public final DoubleValue attackMultiplier;

        public final BooleanValue breakable;

        public final IntValue durability;
        public int durabilityValue;

        public final ToolConfig toolConfig;

        public final BooleanValue enableTunneling;
    }

    public static class Spear {
        Spear(final ForgeConfigSpec.Builder builder) {
            builder.push("spear");
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
            
            supernovaCost = builder.comment("Cost in minutes of using the supernova ability.")
                    .translation(LANG_PREFIX + "supernova_cost")
                    .defineInRange("supernova_cost", 60, 0, Integer.MAX_VALUE);
        }

        public final DoubleValue sunsBlessingAttackMultiplier;

        public final IntValue effectDuration;

        public final IntValue solarBeamCost;

        public final IntValue supernovaCost;
    }

    public static class Mobs {
        Mobs(final ForgeConfigSpec.Builder builder) {
            builder.push("mobs");
            FROSTMAW = new Frostmaw(builder);
            UMVUTHI = new Umvuthi(builder);
            FERROUS_WROUGHTNAUT = new FerrousWroughtnaut(builder);
            SCULPTOR = new Sculptor(builder);
            GROTTOL = new Grottol(builder);
            LANTERN = new Lantern(builder);
            UMVUTHANA = new Umvuthana(builder);
            NAGA = new Naga(builder);
            FOLIAATH = new Foliaath(builder);
            builder.pop();
        }

        public final Frostmaw FROSTMAW;

        public final Umvuthi UMVUTHI;

        public final FerrousWroughtnaut FERROUS_WROUGHTNAUT;

        public final Sculptor SCULPTOR;

        public final Grottol GROTTOL;

        public final Lantern LANTERN;

        public final Umvuthana UMVUTHANA;

        public final Naga NAGA;

        public final Foliaath FOLIAATH;
    }

    public static class ToolsAndAbilities {
        ToolsAndAbilities(final ForgeConfigSpec.Builder builder) {
            builder.push("tools_and_abilities");
            SUNS_BLESSING = new SunsBlessing(builder);
            WROUGHT_HELM = new WroughtHelm(builder);
            AXE_OF_A_THOUSAND_METALS = new AxeOfAThousandMetals(builder);
            SOL_VISAGE = new SolVisage(builder);
            ICE_CRYSTAL = new IceCrystal(builder);
            UMVUTHANA_MASK = new UmvuthanaMask(builder);
            GEOMANCER_ARMOR = new GeomancerArmor(builder);
            SPEAR = new Spear(builder);
            NAGA_FANG_DAGGER = new NagaFangDagger(builder);
            BLOW_GUN = new Blowgun(builder);
            EARTHREND_GAUNTLET = new EarthrendGauntlet(builder);
            builder.pop();
        }

        public final SunsBlessing SUNS_BLESSING;

        public final WroughtHelm WROUGHT_HELM;

        public final AxeOfAThousandMetals AXE_OF_A_THOUSAND_METALS;

        public final SolVisage SOL_VISAGE;

        public final IceCrystal ICE_CRYSTAL;

        public final UmvuthanaMask UMVUTHANA_MASK;

        public final GeomancerArmor GEOMANCER_ARMOR;

        public final Spear SPEAR;

        public final NagaFangDagger NAGA_FANG_DAGGER;

        public final Blowgun BLOW_GUN;

        public final EarthrendGauntlet EARTHREND_GAUNTLET;
    }

    public static class Client {
        private Client(final ForgeConfigSpec.Builder builder) {
            builder.push("client");
            this.glowEffect = builder.comment("Toggles the lantern glow effect, which may look bad with certain shaders.")
                    .translation(LANG_PREFIX + "glow_effect")
                    .define("glow_effect", true);
            this.umvuthanaFootprints = builder.comment("Toggles the Umvuthana footprint effects, which may decrease performance.")
                    .translation(LANG_PREFIX + "umvuthana_footprints")
                    .define("umvuthana_footprints", true);
            this.doCameraShakes = builder.comment("Enable camera shaking during certain mob attacks and abilities.")
                    .translation(LANG_PREFIX + "do_camera_shake")
                    .define("do_camera_shake", true);
            this.playBossMusic = builder.comment("Play boss battle themes during boss encounters.")
                    .translation(LANG_PREFIX + "play_boss_music")
                    .define("play_boss_music", true);
            this.customBossBars = builder.comment("Use custom boss health bar textures, if the boss has them.")
                    .translation(LANG_PREFIX + "custom_boss_bar")
                    .define("custom_boss_bar", true);
            this.customPlayerAnims = builder.comment("Use custom player animations.")
                    .translation(LANG_PREFIX + "custom_player_anims")
                    .define("custom_player_anims", true);
            this.doUmvuthanaCraneHealSound = builder.comment("Play Umvuthana Crane heal sounds. Turn this off if you are experiencing crashes when Cranes appear during Umvuthi's boss battle.")
                    .translation(LANG_PREFIX + "crane_heal_sounds")
                    .define("crane_heal_sounds", true);
            builder.pop();
        }

        public final BooleanValue glowEffect;

        public final BooleanValue umvuthanaFootprints;

        public final BooleanValue doCameraShakes;

        public final BooleanValue playBossMusic;

        public final BooleanValue customBossBars;

        public final BooleanValue customPlayerAnims;

        public final BooleanValue doUmvuthanaCraneHealSound;
    }

    public static class Common {
        private Common(final ForgeConfigSpec.Builder builder) {
            TOOLS_AND_ABILITIES = new ToolsAndAbilities(builder);
            MOBS = new Mobs(builder);
        }

        public final ToolsAndAbilities TOOLS_AND_ABILITIES;

        public final Mobs MOBS;
    }
}
