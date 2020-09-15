package com.ilexiconn.llibrary.client.gui.config;

import java.util.List;

public class ConfigCategory {
    private String name;
    private List<ConfigProperty> properties;

    public ConfigCategory(String name, List<ConfigProperty> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return this.name;
    }

    public List<ConfigProperty> getProperties() {
        return this.properties;
    }
}
