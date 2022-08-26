package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WroughtnautChamberPieces {

    private static final ResourceLocation PART = new ResourceLocation(MowziesMobs.MODID, "wroughtnaut_chamber");

    public static void start(StructureManager manager, BlockPos pos, Rotation rot, StructurePieceAccessor pieces) {
//        BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(rot);
//        BlockPos blockPos = rotationOffset.offset(pos);
        pieces.addPiece(new WroughtnautChamberPieces.Piece(manager, PART, pos, rot));
    }

    public static class Piece extends TemplateStructurePiece {
        private final BlockPos startPos;
        private BlockPos wallPos;

        public Piece(StructureManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(FeatureHandler.WROUGHTNAUT_CHAMBER_PIECE, 0, templateManagerIn, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotationIn, resourceLocationIn), pos);
            this.startPos = pos;
            this.wallPos = null;
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(FeatureHandler.WROUGHTNAUT_CHAMBER_PIECE, tagCompound, context.structureManager(), (resourceLocation) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot")), resourceLocation));
            this.startPos = new BlockPos(
                    tagCompound.getInt("StartX"),
                    tagCompound.getInt("StartY"),
                    tagCompound.getInt("StartZ")
            );
            if (tagCompound.getBoolean("HasWall")) {
                this.wallPos = new BlockPos(
                        tagCompound.getInt("WallX"),
                        tagCompound.getInt("WallY"),
                        tagCompound.getInt("WallZ")
                );
            }
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation resourceLocation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super.addAdditionalSaveData(context, tagCompound);
            tagCompound.putString("Rot", this.placeSettings.getRotation().name());
            tagCompound.putInt("StartX", startPos.getX());
            tagCompound.putInt("StartY", startPos.getY());
            tagCompound.putInt("StartZ", startPos.getZ());
            tagCompound.putBoolean("HasWall", wallPos != null);
            if (wallPos != null) {
                tagCompound.putInt("WallX", wallPos.getX());
                tagCompound.putInt("WallY", wallPos.getY());
                tagCompound.putInt("WallZ", wallPos.getZ());
            }
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
    }
}
