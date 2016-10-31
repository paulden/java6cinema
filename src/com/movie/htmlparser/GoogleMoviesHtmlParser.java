package com.movie.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.movie.exceptions.HtmlParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.movie.cinema.Cinema;
import com.movie.cinema.Film;
import com.movie.cinema.Seance;

/**
 * GoogleMoviesHtmlParser retrieves a show list close to a specific place, or the show times from a given cinema, <br>
 * from the Google Movies website, via HTML parsing.
 * The various methods return a {@link Cinema} or a list of {@link Cinema}  list containing the movies with the show times.
 * @author Kévin
 *
 */
public final class GoogleMoviesHtmlParser {
	
	/**
	 * Static string defining if a movie's language is French
	 */
	private static String VF = "Dubbed in French";
	
	/**
	 * Static String defining if the movie has french subtitles
	 */
	private static String VOSTFR = "Subtitled in French";
	
	/**
	 * Private constructor.
	 */
	private GoogleMoviesHtmlParser() {
		
	}
	
	
	/**
	 * Static function getting all {@link Cinema} with their shows close to a specific place.
	 * @param place a city or street name
	 * @return a list of {@link Cinema} updated with their shows
	 * @throws IOException if there is a failure while accessing Google Movies website
	 */
	public static List<Cinema> getAllCinemaWithSeancesNearAPlace(String place) throws IOException{
		Document doc;
		Element movieResults;
		
		List<Cinema> cinemaList = new ArrayList<>();

			// Allows to display the list starting from a n-th cinema.
			int start = 0;
			do {
				
				// Get HTML code from Google movies
				doc = Jsoup.connect("https://www.google.com/movies?hl=fr&near=" + place + "&start="+start).get();
				movieResults = doc.getElementById("movie_results");
				
				// If there is a "movie_results" tag in the HTML, it might have found some cinemas.
				if(movieResults!=null) {
					
					// We get a cinema list that can be empty if no cinema was found.
					Elements allTheaters = movieResults.getElementsByClass("theater");
					
					// For each HTML element of the list, we create a Cinema object containing the relevant data
					for(Element theater : allTheaters) {
						Cinema cinema = GoogleMoviesHtmlParser.getCinemaFromHtmlTheaterElement(theater);
						cinemaList.add(cinema);
					}
				}
				// Increasing the search start by 10 to go to the next set of results
				start+=10;
			
			
			}while(movieResults!=null); // When we don't get anymore results we stop.
			
		return cinemaList;
	}
	
	/**
	 * Returns a {@link Cinema} object containing all the {@link Seance } of all the {@link Film } being shown in this cinema.
	 * @param cinemaName the cinema's name
	 * @return a {@link Cinema} with all relevant information about movies and shows.
	 * @throws IOException if there is a failure while accessing Google Movies website
	 * @throws HtmlParserException If the parser found no matching cinema name or too many matching cinema names
	 */
	public static Cinema getCinemaWithSeances(String cinemaName) throws IOException, HtmlParserException {
		Document doc;
		Element movieResults;
		
		// Create the query from the cinema name
		String query = cinemaName.replace(" ", "+");
		
		// Retrieve the HTML code from google movies
		doc = Jsoup.connect("https://www.google.com/movies?hl=fr&q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		
		// If there is a "movie_results" tag in the HTML, it might have found some cinemas.
		if(movieResults!=null) {
			// Gathering the cinema result list (can be empty)
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			// If the list contains only one element, the we found the right cinema and we return a Cinema object with all the gathered data.
			if (allTheaters == null) {
				throw new HtmlParserException("Le cinema " + cinemaName + " est introuvable.");
			} else if (allTheaters.size()==0) {
				throw new HtmlParserException("Le cinema " + cinemaName + " est introuvable.");
			} else if (allTheaters.size()>1) {
				throw new HtmlParserException("Plusieurs cinémas ont été trouvés sous ce nom " + cinemaName + ".");
			} else {
				Element theater = allTheaters.first();
				return GoogleMoviesHtmlParser.getCinemaFromHtmlTheaterElement(theater);
			}
		} else { // Otherwise no cinema has been found and we throw an exception.
			throw new HtmlParserException("Aucun cinéma n'a été trouvé sous le nom " + cinemaName);
		}
	}
	
	/**
	 * Returns a {@link Cinema} list matching a name, conatining all the {@link Seance} of the different {@link Film} being shown in these cinemas.
	 * @param cinemaName the cinema name
	 * @return A list of cinemas mathcing the given name, with all relevant information about movies and shows.
	 * @throws IOException if there is a failure while accessing Google Movies website
	 */
	public static List<Cinema> getAllCinemaWithSeances(String cinemaName) throws IOException {
		Document doc;
		Element movieResults;
		
		// Create the query from the cinema name
		String query = cinemaName.replace(" ", "+");
		
		// Create the cinema list we will return
		List<Cinema> cinemaList = new ArrayList<>();
		
		// get HTML code from google movies
		doc = Jsoup.connect("https://www.google.com/movies?hl=fr&q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		
		// If there is a "movie_results" tag in the HTML, it might have found some cinemas.
		if(movieResults!=null) {
			// Gathering the cinema result list (can be empty)
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			// For each HTML element of the list, we create a Cinema object containing the relevant data
			for(Element theater : allTheaters) {
				Cinema cinema = GoogleMoviesHtmlParser.getCinemaFromHtmlTheaterElement(theater);
				cinemaList.add(cinema);
			}
			
			return cinemaList;
		} else { // Otherwise we return an empty list
			return cinemaList;
		}
	}

	/**
	 * Updates a {@link Cinema} object with all the {@link Seance} of the different {@link Film} being shown in it.
	 * @param cinema the cinema object to update
	 * @throws IOException if there is a failure while accessing google movies
	 * @throws HtmlParserException if no such cinema has been found
	 */
	public static void updateCinemaWithSeances(Cinema cinema) throws IOException, HtmlParserException{
		String cinemaName = cinema.getNom();
		String cinemaAddress = cinema.getAdresse();
		List<Cinema> cinemaList = GoogleMoviesHtmlParser.getAllCinemaWithSeances(cinemaName);
		
		Cinema bestCinema = null;
		double bestMatchingAddress = 0;
		// For each cinema, we compare the addresses and return only the cinema with the address that matches the best
		for (Cinema cinemaFound : cinemaList) {
			String addressFound = cinemaFound.getAdresse();
			cinemaAddress = cinemaAddress.replace(",", "");
			cinemaAddress = addressFound.replace("-", " ");
			String[] addressSplit = cinemaAddress.split(" ");
			double matching = 0;
			for (String word : addressSplit) {
				if (addressFound.contains(word)) {
					matching++;
				}
			}
			matching/=addressSplit.length;
			if (matching>bestMatchingAddress) {
				bestMatchingAddress = matching;
				bestCinema = cinemaFound;
			}
		}
		
		if(bestCinema != null) {
			cinema.setFilmList(bestCinema.getFilmList());
		} else {
			throw new HtmlParserException("Aucun cinéma n'a été trouvé correspondant au nom " + cinemaName + " et à l'adresse " + cinemaAddress);
		}
		
	}
	
	/**
	 * Gets a {@link Cinema} object from the right HTML element, containing all the {@link Seance} of the different {@link Film} being shown in it.
	 * @param theater the HTML element
	 * @return the {@link Cinema} object with the relevant data.
	 */
	private static Cinema getCinemaFromHtmlTheaterElement (Element theater) {
		// We retrieve the "desc" class element which contains the cinema description
		Element cinemaElement = theater.getElementsByClass("desc").first();
		String name = cinemaElement.getElementsByClass("name").first().text();
		String address = cinemaElement.getElementsByClass("info").first().text();
		
		// Create a cinema object based on the name and address
		Cinema cinema = new Cinema(name, address);
		
		// Get the movie list (can be empty)
		Elements moviesList = theater.getElementsByClass("movie");
					
		// FOr each movie we gather the shows information and add it to the cinema object
		for(Element movie : moviesList) {
			
			// Get the movie title
			String nameMovie = movie.getElementsByClass("name").first().text();
			
			// Get the movie information (separated by "-"). THe first one is the duration and the second one the rating.
			String info = movie.getElementsByClass("info").first().text();
			
			String[] infoList = info.split(" - ");
			String duration;
			if(infoList.length>=1) {
				duration = infoList[0];
			} else {
				duration = "N/C";
			}
			
			String rated;
			if (infoList.length>=2) {
				rated = infoList[1];
			} else {
				rated = "N/C";
			}
			
			// If the movie name is not null we can create a Film object with gathered data.
			if(nameMovie != null && !nameMovie.isEmpty()) {
				Film film = new Film(nameMovie, duration, rated);
				
				// Get HTML code about the shows
				Element allTimeAllLanguageElement = movie.getElementsByClass("times").first();
				String timeTextHtml = allTimeAllLanguageElement.html();
				
				// Split this content based on the <br> tag , separating the VF and VOSFTR shows
				String[] timeEachLanguageSplit = timeTextHtml.split("<br>");
				
				// For each language we get the show time
				for(String timeEachLanguage : timeEachLanguageSplit) {
					
					// Create an element from the HTML string
					Element allTimeElement = Jsoup.parse(timeEachLanguage).body();
					
					//The content "ownText" contains the language information
					String language = allTimeElement.ownText();
					
					// Each child of this element has a show time displayed like : "15:30"
					for(Element timeElement : allTimeElement.children()) {
						// Get the time and split it in hours/minutes
						String time = timeElement.ownText();
						String[] hoursAndMinutes = time.split(":");
						int hours = Integer.valueOf(hoursAndMinutes[0]);
						int minutes = Integer.valueOf(hoursAndMinutes[1]);
						//If the language is not defined or is "french", we add a VF show. Else we add a VOSTFR show.
						if(language==null || language.isEmpty() || VF.equals(language)) {
							film.addSeanceVF(new Seance(film, hours, minutes, cinema, Seance.Language.VF));
						} else {
							film.addSeanceVOSTFR(new Seance(film, hours, minutes, cinema, Seance.Language.VOSTFR));
						}
					}			
				}	
				cinema.addFilm(film);
			}
		}
		return cinema;
	}

}
