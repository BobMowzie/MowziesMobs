package com.ilexiconn.llibrary.client.gui.survivaltab;

import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.4.0
 */
@OnlyIn(Dist.CLIENT)
public enum SurvivalTabHandler {
    INSTANCE;

    private List<SurvivalTab> survivalTabList = new ArrayList<>();
    private int currentPage;

    /**
     * Create a new survival tab instance and register it.
     *
     * @param label     the unlocalized survival tab label
     * @param container the container class
     * @return the new survival tab instance
     */
    public SurvivalTab create(String label, Class<? extends Container> container) {
        SurvivalTab survivalTab = new SurvivalTab(this.survivalTabList.size(), label, container);
        this.survivalTabList.add(survivalTab);
        return survivalTab;
    }

    public List<SurvivalTab> getSurvivalTabList() {
        return this.survivalTabList;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;
    }
}
