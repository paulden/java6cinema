package com.movie.tests;

import java.io.IOException;

import com.movie.exceptions.NoPathException;
import com.movie.locations.Path;

/**
 * This class contains the testing programs to check the time-calculating program.
 * The test is about going from Centrale to Châtenay-Malabry's REX theater.
 * @author Renaud
 *
 */
public class TestTimeToCinema {

	public static void main(String[] args) {
		double origin_lat = 48.7648573;
		double origin_lng = 2.2885256;
		double dest_lat = 48.7659532;
		double dest_lng = 2.2599667;
		String mode = "walking";

		Path path;
		try {
			path = new Path(origin_lat, origin_lng, dest_lat, dest_lng, mode);
			System.out.println("Time in seconds : " + path.getValue());
			System.out.println("Human-readable time : " + path.getReadable());
		} catch (IOException|NoPathException e) {
			System.err.println(e.getMessage());
		}

	}

}
