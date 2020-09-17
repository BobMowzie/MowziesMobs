package com.ilexiconn.llibrary.server;

import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.server.capability.EntityDataCapabilityImplementation;
import com.ilexiconn.llibrary.server.capability.EntityDataHandler;
import com.ilexiconn.llibrary.server.capability.IEntityData;
import com.ilexiconn.llibrary.server.capability.IEntityDataCapability;
import com.ilexiconn.llibrary.server.entity.EntityProperties;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import com.ilexiconn.llibrary.server.entity.PropertiesTracker;
import com.ilexiconn.llibrary.server.event.CollectEntityDataEvent;
import com.ilexiconn.llibrary.server.network.PropertiesMessage;
import com.ilexiconn.llibrary.server.world.WorldDataHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public enum ServerEventHandler {
    INSTANCE;

    private static final ResourceLocation EXTENDED_DATA_ID = new ResourceLocation("com/ilexiconn/llibrary", "ExtendedEntityDataCapability");

    private int updateTimer;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        // TODO: 1.13: Only support adding entity data through this event
        List<IEntityData> collectedData = new ArrayList<>();
        MinecraftForge.EVENT_BUS.post(new CollectEntityDataEvent(event.getObject(), collectedData));
        event.addCapability(EXTENDED_DATA_ID, new EntityDataCapabilityImplementation(collectedData));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFinishAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        ICapabilityProvider extendedData = event.getCapabilities().get(EXTENDED_DATA_ID);
        if (extendedData instanceof IEntityDataCapability) {
            EntityDataHandler.INSTANCE.putQueuedData(event.getObject(), (IEntityDataCapability) extendedData);
        } else {
            EntityDataHandler.INSTANCE.releaseQueuedData(event.getObject());
        }
    }

    @SubscribeEvent
    public void onCollectEntityData(CollectEntityDataEvent event) {
        Entity entity = event.getEntity();
        boolean cached = EntityPropertiesHandler.INSTANCE.hasEntityInCache(entity.getClass());
        List<String> entityPropertiesIDCache = !cached ? new ArrayList<>() : null;
        EntityPropertiesHandler.INSTANCE.getRegisteredProperties().filter(propEntry -> propEntry.getKey().isAssignableFrom(entity.getClass())).forEach(propEntry -> {
            for (Class<? extends EntityProperties> propClass : propEntry.getValue()) {
                try {
                    Constructor<? extends EntityProperties> constructor = propClass.getConstructor();
                    EntityProperties prop = constructor.newInstance();
                    String propID = prop.getID();
                    event.registerData(prop);
                    if (!cached) {
                        entityPropertiesIDCache.add(propID);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        if (!cached) {
            EntityPropertiesHandler.INSTANCE.addEntityToCache(entity.getClass(), entityPropertiesIDCache);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        NBTTagCompound compound = new NBTTagCompound();
        IEntityDataCapability originalCap = event.getOriginal().getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
        IEntityDataCapability newCap = event.getEntityPlayer().getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
        if (originalCap != null && newCap != null) {
            originalCap.saveToNBT(compound);
            newCap.loadFromNBT(compound);
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().world.isRemote || !(event.getEntity() instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        List<PropertiesTracker<?>> trackers = EntityPropertiesHandler.INSTANCE.getEntityTrackers(player);
        if (trackers != null && trackers.size() > 0) {
            boolean hasPlayer = false;
            for (PropertiesTracker<?> tracker : trackers) {
                if (hasPlayer = tracker.getEntity() == player) {
                    break;
                }
            }
            if (!hasPlayer) {
                EntityPropertiesHandler.INSTANCE.addTracker(player, player);
            }
            for (PropertiesTracker<?> tracker : trackers) {
                tracker.updateTracker();
                if (tracker.isTrackerReady()) {
                    tracker.onSync();
                    PropertiesMessage message = new PropertiesMessage(tracker, tracker.getEntity());
                    LLibrary.NETWORK_WRAPPER.sendTo(message, player);
                    tracker.reset();
                }
            }
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            EntityPropertiesHandler.INSTANCE.addTracker(player, player);
        }
    }

    @SubscribeEvent
    public void onEntityStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPropertiesHandler.INSTANCE.addTracker((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
        }
    }

    @SubscribeEvent
    public void onEntityStopTracking(PlayerEvent.StopTracking event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPropertiesHandler.INSTANCE.removeTracker((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
        }
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.updateTimer++;
            if (this.updateTimer > 20) {
                this.updateTimer = 0;
                Iterator<Map.Entry<EntityPlayerMP, List<PropertiesTracker<?>>>> iterator = EntityPropertiesHandler.INSTANCE.getTrackerIterator();
                while (iterator.hasNext()) {
                    Map.Entry<EntityPlayerMP, List<PropertiesTracker<?>>> trackerEntry = iterator.next();
                    EntityPlayerMP player = trackerEntry.getKey();
                    WorldServer playerWorld = DimensionManager.getWorld(player.dimension);
                    if (player == null || player.isDead || playerWorld == null || !playerWorld.loadedEntityList.contains(player)) {
                        trackerEntry.getValue().forEach(PropertiesTracker::removeTracker);
                        iterator.remove();
                    } else {
                        Iterator<PropertiesTracker<?>> it = trackerEntry.getValue().iterator();
                        while (it.hasNext()) {
                            PropertiesTracker tracker = it.next();
                            Entity entity = tracker.getEntity();
                            WorldServer entityWorld = DimensionManager.getWorld(entity.dimension);
                            if (entity == null || entity.isDead || entityWorld == null || !entityWorld.loadedEntityList.contains(entity)) {
                                it.remove();
                                tracker.removeTracker();
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            WorldDataHandler.INSTANCE.loadWorldData(event.getWorld().getSaveHandler(), event.getWorld());
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (!event.getWorld().isRemote) {
            WorldDataHandler.INSTANCE.saveWorldData(event.getWorld().getSaveHandler(), event.getWorld());
        }
    }
}
