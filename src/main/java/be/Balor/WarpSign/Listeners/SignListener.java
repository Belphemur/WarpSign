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

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import be.Balor.Manager.Permissions.PermissionManager;
import be.Balor.WarpSign.ConfigEnum;
import be.Balor.WarpSign.WarpSign;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SignListener implements Listener {

	private final WarpSign plugin;

	/**
	 * @param plugin
	 */
	public SignListener(WarpSign plugin) {
		super();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled())
			return;
		String line0 = event.getLine(0);
		Player p = event.getPlayer();
		if (line0.indexOf(plugin.getConfString(ConfigEnum.KEYWORD)) != 0)
			return;
		if (!PermissionManager.hasPerm(p, "admincmd.warpsign.edit"))
			event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getBlock().getState() instanceof Sign))
			return;
		Sign sign = (Sign) event.getBlock().getState();
		Player p = event.getPlayer();
		String line0 = sign.getLine(0);
		if (line0.indexOf(plugin.getConfString(ConfigEnum.KEYWORD)) != 0)
			return;
		if (!PermissionManager.hasPerm(p, "admincmd.warpsign.edit"))
			event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
	}
}
