package com.htmlparser;
import com.cinema.Cinema;

/**
 * Class contenant un main pour tester le {@link GoogleMoviesHtmlParser}.
 * @author Kévin
 *
 */
public class HtmlParserTest {
	
	public static String VF = "Dubbed in French";
	public static String VOSTFR = "Subtitled in French";
	
	public static void main(String[] args) {
		try {
			for(Cinema cinema : GoogleMoviesHtmlParser.getAllCinemaWithSeancesNearAPlace("antony")) {
				System.out.println(cinema);
			}
			System.out.println(GoogleMoviesHtmlParser.getCinemaWithSeances("grand rex"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
