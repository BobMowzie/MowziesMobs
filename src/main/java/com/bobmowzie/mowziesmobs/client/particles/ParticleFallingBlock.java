package com.bobmowzie.mowziesmobs.client.particles;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleFallingBlock extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
    public Vector3f rotAxis;
    public float rotAngle;
    public float rotationSpeed;
    public float prevRotAngle;
    public float size;
    public BlockState storedBlock;
    public float particleScale = 1;

    private EnumScaleBehavior behavior;

    public enum EnumScaleBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    public ParticleFallingBlock(World world, double x, double y, double z, float rotationSpeed, int duration, float size, float motionX, float motionY, float motionZ, EnumScaleBehavior behavior, BlockState blockState) {
        super(world, x, y, z);
        particleScale = 1;
        this.size = size;
        maxAge = duration;
        particleAlpha = 1;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.rotationSpeed = rotationSpeed;
        this.behavior = behavior;
        this.storedBlock = blockState;

        Vector3f motionVec = new Vector3f(motionX, motionY, motionZ);
        motionVec.cross(new Vector3f(0, 1, 0));
        rotAxis = motionVec;
    }

    @Override
    public int getBrightnessForRender(float delta) {
        return 240 | super.getBrightnessForRender(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        super.tick();
        if (age >= maxAge) {
            setExpired();
        }
        age++;

        motionY -= 0.2;

        prevRotAngle = rotAngle;

        if (!onGround) rotAngle -= rotationSpeed;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo activeRenderInfo, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);

        float var = (age + partialTicks)/maxAge;
        if (behavior == EnumScaleBehavior.GROW) {
            particleScale = size * var;
        }
        else if (behavior == EnumScaleBehavior.SHRINK) {
            particleScale = size * (1 - var);
        }
        else if (behavior == EnumScaleBehavior.GROW_THEN_SHRINK) {
            particleScale = (float) (size * (1 - var - Math.pow(2000, -var)));
        }
        else {
            particleScale = size;
        }

        if (storedBlock != null)
        {

            if (storedBlock.getRenderType() == BlockRenderType.MODEL)
            {

                if (storedBlock != world.getBlockState(new BlockPos(activeRenderInfo.getRenderViewEntity())) && storedBlock.getRenderType() != BlockRenderType.INVISIBLE)
                {
                    float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
                    float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
                    float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
                    float f8 = (float)(this.prevRotAngle + (this.rotAngle - this.prevRotAngle) * (double)partialTicks);

                    GlStateManager.pushMatrix();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();

                    bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
                    BlockPos blockpos = new BlockPos(posX, posY, posZ);

                    GlStateManager.translatef(0, 0.5f, 0);

                    GlStateManager.translatef(f5, f6, f7);

                    GlStateManager.scalef(particleScale, particleScale, particleScale);

                    GlStateManager.rotatef(f8, rotAxis.getX(), rotAxis.getY(), rotAxis.getZ());

                    GlStateManager.translated(-0.5 - blockpos.getX(), -0.5 - blockpos.getY(), -0.5 - blockpos.getZ());

                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                    blockrendererdispatcher.getBlockModelRenderer().renderModelFlat(world, blockrendererdispatcher.getModelForState(storedBlock), storedBlock, blockpos, bufferbuilder, false, rand, MathHelper.getPositionRandom(new BlockPos(0, 0, 0)));
                    tessellator.draw();

//                    GlStateManager.disableLighting();
                    GlStateManager.popMatrix();
                }
            }
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.TERRAIN_SHEET;
    }

    public static final class FallingBlockFactory extends ParticleFactory<ParticleFallingBlock.FallingBlockFactory, ParticleFallingBlock> {
        public FallingBlockFactory() {
            super(ParticleFallingBlock.class, ParticleTextureStitcher.create(ParticleFallingBlock.class, AtlasTexture.LOCATION_BLOCKS_TEXTURE));
        }

        @Override
        public ParticleFallingBlock createParticle(ImmutableParticleArgs args) {
            return new ParticleFallingBlock(args.world, args.x, args.y, args.z, (float) args.data[0], (int) args.data[1], (float) args.data[2], (float) args.data[3], (float) args.data[4], (float) args.data[5], (EnumScaleBehavior) args.data[6], (BlockState) args.data[7]);
        }
    }

    public static void spawnFallingBlock(World world, double x, double y, double z, float rotationSpeed, int duration, float size, float motionX, float motionY, float motionZ, EnumScaleBehavior behavior, BlockState blockState) {
        MMParticle.FALLING_BLOCK.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(rotationSpeed, duration, size, motionX, motionY, motionZ, behavior, blockState));
    }
}
