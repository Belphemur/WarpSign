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
package be.Balor.WarpSign;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import be.Balor.Manager.Permissions.PermParent;
import be.Balor.Tools.Configuration.File.ExtendedConfiguration;
import be.Balor.WarpSign.Listeners.BlockPlaceListener;
import be.Balor.WarpSign.Listeners.SignCountListener;
import be.Balor.WarpSign.Listeners.SignListener;
import be.Balor.WarpSign.Utils.WarpSignContainer;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class WarpSign extends AbstractAdminCmdPlugin {
	private static Connection sqlLite;
	public static WarpSign INSTANCE;
	private static PreparedStatement insertStmt, getSignStmt;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {
		getLogger().info("Plugin Disabled.");
		if (sqlLite != null) {
			try {
				sqlLite.close();
			} catch (final SQLException e) {}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin#registerCmds()
	 */
	@Override
	public void registerCmds() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin#registerPermParents()
	 */
	@Override
	protected void registerPermParents() {
		permissionLinker.addPermParent("admincmd.warpsign.*");
		permissionLinker.setMajorPerm(new PermParent("admincmd.*"));
		permissionLinker.addPermChild("admincmd.warpsign.edit");
		permissionLinker.addPermChild("admincmd.warpsign.use");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin#setDefaultLocale()
	 */
	@Override
	protected void setDefaultLocale() {

	}

	@Override
	public void onEnable() {
		super.onEnable();
		INSTANCE = this;
		final PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(
				"Plugin Enabled. (version " + pdfFile.getVersion() + ")");
		ConfigEnum.setPluginInfos(pdfFile);
		final ExtendedConfiguration conf = ExtendedConfiguration
				.loadConfiguration(new File(getDataFolder(), "config.yml"));
		conf.addDefaults(ConfigEnum.getDefaultvalues());
		conf.options().header(ConfigEnum.getHeader());
		conf.options().copyDefaults(true);
		try {
			conf.save();
		} catch (final IOException e1) {
			getLogger().log(Level.SEVERE, "Configuration saving problem", e1);
		}
		ConfigEnum.setConfig(conf);
		initSqlLite();
		final PluginManager pluginManager = getServer().getPluginManager();
		if (ConfigEnum.COUNT.getBoolean()) {
			pluginManager.registerEvents(new SignCountListener(), this);
		} else {
			pluginManager.registerEvents(new SignListener(), this);
		}
		if (ConfigEnum.BLOCK_PROTECTION.getBoolean()) {
			pluginManager.registerEvents(new BlockPlaceListener(), this);
		}
		permissionLinker.registerAllPermParent();

	}
	private void initSqlLite() {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (final ClassNotFoundException e) {
			errorHandler(e);
			return;
		}
		try {
			final File db = new File(getDataFolder(), "warpsign.db");
			if (!db.exists()) {
				db.createNewFile();
			}
			sqlLite = DriverManager.getConnection("jdbc:sqlite:"
					+ db.getAbsolutePath());
		} catch (final Exception e) {
			errorHandler(e);
			return;
		}
		try {
			final Statement stmt = sqlLite.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS `signs` ("
					+ "  `world` varchar(64) NOT NULL,"
					+ "  `name` varchar(64) NOT NULL,"
					+ "  `warpCount` int(11) NOT NULL DEFAULT '0',"
					+ "  `x` int(11) NOT NULL," + "  `y` int(11) NOT NULL,"
					+ "  `z` int(11) NOT NULL," + "  PRIMARY KEY (`x`,`y`,`z`)"
					+ ") ");
			insertStmt = sqlLite
					.prepareStatement("INSERT OR IGNORE INTO `signs` (`world`, `name`, `x`, `y`, `z`) VALUES (?, ?, ?, ?, ?)");
			getSignStmt = sqlLite
					.prepareStatement("SELECT world,name, warpcount FROM signs WHERE x=? AND y=? AND z=?");
		} catch (final SQLException e) {
			errorHandler(e);
			return;
		}

	}
	private void errorHandler(final Exception e) {
		getLogger().log(Level.SEVERE, "SQLite Error", e);
		Bukkit.getPluginManager().disablePlugin(this);
	}
	public static void logSqliteException(final SQLException e) {
		INSTANCE.getLogger().log(Level.WARNING, "SQLite Error", e);
	}
	/**
	 * @return the sqlLite
	 */
	public static Connection getSqlLite() {
		return sqlLite;
	}
	public static void insertSign(final String world, final String warp,
			final Block block) {
		try {
			insertStmt.clearParameters();
			insertStmt.setString(1, world);
			insertStmt.setString(2, warp);
			insertStmt.setInt(3, block.getX());
			insertStmt.setInt(4, block.getY());
			insertStmt.setInt(5, block.getZ());
			insertStmt.execute();
		} catch (final SQLException e) {
			logSqliteException(e);
		}
	}
	public static WarpSignContainer getSign(final Sign sign) {
		final Block block = sign.getBlock();
		try {
			getSignStmt.clearParameters();
			getSignStmt.setInt(1, block.getX());
			getSignStmt.setInt(2, block.getY());
			getSignStmt.setInt(3, block.getZ());
			getSignStmt.execute();
			final ResultSet result = getSignStmt.getResultSet();
			if (!result.next()) {
				return null;
			}
			final WarpSignContainer warpSignContainer = new WarpSignContainer(
					result.getString("name"), result.getString("world"), sign);
			warpSignContainer.count = result.getInt("warpCount");
			return warpSignContainer;
		} catch (final SQLException e) {
			logSqliteException(e);
		}
		return null;
	}
}
