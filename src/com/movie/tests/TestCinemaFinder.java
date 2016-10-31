package com.movie.tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.movie.cinema.CinemaFinder;
import com.movie.cinema.Film;
import com.movie.cinema.Seance;
import com.movie.locations.Path;
import com.movie.locations.Path.ModeTrajet;

import org.json.JSONException;

/**
 * Tests the CinemaFinder class methods. <br>
 * The goal is to give a user a list of possible shows.
 */
public class TestCinemaFinder {

	
	public TestCinemaFinder() {
	}

	public static void main(String[] args) {

		CinemaFinder cinemaFinder = new CinemaFinder();
		try {
			// find the closest cinema in a 5km radius
			cinemaFinder.findClosestCinemas(5000);
			// update with the shows
			cinemaFinder.updateAllSeances();
			cinemaFinder.printCinemaList();

			// We choose to test the case where the user only wants to walk or use public transportation
			Set<Path.ModeTrajet> modeTrajetPossible = new HashSet<>();
			modeTrajetPossible.add(ModeTrajet.WALKING);
			modeTrajetPossible.add(ModeTrajet.TRANSIT);

			// We test the functions for a user located in Centrale
			cinemaFinder.updateTempsTrajet("3 avenue sully prud'homme", modeTrajetPossible);
			cinemaFinder.printCinemaList();

			// Printing the shows that were found
			List<Seance> bestSeanceList = cinemaFinder.findBestSeances(0, null, modeTrajetPossible);
			for(Seance seance : bestSeanceList) {
				System.out.println(seance);
			}

			Map<String, Film> filmSeanceListMap = cinemaFinder.findBestSeancesForEachFilm(90, null, null);
			for(Film film : filmSeanceListMap.values()) {
				System.out.println(film);
			}

		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

	}

}
