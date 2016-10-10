package com.htmlparser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserTest {
	
	public static String VF = "Dubbed in French";
	public static String VOSTFR = "Subtitled in French";
	
public static void main(String[] args) {
	
	Document doc;
	Element movieResults;
	List<Cinema> cinemaList = new ArrayList<>();
	try {
		int start = 0;
		int i = 1;
		do {
		doc = Jsoup.connect("https://www.google.com/movies?near=antony&start="+start).get();
		movieResults = doc.getElementById("movie_results");
		if(movieResults!=null) {
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			for(Element theater : allTheaters) {
				Element cinemaElement = theater.getElementsByClass("desc").first();
				String name = cinemaElement.getElementsByClass("name").first().text();
				String adresse = cinemaElement.getElementsByClass("info").first().text();
				
				Cinema cinema = new Cinema(name, adresse);
				//System.out.println("Theather " + i + " : {name:" + name + "; adresse:" + adresse + "}");
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
							//System.out.println("Language : " + language);
							
							for(Element timeElement : allTimeElement.children()) {
								String time = timeElement.ownText();
								//System.out.println("OwnTextTimeElement : " + time);
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
						//System.out.println(film);
					}
					
					/*
					System.out.println("Element times : " + timeElement);
					for(Element element : timeElement.children()) {
						System.out.println("Sous-element : " + element);
						System.out.println("Texte : " + element.text());
					}
					String timeListStr = movie.getElementsByClass("times").first().text();
					
					List<String> seanceListVOSTFR = new ArrayList<>();
					List<String> seanceListVF = new ArrayList<>();
					
					//timeListStr = timeListStr.replace(" " + (char)160, " ");
					String[] timeList = timeListStr.split(" " + (char)160);
					//System.out.println(timeListStr);
					String language = "VF";
					for(String time : timeList) {
						if (VF.equals(time)) {
							language = "VF";
						} else if (VOSTFR.equals(time)) {
							language = "VOSTFR";
						} else {
							String[] heuresEtMinutes = time.split(":");
							int heures = Integer.valueOf(heuresEtMinutes[0]);
							int minutes = Integer.valueOf(heuresEtMinutes[1]);
							
							if (language.equals("VF")) {
								film.addSeanceVF(new Seance(film, heures, minutes, cinema));
								seanceListVF.add(time);
							} else {
								film.addSeanceVOSTFR(new Seance(film, heures, minutes, cinema));
								seanceListVOSTFR.add(time);
							}
						}
					}
					
					*/
					

					//System.out.println("{name:" + nameMovie + "; info:" + info + ";seances VF:" + seanceListVF + "; seances VOSTFR:" + seanceListVOSTFR + "}");
				}
				
				i++;
				
				System.out.println(cinema);
			}
		}
		start+=10;
		
		}while(movieResults!=null);
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

}
