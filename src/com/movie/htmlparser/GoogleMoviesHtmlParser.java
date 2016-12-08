package com.movie.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.movie.exceptions.HtmlParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.movie.cinema.Theater;
import com.movie.cinema.Movie;
import com.movie.cinema.Screening;

/**
 * GoogleMoviesHtmlParser retrieves a show list close to a specific place, or the show times from a given theater, <br>
 * from the Google Movies website, via HTML parsing.
 * The various methods return a {@link Theater} or a list of {@link Theater}  list containing the movies with the show times.
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
	 * Static function getting all {@link Theater} with their shows close to a specific place.
	 * @param place a city or street name
	 * @return a list of {@link Theater} updated with their shows
	 * @throws IOException if there is a failure while accessing Google Movies website
	 */
	public static List<Theater> getAllTheaterWithScreeningNearAPlace(String place) throws IOException{
		Document doc;
		Element movieResults;
		
		List<Theater> theaterList = new ArrayList<>();

			// Allows to display the list starting from a n-th theater.
			int start = 0;
			do {
				
				// Get HTML code from Google movies
				doc = Jsoup.connect("https://www.google.com/movies?hl=fr&near=" + place + "&start="+start).get();
				movieResults = doc.getElementById("movie_results");
				
				// If there is a "movie_results" tag in the HTML, it might have found some theaters.
				if(movieResults!=null) {
					
					// We get a theater list that can be empty if no theater was found.
					Elements allTheaters = movieResults.getElementsByClass("theater");
					
					// For each HTML element of the list, we create a theater object containing the relevant data
					for(Element theaterElement : allTheaters) {
						Theater theater = GoogleMoviesHtmlParser.getTheaterFromHtmlTheaterElement(theaterElement);
						theaterList.add(theater);
					}
				}
				// Increasing the search start by 10 to go to the next set of results
				start+=10;
			
			
			}while(movieResults!=null); // When we don't get anymore results we stop.
			
		return theaterList;
	}
	
	/**
	 * Returns a {@link Theater} object containing all the {@link Screening } of all the {@link Movie } being shown in this theater.
	 * @param theaterName the theater's name
	 * @return a {@link Theater} with all relevant information about movies and shows.
	 * @throws IOException if there is a failure while accessing Google Movies website
	 * @throws HtmlParserException If the parser found no matching theater name or too many matching theater names
	 */
	public static Theater getTheatersWithScreening(String theaterName) throws IOException, HtmlParserException {
		Document doc;
		Element movieResults;
		
		// Create the query from the theater name
		String query = theaterName.replace(" ", "+");
		
		// Retrieve the HTML code from google movies
		doc = Jsoup.connect("https://www.google.com/movies?hl=fr&q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		
		// If there is a "movie_results" tag in the HTML, it might have found some theaters.
		if(movieResults!=null) {
			// Gathering the theater result list (can be empty)
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			// If the list contains only one element, the we found the right theater and we return a theater object with all the gathered data.
			if (allTheaters == null) {
				throw new HtmlParserException("Le cinema " + theaterName + " est introuvable.");
			} else if (allTheaters.size()==0) {
				throw new HtmlParserException("Le cinema " + theaterName + " est introuvable.");
			} else if (allTheaters.size()>1) {
				throw new HtmlParserException("Plusieurs cinémas ont été trouvés sous ce nom " + theaterName + ".");
			} else {
				Element theater = allTheaters.first();
				return GoogleMoviesHtmlParser.getTheaterFromHtmlTheaterElement(theater);
			}
		} else { // Otherwise no theater has been found and we throw an exception.
			throw new HtmlParserException("Aucun cinéma n'a été trouvé sous le nom " + theaterName);
		}
	}
	
	
	/**
	 * Returns a {@link Theater} list matching a name, conatining all the {@link Screening} of the different {@link Movie} being shown in these theaters.
	 * @param theaterName the theater name
	 * @return A list of theaters mathcing the given name, with all relevant information about movies and shows.
	 * @throws IOException if there is a failure while accessing Google Movies website
	 */
	public static List<Theater> getAllTheatersWithScreening(String theaterName) throws IOException {
		Document doc;
		Element movieResults;
		
		// Create the query from the theater name
		String query = theaterName.replace(" ", "+");
		
		// Create the theater list we will return
		List<Theater> theaterList = new ArrayList<>();
		
		// get HTML code from google movies
		doc = Jsoup.connect("https://www.google.com/movies?hl=fr&q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		
		// If there is a "movie_results" tag in the HTML, it might have found some theaters.
		if(movieResults!=null) {
			// Gathering the theater result list (can be empty)
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			// For each HTML element of the list, we create a theater object containing the relevant data
			for(Element theaterElement : allTheaters) {
				Theater theater = GoogleMoviesHtmlParser.getTheaterFromHtmlTheaterElement(theaterElement);
				theaterList.add(theater);
			}
			
			return theaterList;
		} else { // Otherwise we return an empty list
			return theaterList;
		}
	}
	
	/**
	 * Permet de renvoyer une liste de {@link Theater} correspondant à un nom, et contenant toutes les {@link Screening} des différents {@link Movie} passant dans ce cinéma.
	 * @param theaterToFind Le theatre à créer
	 * @return Une liste de cinéma correspondant au nom, avec toutes les infos des films et leurs séances. Peut renvoyer une liste vide.
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 */
	public static List<Theater> getAllTheatersWithScreeningWithRandomHtml(Theater theaterToFind) throws IOException {
		Document doc;
		Element movieResults;
		
		//On crée la liste des cinémas
		List<Theater> theaterList = new ArrayList<>();
		
		//On récupère le code html à partir du site google/movies
		doc = HtmlGenerator.generateRandomHtmlTheater(theaterToFind);
		movieResults = doc.getElementById("movie_results");
		
		//Si le document possède la balise "movies_results", alors il a possiblement trouvé des cinéma.
		if(movieResults!=null) {
			//On obtient une liste de cinéma. La liste peut être vide, si aucun cinéma n'est trouvé.
			Elements allTheatersElement = movieResults.getElementsByClass("theater");
			
			//Pour chaque Element de la liste, on créé un objet theater contenant les infos et on l'ajoute à la liste.
			for(Element theaterElement : allTheatersElement) {
				Theater theater = GoogleMoviesHtmlParser.getTheaterFromHtmlTheaterElement(theaterElement);
				theaterList.add(theater);
			}
			
			return theaterList;
		} else { //Sinon on renvoie la liste vide
			return theaterList;
		}
	}

	/**
	 * Updates a {@link Theater} object with all the {@link Screening} of the different {@link Movie} being shown in it.
	 * @param theater the theater object to update
	 * @throws IOException if there is a failure while accessing google movies
	 * @throws HtmlParserException if no such theater has been found
	 */
	public static void updateTheaterWithScreening(Theater theater) throws IOException, HtmlParserException {
		String theaterName = theater.getName();
		String theaterAdress = theater.getAddress();
		
		//List<theater> theaterList = GoogleMoviesHtmlParser.getAlltheaterWithSeances(theaterName);
		List<Theater> theaterList = GoogleMoviesHtmlParser.getAllTheatersWithScreeningWithRandomHtml(theater);
		
		Theater bestTheater = null;
		double bestMatchingAddress = 0;
		// For each theater, we compare the addresses and return only the theater with the address that matches the best
		for (Theater theaterFound : theaterList) {
			String addressFound = theaterFound.getAddress();
			theaterAdress = theaterAdress.replace(",", "");
			theaterAdress = addressFound.replace("-", " ");
			String[] addressSplit = theaterAdress.split(" ");
			double matching = 0;
			for (String word : addressSplit) {
				if (addressFound.contains(word)) {
					matching++;
				}
			}
			matching/=addressSplit.length;
			if (matching>bestMatchingAddress) {
				bestMatchingAddress = matching;
				bestTheater = theaterFound;
			}
		}
		
		if(bestTheater != null) {
			theater.setMovieList(bestTheater.getMovieList());
		} else {
			throw new HtmlParserException("Aucun cinéma n'a été trouvé correspondant au nom " + theaterName + " et à l'adresse " + theaterAdress);
		}
		
	}

	
	/**
	 * Gets a {@link Theater} object from the right HTML element, containing all the {@link Screening} of the different {@link Movie} being shown in it.
	 * @param theaterElement the HTML element
	 * @return the {@link Theater} object with the relevant data.
	 */
	private static Theater getTheaterFromHtmlTheaterElement (Element theaterElement) {
		// We retrieve the "desc" class element which contains the theater description
		Element descElement = theaterElement.getElementsByClass("desc").first();
		Element nameElement = descElement.getElementsByClass("name").first();
		String name;
		if(nameElement!=null) {
			name = nameElement.text();
		} else {
			name = "Nom introuvable";
		}
		
		Element adresseElement = descElement.getElementsByClass("info").first();
		String address;
		if (adresseElement!=null) {
			address = adresseElement.text();
		} else {
			address = "Adresse introuvable";
		}
		
		// Create a theater object based on the name and address
		Theater theater = new Theater(name, address);
		
		// Get the movie list (can be empty)
		Elements moviesList = theaterElement.getElementsByClass("movie");
					
		// FOr each movie we gather the shows information and add it to the theater object
		for(Element movie : moviesList) {
			
			//On récupère le nom du film
			String nameMovie;
			Element nameMovieElement = movie.getElementsByClass("name").first();
			if (nameMovieElement!=null) {
				nameMovie = nameMovieElement.text();
			} else {
				nameMovie = "Nom introuvable";
			}
			
			//On récupère les infos du film séparés par -. La première est sa durée et la seconde est l'âge recommandé.
			String info;
			Element infoElement = movie.getElementsByClass("info").first();
			String duration;
			String rated;
			if (infoElement!=null) {
				info = infoElement.text();
				
				String[] infoList = info.split(" - ");
				if(infoList.length>=1) {
					duration = infoList[0];
				} else {
					duration = "N/C";
				}
				
				if (infoList.length>=2) {
					rated = infoList[1];
				} else {
					rated = "N/C";
				}
			} else {
				duration = "N/C";
				rated = "N/C";
			}
			
			// If the movie name is not null we can create a Film object with gathered data.
			if(nameMovie != null && !nameMovie.isEmpty()) {
				Movie film = new Movie(nameMovie, duration, rated);
				
				// Get HTML code about the shows
				Element allTimeAllLanguageElement = movie.getElementsByClass("times").first();
				
				if(allTimeAllLanguageElement != null) {
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
								film.addScreeningVF(new Screening(film, hours, minutes, theater, Screening.Language.VF));
							} else {
								film.addScreeningVOSTFR(new Screening(film, hours, minutes, theater, Screening.Language.VOSTFR));
							}
						}			
					}	
				}
				theater.addMovie(film);
			}
		}
		return theater;
	}

}
