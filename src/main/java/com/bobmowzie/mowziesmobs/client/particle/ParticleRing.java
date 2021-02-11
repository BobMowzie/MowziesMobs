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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Locale;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleRing extends SpriteTexturedParticle {
    public float r, g, b;
    public float opacity;
    public boolean facesCamera;
    public float yaw, pitch;
    public float size;

    private EnumRingBehavior behavior;

    public enum EnumRingBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    public ParticleRing(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera, EnumRingBehavior behavior) {
        super(world, x, y, z);
        setSize(1, 1);
        this.size = size;
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
        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    /*@Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
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

        if (!facesCamera) {
            rotationX = 1;
            rotationZ = 1;
            rotationXY = 0;
            rotationXZ = 0;
            rotationYZ = 0;
        }

        float f = this.getMinU();
        float f1 = this.getMaxU();
        float f2 = this.getMinV();
        float f3 = this.getMaxV();
        float f4 = 0.1F * this.particleScale;

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {
                new Vec3d((double)(-rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(-rotationYZ * f4 - rotationXZ * f4)),
                new Vec3d((double)(-rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(-rotationYZ * f4 + rotationXZ * f4)),
                new Vec3d((double)(rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(rotationYZ * f4 + rotationXZ * f4)),
                new Vec3d((double)(rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(rotationYZ * f4 - rotationXZ * f4))
        };

        if (this.particleAngle != 0.0F)
        {
            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float)entityIn.getLookDirection().x;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float)entityIn.getLookDirection().y;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float)entityIn.getLookDirection().z;
            Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

            for (int l = 0; l < 4; ++l)
            {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double)(2.0F * f9)));
            }
        };
        Matrix4d boxTranslate = new Matrix4d();
        Matrix4d boxRotateX = new Matrix4d();
        Matrix4d boxRotateY = new Matrix4d();
        boxTranslate.set(new Vector3d(f5, f6, f7));
        boxRotateX.rotX(pitch);
        boxRotateY.rotY(yaw);

        Point3d[] vertices = new Point3d[] {
                new Point3d(avec3d[0].x, avec3d[0].y,  avec3d[0].z),
                new Point3d(avec3d[1].x, avec3d[1].y,  avec3d[1].z),
                new Point3d(avec3d[2].x,  avec3d[2].y,  avec3d[2].z),
                new Point3d(avec3d[3].x,  avec3d[3].y,  avec3d[3].z)
        };
        for (Point3d vertex: vertices) {
            if (!facesCamera) {
                boxRotateX.transform(vertex);
                boxRotateY.transform(vertex);
            }
            boxTranslate.transform(vertex);
        }

        buffer.pos(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ()).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(vertices[1].getX(), vertices[1].getY(), vertices[1].getZ()).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(vertices[2].getX(), vertices[2].getY(), vertices[2].getZ()).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(vertices[3].getX(), vertices[3].getY(), vertices[3].getZ()).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }*/ // TODO

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class RingFactory implements IParticleFactory<RingData> {
        private final IAnimatedSprite spriteSet;

        public RingFactory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(RingData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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

            public ParticleRing.RingData read(ParticleType<ParticleRing.RingData> particleTypeIn, PacketBuffer buffer) {
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
        public void write(PacketBuffer buffer) {
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
    }
}
