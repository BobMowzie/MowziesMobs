package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

public class MMEntityRegistry implements IContentHandler
{
    public static void registerEntity(Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int frequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, BiomeGenBase... biomes)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, MowziesMobs.instance, 64, 1, true);
        if (addEgg)
        {
            EntityList.entityEggs.put(entityID, new EntityList.EntityEggInfo(entityID, mainColor, subColor));
        }
        if (addSpawn)
        {
            EntityRegistry.addSpawn(entityClass, frequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }

    public void init()
    {

    }

    public void gameRegistry() throws Exception
    {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.monster, BiomeGenBase.jungleHills, BiomeGenBase.jungle, BiomeGenBase.jungleEdge);
        registerEntity(EntityBabyFoliaath.class, "BabyFoliaath", false, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityWroughtnaut.class, "FerrousWroughtnaut", true, 0x8C8C8C, 0xFFFFFF, false, 1, 1, 1, EnumCreatureType.monster);
    }
}
