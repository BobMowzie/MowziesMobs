package com.ilexiconn.llibrary.server.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.lang.reflect.Field;

@Deprecated
public abstract class EntryProperty {
    protected final ConfigEntry entry;
    protected final Object wrappedConfig;
    protected final Field wrappedField;
    protected final Property forgeConfigurationProperty;
    protected final Object defaultValue;

    protected EntryProperty(Object wrappedConfig, Field wrappedField, Configuration forgeConfiguration) {
        this.entry = wrappedField.getAnnotation(ConfigEntry.class);
        this.wrappedConfig = wrappedConfig;
        this.wrappedField = wrappedField;
        this.forgeConfigurationProperty = this.createForgeConfigurationProperty(forgeConfiguration);
        this.defaultValue = this.get();

        this.load();
    }

    public String getName() {
        return this.entry.name().isEmpty() ? this.wrappedField.getName() : this.entry.name();
    }

    public Object get() {
        try {
            return this.wrappedField.get(this.wrappedConfig);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access ConfigEntry field. Is it not public?");
        }
    }

    public void set(Object value) {
        try {
            this.wrappedField.set(this.wrappedConfig, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access ConfigEntry field. Is it not public?");
        }
    }

    public void reset() {
        this.set(this.defaultValue);
    }

    public abstract void save();

    public abstract void load();

    protected abstract Property createForgeConfigurationProperty(Configuration forgeConfiguration);

    public static class IntegerEntryProperty extends EntryProperty {
        public IntegerEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            int minInt = this.entry.minValue().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(this.entry.minValue());
            int maxInt = this.entry.maxValue().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(this.entry.maxValue());
            return forgeConfiguration.get(this.entry.category(), this.getName(), (int)this.get(), this.entry.comment(), minInt, maxInt);
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((int)this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getInt());
        }
    }

    public static class BooleanEntryProperty extends EntryProperty {
        public BooleanEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            return forgeConfiguration.get(this.entry.category(), this.getName(), (boolean)this.get(), this.entry.comment());
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((boolean)this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getBoolean());
        }
    }

    public static class StringEntryProperty extends EntryProperty {
        public StringEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            return forgeConfiguration.get(this.entry.category(), this.getName(), (String)this.get(), this.entry.comment());
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((String)this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getString());
        }
    }

    public static class FloatEntryProperty extends EntryProperty {
        public FloatEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            float minFloat = this.entry.minValue().isEmpty() ? Float.MIN_VALUE : Float.parseFloat(this.entry.minValue());
            float maxFloat = this.entry.maxValue().isEmpty() ? Float.MAX_VALUE : Float.parseFloat(this.entry.maxValue());
            return forgeConfiguration.get(this.entry.category(), this.getName(), (float)this.get(), this.entry.comment(), minFloat, maxFloat);
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((float)this.get());
        }

        @Override
        public void load() {
            this.set((float)this.forgeConfigurationProperty.getDouble());
        }
    }

    public static class DoubleEntryProperty extends EntryProperty {
        public DoubleEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            double minDouble = this.entry.minValue().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(this.entry.minValue());
            double maxDouble = this.entry.maxValue().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(this.entry.maxValue());
            return forgeConfiguration.get(this.entry.category(), this.getName(), (double)this.get(), this.entry.comment(), minDouble, maxDouble);
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((double)this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getDouble());
        }
    }

    public static class IntegerArrayEntryProperty extends EntryProperty {
        public IntegerArrayEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            Property property = forgeConfiguration.get(this.entry.category(), this.getName(), (int[])this.get(), this.entry.comment());
            property.setValidValues(this.entry.validValues());
            return property;
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((int[])this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getIntList());
        }
    }

    public static class BooleanArrayEntryProperty extends EntryProperty {
        public BooleanArrayEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            Property property = forgeConfiguration.get(this.entry.category(), this.getName(), (boolean[])this.get(), this.entry.comment());
            property.setValidValues(this.entry.validValues());
            return property;
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((boolean[])this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getBooleanList());
        }
    }

    public static class StringArrayEntryProperty extends EntryProperty {
        public StringArrayEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            Property property = forgeConfiguration.get(this.entry.category(), this.getName(), (String[])this.get(), this.entry.comment());
            property.setValidValues(this.entry.validValues());
            return property;
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((String[])this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getStringList());
        }
    }

    public static class FloatArrayEntryProperty extends EntryProperty {
        public FloatArrayEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            Property property = forgeConfiguration.get(this.entry.category(), this.getName(), this.getDoubleArray(), this.entry.comment());
            property.setValidValues(this.entry.validValues());
            return property;
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set(this.getDoubleArray());
        }

        @Override
        public void load() {
            this.setDoubleArray(this.forgeConfigurationProperty.getDoubleList());
        }

        public double[] getDoubleArray() {
            float[] floats = (float[])this.get();
            double[] doubles = new double[floats.length];
            for (int i = 0; i < floats.length; i++) {
                floats[i] = (float)doubles[i];
            }
            return doubles;
        }

        public void setDoubleArray(double[] doubles) {
            float[] floats = new float[doubles.length];
            for (int i = 0; i < doubles.length; i++) {
                floats[i] = (float)doubles[i];
            }
            this.set(floats);
        }
    }

    public static class DoubleArrayEntryProperty extends EntryProperty {
        public DoubleArrayEntryProperty(Object wrappedConfig, Field field, Configuration forgeConfiguration) {
            super(wrappedConfig, field, forgeConfiguration);
        }

        @Override
        protected Property createForgeConfigurationProperty(Configuration forgeConfiguration) {
            Property property = forgeConfiguration.get(this.entry.category(), this.getName(), (double[])this.get(), this.entry.comment());
            property.setValidValues(this.entry.validValues());
            return property;
        }

        @Override
        public void save() {
            this.forgeConfigurationProperty.set((double[])this.get());
        }

        @Override
        public void load() {
            this.set(this.forgeConfigurationProperty.getDoubleList());
        }
    }

    public static Class<? extends EntryProperty> getBuiltInPropertyClass(Class<?> type) {
        if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            return IntegerEntryProperty.class;
        } else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            return BooleanEntryProperty.class;
        } else if (String.class.isAssignableFrom(type)) {
            return StringEntryProperty.class;
        } else if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
            return FloatEntryProperty.class;
        } else if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
            return DoubleEntryProperty.class;
        } else if (Integer[].class.isAssignableFrom(type) || int[].class.isAssignableFrom(type)) {
            return IntegerArrayEntryProperty.class;
        } else if (Boolean[].class.isAssignableFrom(type) || boolean[].class.isAssignableFrom(type)) {
            return BooleanArrayEntryProperty.class;
        } else if (String[].class.isAssignableFrom(type)) {
            return StringArrayEntryProperty.class;
        } else if (Float[].class.isAssignableFrom(type) || float[].class.isAssignableFrom(type)) {
            return FloatArrayEntryProperty.class;
        } else if (Double[].class.isAssignableFrom(type) || double[].class.isAssignableFrom(type)) {
            return DoubleArrayEntryProperty.class;
        } else {
            return null;
        }
    }
}
