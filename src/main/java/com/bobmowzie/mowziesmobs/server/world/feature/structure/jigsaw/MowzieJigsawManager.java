package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
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

    public static Optional<PieceGenerator<JigsawConfiguration>> addPieces(
            PieceGeneratorSupplier.Context<JigsawConfiguration> context,
            PieceFactory pieceFactory, BlockPos genPos, boolean villageBoundaryAdjust, boolean useTerrainHeight, int maxDistFromStart,
            String pathJigsawName, String interiorJigsawName,
            Set<String> mustConnectPools, Set<String> replacePools, String deadEndConnectorPool
    ) {
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
                offset = ((MowziePoolElement) structurepoolelement).bounds.offset;
                offset = offset.rotate(rotation);
            }
            int centerX = (pieceBoundingBox.maxX() + pieceBoundingBox.minX()) / 2;
            int centerZ = (pieceBoundingBox.maxZ() + pieceBoundingBox.minZ()) / 2;
            int height;
            if (useTerrainHeight) {
                height = genPos.getY() + chunkgenerator.getFirstFreeHeight(centerX + offset.getX(), centerZ + offset.getZ(), Heightmap.Types.WORLD_SURFACE_WG, levelheightaccessor) + offset.getY();
            } else {
                height = genPos.getY();
            }

            if (!predicate.test(chunkgenerator.getNoiseBiome(QuartPos.fromBlock(centerX), QuartPos.fromBlock(height), QuartPos.fromBlock(centerZ)))) {
                return Optional.empty();
            } else {
                int l = pieceBoundingBox.minY() + poolelementstructurepiece.getGroundLevelDelta();
                poolelementstructurepiece.move(0, height - l, 0);
                return Optional.of((builder, p_210283_) -> {
                    List<PoolElementStructurePiece> list = Lists.newArrayList();
                    list.add(poolelementstructurepiece);
                    if (jigsawconfiguration.maxDepth() >= 0) {
                        AABB aabb = new AABB((double)(centerX - maxDistFromStart), (double)(height - maxDistFromStart), (double)(centerZ - maxDistFromStart), (double)(centerX + maxDistFromStart + 1), (double)(height + maxDistFromStart + 1), (double)(centerZ + maxDistFromStart + 1));
                        VoxelShape shape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(pieceBoundingBox)), BooleanOp.ONLY_FIRST);

                        // Bounding voxelshapes
                        MutableObject<VoxelShape> free = new MutableObject<>(shape);
                        MutableObject<VoxelShape> interiorFree = new MutableObject<>(Shapes.empty());
                        MutableObject<Map<String, VoxelShape>> specialBounds = new MutableObject<>(new HashMap<>());

                        Placer placer = new Placer(registry, jigsawconfiguration.maxDepth(), pieceFactory, chunkgenerator, structuremanager, list, worldgenrandom, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds);

                        // Place starting piece
                        PieceState startingPiece = new PieceState(poolelementstructurepiece, 0, null);
                        for (StructureBlockInfo structureBlockInfo : placer.getJigsawBlocksFromPieceState(startingPiece)) {
                            placer.placing.add(new Pair<>(structureBlockInfo, startingPiece));
                        }

                        // Iteratively place child pieces until 'placing' is empty or max depth is reached
                        while(!placer.placing.isEmpty()) {
                            Pair<StructureBlockInfo, PieceState> nextJigsawBlock = placer.placing.first();
                            placer.placing.remove(nextJigsawBlock);
                            if (nextJigsawBlock.getSecond().depth > jigsawconfiguration.maxDepth()) {
                                break;
                            }
                            placer.tryPlacingChildren(nextJigsawBlock.getFirst(), nextJigsawBlock.getSecond(), villageBoundaryAdjust, levelheightaccessor);
                        }

                        Placer fallbackPlacer = new FallbackPlacer(registry, jigsawconfiguration.maxDepth(), pieceFactory, chunkgenerator, structuremanager, list, worldgenrandom, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds, placer);
                        while(!fallbackPlacer.placing.isEmpty()) {
                            Pair<StructureBlockInfo, PieceState> nextJigsawBlock = fallbackPlacer.placing.first();
                            fallbackPlacer.placing.remove(nextJigsawBlock);
                            fallbackPlacer.tryPlacingChildren(nextJigsawBlock.getFirst(), nextJigsawBlock.getSecond(), villageBoundaryAdjust, levelheightaccessor);
                        }

                        Placer interiorPlacer = new InteriorPlacer(registry, jigsawconfiguration.maxDepth(), pieceFactory, chunkgenerator, structuremanager, list, worldgenrandom, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds, fallbackPlacer);
                        while(!interiorPlacer.placing.isEmpty()) {
                            Pair<StructureBlockInfo, PieceState> nextJigsawBlock = interiorPlacer.placing.first();
                            interiorPlacer.placing.remove(nextJigsawBlock);
                            interiorPlacer.tryPlacingChildren(nextJigsawBlock.getFirst(), nextJigsawBlock.getSecond(), villageBoundaryAdjust, levelheightaccessor);
                        }

                        list.sort((p1, p2) -> {
                            int i1, i2;
                            i1 = i2 = 0;
                            if (p1.getElement() instanceof MowziePoolElement) i1 = ((MowziePoolElement) p1.getElement()).genOrder;
                            if (p2.getElement() instanceof MowziePoolElement) i2 = ((MowziePoolElement) p2.getElement()).genOrder;
                            return Integer.compare(i1, i2);
                        });

                        list.forEach(builder::addPiece);
                    }
                });
            }
        }
    }

    public interface PieceFactory {
        PoolElementStructurePiece create(StructureManager p_210301_, StructurePoolElement p_210302_, BlockPos p_210303_, int p_210304_, Rotation p_210305_, BoundingBox p_210306_);
    }

    static class PieceState {
        final PoolElementStructurePiece piece;
        final int depth;
        final PieceState parent;
        final Set<PieceState> children;

        PieceState(PoolElementStructurePiece p_210311_, int p_210313_, PieceState parent) {
            this.piece = p_210311_;
            this.depth = p_210313_;
            this.parent = parent;
            this.children = new HashSet<>();
        }
    }

    public record PieceSelection(PieceState pieceState, StructurePoolElement origPiece, StructurePoolElement nextPiece, PoolElementStructurePiece poolelementstructurepiece, StructureBlockInfo origJigsaw, StructureBlockInfo connectedJigsaw, StructureBlockInfo nextJigsaw, BoundingBox nextPieceBoundingBoxPlaced, BoundingBox nextPieceInteriorBoundingBox, int l2) {};

    private static Map<String, Integer> poolPlaceOrder = new HashMap<>();
    static {
        poolPlaceOrder.put("mowziesmobs:monastery/interior/blocker_pool", -5);
    }

    public static Comparator<Pair<StructureBlockInfo, PieceState>> placeOrderComparator = (p1, p2) -> {
        int p1Order = poolPlaceOrder.getOrDefault(p1.getFirst().nbt.getString("pool"), 0);
        int p2Order = poolPlaceOrder.getOrDefault(p2.getFirst().nbt.getString("pool"), 0);
        int result = Integer.compare(p1Order, p2Order);
        if (result == 0) result = Integer.compare(p1.getSecond().depth, p2.getSecond().depth);
        if (result == 0) result = Integer.compare(p1.hashCode(), p2.hashCode());
        return result;
    };

    static class Placer {
        final Registry<StructureTemplatePool> pools;
        final int maxDepth;
        final PieceFactory factory;
        final ChunkGenerator chunkGenerator;
        final StructureManager structureManager;
        final List<? super PoolElementStructurePiece> pieces;
        protected final Random random;
        protected final String pathJigsawName;
        protected final String interiorJigsawName;

        MutableObject<VoxelShape> free;
        MutableObject<VoxelShape> interiorFree;
        MutableObject<Map<String, VoxelShape>> specialBounds;

        final SortedSet<Pair<StructureBlockInfo, PieceState>> placing = new TreeSet<>(placeOrderComparator);
        final SortedSet<Pair<StructureBlockInfo, PieceState>> fallbacks = new TreeSet<>(placeOrderComparator);
        final SortedSet<Pair<StructureBlockInfo, PieceState>> interior = new TreeSet<>(placeOrderComparator);
        protected int numPaths;

        Placer(Registry<StructureTemplatePool> p_210323_, int p_210324_, PieceFactory p_210325_, ChunkGenerator p_210326_, StructureManager p_210327_, List<? super PoolElementStructurePiece> p_210328_, Random p_210329_,
               String pathJigsawName, String interiorJigsawName,
               MutableObject<VoxelShape> free, MutableObject<VoxelShape> interiorFree, MutableObject<Map<String, VoxelShape>> specialBounds) {
            this.pools = p_210323_;
            this.maxDepth = p_210324_;
            this.factory = p_210325_;
            this.chunkGenerator = p_210326_;
            this.structureManager = p_210327_;
            this.pieces = p_210328_;
            this.random = p_210329_;
            this.numPaths = 0;

            this.pathJigsawName = pathJigsawName;
            this.interiorJigsawName = interiorJigsawName;

            this.free = free;
            this.interiorFree = interiorFree;
            this.specialBounds = specialBounds;
        }

        void tryPlacingChildren(StructureBlockInfo thisPieceJigsawBlock, PieceState pieceState, boolean villageBoundaryAdjust, LevelHeightAccessor heightAccessor) {
            if (skipJigsawBlock(thisPieceJigsawBlock, pieceState)) return;

            if (thisPieceJigsawBlock.nbt.getString("target").equals(pathJigsawName)) {
                if (numPaths > 0) numPaths--;
            }
            String pool = selectPool(thisPieceJigsawBlock);
            ResourceLocation poolResourceLocation = new ResourceLocation(pool);
            Optional<StructureTemplatePool> poolOptional = this.pools.getOptional(poolResourceLocation);
            // If pool exists and is not empty
            if (poolOptional.isPresent() && (poolOptional.get().size() != 0 || Objects.equals(poolResourceLocation, Pools.EMPTY.location()))) {
                ResourceLocation fallbackPoolResourceLocation = poolOptional.get().getFallback();
                Optional<StructureTemplatePool> fallbackPoolOptional = this.pools.getOptional(fallbackPoolResourceLocation);
                // If fallback pool exists and is not empty
                if (fallbackPoolOptional.isPresent() && (fallbackPoolOptional.get().size() != 0 || Objects.equals(fallbackPoolResourceLocation, Pools.EMPTY.location()))) {

                    PieceSelection pieceSelection = selectPiece(pieceState, poolOptional.get(), fallbackPoolOptional.get(), villageBoundaryAdjust, thisPieceJigsawBlock, heightAccessor);

                    if (pieceSelection != null) addNextPieceState(pieceSelection);
                } else {
                    LOGGER.warn("Empty or non-existent fallback pool: {}", (Object)fallbackPoolResourceLocation);
                }
            } else {
                LOGGER.warn("Empty or non-existent pool: {}", (Object)poolResourceLocation);
            }

        }

        protected boolean skipJigsawBlock(StructureBlockInfo thisPieceJigsawBlock, PieceState pieceState) {
            if (thisPieceJigsawBlock.nbt.getString("target").equals(interiorJigsawName)) {
                interior.add(Pair.of(thisPieceJigsawBlock, pieceState));
                return true;
            }
            return false;
        }

        List<StructureBlockInfo> getJigsawBlocksFromPieceState(PieceState pieceState) {
            StructurePoolElement structurepoolelement = pieceState.piece.getElement();
            BlockPos blockpos = pieceState.piece.getPosition();
            Rotation rotation = pieceState.piece.getRotation();
            return structurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random);
        }

        String selectPool(StructureBlockInfo thisPieceJigsawBlock) {
            return thisPieceJigsawBlock.nbt.getString("pool");
        }

        List<StructurePoolElement> addPoolElements(PieceState pieceState, StructureTemplatePool pool, StructureTemplatePool fallbackPool) {
            List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
            structurePoolElements.addAll(pool.getShuffledTemplates(this.random));
            return structurePoolElements;
        }

        boolean isJigsawBlockFacingFreeSpace(StructureBlockInfo jigsawBlockInfo, MutableObject<VoxelShape> freeSpace) {
            Direction direction = JigsawBlock.getFrontFacing(jigsawBlockInfo.state);
            BlockPos thisJigsawBlockPos = jigsawBlockInfo.pos;
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
            AABB aabb = new AABB(nextJigsawBlockPos);
            return !Shapes.joinIsNotEmpty(freeSpace.getValue(), Shapes.create(aabb.deflate(0.25D)), BooleanOp.ONLY_SECOND);
        }

        boolean canJigsawBlockFitNextPiece(StructureBlockInfo jigsawBlockInfo, MutableObject<VoxelShape> freeSpace) {
            Direction direction = JigsawBlock.getFrontFacing(jigsawBlockInfo.state);
            BlockPos thisJigsawBlockPos = jigsawBlockInfo.pos;
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
            AABB aabb = new AABB(nextJigsawBlockPos);
            Vec3i offset = direction.getNormal();
            aabb = aabb.inflate(2).move(offset.getX() * 2, offset.getY() * 2, offset.getZ() * 2);
            return !Shapes.joinIsNotEmpty(freeSpace.getValue(), Shapes.create(aabb.deflate(0.25D)), BooleanOp.ONLY_SECOND);
        }

        PieceSelection selectPiece(PieceState pieceState, StructureTemplatePool poolOptional, StructureTemplatePool fallbackPoolOptional, boolean villageBoundaryAdjust, StructureBlockInfo thisPieceJigsawBlock, LevelHeightAccessor heightAccessor) {
            BoundingBox thisPieceBoundingBox = pieceState.piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.minY();
            Direction direction = JigsawBlock.getFrontFacing(thisPieceJigsawBlock.state);
            BlockPos thisJigsawBlockPos = thisPieceJigsawBlock.pos;
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
            int thisPieceHeightFromBottomToJigsawBlock = thisJigsawBlockPos.getY() - thisPieceMinY;
            StructurePoolElement structurepoolelement = pieceState.piece.getElement();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean thisPieceIsRigid = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            int k = -1;

            // Loop through pool elements
            List<StructurePoolElement> structurePoolElements = addPoolElements(pieceState, poolOptional, fallbackPoolOptional);
            for(StructurePoolElement nextPieceCandidate : structurePoolElements) {
                // If empty element, break from the loop and spawn nothing
                if (nextPieceCandidate == FallbackPoolElement.INSTANCE) {
                    break;
                }
                if (nextPieceCandidate == EmptyPoolElement.INSTANCE || nextPieceCandidate instanceof SinglePoolElement && ((SinglePoolElement) nextPieceCandidate).toString().equals("Single[Left[minecraft:empty]]")) {
                    return null;
                }

                if (nextPieceCandidate instanceof MowziePoolElement) {
                    MowziePoolElement mowziePoolElement = (MowziePoolElement) nextPieceCandidate;
                    if (!mowziePoolElement.checkCriteria(pieceState, this)) continue;
                }

                // Iterate through possible rotations of this element
                for (Rotation nextPieceRotation : Rotation.getShuffled(this.random)) {
                    List<StructureBlockInfo> nextPieceJigsawBlocks = nextPieceCandidate.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, nextPieceRotation, this.random);
                    BoundingBox nextPieceBoundingBoxOrigin = nextPieceCandidate.getBoundingBox(this.structureManager, BlockPos.ZERO, nextPieceRotation);

                    int largestSizeOfNextNextPiece;
                    // Village boundary adjustment
                    if (villageBoundaryAdjust && nextPieceBoundingBoxOrigin.getYSpan() <= 16) {
                        // Map each jigsaw block to the size of the largest next piece it can generate and largestSizeOfNextNextPiece becomes the largest of these
                        largestSizeOfNextNextPiece = nextPieceJigsawBlocks.stream().mapToInt((blockInfo) -> {
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
                        largestSizeOfNextNextPiece = 0;
                    }

                    for (StructureBlockInfo nextPieceJigsawBlock : nextPieceJigsawBlocks) {
                        boolean canAttach;
                        if (nextPieceCandidate instanceof MowziePoolElement && ((MowziePoolElement) nextPieceCandidate).twoWay()) {
                            canAttach = MowziePoolElement.canAttachTwoWays(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        } else {
                            canAttach = JigsawBlock.canAttach(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        }
                        if (canAttach) {
                            BlockPos nextPieceJigsawBlockPos = nextPieceJigsawBlock.pos;
                            BlockPos nextPiecePos = nextJigsawBlockPos.subtract(nextPieceJigsawBlockPos);
                            BoundingBox nextPieceBoundingBox = nextPieceCandidate.getBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                            int nextPieceMinY = nextPieceBoundingBox.minY();
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                nextPieceMinY -= ((MowziePoolElement) nextPieceCandidate).bounds.boundsMinOffset.getY();
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
                            if (largestSizeOfNextNextPiece > 0) {
                                int j2 = Math.max(largestSizeOfNextNextPiece + 1, nextPieceBoundingBoxPlaced.maxY() - nextPieceBoundingBoxPlaced.minY());
                                nextPieceBoundingBoxPlaced.encapsulate(new BlockPos(nextPieceBoundingBoxPlaced.minX(), nextPieceBoundingBoxPlaced.minY() + j2, nextPieceBoundingBoxPlaced.minZ()));
                            }

                            // Check height params
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                Optional<Integer> maxHeight = ((MowziePoolElement) nextPieceCandidate).maxHeight;
                                Optional<Integer> minHeight = ((MowziePoolElement) nextPieceCandidate).minHeight;
                                if (maxHeight.isPresent() || minHeight.isPresent()) {
                                    int freeHeight = this.chunkGenerator.getFirstFreeHeight(nextPieceBoundingBoxPlaced.minX() + nextPieceBoundingBoxPlaced.getXSpan() / 2, nextPieceBoundingBoxPlaced.minZ() + nextPieceBoundingBoxPlaced.getZSpan() / 2, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                    if (maxHeight.isPresent() && nextPieceMinY - freeHeight > maxHeight.get()) continue;
                                    if (minHeight.isPresent() && nextPieceMinY - freeHeight < minHeight.get()) continue;
                                }
                            }

                            if (checkBounds(nextPieceCandidate, pieceState, nextPieceBoundingBoxPlaced, nextPiecePos, nextPieceRotation, i2)) {
                                // Special bounding boxes
                                if (nextPieceCandidate instanceof MowziePoolElement) {
                                    MowziePoolElement mowziePoolElement = (MowziePoolElement) nextPieceCandidate;

                                    Optional<String> needsOverlapBounds = mowziePoolElement.bounds.needsOverlapBounds;
                                    if (needsOverlapBounds.isPresent() && specialBounds.getValue().containsKey(needsOverlapBounds.get())) {
                                        if (!Shapes.joinIsNotEmpty(specialBounds.getValue().get(needsOverlapBounds.get()), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced).deflate(0.25D)), BooleanOp.AND)) {
                                            continue;
                                        }
                                    }
                                }

                                int pieceGroundLevelDelta = pieceState.piece.getGroundLevelDelta();
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

                                BoundingBox interiorBounds = null;
                                if (nextPieceCandidate instanceof MowziePoolElement) {
                                    interiorBounds = ((MowziePoolElement) nextPieceCandidate).getInteriorBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                                    if (interiorBounds != null) interiorBounds = interiorBounds.moved(0, i2, 0);
                                }
                                return new PieceSelection(pieceState, structurepoolelement, nextPieceCandidate, poolelementstructurepiece, thisPieceJigsawBlock, nextPieceJigsawBlock, null, nextPieceBoundingBoxPlaced, interiorBounds, l2);
                            }
                        }
                    }
                }
            }
            this.fallbacks.add(new Pair<>(thisPieceJigsawBlock, pieceState));
            return null;
        }

        protected boolean checkBounds(StructurePoolElement nextPieceCandidate, PieceState pieceState, BoundingBox nextPieceBoundingBoxPlaced, BlockPos nextPiecePos, Rotation nextPieceRotation, int i2) {
            boolean ignoreBounds = false;
            BoundingBox spaceCheckBounds = nextPieceBoundingBoxPlaced;
            if (nextPieceCandidate instanceof MowziePoolElement) {
                ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                spaceCheckBounds = ((MowziePoolElement) nextPieceCandidate).getCheckBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation).moved(0, i2, 0);
            }
            VoxelShape freeSpace = Shapes.joinUnoptimized(free.getValue(), Shapes.create(AABB.of(pieceState.piece.getBoundingBox())), BooleanOp.OR);
            return ignoreBounds || !Shapes.joinIsNotEmpty(freeSpace, Shapes.create(AABB.of(spaceCheckBounds).deflate(0.25D)), BooleanOp.ONLY_SECOND);
        }

        void addNextPieceState(PieceSelection pieceSelection) {
            // Subtract the bounding box from the free space
            if (!(pieceSelection.nextPiece instanceof MowziePoolElement && (((MowziePoolElement) pieceSelection.nextPiece).ignoresBounds() || !((MowziePoolElement) pieceSelection.nextPiece).placeBounds()))) {
                free.setValue(Shapes.joinUnoptimized(free.getValue(), Shapes.create(AABB.of(pieceSelection.nextPieceBoundingBoxPlaced)), BooleanOp.ONLY_FIRST));
            }

            // Add the bounds to the interior space
            if (pieceSelection.nextPiece instanceof MowziePoolElement && pieceSelection.nextPieceInteriorBoundingBox != null) {
                interiorFree.setValue(Shapes.joinUnoptimized(interiorFree.getValue(), Shapes.create(AABB.of(pieceSelection.nextPieceInteriorBoundingBox)), BooleanOp.OR));
            }

            // Special bounds - add this piece's bounds to any special bounds its set to
            if (pieceSelection.nextPiece instanceof MowziePoolElement) {
                MowziePoolElement mowziePoolElement = (MowziePoolElement) pieceSelection.nextPiece;
                Optional<String> nextPieceSpecialBounds = mowziePoolElement.bounds.specialBounds;
                if (nextPieceSpecialBounds.isPresent()) {
                    if (specialBounds.getValue().containsKey(nextPieceSpecialBounds.get())) {
                        specialBounds.getValue().put(nextPieceSpecialBounds.get(), Shapes.joinUnoptimized(specialBounds.getValue().get(nextPieceSpecialBounds.get()), Shapes.create(AABB.of(pieceSelection.nextPieceBoundingBoxPlaced)), BooleanOp.OR));
                    } else {
                        specialBounds.getValue().put(nextPieceSpecialBounds.get(), Shapes.create(AABB.of(pieceSelection.nextPieceBoundingBoxPlaced)));
                    }
                }
            }

            BoundingBox thisPieceBoundingBox = pieceSelection.pieceState.piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.minY();
            int thisPieceHeightFromBottomToJigsawBlock = pieceSelection.origJigsaw.pos.getY() - thisPieceMinY;
            int pieceGroundLevelDelta = pieceSelection.pieceState.piece.getGroundLevelDelta();
            int k1 = thisPieceHeightFromBottomToJigsawBlock - pieceSelection.connectedJigsaw.pos.getY() + JigsawBlock.getFrontFacing(pieceSelection.origJigsaw.state).getStepY();
            int k2;
            if (pieceSelection.nextPiece.getProjection() == StructureTemplatePool.Projection.RIGID) {
                k2 = pieceGroundLevelDelta - k1;
            } else {
                k2 = pieceSelection.nextPiece.getGroundLevelDelta();
            }

            pieceSelection.pieceState.piece.addJunction(new JigsawJunction(pieceSelection.connectedJigsaw.pos.getX(), pieceSelection.l2 - thisPieceHeightFromBottomToJigsawBlock + pieceGroundLevelDelta, pieceSelection.connectedJigsaw.pos.getZ(), k1, pieceSelection.nextPiece.getProjection()));
            pieceSelection.poolelementstructurepiece.addJunction(new JigsawJunction(pieceSelection.origJigsaw.pos.getX(), pieceSelection.l2 - pieceSelection.connectedJigsaw.pos.getY() + k2, pieceSelection.origJigsaw.pos.getZ(), -k1, pieceSelection.origPiece.getProjection()));
            this.pieces.add(pieceSelection.poolelementstructurepiece);
            PieceState nextPieceState = new PieceState(pieceSelection.poolelementstructurepiece, pieceSelection.pieceState.depth + 1, pieceSelection.pieceState);

            // Queue up the next jigsaw pieces
            List<StructureBlockInfo> nextJigsaws = getJigsawBlocksFromPieceState(nextPieceState);
            // Skip the jigsaw piece that just connected (unless its two-way)
            if (!(pieceSelection.poolelementstructurepiece.getElement() instanceof MowziePoolElement) || !((MowziePoolElement) pieceSelection.poolelementstructurepiece.getElement()).twoWay()) {
                nextJigsaws.removeIf(jigsaw -> {
                    Direction direction = JigsawBlock.getFrontFacing(pieceSelection.origJigsaw.state);
                    BlockPos thisJigsawBlockPos = pieceSelection.origJigsaw.pos;
                    BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
                    return jigsaw.pos.equals(nextJigsawBlockPos);
                });
            }

            // Count up the number of next paths. Check for overridden count first.
            if (pieceSelection.nextPiece instanceof MowziePoolElement && ((MowziePoolElement) pieceSelection.nextPiece).numPathsOverride.isPresent()) {
                numPaths += ((MowziePoolElement) pieceSelection.nextPiece).numPathsOverride.get();
            }
            else {
                for (StructureBlockInfo jigsaw : nextJigsaws) {
                    if (jigsaw.nbt.getString("target").equals(pathJigsawName)) {
                        numPaths++;
                    }
                }
            }

            for (StructureBlockInfo jigsaw : nextJigsaws) {
                this.placing.add(new Pair<>(jigsaw, nextPieceState));
            }

            pieceSelection.pieceState.children.add(nextPieceState);
        }

    }

    static final class FallbackPlacer extends Placer {

        FallbackPlacer(Registry<StructureTemplatePool> p_210323_, int p_210324_, PieceFactory p_210325_, ChunkGenerator p_210326_, StructureManager p_210327_, List<? super PoolElementStructurePiece> p_210328_, Random p_210329_,
                       String pathJigsawName, String interiorJigsawName,
                       MutableObject<VoxelShape> free, MutableObject<VoxelShape> interiorFree, MutableObject<Map<String, VoxelShape>> specialBounds,
                       Placer previousPlacer)
        {
            super(p_210323_, p_210324_, p_210325_, p_210326_, p_210327_, p_210328_, p_210329_, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds);
            this.placing.addAll(previousPlacer.placing);
            this.placing.addAll(previousPlacer.fallbacks);
            this.interior.addAll(previousPlacer.interior);
            this.numPaths = previousPlacer.numPaths;
        }

        List<StructurePoolElement> addPoolElements(PieceState pieceState, StructureTemplatePool pool, StructureTemplatePool fallbackPool) {
            List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
            structurePoolElements.addAll(fallbackPool.getShuffledTemplates(this.random));
            return structurePoolElements;
        }
    }

    static final class InteriorPlacer extends Placer {

        InteriorPlacer(Registry<StructureTemplatePool> p_210323_, int p_210324_, PieceFactory p_210325_, ChunkGenerator p_210326_, StructureManager p_210327_, List<? super PoolElementStructurePiece> p_210328_, Random p_210329_,
                       String pathJigsawName, String interiorJigsawName,
                       MutableObject<VoxelShape> free, MutableObject<VoxelShape> interiorFree, MutableObject<Map<String, VoxelShape>> specialBounds,
                       Placer previousPlacer)
        {
            super(p_210323_, p_210324_, p_210325_, p_210326_, p_210327_, p_210328_, p_210329_, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds);
            this.placing.addAll(previousPlacer.interior);
            this.free = interiorFree;
            this.numPaths = previousPlacer.numPaths;
        }

        @Override
        protected boolean skipJigsawBlock(StructureBlockInfo thisPieceJigsawBlock, PieceState pieceState) {
            return false;
        }

        @Override
        protected boolean checkBounds(StructurePoolElement nextPieceCandidate, PieceState pieceState, BoundingBox nextPieceBoundingBoxPlaced, BlockPos nextPiecePos, Rotation nextPieceRotation, int i2) {
            boolean ignoreBounds = false;
            BoundingBox spaceCheckBounds = nextPieceBoundingBoxPlaced;
            if (nextPieceCandidate instanceof MowziePoolElement) {
                ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                spaceCheckBounds = ((MowziePoolElement) nextPieceCandidate).getCheckBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation).moved(0, i2, 0);
            }
            return ignoreBounds || !Shapes.joinIsNotEmpty(free.getValue(), Shapes.create(AABB.of(spaceCheckBounds).deflate(0.25D)), BooleanOp.ONLY_SECOND);
        }
    }

    /* DOESNT WORK. Maybe try again later
    static final class DeadEndConnectorPlacer extends Placer {
        Map<BlockPos, BlockPos> destinations = new HashMap<>();
        private Set<String> mustConnectPools;
        private Set<String> toReplacePools;
        private String deadEndConnectorPool;

        DeadEndConnectorPlacer(Registry<StructureTemplatePool> p_210323_, int p_210324_, PieceFactory p_210325_, ChunkGenerator p_210326_, StructureManager p_210327_, List<? super PoolElementStructurePiece> p_210328_, Random p_210329_, String pathJigsawName, Placer oldPlacer, Set<String> mustConnectPools, Set<String> toReplacePools, String deadEndConnectorPool) {
            super(p_210323_, p_210324_, p_210325_, p_210326_, p_210327_, p_210328_, p_210329_, pathJigsawName);
            this.numPaths = oldPlacer.numPaths;
            this.mustConnectPools = mustConnectPools;
            this.toReplacePools = toReplacePools;
            this.deadEndConnectorPool = deadEndConnectorPool;
            List<StructureBlockInfo> needConnecting = new ArrayList<>();
            while (!oldPlacer.placing.isEmpty()) {
                Pair<StructureBlockInfo, PieceState> toPlace = oldPlacer.placing.removeFirst();
                StructureBlockInfo jigsawBlock = toPlace.getFirst();
                PieceState pieceState = toPlace.getSecond();
                if (jigsawMustConnect(jigsawBlock) && canJigsawBlockFitNextPiece(jigsawBlock, pieceState.free)) {
                    needConnecting.add(jigsawBlock);
                    this.placing.addLast(toPlace);
                }
            }

            // Match each pos with its closest neighbor - naive approach for now. Robust approach needs Kuhn–Munkres algorithm
            Set<StructureBlockInfo> used = new HashSet<>();
            for (StructureBlockInfo block1 : needConnecting) {
                if (used.contains(block1)) continue;
                StructureBlockInfo closest = null;
                double bestDist = Double.MAX_VALUE;
                for (StructureBlockInfo block2 : needConnecting) {
                    if (block1 == block2 || used.contains(block2)) continue;
                    double dist = block1.pos.distSqr(block2.pos);
                    if (dist < bestDist) {
                        bestDist = dist;
                        closest = block2;
                    }
                }
                if (closest != null) {
                    destinations.put(block1.pos, closest.pos);
                    destinations.put(closest.pos, block1.pos);
                    used.add(block1);
                    used.add(closest);
                }
            }
        }

        // Replace the pool with dead end connector pool
        @Override
        String selectPool(StructureBlockInfo thisPieceJigsawBlock) {
            String pool = super.selectPool(thisPieceJigsawBlock);
            if (toReplacePools != null && toReplacePools.contains(pool)) {
                pool = deadEndConnectorPool;
            }
            return pool;
        }

        @Override
        List<StructurePoolElement> addPoolElements(PieceState pieceState, StructureTemplatePool pool, StructureTemplatePool fallbackPool) {
            List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
            structurePoolElements.addAll(pool.getShuffledTemplates(this.random));
            return structurePoolElements;
        }

        boolean jigsawMustConnect(StructureBlockInfo jigsaw) {
            return mustConnectPools.contains(jigsaw.nbt.getString("pool"));
        }

        @Override
        void addNextPieceState(PieceSelection pieceSelection) {
            super.addNextPieceState(pieceSelection);
        }

        @Override
        PieceSelection selectPiece(PieceState pieceState, StructureTemplatePool pool, StructureTemplatePool fallbackPool, boolean villageBoundaryAdjust, StructureBlockInfo thisPieceJigsawBlock, LevelHeightAccessor heightAccessor) {
            // If this isn't coming after a must-connect jigsaw block, use normal placement
            if (!jigsawMustConnect(thisPieceJigsawBlock)) return super.selectPiece(pieceState, pool, fallbackPool, villageBoundaryAdjust, thisPieceJigsawBlock, heightAccessor);

            // If no destination, stop placing
            BlockPos destination = this.destinations.get(thisPieceJigsawBlock.pos);
            if (destination == null) return null;
            if (thisPieceJigsawBlock.pos.distSqr(destination) <= 1) return null;

            BoundingBox thisPieceBoundingBox = pieceState.piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.minY();
            Direction direction = JigsawBlock.getFrontFacing(thisPieceJigsawBlock.state);
            BlockPos thisJigsawBlockPos = thisPieceJigsawBlock.pos;
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
            int thisPieceHeightFromBottomToJigsawBlock = thisJigsawBlockPos.getY() - thisPieceMinY;
            StructurePoolElement structurepoolelement = pieceState.piece.getElement();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean thisPieceIsRigid = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            int k = -1;

            float closestDist = Float.MAX_VALUE;
            List<PieceSelection> rejects = new ArrayList<>();
            PieceSelection pieceSelection = null;
            boolean ignoreBounds = false;
            // Loop through pool elements
            List<StructurePoolElement> structurePoolElements = addPoolElements(pieceState, pool, fallbackPool);
            Set<StructurePoolElement> structurePoolElementSet = new HashSet<>(structurePoolElements);
            for(StructurePoolElement nextPieceCandidate : structurePoolElementSet) {
                // If empty element, break from the loop and spawn nothing
                if (nextPieceCandidate instanceof SinglePoolElement && ((SinglePoolElement) nextPieceCandidate).toString().equals("Single[Left[minecraft:empty]]")) {
                    break;
                }

                if (nextPieceCandidate instanceof MowziePoolElement) {
                    MowziePoolElement mowziePoolElement = (MowziePoolElement) nextPieceCandidate;
                    if (!mowziePoolElement.checkCriteria(pieceState, this)) continue;
                }

                // Iterate through possible rotations of this element
                for (Rotation nextPieceRotation : Rotation.getShuffled(this.random)) {
                    List<StructureBlockInfo> nextPieceJigsawBlocks = nextPieceCandidate.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, nextPieceRotation, this.random);
                    BoundingBox nextPieceBoundingBoxOrigin = nextPieceCandidate.getBoundingBox(this.structureManager, BlockPos.ZERO, nextPieceRotation);
                    int largestSizeOfNextNextPiece;
                    // Village boundary adjustment
                    if (villageBoundaryAdjust && nextPieceBoundingBoxOrigin.getYSpan() <= 16) {
                        // Map each jigsaw block to the size of the largest next piece it can generate and largestSizeOfNextNextPiece becomes the largest of these
                        largestSizeOfNextNextPiece = nextPieceJigsawBlocks.stream().mapToInt((blockInfo) -> {
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
                        largestSizeOfNextNextPiece = 0;
                    }

                    for (StructureBlockInfo nextPieceJigsawBlock : nextPieceJigsawBlocks) {
                        boolean canAttach;
                        if (nextPieceCandidate instanceof MowziePoolElement && ((MowziePoolElement) nextPieceCandidate).twoWay()) {
                            canAttach = MowziePoolElement.canAttachTwoWays(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        } else {
                            canAttach = JigsawBlock.canAttach(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        }
                        if (canAttach) {
                            BlockPos nextPieceJigsawBlockPos = nextPieceJigsawBlock.pos;
                            BlockPos nextPiecePos = nextJigsawBlockPos.subtract(nextPieceJigsawBlockPos);
                            BoundingBox nextPieceBoundingBox = nextPieceCandidate.getBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                            int nextPieceMinY = nextPieceBoundingBox.minY();
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                nextPieceMinY -= ((MowziePoolElement) nextPieceCandidate).bounds.boundsMinOffset.getY();
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
                            if (largestSizeOfNextNextPiece > 0) {
                                int j2 = Math.max(largestSizeOfNextNextPiece + 1, nextPieceBoundingBoxPlaced.maxY() - nextPieceBoundingBoxPlaced.minY());
                                nextPieceBoundingBoxPlaced.encapsulate(new BlockPos(nextPieceBoundingBoxPlaced.minX(), nextPieceBoundingBoxPlaced.minY() + j2, nextPieceBoundingBoxPlaced.minZ()));
                            }

                            // Check height params
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                Optional<Integer> maxHeight = ((MowziePoolElement) nextPieceCandidate).maxHeight;
                                Optional<Integer> minHeight = ((MowziePoolElement) nextPieceCandidate).minHeight;
                                if (maxHeight.isPresent() || minHeight.isPresent()) {
                                    int freeHeight = this.chunkGenerator.getFirstFreeHeight(nextPieceBoundingBoxPlaced.minX() + nextPieceBoundingBoxPlaced.getXSpan() / 2, nextPieceBoundingBoxPlaced.minZ() + nextPieceBoundingBoxPlaced.getZSpan() / 2, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                    if (maxHeight.isPresent() && nextPieceMinY - freeHeight > maxHeight.get()) continue;
                                    if (minHeight.isPresent() && nextPieceMinY - freeHeight < minHeight.get()) continue;
                                }
                            }

                            if (nextPieceCandidate instanceof MowziePoolElement)
                                ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                            if (ignoreBounds || !Shapes.joinIsNotEmpty(pieceState.free.getValue(), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                // Special bounding boxes
                                if (nextPieceCandidate instanceof MowziePoolElement) {
                                    MowziePoolElement mowziePoolElement = (MowziePoolElement) nextPieceCandidate;

                                    Optional<String> needsOverlapBounds = mowziePoolElement.bounds.needsOverlapBounds;
                                    if (needsOverlapBounds.isPresent() && pieceState.specialBounds.getValue().containsKey(needsOverlapBounds.get())) {
                                        if (!Shapes.joinIsNotEmpty(pieceState.specialBounds.getValue().get(needsOverlapBounds.get()), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced).deflate(0.25D)), BooleanOp.AND)) {
                                            continue;
                                        }
                                    }
                                }

                                int pieceGroundLevelDelta = pieceState.piece.getGroundLevelDelta();
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

                                StructureBlockInfo nextJigsaw = null;
                                StructurePoolElement nextStructurepoolelement = poolelementstructurepiece.getElement();
                                BlockPos blockpos = poolelementstructurepiece.getPosition();
                                Rotation rotation = poolelementstructurepiece.getRotation();
                                for (StructureBlockInfo otherJigsaw : nextStructurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                                    if (jigsawMustConnect(otherJigsaw)) {
                                        if (otherJigsaw.pos.distSqr(destination) <= 1) {
                                            // Connected!
                                            this.destinations.remove(thisPieceJigsawBlock.pos);
                                            this.destinations.remove(destination);
                                            return new PieceSelection(pieceState, structurepoolelement, nextPieceCandidate, poolelementstructurepiece, thisPieceJigsawBlock, nextPieceJigsawBlock, null, nextPieceBoundingBoxPlaced, l2);
                                        }
                                        nextJigsaw = otherJigsaw;
                                        break;
//                                        if (canJigsawBlockFitNextPiece(otherJigsaw, pieceState.free)) {
//                                            nextJigsaw = otherJigsaw;
//                                            break;
//                                        }
                                    }
                                }
                                if (nextJigsaw == null) continue;

                                float distance = (float) nextJigsaw.pos.distSqr(destination);
                                PieceSelection thisPieceSelection = new PieceSelection(pieceState, structurepoolelement, nextPieceCandidate, poolelementstructurepiece, thisPieceJigsawBlock, nextPieceJigsawBlock, nextJigsaw, nextPieceBoundingBoxPlaced, l2);
                                if (distance < closestDist) {
                                    rejects.add(pieceSelection);
                                    pieceSelection = thisPieceSelection;
                                    closestDist = distance;
                                }
                                else rejects.add(thisPieceSelection);
                            }
                        }
                    }
                }
            }
            if (pieceSelection != null) {
                this.destinations.remove(pieceSelection.origJigsaw.pos);
                this.destinations.put(pieceSelection.nextJigsaw.pos, destination);
                this.destinations.put(destination, pieceSelection.nextJigsaw.pos);
            }
            return pieceSelection;
        }
    }*/
}
