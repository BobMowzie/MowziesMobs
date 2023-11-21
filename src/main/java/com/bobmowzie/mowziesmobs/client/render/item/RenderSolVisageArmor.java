package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class RenderSolVisageArmor extends GeoArmorRenderer<ItemSolVisage> {

    public RenderSolVisageArmor() {
        super(new SolVisageModel());
    }
}
