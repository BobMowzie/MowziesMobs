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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITIES, MowziesMobs.MODID);

    public static final RegistryObject<EntityType<EntityFoliaath>> FOLIAATH = REG.register("foliaath", () -> EntityType.Builder.of(EntityFoliaath::new, MobCategory.MONSTER).sized(0.5f, 2.5f).build(new ResourceLocation(MowziesMobs.MODID, "foliaath").toString()));
    public static final RegistryObject<EntityType<EntityBabyFoliaath>> BABY_FOLIAATH = REG.register("baby_foliaath", () -> EntityType.Builder.of(EntityBabyFoliaath::new, MobCategory.MONSTER).sized(0.4f, 0.4f).build(new ResourceLocation(MowziesMobs.MODID, "baby_foliaath").toString()));
    public static final RegistryObject<EntityType<EntityWroughtnaut>> WROUGHTNAUT = REG.register("ferrous_wroughtnaut", () -> EntityType.Builder.of(EntityWroughtnaut::new, MobCategory.MONSTER).sized(2.5f, 3.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "ferrous_wroughtnaut").toString()));
    private static EntityType.Builder<EntityBarakoanToBarakoana> barakoanToBarakoanaBuilder() {
        return EntityType.Builder.of(EntityBarakoanToBarakoana::new, MobCategory.MONSTER);
    }
    public static final RegistryObject<EntityType<EntityBarakoanToBarakoana>> BARAKOAN_TO_BARAKOANA = REG.register("barakoan_barakoana", () -> barakoanToBarakoanaBuilder().sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoan_barakoana").toString()));
    private static EntityType.Builder<EntityBarakoanToPlayer> barakoanToPlayerBuilder() {
        return EntityType.Builder.of(EntityBarakoanToPlayer::new, MobCategory.MONSTER);
    }
    public static final RegistryObject<EntityType<EntityBarakoanToPlayer>> BARAKOAN_TO_PLAYER = REG.register("barakoan_player", () -> barakoanToPlayerBuilder().sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoan_player").toString()));
    private static EntityType.Builder<EntityBarakoayaToPlayer> barakoayaToPlayerBuilder() {
        return EntityType.Builder.of(EntityBarakoayaToPlayer::new, MobCategory.MONSTER);
    }
    public static final RegistryObject<EntityType<EntityBarakoayaToPlayer>> BARAKOAYA_TO_PLAYER = REG.register("barakoa_sunblocker_player", () -> barakoayaToPlayerBuilder().sized(MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoa_sunblocker_player").toString()));
    public static final RegistryObject<EntityType<EntityBarakoaVillager>> BARAKOA_VILLAGER = REG.register("barakoaya", () -> EntityType.Builder.of(EntityBarakoaVillager::new, MobCategory.MONSTER).sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoaya").toString()));
    public static final RegistryObject<EntityType<EntityBarakoana>> BARAKOANA = REG.register("barakoana", () -> EntityType.Builder.of(EntityBarakoana::new, MobCategory.MONSTER).sized(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoana").toString()));
    public static final RegistryObject<EntityType<EntityBarakoaya>> BARAKOAYA = REG.register("barakoa_sunblocker", () -> EntityType.Builder.of(EntityBarakoaya::new, MobCategory.MONSTER).sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barakoa_sunblocker").toString()));
    public static final RegistryObject<EntityType<EntityBarako>> BARAKO = REG.register("barako", () -> EntityType.Builder.of(EntityBarako::new, MobCategory.MONSTER).sized(1.5f, 2.4f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "barako").toString()));
    public static final RegistryObject<EntityType<EntityFrostmaw>> FROSTMAW = REG.register("frostmaw", () -> EntityType.Builder.of(EntityFrostmaw::new, MobCategory.MONSTER).sized(4f, 4f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "frostmaw").toString()));
    public static final RegistryObject<EntityType<EntityGrottol>> GROTTOL = REG.register("grottol", () -> EntityType.Builder.of(EntityGrottol::new, MobCategory.MONSTER).sized(0.9F, 1.2F).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "grottol").toString()));
    public static final RegistryObject<EntityType<EntityLantern>> LANTERN = REG.register("lantern", () -> EntityType.Builder.of(EntityLantern::new, MobCategory.AMBIENT).sized(1.0f, 1.0f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "lantern").toString()));
    public static final RegistryObject<EntityType<EntityNaga>> NAGA = REG.register("naga", () -> EntityType.Builder.of(EntityNaga::new, MobCategory.MONSTER).sized(3.0f, 1.0f).setTrackingRange(128).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "naga").toString()));
//    public static final RegistryObject<EntityType<EntitySculptor> SCULPTOR = register("sculptor", EntityType.Builder.create(EntitySculptor::new, EntityClassification.MISC).size(1.0f, 2.0f).setUpdateInterval(1).build(null));

    private static EntityType.Builder<EntitySunstrike> sunstrikeBuilder() {
        return EntityType.Builder.of(EntitySunstrike::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntitySunstrike>> SUNSTRIKE = REG.register("sunstrike", () -> sunstrikeBuilder().sized(0.1F, 0.1F).build(new ResourceLocation(MowziesMobs.MODID, "sunstrike").toString()));
    private static EntityType.Builder<EntitySolarBeam> solarBeamBuilder() {
        return EntityType.Builder.of(EntitySolarBeam::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntitySolarBeam>> SOLAR_BEAM = REG.register("solar_beam", () -> solarBeamBuilder().sized(0.1F, 0.1F).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "solar_beam").toString()));
    private static EntityType.Builder<EntityBoulder> boulderBuilder() {
        return EntityType.Builder.of(EntityBoulder::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_SMALL = REG.register("boulder_small", () -> boulderBuilder().sized(1, 1).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_small").toString()));;
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_MEDIUM = REG.register("boulder_medium", () -> boulderBuilder().sized(2, 1.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_medium").toString()));
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_LARGE = REG.register("boulder_large", () -> boulderBuilder().sized(3, 2.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_large").toString()));
    public static final RegistryObject<EntityType<EntityBoulder>> BOULDER_HUGE = REG.register("boulder_huge", () -> boulderBuilder().sized(4, 3.5f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "boulder_huge").toString()));
    public static final RegistryObject<EntityType<EntityBoulder>>[] BOULDERS = new RegistryObject[] {BOULDER_SMALL, BOULDER_MEDIUM, BOULDER_LARGE, BOULDER_HUGE};
    private static EntityType.Builder<EntityAxeAttack> axeAttackBuilder() {
        return EntityType.Builder.of(EntityAxeAttack::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityAxeAttack>> AXE_ATTACK = REG.register("axe_attack", () -> axeAttackBuilder().sized(1f, 1f).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "axe_attack").toString()));
    private static EntityType.Builder<EntityIceBreath> iceBreathBuilder() {
        return EntityType.Builder.of(EntityIceBreath::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityIceBreath>> ICE_BREATH = REG.register("ice_breath", () -> iceBreathBuilder().sized(0F, 0F).setUpdateInterval(1).build(new ResourceLocation(MowziesMobs.MODID, "ice_breath").toString()));
    private static EntityType.Builder<EntityIceBall> iceBallBuilder() {
        return EntityType.Builder.of(EntityIceBall::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityIceBall>> ICE_BALL = REG.register("ice_ball", () -> iceBallBuilder().sized(0.5F, 0.5F).setUpdateInterval(20).build(new ResourceLocation(MowziesMobs.MODID, "ice_ball").toString()));
    private static EntityType.Builder<EntityFrozenController> frozenControllerBuilder() {
        return EntityType.Builder.of(EntityFrozenController::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityFrozenController>> FROZEN_CONTROLLER = REG.register("frozen_controller", () -> frozenControllerBuilder().noSummon().sized(0, 0).build(new ResourceLocation(MowziesMobs.MODID, "frozen_controller").toString()));
    private static EntityType.Builder<EntityDart> dartBuilder() {
        return EntityType.Builder.of(EntityDart::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityDart>> DART = REG.register("dart", () -> dartBuilder().noSummon().sized(0.5F, 0.5F).setUpdateInterval(20).build(new ResourceLocation(MowziesMobs.MODID, "dart").toString()));
    private static EntityType.Builder<EntityPoisonBall> poisonBallBuilder() {
        return EntityType.Builder.of(EntityPoisonBall::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityPoisonBall>> POISON_BALL = REG.register("poison_ball", () -> poisonBallBuilder().sized(0.5F, 0.5F).setUpdateInterval(20).build(new ResourceLocation(MowziesMobs.MODID, "poison_ball").toString()));
    private static EntityType.Builder<EntitySuperNova> superNovaBuilder() {
        return EntityType.Builder.of(EntitySuperNova::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntitySuperNova>> SUPER_NOVA = REG.register("super_nova", () -> superNovaBuilder().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(MowziesMobs.MODID, "super_nova").toString()));
    private static EntityType.Builder<EntityFallingBlock> fallingBlockBuilder() {
        return EntityType.Builder.of(EntityFallingBlock::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityFallingBlock>> FALLING_BLOCK = REG.register("falling_block", () -> fallingBlockBuilder().sized(1, 1).build(new ResourceLocation(MowziesMobs.MODID, "falling_block").toString()));
    private static EntityType.Builder<EntityBlockSwapper> blockSwapperBuilder() {
        return EntityType.Builder.of(EntityBlockSwapper::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityBlockSwapper>> BLOCK_SWAPPER = REG.register("block_swapper", () -> blockSwapperBuilder().noSummon().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(MowziesMobs.MODID, "block_swapper").toString()));
    private static EntityType.Builder<EntityCameraShake> cameraShakeBuilder() {
        return EntityType.Builder.of(EntityCameraShake::new, MobCategory.MISC);
    }
    public static final RegistryObject<EntityType<EntityCameraShake>> CAMERA_SHAKE = REG.register("camera_shake", () -> cameraShakeBuilder().sized(1, 1).setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(MowziesMobs.MODID, "camera_shake").toString()));

    @SubscribeEvent
    public static void onCreateAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.FOLIAATH.get(), EntityFoliaath.createAttributes().build());
        event.put(EntityHandler.BABY_FOLIAATH.get(), EntityBabyFoliaath.createAttributes().build());
        event.put(EntityHandler.WROUGHTNAUT.get(), EntityWroughtnaut.createAttributes().build());
        event.put(EntityHandler.BARAKOANA.get(), EntityBarakoana.createAttributes().build());
        event.put(EntityHandler.BARAKOA_VILLAGER.get(), EntityBarakoa.createAttributes().build());
        event.put(EntityHandler.BARAKOAN_TO_PLAYER.get(), EntityBarakoanToPlayer.createAttributes().build());
        event.put(EntityHandler.BARAKOAYA_TO_PLAYER.get(), EntityBarakoanToPlayer.createAttributes().build());
        event.put(EntityHandler.BARAKOAN_TO_BARAKOANA.get(), EntityBarakoa.createAttributes().build());
        event.put(EntityHandler.BARAKOAYA.get(), EntityBarakoa.createAttributes().build());
        event.put(EntityHandler.BARAKO.get(), EntityBarako.createAttributes().build());
        event.put(EntityHandler.FROSTMAW.get(), EntityFrostmaw.createAttributes().build());
        event.put(EntityHandler.NAGA.get(), EntityNaga.createAttributes().build());
        event.put(EntityHandler.LANTERN.get(), EntityLantern.createAttributes().build());
        event.put(EntityHandler.GROTTOL.get(), EntityGrottol.createAttributes().build());
//        event.put(EntityHandler.SCULPTOR.get(), EntitySculptor.createAttributes().create());
    }
}
