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
	private String name;
	
	/**
	 * The cinema's address (in string form)
	 */
	private String address;

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

	/**
	 * Constructor with the name and the address of the cinema.
	 * @param name The name of the cinema
	 * @param address The address of the cinema
	 */
	public Cinema(String name, String address) {
		this.name = name;
		this.address = address;
		filmList = new ArrayList<Film>();
		tempsTrajetMap = new HashMap<>();
	}
	
	/**
	 * Constructor with the name, the address and the gps coordinates of the cinema.
	 * @param name The name of the cinema
	 * @param address The address of the cinema
	 * @param lat The latitude of the cinema
	 * @param lng The longitude of the cinema
	 */
	public Cinema(String name, String address, double lat, double lng) {
		this.name = name;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		filmList = new ArrayList<Film>();
		tempsTrajetMap = new HashMap<>();
	}
	
	/**
	 * Add a film to the list of the movies in this cinema. The cinema of all seances of this movie are set to this cinema.
	 * @param film The film to add
	 */
	public void addFilm(Film film) {
		for(Seance seance : film.getSeanceListVF()) {
			seance.setCinema(this);
		}
		for(Seance seance : film.getSeanceListVOSTFR()) {
			seance.setCinema(this);
		}
		filmList.add(film);
	}

	/**
	 * Getter for the name of the cinema.
	 * @return The name of the cinema
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of the name of the cinema
	 * @param name The new name of the cinema
	 */
	public void setName(String name) {
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return "Cinema [nom=" + name + ",adresse=" + address + ", lat=" + lat + ", lng=" + lng + ", tempsTrajetMap=" + tempsTrajetMap + ", filmList=" + filmList + "]";
	}
	
	

}
