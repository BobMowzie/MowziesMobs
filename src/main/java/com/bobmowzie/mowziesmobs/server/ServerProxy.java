package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.eventbus.api.IEventBus;

import java.io.IOException;

public class ServerProxy {
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
                return Optional.absent();
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
            return Optional.absent();
        }
    };

    public void init(final IEventBus modbus) {
        DataSerializers.registerSerializer(OPTIONAL_TRADE);
    }

    public void onLateInit(final IEventBus modbus) {}

    public void playSunstrikeSound(EntitySunstrike strike) {}

    public void playIceBreathSound(Entity entity) {}

    public void playBoulderChargeSound(PlayerEntity player) {}

    public void playNagaSwoopSound(EntityNaga naga) {}

    public void solarBeamHitWroughtnaught(LivingEntity caster) {}
}
