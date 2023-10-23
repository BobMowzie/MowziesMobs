package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.masks.UmvuthanaMaskModel;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class RenderUmvuthanaMask extends GeoArmorRenderer<ItemUmvuthanaMask> {

    public RenderUmvuthanaMask() {
        super(new UmvuthanaMaskModel());
    }
}
