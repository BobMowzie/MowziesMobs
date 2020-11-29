package com.bobmowzie.mowziesmobs.client.particles.util;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.client.particles.util.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class ParticleRibbon extends AdvancedParticleBase {
    public Vec3d[] positions;
    public Vec3d[] prevPositions;

    protected ParticleRibbon(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean faceCamera, boolean emissive, int length, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, yaw, pitch, roll, scale, r, g, b, a, drag, duration, faceCamera, emissive, 0, components);
        positions = new Vec3d[length];
        prevPositions = new Vec3d[length];
        Vec3d pos = new Vec3d(getPosX(), getPosY(), getPosZ());
        positions[0] = new Vec3d(getPosX(), getPosY(), getPosZ());
        prevPositions[0] = getPrevPos();
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = positions.length - 1; i > 0; i--) {
            positions[i] = positions[i - 1];
            prevPositions[i] = prevPositions[i - 1];
        }
        positions[0] = new Vec3d(getPosX(), getPosY(), getPosZ());
        prevPositions[0] = getPrevPos();
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

        float f = 0;//(float)this.particleTextureIndexX / 16.0F;
        float f1 = 0;//f + 0.0624375F;
        float f2 = 0;//(float)this.particleTextureIndexY / 16.0F;
        float f3 = 0;//f2 + 0.0624375F;
//
//        if (this.particleTexture != null)
//        {
//            f = this.particleTexture.getMinU();
//            f1 = this.particleTexture.getMaxU();
//            f2 = this.particleTexture.getMinV();
//            f3 = this.particleTexture.getMaxV();
//        }

        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;

        float r =  particleRed;
        float g = particleGreen;
        float b = particleBlue;
        float a = particleAlpha;
        float scale = particleScale;
        float prevR = r;
        float prevG = g;
        float prevB = b;
        float prevA = a;
        float prevScale = scale;

        for (ParticleComponent component : components) {
            if (component instanceof PropertyOverLength) {
                PropertyOverLength pOverLength = (PropertyOverLength) component;
                float value = pOverLength.evaluate(0);
                if (pOverLength.getProperty() == EnumRibbonProperty.SCALE) {
                    prevScale *= value;
                }
                else if (pOverLength.getProperty() == EnumRibbonProperty.RED) {
                    prevR *= value;
                }
                else if (pOverLength.getProperty() == EnumRibbonProperty.GREEN) {
                    prevG *= value;
                }
                else if (pOverLength.getProperty() == EnumRibbonProperty.BLUE) {
                    prevB *= value;
                }
                else if (pOverLength.getProperty() == EnumRibbonProperty.ALPHA) {
                    prevA *= value;
                }
            }
        }

        Vec3d offsetDir = new Vec3d(0, 0, 0);
        for (int index = 0; index < positions.length - 1; index++) {
            if (positions[index] == null || positions[index + 1] == null) continue;

            r = particleRed;
            g = particleGreen;
            b = particleBlue;
            scale = particleScale;
            float t = ((float)index + 1) / ((float)positions.length - 1);

            for (ParticleComponent component : components) {
                if (component instanceof PropertyOverLength) {
                    PropertyOverLength pOverLength = (PropertyOverLength) component;
                    float value = pOverLength.evaluate(t);
                    if (pOverLength.getProperty() == EnumRibbonProperty.SCALE) {
                        scale *= value;
                    }
                    else if (pOverLength.getProperty() == EnumRibbonProperty.RED) {
                        r *= value;
                    }
                    else if (pOverLength.getProperty() == EnumRibbonProperty.GREEN) {
                        g *= value;
                    }
                    else if (pOverLength.getProperty() == EnumRibbonProperty.BLUE) {
                        b *= value;
                    }
                    else if (pOverLength.getProperty() == EnumRibbonProperty.ALPHA) {
                        a *= value;
                    }
                }
            }

            Vec3d interpPos = new Vec3d(interpPosX, interpPosY, interpPosZ);
            Vec3d p1 = prevPositions[index].add(positions[index].subtract(prevPositions[index]).scale(partialTicks)).subtract(interpPos);
            Vec3d p2 = prevPositions[index + 1].add(positions[index + 1].subtract(prevPositions[index + 1]).scale(partialTicks)).subtract(interpPos);

            if (index == 0) {
                Vec3d moveDir = p2.subtract(p1).normalize();
                if (faceCamera) {
//                    offsetDir = moveDir.crossProduct(cameraViewDir).normalize();
                } else {
                    offsetDir = moveDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
                }
                offsetDir = offsetDir.scale(prevScale);
            }

            Vec3d[] avec3d2 = new Vec3d[] {offsetDir.scale(-1), offsetDir, null, null};
            Vec3d moveDir = p2.subtract(p1).normalize();
            if (faceCamera) {
//                offsetDir = moveDir.crossProduct(cameraViewDir).normalize();
            }
            else {
                offsetDir = moveDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
            }
            offsetDir = offsetDir.scale(scale);
            avec3d2[2] = offsetDir;
            avec3d2[3] = offsetDir.scale(-1);

            Point3d[] vertices2 = new Point3d[] {
                    new Point3d(avec3d2[0].x, avec3d2[0].y,  avec3d2[0].z),
                    new Point3d(avec3d2[1].x, avec3d2[1].y,  avec3d2[1].z),
                    new Point3d(avec3d2[2].x,  avec3d2[2].y,  avec3d2[2].z),
                    new Point3d(avec3d2[3].x,  avec3d2[3].y,  avec3d2[3].z)
            };
            Matrix4d boxTranslate = new Matrix4d();
            boxTranslate.set(new Vector3d(p1.x, p1.y, p1.z));
            boxTranslate.transform(vertices2[0]);
            boxTranslate.transform(vertices2[1]);
            boxTranslate.set(new Vector3d(p2.x, p2.y, p2.z));
            boxTranslate.transform(vertices2[2]);
            boxTranslate.transform(vertices2[3]);

            buffer.pos(vertices2[0].getX(), vertices2[0].getY(), vertices2[0].getZ()).tex((double) f1, (double) f3).color(prevR, prevG, prevB, prevA).lightmap(j, k).endVertex();
            buffer.pos(vertices2[1].getX(), vertices2[1].getY(), vertices2[1].getZ()).tex((double) f1, (double) f2).color(prevR, prevG, prevB, prevA).lightmap(j, k).endVertex();
            buffer.pos(vertices2[2].getX(), vertices2[2].getY(), vertices2[2].getZ()).tex((double) f, (double) f2).color(r, g, b, a).lightmap(j, k).endVertex();
            buffer.pos(vertices2[3].getX(), vertices2[3].getY(), vertices2[3].getZ()).tex((double) f, (double) f3).color(r, g, b, a).lightmap(j, k).endVertex();

            prevR = r;
            prevG = g;
            prevB = b;
            prevA = a;
        }

        for (ParticleComponent component : components) {
            component.postRender(this, buffer, partialTicks, j, k);
        }
    }

    public static final class ParticleRibbonFactory extends ParticleFactory<ParticleRibbon.ParticleRibbonFactory, ParticleRibbon> {
        public ParticleRibbonFactory(ResourceLocation texture) {
            super(null);
        }

        @Override
        public ParticleRibbon createParticle(ImmutableParticleArgs args) {
            return new ParticleRibbon(args.world, args.x, args.y, args.z, (double) args.data[0], (double) args.data[1], (double) args.data[2], (double) args.data[3], (double) args.data[4], (double) args.data[5], (double) args.data[6], (double) args.data[7], (double) args.data[8], (double) args.data[9], (double) args.data[10], (double) args.data[11], (double) args.data[12], (boolean) args.data[13], (boolean) args.data[14], (int) args.data[15], (ParticleComponent[]) args.data[16]);
        }
    }

    public static void spawnRibbon(World world, MMParticle particle, int length, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive) {
        particle.spawn(world, x, y, z, ParticleRibbonFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, yaw, pitch, roll, scale, r, g, b, a, drag, duration, faceCamera, emissive, length, new ParticleComponent[] {}));
    }

    public static void spawnRibbon(World world, MMParticle particle, int length, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, ParticleComponent[] components) {
        particle.spawn(world, x, y, z, ParticleRibbonFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, yaw, pitch, roll, scale, r, g, b, a, drag, duration, faceCamera, emissive, length, components));
    }
}