package com.irishabroad.playergraphs;

import java.awt.Color;
import java.awt.Font;

public class Constants {

	private Constants() {
		// Private constructor
	}
	
	public static final Color BACKGROUND_COLOUR = new Color(245, 245, 245);
	
	public static final Color[] PLAYER_COLOURS = new Color[] {	
			new Color(19, 148, 66),
			new Color(66, 206, 244),
			Color.decode("#fcb913"),
			Color.decode("#39b54a"),
			Color.decode("#d1d3d4"),
			new Color(19, 148, 66),
			new Color(66, 206, 244),
			Color.decode("#fcb913"),
			Color.decode("#39b54a"),
			Color.decode("#d1d3d4")
	};
	
	public static final String UBUNTU = "Ubuntu";
	
	public static final Font PLAYER_NAME_FONT = new Font(UBUNTU, Font.PLAIN, 20);
	public static final Font NUMBER_INDEX_FONT = new Font(UBUNTU, Font.PLAIN, 15);
	public static final Font CAPS_FONT = new Font(UBUNTU, Font.BOLD, 23);
	public static final Font YEARS_FONT = new Font(UBUNTU, Font.BOLD, 23);
	public static final Font MONTHS_FONT = new Font(UBUNTU, Font.BOLD, 18);

}
