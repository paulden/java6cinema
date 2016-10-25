package com.movie.cinema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movie.locations.Path;

/**
 * Class représentant un cinéma.
 * La classe contient son nom, son adresse et la liste des films y passant.
 * @author Kévin
 *
 */
public class Cinema {
	
	/**
	 * Le nom du cinéma.
	 */
	private String nom;
	
	/**
	 * L'adresse du cinéma.
	 */
	private String adresse;

	/**
	 * La liste des films passant dans ce cinéma.
	 */
	private List<Film> filmList;
	
	private double lat;
	private double lng;
	
	/**
	 * Une map associant à un mode de trajet, un temps de trajet en seconde.
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
	 * Getter de la latitude.
	 * @return La latitude : vaut -1 si la latitude n'est pas set.
	 */
	public double getLat(){
		return lat;
	}
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	/**
	 * Getter de la longitude. 
	 * @return La longitude. Vaut -1 si la longitude n'est pas set.
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
