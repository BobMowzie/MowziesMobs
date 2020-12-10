package com.bobmowzie.mowziesmobs.server.config;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import javax.tools.Tool;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class  ConfigHandler {
    private ConfigHandler() {}

    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";

    public static final General GENERAL;
    public static final ToolsAndAbilities TOOLS_AND_ABILITIES;
    public static final Mobs MOBS;
    public static final Client CLIENT;

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        GENERAL = new General(COMMON_BUILDER);
        TOOLS_AND_ABILITIES = new ToolsAndAbilities(COMMON_BUILDER);
        MOBS = new Mobs(COMMON_BUILDER);
        CLIENT = new Client(CLIENT_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }


    // Config templates
    public static class BiomeConfig {
        BiomeConfig(final ForgeConfigSpec.Builder builder, List<String> biomeTypes, List<String> biomeWhitelist, List<String> biomeBlacklist) {
            builder.push("biome_config");
            this.biomeTypes = builder.comment("Each entry is a combination of allowed biome types.", "Separate types with commas to require biomes to have all types in an entry", "Put a '!' before a biome type to mean NOT that type", "A blank entry means all biomes. No entries means no biomes.", "For example, 'FOREST,MAGICAL,!SNOWY' would mean all biomes that are magical forests but not snowy", "'!MOUNTAIN' would mean all non-mountain biomes")
                    .translation(LANG_PREFIX + "biome_type")
                    .define("biome_type", biomeTypes);
            this.biomeWhitelist = builder.comment("Allow spawns in these biomes regardless of the biome type settings")
                    .translation(LANG_PREFIX + "biome_whitelist")
                    .define("biome_whitelist", biomeWhitelist);
            this.biomeBlacklist = builder.comment("Prevent spawns in these biomes regardless of the biome type settings")
                    .translation(LANG_PREFIX + "biome_blacklist")
                    .define("biome_blacklist", biomeBlacklist);
            builder.pop();
        }

        public final ConfigValue<List<String>> biomeTypes;

        public final ConfigValue<List<String>> biomeWhitelist;

        public final ConfigValue<List<String>> biomeBlacklist;
    }

    public static class SpawnConfig {
        SpawnConfig(final ForgeConfigSpec.Builder builder, int spawnRate, int minGroupSize, int maxGroupSize, BiomeConfig biomeConfig, List<String> allowedBlocks, int heightMax, int heightMin, boolean needsDarkness, boolean needsSeeSky, boolean needsCantSeeSky) {
            builder.comment("Controls for vanilla-style mob spawning");
            builder.push("spawn_config");
            this.spawnRate = builder.comment("Smaller number causes less spawning, 0 to disable spawning")
                    .translation(LANG_PREFIX + "spawn_rate")
                    .define("spawn_rate", spawnRate);
            this.minGroupSize = builder.comment("Minimum number of mobs that appear in a spawn group")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("min_group_size", minGroupSize);
            this.maxGroupSize = builder.comment("Maximum number of mobs that appear in a spawn group")
                    .translation(LANG_PREFIX + "max_group_size")
                    .define("max_group_size", maxGroupSize);
            this.biomeConfig = biomeConfig;
            this.dimensions = builder.comment("Names of dimensions this mob can spawn in")
                    .translation(LANG_PREFIX + "dimensions")
                    .define("dimensions", Arrays.asList("minecraft:overworld"));
            this.allowedBlocks = builder.comment("Names of blocks this mob is allowed to spawn on. Leave blank to allow any block.")
                    .translation(LANG_PREFIX + "allowed_blocks")
                    .define("allowed_blocks", allowedBlocks);
            this.heightMax = builder.comment("Maximum height for this spawn. -1 to ignore.")
                    .translation(LANG_PREFIX + "height_max")
                    .define("height_max", heightMax);
            this.heightMin = builder.comment("Minimum height for this spawn. -1 to ignore.")
                    .translation(LANG_PREFIX + "height_min")
                    .define("height_min", heightMin);
            this.needsDarkness = builder.comment("Set to true to only allow this mob to spawn in the dark, like zombies and skeletons.")
                    .translation(LANG_PREFIX + "needs_darkness")
                    .define("needs_darkness", needsDarkness);
            this.needsSeeSky = builder.comment("Set to true to only spawn mob if it can see the sky.")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("needs_see_sky", needsSeeSky);
            this.needsCantSeeSky = builder.comment("Set to true to only spawn mob if it can't see the sky.")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("needs_cant_see_sky", needsCantSeeSky);
            builder.pop();
        }

        public final ConfigValue<Integer> spawnRate;

        public final ConfigValue<Integer> minGroupSize;

        public final ConfigValue<Integer> maxGroupSize;

        public final BiomeConfig biomeConfig;

        public final ConfigValue<List<String>> dimensions;

        public final ConfigValue<Integer> heightMin;

        public final ConfigValue<Integer> heightMax;

        public final ConfigValue<Boolean> needsDarkness;

        public final ConfigValue<Boolean> needsSeeSky;

        public final ConfigValue<Boolean> needsCantSeeSky;

        public final ConfigValue<List<String>> allowedBlocks;
    }

    public static class GenerationConfig {
        GenerationConfig(final ForgeConfigSpec.Builder builder, int generationFrequency, float generationChance, BiomeConfig biomeConfig, float heightMin, float heightMax) {
            builder.comment("Controls for spawning structure/mob with world generation");
            builder.push("generation_config");
            this.generationFrequency = builder.comment("Smaller number causes more generation, 0 to disable spawning", "Maximum number of chunks between placements of this mob/structure")
                    .translation(LANG_PREFIX + "generation_frequency")
                    .define("generation_frequency", generationFrequency);
            this.generationChance = builder.comment("Probability that generation succeeds.", "For example, set to 0.5 to randomly not generate half of these structures in the world.", "Set to 1 to allow all generation attempts to succeed.")
                    .translation(LANG_PREFIX + "generation_chance")
                    .define("generation_chance", generationChance);
            this.biomeConfig = biomeConfig;
            this.dimensions = builder.comment("Names of dimensions this mob/structure can generate in")
                    .translation(LANG_PREFIX + "dimensions")
                    .define("dimensions", Arrays.asList("minecraft:overworld"));
            this.heightMax = builder.comment("Maximum height for generation placement. -1 to ignore")
                    .translation(LANG_PREFIX + "height_max")
                    .define("height_max", heightMax);
            this.heightMin = builder.comment("Minimum height for generation placement. -1 to ignore")
                    .translation(LANG_PREFIX + "height_min")
                    .define("height_min", heightMin);
            builder.pop();
        }

        public final ConfigValue<Integer> generationFrequency;

        public final ConfigValue<Float> generationChance;

        public final BiomeConfig biomeConfig;

        public final ConfigValue<List<String>> dimensions;

        public final ConfigValue<Float> heightMin;

        public final ConfigValue<Float> heightMax;
    }

    public static class CombatConfig {
        CombatConfig(final ForgeConfigSpec.Builder builder, float healthMultiplier, float attackMultiplier) {
            builder.push("combat_config");
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .define("health_multiplier", healthMultiplier);
            this.attackMultiplier = builder.comment("Scale mob attack damage by this value")
                    .translation(LANG_PREFIX + "attack_multiplier")
                    .define("attack_multiplier", attackMultiplier);
            builder.pop();
        }

        public final ConfigValue<Float> healthMultiplier;

        public final ConfigValue<Float> attackMultiplier;
    }

    public static class ToolConfig {
        ToolConfig(final ForgeConfigSpec.Builder builder, float attackDamage, float attackSpeed) {
            builder.push("tool_config");
            this.attackDamage = builder.comment("Tool attack damage")
                    .translation(LANG_PREFIX + "attack_damage")
                    .define("attack_damage", attackDamage);
            this.attackSpeed = builder.comment("Tool attack speed")
                    .translation(LANG_PREFIX + "attack_speed")
                    .define("attack_speed", attackSpeed);
            builder.pop();
        }

        public final ConfigValue<Float> attackDamage;

        public final ConfigValue<Float> attackSpeed;
    }

    public static class ArmorConfig {
        ArmorConfig(final ForgeConfigSpec.Builder builder, int damageReduction, float toughness) {
            builder.push("armor_config");
            this.damageReduction = builder.comment("See official Minecraft Wiki for an explanation of how armor damage reduction works.")
                    .translation(LANG_PREFIX + "damage_reduction")
                    .define("damage_reduction", damageReduction);
            this.toughness = builder.comment("See official Minecraft Wiki for an explanation of how armor toughness works.")
                    .translation(LANG_PREFIX + "toughness")
                    .define("toughness", toughness);
            builder.pop();
        }

        public final ConfigValue<Integer> damageReduction;

        public final ConfigValue<Float> toughness;
    }

    // Mob configuration
    public static class Foliaath {
        Foliaath(final ForgeConfigSpec.Builder builder) {
            builder.push("foliaath");
            spawnConfig = new SpawnConfig(builder,
                    20, 1, 3,
                    new BiomeConfig(builder, Arrays.asList("JUNGLE"), new ArrayList<>(), new ArrayList<>()),
                    Arrays.asList("grass_block", "jungle_leaves", "oak_leaves", "oak_log", "jungle_log"),
                    -1, 60, false, false, false
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
                    4, 1, 1,
                    new BiomeConfig(builder, Arrays.asList("SAVANNA"), new ArrayList<>(), new ArrayList<>()),
                    Arrays.asList("grass", "stone", "sand"),
                    -1, 60, false, false, false
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
                    3, 1, 3,
                    new BiomeConfig(builder, Arrays.asList("BEACH,MOUNTAIN", "BEACH,HILLS"),  Arrays.asList("stone_beach"), new ArrayList<>()),
                    Arrays.asList("grass", "stone", "sand"),
                    -1, -1, false, true, false
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
                    5, 1, 2,
                    new BiomeConfig(builder, Arrays.asList("FOREST,MAGICAL,!SNOWY"),  Arrays.asList("roofed_forest", "mutated_roofed_forest"), new ArrayList<>()),
                    Arrays.asList("grass", "leaves", "leaves2", "log", "log2"),
                    -1, 60, true, false, false
            );
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .define("health_multiplier", 1.0f);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final ConfigValue<Float> healthMultiplier;
    }

    public static class Grottol {
        Grottol(final ForgeConfigSpec.Builder builder) {
            builder.push("grottol");
            this.spawnConfig = new SpawnConfig(builder,
                    2, 1, 1,
                    new BiomeConfig(builder,  Arrays.asList(""), new ArrayList<>(), new ArrayList<>()),
                    new ArrayList<>(),
                    55, -1, false, false, true
            );
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .define("health_multiplier", 1.0f);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final ConfigValue<Float> healthMultiplier;
    }

    public static class FerrousWroughtnaut {
        FerrousWroughtnaut(final ForgeConfigSpec.Builder builder) {
            builder.push("ferrous_wroughtnaut");
            generationConfig = new GenerationConfig(builder, 2, 0.5f,
                    new BiomeConfig(builder,  Arrays.asList(""), new ArrayList<>(), new ArrayList<>()),
                    30, 55
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
        public final ConfigValue<Boolean> hasBossBar;

        public final ConfigValue<Boolean> healsOutOfBattle;
    }

    public static class Barako {
        Barako(final ForgeConfigSpec.Builder builder) {
            builder.push("barako");
            builder.comment("Generation controls for Barakoa villages");
            generationConfig = new GenerationConfig(builder, 12, 0.8f,
                    new BiomeConfig(builder,  Arrays.asList("SAVANNA"), new ArrayList<>(), new ArrayList<>()),
                    50, 100
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Barako's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable Barako healing while not in combat")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final ConfigValue<Boolean> hasBossBar;

        public final ConfigValue<Boolean> healsOutOfBattle;
    }

    public static class Frostmaw {
        Frostmaw(final ForgeConfigSpec.Builder builder) {
            builder.push("frostmaw");
            generationConfig = new GenerationConfig(builder, 12, 0.8f,
                    new BiomeConfig(builder,  Arrays.asList("SNOWY,!OCEAN,!RIVER,!BEACH"), new ArrayList<>(), new ArrayList<>()),
                    50, 100
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

        public final ConfigValue<Boolean> stealableIceCrystal;

        public final ConfigValue<Boolean> hasBossBar;

        public final ConfigValue<Boolean> healsOutOfBattle;
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

        public final ConfigValue<Boolean> breakable;
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

        public final ConfigValue<Boolean> breakable;
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

        public final ConfigValue<Boolean> breakable;
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
                    .define("attack_multiplier", 1f);
            breakable = builder.comment("Set to true for the ice crystal to have limited durability.", "Prevents regeneration in inventory.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            durability = builder.comment("Ice crystal durability")
                    .translation(LANG_PREFIX + "durability")
                    .define("durability", 600);
            builder.pop();
        }

        public final ConfigValue<Float> attackMultiplier;

        public final ConfigValue<Boolean> breakable;

        public final ConfigValue<Integer> durability;
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
                    .define("poison_duration", 40);
            bonusDamage = builder.comment("Bonus damage when attacking from behind")
                    .translation(LANG_PREFIX + "bonus_damage")
                    .define("bonus_damage", 3f);
            builder.pop();
        }

        public final ToolConfig toolConfig;

        public final ConfigValue<Integer> poisonDuration;

        public final ConfigValue<Float> bonusDamage;
    }

    public static class Blowgun {
        Blowgun(final ForgeConfigSpec.Builder builder) {
            builder.push("blowgun");
            poisonDuration = builder.comment("Duration in ticks of the poison effect (20 ticks = 1 second).")
                    .translation(LANG_PREFIX + "poison_duration")
                    .define("poison_duration", 40);
            attackDamage = builder.comment("Multiply all damage done with the blowgun/darts by this amount.")
                    .translation(LANG_PREFIX + "attack_damage")
                    .define("attack_damage", 1f);
            builder.pop();
        }

        public final ConfigValue<Float> attackDamage;

        public final ConfigValue<Integer> poisonDuration;
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
            sunsBlessingAttackMultiplier = builder.translation(LANG_PREFIX + "suns_blessing_attack_multiplier")
                    .define("suns_blessing_attack_multiplier", 1f);
            geomancyAttackMultiplier = builder.translation(LANG_PREFIX + "geomancy_attack_multiplier")
                    .define("geomancy_attack_multiplier", 1f);
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

        public final ConfigValue<Float> sunsBlessingAttackMultiplier;

        public final ConfigValue<Float> geomancyAttackMultiplier;

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
        public final ConfigValue<List<String>> freeze_blacklist;

        private General(final ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.freeze_blacklist = builder.comment("Add a mob's full name here to prevent it from being frozen or taking damage from ice magic.")
                    .translation(LANG_PREFIX + "freeze_blacklist")
                    .define("freeze_blacklist", Arrays.asList("mowziesmobs:frostmaw", "minecraft:enderdragon"));
            builder.pop();
        }
    }

    public static class Client {
        private Client(final ForgeConfigSpec.Builder builder) {
            builder.push("client");
            this.glowEffect = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "glow_effect")
                    .define("glow_effect", true);
            builder.pop();
        }

        public final ConfigValue<Boolean> glowEffect;
    }
}
