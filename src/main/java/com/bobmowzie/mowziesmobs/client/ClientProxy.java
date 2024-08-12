package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.block.SculptorBlockMarking;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.sound.*;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {
    private static final List<SunblockSound> sunblockSounds = new ArrayList<>();
    public static final Map<UUID, ResourceLocation> bossBarRegistryNames = new HashMap<>();

    public static final Long2ObjectMap<SculptorBlockMarking> sculptorMarkedBlocks = new Long2ObjectOpenHashMap<>();

    private Entity referencedMob = null;

    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);

        modbus.register(MMModels.class);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FrozenRenderHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(AbilityClientEventHandler.INSTANCE);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientLayerRegistry::onAddLayers);
    }

    @Override
    public void onLateInit(final IEventBus modbus) {
        ItemPropertyFunction pulling = ItemProperties.getProperty(Items.BOW, new ResourceLocation("pulling"));
        ItemProperties.register(ItemHandler.BLOWGUN.get().asItem(), new ResourceLocation("pulling"), pulling);
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getInstance().getSoundManager().play(new SunstrikeSound(strike));
    }

    @Override
    public void playIceBreathSound(Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new IceBreathSound(entity));
    }

    @Override
    public void playBoulderChargeSound(LivingEntity player) {
        Minecraft.getInstance().getSoundManager().play(new SpawnBoulderChargeSound(player));
    }

    @Override
    public void playNagaSwoopSound(EntityNaga naga) {
        Minecraft.getInstance().getSoundManager().play(new NagaSwoopSound(naga));
    }

    @Override
    public void playBlackPinkSound(AbstractMinecart entity) {
        Minecraft.getInstance().getSoundManager().play(new BlackPinkSound(entity));
    }

    @Override
    public void playSunblockSound(LivingEntity entity) {
        if (ConfigHandler.CLIENT.doUmvuthanaCraneHealSound.get()) {
            sunblockSounds.removeIf(e -> e == null || e.isStopped());
            if (sunblockSounds.size() < 10) {
                SunblockSound sunblockSound = new SunblockSound(entity);
                sunblockSounds.add(sunblockSound);
                try {
                    Minecraft.getInstance().getSoundManager().play(sunblockSound);
                } catch (ConcurrentModificationException ignored) {

                }
            }
        }
    }

    @Override
    public void playSolarBeamSound(EntitySolarBeam entity) {
        Minecraft.getInstance().getSoundManager().play(new SolarBeamSound(entity, false));
        Minecraft.getInstance().getSoundManager().play(new SolarBeamSound(entity, true));
    }
    
    @Override
    public double getLanternParticleRED() {
    	return (double)ConfigHandler.CLIENT.lanternParticleColorRED.get();
    }
    
    @Override
    public double getLanternParticleGREEN() {
    	return (double)ConfigHandler.CLIENT.lanternParticleColorGREEN.get();
    }
    
    @Override
    public double getLanternParticleBLUE() {
    	return (double)ConfigHandler.CLIENT.lanternParticleColorBLUE.get();
    }
    
    @Override
    public double getLanternGlowRED() {
    	return (double)ConfigHandler.CLIENT.lanternGlowColorRED.get();
    }
    
    @Override
    public double getLanternGlowGREEN() {
    	return (double)ConfigHandler.CLIENT.lanternGlowColorGREEN.get();
    }
    
    @Override
    public double getLanternGlowBLUE() {
    	return (double)ConfigHandler.CLIENT.lanternGlowColorBLUE.get();
    }

    @Override
    public void minecartParticles(ClientLevel world, AbstractMinecart minecart, float scale, double x, double y, double z, BlockState state, BlockPos pos) {
        final int size = 3;
        float offset =  -0.5F * scale;
        for (int ix = 0; ix < size; ix++) {
            for (int iy = 0; iy < size; iy++) {
                for (int iz = 0; iz < size; iz++) {
                    double dx = (double) ix / size * scale;
                    double dy = (double) iy / size * scale;
                    double dz = (double) iz / size * scale;
                    Vec3 minecartMotion = minecart.getDeltaMovement();
                    Minecraft.getInstance().particleEngine.add(new TerrainParticle(
                            world,
                            x + dx + offset, y + dy + offset, z + dz + offset,
                            dx + minecartMotion.x(), dy + minecartMotion.y(), dz + minecartMotion.z(),
                            state
                    ) {}.updateSprite(state, pos));
                }
            }
        }
    }

    public void setTPS(float tickRate) {

    }

    public Entity getReferencedMob() {
        return referencedMob;
    }

    public void setReferencedMob(Entity referencedMob) {
        this.referencedMob = referencedMob;
    }

    public void sculptorMarkBlock(int id, BlockPos pos) {
        SculptorBlockMarking blockMarking = sculptorMarkedBlocks.get(pos.asLong());
        if (blockMarking == null) {
            blockMarking = new SculptorBlockMarking(pos);
            sculptorMarkedBlocks.put(pos.asLong(), blockMarking);
        }
        else {
            blockMarking.resetTick();
        }
    }

    public void updateMarkedBlocks() {
        Iterator<SculptorBlockMarking> iterator = sculptorMarkedBlocks.values().iterator();

        while(iterator.hasNext()) {
            SculptorBlockMarking blockMarking = iterator.next();
            int i = blockMarking.getTicks();
            if (i > blockMarking.getDuration()) {
                iterator.remove();
            }
            blockMarking.tick();
        }
    }
}
