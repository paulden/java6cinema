package com.cinema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.googleplaces.ClosestCinemas;
import com.googleplaces.MyAddress;
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
	 * Permet de set la liste des cinémas avec la liste des cinémas les plus proches.
	 * @param radius Le rayon de la recherche en mètres
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ClientProtocolException 
	 */
	public void findClosestCinemas(double radius) throws ClientProtocolException, JSONException, IOException {
		ClosestCinemas closestCinemas = new ClosestCinemas();
		closestCinemas.setClosestCinemas(radius);
		List<Cinema> closestCinemaList = closestCinemas.getClosestCinemas();
		this.cinemaList = closestCinemaList;
	}
	
	/**
	 * Permet d'update la liste des cinémas avec leurs séances.
	 */
	public void updateAllSeances() {
		for (Cinema cinema : cinemaList) {
			try {
				GoogleMoviesHtmlParser.updateCinemaWithSeances(cinema);
			} catch(HtmlParserException | IOException e) {
				System.err.println("Le cinéma " + cinema.getNom() + " situé au \'" + cinema.getAdresse() + "\'" + " n'a pas pu être mis à jour avec les séances car : ");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Permet d'update la liste des cinémas avec le temps de trajet entre la personne et le cinéma.
	 */
	public void updateTempsTrajet() {
		for (Cinema cinema : cinemaList) {
			for(Path.ModeTrajet modeTrajet : Path.ModeTrajet.values()) {
				Map<Path.ModeTrajet, Integer> cinemaMap = cinema.getTempsTrajetMap();
				try {
					MyAddress myAdress = new MyAddress();
					myAdress.setMyIP();
					myAdress.setMyLat();
					myAdress.setMyLng();
					double originLat = myAdress.getMyLat();
					double originLng = myAdress.getMyLng();
					
					double destLat = cinema.getLat();
					double destLng = cinema.getLng();
					
					Path path = new Path(originLat, originLng, destLat, destLng, modeTrajet.toString());
					cinemaMap.put(modeTrajet, path.getValue());
				} catch(IOException | NoPathException e) {
					System.err.println("Le cinema n'a pas pu être mis à jour avec son temps de trajet car : " + e.getStackTrace());
				}
			}

		}
	}
	
	
}
