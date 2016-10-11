package com.htmlparser;
import java.io.IOException;

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
		//System.out.println(GoogleMoviesHtmlParser.getAllCinemaWithSeancesNearAPlace("antony"));
		System.out.println(GoogleMoviesHtmlParser.getCinemaWithSeances("rex"));
	} catch (IOException | HtmlParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
