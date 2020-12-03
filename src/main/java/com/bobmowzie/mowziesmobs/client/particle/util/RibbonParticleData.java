package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RibbonParticleData extends AdvancedParticleData {
    public static final IDeserializer<RibbonParticleData> DESERIALIZER = new IDeserializer<RibbonParticleData>() {
        public RibbonParticleData deserialize(ParticleType<RibbonParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double airDrag = (double) reader.readDouble();
            reader.expect(' ');
            double red = (double) reader.readDouble();
            reader.expect(' ');
            double green = (double) reader.readDouble();
            reader.expect(' ');
            double blue = (double) reader.readDouble();
            reader.expect(' ');
            double alpha = (double) reader.readDouble();
            reader.expect(' ');
            boolean faceCamera = reader.readBoolean();
            reader.expect(' ');
            double scale = (double) reader.readDouble();
            reader.expect(' ');
            double yaw = (double) reader.readDouble();
            reader.expect(' ');
            double pitch = (double) reader.readDouble();
            reader.expect(' ');
            double roll = (double) reader.readDouble();
            reader.expect(' ');
            boolean emissive = reader.readBoolean();
            reader.expect(' ');
            double duration = (double) reader.readDouble();
            reader.expect(' ');
            reader.readDouble();
            reader.expect(' ');
            int length = reader.readInt();
            return new RibbonParticleData(particleTypeIn, faceCamera, yaw, pitch, roll, scale, red, green, blue, alpha, airDrag, duration, emissive, length);
        }

        public RibbonParticleData read(ParticleType<RibbonParticleData> particleTypeIn, PacketBuffer buffer) {
            double airDrag = buffer.readFloat();
            double red = buffer.readFloat();
            double green = buffer.readFloat();
            double blue = buffer.readFloat();
            double alpha = buffer.readFloat();
            boolean faceCamera = buffer.readBoolean();
            double scale = buffer.readFloat();
            double yaw = buffer.readFloat();
            double pitch = buffer.readFloat();
            double roll = buffer.readFloat();
            boolean emissive = buffer.readBoolean();
            double duration = buffer.readFloat();
            buffer.readFloat();
            int length = buffer.readInt();
            return new RibbonParticleData(particleTypeIn, faceCamera, yaw, pitch, roll, scale, red, green, blue, alpha, airDrag, duration, emissive, length);
        }
    };

    private final int length;

    public RibbonParticleData(ParticleType<? extends RibbonParticleData> type, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length) {
        this(type, faceCamera, yaw, pitch, roll, scale, r, g, b, a, drag, duration, emissive, length, new ParticleComponent[]{});
    }

    public RibbonParticleData(ParticleType<? extends RibbonParticleData> type, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, ParticleComponent[] components) {
        super(type, faceCamera, yaw, pitch, roll, 0, scale, r, g, b, a, drag, duration, emissive, components);
        this.length = length;
    }

    @Override
    public void write(PacketBuffer buffer) {
        super.write(buffer);
        buffer.writeInt(this.length);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getParameters() {
        return super.getParameters() + " " + this.length;
    }

    @OnlyIn(Dist.CLIENT)
    public int getLength() {
        return this.length;
    }
}
