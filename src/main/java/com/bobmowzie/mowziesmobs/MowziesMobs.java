package com.bobmowzie.mowziesmobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.model.tools.MowzieGeoBuilder;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ability.AbilityCommonEventHandler;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionTypeHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.world.BiomeModifiersHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.ConfiguredFeatureHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw.JigsawHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.processor.ProcessorHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

@Mod(MowziesMobs.MODID)
@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class MowziesMobs {
    public static final String MODID = "mowziesmobs";
    public static final Logger LOGGER = LogManager.getLogger();
    public static ServerProxy PROXY;

    public static SimpleChannel NETWORK;

    public MowziesMobs() {
        GeckoLibMod.DISABLE_IN_DEV = true;
        MowzieGeoBuilder.registerGeoBuilder(MODID, new MowzieGeoBuilder());
        GeckoLib.initialize();

        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CreativeTabHandler.INSTANCE.onInit();
        EntityHandler.REG.register(bus);
        MMSounds.REG.register(bus);
        BlockHandler.REG.register(bus);
        BlockEntityHandler.REG.register(bus);
        ParticleHandler.REG.register(bus);
        FeatureHandler.REG.register(bus);
        ContainerHandler.REG.register(bus);
        EffectHandler.REG.register(bus);
        PotionTypeHandler.REG.register(bus);
        BiomeModifiersHandler.REG.register(bus);

        PROXY.init(bus);
        bus.<FMLCommonSetupEvent>addListener(this::init);
        bus.<FMLLoadCompleteEvent>addListener(this::init);
        bus.addListener(this::onModConfigEvent);
        bus.addListener(CapabilityHandler::registerCapabilities);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.register(new AbilityCommonEventHandler());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityHandler::attachEntityCapability);
    }
    
    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHandler.COMMON_CONFIG) {
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeed.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeed.get().floatValue();     
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackSpeed.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.durabilityValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.durability.get();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReductionValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReduction.get();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughnessValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughness.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReductionValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReduction.get();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughnessValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughness.get().floatValue(); 
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReductionValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReduction.get();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughnessValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughness.get().floatValue();
        };
    }

    public void init(final FMLCommonSetupEvent event) {
        SpawnHandler.registerSpawnPlacementTypes();
        PROXY.initNetwork();
        AdvancementHandler.preInit();
        LootTableHandler.init();
        PotionTypeHandler.init();

        event.enqueueWork(() -> {
            JigsawHandler.registerJigsawElements();
            ProcessorHandler.registerStructureProcessors();
            FeatureHandler.registerStructurePieces();
            ConfiguredFeatureHandler.registerConfiguredFeatures();
        });
    }

    private void init(FMLLoadCompleteEvent event) {
        ItemHandler.initializeAttributes();
        ItemHandler.initializeDispenserBehaviors();
        BlockHandler.init();
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.onLateInit(bus);
    }
}
