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
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITIES, MowziesMobs.MODID);

    public static final RegistryObject<EntityType<EntityFoliaath>> FOLIAATH = REG.register("foliaath", () -> EntityType.Builder.create(EntityFoliaath::new, EntityClassification.MONSTER).size(0.5f, 2.5f).build(new ResourceLocation(MowziesMobs.MODID, "foliaath").toString()));
    public static final RegistryObject<EntityType<EntityBabyFoliaath>> BABY_FOLIAATH = REG.register("baby_foliaath", () -> EntityType.Builder.create(EntityBabyFoliaath::new, EntityClassification.MONSTER).size(0.4f, 0.4f).build(new ResourceLocation(MowziesMobs.MODID, "baby_foliaath").toString()));
    public static final RegistryObject<EntityType<EntityWroughtnaut>> WROUGHTNAUT = REG.register("ferrous_wroughtnaut", () ->  EntityType.Builder.create(EntityWroughtnaut::new, EntityClassification.MONSTER).size(2.5f, 3.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "ferrous_wroughtnaut").toString()));
    private static final EntityType.Builder<EntityBarakoanToBarakoana> barakoanToBarakoanaBuilder = EntityType.Builder.create(EntityBarakoanToBarakoana::new, EntityClassification.MONSTER);
    public static final RegistryObject<EntityType<EntityBarakoanToBarakoana>> BARAKOAN_TO_BARAKOANA = REG.register("barakoan_barakoana", () ->  barakoanToBarakoanaBuilder.size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoan_barakoana").toString()));
    private static final EntityType.Builder<EntityBarakoanToPlayer> barakoanToPlayerBuilder = EntityType.Builder.create(EntityBarakoanToPlayer::new, EntityClassification.MONSTER);
    public static final RegistryObject<EntityType<EntityBarakoanToPlayer>> BARAKOAN_TO_PLAYER = REG.register("barakoan_player", () ->  barakoanToPlayerBuilder.size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoan_player").toString()));
    private static final EntityType.Builder<EntityBarakoayaToPlayer> barakoayaToPlayerBuilder = EntityType.Builder.create(EntityBarakoayaToPlayer::new, EntityClassification.MONSTER);
    public static final RegistryObject<EntityType<EntityBarakoayaToPlayer>> BARAKOAYA_TO_PLAYER = REG.register("barakoa_sunblocker_player", () ->  barakoayaToPlayerBuilder.size(MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoa_sunblocker_player").toString()));
    public static final RegistryObject<EntityType<EntityBarakoaVillager>> BARAKOA_VILLAGER = REG.register("barakoaya", () ->  EntityType.Builder.create(EntityBarakoaVillager::new, EntityClassification.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoaya").toString()));
    public static final RegistryObject<EntityType<EntityBarakoana>> BARAKOANA = REG.register("barakoana", () ->  EntityType.Builder.create(EntityBarakoana::new, EntityClassification.MONSTER).size(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoana").toString()));
    public static final RegistryObject<EntityType<EntityBarakoaya>> BARAKOAYA = REG.register("barakoa_sunblocker", () ->  EntityType.Builder.create(EntityBarakoaya::new, EntityClassification.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoa_sunblocker").toString()));
    public static final RegistryObject<EntityType<EntityBarako>> BARAKO = REG.register("barako", () ->  EntityType.Builder.create(EntityBarako::new, EntityClassification.MONSTER).size(1.5f, 2.4f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barako").toString()));
    public static final RegistryObject<EntityType<EntityFrostmaw>> FROSTMAW = REG.register("frostmaw", () ->  EntityType.Builder.create(EntityFrostmaw::new, EntityClassification.MONSTER).size(4f, 4f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "frostmaw").toString()));
    public static final RegistryObject<EntityType<EntityGrottol>> GROTTOL = REG.register("grottol", () ->  EntityType.Builder.create(EntityGrottol::new, EntityClassification.MONSTER).size(0.9F, 1.2F).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "grottol").toString()));
    public static final RegistryObject<EntityType<EntityLantern>> LANTERN = REG.register("lantern", () ->  EntityType.Builder.create(EntityLantern::new, EntityClassification.AMBIENT).size(1.0f, 1.0f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "lantern").toString()));
    public static final RegistryObject<EntityType<EntityNaga>> NAGA = REG.register("naga", () ->  EntityType.Builder.create(EntityNaga::new, EntityClassification.MONSTER).size(3.0f, 1.0f).setTrackingRange(128).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "naga").toString()));
//    public static final RegistryObject<EntityType<EntitySculptor> SCULPTOR = register("sculptor", EntityType.Builder.create(EntitySculptor::new, EntityClassification.MISC).size(1.0f, 2.0f).setUpdateInterval(1).build(null));

    private static final EntityType.Builder<EntitySunstrike> sunstrikeBuilder = EntityType.Builder.create(EntitySunstrike::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntitySunstrike>> SUNSTRIKE = REG.register("sunstrike", () ->  sunstrikeBuilder.size(0.1F, 0.1F).build(new ResourceLocation(MowziesMobs.MODID, "sunstrike").toString()));
    private static final EntityType.Builder<EntitySolarBeam> solarBeamBuilder = EntityType.Builder.create(EntitySolarBeam::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntitySolarBeam>> SOLAR_BEAM = REG.register("solar_beam", () ->  solarBeamBuilder.size(0.1F, 0.1F).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "solar_beam").toString()));
    private static final EntityType.Builder<EntityBoulder> boulderBuilder = EntityType.Builder.create(EntityBoulder::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_SMALL = REG.register("boulder_small", () ->  boulderBuilder.size(1, 1).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_small").toString()));;
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_MEDIUM = REG.register("boulder_medium", () ->  boulderBuilder.size(2, 1.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_medium").toString()));
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_LARGE = REG.register("boulder_large", () ->  boulderBuilder.size(3, 2.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_large").toString()));
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_HUGE = REG.register("boulder_huge", () ->  boulderBuilder.size(4, 3.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_huge").toString()));
    public static final RegistryObject<EntityType<EntityBoulder>>[] BOULDERS = new RegistryObject[] {BOULDER_SMALL, BOULDER_MEDIUM, BOULDER_LARGE, BOULDER_HUGE};
    private static final EntityType.Builder<EntityAxeAttack> axeAttackBuilder = EntityType.Builder.create(EntityAxeAttack::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityAxeAttack>> AXE_ATTACK = REG.register("axe_attack", () ->  axeAttackBuilder.size(1f, 1f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "axe_attack").toString()));
    private static final EntityType.Builder<EntityIceBreath> iceBreathBuilder = EntityType.Builder.create(EntityIceBreath::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityIceBreath>> ICE_BREATH = REG.register("ice_breath", () ->  iceBreathBuilder.size(0F, 0F).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "ice_breath").toString()));
    private static final EntityType.Builder<EntityIceBall> iceBallBuilder = EntityType.Builder.create(EntityIceBall::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityIceBall>> ICE_BALL = REG.register("ice_ball", () ->  iceBallBuilder.size(0.5F, 0.5F).setUpdateInterval(20).build(new ResourceLocation(MowziesMobs.MODID, "ice_ball").toString()));
    private static final EntityType.Builder<EntityFrozenController> frozenControllerBuilder = EntityType.Builder.create(EntityFrozenController::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityFrozenController>> FROZEN_CONTROLLER = REG.register("frozen_controller", () ->  frozenControllerBuilder.disableSummoning().size(0, 0).build(new ResourceLocation(MowziesMobs.MODID, "frozen_controller").toString()));
    private static final EntityType.Builder<EntityDart> dartBuilder = EntityType.Builder.create(EntityDart::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityDart>> DART = REG.register("dart", () ->  dartBuilder.disableSummoning().size(0.5F, 0.5F).setUpdateInterval(20).build(new ResourceLocation(MowziesMobs.MODID, "dart").toString()));
    private static final EntityType.Builder<EntityPoisonBall> poisonBallBuilder = EntityType.Builder.create(EntityPoisonBall::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityPoisonBall>> POISON_BALL = REG.register("poison_ball", () ->  poisonBallBuilder.size(0.5F, 0.5F).setUpdateInterval(20).build(new ResourceLocation(MowziesMobs.MODID, "poison_ball").toString()));
    private static final EntityType.Builder<EntitySuperNova> superNovaBuilder = EntityType.Builder.create(EntitySuperNova::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntitySuperNova>> SUPER_NOVA = REG.register("super_nova", () ->  superNovaBuilder.size(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(MowziesMobs.MODID, "super_nova").toString()));
    private static final EntityType.Builder<EntityFallingBlock> fallingBlockBuilder = EntityType.Builder.create(EntityFallingBlock::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityFallingBlock>> FALLING_BLOCK = REG.register("falling_block", () ->  fallingBlockBuilder.size(1, 1).build(new ResourceLocation(MowziesMobs.MODID, "falling_block").toString()));
    private static final EntityType.Builder<EntityBlockSwapper> blockSwapperBuilder = EntityType.Builder.create(EntityBlockSwapper::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityBlockSwapper>> BLOCK_SWAPPER = REG.register("block_swapper", () ->  blockSwapperBuilder.disableSummoning().size(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(MowziesMobs.MODID, "block_swapper").toString()));
    private static final EntityType.Builder<EntityCameraShake> cameraShakeBuilder = EntityType.Builder.create(EntityCameraShake::new, EntityClassification.MISC);
    public static final RegistryObject<EntityType<EntityCameraShake>> CAMERA_SHAKE = REG.register("camera_shake", () ->  cameraShakeBuilder.disableSummoning().size(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(MowziesMobs.MODID, "camera_shake").toString()));

    @SubscribeEvent
    public static void onCreateAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.FOLIAATH.get(), EntityFoliaath.createAttributes().create());
        event.put(EntityHandler.BABY_FOLIAATH.get(), EntityBabyFoliaath.createAttributes().create());
        event.put(EntityHandler.WROUGHTNAUT.get(), EntityWroughtnaut.createAttributes().create());
        event.put(EntityHandler.BARAKOANA.get(), EntityBarakoana.createAttributes().create());
        event.put(EntityHandler.BARAKOA_VILLAGER.get(), EntityBarakoa.createAttributes().create());
        event.put(EntityHandler.BARAKOAN_TO_PLAYER.get(), EntityBarakoanToPlayer.createAttributes().create());
        event.put(EntityHandler.BARAKOAYA_TO_PLAYER.get(), EntityBarakoanToPlayer.createAttributes().create());
        event.put(EntityHandler.BARAKOAN_TO_BARAKOANA.get(), EntityBarakoa.createAttributes().create());
        event.put(EntityHandler.BARAKOAYA.get(), EntityBarakoa.createAttributes().create());
        event.put(EntityHandler.BARAKO.get(), EntityBarako.createAttributes().create());
        event.put(EntityHandler.FROSTMAW.get(), EntityFrostmaw.createAttributes().create());
        event.put(EntityHandler.NAGA.get(), EntityNaga.createAttributes().create());
        event.put(EntityHandler.LANTERN.get(), EntityLantern.createAttributes().create());
        event.put(EntityHandler.GROTTOL.get(), EntityGrottol.createAttributes().create());
//        event.put(EntityHandler.SCULPTOR.get(), EntitySculptor.createAttributes().create());
    }
}
