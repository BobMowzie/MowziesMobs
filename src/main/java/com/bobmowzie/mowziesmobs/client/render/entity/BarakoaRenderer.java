package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.BarakoaModel;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;

@SideOnly(Side.CLIENT)
public class BarakoaRenderer extends RenderLiving<EntityBarakoa> {
    private static final EnumMap<MaskType, ResourceLocation> TEXTURES = MaskType.newEnumMap(ResourceLocation.class);

    static {
        for (MaskType mask : MaskType.values()) {
            TEXTURES.put(mask, new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + mask.name + ".png"));
        }
    }

    public BarakoaRenderer(RenderManager mgr) {
        super(mgr, new BarakoaModel(), 0.6F);
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
