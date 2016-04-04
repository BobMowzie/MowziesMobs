package com.bobmowzie.mowziesmobs.server.entity;

public class MowzieEntityEggInfo {
    public int entityID;
    public int primaryColor;
    public int secondaryColor;

    public MowzieEntityEggInfo(int entityID, int primaryColor, int secondaryColor) {
        this.entityID = entityID;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }
}
