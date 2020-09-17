package com.ilexiconn.llibrary.server.network;

import com.google.common.collect.SetMultimap;
import com.ilexiconn.llibrary.LLibrary;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handler class for message IDs.
 *
 * @author iLexiconn
 * @since 1.2.0
 */
public enum NetworkHandler {
    INSTANCE;

    private Map<SimpleNetworkWrapper, Integer> idMap = new HashMap<>();

    /**
     * Register a message to both sides.
     *
     * @param networkWrapper the network wrapper
     * @param clazz          the message class
     * @param <T>            the message type
     */
    public <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(SimpleNetworkWrapper networkWrapper, Class<T> clazz) {
        try {
            AbstractMessage<T> message = clazz.getDeclaredConstructor().newInstance();
            if (message.registerOnSide(Side.CLIENT)) {
                this.registerMessage(networkWrapper, clazz, Side.CLIENT);
            }
            if (message.registerOnSide(Side.SERVER)) {
                this.registerMessage(networkWrapper, clazz, Side.SERVER);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register a message to a specific side.
     *
     * @param networkWrapper the network wrapper
     * @param clazz          the message class
     * @param side           the side
     * @param <T>            the message type
     * @deprecated use {@link NetworkHandler#registerMessage(SimpleNetworkWrapper, Class)} in combination with {@link AbstractMessage#registerOnSide(Side)} instead.
     */
    @Deprecated
    public <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(SimpleNetworkWrapper networkWrapper, Class<T> clazz, Side side) {
        int id = 0;
        if (this.idMap.containsKey(networkWrapper)) {
            id = this.idMap.get(networkWrapper);
        }
        networkWrapper.registerMessage(clazz, clazz, id, side);
        this.idMap.put(networkWrapper, id + 1);
    }

    public void injectNetworkWrapper(ModContainer mod, ASMDataTable data) {
        SetMultimap<String, ASMDataTable.ASMData> annotations = data.getAnnotationsFor(mod);
        if (annotations != null) {
            Set<ASMDataTable.ASMData> targetList = annotations.get(NetworkWrapper.class.getName());
            ClassLoader classLoader = Loader.instance().getModClassLoader();
            for (ASMDataTable.ASMData target : targetList) {
                try {
                    Class<?> targetClass = Class.forName(target.getClassName(), true, classLoader);
                    Field field = targetClass.getDeclaredField(target.getObjectName());
                    field.setAccessible(true);
                    NetworkWrapper annotation = field.getAnnotation(NetworkWrapper.class);
                    SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(mod.getModId());
                    field.set(null, networkWrapper);
                    for (Class messageClass : annotation.value()) {
                        this.registerMessage(networkWrapper, messageClass);
                    }
                } catch (Exception e) {
                    LLibrary.LOGGER.fatal("Failed to inject network wrapper for mod container {}", mod, e);
                }
            }
        }
    }
}
