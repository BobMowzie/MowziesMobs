package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public enum EntityHandler {
    INSTANCE;

    public static final String BARAKOAYA_ID = "barakoaya";

    private static final Map<EntityType<LivingEntity>, MowzieEntityEggInfo> entityEggs = new LinkedHashMap<>();

    private static int nextEntityId;

    public static EntityType<EntityFoliaath> FOLIAATH;
    public static EntityType<EntityBabyFoliaath> BABY_FOLIAATH;
    public static EntityType<EntityWroughtnaut> WROUGHTNAUT;
    public static EntityType<EntityBarakoanToBarakoana> BARAKOAN_TO_BARAKOANA;
    public static EntityType<EntityBarakoanToPlayer> BARAKOAN_TO_PLAYER;
    public static EntityType<EntityBarakoaya> BARAKOAYA;
    public static EntityType<EntityBarakoana> BARAKOANA;
    public static EntityType<EntityBarako> BARAKO;
    public static EntityType<EntityFrostmaw> FROSTMAW;
    public static EntityType<EntityGrottol> GROTTOL;
    public static EntityType<EntityLantern> LANTERN;
    public static EntityType<EntityNaga> NAGA;

    public static EntityType<EntitySunstrike> SUNSTRIKE;
    public static EntityType<EntitySolarBeam> SOLAR_BEAM;
    public static EntityType<EntityBoulder> BOULDER;
    public static EntityType<EntityAxeAttack> AXE_ATTACK;
    public static EntityType<EntityIceBreath> ICE_BREATH;
    public static EntityType<EntityIceBall> ICE_BALL;
    public static EntityType<EntityFrozenController> FROZEN_CONTROLLER;
    public static EntityType<EntityRing> RING;
    public static EntityType<EntityDart> DART;
    public static EntityType<EntityPoisonBall> POISON_BALL;
    public static EntityType<EntitySuperNova> SUPER_NOVA;
    public static EntityType<EntityBlockSwapper> BLOCK_SWAPPER;

    public static void register() {
        FOLIAATH = register("foliaath", EntityType.Builder.create(EntityFoliaath::new, EntityClassification.MONSTER).size(0.4f, 0.4f), true, 0x47CC3B, 0xC03BCC);
        BABY_FOLIAATH = register("baby_foliaath", EntityType.Builder.create(EntityBabyFoliaath::new, EntityClassification.MONSTER).size(0.5f, 2.5f));
        WROUGHTNAUT = register("ferrous_wroughtnaut", EntityType.Builder.create(EntityWroughtnaut::new, EntityClassification.MONSTER).size(2.5f, 3.5f), true, 0x8C8C8C, 0xFFFFFF);
//        BARAKOAN_TO_BARAKOANA = register("barakoan_barakoana", EntityType.Builder.create(EntityBarakoanToBarakoana::new, EntityClassification.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityWidth));
//        BARAKOAN_TO_PLAYER = register("barakoan_player", EntityType.Builder.create(EntityBarakoanToPlayer::new, EntityClassification.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityWidth));
        BARAKOAYA = register("barakoaya", EntityType.Builder.create(EntityBarakoaya::new, EntityClassification.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight), true, 0xBA6656, 0xFAFA78);
        BARAKOANA = register("barakoana", EntityType.Builder.create(EntityBarakoana::new, EntityClassification.MONSTER).size(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight), true, 0xBA6656, 0xFAFA78);
        BARAKO = register("barako", EntityType.Builder.create(EntityBarako::new, EntityClassification.MONSTER).size(1.5f, 2.4f), true, 0xBA6656, 0xFFFF00);
        FROSTMAW = register("frostmaw", EntityType.Builder.create(EntityFrostmaw::new, EntityClassification.MONSTER).size(4f, 4f), true, 0xf7faff, 0xafcdff);
        GROTTOL = register("grottol", EntityType.Builder.create(EntityGrottol::new, EntityClassification.MONSTER).size(0.9F, 1.2F), true, 0x777777, 0xbce0ff);
        LANTERN = register("lantern", EntityType.Builder.create(EntityLantern::new, EntityClassification.AMBIENT).size(1.0f, 1.0f), true, 0x6dea00, 0x235a10);
        NAGA = register("naga", EntityType.Builder.create(EntityNaga::new, EntityClassification.MONSTER).size(3.0f, 1.0f).setTrackingRange(128), true, 0x154850, 0x8dd759);

        /*SUNSTRIKE = register("sunstrike", EntityType.Builder.create(EntitySunstrike::new, EntityClassification.MISC).disableSummoning().size(0.1F, 0.1F));
        SOLAR_BEAM = register("solar_beam", EntityType.Builder.create(EntitySolarBeam::new, EntityClassification.MISC).disableSummoning().size(0.1F, 0.1F));
        BOULDER = register("boulder", EntityType.Builder.create(EntityBoulder::new, EntityClassification.MISC).disableSummoning().size(10, 10));
        AXE_ATTACK = register("axe_attack", EntityType.Builder.create(EntityAxeAttack::new, EntityClassification.MISC).disableSummoning().size(1f, 1f));
        ICE_BREATH = register("ice_breath", EntityType.Builder.create(EntityIceBreath::new, EntityClassification.MISC).disableSummoning().size(0F, 0F));
        ICE_BALL = register("ice_ball", EntityType.Builder.create(EntityIceBall::new, EntityClassification.MISC).disableSummoning().size(0.5F, 0.5F));
        FROZEN_CONTROLLER = register("frozen_controller", EntityType.Builder.create(EntityFrozenController::new, EntityClassification.MISC).disableSummoning().size(0, 0));
        RING = register("ring", EntityType.Builder.create(EntityRing::new, EntityClassification.MISC).disableSummoning().size(1F, 1F));
        DART = register("dart", EntityType.Builder.create(EntityDart::new, EntityClassification.MISC).disableSummoning().size(0.5F, 0.5F));
        POISON_BALL = register("poison_ball", EntityType.Builder.create(EntityPoisonBall::new, EntityClassification.MISC).disableSummoning().size(0.5F, 0.5F));
        SUPER_NOVA = register("super_nova", EntityType.Builder.create(EntitySuperNova::new, EntityClassification.MISC).disableSummoning().size(1, 1));
        BLOCK_SWAPPER = register("block_swapper", EntityType.Builder.create(EntityBlockSwapper::new, EntityClassification.MISC).disableSummoning().size(1, 1));*/
    }

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        return register(key, builder, false, 0, 0);
    }

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder, boolean addEgg, int mainColor, int subColor) {
        if (addEgg) {
//            entityEggs.put(res, new MowzieEntityEggInfo(res, entityClass, mainColor, subColor)); TODO
        }
        return Registry.register(Registry.ENTITY_TYPE, key, builder.build(key));
    }

    private static int nextEntityId() {
        return nextEntityId++;
    }

    public MowzieEntityEggInfo getEntityEggInfo(EntityType<?> id) {
        return entityEggs.get(id);
    }

    public boolean hasEntityEggInfo(EntityType<?> type) {
        return entityEggs.containsKey(type);
    }

    public MobEntity createEntity(EntityType<?> type, World world) {
        MobEntity entity = null;
        try {
            Class<? extends MobEntity> clazz = entityEggs.get(type).clazz;
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
