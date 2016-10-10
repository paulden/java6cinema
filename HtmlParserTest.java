import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserTest {
	
public static void main(String[] args) {
	
	Document doc;
	Element movieResults;
	try {
		int start = 0;
		int i = 1;
		do {
		doc = Jsoup.connect("https://www.google.com/movies?near=antony&start="+start).get();
		movieResults = doc.getElementById("movie_results");
		if(movieResults!=null) {
			Elements allTheaters = movieResults.getElementsByClass("theater");
			
			for(Element theater : allTheaters) {
				Element cinema = theater.getElementsByClass("desc").first();
				String name = cinema.getElementsByClass("name").first().text();
				String adresse = cinema.getElementsByClass("info").first().text();
				System.out.println("Theather " + i + " : {name:" + name + "; adresse:" + adresse + "}");
				Elements moviesList = theater.getElementsByClass("movie");
				
				for(Element movie : moviesList) {
					String nameMovie = movie.getElementsByClass("name").first().text();
					String info = movie.getElementsByClass("info").first().text();
					String time = movie.getElementsByClass("times").first().text();
					System.out.println("{name:" + nameMovie + "; info:" + info + ";times:" + time + "}");
				}
				
				i++;
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
