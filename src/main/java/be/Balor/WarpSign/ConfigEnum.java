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
/**
 * @author Balor (aka Antoine Aflalo)
 *
 */
package be.Balor.WarpSign;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;

public enum ConfigEnum {
	KEYWORD(
			"warpKeyWord",
			"[ACWarp]",
			"set the keyword used to recognise a WarpSign.\n "
					+ "BE CAREFULL if you change it, older WarpSign will be not working."),
	WORLDNF(
			"worldNotFound",
			"This World doesn't exists : ",
			"Message used when using the autoComplete feature and the World couldn't be found."),
	WARPNF(
			"warpNotFound",
			"This Warp doesn't exists : ",
			"Message used when using the autoComplete feature and the Warp couldn't be found."),
	COLOR(
			"useColor",
			true,
			"When set to true, will automatically color the World and Warp name on the sign using the color defined below."),
	WORDC(
			"wordColor",
			"&" + ChatColor.GOLD.getChar(),
			"This color will be use for colouring the Word's name on the sign."),
	WARPC(
			"warpColor",
			"&" + ChatColor.GREEN.getChar(),
			"This color will be use for colouring the Warp's name on the sign."),
	COUNT(
			"useCount",
			true,
			"When set to true, the last line of the sign is used to count the number of teleportation done using the sign."),
	COUNT_MSG(
			"countMessage",
			"&" + ChatColor.RED.getChar() + "Count: &"
					+ ChatColor.LIGHT_PURPLE.getChar(),
			"When Count is set to true, this message will be used to display the teleport count."),
	TP_MSG(
			"teleportMessage",
			"&" + ChatColor.GREEN.getChar() + "Teleported successfully to &"
					+ ChatColor.WHITE.getChar(),
			"Teleport message used when successfully teleported to the Warp Point."),
	BLOCK_PROTECTION(
			"blockProtect",
			true,
			"Activate some block protection to avoid block placing near the Sign (WarpSign)"),
	BLOCK_PERIMETER(
			"blockPerim",
			5,
			"Distance in block that represent the protected zone when using the block protection"),
	BLOCK_PROTECT_MSG(
			"blockMsg",
			"",
			"Message displayed when somebody try to place a block in the block protection perimeter. If empty, nothing is displayed.");

	private final String confVal;
	private final Object defaultVal;
	private final String description;
	private static ConfigurationSection config;
	private static String pluginVersion;
	private static String pluginName;

	/**
	 * @param confVal
	 * @param defaultVal
	 * @param description
	 */
	private ConfigEnum(final String confVal, final Object defaultVal,
			final String description) {
		this.confVal = confVal;
		this.defaultVal = defaultVal;
		this.description = description;
	}

	public String getString() {
		return config.getString(confVal);
	}

	public int getInt() {
		return config.getInt(confVal);
	}

	public double getDouble() {
		return config.getDouble(confVal);
	}

	public boolean getBoolean() {
		return config.getBoolean(confVal);
	}

	public long getLong() {
		return config.getLong(confVal);
	}

	/**
	 * @return the defaultvalues
	 */
	public static Map<String, Object> getDefaultvalues() {
		final Map<String, Object> values = new LinkedHashMap<String, Object>();
		for (final ConfigEnum ce : values()) {
			values.put(ce.confVal, ce.defaultVal);
		}
		return values;
	}

	public static String getHeader() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("Configuration file of ").append(pluginName).append('\n');
		buffer.append("Plugin Version: ").append(pluginVersion).append('\n')
				.append('\n');
		for (final ConfigEnum ce : values()) {
			buffer.append(ce.confVal).append("\t:\t").append(ce.description)
					.append(" (Default : ").append(ce.defaultVal).append(')')
					.append('\n');
		}
		return buffer.toString();
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public static void setConfig(final ConfigurationSection config) {
		ConfigEnum.config = config;
	}
	public static void setPluginInfos(final PluginDescriptionFile pdf) {
		ConfigEnum.pluginVersion = pdf.getVersion();
		ConfigEnum.pluginName = pdf.getName();
	}
}
