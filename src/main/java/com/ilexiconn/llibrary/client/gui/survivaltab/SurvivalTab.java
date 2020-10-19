package com.ilexiconn.llibrary.client.gui.survivaltab;

import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SurvivalTab {
    private int index;
    private String label;
    private Class<? extends Container> container;

    SurvivalTab(int index, String label, Class<? extends Container> container) {
        this.index = index;
        this.label = label;
        this.container = container;
    }

    public int getIndex() {
        return this.index;
    }

    public int getColumn() {
        if (this.index > 7) {
            return ((this.index - 8) % 8) % 5;
        } else {
            return this.index % 9;
        }
    }

    public int getPage() {
        if (this.index > 7) {
            return ((this.index - 8) / 8) + 1;
        } else {
            return 0;
        }
    }

    public String getLabel() {
        return this.label;
    }

    public Class<? extends Container> getContainer() {
        return this.container;
    }
}

