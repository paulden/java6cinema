package com.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * GoogleMoviesHtmlParser permet de récuperer à partir du site google/movies, les séances de cinéma près d'un endroit donnée<br>
 * ou les horaires d'un cinéma donné. <br>
 * Les différentes méthodes renvoient un ou une liste d'objet {@link Cinema} contenant les différents films avec leurs horaires.
 * @author Kévin
 *
 */
public class GoogleMoviesHtmlParser {
	
	/**
	 * Static String déterminant dans le html si un film est en français
	 */
	private static String VF = "Dubbed in French";
	
	/**
	 * Static String déterminant dans le html si un film est sous titré en français
	 */
	private static String VOSTFR = "Subtitled in French";
	
	/**
	 * Fonction static permettant d'obtenir tous les cinémas et leurs séances à proximité d'un endroit donnée.
	 * @param place Le nom d'une ville ou d'une rue
	 * @return La liste des cinémas avec leurs séances
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 */
	public static List<Cinema> getAllCinemaWithSeancesNearAPlace(String place) throws IOException{
		Document doc;
		Element movieResults;
		
		List<Cinema> cinemaList = new ArrayList<>();

			int start = 0;
			do {
				doc = Jsoup.connect("https://www.google.com/movies?near=" + place + "&start="+start).get();
				movieResults = doc.getElementById("movie_results");
				if(movieResults!=null) {
					Elements allTheaters = movieResults.getElementsByClass("theater");
					
					for(Element theater : allTheaters) {
						Element cinemaElement = theater.getElementsByClass("desc").first();
						String name = cinemaElement.getElementsByClass("name").first().text();
						String adresse = cinemaElement.getElementsByClass("info").first().text();
						
						Cinema cinema = new Cinema(name, adresse);
						Elements moviesList = theater.getElementsByClass("movie");
						
						
						for(Element movie : moviesList) {
							String nameMovie = movie.getElementsByClass("name").first().text();
							String info = movie.getElementsByClass("info").first().text();
							
							String[] infoList = info.split(" - ");
							String duree;
							if(infoList.length>=1) {
								duree = infoList[0];
							} else {
								duree = "N/C";
							}
							
							String rated;
							if (infoList.length>=2) {
								rated = infoList[1];
							} else {
								rated = "N/C";
							}
							
							if(nameMovie != null && !new String().equals(nameMovie)) {
								Film film = new Film(nameMovie, duree, rated, cinema);
								
								Element allTimeAllLanguageElement = movie.getElementsByClass("times").first();
								String timeTextHtml = allTimeAllLanguageElement.html();
								String[] timeEachLanguageSplitted = timeTextHtml.split("<br>");
								for(String timeEachLanguage : timeEachLanguageSplitted) {
									Element allTimeElement = Jsoup.parse(timeEachLanguage).body();
									String language = allTimeElement.ownText();
									
									for(Element timeElement : allTimeElement.children()) {
										String time = timeElement.ownText();
										String[] heuresEtMinutes = time.split(":");
										int heures = Integer.valueOf(heuresEtMinutes[0]);
										int minutes = Integer.valueOf(heuresEtMinutes[1]);
										if(language==null || new String().equals(language) || VF.equals(language)) {
											film.addSeanceVF(new Seance(film, heures, minutes, cinema));
										} else {
											film.addSeanceVOSTFR(new Seance(film, heures, minutes, cinema));
										}
									}			
								}	
								
								cinema.addFilm(film);
								cinemaList.add(cinema);
							}
						}
					}
				}
				start+=10;
			
			}while(movieResults!=null);
			
		return cinemaList;
	}
	
	/**
	 * Permet de renvoyer un objet {@link Cinema} contenant toutes les {@link Seance} des différents {@link Film} passant dans ce cinéma.
	 * @param name Le nom du cinéma
	 * @return Le cinéma avec toutes les infos des films et leurs séances.
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 * @throws HtmlParserException Si le parser a trouvé aucun ou plusieurs cinéma avec le nom donné
	 */
	public static Cinema getCinemaWithSeances(String cinemaName) throws IOException, HtmlParserException {
		Document doc;
		Element movieResults;
		
		String query = cinemaName.replace(" ", "+");

		doc = Jsoup.connect("https://www.google.com/movies?q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		if(movieResults!=null) {
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			if (allTheaters == null) {
				throw new HtmlParserException("Le cinema " + cinemaName + " est introuvable.");
			} else if (allTheaters.size()>1) {
				throw new HtmlParserException("Plusieurs cinémas ont été trouvés sous ce nom " + cinemaName + ".");
			} else {
				Element theater = allTheaters.first();
				Element cinemaElement = theater.getElementsByClass("desc").first();
				String name = cinemaElement.getElementsByClass("name").first().text();
				String adresse = cinemaElement.getElementsByClass("info").first().text();
				
				Cinema cinema = new Cinema(name, adresse);
				Elements moviesList = theater.getElementsByClass("movie");
							
				for(Element movie : moviesList) {
					String nameMovie = movie.getElementsByClass("name").first().text();
					String info = movie.getElementsByClass("info").first().text();
					
					String[] infoList = info.split(" - ");
					String duree;
					if(infoList.length>=1) {
						duree = infoList[0];
					} else {
						duree = "N/C";
					}
					
					String rated;
					if (infoList.length>=2) {
						rated = infoList[1];
					} else {
						rated = "N/C";
					}
					
					if(nameMovie != null && !new String().equals(nameMovie)) {
						Film film = new Film(nameMovie, duree, rated, cinema);
						
						Element allTimeAllLanguageElement = movie.getElementsByClass("times").first();
						String timeTextHtml = allTimeAllLanguageElement.html();
						String[] timeEachLanguageSplitted = timeTextHtml.split("<br>");
						for(String timeEachLanguage : timeEachLanguageSplitted) {
							Element allTimeElement = Jsoup.parse(timeEachLanguage).body();
							String language = allTimeElement.ownText();
							
							for(Element timeElement : allTimeElement.children()) {
								String time = timeElement.ownText();
								String[] heuresEtMinutes = time.split(":");
								int heures = Integer.valueOf(heuresEtMinutes[0]);
								int minutes = Integer.valueOf(heuresEtMinutes[1]);
								if(language==null || new String().equals(language) || VF.equals(language)) {
									film.addSeanceVF(new Seance(film, heures, minutes, cinema));
								} else {
									film.addSeanceVOSTFR(new Seance(film, heures, minutes, cinema));
								}
							}			
						}	
						
						cinema.addFilm(film);
					}
				}
				return cinema;
			}
		} else {
			throw new HtmlParserException("Aucun cinéma n'a été trouvé sous le nom " + cinemaName);
		}
	}

}
