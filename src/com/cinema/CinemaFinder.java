package com.cinema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.htmlparser.GoogleMoviesHtmlParser;
import com.htmlparser.HtmlParserException;
import com.path_to_cinema.NoPathException;
import com.path_to_cinema.Path;

/**
 * Class récupérant les cinémas les plus proches, puis récupérant les horaires de ces cinémas, pour<br>
 * enfin pouvoir trouver les cinémas les plus adaptés en fonction des séances et des distances aux cinémas.
 * @author Kévin
 *
 */
public class CinemaFinder {
	
	private List<Cinema> cinemaList;

	public CinemaFinder() {
		this.cinemaList = new ArrayList<>();
	}
	
	public CinemaFinder(List<Cinema> cinemaList) {
		this.cinemaList = cinemaList;
	}

	public List<Cinema> getCinemaList() {
		return cinemaList;
	}

	public void setCinemaList(List<Cinema> cinemaList) {
		this.cinemaList = cinemaList;
	}
	
	/**
	 * Permet d'update la liste des cinémas avec leurs séances.
	 */
	public void updateAllSeances() {
		for (Cinema cinema : cinemaList) {
			try {
				GoogleMoviesHtmlParser.updateCinemaWithSeances(cinema);
			} catch(HtmlParserException | IOException e) {
				System.err.println("Le cinéma " + cinema.getNom() + " n'a pas pu être mis à jour avec les séances car : " + e.getStackTrace());
			}
		}
	}
	
	/**
	 * Permet d'update la liste des cinémas avec la distance entre la personne et le cinéma.
	 */
	public void updateDistance() {
		for (Cinema cinema : cinemaList) {
			for(Path.ModeTrajet modeTrajet : Path.ModeTrajet.values()) {
				Map<Path.ModeTrajet, Integer> cinemaMap = cinema.getTempsTrajetMap();
				try {
					Path path = new Path(0, 0, 0, 0, modeTrajet.toString());
					cinemaMap.put(modeTrajet, path.getValue());
				} catch(IOException | NoPathException e) {
					System.err.println("Le cinema n'a pas pu être mis à jour avec sa distance car : " + e.getStackTrace());
				}
			}

		}
	}
}
