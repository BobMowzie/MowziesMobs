package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

public enum EntityHandler {
    INSTANCE;

    public void registerEntity(Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, boolean addSpawn, int frequency, int minGroup, int maxGroup, EnumCreatureType typeOfCreature, BiomeGenBase... biomes) {
        int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerModEntity(entityClass, name, id, MowziesMobs.INSTANCE, 64, 1, true);
        if (addEgg) {
            EntityRegistry.registerGlobalEntityID(entityClass, name, id, mainColor, subColor);
        }
        if (addSpawn) {
            EntityRegistry.addSpawn(entityClass, frequency, minGroup, maxGroup, typeOfCreature, biomes);
        }
    }

    public void onInit() {
        registerEntity(EntityFoliaath.class, "Foliaath", true, 0x47CC3B, 0xC03BCC, false, 20, 3, 1, EnumCreatureType.monster, BiomeGenBase.jungleHills, BiomeGenBase.jungle, BiomeGenBase.jungleEdge);
        registerEntity(EntityBabyFoliaath.class, "BabyFoliaath", false, 0x47CC3B, 0xC03BCC, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityWroughtnaut.class, "FerrousWroughtnaut", true, 0x8C8C8C, 0xFFFFFF, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeHunter.class, "TribesmanHunter", false, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeElite.class, "TribesmanElite", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);
        registerEntity(EntityTribeVillager.class, "TribesmanVillager", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.creature);
        registerEntity(EntityTribeLeader.class, "TribeLeader", true, 0xBA6656, 0xFAFA78, false, 1, 1, 1, EnumCreatureType.monster);

        EntityRegistry.registerModEntity(EntitySunstrike.class, "Sunstrike", EntityRegistry.findGlobalUniqueEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
        EntityRegistry.registerModEntity(EntitySolarBeam.class, "SolarBeam", EntityRegistry.findGlobalUniqueEntityId(), MowziesMobs.INSTANCE, 64, 1, true);

        EntityRegistry.registerModEntity(EntityDart.class, "dart", EntityRegistry.findGlobalUniqueEntityId(), MowziesMobs.INSTANCE, 64, 1, true);
    }
}
