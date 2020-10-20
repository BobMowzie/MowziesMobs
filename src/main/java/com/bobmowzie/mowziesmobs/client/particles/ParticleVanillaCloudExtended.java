package com.bobmowzie.mowziesmobs.client.particles;

import com.bobmowzie.mowziesmobs.MowziesMobs;
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

public class ParticleVanillaCloudExtended extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
    private float oSize;
    private float airDrag;
    private float red, green, blue;

    private Vec3d[] destination;

    protected ParticleVanillaCloudExtended(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration) {
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
//        this.particleScale *= 0.75F;
//        this.particleScale *= 2.5F;
//        this.oSize = this.particleScale * (float)scale;
        this.maxAge = (int)duration;
        airDrag = (float)drag;
        destination = null;
    }

    protected ParticleVanillaCloudExtended(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3d[] destination) {
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
//        this.particleScale *= 0.75F;
//        this.particleScale *= 2.5F;
//        this.oSize = this.particleScale * (float)scale;
        this.maxAge = (int)duration;
        airDrag = (float)drag;
        this.destination = destination;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f0 = ((float)this.age + partialTicks) / (float)this.maxAge * 32.0F;
        f0 = MathHelper.clamp(f0, 0.0F, 1.0F);
//        this.particleScale = this.oSize * f0;

        particleRed = red;
        particleGreen = green;
        particleBlue = blue;

        float f = 0;//(float)this.particleTextureIndexX / 16.0F;
        float f1 = 0;//f + 0.0624375F;
        float f2 = 0;//(float)this.particleTextureIndexY / 16.0F;
        float f3 = 0;//f2 + 0.0624375F;
        float f4 = 0;//0.1F * this.particleScale;

//        if (this.particleTexture != null)
//        {
//            float uDist = this.particleTexture.getMaxU() - this.particleTexture.getMinU();
//            float vDist = this.particleTexture.getMaxV() - this.particleTexture.getMinV();
//            f = this.particleTexture.getMinU() + particleTextureIndexX * uDist / 4f;
//            f1 = this.particleTexture.getMinU() + (particleTextureIndexX + 1) * uDist / 4f;
//            f2 = this.particleTexture.getMinV() + particleTextureIndexY * vDist / 4f;
//            f3 = this.particleTexture.getMinV() + (particleTextureIndexY + 1) * vDist / 4f;
//        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {new Vec3d((double)(-rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double)(-rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(rotationYZ * f4 - rotationXZ * f4))};

//        if (this.particleAngle != 0.0F)
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

        buffer.pos((double)f5 + avec3d[0].x, (double)f6 + avec3d[0].y, (double)f7 + avec3d[0].z).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + avec3d[1].x, (double)f6 + avec3d[1].y, (double)f7 + avec3d[1].z).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + avec3d[2].x, (double)f6 + avec3d[2].y, (double)f7 + avec3d[2].z).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + avec3d[3].x, (double)f6 + avec3d[3].y, (double)f7 + avec3d[3].z).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

//        int particleTextureIndex = (int) (8 * (1f - (float)this.age / (float)this.maxAge));
//        this.particleTextureIndexY = (int) ((float)particleTextureIndex / 4f);
//        this.particleTextureIndexX = particleTextureIndex % 4;
//
//        if (destination != null && destination.length == 1) {
//            particleTextureIndex = (int) (8 * ((float)this.age / (float)this.maxAge));
//            this.particleTextureIndexY = (int) ((float)particleTextureIndex / 4f);
//            this.particleTextureIndexX = particleTextureIndex % 4;
//
//            Vec3d destinationVec = destination[0];
//            Vec3d diff = destinationVec.subtract(new Vec3d(posX, posY, posZ));
//            if (diff.length() < 0.5) this.setExpired();
//            float attractScale = 0.7f * ((float)this.age / (float)this.maxAge) * ((float)this.age / (float)this.maxAge);
//            motionX = diff.x * attractScale;
//            motionY = diff.y * attractScale;
//            motionZ = diff.z * attractScale;
//        }

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

    public static final class CloudFactory extends ParticleFactory<ParticleVanillaCloudExtended.CloudFactory, ParticleVanillaCloudExtended> {
        public CloudFactory() {
            super(ParticleVanillaCloudExtended.class, ParticleTextureStitcher.create(ParticleVanillaCloudExtended.class, new ResourceLocation(MowziesMobs.MODID, "particles/cloud_vanilla")));
        }

        @Override
        public ParticleVanillaCloudExtended createParticle(ImmutableParticleArgs args) {
            if (args.data.length == 9) return new ParticleVanillaCloudExtended(args.world, args.x, args.y, args.z, (double) args.data[0], (double) args.data[1], (double) args.data[2], (double) args.data[3], (double) args.data[4], (double) args.data[5], (double) args.data[6], (double) args.data[7], (double) args.data[8]);
            else return new ParticleVanillaCloudExtended(args.world, args.x, args.y, args.z, (double) args.data[0], (double) args.data[1], (double) args.data[2], (double) args.data[3], (double) args.data[4], (double) args.data[5], (double) args.data[6], (double) args.data[7], (double) args.data[8], (Vec3d[]) args.data[9]);
        }
    }

    public static void spawnVanillaCloud(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration) {
        MMParticle.VANILLA_CLOUD.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, scale, r, g, b, drag, duration));
    }

    public static void spawnVanillaCloudDestination(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3d[] destination) {
        MMParticle.VANILLA_CLOUD.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, scale, r, g, b, drag, duration, destination));
    }
}
