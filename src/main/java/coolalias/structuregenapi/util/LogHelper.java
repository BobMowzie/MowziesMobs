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

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper
{
	private static Logger logger = Logger.getLogger("StructureGenAPI");

	public static void log(Level logLevel, String message) {
		logger.log(logLevel, message);
	}

	public static void severe(String message) {
		logger.log(Level.SEVERE, message);
	}

	public static void warning(String message) {
		logger.log(Level.WARNING, message);
	}

	public static void info(String message) {
		logger.log(Level.INFO, message);
	}

	public static void fine(String message) {
		logger.log(Level.FINE, message);
	}

	public static void finer(String message) {
		logger.log(Level.FINER, message);
	}

	public static void finest(String message) {
		logger.log(Level.FINEST, message);
	}
}
