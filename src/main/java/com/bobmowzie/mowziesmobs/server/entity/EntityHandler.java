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
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public enum EntityHandler {
    INSTANCE;

    public static final String BARAKOAYA_ID = "barakoaya";

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
    public static EntityType<EntityBoulder> BOULDER_SMALL;
    public static EntityType<EntityBoulder> BOULDER_MEDIUM;
    public static EntityType<EntityBoulder> BOULDER_LARGE;
    public static EntityType<EntityBoulder> BOULDER_HUGE;
    public static EntityType<EntityBoulder>[] BOULDERS;
    public static EntityType<EntityAxeAttack> AXE_ATTACK;
    public static EntityType<EntityIceBreath> ICE_BREATH;
    public static EntityType<EntityIceBall> ICE_BALL;
    public static EntityType<EntityFrozenController> FROZEN_CONTROLLER;
    public static EntityType<EntityDart> DART;
    public static EntityType<EntityPoisonBall> POISON_BALL;
    public static EntityType<EntitySuperNova> SUPER_NOVA;
    public static EntityType<EntityFallingBlock> FALLING_BLOCK;
    public static EntityType<EntityBlockSwapper> BLOCK_SWAPPER;

    public static void register() {
        // Mobs
        FOLIAATH = register("foliaath", EntityType.Builder.create(EntityFoliaath::new, EntityClassification.MONSTER).size(0.5f, 2.5f));

        BABY_FOLIAATH = register("baby_foliaath", EntityType.Builder.create(EntityBabyFoliaath::new, EntityClassification.MONSTER).size(0.4f, 0.4f));

        WROUGHTNAUT = register("ferrous_wroughtnaut", EntityType.Builder.create(EntityWroughtnaut::new, EntityClassification.MONSTER).size(2.5f, 3.5f).setUpdateInterval(1));

        EntityType.Builder<EntityBarakoanToBarakoana> barakoanToBarakoanaBuilder = EntityType.Builder.create(EntityBarakoanToBarakoana::new, EntityClassification.MONSTER);
        BARAKOAN_TO_BARAKOANA = register("barakoan_barakoana", barakoanToBarakoanaBuilder.size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));

        EntityType.Builder<EntityBarakoanToPlayer> barakoanToPlayerBuilder = EntityType.Builder.create(EntityBarakoanToPlayer::new, EntityClassification.MONSTER);
        BARAKOAN_TO_PLAYER = register("barakoan_player", barakoanToPlayerBuilder.size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));

        BARAKOAYA = register("barakoaya", EntityType.Builder.create(EntityBarakoaya::new, EntityClassification.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));

        BARAKOANA = register("barakoana", EntityType.Builder.create(EntityBarakoana::new, EntityClassification.MONSTER).size(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight).setUpdateInterval(1));

        BARAKO = register("barako", EntityType.Builder.create(EntityBarako::new, EntityClassification.MONSTER).size(1.5f, 2.4f).setUpdateInterval(1));

        FROSTMAW = register("frostmaw", EntityType.Builder.create(EntityFrostmaw::new, EntityClassification.MONSTER).size(4f, 4f).setUpdateInterval(1));

        GROTTOL = register("grottol", EntityType.Builder.create(EntityGrottol::new, EntityClassification.MONSTER).size(0.9F, 1.2F).setUpdateInterval(1));

        LANTERN = register("lantern", EntityType.Builder.create(EntityLantern::new, EntityClassification.AMBIENT).size(1.0f, 1.0f).setUpdateInterval(1));

        NAGA = register("naga", EntityType.Builder.create(EntityNaga::new, EntityClassification.MONSTER).size(3.0f, 1.0f).setTrackingRange(128).setUpdateInterval(1));

        // Non-mobs
        EntityType.Builder<EntitySunstrike> sunstrikeBuilder = EntityType.Builder.create(EntitySunstrike::new, EntityClassification.MISC);
        SUNSTRIKE = register("sunstrike", sunstrikeBuilder.disableSummoning().size(0.1F, 0.1F));

        EntityType.Builder<EntitySolarBeam> solarBeamBuilder = EntityType.Builder.create(EntitySolarBeam::new, EntityClassification.MISC);
        SOLAR_BEAM = register("solar_beam", solarBeamBuilder.disableSummoning().size(0.1F, 0.1F).setUpdateInterval(1));

        EntityType.Builder<EntityBoulder> boulderBuilder = EntityType.Builder.create(EntityBoulder::new, EntityClassification.MISC);
        BOULDER_SMALL = register("boulder_small", boulderBuilder.disableSummoning().size(1, 1).setUpdateInterval(1));
        BOULDER_MEDIUM = register("boulder_medium", boulderBuilder.disableSummoning().size(2, 1.5f).setUpdateInterval(1));
        BOULDER_LARGE = register("boulder_large", boulderBuilder.disableSummoning().size(3, 2.5f).setUpdateInterval(1));
        BOULDER_HUGE = register("boulder_huge", boulderBuilder.disableSummoning().size(4, 3.5f).setUpdateInterval(1));
        BOULDERS = new EntityType[] {BOULDER_SMALL, BOULDER_MEDIUM, BOULDER_LARGE, BOULDER_HUGE};

        EntityType.Builder<EntityAxeAttack> axeAttackBuilder = EntityType.Builder.create(EntityAxeAttack::new, EntityClassification.MISC);
        AXE_ATTACK = register("axe_attack", axeAttackBuilder.disableSummoning().size(1f, 1f).setUpdateInterval(1));

        EntityType.Builder<EntityIceBreath> iceBreathBuilder = EntityType.Builder.create(EntityIceBreath::new, EntityClassification.MISC);
        ICE_BREATH = register("ice_breath", iceBreathBuilder.disableSummoning().size(0F, 0F).setUpdateInterval(1));

        EntityType.Builder<EntityIceBall> iceBallBuilder = EntityType.Builder.create(EntityIceBall::new, EntityClassification.MISC);
        ICE_BALL = register("ice_ball", iceBallBuilder.disableSummoning().size(0.5F, 0.5F).setUpdateInterval(20));

        EntityType.Builder<EntityFrozenController> frozenControllerBuilder = EntityType.Builder.create(EntityFrozenController::new, EntityClassification.MISC);
        FROZEN_CONTROLLER = register("frozen_controller", frozenControllerBuilder.disableSummoning().size(0, 0));

        EntityType.Builder<EntityDart> dartBuilder = EntityType.Builder.create(EntityDart::new, EntityClassification.MISC);
        DART = register("dart", dartBuilder.disableSummoning().size(0.5F, 0.5F).setUpdateInterval(20));

        EntityType.Builder<EntityPoisonBall> poisonBallBuilder = EntityType.Builder.create(EntityPoisonBall::new, EntityClassification.MISC);
        POISON_BALL = register("poison_ball", poisonBallBuilder.disableSummoning().size(0.5F, 0.5F).setUpdateInterval(20));

        EntityType.Builder<EntitySuperNova> superNovaBuilder = EntityType.Builder.create(EntitySuperNova::new, EntityClassification.MISC);
        SUPER_NOVA = register("super_nova", superNovaBuilder.disableSummoning().size(1, 1).setUpdateInterval(Integer.MAX_VALUE));

        EntityType.Builder<EntityFallingBlock> fallingBlockBuilder = EntityType.Builder.create(EntityFallingBlock::new, EntityClassification.MISC);
        FALLING_BLOCK = register("falling_block", fallingBlockBuilder.disableSummoning().size(1, 1));

        EntityType.Builder<EntityBlockSwapper> blockSwapperBuilder = EntityType.Builder.create(EntityBlockSwapper::new, EntityClassification.MISC);
        BLOCK_SWAPPER = register("block_swapper", blockSwapperBuilder.disableSummoning().size(1, 1).setUpdateInterval(Integer.MAX_VALUE));

        ForgeRegistries.ENTITIES.registerAll(
            FOLIAATH,
            BABY_FOLIAATH,
            WROUGHTNAUT,
            BARAKOAN_TO_BARAKOANA,
            BARAKOAN_TO_PLAYER,
            BARAKOAYA,
            BARAKOANA,
            BARAKO,
            FROSTMAW,
            GROTTOL,
            LANTERN,
            NAGA,
            SUNSTRIKE,
            SOLAR_BEAM,
            BOULDER_SMALL,
            BOULDER_MEDIUM,
            BOULDER_LARGE,
            BOULDER_HUGE,
            AXE_ATTACK,
            ICE_BREATH,
            ICE_BALL,
            FROZEN_CONTROLLER,
            DART,
            POISON_BALL,
            SUPER_NOVA,
            FALLING_BLOCK,
            BLOCK_SWAPPER
        );
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceLocation regName = new ResourceLocation(MowziesMobs.MODID, name);
        return (EntityType<T>) builder.build(name).setRegistryName(regName);
    }

    public static void initializeAttributes() {
        GlobalEntityTypeAttributes.put(FOLIAATH, EntityFoliaath.createAttributes().create());
        GlobalEntityTypeAttributes.put(BABY_FOLIAATH, EntityBabyFoliaath.createAttributes().create());
        GlobalEntityTypeAttributes.put(WROUGHTNAUT, EntityWroughtnaut.createAttributes().create());
        GlobalEntityTypeAttributes.put(BARAKOANA, EntityBarakoana.createAttributes().create());
        GlobalEntityTypeAttributes.put(BARAKOAYA, EntityBarakoa.createAttributes().create());
        GlobalEntityTypeAttributes.put(BARAKOAN_TO_PLAYER, EntityBarakoanToPlayer.createAttributes().create());
        GlobalEntityTypeAttributes.put(BARAKOAN_TO_BARAKOANA, EntityBarakoa.createAttributes().create());
        GlobalEntityTypeAttributes.put(BARAKO, EntityBarako.createAttributes().create());
        GlobalEntityTypeAttributes.put(FROSTMAW, EntityFrostmaw.createAttributes().create());
        GlobalEntityTypeAttributes.put(NAGA, EntityNaga.createAttributes().create());
        GlobalEntityTypeAttributes.put(LANTERN, EntityLantern.createAttributes().create());
        GlobalEntityTypeAttributes.put(GROTTOL, EntityGrottol.createAttributes().create());
    }
}
