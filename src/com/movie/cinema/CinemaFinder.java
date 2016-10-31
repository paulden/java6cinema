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
 * This class uses the methods from all other classes to find the cinemas that are closest to the user.<br>
 * It can also retrieve these cinema's showtime, to finally give the user a personal list of shows he can get to in time.
 * @author Kévin
 *
 */
public class CinemaFinder {

	/**
	 * A list of the cinemas close to the user
	 */
	private List<Cinema> cinemaList;

	/**
	 * A showtime list of movies the user can get to in time
	 */
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
	 * Sets the cinemaList with the list of cinemas within a given radius
	 * @param radius the search radius in meters
	 * @throws IOException in case of API connection problem
	 * @throws JSONException in case of unexpected JSON object received
	 */
	public void findClosestCinemas(double radius) throws JSONException, IOException {
		ClosestCinemas closestCinemas = new ClosestCinemas();
		closestCinemas.setClosestCinemas(radius);
		this.cinemaList = closestCinemas.getClosestCinemas();
	}
	
	/**
	 * Update the cinema list by adding the shows to the cinemas in cinemaList.
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
	 * Updates the cinemaList by adding the time it takes for the user to reach the cinema, using only specified transportation modes.
	 * @param originAddress : The users's location or the location he wants to start from
	 * @param modeTrajetPossible : A set of the transportation modes the user is willing to use to get to the cinema
	 */
	public void updateTempsTrajet(String originAddress, Set<Path.ModeTrajet> modeTrajetPossible) {
		if (modeTrajetPossible == null) {
			modeTrajetPossible = new HashSet<>();
			Collections.addAll(modeTrajetPossible, Path.ModeTrajet.values());
		}
		
		
		MyAddress myAdress;
		if(originAddress==null) {
			myAdress = new MyAddress();
		} else {
			myAdress = new MyAddress(originAddress);
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
	 * Finds a list of shows the user can get to based on the list of closest cinemas updated with the show list and the transportation time.<br>
	 * This method also sets the CinemaFinder's bestSeanceList attribute.
	 * @param minutes : the maximum time the user is wiling to wait before the show starts, in minutes
	 * @param departureTime : the moment when the user will be available to start going to a cinema (Calendar)
	 * @param modeTrajetPossible :  A set of the transportation modes the user is willing to use to get to the cinema
	 * @return A list containing the best shows for the user
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

	/**
	 * This function uses the findBestSeances method to return a movie/shows map. <br>
	 * It allows the user to see all the shows he can go to, sorted by movie.
	 * @param minutes : the maximum time the user is wiling to wait before the show starts, in minutes
	 * @param departureTime : the moment when the user will be available to start going to a cinema (Calendar)
	 * @param modeTrajetPossible :  A set of the transportation modes the user is willing to use to get to the cinema
	 * @return A movie/shows map
	 */
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


	/**
	 * This function goes through a list of shows to select only the ones the user can get to in the specified amount of time.<br>
	 * The selected shows are added to the bestSeanceList attribute.
	 * @param minutes : the maximum time the user is wiling to wait before the show starts, in minutes
	 * @param departureTime : the moment when the user will be available to start going to a cinema (Calendar)
	 * @param modeTrajetPossible :  A set of the transportation modes the user is willing to use to get to the cinema
	 * @param seanceList : a list of shows
	 */
	private void addBestSeancesFrom(int minutes, List<Seance> seanceList, Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		long millis = minutes * 60000;
		if (minutes == 0) {
			Calendar now = Calendar.getInstance();
			Calendar night = Calendar.getInstance();
			night.set(Calendar.HOUR_OF_DAY, 23);
			night.set(Calendar.MINUTE, 59);
			millis = night.getTimeInMillis() - now.getTimeInMillis();
		}

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
					int duration = tempsTrajetMap.get(mode);
					//Si l'heure de la séance est supérieur à l'heure du depart plus la durée du trajet, alors on ajoute cette séance.
					if(seanceMillisTime > departureTimeInMillis + (long) duration * 1000
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
