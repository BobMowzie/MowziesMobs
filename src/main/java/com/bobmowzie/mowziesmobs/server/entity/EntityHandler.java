package com.bobmowzie.mowziesmobs.server.entity;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bobmowzie.mowziesmobs.server.entity.tribe.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;

public enum EntityHandler {
    INSTANCE;

    private final Map<String, MowzieEntityEggInfo> entityEggs = new LinkedHashMap<>();

    private int nextEntityId;

    public void onInit() {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.MONSTER, Biomes.JUNGLE_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_EDGE);
        registerEntity(EntityBabyFoliaath.class, "BabyFoliaath", false, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityWroughtnaut.class, "FerrousWroughtnaut", true, 0x8C8C8C, 0xFFFFFF, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityTribeHunter.class, "TribesmanHunter", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityTribeElite.class, "TribesmanElite", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityTribeVillager.class, "TribesmanVillager", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.CREATURE);
        registerEntity(EntityTribePlayer.class, "TribesmanPlayer", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.CREATURE);
        registerEntity(EntityTribeLeader.class, "TribeLeader", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.MONSTER);

        EntityRegistry.registerModEntity(EntitySunstrike.class, "Sunstrike", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(EntitySolarBeam.class, "SolarBeam", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);

        EntityRegistry.registerModEntity(EntityDart.class, "Dart", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
    }

    public void registerEntity(Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int spawnFrequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, Biome... biomes) {
        int entityId = nextEntityId();
        EntityRegistry.registerModEntity(entityClass, name, entityId, MowziesMobs.INSTANCE, 64, 1, true);
        if (addEgg) {
            entityEggs.put(name, new MowzieEntityEggInfo(name, entityClass, mainColor, subColor));
        }
        if (addSpawn) {
            EntityRegistry.addSpawn(entityClass, spawnFrequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }

    private int nextEntityId() {
        return nextEntityId++;
    }

    public MowzieEntityEggInfo getEntityEggInfo(String id) {
        return entityEggs.get(id);
    }

    public boolean hasEntityEggInfo(String name) {
        return entityEggs.containsKey(name);
    }

    public EntityLiving createEntity(String name, World world) {
        EntityLiving entity = null;
        try {
            Class<? extends EntityLiving> clazz = entityEggs.get(name).clazz;
            if (clazz != null) {
                entity = (EntityLiving) clazz.getConstructor(World.class).newInstance(world);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public Iterator<MowzieEntityEggInfo> getEntityEggInfoIterator() {
        return entityEggs.values().iterator();
    }
}
