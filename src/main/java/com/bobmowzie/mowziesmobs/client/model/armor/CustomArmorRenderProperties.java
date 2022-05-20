package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

public class CustomArmorRenderProperties implements IItemRenderProperties {
    private static boolean init;

    public static WroughtHelmModel WROUGHT_HELM_MODEL;
    public static SolVisageModel SOL_VISAGE_MODEL;
    public static BarakoaMaskModel BARAKOA_MASK_MODEL;

    public static void initializeModels() {
        init = true;
        WROUGHT_HELM_MODEL = new WroughtHelmModel(Minecraft.getInstance().getEntityModels().bakeLayer(LayerHandler.WROUGHT_HELM_LAYER));
    }

    public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
        if(!init){
            initializeModels();
        }
        if (itemStack.getItem() == ItemHandler.WROUGHT_HELMET){
            return WROUGHT_HELM_MODEL;
        }
        return _default;
    }
}
