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
public class Theater {
	
	/**
	 * The theater's name.
	 */
	private String name;
	
	/**
	 * The theater's address (in string form)
	 */
	private String address;

	/**
	 * List of movies currently being shown at the theater
	 */
	private List<Movie> movieList;

	/**
	 * The theater's GPS coordinates
	 */
	private double lat;
	private double lng;
	
	/**
	 * A map associating a transportation mode with the time it takes to reach the theater using this mode (time in seconds)
	 */
	private Map<Path.TransportationMode, Integer> timeTransportationMap;

	/**
	 * Constructor with the name and the address of the theater.
	 * @param name The name of the theater
	 * @param address The address of the theater
	 */
	public Theater(String name, String address) {
		this.name = name;
		this.address = address;
		movieList = new ArrayList<Movie>();
		timeTransportationMap = new HashMap<>();
	}
	
	/**
	 * Constructor with the name, the address and the gps coordinates of the theater.
	 * @param name The name of the theater
	 * @param address The address of the theater
	 * @param lat The latitude of the theater
	 * @param lng The longitude of the theater
	 */
	public Theater(String name, String address, double lat, double lng) {
		this.name = name;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		movieList = new ArrayList<Movie>();
		timeTransportationMap = new HashMap<>();
	}
	
	/**
	 * Add a movie to the list of the movies in this theater. The theater of all screenings of this movie are set to this theater.
	 * @param movie The movie to add
	 */
	public void addMovie(Movie movie) {
		for(Screening screening : movie.getScreeningVFList()) {
			screening.setTheater(this);
		}
		for(Screening screening : movie.getScreeningVOSTFRList()) {
			screening.setTheater(this);
		}
		movieList.add(movie);
	}

	/**
	 * Getter for the name of the theater.
	 * @return The name of the theater
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of the name of the theater
	 * @param name The new name of the theater
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Latitude getter
	 * @return theater's latitude or -1 if the latitude hasn't been set.
	 */
	public double getLat(){
		return lat;
	}
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	/**
	 * Longitude getter
	 * @return theater's longitude or -1 if the longitude hasn't been set.
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

	public List<Movie> getMovieList() {
		return movieList;
	}

	public void setMovieList(List<Movie> movieListToSet) {
		this.movieList = new ArrayList<>();
		for (Movie movie : movieListToSet) {
			for(Screening screening : movie.getScreeningVFList()) {
				screening.setTheater(this);
			}
			for(Screening screening : movie.getScreeningVOSTFRList()) {
				screening.setTheater(this);
			}
			
			this.movieList.add(movie);		
		}
	}	

	public Map<Path.TransportationMode, Integer> getTimeTransportationMap() {
		return timeTransportationMap;
	}

	public void setTimeTransportationMap(Map<Path.TransportationMode, Integer> timeTransportationMap) {
		this.timeTransportationMap = timeTransportationMap;
	}

	@Override
	public String toString() {
		return "Theater [name=" + name + ",adresse=" + address + ", lat=" + lat + ", lng=" + lng + ", timeTransportationMap=" + timeTransportationMap + ", movieList=" + movieList + "]";
	}
	
	

}
