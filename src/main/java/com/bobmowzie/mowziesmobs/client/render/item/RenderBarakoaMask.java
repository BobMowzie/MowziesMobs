package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.masks.BarakoaMaskModel;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class RenderBarakoaMask extends GeoArmorRenderer<ItemBarakoaMask> {

    public RenderBarakoaMask() {
        super(new BarakoaMaskModel());
    }
}
