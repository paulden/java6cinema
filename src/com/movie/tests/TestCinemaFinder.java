package com.movie.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.movie.cinema.TheaterFinder;
import com.movie.cinema.Movie;
import com.movie.cinema.Screening;
import com.movie.locations.Path;
import com.movie.locations.Path.TransportationMode;

import org.json.JSONException;

/**
 * Tests the CinemaFinder class methods. <br>
 * The goal is to give a user a list of possible shows.
 */
public class TestCinemaFinder {

	
	public TestCinemaFinder() {
	}

	public static void main(String[] args) {

		TheaterFinder cinemaFinder = new TheaterFinder();
		try {
			Set<Path.TransportationMode> modeTrajetPossible = new HashSet<>();
			modeTrajetPossible.add(TransportationMode.WALKING);
			modeTrajetPossible.add(TransportationMode.TRANSIT);
			
			Calendar departureTime = Calendar.getInstance();
			departureTime.set(Calendar.HOUR_OF_DAY, 18);
			departureTime.set(Calendar.MINUTE, 0);
			List<Screening> bestSeanceList = cinemaFinder.findBestSeances(90, 10000, "20 place de Beaume, Elancourt", departureTime,
					modeTrajetPossible, true);
			for(Screening seance : bestSeanceList) {
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
