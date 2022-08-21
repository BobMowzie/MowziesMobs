package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class TagHandler {
    public static final TagKey<Item> CAN_HIT_GROTTOL = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "can_hit_grottol"));
    public static final TagKey<EntityType<?>> BARAKOA = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "barakoa"));
    public static final TagKey<EntityType<?>> BARAKOA_BARAKO_ALIGNED = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "barakoa_barako_aligned"));
    public static final TagKey<Biome> HAS_MOWZIE_STRUCTURE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/has_mowzie_structure"));
}
