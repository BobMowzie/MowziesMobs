package com.bobmowzie.mowziesmobs.client.particle.util;

import com.bobmowzie.mowziesmobs.client.particle.ParticleRibbon;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AdvancedParticleBase extends SpriteTexturedParticle {
    public float airDrag;
    public float red, green, blue, alpha;
    public float prevRed, prevGreen, prevBlue, prevAlpha;
    public float scale, prevScale, particleScale;
    public ParticleRotation rotation;
    public boolean emissive;
    public double prevMotionX, prevMotionY, prevMotionZ;

    public ParticleComponent[] components;

    public ParticleRibbon ribbon;

    protected AdvancedParticleBase(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
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
        this.rotation = rotation;
        this.components = components;
        this.emissive = emissive;
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
        this.rotation.setPrevValues();
        this.prevScale = this.scale;
        this.canCollide = canCollide;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
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
        rotation.setPrevValues();
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
            ribbon.positions[0] = new Vector3d(posX, posY, posZ);
            ribbon.prevPositions[0] = getPrevPos();
        }
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        particleAlpha = prevAlpha + (alpha - prevAlpha) * partialTicks;
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        particleRed = prevRed + (red - prevRed) * partialTicks;
        particleGreen = prevGreen + (green - prevGreen) * partialTicks;
        particleBlue = prevBlue + (blue - prevBlue) * partialTicks;
        particleScale = prevScale + (scale - prevScale) * partialTicks;

        for (ParticleComponent component : components) {
            component.preRender(this, partialTicks);
        }

        Vector3d Vector3d = renderInfo.getProjectedView();
        float f = (float)(MathHelper.lerp(partialTicks, this.prevPosX, this.posX) - Vector3d.getX());
        float f1 = (float)(MathHelper.lerp(partialTicks, this.prevPosY, this.posY) - Vector3d.getY());
        float f2 = (float)(MathHelper.lerp(partialTicks, this.prevPosZ, this.posZ) - Vector3d.getZ());

        Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        if (rotation instanceof ParticleRotation.FaceCamera) {
            ParticleRotation.FaceCamera faceCameraRot = (ParticleRotation.FaceCamera) rotation;
            if (faceCameraRot.faceCameraAngle == 0.0F && faceCameraRot.prevFaceCameraAngle == 0.0F) {
                quaternion = renderInfo.getRotation();
            } else {
                quaternion = new Quaternion(renderInfo.getRotation());
                float f3 = MathHelper.lerp(partialTicks, faceCameraRot.prevFaceCameraAngle, faceCameraRot.faceCameraAngle);
                quaternion.multiply(Vector3f.ZP.rotation(f3));
            }
        }
        else if (rotation instanceof ParticleRotation.EulerAngles) {
            ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) rotation;
            float rotX = eulerRot.prevPitch + (eulerRot.pitch - eulerRot.prevPitch) * partialTicks;
            float rotY = eulerRot.prevYaw + (eulerRot.yaw - eulerRot.prevYaw) * partialTicks;
            float rotZ = eulerRot.prevRoll + (eulerRot.roll - eulerRot.prevRoll) * partialTicks;
            Quaternion quatX = new Quaternion(rotX, 0, 0, false);
            Quaternion quatY = new Quaternion(0, rotY, 0, false);
            Quaternion quatZ = new Quaternion(0, 0, rotZ, false);
            quaternion.multiply(quatZ);
            quaternion.multiply(quatY);
            quaternion.multiply(quatX);
        }
        if (rotation instanceof ParticleRotation.OrientVector) {
            ParticleRotation.OrientVector orientRot = (ParticleRotation.OrientVector) rotation;
            double x = orientRot.prevOrientation.x + (orientRot.orientation.x - orientRot.prevOrientation.x) * partialTicks;
            double y = orientRot.prevOrientation.y + (orientRot.orientation.y - orientRot.prevOrientation.y) * partialTicks;
            double z = orientRot.prevOrientation.z + (orientRot.orientation.z - orientRot.prevOrientation.z) * partialTicks;
            float pitch = (float) Math.asin(-y);
            float yaw = (float) (MathHelper.atan2(x, z));
            Quaternion quatX = new Quaternion(pitch, 0, 0, false);
            Quaternion quatY = new Quaternion(0, yaw, 0, false);
            quaternion.multiply(quatY);
            quaternion.multiply(quatX);
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = particleScale * 0.1f;

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
        buffer.pos(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();

        for (ParticleComponent component : components) {
            component.postRender(this, buffer, renderInfo, partialTicks, j);
        }
    }

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

    public Vector3d getPrevPos() {
        return new Vector3d(prevPosX, prevPosY, prevPosZ);
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

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<AdvancedParticleData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(AdvancedParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AdvancedParticleBase particle = new AdvancedParticleBase(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getCanCollide(), typeIn.getComponents());
            particle.setColor((float) typeIn.getRed(), (float) typeIn.getGreen(), (float) typeIn.getBlue());
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }

    public static void spawnParticle(World world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide) {
        spawnParticle(world, particle, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, r, g, b, a, drag, duration, emissive, canCollide, new ParticleComponent[]{});
    }

    public static void spawnParticle(World world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        ParticleRotation rotation = faceCamera ? new ParticleRotation.FaceCamera((float) faceCameraAngle) : new ParticleRotation.EulerAngles((float)yaw, (float)pitch, (float)roll);
        world.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnParticle(World world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        world.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), x, y, z, motionX, motionY, motionZ);
    }
}
