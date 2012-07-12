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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import be.Balor.Manager.Permissions.PermParent;
import be.Balor.Tools.Configuration.File.ExtendedConfiguration;
import be.Balor.WarpSign.Listeners.SignCountListener;
import be.Balor.WarpSign.Listeners.SignListener;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class WarpSign extends AbstractAdminCmdPlugin {
	private static Connection sqlLite;
	public static WarpSign INSTANCE;
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
		if (ConfigEnum.COUNT.getBoolean()) {
			getServer().getPluginManager().registerEvents(
					new SignCountListener(
							ExtendedConfiguration.loadConfiguration(new File(
									getDataFolder(), "counts.yml")), this),
					this);
		} else {
			getServer().getPluginManager().registerEvents(new SignListener(),
					this);
		}
		permissionLinker.registerAllPermParent();
		initSqlLite();

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
					+ "  `count` int(11) NOT NULL DEFAULT '0',"
					+ "  `x` int(11) NOT NULL," + "  `y` int(11) NOT NULL,"
					+ "  `z` int(11) NOT NULL," + "  PRIMARY KEY (`x`,`y`,`z`)"
					+ ") ");
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
}
