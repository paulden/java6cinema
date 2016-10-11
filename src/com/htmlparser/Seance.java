package com.htmlparser;
import java.util.Calendar;

/**
 * Class repr�sentant une s�ance de cin�ma.<br>
 * Contient le {@link Film} associ�, la date � laquelle passe le film et le {@link Cinema} o� se d�roule la s�ance.
 * @author K�vin
 *
 */
public class Seance {
	
	private Film film;
	private Calendar date;
	private Cinema cinema;
	
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

	@Override
	public String toString() {
		return "Seance [date=" + date.getTime() + "]";
	}
	
	
	

}
