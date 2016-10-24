package com.cinema;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

public class TestCinemaFinder {

	public TestCinemaFinder() {
		// TODO Auto-generated constructor stub
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
			
			List<Seance> bestSeanceList = cinemaFinder.findBestSeances(0);
			for(Seance seance : bestSeanceList) {
				System.out.println(seance);
			}
			
			Map<Film, List<Seance>> filmSeanceListMap = cinemaFinder.findBestSeancesForEachFilm(0);
			for(Film film : filmSeanceListMap.keySet()) {
				List<Seance> seanceList = filmSeanceListMap.get(film);
				StringBuilder sb = new StringBuilder();
				sb.append(film.getName()).append(":[");
				for(Seance seance : seanceList) {
					sb.append(seance).append(",");
				}
				sb.deleteCharAt(sb.length()-1).append("]");
				System.out.println(sb.toString());
			}
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

	}

}
