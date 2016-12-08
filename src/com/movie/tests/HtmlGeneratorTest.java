package com.movie.tests;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

import com.movie.cinema.Theater;
import com.movie.htmlparser.GoogleMoviesHtmlParser;
import com.movie.htmlparser.HtmlGenerator;

public class HtmlGeneratorTest {

	public HtmlGeneratorTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Document doc = HtmlGenerator.generateRandomHtmlTheater(null);
		System.out.println(doc.html());
		try {
			List<Theater> listCinema = GoogleMoviesHtmlParser.getAllTheatersWithScreeningWithRandomHtml(null);
			for(Theater cinema : listCinema) {
				System.out.println(cinema);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
