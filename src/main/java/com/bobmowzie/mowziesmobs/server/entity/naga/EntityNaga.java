package com.bobmowzie.mowziesmobs.server.entity.naga;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.world.World;

/**
 * Created by Josh on 9/9/2018.
 */
public class EntityNaga extends MowzieEntity {
    public EntityNaga(World world) {
        super(world);
        setSize(3, 1);
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }
}
