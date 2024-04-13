package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderSolVisageItem extends GeoItemRenderer<ItemSolVisage> {

    public RenderSolVisageItem() {
        super(new SolVisageModel());
    }
}
