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
	 * Permet de trouver les séances auxquelles l'utilisateur peut assister à partir de la liste des cinémas mis à jour avec les séances et les temps de trajet.
	 * @return Une liste contenant les meilleures séances.
	 */
	public List<Seance> findBestSeances(Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		this.bestSeanceList = new ArrayList<>();
		
		for(Cinema cinema : cinemaList) {
			List<Film> filmCinemaList = cinema.getFilmList();
			try {
				for (Film film : filmCinemaList) {
					List<Seance> seanceVFFilmCinemaList = film.getSeanceListVF();
					List<Seance> seanceVOSTFRFilmCinemaList = film.getSeanceListVOSTFR();
					addBestSeancesFrom(seanceVFFilmCinemaList, departureTime, modeTrajetPossible);
					addBestSeancesFrom(seanceVOSTFRFilmCinemaList, departureTime, modeTrajetPossible);
				}
			} catch (NullPointerException e) {
				System.out.println("No movie found for the cinema " + cinema.getNom());
			}
		}		
		
		return this.bestSeanceList;
	}
	
	public Map<String, Film> findBestSeancesForEachFilm(Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		findBestSeances(departureTime, modeTrajetPossible);
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
	 * Get a list of seances the user has time to go to, within the amount of time he specified.
	 * @param minutes the maximum time wanted by the user before a seance starts
	 * @return seanceList a list of all the seances the user can go to in the amount of time he specified
	 */
	public List<Seance> findSeancesWithTimeConstraint(int minutes) {
		List<Seance> targetSeances = findBestSeances(null, null);
		int millis = minutes * 60000;

		for (Iterator<Seance> iterator = targetSeances.iterator(); iterator.hasNext(); ) {
			Seance seance = iterator.next();
			if (seance.getDate().getTimeInMillis() > millis) {
				iterator.remove();
			}
		}

		return targetSeances;
	}
	
	private void addBestSeancesFrom(List<Seance> seanceList, Calendar departureTime, Set<Path.ModeTrajet> modeTrajetPossible) {
		if (departureTime==null) {
			departureTime = Calendar.getInstance();
		}
		
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
			Map<Path.ModeTrajet, Integer> tempsTrajetMap = seance.getCinema().getTempsTrajetMap();
			for (Path.ModeTrajet mode : tempsTrajetMap.keySet()) {
				if(modeTrajetPossible.contains(mode)) {
					int duree = tempsTrajetMap.get(mode);
					boolean seanceAdded = false;
					//Si l'heure de la séance est supérieur à l'heure du depart plus la durée du trajet, alors on ajoute cette séance.
					if(seanceTime.getTimeInMillis() > departureTime.getTimeInMillis() + (long) duree * 1000) {
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
