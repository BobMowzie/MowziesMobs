package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.UmvuthanaMaskModel;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class RenderUmvuthanaMaskArmor extends GeoArmorRenderer<ItemUmvuthanaMask> {

    public RenderUmvuthanaMaskArmor() {
        super(new UmvuthanaMaskModel());
    }
}
