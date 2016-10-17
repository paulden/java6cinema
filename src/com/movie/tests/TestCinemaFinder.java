package com.movie.tests;

import java.io.IOException;

import com.movie.cinema.Cinema;
import com.movie.cinema.CinemaFinder;
import org.json.JSONException;

public class TestCinemaFinder {

	public TestCinemaFinder() {
	}

	public static void main(String[] args) {
		CinemaFinder cinemaFinder = new CinemaFinder();
		try {
			cinemaFinder.findClosestCinemas(5000);
			cinemaFinder.updateAllSeances();
			for (Cinema cinema : cinemaFinder.getCinemaList()) {
				System.out.println(cinema);
			}
			cinemaFinder.updateTempsTrajet();
			for (Cinema cinema : cinemaFinder.getCinemaList()) {
				System.out.println(cinema);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

	}

}
