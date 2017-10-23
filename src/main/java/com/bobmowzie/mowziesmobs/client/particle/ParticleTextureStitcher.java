package com.bobmowzie.mowziesmobs.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ParticleTextureStitcher<T> {
	private final ResourceLocation[] textures;
	private TextureAtlasSprite[] loadedSprites;

	private ParticleTextureStitcher(ResourceLocation[] textures) {
		this.textures = textures;
	}

	/**
	 * Creates a new particle texture stitcher for the specified type and
	 * textures. The particle class must implement
	 * {@link IParticleSpriteReceiver}
	 *
	 * @param cls
	 * @param textures
	 * @return
	 */
	public static <T extends Particle & IParticleSpriteReceiver> ParticleTextureStitcher create(Class<T> cls, ResourceLocation[] textures) {
		return new ParticleTextureStitcher<T>(textures);
	}

	/**
	 * Creates a new particle texture stitcher for the specified type and
	 * texture. The particle class must implement
	 * {@link IParticleSpriteReceiver}
	 *
	 * @param cls
	 * @param texture
	 * @return
	 */
	public static <T extends Particle & IParticleSpriteReceiver> ParticleTextureStitcher create(Class<T> cls, ResourceLocation texture) {
		return create(cls, new ResourceLocation[]{texture});
	}

	/**
	 * Returns the particle textures
	 *
	 * @return
	 */
	public ResourceLocation[] getTextures() {
		return this.textures;
	}

	/**
	 * Sets the particle sprites
	 *
	 * @param sprites
	 */
	public void setSprites(TextureAtlasSprite[] sprites) {
		this.loadedSprites = sprites;
	}

	/**
	 * Returns the particle sprites
	 *
	 * @return
	 */
	public TextureAtlasSprite[] getSprites() {
		return this.loadedSprites;
	}

	public enum Stitcher {
		INSTANCE;

		@SubscribeEvent
		public void onTextureStitch(TextureStitchEvent.Pre event) {
			TextureMap map = event.getMap();
			MMParticle[] particles = MMParticle.values();
			for (MMParticle particle : particles) {
				ParticleTextureStitcher stitcher = particle.getFactory().getStitcher();
				if (stitcher != null) {
					ResourceLocation[] textures = stitcher.getTextures();
					TextureAtlasSprite[] sprites = new TextureAtlasSprite[textures.length];
					for (int i = 0; i < textures.length; i++) {
						sprites[i] = map.registerSprite(textures[i]);
					}
					stitcher.setSprites(sprites);
				}
			}
		}
	}

	/**
	 * Any particle that uses stitched textures must implement this interface.
	 * {@link Particle#getFXLayer()} must return 1 in order for this to work.
	 */
	public static interface IParticleSpriteReceiver {
		/**
		 * Sets the stitched particle sprites.
		 *
		 * @param sprites
		 */
		default void setStitchedSprites(TextureAtlasSprite[] sprites) {
			((Particle) this).setParticleTexture(sprites[0]);
		}
	}
}