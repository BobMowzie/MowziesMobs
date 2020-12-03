package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import com.mojang.brigadier.StringReader;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class AdvancedParticleData implements IParticleData {
    public static final IParticleData.IDeserializer<AdvancedParticleData> DESERIALIZER = new IParticleData.IDeserializer<AdvancedParticleData>() {
        public AdvancedParticleData deserialize(ParticleType<AdvancedParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
            double faceCameraAngle = (double) reader.readDouble();
            return new AdvancedParticleData(particleTypeIn, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, red, green, blue, alpha, airDrag, duration, emissive);
        }

        public AdvancedParticleData read(ParticleType<AdvancedParticleData> particleTypeIn, PacketBuffer buffer) {
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
            double faceCameraAngle = buffer.readFloat();
            return new AdvancedParticleData(particleTypeIn, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, red, green, blue, alpha, airDrag, duration, emissive);
        }
    };

    private final ParticleType<? extends AdvancedParticleData> type;

    private final float airDrag;
    private final float red, green, blue, alpha;
    private final boolean faceCamera;
    private final float scale;
    private final float yaw, pitch, roll;
    private final boolean emissive;
    private final float duration;
    private final float faceCameraAngle;

    private final ParticleComponent[] components;

    public AdvancedParticleData(ParticleType<? extends AdvancedParticleData> type, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive) {
        this(type, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, r, g, b, a, drag, duration, emissive, new ParticleComponent[]{});
    }

    public AdvancedParticleData(ParticleType<? extends AdvancedParticleData> type, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, ParticleComponent[] components) {
        this.type = type;

        this.faceCamera = faceCamera;
        this.faceCameraAngle = (float) faceCameraAngle;

        this.yaw = (float) yaw;
        this.pitch = (float) pitch;
        this.roll = (float) roll;

        this.scale = (float) scale;

        this.red = (float) r;
        this.green = (float) g;
        this.blue = (float) b;
        this.alpha = (float) a;
        this.emissive = emissive;

        this.airDrag = (float) drag;

        this.duration = (float) duration;

        this.components = components;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.airDrag);
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.alpha);
        buffer.writeBoolean(this.faceCamera);
        buffer.writeFloat(this.scale);
        buffer.writeFloat(this.yaw);
        buffer.writeFloat(this.pitch);
        buffer.writeFloat(this.roll);
        buffer.writeBoolean(this.emissive);
        buffer.writeFloat(this.duration);
        buffer.writeFloat(this.faceCameraAngle);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %b %.2f %.2f %.2f %.2f %b %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                this.airDrag, this.red, this.green, this.blue, this.alpha, this.faceCamera, this.scale, this.yaw, this.pitch, this.roll, this.emissive, this.duration, this.faceCameraAngle);
    }

    @Override
    public ParticleType<? extends AdvancedParticleData> getType() {
        return type;
    }

    @OnlyIn(Dist.CLIENT)
    public double getRed() {
        return this.red;
    }

    @OnlyIn(Dist.CLIENT)
    public double getGreen() {
        return this.green;
    }

    @OnlyIn(Dist.CLIENT)
    public double getBlue() {
        return this.blue;
    }

    @OnlyIn(Dist.CLIENT)
    public double getAlpha() {
        return this.alpha;
    }

    @OnlyIn(Dist.CLIENT)
    public double getAirDrag() {
        return airDrag;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean isFaceCamera() {
        return faceCamera;
    }

    @OnlyIn(Dist.CLIENT)
    public double getScale() {
        return scale;
    }

    @OnlyIn(Dist.CLIENT)
    public double getYaw() {
        return yaw;
    }

    @OnlyIn(Dist.CLIENT)
    public double getPitch() {
        return pitch;
    }

    @OnlyIn(Dist.CLIENT)
    public double getRoll() {
        return roll;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmissive() {
        return emissive;
    }

    @OnlyIn(Dist.CLIENT)
    public double getDuration() {
        return duration;
    }

    @OnlyIn(Dist.CLIENT)
    public double getFaceCameraAngle() {
        return faceCameraAngle;
    }

    @OnlyIn(Dist.CLIENT)
    public ParticleComponent[] getComponents() {
        return components;
    }
}
