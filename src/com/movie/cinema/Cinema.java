package com.movie.cinema;
import java.util.ArrayList;
import java.util.List;

/**
 * Class représentant un cinéma.
 * La classe contient son nom, son adresse et la liste des films y passant.
 * @author Kévin
 *
 */
public class Cinema {
	
	private String nom;
	private String adresse;
	private double lat;
	private double lng;
	private List<Film> filmList;
	
	public Cinema(String nom, String adresse) {
		this.nom = nom;
		this.adresse = adresse;
		filmList = new ArrayList<Film>();
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

	@Override
	public String toString() {
		return "Cinema [nom=" + nom + ",adresse=" + adresse + ", filmList=" + filmList + "]";
	}
	
	

}
