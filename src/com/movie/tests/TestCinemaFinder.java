package com.movie.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.movie.cinema.Cinema;
import com.movie.cinema.CinemaFinder;
import com.movie.cinema.Film;
import com.movie.cinema.Seance;
import org.json.JSONException;

public class TestCinemaFinder {

	
	public TestCinemaFinder() {
		
	}

	/**
	 * Generates a hardcoded cinemaFinder to save time during tests.
	 * @return cinemaFinder : a hardcoded version of a simple cinema finder
	 */
	private static CinemaFinder generateHardCodedCinemaFinder() {
		CinemaFinder cinemaFinder = new CinemaFinder();

		List<Cinema> cinemaList = new ArrayList<>();
		Cinema cinema = new Cinema(
				"Le Scarron",
				"8 Avenue Jeanne et Maurice Dolivet, Fontenay-aux-Roses",
				48.79147520000001,
				2.2900317
		);

		Film film = new Film(
				"Les Sept Mercenaires",
				"2h12mn",
				"Classification: Tous publics",
				cinema
		);

		film.addSeanceVOSTFR(
				new Seance(film, 21, 0, cinema)
		);
		cinema.addFilm(film);
		cinemaList.add(cinema);
		cinemaFinder.setCinemaList(cinemaList);
		return cinemaFinder;
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
