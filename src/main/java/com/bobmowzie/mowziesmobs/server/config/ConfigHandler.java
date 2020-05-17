package com.bobmowzie.mowziesmobs.server.config;

import java.io.File;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RangeDouble;

@Config(modid = MowziesMobs.MODID, category = "", name = MowziesMobs.MODID)
public class ConfigHandler {
    @Ignore
    public static File configDir;

    @Ignore
    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";

    public static class SpawnData {
        SpawnData(int spawnRate, int minGroupSize, int maxGroupSize) {
            this.spawnRate = spawnRate;
            this.minGroupSize = minGroupSize;
            this.maxGroupSize = maxGroupSize;
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
    }

    public static class GenerationData {
        GenerationData(int generationFrequency) {
            this.generationFrequency = generationFrequency;
        }

        @Name("generation_frequency")
        @LangKey(LANG_PREFIX + "generation_frequency")
        @Comment({"Smaller number causes more generation, 0 to disable spawning", "Minimum number of chunks between placements of this mob/structure"})
        @RangeInt(min = 1, max = 1000)
        public int generationFrequency = 15;
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

    @Name("foliaath")
    @LangKey(LANG_PREFIX + "foliaath")
    public static final Foliaath FOLIAATH = new Foliaath();
    public static class Foliaath {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData SPAWN_DATA = new SpawnData(20, 1, 3);

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData COMBAT_DATA = new CombatData(1, 1);
    }

    @Name("barakoa")
    @LangKey(LANG_PREFIX + "barakoa")
    public static final Barakoa BARAKOA = new Barakoa();
    public static class Barakoa {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning", "Controls spawning for Barakoana hunting groups", "Group size controls how many elites spawn, not followers", "See Barako config for village controls"})
        public SpawnData SPAWN_DATA = new SpawnData(4, 1, 1);

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData COMBAT_DATA = new CombatData(1, 1);
    }

    @Name("naga")
    @LangKey(LANG_PREFIX + "naga")
    public static final Naga NAGA = new Naga();
    public static class Naga {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData SPAWN_DATA = new SpawnData(3, 1, 3);

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData COMBAT_DATA = new CombatData(1, 1);
    }

    @Name("lantern")
    @LangKey(LANG_PREFIX + "lantern")
    public static final Lantern LANTERN = new Lantern();
    public static class Lantern {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData SPAWN_DATA = new SpawnData(4, 1, 2);

        @Name("health_multiplier")
        @LangKey(LANG_PREFIX + "health_multiplier")
        @Comment("Scale mob health by this value")
        @RangeDouble(min = 0, max = 100)
        public float healthMultiplier = 1;
    }

    @Name("grottol")
    @LangKey(LANG_PREFIX + "grottol")
    public static final Grottol GROTTOL = new Grottol();
    public static class Grottol {
        @Name("spawn_data")
        @LangKey(LANG_PREFIX + "spawn_data")
        @Comment({"Controls for vanilla-style mob spawning"})
        public SpawnData SPAWN_DATA = new SpawnData(1, 1, 1);

        @Name("health_multiplier")
        @LangKey(LANG_PREFIX + "health_multiplier")
        @Comment("Scale mob health by this value")
        @RangeDouble(min = 0, max = 100)
        public float healthMultiplier = 1;
    }

    @Name("ferrous_wroughtnaut")
    @LangKey(LANG_PREFIX + "ferrous_wroughtnaut")
    public static final Ferrous_Wroughtnaut FERROUS_WROUGHTNAUT = new Ferrous_Wroughtnaut();
    public static class Ferrous_Wroughtnaut {
        @Name("ferrous_wroughtnaut")
        @LangKey(LANG_PREFIX + "ferrous_wroughtnaut")
        @Comment({"Controls for spawning mob/structure with world generation"})
        public GenerationData GENERATION_DATA = new GenerationData(40);

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData COMBAT_DATA = new CombatData(1, 1);
    }

    @Name("barako")
    @LangKey(LANG_PREFIX + "barako")
    public static final Barako BARAKO = new Barako();
    public static class Barako {
        @Name("generation_data")
        @LangKey(LANG_PREFIX + "generation_data")
        @Comment({"Controls for spawning mob/structure with world generation", "Generation controls for Barakoa villages"})
        public GenerationData GENERATION_DATA = new GenerationData(15);

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData COMBAT_DATA = new CombatData(1, 1);
    }

    @Name("frostmaw")
    @LangKey(LANG_PREFIX + "frostmaw")
    public static final Frostmaw FROSTMAW = new Frostmaw();
    public static class Frostmaw {
        @Name("generation_data")
        @LangKey(LANG_PREFIX + "generation_data")
        @Comment({"Controls for spawning mob/structure with world generation"})
        public GenerationData GENERATION_DATA = new GenerationData(15);

        @Name("combat_data")
        @LangKey(LANG_PREFIX + "combat_data")
        public CombatData COMBAT_DATA = new CombatData(1, 1);
    }

    @Name("tools_and_abilities")
    @LangKey(LANG_PREFIX + "tools_and_abilities")
    public static final ToolsAndAbilities TOOLS_AND_ABILITIES = new ToolsAndAbilities();
    public static class ToolsAndAbilities {
        @Name("axe_attack_multiplier")
        @LangKey(LANG_PREFIX + "axe_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float axeAttackMultiplier = 1;

        @Name("suns_blessing_attack_multiplier")
        @LangKey(LANG_PREFIX + "suns_blessing_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float sunsBlessingAttackMultiplier = 1;

        @Name("spear_attack_multiplier")
        @LangKey(LANG_PREFIX + "spear_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float spearAttackMultiplier = 1;

        @Name("blowgun_attack_multiplier")
        @LangKey(LANG_PREFIX + "blowgun_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float blowgunAttackMultiplier = 1;

        @Name("geomancy_attack_multiplier")
        @LangKey(LANG_PREFIX + "geomancy_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float geomancyAttackMultiplier = 1;

        @Name("ice_crystal_attack_multiplier")
        @LangKey(LANG_PREFIX + "ice_crystal_attack_multiplier")
        @RangeDouble(min = 0, max = 100)
        public float iceCrystalAttackMultiplier = 1;
    }
}
