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

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import be.Balor.Manager.Permissions.PermissionManager;
import be.Balor.Tools.Warp;
import be.Balor.WarpSign.ConfigEnum;
import be.Balor.WarpSign.Utils;
import be.Balor.World.ACWorld;
import static be.Balor.Tools.Utils.colorParser;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SignListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled())
			return;
		String line0 = event.getLine(0);
		Player p = event.getPlayer();
		if (line0.indexOf(ConfigEnum.KEYWORD.getString()) != 0)
			return;
		if (!PermissionManager.hasPerm(p, "admincmd.warpsign.edit"))
		{
			event.setCancelled(true);
			return;
		}
		if (ConfigEnum.AUTOCOMPLETE.getBoolean()) {
			String world = event.getLine(1);
			String warp = event.getLine(2);
			if (Bukkit.getServer().getWorld(world) == null) {
				String found = Utils.matchWorldName(Bukkit.getServer().getWorlds(), world);
				if (found == null) {
					event.setLine(1, ConfigEnum.WORLDNF.getString() + world);
					return;
				}
				event.setLine(1, found);
				world = found;
			}
			ACWorld acWorld = ACWorld.getWorld(world);
			Warp warpPoint = acWorld.getWarp(warp);
			if (warpPoint == null) {
				event.setLine(2, ConfigEnum.WARPNF.getString() + warp);
				return;
			}
			event.setLine(2, warpPoint.name);
		}
		if (ConfigEnum.COLOR.getBoolean()) {
			event.setLine(1, colorParser(ConfigEnum.WORDC.getString()) + event.getLine(1));
			event.setLine(2, colorParser(ConfigEnum.WARPC.getString()) + event.getLine(2));
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getBlock().getState() instanceof Sign))
			return;
		Sign sign = (Sign) event.getBlock().getState();
		if (sign.getLine(0).indexOf(ConfigEnum.KEYWORD.getString()) != 0)
			return;
		if (!PermissionManager.hasPerm(event.getPlayer(), "admincmd.warpsign.edit"))
			event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
	}
}
