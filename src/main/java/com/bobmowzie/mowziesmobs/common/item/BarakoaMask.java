package com.bobmowzie.mowziesmobs.common.item;

public enum BarakoaMask {
    FURY(5), FEAR(1), RAGE(3), BLISS(8), MISERY(11);

    private final String unlocalizedName;

    private final int potionEffectId;

    private final String armorTexture;

    BarakoaMask(int potionEffectId) {
        this.potionEffectId = potionEffectId;
        int num = ordinal() + 1;
        unlocalizedName = "barakoaMask" + num;
        armorTexture = String.format(ItemBarakoaMask.ARMOR_TEXTURE_FORMAT, num);
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public int getPotionEffectId() {
        return potionEffectId;
    }

    public String getArmorTexture() {
        return armorTexture;
    }
}
