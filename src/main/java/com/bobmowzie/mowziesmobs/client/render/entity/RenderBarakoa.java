package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarakoa;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumMap;

@OnlyIn(Dist.CLIENT)
public class RenderBarakoa extends MobRenderer<EntityBarakoa, ModelBarakoa<EntityBarakoa>> {
    private static final EnumMap<MaskType, ResourceLocation> TEXTURES = MaskType.newEnumMap(ResourceLocation.class);

    static {
        for (MaskType mask : MaskType.values()) {
            TEXTURES.put(mask, new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + mask.name + (ConfigHandler.CLIENT.oldBarakoaTextures.get() ? "_old" : "") + ".png"));
        }
    }

    public RenderBarakoa(EntityRendererManager mgr) {
        super(mgr, new ModelBarakoa<>(), 0.6F);
        addLayer(new ItemLayer(this, getEntityModel().bone, Items.BONE.getDefaultInstance()));
        addLayer(new ItemLayer(this, getEntityModel().spear, ItemHandler.SPEAR.getDefaultInstance()));
        addLayer(new ItemLayer(this, getEntityModel().blowgun, ItemHandler.BLOWGUN.getDefaultInstance()));
        addLayer(new ItemLayer(this, getEntityModel().staff, ItemHandler.SUNBLOCK_STAFF.getDefaultInstance()));
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
