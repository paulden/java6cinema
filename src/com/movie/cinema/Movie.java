package com.movie.cinema;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a movie.<br>
 * Contains the movie title, its duration, its rating, the {@link Theater} in which it is shown, and a list of {@link Screening} in french and french-subtitled version.
 * @author Kévin
 *
 */
public class Movie {

	/**
	 * Movie title
	 */
	private String name;

	/**
	 * Movie duration
	 */
	private String duration;

	/**
	 * Movie rating
	 */
	private String rated;

	/**
	 * A list of shows of this movie in french version
	 */
	private List<Screening> screeningVFList;

	/**
	 * A list of shows of this movie in french-subtitled version
	 */
	private List<Screening> screeningVOSTFRList;

	public Movie(String name, String duration, String rated) {
		super();
		this.name = name;
		this.duration = duration;
		this.rated = rated;
		this.screeningVFList = new ArrayList<Screening>();
		this.screeningVOSTFRList = new ArrayList<Screening>();
	}
	
	public void addScreeningVF(Screening screening) {
		this.screeningVFList.add(screening);
	}
	
	public void addScreeningVOSTFR(Screening screening) {
		this.screeningVOSTFRList.add(screening);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public List<Screening> getScreeningVFList() {
		return screeningVFList;
	}

	public void setScreeningVFList(List<Screening> seanceListVF) {
		this.screeningVFList = seanceListVF;
	}

	public List<Screening> getScreeningVOSTFRList() {
		return screeningVOSTFRList;
	}

	public void setScreeningListVOSTFR(List<Screening> seanceListVOSTFR) {
		this.screeningVOSTFRList = seanceListVOSTFR;
	}

	@Override
	public String toString() {
		return "Movie [name=" + name + ", duration=" + duration + ", rated=" + rated + ", screeningVFList="
				+ screeningVFList + ", screeningVOSTFRList=" + screeningVOSTFRList + "]";
	}


	
	

}
