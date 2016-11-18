package com.movie.cinema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movie.locations.Path;

/**
 * Class representing a movie theater.
 * It contains its name, its address (and latitude/longitude), and a list of movies being shown.
 * @author Kévin
 */
public class Cinema {
	
	/**
	 * The cinema's name.
	 */
	private String nom;
	
	/**
	 * The cinema's address (in string form)
	 */
	private String adresse;

	/**
	 * List of movies currently being shown at the cinema
	 */
	private List<Film> filmList;

	/**
	 * The cinema's GPS coordinates
	 */
	private double lat;
	private double lng;
	
	/**
	 * A map associating a transportation mode with the time it takes to reach the cinema using this mode (time in seconds)
	 */
	private Map<Path.ModeTrajet, Integer> tempsTrajetMap;

	public Cinema(String nom, String adresse) {
		this.nom = nom;
		this.adresse = adresse;
		filmList = new ArrayList<Film>();
		tempsTrajetMap = new HashMap<>();
	}
	
	public Cinema(String nom, String adresse, double lat, double lng) {
		this.nom = nom;
		this.adresse = adresse;
		this.lat = lat;
		this.lng = lng;
		filmList = new ArrayList<Film>();
		tempsTrajetMap = new HashMap<>();
	}
	
	public Cinema(String nom, String adresse, List<Film> filmList) {
		super();
		this.nom = nom;
		this.adresse = adresse;
		this.filmList = filmList;
		this.lat = -1;
		this.lng = -1;
	}
	
	public void addFilm(Film film) {
		filmList.add(film);
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Latitude getter
	 * @return cinema's latitude or -1 if the latitude hasn't been set.
	 */
	public double getLat(){
		return lat;
	}
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	/**
	 * Longitude getter
	 * @return cinema's longitude or -1 if the longitude hasn't been set.
	 */
	public double getLng(){
		return lng;
	}
	
	public void setLng(double lng){
		this.lng = lng;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public List<Film> getFilmList() {
		return filmList;
	}

	public void setFilmList(List<Film> filmList) {
		this.filmList = new ArrayList<>();
		for (Film film : filmList) {
			for(Seance seance : film.getSeanceListVF()) {
				seance.setCinema(this);
			}
			for(Seance seance : film.getSeanceListVOSTFR()) {
				seance.setCinema(this);
			}
			
			this.filmList.add(film);		
		}
	}	

	public Map<Path.ModeTrajet, Integer> getTempsTrajetMap() {
		return tempsTrajetMap;
	}

	public void setTempsTrajetMap(Map<Path.ModeTrajet, Integer> tempsTrajetMap) {
		this.tempsTrajetMap = tempsTrajetMap;
	}

	@Override
	public String toString() {
		return "Cinema [nom=" + nom + ",adresse=" + adresse + ", lat=" + lat + ", lng=" + lng + ", tempsTrajetMap=" + tempsTrajetMap + ", filmList=" + filmList + "]";
	}
	
	

}
