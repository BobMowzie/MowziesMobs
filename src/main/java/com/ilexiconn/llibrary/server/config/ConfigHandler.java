package com.ilexiconn.llibrary.server.config;

import com.google.common.collect.SetMultimap;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author iLexiconn
 * @since 1.0.0
 * @deprecated Use Forge config
 */
@Deprecated
public enum ConfigHandler {
    INSTANCE;

    private class ConfigContainer {
        public final Object wrappedConfig;
        public final Configuration forgeConfiguration;
        private final List<EntryProperty> entryProperties;

        public ConfigContainer(ModContainer mod, Object wrappedConfig, Configuration forgeConfiguration) {
            this.wrappedConfig = wrappedConfig;
            this.forgeConfiguration = forgeConfiguration;
            this.entryProperties = Arrays.stream(wrappedConfig.getClass().getFields())
                    .filter(field -> field.isAnnotationPresent(ConfigEntry.class))
                    .map(field -> {
                        Class<? extends EntryProperty> entryPropertyClass = EntryProperty.getBuiltInPropertyClass(field.getType());
                        if (entryPropertyClass == null) {
                            entryPropertyClass = ConfigHandler.this.entryPropertyClasses.get(field.getType());
                        }
                        if (entryPropertyClass != null) {
                            try {
                                Constructor<? extends EntryProperty> constructor = entryPropertyClass.getConstructor(Object.class, Field.class, Configuration.class);
                                return constructor.newInstance(wrappedConfig, field, forgeConfiguration);
                            } catch (Exception e) {
                                LLibrary.LOGGER.error("Failed to create property entry {} for mod {}", field.getName(), mod.getName(), e);
                            }
                        } else {
                            LLibrary.LOGGER.error("Found unsupported config entry {} for mod {}", field.getName(), mod.getName());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull).collect(Collectors.toList());
        }

        public void save() {
            this.entryProperties.forEach(EntryProperty::save);
            this.forgeConfiguration.save();
        }

        public void load() {
            this.entryProperties.forEach(EntryProperty::load);
        }

        public void reset() {
            this.entryProperties.forEach(EntryProperty::reset);
        }
    }

    private Map<Class<?>, Class<EntryProperty>> entryPropertyClasses = new HashMap<>();
    private Map<String, ConfigContainer> configContainers = new HashMap<>();

    /**
     * Register an entry property class.
     *
     * @param type the class to handle
     * @param entryAdapter the property class
     * @param <T> the entry type
     */
    public <T> void registerEntryPropertyClass(Class<T> type, Class<EntryProperty> entryAdapter) {
        this.entryPropertyClasses.put(type, entryAdapter);
    }

    /**
     * @param modid the mod id
     * @return true if the mod with that id registered a config
     */
    public boolean hasConfigForID(String modid) {
        return this.configContainers.containsKey(modid);
    }

    /**
     * @param modid the mod id
     * @return the {@link Configuration} instance of the mod, null if none can be found
     */
    public Configuration getConfigForID(String modid) {
        ConfigContainer configContainer = this.configContainers.get(modid);
        if (configContainer != null) {
            return configContainer.forgeConfiguration;
        }
        return null;
    }

    /**
     * @param modid the mod id
     * @return the wrapped config object of the mod, null if none can be found
     */
    @Deprecated
    public Object getObjectForID(String modid) {
        ConfigContainer configContainer = this.configContainers.get(modid);
        if (configContainer != null) {
            return configContainer.wrappedConfig;
        }
        return null;
    }

    /**
     * @param modid the mod id
     * @param type the config type
     * @return the config instance of the mod, null if none can be found
     */
    public <T> T getObjectForID(String modid, Class<T> type) {
        ConfigContainer configContainer = this.configContainers.get(modid);
        if (configContainer != null) {
            return type.cast(configContainer.wrappedConfig);
        }
        return null;
    }

    /**
     * Saves the config file for the mod.
     *
     * @param modid the mod id
     */
    public void saveConfigForID(String modid) {
        ConfigContainer configContainer = this.configContainers.get(modid);
        if (configContainer != null) {
            configContainer.save();
        }
    }

    /**
     * Loads the config file for the mod.
     *
     * @param modid the mod id
     */
    public void loadConfigForID(String modid) {
        ConfigContainer configContainer = this.configContainers.get(modid);
        if (configContainer != null) {
            configContainer.load();
        }
    }

    public void injectConfig(ModContainer mod, ASMDataTable data) {
        SetMultimap<String, ASMDataTable.ASMData> annotations = data.getAnnotationsFor(mod);
        if (annotations != null) {
            Set<ASMDataTable.ASMData> targetList = annotations.get(Config.class.getName());
            ClassLoader classLoader = Loader.instance().getModClassLoader();
            for (ASMDataTable.ASMData target : targetList) {
                try {
                    Class<?> targetClass = Class.forName(target.getClassName(), true, classLoader);
                    Field field = targetClass.getDeclaredField(target.getObjectName());
                    field.setAccessible(true);
                    Class<?> configClass = field.getType();
                    File configFile = new File(".", "config" + File.separator + mod.getModId() + ".cfg");
                    field.set(null, this.registerConfig(mod, configFile, configClass.newInstance()));
                } catch (Exception e) {
                    LLibrary.LOGGER.fatal("Failed to inject config for mod container {}", e, mod);
                }
            }
        }
    }

    private <T> T registerConfig(ModContainer mod, File file, T config) {
        ConfigContainer configContainer = new ConfigContainer(mod, config, new Configuration(file));
        this.configContainers.put(mod.getModId(), configContainer);
        configContainer.load();
        configContainer.save();
        return config;
    }
}
