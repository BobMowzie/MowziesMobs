package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public class MowzieJigsawManager {
    static final Logger LOGGER = LogUtils.getLogger();

    public static Optional<PieceGenerator<JigsawConfiguration>> addPieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context, PieceFactory pieceFactory, BlockPos genPos, boolean villageBoundaryAdjust, boolean useTerrainHeight) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        RegistryAccess registryaccess = context.registryAccess();
        JigsawConfiguration jigsawconfiguration = context.config();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        StructureManager structuremanager = context.structureManager();
        LevelHeightAccessor levelheightaccessor = context.heightAccessor();
        Predicate<Holder<Biome>> predicate = context.validBiome();
        StructureFeature.bootstrap();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = jigsawconfiguration.startPool().value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            PoolElementStructurePiece poolelementstructurepiece = pieceFactory.create(structuremanager, structurepoolelement, genPos, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuremanager, genPos, rotation));
            BoundingBox pieceBoundingBox = poolelementstructurepiece.getBoundingBox();
            BlockPos offset = BlockPos.ZERO;
            if (structurepoolelement instanceof MowziePoolElement) {
                offset = new BlockPos(((MowziePoolElement) structurepoolelement).offsetX, ((MowziePoolElement) structurepoolelement).offsetY, ((MowziePoolElement) structurepoolelement).offsetZ);
                offset = offset.rotate(rotation);
            }
            int centerX = (pieceBoundingBox.maxX() + pieceBoundingBox.minX()) / 2;
            int centerZ = (pieceBoundingBox.maxZ() + pieceBoundingBox.minZ()) / 2;
            int height;
            if (useTerrainHeight) {
                System.out.println((centerX + offset.getX()) + " " + (centerZ + offset.getZ()));
                height = genPos.getY() + chunkgenerator.getFirstFreeHeight(centerX + offset.getX(), centerZ + offset.getZ(), Heightmap.Types.WORLD_SURFACE_WG, levelheightaccessor) + offset.getY();
            } else {
                height = genPos.getY();
            }

            if (!predicate.test(chunkgenerator.getNoiseBiome(QuartPos.fromBlock(centerX), QuartPos.fromBlock(height), QuartPos.fromBlock(centerZ)))) {
                return Optional.empty();
            } else {
                int l = pieceBoundingBox.minY() + poolelementstructurepiece.getGroundLevelDelta();
                poolelementstructurepiece.move(0, height - l, 0);
                return Optional.of((p_210282_, p_210283_) -> {
                    List<PoolElementStructurePiece> list = Lists.newArrayList();
                    list.add(poolelementstructurepiece);
                    if (jigsawconfiguration.maxDepth() > 0) {
                        int i1 = 80;
                        AABB aabb = new AABB((double)(centerX - 80), (double)(height - 80), (double)(centerZ - 80), (double)(centerX + 80 + 1), (double)(height + 80 + 1), (double)(centerZ + 80 + 1));
                        Placer jigsawplacement$placer = new Placer(registry, jigsawconfiguration.maxDepth(), pieceFactory, chunkgenerator, structuremanager, list, worldgenrandom);
                        VoxelShape shape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(pieceBoundingBox)), BooleanOp.ONLY_FIRST);
                        // Place starting piece
                        jigsawplacement$placer.placing.addLast(new PieceState(poolelementstructurepiece, new MutableObject<>(shape), 0));

                        // Iteratively place child pieces until 'placing' is empty
                        while(!jigsawplacement$placer.placing.isEmpty()) {
                            PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
                            jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, villageBoundaryAdjust, levelheightaccessor);
                        }

                        list.forEach(p_210282_::addPiece);
                    }
                });
            }
        }
    }

    public static void addPieces(RegistryAccess p_210291_, PoolElementStructurePiece p_210292_, int p_210293_, PieceFactory p_210294_, ChunkGenerator p_210295_, StructureManager p_210296_, List<? super PoolElementStructurePiece> p_210297_, Random p_210298_, LevelHeightAccessor p_210299_) {
        Registry<StructureTemplatePool> registry = p_210291_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Placer jigsawplacement$placer = new Placer(registry, p_210293_, p_210294_, p_210295_, p_210296_, p_210297_, p_210298_);
        jigsawplacement$placer.placing.addLast(new PieceState(p_210292_, new MutableObject<>(Shapes.INFINITY), 0));

        while(!jigsawplacement$placer.placing.isEmpty()) {
            PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
            jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, false, p_210299_);
        }

    }

    public interface PieceFactory {
        PoolElementStructurePiece create(StructureManager p_210301_, StructurePoolElement p_210302_, BlockPos p_210303_, int p_210304_, Rotation p_210305_, BoundingBox p_210306_);
    }

    static final class PieceState {
        final PoolElementStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int depth;

        PieceState(PoolElementStructurePiece p_210311_, MutableObject<VoxelShape> p_210312_, int p_210313_) {
            this.piece = p_210311_;
            this.free = p_210312_;
            this.depth = p_210313_;
        }
    }

    static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final PieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureManager structureManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final Random random;
        final Deque<PieceState> placing = Queues.newArrayDeque();

        Placer(Registry<StructureTemplatePool> p_210323_, int p_210324_, PieceFactory p_210325_, ChunkGenerator p_210326_, StructureManager p_210327_, List<? super PoolElementStructurePiece> p_210328_, Random p_210329_) {
            this.pools = p_210323_;
            this.maxDepth = p_210324_;
            this.factory = p_210325_;
            this.chunkGenerator = p_210326_;
            this.structureManager = p_210327_;
            this.pieces = p_210328_;
            this.random = p_210329_;
        }

        void tryPlacingChildren(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth, boolean villageBoundaryAdjust, LevelHeightAccessor heightAccessor) {
            StructurePoolElement structurepoolelement = piece.getElement();
            BlockPos blockpos = piece.getPosition();
            Rotation rotation = piece.getRotation();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean thisPieceIsRigid = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableobject = new MutableObject<>();
            BoundingBox thisPieceBoundingBox = piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.minY();

            label139:
            // Loop through all jigsaw blocks in this piece
            for(StructureTemplate.StructureBlockInfo thisPieceJigsawBlock : structurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                Direction direction = JigsawBlock.getFrontFacing(thisPieceJigsawBlock.state);
                BlockPos thisJigsawBlockPos = thisPieceJigsawBlock.pos;
                BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
                int thisPieceHeightFromBottomToJigsawBlock = thisJigsawBlockPos.getY() - thisPieceMinY;
                int k = -1;
                ResourceLocation poolResourceLocation = new ResourceLocation(thisPieceJigsawBlock.nbt.getString("pool"));
                Optional<StructureTemplatePool> poolOptional = this.pools.getOptional(poolResourceLocation);
                // If pool exists and is not empty
                if (poolOptional.isPresent() && (poolOptional.get().size() != 0 || Objects.equals(poolResourceLocation, Pools.EMPTY.location()))) {
                    ResourceLocation fallbackPoolResourceLocation = poolOptional.get().getFallback();
                    Optional<StructureTemplatePool> fallbackPoolOptional = this.pools.getOptional(fallbackPoolResourceLocation);
                    // If fallback pool exists and is not empty
                    if (fallbackPoolOptional.isPresent() && (fallbackPoolOptional.get().size() != 0 || Objects.equals(fallbackPoolResourceLocation, Pools.EMPTY.location()))) {
                        boolean nextPosInsideThisBounds = thisPieceBoundingBox.isInside(nextJigsawBlockPos);
                        MutableObject<VoxelShape> freeSpace;
//                        if (nextPosInsideThisBounds) {
//                            freeSpace = mutableobject;
//                            if (mutableobject.getValue() == null) {
//                                mutableobject.setValue(Shapes.create(AABB.of(thisPieceBoundingBox)));
//                            }
//                        } else {
                            freeSpace = free;
//                        }

                        List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
                        // Only add pool elements if not at max depth
                        if (depth != this.maxDepth) {
                            structurePoolElements.addAll(poolOptional.get().getShuffledTemplates(this.random));
                        }
                        // If at max depth, only fallback pool elements will be used
                        structurePoolElements.addAll(fallbackPoolOptional.get().getShuffledTemplates(this.random));

                        // Loop through pool elements
                        for(StructurePoolElement nextPieceCandidate : structurePoolElements) {
                            // If empty element, break from the loop and try again
                            if (nextPieceCandidate instanceof SinglePoolElement && ((SinglePoolElement)nextPieceCandidate).toString().equals("Single[Left[minecraft:empty]]")) {
                                break;
                            }

                            // Iterate through possible rotations of this element
                            for(Rotation nextPieceRotation : Rotation.getShuffled(this.random)) {
                                List<StructureTemplate.StructureBlockInfo> nextPieceJigsawBlocks = nextPieceCandidate.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, nextPieceRotation, this.random);
                                BoundingBox nextPieceBoundingBoxOrigin = nextPieceCandidate.getBoundingBox(this.structureManager, BlockPos.ZERO, nextPieceRotation);
                                int l;
                                // Village boundary adjustment
                                if (villageBoundaryAdjust && nextPieceBoundingBoxOrigin.getYSpan() <= 16) {
                                    // Map each jigsaw block to the size of the largest next piece it can generate and l becomes the largest of these
                                    l = nextPieceJigsawBlocks.stream().mapToInt((blockInfo) -> {
                                        // If the next piece's jigsaw block would place inside its own bounding box, return 0
                                        if (!nextPieceBoundingBoxOrigin.isInside(blockInfo.pos.relative(JigsawBlock.getFrontFacing(blockInfo.state)))) {
                                            return 0;
                                        } else {
                                            ResourceLocation resourcelocation2 = new ResourceLocation(blockInfo.nbt.getString("pool"));
                                            Optional<StructureTemplatePool> optional2 = this.pools.getOptional(resourcelocation2);
                                            Optional<StructureTemplatePool> optional3 = optional2.flatMap((p_210344_) -> {
                                                return this.pools.getOptional(p_210344_.getFallback());
                                            });
                                            int j3 = optional2.map((structureTemplatePool) -> {
                                                return structureTemplatePool.getMaxSize(this.structureManager);
                                            }).orElse(0);
                                            int k3 = optional3.map((structureTemplatePool) -> {
                                                return structureTemplatePool.getMaxSize(this.structureManager);
                                            }).orElse(0);
                                            return Math.max(j3, k3);
                                        }
                                    }).max().orElse(0);
                                } else {
                                    l = 0;
                                }

                                for(StructureTemplate.StructureBlockInfo nextPieceJigsawBlock : nextPieceJigsawBlocks) {
                                    boolean canAttach;
                                    if (nextPieceCandidate instanceof MowziePoolElement && ((MowziePoolElement) nextPieceCandidate).twoWay()) {
                                        canAttach = MowziePoolElement.canAttachTwoWays(thisPieceJigsawBlock, nextPieceJigsawBlock);
                                    }
                                    else {
                                        canAttach = JigsawBlock.canAttach(thisPieceJigsawBlock, nextPieceJigsawBlock);
                                    }
                                    if (canAttach) {
                                        BlockPos nextPieceJigsawBlockPos = nextPieceJigsawBlock.pos;
                                        BlockPos nextPiecePos = nextJigsawBlockPos.subtract(nextPieceJigsawBlockPos);
                                        BoundingBox nextPieceBoundingBox = nextPieceCandidate.getBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                                        int nextPieceMinY = nextPieceBoundingBox.minY();
                                        if (nextPieceCandidate instanceof MowziePoolElement) {
                                            nextPieceMinY -= ((MowziePoolElement) nextPieceCandidate).boundsMinY;
                                        }
                                        StructureTemplatePool.Projection structuretemplatepool$projection1 = nextPieceCandidate.getProjection();
                                        boolean nextPieceIsRigid = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                                        int nextPieceJigsawBlockY = nextPieceJigsawBlockPos.getY();
                                        int k1 = thisPieceHeightFromBottomToJigsawBlock - nextPieceJigsawBlockY + JigsawBlock.getFrontFacing(thisPieceJigsawBlock.state).getStepY();
                                        int l1;
                                        if (thisPieceIsRigid && nextPieceIsRigid) {
                                            l1 = thisPieceMinY + k1;
                                        } else {
                                            if (k == -1) {
                                                k = this.chunkGenerator.getFirstFreeHeight(thisJigsawBlockPos.getX(), thisJigsawBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                            }

                                            l1 = k - nextPieceJigsawBlockY;
                                        }

                                        int i2 = l1 - nextPieceMinY;
                                        BoundingBox nextPieceBoundingBoxPlaced = nextPieceBoundingBox.moved(0, i2, 0);
                                        BlockPos blockpos5 = nextPiecePos.offset(0, i2, 0);
                                        if (l > 0) {
                                            int j2 = Math.max(l + 1, nextPieceBoundingBoxPlaced.maxY() - nextPieceBoundingBoxPlaced.minY());
                                            nextPieceBoundingBoxPlaced.encapsulate(new BlockPos(nextPieceBoundingBoxPlaced.minX(), nextPieceBoundingBoxPlaced.minY() + j2, nextPieceBoundingBoxPlaced.minZ()));
                                        }

                                        boolean ignoreBounds = false;
                                        if (nextPieceCandidate instanceof MowziePoolElement) ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                                        if (ignoreBounds || !Shapes.joinIsNotEmpty(freeSpace.getValue(), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                            if (!ignoreBounds) freeSpace.setValue(Shapes.joinUnoptimized(freeSpace.getValue(), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced)), BooleanOp.ONLY_FIRST));
                                            int pieceGroundLevelDelta = piece.getGroundLevelDelta();
                                            int k2;
                                            if (nextPieceIsRigid) {
                                                k2 = pieceGroundLevelDelta - k1;
                                            } else {
                                                k2 = nextPieceCandidate.getGroundLevelDelta();
                                            }

                                            PoolElementStructurePiece poolelementstructurepiece = this.factory.create(this.structureManager, nextPieceCandidate, blockpos5, k2, nextPieceRotation, nextPieceBoundingBoxPlaced);
                                            int l2;
                                            if (thisPieceIsRigid) {
                                                l2 = thisPieceMinY + thisPieceHeightFromBottomToJigsawBlock;
                                            } else if (nextPieceIsRigid) {
                                                l2 = l1 + nextPieceJigsawBlockY;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(thisJigsawBlockPos.getX(), thisJigsawBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                                }

                                                l2 = k + k1 / 2;
                                            }

                                            piece.addJunction(new JigsawJunction(nextJigsawBlockPos.getX(), l2 - thisPieceHeightFromBottomToJigsawBlock + pieceGroundLevelDelta, nextJigsawBlockPos.getZ(), k1, structuretemplatepool$projection1));
                                            poolelementstructurepiece.addJunction(new JigsawJunction(thisJigsawBlockPos.getX(), l2 - nextPieceJigsawBlockY + k2, thisJigsawBlockPos.getZ(), -k1, structuretemplatepool$projection));
                                            this.pieces.add(poolelementstructurepiece);
                                            if (depth + 1 <= this.maxDepth) {
                                                this.placing.addLast(new PieceState(poolelementstructurepiece, freeSpace, depth + 1));
                                            }
                                            continue label139;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LOGGER.warn("Empty or non-existent fallback pool: {}", (Object)fallbackPoolResourceLocation);
                    }
                } else {
                    LOGGER.warn("Empty or non-existent pool: {}", (Object)poolResourceLocation);
                }
            }

        }
    }
}
