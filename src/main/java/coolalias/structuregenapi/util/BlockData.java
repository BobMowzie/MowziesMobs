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

package coolalias.structuregenapi.util;

/**
 * Stores all data needed for post-gen processing, specifically for custom 'hooks'
 */
public class BlockData
{
	private final int x, y, z, id, meta, customData1, customData2;

	public BlockData(int x, int y, int z, int id, int meta, int customData1, int customData2) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.meta = meta;
		this.customData1 = customData1;
		this.customData2 = customData2;
	}

	public final int getPosX() {
		return this.x;
	}

	public final int getPosY() {
		return this.y;
	}

	public final int getPosZ() {
		return this.z;
	}

	public final int getBlockID() {
		return this.id;
	}

	public final int getMetaData() {
		return this.meta;
	}

	public final int getCustomData1() {
		return this.customData1;
	}

	public final int getCustomData2() {
		return this.customData2;
	}
}
