package com.ilexiconn.llibrary.server.world;

import com.ilexiconn.llibrary.LLibrary;

/**
 * @author gegy1000
 * @since 1.2.0
 */
public enum TickRateHandler {
    INSTANCE;

    public static final float DEFAULT_TPS = 20.0F;

    private float tps = DEFAULT_TPS;

    public float getTPS() {
        return this.tps;
    }

    public void setTPS(float tps) {
        if (this.tps != tps) {
            LLibrary.PROXY.setTPS(tps);
        }
        this.tps = tps;
    }

    public void resetTPS() {
        this.setTPS(DEFAULT_TPS);
    }

    public long getTickRate() {
        return (long) (this.tps / DEFAULT_TPS * 50);
    }
}
