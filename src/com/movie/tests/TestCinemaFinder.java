package com.movie.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
			Set<Path.ModeTrajet> modeTrajetPossible = new HashSet<>();
			modeTrajetPossible.add(ModeTrajet.WALKING);
			modeTrajetPossible.add(ModeTrajet.TRANSIT);
			
			Calendar departureTime = Calendar.getInstance();
			departureTime.set(Calendar.HOUR_OF_DAY, 18);
			departureTime.set(Calendar.MINUTE, 0);
			List<Seance> bestSeanceList = cinemaFinder.findBestSeances(90, 10000, "20 place de Beaume, Elancourt", departureTime,
					modeTrajetPossible, true);
			for(Seance seance : bestSeanceList) {
				System.out.println(seance);
			}

			/*
			Map<String, Film> filmSeanceListMap = cinemaFinder.findBestSeancesForEachFilm(90, 0, null, null, modeTrajetPossible, false);
			for(Film film : filmSeanceListMap.values()) {
				System.out.println(film);
			}
			*/

		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

	}

}
