package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelSculptorStaff;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderSculptorStaff extends GeoItemRenderer<ItemSculptorStaff> {

    public RenderSculptorStaff() {
        super(new ModelSculptorStaff());
    }
}
