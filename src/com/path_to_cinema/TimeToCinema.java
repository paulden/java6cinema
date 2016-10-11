package com.path_to_cinema;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

/**
 * This class contains the testing programs to check the time-calculating program.
 * The test is about going from Centrale to Châtenay-Malabry's REX theater.
 * @author Renaud
 *
 */
public class TimeToCinema {
	
	public static double origin_lat = 48.7648573;
	public static double origin_lng = 2.2885256;
	public static double dest_lat = 48.7659532;
	public static double dest_lng = 2.2599667;
	public static String mode = "walking";

	public static void main(String[] args) {
		
		Path path;
		try {
			path = new Path(origin_lat, origin_lng, dest_lat, dest_lng, mode);
			System.out.println("Time in seconds : " + path.getValue());
			System.out.println("Human-readable time : " + path.getReadable());
		} catch (ClientProtocolException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (NoPathException e) {
			System.err.println(e.getMessage());
		}

	}

}
