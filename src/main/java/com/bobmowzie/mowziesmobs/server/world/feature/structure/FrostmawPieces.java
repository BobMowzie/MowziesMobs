package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class FrostmawPieces {

    private static final ResourceLocation FROSTMAW = new ResourceLocation(MowziesMobs.MODID, "frostmaw_spawn");

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.of(
            FROSTMAW, new BlockPos(0, 1, 0)
    );

    public static void start(StructureManager manager, BlockPos pos, Rotation rot, List<StructurePiece> pieces, Random rand) {
        BlockPos rotationOffset = new BlockPos(0, 0, 0).rotate(rot);
        BlockPos blockPos = rotationOffset.offset(pos);
        pieces.add(new FrostmawPieces.Piece(manager, FROSTMAW, blockPos, rot));
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private final Rotation rotation;


        public Piece(StructureManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(FeatureHandler.FROSTMAW_PIECE, 0);
            this.resourceLocation = resourceLocationIn;
            this.templatePosition = pos;
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }


        public Piece(StructureManager templateManagerIn, CompoundTag tagCompound) {
            super(FeatureHandler.FROSTMAW_PIECE, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }


        private void setupPiece(StructureManager templateManager) {
            StructureTemplate template = templateManager.getOrCreate(this.resourceLocation);
            StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }


        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void addAdditionalSaveData(CompoundTag tagCompound) {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }


        /*
         * If you added any data marker structure blocks to your structure, you can access and modify them here. In this case,
         * our structure has a data maker with the string "chest" put into it. So we check to see if the incoming function is
         * "chest" and if it is, we now have that exact position.
         *
         * So what is done here is we replace the structure block with a chest and we can then set the loottable for it.
         *
         * You can set other data markers to do other behaviors such as spawn a random mob in a certain spot, randomize what
         * rare block spawns under the floor, or what item an Item Frame will have.
         */
        @Override
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {

        }

        @Override
        public boolean postProcess(WorldGenLevel p_230383_1_, StructureFeatureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, BoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            this.placeSettings.setRotation(this.rotation).setMirror(Mirror.NONE).setFinalizeEntities(true);
            BlockPos blockpos = FrostmawPieces.OFFSET.get(this.resourceLocation);
            this.templatePosition.offset(StructureTemplate.calculateRelativePosition(placeSettings, new BlockPos(0 - blockpos.getX(), blockpos.getY(), 0 - blockpos.getZ())));

            return super.postProcess(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);
        }
    }
}
