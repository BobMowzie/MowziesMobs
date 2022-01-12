package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.particles.ParticleType;
import net.minecraft.resources.math.AxisAlignedBB;
import net.minecraft.resources.math.MathHelper;
import net.minecraft.world.phys.Matrix4f;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vector4f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

        int j = this.getBrightnessForRender(partialTicks);

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

        Vec3 offsetDir = new Vec3(0, 0, 0);
        for (int index = 0; index < positions.length - 1; index++) {
            if (positions[index] == null || positions[index + 1] == null) continue;

            r = particleRed;
            g = particleGreen;
            b = particleBlue;
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

            Vec3 Vec3 = renderInfo.getProjectedView();
            Vec3 p1 = prevPositions[index].add(positions[index].subtract(prevPositions[index]).scale(partialTicks)).subtract(Vec3);
            Vec3 p2 = prevPositions[index + 1].add(positions[index + 1].subtract(prevPositions[index + 1]).scale(partialTicks)).subtract(Vec3);

            if (index == 0) {
                Vec3 moveDir = p2.subtract(p1).normalize();
                if (rotation instanceof ParticleRotation.FaceCamera) {
                    Vec3 viewVec = new Vec3(renderInfo.getViewVector());
                    offsetDir = moveDir.crossProduct(viewVec).normalize();
                } else {
                    offsetDir = moveDir.crossProduct(new Vec3(0, 1, 0)).normalize();
                }
                offsetDir = offsetDir.scale(prevScale);
            }

            Vec3[] aVec32 = new Vec3[] {offsetDir.scale(-1), offsetDir, null, null};
            Vec3 moveDir = p2.subtract(p1).normalize();
            if (rotation instanceof ParticleRotation.FaceCamera) {
                Vec3 viewVec = new Vec3(renderInfo.getViewVector());
                offsetDir = moveDir.crossProduct(viewVec).normalize();
            }
            else {
                offsetDir = moveDir.crossProduct(new Vec3(0, 1, 0)).normalize();
            }
            offsetDir = offsetDir.scale(scale);
            aVec32[2] = offsetDir;
            aVec32[3] = offsetDir.scale(-1);

            Vector4f[] vertices2 = new Vector4f[] {
                    new Vector4f((float)aVec32[0].x, (float)aVec32[0].y,  (float)aVec32[0].z, 1f),
                    new Vector4f((float)aVec32[1].x, (float)aVec32[1].y,  (float)aVec32[1].z, 1f),
                    new Vector4f((float)aVec32[2].x,  (float)aVec32[2].y,  (float)aVec32[2].z, 1f),
                    new Vector4f((float)aVec32[3].x,  (float)aVec32[3].y,  (float)aVec32[3].z, 1f)
            };
            Matrix4f boxTranslate = Matrix4f.makeTranslate((float)p1.x, (float)p1.y, (float)p1.z);
            vertices2[0].transform(boxTranslate);
            vertices2[1].transform(boxTranslate);
            boxTranslate = Matrix4f.makeTranslate((float)p2.x, (float)p2.y, (float)p2.z);
            vertices2[2].transform(boxTranslate);
            vertices2[3].transform(boxTranslate);

            float halfU = (getMaxU() - getMinU()) / 2 + getMinU();
            float f = this.getMinU() + texPanOffset;
            float f1 = halfU + texPanOffset;
            float f2 = this.getMinV();
            float f3 = this.getMaxV();

            buffer.pos(vertices2[0].getX(), vertices2[0].getY(), vertices2[0].getZ()).tex(f1, f3).color(prevR, prevG, prevB, prevA).lightmap(j).endVertex();
            buffer.pos(vertices2[1].getX(), vertices2[1].getY(), vertices2[1].getZ()).tex(f1, f2).color(prevR, prevG, prevB, prevA).lightmap(j).endVertex();
            buffer.pos(vertices2[2].getX(), vertices2[2].getY(), vertices2[2].getZ()).tex(f, f2).color(r, g, b, a).lightmap(j).endVertex();
            buffer.pos(vertices2[3].getX(), vertices2[3].getY(), vertices2[3].getZ()).tex(f, f3).color(r, g, b, a).lightmap(j).endVertex();

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
    public AxisAlignedBB getBoundingBox() {
        if (positions == null || positions.length <= 0 || positions[0] == null) return super.getBoundingBox();
        double minX = positions[0].getX() - 0.1;
        double minY = positions[0].getY() - 0.1;
        double minZ = positions[0].getZ() - 0.1;
        double maxX = positions[0].getX() + 0.1;
        double maxY = positions[0].getY() + 0.1;
        double maxZ = positions[0].getZ() + 0.1;
        for (Vec3 pos : positions) {
            if (pos == null) continue;
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public float getMinUPublic() {
        return getMinU();
    }

    public float getMaxUPublic() {
        return getMaxU();
    }

    public float getMinVPublic() {
        return getMinV();
    }

    public float getMaxVPublic() {
        return getMaxV();
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Factory implements IParticleFactory<RibbonParticleData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(RibbonParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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