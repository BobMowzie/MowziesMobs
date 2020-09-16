package com.bobmowzie.mowziesmobs.server.net;

import com.mojang.brigadier.Message;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NetBuilder {
    private final NetworkRegistry.ChannelBuilder builder;
    private String version;
    private SimpleChannel channel;
    private int id;

    public NetBuilder(final ResourceLocation name) {
        this.builder = NetworkRegistry.ChannelBuilder.named(name);
    }

    public NetBuilder version(final int version) {
        return this.version(String.valueOf(version));
    }

    public NetBuilder version(final String version) {
        if (this.version == null) {
            this.version = Objects.requireNonNull(version);
            this.builder.networkProtocolVersion(() -> version);
            return this;
        }
        throw new IllegalArgumentException("version already assigned");
    }

    public NetBuilder optionalServer() {
        this.builder.clientAcceptedVersions(this.optionalVersion());
        return this;
    }

    public NetBuilder requiredServer() {
        this.builder.clientAcceptedVersions(this.requiredVersion());
        return this;
    }

    public NetBuilder optionalClient() {
        this.builder.serverAcceptedVersions(this.optionalVersion());
        return this;
    }

    public NetBuilder requiredClient() {
        this.builder.serverAcceptedVersions(this.requiredVersion());
        return this;
    }

    private Predicate<String> optionalVersion() {
        final String v = this.version;
        if (v == null) {
            throw new IllegalStateException("version not specified");
        }
        return value -> NetworkRegistry.ACCEPTVANILLA.equals(value) || NetworkRegistry.ABSENT.equals(value) || v.equals(value);
    }

    private Predicate<String> requiredVersion() {
        final String v = this.version;
        if (v == null) {
            throw new IllegalStateException("version not specified");
        }
        return v::equals;
    }

    private SimpleChannel channel() {
        if (this.channel == null) {
            this.channel = this.builder.simpleChannel();
        }
        return this.channel;
    }

    public <T extends Message> MessageBuilder<T, ServerMessageContext> serverbound(final Supplier<T> factory) {
        return new MessageBuilder<>(factory, new HandlerConsumerFactory<>(LogicalSide.SERVER, ServerMessageContext::new));
    }

    @SuppressWarnings("Convert2MethodRef")
    public <T extends Message> MessageBuilder<T, ClientMessageContext> clientbound(final Supplier<T> factory) {
        return new MessageBuilder<>(factory, DistExecutor.runForDist(() -> () -> new HandlerConsumerFactory<>(LogicalSide.CLIENT, ClientMessageContext::new), () -> () -> new NoopConsumerFactory<>()));
    }

    public SimpleChannel build() {
        return this.channel();
    }

    interface ConsumerFactory<T extends Message, S extends MessageContext> {
        BiConsumer<T, Supplier<NetworkEvent.Context>> create(final Supplier<BiConsumer<? super T, S>> handlerFactory);
    }

    private static class NoopConsumerFactory<T extends Message, S extends MessageContext> implements ConsumerFactory<T, S> {
        @Override
        public BiConsumer<T, Supplier<NetworkEvent.Context>> create(final Supplier<BiConsumer<? super T, S>> handlerFactory) {
            return (msg, ctx) -> ctx.get().setPacketHandled(false);
        }
    }

    private static class HandlerConsumerFactory<T extends Message, S extends MessageContext> implements ConsumerFactory<T, S> {
        private final LogicalSide side;
        private final Function<NetworkEvent.Context, S> contextFactory;

        HandlerConsumerFactory(final LogicalSide side, final Function<NetworkEvent.Context, S> contextFactory) {
            this.side = side;
            this.contextFactory = contextFactory;
        }

        @Override
        public BiConsumer<T, Supplier<NetworkEvent.Context>> create(final Supplier<BiConsumer<? super T, S>> handlerFactory) {
            final BiConsumer<? super T, S> handler = handlerFactory.get();
            return (msg, ctx) -> {
                final NetworkEvent.Context c = ctx.get();
                final LogicalSide receptionSide = c.getDirection().getReceptionSide();
                if (receptionSide == this.side) {
                    final S s = this.contextFactory.apply(c);
                    c.enqueueWork(() -> handler.accept(msg, s));
                }
                c.setPacketHandled(true);
            };
        }
    }

    public class MessageBuilder<T extends Message, S extends MessageContext> {
        private final Supplier<T> factory;
        private final ConsumerFactory<T, S> consumerFactory;

        protected MessageBuilder(final Supplier<T> factory, final ConsumerFactory<T, S> consumerFactory) {
            this.factory = factory;
            this.consumerFactory = consumerFactory;
        }

        public NetBuilder consumer(final Supplier<BiConsumer<? super T, S>> consumer) {
            final Supplier<T> factory = this.factory;
            @SuppressWarnings("unchecked")
            final Class<T> type = (Class<T>) factory.get().getClass();
            NetBuilder.this.channel().messageBuilder(type, NetBuilder.this.id++)
                    .encoder(Message::encode)
                    .decoder(buf -> {
                        final T msg = factory.get();
                        msg.decode(buf);
                        return msg;
                    })
                    .consumer(this.consumerFactory.create(consumer))
                    .add();
            return NetBuilder.this;
        }
    }
}
