package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Locale;

import net.minecraft.client.Camera;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleSnowFlake extends TextureSheetParticle {
    private int swirlTick;
    private final float spread;
    boolean swirls;

    public ParticleSnowFlake(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls) {
        super(world, x, y, z);
        setSize(1, 1);
        xd = vX;
        yd = vY;
        zd = vZ;
        lifetime = (int) duration;
        swirlTick = random.nextInt(120);
        spread = random.nextFloat();
        this.swirls = swirls;
    }

    @Override
    protected float getU1() {
        return super.getU1() - (super.getU1() - super.getU0())/8f;
    }

    @Override
    protected float getV1() {
        return super.getV1() - (super.getV1() - super.getV0())/8f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public void tick() {
        super.tick();

        if (swirls) {
            Vector3f motionVec = new Vector3f((float)xd, (float)yd, (float)zd);
            motionVec.normalize();
            float yaw = (float) Math.atan2(motionVec.x(), motionVec.z());
            float pitch = (float) Math.atan2(motionVec.y(), 1);
            float swirlRadius = 4f * (age / (float) lifetime) * spread;
            Quaternion quatSpin = motionVec.rotation(swirlTick * 0.2f);
            Quaternion quatOrient = new Quaternion(pitch, yaw, 0, false);
            Vector3f vec = new Vector3f(swirlRadius, 0, 0);
            vec.transform(quatOrient);
            vec.transform(quatSpin);
            x += vec.x();
            y += vec.y();
            z += vec.z();
        }

        if (age >= lifetime) {
            remove();
        }
        age++;
        swirlTick++;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float var = (age + partialTicks)/(float)lifetime;
        alpha = (float) (1 - Math.exp(10 * (var - 1)) - Math.pow(2000, -var));
        if (alpha < 0.1) alpha = 0.1f;

        super.render(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class SnowFlakeFactory implements ParticleProvider<ParticleSnowFlake.SnowflakeData> {
        private final SpriteSet spriteSet;

        public SnowFlakeFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(ParticleSnowFlake.SnowflakeData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSnowFlake particle = new ParticleSnowFlake(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getDuration(), typeIn.getSwirls());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }

    public static class SnowflakeData implements ParticleOptions {
        public static final ParticleOptions.Deserializer<ParticleSnowFlake.SnowflakeData> DESERIALIZER = new ParticleOptions.Deserializer<ParticleSnowFlake.SnowflakeData>() {
            public ParticleSnowFlake.SnowflakeData fromCommand(ParticleType<ParticleSnowFlake.SnowflakeData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float duration = (float) reader.readDouble();
                reader.expect(' ');
                boolean swirls = reader.readBoolean();
                return new ParticleSnowFlake.SnowflakeData(duration, swirls);
            }

            public ParticleSnowFlake.SnowflakeData fromNetwork(ParticleType<ParticleSnowFlake.SnowflakeData> particleTypeIn, FriendlyByteBuf buffer) {
                return new ParticleSnowFlake.SnowflakeData(buffer.readFloat(), buffer.readBoolean());
            }
        };

        private final float duration;
        private final boolean swirls;

        public SnowflakeData(float duration, boolean spins) {
            this.duration = duration;
            this.swirls = spins;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.duration);
            buffer.writeBoolean(this.swirls);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %b", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.duration, this.swirls);
        }

        @Override
        public ParticleType<ParticleSnowFlake.SnowflakeData> getType() {
            return ParticleHandler.SNOWFLAKE.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public boolean getSwirls() {
            return this.swirls;
        }

        public static Codec<SnowflakeData> CODEC(ParticleType<SnowflakeData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("duration").forGetter(SnowflakeData::getDuration),
                    Codec.BOOL.fieldOf("swirls").forGetter(SnowflakeData::getSwirls)
                    ).apply(codecBuilder, SnowflakeData::new)
            );
        }
    }
}
