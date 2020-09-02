package com.bobmowzie.mowziesmobs.server.config;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

import java.io.File;

@Config(modid = MowziesMobs.MODID, category = "", name = MowziesMobs.MODID)
public class ConfigHandler {
    @Ignore
    public static File configDir;

    @Ignore
    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";

    // Config templates
    public static class BiomeData {
        BiomeData(String[] biomeTypes, String[] biomeWhitelist, String[] biomeBlacklist) {
            this.biomeTypes = biomeTypes;
            this.biomeWhitelist = biomeWhitelist;
            this.biomeBlacklist = biomeBlacklist;
        }

        @Name("biome_type")
        @LangKey(LANG_PREFIX + "biome_type")
        @Comment({"Each entry is a combination of allowed biome types.", "Separate types with commas to require biomes to have all types in an entry", "Put a '!' before a biome type to mean NOT that type", "A blank entry means all biomes. No entries means no biomes.", "For example, 'FOREST,MAGICAL,!SNOWY' would mean all biomes that are magical forests but not snowy", "'!MOUNTAIN' would mean all non-mountain biomes"})
        @RequiresMcRestart
        public String[] biomeTypes = {};

        @Name("biome_whitelist")
        @LangKey(LANG_PREFIX + "biome_whitelist")
        @Comment("Allow spawns in these biomes regardless of the biome type settings")
        @RequiresMcRestart
        public String[] biomeWhitelist = {};

        @Name("biome_blacklist")
        @LangKey(LANG_PREFIX + "biome_blacklist")
        @Comment("Prevent spawns in these biomes regardless of the biome type settings")
        @RequiresMcRestart
        public String[] biomeBlacklist = {};
    }

    public static class SpawnData {
        SpawnData(int spawnRate, int minGroupSize, int maxGroupSize, BiomeData biomeData, String[] allowedBlocks, int heightMax, int heightMin, boolean needsDarkness, boolean needsSeeSky, boolean needsCantSeeSky) {
            this.spawnRate = spawnRate;
            this.minGroupSize = minGroupSize;
            this.maxGroupSize = maxGroupSize;
            this.biomeData = biomeData;
            this.allowedBlocks = allowedBlocks;
            this.heightMax = heightMax;
            this.heightMin = heightMin;
            this.needsDarkness = needsDarkness;
            this.needsSeeSky = needsSeeSky;
            this.needsCantSeeSky = needsCantSeeSky;
        }

        @Name("spawn_rate")
        @LangKey(LANG_PREFIX + "spawn_rate")
        @Comment("Smaller number causes less spawning, 0 to disable spawning")
        @RangeInt(min = 0, max = 100)
        @RequiresMcRestart
        public int spawnRate = 20;

        @Name("min_group_size")
        @LangKey(LANG_PREFIX + "min_group_size")
        @Comment("Minimum number of mobs that appear in a spawn group")
        @RangeInt(min = 1, max = 100)
        @RequiresMcRestart
        public int minGroupSize = 1;

        @Name("max_group_size")
        @LangKey(LANG_PREFIX + "max_group_size")
        @Comment("Maximum number of mobs that appear in a spawn group")
        @RangeInt(min = 1, max = 100)
        @RequiresMcRestart
        public int maxGroupSize = 3;

        @Name("biome_data")
        @LangKey(LANG_PREFIX + "biome_data")
        @Comment("Control which biomes this spawn is allowed in")
        @RequiresMcRestart
        public BiomeData biomeData;

        @Name("dimensions")
        @LangKey(LANG_PREFIX + "dimensions")
        @Comment("IDs of dimensions this mob can spawn in")
        public int[] dimensions = {0};

        @Name("height_min")
        @LangKey(LANG_PREFIX + "height_min")
        @RangeDouble(min = -1, max = 255)
        @Comment("Minimum height for this spawn. -1 to ignore.")
        public float heightMin = 0;

        @Name("height_max")
        @LangKey(LANG_PREFIX + "height_max")
        @RangeDouble(min = -1, max = 255)
        @Comment("Maximum height for this spawn. -1 to ignore.")
        public float heightMax = 255;

        @Name("needs_darkness")
        @LangKey(LANG_PREFIX + "needs_darkness")
        @Comment("Set to true to only allow this mob to spawn in the dark, like zombies and skeletons")
        public boolean needsDarkness;

        @Name("requires_see_sky")
        @LangKey(LANG_PREFIX + "requires_see_sky")
        @Comment("Set to true to only spawn mob if it can see the sky")
        public boolean needsSeeSky;

        @Name("requires_cant_see_sky")
        @LangKey(LANG_PREFIX + "requires_cant_see_sky")
        @Comment("Set to true to only spawn mob if it can't see the sky")
        public boolean needsCantSeeSky;

        @Name("allowed_blocks")
        @LangKey(LANG_PREFIX + "allowed_blocks")
        @Comment("Names of blocks this mob is allowed to spawn on. Leave blank to allow any block.")
        @RequiresMcRestart
        public String[] allowedBlocks = {};
    }

    public static class GenerationData {
        GenerationData(int generationFrequency, float generationChance, BiomeData biomeData, float heightMin, float heightMax) {
            this.generationFrequency = generationFrequency;
            this.generationChance = generationChance;
            this.biomeData = biomeData;
            this.heightMax = heightMax;
            this.heightMin = heightMin;
        }

        @Name("generation_frequency")
        @LangKey(LANG_PREFIX + "generation_frequency")
        @Comment({"Smaller number causes more generation, 0 to disable spawning", "Maximum number of chunks between placements of this mob/structure"})
        @RangeInt(min = 1, max = 1000)
        public int generationFrequency = 15;

        @Name("generation_chance")
        @LangKey(LANG_PREFIX + "generation_chance")
        @Comment({"Probability that generation succeeds.", "For example, set to 0.5 to randomly not generate half of these structures in the world.", "Set to 1 to allow all generation attempts to succeed."})
        @RangeDouble(min = 0, max = 1)
        public float generationChance = 1;

        @Name("biome_data")
        @LangKey(LANG_PREFIX + "biome_data")
        @Comment("Control which biomes this generation is allowed in")
        @RequiresMcRestart
        public BiomeData biomeData;

        @Name("dimensions")
        @LangKey(LANG_PREFIX + "dimensions")
        @Comment("IDs of dimensions this mob/structure can generate in")
        public int[] dimensions = {0};

        @Name("height_min")
        @LangKey(LANG_PREFIX + "height_min")
        @RangeDouble(min = -1, max = 255)
        @Comment("Minimum height for generation placement. -1 to ignore")
        public float heightMin = 0;

        @Name("height_max")
        @LangKey(LANG_PREFIX + "height_max")
        @RangeDouble(min = -1, max = 255)
        @Comment("Maximum height for generation placement. -1 to ignore")
        public float heightMax = 255;
    }

    public static class CombatData {
        CombatData(float healthMultiplier, float attackMultiplier) {
            this.healthMultiplier = healthMultiplier;
            this.attackMultiplier = attackMultiplier;
        }

        @Name("health_multiplier")
        @LangKey(LANG_PREFIX + "health_multiplier")
        @Comment("Scale mob health by this value")
        @RangeDouble(min = 0, max = 100)
        @RequiresWorldRestart
        public float healthMultiplier = 1;

        @Name("attack_multiplier")
        @LangKey(LANG_PREFIX + "attack_multiplier")
        @Comment("Scale mob attack damage by this value")
        @RangeDouble(min = 0, max = 100)
        @RequiresWorldRestart
        public float attackMultiplier = 1;
    }

    public static class ToolData {
        ToolData(float attackDamage, float attackSpeed) {
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
        }

        @Name("attack_damage")
        @LangKey(LANG_PREFIX + "attack_damage")
        @RangeDouble(min = 0, max = 100000)
        @RequiresMcRestart
        public float attackDamage = 1;

        @Name("attack_speed")
        @LangKey(LANG_PREFIX + "attack_speed")
        @RangeDouble(min = 0, max = 100000)
        @RequiresMcRestart
        public float attackSpeed = 1;
    }

    public static class ArmorData {
        ArmorData(int damageReduction, float toughness) {
            this.damageReduction = damageReduction;
            this.toughness = toughness;
        }

        @Name("damage_reduction")
        @LangKey(LANG_PREFIX + "damage_reduction")
        @Comment("See official Minecraft Wiki for an explanation of how armor damage reduction works.")
        @RangeInt(min = 0, max = 10000)
        @RequiresMcRestart
        public int damageReduction = 1;

        @Name("toughness")
        @LangKey(LANG_PREFIX + "toughness")
        @Comment("See official Minecraft Wiki for an explanation of how armor toughness works.")
        @RangeDouble(min = 0, max = 10000)
        @RequiresMcRestart
        public float toughness = 1;
    }

    // Mob configuration
    public static class Foliaath {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData spawnData = new SpawnData(
                20, 1, 3,
                new BiomeData(new String[] {"JUNGLE"}, new String[] {}, new String[] {}),
                new String[] {"grass", "leaves", "leaves2", "log", "log2"},
                -1, 60, false, false, false
                );

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData combatData = new CombatData(1, 1);
    }

    public static class Barakoa {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning", "Controls spawning for Barakoana hunting groups", "Group size controls how many elites spawn, not followers", "See Barako config for village controls"})
        public SpawnData spawnData = new SpawnData(
                4, 1, 1,
                new BiomeData(new String[] {"SAVANNA"}, new String[] {}, new String[] {}),
                new String[] {"grass", "stone", "sand"},
                -1, 60, false, false, false
        );

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData combatData = new CombatData(1, 1);
    }

    public static class Naga {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData spawnData = new SpawnData(
                3, 1, 3,
                new BiomeData(new String[] {"BEACH,MOUNTAIN", "BEACH,HILLS"}, new String[] {"stone_beach"}, new String[] {}),
                new String[] {"grass", "stone", "sand"},
                -1, -1, false, true, false
        );

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData combatData = new CombatData(1, 1);
    }

    public static class Lantern {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData spawnData = new SpawnData(
                5, 1, 2,
                new BiomeData(new String[] {"FOREST,MAGICAL,!SNOWY"}, new String[] {"roofed_forest", "mutated_roofed_forest"}, new String[] {}),
                new String[] {"grass", "leaves", "leaves2", "log", "log2"},
                -1, 60, true, false, false
        );

        @Name("health_multiplier")
        @LangKey(LANG_PREFIX + "health_multiplier")
        @Comment("Scale mob health by this value")
        @RangeDouble(min = 0, max = 100)
        public float healthMultiplier = 1;

        @Name("glow_effect")
        @LangKey(LANG_PREFIX + "glow_effect")
        public boolean glowEffect = true;
    }

    public static class Grottol {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData spawnData = new SpawnData(
                2, 1, 1,
                new BiomeData(new String[] {""}, new String[] {}, new String[] {}),
                new String[] {},
                55, -1, false, false, true
        );

        @Name("health_multiplier")
        @LangKey(LANG_PREFIX + "health_multiplier")
        @Comment("Scale mob health by this value")
        @RangeDouble(min = 0, max = 100)
        public float healthMultiplier = 1;
    }

    public static class Ferrous_Wroughtnaut {
        @Name("generation_data")
        @LangKey(LANG_PREFIX + "generation_data")
        @Comment({"Controls for spawning mob/structure with world generation"})
        public GenerationData generationData = new GenerationData(2, 0.5f,
                new BiomeData(new String[] {""}, new String[] {}, new String[] {}),
                30, 55
                );

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData combatData = new CombatData(1, 1);

        @Name("has_boss_bar")
        @LangKey(LANG_PREFIX + "has_boss_bar")
        @Comment({"Disable/enable Ferrous Wroughtnauts' boss health bar"})
        @RequiresWorldRestart
        public boolean hasBossBar = true;

        @Name("heals_out_of_battle")
        @LangKey(LANG_PREFIX + "heals_out_of_battle")
        @Comment({"Disable/enable Ferrous Wroughtnaut healing while not active"})
        public boolean healsOutOfBattle = true;
    }

    public static class Barako {
        @Name("generation_data")
        @LangKey(LANG_PREFIX + "generation_data")
        @Comment({"Controls for spawning mob/structure with world generation", "Generation controls for Barakoa villages"})
        public GenerationData generationData = new GenerationData(12, 0.8f,
                new BiomeData(new String[] {"SAVANNA"}, new String[] {}, new String[] {}),
                50, 100
                );

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData combatData = new CombatData(1, 1);

        @Name("has_boss_bar")
        @LangKey(LANG_PREFIX + "has_boss_bar")
        @Comment({"Disable/enable Barako's boss health bar"})
        @RequiresWorldRestart
        public boolean hasBossBar = true;

        @Name("heals_out_of_battle")
        @LangKey(LANG_PREFIX + "heals_out_of_battle")
        @Comment({"Disable/enable Barako healing while not in combat"})
        public boolean healsOutOfBattle = true;
    }

    public static class Frostmaw {
        @Name("generation_data")
        @LangKey(LANG_PREFIX + "generation_data")
        @Comment({"Controls for spawning mob/structure with world generation"})
        public GenerationData generationData = new GenerationData(12, 0.8f,
                new BiomeData(new String[] {"SNOWY,!OCEAN,!RIVER,!BEACH"}, new String[] {}, new String[] {}),
                50, 100
                );

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData combatData = new CombatData(1, 1);

        @Name("stealable_ice_crystal")
        @LangKey(LANG_PREFIX + "stealable_ice_crystal")
        @Comment({"Allow players to steal frostmaws' ice crystals (only using specific means!)"})
        public boolean stealableIceCrystal = true;

        @Name("has_boss_bar")
        @LangKey(LANG_PREFIX + "has_boss_bar")
        @Comment({"Disable/enable frostmaws' boss health bar"})
        @RequiresWorldRestart
        public boolean hasBossBar = true;

        @Name("heals_out_of_battle")
        @LangKey(LANG_PREFIX + "heals_out_of_battle")
        @Comment({"Disable/enable frostmaw healing while asleep"})
        public boolean healsOutOfBattle = true;
    }

    public static class WroughtHelm {
        @Name("armor_data")
        @LangKey(LANG_PREFIX + "armor_data")
        @Comment({"Shared controls for armor"})
        public ArmorData armorData = new ArmorData(ArmorMaterial.IRON.getDamageReductionAmount(EntityEquipmentSlot.HEAD), ArmorMaterial.IRON.getToughness());

        @Name("breakable")
        @LangKey(LANG_PREFIX + "breakable")
        @Comment({"Set to true for the Wrought Helm to have limited durability."})
        @RequiresMcRestart
        public boolean breakable = false;
    }

    public static class AxeOfAThousandMetals {
        @Name("tool_data")
        @LangKey(LANG_PREFIX + "tool_data")
        @Comment({"Shared controls for melee weapons and tools"})
        public ToolData toolData = new ToolData(9, 0.9f);

        @Name("breakable")
        @LangKey(LANG_PREFIX + "breakable")
        @Comment({"Set to true for the Axe of a Thousand Metals to have limited durability."})
        @RequiresMcRestart
        public boolean breakable = false;
    }

    public static class SolVisage {
        @Name("armor_data")
        @LangKey(LANG_PREFIX + "armor_data")
        @Comment({"Shared controls for armor"})
        public ArmorData armorData = new ArmorData(ArmorMaterial.GOLD.getDamageReductionAmount(EntityEquipmentSlot.HEAD), ArmorMaterial.GOLD.getToughness());

        @Name("breakable")
        @LangKey(LANG_PREFIX + "breakable")
        @Comment({"Set to true for the Sol Visage to have limited durability."})
        @RequiresMcRestart
        public boolean breakable = false;
    }

    public static class BarakoaMask {
        @Name("armor_data")
        @LangKey(LANG_PREFIX + "armor_data")
        @Comment({"Shared controls for armor"})
        public ArmorData armorData = new ArmorData(ArmorMaterial.LEATHER.getDamageReductionAmount(EntityEquipmentSlot.HEAD), ArmorMaterial.LEATHER.getToughness());
    }

    public static class IceCrystal {
        @Name("ice_crystal_attack_multiplier")
        @LangKey(LANG_PREFIX + "ice_crystal_attack_multiplier")
        @Comment({"Multiply all damage done with the ice crystal by this amount."})
        @RangeDouble(min = 0, max = 100)
        public float attackMultiplier = 1;

        @Name("ice_crystal_breakable")
        @LangKey(LANG_PREFIX + "ice_crystal_breakable")
        @Comment({"Set to true for the ice crystal to have limited durability.", "Prevents regeneration in inventory."})
        @RequiresMcRestart
        public boolean breakable = false;

        @Name("durability")
        @LangKey(LANG_PREFIX + "durability")
        @Comment({"Ice crystal durability"})
        @RangeInt(min = 1, max = 100000000)
        @RequiresMcRestart
        public int durability = 600;
    }

    public static class BarakoaSpear {
        @Name("tool_data")
        @LangKey(LANG_PREFIX + "tool_data")
        @Comment({"Shared controls for melee weapons and tools"})
        public ToolData toolData = new ToolData(5, 1.6f);
    }

    public static class NagaFangDagger {
        @Name("tool_data")
        @LangKey(LANG_PREFIX + "tool_data")
        @Comment({"Shared controls for melee weapons and tools"})
        public ToolData toolData = new ToolData(3, 2);

        @Name("poison_duration")
        @LangKey(LANG_PREFIX + "poison_duration")
        @Comment({"Duration in ticks of the poison effect (20 ticks = 1 second)."})
        @RangeInt(min = 0, max = 100000)
        public int poisonDuration = 40;

        @Name("bonus_damage")
        @LangKey(LANG_PREFIX + "bonus_damage")
        @Comment({"Bonus damage when attacking from behind"})
        @RangeDouble(min = 0, max = 100000)
        public float bonusDamage = 3;
    }

    public static class BlowGun {
        @Name("attack_damage")
        @LangKey(LANG_PREFIX + "attack_damage")
        @RangeDouble(min = 0, max = 100000)
        @RequiresMcRestart
        public float attackDamage = 1;

        @Name("poison_duration")
        @LangKey(LANG_PREFIX + "poison_duration")
        @Comment({"Duration in ticks of the poison effect (20 ticks = 1 second)."})
        @RangeInt(min = 0, max = 100000)
        public int poisonDuration = 40;
    }

    @Name("mobs")
    @LangKey(LANG_PREFIX + "mobs")
    public static final Mobs MOBS = new Mobs();
    public static class Mobs {
        @Name("frostmaw")
        @LangKey(LANG_PREFIX + "frostmaw")
        public Frostmaw FROSTMAW = new Frostmaw();

        @Name("barako")
        @LangKey(LANG_PREFIX + "barako")
        public Barako BARAKO = new Barako();

        @Name("ferrous_wroughtnaut")
        @LangKey(LANG_PREFIX + "ferrous_wroughtnaut")
        public Ferrous_Wroughtnaut FERROUS_WROUGHTNAUT = new Ferrous_Wroughtnaut();

        @Name("grottol")
        @LangKey(LANG_PREFIX + "grottol")
        public Grottol GROTTOL = new Grottol();

        @Name("lantern")
        @LangKey(LANG_PREFIX + "lantern")
        public Lantern LANTERN = new Lantern();

        @Name("barakoa")
        @LangKey(LANG_PREFIX + "barakoa")
        public Barakoa BARAKOA = new Barakoa();

        @Name("naga")
        @LangKey(LANG_PREFIX + "naga")
        public Naga NAGA = new Naga();

        @Name("foliaath")
        @LangKey(LANG_PREFIX + "foliaath")
        public Foliaath FOLIAATH = new Foliaath();
    }

    @Name("tools_and_abilities")
    @LangKey(LANG_PREFIX + "tools_and_abilities")
    public static final ToolsAndAbilities TOOLS_AND_ABILITIES = new ToolsAndAbilities();
    public static class ToolsAndAbilities {

        @Name("suns_blessing_attack_multiplier")
        @LangKey(LANG_PREFIX + "suns_blessing_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float sunsBlessingAttackMultiplier = 1;

        @Name("geomancy_attack_multiplier")
        @LangKey(LANG_PREFIX + "geomancy_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float geomancyAttackMultiplier = 1;

        @Name("wrought_helm")
        @LangKey(LANG_PREFIX + "wrought_helm")
        public WroughtHelm WROUGHT_HELM = new WroughtHelm();

        @Name("axe_of_a_thousand_metals")
        @LangKey(LANG_PREFIX + "axe_of_a_thousand_metals")
        public AxeOfAThousandMetals AXE_OF_A_THOUSAND_METALS = new AxeOfAThousandMetals();

        @Name("sol_visage")
        @LangKey(LANG_PREFIX + "sol_visage")
        public SolVisage SOL_VISAGE = new SolVisage();

        @Name("ice_crystal")
        @LangKey(LANG_PREFIX + "ice_crystal")
        public IceCrystal ICE_CRYSTAL = new IceCrystal();

        @Name("barakoa_mask")
        @LangKey(LANG_PREFIX + "barakoa_mask")
        public BarakoaMask BARAKOA_MASK = new BarakoaMask();

        @Name("barakoa_spear")
        @LangKey(LANG_PREFIX + "barakoa_spear")
        public BarakoaSpear BARAKOA_SPEAR = new BarakoaSpear();

        @Name("naga_fang_dagger")
        @LangKey(LANG_PREFIX + "naga_fang_dagger")
        public NagaFangDagger NAGA_FANG_DAGGER = new NagaFangDagger();

        @Name("blow_gun")
        @LangKey(LANG_PREFIX + "blow_gun")
        public BlowGun BLOW_GUN = new BlowGun();
    }

    @Name("general")
    @LangKey(LANG_PREFIX + "general")
    public static final General GENERAL = new General();
    public static class General {
        @Name("freeze_blacklist")
        @LangKey(LANG_PREFIX + "freeze_blacklist")
        @Comment({"Add a mob's full name here to prevent it from being frozen or taking damage from ice magic."})
        public String[] freeze_blacklist = {"mowziesmobs:frostmaw", "minecraft:ender_dragon"};
    }
}
