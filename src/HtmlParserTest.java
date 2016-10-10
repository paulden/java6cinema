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
				System.out.println("Theather " + i + " : {name:" + name + "; adresse:" + adresse + "}");
				Elements moviesList = theater.getElementsByClass("movie");
				
				
				for(Element movie : moviesList) {
					String nameMovie = movie.getElementsByClass("name").first().text();
					String info = movie.getElementsByClass("info").first().text();
					
					String[] infoList = info.split(" - ");
					String duree = infoList[0];
					String rated = infoList[1];
					
					Film film = new Film(nameMovie, duree, rated, cinema);
					
					String timeListStr = movie.getElementsByClass("times").first().text();
					
					List<String> seanceListVOSTFR = new ArrayList<>();
					List<String> seanceListVF = new ArrayList<>();
					
					//timeListStr = timeListStr.replace(" " + (char)160, " ");
					String[] timeList = timeListStr.split(" " + (char)160);
					System.out.println(timeListStr);
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
					
					cinema.addFilm(film);
					System.out.println(film);
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
