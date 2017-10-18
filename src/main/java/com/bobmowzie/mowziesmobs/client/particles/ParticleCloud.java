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

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleCloud extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
	private int whichTex;
	private float red, green, blue;
	private float scale;
	private EnumCloudBehavior behavior;

	public ParticleCloud(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, boolean noisy, double scale, int duration, EnumCloudBehavior behavior) {
		super(world, x, y, z);
		this.scale = (float) scale * 0.5f;
		particleMaxAge = duration;
		whichTex = noisy ? 1 : 0;
		motionX = vx;
		motionY = vy;
		motionZ = vz;
		red = (float) r;
		green = (float) g;
		blue = (float) b;
		this.behavior = behavior;
	}

	@Override
	public int getFXLayer() {
		return 1;
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
		float var = (particleAge + partialTicks) / (float) particleMaxAge;
		particleAlpha = 0.2f * ((float) (1 - Math.exp(5 * (var - 1))));
		if (particleAlpha < 0.01) particleAlpha = 0.01f;
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		if (behavior == EnumCloudBehavior.SHRINK) particleScale = scale * ((1 - 0.7f * var) + 0.3f);
		else if (behavior == EnumCloudBehavior.GROW) particleScale = scale * ((0.7f * var) + 0.3f);
		else particleScale = scale;

		float f = (float) this.particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = (float) this.particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * this.particleScale;

		if (this.particleTexture != null) {
			int row = (int) (whichTex / 2f);
			int column = (int) whichTex % 2;
			float uRange = particleTexture.getMaxU() - particleTexture.getMinU();
			float spriteWidth = uRange / 2f;
			f = particleTexture.getMinU() + (column * spriteWidth);
			f1 = particleTexture.getMinU() + (spriteWidth * (column + 1));
			f2 = particleTexture.getMinV() + (row * spriteWidth);
			f3 = particleTexture.getMinV() + (spriteWidth * (row + 1));
		}

		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[]{new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4))};

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

		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
	}

	@Override
	public void setParticleTextureIndex(int particleTextureIndex) {
		if (this.getFXLayer() != 0) {
		} else {
			this.particleTextureIndexX = particleTextureIndex % 16;
			this.particleTextureIndexY = particleTextureIndex / 16;
		}
	}

	public enum EnumCloudBehavior {
		SHRINK,
		GROW,
		CONSTANT
	}

	public static final class CloudFactory extends ParticleFactory<ParticleCloud.CloudFactory, ParticleCloud> {
		public CloudFactory() {
			super(ParticleCloud.class, ParticleTextureStitcher.create(ParticleCloud.class, new ResourceLocation(MowziesMobs.MODID, "particles/cloud")));
		}

		@Override
		public ParticleCloud createParticle(ImmutableParticleArgs args) {
			if (args.data.length >= 10)
				return new ParticleCloud(args.world, args.x, args.y, args.z, (double) args.data[0], (double) args.data[1], (double) args.data[2], (double) args.data[3], (double) args.data[4], (double) args.data[5], (boolean) args.data[6], (double) args.data[7], (int) args.data[8], (EnumCloudBehavior) args.data[9]);
			return new ParticleCloud(args.world, args.x, args.y, args.z, 0, 0, 0, 1, 1, 1, false, 10, 40, EnumCloudBehavior.CONSTANT);
		}
	}
}
