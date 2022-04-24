package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleOption;
import net.minecraft.particles.ParticleType;
import net.minecraft.sounds.registry.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;
import java.util.Optional;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleCloud extends SpriteTexturedParticle {
    private final float red;
    private final float green;
    private final float blue;
    private final float scale;
    private final EnumCloudBehavior behavior;
    private final float airDrag;

    public enum EnumCloudBehavior {
        SHRINK,
        GROW,
        CONSTANT
    }

    public ParticleCloud(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration, EnumCloudBehavior behavior, double airDrag) {
        super(world, x, y, z);
        this.scale = (float) scale * 0.5f * 0.1f;
        maxAge = duration;
        motionX = vx * 0.5;
        motionY = vy * 0.5;
        motionZ = vz * 0.5;
        red = (float) r;
        green = (float) g;
        blue = (float) b;
        this.behavior = behavior;
        particleAngle = prevParticleAngle = (float) (random.nextInt(4) * Math.PI/2);
        this.airDrag = (float) airDrag;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public void tick() {
        super.tick();
        motionX *= airDrag;
        motionY *= airDrag;
        motionZ *= airDrag;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        float var = (age + partialTicks)/(float)maxAge;
        particleAlpha = 0.2f * ((float) (1 - Math.exp(5 * (var - 1)) - Math.pow(2000, -var)));
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        if (behavior == EnumCloudBehavior.SHRINK) this.particleScale = scale * ((1 - 0.7f * var) + 0.3f);
        else if (behavior == EnumCloudBehavior.GROW) this.particleScale = scale * ((0.7f * var) + 0.3f);
        else this.particleScale = scale;

        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class CloudFactory implements IParticleFactory<CloudData> {
        private final IAnimatedSprite spriteSet;

        public CloudFactory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(CloudData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleCloud particleCloud = new ParticleCloud(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getScale(), typeIn.getDuration(), typeIn.getBehavior(), typeIn.getAirDrag());
            particleCloud.selectSpriteWithAge(spriteSet);
            particleCloud.setColor(typeIn.getR(), typeIn.getG(), typeIn.getB());
            return particleCloud;
        }
    }

    public static class CloudData implements IParticleData {
        public static final IParticleData.IDeserializer<ParticleCloud.CloudData> DESERIALIZER = new IParticleData.IDeserializer<ParticleCloud.CloudData>() {
            public ParticleCloud.CloudData deserialize(ParticleType<ParticleCloud.CloudData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                reader.expect(' ');
                float airDrag = (float) reader.readDouble();
                return new ParticleCloud.CloudData(particleTypeIn, r, g, b, scale, duration, EnumCloudBehavior.CONSTANT, airDrag);
            }

            public ParticleCloud.CloudData read(ParticleType<ParticleCloud.CloudData> particleTypeIn, FriendlyByteBuf buffer) {
                return new ParticleCloud.CloudData(particleTypeIn, buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), EnumCloudBehavior.CONSTANT, buffer.readFloat());
            }
        };

        private final ParticleType<ParticleCloud.CloudData> type;

        private final float r;
        private final float g;
        private final float b;
        private final float scale;
        private final int duration;
        private final EnumCloudBehavior behavior;
        private final float airDrag;

        public CloudData(ParticleType<ParticleCloud.CloudData> type, float r, float g, float b, float scale, int duration, EnumCloudBehavior behavior, float airDrag) {
            this.type = type;
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
            this.behavior = behavior;
            this.airDrag = airDrag;
            this.duration = duration;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
            buffer.writeFloat(this.airDrag);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b, this.scale, this.duration, this.airDrag);
        }

        @Override
        public ParticleType<ParticleCloud.CloudData> getType() {
            return type;
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
        public EnumCloudBehavior getBehavior() {
            return this.behavior;
        }

        @OnlyIn(Dist.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public float getAirDrag() {
            return this.airDrag;
        }

        public static Codec<CloudData> CODEC(ParticleType<CloudData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(CloudData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(CloudData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(CloudData::getB),
                    Codec.FLOAT.fieldOf("scale").forGetter(CloudData::getScale),
                    Codec.STRING.fieldOf("behavior").forGetter((cloudData) -> cloudData.getBehavior().toString()),
                    Codec.INT.fieldOf("duration").forGetter(CloudData::getDuration),
                    Codec.FLOAT.fieldOf("airdrag").forGetter(CloudData::getAirDrag)
            ).apply(codecBuilder, (r, g, b, scale, behavior, duration, airdrag) ->
                        new CloudData(particleType, r, g, b, scale, duration, EnumCloudBehavior.valueOf(behavior), airdrag))
            );
        }
    }
}
