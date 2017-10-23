package com.bobmowzie.mowziesmobs.client.particles;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleRing extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
	public float r, g, b;
	public float opacity;
	public boolean facesCamera;
	public float yaw, pitch;
	public float size;

	public ParticleRing(World world, double x, double y, double z, float yaw, float pitch, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera, float motionX, float motionY, float motionZ) {
		super(world, x, y, z);
		particleScale = 1;
		this.size = size;
		particleMaxAge = duration;
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
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public int getBrightnessForRender(float delta) {
		return 240 | super.getBrightnessForRender(delta) & 0xFF0000;
	}

	@Override
	public boolean shouldDisableDepth() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (particleAge >= particleMaxAge) {
			setExpired();
		}
		particleAge++;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		particleScale = size * (particleAge + partialTicks) / particleMaxAge;
		particleAlpha = opacity * 0.9f * (1 - (particleAge + partialTicks) / particleMaxAge) + 0.1f;
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

		float f = (float) this.particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = (float) this.particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * this.particleScale;

		if (this.particleTexture != null) {
			f = this.particleTexture.getMinU();
			f1 = this.particleTexture.getMaxU();
			f2 = this.particleTexture.getMinV();
			f3 = this.particleTexture.getMaxV();
		}

		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[]{
				new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)),
				new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4))
		};

		if (this.particleAngle != 0.0F) {
			float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

			for (int l = 0; l < 4; ++l) {
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
			}
		}
		;
		Matrix4d boxTranslate = new Matrix4d();
		Matrix4d boxRotateX = new Matrix4d();
		Matrix4d boxRotateY = new Matrix4d();
		boxTranslate.set(new Vector3d(f5, f6, f7));
		boxRotateX.rotX(pitch);
		boxRotateY.rotY(yaw);

		Point3d[] vertices = new Point3d[]{
				new Point3d(avec3d[0].x, avec3d[0].y, avec3d[0].z),
				new Point3d(avec3d[1].x, avec3d[1].y, avec3d[1].z),
				new Point3d(avec3d[2].x, avec3d[2].y, avec3d[2].z),
				new Point3d(avec3d[3].x, avec3d[3].y, avec3d[3].z)
		};
		for (Point3d vertex : vertices) {
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
	}

	public static final class RingFactory extends ParticleFactory<ParticleRing.RingFactory, ParticleRing> {
		public RingFactory() {
			super(ParticleRing.class, ParticleTextureStitcher.create(ParticleRing.class, new ResourceLocation(MowziesMobs.MODID, "particles/ring")));
		}

		@Override
		public ParticleRing createParticle(ImmutableParticleArgs args) {
			if (args.data.length >= 12) {
				return new ParticleRing(args.world, args.x, args.y, args.z, (float) args.data[0], (float) args.data[1], (int) args.data[2], (float) args.data[3], (float) args.data[4], (float) args.data[5], (float) args.data[6], (float) args.data[7], (boolean) args.data[8], (float) args.data[9], (float) args.data[10], (float) args.data[11]);
			} else
				return new ParticleRing(args.world, args.x, args.y, args.z, 0, 0, 25, 0.8f, 0.8f, 1f, 1f, 5, true, 0, 0, 0);
		}
	}
}
