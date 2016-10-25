package com.movie.cinema;

import java.io.IOException;
import java.util.*;

import com.movie.exceptions.HtmlParserException;
import com.movie.exceptions.NoPathException;
import com.movie.htmlparser.GoogleMoviesHtmlParser;
import com.movie.locations.ClosestCinemas;
import com.movie.locations.MyAddress;
import com.movie.locations.Path;
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
	 * @throws IOException in case of API connection problem
	 * @throws JSONException in case of unexpected JSON object recieved
	 */
	public void findClosestCinemas(double radius) throws JSONException, IOException {
		ClosestCinemas closestCinemas = new ClosestCinemas();
		closestCinemas.setClosestCinemas(radius);
		this.cinemaList = closestCinemas.getClosestCinemas();
	}
	
	/**
	 * Permet d'update la liste des cinémas avec leurs séances.
	 */
	public void updateAllSeances() {
		for (Iterator<Cinema> iterator = cinemaList.iterator(); iterator.hasNext(); ) {
			Cinema cinema = iterator.next();
			try {
				GoogleMoviesHtmlParser.updateCinemaWithSeances(cinema);
			} catch(HtmlParserException | IOException e) {
				System.err.println("Le cinéma " + cinema.getNom() + " situé au \'" + cinema.getAdresse() + "\'" + " n'a pas pu être mis à jour avec les séances car : ");
				e.printStackTrace();
				iterator.remove();
			}
		}
	}
	
	/**
	 * Permet d'update la liste des cinémas avec le temps de trajet entre la personne et le cinéma.
	 */
	public void updateTempsTrajet(String adresseDepart, Set<Path.ModeTrajet> modeTrajetPossible) {
		if (modeTrajetPossible == null) {
			modeTrajetPossible = new HashSet<>();
			Collections.addAll(modeTrajetPossible, Path.ModeTrajet.values());
		}
		
		
		MyAddress myAdress;
		if(adresseDepart==null) {
			myAdress = new MyAddress();
		} else {
			myAdress = new MyAddress(adresseDepart);
		}
		
		double originLat = myAdress.getMyLat();
		double originLng = myAdress.getMyLng();
		
		for (Cinema cinema : cinemaList) {
			double destLat = cinema.getLat();
			double destLng = cinema.getLng();
			for(Path.ModeTrajet modeTrajet : modeTrajetPossible) {
				Map<Path.ModeTrajet, Integer> cinemaMap = cinema.getTempsTrajetMap();
				try {	
					Path path = new Path(originLat, originLng, destLat, destLng, modeTrajet.toString());
					cinemaMap.put(modeTrajet, path.getValue());
				} catch(IOException | NoPathException e) {
					System.err.println("Le cinema n'a pas pu être mis à jour avec son temps de trajet car : " + e.getStackTrace());
				}
			}

		}
	}
	
	/**
	 * Permet de trouver les séances auxquelles l'utilisateur peut assister à partir de la liste des cinémas mis à jour avec les séances et les temps de trajet.
	 * @return Une liste contenant les meilleures séances.
	 */
	public List<Seance> findBestSeances(int minutes, Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		this.bestSeanceList = new ArrayList<>();
		
		for(Cinema cinema : cinemaList) {
			List<Film> filmCinemaList = cinema.getFilmList();
			try {
				for (Film film : filmCinemaList) {
					List<Seance> seanceVFFilmCinemaList = film.getSeanceListVF();
					List<Seance> seanceVOSTFRFilmCinemaList = film.getSeanceListVOSTFR();
					addBestSeancesFrom(minutes, seanceVFFilmCinemaList, departureTime, modeTrajetPossible);
					addBestSeancesFrom(minutes, seanceVOSTFRFilmCinemaList, departureTime, modeTrajetPossible);
				}
			} catch (NullPointerException e) {
				System.out.println("No movie found for the cinema " + cinema.getNom());
			}
		}		
		
		return this.bestSeanceList;
	}
	
	public Map<String, Film> findBestSeancesForEachFilm(int minutes, Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		findBestSeances(minutes, departureTime, modeTrajetPossible);
		Map<String, Film> filmMap = new HashMap<>();
		try {
			for (Seance seance : bestSeanceList) {
				String filmName = seance.getFilm().getName();
				Film film;
				if (filmMap.containsKey(filmName)) {
					film = filmMap.get(filmName);
				} else {
					film = new Film(filmName, seance.getFilm().getDuree(), seance.getFilm().getRated());
					filmMap.put(filmName, film);
				}
				List<Seance> seanceListVF = film.getSeanceListVF();
				List<Seance> seanceListVOSTFR = film.getSeanceListVOSTFR();
				if (seance.getLanguage() == Seance.Language.VF) {
					seanceListVF.add(seance);
				} else {
					seanceListVOSTFR.add(seance);
				}
			}
		} catch (NullPointerException e) {
			System.out.println("No movie found.");
		}
		return filmMap;
	}


	
	private void addBestSeancesFrom(int minutes, List<Seance> seanceList, Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		int millis = minutes * 60000;

		if (departureTime==null) {
			departureTime = Calendar.getInstance();
		}
		long departureTimeInMillis = departureTime.getTimeInMillis();
		
		if (modeTrajetPossible==null) {
			modeTrajetPossible = new HashSet<>();
			Collections.addAll(modeTrajetPossible, Path.ModeTrajet.values());
		}
		Map<Path.ModeTrajet, Boolean> seanceAddedMap = new HashMap<>();
		for(Path.ModeTrajet mode : modeTrajetPossible) {
			seanceAddedMap.put(mode, false);
		}
		boolean allSeanceAdded = false;
		Iterator<Seance> it = seanceList.iterator();
		while(!allSeanceAdded && it.hasNext()) {
			Seance seance = it.next();
			Calendar seanceTime = seance.getDate();
			long seanceMillisTime = seanceTime.getTimeInMillis();

			Map<Path.ModeTrajet, Integer> tempsTrajetMap = seance.getCinema().getTempsTrajetMap();
			boolean seanceAdded = false;
			for (Path.ModeTrajet mode : tempsTrajetMap.keySet()) {
				if(modeTrajetPossible.contains(mode)) {
					int duree = tempsTrajetMap.get(mode);
					//Si l'heure de la séance est supérieur à l'heure du depart plus la durée du trajet, alors on ajoute cette séance.
					if(seanceMillisTime > departureTimeInMillis + (long) duree * 1000
							&&
							seanceMillisTime <= departureTimeInMillis + millis
							) {
						seance.getModeTrajetList().add(mode);
						if (!seanceAdded) {
							bestSeanceList.add(seance);
							seanceAdded = true;
						}	
						seanceAddedMap.put(mode, true);
					}
				}
			}
			allSeanceAdded = true;
			for(Boolean bool : seanceAddedMap.values()) {
				if(!bool) {
					allSeanceAdded = false;
				}
			}
		}
	}
	
	public void printCinemaList() {
		if (cinemaList.isEmpty()) {
			System.out.println("No cinema found.");
		}
		for (Cinema cinema : cinemaList) {
			System.out.println(cinema);
		}
	}
	
}
