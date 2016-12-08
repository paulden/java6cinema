package com.movie.cinema;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.movie.locations.Path;

/**
 * Class representing a film show.<br>
 * Contains the {@link Movie}, the showtime and the {@link Theater} where the movie is shown.
 * @author Kévin
 *
 */
public class Screening {
	
	public enum Language {VF,VOSTFR}
	
	private Movie movie;
	private Calendar date;
	private Theater theater;
	private Language language;
	
	/**
	 * Transportation mode the user can use to get to the show in time.
	 */
	private List<Path.TransportationMode> transportationModeList;
	
	public Screening(Movie movie, int hour, int minutes, Theater theater, Language language) {
		this.movie = movie;
		this.theater = theater;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.HOUR_OF_DAY, hour);
		this.date.set(Calendar.MINUTE, minutes);
		this.date.set(Calendar.SECOND, 0);
		this.setLanguage(language);
		this.transportationModeList = new ArrayList<>();
	}
	
	public Screening(Movie movie, Calendar date, Theater theater, Language language) {
		super();
		this.movie = movie;
		this.date = date;
		this.theater = theater;
		this.setLanguage(language);
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Theater getTheater() {
		return theater;
	}

	public void setTheater(Theater theater) {
		this.theater = theater;
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
	public List<Path.TransportationMode> getTransportationModeList() {
		return transportationModeList;
	}

	/**
	 * @param transportationModeList the transportationModeList to set
	 */
	public void setTransportationModeList(List<Path.TransportationMode> transportationModeList) {
		this.transportationModeList = transportationModeList;
	}

	@Override
	public String toString() {
		return "Screening [movie=" + movie.getName() + ", theater=" + theater.getName() + ", transportationModeList=" + transportationModeList + ", date=" + date.getTime() + "]";
	}
	
	
	

}
