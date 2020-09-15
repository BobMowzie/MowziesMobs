package com.ilexiconn.llibrary.server.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class ModUtils {
    private static final ForgeRegistry<Item> ITEM_REGISTRY = RegistryManager.ACTIVE.getRegistry(GameData.ITEMS);

    private static final Map<String, ModContainer> resourceIDToContainerMap = new HashMap<>();

    static {
        resourceIDToContainerMap.put("minecraft", Loader.instance().getMinecraftModContainer());
        Map<String, ModContainer> map = Loader.instance().getIndexedModList();
        for (Map.Entry<String, ModContainer> modEntry : map.entrySet()) {
            String resourceID = modEntry.getKey().toLowerCase(Locale.ENGLISH);
            ModContainer modContainer = modEntry.getValue();
            resourceIDToContainerMap.put(resourceID, modContainer);
        }
    }

    /**
     * @param stack the item stack
     * @return the mod name of the specified item stack
     */
    public static String getModNameForStack(ItemStack stack) {
        return ModUtils.getModNameForItem(stack.getItem());
    }

    /**
     * @param block the block
     * @return the mod name of the specified block
     */
    public static String getModNameForBlock(Block block) {
        return ModUtils.getModNameForItem(Item.getItemFromBlock(block));
    }

    /**
     * @param item the item
     * @return the mod name of the specified item
     */
    public static String getModNameForItem(Item item) {
        ResourceLocation resourceLocation = ITEM_REGISTRY.getKey(item);
        if (resourceLocation != null) {
            String modID = resourceLocation.getNamespace();
            String resourceID = modID.toLowerCase(Locale.ENGLISH);
            return ModUtils.getNameForResourceID(resourceID);
        }
        return null;
    }

    /**
     * @param resourceID the resource domain
     * @return the mod name
     */
    public static String getNameForResourceID(String resourceID) {
        return ModUtils.resourceIDToContainerMap.get(resourceID).getName();
    }

    /**
     * @param stack the item stack
     * @return the mod container of the specified item stack
     * @since 1.2.1
     */
    public static ModContainer getContainerForStack(ItemStack stack) {
        return ModUtils.getContainerForItem(stack.getItem());
    }

    /**
     * @param block the block
     * @return the mod container of the specified block
     * @since 1.2.1
     */
    public static ModContainer getContainerForBlock(Block block) {
        return ModUtils.getContainerForItem(Item.getItemFromBlock(block));
    }

    /**
     * @param item the item
     * @return the mod container of the specified item
     * @since 1.2.1
     */
    public static ModContainer getContainerForItem(Item item) {
        ResourceLocation resourceLocation = ITEM_REGISTRY.getKey(item);
        if (resourceLocation != null) {
            String modID = resourceLocation.getNamespace();
            String resourceID = modID.toLowerCase(Locale.ENGLISH);
            return ModUtils.getContainerForResourceID(resourceID);
        }
        return null;
    }

    /**
     * @param resourceID the resource domain
     * @return the mod container
     * @since 1.2.1
     */
    public static ModContainer getContainerForResourceID(String resourceID) {
        return ModUtils.resourceIDToContainerMap.get(resourceID);
    }
}
