package com.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.element.CheckboxElement;
import net.ilexiconn.llibrary.client.gui.element.ColorElement;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.server.property.IBooleanProperty;
import net.ilexiconn.llibrary.server.property.IIntProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LLibraryConfigGUI extends ConfigGUI {
    private static final List<ConfigProperty> GENERAL_PROPERTIES = new ArrayList<>();
    private static final List<ConfigProperty> APPEARANCE_PROPERTIES = new ArrayList<>();

    static {
        LLibraryConfigGUI.GENERAL_PROPERTIES.add(new ConfigProperty("Patreon Effects") {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.hasPatreonEffects();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setPatreonEffects(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.GENERAL_PROPERTIES.add(new ConfigProperty("Version Checker") {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.hasVersionCheck();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setVersionCheck(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.GENERAL_PROPERTIES.add(new ConfigProperty("Survival Tabs Always Visible") {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.areTabsAlwaysVisible();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setTabsAlwaysVisible(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.GENERAL_PROPERTIES.add(new ConfigProperty("Survival Tabs Left Side") {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.areTabsLeftSide();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setTabsLeftSide(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.add(new ConfigProperty("Accent Color") {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new ColorElement<>(gui, x, y, 195, 149, new IIntProperty() {
                    @Override
                    public int getInt() {
                        return LLibrary.CONFIG.getAccentColor();
                    }

                    @Override
                    public void setInt(int value) {
                        LLibrary.CONFIG.setAccentColor(value);
                    }

                    @Override
                    public boolean isValidInt(int value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.add(new ConfigProperty("Dark Mode") {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.getColorMode().equals("dark");
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setColorMode(value ? "dark" : "light");
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
    }

    public LLibraryConfigGUI(GuiScreen parent) {
        super(parent, LLibrary.INSTANCE, null);
        this.categories.add(new ConfigCategory("General", LLibraryConfigGUI.GENERAL_PROPERTIES));
        this.categories.add(new ConfigCategory("Appearance", LLibraryConfigGUI.APPEARANCE_PROPERTIES));
    }

    @Override
    public void onGuiClosed() {
        LLibrary.CONFIG.save();
        super.onGuiClosed();
    }
}
