package com.movie.cinema;

import java.io.IOException;
import java.util.*;

import com.movie.exceptions.HtmlParserException;
import com.movie.exceptions.NoPathException;
import com.movie.htmlparser.GoogleMoviesHtmlParser;
import com.movie.locations.ClosestTheaters;
import com.movie.locations.MyAddress;
import com.movie.locations.Path;

import org.json.JSONException;



/**
 * This class uses the methods from all other classes to find the theaters that are closest to the user.<br>
 * It can also retrieve these theater's showtime, to finally give the user a personal list of shows he can get to in time.
 * @author Kévin
 *
 */
public class TheaterFinder {

	/**
	 * A list of the theaters close to the user
	 */
	private List<Theater> theaterList;

	/**
	 * A showtime list of movies the user can get to in time
	 */
	private List<Screening> bestScreeningList;

	public TheaterFinder() {
		this.theaterList = new ArrayList<>();
	}
	
	public TheaterFinder(List<Theater> theaterList) {
		this.theaterList = theaterList;
	}

	public List<Theater> getTheaterList() {
		return theaterList;
	}

	public void setTheaterList(List<Theater> theaterList) {
		this.theaterList = theaterList;
	}
	
	/**
	 * Sets the theaterList with the list of theaters within a given radius
	 * @param radius the search radius in meters
	 * @throws IOException in case of API connection problem
	 * @throws JSONException in case of unexpected JSON object received
	 */
	public void updateClosestTheaters(String address, double radius) throws JSONException, IOException {
		ClosestTheaters closestTheaters = new ClosestTheaters();
		closestTheaters.setClosestTheaters(address, radius);
		this.theaterList = closestTheaters.getClosestTheaters();
	}
	
	/**
	 * Update the theater list by adding the shows to the theaters in theaterList.
	 */
	public void updateAllSeances() {
		for (Iterator<Theater> iterator = theaterList.iterator(); iterator.hasNext(); ) {
			Theater theater = iterator.next();
			try {
				GoogleMoviesHtmlParser.updateTheaterWithScreening(theater);
			} catch(HtmlParserException | IOException e) {
				System.err.println("Le cinéma " + theater.getName() + " situé au \'" + theater.getAddress() + "\'" + " n'a pas pu être mis à jour avec les séances car : ");
				e.printStackTrace();
				iterator.remove();
			}
		}
	}
	
	/**
	 * Updates the theaterList by adding the time it takes for the user to reach the theater, using only specified transportation modes.
	 * @param originAddress : The users's location or the location he wants to start from
	 * @param transportationModePossibilities : A set of the transportation modes the user is willing to use to get to the theater
	 */
	public void updateTempsTrajet(String originAddress, Set<Path.TransportationMode> transportationModePossibilities) {
		if (transportationModePossibilities == null) {
			transportationModePossibilities = new HashSet<>();
			Collections.addAll(transportationModePossibilities, Path.TransportationMode.values());
		}
		
		
		MyAddress myAdress;
		if(originAddress==null) {
			myAdress = new MyAddress();
		} else {
			myAdress = new MyAddress(originAddress);
		}
		
		double originLat = myAdress.getMyLat();
		double originLng = myAdress.getMyLng();
		
		for (Theater theater : theaterList) {
			double destLat = theater.getLat();
			double destLng = theater.getLng();
			for(Path.TransportationMode transportationMode : transportationModePossibilities) {
				Map<Path.TransportationMode, Integer> timeTransportationModeMap = theater.getTimeTransportationMap();
				try {	
					Path path = new Path(originLat, originLng, destLat, destLng, transportationMode.toString());
					timeTransportationModeMap.put(transportationMode, path.getValue());
				} catch(IOException | NoPathException e) {
					System.err.println("Le cinema n'a pas pu être mis à jour avec son temps de trajet car : " + e.getStackTrace());
				}
			}

		}
	}
	
	/**
	 * Finds a list of shows the user can get to based on the list of closest theaters updated with the show list and the transportation time.<br>
	 * This method also sets the theaterFinder's bestSeanceList attribute.
	 * @param minutes : the maximum time the user is wiling to wait before the show starts, in minutes
	 * @param departureTime : the moment when the user will be available to start going to a theater (Calendar)
	 * @param transportationModePossibilities :  A set of the transportation modes the user is willing to use to get to the theater
	 * @return A list containing the best shows for the user
	 */
	public List<Screening> findBestSeances(int minutes, double radius, String departureAdress, Calendar departureTime,
			Set<Path.TransportationMode> transportationModePossibilities, boolean update) throws JSONException, IOException {
		
		if(update) {
			this.bestScreeningList = new ArrayList<>();
			long time1 = System.currentTimeMillis();
			updateClosestTheaters(departureAdress, radius);
			long time2 = System.currentTimeMillis();
			System.out.println("Temps update closest cinema : " + String.valueOf(time2-time1));
			updateAllSeances();
			long time3 = System.currentTimeMillis();
			System.out.println("Temps update all seances : " + String.valueOf(time3-time2));
			updateTempsTrajet(departureAdress, transportationModePossibilities);
			long time4 = System.currentTimeMillis();
			System.out.println("Temps update temps trajet : " + String.valueOf(time4-time3));
		}	
		for(Theater theater : theaterList) {
			List<Movie> movieList = theater.getMovieList();
			try {
				for (Movie film : movieList) {
					List<Screening> screeningVFList = film.getScreeningVFList();
					List<Screening> screeningVOSTFRList = film.getScreeningVOSTFRList();
					addBestSeancesFrom(minutes, screeningVFList, departureTime, transportationModePossibilities);
					addBestSeancesFrom(minutes, screeningVOSTFRList, departureTime, transportationModePossibilities);
				}
			} catch (NullPointerException e) {
				System.out.println("No movie found for the cinema " + theater.getName());
			}
		}		
		
		return this.bestScreeningList;
	}

	/**
	 * This function uses the findBestSeances method to return a movie/shows map. <br>
	 * It allows the user to see all the shows he can go to, sorted by movie.
	 * @param minutes : the maximum time the user is wiling to wait before the show starts, in minutes
	 * @param departureTime : the moment when the user will be available to start going to a theater (Calendar)
	 * @param modeTrajetPossible :  A set of the transportation modes the user is willing to use to get to the theater
	 * @return A movie/shows map
	 */
public Map<String, Movie> findBestSeancesForEachFilm(int minutes, double radius, String departureAdress,
		Calendar departureTime, Set<Path.TransportationMode> modeTrajetPossible, boolean update) throws JSONException, IOException {
	findBestSeances(minutes, radius, departureAdress, departureTime, modeTrajetPossible, update);
		Map<String, Movie> filmMap = new HashMap<>();
		try {
			for (Screening seance : bestScreeningList) {
				String filmName = seance.getMovie().getName();
				Movie film;
				if (filmMap.containsKey(filmName)) {
					film = filmMap.get(filmName);
				} else {
					film = new Movie(filmName, seance.getMovie().getDuration(), seance.getMovie().getRated());
					filmMap.put(filmName, film);
				}
				List<Screening> seanceListVF = film.getScreeningVFList();
				List<Screening> seanceListVOSTFR = film.getScreeningVOSTFRList();
				if (seance.getLanguage() == Screening.Language.VF) {
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
	 * @param departureTime : the moment when the user will be available to start going to a theater (Calendar)
	 * @param modeTrajetPossible :  A set of the transportation modes the user is willing to use to get to the theater
	 * @param seanceList : a list of shows
	 */
	private void addBestSeancesFrom(int minutes, List<Screening> seanceList, Calendar departureTime, Set<Path.TransportationMode> modeTrajetPossible) {
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
			Collections.addAll(modeTrajetPossible, Path.TransportationMode.values());
		}
		Map<Path.TransportationMode, Boolean> seanceAddedMap = new HashMap<>();
		for(Path.TransportationMode mode : modeTrajetPossible) {
			seanceAddedMap.put(mode, false);
		}
		boolean allSeanceAdded = false;
		Iterator<Screening> it = seanceList.iterator();
		while(!allSeanceAdded && it.hasNext()) {
			Screening seance = it.next();
			Calendar seanceTime = seance.getDate();
			long seanceMillisTime = seanceTime.getTimeInMillis();

			Map<Path.TransportationMode, Integer> tempsTrajetMap = seance.getTheater().getTimeTransportationMap();
			boolean seanceAdded = false;
			for (Path.TransportationMode mode : tempsTrajetMap.keySet()) {
				if(modeTrajetPossible.contains(mode)) {
					int duration = tempsTrajetMap.get(mode);
					//Si l'heure de la séance est supérieur à l'heure du depart plus la durée du trajet, alors on ajoute cette séance.
					if(seanceMillisTime > departureTimeInMillis + (long) duration * 1000
							&&
							seanceMillisTime <= departureTimeInMillis + millis
							) {
						seance.getTransportationModeList().add(mode);
						if (!seanceAdded) {
							bestScreeningList.add(seance);
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
	
	public void printTheaterList() {
		if (theaterList.isEmpty()) {
			System.out.println("No theater found.");
		}
		for (Theater theater : theaterList) {
			System.out.println(theater);
		}
	}
	
}
