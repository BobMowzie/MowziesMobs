package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WroughtnautChamberPieces {

    private static final ResourceLocation PART = new ResourceLocation(MowziesMobs.MODID, "wroughtnaut_chamber");

    public static void start(TemplateManager manager, BlockPos pos, Rotation rot, List<StructurePiece> pieces, Random rand) {
        BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(rot);
        BlockPos blockPos = rotationOffset.add(pos);
        pieces.add(new WroughtnautChamberPieces.Piece(manager, PART, blockPos, rot));
    }

    public static class Piece extends TemplateStructurePiece {
        private final ResourceLocation resourceLocation;
        private Rotation rotation;
        private final BlockPos startPos;
        private BlockPos wallPos;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn)
        {
            super(FeatureHandler.WROUGHTNAUT_CHAMBER_PIECE, 0);
            this.resourceLocation = resourceLocationIn;
            this.templatePosition = pos;
            this.rotation = rotationIn;
            this.startPos = pos;
            this.wallPos = null;
            this.setupPiece(templateManagerIn);
        }


        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound)
        {
            super(FeatureHandler.WROUGHTNAUT_CHAMBER_PIECE, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
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
            this.setupPiece(templateManagerIn);
        }


        private void setupPiece(TemplateManager templateManager)
        {
            Template template = templateManager.getTemplateDefaulted(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }


        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void readAdditional(CompoundNBT tagCompound)
        {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
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
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {

        }

        @Override
        public boolean func_230383_a_(ISeedReader worldIn, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox mutableBoundingBoxIn, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            Pair<BlockPos, Rotation> chamberResults;
            if (wallPos == null) {
                chamberResults = tryWroughtChamber(worldIn, startPos.getX(), startPos.getY(), startPos.getZ());
                this.placeSettings.setIgnoreEntities(false);
            }
            else {
                chamberResults = Pair.of(wallPos, rotation);
                this.placeSettings.setIgnoreEntities(true);
            }

            if (chamberResults == null) return false;
            wallPos = chamberResults.getLeft();
            rotation = chamberResults.getRight();
            this.templatePosition = chamberResults.getLeft();
            this.placeSettings.setRotation(chamberResults.getRight());
            this.placeSettings.func_237133_d_(true);
//            System.out.println("Wroughtnaut Chamber at " + templatePosition.getX() + " " + templatePosition.getY() + " " + templatePosition.getZ());
            BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(placeSettings.getRotation());
            this.templatePosition = this.templatePosition.add(rotationOffset);
            mutableBoundingBoxIn = template.getMutableBoundingBox(placeSettings, templatePosition);

            return super.func_230383_a_(worldIn, p_230383_2_, p_230383_3_, p_230383_4_, mutableBoundingBoxIn, p_230383_6_, p_230383_7_);
        }

        @Nullable
        public static Pair<BlockPos, Rotation> tryWroughtChamber(IWorld world, int x, int surfaceY, int z) {
            int xzCheckDistance = 6; // Always starts at chunk center, so it can safely check 6 blocks in any direction

            int heightMax = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMax.get().intValue();
            int heightMin = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMin.get().intValue();
            if (heightMax == -1) heightMax = surfaceY;
            if (heightMin == -1) heightMin = 0;
            for (int y = heightMax; y >= heightMin; y--) {
                BlockPos airPos = checkForAirInXY(world, new BlockPos(x, y, z), xzCheckDistance);
                if (airPos != null) {
                    x = airPos.getX();
                    z = airPos.getZ();
                    for (int y2 = 1; y2 <= 30; y2++) {
                        BlockPos p0 = new BlockPos(x, y - y2, z);
                        if (world.getBlockState(p0).isNormalCube(world, p0)) {
                            int y4 = 0;
                            int y5 = 0;
                            for (int x2 = 0; x2 <= xzCheckDistance; x2++) {
                                BlockPos p1 = new BlockPos(x - x2, y - y2 + y4 + 1, z);
                                if (world.getBlockState(p1).isNormalCube(world, p1)) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x - x2, y - y2 + y4 + 1 + y3, z);
                                        if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                            wall = false;
                                            y4 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x - x2, y - y2 + y4, z);
                                        if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                            return Pair.of(new BlockPos(x - x2, y - y2 + y4, z), Rotation.CLOCKWISE_180);
                                        }
                                    }
                                }
                                p1 = new BlockPos(x + x2, y - y2 + y5 + 1, z);
                                if (world.getBlockState(p1).isNormalCube(world, p1)) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x + x2, y - y2 + y5 + 1 + y3, z);
                                        if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                            wall = false;
                                            y5 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x + x2, y - y2 + y5, z);
                                        if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                            return Pair.of(new BlockPos(x + x2, y - y2 + y5, z), Rotation.NONE);
                                        }
                                    }
                                }
                            }
                            y4 = 0;
                            y5 = 0;
                            for (int z2 = 0; z2 <= xzCheckDistance; z2++) {
                                BlockPos p1 = new BlockPos(x, y - y2 + y4 + 1, z - z2);
                                if (world.getBlockState(p1).isOpaqueCube(world, p1)) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y4 + 1 + y3, z - z2);
                                        if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                            wall = false;
                                            y4 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y4, z - z2);
                                        if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                            return Pair.of(new BlockPos(x, y - y2 + y4, z - z2), Rotation.COUNTERCLOCKWISE_90);
                                        }
                                    }
                                }
                                p1 = new BlockPos(x, y - y2 + y5 + 1, z + z2);
                                if (world.getBlockState(p1).isNormalCube(world, p1)) {
                                    Boolean wall = true;
                                    for (int y3 = 1; y3 <= 4; y3++) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y5 + 1 + y3, z + z2);
                                        if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                            wall = false;
                                            y5 += y3;
                                            break;
                                        }
                                    }
                                    if (wall) {
                                        BlockPos p2 = new BlockPos(x, y - y2 + y5, z + z2);
                                        if (world.getBlockState(p2).isNormalCube(world, p2)) {
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
        public static BlockPos checkForAirInXY(IWorld world, BlockPos start, int range) {
            for (int dx = -range; dx < range; dx++) {
                for (int dz = -range; dz < range; dz++) {
                    BlockPos check = start.add(dx, 0, dz);
                    if (world.chunkExists(check.getX() >> 4, check.getZ() >> 4)) {
                        if (world.isAirBlock(check)) return check;
                    }
                }
            }
            return null;
        }
    }
}
