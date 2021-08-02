package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import com.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerProxy {
    private int nextMessageId;

    public static final IDataSerializer<Optional<Trade>> OPTIONAL_TRADE = new IDataSerializer<Optional<Trade>>() {
        @Override
        public void write(PacketBuffer buf, Optional<Trade> value) {
            if (value.isPresent()) {
                Trade trade = value.get();
                buf.writeItemStack(trade.getInput());
                buf.writeItemStack(trade.getOutput());
                buf.writeInt(trade.getWeight());
            } else {
                buf.writeItemStack(ItemStack.EMPTY);
            }
        }

        @Override
        public Optional<Trade> read(PacketBuffer buf) {
            ItemStack input = buf.readItemStack();
            if (input == ItemStack.EMPTY) {
                return Optional.empty();
            }
            return Optional.of(new Trade(input, buf.readItemStack(), buf.readInt()));
        }

        @Override
        public DataParameter<Optional<Trade>> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public Optional<Trade> copyValue(Optional<Trade> value) {
            if (value.isPresent()) {
            	return Optional.of(new Trade(value.get()));
            }
            return Optional.empty();
        }
    };

    public void init(final IEventBus modbus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_CONFIG);
        DataSerializers.registerSerializer(OPTIONAL_TRADE);
    }

    public void onLateInit(final IEventBus modbus) {}

    public void playSunstrikeSound(EntitySunstrike strike) {}

    public void playIceBreathSound(Entity entity) {}

    public void playBoulderChargeSound(PlayerEntity player) {}

    public void playNagaSwoopSound(EntityNaga naga) {}

    public void playBlackPinkSound(AbstractMinecartEntity entity) {}

    public void playSunblockSound(LivingEntity entity) {}

    public void minecartParticles(ClientWorld world, AbstractMinecartEntity minecart, float scale, double x, double y, double z, BlockState state, BlockPos pos) {}

    public void solarBeamHitWroughtnaught(LivingEntity caster) {}

    public void initNetwork() {
        final String version = "1";
        MowziesMobs.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MowziesMobs.MODID, "net"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();
        this.registerMessage(AnimationMessage.class, AnimationMessage::serialize, AnimationMessage::deserialize, new AnimationMessage.Handler());
        this.registerMessage(MessageLeftMouseDown.class, MessageLeftMouseDown::serialize, MessageLeftMouseDown::deserialize, new MessageLeftMouseDown.Handler());
        this.registerMessage(MessageLeftMouseUp.class, MessageLeftMouseUp::serialize, MessageLeftMouseUp::deserialize, new MessageLeftMouseUp.Handler());
        this.registerMessage(MessageRightMouseDown.class, MessageRightMouseDown::serialize, MessageRightMouseDown::deserialize, new MessageRightMouseDown.Handler());
        this.registerMessage(MessageRightMouseUp.class, MessageRightMouseUp::serialize, MessageRightMouseUp::deserialize, new MessageRightMouseUp.Handler());
        this.registerMessage(MessageAddFreezeProgress.class, MessageAddFreezeProgress::serialize, MessageAddFreezeProgress::deserialize, new MessageAddFreezeProgress.Handler());
        this.registerMessage(MessageBarakoTrade.class, MessageBarakoTrade::serialize, MessageBarakoTrade::deserialize, new MessageBarakoTrade.Handler());
        this.registerMessage(MessageBlackPinkInYourArea.class, MessageBlackPinkInYourArea::serialize, MessageBlackPinkInYourArea::deserialize, new MessageBlackPinkInYourArea.Handler());
        this.registerMessage(MessagePlayerAttackMob.class, MessagePlayerAttackMob::serialize, MessagePlayerAttackMob::deserialize, new MessagePlayerAttackMob.Handler());
        this.registerMessage(MessagePlayerSolarBeam.class, MessagePlayerSolarBeam::serialize, MessagePlayerSolarBeam::deserialize, new MessagePlayerSolarBeam.Handler());
        this.registerMessage(MessagePlayerSummonSunstrike.class, MessagePlayerSummonSunstrike::serialize, MessagePlayerSummonSunstrike::deserialize, new MessagePlayerSummonSunstrike.Handler());
        this.registerMessage(MessageRemoveFreezeProgress.class, MessageRemoveFreezeProgress::serialize, MessageRemoveFreezeProgress::deserialize, new MessageRemoveFreezeProgress.Handler());
        this.registerMessage(MessageUnfreezeEntity.class, MessageUnfreezeEntity::serialize, MessageUnfreezeEntity::deserialize, new MessageUnfreezeEntity.Handler());
        this.registerMessage(MessagePlayerStartSummonBoulder.class, MessagePlayerStartSummonBoulder::serialize, MessagePlayerStartSummonBoulder::deserialize, new MessagePlayerStartSummonBoulder.Handler());
        this.registerMessage(MessageSunblockEffect.class, MessageSunblockEffect::serialize, MessageSunblockEffect::deserialize, new MessageSunblockEffect.Handler());
        this.registerMessage(MessageUseAbility.class, MessageUseAbility::serialize, MessageUseAbility::deserialize, new MessageUseAbility.Handler());
        this.registerMessage(MessagePlayerTryAbility.class, MessagePlayerTryAbility::serialize, MessagePlayerTryAbility::deserialize, new MessagePlayerTryAbility.Handler());
    }

    private <MSG> void registerMessage(final Class<MSG> clazz, final BiConsumer<MSG, PacketBuffer> encoder, final Function<PacketBuffer, MSG> decoder, final BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        MowziesMobs.NETWORK.messageBuilder(clazz, this.nextMessageId++)
                .encoder(encoder).decoder(decoder)
                .consumer(consumer)
                .add();
    }

    public void setTPS(float tickRate) {
    }


    public Entity getReferencedMob() {
        return null;
    }

    public void setReferencedMob(Entity referencedMob) {}
}
