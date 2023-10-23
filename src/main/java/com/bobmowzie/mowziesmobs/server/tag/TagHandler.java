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
    public static final TagKey<EntityType<?>> UMVUTHANA = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "umvuthana"));
    public static final TagKey<EntityType<?>> UMVUTHANA_UMVUTHI_ALIGNED = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "umvuthana_umvuthi_aligned"));


    public static final TagKey<Biome> HAS_MOWZIE_STRUCTURE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/has_mowzie_structure"));
}
