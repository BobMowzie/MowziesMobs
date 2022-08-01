package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class ParticleOrb extends TextureSheetParticle {
    private double targetX;
    private double targetY;
    private double targetZ;
    private double startX;
    private double startY;
    private double startZ;
    private double signX;
    private double signZ;
    private float red, green, blue;
    private int mode;
    private double duration;

    public ParticleOrb(ClientLevel world, double x, double y, double z, double targetX, double targetZ) {
        super(world, x, y, z);
        this.targetX = targetX;
        this.targetZ = targetZ;
        quadSize = (4.5F + random.nextFloat() * 1.5F) * 0.1f;
        lifetime = 120;
        signX = Math.signum(targetX - x);
        signZ = Math.signum(targetZ - z);
        mode = 0;
        alpha = 0;
        red = green = blue = 1;
    }

    public ParticleOrb(ClientLevel world, double x, double y, double z, double targetX, double targetY, double targetZ, double speed) {
        this(world, x, y, z, targetX, targetZ);
        this.targetY = targetY;
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.duration = speed;
        mode = 1;
        alpha = 0.1f;
    }

    public ParticleOrb(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        quadSize = (float) scale * 0.1f;
        lifetime = duration;
        this.duration = duration;
        xd = vx;
        yd = vy;
        zd = vz;
        setColor((float) r, (float) g, (float) b);
        mode = 2;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public int getLightColor(float delta) {
        return 240 | super.getLightColor(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        alpha = 0.1f;
        xo = x;
        yo = y;
        zo = z;
        if (mode == 0) {
            double vecX = targetX - x;
            double vecZ = targetZ - z;
            double dist = Math.sqrt(vecX * vecX + vecZ * vecZ);
            if (dist > 2 || Math.signum(vecX) != signX || Math.signum(vecZ) != signZ || age > lifetime) {
                remove();
                return;
            }
            final double peak = 0.5;
            alpha = (float) (dist > peak ? MathUtils.linearTransformd(dist, peak, 2, 1, 0) : MathUtils.linearTransformd(dist, 0.1F, peak, 0, 1));
            final double minVel = 0.05, maxVel = 0.3;
            double progress = Math.sin(-Math.PI / 4 * dist) + 1;
            double magMultipler = (progress * (maxVel - minVel) + minVel) / dist;
            vecX *= magMultipler;
            vecZ *= magMultipler;
            xd = vecX;
            yd = progress;
            zd = vecZ;
            move(xd, yd, zd);
        } else if (mode == 1) {
            alpha = ((float)age/(float)duration);//(float) (1 * Math.sqrt(Math.pow(posX - startX, 2) + Math.pow(posY - startY, 2) + Math.pow(posZ - startZ, 2)) / Math.sqrt(Math.pow(targetX - startX, 2) + Math.pow(targetY - startY, 2) + Math.pow(targetZ - startZ, 2)));
            x = startX + (targetX - startX) / (1 + Math.exp(-(8 / duration) * (age - duration / 2)));
            y = startY + (targetY - startY) / (1 + Math.exp(-(8 / duration) * (age - duration / 2)));
            z = startZ + (targetZ - startZ) / (1 + Math.exp(-(8 / duration) * (age - duration / 2)));
            if (age == duration) {
                remove();
            }
        }
        else if (mode == 2) {
            super.tick();
//            particleAlpha = ((float)age/(float)maxAge);
            if (age >= lifetime) {
                remove();
            }
        }
        age++;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (mode == 2) alpha = Math.max(1 - ((float)age + partialTicks)/(float)duration, 0.001f);
        else alpha = ((float)age + partialTicks)/(float)duration;
        rCol = red;
        gCol = green;
        bCol = blue;

        super.render(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OrbFactory implements ParticleProvider<OrbData> {
        private final SpriteSet spriteSet;

        public OrbFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(ParticleOrb.OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleOrb particle;
            if (typeIn.getMode() == 0) particle = new ParticleOrb(worldIn, x, y, z, typeIn.getTargetX(), typeIn.getTargetZ());
            else if (typeIn.getMode() == 1) particle = new ParticleOrb(worldIn, x, y, z, typeIn.getTargetX(), typeIn.getTargetY(), typeIn.getTargetZ(), typeIn.getSpeed());
            else particle = new ParticleOrb(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getScale(), typeIn.getDuration());
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }

    public static class OrbData implements ParticleOptions {
        public static final ParticleOptions.Deserializer<ParticleOrb.OrbData> DESERIALIZER = new ParticleOptions.Deserializer<ParticleOrb.OrbData>() {
            public ParticleOrb.OrbData fromCommand(ParticleType<ParticleOrb.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                return new ParticleOrb.OrbData(r, g, b, scale, duration);
            }

            public ParticleOrb.OrbData fromNetwork(ParticleType<ParticleOrb.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new ParticleOrb.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
            }
        };

        private final float r;
        private final float g;
        private final float b;
        private float scale;
        private int duration;

        private float targetX;
        private float targetY;
        private float targetZ;
        private float speed;

        private int mode;

        public OrbData(float targetX, float targetZ) {
            this.targetX = targetX;
            this.targetZ = targetZ;
            this.r = this.g = this.b = 1;

            this.mode = 0;
        }

        public OrbData(float targetX, float targetY, float targetZ, float speed) {
            this(targetX, targetZ);
            this.targetY = targetY;
            this.speed = speed;

            this.mode = 1;
        }

        public OrbData(float r, float g, float b, float scale, int duration) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
            this.duration = duration;

            this.mode = 2;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b, this.scale, this.duration);
        }

        @Override
        public ParticleType<ParticleOrb.OrbData> getType() {
            return ParticleHandler.ORB.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getR() {
            return this.r;
        }

        @OnlyIn(Dist.CLIENT)
        public float getG() {
            return this.g;
        }

        @OnlyIn(Dist.CLIENT)
        public float getB() {
            return this.b;
        }

        @OnlyIn(Dist.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @OnlyIn(Dist.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public float getTargetX() {
            return this.targetX;
        }

        @OnlyIn(Dist.CLIENT)
        public float getTargetY() {
            return this.targetY;
        }

        @OnlyIn(Dist.CLIENT)
        public float getTargetZ() {
            return this.targetZ;
        }

        @OnlyIn(Dist.CLIENT)
        public float getSpeed() {
            return this.speed;
        }

        @OnlyIn(Dist.CLIENT)
        public int getMode() {
            return this.mode;
        }

        public static Codec<OrbData> CODEC(ParticleType<OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(OrbData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(OrbData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(OrbData::getB),
                    Codec.FLOAT.fieldOf("scale").forGetter(OrbData::getScale),
                    Codec.INT.fieldOf("duration").forGetter(OrbData::getDuration)
                    ).apply(codecBuilder, OrbData::new)
            );
        }
    }
}
