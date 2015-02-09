package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.entity.EntityList;

import java.util.Random;

public class MMEntities implements IContentProvider
{

    public void init()
    {
        registerEntity(EntityFoliaath.class, "EntityFoliaath", true);
    }

    public static void registerEntity(Class entityClass, String name, boolean addEgg)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, MowziesMobs.instance, 64, 1, true);
        if (addEgg)
        {
            Random random = new Random(name.hashCode());
            int mainColor = random.nextInt() * 14444000;
            int subColor = random.nextInt() * 17777000;
            EntityList.entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, mainColor, subColor));
        }
    }
}
