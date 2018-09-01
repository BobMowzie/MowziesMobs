package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDynamicsTester extends MowzieEntity {
    @SideOnly(Side.CLIENT)
    public DynamicChain dc;

    public EntityDynamicsTester(World world) {
        super(world);
        tasks.addTask(4, new EntityAIWander(this, 0.3));
        tasks.addTask(8, new EntityAILookIdle(this));
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

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            if (ticksExisted == 1) {
                dc = new DynamicChain(this);
            }
            dc.update(0.1f, 0.3f, 0.6f, 1f, true, 0.5f);
            renderYawOffset = rotationYaw;
        }
    }
}
