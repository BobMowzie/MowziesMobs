package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class WroughtnautChamberPieces {

    private static final ResourceLocation PART = new ResourceLocation(MowziesMobs.MODID, "wroughtnaut_chamber");

    public static void start(StructureTemplateManager manager, BlockPos pos, Rotation rot, StructurePieceAccessor pieces) {
        pieces.addPiece(new WroughtnautChamberPieces.Piece(manager, PART, pos, rot));
    }

    public static class Piece extends TemplateStructurePiece {

        public Piece(StructureTemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(StructureTypeHandler.WROUGHTNAUT_CHAMBER_PIECE.get(), 0, templateManagerIn, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotationIn, resourceLocationIn), pos);
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(StructureTypeHandler.WROUGHTNAUT_CHAMBER_PIECE.get(), tagCompound, context.structureTemplateManager(), (resourceLocation) -> makeSettings(Rotation.valueOf(tagCompound.contains("Rot") ? tagCompound.getString("Rot") : Rotation.NONE.name()), resourceLocation));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation resourceLocation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super.addAdditionalSaveData(context, tagCompound);
            tagCompound.putString("Rot", this.placeSettings.getRotation().name());
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
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, RandomSource rand, BoundingBox sbb) {

        }
    }
}
