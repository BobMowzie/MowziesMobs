package com.ilexiconn.llibrary.client.gui.element.color;

import java.util.function.Supplier;

/**
 * @author iLexiconn
 * @since 1.4.0
 */
public class ColorScheme {
    private Supplier<Integer> primaryColor;
    private Supplier<Integer> secondaryColor;

    private ColorScheme(Supplier<Integer> primaryColor, Supplier<Integer> secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    /**
     * Create a new color scheme that can be used by elements.
     *
     * @param primaryColor   the primary color
     * @param secondaryColor the secondary color
     * @return the new color scheme instance
     */
    public static ColorScheme create(Supplier<Integer> primaryColor, Supplier<Integer> secondaryColor) {
        return new ColorScheme(primaryColor, secondaryColor);
    }

    public int getPrimaryColor() {
        return this.primaryColor.get();
    }

    public int getSecondaryColor() {
        return this.secondaryColor.get();
    }
}
