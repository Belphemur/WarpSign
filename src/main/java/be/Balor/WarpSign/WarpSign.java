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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import be.Balor.Manager.Permissions.PermParent;
import be.Balor.Tools.Metrics;
import be.Balor.Tools.Configuration.File.ExtendedConfiguration;
import be.Balor.Tools.Debug.ACPluginLogger;
import be.Balor.WarpSign.Listeners.SignListener;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class WarpSign extends AbstractAdminCmdPlugin {
	public final static ACPluginLogger log = ACPluginLogger.getLogger("WarpSign");

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {
		logger.info("Plugin Disabled.");
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
		final PluginDescriptionFile pdfFile = this.getDescription();
		logger.info("Plugin Enabled. (version " + pdfFile.getVersion() + ")");
		ExtendedConfiguration conf = ExtendedConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		conf.addDefaults(ConfigEnum.getDefaultvalues());
		conf.options().header(
				"This is the configuration file of WarpSign\n" + ConfigEnum.getHeader());
		conf.options().copyDefaults(true);
		try {
			conf.save();
		} catch (IOException e1) {
			logger.severe("Configuration saving problem", e1);
		}
		ConfigEnum.setConfig(conf);
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		permissionLinker.registerAllPermParent();
		Metrics metrics;
		try {
			metrics = new Metrics();
			metrics.beginMeasuringPlugin(this);
		} catch (IOException e) {
		}

	}
}
