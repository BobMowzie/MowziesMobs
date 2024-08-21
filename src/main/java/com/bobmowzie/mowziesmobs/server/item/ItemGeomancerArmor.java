package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.client.render.item.RenderGeomancerArmor;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ItemGeomancerArmor extends MowzieArmorItem implements GeoItem {
    private static final ItemGeomancerArmor.GeomancerArmorMaterial GEOMANCER_ARMOR_MATERIAL = new ItemGeomancerArmor.GeomancerArmorMaterial();

    public String controllerName = "controller";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ItemGeomancerArmor(Type slot, Properties builderIn) {
        super(GEOMANCER_ARMOR_MATERIAL, slot, builderIn);
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.GEOMANCER_ARMOR.armorConfig;
    }

    private PlayState predicate(AnimationState<ItemGeomancerArmor> state) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, controllerName, 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private static class GeomancerArmorMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForType(Type equipmentSlotType) {
            return ArmorMaterials.DIAMOND.getDurabilityForType(equipmentSlotType);
        }

        @Override
        public int getDefenseForType(Type equipmentSlotType) {
            return (int) (ArmorMaterials.DIAMOND.getDefenseForType(equipmentSlotType) * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.GEOMANCER_ARMOR.armorConfig.damageReductionMultiplierValue);
        }

        @Override
        public int getEnchantmentValue() {
            return ArmorMaterials.DIAMOND.getEnchantmentValue();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.DIAMOND.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return ArmorMaterials.DIAMOND.getRepairIngredient();
        }

        @Override
        public String getName() {
            return "geomancer_armor";
        }

        @Override
        public float getToughness() {
            return ArmorMaterials.DIAMOND.getToughness() * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughnessMultiplier.get().floatValue();
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.armorRenderer == null)
                    this.armorRenderer = new RenderGeomancerArmor();
                armorRenderer.prepForRender(entityLiving, itemStack, equipmentSlot, original);
                return armorRenderer;
            }
            private GeoArmorRenderer<?> armorRenderer;
        });
    }
}
