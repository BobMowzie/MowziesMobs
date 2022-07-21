package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleSparkle extends TextureSheetParticle {
    private final float red;
    private final float green;
    private final float blue;
    private final float scale;

    public ParticleSparkle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        this.scale = (float) scale * 1f;
        lifetime = duration;
        xd = vx;
        yd = vy;
        zd = vz;
        red = (float) r;
        green = (float) g;
        blue = (float) b;
        hasPhysics = false;
    }

    @Override
    protected float getU1() {
        return super.getU1() - (super.getU1() - super.getU0())/16f;
    }

    @Override
    protected float getV1() {
        return super.getV1() - (super.getV1() - super.getV0())/16f;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float a = ((float)age + partialTicks)/lifetime;
        alpha = -4 * a * a + 4 * a;
        quadSize = (-4 * a * a + 4 * a) * scale;

        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class SparkleFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public SparkleFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSparkle particle = new ParticleSparkle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1, 1, 1, 0.4d, 13);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
