package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.IntegrityProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// Edited from Telepathic Grunt's base code

public class WroughtnautChamberStructure extends ScatteredStructure<NoFeatureConfig> {
    public WroughtnautChamberStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
    {
        super(config);
    }

    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return 8;
    }

    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return 5;
    }

    @Override
    public String getStructureName()
    {
        return MowziesMobs.MODID + ":wroughtnaut_chamber";
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public Structure.IStartFactory getStartFactory()
    {
        return WroughtnautChamberStructure.Start::new;
    }

    protected int getSeedModifier()
    {
        return 123444789;
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        List<String> dimensionNames = ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationConfig.dimensions.get();
        ResourceLocation currDimensionName = worldIn.getDimension().getType().getRegistryName();
        if (currDimensionName == null || !dimensionNames.contains(currDimensionName.toString())) {
            return false;
        }

        /*for (Rotation rot: Rotation.values()) {
            BlockPos checkPos = pos;

            // Check this air
            if (worldIn.getBlockState(checkPos).getMaterial().isSolid()) continue;

            // Check ground
            if (!worldIn.getBlockState(checkPos.offset(Direction.DOWN)).getMaterial().isSolid()) continue;

            // Move scan to wall
            boolean flag = true;
            BlockPos offset = new BlockPos(1, 0, 0).rotate(rot);
            checkPos = checkPos.add(offset);
            for (int i = 0; i < 4; i++) {
                BlockState state = worldIn.getBlockState(checkPos);
                if (!state.getMaterial().isSolid()) {
                    flag = false;
                    continue;
                }
                checkPos = checkPos.offset(Direction.UP);
            }
            if (!flag) continue;

            pos = pos.add(offset);
            System.out.println("Wroughtnaut chamber at " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
//            loadTemplate(worldIn.getWorld(), pos, new ResourceLocation(MowziesMobs.MODID, "wroughtnaut_chamber"), rot);
            return true;
        }
        return false;*/

        return super.place(worldIn, generator, rand, pos, config);
    }

    private boolean loadTemplate(World world, BlockPos blockpos, ResourceLocation name, Rotation rotation) {
        if (!world.isRemote()) {
            BlockPos blockpos1 = blockpos.add(0, 1, 0);
            ServerWorld serverworld = (ServerWorld) world;
            TemplateManager templatemanager = serverworld.getStructureTemplateManager();

            Template template;
            try {
                template = templatemanager.getTemplate(name);
            } catch (ResourceLocationException var10) {
                return false;
            }

            if (template == null) {
                return false;
            } else {
                BlockPos blockpos2 = template.getSize();

                PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(rotation).setIgnoreEntities(false).setChunk((ChunkPos) null);
//                if (this.integrity < 1.0F) {
//                    placementsettings.clearProcessors().addProcessor(new IntegrityProcessor(MathHelper.clamp(this.integrity, 0.0F, 1.0F))).setRandom(func_214074_b(this.seed));
//                }

                template.addBlocksToWorldChunk(world, blockpos1, placementsettings);
                return true;
            }
        }
        return false;
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
        }

        @Override
        public void generateStructure(IWorld worldIn, Random rand, MutableBoundingBox structurebb, ChunkPos pos) {
            super.generateStructure(worldIn, rand, structurebb, pos);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            //Check out vanilla's WoodlandMansionStructure for how they offset the x and z
            //so that they get the y value of the land at the mansion's entrance, no matter
            //which direction the mansion is rotated.
            //
            //However, for most purposes, getting the y value of land with the default x and z is good enough.
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos pos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            WroughtnautChamberPieces.start(templateManagerIn, pos, rotation, this.components, this.rand);
//            System.out.println("Wroughtnaut chamber at " + pos.getX() + " " + pos.getY() + " " + pos.getZ());

            //Sets the bounds of the structure.
//            this.recalculateStructureSize();
            this.bounds = MutableBoundingBox.getNewBoundingBox();
            bounds.minX = (chunkX << 4) + 7;
            bounds.minZ = (chunkZ << 4) + 7;
            bounds.minY = surfaceY;
            bounds.maxX = bounds.minX + 1;
            bounds.maxZ = bounds.minZ + 1;
            bounds.maxY = bounds.minY + 1;
        }
    }
}
