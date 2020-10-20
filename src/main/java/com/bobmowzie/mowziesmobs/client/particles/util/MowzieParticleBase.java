package com.bobmowzie.mowziesmobs.client.particles.util;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class MowzieParticleBase extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
    public float airDrag;
    public float red, green, blue, alpha;
    public float prevRed, prevGreen, prevBlue, prevAlpha;
    public boolean faceCamera;
    public float scale, prevScale, particleScale;
    public float yaw, pitch, roll;
    public float prevYaw, prevPitch, prevRoll;
    public boolean emissive;
    public double prevMotionX, prevMotionY, prevMotionZ;

    ParticleComponent[] components;

    ParticleRibbon ribbon;

    protected MowzieParticleBase(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean faceCamera, boolean emissive, float faceCameraAngle, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.red = (float) (r);
        this.green = (float) (g);
        this.blue = (float) (b);
        this.alpha = (float) (a);
        this.scale = (float)scale;
        this.maxAge = (int)duration;
        airDrag = (float)drag;
        this.faceCamera = faceCamera;
        this.yaw = (float) (yaw);
        this.pitch = (float) (pitch);
        this.roll = (float) (roll);
        this.components = components;
        this.emissive = emissive;
        this.particleAngle = faceCameraAngle;
        this.ribbon = null;

        for (ParticleComponent component : components) {
            component.init(this);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRed = this.red;
        this.prevGreen = this.green;
        this.prevBlue = this.blue;
        this.prevAlpha = this.alpha;
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;
        this.prevRoll = this.roll;
        this.prevParticleAngle = this.particleAngle;
        this.prevScale = this.scale;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getBrightnessForRender(float partialTick)
    {
        int i = super.getBrightnessForRender(partialTick);
        if (emissive) {
            int k = i >> 16 & 255;
            return 240 | k << 16;
        }
        else return i;
    }

    @Override
    public void tick() {
        prevRed = red;
        prevGreen = green;
        prevBlue = blue;
        prevAlpha = alpha;
        prevScale = scale;
        prevYaw = yaw;
        prevPitch = pitch;
        prevRoll = roll;
        prevParticleAngle = particleAngle;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        prevMotionX = motionX;
        prevMotionY = motionY;
        prevMotionZ = motionZ;

        for (ParticleComponent component : components) {
            component.preUpdate(this);
        }

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        //this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        this.motionX *= airDrag;
        this.motionY *= airDrag;
        this.motionZ *= airDrag;

        for (ParticleComponent component : components) {
            component.postUpdate(this);
        }

        if (ribbon != null) {
            ribbon.setPosition(posX, posY, posZ);
            ribbon.positions[0] = new Vec3d(posX, posY, posZ);
            ribbon.prevPositions[0] = getPrevPos();
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        particleAlpha = prevAlpha + (alpha - prevAlpha) * partialTicks;
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        particleRed = prevRed + (red - prevRed) * partialTicks;
        particleGreen = prevGreen + (green - prevGreen) * partialTicks;
        particleBlue = prevBlue + (blue - prevBlue) * partialTicks;
        particleScale = prevScale + (scale - prevScale) * partialTicks;

        for (ParticleComponent component : components) {
            component.preRender(this, partialTicks);
        }

        if (!faceCamera) {
            rotationX = 1;
            rotationZ = 1;
            rotationXY = 0;
            rotationXZ = 0;
            rotationYZ = 0;
        }

        float f = 0;//(float)this.particleTextureIndexX / 16.0F;
        float f1 = 0;//f + 0.0624375F;
        float f2 = 0;//(float)this.particleTextureIndexY / 16.0F;
        float f3 = 0;//f2 + 0.0624375F;
        float f4 = 0;//0.1F * this.particleScale;

//        if (this.particleTexture != null)
//        {
//            f = this.particleTexture.getMinU();
//            f1 = this.particleTexture.getMaxU();
//            f2 = this.particleTexture.getMinV();
//            f3 = this.particleTexture.getMaxV();
//        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {new Vec3d((double)(-rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double)(-rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(rotationYZ * f4 - rotationXZ * f4))};

//        if (faceCamera && this.particleAngle != 0.0F)
//        {
//            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
//            float f9 = MathHelper.cos(f8 * 0.5F);
//            float f10 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.x;
//            float f11 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.y;
//            float f12 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.z;
//            Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);
//
//            for (int l = 0; l < 4; ++l)
//            {
//                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double)(2.0F * f9)));
//            }
//        }
        Matrix4d boxTranslate = new Matrix4d();
        Matrix4d boxRotateX = new Matrix4d();
        Matrix4d boxRotateY = new Matrix4d();
        Matrix4d boxRotateZ = new Matrix4d();
        boxTranslate.set(new Vector3d(f5, f6, f7));
        boxRotateX.rotX(prevPitch + (pitch - prevPitch) * partialTicks);
        boxRotateY.rotY(prevYaw + (yaw - prevYaw) * partialTicks);
        boxRotateZ.rotZ(prevRoll + (roll - prevRoll) * partialTicks);

        Point3d[] vertices = new Point3d[] {
                new Point3d(avec3d[0].x, avec3d[0].y,  avec3d[0].z),
                new Point3d(avec3d[1].x, avec3d[1].y,  avec3d[1].z),
                new Point3d(avec3d[2].x,  avec3d[2].y,  avec3d[2].z),
                new Point3d(avec3d[3].x,  avec3d[3].y,  avec3d[3].z)
        };
        for (Point3d vertex: vertices) {
            if (!faceCamera) {
                boxRotateZ.transform(vertex);
                boxRotateY.transform(vertex);
                boxRotateX.transform(vertex);
            }
            boxTranslate.transform(vertex);
        }

        buffer.pos(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ()).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(vertices[1].getX(), vertices[1].getY(), vertices[1].getZ()).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(vertices[2].getX(), vertices[2].getY(), vertices[2].getZ()).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(vertices[3].getX(), vertices[3].getY(), vertices[3].getZ()).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();

        for (ParticleComponent component : components) {
            component.postRender(this, buffer, partialTicks, j, k);
        }
    }

//    @Override
//    public boolean shouldDisableDepth() {
//        return true;
//    }

    public float getAge() {
        return age;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        setPosition(posX, this.posY, this.posZ);
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        setPosition(this.posX, posY, this.posZ);
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        setPosition(this.posX, this.posY, posZ);
    }

    public double getMotionX() {
        return motionX;
    }

    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    public double getMotionY() {
        return motionY;
    }

    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    public double getMotionZ() {
        return motionZ;
    }

    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }

    public float getAngle() {
        return particleAngle;
    }

    public void setAngle(float angle) {
        this.particleAngle = angle;
    }

    public Vec3d getPrevPos() {
        return new Vec3d(prevPosX, prevPosY, prevPosZ);
    }

    public double getPrevPosX() {
        return prevPosX;
    }

    public double getPrevPosY() {
        return prevPosY;
    }

    public double getPrevPosZ() {
        return prevPosZ;
    }

    public World getWorld() {
        return world;
    }

    public static final class ParticleBaseFactory extends ParticleFactory<ParticleBaseFactory, MowzieParticleBase> {
        public ParticleBaseFactory(ResourceLocation texture) {
            super(MowzieParticleBase.class, ParticleTextureStitcher.create(MowzieParticleBase.class, texture));
        }

        @Override
        public MowzieParticleBase createParticle(ImmutableParticleArgs args) {
            return new MowzieParticleBase(args.world, args.x, args.y, args.z, (double) args.data[0], (double) args.data[1], (double) args.data[2], (double) args.data[3], (double) args.data[4], (double) args.data[5], (double) args.data[6], (double) args.data[7], (double) args.data[8], (double) args.data[9], (double) args.data[10], (double) args.data[11], (double) args.data[12], (boolean) args.data[13], (boolean) args.data[14], (float) args.data[15], (ParticleComponent[]) args.data[16]);
        }
    }

    public static void spawnParticle(World world, MMParticle particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, float faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive) {
        particle.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, yaw, pitch, roll, scale, r, g, b, a, drag, duration, faceCamera, emissive, faceCameraAngle, new ParticleComponent[] {}));
    }

    public static void spawnParticle(World world, MMParticle particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, float faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, ParticleComponent[] components) {
        particle.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, yaw, pitch, roll, scale, r, g, b, a, drag, duration, faceCamera, emissive, faceCameraAngle, components));
    }
}
