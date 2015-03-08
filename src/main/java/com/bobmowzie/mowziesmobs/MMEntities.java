package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

public class MMEntities implements IContentProvider
{

    public void init()
    {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, true, 20, 1, 1, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.jungleHills, BiomeGenBase.jungle, BiomeGenBase.jungleEdge});
    }

    public static void registerEntity(Class entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int frequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, BiomeGenBase... biomes)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, MowziesMobs.instance, 64, 1, true);
        if (addEgg)
        {
            EntityList.entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, mainColor, subColor));
        }
        if (addSpawn)
        {
            EntityRegistry.addSpawn(entityClass, frequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }
}
