package com.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.ElementGUI;
import net.ilexiconn.llibrary.client.gui.config.property.ForgeConfigProperty;
import net.ilexiconn.llibrary.client.gui.element.ButtonElement;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.client.gui.element.LabelElement;
import net.ilexiconn.llibrary.client.gui.element.ListElement;
import net.ilexiconn.llibrary.client.gui.element.color.ColorScheme;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class ConfigGUI extends ElementGUI {
    public static final ColorScheme RETURN = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getSecondaryColor());
    public static final ColorScheme SIDEBAR = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getTertiaryColor());
    public static final ResourceLocation SETTINGS_ICON = new ResourceLocation("com/ilexiconn/llibrary", "textures/gui/settings.png");

    protected GuiScreen parent;
    protected String parentName;
    protected List<ConfigCategory> categories = new ArrayList<>();
    protected Map<ConfigProperty, Element<ConfigGUI>> propertyElements = new HashMap<>();
    protected ConfigCategory selectedCategory;

    private Mod mod;

    public ConfigGUI(GuiScreen parent, Object mod, Configuration config) {
        this(parent, mod, config, "Mod List");
    }

    public ConfigGUI(GuiScreen parent, Object mod, Configuration config, String parentName) {
        this.parent = parent;
        this.parentName = parentName;
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            throw new RuntimeException("@Mod annotation not found in class " + mod + "!");
        }
        this.mod = mod.getClass().getAnnotation(Mod.class);
        if (config != null) {
            this.categories.addAll(config.getCategoryNames().stream()
                    .map(config::getCategory)
                    .filter(category -> category.size() > 0)
                    .map(category -> {
                        List<ConfigProperty> configProperties = new ArrayList<>();
                        for (Property property : category.values()) {
                            ConfigProperty configProperty = ForgeConfigProperty.factory(property);
                            if (configProperty != null) {
                                configProperties.add(configProperty);
                            }
                        }
                        return new ConfigCategory(category.getQualifiedName(), configProperties);
                    })
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public void initElements() {
        this.addElement(new ButtonElement<>(this, "<", 0, 0, 30, 20, button -> {
            this.mc.displayGuiScreen(this.parent);
            return true;
        }).withColorScheme(this.getReturnButtonColorScheme()));
        this.addElement(new LabelElement<>(this, this.parentName, 35, 6));
        this.addElement(new LabelElement<>(this, this.mod.name().toUpperCase() + " SETTINGS", 35, 26));
        ListElement<ConfigGUI> categoryList = (ListElement<ConfigGUI>) new ListElement<>(this, 0, 40, 120, this.height - 40, this.categories.stream().map(ConfigCategory::getName).collect(Collectors.toList()), 20, list -> {
            this.selectedCategory = this.categories.get(list.getSelectedIndex());
            this.propertyElements.values().forEach(this::removeElement);
            this.propertyElements.clear();
            return true;
        }).withPersistence(true).withColorScheme(this.getSidebarColorScheme());
        categoryList.setSelectedIndex(0);
        this.selectedCategory = this.categories.get(0);
        this.addElement(categoryList);
        this.propertyElements.clear();
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float partialTicks) {
        Gui.drawRect(0, 0, this.width, 40, this.getTopBackgroundColor());
        Gui.drawRect(120, 40, this.width, this.height, this.getContentBackgroundColor());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        int color = this.getAccentColor();
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, a);
        GlStateManager.scale(0.15F, 0.15F, 0.15F);
        this.mc.getTextureManager().bindTexture(ConfigGUI.SETTINGS_ICON);
        this.drawTexturedModalRect(40, 135, 0, 0, 128, 128);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        int x = 125;
        int y = 45;
        for (ConfigProperty property : this.selectedCategory.getProperties()) {
            this.fontRenderer.drawString(property.name, x, y, this.getTextColor());
            y += 10;
            if (property.description != null && property.description.length() > 0) {
                this.fontRenderer.drawString(property.description, x, y, this.getTextColor());
                y += 10;
            }
            Element<ConfigGUI> propertyElement = this.propertyElements.get(property);
            if (propertyElement == null) {
                propertyElement = property.provideElement(this, x, y);
                if (propertyElement != null) {
                    this.decoratePropertyElement(propertyElement);
                    this.propertyElements.put(property, propertyElement);
                    this.addElement(propertyElement);
                }
            }
            if (propertyElement != null) {
                y += propertyElement.getHeight() + 4;
            }
        }
    }

    @Override
    public void onGuiClosed() {
        MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.OnConfigChangedEvent(this.mod.modid(), null, this.mc.world != null, false));
        super.onGuiClosed();
    }

    public ColorScheme getReturnButtonColorScheme() {
        return RETURN;
    }

    public ColorScheme getSidebarColorScheme() {
        return SIDEBAR;
    }

    public int getTopBackgroundColor() {
        return LLibrary.CONFIG.getPrimaryColor();
    }

    public int getContentBackgroundColor() {
        return LLibrary.CONFIG.getColorMode().equals("dark") ? 0xFF191919 : 0xFFFFFFFF;
    }

    public int getAccentColor() {
        return LLibrary.CONFIG.getAccentColor();
    }

    public int getTextColor() {
        return LLibrary.CONFIG.getTextColor();
    }

    public void decoratePropertyElement(Element<ConfigGUI> element) {}

    public GuiScreen getParent() {
        return this.parent;
    }
}
