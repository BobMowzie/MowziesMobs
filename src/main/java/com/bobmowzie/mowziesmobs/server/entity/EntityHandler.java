package com.bobmowzie.mowziesmobs.server.entity;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.google.common.reflect.Reflection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public enum EntityHandler {
    INSTANCE;

    public static final String BARAKOAYA_ID = "barakoaya";

    private static final Map<ResourceLocation, MowzieEntityEggInfo> entityEggs = new LinkedHashMap<>();

    private static int nextEntityId;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<EntityEntry> event) {
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        registerEntity(registry, EntityFoliaath.class, "foliaath", true, 0x47CC3B, 0xC03BCC, 64);
        registerEntity(registry, EntityBabyFoliaath.class, "baby_foliaath", false, 0x47CC3B, 0xC03BCC, 64);
        registerEntity(registry, EntityWroughtnaut.class, "ferrous_wroughtnaut", true, 0x8C8C8C, 0xFFFFFF, 64);
        registerEntity(registry, EntityBarakoanToBarakoana.class, "barakoan_barakoana", false, 0xBA6656, 0xFAFA78, 64);
        registerEntity(registry, EntityBarakoana.class, "barakoana", true, 0xBA6656, 0xFAFA78, 64);
        registerEntity(registry, EntityBarakoaya.class, BARAKOAYA_ID, true, 0xBA6656, 0xFAFA78, 64);
        registerEntity(registry, EntityBarakoanToPlayer.class, "barakoan_player", false, 0xBA6656, 0xFAFA78, 64);
        registerEntity(registry, EntityBarako.class, "barako", true, 0xBA6656, 0xFFFF00, 64);
        registerEntity(registry, EntityFrostmaw.class, "frostmaw", true, 0xf7faff, 0xafcdff, 64);
        registerEntity(registry, EntityGrottol.class, "grottol", true, 0x777777, 0xbce0ff, 64);
        registerEntity(registry, EntityLantern.class, "lantern", true, 0x6dea00, 0x235a10, 64);
        registerEntity(registry, EntityNaga.class, "naga", true, 0x154850, 0x8dd759, 128);

        registerEntity(registry, EntitySunstrike.class, "sunstrike", 64);
        registerEntity(registry, EntitySolarBeam.class, "solar_beam", 64);
        registerEntity(registry, EntityBoulder.class, "boulder", 64);
        registerEntity(registry, EntityAxeAttack.class, "axe_attack", 64);
        registerEntity(registry, EntityIceBreath.class, "icebreath", 64);
        registerEntity(registry, EntityIceBall.class, "ice_ball", 64);
        registerEntity(registry, EntityFrozenController.class, "frozen_controller", 64);
        registerEntity(registry, EntityRing.class, "ring", 64);
        registerEntity(registry, EntityDart.class, "dart", 64);
        registerEntity(registry, EntityPoisonBall.class, "poison_ball", 64);
        registerEntity(registry, EntitySuperNova.class, "super_nova", 64);
        registerEntity(registry, EntityBlockSwapper.class, "block_swapper", 64);
    }

    public static void registerEntity(IForgeRegistry<EntityEntry> registry, Class<? extends EntityLiving> entityClass, String name, boolean addEgg, int mainColor, int subColor, int trackingDistance, Biome... biomes) {
        Reflection.initialize(entityClass);
        int entityId = nextEntityId();
        ResourceLocation res = new ResourceLocation(MowziesMobs.MODID, name);
        registry.register(EntityEntryBuilder.create()
                .entity(entityClass)
                .id(res, entityId)
                .name(name)
                .tracker(trackingDistance, 1, true)
                .build());

        if (addEgg) {
            entityEggs.put(res, new MowzieEntityEggInfo(res, entityClass, mainColor, subColor));
        }
    }

    public static void registerEntity(IForgeRegistry<EntityEntry> registry, Class<? extends Entity> entityClass, String name, int trackingDistance) {
        int entityId = nextEntityId();
        ResourceLocation res = new ResourceLocation(MowziesMobs.MODID, name);
        registry.register(EntityEntryBuilder.create()
                .entity(entityClass)
                .id(res, entityId)
                .name(name)
                .tracker(trackingDistance, 1, true)
                .build());
    }

    private static int nextEntityId() {
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
