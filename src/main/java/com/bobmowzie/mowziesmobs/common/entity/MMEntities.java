package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MMEntities implements IContentHandler
{
    private static final Map<Integer, MMEntityEggInfo> ENTITY_EGGS = new LinkedHashMap<Integer, MMEntityEggInfo>();

    private static int nextEntityId;

    public static void registerEntity(Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int frequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, BiomeGenBase... biomes)
    {
        int entityId = nextEntityId();
        EntityRegistry.registerModEntity(entityClass, name, entityId, MowziesMobs.instance, 64, 1, true);
        if (addEgg)
        {
            ENTITY_EGGS.put(entityId, new MMEntityEggInfo(entityId, mainColor, subColor, name));
        }
        if (addSpawn)
        {
            EntityRegistry.addSpawn(entityClass, frequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }

    private static int nextEntityId()
    {
        return nextEntityId++;
    }

    public static MMEntityEggInfo getEntityEggInfo(int id)
    {
        return ENTITY_EGGS.get(id);
    }

    public static boolean hasEntityEggInfo(int id)
    {
        return ENTITY_EGGS.containsKey(id);
    }

    public static Iterator<MMEntityEggInfo> getEntityEggInfoIterator()
    {
        return ENTITY_EGGS.values().iterator();
    }

    public static Entity createEntityById(int id, World world)
    {
        Entity entity = null;
        try
        {
            EntityRegistration reg = EntityRegistry.instance().lookupModSpawn(MowziesMobs.getModContainer(), id);
            Class clazz = reg.getEntityClass();
            if (clazz != null)
            {
                entity = (Entity) clazz.getConstructor(World.class).newInstance(world);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return entity;
    }

    public static String getEntityNameById(int id)
    {
        String name = "missingno";
        try
        {
            name = EntityRegistry.instance().lookupModSpawn(MowziesMobs.getModContainer(), id).getEntityName();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return name;
    }

    public void init()
    {
    }

    public void gameRegistry() throws Exception
    {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.monster, BiomeGenBase.jungleHills, BiomeGenBase.jungle, BiomeGenBase.jungleEdge);
        registerEntity(EntityBabyFoliaath.class, "BabyFoliaath", false, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityWroughtnaut.class, "FerrousWroughtnaut", true, 0x8C8C8C, 0xFFFFFF, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeHunter.class, "TribesmanHunter", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeElite.class, "TribesmanElite", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeVillager.class, "TribesmanVillager", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.creature);
        registerEntity(EntityTribeLeader.class, "TribeLeader", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        EntityRegistry.registerModEntity(EntitySunstrike.class, "Sunstrike", nextEntityId(), MowziesMobs.instance, 64, 1, true);

        EntityRegistry.registerModEntity(EntityDart.class, "dart", nextEntityId(), MowziesMobs.instance, 64, 1, true);
    }
}
