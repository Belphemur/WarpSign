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
package be.Balor.WarpSign.Listeners;

import java.io.IOException;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldSaveEvent;

import be.Balor.Tools.Configuration.File.ExtendedConfiguration;
import be.Balor.WarpSign.ConfigEnum;
import be.Balor.WarpSign.WarpSign;
import be.Balor.WarpSign.Utils.WarpSignContainer;
import static be.Balor.Tools.Utils.colorParser;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SignCountListener extends SignListener {
	private final ExtendedConfiguration counts;

	/**
	 * @param counts
	 */
	public SignCountListener(ExtendedConfiguration counts) {
		super();
		this.counts = counts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * be.Balor.WarpSign.Listeners.SignListener#onPlayerInteract(org.bukkit.
	 * event.player.PlayerInteractEvent)
	 */
	@Override
	public WarpSignContainer onPlayerInteract(PlayerInteractEvent event) {
		WarpSignContainer warp = super.onPlayerInteract(event);
		if (warp == null)
			return null;
		int count = counts.getInt(warp.toString(), 0);
		counts.set(warp.toString(), ++count);
		warp.sign.setLine(3, colorParser(ConfigEnum.COUNT_MSG.getString()) + count);
		return warp;
	}

	public void onSave(WorldSaveEvent event) {
		try {
			counts.save();
		} catch (IOException e) {
			WarpSign.log.severe("Problem when saving the TP count", e);
		}
	}
}
