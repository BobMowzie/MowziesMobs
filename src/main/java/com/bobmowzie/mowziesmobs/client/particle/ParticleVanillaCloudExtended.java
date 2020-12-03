package com.bobmowzie.mowziesmobs.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.particle.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class ParticleVanillaCloudExtended extends SpriteTexturedParticle {
    private final IAnimatedSprite animatedSprite;

    private float oSize;
    private float airDrag;
    private float red, green, blue;

    private Vec3d[] destination;

    protected ParticleVanillaCloudExtended(World worldIn, IAnimatedSprite animatedSprite, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3d[] destination) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += motionX;
        this.motionY += motionY;
        this.motionZ += motionZ;
        float f1 = 1.0F - this.rand.nextFloat() * 0.3F;
        this.red = (float) (f1 * r);
        this.green = (float) (f1 * g);
        this.blue = (float) (f1 * b);
        this.particleScale *= 0.75F;
        this.particleScale *= 2.5F;
        this.oSize = this.particleScale * (float)scale;
        this.maxAge = (int)duration;
        if (maxAge == 0) maxAge = 1;
        airDrag = (float)drag;
        this.destination = destination;
        canCollide = false;
        this.animatedSprite = animatedSprite;
        if (destination != null) this.setSprite(animatedSprite.get(this.maxAge - this.age, this.maxAge));
        else this.selectSpriteWithAge(this.animatedSprite);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        this.selectSpriteWithAge(this.animatedSprite);

        if (destination != null && destination.length == 1) {
            this.setSprite(animatedSprite.get(this.maxAge - this.age, this.maxAge));

            Vec3d destinationVec = destination[0];
            Vec3d diff = destinationVec.subtract(new Vec3d(posX, posY, posZ));
            if (diff.length() < 0.5) this.setExpired();
            float attractScale = 0.7f * ((float)this.age / (float)this.maxAge) * ((float)this.age / (float)this.maxAge);
            motionX = diff.x * attractScale;
            motionY = diff.y * attractScale;
            motionZ = diff.z * attractScale;
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= airDrag;
        this.motionY *= airDrag;
        this.motionZ *= airDrag;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static final class CloudFactory implements IParticleFactory<VanillaCloudData> {
        private final IAnimatedSprite spriteSet;

        public CloudFactory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(VanillaCloudData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleVanillaCloudExtended particle = new ParticleVanillaCloudExtended(worldIn, spriteSet, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getDrag(), typeIn.getDuration(), typeIn.getDestination());
            particle.setColor(typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue());
            return particle;
        }
    }

    public static class VanillaCloudData implements IParticleData {
        public static final IParticleData.IDeserializer<VanillaCloudData> DESERIALIZER = new IParticleData.IDeserializer<VanillaCloudData>() {
            public VanillaCloudData deserialize(ParticleType<VanillaCloudData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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

            public VanillaCloudData read(ParticleType<VanillaCloudData> particleTypeIn, PacketBuffer buffer) {
                return new VanillaCloudData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), null);
            }
        };

        private final float red;
        private final float green;
        private final float blue;
        private final float scale;
        private final float drag;
        private final float duration;
        private final Vec3d[] destination;

        public VanillaCloudData(float scale, float redIn, float greenIn, float blueIn, float drag, float duration, Vec3d[] destination) {
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.scale = scale;
            this.drag = drag;
            this.duration = duration;
            this.destination = destination;
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeFloat(this.scale);
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.drag);
            buffer.writeFloat(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
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
        public Vec3d[] getDestination() {
            return this.destination;
        }
    }

    public static void spawnVanillaCloud(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration) {
        world.addParticle(new VanillaCloudData((float)scale, (float)r, (float)g, (float)b, (float)drag, (float)duration, null), x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnVanillaCloudDestination(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3d[] destination) {
        world.addParticle(new VanillaCloudData((float)scale, (float)r, (float)g, (float)b, (float)drag, (float)duration, destination), x, y, z, motionX, motionY, motionZ);
    }
}
