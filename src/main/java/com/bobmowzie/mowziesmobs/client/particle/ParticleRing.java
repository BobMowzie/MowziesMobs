package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Quaternion;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vector3f;
import net.minecraft.sounds.registry.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Locale;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleRing extends SpriteTexturedParticle {
    public float r, g, b;
    public float opacity;
    public boolean facesCamera;
    public float yaw, pitch;
    public float size;

    private final EnumRingBehavior behavior;

    public enum EnumRingBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    public ParticleRing(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera, EnumRingBehavior behavior) {
        super(world, x, y, z);
        setSize(1, 1);
        this.size = size * 0.1f;
        maxAge = duration;
        particleAlpha = 1;
        this.r = r;
        this.g = g;
        this.b = b;
        this.opacity = opacity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.facesCamera = facesCamera;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.behavior = behavior;
    }

    @Override
    public int getBrightnessForRender(float delta) {
        return 240 | super.getBrightnessForRender(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        super.tick();
        if (age >= maxAge) {
            setExpired();
        }
        age++;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        float var = (age + partialTicks)/maxAge;
        if (behavior == EnumRingBehavior.GROW) {
            particleScale = size * var;
        }
        else if (behavior == EnumRingBehavior.SHRINK) {
            particleScale = size * (1 - var);
        }
        else if (behavior == EnumRingBehavior.GROW_THEN_SHRINK) {
            particleScale = (float) (size * (1 - var - Math.pow(2000, -var)));
        }
        else {
            particleScale = size;
        }
        particleAlpha = opacity * 0.95f * (1 - (age + partialTicks)/maxAge) + 0.05f;
        particleRed = r;
        particleGreen = g;
        particleBlue = b;

        Vec3 Vec3 = renderInfo.getProjectedView();
        float f = (float)(Mth.lerp(partialTicks, this.xo, this.posX) - Vec3.x());
        float f1 = (float)(Mth.lerp(partialTicks, this.prevPosY, this.posY) - Vec3.y());
        float f2 = (float)(Mth.lerp(partialTicks, this.zo, this.posZ) - Vec3.z());
        Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        if (facesCamera) {
            if (this.particleAngle == 0.0F) {
                quaternion = renderInfo.getRotation();
            } else {
                quaternion = new Quaternion(renderInfo.getRotation());
                float f3 = Mth.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
                quaternion.multiply(Vector3f.ZP.rotation(f3));
            }
        }
        else {
            Quaternion quatX = new Quaternion(pitch, 0, 0, false);
            Quaternion quatY = new Quaternion(0, yaw, 0, false);
            quaternion.multiply(quatY);
            quaternion.multiply(quatX);
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getScale(partialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int j = this.getBrightnessForRender(partialTicks);
        buffer.pos(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class RingFactory implements IParticleFactory<RingData> {
        private final IAnimatedSprite spriteSet;

        public RingFactory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(RingData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRing particle = new ParticleRing(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getYaw(), typeIn.getPitch(), typeIn.getDuration(), typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getA(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior());
            particle.selectSpriteWithAge(spriteSet);
            return particle;
        }
    }

    public static class RingData implements IParticleData {
        public static final IParticleData.IDeserializer<ParticleRing.RingData> DESERIALIZER = new IParticleData.IDeserializer<ParticleRing.RingData>() {
            public ParticleRing.RingData deserialize(ParticleType<ParticleRing.RingData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float yaw = (float) reader.readDouble();
                reader.expect(' ');
                float pitch = (float) reader.readDouble();
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float a = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                reader.expect(' ');
                boolean facesCamera = reader.readBoolean();
                return new ParticleRing.RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.GROW);
            }

            public ParticleRing.RingData read(ParticleType<ParticleRing.RingData> particleTypeIn, FriendlyByteBuf buffer) {
                return new ParticleRing.RingData(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), EnumRingBehavior.GROW);
            }
        };

        private final float yaw;
        private final float pitch;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final float scale;
        private final int duration;
        private final boolean facesCamera;
        private final EnumRingBehavior behavior;

        public RingData(float yaw, float pitch, int duration, float r, float g, float b, float a, float scale, boolean facesCamera, EnumRingBehavior behavior) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.scale = scale;
            this.duration = duration;
            this.facesCamera = facesCamera;
            this.behavior = behavior;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d %b", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.yaw, this.pitch, this.r, this.g, this.b, this.scale, this.a, this.duration, this.facesCamera);
        }

        @Override
        public ParticleType<ParticleRing.RingData> getType() {
            return ParticleHandler.RING.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getYaw() {
            return this.yaw;
        }

        @OnlyIn(Dist.CLIENT)
        public float getPitch() {
            return this.pitch;
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
        public float getA() {
            return this.a;
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
        public boolean getFacesCamera() {
            return this.facesCamera;
        }

        @OnlyIn(Dist.CLIENT)
        public EnumRingBehavior getBehavior() {
            return this.behavior;
        }

        public static Codec<RingData> CODEC(ParticleType<RingData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("yaw").forGetter(RingData::getYaw),
                    Codec.FLOAT.fieldOf("pitch").forGetter(RingData::getPitch),
                    Codec.FLOAT.fieldOf("r").forGetter(RingData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(RingData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(RingData::getB),
                    Codec.FLOAT.fieldOf("a").forGetter(RingData::getA),
                    Codec.FLOAT.fieldOf("scale").forGetter(RingData::getScale),
                    Codec.INT.fieldOf("duration").forGetter(RingData::getDuration),
                    Codec.BOOL.fieldOf("facesCamera").forGetter(RingData::getFacesCamera),
                    Codec.STRING.fieldOf("behavior").forGetter((ringData) -> ringData.getBehavior().toString())
                    ).apply(codecBuilder, (yaw, pitch, r, g, b, a, scale, duration, facesCamera, behavior) ->
                            new RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.valueOf(behavior)))
            );
        }
    }
}
