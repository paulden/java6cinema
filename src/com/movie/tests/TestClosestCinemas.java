package com.movie.tests;

import java.io.IOException;
import java.util.List;

import com.movie.cinema.Cinema;
import com.movie.locations.ClosestCinemas;
import org.json.JSONException;

/**
 * Tests the ClosestCinema class methods.
 */
public class TestClosestCinemas {
	
	private static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	public static double lat = 48.7648573;
	public static double lng = 2.2885256;
	public static double radius = 5000;

	private static ClosestCinemas closestCinemas = new ClosestCinemas();

	/**
	 * Tests the setClosestCinemas method for a 10km-radius
	 */
	public static void main(String[] args) {

		try {
			closestCinemas.setClosestCinemas("avenue sully prudhomme", 2000);
			List<Cinema> cinemaList = closestCinemas.getClosestCinemas();
			System.out.println(closestCinemas.getMyAddress());

			for (Cinema cinema : cinemaList) {
				System.out.println(cinema.toString());
			}
		} catch (IOException | JSONException e) {
			System.out.println("An error occurred :");
			e.printStackTrace();
		}
	}

}
