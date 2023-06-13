package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.*;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ParticleDecal extends AdvancedParticleBase {
    protected ParticleDecal(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components);
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

        if (!doRender) return;

        Vec3 Vector3d = renderInfo.getPosition();
        float xInterp = (float)(Mth.lerp(partialTicks, this.xo, this.x) - Vector3d.x());
        float yInterp = (float)(Mth.lerp(partialTicks, this.yo, this.y) - Vector3d.y());
        float zInterp = (float)(Mth.lerp(partialTicks, this.zo, this.z) - Vector3d.z());

        /*Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        if (rotation instanceof ParticleRotation.EulerAngles) {
            ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) rotation;
            float rotX = eulerRot.prevPitch + (eulerRot.pitch - eulerRot.prevPitch) * partialTicks;
            float rotY = eulerRot.prevYaw + (eulerRot.yaw - eulerRot.prevYaw) * partialTicks;
            float rotZ = eulerRot.prevRoll + (eulerRot.roll - eulerRot.prevRoll) * partialTicks;
            Quaternion quatX = new Quaternion(rotX, 0, 0, false);
            Quaternion quatY = new Quaternion(0, rotY, 0, false);
            Quaternion quatZ = new Quaternion(0, 0, rotZ, false);
            quaternion.mul(quatZ);
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        }
        if (rotation instanceof ParticleRotation.OrientVector) {
            ParticleRotation.OrientVector orientRot = (ParticleRotation.OrientVector) rotation;
            double x = orientRot.prevOrientation.x + (orientRot.orientation.x - orientRot.prevOrientation.x) * partialTicks;
            double y = orientRot.prevOrientation.y + (orientRot.orientation.y - orientRot.prevOrientation.y) * partialTicks;
            double z = orientRot.prevOrientation.z + (orientRot.orientation.z - orientRot.prevOrientation.z) * partialTicks;
            float pitch = (float) Math.asin(-y);
            float yaw = (float) (Mth.atan2(x, z));
            Quaternion quatX = new Quaternion(pitch, 0, 0, false);
            Quaternion quatY = new Quaternion(0, yaw, 0, false);
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        }*/

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = particleScale * 0.1f;

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
//            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(xInterp, yInterp, zInterp);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(partialTicks);
//        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
//        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();

//        System.out.println("CORRECT " + avector3f[0].x());

        particleScale = 0.6f;
        float rotation = 0.0f;//(getAge() + partialTicks) * 0.2f;
        Vec3 minCorner = new Vec3(-particleScale, -particleScale, -particleScale).yRot(rotation).add(x, y, z);
        Vec3 maxCorner = new Vec3(particleScale, particleScale, particleScale).yRot(rotation).add(x, y, z);

        for(BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(minCorner), new BlockPos(maxCorner))) {
            renderBlockDecal(buffer, renderInfo, level, blockpos, x, y, z, u0, u1, v0, v1, particleScale, alpha, rotation);
        }

        for (ParticleComponent component : components) {
            component.postRender(this, buffer, renderInfo, partialTicks, lightColor);
        }
    }

    // From https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
    private static Vec2 getLineIntersection(Vec2 p0, Vec2 p1, Vec2 q0, Vec2 q1) {
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p1.x - p0.x;     s1_y = p1.y - p0.y;
        s2_x = q1.x - q0.x;     s2_y = q1.y - q0.y;

        float s, t;
        s = (-s1_y * (p0.x - q0.x) + s1_x * (p0.y - q0.y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (p0.y - q0.y) - s2_y * (p0.x - q0.x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            // Collision detected
            return new Vec2(p0.x + (t * s1_x), p0.y + (t * s1_y));
        }

        return null; // No collision
    }

    private static boolean isPointInSquare(Vec2 point, Vec2 center, float sideLength, float angle) {
        Vec2 pointCentered = point.add(center.scale(-1));
        Vec2 pointRotated = rotateVec2(pointCentered, -angle);
        float e = 0.00001f;
        return pointRotated.x >= -sideLength - e  && pointRotated.x <= sideLength + e &&
                pointRotated.y >= -sideLength - e && pointRotated.y <= sideLength + e;
    }

    private static Vec2 rotateVec2(Vec2 v, float angle) {
        return new Vec2(v.x * (float) Math.cos(angle) - v.y * (float) Math.sin(angle),
                v.x * (float) Math.sin(angle) + v.y * (float) Math.cos(angle));
    }

    private static void renderBlockDecal(VertexConsumer buffer, Camera renderInfo, LevelReader level, BlockPos blockPos, double x, double y, double z, float u0, float u1, float v0, float v1, float scale, float alpha, float rotation) {
        Vec2 center = new Vec2((float) x, (float) z);
        Vec2 decalCorner0 = rotateVec2(new Vec2(-scale, -scale), rotation).add(center);
        Vec2 decalCorner1 = rotateVec2(new Vec2(scale, -scale), rotation).add(center);
        Vec2 decalCorner2 = rotateVec2(new Vec2(scale, scale), rotation).add(center);
        Vec2 decalCorner3 = rotateVec2(new Vec2(-scale, scale), rotation).add(center);
        Vec2 decalCorners[] = new Vec2[] {decalCorner0, decalCorner1, decalCorner2, decalCorner3};
        BlockPos blockpos = blockPos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            if (blockstate.isCollisionShapeFullBlock(level, blockpos)) {
                VoxelShape voxelshape = blockstate.getShape(level, blockPos.below());
                if (!voxelshape.isEmpty()) {
                    float f = alpha;
                    if (f >= 0.0F) {
                        if (f > 1.0F) {
                            f = 1.0F;
                        }

                        AABB aabb = voxelshape.bounds();
                        float d0 = blockPos.getX() + (float) aabb.minX;
                        float d1 = blockPos.getX() + (float) aabb.maxX;
                        float d2 = blockPos.getY() + (float) aabb.minY + 0.015625f;
                        float d3 = blockPos.getZ() + (float) aabb.minZ;
                        float d4 = blockPos.getZ() + (float) aabb.maxZ;
                        Vec2 blockCorner0 = new Vec2(d0, d3);
                        Vec2 blockCorner1 = new Vec2(d1, d3);
                        Vec2 blockCorner2 = new Vec2(d1, d4);
                        Vec2 blockCorner3 = new Vec2(d0, d4);
                        Vec2 blockCorners[] = new Vec2[] {blockCorner0, blockCorner1, blockCorner2, blockCorner3};
                        List<Vec2> corners = new ArrayList<Vec2>();
                        for (int i = 0; i < blockCorners.length; i++) {
                            for (int j = 0; j < decalCorners.length; j++) {
                                Vec2 checkCorner0 = decalCorners[i];
                                Vec2 checkCorner1 = decalCorners[(i + 1) % decalCorners.length];
                                Vec2 checkCorner2 = blockCorners[j];
                                Vec2 checkCorner3 = blockCorners[(j + 1) % blockCorners.length];
                                corners.add(getLineIntersection(checkCorner0, checkCorner1, checkCorner2, checkCorner3));
                            }
                        }
                        corners.add(blockCorner0);
                        corners.add(blockCorner1);
                        corners.add(blockCorner2);
                        corners.add(blockCorner3);
                        corners.add(decalCorner0);
                        corners.add(decalCorner1);
                        corners.add(decalCorner2);
                        corners.add(decalCorner3);
                        corners.removeIf((e) -> e == null || !isPointInSquare(e, center, scale, rotation) || !isPointInSquare(e, new Vec2(blockPos.getX() + 0.5f, blockPos.getZ() + 0.5f), 0.5f, 0f));
                        if (corners.size() != 4) return;

                        float f1 = (float)(d0);
                        float f2 = (float)(d1);
                        float f3 = (float)(d2);
                        float f4 = (float)(d3);
                        float f5 = (float)(d4);
                        float f6 = ((f1 - (float)x) / 2.0F / scale + 0.5F) * (u1 - u0) + u0;
                        float f7 = ((f2 - (float)x) / 2.0F / scale + 0.5F) * (u1 - u0) + u0;
                        float f8 = ((f4 - (float)z) / 2.0F / scale + 0.5F) * (v1 - v0) + v0;
                        float f9 = ((f5 - (float)z) / 2.0F / scale + 0.5F) * (v1 - v0) + v0;

//                        decalVertex(buffer, renderInfo, f, decalCorner3.x, f3, decalCorner3.y, u0, v0);
//                        decalVertex(buffer, renderInfo, f, decalCorner2.x, f3, decalCorner2.y, u0, v1);
//                        decalVertex(buffer, renderInfo, f, decalCorner1.x, f3, decalCorner1.y, u1, v1);
//                        decalVertex(buffer, renderInfo, f, decalCorner0.x, f3, decalCorner0.y, u1, v0);

                        for (int i = 0; i < 4 && i < corners.size(); i++) {
                            Vec2 corner = corners.get(i);
                            Vec2 cornerRelative = rotateVec2(corner.add(center.negated()), -rotation);
                            Vec2 uv = new Vec2((cornerRelative.x / (2.0f * scale) + 0.5f) * (u1 - u0) + u0, (cornerRelative.y / (2.0f * scale) + 0.5f) * (v1 - v0) + v0);
                            decalVertex(buffer, renderInfo, f, corner.x, f3, corner.y, uv.x, uv.y);
                        }
                    }

                }
            }
        }
    }

    private static void decalVertex(VertexConsumer buffer, Camera renderInfo, float alpha, float x, float y, float z, float u, float v) {
        Vec3 Vector3d = renderInfo.getPosition();
//        Vector3d = new Vec3(0, 1, 0);
        buffer.vertex(x - Vector3d.x(), y - Vector3d.y(), z - Vector3d.z()).uv(u, v).color(1.0F, 1.0F, 1.0F, alpha).uv2(15728880).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<AdvancedParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(AdvancedParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleDecal particle = new ParticleDecal(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getCanCollide(), typeIn.getComponents());
            particle.setColor((float) typeIn.getRed(), (float) typeIn.getGreen(), (float) typeIn.getBlue());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }

    public static void spawnDecal(Level world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        world.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), x, y, z, motionX, motionY, motionZ);
    }
}
