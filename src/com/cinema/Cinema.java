package com.cinema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.path_to_cinema.Path;

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
	}
	
	public Cinema(String nom, String adresse, List<Film> filmList) {
		super();
		this.nom = nom;
		this.adresse = adresse;
		this.filmList = filmList;
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
	
	public double getLat(){
		return lat;
	}
	
	public void setLat(){
		this.lat = lat;
	}
	
	public double getLng(){
		return lng;
	}
	
	public void setLng(){
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
		this.filmList = filmList;
	}	

	public Map<Path.ModeTrajet, Integer> getTempsTrajetMap() {
		return tempsTrajetMap;
	}

	public void setTempsTrajetMap(Map<Path.ModeTrajet, Integer> tempsTrajetMap) {
		this.tempsTrajetMap = tempsTrajetMap;
	}

	@Override
	public String toString() {
		return "Cinema [nom=" + nom + ",adresse=" + adresse + ", filmList=" + filmList + "]";
	}
	
	

}
