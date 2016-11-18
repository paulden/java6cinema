package com.movie.tests;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

import com.movie.cinema.Cinema;
import com.movie.htmlparser.GoogleMoviesHtmlParser;
import com.movie.htmlparser.HtmlGenerator;

public class HtmlGeneratorTest {

	public HtmlGeneratorTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Document doc = HtmlGenerator.generateRandomHtmlCinema(null);
		System.out.println(doc.html());
		try {
			List<Cinema> listCinema = GoogleMoviesHtmlParser.getAllCinemaWithSeancesWithRandomHtml(null);
			for(Cinema cinema : listCinema) {
				System.out.println(cinema);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
