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
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;

import be.Balor.Tools.Configuration.File.ExtendedConfiguration;
import be.Balor.WarpSign.ConfigEnum;
import be.Balor.WarpSign.Utils.WarpSignContainer;
import be.Balor.bukkit.AdminCmd.ACPluginManager;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;
import static be.Balor.Tools.Utils.colorParser;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SignCountListener extends SignListener {
	private final ExtendedConfiguration counts;
	private final AbstractAdminCmdPlugin plugin;

	/**
	 * @param counts
	 * @param plugin
	 */
	public SignCountListener(ExtendedConfiguration counts, AbstractAdminCmdPlugin plugin) {
		super();
		this.counts = counts;
		this.plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * be.Balor.WarpSign.Listeners.SignListener#onPlayerInteract(org.bukkit.
	 * event.player.PlayerInteractEvent)
	 */
	@Override
	@EventHandler
	public WarpSignContainer onPlayerInteract(PlayerInteractEvent event) {
		final WarpSignContainer warp = super.onPlayerInteract(event);
		if (warp == null)
			return null;
		int count = counts.getInt(warp.toString(), 0);
		counts.set(warp.toString(), ++count);
		warp.sign.setLine(3, colorParser(ConfigEnum.COUNT_MSG.getString()) + count);
		ACPluginManager.scheduleSyncTask(new Runnable() {

			@Override
			public void run() {
				warp.sign.update();
			}
		});
		return warp;
	}

	@EventHandler
	public void onSave(WorldSaveEvent event) {
		saveCounts();
	}

	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		if (!event.getPlugin().equals(plugin))
			return;
		saveCounts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * be.Balor.WarpSign.Listeners.SignListener#onBlockBreak(org.bukkit.event
	 * .block.BlockBreakEvent)
	 */
	@Override
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		super.onBlockBreak(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * be.Balor.WarpSign.Listeners.SignListener#onSignChange(org.bukkit.event
	 * .block.SignChangeEvent)
	 */
	@Override
	@EventHandler(priority = EventPriority.HIGH)
	public void onSignChange(SignChangeEvent event) {
		super.onSignChange(event);
	}

	private void saveCounts() {
		try {
			counts.save();
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Problem when saving the TP count", e);
		}
	}
}
