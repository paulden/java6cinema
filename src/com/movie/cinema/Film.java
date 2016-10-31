package com.movie.cinema;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a movie.<br>
 * Contains the movie title, its duration, its rating, the {@link Cinema} in which it is shown, and a list of {@link Seance} in french and french-subtitled version.
 * @author Kévin
 *
 */
public class Film {

	/**
	 * Movie title
	 */
	private String name;

	/**
	 * Movie duration
	 */
	private String duree;

	/**
	 * Movie rating
	 */
	private String rated;

	/**
	 * A list of shows of this movie in french version
	 */
	private List<Seance> seanceListVF;

	/**
	 * A list of shows of this movie in french-subtitled version
	 */
	private List<Seance> seanceListVOSTFR;

	public Film(String name, String duration, String rated) {
		super();
		this.name = name;
		this.duree = duration;
		this.rated = rated;
		this.seanceListVF = new ArrayList<Seance>();
		this.seanceListVOSTFR = new ArrayList<Seance>();
	}
	
	public void addSeanceVF(Seance seance) {
		this.seanceListVF.add(seance);
	}
	
	public void addSeanceVOSTFR(Seance seance) {
		this.seanceListVOSTFR.add(seance);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duration) {
		this.duree = duration;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public List<Seance> getSeanceListVF() {
		return seanceListVF;
	}

	public void setSeanceListVF(List<Seance> seanceListVF) {
		this.seanceListVF = seanceListVF;
	}

	public List<Seance> getSeanceListVOSTFR() {
		return seanceListVOSTFR;
	}

	public void setSeanceListVOSTFR(List<Seance> seanceListVOSTFR) {
		this.seanceListVOSTFR = seanceListVOSTFR;
	}

	@Override
	public String toString() {
		return "Film [name=" + name + ", duree=" + duree + ", rated=" + rated + ", seanceListVF="
				+ seanceListVF + ", seanceListVOSTFR=" + seanceListVOSTFR + "]";
	}


	
	

}
