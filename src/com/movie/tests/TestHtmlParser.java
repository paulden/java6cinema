package com.movie.tests;
import com.movie.cinema.Theater;
import com.movie.htmlparser.GoogleMoviesHtmlParser;

/**
 * Tests the {@link GoogleMoviesHtmlParser} class methods.
 * @author Kévin
 *
 */
public class TestHtmlParser {
	
	public static void main(String[] args) {
		try {
			for(Theater cinema : GoogleMoviesHtmlParser.getAllTheaterWithScreeningNearAPlace("antony")) {
				System.out.println(cinema);
			}
			System.out.println(GoogleMoviesHtmlParser.getTheatersWithScreening("grand rex"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
