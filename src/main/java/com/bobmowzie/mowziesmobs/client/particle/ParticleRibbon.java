package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.AABB;
import com.mojang.math.Matrix4f;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector4f;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.Camera;

public class ParticleRibbon extends AdvancedParticleBase {
    public Vec3[] positions;
    public Vec3[] prevPositions;

    public float texPanOffset;

    protected ParticleRibbon(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, rotation, scale, r, g, b, a, drag, duration, emissive, false, components);
        positions = new Vec3[length];
        prevPositions = new Vec3[length];
        if (positions.length >= 1) positions[0] = new Vec3(getPosX(), getPosY(), getPosZ());
        if (prevPositions.length >= 1) prevPositions[0] = getPrevPos();
    }

    @Override
    protected void updatePosition() {
        super.updatePosition();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        alpha = prevAlpha + (alpha - prevAlpha) * partialTicks;
        if (alpha < 0.01) alpha = 0.01f;
        rCol = prevRed + (red - prevRed) * partialTicks;
        gCol = prevGreen + (green - prevGreen) * partialTicks;
        bCol = prevBlue + (blue - prevBlue) * partialTicks;
        particleScale = prevScale + (scale - prevScale) * partialTicks;

        for (ParticleComponent component : components) {
            component.preRender(this, partialTicks);
        }

        int j = this.getLightColor(partialTicks);

        float r =  rCol;
        float g = gCol;
        float b = bCol;
        float a = alpha;
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

        Vec3 offsetDir = new Vec3(0, 0, 0);
        for (int index = 0; index < positions.length - 1; index++) {
            if (positions[index] == null || positions[index + 1] == null) continue;

            r = rCol;
            g = gCol;
            b = bCol;
            scale = particleScale;
            float t = ((float)index + 1) / ((float)positions.length - 1);
            float tPrev = ((float)index) / ((float)positions.length - 1);

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

            Vec3 Vector3d = renderInfo.getPosition();
            Vec3 p1 = prevPositions[index].add(positions[index].subtract(prevPositions[index]).scale(partialTicks)).subtract(Vector3d);
            Vec3 p2 = prevPositions[index + 1].add(positions[index + 1].subtract(prevPositions[index + 1]).scale(partialTicks)).subtract(Vector3d);

            if (index == 0) {
                Vec3 moveDir = p2.subtract(p1).normalize();
                if (rotation instanceof ParticleRotation.FaceCamera) {
                    Vec3 viewVec = new Vec3(renderInfo.getLookVector());
                    offsetDir = moveDir.cross(viewVec).normalize();
                } else {
                    offsetDir = moveDir.cross(new Vec3(0, 1, 0)).normalize();
                }
                offsetDir = offsetDir.scale(prevScale);
            }

            Vec3[] aVector3d2 = new Vec3[] {offsetDir.scale(-1), offsetDir, null, null};
            Vec3 moveDir = p2.subtract(p1).normalize();
            if (rotation instanceof ParticleRotation.FaceCamera) {
                Vec3 viewVec = new Vec3(renderInfo.getLookVector());
                offsetDir = moveDir.cross(viewVec).normalize();
            }
            else {
                offsetDir = moveDir.cross(new Vec3(0, 1, 0)).normalize();
            }
            offsetDir = offsetDir.scale(scale);
            aVector3d2[2] = offsetDir;
            aVector3d2[3] = offsetDir.scale(-1);

            Vector4f[] vertices2 = new Vector4f[] {
                    new Vector4f((float)aVector3d2[0].x, (float)aVector3d2[0].y,  (float)aVector3d2[0].z, 1f),
                    new Vector4f((float)aVector3d2[1].x, (float)aVector3d2[1].y,  (float)aVector3d2[1].z, 1f),
                    new Vector4f((float)aVector3d2[2].x,  (float)aVector3d2[2].y,  (float)aVector3d2[2].z, 1f),
                    new Vector4f((float)aVector3d2[3].x,  (float)aVector3d2[3].y,  (float)aVector3d2[3].z, 1f)
            };
            Matrix4f boxTranslate = Matrix4f.createTranslateMatrix((float)p1.x, (float)p1.y, (float)p1.z);
            vertices2[0].transform(boxTranslate);
            vertices2[1].transform(boxTranslate);
            boxTranslate = Matrix4f.createTranslateMatrix((float)p2.x, (float)p2.y, (float)p2.z);
            vertices2[2].transform(boxTranslate);
            vertices2[3].transform(boxTranslate);

            float halfU = (getU1() - getU0()) / 2 + getU0();
            float f = this.getU0() + texPanOffset;
            float f1 = halfU + texPanOffset;
            float f2 = this.getV0();
            float f3 = this.getV1();

            buffer.vertex(vertices2[0].x(), vertices2[0].y(), vertices2[0].z()).uv(f1, f3).color(prevR, prevG, prevB, prevA).uv2(j).endVertex();
            buffer.vertex(vertices2[1].x(), vertices2[1].y(), vertices2[1].z()).uv(f1, f2).color(prevR, prevG, prevB, prevA).uv2(j).endVertex();
            buffer.vertex(vertices2[2].x(), vertices2[2].y(), vertices2[2].z()).uv(f, f2).color(r, g, b, a).uv2(j).endVertex();
            buffer.vertex(vertices2[3].x(), vertices2[3].y(), vertices2[3].z()).uv(f, f3).color(r, g, b, a).uv2(j).endVertex();

            prevR = r;
            prevG = g;
            prevB = b;
            prevA = a;
        }

        for (ParticleComponent component : components) {
            component.postRender(this, buffer, renderInfo, partialTicks, j);
        }
    }

    @Override
    public AABB getBoundingBox() {
        if (positions == null || positions.length <= 0 || positions[0] == null) return super.getBoundingBox();
        double minX = positions[0].x() - 0.1;
        double minY = positions[0].y() - 0.1;
        double minZ = positions[0].z() - 0.1;
        double maxX = positions[0].x() + 0.1;
        double maxY = positions[0].y() + 0.1;
        double maxZ = positions[0].z() + 0.1;
        for (Vec3 pos : positions) {
            if (pos == null) continue;
            minX = Math.min(minX, pos.x());
            minY = Math.min(minY, pos.y());
            minZ = Math.min(minZ, pos.z());
            maxX = Math.max(maxX, pos.x());
            maxY = Math.max(maxY, pos.y());
            maxZ = Math.max(maxZ, pos.z());
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public float getMinUPublic() {
        return getU0();
    }

    public float getMaxUPublic() {
        return getU1();
    }

    public float getMinVPublic() {
        return getV0();
    }

    public float getMaxVPublic() {
        return getV1();
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Factory implements ParticleProvider<RibbonParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(RibbonParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRibbon particle = new ParticleRibbon(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getLength(), typeIn.getComponents());
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }

    public static void spawnRibbon(Level world, ParticleType<? extends RibbonParticleData> particle, int length, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive) {
        spawnRibbon(world, particle, length, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, scale, r, g, b, a, drag, duration, emissive, new ParticleComponent[]{});
    }

    public static void spawnRibbon(Level world, ParticleType<? extends RibbonParticleData> particle, int length, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, ParticleComponent[] components) {
        ParticleRotation rotation = faceCamera ? new ParticleRotation.FaceCamera((float) 0) : new ParticleRotation.EulerAngles((float)yaw, (float)pitch, (float)roll);
        world.addParticle(new RibbonParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, length, components), x, y, z, motionX, motionY, motionZ);
    }
}