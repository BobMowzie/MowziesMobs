package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DecalParticleData extends AdvancedParticleData {
    public static final Deserializer<DecalParticleData> DESERIALIZER = new Deserializer<DecalParticleData>() {
        public DecalParticleData fromCommand(ParticleType<DecalParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double airDrag = reader.readDouble();
            reader.expect(' ');
            double red = reader.readDouble();
            reader.expect(' ');
            double green = reader.readDouble();
            reader.expect(' ');
            double blue = reader.readDouble();
            reader.expect(' ');
            double alpha = reader.readDouble();
            reader.expect(' ');
            double scale = reader.readDouble();
            reader.expect(' ');
            double angle = reader.readDouble();
            reader.expect(' ');
            boolean emissive = reader.readBoolean();
            reader.expect(' ');
            double duration = reader.readDouble();
            reader.expect(' ');
            int spriteSize = reader.readInt();
            reader.expect(' ');
            int bufferSize = reader.readInt();
            reader.expect(' ');
            return new DecalParticleData(particleTypeIn, angle, scale, red, green, blue, alpha, airDrag, duration, emissive, spriteSize, bufferSize);
        }

        public DecalParticleData fromNetwork(ParticleType<DecalParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            double airDrag = buffer.readFloat();
            double red = buffer.readFloat();
            double green = buffer.readFloat();
            double blue = buffer.readFloat();
            double alpha = buffer.readFloat();
            double scale = buffer.readFloat();
            double angle = buffer.readFloat();
            boolean emissive = buffer.readBoolean();
            double duration = buffer.readFloat();
            int spriteSize = buffer.readInt();
            int bufferSize = buffer.readInt();
            return new DecalParticleData(particleTypeIn, angle, scale, red, green, blue, alpha, airDrag, duration, emissive, spriteSize, bufferSize);
        }
    };

    private final int spriteSize;
    private final int bufferSize;

    public DecalParticleData(ParticleType<? extends DecalParticleData> type, double rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int spriteSize, int bufferSize) {
        this(type, rotation, scale, r, g, b, a, drag, duration, emissive, spriteSize, bufferSize, new ParticleComponent[]{});
    }

    public DecalParticleData(ParticleType<? extends DecalParticleData> type, double angle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int spriteSize, int bufferSize, ParticleComponent[] components) {
        super(type, new ParticleRotation.EulerAngles((float) angle, 0, 0), scale, r, g, b, a, drag, duration, emissive, false, components);
        this.spriteSize = spriteSize;
        this.bufferSize = bufferSize;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        super.writeToNetwork(buffer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String writeToString() {
        return super.writeToString() + " " + this.spriteSize + " " + this.bufferSize;
    }

    @OnlyIn(Dist.CLIENT)
    public double getAngle() {
        if (this.getRotation() instanceof ParticleRotation.EulerAngles) {
            return ((ParticleRotation.EulerAngles) this.getRotation()).yaw;
        }
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSpriteSize() {
        return spriteSize;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBufferSize() {
        return bufferSize;
    }

    public static Codec<DecalParticleData> CODEC_RIBBON(ParticleType<DecalParticleData> particleType) {
        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                Codec.DOUBLE.fieldOf("scale").forGetter(DecalParticleData::getScale),
                Codec.DOUBLE.fieldOf("r").forGetter(DecalParticleData::getRed),
                Codec.DOUBLE.fieldOf("g").forGetter(DecalParticleData::getGreen),
                Codec.DOUBLE.fieldOf("b").forGetter(DecalParticleData::getBlue),
                Codec.DOUBLE.fieldOf("a").forGetter(DecalParticleData::getAlpha),
                Codec.DOUBLE.fieldOf("drag").forGetter(DecalParticleData::getAirDrag),
                Codec.DOUBLE.fieldOf("duration").forGetter(DecalParticleData::getDuration),
                Codec.BOOL.fieldOf("emissive").forGetter(DecalParticleData::isEmissive),
                Codec.DOUBLE.fieldOf("angle").forGetter(DecalParticleData::getAngle),
                Codec.INT.fieldOf("sprite_size").forGetter(DecalParticleData::getSpriteSize),
                Codec.INT.fieldOf("buffer_size").forGetter(DecalParticleData::getBufferSize)
                ).apply(codecBuilder, (scale, r, g, b, a, drag, duration, emissive, angle, spriteSize, bufferSize) ->
                    new DecalParticleData(particleType, angle, scale, r, g, b, a, drag, duration, emissive, spriteSize, bufferSize, new ParticleComponent[]{}))
        );
    }
}
