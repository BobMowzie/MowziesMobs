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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockRotationData
{
	/** Valid rotation types. Each type is handled like vanilla block of this kind. */
	public enum Rotation {
		/** 0 - north/south, 1 - east/west */
		ANVIL,
		/**
		 * 0x8 flags top, for which 0x1 flags the hinge direction;
		 * Facings (for bottom only): 0 - west, 1 - north, 2 - east, 3 - south, 0x4 flags door as open
		 */
		DOOR,
		/** Facings: 0 - south, 1 - west, 2 - north, 3 - east; e.g. beds, pumpkins, tripwire hooks */
		GENERIC,
		/**
		 * Most containers (chests, furnaces) use this, as well as pistons, ladders, and other things.
		 * Facings: 2 - north, 3 - south, 4 - west, 5 - east [for ladders and signs, they are attached to that side of the block]
		 */
		PISTON_CONTAINER,
		QUARTZ,
		RAIL,
		REPEATER,
		/**
		 * Marks the direction in which text / banners show. Increments are in 1/16 of a
		 * full circle, starting from south and moving clockwise as if looking at a compass.
		 * E.g., 0 is south, 1 is south-southwest, 2 is southwest, all the way up to 16 which is south-southeast
		 */
		SIGNPOST,
		SKULL,
		/** Ascends to the: 0 - east, 1 - west, 2 - south, 3 - north; 0x4 flags inverted stairs */
		STAIRS,
		/**
		 * Attached to wall: 0 - south, 1 - north, 2 - east, 3 - west
		 * 0x4 flags trapdoor as open, 0x8 flags trapdoor as being in top half of block
		 */
		TRAPDOOR,
		/** Side of block to which vine is anchored: 1 - south, 2 - west, 4 - north, 8 - east */
		VINE,
		/**
		 * Facings: 1 - east, 2 - west, 3 - south, 4 - north
		 * (button only: 0 - down, 5 - up)
		 */
		WALL_MOUNTED,
		/**
		 * Facings: 1 - east, 2 - west, 3 - south, 4 - north,
		 * 5 - north/south ground, 6 - east/west ground,
		 * 7 - north/south ceiling, 0 - east/west ceiling
		 * 0x8 flags power
		 */
		LEVER,
		/**
		 * 0-3 - wood type, 0x4 - east/west, 0x8 north/south;
		 * if neither 0x4 nor 0x8 are set, wood is up/down; if both are set, wood is all bark
		 */
		WOOD
	}

	/** A mapping of block to rotation type for handling rotation. Allows custom block to be added. */
	private static final Map<Block, Rotation> blockRotationData = new HashMap<>();

	/**
	 * Returns the rotation type for the block given, or null if no type is registered
	 */
	public static final Rotation getBlockRotationType(Block block) {
		return blockRotationData.get(block);
	}

	/**
	 * Maps a block to a specified rotation type. Allows custom block to rotate with structure.
	 * @param block a valid block
	 * @param rotationType types predefined by enumerated type ROTATION
	 * @return false if a rotation type has already been specified for the given block
	 */
	public static final boolean registerCustomBlockRotation(Block block, Rotation rotationType) {
		return registerCustomBlockRotation(block, rotationType, false);
	}

	/**
	 * Maps a block to a specified rotation type. Allows custom block to rotate with structure.
	 * @param block a valid block
	 * @param rotationType types predefined by enumerated type ROTATION
	 * @param override if true, will override the previously set rotation data for specified block
	 * @return false if a rotation type has already been specified for the given block
	 */
	public static final boolean registerCustomBlockRotation(Block block, Rotation rotationType, boolean override) {
		if (blockRotationData.containsKey(block)) {
			Logger.getLogger("StructureGenAPI").warning("Block " + block + " already has a rotation type." + (override ? " Overriding previous data." : ""));
			if (override) {
				blockRotationData.remove(block);
			} else {
				return false;
			}
		}

		blockRotationData.put(block, rotationType);

		return true;
	}

	/** Set rotation data for vanilla block */
	static
	{
		blockRotationData.put(Blocks.anvil, Rotation.ANVIL);

		blockRotationData.put(Blocks.iron_door, Rotation.DOOR);
		blockRotationData.put(Blocks.wooden_door, Rotation.DOOR);

		blockRotationData.put(Blocks.bed, Rotation.GENERIC);
		blockRotationData.put(Blocks.cocoa, Rotation.GENERIC);
		blockRotationData.put(Blocks.fence_gate, Rotation.GENERIC);
		blockRotationData.put(Blocks.pumpkin, Rotation.GENERIC);
		blockRotationData.put(Blocks.lit_pumpkin, Rotation.GENERIC);
		blockRotationData.put(Blocks.end_portal_frame, Rotation.GENERIC);
		blockRotationData.put(Blocks.tripwire_hook, Rotation.GENERIC);

		blockRotationData.put(Blocks.chest, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.trapped_chest, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.dispenser, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.dropper, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.ender_chest, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.lit_furnace, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.furnace, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.hopper, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.ladder, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.wall_sign, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.piston, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.piston_extension, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.piston_head, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.sticky_piston, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.activator_rail, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.detector_rail, Rotation.PISTON_CONTAINER);
		blockRotationData.put(Blocks.golden_rail, Rotation.PISTON_CONTAINER);

		blockRotationData.put(Blocks.quartz_block, Rotation.QUARTZ);

		blockRotationData.put(Blocks.rail, Rotation.RAIL);

		blockRotationData.put(Blocks.powered_comparator, Rotation.REPEATER);
		blockRotationData.put(Blocks.unpowered_comparator, Rotation.REPEATER);
		blockRotationData.put(Blocks.powered_repeater, Rotation.REPEATER);
		blockRotationData.put(Blocks.unpowered_repeater, Rotation.REPEATER);

		blockRotationData.put(Blocks.standing_sign, Rotation.SIGNPOST);

		blockRotationData.put(Blocks.skull, Rotation.SKULL);

		// Mineral stairs:
		blockRotationData.put(Blocks.brick_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.nether_brick_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.quartz_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.sandstone_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.stone_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.stone_brick_stairs, Rotation.STAIRS);
		// Wooden stairs
		blockRotationData.put(Blocks.acacia_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.birch_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.dark_oak_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.jungle_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.oak_stairs, Rotation.STAIRS);
		blockRotationData.put(Blocks.spruce_stairs, Rotation.STAIRS);

		blockRotationData.put(Blocks.trapdoor, Rotation.TRAPDOOR);

		blockRotationData.put(Blocks.vine, Rotation.VINE);

		blockRotationData.put(Blocks.lever, Rotation.LEVER);

		blockRotationData.put(Blocks.stone_button, Rotation.WALL_MOUNTED);
		blockRotationData.put(Blocks.wooden_button, Rotation.WALL_MOUNTED);
		blockRotationData.put(Blocks.redstone_torch, Rotation.WALL_MOUNTED);
		blockRotationData.put(Blocks.unlit_redstone_torch, Rotation.WALL_MOUNTED);
		blockRotationData.put(Blocks.torch, Rotation.WALL_MOUNTED);

		blockRotationData.put(Blocks.log, Rotation.WOOD);
		blockRotationData.put(Blocks.log2, Rotation.WOOD);
	}
}
