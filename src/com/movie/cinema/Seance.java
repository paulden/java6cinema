package com.movie.cinema;
import java.util.Calendar;

import com.path_to_cinema.Path;

/**
 * Class représentant une séance de cinéma.<br>
 * Contient le {@link Film} associé, la date à laquelle passe le film et le {@link Cinema} où se déroule la séance.
 * @author Kévin
 *
 */
public class Seance {
	
	private Film film;
	private Calendar date;
	private Cinema cinema;
	
	/**
	 * Le mode de trajet pour arriver à l'heure à cette séance.
	 */
	private Path.ModeTrajet modeTrajet;
	
	public Seance(Film film, int heure, int minutes, Cinema cinema) {
		this.film = film;
		this.cinema = cinema;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.HOUR_OF_DAY, heure);
		this.date.set(Calendar.MINUTE, minutes);
		this.date.set(Calendar.SECOND, 0);
	}
	
	public Seance(Film film, Calendar date, Cinema cinema) {
		super();
		this.film = film;
		this.date = date;
		this.cinema = cinema;
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

	public Path.ModeTrajet getModeTrajet() {
		return modeTrajet;
	}

	public void setModeTrajet(Path.ModeTrajet modeTrajet) {
		this.modeTrajet = modeTrajet;
	}

	@Override
	public String toString() {
		return "Seance [date=" + date.getTime() + "]";
	}
	
	
	

}
