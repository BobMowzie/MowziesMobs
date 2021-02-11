package com.bobmowzie.mowziesmobs.client.particle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleFallingBlock extends SpriteTexturedParticle {
    public Vector3f rotAxis;
    public float rotAngle;
    public float rotationSpeed;
    public float prevRotAngle;
    public float size;
    public BlockState storedBlock;

    private EnumScaleBehavior behavior;

    public enum EnumScaleBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    public ParticleFallingBlock(World world, double x, double y, double z, float motionX, float motionY, float motionZ, float rotationSpeed, int duration, float size, EnumScaleBehavior behavior, BlockState blockState) {
        super(world, x, y, z);
        this.setSprite(Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(blockState));
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
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.draw();

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.alphaFunc(516, 0.1F);

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

                if (storedBlock != world.getBlockState(new BlockPos(renderInfo.getRenderViewEntity())) && storedBlock.getRenderType() != BlockRenderType.INVISIBLE)
                {
                    Vec3d vec3d = renderInfo.getProjectedView();
                    float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - vec3d.getX());
                    float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - vec3d.getY());
                    float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - vec3d.getZ());
                    float f8 = (float)(this.prevRotAngle + (this.rotAngle - this.prevRotAngle) * (double)partialTicks);

                    RenderSystem.pushMatrix();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();

                    bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
                    BlockPos blockpos = new BlockPos(posX, posY, posZ);

                    RenderSystem.translatef(0, 0.5f, 0);

                    RenderSystem.translatef(f5, f6, f7);

                    RenderSystem.scalef(particleScale, particleScale, particleScale);

                    RenderSystem.rotatef(f8, rotAxis.getX(), rotAxis.getY(), rotAxis.getZ());

                    RenderSystem.translated(-0.5 - blockpos.getX(), -0.5 - blockpos.getY(), -0.5 - blockpos.getZ());

                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//                    blockrendererdispatcher.getBlockModelRenderer().renderModelFlat(world, blockrendererdispatcher.getModelForState(storedBlock), storedBlock, blockpos, RenderSystem.bufferbuilder, false, rand, MathHelper.getPositionRandom(new BlockPos(0, 0, 0)));
                    tessellator.draw();

//                    GlStateManager.disableLighting();
                    RenderSystem.popMatrix();
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

    @OnlyIn(Dist.CLIENT)
    public static final class FallingBlockFactory implements IParticleFactory<FallingBlockData> {
        private IAnimatedSprite spriteSet;

        public FallingBlockFactory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(FallingBlockData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BlockState blockstate = typeIn.getBlockState();
            return !blockstate.isAir() && blockstate.getBlock() != Blocks.MOVING_PISTON ? new ParticleFallingBlock(worldIn, x, y, z, (float) xSpeed, (float) ySpeed, (float) zSpeed, typeIn.getRotationSpeed(), typeIn.getDuration(), typeIn.getScale(), typeIn.getBehavior(), blockstate) : null;
        }
    }

    public static void spawnFallingBlock(World world, double x, double y, double z, float rotationSpeed, int duration, float size, float motionX, float motionY, float motionZ, EnumScaleBehavior behavior, BlockState blockState) {
        world.addParticle(new FallingBlockData(rotationSpeed, duration, size, behavior, blockState), x, y, z, motionX, motionY, motionZ);
    }

    public static class FallingBlockData implements IParticleData {
        public static final IParticleData.IDeserializer<FallingBlockData> DESERIALIZER = new IParticleData.IDeserializer<FallingBlockData>() {
            public FallingBlockData deserialize(ParticleType<FallingBlockData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float rotationSpeed = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                BlockState state = (new BlockStateParser(reader, false)).parse(false).getState();
                return new FallingBlockData(rotationSpeed, duration, scale,  EnumScaleBehavior.CONSTANT, state);
            }

            public FallingBlockData read(ParticleType<FallingBlockData> particleTypeIn, PacketBuffer buffer) {
                return new FallingBlockData(buffer.readFloat(), buffer.readInt(), buffer.readFloat(), EnumScaleBehavior.CONSTANT, Block.BLOCK_STATE_IDS.getByValue(buffer.readVarInt()));
            }
        };

        private final float rotationSpeed;
        private final int duration;
        private final float scale;
        private final EnumScaleBehavior behavior;
        private final BlockState blockState;

        public FallingBlockData(float rotationSpeed, int duration, float size, EnumScaleBehavior behavior, BlockState blockState) {
            this.rotationSpeed = rotationSpeed;
            this.duration = duration;
            this.scale = size;
            this.behavior = behavior;
            this.blockState = blockState;
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeFloat(this.rotationSpeed);
            buffer.writeInt(this.duration);
            buffer.writeFloat(this.scale);
            buffer.writeVarInt(Block.BLOCK_STATE_IDS.get(this.blockState));
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
            return String.format(Locale.ROOT, "%s %.2f %d %.2f %s", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.rotationSpeed, this.duration, this.scale, this.duration, BlockStateParser.toString(this.blockState));
        }

        @Override
        public ParticleType<FallingBlockData> getType() {
            return ParticleHandler.FALLING_BLOCK.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getRotationSpeed() {
            return this.rotationSpeed;
        }

        @OnlyIn(Dist.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @OnlyIn(Dist.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public EnumScaleBehavior getBehavior() {
            return this.behavior;
        }

        @OnlyIn(Dist.CLIENT)
        public BlockState getBlockState() {
            return this.blockState;
        }
    }
}
