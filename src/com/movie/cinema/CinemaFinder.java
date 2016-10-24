package com.movie.cinema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.movie.exceptions.HtmlParserException;
import com.movie.exceptions.NoPathException;
import com.movie.htmlparser.GoogleMoviesHtmlParser;
import com.movie.locations.ClosestCinemas;
import com.movie.locations.MyAddress;
import com.movie.locations.Path;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;



/**
 * Class récupérant les cinémas les plus proches, puis récupérant les horaires de ces cinémas, pour<br>
 * enfin pouvoir trouver les cinémas les plus adaptés en fonction des séances et des distances aux cinémas.
 * @author Kévin
 *
 */
public class CinemaFinder {
	
	private List<Cinema> cinemaList;
	
	private List<Seance> bestSeanceList;

	public CinemaFinder() {
		this.cinemaList = new ArrayList<>();
	}
	
	public CinemaFinder(List<Cinema> cinemaList) {
		this.cinemaList = cinemaList;
	}

	public List<Cinema> getCinemaList() {
		return cinemaList;
	}

	public void setCinemaList(List<Cinema> cinemaList) {
		this.cinemaList = cinemaList;
	}
	
	/**
	 * Permet de set la liste des cinémas avec la liste des cinémas les plus proches.
	 * @param radius Le rayon de la recherche en mètres
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ClientProtocolException 
	 */
	public void findClosestCinemas(double radius) throws ClientProtocolException, JSONException, IOException {
		ClosestCinemas closestCinemas = new ClosestCinemas();
		closestCinemas.setClosestCinemas(radius);
		List<Cinema> closestCinemaList = closestCinemas.getClosestCinemas();
		this.cinemaList = closestCinemaList;
	}
	
	/**
	 * Permet d'update la liste des cinémas avec leurs séances.
	 */
	public void updateAllSeances() {
		for (Cinema cinema : cinemaList) {
			try {
				GoogleMoviesHtmlParser.updateCinemaWithSeances(cinema);
			} catch(HtmlParserException | IOException e) {
				System.err.println("Le cinéma " + cinema.getNom() + " situé au \'" + cinema.getAdresse() + "\'" + " n'a pas pu être mis à jour avec les séances car : ");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Permet d'update la liste des cinémas avec le temps de trajet entre la personne et le cinéma.
	 */
	public void updateTempsTrajet() {
		for (Cinema cinema : cinemaList) {
			for(Path.ModeTrajet modeTrajet : Path.ModeTrajet.values()) {
				Map<Path.ModeTrajet, Integer> cinemaMap = cinema.getTempsTrajetMap();
				try {
					MyAddress myAdress = new MyAddress();
					double originLat = myAdress.getMyLat();
					double originLng = myAdress.getMyLng();
					
					double destLat = cinema.getLat();
					double destLng = cinema.getLng();
					
					Path path = new Path(originLat, originLng, destLat, destLng, modeTrajet.toString());
					cinemaMap.put(modeTrajet, path.getValue());
				} catch(IOException | NoPathException e) {
					System.err.println("Le cinema n'a pas pu être mis à jour avec son temps de trajet car : " + e.getStackTrace());
				}
			}

		}
	}
	
	/**
	 * Permet de trouver les meilleurs séances à partir de la liste des cinémas mis à jour avec les séances et les temps de trajets.
	 * @param nombreSeance Le nombre de séances à trouver
	 * @return Une liste de taille nombreSeance contenant les meilleurs séances.
	 */
	public List<Seance> findBestSeances(int nombreSeance) {
		this.bestSeanceList = new ArrayList<>();
		
		for(Cinema cinema : cinemaList) {
			List<Film> filmCinemaList = cinema.getFilmList();
			for(Film film : filmCinemaList) {
				List<Seance> seanceVFFilmCinemaList = film.getSeanceListVF();
				List<Seance> seanceVOSTFRFilmCinemaList = film.getSeanceListVOSTFR();
				addBestSeancesFrom(seanceVFFilmCinemaList, nombreSeance, null, null);
				addBestSeancesFrom(seanceVOSTFRFilmCinemaList, nombreSeance, null, null);
			}
		}		
		
		return this.bestSeanceList;
	}
	
	public Map<Film, List<Seance>> findBestSeancesForEachFilm(int nombreSeance) {
		findBestSeances(nombreSeance);
		Map<Film, List<Seance>> filmSeanceListMap = new HashMap<>();
		for(Seance seance : bestSeanceList) {
			Film film = seance.getFilm();
			if (filmSeanceListMap.containsKey(film)) {
				List<Seance> seanceList = filmSeanceListMap.get(film);
				seanceList.add(seance);
			} else {
				List<Seance> seanceList = new ArrayList<>();
				seanceList.add(seance);
				filmSeanceListMap.put(film, seanceList);
			}
		}
		return filmSeanceListMap;
	}
	
	private void addBestSeancesFrom(List<Seance> seanceList, int nombreSeance, Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		if (departureTime==null) {
			departureTime = Calendar.getInstance();
		}
		
		if (modeTrajetPossible==null) {
			modeTrajetPossible = new HashSet<>();
			for(Path.ModeTrajet modeTrajet : Path.ModeTrajet.values()) {
				modeTrajetPossible.add(modeTrajet);
			}
		}
		Map<Path.ModeTrajet, Boolean> seanceAddedMap = new HashMap<>();
		for(Path.ModeTrajet mode : modeTrajetPossible) {
			seanceAddedMap.put(mode, false);
		}
		boolean allSeanceAdded = false;
		Iterator<Seance> it = seanceList.iterator();
		do {
			Seance seance = it.next();
			Calendar seanceTime = seance.getDate();
			Map<Path.ModeTrajet, Integer> tempsTrajetMap = seance.getCinema().getTempsTrajetMap();
			for (Path.ModeTrajet mode : tempsTrajetMap.keySet()) {
				if(modeTrajetPossible.contains(mode)) {
					int duree = tempsTrajetMap.get(mode);
					//Si l'heure de la séance est inférieur à l'heure du depart plus la durée du trajet, alors on ajoute cette séance.
					if(seanceTime.getTimeInMillis() > departureTime.getTimeInMillis() + (long) duree * 1000) {
						seance.setModeTrajet(mode);
						bestSeanceList.add(seance);
						seanceAddedMap.put(mode, true);
					}
					
					allSeanceAdded = true;
					for(Boolean bool : seanceAddedMap.values()) {
						if(!bool) {
							allSeanceAdded = false;
						}
					}
				}
			}
		}while(!allSeanceAdded && it.hasNext());
	}
	
	
	
}
