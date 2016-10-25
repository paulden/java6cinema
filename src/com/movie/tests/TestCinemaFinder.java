package com.movie.tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.movie.cinema.CinemaFinder;
import com.movie.cinema.Film;
import com.movie.cinema.Seance;
import org.json.JSONException;

public class TestCinemaFinder {

	
	public TestCinemaFinder() {
		
	}

	public static void main(String[] args) {
		CinemaFinder cinemaFinder = new CinemaFinder();
		try {
			cinemaFinder.findClosestCinemas(5000);
			cinemaFinder.updateAllSeances();
			cinemaFinder.printCinemaList();

			cinemaFinder.updateTempsTrajet();
			cinemaFinder.printCinemaList();


			List<Seance> bestSeanceList = cinemaFinder.findBestSeances(null, null);
			for(Seance seance : bestSeanceList) {
				System.out.println(seance);
			}

			Map<String, Film> filmSeanceListMap = cinemaFinder.findBestSeancesForEachFilm(null, null);
			for(Film film : filmSeanceListMap.values()) {
				System.out.println(film);
			}

		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

	}

}
