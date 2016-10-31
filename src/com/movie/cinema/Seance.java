package com.movie.cinema;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.movie.locations.Path;

/**
 * Class representing a film show.<br>
 * Contains the {@link Film}, the showtime and the {@link Cinema} where the movie is shown.
 * @author Kévin
 *
 */
public class Seance {
	
	public enum Language {VF,VOSTFR}
	
	private Film film;
	private Calendar date;
	private Cinema cinema;
	private Language language;
	
	/**
	 * Transportation mode the user can use to get to the show in time.
	 */
	private List<Path.ModeTrajet> modeTrajetList;
	
	public Seance(Film film, int hour, int minutes, Cinema cinema, Language language) {
		this.film = film;
		this.cinema = cinema;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.HOUR_OF_DAY, hour);
		this.date.set(Calendar.MINUTE, minutes);
		this.date.set(Calendar.SECOND, 0);
		this.setLanguage(language);
		this.modeTrajetList = new ArrayList<>();
	}
	
	public Seance(Film film, Calendar date, Cinema cinema, Language language) {
		super();
		this.film = film;
		this.date = date;
		this.cinema = cinema;
		this.setLanguage(language);
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	/**
	 * @return the language
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/**
	 * @return the modeTrajetList
	 */
	public List<Path.ModeTrajet> getModeTrajetList() {
		return modeTrajetList;
	}

	/**
	 * @param modeTrajetList the modeTrajetList to set
	 */
	public void setModeTrajetList(List<Path.ModeTrajet> modeTrajetList) {
		this.modeTrajetList = modeTrajetList;
	}

	@Override
	public String toString() {
		return "Seance [film=" + film.getName() + ", cinema=" + cinema.getNom() + ", modeTrajet=" + modeTrajetList + ", date=" + date.getTime() + "]";
	}
	
	
	

}
