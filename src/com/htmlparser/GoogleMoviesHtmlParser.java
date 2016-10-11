package com.htmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * GoogleMoviesHtmlParser permet de r�cuperer � partir du site google/movies, les s�ances de cin�ma pr�s d'un endroit donn�e<br>
 * ou les horaires d'un cin�ma donn�. <br>
 * Les diff�rentes m�thodes renvoient un ou une liste d'objet {@link Cinema} contenant les diff�rents films avec leurs horaires.
 * @author K�vin
 *
 */
public class GoogleMoviesHtmlParser {
	
	/**
	 * Static String d�terminant dans le html si un film est en fran�ais
	 */
	private static String VF = "Dubbed in French";
	
	/**
	 * Static String d�terminant dans le html si un film est sous titr� en fran�ais
	 */
	private static String VOSTFR = "Subtitled in French";
	
	/**
	 * Fonction static permettant d'obtenir tous les cin�mas et leurs s�ances � proximit� d'un endroit donn�e.
	 * @param place Le nom d'une ville ou d'une rue
	 * @return La liste des cin�mas avec leurs s�ances
	 * @throws IOException lors d'une erreur pour acc�der � la page google/movies
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
	 * Permet de renvoyer un objet {@link Cinema} contenant toutes les {@link Seance} des diff�rents {@link Film} passant dans ce cin�ma.
	 * @param name Le nom du cin�ma
	 * @return Le cin�ma avec toutes les infos des films et leurs s�ances.
	 * @throws IOException lors d'une erreur pour acc�der � la page google/movies
	 * @throws HtmlParserException Si le parser a trouv� aucun ou plusieurs cin�ma avec le nom donn�
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
				throw new HtmlParserException("Plusieurs cin�mas ont �t� trouv�s sous ce nom " + cinemaName + ".");
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
			throw new HtmlParserException("Aucun cin�ma n'a �t� trouv� sous le nom " + cinemaName);
		}
	}

}
