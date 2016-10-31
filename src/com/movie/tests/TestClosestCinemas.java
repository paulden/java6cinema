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

	private static ClosestCinemas closestCinemas = new ClosestCinemas();

	/**
	 * Tests the setClosestCinemas method for a 10km-radius
	 */
	public static void main(String[] args) throws IOException, JSONException {

		closestCinemas.setClosestCinemas(10000);
		List<Cinema> cinemaList = closestCinemas.getClosestCinemas();

		for(Cinema cinema : cinemaList){
			System.out.println(cinema.toString());
		}
	}

}
