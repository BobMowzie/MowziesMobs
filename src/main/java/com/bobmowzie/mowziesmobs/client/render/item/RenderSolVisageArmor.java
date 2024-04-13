package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;

public class RenderSolVisageArmor extends MowzieGeoArmorRenderer<ItemSolVisage> {

    public RenderSolVisageArmor() {
        super(new SolVisageModel());
    }
}
