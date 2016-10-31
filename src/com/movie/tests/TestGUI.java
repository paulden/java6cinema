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
	 */

	public static void main(String[] args) {

		Window window = new Window();

		window.setVisible(true);

	}

}