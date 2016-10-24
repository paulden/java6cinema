package com.movie.tests;

import com.movie.gui.Fenetre;
import java.io.IOException; 
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.JFrame;

import javax.swing.*;

public class TestGUI {
	
	private static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	public static double lat = 48.7648573;
	public static double lng = 2.2885256;
	public static double radius = 5000;
	//public static ClosestCinemas closestCinemas = new ClosestCinemas();

	public static void main(String[] args) throws IOException, JSONException {
		
		Fenetre fenetre = new Fenetre();
	    
		fenetre.setVisible(true);

	}

}