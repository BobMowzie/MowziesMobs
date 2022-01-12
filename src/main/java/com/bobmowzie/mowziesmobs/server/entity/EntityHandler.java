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
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.world.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityHandler {
    public static final EntityType<EntityFoliaath> FOLIAATH = register("foliaath", EntityType.Builder.of(EntityFoliaath::new, MobCategory.MONSTER).size(0.5f, 2.5f));
    public static final EntityType<EntityBabyFoliaath> BABY_FOLIAATH = register("baby_foliaath", EntityType.Builder.of(EntityBabyFoliaath::new, MobCategory.MONSTER).size(0.4f, 0.4f));
    public static final EntityType<EntityWroughtnaut> WROUGHTNAUT = register("ferrous_wroughtnaut", EntityType.Builder.of(EntityWroughtnaut::new, MobCategory.MONSTER).size(2.5f, 3.5f).setUpdateInterval(1));
    private static final EntityType.Builder<EntityBarakoanToBarakoana> barakoanToBarakoanaBuilder = EntityType.Builder.of(EntityBarakoanToBarakoana::new, MobCategory.MONSTER);
    public static final EntityType<EntityBarakoanToBarakoana> BARAKOAN_TO_BARAKOANA = register("barakoan_barakoana", barakoanToBarakoanaBuilder.size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));
    private static final EntityType.Builder<EntityBarakoanToPlayer> barakoanToPlayerBuilder = EntityType.Builder.of(EntityBarakoanToPlayer::new, MobCategory.MONSTER);
    public static final EntityType<EntityBarakoanToPlayer> BARAKOAN_TO_PLAYER = register("barakoan_player", barakoanToPlayerBuilder.size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));
    private static final EntityType.Builder<EntityBarakoayaToPlayer> barakoayaToPlayerBuilder = EntityType.Builder.of(EntityBarakoayaToPlayer::new, MobCategory.MONSTER);
    public static final EntityType<EntityBarakoayaToPlayer> BARAKOAYA_TO_PLAYER = register("barakoa_sunblocker_player", barakoayaToPlayerBuilder.size(MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight).setUpdateInterval(1));
    public static final EntityType<EntityBarakoaVillager> BARAKOA_VILLAGER = register("barakoaya", EntityType.Builder.of(EntityBarakoaVillager::new, MobCategory.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));
    public static final EntityType<EntityBarakoana> BARAKOANA = register("barakoana", EntityType.Builder.of(EntityBarakoana::new, MobCategory.MONSTER).size(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight).setUpdateInterval(1));
    public static final EntityType<EntityBarakoaya> BARAKOAYA = register("barakoa_sunblocker", EntityType.Builder.of(EntityBarakoaya::new, MobCategory.MONSTER).size(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).setUpdateInterval(1));
    public static final EntityType<EntityBarako> BARAKO = register("barako", EntityType.Builder.of(EntityBarako::new, MobCategory.MONSTER).size(1.5f, 2.4f).setUpdateInterval(1));
    public static final EntityType<EntityFrostmaw> FROSTMAW = register("frostmaw", EntityType.Builder.of(EntityFrostmaw::new, MobCategory.MONSTER).size(4f, 4f).setUpdateInterval(1));
    public static final EntityType<EntityGrottol> GROTTOL = register("grottol", EntityType.Builder.of(EntityGrottol::new, MobCategory.MONSTER).size(0.9F, 1.2F).setUpdateInterval(1));
    public static final EntityType<EntityLantern> LANTERN = register("lantern", EntityType.Builder.of(EntityLantern::new, MobCategory.AMBIENT).size(1.0f, 1.0f).setUpdateInterval(1));
    public static final EntityType<EntityNaga> NAGA = register("naga", EntityType.Builder.of(EntityNaga::new, MobCategory.MONSTER).size(3.0f, 1.0f).setTrackingRange(128).setUpdateInterval(1));
    public static final EntityType<EntitySculptor> SCULPTOR = register("sculptor", EntityType.Builder.of(EntitySculptor::new, MobCategory.MISC).size(1.0f, 2.0f).setUpdateInterval(1));

    private static final EntityType.Builder<EntitySunstrike> sunstrikeBuilder = EntityType.Builder.of(EntitySunstrike::new, MobCategory.MISC);
    public static final EntityType<EntitySunstrike> SUNSTRIKE = register("sunstrike", sunstrikeBuilder.size(0.1F, 0.1F));
    private static final EntityType.Builder<EntitySolarBeam> solarBeamBuilder = EntityType.Builder.of(EntitySolarBeam::new, MobCategory.MISC);
    public static final EntityType<EntitySolarBeam> SOLAR_BEAM = register("solar_beam", solarBeamBuilder.size(0.1F, 0.1F).setUpdateInterval(1));
    private static final EntityType.Builder<EntityBoulder> boulderBuilder = EntityType.Builder.of(EntityBoulder::new, MobCategory.MISC);
    public static final EntityType<EntityBoulder> BOULDER_SMALL = register("boulder_small", boulderBuilder.size(1, 1).setUpdateInterval(1));;
    public static final EntityType<EntityBoulder> BOULDER_MEDIUM = register("boulder_medium", boulderBuilder.size(2, 1.5f).setUpdateInterval(1));
    public static final EntityType<EntityBoulder> BOULDER_LARGE = register("boulder_large", boulderBuilder.size(3, 2.5f).setUpdateInterval(1));
    public static final EntityType<EntityBoulder> BOULDER_HUGE = register("boulder_huge", boulderBuilder.size(4, 3.5f).setUpdateInterval(1));
    public static final EntityType<EntityBoulder>[] BOULDERS = new EntityType[] {BOULDER_SMALL, BOULDER_MEDIUM, BOULDER_LARGE, BOULDER_HUGE};
    private static final EntityType.Builder<EntityAxeAttack> axeAttackBuilder = EntityType.Builder.of(EntityAxeAttack::new, MobCategory.MISC);
    public static final EntityType<EntityAxeAttack> AXE_ATTACK = register("axe_attack", axeAttackBuilder.size(1f, 1f).setUpdateInterval(1));
    private static final EntityType.Builder<EntityIceBreath> iceBreathBuilder = EntityType.Builder.of(EntityIceBreath::new, MobCategory.MISC);
    public static final EntityType<EntityIceBreath> ICE_BREATH = register("ice_breath", iceBreathBuilder.size(0F, 0F).setUpdateInterval(1));
    private static final EntityType.Builder<EntityIceBall> iceBallBuilder = EntityType.Builder.of(EntityIceBall::new, MobCategory.MISC);
    public static final EntityType<EntityIceBall> ICE_BALL = register("ice_ball", iceBallBuilder.size(0.5F, 0.5F).setUpdateInterval(20));
    private static final EntityType.Builder<EntityFrozenController> frozenControllerBuilder = EntityType.Builder.of(EntityFrozenController::new, MobCategory.MISC);
    public static final EntityType<EntityFrozenController> FROZEN_CONTROLLER = register("frozen_controller", frozenControllerBuilder.noSummon().size(0, 0));
    private static final EntityType.Builder<EntityDart> dartBuilder = EntityType.Builder.of(EntityDart::new, MobCategory.MISC);
    public static final EntityType<EntityDart> DART = register("dart", dartBuilder.noSummon().size(0.5F, 0.5F).setUpdateInterval(20));
    private static final EntityType.Builder<EntityPoisonBall> poisonBallBuilder = EntityType.Builder.of(EntityPoisonBall::new, MobCategory.MISC);
    public static final EntityType<EntityPoisonBall> POISON_BALL = register("poison_ball", poisonBallBuilder.size(0.5F, 0.5F).setUpdateInterval(20));
    private static final EntityType.Builder<EntitySuperNova> superNovaBuilder = EntityType.Builder.of(EntitySuperNova::new, MobCategory.MISC);
    public static final EntityType<EntitySuperNova> SUPER_NOVA = register("super_nova", superNovaBuilder.size(1, 1).setUpdateInterval(Integer.MAX_VALUE));
    private static final EntityType.Builder<EntityFallingBlock> fallingBlockBuilder = EntityType.Builder.of(EntityFallingBlock::new, MobCategory.MISC);
    public static final EntityType<EntityFallingBlock> FALLING_BLOCK = register("falling_block", fallingBlockBuilder.size(1, 1));
    private static final EntityType.Builder<EntityBlockSwapper> blockSwapperBuilder = EntityType.Builder.of(EntityBlockSwapper::new, MobCategory.MISC);
    public static final EntityType<EntityBlockSwapper> BLOCK_SWAPPER = register("block_swapper", blockSwapperBuilder.noSummon().size(1, 1).setUpdateInterval(Integer.MAX_VALUE));
    private static final EntityType.Builder<EntityCameraShake> cameraShakeBuilder = EntityType.Builder.of(EntityCameraShake::new, MobCategory.MISC);
    public static final EntityType<EntityCameraShake> CAMERA_SHAKE = register("camera_shake", cameraShakeBuilder.noSummon().size(1, 1).setUpdateInterval(Integer.MAX_VALUE));

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceLocation regName = new ResourceLocation(MowziesMobs.MODID, name);
        return (EntityType<T>) builder.build(name).setRegistryName(regName);
    }

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        try {
            for (Field f : EntityHandler.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof EntityType) {
                    event.getRegistry().register((EntityType) obj);
                } else if (obj instanceof EntityType[]) {
                    for (EntityType type : (EntityType[]) obj) {
                        event.getRegistry().register(type);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void onCreateAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.FOLIAATH, EntityFoliaath.createAttributes().create());
        event.put(EntityHandler.BABY_FOLIAATH, EntityBabyFoliaath.createAttributes().create());
        event.put(EntityHandler.WROUGHTNAUT, EntityWroughtnaut.createAttributes().create());
        event.put(EntityHandler.BARAKOANA, EntityBarakoana.createAttributes().create());
        event.put(EntityHandler.BARAKOA_VILLAGER, EntityBarakoa.createAttributes().create());
        event.put(EntityHandler.BARAKOAN_TO_PLAYER, EntityBarakoanToPlayer.createAttributes().create());
        event.put(EntityHandler.BARAKOAYA_TO_PLAYER, EntityBarakoanToPlayer.createAttributes().create());
        event.put(EntityHandler.BARAKOAN_TO_BARAKOANA, EntityBarakoa.createAttributes().create());
        event.put(EntityHandler.BARAKOAYA, EntityBarakoa.createAttributes().create());
        event.put(EntityHandler.BARAKO, EntityBarako.createAttributes().create());
        event.put(EntityHandler.FROSTMAW, EntityFrostmaw.createAttributes().create());
        event.put(EntityHandler.NAGA, EntityNaga.createAttributes().create());
        event.put(EntityHandler.LANTERN, EntityLantern.createAttributes().create());
        event.put(EntityHandler.GROTTOL, EntityGrottol.createAttributes().create());
        event.put(EntityHandler.SCULPTOR, EntitySculptor.createAttributes().create());
    }
}
