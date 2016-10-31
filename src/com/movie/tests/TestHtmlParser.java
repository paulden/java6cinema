package com.movie.tests;
import com.movie.cinema.Cinema;
import com.movie.htmlparser.GoogleMoviesHtmlParser;

/**
 * Tests the {@link GoogleMoviesHtmlParser} class methods.
 * @author Kévin
 *
 */
public class TestHtmlParser {
	
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
