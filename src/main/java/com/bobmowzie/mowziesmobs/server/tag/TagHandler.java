package com.bobmowzie.mowziesmobs.server.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class TagHandler {
    public static final TagKey<Item> CAN_HIT_GROTTOL = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("can_hit_grottol"));
    public static final TagKey<EntityType<?>> BARAKOA = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("barakoa"));
    public static final TagKey<EntityType<?>> BARAKOA_BARAKO_ALIGNED = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("barakoa_barako_aligned"));
}
