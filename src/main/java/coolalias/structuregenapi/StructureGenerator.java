/**
    Copyright (C) <2013> <coolAlias>

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

package coolalias.structuregenapi;

import coolalias.structuregenapi.util.StructureGeneratorBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * This class is responsible for handling custom block hooks used in your block arrays,
 * though currently it is here only as a place-holder.
 * 
 * See structuregen.mod.ModStructureGenerator or the tutorial series for more information
 * on how to properly create and use your own.
 */
public class StructureGenerator extends StructureGeneratorBase
{
	public StructureGenerator() {}

	public StructureGenerator(Entity entity, int[][][][] blocks) {
		super(entity, blocks);
	}

	public StructureGenerator(Entity entity, int[][][][] blocks, int structureFacing) {
		super(entity, blocks, structureFacing);
	}

	public StructureGenerator(Entity entity, int[][][][] blocks, int structureFacing, int offX, int offY, int offZ) {
		super(entity, blocks, structureFacing, offX, offY, offZ);
	}

	@Override
	public int getRealBlockID(int fakeID, int customData1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onCustomBlockAdded(World world, int x, int y, int z, int fakeID, int customData1, int customData2) {
		// TODO Auto-generated method stub
	}
}
