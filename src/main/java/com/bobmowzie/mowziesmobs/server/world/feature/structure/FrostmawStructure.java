package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FrostmawStructure extends MowzieStructure {
    public FrostmawStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public String getStructureName()
    {
        return MowziesMobs.MODID + ":frostmaw_spawn";
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig;
    }

    protected int getSeedModifier() {
        return 1237654789;
    }

    @Override
    public IStartFactory getStartFactory() {
        return FrostmawStructure.Start::new;
    }

    public static class Start extends StructureStart {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
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
            int surfaceY = generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            int heightMax = ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.heightMax.get().intValue();
            int heightMin = ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.heightMin.get().intValue();
            if (heightMax != -1 && surfaceY > heightMax) return;
            if (heightMin != -1 && surfaceY < heightMin) return;
            BlockPos blockpos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            FrostmawPieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

            //Sets the bounds of the structure.
            this.recalculateStructureSize();
        }
    }
}
