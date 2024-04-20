package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class TagHandler {
    public static final TagKey<Item> CAN_HIT_GROTTOL = TagKey.create(Registries.ITEM, new ResourceLocation(MowziesMobs.MODID, "can_hit_grottol"));
    public static final TagKey<EntityType<?>> UMVUTHANA = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MowziesMobs.MODID, "umvuthana"));
    public static final TagKey<EntityType<?>> UMVUTHANA_UMVUTHI_ALIGNED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MowziesMobs.MODID, "umvuthana_umvuthi_aligned"));


    public static final TagKey<Biome> HAS_MOWZIE_STRUCTURE = TagKey.create(Registries.BIOME, new ResourceLocation(MowziesMobs.MODID, "has_structure/has_mowzie_structure"));
    public static final TagKey<Biome> IS_MAGICAL = TagKey.create(Registries.BIOME, new ResourceLocation(MowziesMobs.MODID, "is_magical"));
}
