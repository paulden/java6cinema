package com.movie.tests;

import java.io.IOException;
import java.util.List;

import com.movie.cinema.Theater;
import com.movie.locations.ClosestTheaters;
import org.json.JSONException;

/**
 * Tests the ClosestCinema class methods.
 */
public class TestClosestCinemas {
	
	private static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	public static double lat = 48.7648573;
	public static double lng = 2.2885256;
	public static double radius = 5000;

	private static ClosestTheaters closestCinemas = new ClosestTheaters();

	/**
	 * Tests the setClosestCinemas method for a 10km-radius
	 */
	public static void main(String[] args) {

		try {
			closestCinemas.setClosestTheaters("avenue sully prudhomme", 2000);
			List<Theater> cinemaList = closestCinemas.getClosestTheaters();
			System.out.println(closestCinemas.getMyAddress());

			for (Theater cinema : cinemaList) {
				System.out.println(cinema.toString());
			}
		} catch (IOException | JSONException e) {
			System.out.println("An error occurred :");
			e.printStackTrace();
		}
	}

}
