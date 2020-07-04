package edu.miu.reversi;

import java.awt.Color;

public class Globals
{
	public static final int MINIMUM_WINDOW_WIDTH = 620;
	public static final int MINIMUM_WINDOW_HEIGHT = 706;
	public static final Color SPLASH_BACKGROUND_COLOR = new Color(153, 114, 13);
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	public static final Color BOARD_COLOR = new Color(153, 122, 21);
	public static final int GRID_SIZE_INTEGER = 8;
	public static final double GRID_SIZE_DECIMAL = GRID_SIZE_INTEGER * 1.0;
	public static final int GRID_LINE_WIDTH = 2;
	
	public static final String APPLICATION_TITLE = "ASD Course Project 2020 ";
	public static final int LABEL_FONT_SIZE = 12;
	public static final int BUTTON_VERTICAL_INSET = 2;
	public static final int BUTTON_HORIZONTAL_INSET = 20;
	public static final int TEXTFIELD_SIZING_FONT = 16;
	public static final int AFTER_COMPONENT_SPACER_WIDTH = 16;
	public static final int AFTER_LABEL_SPACER_WIDTH = 4;
	public static final int COMPUTER_MOVE_BLINKS = 3;
	public static final int BLINKING_SPEED_MILLISECONDS = 300;
	
	public static final int[][] START_POS = new int[][]{{3, 3}, {4, 3}, {3, 4}, {4, 4}};
	
	public static final int MINIMUM_OPTIONS_WINDOW_WIDTH = 300;
	public static final int MINIMUM_OPTIONS_WINDOW_HEIGHT = 300;
	public static final String OPTIONS_WINDOW_TITLE = "Options";
	
}
