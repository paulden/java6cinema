package com.movie.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.movie.cinema.Cinema;
import com.movie.cinema.CinemaFinder;
import com.movie.cinema.Film;
import com.movie.cinema.Seance;
import com.movie.cinema.Seance.Language;
import com.movie.locations.Path;
import com.movie.locations.Path.ModeTrajet;

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
				"Classification: Tous publics"
		);

		film.addSeanceVOSTFR(
				new Seance(film, 21, 0, cinema, Language.VF)
		);
		cinema.addFilm(film);
		cinemaList.add(cinema);
		cinemaFinder.setCinemaList(cinemaList);
		return cinemaFinder;
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
