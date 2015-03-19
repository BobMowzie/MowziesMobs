package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.entity.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityWroughtnaut;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

public class MMEntities
{

    public static void init()
    {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.jungleHills, BiomeGenBase.jungle, BiomeGenBase.jungleEdge});
        registerEntity(EntityBabyFoliaath.class, "BabyFoliaath", true, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.monster, null);
        registerEntity(EntityWroughtnaut.class, "FerrousWroughtnaut", true, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.monster, null);
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
