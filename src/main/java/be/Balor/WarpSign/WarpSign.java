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

import be.Balor.Manager.Permissions.PermParent;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class WarpSign extends AbstractAdminCmdPlugin {

	/**
	 * @param name
	 */
	public WarpSign(String name) {
		super("WarpSign");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {

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
	}
}
