package com.movie.htmlparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.movie.cinema.Cinema;
import com.movie.cinema.Film;

public class HtmlGenerator {
	
	public static int numberMaxSeances = 5;

	public HtmlGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<Film> generateRandomFilm() {
		List<Film> filmList = new ArrayList<Film>();
		
		Film film1 = new Film("Les animaux fantastiques", "2h20", "Tous publics");
		filmList.add(film1);
		
		Film film2 = new Film("Le petit locataire", "1h39", "Tous publics");
		filmList.add(film2);
		
		Film film3 = new Film("Inferno", "2h01", "Tous publics");
		filmList.add(film3);
		
		Film film4 = new Film("Tu ne tueras point", "2h11", "Interdit au moins de 12 ans");
		filmList.add(film4);
		
		Film film5 = new Film("Snowden", "2h14", "Tous publics");
		filmList.add(film5);
		
		Film film6 = new Film("Mal de pierres", "1h56", "Tous publics");
		filmList.add(film6);
		
		Film film7 = new Film("Les Trolls", "1h32", "Tous publics");
		filmList.add(film7);
		
		Film film8 = new Film("Doctor Strange", "1h55", "Tous publics");
		filmList.add(film8);
		
		return filmList;
		
	}
	
	public static Document generateRandomHtmlCinema(Cinema cinema) {
		
		Random random = new Random();
		
		String adresse;
		String name;
		if (cinema!=null) {
			name = cinema.getName();
			adresse = cinema.getAddress();
		} else {
			adresse = "N/C";
			name = "N/C";
		}
		Document htmlDoc = Jsoup.parse("<generatedCinema></generatedCinema>");
		htmlDoc.select("generatedCinema").first().append("<div id=\"movie_results\"></div>");
		
		Element movieResultElement = htmlDoc.getElementById("movie_results");
		movieResultElement.append("<div class=\"theater\"></div>");
		
		Element theatherElement = movieResultElement.getElementsByClass("theater").first();
		theatherElement.append("<div class=\"desc\"></div>");
		
		Element descElement = theatherElement.getElementsByClass("desc").first();
		descElement.append("<div class=\"name\">" + name + "</div>");
		descElement.append("<div class=\"info\">" + adresse + "</div>");
		
		List<Film> filmList = HtmlGenerator.generateRandomFilm();
		
		int filmNumber = random.nextInt(filmList.size());
		
		for(int i=0;i<filmNumber;i++) {
			int filmIndex = random.nextInt(filmList.size());
			Film film = filmList.get(filmIndex);
			filmList.remove(filmIndex);
			
			String nameFilm = film.getName();
			String duration = film.getDuree();
			String rated = film.getRated();
			
			theatherElement.append("<div class=\"movie\"></div>");
			
			Element movieElement = theatherElement.getElementsByClass("movie").last();
			movieElement.append("<div class=\"name\">" + nameFilm + "</div>");
			movieElement.append("<div class=\"info\">" + duration + " - " + rated + "</div>");
			
			
			boolean isVF = random.nextBoolean();
			boolean isVO;
			if (isVF) {
				isVO = random.nextBoolean();
			} else {
				isVO = true;
			}
			
			movieElement.append("<div class=\"times\"></div>");
			Element timeElement = movieElement.getElementsByClass("times").first();
			if (isVF) {
				timeElement.append("Dubbed in French");
				
				int seanceNumber = random.nextInt(HtmlGenerator.numberMaxSeances) + 1;
				
				for(int j=0;j<seanceNumber;j++) {
					int hours = random.nextInt(12)+8;
					int minutes = random.nextInt(60);
					timeElement.append("<div>" + String.valueOf(hours)+ ":" + String.valueOf(minutes)+ "</div>");
				}
	
				timeElement.append("<br>");
			}

			if (isVO) {
				timeElement.append("Subtitled in French");
				
				int seanceNumber = random.nextInt(HtmlGenerator.numberMaxSeances) + 1;
				
				for(int j=0;j<seanceNumber;j++) {
					int hours = random.nextInt(12)+8;
					int minutes = random.nextInt(60);
					timeElement.append("<div>" + String.valueOf(hours)+ ":" + String.valueOf(minutes)+ "</div>");
				}
			}
		}
		
		return htmlDoc;
	}

}
