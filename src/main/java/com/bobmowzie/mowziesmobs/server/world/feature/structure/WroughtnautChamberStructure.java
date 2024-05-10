package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.datagen.StructureSetHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

public class WroughtnautChamberStructure extends MowzieStructure {
	public static final Codec<WroughtnautChamberStructure> CODEC = simpleCodec(WroughtnautChamberStructure::new);
	
    public WroughtnautChamberStructure(Structure.StructureSettings settings) {
        super(settings, ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig, StructureTypeHandler.FERROUS_WROUGHTNAUT_BIOMES, false, false, true);
    }

    @Override
    public void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        Pair<BlockPos, Rotation> tryResult = tryWroughtChamber(context.chunkGenerator(), context.heightAccessor(), x, y, z, context.randomState());
        if (tryResult == null) return;
        BlockPos pos = tryResult.getLeft();
        Rotation rotation = tryResult.getRight();
        BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(rotation);
        pos = pos.offset(rotationOffset);
        WroughtnautChamberPieces.start(context.structureTemplateManager(), pos, rotation, builder);
    }

    @Nullable
    public static Pair<BlockPos, Rotation> tryWroughtChamber(ChunkGenerator generator, LevelHeightAccessor heightAccessor, int x, int surfaceY, int z, RandomState state) {
        int xzCheckDistance = 8; // Always starts at chunk center, so it can safely check 8 blocks in any direction

        int heightMax = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMax.get().intValue();
        int heightMin = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMin.get().intValue();
        if (heightMax == -65 || heightMax > surfaceY) heightMax = surfaceY;
        if (heightMin == -65) heightMin = -64;

        for (int dx = -xzCheckDistance; dx < xzCheckDistance; dx += 2) {
            for (int dz = -xzCheckDistance; dz < xzCheckDistance; dz += 2) {
                // Check for air to find a cave
                BlockPos airPos = null;
                NoiseColumn column = generator.getBaseColumn(x + dx, z + dz, heightAccessor, state);
                for (int y = heightMax; y > heightMin; y--) {
                    if (!column.getBlock(y).isSolid()) {
                        airPos = new BlockPos(x + dx, y, z + dz);
                        break;
                    }
                }

                // If air was found, check down to find the cave floor
                if (airPos != null) {
                    BlockPos groundPos = null;
                    for (int y = airPos.getY(); y > heightMin; y--) {
                        if (column.getBlock(y).isSolid()) {
                            // Found floor of cave
                            groundPos = airPos.atY(y);
                            break;
                        }
                    }

                    // If ground was found, search for wall in all 4 horizontal directions
                    if (groundPos != null) {
                        for (Direction dir : Direction.Plane.HORIZONTAL) {
                            BlockPos.MutableBlockPos checkWallPos = groundPos.above().mutable();
                            for (int d = 1; d <= xzCheckDistance; d++) {
                                checkWallPos.move(dir);
                                NoiseColumn wallCheckColumn = generator.getBaseColumn(checkWallPos.getX(), checkWallPos.getZ(), heightAccessor, state);
                                int wallBaseY = checkWallPos.getY() - 1;

                                // Check upwards to see if four blocks up are solid. If not, checkWallPos moves up to the new floor
                                for (int wallHeightCount = 1; true; wallHeightCount++) {
                                    BlockState wallBlock = wallCheckColumn.getBlock(checkWallPos.getY());
                                    if (!wallBlock.isSolid()) {
                                        // If not solid, no wall. Move checkWallPos in dir direction and check again
                                        break;
                                    }
                                    if (wallHeightCount == 4) {
                                        // Found a wall! Return its position and rotation
                                        Rotation rotation = switch (dir) {
                                            case NORTH -> Rotation.COUNTERCLOCKWISE_90;
                                            case EAST -> Rotation.NONE;
                                            case WEST -> Rotation.CLOCKWISE_180;
                                            default -> Rotation.CLOCKWISE_90;
                                        };
                                        return Pair.of(new BlockPos(checkWallPos.getX(), wallBaseY, checkWallPos.getZ()), rotation);
                                    }
                                    checkWallPos.move(Direction.UP);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

	@Override
	public StructureType<?> type() {
		return StructureTypeHandler.WROUGHTNAUT_CHAMBER.get();
	}
}
