package com.movie.tests;

import com.movie.gui.Window;
import java.io.IOException; 
import org.json.JSONException;

/**
 * Tests the GUI package methods
 */
public class TestGUI {
	
	/** Test designed to display the window Window built in the
	 * com.movie.gui package
	 * 
	 * - User is required to provide some information (distance max from cinema,
	 * means of transportation he can use, max duration before beginning)
	 * - Distance max is managed by a slider by default at 10 km
	 * - Means of transportation are managed via checkboxes. If none are checked,
	 * the program will automatically provide results for all means
	 * - Max duration is expressed in minutes. If the duration entered is invalid
	 * (e.g. no duration entered or invalid characters), the program will return
	 * results for a duration of 60 minutes.
	 */

	public static void main(String[] args) {

		Window window = new Window();

		window.setVisible(true);

	}

}