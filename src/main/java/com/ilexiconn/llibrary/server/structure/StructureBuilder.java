package com.ilexiconn.llibrary.server.structure;

import com.ilexiconn.llibrary.server.structure.rule.FixedRule;
import com.ilexiconn.llibrary.server.structure.rule.RepeatRule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Rotation;
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
    private Direction front = Direction.EAST;
    private Direction top = Direction.UP;

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
        pooledPos.close();
    }

    @Override
    public StructureBuilder rotate(Direction front, Direction top) {
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
            boolean inverted = top == Direction.DOWN;
            for (BlockPos coords : oldComp.blocks.keySet()) {
                BlockPos newCoords = transform(coords, frontVec, topVec, perpVec);
                BlockList newList = oldComp.blocks.get(coords).copy();
                BlockState[] states = newList.getStates();
                for (int i = 0; i < states.length; i++) {
                    BlockState state = states[i];
                    Rotation rot = Rotation.NONE;
                    if (front.rotateYCCW() == Direction.NORTH) rot = Rotation.CLOCKWISE_90;
                    else if (front.getOpposite() == Direction.NORTH) rot = Rotation.CLOCKWISE_180;
                    else if (front.rotateY() == Direction.NORTH) rot = Rotation.COUNTERCLOCKWISE_90;
                    states[i] = state.rotate(rot);
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

    private static Direction transform(Direction facing, Vec3i vec3i, Vec3i vec3i1, Vec3i vec3i2) {
        BlockPos vec = transform(facing.getDirectionVec(), vec3i, vec3i1, vec3i2);
        return Direction.getFacingFromVector(vec.getX(), vec.getY(), vec.getZ());
    }

    @Override
    public StructureBuilder rotateTowards(Direction facing) {
        if (facing.getAxis() == Direction.Axis.Y) {
            throw new IllegalArgumentException("Must be horizontal facing: " + facing);
        }
        int idx = facing.getHorizontalIndex() - (this.front.getAxis() == Axis.Y ? this.top.getHorizontalIndex() : this.front.getHorizontalIndex()) - 1;
        idx = (idx % 4 + 4) % 4;
        return this.rotate(Direction.byHorizontalIndex(idx), Direction.UP);
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

    public StructureBuilder setOrientation(Direction front, Direction top) {
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

    public StructureBuilder setBlock(int x, int y, int z, BlockState block) {
        return this.setBlock(x, y, z, new BlockList(block));
    }

    public StructureBuilder setBlock(int x, int y, int z, BlockList list) {
        this.blocks.put(new BlockPos(x + this.offsetX, y + this.offsetY, z + this.offsetZ), list);
        return this;
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, BlockState block) {
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

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, BlockState block) {
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

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, BlockState state) {
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
