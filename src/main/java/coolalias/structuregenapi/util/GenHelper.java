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

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * 
 * Utility methods useful for any sort of structure generation
 *
 */
public class GenHelper
{
	/**
	 * Use this method to add an ItemStack to the first available slot in a TileEntity that
	 * implements IInventory (and thus, by extension, ISidedInventory)
	 * @return true if entire itemstack was added
	 */
	public static final boolean addItemToTileInventory(World world, ItemStack itemstack, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile == null || !(tile instanceof IInventory)) {
			LogHelper.warning("Tile Entity at " + x + "/" + y + "/" + z + " is " + (tile != null ? "not an IInventory" : "null"));
			return false;
		}

		if (itemstack.stackSize < 1) {
			LogHelper.warning("Trying to add ItemStack of size 0 to Tile Inventory");
			return false;
		}

		IInventory inventory = (IInventory) tile;
		int remaining = itemstack.stackSize;

		for (int i = 0; i < inventory.getSizeInventory() && remaining > 0; ++i) {
			ItemStack slotstack = inventory.getStackInSlot(i);
			if (slotstack == null && inventory.isItemValidForSlot(i, itemstack)) {
				remaining -= inventory.getInventoryStackLimit();
				itemstack.stackSize = (remaining > 0 ? inventory.getInventoryStackLimit() : itemstack.stackSize);
				inventory.setInventorySlotContents(i, itemstack);
				inventory.markDirty();
			} else if (slotstack != null && itemstack.isStackable() && inventory.isItemValidForSlot(i, itemstack)) {
				if (slotstack.getItem() == itemstack.getItem()  && (!itemstack.getHasSubtypes() || 
						itemstack.getItemDamage() == slotstack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack, slotstack))
				{
					int l = slotstack.stackSize + remaining;

					if (l <= itemstack.getMaxStackSize() && l <= inventory.getInventoryStackLimit()) {
						remaining = 0;
						slotstack.stackSize = l;
						inventory.markDirty();
					} else if (slotstack.stackSize < itemstack.getMaxStackSize() && itemstack.getMaxStackSize() <= inventory.getInventoryStackLimit()) {
						remaining -= itemstack.getMaxStackSize() - slotstack.stackSize;
						slotstack.stackSize = itemstack.getMaxStackSize();
						inventory.markDirty();
					}
				}
			}
		}

		return remaining < 1;
	}

	/**
	 * Sets an entity's location so that it doesn't spawn inside of walls.
	 * Automatically removes placeholder block at coordinates x/y/z.
	 * @return false if no suitable location found
	 */
	public static final boolean setEntityInStructure(World world, Entity entity, int x, int y, int z) {
		if (entity == null) {
			return false;
		}
		int i = 0;
		int iMax = (entity.width > 1.0F ? 16 : 4);
		int factor = 1;

		world.setBlockToAir(x, y, z);

		entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);

		while (entity.isEntityInsideOpaqueBlock() && i < iMax) {
			if (i == 4 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F) {
				entity.setLocationAndAngles(x, y, z, 90.0F, 0.0F);
				LogHelper.finest("Large entity; rotating 90 degrees");
			} else if (i == 8 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F) {
				entity.setLocationAndAngles(x, y, z, 180.0F, 0.0F);
				LogHelper.finest("Large entity; rotating 180 degrees");
			} else if (i == 12 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F) {
				entity.setLocationAndAngles(x, y, z, 270.0F, 0.0F);
				LogHelper.finest("Large entity; rotating 270 degrees");
			}

			LogHelper.finer("Entity inside opaque block at " + entity.posX + "/" + entity.posY + "/" + entity.posZ);

			switch(i % 4) {
			case 0: entity.setPosition(entity.posX + 0.5D, entity.posY, entity.posZ + 0.5D); break;
			case 1: entity.setPosition(entity.posX, entity.posY, entity.posZ - 1.0D); break;
			case 2: entity.setPosition(entity.posX - 1.0D, entity.posY, entity.posZ); break;
			case 3: entity.setPosition(entity.posX, entity.posY, entity.posZ + 1.0D); break;
			}

			++i;
			/*
			if (i == 12 && factor == 1 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F) {
				System.out.println("[GEN STRUCTURE][SPAWN] Large entity still inside opaque block; resetting with factor of 2");
				factor = 2;
				i = 0;
			}
			 */
		}
		if (entity.isEntityInsideOpaqueBlock()) {
			LogHelper.warning("Failed to set entity in open space. Returning to default position.");
			entity.setPosition(entity.posX + 0.5D, entity.posY, entity.posZ + 0.5D);
			return false;
		}

		return true;
	}

	/**
	 * Spawns an entity in the structure by using setEntityInStructure.
	 * @return true if entity spawned without collision (entity will still spawn if false, but may be in a wall)
	 */
	public static final boolean spawnEntityInStructure(World world, Entity entity, int x, int y, int z) {
		if (world.isRemote || entity == null) {
			return false;
		}
		boolean collided = setEntityInStructure(world, entity, x, y, z);
		world.spawnEntityInWorld(entity);
		LogHelper.finest("Spawned entity at " + entity.posX + "/" + entity.posY + "/" + entity.posZ);
		return collided;
	}

	/**
	 * Returns an AxisAlignedBB suitable for a hanging entity at x/y/z facing direction
	 */
	public static final AxisAlignedBB getHangingEntityAxisAligned(int x, int y, int z, int direction) {
		double minX = (double) x, minZ = (double) z, maxX = minX, maxZ =  minZ;
		switch(direction) {
		case 2: // frame facing NORTH
			minX += 0.25D;
			maxX += 0.75D;
			minZ += 0.5D;
			maxZ += 1.5D;
			break;
		case 3: // frame facing SOUTH
			minX += 0.25D;
			maxX += 0.75D;
			minZ -= 0.5D;
			maxZ += 0.5D;
			break;
		case 4: // frame facing WEST
			minX += 0.5D;
			maxX += 1.5D;
			minZ += 0.25D;
			maxZ += 0.75D;
			break;
		case 5: // frame facing EAST
			minX -= 0.5D;
			maxX += 0.5D;
			minZ += 0.25D;
			maxZ += 0.75D;
			break;
		}

		return AxisAlignedBB.getBoundingBox(minX, (double) y, minZ, maxX, (double) y + 1, maxZ);
	}

	/**
	 * Places a hanging item entity in the world at the correct location and facing.
	 * Note that you MUST use a WALL_MOUNTED type block id (such as torch) for your custom
	 * block id's getRealBlockID return value in order for orientation to be correct.
	 * Coordinates x,y,z are the location of the block used to spawn the entity
	 * NOTE: Automatically removes the dummy block at x/y/z before placing the entity, so the
	 * metadata stored in the block will no longer be available, but will be returned by this
	 * method so it can be stored in a local variable for later use.
	 * @param hanging Must be an instance of ItemHangingEntity, such as Item.painting
	 * @return Returns direction for further processing such as for ItemFrames, or -1 if no entity set
	 */
	public static final int setHangingEntity(World world, ItemStack hanging, int x, int y, int z) {
		if (hanging.getItem() == null || !(hanging.getItem() instanceof ItemHangingEntity)) {
			return -1;
		}

		if (world.getBlockMetadata(x, y, z) < 1 || world.getBlockMetadata(x, y, z) > 5) {
			LogHelper.warning("Hanging entity has invalid metadata of " + world.getBlockMetadata(x, y, z) + ". Valid values are 1,2,3,4");
			return - 1;
		}

		int[] metaToFacing = {5, 4, 3, 2};
		int direction = metaToFacing[world.getBlockMetadata(x, y, z) - 1];
		//FakePlayer player = new FakePlayer(world,"fake");

		world.setBlockToAir(x, y, z);
		switch(direction) {
		case 2: ++z; break; // frame facing NORTH
		case 3: --z; break; // frame facing SOUTH
		case 4: ++x; break; // frame facing WEST
		case 5: --x; break; // frame facing EAST
		}

		// TODO ((ItemHangingEntity) hanging.getItem()).onItemUse(hanging, player, world, x, y, z, direction, 0, 0, 0);

		return direction;
	}

	/**
	 * Set's the itemstack contained in ItemFrame at x/y/z with default rotation.
	 * @param direction Use the value returned from the setHangingEntity method
	 */
	public static final void setItemFrameStack(World world, ItemStack itemstack, int x, int y, int z, int direction) {
		setItemFrameStack(world, itemstack, x, y, z, direction, 0);
	}

	/**
	 * Set's the itemstack contained in ItemFrame at x/y/z with specified rotation.
	 * @param direction Use the value returned from the setHangingEntity method
	 * @param itemRotation 0,1,2,3 starting at default and rotating 90 degrees clockwise
	 */
	public static final void setItemFrameStack(World world, ItemStack itemstack, int x, int y, int z, int direction, int itemRotation) {
		List<EntityItemFrame> frames = world.getEntitiesWithinAABB(EntityItemFrame.class, getHangingEntityAxisAligned(x, y, z, direction));
		if (frames != null && !frames.isEmpty()) {
			for (EntityItemFrame frame : frames) {
				frame.setDisplayedItem(itemstack);
				frame.setItemRotation(itemRotation);
			}
		}
	}

	/**
	 * Sets the art for a painting at location x/y/z and sends a packet to update players.
	 * @param direction Use the value returned from the setHangingEntity method
	 * @return false if 'name' didn't match any EnumArt values.
	 */
	public static final boolean setPaintingArt(World world, String name, int x, int y, int z, int direction) {
		List<EntityPainting> paintings = world.getEntitiesWithinAABB(EntityPainting.class, getHangingEntityAxisAligned(x, y, z, direction));
		if (paintings != null && !paintings.isEmpty() && name.length() > 0) {
			for (EntityPainting toEdit : paintings) {
				EnumArt[] aenumart = EnumArt.values();
				int i1 = aenumart.length;
				for (int j1 = 0; j1 < i1; ++j1) {
					EnumArt enumart = aenumart[j1];
					if (enumart.title.equals(name)) {
						toEdit.art = enumart;
						// TODO PacketDispatcher.sendPacketToAllAround(x, y, z, 64, world.provider.dimensionId, new Packet25EntityPainting(toEdit));
						return true;
					}
				}
				LogHelper.warning(name + " does not match any values in EnumArt; unable to set painting art.");
			}
		}
		LogHelper.warning("No EntityPainting was found at " + x + "/" + y + "/" + z);
		return false;
	}

	/**
	 * Adds text to a sign in the world. Use EnumChatFormatting to set colors. Text of more
	 * than 15 characters per line will be truncated automatically.
	 * @param text A String array of no more than 4 elements; additional elements will be ignored
	 * @return false if no sign tile entity was found at x/y/z
	 */
	public static final boolean setSignText(World world, String[] text, int x, int y, int z) {
		TileEntitySign sign = (world.getTileEntity(x, y, z) instanceof TileEntitySign ? (TileEntitySign) world.getTileEntity(x, y, z) : null);
		if (sign != null) {
			for (int i = 0; i < sign.signText.length && i < text.length; ++i) {
				if (text[i] == null) {
					LogHelper.warning("Uninitialized String element while setting sign text at index " + i);
					continue;
				} else if (text[i].length() > 15) {
					LogHelper.warning(text[i] + " is too long to fit on a sign; maximum length is 15 characters.");
					sign.signText[i] = text[i].substring(0, 15);
				} else {
					sign.signText[i] = text[i];
				}
			}

			return true;
		}

		LogHelper.warning("No TileEntitySign was found at " + x + "/" + y + "/" + z);
		return false;
	}

	/**
	 * Method to set skulls not requiring extra rotation data (i.e. wall-mounted skulls whose rotation is determined by metadata)
	 */
	public static final boolean setSkullData(World world, String name, int type, int x, int y, int z) {
		return setSkullData(world, name, type, -1, x, y, z);
	}

	/**
	 * Sets skull type and name for a TileEntitySkull at x/y/z
	 * @param name Must be a valid player username
	 * @param type Type of skull: 0 Skeleton, 1 Wither Skeleton, 2 Zombie, 3 Human, 4 Creeper
	 * @param rot Sets the rotation for the skull if positive value is used
	 * @return false if errors were encountered (i.e. incorrect tile entity at x/y/z)
	 */
	public static final boolean setSkullData(World world, String name, int type, int rot, int x, int y, int z)
	{
		TileEntitySkull skull = (world.getTileEntity(x, y, z) instanceof TileEntitySkull ? (TileEntitySkull) world.getTileEntity(x, y, z) : null);

		if (skull != null)
		{
			if (type > 4 || type < 0) {
				LogHelper.warning("Custom data value " + type + " not valid for skulls. Valid values are 0 to 4.");
				type = 0;
			}
			// func_152107_a is setType(int)
			skull.func_152107_a(type);
			//setSkullType(type, name);

			if (rot > -1) {
				// func_145903_a is setSkullRotation(int)
				skull.func_145903_a(rot % 16);
			}

			return true;
		}

		LogHelper.warning("No TileEntitySkull found at " + x + "/" + y + "/" + z);
		return false;
	}
	/**
	 * This method will return the correct metadata value for the block type based on
	 * how it was rotated in the world, IF and ONLY IF you used the correct metadata
	 * value to set the block's default orientation for your structure's default facing.
	 * 
	 * If your structure's front faces EAST by default, for example, and you want a wall
	 * sign out front greeting all your guests, you'd better use '5' as its metadata value
	 * in your blockArray so it faces EAST as well.
	 * 
	 * Please read the blockArray notes very carefully and test out your structure to make
	 * sure everything is oriented how you thought it was.
	 * 
	 * @param rotations	The number of rotations to apply
	 * @param block		The block being rotated
	 * @param origMeta	The block's original metadata value
	 */
	public static final int getMetadata(int rotations, Block block, int origMeta) {
		if (BlockRotationData.getBlockRotationType(block) == null) {
			return origMeta; // no rotation data, return original metadata value
		}
		int meta = origMeta;
		int bitface;
		int tickDelay = (meta >> 2);// used by repeaters, comparators, etc.
		int bit4 = (meta & 4);		// most commonly used for actual rotation
		int bit8 = (meta & 8);		// usually 'on' or 'off' flag, but also top/bottom for doors
		int bit9 = (meta >> 3);		// used by pistons for something, can't remember what...
		int extra = (meta & ~3);	// used by doors for hinge orientation, I think

		for (int i = 0; i < rotations; ++i) {
			bitface = meta % 4;
			switch(BlockRotationData.getBlockRotationType(block)) {
			case ANVIL:
				meta ^= 1;
				break;
			case DOOR:
				if (bit8 != 0) return meta;
				meta = (bitface == 3 ? 0 : bitface + 1);
				meta |= extra;
				break;
			case GENERIC:
				meta = (bitface == 3 ? 0 : bitface + 1) | bit4 | bit8;
				break;
			case PISTON_CONTAINER:
				meta -= meta > 7 ? 8 : 0;
				if (meta > 1) meta = meta == 2 ? 5 : meta == 5 ? 3 : meta == 3 ? 4 : 2;
				meta |= bit8 | bit9 << 3;
				break;
			case QUARTZ:
				meta = meta == 3 ? 4 : meta == 4 ? 3 : meta;
				break;
			case RAIL:
				if (meta < 2) meta ^= 1;
				else if (meta < 6) meta = meta == 2 ? 5 : meta == 5 ? 3 : meta == 3 ? 4 : 2;
				else meta = meta == 9 ? 6 : meta + 1;
				break;
			case REPEATER:
				meta = (bitface == 3 ? 0 : bitface + 1) | (tickDelay << 2);
				break;
			case SIGNPOST:
				meta = meta < 12 ? meta + 4 : meta - 12;
				break;
			case SKULL:
				meta = meta == 1 ? 1 : meta == 4 ? 2 : meta == 2 ? 5 : meta == 5 ? 3 : 4;
				break;
			case STAIRS:
				meta = (bitface == 0 ? 2 : bitface == 2 ? 1 : bitface == 1 ? 3 : 0) | bit4;
				break;
			case TRAPDOOR:
				meta = (bitface == 0 ? 3 : bitface == 3 ? 1 : bitface == 1 ? 2 : 0) | bit4 | bit8;
				break;
			case VINE:
				meta = meta == 1 ? 2 : meta == 2 ? 4 : meta == 4 ? 8 : 1;
				break;
			case WALL_MOUNTED:
				if (meta > 0 && meta < 5) meta = meta == 4 ? 1 : meta == 1 ? 3 : meta == 3 ? 2 : 4;
				break;
			case LEVER:
				meta -= meta > 7 ? 8 : 0;
				if (meta > 0 && meta < 5) meta = meta == 4 ? 1 : meta == 1 ? 3 : meta == 3 ? 2 : 4;
				else if (meta == 5 || meta == 6) meta = meta == 5 ? 6 : 5;
				else meta = meta == 7 ? 0 : 7;
				meta |= bit8;
				break;
			case WOOD:
				if (meta > 4 && meta < 12) meta = meta < 8 ? meta + 4 : meta - 4;
				break;
			default:
				break;
			}
		}

		return meta;
	}

	/**
	 * Fixes blocks metadata after they've been placed in the world, specifically for blocks
	 * such as rails, furnaces, etc. whose orientation is automatically determined by the block
	 * when placed via the onBlockAdded method.
	 */
	public static final void setMetadata(World world, int x, int y, int z, int origMeta) {
		Block block = world.getBlock(x, y, z); 
		if (BlockRotationData.getBlockRotationType(block) == null) {
			return;
		}

		switch(BlockRotationData.getBlockRotationType(block)) {
		case PISTON_CONTAINER: world.setBlockMetadataWithNotify(x, y, z, origMeta, 2); break;
		case RAIL: world.setBlockMetadataWithNotify(x, y, z, origMeta, 2); break;
		default: break;
		}
	}

	/**
	 * Returns true if material for realID matches or is compatible with worldID material
	 */
	public static final boolean materialsMatch(Block block1, Block block2) {
		return  (block1.getMaterial() == Material.grass && block2.getMaterial() == Material.ground) ||
				(block1.getMaterial().isLiquid() && block2.getMaterial() == block1.getMaterial()) ||
				(block1.getMaterial() == Material.ice && block2.getMaterial() == Material.water) ||
				(block1.getMaterial() == Material.piston && block2.getMaterial() == Material.piston) ||
				(block1 instanceof BlockRedstoneTorch && block2 instanceof BlockRedstoneTorch) ||
				(block1 instanceof BlockRedstoneRepeater && block2 instanceof BlockRedstoneRepeater);
	}
}
