package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.UmvuthanaMaskModel;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;

public class RenderUmvuthanaMaskArmor extends MowzieGeoArmorRenderer<ItemUmvuthanaMask> {

    public RenderUmvuthanaMaskArmor() {
        super(new UmvuthanaMaskModel());
    }
}
