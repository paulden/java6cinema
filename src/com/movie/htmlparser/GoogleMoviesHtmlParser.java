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
 * GoogleMoviesHtmlParser permet de récupérer à partir du site google/movies, les séances de cinéma près d'un endroit donnée<br>
 * ou les horaires d'un cinéma donné. <br>
 * Les différentes méthodes renvoient un ou une liste d'objet {@link Cinema} contenant les différents films avec leurs horaires.
 * @author Kévin
 *
 */
public final class GoogleMoviesHtmlParser {
	
	/**
	 * Static String déterminant dans le html si un film est en français
	 */
	private static String VF = "Dubbed in French";
	
	/**
	 * Static String déterminant dans le html si un film est sous titré en français
	 */
	private static String VOSTFR = "Subtitled in French";
	
	/**
	 * Private constructeur.
	 */
	private GoogleMoviesHtmlParser() {
		
	}
	
	
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

			//Permet d'afficher à partir du x ième cinéma.
			int start = 0;
			do {
				
				//Récupère le code html provenant de google/movies
				doc = Jsoup.connect("https://www.google.com/movies?hl=fr&near=" + place + "&start="+start).get();
				movieResults = doc.getElementById("movie_results");
				
				//Si l'html possède une balise movie_results, alors il a possiblement trouvé des cinémas.
				if(movieResults!=null) {
					
					//On obtient une liste de cinéma. La liste peut être vide, si aucun cinéma n'est trouvé.
					Elements allTheaters = movieResults.getElementsByClass("theater");
					
					//Pour chaque Element de la liste, on créé un objet Cinema contenant les infos et on l'ajoute à la liste.
					for(Element theater : allTheaters) {
						Cinema cinema = GoogleMoviesHtmlParser.getCinemaFromHtmlTheaterElement(theater);
						cinemaList.add(cinema);
					}
				}
				//On augmente de 10 le début de la recherche
				start+=10;
			
			
			}while(movieResults!=null); //On s'arrête quand on n'a plus aucun résultat.
			
		return cinemaList;
	}
	
	/**
	 * Permet de renvoyer un objet {@link Cinema} contenant toutes les {@link Seance} des différents {@link Film} passant dans ce cinéma.
	 * @param cinemaName Le nom du cinéma
	 * @return Le cinéma avec toutes les infos des films et leurs séances.
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 * @throws HtmlParserException Si le parser a trouvé aucun ou plusieurs cinéma avec le nom donné
	 */
	public static Cinema getCinemaWithSeances(String cinemaName) throws IOException, HtmlParserException {
		Document doc;
		Element movieResults;
		
		//On créé la query à partir du nom du cinéma
		String query = cinemaName.replace(" ", "+");
		
		//On récupère le code html à partir du site google/movies
		doc = Jsoup.connect("https://www.google.com/movies?hl=fr&q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		
		//Si le document possède la balise "movies_results", alors il a possiblement trouvé des cinéma.
		if(movieResults!=null) {
			//On récupère les différents cinema, peut être vide.
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			//Si la liste possède un seul element, alors on a trouvé le bon cinéma et on renvoit un objet Cinema possédant les informations.
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
		} else { //Sinon il n'a trouvé aucun cinéma et on envoit une exception.
			throw new HtmlParserException("Aucun cinéma n'a été trouvé sous le nom " + cinemaName);
		}
	}
	
	
	/**
	 * Permet de renvoyer une liste de {@link Cinema} correspondant à un nom, et contenant toutes les {@link Seance} des différents {@link Film} passant dans ce cinéma.
	 * @param cinemaName Le nom du cinéma
	 * @return Une liste de cinéma correspondant au nom, avec toutes les infos des films et leurs séances. Peut renvoyer une liste vide.
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 */
	public static List<Cinema> getAllCinemaWithSeances(String cinemaName) throws IOException {
		Document doc;
		Element movieResults;
		
		//On créé la query à partir du nom du cinéma
		String query = cinemaName.replace(" ", "+");
		
		//On crée la liste des cinémas
		List<Cinema> cinemaList = new ArrayList<>();
		
		//On récupère le code html à partir du site google/movies
		doc = Jsoup.connect("https://www.google.com/movies?hl=fr&q=" + query).get();
		movieResults = doc.getElementById("movie_results");
		
		//Si le document possède la balise "movies_results", alors il a possiblement trouvé des cinéma.
		if(movieResults!=null) {
			//On obtient une liste de cinéma. La liste peut être vide, si aucun cinéma n'est trouvé.
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			//Pour chaque Element de la liste, on créé un objet Cinema contenant les infos et on l'ajoute à la liste.
			for(Element theater : allTheaters) {
				Cinema cinema = GoogleMoviesHtmlParser.getCinemaFromHtmlTheaterElement(theater);
				cinemaList.add(cinema);
			}
			
			return cinemaList;
		} else { //Sinon on renvoie la liste vide
			return cinemaList;
		}
	}
	
	/**
	 * Permet de renvoyer une liste de {@link Cinema} correspondant à un nom, et contenant toutes les {@link Seance} des différents {@link Film} passant dans ce cinéma.
	 * @param cinemaName Le nom du cinéma
	 * @return Une liste de cinéma correspondant au nom, avec toutes les infos des films et leurs séances. Peut renvoyer une liste vide.
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 */
	public static List<Cinema> getAllCinemaWithSeancesWithRandomHtml(Cinema cinemaToFind) throws IOException {
		Document doc;
		Element movieResults;
		
		//On crée la liste des cinémas
		List<Cinema> cinemaList = new ArrayList<>();
		
		//On récupère le code html à partir du site google/movies
		doc = HtmlGenerator.generateRandomHtmlCinema(cinemaToFind);
		movieResults = doc.getElementById("movie_results");
		
		//Si le document possède la balise "movies_results", alors il a possiblement trouvé des cinéma.
		if(movieResults!=null) {
			//On obtient une liste de cinéma. La liste peut être vide, si aucun cinéma n'est trouvé.
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			//Pour chaque Element de la liste, on créé un objet Cinema contenant les infos et on l'ajoute à la liste.
			for(Element theater : allTheaters) {
				Cinema cinema = GoogleMoviesHtmlParser.getCinemaFromHtmlTheaterElement(theater);
				cinemaList.add(cinema);
			}
			
			return cinemaList;
		} else { //Sinon on renvoie la liste vide
			return cinemaList;
		}
	}

	/**
	 * Permet de mettre à jour un objet {@link Cinema} contenant toutes les {@link Seance} des différents {@link Film} passant dans ce cinéma.
	 * @param cinema Le cinema contenant le nom du cinéma
	 * @throws IOException lors d'une erreur pour accéder à la page google/movies
	 * @throws HtmlParserException si aucun cinéma correspondant n'a été trouvé
	 */
	public static void updateCinemaWithSeances(Cinema cinema) throws IOException, HtmlParserException{
		String cinemaName = cinema.getNom();
		String cinemaAdresse = cinema.getAdresse();
		
		//List<Cinema> cinemaList = GoogleMoviesHtmlParser.getAllCinemaWithSeances(cinemaName);
		List<Cinema> cinemaList = GoogleMoviesHtmlParser.getAllCinemaWithSeancesWithRandomHtml(cinema);
		
		Cinema bestCinema = null;
		double bestCorrespondanceAdresse = 0;
		//Pour chaque cinéma, on compare les adresses et on renvoit le cinéma qui correspond au mieux à l'adresse
		for (Cinema cinemaTrouve : cinemaList) {
			String adresseTrouve = cinemaTrouve.getAdresse();
			cinemaAdresse = cinemaAdresse.replace(",", "");
			cinemaAdresse = adresseTrouve.replace("-", " ");
			String[] adresseSplited = cinemaAdresse.split(" ");
			double correspondance = 0;
			for (String word : adresseSplited) {
				if (adresseTrouve.contains(word)) {
					correspondance++;
				}
			}
			correspondance/=adresseSplited.length;
			if (correspondance>bestCorrespondanceAdresse) {
				bestCorrespondanceAdresse = correspondance;
				bestCinema = cinemaTrouve;
			}
		}
		
		if(bestCinema != null) {
			cinema.setFilmList(bestCinema.getFilmList());;
		} else {
			throw new HtmlParserException("Aucun cinéma n'a été trouvé correspondant au nom " + cinemaName + " et à l'adresse " + cinemaAdresse);
		}
		
	}
	
	/**
	 * Permet d'obtenir à partir du bon element html, un objet {@link Cinema} contenant les films et les séances qui y sont.
	 * @param theater Le bon element html.
	 * @return L'objet {@link Cinema} avec les infos.
	 */
	private static Cinema getCinemaFromHtmlTheaterElement (Element theater) {
		//On récupère l'élément de class "desc" qui contient la description du cinéma
		Element cinemaElement = theater.getElementsByClass("desc").first();
		Element nameElement = cinemaElement.getElementsByClass("name").first();
		String name;
		if(nameElement!=null) {
			name = nameElement.text();
		} else {
			name = "Nom introuvable";
		}
		
		Element adresseElement = cinemaElement.getElementsByClass("info").first();
		String adresse;
		if (adresseElement!=null) {
			adresse = adresseElement.text();
		} else {
			adresse = "Adresse introuvable";
		}
		
		//On crée un objet cinema avec ce nom et cette adresse.
		Cinema cinema = new Cinema(name, adresse);
		
		//On récupère la liste des films. Cette liste peut être vide si aucun élément de classe "movie" est trouvé.
		Elements moviesList = theater.getElementsByClass("movie");
					
		//Pour chaque film, on récupère ces infos et les différentes séances et on l'ajoute à l'objet cinema.
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
			String duree;
			String rated;
			if (infoElement!=null) {
				info = infoElement.text();
				
				String[] infoList = info.split(" - ");
				if(infoList.length>=1) {
					duree = infoList[0];
				} else {
					duree = "N/C";
				}
				
				if (infoList.length>=2) {
					rated = infoList[1];
				} else {
					rated = "N/C";
				}
			} else {
				duree = "N/C";
				rated = "N/C";
			}
			

			
			//Si le nom du film n'est pas nul, on peut créer un objet Film avec les données récupérés.
			if(nameMovie != null && !new String().equals(nameMovie)) {
				Film film = new Film(nameMovie, duree, rated);
				
				//On récupère le contenu html contenant les différentes séances.
				Element allTimeAllLanguageElement = movie.getElementsByClass("times").first();
				
				if(allTimeAllLanguageElement != null) {
					String timeTextHtml = allTimeAllLanguageElement.html();
					
					//On sépare ce contenu en fonction de la balise <br> qui sépare les différentes langues VF et VOSTFR
					String[] timeEachLanguageSplitted = timeTextHtml.split("<br>");
					
					//Pour chaque langue, on récupère l'horaire des séances à partir de la string html
					for(String timeEachLanguage : timeEachLanguageSplitted) {
						
						//On recrée un Element à partir de la string html
						Element allTimeElement = Jsoup.parse(timeEachLanguage).body();
						
						//Le langage est contenu dans le ownText de du contenu.
						String language = allTimeElement.ownText();
						
						//Chaque enfant de l'élément possède l'horaire d'une séance sous la forme "15:30"
						for(Element timeElement : allTimeElement.children()) {
							//On récupère l'horaire puis on la sépare en heures et minutes
							String time = timeElement.ownText();
							String[] heuresEtMinutes = time.split(":");
							int heures = Integer.valueOf(heuresEtMinutes[0]);
							int minutes = Integer.valueOf(heuresEtMinutes[1]);
							//Si le langage n'est pas défini ou est VF, alors on ajoute une séance VF sinon on ajoute une séance VOSTFR
							if(language==null || new String().equals(language) || VF.equals(language)) {
								film.addSeanceVF(new Seance(film, heures, minutes, cinema, Seance.Language.VF));
							} else {
								film.addSeanceVOSTFR(new Seance(film, heures, minutes, cinema, Seance.Language.VOSTFR));
							}
						}			
					}	
				}

				cinema.addFilm(film);
			}
		}
		return cinema;
	}

}
