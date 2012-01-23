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

import org.bukkit.block.Sign;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class WarpSignContainer {
	public final String warpName;
	public final String worldName;
	public final Sign sign;

	/**
	 * @param warpName
	 * @param worldName
	 * @param sign
	 */
	public WarpSignContainer(String warpName, String worldName, Sign sign) {
		super();
		this.warpName = warpName;
		this.worldName = worldName;
		this.sign = sign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return worldName + '.' + warpName;
	}

}
