package com.ilexiconn.llibrary.server.structure;

import net.ilexiconn.llibrary.server.structure.rule.FixedRule;
import net.ilexiconn.llibrary.server.structure.rule.RepeatRule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class StructureBuilder extends StructureGenerator {
    private final HashMap<BlockPos, BlockList> blocks = new HashMap<>();
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private final List<RepeatRule> repeats = new ArrayList<>();
    private final List<ComponentInfo> components = new ArrayList<>();
    private ComponentInfo currentLayer;
    private EnumFacing front = EnumFacing.EAST;
    private EnumFacing top = EnumFacing.UP;

    @Override
    public void generate(World world, BlockPos pos, Random random) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (ComponentInfo layer : this.components) {
            for (RepeatRule rule : layer.repeats) {
                rule.reset(world, random, mutablePos);
            }
        }
        BlockPos.PooledMutableBlockPos pooledPos = BlockPos.PooledMutableBlockPos.retain();
        for (ComponentInfo layer : this.components) {
            mutablePos.setPos(pos);
            for (RepeatRule rule : layer.repeats) {
                rule.init(world, random, mutablePos);
                while (rule.continueRepeating(world, random, mutablePos)) {
                    for (Map.Entry<BlockPos, BlockList> e : layer.blocks.entrySet()) {
                        BlockPos coords = e.getKey();
                        int blockX = coords.getX() + mutablePos.getX();
                        int blockY = coords.getY() + mutablePos.getY();
                        int blockZ = coords.getZ() + mutablePos.getZ();
                        world.setBlockState(pooledPos.setPos(blockX, blockY, blockZ).toImmutable(), e.getValue().getRandom(random));
                    }
                    rule.repeat(world, random, mutablePos);
                }
            }
        }
        pooledPos.release();
    }

    @Override
    public StructureBuilder rotate(EnumFacing front, EnumFacing top) {
        if (front == top || front.getOpposite() == top) {
            throw new IllegalArgumentException("Invalid rotation: " + front + " & " + top);
        }
        Vec3i frontVec = new Vec3i(front.getXOffset(), front.getYOffset(), front.getZOffset());
        Vec3i topVec = new Vec3i(-top.getXOffset(), -top.getYOffset(), -top.getZOffset());
        Vec3i perpVec = topVec.crossProduct(frontVec);
        StructureBuilder copy = new StructureBuilder();
        copy.front = transform(this.front, frontVec, topVec, perpVec);
        copy.top = transform(this.top, frontVec, topVec, perpVec);
        for (ComponentInfo oldComp : this.components) {
            ComponentInfo newComp = new ComponentInfo();
            newComp.repeats.addAll(oldComp.repeats);
            newComp.front = transform(oldComp.front, frontVec, topVec, perpVec);
            newComp.top = transform(oldComp.top, frontVec, topVec, perpVec);
            HashMap<BlockPos, BlockList> blocks = newComp.blocks;
            boolean inverted = top == EnumFacing.DOWN;
            for (BlockPos coords : oldComp.blocks.keySet()) {
                BlockPos newCoords = transform(coords, frontVec, topVec, perpVec);
                BlockList newList = oldComp.blocks.get(coords).copy();
                IBlockState[] states = newList.getStates();
                for (int i = 0; i < states.length; i++) {
                    IBlockState state = states[i];
                    if (state.getBlock() instanceof BlockStairs) {
                        EnumFacing facing = transform(state.getValue(BlockStairs.FACING), frontVec, topVec, perpVec);
                        EnumFacing perp = transform(EnumFacing.UP, frontVec, topVec, perpVec);
                        if (facing.getAxis() == Axis.Y) {
                            if (state.getValue(BlockStairs.HALF) == EnumHalf.BOTTOM) {
                                perp = perp.getOpposite();
                                if (facing == EnumFacing.UP) {
                                    state = state.cycleProperty(BlockStairs.HALF);
                                }
                            } else if (facing == EnumFacing.DOWN) {
                                state = state.cycleProperty(BlockStairs.HALF);
                            }
                            state = state.withProperty(BlockStairs.FACING, perp);
                        } else {
                            state = state.withProperty(BlockStairs.FACING, facing);
                        }
                        if (inverted) {
                            state = state.cycleProperty(BlockStairs.HALF);
                        }
                    } else if (state.getBlock() instanceof BlockSlab) {
                        if (inverted) {
                            state = state.cycleProperty(BlockSlab.HALF);
                        }
                    } else if (state.getBlock() instanceof BlockVine) {
                        EnumFacing facing = transform(state.getValue(BlockVine.NORTH) ? EnumFacing.NORTH : state.getValue(BlockVine.EAST) ? EnumFacing.EAST : state.getValue(BlockVine.SOUTH) ? EnumFacing.SOUTH : EnumFacing.WEST, frontVec, topVec, perpVec);
                        if (inverted) {
                            facing = facing.getOpposite();
                        }
                        state = state.withProperty(BlockVine.NORTH, facing == EnumFacing.NORTH);
                        state = state.withProperty(BlockVine.EAST, facing == EnumFacing.EAST);
                        state = state.withProperty(BlockVine.SOUTH, facing == EnumFacing.SOUTH);
                        state = state.withProperty(BlockVine.WEST, facing == EnumFacing.WEST);
                    } else if (state.getBlock() instanceof BlockLog) {
                        EnumAxis axis = state.getValue(BlockLog.LOG_AXIS);
                        EnumFacing facing = axis == EnumAxis.X ? EnumFacing.EAST : axis == EnumAxis.Y ? EnumFacing.UP : EnumFacing.SOUTH;
                        EnumFacing transformed = transform(facing, frontVec, topVec, perpVec);
                        state = state.withProperty(BlockLog.LOG_AXIS, EnumAxis.fromFacingAxis(transformed.getAxis()));
                    } else {
                        for (IProperty prop : state.getPropertyKeys()) {
                            if (prop instanceof PropertyDirection) {
                                PropertyDirection propDir = (PropertyDirection) prop;
                                EnumFacing facing = state.getValue(propDir);
                                EnumFacing newFacing = transform(facing, frontVec, topVec, perpVec);
                                if (propDir.getAllowedValues().contains(newFacing)) {
                                    state = state.withProperty(propDir, newFacing);
                                }
                            }
                        }
                    }
                    states[i] = state;
                }
                blocks.put(newCoords, newList);
            }
            copy.components.add(newComp);
        }
        return copy;
    }

    private static BlockPos transform(Vec3i pos, Vec3i vec3i, Vec3i vec3i1, Vec3i vec3i2) {
        return new BlockPos(
                vec3i1.getX() * -pos.getY() + vec3i2.getX() * pos.getZ() + vec3i.getX() * pos.getX(),
                vec3i1.getY() * -pos.getY() + vec3i2.getY() * pos.getZ() + vec3i.getY() * pos.getX(),
                vec3i1.getZ() * -pos.getY() + vec3i2.getZ() * pos.getZ() + vec3i.getZ() * pos.getX()
        );
    }

    private static EnumFacing transform(EnumFacing facing, Vec3i vec3i, Vec3i vec3i1, Vec3i vec3i2) {
        BlockPos vec = transform(facing.getDirectionVec(), vec3i, vec3i1, vec3i2);
        return EnumFacing.getFacingFromVector(vec.getX(), vec.getY(), vec.getZ());
    }

    @Override
    public StructureBuilder rotateTowards(EnumFacing facing) {
        if (facing.getAxis() == Axis.Y) {
            throw new IllegalArgumentException("Must be horizontal facing: " + facing);
        }
        int idx = facing.getHorizontalIndex() - (this.front.getAxis() == Axis.Y ? this.top.getHorizontalIndex() : this.front.getHorizontalIndex()) - 1;
        idx = (idx % EnumFacing.HORIZONTALS.length + EnumFacing.HORIZONTALS.length) % EnumFacing.HORIZONTALS.length;
        return this.rotate(EnumFacing.HORIZONTALS[idx], EnumFacing.UP);
    }

    public StructureBuilder startComponent() {
        this.currentLayer = new ComponentInfo();
        this.blocks.clear();
        this.repeats.clear();
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        return this;
    }

    public StructureBuilder setOrientation(EnumFacing front, EnumFacing top) {
        this.currentLayer.front = front;
        this.currentLayer.top = top;
        return this;
    }

    public StructureBuilder endComponent() {
        this.currentLayer.blocks.putAll(this.blocks);
        this.currentLayer.repeats.addAll(this.repeats);
        this.components.add(this.currentLayer);
        return this;
    }

    public StructureBuilder setOffset(int x, int y, int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    public StructureBuilder translate(int x, int y, int z) {
        this.offsetX += x;
        this.offsetY += y;
        this.offsetZ += z;
        return this;
    }

    public StructureBuilder setBlock(int x, int y, int z, Block block) {
        return this.setBlock(x, y, z, block.getDefaultState());
    }

    public StructureBuilder setBlock(int x, int y, int z, IBlockState block) {
        return this.setBlock(x, y, z, new BlockList(block));
    }

    public StructureBuilder setBlock(int x, int y, int z, BlockList list) {
        this.blocks.put(new BlockPos(x + this.offsetX, y + this.offsetY, z + this.offsetZ), list);
        return this;
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, IBlockState block) {
        return this.cube(startX, startY, startZ, width, height, depth, new BlockList(block));
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, BlockList list) {
        if (depth > 1) {
            this.fillCube(startX, startY, startZ, width, height, 1, list);
            this.fillCube(startX, startY, startZ + depth - 1, width, height, 1, list);
        }

        if (width > 1) {
            this.fillCube(startX, startY, startZ, 1, height, depth, list);
            this.fillCube(startX + width - 1, startY, startZ, 1, height, depth, list);
        }

        if (height > 1) {
            this.fillCube(startX, startY, startZ, width, 1, depth, list);
            this.fillCube(startX, startY + height - 1, startZ, width, 1, depth, list);
        }
        return this;
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, IBlockState block) {
        return this.fillCube(startX, startY, startZ, width, height, depth, new BlockList(block));
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, BlockList list) {
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                for (int z = startZ; z < startZ + depth; z++) {
                    this.setBlock(x, y, z, list);
                }
            }
        }
        return this;
    }

    public StructureBuilder repeat(int spacingX, int spacingY, int spacingZ, int times) {
        return this.repeat(spacingX, spacingY, spacingZ, new FixedRule(times));
    }

    public StructureBuilder repeat(int spacingX, int spacingY, int spacingZ, RepeatRule repeatRule) {
        repeatRule.setSpacing(spacingX, spacingY, spacingZ);
        return this.addBakedRepeatRule(repeatRule);
    }

    public StructureBuilder addBakedRepeatRule(RepeatRule repeatRule) {
        this.repeats.add(repeatRule);
        return this;
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return this.cube(startX, startY, startZ, width, height, depth, block.getDefaultState());
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return this.fillCube(startX, startY, startZ, width, height, depth, block.getDefaultState());
    }

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return this.wireCube(startX, startY, startZ, width, height, depth, block.getDefaultState());
    }

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, IBlockState state) {
        return this.wireCube(startX, startY, startZ, width, height, depth, new BlockList(state));
    }

    private StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, BlockList list) {
        this.fillCube(startX, startY, startZ, 1, height, 1, list);
        this.fillCube(startX + width - 1, startY, startZ, 1, height, 1, list);
        this.fillCube(startX + width - 1, startY, startZ + depth - 1, 1, height, 1, list);
        this.fillCube(startX, startY, startZ + depth - 1, 1, height, 1, list);

        this.fillCube(startX, startY, startZ, width, 1, 1, list);
        this.fillCube(startX, startY + height, startZ, width, 1, 1, list);
        this.fillCube(startX, startY, startZ + depth - 1, width, 1, 1, list);
        this.fillCube(startX, startY + height, startZ + depth - 1, width, 1, 1, list);

        this.fillCube(startX, startY, startZ, 1, 1, depth, list);
        this.fillCube(startX, startY + height, startZ, 1, 1, depth, list);
        this.fillCube(startX + width - 1, startY, startZ, 1, 1, depth, list);
        this.fillCube(startX + width - 1, startY + height, startZ, 1, 1, depth, list);
        return this;
    }
}
