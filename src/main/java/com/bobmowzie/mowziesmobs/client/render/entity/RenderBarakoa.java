package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

@SideOnly(Side.CLIENT)
public class RenderBarakoa extends RenderLiving<EntityBarakoa> {
    private static final EnumMap<MaskType, ResourceLocation> TEXTURES = MaskType.newEnumMap(ResourceLocation.class);

    static {
        for (MaskType mask : MaskType.values()) {
            TEXTURES.put(mask, new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + mask.name + ".png"));
        }
    }

    public RenderBarakoa(RenderManager mgr) {
        super(mgr, new ModelBarakoa(), 0.6F);
    }

    @Override
    protected float getDeathMaxRotation(EntityBarakoa entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBarakoa entity) {
        return TEXTURES.get(entity.getMask());
    }
}
