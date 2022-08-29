package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class TagHandler {
    public static final TagKey<Item> CAN_HIT_GROTTOL = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "can_hit_grottol"));
    public static final TagKey<EntityType<?>> BARAKOA = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "barakoa"));
    public static final TagKey<EntityType<?>> BARAKOA_BARAKO_ALIGNED = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "barakoa_barako_aligned"));


    public static final TagKey<Biome> HAS_FOLIAATH = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/foliaath/has_foliaath"));
    public static final TagKey<Biome> NO_FOLIAATH = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/foliaath/no_foliaath"));

    public static final TagKey<Biome> HAS_BARAKOANA = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/barakoana/has_barakoana"));
    public static final TagKey<Biome> NO_BARAKOANA = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/barakoana/no_barakoana"));

    public static final TagKey<Biome> HAS_GROTTOL = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/grottol/has_grottol"));
    public static final TagKey<Biome> NO_GROTTOL = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/grottol/no_grottol"));

    public static final TagKey<Biome> HAS_LANTERN = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/lantern/has_lantern"));
    public static final TagKey<Biome> NO_LANTERN = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/lantern/no_lantern"));

    public static final TagKey<Biome> HAS_NAGA = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/naga/has_naga"));
    public static final TagKey<Biome> NO_NAGA = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_spawn/naga/no_naga"));

    public static final TagKey<Biome> HAS_WROUGHT_CHAMBER = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/wrought_chamber/has_wrought_chamber"));
    public static final TagKey<Biome> NO_WROUGHT_CHAMBER = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/wrought_chamber/no_wrought_chamber"));

    public static final TagKey<Biome> HAS_BARAKOA_VILLAGE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/barakoa_village/has_barakoa_village"));
    public static final TagKey<Biome> NO_BARAKOA_VILLAGE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/barakoa_village/no_barakoa_village"));

    public static final TagKey<Biome> HAS_FROSTMAW = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/frostmaw/has_frostmaw"));
    public static final TagKey<Biome> NO_FROSTMAW = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/frostmaw/no_frostmaw"));
}
