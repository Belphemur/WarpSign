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

import static be.Balor.Tools.Utils.colorParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import be.Balor.WarpSign.ConfigEnum;
import be.Balor.WarpSign.WarpSign;
import be.Balor.WarpSign.Utils.WarpSignContainer;
import be.Balor.bukkit.AdminCmd.ACPluginManager;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SignCountListener extends SignListener {
	private PreparedStatement getCountStmt;
	private PreparedStatement updateCountStmt;
	/**
	 * 
	 */
	public SignCountListener() {
		try {
			final Connection sqlLite = WarpSign.getSqlLite();
			getCountStmt = sqlLite
					.prepareStatement("SELECT warpCount FROM `signs` WHERE `x`=? AND `y`= ? AND `z`=?;");
			updateCountStmt = sqlLite
					.prepareStatement("UPDATE `signs` SET `warpcount` = warpcount+1 WHERE `signs`.`x` =? AND `signs`.`y` =? AND `signs`.`z` =?;");

		} catch (final SQLException e) {
			WarpSign.logSqliteException(e);
		}
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
	public WarpSignContainer onPlayerInteract(final PlayerInteractEvent event) {
		final WarpSignContainer warp = super.onPlayerInteract(event);
		if (warp == null) {
			return null;
		}
		int count = 1;
		final Block block = warp.sign.getBlock();
		try {
			getCountStmt.clearParameters();
			getCountStmt.setInt(1, block.getX());
			getCountStmt.setInt(2, block.getY());
			getCountStmt.setInt(3, block.getZ());
			getCountStmt.execute();
			final ResultSet result = getCountStmt.getResultSet();
			if (!result.next()) {
				WarpSign.insertSign(warp.worldName, warp.warpName, block);
			} else {
				count = result.getInt(1);
			}
			count++;
			updateCountStmt.clearParameters();
			updateCountStmt.setInt(1, block.getX());
			updateCountStmt.setInt(2, block.getY());
			updateCountStmt.setInt(3, block.getZ());
			updateCountStmt.executeUpdate();
		} catch (final SQLException e) {
			WarpSign.logSqliteException(e);
		}

		warp.sign.setLine(3, colorParser(ConfigEnum.COUNT_MSG.getString())
				+ count);
		ACPluginManager.scheduleSyncTask(new Runnable() {

			@Override
			public void run() {
				warp.sign.update();
			}
		});
		return warp;
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
	public void onBlockBreak(final BlockBreakEvent event) {
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
	public void onSignChange(final SignChangeEvent event) {
		super.onSignChange(event);
	}
}
