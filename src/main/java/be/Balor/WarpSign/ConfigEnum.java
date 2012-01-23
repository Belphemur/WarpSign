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

import java.util.HashMap;
import java.util.Map;

public enum ConfigEnum {
	KEYWORD("warpKeyWord", "[ACWarp]", "set the keyword used to recognise a WarpSign.\n "
			+ "BE CAREFULL if you change it, older WarpSign will be not working."), 
	AUTOCOMPLETE(
			"autoComplete", true,
			"when typing the name of a world of a warp, if set to true, will autocomplete the Sign.");

	private final String confVal;
	private final Object defaultVal;
	private final String description;

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

	/**
	 * @return the confVal
	 */
	public String getConfVal() {
		return confVal;
	}

	/**
	 * @return the defaultVal
	 */
	public Object getDefaultVal() {
		return defaultVal;
	}

	/**
	 * @return the defaultvalues
	 */
	public static Map<String, Object> getDefaultvalues() {
		Map<String, Object> values = new HashMap<String, Object>();
		for (ConfigEnum ce : values())
			values.put(ce.getConfVal(), ce.getDefaultVal());
		return values;
	}

	public static String getHeader() {
		StringBuffer buffer = new StringBuffer();
		for (ConfigEnum ce : values())
			buffer.append(ce.confVal).append(" : ").append(ce.description).append(" (Default : ")
					.append(ce.defaultVal).append(')').append(System.getProperty("line.separator"));
		return buffer.toString();
	}
}
