package com.bobmowzie.mowziesmobs.server.entity;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;

public enum EntityHandler {
    INSTANCE;

    public static final String BARAKOAYA_ID = "barakoaya";

    private final Map<ResourceLocation, MowzieEntityEggInfo> entityEggs = new LinkedHashMap<>();

    private int nextEntityId;

    public void onInit() {
        registerEntity(EntityFoliaath.class, "foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.MONSTER, Biomes.JUNGLE_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_EDGE);
        registerEntity(EntityBabyFoliaath.class, "baby_foliaath", false, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityWroughtnaut.class, "ferrous_wroughtnaut", true, 0x8C8C8C, 0xFFFFFF, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityBarakoanToBarakoana.class, "barakoan_barakoana", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityBarakoana.class, "barakoana", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityBarakoaya.class, BARAKOAYA_ID, true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.CREATURE);
        registerEntity(EntityBarakoanToPlayer.class, "barakoan_player", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.CREATURE);
        registerEntity(EntityBarako.class, "barako", true, 0xBA6656, 0xFFFF4D, false, 1, 1, 1, EnumCreatureType.MONSTER);
        registerEntity(EntityFrostmaw.class, "frostmaw", true, 0xf7faff, 0xafcdff, false, 1, 1, 1, EnumCreatureType.MONSTER);

        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "sunstrike"), EntitySunstrike.class, "sunstrike", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "solar_beam"), EntitySolarBeam.class, "solar_beam", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "boulder"), EntityBoulder.class, "boulder", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "icebreath"), EntityIceBreath.class, "icebreath", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "ring"), EntityRing.class, "ring", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "frozen_controller"), EntityFrozenController.class, "frozen_controller", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);

        EntityRegistry.registerModEntity(new ResourceLocation(MowziesMobs.MODID, "dart"), EntityDart.class, "dart", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
    }

    public void registerEntity(Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int spawnFrequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, Biome... biomes) {
        int entityId = nextEntityId();
        ResourceLocation res = new ResourceLocation(MowziesMobs.MODID, name);
        EntityRegistry.registerModEntity(res, entityClass, name, entityId, MowziesMobs.INSTANCE, 64, 1, true);
        if (addEgg) {
            entityEggs.put(res, new MowzieEntityEggInfo(res, entityClass, mainColor, subColor));
        }
        if (addSpawn) {
            EntityRegistry.addSpawn(entityClass, spawnFrequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }

    private int nextEntityId() {
        return nextEntityId++;
    }

    public MowzieEntityEggInfo getEntityEggInfo(ResourceLocation id) {
        return entityEggs.get(id);
    }

    public boolean hasEntityEggInfo(ResourceLocation name) {
        return entityEggs.containsKey(name);
    }

    public EntityLiving createEntity(ResourceLocation id, World world) {
        EntityLiving entity = null;
        try {
            Class<? extends EntityLiving> clazz = entityEggs.get(id).clazz;
            if (clazz != null) {
                entity = clazz.getConstructor(World.class).newInstance(world);
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
