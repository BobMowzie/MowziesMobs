package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WroughtnautChamberPieces {

    private static final ResourceLocation PART = new ResourceLocation(MowziesMobs.MODID, "wroughtnaut_chamber");

    public static void start(StructureManager manager, BlockPos pos, Rotation rot, List<StructurePiece> pieces, Random rand) {
        BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(rot);
        BlockPos blockPos = rotationOffset.offset(pos);
        pieces.add(new WroughtnautChamberPieces.Piece(manager, PART, blockPos, rot));
    }

    public static class Piece extends TemplateStructurePiece {
        private final BlockPos startPos;
        private BlockPos wallPos;

        public Piece(StructureManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn)
        {
            super(FeatureHandler.WROUGHTNAUT_CHAMBER_PIECE, 0, templateManagerIn, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotationIn, resourceLocationIn), pos);
            this.startPos = pos;
            this.wallPos = null;
        }

        public Piece(ServerLevel level, CompoundTag tagCompound)
        {
            super(FeatureHandler.WROUGHTNAUT_CHAMBER_PIECE, tagCompound, level, (resourceLocation) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot")), resourceLocation));
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
        protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound)
        {
            super.addAdditionalSaveData(level, tagCompound);
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

        @Override
        public boolean postProcess(WorldGenLevel worldIn, StructureFeatureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, BoundingBox mutableBoundingBoxIn, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            Rotation rot = placeSettings.getRotation();
            Pair<BlockPos, Rotation> chamberResults;
            if (wallPos == null) {
                chamberResults = tryWroughtChamber(worldIn, startPos.getX(), startPos.getY(), startPos.getZ());
                this.placeSettings.setIgnoreEntities(false);
            }
            else {
                chamberResults = Pair.of(wallPos, rot);
                this.placeSettings.setIgnoreEntities(true);
            }

            if (chamberResults == null) return false;
            wallPos = chamberResults.getLeft();
            rot = chamberResults.getRight();
            this.templatePosition = chamberResults.getLeft();
            this.placeSettings.setRotation(rot);
            this.placeSettings.setFinalizeEntities(true);
//            System.out.println("Wroughtnaut Chamber at " + templatePosition.getX() + " " + templatePosition.getY() + " " + templatePosition.getZ());
            BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(placeSettings.getRotation());
            this.templatePosition = this.templatePosition.offset(rotationOffset);
            mutableBoundingBoxIn = template.getBoundingBox(placeSettings, templatePosition);

            return super.postProcess(worldIn, p_230383_2_, p_230383_3_, p_230383_4_, mutableBoundingBoxIn, p_230383_6_, p_230383_7_);
        }

        @Nullable
        public static Pair<BlockPos, Rotation> tryWroughtChamber(LevelAccessor world, int x, int surfaceY, int z) {
            int xzCheckDistance = 6; // Always starts at chunk center, so it can safely check 6 blocks in any direction
            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, surfaceY, z));

            int heightMax = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMax.get().intValue();
            int heightMin = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMin.get().intValue();
            if (heightMax == -1) heightMax = surfaceY;
            if (heightMin == -1) heightMin = 0;
            heightMax = Math.min(heightMax, world.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z));
            for (int y = heightMax; y >= heightMin; y--) {
                BlockPos airPos = checkForAirInXY(world, new BlockPos(x, y, z), xzCheckDistance);
                if (airPos != null) {
                    x = airPos.getX();
                    z = airPos.getZ();
                    for (int y2 = 1; y2 <= 30; y2++) {
                        BlockPos p0 = new BlockPos(x, y - y2, z);
                        ChunkPos p0ChunkPos = new ChunkPos(p0);
                        if (!chunkPos.equals(p0ChunkPos)) continue;
                        if (world.getBlockState(p0).isRedstoneConductor(world, p0)) {
                            int y4 = 0;
                            int y5 = 0;
                            for (int x2 = 0; x2 <= xzCheckDistance; x2++) {
                                BlockPos p1 = new BlockPos(x - x2, y - y2 + y4 + 1, z);
                                ChunkPos p1ChunkPos = new ChunkPos(p1);
                                if (!chunkPos.equals(p1ChunkPos)) continue;
                                if (world.getBlockState(p1).isRedstoneConductor(world, p1)) {
                                    boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x - x2, y - y2 + y4 + 1 + y3, z);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (!world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            wall = false;
                                            y4 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x - x2, y - y2 + y4, z);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            return Pair.of(new BlockPos(x - x2, y - y2 + y4, z), Rotation.CLOCKWISE_180);
                                        }
                                    }
                                }
                                p1 = new BlockPos(x + x2, y - y2 + y5 + 1, z);
                                p1ChunkPos = new ChunkPos(p1);
                                if (!chunkPos.equals(p1ChunkPos)) continue;
                                if (world.getBlockState(p1).isRedstoneConductor(world, p1)) {
                                    boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x + x2, y - y2 + y5 + 1 + y3, z);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (!world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            wall = false;
                                            y5 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x + x2, y - y2 + y5, z);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            return Pair.of(new BlockPos(x + x2, y - y2 + y5, z), Rotation.NONE);
                                        }
                                    }
                                }
                            }
                            y4 = 0;
                            y5 = 0;
                            for (int z2 = 0; z2 <= xzCheckDistance; z2++) {
                                BlockPos p1 = new BlockPos(x, y - y2 + y4 + 1, z - z2);
                                ChunkPos p1ChunkPos = new ChunkPos(p1);
                                if (!chunkPos.equals(p1ChunkPos)) continue;
                                if (world.getBlockState(p1).isSolidRender(world, p1)) {
                                    boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y4 + 1 + y3, z - z2);
                                        if (!world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            wall = false;
                                            y4 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y4, z - z2);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            return Pair.of(new BlockPos(x, y - y2 + y4, z - z2), Rotation.COUNTERCLOCKWISE_90);
                                        }
                                    }
                                }
                                p1 = new BlockPos(x, y - y2 + y5 + 1, z + z2);
                                p1ChunkPos = new ChunkPos(p1);
                                if (!chunkPos.equals(p1ChunkPos)) continue;
                                if (world.getBlockState(p1).isRedstoneConductor(world, p1)) {
                                    boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y5 + 1 + y3, z + z2);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (!world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            wall = false;
                                            y5 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y5, z + z2);
                                        ChunkPos p2ChunkPos = new ChunkPos(p2);
                                        if (!chunkPos.equals(p2ChunkPos)) continue;
                                        if (world.getBlockState(p2).isRedstoneConductor(world, p2)) {
                                            return Pair.of(new BlockPos(x, y - y2 + y5, z + z2), Rotation.CLOCKWISE_90);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            return null;
        }

        @Nullable
        public static BlockPos checkForAirInXY(LevelAccessor world, BlockPos start, int range) {
            for (int dx = -range; dx < range; dx++) {
                for (int dz = -range; dz < range; dz++) {
                    BlockPos check = start.offset(dx, 0, dz);
                    if (world.hasChunk(check.getX() >> 4, check.getZ() >> 4)) {
                        if (world.isEmptyBlock(check)) return check;
                    }
                }
            }
            return null;
        }
    }
}
