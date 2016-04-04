package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public enum EntityHandler {
    INSTANCE;

    private final Map<Integer, MowzieEntityEggInfo> ENTITY_EGGS = new LinkedHashMap<>();

    private int nextEntityId;

    public void registerEntity(Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int frequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, BiomeGenBase... biomes) {
        int entityId = nextEntityId();
        EntityRegistry.registerModEntity(entityClass, name, entityId, MowziesMobs.INSTANCE, 64, 1, true);
        if (addEgg) {
            ENTITY_EGGS.put(entityId, new MowzieEntityEggInfo(entityId, mainColor, subColor));
        }
        if (addSpawn) {
            EntityRegistry.addSpawn(entityClass, frequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }

    private int nextEntityId() {
        return nextEntityId++;
    }

    public MowzieEntityEggInfo getEntityEggInfo(int id) {
        return ENTITY_EGGS.get(id);
    }

    public boolean hasEntityEggInfo(int id) {
        return ENTITY_EGGS.containsKey(id);
    }

    public Iterator<MowzieEntityEggInfo> getEntityEggInfoIterator() {
        return ENTITY_EGGS.values().iterator();
    }

    public Entity createEntityById(int id, World world) {
        Entity entity = null;
        try {
            EntityRegistration reg = EntityRegistry.instance().lookupModSpawn(MowziesMobs.getModContainer(), id);
            Class clazz = reg.getEntityClass();
            if (clazz != null) {
                entity = (Entity) clazz.getConstructor(World.class).newInstance(world);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public String getEntityNameById(int id) {
        String name = "missingno";
        try {
            name = EntityRegistry.instance().lookupModSpawn(MowziesMobs.getModContainer(), id).getEntityName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public void onInit() {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.monster, BiomeGenBase.jungleHills, BiomeGenBase.jungle, BiomeGenBase.jungleEdge);
        registerEntity(EntityBabyFoliaath.class, "BabyFoliaath", false, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityWroughtnaut.class, "FerrousWroughtnaut", true, 0x8C8C8C, 0xFFFFFF, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeHunter.class, "TribesmanHunter", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeElite.class, "TribesmanElite", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeVillager.class, "TribesmanVillager", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.creature);
        registerEntity(EntityTribeLeader.class, "TribeLeader", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);

        EntityRegistry.registerModEntity(EntitySunstrike.class, "Sunstrike", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(EntitySolarBeam.class, "SolarBeam", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);

        EntityRegistry.registerModEntity(EntityDart.class, "dart", nextEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
    }
}
