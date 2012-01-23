/************************************************************************
 * This file is part of AdminCmd.
 *
 * AdminCmd is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AdminCmd is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AdminCmd.  If not, see <http://www.gnu.org/licenses/>.
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

public enum ConfigEnum {
	KEYWORD("warpKeyWord", "[ACWarp]", "set the keyword used to recognise a WarpSign.\n "
			+ "BE CAREFULL if you change it, older WarpSign will be not working."), 
	AUTOCOMPLETE(
			"autoComplete", true,
			"when typing the name of a world of a warp, if set to true, will autocomplete the Sign."), 
	WORLDNF(
			"worldNotFound", "This World doesn't exists : ",
			"Message used when using the autoComplete feature and the World couldn't be found."), 
	WARPNF(
			"warpNotFound", "This Warp doesn't exists : ",
			"Message used when using the autoComplete feature and the Warp couldn't be found."), 
	COLOR(
			"useColor", true,
			"When set to true, will automatically color the World and Warp name on the sign using the color defined below."),
	WORDC(
			"wordColor", "&" + ChatColor.GOLD.getChar(),
			"This color will be use for colouring the Word's name on the sign."), 
	WARPC(
			"warpColor", "&" + ChatColor.GREEN.getChar(),
			"This color will be use for colouring the Warp's name on the sign.");

	private final String confVal;
	private final Object defaultVal;
	private final String description;
	private static ConfigurationSection config;

	/**
	 * @param confVal
	 * @param defaultVal
	 * @param description
	 */
	private ConfigEnum(String confVal, Object defaultVal, String description) {
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
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		for (ConfigEnum ce : values())
			values.put(ce.confVal, ce.defaultVal);
		return values;
	}

	public static String getHeader() {
		StringBuffer buffer = new StringBuffer();
		for (ConfigEnum ce : values())
			buffer.append(ce.confVal).append("\t:\t").append(ce.description).append(" (Default : ")
					.append(ce.defaultVal).append(')').append('\n');
		return buffer.toString();
	}
	/**
	 * @param config the config to set
	 */
	public static void setConfig(ConfigurationSection config) {
		ConfigEnum.config = config;
	}
}
