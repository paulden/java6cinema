package com.movie.tests;

import com.movie.gui.Fenetre;
import java.io.IOException; 
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.JFrame;

import javax.swing.*;

/**
 * Tests the GUI package methods
 */
public class TestGUI {
	
	/** Test designed to display the window Fenetre built in the
	 * com.movie.gui package 
	 * @param args
	 * @throws IOException
	 * @throws JSONException
	 */

	public static void main(String[] args) throws IOException, JSONException {
		
		Fenetre fenetre = new Fenetre();
	    
		fenetre.setVisible(true);

	}

}