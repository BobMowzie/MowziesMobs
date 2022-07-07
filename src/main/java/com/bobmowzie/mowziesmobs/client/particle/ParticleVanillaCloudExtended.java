package com.bobmowzie.mowziesmobs.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class ParticleVanillaCloudExtended extends TextureSheetParticle {
    private final SpriteSet animatedSprite;

    private final float oSize;
    private final float airDrag;
    private final float red;
    private final float green;
    private final float blue;

    private final Vec3[] destination;

    protected ParticleVanillaCloudExtended(ClientLevel worldIn, SpriteSet animatedSprite, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3[] destination) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.xd *= 0.10000000149011612D;
        this.yd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        this.xd += motionX;
        this.yd += motionY;
        this.zd += motionZ;
        float f1 = 1.0F - this.random.nextFloat() * 0.3F;
        this.red = (float) (f1 * r);
        this.green = (float) (f1 * g);
        this.blue = (float) (f1 * b);
        this.quadSize *= 0.75F;
        this.quadSize *= 2.5F;
        this.oSize = this.quadSize * (float)scale;
        this.lifetime = (int)duration;
        if (lifetime == 0) lifetime = 1;
        airDrag = (float)drag;
        this.destination = destination;
        hasPhysics = false;
        this.animatedSprite = animatedSprite;
        if (destination != null) this.setSprite(animatedSprite.get(this.lifetime - this.age, this.lifetime));
        else this.setSpriteFromAge(this.animatedSprite);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }

        this.setSpriteFromAge(this.animatedSprite);

        if (destination != null && destination.length == 1) {
            this.setSprite(animatedSprite.get(this.lifetime - this.age, this.lifetime));

            Vec3 destinationVec = destination[0];
            Vec3 diff = destinationVec.subtract(new Vec3(x, y, z));
            if (diff.length() < 0.5) this.remove();
            float attractScale = 0.7f * ((float)this.age / (float)this.lifetime) * ((float)this.age / (float)this.lifetime);
            xd = diff.x * attractScale;
            yd = diff.y * attractScale;
            zd = diff.z * attractScale;
        }
        this.move(this.xd, this.yd, this.zd);
        this.xd *= airDrag;
        this.yd *= airDrag;
        this.zd *= airDrag;

        if (this.onGround)
        {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static final class CloudFactory implements ParticleProvider<VanillaCloudData> {
        private final SpriteSet spriteSet;

        public CloudFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(VanillaCloudData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleVanillaCloudExtended particle = new ParticleVanillaCloudExtended(worldIn, spriteSet, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getDrag(), typeIn.getDuration(), typeIn.getDestination());
            particle.setColor(typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue());
            return particle;
        }
    }

    public static class VanillaCloudData implements ParticleOptions {
        public static final ParticleOptions.Deserializer<VanillaCloudData> DESERIALIZER = new ParticleOptions.Deserializer<VanillaCloudData>() {
            public VanillaCloudData fromCommand(ParticleType<VanillaCloudData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                float red = (float) reader.readDouble();
                reader.expect(' ');
                float green = (float) reader.readDouble();
                reader.expect(' ');
                float blue = (float) reader.readDouble();
                reader.expect(' ');
                float drag = (float) reader.readDouble();
                reader.expect(' ');
                float duration = (float) reader.readDouble();
                return new VanillaCloudData(scale, red, green, blue, drag, duration, null);
            }

            public VanillaCloudData fromNetwork(ParticleType<VanillaCloudData> particleTypeIn, FriendlyByteBuf buffer) {
                return new VanillaCloudData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), null);
            }
        };

        private final float red;
        private final float green;
        private final float blue;
        private final float scale;
        private final float drag;
        private final float duration;
        private final Vec3[] destination;

        public VanillaCloudData(float scale, float redIn, float greenIn, float blueIn, float drag, float duration, Vec3[] destination) {
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.scale = scale;
            this.drag = drag;
            this.duration = duration;
            this.destination = destination;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.scale);
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.drag);
            buffer.writeFloat(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.scale, this.red, this.green, this.blue, this.drag, this.duration);
        }

        @Override
        public ParticleType<VanillaCloudData> getType() {
            return ParticleHandler.VANILLA_CLOUD_EXTENDED.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @OnlyIn(Dist.CLIENT)
        public float getRed() {
            return this.red;
        }

        @OnlyIn(Dist.CLIENT)
        public float getGreen() {
            return this.green;
        }

        @OnlyIn(Dist.CLIENT)
        public float getBlue() {
            return this.blue;
        }

        @OnlyIn(Dist.CLIENT)
        public float getDrag() {
            return this.drag;
        }

        @OnlyIn(Dist.CLIENT)
        public float getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public Vec3[] getDestination() {
            return this.destination;
        }

        public static Codec<VanillaCloudData> CODEC(ParticleType<VanillaCloudData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(VanillaCloudData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(VanillaCloudData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(VanillaCloudData::getBlue),
                    Codec.FLOAT.fieldOf("scale").forGetter(VanillaCloudData::getScale),
                    Codec.FLOAT.fieldOf("duration").forGetter(VanillaCloudData::getDuration),
                    Codec.FLOAT.fieldOf("drag").forGetter(VanillaCloudData::getScale)
                    ).apply(codecBuilder, (r, g, b, scale, duration, drag) ->
                        new VanillaCloudData(r, g, b, scale, drag, duration, null))
            );
        }
    }

    public static void spawnVanillaCloud(Level world, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration) {
        world.addParticle(new VanillaCloudData((float)scale, (float)r, (float)g, (float)b, (float)drag, (float)duration, null), x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnVanillaCloudDestination(Level world, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3[] destination) {
        world.addParticle(new VanillaCloudData((float)scale, (float)r, (float)g, (float)b, (float)drag, (float)duration, destination), x, y, z, motionX, motionY, motionZ);
    }
}
