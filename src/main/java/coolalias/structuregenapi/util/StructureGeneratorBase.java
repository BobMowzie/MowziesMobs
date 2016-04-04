/**
    Copyright (C) <2014> <coolAlias>

    This file is part of coolAlias' Structure Generation Tool; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package coolalias.structuregenapi.util;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class StructureGeneratorBase extends WorldGenerator
{
	/** Use this value to skip setting a block at an x,y,z coordinate for whatever reason. */
	public static final int SET_NO_BLOCK = Integer.MAX_VALUE;

	/** The directional values associated with player facing: */
	public static final int SOUTH = 0, WEST = 1, NORTH = 2, EAST = 3;

	/** Stores the direction this structure faces. Default is EAST.*/
	private int structureFacing = EAST, manualRotations = 0;

	/** Stores the player's facing for structure generation */
	private int facing;

	/** Stores amount to offset structure's location in the world, if any. */
	private int offsetX = 0, offsetY = 0, offsetZ = 0;

	/** When true all block will be set to air within the structure's area. */
	private boolean removeStructure = false;

	/** Stores the data for current layer. See StructureArray.java for information on how create a blockArray. */
	private int[][][][] blockArray;

	/** Stores a list of the structure to build, in 'layers' made up of individual blockArrays. */
	private final List<int[][][][]> blockArrayList = new LinkedList();

	/** Stores block that need to be set post-generation, such as torches */
	private final List<BlockData> postGenBlocks = new LinkedList();

	/**
	 * Basic constructor. Sets generator to notify other block of block it changes.
	 */
	public StructureGeneratorBase() {
		super(true);
	}

	/**
	 * Constructs the generator based on the player's facing and blockArray for the structure
	 */
	public StructureGeneratorBase(Entity entity, int[][][][] blocks) {
		this(entity, blocks, EAST, 0, 0, 0);
	}

	/**
	 * Constructs the generator with the player's facing, the blockArray for the structure
	 * and the structure's facing
	 */
	public StructureGeneratorBase(Entity entity, int[][][][] blocks, int structureFacing) {
		this(entity, blocks, structureFacing, 0, 0, 0);
	}

	/**
	 * Constructor for one line setting of all variables necessary to generate structure
	 * @param blocks The structure's blockArray
	 * @param structureFacing The direction in which the structure faces
	 * @param offX Amount to offset the structure's location along the east-west axis
	 * @param offY Amount to offset the structure's location along the vertical axis
	 * @param offZ Amount to offset the structure's location along the north-south axis
	 */
	public StructureGeneratorBase(Entity entity, int[][][][] blocks, int structureFacing, int offX, int offY, int offZ)
	{
		super(true);
		setPlayerFacing(entity);
		setBlockArray(blocks);
		setStructureFacing(structureFacing);
		setOffset(offX, offY, offZ);
	}

	/**
	 * Allows the use of block ids greater than 4095 as custom 'hooks' to trigger onCustomBlockAdded
	 * @param fakeID ID you use to identify your 'event'. Absolute value must be greater than 4095
	 * @param customData1 Custom data may be used to subtype events for given fakeID
	 * Returns the real id of the block to spawn in the world; must be <= 4095
	 */
	public abstract int getRealBlockID(int fakeID, int customData1);

	/**
	 * A custom 'hook' to allow setting of tile entities, spawning entities, etc.
	 * @param fakeID The custom identifier used to distinguish between types
	 * @param customData1 Custom data which can be used to subtype events for given fakeID
	 * @param customData2 Additional custom data
	 */
	public abstract void onCustomBlockAdded(World world, int x, int y, int z, int fakeID, int customData1, int customData2);

	/**
	 * Returns facing value as set from player, or 0 if no facing was specified
	 */
	public final int getPlayerFacing() {
		return facing;
	}

	/**
	 * Sets the direction in which the player is facing. The structure will be generated
	 * opposite of player view (so player will be looking at front when finished)
	 */
	public final void setPlayerFacing(Entity entity) {
		if (entity == null) LogHelper.warning("Null Pointer Exception! Cannot set facing from a null entity.");
		else facing = MathHelper.floor_double((double)((entity.rotationYaw * 4F) / 360f) + 0.5D) &3;
	}

	/**
	 * Sets the default direction the structure is facing. This side will always face the player
	 * unless you manually rotate the structure with the rotateStructureFacing() method.
	 */
	public final void setStructureFacing(int facing) {
		structureFacing = facing % 4;
	}

	/**
	 * This will manually rotate the structure's facing 90 degrees clockwise.
	 * Note that a different side will now face the player when generated.
	 */
	public final void rotateStructureFacing() {
		structureFacing = ++structureFacing % 4;
		manualRotations = ++manualRotations % 4;
	}

	/**
	 * Manually rotates the structure's facing a specified number of times.
	 */
	public final void rotateStructureFacing(int rotations) {
		structureFacing = (structureFacing + rotations) % 4;
		manualRotations = (manualRotations + rotations) % 4;
	}

	/**
	 * Returns a string describing current facing of structure
	 */
	public final String currentStructureFacing() {
		return (structureFacing == EAST ? "East" : structureFacing == WEST ? "West" : structureFacing == NORTH ? "North" : "South");
	}

	/**
	 * Adds a block array 'layer' to the list to be generated
	 */
	public final void addBlockArray(int blocks[][][][])
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			blockArrayList.add(blocks);
			if (blockArray == null)
				blockArray = blocks;
		}
	}

	/**
	 * Overwrites current list with the provided blockArray
	 */
	public final void setBlockArray(int blocks[][][][])
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			blockArrayList.clear();
			blockArrayList.add(blocks);
			blockArray = blocks;
		}
	}

	/**
	 * Adds all elements contained in the parameter list to the structure
	 */
	public final void addBlockArrayList(List<int[][][][]> list)
	{
		blockArrayList.addAll(list);

		if (blockArray == null && list.size() > 0)
			blockArray = list.get(0);
	}

	/**
	 * Overwrites current blockArrayList with list provided
	 */
	public final void setBlockArrayList(List<int[][][][]> list)
	{
		blockArrayList.clear();
		blockArrayList.addAll(list);
		blockArray = (list.size() > 0 ? list.get(0) : null);
	}

	/**
	 * Overwrites current Structure information with passed in structure
	 * Sets structure facing to the default facing of the structure
	 * Does NOT set offset for the structure
	 */
	public final void setStructure(Structure structure) {
		if (structure != null) {
			reset();
			setBlockArrayList(structure.blockArrayList());
			setStructureFacing(structure.getFacing());
		} else {
			LogHelper.severe("NULL Structure cannot be set!");
		}
	}

	/**
	 * Overwrites current Structure information with passed in structure and rotates it
	 * a number of times starting from its default facing
	 */
	public final void setStructureWithRotation(Structure structure, int rotations) {
		setStructure(structure);
		manualRotations = 0;
		for (int i = 0; i < rotations % 4; ++i)
			rotateStructureFacing();
	}

	/**
	 * Returns lowest structure layer's width along the x axis or 0 if no structure has been added
	 */
	public final int getWidthX() {
		return blockArray != null ? blockArray[0].length : 0;
	}

	/**
	 * Returns lowest structure layer's width along the z axis or 0 if no structure has been set
	 */
	public final int getWidthZ() {
		return blockArray != null ? blockArray[0][0].length : 0;
	}

	/**
	 * Returns current structure layer's height or 0 if no structure has been set
	 */
	public final int getHeight() {
		return blockArray != null ? blockArray.length : 0;
	}

	/**
	 * Returns the original facing for the structure
	 */
	public final int getOriginalFacing() {
		return (structureFacing + (4 - manualRotations)) % 4;
	}

	/**
	 * Returns true if the structure has been rotated onto the opposite axis from original
	 */
	public final boolean isOppositeAxis() {
		return getOriginalFacing() % 2 != structureFacing % 2;
	}

	/**
	 * Sets the amount by which to offset the structure's generated location in the world.
	 * For advanced users only. Recommended to use setDefaultOffset() methods instead.
	 */
	public final void setOffset(int offX, int offY, int offZ) {
		offsetX = offX;
		offsetY = offY;
		offsetZ = offZ;
	}

	/**
	 * Call this only after setting the blockArray and immediately before generation.
	 * Sets a default offset amount that will keep the entire structure's boundaries
	 * from overlapping with the position spawned at, so it will never spawn on the player.
	 */
	public final void setDefaultOffset() {
		setDefaultOffset(0,0,0);
	}

	/**
	 * Sets offsets such that the structure always generates in front of the player,
	 * regardless of structure facing, offset by parameters x/y/z.
	 * Only call this method immediately before generation.
	 * NOTE: If your structures y=0 layer has an area smaller than another part of the structure,
	 * setting default offset will not work correctly.
	 * @param x Positive value spawns structure further away from player, negative closer to or behind
	 * @param z Positive value spawns structure more to the right, negative to the left
	 */
	public final void setDefaultOffset(int x, int y, int z) {
		/** flagNS is true if structure's facing is north or south */
		boolean flagNS = getOriginalFacing() % 2 == 0;
		int length = flagNS ? getWidthX() : getWidthZ();
		int adj1 = length - (flagNS ? getWidthZ() : getWidthX());

		/** Flag1 tags structures of certain dimension specifications for adjustment */
		boolean flag1 = (flagNS ? (getWidthX() % 2 == 0 && adj1 % 2 == 1) || (getWidthX() % 2 == 1 && adj1 % 2 == -1)
				: (getWidthX() % 2 == 0 && adj1 % 2 == -1) || (getWidthX() % 2 == 1 && adj1 % 2 == 1));

		if (flag1 && !flagNS) {
			adj1 = -adj1;
		}

		int adj2 = (length+1) % 2;
		int adj3 = adj1 % 2;
		int adj4 = adj1 / 2 + adj3;

		switch(getOriginalFacing()) {
		case 0: // SOUTH
			offsetZ = x + length / 2 - (manualRotations == 0 ? adj1 / 2 + (adj3 == 0 ? 0 : adj1 < 0 && flag1 ? adj3 : adj2) : manualRotations == 1 ? (adj3 == 0 ? adj2 : adj1 > 0 && flag1 ? adj3 : 0) : manualRotations == 2 ? adj1 / 2 + (adj3 == 0 || flag1 ? adj2 : adj3) : 0);
			offsetX = -z + (manualRotations == 0 ? adj2 + (adj3 > 0 && !flag1 ? adj4 : 0) : manualRotations == 1 ? (adj3 == 0 ? adj2 : flag1 ? (adj3 < 0 ? -adj3 : 0) : adj3) : manualRotations == 2 ? (adj3 > 0 && !flag1 ? adj4 : 0) : 0);
			break;
		case 1: // WEST
			offsetX = x + length / 2 - (manualRotations == 0 ? (flag1 ? -adj4 : adj1 / 2) : manualRotations == 2 ? (flag1 ? (adj1 > 0 ? -adj1 / 2 : -adj4) : adj1 / 2 + (adj3 == 0 ? adj2 : 0)) : manualRotations == 3 ? (adj3 == 0 || flag1 ? adj2 : -adj3) : 0);
			offsetZ = z + (manualRotations == 1 ? (adj3 < 0 && !flag1 ? adj4 : adj3 > 0 && flag1 ? (adj1 > 1 ? -adj1 / 2 : -adj4) : 0) + (adj3 == 0 ? -adj2 : 0) : manualRotations == 2 ? (adj3 == 0 || flag1 ? -adj2 : adj3) : manualRotations == 3 ? (adj3 < 0 && !flag1 ? adj4 : 0) : 0);
			break;
		case 2: // NORTH
			offsetZ = -x - length / 2 + (manualRotations == 0 ? adj1 / 2 + (adj3 == 0 || flag1 ? adj2 : adj3) : manualRotations == 2 ? (flag1 ? adj4 : adj1 / 2) : manualRotations == 3 ? (adj3 == 0 || flag1 ? adj2 : 0) : 0);
			offsetX = z - (manualRotations == 0 ? (adj3 > 0 ? adj3 - adj2 : 0) : manualRotations == 2 ? (adj3 > 0 ? adj3 : adj2) : manualRotations == 3 ? (adj3 > 0 ? adj3 - adj2 : adj3 < 0 ? -adj3 : adj2) : 0);
			break;
		case 3: // EAST
			offsetX = -x - length / 2 + (manualRotations == 0 ? adj1 / 2 + (adj3 == 0 ? adj2 : flag1 ? -adj1 + (adj1 > 0 ? adj3 : 0) : 0) : manualRotations == 1 ? (adj3 == 0 || flag1 ? adj2 : -adj3) : manualRotations == 2 ? (flag1 ? -adj4 : adj1 / 2) : 0);
			offsetZ = -z - (manualRotations == 0 ? (adj3 == 0 || flag1 ? -adj2 : adj3) : manualRotations == 1 ? (adj3 != 0 && !flag1 ? adj4 : 0) : manualRotations == 3 ? (adj3 < 0 && !flag1 ? adj4 : adj3 > 0 && flag1 ? -adj4 : 0) + (adj3 == 0 ? -adj2 : flag1 && adj1 > 1 ? adj3 : 0) : 0);
			break;
		}

		offsetY = 1 + y;
	}

	/**
	 * Toggles between generate and remove structure setting. Returns value for ease of reference.
	 */
	public final boolean toggleRemoveStructure() {
		removeStructure = !removeStructure;
		return removeStructure;
	}

	/**
	 * Sets remove structure to true or false
	 */
	public final void setRemoveStructure(boolean value) {
		removeStructure = value;
	}

	/**
	 * Returns true if the generator has enough information to generate a structure
	 */
	public final boolean canGenerate() {
		return blockArrayList.size() > 0 || blockArray != null;
	}

	/**
	 * Generates each consecutive blockArray in the current list at location posX, posZ,
	 * with posY incremented by the height of each previously generated blockArray.
	 */
	@Override
	public final boolean generate(World world, Random random, int posX, int posY, int posZ) {
		if (world.isRemote || !canGenerate()) {
			return false;
		}

		boolean generated = true;
		int rotations = ((isOppositeAxis() ? structureFacing + 2 : structureFacing) + facing) % 4;

		setOffsetFromRotation();

		for (int[][][][] blocks : blockArrayList) {
			if (!generated) { break; }
			this.blockArray = blocks;
			generated = generateLayer(world, random, posX, posY, posZ, rotations);
			offsetY += blocks.length;
		}

		if (generated)
			doPostGenProcessing(world);

		reset();

		return generated;
	}

	/**
	 * Custom 'generate' method that generates a single 'layer' from the list of blockArrays
	 */
	private final boolean generateLayer(World world, Random random, int posX, int posY, int posZ, int rotations) {
		int centerX = blockArray[0].length / 2, centerZ = blockArray[0][0].length / 2;

		for (int y = (removeStructure ? blockArray.length - 1 : 0); (removeStructure ? y >= 0 : y < blockArray.length); y = (removeStructure ? --y : ++y)) {
			for (int x = 0; x < blockArray[y].length; ++x) {
				for (int z = 0; z < blockArray[y][x].length; ++z) {
					if (blockArray[y][x][z].length == 0 || blockArray[y][x][z][0] == SET_NO_BLOCK) {
						continue;
					}

					int rotX = posX, rotZ = posZ, rotY = posY + y + offsetY;
					switch(rotations) {
					case 0: // Player is looking at the front of the default structure
						rotX += x - centerX + offsetX;
						rotZ += z - centerZ + offsetZ;
						break;
					case 1: // Rotate structure 90 degrees clockwise
						rotX += -(z - centerZ + offsetZ);
						rotZ += x - centerX + offsetX;
						break;
					case 2: // Rotate structure 180 degrees
						rotX += -(x - centerX + offsetX);
						rotZ += -(z - centerZ + offsetZ);
						break;
					case 3: // Rotate structure 270 degrees clockwise
						rotX += z - centerZ + offsetZ;
						rotZ += -(x - centerX + offsetX);
						break;
					default:
						LogHelper.warning("Error computing number of rotations.");
						break;
					}

					int customData1 = (blockArray[y][x][z].length > 2 ? blockArray[y][x][z][2] : 0);
					int fakeID = blockArray[y][x][z][0];
					int realID = (Math.abs(fakeID) > 4095 ? getRealBlockID(fakeID, customData1) : fakeID);

					if (removeStructure) {
						if (!removeBlockAt(world, fakeID, realID, rotX, rotY, rotZ, rotations))
							return false;
					} else {
						if (Math.abs(realID) > 4095) {
							LogHelper.warning("Invalid block ID. Initial ID: " + fakeID + ", returned id from getRealID: " + realID);
							continue;
						}

						int customData2 = (blockArray[y][x][z].length > 3 ? blockArray[y][x][z][3] : 0);
						int meta = (blockArray[y][x][z].length > 1 ? blockArray[y][x][z][1] : 0);

						setBlockAt(world, fakeID, realID, meta, customData1, customData2, rotX, rotY, rotZ);
					}
				}
			}
		}

		return true;
	}

	/**
	 * Handles setting block with fakeID at x/y/z in world.
	 * Arguments should be those retrieved from blockArray
	 */
	private final void setBlockAt(World world, int fakeID, int realID, int meta, int customData1, int customData2, int x, int y, int z) {
		Block block = Block.getBlockById(realID);
		if (realID >= 0 || world.isAirBlock(x, y, z) || !world.getBlock(x, y, z).getMaterial().blocksMovement()) {
			if (BlockRotationData.getBlockRotationType(block) != null) {
				int rotations = ((isOppositeAxis() ? structureFacing + 2 : structureFacing) + facing) % 4;
				meta = GenHelper.getMetadata(rotations, block, meta);
			}

			if (BlockRotationData.getBlockRotationType(block) != null &&
					(BlockRotationData.getBlockRotationType(block) == BlockRotationData.Rotation.WALL_MOUNTED ||
					BlockRotationData.getBlockRotationType(block) == BlockRotationData.Rotation.LEVER))
			{
				LogHelper.fine("Block " + block + " requires post-processing. Adding to list. Meta = " + meta);
				postGenBlocks.add(new BlockData(x, y, z, fakeID, meta, customData1, customData2));
			} else {
				world.setBlock(x, y, z, block, meta, 2);
				if (BlockRotationData.getBlockRotationType(block) != null) {
					GenHelper.setMetadata(world, x, y, z, meta);
				}

				if (Math.abs(fakeID) > 4095) {
					onCustomBlockAdded(world, x, y, z, fakeID, customData1, customData2);
				}
			}
		}
	}

	/**
	 * Removes block at x/y/z and cleans up any items/entities that may be left behind
	 * Returns false if realID is mismatched with world's blockID at x/y/z
	 */
	private final boolean removeBlockAt(World world, int fakeID, int realID, int x, int y, int z, int rotations) {
		Block realBlock = Block.getBlockById(Math.abs(realID));
		Block worldBlock = world.getBlock(x, y, z);

		if (realBlock == null || worldBlock == null || (realID < 0 && worldBlock != realBlock)) {
			return true;
		} else if (realBlock == worldBlock || GenHelper.materialsMatch(realBlock, worldBlock)) {
			world.setBlockToAir(x, y, z);
			List <Entity> list = world.getEntitiesWithinAABB(Entity.class, GenHelper.getHangingEntityAxisAligned(x, y, z, Direction.directionToFacing[rotations]).expand(1.0F, 1.0F, 1.0F));
			for (Entity entity : list) {
				if (!(entity instanceof EntityPlayer)) {
					entity.setDead();
				}
			}
		} else {
			LogHelper.info("Incorrect location for structure removal, aborting. Last block id checked: world " + worldBlock + ", real " + realID + ", fake " + fakeID);
			return false;
		}

		return true;
	}

	/**
	 * Sets block flagged for post-GENERATOR processing; triggers onCustomBlockAdded method where applicable
	 */
	private final void doPostGenProcessing(World world) {
		int fakeID, realID;

		for (BlockData block : postGenBlocks) {
			fakeID = block.getBlockID();
			realID = (Math.abs(fakeID) > 4095 ? getRealBlockID(fakeID, block.getCustomData1()) : fakeID);
			if (Math.abs(realID) > 4095) {
				LogHelper.warning("Invalid block ID. Initial ID: " + fakeID + ", returned id from getRealID: " + realID);
				continue;
			}

			LogHelper.fine("Post-GENERATOR processing for initial ID: " + fakeID + ", returned id from getRealID: " + realID);
			Block realBlock = Block.getBlockById(Math.abs(realID));
			if (realID >= 0 || world.isAirBlock(block.getPosX(), block.getPosY(), block.getPosZ())
					|| !world.getBlock(block.getPosX(), block.getPosY(), block.getPosZ()).getMaterial().blocksMovement())
			{
				world.setBlock(block.getPosX(), block.getPosY(), block.getPosZ(), realBlock, block.getMetaData(), 3);

				if (world.getBlockMetadata(block.getPosX(), block.getPosY(), block.getPosZ()) != block.getMetaData()) {
					LogHelper.warning("Mismatched metadata. Meta from world: " + world.getBlockMetadata(block.getPosX(), block.getPosY(), block.getPosZ()) + ", original: " + block.getMetaData());
				}

				if (Math.abs(fakeID) > 4095) {
					onCustomBlockAdded(world, block.getPosX(), block.getPosY(), block.getPosZ(), fakeID, block.getCustomData1(), block.getCustomData2());
				}
			}
		}

		postGenBlocks.clear();
	}

	/**
	 * Adjusts offsetX and offsetZ amounts to compensate for manual rotation
	 */
	private final void setOffsetFromRotation() {
		int x, z;
		for (int i = 0; i < manualRotations; ++i) {
			x = -offsetZ;
			z = offsetX;
			offsetX = x;
			offsetZ = z;
		}
	}

	/**
	 * Clears blockArray, blockArrayList and offsets for next structure
	 */
	private final void reset() {
		blockArrayList.clear();
		blockArray = null;
		offsetX = offsetY = offsetZ = 0;
	}
}
