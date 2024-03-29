package com.bobmowzie.mowziesmobs.server.item;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.bobmowzie.mowziesmobs.client.render.item.RenderSculptorStaff;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemSculptorStaff extends MowzieToolItem implements IAnimatable, ISyncable {
    public String controllerName = "controller";
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemSculptorStaff(Properties properties) {
        super(1f,2f, Tiers.STONE, BlockTags.MINEABLE_WITH_HOE, properties);
        GeckoLibNetwork.registerSyncable(this);

    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new RenderSculptorStaff();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.ROCK_SLING);
        player.startUsingItem(hand);
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }


    public int getUseDuration(ItemStack stack) {
        return 72000;
    }


    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
//        animationData.addAnimationController(new AnimationController<>(this, controllerName, 3, this::predicate));
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
//        event.getController().setAnimation(new AnimationBuilder().addAnimation("empty", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {

    }

    public void playAnimation(LivingEntity entity, InteractionHand hand, int state) {
        ItemStack stack = entity.getItemInHand(hand);
        playAnimation(entity, stack, state);
    }

    public void playAnimation(LivingEntity entity, ItemStack stack, int state) {
        if (!entity.level.isClientSide) {
            int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel)entity.level);
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
                return entity;
            });
            GeckoLibNetwork.syncAnimation(target, this, id, state);
        }
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig;
    }
}
