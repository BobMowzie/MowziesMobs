package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;

public class TagHandler {
    public static class Blocks {

        private static Tags.IOptionalNamedTag<Block> createTag(String name) {
            return BlockTags.createOptional(new ResourceLocation(MowziesMobs.MODID, name));
        }

        private static Tags.IOptionalNamedTag<Block> createForgeTag(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static class Items {
        public static final Tags.IOptionalNamedTag<Item> CAN_HIT_GROTTOL = createTag("can_hit_grottol");

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(MowziesMobs.MODID, name));
        }

        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static class EntityTypes {
        public static final Tag.Named<EntityType<?>> BARAKOA = createTag("barakoa");
        public static final Tag.Named<EntityType<?>> BARAKOA_BARAKO_ALIGNED = createTag("barakoa_barako_aligned");

        private static Tag.Named<EntityType<?>> createTag(String name) {
            return EntityTypeTags.createOptional(new ResourceLocation(MowziesMobs.MODID, name));
        }

        private static Tag.Named<EntityType<?>> createForgeTag(String name) {
            return EntityTypeTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
