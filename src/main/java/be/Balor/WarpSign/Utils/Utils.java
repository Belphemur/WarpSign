/************************************************************************
 * This file is part of WarpSign.									
 *																		
 * WarpSign is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * WarpSign is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with WarpSign.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package be.Balor.WarpSign.Utils;

import java.util.Collection;

import org.bukkit.World;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class Utils {
	public static String matchWorldName(Collection<World> container, String search) {
		String found = null;
		String lowerSearch = search.toLowerCase();
		int delta = Integer.MAX_VALUE;
		for (World w : container) {
			String str = w.getName();
			if (str.toLowerCase().startsWith(lowerSearch)) {
				int curDelta = str.length() - lowerSearch.length();
				if (curDelta < delta) {
					found = str;
					delta = curDelta;
				}
				if (curDelta == 0)
					break;
			}
		}
		return found;

	}
}
