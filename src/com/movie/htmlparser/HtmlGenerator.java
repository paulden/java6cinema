package com.movie.htmlparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.movie.cinema.Cinema;

public class HtmlGenerator {

	public HtmlGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static Document generateRandomHtmlCinema(Cinema cinema) {
		
		String[] nameFilmList = new String[] {"Film1", "Film2", "Film3", "Film4"};
		
		String adresse;
		String name;
		if (cinema!=null) {
			name = cinema.getNom();
			adresse = cinema.getAdresse();
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
		
		for(String nameFilm : nameFilmList) {
			theatherElement.append("<div class=\"movie\"></div>");
			
			Element movieElement = theatherElement.getElementsByClass("movie").last();
			movieElement.append("<div class=\"name\">" + nameFilm + "</div>");
			movieElement.append("<div class=\"times\">Dubbed in French</div>");
			
			Element timeElement = movieElement.getElementsByClass("times").first();
			timeElement.append("<div>15:30</div><div>16:30</div>");
			timeElement.append("<br>");
			timeElement.append("Subtitled in French");
			timeElement.append("<div>15:30</div><div>16:30</div>");
			
		}
		
		return htmlDoc;
	}

}
