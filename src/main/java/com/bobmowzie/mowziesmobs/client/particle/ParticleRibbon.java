package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class ParticleRibbon extends AdvancedParticleBase {
    public Vec3d[] positions;
    public Vec3d[] prevPositions;

    protected ParticleRibbon(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, rotation, scale, r, g, b, a, drag, duration, emissive, components);
        positions = new Vec3d[length];
        prevPositions = new Vec3d[length];
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

        float f = this.getMinU();
        float f1 = this.getMaxU();
        float f2 = this.getMinV();
        float f3 = this.getMaxV();

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
                if (rotation instanceof ParticleRotation.FaceCamera) {
                    offsetDir = moveDir.crossProduct(entityIn.getLookDirection()).normalize();
                } else {
                    offsetDir = moveDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
                }
                offsetDir = offsetDir.scale(prevScale);
            }

            Vec3d[] avec3d2 = new Vec3d[] {offsetDir.scale(-1), offsetDir, null, null};
            Vec3d moveDir = p2.subtract(p1).normalize();
            if (rotation instanceof ParticleRotation.FaceCamera) {
                offsetDir = moveDir.crossProduct(entityIn.getLookDirection()).normalize();
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

    @OnlyIn(Dist.CLIENT)
    public static final class Factory implements IParticleFactory<RibbonParticleData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(RibbonParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRibbon particle = new ParticleRibbon(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getLength(), typeIn.getComponents());
            particle.selectSpriteWithAge(spriteSet);
            return particle;
        }
    }

    public static void spawnRibbon(World world, ParticleType<? extends RibbonParticleData> particle, int length, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive) {
        spawnRibbon(world, particle, length, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, scale, r, g, b, a, drag, duration, emissive, new ParticleComponent[]{});
    }

    public static void spawnRibbon(World world, ParticleType<? extends RibbonParticleData> particle, int length, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, ParticleComponent[] components) {
        ParticleRotation rotation = faceCamera ? new ParticleRotation.FaceCamera((float) 0) : new ParticleRotation.EulerAngles((float)yaw, (float)pitch, (float)roll);
        world.addParticle(new RibbonParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, length, components), x, y, z, motionX, motionY, motionZ);
    }
}