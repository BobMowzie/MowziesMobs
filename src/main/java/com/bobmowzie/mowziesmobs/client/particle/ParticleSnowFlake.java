package com.bobmowzie.mowziesmobs.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Locale;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleSnowFlake extends SpriteTexturedParticle {
    private int swirlTick;
    private float spread;
    boolean swirls;

    public ParticleSnowFlake(World world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls) {
        super(world, x, y, z);
        setSize(1, 1);
        motionX = vX;
        motionY = vY;
        motionZ = vZ;
        maxAge = (int) duration;
        swirlTick = rand.nextInt(120);
        spread = rand.nextFloat();
        this.swirls = swirls;
    }

    @Override
    protected float getMaxU() {
        return super.getMaxU() - (super.getMaxU() - super.getMinU())/8f;
    }

    @Override
    protected float getMaxV() {
        return super.getMaxV() - (super.getMaxV() - super.getMinV())/8f;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        if (swirls) {
            /*Vec3d motionVec = new Vec3d(motionX, motionY, motionZ).normalize();
            float yaw = (float) Math.atan2(motionVec.x, motionVec.z);
            float xzDistance = (float) motionVec.length();
            float pitch = (float) Math.atan2(motionVec.y, xzDistance);
            float swirlRadius = 4f * (age / (float) maxAge) * spread;
            Point3d point = new Point3d(swirlRadius * Math.cos(swirlTick * 0.2), swirlRadius * Math.sin(swirlTick * 0.2), 0);
            Matrix4d boxRotateX = new Matrix4d();
            Matrix4d boxRotateY = new Matrix4d();
            boxRotateX.rotX(pitch);
            boxRotateY.rotY(yaw);
            boxRotateX.transform(point);
            boxRotateY.transform(point);
            posX += point.x;
            posY += point.y;
            posZ += point.z;*/ // TODO

//            posY += swirlRadius * Math.cos(swirlTick * 0.2) * (Math.sqrt(1 - y * y)) * (Math.sqrt(1 - z * z));
        }

        if (age >= maxAge) {
            setExpired();
        }
        age++;
        swirlTick++;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        float var = (age + partialTicks)/(float)maxAge;
        particleAlpha = (float) (1 - Math.exp(10 * (var - 1)) - Math.pow(2000, -var));
        if (particleAlpha < 0.1) particleAlpha = 0.1f;

        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class SnowFlakeFactory implements IParticleFactory<ParticleSnowFlake.SnowflakeData> {
        private final IAnimatedSprite spriteSet;

        public SnowFlakeFactory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(ParticleSnowFlake.SnowflakeData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSnowFlake particle = new ParticleSnowFlake(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getDuration(), typeIn.getSwirls());
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }

    public static class SnowflakeData implements IParticleData {
        public static final IParticleData.IDeserializer<ParticleSnowFlake.SnowflakeData> DESERIALIZER = new IParticleData.IDeserializer<ParticleSnowFlake.SnowflakeData>() {
            public ParticleSnowFlake.SnowflakeData deserialize(ParticleType<ParticleSnowFlake.SnowflakeData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float duration = (float) reader.readDouble();
                reader.expect(' ');
                boolean swirls = reader.readBoolean();
                return new ParticleSnowFlake.SnowflakeData(duration, swirls);
            }

            public ParticleSnowFlake.SnowflakeData read(ParticleType<ParticleSnowFlake.SnowflakeData> particleTypeIn, PacketBuffer buffer) {
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
        public void write(PacketBuffer buffer) {
            buffer.writeFloat(this.duration);
            buffer.writeBoolean(this.swirls);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
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
    }
}
