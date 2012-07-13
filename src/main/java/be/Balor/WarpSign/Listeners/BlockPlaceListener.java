/*This file is part of WarpSign.

    WarpSign is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WarpSign is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WarpSign.  If not, see <http://www.gnu.org/licenses/>.*/
package be.Balor.WarpSign.Listeners;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import be.Balor.WarpSign.ConfigEnum;
import be.Balor.WarpSign.WarpSign;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class BlockPlaceListener implements Listener {
	private PreparedStatement blockPlaceStmt;
	/**
	 * 
	 */
	public BlockPlaceListener() {
		try {
			blockPlaceStmt = WarpSign
					.getSqlLite()
					.prepareStatement(
							"SELECT name FROM `signs` WHERE worldloc=? AND (((x-?)*(x-?)) + ((y-?)*(y-?)) + ((z-?)*(z-?))) <="
									+ Math.pow(
											ConfigEnum.BLOCK_PERIMETER.getInt(),
											2));
		} catch (final SQLException e) {
			WarpSign.logSqliteException(e);
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Block block = event.getBlock();
		try {
			blockPlaceStmt.clearParameters();
			blockPlaceStmt.setString(1, block.getWorld().getName());
			blockPlaceStmt.setInt(2, block.getX());
			blockPlaceStmt.setInt(3, block.getX());
			blockPlaceStmt.setInt(4, block.getY());
			blockPlaceStmt.setInt(5, block.getY());
			blockPlaceStmt.setInt(6, block.getZ());
			blockPlaceStmt.setInt(7, block.getZ());
			blockPlaceStmt.execute();
			if (blockPlaceStmt.getResultSet().next()) {
				final String msg = ConfigEnum.BLOCK_PROTECT_MSG.getString();
				if (!msg.isEmpty()) {
					event.getPlayer().sendMessage(msg);
				}
				event.setCancelled(true);
			}
		} catch (final SQLException e) {
			WarpSign.logSqliteException(e);
		}
	}
}
