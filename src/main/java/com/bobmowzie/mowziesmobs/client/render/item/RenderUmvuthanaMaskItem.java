package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.UmvuthanaMaskModel;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderUmvuthanaMaskItem extends GeoItemRenderer<ItemUmvuthanaMask> {

    public RenderUmvuthanaMaskItem() {
        super(new UmvuthanaMaskModel());
    }
}
